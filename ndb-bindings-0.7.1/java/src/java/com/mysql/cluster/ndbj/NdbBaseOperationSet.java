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
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public interface NdbBaseOperationSet {

    /**
     *
     * @param columnId integer position (offset) of column number in schema definition (columnId starts from  position '1' for the first column in a schema)
     * @param val
     * @throws NdbApiException if there is a problem in the cluster.
     */
    public void setShort(long columnId, short val)
        throws NdbApiException;

    /**
     *
     * @param columnName name of the Column in the Schema
     * @param val the value to be set for the column the value to be set for the column
     * @throws NdbApiException
     */
    public void setShort(String columnName, short val)
        throws NdbApiException;

    /**
     *
     * @param columnId integer position (offset) of column number in schema definition (columnId starts from  position '1' for the first column in a schema)
     * @param val
     * @throws NdbApiException if there is a problem in the cluster.
     */
    public void setInt(long columnId, int val) throws NdbApiException;

    /**
     *
     * @param columnName name of the Column in the Schema
     * @param val the value to be set for the column the value to be set for the column
     * @throws NdbApiException
     */
    public void setInt(String columnName, int val)
        throws NdbApiException;

    /**
     *
     * @param columnId integer position (offset) of column number in schema definition (columnId starts from  position '1' for the first column in a schema)
     * @param val the value to be set for the column
     * @throws NdbApiException if there was a problem in the cluster when trying to set a value for a column.
     */
    public void setLong(long columnId, long val)
        throws NdbApiException;

    /**
     *
     * @param columnName name of the Column in the Schema
     * @param val the value to be set for the column
     * @throws NdbApiException if there was a problem in the cluster when trying to set a value for a column.
     */
    public void setLong(String columnName, long val)
        throws NdbApiException;

    /**
     *
     * @param columnId integer position (offset) of column number in schema definition (columnId starts from  position '1' for the first column in a schema)
     * @param val the value to be set for the column
     * @throws NdbApiException if there was a problem in the cluster when trying to set a value for a column.
     */
    public void setFloat(long columnId, float val)
        throws NdbApiException;

    /**
     *
     * @param columnName name of the Column in the Schema
     * @param val the value to be set for the column
     * @throws NdbApiException if there was a problem in the cluster when trying to set a value for a column.
     */
    public void setFloat(String columnName, float val)
        throws NdbApiException;

    /**
     *
     * @param columnId integer position (offset) of column number in schema definition (columnId starts from  position '1' for the first column in a schema)
     * @param val the value to be set for the column
     * @throws NdbApiException if there was a problem in the cluster when trying to set a value for a column.
     */
    public void setDouble(long columnId, double val)
        throws NdbApiException;

    /**
     *
     * @param columnName name of the Column in the Schema
     * @param val the value to be set for the column
     * @throws NdbApiException if there was a problem in the cluster when trying to set a value for a column.
     */
    public void setDouble(String columnName, double val)
        throws NdbApiException;

    /**
     * Timestamp objects stored in the database lose nanosecond accuracy.
     * @param columnId integer position (offset) of column number in schema definition (columnId starts from  position '1' for the first column in a schema)
     * @param t if t is null, then setNull method is called for the column
     * @throws NdbApiException if there was a problem in the cluster when trying to set a value for a column.
     */
    public void setTimestamp(long columnId, Timestamp t)
        throws NdbApiException;
    public void setDatetime(long columnId, Timestamp t)
        throws NdbApiException;
    public void setDate(long columnId, Date t)
        throws NdbApiException;
    public void setTime(long columnId, Time t)
        throws NdbApiException;
    /**
     * Timestamp objects stored in the database lose nanosecond accuracy.
     * @param columnName name of the Column in the Schema
     * @param t the timestamp
     * @throws NdbApiException if there was a problem in the cluster when trying to set a value for a column. if columnName is null or empty string
     * @throws NdbApiException if Timestamp value is for year
     * after 2037
     */
    public void setTimestamp(String columnName, Timestamp t)
        throws NdbApiException;

	/** TODO docs for these */
    public void setDatetime(String columnName, Timestamp t)
        throws NdbApiException;
    public void setDate(String columnName, Date t)
        throws NdbApiException;
    public void setTime(String columnName, Time t)
        throws NdbApiException;
    /**
     *
     * @param columnId integer position (offset) of column number in schema definition (columnId starts from  position '1' for the first column in a schema)
     * @param val the value to be set for the column
     * @throws NdbApiException if there was a problem in the cluster when trying to set a value for a column.
     */
    public void setBytes(long columnId, byte[] val)
        throws NdbApiException;

    /**
     *
     * @param columnName name of the Column in the Schema
     * @param val the value to be set for the column
     * @throws NdbApiException if there was a problem in the cluster when trying to set a value for a column.
     */
    public void setBytes(String columnName, byte[] val)
        throws NdbApiException;

    /**
     * This method first queries the cluster about the charset of columnName,
     * before setting columnName to val.
     * The supported charsets are Latin1 and utf8. If columnName has
     * no charset, it is assume to be VARBINARY, and the charset is assumed to
     * be latin1.
     *
     * @param columnId name of the Column in the Schema
     * @param val the value to be set for the column
     * @throws NdbApiException if there was a problem in the cluster when trying to set a value for a column.
     */
    public void setString(long columnId, String val)
        throws NdbApiException;

    /**
     * This method first queries the cluster about the charsett of columnName,
     * before setting columnName to val.
     * The supported charsets are Latin1 and utf8. If columnName has
     * no charset, it is assume to be VARBINARY, and the charset is assumed to
     * be latin1.
     *
     * @param columnName name of the Column in the Schema
     * @param val the value to be set for the column
     * @throws NdbApiException if there was a problem in the cluster when trying to set a value for a column.
     */
    public void setString(String columnName, String val)
        throws NdbApiException;

    /**
     *
     * @param columnId integer position (offset) of column number in schema definition (columnId starts from  position '1' for the first column in a schema)
     * @throws NdbApiException if there was a problem in the cluster when trying to set a value for a column.
     */
    public void setNull(long columnId) throws NdbApiException;

    /**
     *
     * @param columnName name of the Column in the Schema
     * @throws NdbApiException if there was a problem in the cluster when trying to set a value for a column.
     */
    public void setNull(String columnName) throws NdbApiException;

    public void setDecimal(long columnId, BigDecimal decVal) throws NdbApiException;
    public void setDecimal(String columnName, BigDecimal decVal) throws NdbApiException;



}
