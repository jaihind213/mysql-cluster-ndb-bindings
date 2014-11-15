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
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * NdbBaseOperationEquals defines equality methods, known in SQL as "where equals".
 * NdbBaseOperationEquals is implemented by NdbOperation (Primary key operations), and NdbIndexScanOperation (Ordered Index) interfaces.
 * <br>An equality operation is a match on a primary key or an index.
 * <p>In the following trivial SQL example, the column value in table my_table
 * can be a primary key or an index defined on the table.
 * The equal methods defined in this interface correspond to this SQL "where equals":
 * <br>
 * <code>
 *   SELECT * FROM my_table where value='X';
 * </code>
 *
 * @see NdbOperation
 * @see NdbIndexScanOperation
 */
public interface NdbBaseOperationEquals extends NdbBaseOperationResults {

    /**
     * 	This method defines a search condition for a tuple using equality, i.e., a row WHERE
     *  the value of its attribute in position columnId is equal to the user-supplied 'val'.
     *  To set search conditions on multiple columns, use multiple calls to equal() for each of the columns;
     *  in such cases all of them must be satisfied for the tuple to be selected.
     * @param columnId integer position (offset) of column number in schema definition (columnId starts from  position '1' for the first column in a schema)
     * @param val the equality value for the column (i.e., 'val' in the SQL  "WHERE column='val')
     * @throws NdbApiException if there was a problem in the cluster when trying to find tuples using this column.
     * @throws NdbApiRuntimeException If a bad columnName is entered
     */
    public void equalInt(long columnId, int val) throws NdbApiException;

    /**
     * 	This method defines a search condition for a tuple using equality, i.e., a row WHERE
     *  the value of its attribute in position columnId is equal to the user-supplied 'val'.
     *  To set search conditions on multiple columns, use multiple calls to equal() for each of the columns;
     *  in such cases all of them must be satisfied for the tuple to be selected.
     * @param columnId integer position (offset) of column number in schema definition (columnId starts from  position '1' for the first column in a schema)
     * @param val the equality value for the column (i.e., 'val' in the SQL  "WHERE column='val')
     * @throws NdbApiException if there was a problem in the cluster when trying to find tuples using this column.
     * @throws NdbApiRuntimeException If a bad columnName is entered
     * @deprecated Use equalInt instead
     */
    @Deprecated
    public void equal(long columnId, int val) throws NdbApiException;

    /**
     * 	This method defines a search condition for a tuple using equality, i.e., a row where
     *  the value of its attribute with name columnName is equal to the user-supplied 'val'.
     *  To set search conditions on multiple attributes, use several calls to equal();
     *  in such cases all of them must be satisfied for the tuple to be selected.
     *
     * @param columnName name of the Column in the Schema
     * @param val the equality value for the column (i.e., 'val' in the SQL  "WHERE column='val')
     * @throws NdbApiException if there was a problem in the cluster when trying to find tuples using this column.
     * @throws NdbApiRuntimeException If a bad columnName is entered
     */
    public void equalInt(String columnName, int val) throws NdbApiException;

    /**
     * 	This method defines a search condition for a tuple using equality, i.e., a row where
     *  the value of its attribute with name columnName is equal to the user-supplied 'val'.
     *  To set search conditions on multiple attributes, use several calls to equal();
     *  in such cases all of them must be satisfied for the tuple to be selected.
     *
     * @param columnName name of the Column in the Schema
     * @param val the equality value for the column (i.e., 'val' in the SQL  "WHERE column='val')
     * @throws NdbApiException if there was a problem in the cluster when trying to find tuples using this column.
     * @throws NdbApiRuntimeException If a bad columnName is entered
     * @deprecated Use equalInt instead
     */
    @Deprecated
    public void equal(String columnName, int val) throws NdbApiException;
    /**
     * 	This method defines a search condition for a tuple using equality, i.e., a row where
     *  the value of its attribute in position columnId is equal to the user-supplied 'val'.
     *  To set search conditions on multiple attributes, use several calls to equal();
     *  in such cases all of them must be satisfied for the tuple to be selected.
     *
     * @param columnId integer position (offset) of column number in schema definition (columnId starts from  position '1' for the first column in a schema)
     * @param val the equality value for the column (i.e., 'val' in the SQL  "WHERE column='val')
     * @throws NdbApiException if there was a problem in the cluster when trying to find tuples using this column.
     * @throws NdbApiRuntimeException If a bad columnName is entered
     */
    public void equalString(long columnId, String val) throws NdbApiException;
    /**
     * 	This method defines a search condition for a tuple using equality, i.e., a row where
     *  the value of its attribute in position columnId is equal to the user-supplied 'val'.
     *  To set search conditions on multiple attributes, use several calls to equal();
     *  in such cases all of them must be satisfied for the tuple to be selected.
     *
     * @param columnId integer position (offset) of column number in schema definition (columnId starts from  position '1' for the first column in a schema)
     * @param val the equality value for the column (i.e., 'val' in the SQL  "WHERE column='val')
     * @throws NdbApiException if there was a problem in the cluster when trying to find tuples using this column.
     * @throws NdbApiRuntimeException If a bad columnName is entered
     * @deprecated Use equalString instead
     */
    @Deprecated
    public void equal(long columnId, String val) throws NdbApiException;

    /**
     * 	This method defines a search condition for a tuple using equality, i.e., a row where
     *  the value of its attribute with name columnName is equal to the user-supplied 'val'.
     *  To set search conditions on multiple attributes, use several calls to equal();
     *  in such cases all of them must be satisfied for the tuple to be selected.
     *
     * <p>The column can be of type VARCHAR, CHAR, VARBINARY or BINARY.
     * If it is of type VARCHAR or CHAR, the supported CHARSETs are Latin1 or utf8.
     * <br>Check the string column's CHARSET using the mysql client
     * <br>
     * >show create table MyTable;
     * <br>
     * This method is slightly lower performance than the equalUtf8 and equalLatin1 methods.
     * It uses a cached table object (that may need to be loaded from cluster, first time it is used
     * or whenever the table schema is changed) to check the column CHARSET type.
     *
     * @param columnName name of the Column in the Schema
     * @param val the equality value for the column (i.e., 'val' in the SQL  "WHERE column='val')
     * @throws NdbApiException if there was a problem in the cluster when trying to find tuples using this column.
     * @throws NdbApiRuntimeException If a bad columnName is entered
     */
    public void equalString(String columnName, String val) throws NdbApiException;
    /**
     * 	This method defines a search condition for a tuple using equality, i.e., a row where
     *  the value of its attribute with name columnName is equal to the user-supplied 'val'.
     *  To set search conditions on multiple attributes, use several calls to equal();
     *  in such cases all of them must be satisfied for the tuple to be selected.
     *
     * <p>The column can be of type VARCHAR, CHAR, VARBINARY or BINARY.
     * If it is of type VARCHAR or CHAR, the supported CHARSETs are Latin1 or utf8.
     * <br>Check the string column's CHARSET using the mysql client
     * <br>
     * >show create table MyTable;
     * <br>
     * This method is slightly lower performance than the equalUtf8 and equalLatin1 methods.
     * It uses a cached table object (that may need to be loaded from cluster, first time it is used
     * or whenever the table schema is changed) to check the column CHARSET type.
     *
     * @param columnName name of the Column in the Schema
     * @param val the equality value for the column (i.e., 'val' in the SQL  "WHERE column='val')
     * @throws NdbApiException if there was a problem in the cluster when trying to find tuples using this column.
     * @throws NdbApiRuntimeException If a bad columnName is entered
     * @deprecated Use equalString instead
     */
    @Deprecated
    public void equal(String columnName, String val) throws NdbApiException;


    /**
     *
     * @param columnId integer position (offset) of column number in schema definition (columnId starts from  position '1' for the first column in a schema)
     * @param val the equality value for the column (i.e., 'val' in the SQL  "WHERE column='val')
     * @throws NdbApiException if there was a problem in the cluster when trying to find tuples using this column.
     * @throws NdbApiRuntimeException If a bad columnName is entered
     */
    public void equalBytes(long columnId, byte[] val) throws NdbApiException;
    @Deprecated
    public void equal(long columnId, byte[] val) throws NdbApiException;

    /**
     *
     * @param columnName name of the Column in the Schema
     * @param val the equality value for the column (i.e., 'val' in the SQL  "WHERE column='val')
     * @throws NdbApiException if there was a problem in the cluster when trying to find tuples using this column.
     * @throws NdbApiRuntimeException If a bad columnName is entered
     */
    public void equalBytes(String columnName, byte[] val) throws NdbApiException;
    @Deprecated
    public void equal(String columnName, byte[] val) throws NdbApiException;

    /**
     *
     * @param columnName name of the Column in the Schema
     * @param val the equality value for the column (i.e., 'val' in the SQL  "WHERE column='val')
     * @throws NdbApiException if there was a problem in the cluster when trying to find tuples using this column.
     * @throws NdbApiRuntimeException If a bad columnName is entered
     */
    public void equalLong(String columnName, long val) throws NdbApiException;
    @Deprecated
    public void equal(String columnName, long val) throws NdbApiException;

    /**
     *
     * @param columnId integer position (offset) of column number in schema definition (columnId starts from  position '1' for the first column in a schema)
     * @param val the equality value for the column (i.e., 'val' in the SQL  "WHERE column='val')
     * @throws NdbApiException if there was a problem in the cluster when trying to find tuples using this column.
     * @throws NdbApiRuntimeException If a bad columnName is entered
     */
    public void equalLong(long columnId, long val) throws NdbApiException;
    @Deprecated
    public void equal(long columnId, long val) throws NdbApiException;
    /**
     *
     * @param columnName name of the Column in the Schema
     * @param val the equality value for the column (i.e., 'val' in the SQL  "WHERE column='val')
     * @throws NdbApiException if there was a problem in the cluster when trying to find tuples using this column.
     * @throws NdbApiRuntimeException If a bad columnName is entered
     */
    public void equalUint(String columnName, long val) throws NdbApiException;

    /**
     *
     * @param columnId integer position (offset) of column number in schema definition (columnId starts from  position '1' for the first column in a schema)
     * @param val the equality value for the column (i.e., 'val' in the SQL  "WHERE column='val')
     * @throws NdbApiException if there was a problem in the cluster when trying to find tuples using this column.
     * @throws NdbApiRuntimeException If a bad columnName is entered
     */
    public void equalUint(long columnId, long val) throws NdbApiException;


    /**
     *
     * @param columnName name of the Column in the Schema
     * @param val the equality value for the column (i.e., 'val' in the SQL  "WHERE column='val')
     * @throws NdbApiException if there was a problem in the cluster when trying to find tuples using this column.
     * @throws NdbApiRuntimeException If a bad columnName is entered
     */
    public void equalUlong(String columnName, BigInteger val) throws NdbApiException;
    @Deprecated
    public void equal(String columnName, BigInteger val) throws NdbApiException;

    /**
     *
     * @param columnId integer position (offset) of column number in schema definition (columnId starts from  position '1' for the first column in a schema)
     * @param val the equality value for the column (i.e., 'val' in the SQL  "WHERE column='val')
     * @throws NdbApiException if there was a problem in the cluster when trying to find tuples using this column.
     * @throws NdbApiRuntimeException If a bad columnName is entered
     */
    public void equalUlong(long columnId, BigInteger val) throws NdbApiException;
    @Deprecated
    public void equal(long columnId, BigInteger val) throws NdbApiException;


    /**
     *
     * @param columnName name of the Column in the Schema
     * @param val the equality value for the column (i.e., 'val' in the SQL  "WHERE column='val')
     * @throws NdbApiException if there was a problem in the cluster when trying to find tuples using this column.
     * @throws NdbApiRuntimeException If a bad columnName is entered
     */
    public void equalTimestamp(String columnName, Timestamp val)	throws NdbApiException;
    @Deprecated
    public void equal(String columnName, Timestamp val)	throws NdbApiException;	/**
                                                                                 *
                                                                                 * @param columnId integer position (offset) of column number in schema definition (columnId starts from  position '1' for the first column in a schema)
                                                                                 * @param val the equality value for the column (i.e., 'val' in the SQL  "WHERE column='val')
                                                                                 * @throws NdbApiException if there was a problem in the cluster when trying to find tuples using this column.
                                                                                 * @throws NdbApiRuntimeException If a bad columnName is entered
                                                                                 */
    public void equalTimestamp(long columnId, Timestamp  val) throws NdbApiException;
    @Deprecated
    public void equal(long columnId, Timestamp  val) throws NdbApiException;
    /**
     *
     * @param columnName name of the Column in the Schema
     * @param val the equality value for the column (i.e., 'val' in the SQL  "WHERE column='val')
     * @throws NdbApiException if there was a problem in the cluster when trying to find tuples using this column.
     * @throws NdbApiRuntimeException If a bad columnName is entered
     */
    public void equalDatetime(String columnName, Timestamp val)	throws NdbApiException;

    /**
     *
     * @param columnId integer position (offset) of column number in schema definition (columnId starts from  position '1' for the first column in a schema)
     * @param val the equality value for the column (i.e., 'val' in the SQL  "WHERE column='val')
     * @throws NdbApiException if there was a problem in the cluster when trying to find tuples using this column.
     * @throws NdbApiRuntimeException If a bad columnName is entered
     */
    public void equalDatetime(long columnId, Timestamp val) throws NdbApiException;
    /**
     *
     * @param columnName name of the Column in the Schema
     * @throws NdbApiException if there was a problem in the cluster when trying to find tuples using this column.
     * @throws NdbApiRuntimeException If a bad columnName is entered
     */
    public void equalNull(String columnName)	throws NdbApiException;


    /**
     *
     * @param columnId integer position (offset) of column number in schema definition (columnId starts from  position '1' for the first column in a schema)
     * @throws NdbApiException if there was a problem in the cluster when trying to find tuples using this column.
     * @throws NdbApiRuntimeException If a bad columnName is entered
     */
    public void equalNull(long columnId) throws NdbApiException;
    public void equalShort(String anAttrName, short theValue) throws NdbApiException;
    public void equalShort(long anAttrId, short theValue) throws NdbApiException;

    public void equalDecimal(NdbColumn theColumn, BigDecimal decVal) throws NdbApiException;
    public void equalDecimal(String anAttrName, BigDecimal decVal) throws NdbApiException;
    public void equalDecimal(long anAttrId, BigDecimal decVal) throws NdbApiException;
}
