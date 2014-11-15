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

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Time;

/**
 * A NdbResultSet is used to store attributes in rows
 * that were selected after executing some query operation
 * on the cluster.
 *
 * You should be consistent in use of NdbResultSet objects.
 * Either access columns by name (String) or id (int).
 * Do not mix usage of access by name and id.
 * Id counts start at '0' (for the first column in a table).
 */
public class NdbResultSetImpl extends ThrowingResultSet
    implements NdbResultSet {


    /**
     * We start reading results from '0'
     */
    protected Map<String,NdbRecAttr> resultSet;
    protected ArrayList<NdbColumn> theColumns;
    NdbOperation op = null;



    private int lastColumnNull;


    private boolean fetched = false;

    NdbResultSetMetaData theMetaData = null;




    /**
     * Creates a new <code>NdbResultSetImpl</code> instance.
     *
     */
    protected NdbResultSetImpl() {
        resultSet = new HashMap<String,NdbRecAttr>();
    }
    /**
     * Package-hidden constructor.
     * <br> Use resultData() method on an operation object to create
     * a NdbResultSet object.
     * @param op
     * @param ht
     */
    NdbResultSetImpl(NdbOperation op, Map<String,NdbRecAttr> ht) {
        resultSet = ht;

        this.op = op;
        lastColumnNull = -1;

    }


    /**
     * @see com.mysql.cluster.ndbj.NdbResultSet#getInt(int)
     */
    @Override
    public int getInt(int columnId) throws NdbApiException {
        NdbRecAttr rec = resultSet.get(new Integer(columnId - 1));
        testNull(rec);
        return rec.getInt();
    }

    /**
     * @see com.mysql.cluster.ndbj.NdbResultSet#getInt(java.lang.String)
     */
    @Override
    public int getInt(String columnName) throws NdbApiException {
        NdbRecAttr rec = resultSet.get(columnName);
        testNull(rec);
        return rec.getInt();
    }

    /**
     * @see com.mysql.cluster.ndbj.NdbResultSet#getShort(int)
     */
    @Override
    public short getShort(int columnId) throws NdbApiException {
        NdbRecAttr rec = resultSet.get(new Integer(columnId - 1));
        testNull(rec);
        return rec.getShort();
    }

    /**
     * @see com.mysql.cluster.ndbj.NdbResultSet#getShort(java.lang.String)
     */
    @Override
    public short getShort(String columnName) throws NdbApiException {
        NdbRecAttr rec = resultSet.get(columnName);
        testNull(rec);
        return rec.getShort();
    }

    /**
     * @see com.mysql.cluster.ndbj.NdbResultSet#getLong(int)
     */
    @Override
    public long getLong(int columnId) throws NdbApiException {
        NdbRecAttr rec = resultSet.get(new Integer(columnId - 1));
        testNull(rec);
        return rec.getLong();
    }

    /**
     * @see com.mysql.cluster.ndbj.NdbResultSet#getLong(java.lang.String)
     */
    @Override
    public long getLong(String columnName) throws NdbApiException {
        NdbRecAttr rec = resultSet.get(columnName);
        testNull(rec);
        return rec.getLong();
    }


    /**
     * @see com.mysql.cluster.ndbj.NdbResultSet#getFloat(int)
     */
    @Override
    public float getFloat(int columnId) throws NdbApiException {
        NdbRecAttr rec = resultSet.get(new Integer(columnId - 1));
        testNull(rec);
        return rec.getFloat();
    }

    /**
     * @see com.mysql.cluster.ndbj.NdbResultSet#getFloat(java.lang.String)
     */
    @Override
    public float getFloat(String columnName) throws NdbApiException {
        NdbRecAttr rec = resultSet.get(columnName);
        testNull(rec);
        return rec.getFloat();
    }

    /**
     * @see com.mysql.cluster.ndbj.NdbResultSet#getDouble(int)
     */
    @Override
    public double getDouble(int columnId) throws NdbApiException {
        NdbRecAttr rec = resultSet.get(new Integer(columnId - 1));
        testNull(rec);
        return rec.getDouble();
    }

    /**
     * @see com.mysql.cluster.ndbj.NdbResultSet#getDouble(java.lang.String)
     */
    @Override
    public double getDouble(String columnName) throws NdbApiException {
        NdbRecAttr rec = resultSet.get(columnName);
        testNull(rec);
        return rec.getDouble();
    }

    /**
     * @see com.mysql.cluster.ndbj.NdbResultSet#getTimestamp(int)
     */
    @Override
    public Timestamp getTimestamp(int columnId) throws NdbApiException {
        NdbRecAttr rec = resultSet.get(new Integer(columnId - 1));
        testNull(rec);

        if (rec.getType() == NdbColumn.Type.Timestamp) {
            return rec.getTimestamp();
        } else {
            return rec.getDatetime();
        }
    }

    /**
     * @see com.mysql.cluster.ndbj.NdbResultSet#getTimestamp(java.lang.String)
     */
    @Override
    public Timestamp getTimestamp(String columnName) throws NdbApiException {
        NdbRecAttr rec = resultSet.get(columnName);
        testNull(rec);

        if (rec.getType() == NdbColumn.Type.Timestamp) {
            return rec.getTimestamp();
        } else {
            return rec.getDatetime();
        }
    }

    @Override
    public Timestamp getTimestamp(int columnId, Calendar cal) throws NdbApiException {
        return getTimestamp(columnId);
    }

    @Override
    public Timestamp getTimestamp(String columnName, Calendar cal) throws NdbApiException {
        return getTimestamp(columnName);
    }

    @Override
    public Date getDate(int columnId) throws NdbApiException {
        NdbRecAttr rec = resultSet.get(columnId - 1);
        testNull(rec);
        return rec.getDate();
    }

    @Override
    public Date getDate(String columnName) throws NdbApiException {
        NdbRecAttr rec = resultSet.get(columnName);
        testNull(rec);
        return rec.getDate();
    }

    @Override
    public Time getTime(int columnId) throws NdbApiException {
        NdbRecAttr rec = resultSet.get(columnId - 1);
        testNull(rec);
        return rec.getTime();
    }

    @Override
    public Time getTime(String columnName) throws NdbApiException {
        NdbRecAttr rec = resultSet.get(columnName);
        testNull(rec);
        return rec.getTime();
    }

    /**
     * @see com.mysql.cluster.ndbj.NdbResultSet#getBytes(int)
     */
    @Override
    public byte[] getBytes(int columnId) throws NdbApiException {
        NdbRecAttr rec = resultSet.get(new Integer(columnId - 1));
        testNull(rec);
        return rec.getBytes();
    }

    /**
     * @see com.mysql.cluster.ndbj.NdbResultSet#getBytes(java.lang.String)
     */
    @Override
    public byte[] getBytes(String columnName) throws NdbApiException {
        NdbRecAttr rec = resultSet.get(columnName);
        testNull(rec);
        return rec.getBytes();
    }
    
    /**
     * @see com.mysql.cluster.ndbj.NdbResultSet#getBytes(int)
     */
    @Override
    public byte[] getStringBytes(int columnId) throws NdbApiException {
        NdbRecAttr rec = resultSet.get(new Integer(columnId - 1));
        testNull(rec);
        return rec.getStringBytes();
    }

    /**
     * @see com.mysql.cluster.ndbj.NdbResultSet#getBytes(java.lang.String)
     */
    @Override
    public byte[] getStringBytes(String columnName) throws NdbApiException {
        NdbRecAttr rec = resultSet.get(columnName);
        testNull(rec);
        return rec.getStringBytes();
    }

    /**
     * @see com.mysql.cluster.ndbj.NdbResultSet#getString(int)
     */
    @Override
    public String getString(int columnId) throws NdbApiException {
        NdbRecAttr rec = resultSet.get(new Integer(columnId - 1));
        testNull(rec);
        return rec.getString();
    }

    /**
     * @see com.mysql.cluster.ndbj.NdbResultSet#getString(java.lang.String)
     */
    @Override
    public String getString(String columnName) throws NdbApiException {
        NdbRecAttr rec = resultSet.get(columnName);
        testNull(rec);
        return rec.getString();
    }

    /**
     * @see com.mysql.cluster.ndbj.NdbResultSet#getBlob(int)
     */
    @Override
    public NdbBlob getBlob(int columnId) throws NdbApiException {
        return this.op.getBlobHandle(columnId);
    }
    /**
     * @see com.mysql.cluster.ndbj.NdbResultSet#getBlob(java.lang.String)
     */
    @Override
    public NdbBlob getBlob(String columnName) throws NdbApiException {
        return this.op.getBlobHandle(columnName);
    }

    /**
     * @see com.mysql.cluster.ndbj.NdbResultSetImpl#getDecimal(int)
     */
    @Override
    public BigDecimal getDecimal(int columnId) throws NdbApiException {
        NdbRecAttr rec = resultSet.get(new Integer(columnId - 1));
        testNull(rec);
        return rec.getDecimal();
    }

    /**
     * @see com.mysql.cluster.ndbj.NdbResultSetImpl#getDecimal(String)
     */
    @Override
    public BigDecimal getDecimal(String columnName) throws NdbApiException {
        NdbRecAttr rec = resultSet.get(columnName);
        testNull(rec);
        return rec.getDecimal();
    }

    /**
     * Allow fine-grained handling of tuple fetching.
     *
     * @todo This needs to return int for proper usage.
     */
    public boolean next(boolean fetchAllowed) throws NdbApiException {
        // TODO: Seriously - this method and the next need to be redesigned
        if (NdbScanOperationImpl.class.isInstance(op))  {
            int scanCheck = ((NdbScanOperation)op).nextResult(fetchAllowed);
            return (scanCheck==0);
        } else {
            // It's not a scan operation
            if (fetched) {
                return false;
            }
            fetched=true;
            return true;
        }

    }

    /**
     * @see com.mysql.cluster.ndbj.NdbResultSet#next()
     */
    @Override
    public boolean next() throws NdbApiException {
        return next(true);
    }

    /*
        // TODO: Please double check me
        if (op == null) {
            throw new NdbApiException("NdbResultSet.next() "
                                      + "- op == null");
        }
        // nextResult on NdbIndexScanOperation and NdbScanOperation return:
        // -1: Indicates that an error has occurred.
        // 0: Another tuple has been received.
        // 1: There are no more tuples to scan.
        // 2: There are no more cached records
        //   (invoke nextResult(true) to fetch more records).

        if (NdbScanOperationImpl.class.isInstance((Object)op) ||
            NdbIndexScanOperationImpl.class.isInstance((Object)op)) {

            scanCheck = ((NdbScanOperation)op).nextResult();

            // if nextResult(false) returns scanCheck == 0,
            // transfer the record to another transaction.

            // if nextResult(false) returns scanCheck == 2,
            // execute and commit the other transaction

            // Following this, call nextResult(true) - this fetches more
            // records and caches them in the NDB API.

            if (scanCheck == 2) {
                scanCheck = ((NdbScanOperation)op).nextResult(true, true);
                run = true;
            } else if (scanCheck == 0 && !run) {
                scanCheck = ((NdbScanOperation)op).nextResult(false, true);
            }
            if (scanCheck < 0) {
                throw new NdbApiException("problem with NdbResultSet.next()");
            }
            if (scanCheck == 0) {
                hasData = true;
            } else if (scanCheck == 2) {
                if (rowsModified) {
                    // TODO: FIX This section - we're not storing an
                    // NdbTransaction within NdbScanOp any more
                    NdbTransaction trans = null;
                    trans = ((NdbScanOperationImpl)theOp).getNdbTransaction();

                    trans.execute(ExecType.NoCommit,
                                  AbortOption.AbortOnError, true);
                    rowsModified = false;
                }
                return next();
            } else if (scanCheck == 1) {
                hasData = false;
            }
            run = false;
            return hasData;
        } else {
            if (hasData) {
                hasData = false;
                return true;
            }
            return false;
        }
    }
    */

    /**
     * @see com.mysql.cluster.ndbj.NdbResultSet#deleteRow()
     */
    @Override
    public void deleteRow() throws NdbApiException {

        if (!NdbScanOperationImpl.class.isInstance(op)) {

            throw new NdbApiException("NdbResultSet::deleteRow(). "
                                      + "Not implemented for PK operations.");
        }
        else if (op != null) {
            ((NdbScanOperation)op).deleteCurrentTuple();
        }
        else {
            throw new NdbApiException("No underlying scan operation.");
        }
    }

    /**
     * @see com.mysql.cluster.ndbj.NdbResultSet#updateRow()
     */
    public NdbOperation getUpdateOperation() throws NdbApiException {
        if (!NdbScanOperationImpl.class.isInstance(op)) {

            throw new NdbApiException("NdbResultSet::updateRow(). "
                                      + "Not implemented for PK operations.");
        }
        else if (op != null) {
            NdbOperation theOp = ((NdbScanOperation)op).updateCurrentTuple();
            return theOp;
        }
        else {
            throw new NdbApiException("No underlying scan operation.");
        }
    }
    /**
     * @see com.mysql.cluster.ndbj.NdbResultSet#updateRow()
     */
    public NdbOperation getUpdateOperation(NdbTransaction trans)
        throws NdbApiException {
        if (!NdbScanOperationImpl.class.isInstance(op)) {

            throw new NdbApiException("NdbResultSet::updateRow(). "
                                      + "Not implemented for PK operations.");
        }
        else if (op != null) {
            NdbOperation theOp = ((NdbScanOperation)op)
                .updateCurrentTuple((NdbTransactionImpl)trans);
            return theOp;
        }
        else {
            throw new NdbApiException("No underlying scan operation.");
        }
    }

    /**
     * @see com.mysql.cluster.ndbj.NdbResultSet#wasNull()
     */
    @Override
    public boolean wasNull() throws NdbApiException {
        if (lastColumnNull == -1) {
            throw new NdbApiException("No Column Read Before Calling " +
                                      "NdbResultSet.wasNull()");
        }

        return (lastColumnNull > 0 ? true : false);
    }

    /**
     * This private method tests to see if the last column read was NULL
     * and sets a flag if true.
     * It is not thread-safe.
     * @param rec
     libndbclient-dev.links */
    private void testNull(NdbRecAttr rec) throws NdbApiException {
        if (rec == null) {
            throw new NdbApiException("Couldn't find NdbRecAttrImpl for " +
                                      "column. Have you called getXXX() " +
                                      " on the column?");
        } else if (rec.isNULL()==-1) {
            throw new NdbApiException("Record is not open for reading");
        } else if (rec.isNULL()==1) {
            lastColumnNull = 1;
        } else {
            lastColumnNull = 0;
        }
    }

    /* (non-Javadoc)
     * @see com.mysql.cluster.ndbj.ThrowingResultSet#getMetaData()
     */
    @Override
    public NdbResultSetMetaData getMetaData() throws SQLException {

        if (theMetaData == null) {
            theMetaData = new NdbResultSetMetaData(this);
        }
        return theMetaData;

    }
}

