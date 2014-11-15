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

import java.util.*;

import testsuite.BaseNdbjTestCase;
import com.mysql.cluster.ndbj.*;
import com.mysql.cluster.ndbj.NdbOperation.AbortOption;
import com.mysql.cluster.ndbj.NdbTransaction.ExecType;

public class ImmediatePollTimeoutTest extends BaseNdbjTestCase {
    int ourCallbackCount;

    /////////////////////////////////////////////////////////////////////
    // GENERAL STUFF
    /////////////////////////////////////////////////////////////////////

    static int REQUEST_TRANSACTIONS_TIMEOUT   = 0;
    static int FORCE_SEND                     = 1;

    /////////////////////////////////////////////////////////////////////
    // TABLE METADATA
    /////////////////////////////////////////////////////////////////////

    private static String tablename      = "t_immediatepolltimeouttest";

    /////////////////////////////////////////////////////////////////////
    // INITIALISATION CODE
    /////////////////////////////////////////////////////////////////////

    public ImmediatePollTimeoutTest(String arg0) {
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
     * Test multiple batches of async operations, with no-wait poll.
     */
    public void testOperation() throws NdbApiException {
        HashMap<Integer,BaseCallback> hash = new HashMap<Integer,BaseCallback>();
        int ourOutstandingTransactionCount = 0;

        for (int loop = 0; loop < 100; loop++) {
            for (int elementCount = 0; elementCount < 50; elementCount++) {
                NdbOperation orderOp;
                BaseCallback callback;
                int id = (50 * loop) + elementCount;

                trans = ndb.startTransaction();
                orderOp = trans.getInsertOperation(tablename);
                orderOp.equalInt("ATTR1", id);
                orderOp.setInt("ATTR2", id);

                callback = new HashCallback(hash, id, ndb, trans, orderOp.resultData());
                hash.put(callback.hashCode(), callback);

                trans.executeAsynchPrepare(ExecType.Commit,
                                           callback, AbortOption.AbortOnError);

                ourOutstandingTransactionCount++;
            }

            ourCallbackCount = 0;

            // send all pending transactions - ourCallbackCount and hash will
            // be updated by the callbacks
            ndb.sendPreparedTransactions(FORCE_SEND);
            ndb.pollNdb(REQUEST_TRANSACTIONS_TIMEOUT);

            ourOutstandingTransactionCount = ourOutstandingTransactionCount - ourCallbackCount;
        }

        ourCallbackCount = 0;

        ndb.pollNdb(100000, 100000);

        // all transactions should be completed
        ourOutstandingTransactionCount = ourOutstandingTransactionCount - ourCallbackCount;
        assertEquals(0, ourOutstandingTransactionCount);
        assertEquals(0, hash.size());
    }

    /**
     * Test callback. Increments the global ourCallbackCount, and removes
     * itself from the hashtable when callback is sent.
     */
    class HashCallback extends BaseCallback {
        int                            myCallbackNum;
        Ndb                            myNdb;
        NdbResultSet                   myResults;
        HashMap<Integer, BaseCallback> myMap;

        public HashCallback(HashMap<Integer, BaseCallback> theMap,
                            int theCallbackNum, Ndb theNdb, NdbTransaction theTrans,
                            NdbResultSet theResults) {
            super(theTrans);
            myMap         = theMap;
            myCallbackNum = theCallbackNum;
            myNdb         = theNdb;
            myResults     = theResults;
        }

        @Override
        public void callback(int res) {
            theTrans.close();
            myMap.remove(this.hashCode());

            ourCallbackCount++;
        }
    }
}
