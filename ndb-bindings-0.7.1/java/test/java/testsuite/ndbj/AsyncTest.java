/* -*- mode: java; c-basic-offset: 4; indent-tabs-mode: nil; -*-
 *  vim:expandtab:shiftwidth=4:tabstop=4:smarttab:
 *
 *  ndb-bindings: Bindings for the NDB API
 *  Copyright (C) 2008 MySQL
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package testsuite.ndbj;

import testsuite.BaseNdbjTestCase;
import testsuite.TestCallback;
import com.mysql.cluster.ndbj.*;
import com.mysql.cluster.ndbj.NdbOperation.AbortOption;
import com.mysql.cluster.ndbj.NdbTransaction.ExecType;

import java.sql.SQLException;

public class AsyncTest extends BaseNdbjTestCase {

    /////////////////////////////////////////////////////////////////////
    // GENERAL STUFF
    /////////////////////////////////////////////////////////////////////

    private static final int NUM_REQUEST_TRANSACTIONS       = 2;
    private static final int REQUEST_TRANSACTIONS_TIMEOUT   = 1000;

    private static final int FORCE_SEND                     = 1;

    /////////////////////////////////////////////////////////////////////
    // TABLE METADATA
    /////////////////////////////////////////////////////////////////////

    static final String tablename      = "t_async";

    /////////////////////////////////////////////////////////////////////
    // INITIALISATION CODE
    /////////////////////////////////////////////////////////////////////

    public AsyncTest(String arg0) {
        super(arg0);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        // Create required tables
        this.createTable(tablename,
                         "    (ATTR1 INT UNSIGNED auto_increment not null," +
                         "     ATTR2 INT UNSIGNED NOT NULL," +
                         "     PRIMARY KEY (ATTR1) )" +
                         "  ENGINE=NDBCLUSTER");
    }

    /////////////////////////////////////////////////////////////////////
    // TEST CODE
    /////////////////////////////////////////////////////////////////////

    /**
     * Basic test of single async transaction.
     */
    public void testSingleTx_Success() throws SQLException {
        trans = ndb.startTransaction();

        // insert a single row - (1, 2)
        NdbOperation orderOp1 = trans.getInsertOperation(tablename);
        orderOp1.equalInt("ATTR1", 1);
        orderOp1.setInt("ATTR2", 2);

        TestCallback callback = new TestCallback(100, ndb, trans,
                                                 orderOp1.resultData());

        // execute transaction and wait for completion
        trans.executeAsynchPrepare(ExecType.Commit, callback,
                                   AbortOption.AbortOnError);

        ndb.sendPollNdb(REQUEST_TRANSACTIONS_TIMEOUT,
                        NUM_REQUEST_TRANSACTIONS, FORCE_SEND);

        // check success and inserted values
        assertEquals(0, callback.result);

        Long a1 = (Long)getSingleIndexedValueWithQuery(conn, 1,
                                                       "select ATTR1 from " + tablename);
        Long a2 = (Long)getSingleIndexedValueWithQuery(conn, 1,
                                                       "select ATTR2 from " + tablename);
        assertEquals(1, a1.intValue());
        assertEquals(2, a2.intValue());
    }

    /**
     * Test two async transactions that will conflict with a duplicate key
     * error.
     */
    public void testMultiTx_Failure() throws SQLException {
        // insert a single row - (1, 2)
        trans = ndb.startTransaction();
        NdbOperation orderOp1 = trans.getInsertOperation(tablename);
        orderOp1.equalInt("ATTR1", 1);
        orderOp1.setInt("ATTR2", 2);

        TestCallback callback = new TestCallback(1, ndb, trans,
                                                 orderOp1.resultData());
        callback.throwOnError = false;

        trans.executeAsynchPrepare(ExecType.Commit, callback,
                                   AbortOption.AbortOnError);

        // insert another row - (1, 3)
        trans = ndb.startTransaction();
        NdbOperation orderOp2 = trans.getInsertOperation(tablename);
        orderOp2.equalInt("ATTR1", 1);
        orderOp2.setInt("ATTR2", 3);

        TestCallback callback2 = new TestCallback(2, ndb, trans,
                                                  orderOp2.resultData());
        callback2.throwOnError = false;

        trans.executeAsynchPrepare(ExecType.Commit, callback2,
                                   AbortOption.AbortOnError);

        // wait for both async transactions
        ndb.sendPollNdb(REQUEST_TRANSACTIONS_TIMEOUT,
                        NUM_REQUEST_TRANSACTIONS, FORCE_SEND);

        // one of the callbacks should have failed
        NdbError error;
        if(callback2.error == null)
        {
            error = callback.error;
            assertEquals(0, callback2.result);
            assertEquals(-1, callback.result);
        }
        else
        {
            assertNull("Only one operation should fail", callback.error);
            error = callback2.error;
            assertEquals(-1, callback2.result);
            assertEquals(0, callback.result);
        }
        assertNotNull("Error must be raised", error);
        // duplicate tuple error
        assertEquals(630, error.getCode());

        // check inserted row
        Long a1 = (Long)getSingleIndexedValueWithQuery(conn, 1,
                                                       "select ATTR1 from " + tablename);
        Long a2 = (Long)getSingleIndexedValueWithQuery(conn, 1,
                                                       "select ATTR2 from " + tablename);
        assertEquals(1, a1.intValue());
        // ATTR2 should be 2 or 3
        assertTrue(2 == a2.intValue() || 3 == a2.intValue());
    }

    public void testOperation() throws NdbApiException {
        final int ROWS = 25;

        for (int loop = 0; loop < 100; loop++) {
            // Process request transaction

            NdbOperation orderOp;
            BaseCallback callback;

            for (int elementCount = 0; elementCount < ROWS; elementCount++) {
                int id = (loop * 100) + elementCount;

                trans = ndb.startTransaction();
                orderOp = trans.getInsertOperation(tablename);
                orderOp.equalInt("ATTR1", id);
                orderOp.setInt("ATTR2", id + 1);

                callback = new TestCallback(tablename, id, ndb, trans,
                                            orderOp.resultData());
                trans.executeAsynchPrepare(ExecType.Commit, callback,
                                           AbortOption.AbortOnError);
            }

            ndb.sendPollNdb(REQUEST_TRANSACTIONS_TIMEOUT,
                            NUM_REQUEST_TRANSACTIONS, FORCE_SEND);
        }

        ndb.sendPollNdb(100000, 100000, FORCE_SEND);
    }
}
