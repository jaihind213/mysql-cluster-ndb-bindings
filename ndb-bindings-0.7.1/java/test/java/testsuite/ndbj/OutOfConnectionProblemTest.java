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
import com.mysql.cluster.ndbj.*;

public class OutOfConnectionProblemTest extends BaseNdbjTestCase {

    static boolean alreadySetUp = false;

    /////////////////////////////////////////////////////////////////////
    // GENERAL STUFF
    /////////////////////////////////////////////////////////////////////

    static final int     NUMBER_OF_TEST_ITERATIONS    = 1000000;

    static final int     NUMBER_OF_ELEMENT_INSERTIONS = 25;

    static final int     NUM_TRANSACTIONS             = 500;
    static final int     TRANSACTIONS_TIMEOUT         = 10;
    static final int     FORCE_SEND                   = 1;

    /////////////////////////////////////////////////////////////////////
    // TABLE METADATA
    /////////////////////////////////////////////////////////////////////

    static final String PROBTABLE  = "OutOfConnectionProblemTable";

    static int PROBTABLE_FIELD1;

    /////////////////////////////////////////////////////////////////////
    // OTHER
    /////////////////////////////////////////////////////////////////////

    static int myTransactionCount      = 0;
    static int myTotalTransactionCount = 0;

    /////////////////////////////////////////////////////////////////////
    // INITIALISATION CODE
    /////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {
        junit.textui.TestRunner.run(OutOfConnectionProblemTest.class);
    }

    public OutOfConnectionProblemTest(String arg0) {
        super(arg0);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        if (!alreadySetUp) {
            // Create required tables

            createTable(PROBTABLE, "(field1 INT UNSIGNED NOT NULL, a integer, b integer, c integer, d integer, e int,f integer, g integer, PRIMARY KEY(field1)) ENGINE=ndb PARTITION BY KEY (field1);");

            NdbDictionary dictionary = ndb.getDictionary();

            NdbTable  probTable = dictionary.getTable(PROBTABLE);
            PROBTABLE_FIELD1    = probTable.getColumn("field1").getColumnNo();
        }
    }

    /////////////////////////////////////////////////////////////////////
    // TEST CODE
    /////////////////////////////////////////////////////////////////////

    public void testOperation()  throws NdbApiException {

        try {
            long startTime = System.currentTimeMillis();

            for (int iteration = 0;
                 iteration < NUMBER_OF_TEST_ITERATIONS;
                 iteration++) {
                generateRequestTxs(iteration);

                sendPollNdb(TRANSACTIONS_TIMEOUT, NUM_TRANSACTIONS, FORCE_SEND);
                System.gc(); System.runFinalization();
            }

            System.out.println("Performing cooldown");

            pollNdb(100000, myTransactionCount);

            long endTime     = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;

            System.out.println("Test completed with no exceptions and took " + elapsedTime / 1000 + " seconds");
        }
        finally {
            System.out.println("End of test.");
        }
    }
    static int cnt=0;
    public void generateRequestTxs(int theLastBatch) throws NdbApiException {

        boolean  success = false;

        NdbOperation orderOp;
        BaseCallback callback;

        try {

            for (int elementCount = 1;
                 elementCount < (NUMBER_OF_ELEMENT_INSERTIONS) + 1;
                 elementCount++) {

                cnt++;
                trans = startCountedTransaction();

                orderOp = trans.getInsertOperation(PROBTABLE);

                orderOp.equalInt(PROBTABLE_FIELD1, cnt);
                orderOp.setInt("a", cnt);
                orderOp.setInt("b", cnt);
                orderOp.setInt("c", cnt);
                orderOp.setInt("d", cnt);
                orderOp.setInt("e", cnt);
                orderOp.setInt("f", cnt);
                orderOp.setInt("g", cnt);

                callback = new CountingCallback(cnt, ndb, orderOp.resultData(), trans);

                trans.executeAsynchPrepare(NdbTransaction.ExecType.Commit,
                                           callback,
                                           NdbOperation.AbortOption.AbortOnError);
            }


            success = true;
        }
        finally {
            System.out.println("Started " + myTotalTransactionCount + " txs in total " + (success ? "(no errors)" : "(errors, aborting)"));
        }
    }

    private NdbTransaction startCountedTransaction() throws NdbApiException {
        myTransactionCount++;
        myTotalTransactionCount++;

        return ndb.startTransaction();
    }

    private int sendPollNdb(int aMillisecondNumber, int minNoOfEventsToWakeup, int forceSend) throws NdbApiException {
        ndb.sendPreparedTransactions(forceSend);

        return pollNdb(aMillisecondNumber, minNoOfEventsToWakeup);
    }

    private int pollNdb(int aMillisecondNumber, int minNoOfEventsToWakeup) throws NdbApiException {
        int numberOfCallbacks = ndb.pollNdb(aMillisecondNumber, minNoOfEventsToWakeup);

        System.out.println("Poll produced " + numberOfCallbacks + " callbacks (" + myTransactionCount +
                           " txs outstanding) [Total " + myTotalTransactionCount + "]");

        return numberOfCallbacks;
    }

    class CountingCallback extends BaseCallback {
        NdbTransaction t;
        public CountingCallback(int theCallbackNum, Ndb theNdb, NdbResultSet theResults, NdbTransaction trans) {
            super(trans);
            t=trans;
        }

        @Override
        public void callback(int theResult) {
            myTransactionCount--;
            //System.out.print("Callback (" + theTrans + "): ");

            //System.out.print("Transaction was " + (theTrans.isClosed() ? "closed" : "open") + " (before close()) " );
            //theTrans.close();
            t.close();

            //System.out.println("and is now " + (theTrans.isClosed() ? "closed" : "open") );
        }

    }

}
