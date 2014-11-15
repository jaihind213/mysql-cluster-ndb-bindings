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
import java.sql.SQLException;

public class MultipleAsyncTest extends BaseNdbjTestCase {

    /////////////////////////////////////////////////////////////////
    // GENERAL STUFF
    /////////////////////////////////////////////////////////////////

    private static final int REQUEST_TRANSACTIONS_TIMEOUT   = 1000;
    private static final int FORCE_SEND                     = 1;

    /////////////////////////////////////////////////////////////////
    // TABLE METADATA
    /////////////////////////////////////////////////////////////////

    private static final String tablename1 = "t_multi_async1";
    private static final String tablename2 = "t_multi_async2";
    private static final String tablename3 = "t_multi_async3";

    /////////////////////////////////////////////////////////////////
    // INITIALISATION CODE
    /////////////////////////////////////////////////////////////////

    public MultipleAsyncTest(String arg0) {
        super(arg0);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        // Create required tables
        createTable(tablename1,
                    "    (ATTR1 INT UNSIGNED auto_increment not null," +
                    "     ATTR2 INT UNSIGNED NOT NULL," +
                    "     PRIMARY KEY (ATTR1) )" +
                    "  ENGINE=NDBCLUSTER");
        createTable(tablename2,
                    "    (ATTR1 INT UNSIGNED auto_increment not null," +
                    "     ATTR2 INT UNSIGNED NOT NULL," +
                    "     PRIMARY KEY (ATTR1) )" +
                    "  ENGINE=NDBCLUSTER");
        createTable(tablename3,
                    "    (ATTR1 INT UNSIGNED auto_increment not null," +
                    "     ATTR2 INT UNSIGNED NOT NULL," +
                    "     PRIMARY KEY (ATTR1) )" +
                    "  ENGINE=NDBCLUSTER");
    }

    //////////////////////////////////////////////////////////////////
    // TEST CODE
    //////////////////////////////////////////////////////////////////

    /**
     * Helper method to create an async transaction.
     */
    private void sendTransaction(String table, int id) throws SQLException {
        NdbTransaction trans = ndb.startTransaction();

        NdbOperation op = trans.getInsertOperation(table);

        op.equalInt("ATTR1", id);
        op.setInt("ATTR2", id);

        BaseCallback callback = new TestCallback(table, id, ndb,
                                                 trans, op.resultData());

        trans.executeAsynchPrepare(NdbTransaction.ExecType.Commit, callback,
                                   NdbOperation.AbortOption.AbortOnError);
    }

    /**
     * Test to send many async transactions.
     */
    public void testMultipleOperation() throws SQLException {
        final int rows = 25;

        // send a large amount of async inserts into three tables
        for (int loop = 0; loop < 100; loop++) {
            for (int elementCount = 0; elementCount < rows; elementCount++) {
                int id = (rows * loop) + elementCount;

                // send one transaction for each table
                sendTransaction(tablename1, 10000 + id);
                sendTransaction(tablename2, 20000 + id);
                sendTransaction(tablename3, 30000 + id);
            }

            ndb.sendPollNdb(REQUEST_TRANSACTIONS_TIMEOUT, /* NUM_REQUEST_TRANSACTIONS */ 2, FORCE_SEND);
        }

        ndb.sendPollNdb(100000, 100000, FORCE_SEND);

        // check table counts
        int tableRows = 100 * rows;

        assertEquals("table 1 row count", tableRows, getRowCount(tablename1));
        assertEquals("table 2 row count", tableRows, getRowCount(tablename2));
        assertEquals("table 3 row count", tableRows, getRowCount(tablename3));
        assertEquals("table 1 row count w/range check", new Long(tableRows),
                     (Long)getSingleValueWithQuery("select count(*) from " +
                                                   tablename1 + " where attr1 between 10000 and 13000"));
        assertEquals("table 2 row count w/range check", new Long(tableRows),
                     (Long)getSingleValueWithQuery("select count(*) from " +
                                                   tablename2 + " where attr1 between 20000 and 23000"));
        assertEquals("table 3 row count w/range check", new Long(tableRows),
                     (Long)getSingleValueWithQuery("select count(*) from " +
                                                   tablename3 + " where attr1 between 30000 and 33000"));
    }
}
