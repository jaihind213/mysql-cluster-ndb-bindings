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

public class QuickBFTest extends BaseNdbjTestCase {

    /////////////////////////////////////////////////////////////////////
    // GENERAL STUFF
    /////////////////////////////////////////////////////////////////////

    static final int MAXIMUM_OPERATION_VOLUME       = 300; // TO DO: Calculate this

    static final int NUMBER_OF_ELEMENT_INSERTIONS   = 25;

    static final int NUM_REQUEST_TRANSACTIONS       = 5;

    static final int REQUEST_TRANSACTIONS_TIMEOUT   = 1;

    static final int FORCE_SEND                     = 1;

    static final boolean USE_SYNCHRONOUS            = false;

    /////////////////////////////////////////////////////////////////////
    // TABLE METADATA
    /////////////////////////////////////////////////////////////////////

    static String ORDER      = "accountspike_order";
    static String EXECUTION  = "accountspike_execution";
    static String TRADE      = "accountspike_trade";
    static String SETTLEMENT = "accountspike_settlement";
    static String POSITION   = "accountspike_position";

    /* column ids */
    int ORDER_PARTID;
    int ORDER_ORDERID;
    int ORDER_ACCOUNTID;
    int ORDER_REQUESTID;
    int ORDER_INSTRUMENTID;
    int ORDER_ISBUY;
    int ORDER_QUANTITY;
    int ORDER_PRICE;

    int EXECUTION_PARTID;
    int EXECUTION_EXECUTIONID;
    int EXECUTION_ACCOUNTID;
    int EXECUTION_ORDERID;

    int TRADE_PARTID;
    int TRADE_TRADEID;
    int TRADE_ACCOUNTID;
    int TRADE_ORDERID;
    int TRADE_PRICE;
    int TRADE_QUANTITY;

    int SETTLEMENT_PARTID;
    int SETTLEMENT_SETTLEMENTID;
    int SETTLEMENT_ACCOUNTID;
    int SETTLEMENT_TRADEID1;
    int SETTLEMENT_TRADEID2;
    int SETTLEMENT_QUANTITY;
    int SETTLEMENT_VALUE;

    int POSITION_PARTID;
    int POSITION_ACCOUNTID;
    int POSITION_INSTRUMENTID;
    int POSITION_UMBUY_QUANTITY;
    int POSITION_UMSELL_QUANTITY;
    int POSITION_MATCHED_QUANTITY;
    int POSITION_UMBUY_PRODUCT;
    int POSITION_UMSELL_PRODUCT;
    int POSITION_MATCHED_PRODUCT;

    /////////////////////////////////////////////////////////////////////
    // OTHER
    /////////////////////////////////////////////////////////////////////

    int myTransactionCount      = 0;
    int myTotalTransactionCount = 0;

    ArrayList<NdbTransaction> myCompletedTransactionList = new ArrayList<NdbTransaction>();
    ArrayList<NdbTransaction> myTransactionList = new ArrayList<NdbTransaction>();

    /////////////////////////////////////////////////////////////////////
    // INITIALISATION CODE
    /////////////////////////////////////////////////////////////////////

    public QuickBFTest(String arg0) {
        super(arg0);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        // Create required tables

        createTable(ORDER, "("                                       +
                    "`partid`       SMALLINT UNSIGNED NOT NULL," +
                    "`orderid`      INT      UNSIGNED NOT NULL," +
                    "`accountid`    INT      UNSIGNED NOT NULL," +
                    "`requestid`    INT      UNSIGNED NOT NULL," +
                    "`instrumentid` INT      UNSIGNED NOT NULL," +
                    "`isbuy`        SMALLINT          NOT NULL," +
                    "`quantity`     INT      UNSIGNED NOT NULL," +
                    "`price`        INT               NOT NULL," +
                    "PRIMARY KEY(partid, orderid)"               +
                    ") ENGINE=ndb PARTITION BY KEY (partid);");

        createTable("accountspike_execution", "("                   +
                    "`partid`      SMALLINT UNSIGNED NOT NULL," +
                    "`executionid` INT      UNSIGNED NOT NULL," +
                    "`accountid`   INT      UNSIGNED NOT NULL," +
                    "`orderid`     INT      UNSIGNED NOT NULL," +
                    "PRIMARY KEY(partid, executionid)"          +
                    ") ENGINE=ndb PARTITION BY KEY (partid);");

        createTable("accountspike_trade", "("                     +
                    "`partid`    SMALLINT UNSIGNED NOT NULL," +
                    "`tradeid`   INT      UNSIGNED NOT NULL," +
                    "`accountid` INT      UNSIGNED NOT NULL," +
                    "`orderid`   INT      UNSIGNED NOT NULL," +
                    "`price`     INT      UNSIGNED NOT NULL," +
                    "`quantity`  INT               NOT NULL," +
                    "PRIMARY KEY(partid, tradeid)"            +
                    ") ENGINE=ndb PARTITION BY KEY (partid);");

        createTable("accountspike_settlement", "("                   +
                    "`partid`       SMALLINT UNSIGNED NOT NULL," +
                    "`settlementid` INT      UNSIGNED NOT NULL," +
                    "`accountid`    INT      UNSIGNED NOT NULL," +
                    "`tradeid1`     INT      UNSIGNED NOT NULL," +
                    "`tradeid2`     INT      UNSIGNED NOT NULL," +
                    "`quantity`     INT               NOT NULL," +
                    "`value`        INT               NOT NULL," +
                    "PRIMARY KEY(partid, settlementid)"          +
                    ") ENGINE=ndb PARTITION BY KEY (partid);");

        createTable("accountspike_position", "("                         +
                    "`partid`           SMALLINT UNSIGNED NOT NULL," +
                    "`accountid`        INT      UNSIGNED NOT NULL," +
                    "`instrumentid`     INT      UNSIGNED NOT NULL," +
                    "`umbuy_quantity`   INT               NOT NULL," +
                    "`umsell_quantity`  INT               NOT NULL," +
                    "`matched_quantity` INT               NOT NULL," +
                    "`umbuy_product`    INT               NOT NULL," +
                    "`umsell_product`   INT               NOT NULL," +
                    "`matched_product`  INT               NOT NULL," +
                    "PRIMARY KEY(partid, accountid, instrumentid)"   +
                    ") ENGINE=ndb PARTITION BY KEY (partid);");

        NdbDictionary dictionary = ndb.getDictionary();

        NdbTable  orderTable = dictionary.getTable(ORDER);
        ORDER_PARTID         = orderTable.getColumn("partid").getColumnNo();
        ORDER_ORDERID        = orderTable.getColumn("orderid").getColumnNo();
        ORDER_ACCOUNTID      = orderTable.getColumn("accountid").getColumnNo();
        ORDER_REQUESTID      = orderTable.getColumn("requestid").getColumnNo();
        ORDER_INSTRUMENTID   = orderTable.getColumn("instrumentid").getColumnNo();
        ORDER_ISBUY          = orderTable.getColumn("isbuy").getColumnNo();
        ORDER_QUANTITY       = orderTable.getColumn("quantity").getColumnNo();
        ORDER_PRICE          = orderTable.getColumn("price").getColumnNo();

        NdbTable  execTable   = dictionary.getTable(EXECUTION);
        EXECUTION_PARTID      = execTable.getColumn("partid").getColumnNo();
        EXECUTION_EXECUTIONID = execTable.getColumn("executionid").getColumnNo();
        EXECUTION_ACCOUNTID   = execTable.getColumn("accountid").getColumnNo();
        EXECUTION_ORDERID     = execTable.getColumn("orderid").getColumnNo();

        NdbTable  tradeTable = dictionary.getTable(TRADE);
        TRADE_PARTID         = tradeTable.getColumn("partid").getColumnNo();
        TRADE_TRADEID        = tradeTable.getColumn("tradeid").getColumnNo();
        TRADE_ACCOUNTID      = tradeTable.getColumn("accountid").getColumnNo();
        TRADE_ORDERID        = tradeTable.getColumn("orderid").getColumnNo();
        TRADE_PRICE          = tradeTable.getColumn("price").getColumnNo();
        TRADE_QUANTITY       = tradeTable.getColumn("quantity").getColumnNo();

        NdbTable  settTable = dictionary.getTable(SETTLEMENT);
        SETTLEMENT_PARTID       = settTable.getColumn("partid").getColumnNo();
        SETTLEMENT_SETTLEMENTID = settTable.getColumn("settlementid").getColumnNo();
        SETTLEMENT_ACCOUNTID    = settTable.getColumn("accountid").getColumnNo();
        SETTLEMENT_TRADEID1     = settTable.getColumn("tradeid1").getColumnNo();
        SETTLEMENT_TRADEID2     = settTable.getColumn("tradeid2").getColumnNo();
        SETTLEMENT_QUANTITY     = settTable.getColumn("quantity").getColumnNo();
        SETTLEMENT_VALUE        = settTable.getColumn("value").getColumnNo();

        NdbTable  positTable      = dictionary.getTable(POSITION);
        POSITION_PARTID           = positTable.getColumn("partid").getColumnNo();
        POSITION_ACCOUNTID        = positTable.getColumn("accountid").getColumnNo();
        POSITION_INSTRUMENTID     = positTable.getColumn("instrumentid").getColumnNo();
        POSITION_UMBUY_QUANTITY   = positTable.getColumn("umbuy_quantity").getColumnNo();
        POSITION_UMSELL_QUANTITY  = positTable.getColumn("umsell_quantity").getColumnNo();
        POSITION_MATCHED_QUANTITY = positTable.getColumn("matched_quantity").getColumnNo();
        POSITION_UMBUY_PRODUCT    = positTable.getColumn("umbuy_product").getColumnNo();
        POSITION_UMSELL_PRODUCT   = positTable.getColumn("umsell_product").getColumnNo();
        POSITION_MATCHED_PRODUCT  = positTable.getColumn("matched_product").getColumnNo();

        // Preload an appropriate volume of operations

        trans = ndb.startTransaction();                // TO DO: Need to do this for each data node
        //
        for (long preloadVolume = 0;                   //
             preloadVolume < MAXIMUM_OPERATION_VOLUME; //
             preloadVolume++ ) {                       //
            trans.getInsertOperation(ORDER);          //
        }                                              //
        //
        trans.close();                                 //
    }

    /////////////////////////////////////////////////////////////////////
    // TEST CODE
    /////////////////////////////////////////////////////////////////////

    public void testOperation() throws NdbApiException {
        for (int loop = 0; loop < 100; loop++) {
            // Process request transaction

            NdbOperation orderOp;
            BaseCallback callback;

            for (int elementCount = 1; elementCount < NUMBER_OF_ELEMENT_INSERTIONS + 1; elementCount++) {
                int id = (NUMBER_OF_ELEMENT_INSERTIONS * loop) + elementCount;

                trans = ndb.startTransaction();
                myTransactionList.add(trans);

                orderOp = trans.getInsertOperation(ORDER);

                orderOp.equalInt(ORDER_PARTID, id);
                orderOp.equalInt(ORDER_ORDERID, id);

                orderOp.setInt(ORDER_ACCOUNTID, id);
                orderOp.setInt(ORDER_REQUESTID, id);
                orderOp.setInt(ORDER_INSTRUMENTID, id);
                orderOp.setInt(ORDER_ISBUY, id);
                orderOp.setInt(ORDER_QUANTITY, id);
                orderOp.setInt(ORDER_PRICE, id);

                if (!USE_SYNCHRONOUS) {
                    callback = new CountingCallback(id, ndb, trans, orderOp.resultData());

                    trans.executeAsynchPrepare(NdbTransaction.ExecType.Commit, callback, 
                    		NdbOperation.AbortOption.AbortOnError);
                }
            }

            if (USE_SYNCHRONOUS) {
                trans.execute(NdbTransaction.ExecType.Commit, NdbOperation.AbortOption.AbortOnError, true);
            }
            else {
                System.out.println("Performing first sendPollNdb. ");
                ndb.sendPollNdb(REQUEST_TRANSACTIONS_TIMEOUT, NUM_REQUEST_TRANSACTIONS, FORCE_SEND);
                System.out.println("Completed first sendPollNdb.");

                clearTransactions();
            }

            // Process response transaction

            trans = ndb.startTransaction();

            NdbOperation executionOp;
            NdbOperation tradeOp;
            NdbOperation settlementOp;

            for (int elementCount = 1; elementCount < NUMBER_OF_ELEMENT_INSERTIONS + 1; elementCount++) {
                int id = (NUMBER_OF_ELEMENT_INSERTIONS * loop) + elementCount;

                trans = ndb.startTransaction();
                myTransactionList.add(trans);

                executionOp = trans.getInsertOperation(EXECUTION);

                executionOp.equalInt(EXECUTION_PARTID, id);
                executionOp.equalInt(EXECUTION_EXECUTIONID, id);

                executionOp.setInt(EXECUTION_ACCOUNTID, id);
                executionOp.setInt(EXECUTION_ORDERID, id);

                if (!USE_SYNCHRONOUS) {
                    callback = new CountingCallback(id, ndb, trans, executionOp.resultData());

                    trans.executeAsynchPrepare(NdbTransaction.ExecType.Commit, callback, 
                    		NdbOperation.AbortOption.AbortOnError);
                }

                trans = ndb.startTransaction();
                myTransactionList.add(trans);

                tradeOp = trans.getInsertOperation(TRADE);

                tradeOp.equalInt(TRADE_PARTID, id);
                tradeOp.equalInt(TRADE_TRADEID, id);

                tradeOp.setInt(TRADE_ACCOUNTID, id);
                tradeOp.setInt(TRADE_ORDERID, id);
                tradeOp.setInt(TRADE_PRICE, id);
                tradeOp.setInt(TRADE_QUANTITY, id);

                if (!USE_SYNCHRONOUS) {
                    callback = new CountingCallback(id, ndb, trans, executionOp.resultData());

                    trans.executeAsynchPrepare(NdbTransaction.ExecType.Commit, callback, 
                    		NdbOperation.AbortOption.AbortOnError);
                }

                trans = ndb.startTransaction();
                myTransactionList.add(trans);

                settlementOp = trans.getInsertOperation(SETTLEMENT);

                settlementOp.equalInt(SETTLEMENT_PARTID, id);
                settlementOp.equalInt(SETTLEMENT_SETTLEMENTID, id);

                settlementOp.setInt(SETTLEMENT_ACCOUNTID, id);
                settlementOp.setInt(SETTLEMENT_TRADEID1, id);
                settlementOp.setInt(SETTLEMENT_TRADEID2, id);
                settlementOp.setInt(SETTLEMENT_QUANTITY, id);
                settlementOp.setInt(SETTLEMENT_VALUE, id);

                if (!USE_SYNCHRONOUS) {
                    callback = new CountingCallback(id, ndb, trans, executionOp.resultData());

                    trans.executeAsynchPrepare(NdbTransaction.ExecType.Commit, callback, 
                    		NdbOperation.AbortOption.AbortOnError);
                }
            }

            if (USE_SYNCHRONOUS) {
                trans.execute(NdbTransaction.ExecType.Commit, NdbOperation.AbortOption.AbortOnError, true);
            }
            else {
                System.out.println("Performing second sendPollNdb. ");
                ndb.sendPollNdb(REQUEST_TRANSACTIONS_TIMEOUT, NUM_REQUEST_TRANSACTIONS, FORCE_SEND);
                System.out.println("Completed second sendPollNdb.");

                clearTransactions();
            }
        }
        System.out.println("Performing cooldown");

        ndb.sendPollNdb(100000, 100000, FORCE_SEND);
    }

    private synchronized void clearTransactions() {
        System.out.print("Clearing complete transactions");

        Iterator<NdbTransaction> tranIter = myCompletedTransactionList.iterator();

        while (tranIter.hasNext()) {
            System.out.print(".");
            NdbTransaction completeTransaction = tranIter.next();
            completeTransaction.close();
        }

        myCompletedTransactionList.clear();

        System.out.println(" done.");
    }

    class CountingCallback extends BaseCallback {
        int          myCallbackNum;
        Ndb          myNdb;
        NdbResultSet myResults;

        public CountingCallback(int theCallbackNum, Ndb theNdb, NdbTransaction theTrans, NdbResultSet theResults) {
            super(theTrans);
            myTransactionCount++;
            myTotalTransactionCount++;

            myCallbackNum = theCallbackNum;
            myNdb         = theNdb;
            myResults     = theResults;

            System.out.println("Creating " + this.getClass().getName() + " " + myCallbackNum +
                               ": Current transaction volume " + myTransactionCount +
                               " (" + myTotalTransactionCount + ")");
        }

        @Override
        public void callback(int result) {
            myTransactionCount--;
            System.out.println("Finished " + this.getClass().getName() + " " + myCallbackNum +
                               ": Current transaction volume " + myTransactionCount +
                               " (" + myTotalTransactionCount + ")");

            // myTrans.close();
            myCompletedTransactionList.add(trans);
        }
    }
}
