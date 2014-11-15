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
 * A NdbOperation object represents an operation using a primary key to access
 * a row in a table.
 * <br>The main way to use it is to follow the following steps:
 * <ol>
 * <li>create a NdbOperation object using startTransaction method on a Ndb
 *     object
 * <li>decide on the type of operation to perfom on the operation
 *     (readTuple(), insertTuple(), updateTuple(), deleteTuple())
 * <li>define the search criteria for a row by setting the primary key using
 *     the equal() method.
 *      For example, if you are reading a tuple with a single column as a
 *      primary key, you execute the equal() method once on that column,
 *      specifying the value of the column to indicate the row you are
 *      searching for.
 *     However, if your primary key consists of multiple columns
 *      (e.g., PRIMARY KEY(a, b, c)), then you call the equal() method for
 *     all the of the columns in the primary key.<br>
 *     For examplee, for the following table:
 *       @code { create table t_multi
 *                 (a int NOT NULL,
 *                  b int NOT NULL,
 *                  c char(20),
 *                  PRIMARY KEY (a,b)) engine=ndb;) }
 *     You would call
 *     @code {
 *     operation.equal("a", VAL);
 *     operation.equal("b",VAL2);
 *     }.
 * <li>For any rows found after you called the equal() method(s), you can
 *     get/set the value of columns in those rows. These are known as
 *     <b>attribute actions</b>.
 * <li>Commit the operation by calling the exec() method on the NdbTransaction
 *     object.
 * <li>Retrieve any results using a NdbResultSet.
 * </ol>
 * <p>
 * The following code assumes that the operation is executed on the following
 * table
 * <p>
 * <code> create table t (id int NOT NULL, val varchar(64), PRIMARY KEY id)
 *   engine=ndb;
 * </code>
 * <p>
 * <code>
 *          <br>trans = ndb.startTransaction();
 *          <br>NdbOperation op = trans.getNdbOperation(tableName);
 *          <br>op.readTuple(NdbOperation.LockMode.LM_Exclusive);
 *          <br>op.equal("id",PK_VAL);
 *          <br>op.getValue("val");
 *          <br>NdbResultSet rs = op.resultData();
 *          <br>trans.execute(NdbTransaction.ExecType.Commit,
 *                            NdbTransaction.AbortOption.AbortOnError,1 );
 *          <br>while(rs.next())
 *          <br> String val = rs.getString(columnName);
 * </code>
 *
 * <p>An IllegalStateException is thrown if you attempt to invoke methods on
 * this class after the close method has been called on instances.
 *
 * @see com.mysql.cluster.ndbj.NdbTransaction
 * @see com.mysql.cluster.ndbj.Ndb
 * @see com.mysql.cluster.ndbj.NdbClusterConnectionImpl
 * @see com.mysql.cluster.ndbj.NdbIndexOperation
 * @see com.mysql.cluster.ndbj.NdbScanOperation
 * @see com.mysql.cluster.ndbj.NdbIndexScanOperation
 */
public interface NdbOperation extends NdbBaseOperationEquals,
                              NdbBaseOperationSet {

    public enum LockMode {
        LM_Read(NdbjJNI.NdbOperationImpl_LM_Read_get()),          ///< Read with shared lock
        LM_Exclusive(NdbjJNI.NdbOperationImpl_LM_Exclusive_get()),     ///< Read with exclusive lock
        LM_CommittedRead(NdbjJNI.NdbOperationImpl_LM_CommittedRead_get()); ///< Ignore locks, read last committed value

        public int type = 0;

        public int swigValue() {
            return this.type;
        }
        private LockMode(int type) {
            this.type = type;
        }
    }

    public enum AbortOption {
        DefaultAbortOption(NdbjJNI.NdbOperationImpl_DefaultAbortOption_get()),
        AbortOnError(NdbjJNI.NdbOperationImpl_AbortOnError_get()),
        AO_IgnoreError(NdbjJNI.NdbOperationImpl_AO_IgnoreError_get());
        public int type = 0;

        public int swigValue() {
            return this.type;
        }
        private AbortOption(int type) {
            this.type = type;
        }
    }

    /**
     * Creates a blob handle NdbBlob.
     * <br>A second call with same argument returns the previously created
     *  handle.
     * The handle is linked to the operation and is maintained automatically.
     *
     * @param  columnId integer position (offset) of column number in schema
     *         definition (columnId starts from  position '1' for the first
     *         column in a schema)
     * @return NdbBlob object
     * @throws NdbApiException if there was a problem in the cluster when
     *         selecting this column.
     * NOTE: getting a blob handle on a non-existing column yields a seg fault,
     *       see bug report http://bugs.mysql.com/bug.php?id=21036
     *
     */
    public NdbBlob getBlobHandle(long columnId)
        throws NdbApiException;
    @Deprecated
    public NdbBlob getNdbBlobHandle(long columnId)
        throws NdbApiException;

    /**
     * Gets a BlobHandle object using the String name for the Blob column in
     * the the table. The BlobHandle object can be used to read/write the Blob.
     * @param columnName name of the Column in the Schema
     * @return NdbBlob object
     * @throws NdbApiException if there was a problem in the cluster when
     *  selecting this column.
     */
    public NdbBlob getBlobHandle(String columnName)
        throws NdbApiException;
    @Deprecated
    public NdbBlob getNdbBlobHandle(String columnName)
        throws NdbApiException;

    public NdbTable getTable();

}
