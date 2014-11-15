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

package com.mysql.cluster.ndbj;

/**
 * The NdbScanOperation interface defines common methods provided by
 * both NdbScanOperation and NdbIndexScanOperation objects.
 * <p>A scan operation reads many rows in a table; whereas a primary key
 * operation (provided by NdbOperation objects) read a single row in a table.
 * <p>Scans are executed in parallel over the nodes in a cluster,
 * and is therefore an expensive operation to perform. Use sparingly.
 * <br>Call the close method on the scan object to finish a scan when it is
 * no longer needed.
 *
 *
 * @see com.mysql.cluster.ndbj.NdbScanOperation
 * @see com.mysql.cluster.ndbj.NdbOperation
 * @see com.mysql.cluster.ndbj.NdbTransaction
 * @see com.mysql.cluster.ndbj.Ndb
 * @see com.mysql.cluster.ndbj.NdbClusterConnection
 */

public interface NdbScanOperation extends NdbOperation {

    /**
     *
     * A ScanFlag can be supplied to a readTuples operation to indicate
     * how the data should be ordered before being returned to the client.
     *
     */
    public enum ScanFlag {
        /**
         * SF_TupScan is 0x10000
         * <p>
         * In C++ NDB-API, these Scan Types are defined in NdbScanOperation.hpp
         */
        TUPLE_SCAN(1 << 16),
        /**
         * SF_OrderBy is 0x1000000
         */
        ORDER_BY(1 << 24),
        /**
         * SF_Descending is 0x2000000
         */
        DESCENDING(2 << 24),
        /**
         * SF_ReadRangeNo is 0x4000000
         */
        READ_RANGE_NUM(4 << 24),
        /**
         * TODO: SF_KeyInfo indicate that index information is ??.
         */
        KEY_INFO(1),
         /**
         * No flag is passed by the client.
         */
        NO_FLAG(0);

        public int type;
        private ScanFlag(int type) {
            this.type = type;
        }
        public int swigValue() {
            return this.type;
        }

    }

    /**
     * Get the next tuple in a scan transaction.

     * This method is used by the NdbResultSet to fetch the next tuple in
     * a scan transaction. Following each call to nextResult(), the
     * NdbResultSet objects populated using NdbOperation.getValue() are updated
     * with values from the scanned tuple.
     *
     * The NDB API will receive tuples from each fragment in batches, and
     * needs to explicitly request from the NDB Kernel the sending of each new
     * batch. When a new batch is requested, the NDB Kernel will remove any
     * locks taken on rows in the previous batch, unless they have been already
     * taken over by the application executing updateCurrentTuple(),
     * lockCurrentTuple(), etc.
     *
     * The fetchAllowed parameter is used to control this release of
     * locks from the application. When fetchAllowed is set to false,
     * the NDB API will not request new batches from the NDB Kernel when
     * all received rows have been exhausted, but will instead return 2
     * from nextResult(), indicating that new batches must be
     * requested. You must then call nextResult with fetchAllowed = true
     * in order to contact the NDB Kernel for more records, after taking over
     * locks as appropriate.
     *
     * fetchAllowed = false is useful when you want to update or
     * delete all the records fetched in one transaction(This will save a
     * lot of round trip time and make updates or deletes of scanned
     * records a lot faster).
     *
     * While nextResult(false) returns 0, take over the record to
     * another transaction. When nextResult(false) returns 2 you must
     * execute and commit the other transaction. This will cause the
     * locks to be transferred to the other transaction, updates or
     * deletes will be made and then the locks will be released.
     * After that, call nextResult(true) which will fetch new records and
     * cache them in the NdbApi.
     *
     * NOTE:  If you don't take over the records to another transaction the
     *        locks on those records will be released the next time NDB Kernel
     *        is contacted for more records.


     *
     * @param fetchAllowed If set to false, then fetching is disabled
     * @param forceSend If true send will occur immediately
     * @return
     * -   0: if another tuple was received. <br>
     * -   1: if there are no more tuples to scan. <br>
     * -   2: if there are no more cached records. <br>
     * @throws NdbApiException if unsuccessful
     */
    int nextResult(boolean fetchAllowed, boolean forceSend)
        throws NdbApiException ;

    /**
     * Get the next tuple in a scan transaction.
     *
     * Same as calling nextResult(fetchAllowed, false);
     *
     * @see #nextResult(boolean, boolean)
     */
    int nextResult(boolean fetchAllowed) throws NdbApiException ;

    /**
     * Get the next tuple in a scan transaction.
     *
     * Same as calling nextResult(true,false);
     *
     * @see #nextResult(boolean, boolean)
     */
    int nextResult() throws NdbApiException ;

    /**
     * Close a scan.
     *
     * @param forceSend when this parameter is set to true,
     *        it forces transactions to be sent to the kernel.
     * @param releaseOp set to true in order to release the operation
     *        and free its resources.
     */
    void close(boolean forceSend, boolean releaseOp);

    /**
     * Close a scan.
     *
     * @param forceSend when this parameter is set to true,
     *        it forces transactions to be sent to the kernel.
     */
    void close(boolean forceSend);

    /**
     * Close a scan.
     *
     * <p>It does not force its transactions to be sent to the kernel
     *    or release the operation and free its resources.
     * @see #close(boolean forceSend, boolean releaseOp)
     */
    void close();

    /**
     * Updates the current tuple in the scan using the current transaction
     *
     * @return An NdbOperation to use for the update
     * @throws NdbApiException if there is an internal problem in the cluster.
     */
    NdbOperation updateCurrentTuple() throws NdbApiException;

    /**
     * Update the current tuple in the scan using a specific NdbTransaction
     *
     * @param updateTrans transaction to use for the update
     * @return An NdbOperation to use for the update
     * @throws NdbApiException if there is an internal problem in the cluster.
     */
    NdbOperation updateCurrentTuple(NdbTransactionImpl updateTrans)
        throws NdbApiException;

    /**
     * Delete the current tuple using the current transaction.
     *
     * @throws NdbApiException if there is an internal problem in the cluster.
     */
    void deleteCurrentTuple() throws NdbApiException;

    /**
     * Delete the current tuple using a specific NdbTransaction
     *
     * @param deleteTrans the transaction object used to perform the deletion.
     * @throws NdbApiException if there is an internal problem in the cluster
     */
    void deleteCurrentTuple(NdbTransactionImpl deleteTrans)
        throws NdbApiException;

    /**
     * Lock the current tuple using the current transaction.
     *
     * @return An NdbOperation on the tuple
     * @throws NdbApiException if there is an internal problem in the cluster
     */
    NdbOperation lockCurrentTuple()
        throws NdbApiException;

    /**
     *  Locks the current tuple using a specific transaction.
     *
     * @return An NdbOperation on the tuple
     * @param trans the transaction that should perform the lock.
     * @throws NdbApiException if there is an internal problem in the cluster
     */
    NdbOperation lockCurrentTuple(NdbTransactionImpl trans)
        throws NdbApiException;

    /**
     * Creates a new NdbScanFilter that can be applied to
     * filter the results returned from the scan operation.
     * Note: call getValue() methods on the NdbScanOperation object
     * before you call begin on a NdbScanFilter object.
     *
     * @return A new NdbScanFilter
     * @throws NdbApiException
     */
    NdbScanFilter getNdbScanFilter() throws NdbApiException;


}
