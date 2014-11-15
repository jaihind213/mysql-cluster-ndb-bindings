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

import java.sql.Timestamp;
import java.math.BigInteger; 


/**
 * NdbScanFilter provides an alternative means of specifying
 * filters for scan operations.
 *
 * <br>You must call methods in the following order to define a "Node"
 * in a tree of Scan Filters:
 * (1) call begin() method (to define a logical operation (AND, OR) on filters)
 * (2) define your filter using one of the cmp(), eq(), ne(), etc.
 * (3) call end() method
 *
 * You can nest "Nodes" (consisting of begin(), filters, end()) in logical
 * operations, e.g.:
 * <br>
 * <code>
 * <br>try {
 * <br>  NdbScanFilter sf1 = scanOp.getNdbScanFilter();
 * <br>  sf1.begin(NdbScanFilter.Group.OR); // Root
 * <br>  sf1.eq("colX", 33); //"Node 1"
 * <br>  sf1.eq("colY", 66); //"Node 2"
 * <br>  sf1.end();
 * <br>}
 * <br>catch (NdbApiException e) {...}
 * </code>
 */
public interface NdbScanFilter {


    /**
     * An NdbScanFilter can have different conditions added to it to build
     * a compound filter
     * <br> Conditions can be combined into a compound filter using the
     * following logical operations:
     * AND, OR, NAND and NOR.
     */
    public enum Group {
        AND(NdbjJNI.NdbScanFilterImpl_AND_get()),
        OR(NdbjJNI.NdbScanFilterImpl_OR_get()),
        NAND(NdbjJNI.NdbScanFilterImpl_NAND_get()),
        NOR(NdbjJNI.NdbScanFilterImpl_NOR_get());


        public final int swigValue() {
            return type;
        }

        private Group(int swigValue) {
            this.type = swigValue;
        }
        private final int type;

    }

    /**
     * BinaryCondition enum
     * 
     * <ul>
     * <li>COND_LE: condition less than or equal to (&lt;=)
     * <li>COND_LT: condition less than (&lt;)
     * <li>COND_GE: condition greater than or equal to (&gt;=)
     * <li>COND:GT: condition greater than (&gt;)
     * <li>COND_EQ: condition equal to (=)
     * <li>COND_NE: condition not equal to (!=)
     * <li>COND_LIKE: Pattern match for Strings using specified String.
     *            Similar to 'like' SQL operator, e.g.,
     * <code>
     *   SELECT * FROM TABLE where colX LIKE 'h%';
     * </code>
     * <br> This will match all rows where column, colX, has a String
     * starting with the letter 'h'.
     * <li>COND_NOT_LIKE: Negative pattern match for Strings
     * </ul>
     */
    public enum BinaryCondition {
        COND_LE(NdbjJNI.NdbScanFilterImpl_COND_LE_get()),
        COND_LT(NdbjJNI.NdbScanFilterImpl_COND_LT_get()),
        COND_GE(NdbjJNI.NdbScanFilterImpl_COND_GE_get()),
        COND_GT(NdbjJNI.NdbScanFilterImpl_COND_GT_get()),
        COND_EQ(NdbjJNI.NdbScanFilterImpl_COND_EQ_get()),
        COND_NE(NdbjJNI.NdbScanFilterImpl_COND_NE_get()),
        COND_LIKE(NdbjJNI.NdbScanFilterImpl_COND_LIKE_get()),
        COND_NOT_LIKE(NdbjJNI.NdbScanFilterImpl_COND_NOT_LIKE_get());

        public final int swigValue() {
            return type;
        }

        private BinaryCondition(int swigValue) {
            this.type = swigValue;
        }

        private final int type;

    }

    /**
     * The begin() method starts the definition of a scan filter.
     * Define your scan filters by calling methods such as cmp(),
     * eq() on your NdbScanFilter object.
     * Use end() method to define the end of a scan filter.
     * Note: if you call getValue() methods on the NdbScanOperation object
     * between calling begin() and end() on a NdbScanFilter, the getValue()
     * method will not have any effect. Make sure to call getValue() on your
     * NdbScanOperation object before you define any NdbScanFilter objects.
     *
     * Preconditions: Valid NdbScanOperation object
     *
     * @param group Grouping condition to apply to Filter conditions
     * @throws NdbApiException
     */
    public int begin(Group group) throws NdbApiException;

    /**
     * The begin() method starts the definition of a scan filter.
     * Define your scan filters by calling methods such as cmp(),
     * eq() on your NdbScanFilter object.
     * Use end() method to define the end of a scan filter.
     * Note: if you call getValue() methods on the NdbScanOperation object
     * between calling begin() and end() on a NdbScanFilter, the getValue()
     * method will not have any effect. Make sure to call getValue() on your
     * NdbScanOperation object before you define any NdbScanFilter objects.
     *
     * Preconditions: Valid NdbScanOperation object
     *
     * @throws NdbApiException
     */
    public int begin() throws NdbApiException;

    /**
     * Call end() method to finish the definition of a scan filter.
     * Preconditions: You must have called begin() method on the
     * NdbScanFilter object.
     * @throws NdbApiException
     */
    public int end() throws NdbApiException;

    /**
     * The compare filter for String values.
     * This method filters all rows using the column (specified by columnId)
     * and value (val of type String).
     * @param cond a BinaryCondition type
     * @param val
     * @return completion code from native library
     * @throws NdbApiException if the parameters are invalid,
     *         the column type is not VARBINARY or CHAR/VARCHAR
     * with character set utf-8 or latin-1.
     * @see BinaryCondition
     */
    public int cmpString(BinaryCondition cond, int columnId,
                                  String val)
        throws NdbApiException;

    /**
     *
     * @param cond
     * @param columnName
     * @param val
     * @return completion code from native library
     * @throws NdbApiException
     */
    public int cmpString(BinaryCondition cond, String columnName,
                                  String val)
        throws NdbApiException;

    /**
     * The compare filter for NULL values.
     * This method filters all rows using the column (specified by columnId)
     * and value (val of type String).
     * @param cond a BinaryCondition type
     * @return completion code from native library
     * @throws NdbApiException
     * @see BinaryCondition
     */
    public int cmp(BinaryCondition cond, int columnId)
        throws NdbApiException;

    /**
     *
     * @param cond
     * @param columnName
     * @return completion code from native library
     * @throws NdbApiException
     */
    public int cmp(BinaryCondition cond, String columnName)
        throws NdbApiException;

    /**
     * The compare filter for int values.
     * This method filters all rows using the column (specified by columnId)
     * and value (val of type int).
     * @param cond a BinaryCondition type
     * @param val
     * @return completion code from native library
     * @throws NdbApiException if the parameters are invalid,
     * @see BinaryCondition
     */
    public int cmp(BinaryCondition cond, int columnId, int val)
        throws NdbApiException;

    /**
     *
     * @param cond
     * @param columnName
     * @param val
     * @return completion code from native library
     * @throws NdbApiException
     */
    public int cmp(BinaryCondition cond, String columnName, int val)
        throws NdbApiException;

    /**
     * The compare filter for long values.
     * This method filters all rows using the column (specified by columnId)
     * and value (val of type long).
     * @param cond a BinaryCondition type
     * @param val
     * @return completion code from native library
     * @throws NdbApiException if the parameters are invalid,
     * @see BinaryCondition
     */
    public int cmp(BinaryCondition cond, int columnId, long val)
        throws NdbApiException;

    /**
     *
     * @param cond
     * @param columnName
     * @param val
     * @return completion code from native library
     * @throws NdbApiException
     */
    public int cmp(BinaryCondition cond, String columnName, long val)
        throws NdbApiException;

    /**
     * The compare filter for double values.
     * This method filters all rows using the column (specified by columnId)
     * and value (val of type double).
     * @param cond a BinaryCondition type
     * @param val
     * @return completion code from native library
     * @throws NdbApiException if the parameters are invalid,
     * @see BinaryCondition
     */
    public int cmp(BinaryCondition cond, int columnId, double val)
        throws NdbApiException;

    /**
     *
     * @param cond
     * @param columnName
     * @param val
     * @return completion code from native library
     * @throws NdbApiException
     */
    public int cmp(BinaryCondition cond, String columnName,
                            double val)
        throws NdbApiException;

    /**
     * The compare filter for float values.
     * This method filters all rows using the column (specified by columnId)
     * and value (val of type float).
     * @param cond a BinaryCondition type
     * @param val
     * @return completion code from native library
     * @throws NdbApiException if the parameters are invalid,
     * @see BinaryCondition
     */
    public int cmp(BinaryCondition cond, int columnId, float val)
        throws NdbApiException;

    /**
     *
     * @param cond
     * @param columnName
     * @param val
     * @return completion code from native library
     * @throws NdbApiException
     */
    public int cmp(BinaryCondition cond, String columnName, float val)
        throws NdbApiException;

    /**
     * The compare filter for int values.
     * This method filters all rows using the column (specified by columnId)
     * and value (val of type int).
     * @param cond a BinaryCondition type
     * @param val
     * @return completion code from native library
     * @throws NdbApiException if the parameters are invalid,
     * @see BinaryCondition
     */
    public int cmpTimestamp(BinaryCondition cond, int columnId,
                                     Timestamp val)
        throws NdbApiException;

    /**
     * TODO: Fix this naming problem
     * @param cond
     * @param columnName
     * @param val
     * @return completion code from native library
     * @throws NdbApiException
     */
    public int cmpTimestamp(BinaryCondition cond, String columnName,
                                     Timestamp val)
        throws NdbApiException;

    /**
     * The compare filter for int values.
     * This method filters all rows using the column (specified by columnId)
     * and value (val of type int).
     * @param cond a BinaryCondition type
     * @param val
     * @return completion code from native library
     * @throws NdbApiException if the parameters are invalid,
     * @see BinaryCondition
     */
    public int cmp(BinaryCondition cond, int columnId, Timestamp val)
        throws NdbApiException;

    /**
     *
     * @param cond
     * @param columnName
     * @param val
     * @return completion code from native library
     * @throws NdbApiException
     */
    public int cmp(BinaryCondition cond, String columnName,
                            Timestamp val)
        throws NdbApiException;

    /**
     * The compare filter for int values.
     * This method filters all rows using the column (specified by columnId)
     * and value (val of type int).
     * @param cond a BinaryCondition type
     * @param val
     * @return completion code from native library
     * @throws NdbApiException if the parameters are invalid,
     * @see BinaryCondition
     */
    public int cmp(BinaryCondition cond, int columnId, byte[] val)
        throws NdbApiException;

    /**
     *
     * @param cond
     * @param columnName
     * @param val
     * @return completion code from native library
     * @throws NdbApiException
     */
    public int cmp(BinaryCondition cond, String columnName,
                            byte[] val)
        throws NdbApiException;

    /**
     * The compare filter for int values.
     * This method filters all rows using the column (specified by columnId)
     * and value (val of type int).
     * @param cond a BinaryCondition type
     * @param val
     * @return completion code from native library
     * @throws NdbApiException if the parameters are invalid,
     * @see BinaryCondition
     */
    public int cmp(BinaryCondition cond, int columnId, BigInteger val)
        throws NdbApiException;

    /**
     *
     * @param cond
     * @param columnName
     * @param val
     * @return completion code from native library
     * @throws NdbApiException
     */
    public int cmp(BinaryCondition cond, String columnName,
                            BigInteger val)
        throws NdbApiException;

    /**
     * Filters rows returned from scan; returns rows where
     * the value of an entry in the supplied column number is
     * <b>equal</b> to the supplied int value.
     * @param columnId column number
     * @param value integer used to filter out rows from the scan
     * @throws NdbApiException if filtering failed in cluster
     * @throws NdbApiRuntimeException if the columnId is invalid
     */
    public int eq(int columnId, BigInteger value)
        throws NdbApiException;

    /**
     * Filters rows returned from scan; returns rows where
     * the value of an entry in the supplied column name is
     * <b>equal</b> to the supplied int value.
     *
     * @param columnName column name
     * @param value integer used to filter out rows from the scan
     * @throws NdbApiException if filtering failed in the cluster
     * @throws NdbApiRuntimeException if the column name is invalid
     */
    public int eq(String columnName, BigInteger value)
        throws NdbApiException;

    /**
     * Filters rows returned from scan; returns rows where
     * the supplied columnId is equal to the supplied long value.
     *
     * @param columnId column number
     * @param value long used to filter out rows from the scan
     * @throws NdbApiException if columnId is not defined, or if
     *         filtering failed in the cluster
     */
    public int eq(int columnId, long value) throws NdbApiException;

    /**
     * Filters rows returned from scan; returns rows where
     * the supplied column name is equal to the supplied long value.
     *
     * @param columnName the name of the column
     * @param value long used to filter out rows from the scan
     * @throws NdbApiException if filtering failed in the cluster.
     * @throws NdbApiRuntimeException if the column name is invalid
     */
    public int eq(String columnName, long value)
        throws NdbApiException;

    /**
     * Wrapper method to filter rows where columnId has VARCHAR,
     * VARBINARY or CHAR with supplied value value.
     * @param columnId column number
     * @param value String used to filter out rows from the scan
     * @throws NdbApiException if columnId is not defined, or if
     * filtering failed in the cluster
     */
    public int eq(int columnId, String value) throws NdbApiException;

    /**
     * TODO: Fix naming problem here
     * Wrapper method to filter rows where columnName has VARCHAR,
     * VARBINARY or CHAR with supplied value.
     *
     * @param columnName column name
     * @param value String used to filter out rows from the scan
     * @throws NdbApiException if filtering failed
     * @throws NdbApiRuntimeException if the column name is invalid
     */
    public int eq(String columnName, String value)
        throws NdbApiException;

    /**
     * Filters rows returned from scan; returns rows where values in
     * the supplied column number are <b>not equal</b> to the supplied value.
     *
     * @param columnId
     * @param value
     * @throws NdbApiException
     */
    public int ne(int columnId, BigInteger value)
        throws NdbApiException;

    /**
     * Filters rows returned from scan; returns rows where values in
     * the supplied column name are <b>not equal</b> to the supplied value.
     *
     * @param columnName the name of the column
     * @param value
     * @throws NdbApiException
     */
    public int ne(String columnName, BigInteger value)
        throws NdbApiException;

    /**
     * Filters rows returned from scan; returns rows where values in
     * the supplied column number are <b>not equal</b> to the supplied value.
     *
     * @param columnId
     * @param value
     * @throws NdbApiException
     */
    public int ne(int columnId, long value) throws NdbApiException;

    /**
     * Filters rows returned from scan; returns rows where values in
     * the supplied column name are <b>not equal</b> to the supplied value.
     *
     * @param columnName the name of the column
     * @param value
     * @throws NdbApiException
     */
    public int ne(String columnName, long value)
        throws NdbApiException;

    /**
     * Filters rows returned from scan; returns rows where values in
     * the supplied column number are <b>less than</b> to the supplied value.
     *
     * @param columnId
     * @param value
     * @throws NdbApiException
     */
    public int lt(int columnId, BigInteger value)
        throws NdbApiException;

    /**
     * Filters rows returned from scan; returns rows where values in
     * the supplied column name are <b>less than</b> to the supplied value.
     *
     * @param columnName the name of the column
     * @param value
     * @throws NdbApiException
     */
    public int lt(String columnName, BigInteger value)
        throws NdbApiException;

    /**
     * Filters rows returned from scan; returns rows where values in
     * the supplied column number are <b>less than</b> to the supplied value.
     *
     * @param columnId
     * @param value
     * @throws NdbApiException
     */
    public int lt(int columnId, long value) throws NdbApiException;

    /**
     * Filters rows returned from scan; returns rows where values in
     * the supplied column name are <b>less than</b> to the supplied value.
     *
     * @param columnName the name of the column
     * @param value
     * @throws NdbApiException
     */
    public int lt(String columnName, long value)
        throws NdbApiException;

    /**
     * Filters rows returned from scan; returns rows where values in
     * the supplied column number are <b>less than or equal to</b>
     * to the supplied value.
     *
     * @param columnId
     * @param value
     * @throws NdbApiException
     */
    public int le(int columnId, BigInteger value)
        throws NdbApiException;

    /**
     * Filters rows returned from scan; returns rows where values in
     * the supplied column name are <b>less than or equal to</b> to the
     * supplied value.
     *
     * @param columnName the name of the column
     * @param value
     * @throws NdbApiException
     */
    public int le(String columnName, BigInteger value)
        throws NdbApiException;

    /**
     * Filters rows returned from scan; returns rows where values in
     * the supplied column number are <b>less than or equal to</b> to
     * the supplied value.
     *
     * @param columnId
     * @param value
     * @throws NdbApiException
     */
    public int le(int columnId, long value) throws NdbApiException;

    /**
     * Filters rows returned from scan; returns rows where values in
     * the supplied column name are <b>less than or equal to</b> to the
     * supplied value.
     *
     * @param columnName the name of the column
     * @param value
     * @throws NdbApiException
     */
    public int le(String columnName, long value)
        throws NdbApiException;

    /**
     * Filters rows returned from scan; returns rows where values in
     * the supplied column number are <b>greater than</b> to the supplied
     * value.
     *
     * @param columnId
     * @param value
     * @throws NdbApiException
     */
    public int gt(int columnId, BigInteger value)
        throws NdbApiException;

    /**
     * Filters rows returned from scan; returns rows where values in
     * the supplied column name are <b>greater than</b> to the supplied value.
     *
     * @param columnName the name of the column
     * @param value
     * @throws NdbApiException
     */
    public int gt(String columnName, BigInteger value)
        throws NdbApiException;

    /**
     * Filters rows returned from scan; returns rows where values in
     * the supplied column number are <b>greater than</b> to the supplied
     * value.
     *
     * @param columnId
     * @param value
     * @throws NdbApiException
     */
    public int gt(int columnId, long value) throws NdbApiException;

    /**
     * Filters rows returned from scan; returns rows where values in
     * the supplied column name are <b>greater than</b> to the supplied value.
     *
     * @param columnName the name of the column
     * @param value
     * @throws NdbApiException
     */
    public int gt(String columnName, long value)
        throws NdbApiException;

    /**
     * Filters rows returned from scan; returns rows where values in
     * the supplied column number are <b>greater than or equal to</b> to the
     * supplied value.
     *
     * @param columnId
     * @param value
     * @throws NdbApiException
     */
    public int ge(int columnId, BigInteger value)
        throws NdbApiException;

    /**
     * Filters rows returned from scan; returns rows where values in
     * the supplied column name are <b>greater than or equal to</b> to
     * the supplied value.
     *
     * @param columnName the name of the column
     * @param value
     * @throws NdbApiException
     */
    public int ge(String columnName, BigInteger value)
        throws NdbApiException;

    /**
     * Filters rows returned from scan; returns rows where values in
     * the supplied column number are <b>greater than or equal to</b> to
     * the supplied value.
     *
     * @param columnId
     * @param value
     * @throws NdbApiException
     */
    public int ge(int columnId, long value) throws NdbApiException;

    /**
     * Filters rows returned from scan; returns rows where values in
     * the supplied column name are <b>greater than or equal to</b> to
     * the supplied value.
     *
     * @param columnName the name of the column
     * @param value
     * @throws NdbApiException
     */
    public int ge(String columnName, long value)
        throws NdbApiException;

    /**
     * Check if a column, idenitifed by the column id number, is null.
     * @param columnId column number
     * @return
     * true: if the value of columnName is Null;
     * <br>false: if the value of columnName is not Null;
     * @throws NdbApiException
     */
    public int isNull(int columnId) throws NdbApiException;

    /**
     * Check if a column, idenitifed by the column name, is null.
     *
     * @param columnName name of the column
     * @return
     * true: if the value of columnName is Null;
     * <br>false: if the value of columnName is not Null;
     * @throws NdbApiException if columnName is not defined for the
     * NdbScanOperation
     */
    public int isNull(String columnName) throws NdbApiException;

    /**
     * TODO: Fix Docs here, they are wrong
     * Check if a column, idenitifed by the column id number, is not null
     * @param columnId column number
     * @return
     * true: if the value of columnName is not Null;
     * <br>false: if the value of columnName is Null;
     * @throws NdbApiException if columnName is not defined for the
     * NdbScanOperation
     */
    public int isNotNull(int columnId) throws NdbApiException;

    /**
     * Check if a column, idenitifed by the column name, is not null
     *
     * @param columnName name of the column
     * @return
     * true: if the value of columnName is not Null;
     * <br>false: if the value of columnName is Null;
     * @throws NdbApiException if columnName is not defined for the
     * NdbScanOperation
     */
    public int isNotNull(String columnName) throws NdbApiException;

}
