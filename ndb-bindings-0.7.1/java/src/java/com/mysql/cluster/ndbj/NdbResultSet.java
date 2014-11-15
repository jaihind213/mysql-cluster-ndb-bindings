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


import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * NdbResultSet is an implementation of java.sql.ResultSet.
 *
 * <p>A NdbResultSet is used to store attributes in rows
 * that were selected after executing some query operation
 * on the cluster.
 * <br>Important: Id counts start at '0' (for the first column in a table).
 * <br>Be consistent in use of NdbResultSet objects.
 * Either access columns by name (String) or id (int).
 * Do not mix usage of access by name and id.
 */
public interface NdbResultSet extends java.sql.ResultSet
{

    /**
     * Get an Integer value the next entry in the NdbResultSet object.
     * @param columnId column number (starting from '0')
     * @return integer value
     * @throws NdbApiException if a value of this type cannot be retrieved
     */
    public int getInt(int columnId) throws NdbApiException;

    /**
     * Get an Integer value the next entry in the NdbResultSet object.
     * @param columnName
     * @return integer value
     * @throws NdbApiException if a value of this type cannot be retrieved
     */
    public int getInt(String columnName) throws NdbApiException;

    /**
     * Get a short value the next entry in the NdbResultSet object.
     * @param columnId column number (starting from '0')
     * @return short value
     * @throws NdbApiException if a value of this type cannot be retrieved
     */
    public short getShort(int columnId) throws NdbApiException;

    /**
     * Get a short value the next entry in the NdbResultSet object.
     * @param columnName
     * @return short value
     * @throws NdbApiException if a value of this type cannot be retrieved
     */
    public short getShort(String columnName) throws NdbApiException;

    /**
     * Get a short value the next entry in the NdbResultSet object.
     * @param columnId column number (starting from '0')
     * @return long value
     * @throws NdbApiException if a value of this type cannot be retrieved
     */
    public long getLong(int columnId) throws NdbApiException;

    /**
     *
     * @param columnName
     * @return long value
     * @throws NdbApiException if a value of this type cannot be retrieved
     */
    public long getLong(String columnName) throws NdbApiException;

    /**
     *
     * @param columnId column number (starting from '0')
     * @return float value
     * @throws NdbApiException if a value of this type cannot be retrieved
     */
    public float getFloat(int columnId) throws NdbApiException;

    /**
     *
     * @param columnName
     * @return float value
     * @throws NdbApiException if a value of this type cannot be retrieved
     */
    public float getFloat(String columnName) throws NdbApiException;

    /**
     *
     * @param columnId column number (starting from '0')
     * @return double value
     * @throws NdbApiException if a value of this type cannot be retrieved
     */
    public double getDouble(int columnId) throws NdbApiException;

    /**
     *
     * @param columnName
     * @return double value
     * @throws NdbApiException if a value of this type cannot be retrieved
     */
    public double getDouble(String columnName) throws NdbApiException;

    /**
     *
     * @param columnId column number (starting from '0')
     * @return a Timestamp object
     * @throws NdbApiException if a value of this type cannot be retrieved
     */
    public Timestamp getTimestamp(int columnId)
        throws NdbApiException;

    /**
     *
     * @param columnName
     * @return a Timestamp object
     * @throws NdbApiException if a value of this type cannot be retrieved
     */
    public Timestamp getTimestamp(String columnName)
        throws NdbApiException;

    /**
     *
     * @param columnId column number (starting from '0')
     * @return a byte array that is allocated in JNI. The caller owns the byte
     *         array, so does not need to make a copy of it.
     * @throws NdbApiException if a value of this type cannot be retrieved
     */
    public byte[] getBytes(int columnId) throws NdbApiException;

    /**
     * Returns a byte array for a specified column if it is in the result set.
     * @param columnName name of the column in the schema
     * @return a byte array that is allocated in JNI. The caller owns the byte
     * array, so does not need to make a copy of it.
     * @throws NdbApiException if a value of this type cannot be retrieved
     */
    public byte[] getBytes(String columnName) throws NdbApiException;

    /**
     *
     * @param columnId column number (starting from '0')
     * @return a byte array that is allocated in JNI. The caller owns the byte
     *         array, so does not need to make a copy of it.
     * @throws NdbApiException if a value of this type cannot be retrieved
     */
    public byte[] getStringBytes(int columnId) throws NdbApiException;

    /**
     * Returns a byte array for a specified column if it is in the result set.
     * @param columnName name of the column in the schema
     * @return a byte array that is allocated in JNI. The caller owns the byte
     * array, so does not need to make a copy of it.
     * @throws NdbApiException if a value of this type cannot be retrieved
     */
    public byte[] getStringBytes(String columnName) throws NdbApiException;

    /**
     *
     * @param columnId column number (starting from '0')
     * @return a String object
     * @throws NdbApiException if a value of this type cannot be retrieved
     */
    public String getString(int columnId) throws NdbApiException;

    /**
     *
     * @param columnName
     * @return a String object
     * @throws NdbApiException if a value of this type cannot be retrieved
     */
    public String getString(String columnName) throws NdbApiException;

    /**
     * Call the next() method to get the next entry in the NdbResultSet.
     * @return true if there is another value in the NdbResultSet,
     *         false if there are no more entries in the NdbResultSet.
     * @throws NdbApiException if a problem occurred when attempting
     *         to get the next row.
     */
    public boolean next() throws NdbApiException;

    /**
     * Call the next() method to get the next entry in the NdbResultSet.
     * @param fetchAllowed Make a round trip to the data nodes for more
     *        data if necessary
     * @return true if there is another value in the NdbResultSet,
     *         false if there are no more entries in the NdbResultSet.
     * @throws NdbApiException if a problem occurred when attempting
     *         to get the next row.
     */
    public boolean next(boolean fetchAllowed) throws NdbApiException;

    /**
     * Deletes the next row in the NdbResultSet.
     * @throws NdbApiException if a problem occurred when attempting
     *         to delete the row.
     */
    public void deleteRow() throws NdbApiException;

    /**
     * Updates the next row in the NdbResultSet.
     * @return a NdbOperation object
     * @throws NdbApiException  if a problem occurred when attempting
     *         to update the row.
     */
    public NdbOperation getUpdateOperation() throws NdbApiException;

    /**
     * Updates the next row in the NdbResultSet.
     * @return a NdbOperation object
     * @throws NdbApiException  if a problem occurred when attempting
     *         to update the row.
     */
    public NdbOperation getUpdateOperation(NdbTransaction trans)
        throws NdbApiException;

    /**
     * Reports whether the last column read had a value of SQL NULL.
     * Note that you must first call one of the getter methods
     * (e.g., getInt(..)) on a column to
     * on the NdbResultSet (e.g., rs.getString() ) try to read its value and
     * then call the method wasNull to see if the value read was SQL NULL
     * @return true if the last column read was null (SQL NULL),
     *         false if it was not null  (SQL NULL).
     * @throws NdbApiException if a problem occurred, such as not having
     *         called get&lt;TYPE&gt;() on the NdbResultSet object before
     *         calling the wasNull() method.
     */
    public boolean wasNull() throws NdbApiException;

    public NdbBlob getBlob(int columnId) throws NdbApiException;

    public NdbBlob getBlob(String columnName) throws NdbApiException;

    public BigDecimal getDecimal(int columnId) throws NdbApiException;

    public BigDecimal getDecimal(String columnName) throws NdbApiException;

}
