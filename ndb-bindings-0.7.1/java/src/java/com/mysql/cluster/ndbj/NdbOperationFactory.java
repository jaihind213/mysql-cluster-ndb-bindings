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

import com.mysql.cluster.ndbj.NdbOperation.LockMode;
import com.mysql.cluster.ndbj.NdbScanOperation.ScanFlag;

public interface NdbOperationFactory {

    /**
     * Creates a NdbOperation (primary key operation) and adds it to the
     * transaction.
     * @param tableName name of the table to be operated upon
     * @param lockMode the lock mode to use
     * @return an NdbOperation object
     * @throws NdbApiException if tableName is null or empty, the object
     *         has been closed, or the NdbOperation object could not be created
     */
    public NdbOperation getSelectOperation(String tableName,
                                           NdbOperation.LockMode lockMode)
        throws NdbApiException;

    /**
     * Creates a NdbOperation (primary key operation) and adds it to the
     * transaction.
     * @param tableName name of the table to be operated upon
     * @return an NdbOperation object
     * @throws NdbApiException if tableName is null or empty, the object
     *         has been closed, or the
     * Operation object could not be created
     */
    public NdbOperation getSelectOperation(String tableName)
        throws NdbApiException;

    /**
     * Creates a NdbOperation (primary key operation) and adds it to the
     * transaction.
     * @param table the table to be operated upon
     * @param lockMode the lock mode to use
     * @return an NdbOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     *         been closed, or the Operation object could not be created
     */
    public NdbOperation getSelectOperation(NdbTable table,
                                           NdbOperation.LockMode lockMode)
        throws NdbApiException;

    /**
     * Creates a NdbOperation (primary key operation) and adds it to the
     * transaction.
     * @param table the table to be operated upon
     * @return an NdbOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     *         been closed, or the Operation object could not be created
     */
    public NdbOperation getSelectOperation(NdbTable table)
        throws NdbApiException;

    /**
     * Creates a NdbOperation (primary key operation) and adds it to the
     * transaction.
     * @param tableName name of the table to be operated upon
     * @return an NdbOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     *         been closed, or the Operation object could not be created
     */
    public NdbOperation getUpdateOperation(String tableName)
        throws NdbApiException;

    /**
     * Creates a NdbOperation (primary key operation) and adds it to the
     * transaction.
     * @param table the table to be operated upon
     * @return an NdbOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     *         been closed, or the Operation object could not be created
     */
    public NdbOperation getUpdateOperation(NdbTable table)
        throws NdbApiException;

    /**
     * Creates a NdbOperation (primary key operation) and adds it to the
     * transaction.
     * @param tableName name of the table to be operated upon
     * @return an NdbOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     *         been closed, or the Operation object could not be created
     */
    public NdbOperation getWriteOperation(String tableName)
        throws NdbApiException;

    /**
     * Creates a NdbOperation (primary key operation) and adds it to the
     * transaction.
     * @param table the table to be operated upon
     * @return an NdbOperation object
     * @throws NdbApiException if tableName is null or empty, the object has 
     *         been closed, or the Operation object could not be created
     */
    public NdbOperation getWriteOperation(NdbTable table)
        throws NdbApiException;

    /**
     * Creates a NdbOperation (primary key operation) and adds it to the
     * transaction.
     * @param tableName name of the table to be operated upon
     * @return an NdbOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     *         been closed, or the Operation object could not be created
     */
    public NdbOperation getInsertOperation(String tableName)
        throws NdbApiException;

    /**
     * Creates a NdbOperation (primary key operation) and adds it to the
     * transaction.
     * @param table the table to be operated upon
     * @return an NdbOperation object
     * @throws NdbApiException if tableName is null or empty, the object has 
     *         been closed, or the Operation object could not be created
     */
    public NdbOperation getInsertOperation(NdbTable table)
        throws NdbApiException;

    /**
     * Creates a NdbOperation (primary key operation) and adds it to the
     * transaction.
     * @param tableName name of the table to be operated upon
     * @return an NdbOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     *         been closed, or the Operation object could not be created
     */
    public NdbOperation getDeleteOperation(String tableName)
        throws NdbApiException;

    /**
     * Creates a NdbOperation (primary key operation) and adds it to the
     * transaction.
     * @param table the table to be operated upon
     * @return an NdbOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     *         been closed, or the Operation object could not be created
     */
    public NdbOperation getDeleteOperation(NdbTable table)
        throws NdbApiException;

    /**
     * Creates a NdbIndexOperation (secondary unique index operation) and adds
     * it to the transaction.
     * @param indexName name of the index to be used
     * @param tableName name of the table to be operated upon
     * @param lockMode the LockMode to use
     * @return an NdbOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     *         been closed, or the Operation object could not be created
     */
    public NdbIndexOperation
    getSelectUniqueOperation(String indexName,
                             String tableName,
                             NdbOperation.LockMode lockMode)
        throws NdbApiException;

    /**
     * Creates a NdbIndexOperation (secondary unique index operation) and adds
     * it to the transaction.
     * @param indexName name of the index to be used
     * @param tableName name of the table to be operated upon
     * @return an NdbOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     *         been closed, or the Operation object could not be created
     */
    public NdbIndexOperation getSelectUniqueOperation(String indexName,
                                                      String tableName)
        throws NdbApiException;

    /**
     * Creates a NdbIndexOperation (secondary unique index operation) and adds
     * it to the transaction.
     * @param index the index to use
     * @param table the table to be operated upon
     * @param lockMode the lock mode to use
     * @return an NdbOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     *         been closed, or the Operation object could not be created
     */
    public NdbIndexOperation
    getSelectUniqueOperation(NdbIndex index,
                             NdbTable table,
                             NdbOperation.LockMode lockMode)
        throws NdbApiException;

    /**
     * Creates a NdbIndexOperation (secondary unique index operation) and adds
     * it to the transaction.
     * @param index the index to use
     * @param table the table to be operated upon
     * @return an NdbOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     *         been closed, or the Operation object could not be created
     */
    public NdbIndexOperation getSelectUniqueOperation(NdbIndex index,
                                                      NdbTable table)
        throws NdbApiException;

    /**
     * Creates a NdbIndexOperation (secondary unique index operation) and adds
     * it to the transaction.
     * @param indexName name of the index to use
     * @param tableName name of the table to be operated upon
     * @return an NdbOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     *         been closed, or the Operation object could not be created
     */
    public NdbIndexOperation getUpdateUniqueOperation(String indexName,
                                                      String tableName)
        throws NdbApiException;

    /**
     * Creates a NdbIndexOperation (secondary unique index operation) and adds
     * it to the transaction.
     * @param index the index to use
     * @param table the table to be operated upon
     * @return an NdbOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     *         been closed, or the Operation object could not be created
     */
    public NdbIndexOperation getUpdateUniqueOperation(NdbIndex index,
                                                      NdbTable table)
        throws NdbApiException;


    /**
     * Creates a NdbIndexOperation (secondary unique index operation) and adds
     * it to the transaction.
     * @param indexName name of the index to use
     * @param tableName name of the table to be operated upon
     * @return an NdbOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     *         been closed, or the Operation object could not be created
     */
    public NdbIndexOperation getDeleteUniqueOperation(String indexName,
                                                      String tableName)
        throws NdbApiException;

    /**
     * Creates a NdbIndexOperation (primary key operation) and adds it to the
     * transaction.
     * @param index the index to use
     * @param table the table to be operated upon
     * @return an NdbOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     *         been closed, or the Operation object could not be created
     */
    public NdbIndexOperation getDeleteUniqueOperation(NdbIndex index,
                                                      NdbTable table)
        throws NdbApiException;

    /**
     * Creates a NdbIndexOperation (primary key operation) and adds it to the
     * transaction.
     * @param index the index to use
     * @return an NdbOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     *         been closed, or the Operation object could not be created
     */
    public NdbIndexOperation getDeleteUniqueOperation(NdbIndex index)
        throws NdbApiException;

    /**
     * Creates a NdbScanOperation (table scan operation) and adds it to the
     * transaction.
     * @param tableName name of the table to be operated upon
     * @param lockMode the lock mode to use
     * @param scanFlag flag controlling the operation of the scan
     * @param parallel number of fragments to scan in parallel
     * @param batch how many records will be returned to the client from the
     * server by the next NdbScanOperation::nextResult(true) method call
     * @return an NdbScanOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     * been closed, or the Operation object could not be created
     */
    public NdbScanOperation getSelectScanOperation(String tableName,
                                                   LockMode lockMode,
                                                   ScanFlag scanFlag,
                                                   int parallel, int batch)
        throws NdbApiException;

    /**
     * Creates a NdbScanOperation (table scan operation) and adds it to the
     * transaction.
     * @param tableName name of the table to be operated upon
     * @param lockMode the lock mode to use
     * @return an NdbScanOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     * been closed, or the Operation object could not be created
     */
    public NdbScanOperation getSelectScanOperation(String tableName,
                                                   LockMode lockMode)
        throws NdbApiException;

    /**
     * Creates a NdbScanOperation (table scan operation) and adds it to the
     * transaction.
     * @param tableName name of the table to be operated upon
     * @return an NdbScanOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     * been closed, or the Operation object could not be created
     */
    public NdbScanOperation getSelectScanOperation(String tableName)
        throws NdbApiException;

    /**
     * Creates a NdbScanOperation (table scan operation) and adds it to the
     * transaction.
     * @param table the table to be operated upon
     * @param lockMode the lock mode to use
     * @param scanFlag flag controlling the operation of the scan
     * @param parallel number of fragments to scan in parallel
     * @param batch how many records will be returned to the client from the
     * server by the next NdbScanOperation::nextResult(true) method call
     * @return an NdbScanOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     * been closed, or the Operation object could not be created
     */
    public NdbScanOperation getSelectScanOperation(NdbTable table,
                                                   LockMode lockMode,
                                                   ScanFlag scanFlag,
                                                   int parallel, int batch)
        throws NdbApiException;

    /**
     * Creates a NdbScanOperation (table scan operation) and adds it to the
     * transaction.
     * @param table the table to be operated upon
     * @param lockMode the lock mode to use
     * @return an NdbScanOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     * been closed, or the Operation object could not be created
     */
    public NdbScanOperation getSelectScanOperation(NdbTable table,
                                                   LockMode lockMode)
        throws NdbApiException;

    /**
     * Creates a NdbScanOperation (table scan operation) and adds it to the
     * transaction.
     * @param table the table to be operated upon
     * @return an NdbScanOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     * been closed, or the Operation object could not be created
     */
    public NdbScanOperation getSelectScanOperation(NdbTable table)
        throws NdbApiException;

    /**
     * Creates a NdbScanOperation (table scan operation) and adds it to the
     * transaction.
     * @param table the table to be operated upon
     * @param lockMode the lock mode to use
     * @param scanFlag OR'd set of ScanFlags controlling the operation of the
     * scan
     * @param parallel number of fragments to scan in parallel
     * @param batch how many records will be returned to the client from the
     * server by the next NdbScanOperation::nextResult(true) method call
     * @return an NdbScanOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     * been closed, or the Operation object could not be created
     */
    public NdbScanOperation getSelectScanOperation(NdbTable table,
                                                   LockMode lockMode,
                                                   int scanFlag, int parallel,
                                                   int batch)
        throws NdbApiException;

    /**
     * Creates a NdbScanOperation (table scan operation) and adds it to the
     * transaction.
     * @param tableName the table to be operated upon
     * @param lockMode the lock mode to use
     * @param scanFlag OR'd set of ScanFlags controlling the operation of the
     * scan
     * @param parallel number of fragments to scan in parallel
     * @param batch how many records will be returned to the client from the
     * server by the next NdbScanOperation::nextResult(true) method call
     * @return an NdbScanOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     * been closed, or the Operation object could not be created
     */
    public NdbScanOperation getSelectScanOperation(String tableName,
                                                   LockMode lockMode,
                                                   int scanFlag, int parallel,
                                                   int batch)
        throws NdbApiException;

    /**
     * Creates a NdbIndexScanOperation (index scan operation) and adds it to
     * the transaction.
     * @param indexName name of the index to be scanned
     * @param tableName name of the table to be operated upon
     * @param lockMode the lock mode to use
     * @param scanFlag flag controlling the operation of the scan
     * @param parallel number of fragments to scan in parallel
     * @param batch how many records will be returned to the client from the
     * server by the next NdbScanOperation::nextResult(true) method call
     * @return an NdbIndexScanOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     * been closed, or the Operation object could not be created
     */
    public NdbIndexScanOperation getSelectIndexScanOperation(String indexName,
                                                             String tableName,
                                                             LockMode lockMode,
                                                             ScanFlag scanFlag,
                                                             int parallel,
                                                             int batch)
        throws NdbApiException;

    /**
     * Creates a NdbIndexScanOperation (index scan operation) and adds it to
     * the transaction.
     * @param indexName name of the index to be scanned
     * @param tableName name of the table to be operated upon
     * @param lockMode the lock mode to use
     * @return an NdbIndexScanOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     * been closed, or the Operation object could not be created
     */
    public NdbIndexScanOperation getSelectIndexScanOperation(String indexName,
                                                             String tableName,
                                                             LockMode lockMode)
        throws NdbApiException;

    /**
     * Creates a NdbIndexScanOperation (index scan operation) and adds it to
     * the transaction.
     * @param indexName name of the index to be scanned
     * @param tableName name of the table to be operated upon
     * @return an NdbIndexScanOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     * been closed, or the Operation object could not be created
     */
    public NdbIndexScanOperation getSelectIndexScanOperation(String indexName,
                                                             String tableName)
        throws NdbApiException;

    /**
     * Creates an NdbIndexScanOperation (index scan operation) and adds it to
     * the transaction.
     * @param index the index to be scanned
     * @param table the table to be operated upon
     * @param lockMode the lock mode to use
     * @param scanFlag flag controlling the operation of the scan
     * @param parallel number of fragments to scan in parallel
     * @param batch how many records will be returned to the client from the
     * server by the next NdbScanOperation::nextResult(true) method call
     * @return an NdbScanOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     * been closed, or the Operation object could not be created
     */
    public NdbIndexScanOperation getSelectIndexScanOperation(NdbIndex index,
                                                             NdbTable table,
                                                             LockMode lockMode,
                                                             ScanFlag scanFlag,
                                                             int parallel,
                                                             int batch)
        throws NdbApiException;

    /**
     * Creates an NdbIndexScanOperation (index scan operation) and adds it to
     * the transaction.
     * @param index the index to be scanned
     * @param table the table to be operated upon
     * @param lockMode the lock mode to use
     * @return an NdbScanOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     * been closed, or the Operation object could not be created
     */
    public NdbIndexScanOperation getSelectIndexScanOperation(NdbIndex index,
                                                             NdbTable table,
                                                             LockMode lockMode)
        throws NdbApiException;

    /**
     * Creates an NdbIndexScanOperation (index scan operation) and adds it to
     * the transaction.
     * @param index the index to be scanned
     * @param table the table to be operated upon
     * @return an NdbScanOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     * been closed, or the Operation object could not be created
     */
    public NdbIndexScanOperation getSelectIndexScanOperation(NdbIndex index,
                                                             NdbTable table)
        throws NdbApiException;

    /**
     * Creates a NdbIndexScanOperation (Index scan operation) and adds it to
     * the transaction.
     * @param index the index to be scanned
     * @param lockMode the lock mode to use
     * @param scanFlag flag controlling the operation of the scan
     * @param parallel number of fragments to scan in parallel
     * @param batch how many records will be returned to the client from the
     * server by the next NdbScanOperation::nextResult(true) method call
     * @return an NdbScanOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
 been closed, or the Operation object could not be created
     */
    public NdbIndexScanOperation getSelectIndexScanOperation(NdbIndex index,
                                                             LockMode lockMode,
                                                             ScanFlag scanFlag,
                                                             int parallel,
                                                             int batch)
        throws NdbApiException;

    /**
     * Creates a NdbIndexScanOperation (Index scan operation) and adds it to
     * the transaction.
     * @param index the index to be scanned
     * @param lockMode the lock mode to use
     * @return an NdbScanOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     * been closed, or the Operation object could not be created
     */
    public NdbIndexScanOperation getSelectIndexScanOperation(NdbIndex index,
                                                             LockMode lockMode)
        throws NdbApiException;

    /**
     * Creates a NdbIndexScanOperation (Index scan operation) and adds it to
     * the transaction.
     * @param index the index to be scanned
     * @return an NdbScanOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     * been closed, or the Operation object could not be created
     */
    public NdbIndexScanOperation getSelectIndexScanOperation(NdbIndex index)
        throws NdbApiException;

    /**
     * Creates a NdbIndexScanOperation (index scan operation) and adds it to
     * the transaction.
     * @param indexName the index to be scanned
     * @param tableName the table to be operated upon
     * @param scanFlag OR'd set of ScanFlags controlling the operation of the
     * scan
     * @return an NdbIndexScanOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     * been closed, or the Operation object could not be created
     */
    public NdbIndexScanOperation getSelectIndexScanOperation(String indexName,
                                                             String tableName,
                                                             LockMode lockMode,
                                                             int scanFlag,
                                                             int parallel,
                                                             int batch)
        throws NdbApiException;

    /**
     * Creates an NdbIndexScanOperation (index scan operation) and adds it to
     * the transaction.
     * @param index the index to be scanned
     * @param table the table to be operated upon
     * @param scanFlag OR'd set of ScanFlags controlling the operation of theo
     * scan
     * @return an NdbScanOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     * been closed, or the Operation object could not be created
     */
    public NdbIndexScanOperation getSelectIndexScanOperation(NdbIndex index,
                                                             NdbTable table,
                                                             LockMode lockMode,
                                                             int scanFlag,
                                                             int parallel,
                                                             int batch)
        throws NdbApiException;

    /**
     * Creates a NdbIndexScanOperation (Index scan operation) and adds it to
     * the transaction.
     * @param index the index to be scanned
     * @param scanFlag OR'd set of ScanFlags controlling the operation of the
     * scan
     * @return an NdbScanOperation object
     * @throws NdbApiException if tableName is null or empty, the object has
     * been closed, or the Operation object could not be created
     */
    public NdbIndexScanOperation getSelectIndexScanOperation(NdbIndex index,
                                                             LockMode lockMode,
                                                             int scanFlag,
                                                             int parallel,
                                                             int batch)
        throws NdbApiException;

	public NdbAtomicOperation getAtomicUpdateOperation(NdbTable aTable) throws NdbApiException;

	public NdbAtomicOperation getAtomicUpdateOperation(String aTableName) throws NdbApiException;

}
