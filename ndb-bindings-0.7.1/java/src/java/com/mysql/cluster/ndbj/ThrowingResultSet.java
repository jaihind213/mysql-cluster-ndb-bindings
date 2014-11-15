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
 * Mix-in class providing throwing stubs for JDBCv3 ResultSet methods.
 */

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

public class ThrowingResultSet
    extends ThrowingResultSetJDBC4
    implements ResultSet {


    public void updateAsciiStream(int arg0, InputStream arg1)
        throws SQLException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("updateAsciiStream");
    }

    public void updateAsciiStream(String arg0, InputStream arg1)
        throws SQLException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("updateAsciiStream");
    }

    public void updateBinaryStream(int arg0, InputStream arg1)
        throws SQLException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("updateBinaryStream");
    }

    public void updateBinaryStream(String arg0, InputStream arg1)
        throws SQLException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("updateBinaryStream");
    }

    public void updateBlob(int arg0, InputStream arg1) throws SQLException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("updateBlob");
    }

    public void updateBlob(String arg0, InputStream arg1) throws SQLException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("updateBlob");
    }

    public void updateCharacterStream(int arg0, Reader arg1)
        throws SQLException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("updateCharacterStream");
    }

    public void updateCharacterStream(String arg0, Reader arg1)
        throws SQLException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("updateCharacterStream");
    }

    public void updateClob(int arg0, Reader arg1) throws SQLException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("updateClob");
    }

    public void updateClob(String arg0, Reader arg1) throws SQLException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("updateClob");
    }

    public boolean isWrapperFor(Class<?> arg0) throws SQLException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("isWrapperFor");
    }

    public <T> T unwrap(Class<T> arg0) throws SQLException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("unwrap");
    }

    public boolean absolute(int row) throws SQLException {
        throw new NotImplementedException(new Integer(row)).asSQL();
    }

    public void afterLast() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public void beforeFirst() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public void cancelRowUpdates() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public void clearWarnings() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public void close() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public void deleteRow() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public int findColumn(String columnName) throws SQLException {
        throw new NotImplementedException(columnName).asSQL();
    }

    public boolean first() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public Array getArray(int columnIndex) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex)).asSQL();
    }

    public Array getArray(String columnName) throws SQLException {
        throw new NotImplementedException(columnName).asSQL();
    }

    public InputStream getAsciiStream(int column) throws SQLException {
        throw new NotImplementedException(new Integer(column)).asSQL();
    }

    public InputStream getAsciiStream(String columnName) throws SQLException {
        throw new NotImplementedException(columnName).asSQL();
    }

    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex)).asSQL();
    }

    public BigDecimal getBigDecimal(String columnName) throws SQLException {
        throw new NotImplementedException(columnName).asSQL();
    }

    @Deprecated
    public BigDecimal getBigDecimal(int columnIndex, int scale)
        throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex),
                                          new Integer(scale)).asSQL();
    }

    @Deprecated
    public BigDecimal getBigDecimal(String columnName, int scale)
        throws SQLException {
        throw new NotImplementedException(columnName, new Integer(scale))
            .asSQL();
    }

    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex)).asSQL();
    }

    public InputStream getBinaryStream(String columnName) throws SQLException {
        throw new NotImplementedException(columnName).asSQL();
    }

    public Blob getBlob(int columnIndex) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex)).asSQL();
    }

    public Blob getBlob(String columnName) throws SQLException {
        throw new NotImplementedException(columnName).asSQL();
    }

    public boolean getBoolean(int columnName) throws SQLException {
        throw new NotImplementedException(new Integer(columnName)).asSQL();
    }

    public boolean getBoolean(String columnName) throws SQLException {
        throw new NotImplementedException(columnName).asSQL();
    }

    public byte getByte(int columnIndex) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex)).asSQL();
    }

    public byte getByte(String columnName) throws SQLException {
        throw new NotImplementedException(columnName).asSQL();
    }

    public byte[] getBytes(int columnIndex) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex)).asSQL();
    }

    public byte[] getBytes(String columnName) throws SQLException {
        throw new NotImplementedException(columnName).asSQL();
    }
    
    public byte[] getStringBytes(int columnIndex) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex)).asSQL();
    }

    public byte[] getStringBytes(String columnName) throws SQLException {
        throw new NotImplementedException(columnName).asSQL();
    }

    public Reader getCharacterStream(int columnIndex) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex)).asSQL();
    }

    public Reader getCharacterStream(String columnName) throws SQLException {
        throw new NotImplementedException(columnName).asSQL();
    }

    public Clob getClob(int columnIndex) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex)).asSQL();
    }

    public Clob getClob(String columnName) throws SQLException {
        throw new NotImplementedException(columnName).asSQL();
    }

    public int getConcurrency() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public String getCursorName() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public Date getDate(int columnIndex) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex)).asSQL();
    }

    public Date getDate(String columnName) throws SQLException {
        throw new NotImplementedException(columnName).asSQL();
    }

    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex), cal)
            .asSQL();
    }

    public Date getDate(String columnName, Calendar cal) throws SQLException {
        throw new NotImplementedException(columnName, cal).asSQL();
    }

    public double getDouble(int columnIndex) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex)).asSQL();
    }

    public double getDouble(String columnName) throws SQLException {
        throw new NotImplementedException(columnName).asSQL();
    }

    public BigDecimal getDecimal(int columnIndex) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex)).asSQL();
    }

    public BigDecimal getDecimal(String columnName) throws SQLException {
        throw new NotImplementedException(columnName).asSQL();
    }

    public int getFetchDirection() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public int getFetchSize() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public float getFloat(int columnIndex) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex)).asSQL();
    }

    public float getFloat(String columnName) throws SQLException {
        throw new NotImplementedException(columnName).asSQL();
    }

    public int getInt(int columnIndex) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex)).asSQL();
    }

    public int getInt(String columnName) throws SQLException {
        throw new NotImplementedException(columnName).asSQL();
    }

    public long getLong(int columnIndex) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex)).asSQL();
    }

    public long getLong(String columnName) throws SQLException {
        throw new NotImplementedException(columnName).asSQL();
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public Object getObject(int columnIndex) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex)).asSQL();
    }

    public Object getObject(String columnName) throws SQLException {
        throw new NotImplementedException(columnName).asSQL();
    }

    public Ref getRef(int columnIndex) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex)).asSQL();
    }

    public Ref getRef(String columnName) throws SQLException {
        throw new NotImplementedException(columnName).asSQL();
    }

    public int getRow() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public short getShort(int columnIndex) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex)).asSQL();
    }

    public short getShort(String columnName) throws SQLException {
        throw new NotImplementedException(columnName).asSQL();
    }

    public Statement getStatement() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public String getString(int columnIndex) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex)).asSQL();
    }

    public String getString(String columnName) throws SQLException {
        throw new NotImplementedException(columnName).asSQL();
    }

    public Time getTime(int columnIndex) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex)).asSQL();
    }

    public Time getTime(String columnName) throws SQLException {
        throw new NotImplementedException(columnName).asSQL();
    }

    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex), cal)
            .asSQL();
    }

    public Time getTime(String columnName, Calendar cal) throws SQLException {
        throw new NotImplementedException(columnName, cal).asSQL();
    }

    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex)).asSQL();
    }

    public Timestamp getTimestamp(String columnName) throws SQLException {
        throw new NotImplementedException(columnName).asSQL();
    }

    public Timestamp getTimestamp(int columnIndex, Calendar cal)
        throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex), cal)
            .asSQL();
    }

    public Timestamp getTimestamp(String columnName, Calendar cal)
        throws SQLException {
        throw new NotImplementedException(columnName, cal).asSQL();
    }

    public int getType() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public URL getURL(int columnIndex) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex)).asSQL();
    }

    public URL getURL(String columnName) throws SQLException {
        throw new NotImplementedException(columnName).asSQL();
    }

    @Deprecated
    public InputStream getUnicodeStream(int columnIndex)
        throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex)).asSQL();
    }

    @Deprecated
    public InputStream getUnicodeStream(String columnName)
        throws SQLException {
        throw new NotImplementedException(columnName).asSQL();
    }

    public SQLWarning getWarnings() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public void insertRow() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public boolean isAfterLast() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public boolean isBeforeFirst() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public boolean isFirst() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public boolean isLast() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public boolean last() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public void moveToCurrentRow() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public void moveToInsertRow() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public boolean next() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public boolean previous() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public void refreshRow() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public boolean relative(int rows) throws SQLException {
        throw new NotImplementedException(new Integer(rows)).asSQL();
    }

    public boolean rowDeleted() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public boolean rowInserted() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public boolean rowUpdated() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public void setFetchDirection(int columnIndex) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex)).asSQL();
    }

    public void setFetchSize(int columnIndex) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex)).asSQL();
    }

    public void updateArray(int columnIndex, Array x) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex), x)
            .asSQL();
    }

    public void updateArray(String columnName, Array x) throws SQLException {
        throw new NotImplementedException(columnName, x).asSQL();
    }

    public void updateAsciiStream(int columnIndex, InputStream x, int length)
        throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex), x,
                                          new Integer(length)).asSQL();
    }

    public void updateAsciiStream(String columnName, InputStream x, int length)
        throws SQLException {
        throw new NotImplementedException(columnName, x, new Integer(length))
            .asSQL();
    }

    public void updateBigDecimal(int columnIndex, BigDecimal x)
        throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex), x)
            .asSQL();
    }

    public void updateBigDecimal(String columnName, BigDecimal x)
        throws SQLException {
        throw new NotImplementedException(columnName, x).asSQL();
    }

    public void updateBinaryStream(int columnIndex, InputStream x, int length)
        throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex), x,
                                          new Integer(length)).asSQL();
    }

    public void updateBinaryStream(String columnName, InputStream x,
                                   int length)
        throws SQLException {
        throw new NotImplementedException(columnName, x, new Integer(length))
            .asSQL();
    }

    public void updateBlob(int columnIndex, Blob x) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex), x)
            .asSQL();
    }

    public void updateBlob(String columnName, Blob x) throws SQLException {
        throw new NotImplementedException(columnName, x).asSQL();
    }

    public void updateBoolean(int columnIndex, boolean x)
        throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex), Boolean
                                          .valueOf(x)).asSQL();
    }

    public void updateBoolean(String columnName, boolean x)
        throws SQLException {
        throw new NotImplementedException(columnName, Boolean.valueOf(x))
            .asSQL();
    }

    public void updateByte(int columnIndex, byte x) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex),
                                          new Byte(x)).asSQL();
    }

    public void updateByte(String columnName, byte x) throws SQLException {
        throw new NotImplementedException(columnName, new Byte(x)).asSQL();
    }

    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex), x)
            .asSQL();
    }

    public void updateBytes(String columnName, byte[] x) throws SQLException {
        throw new NotImplementedException(columnName, x).asSQL();
    }

    public void updateCharacterStream(int columnIndex, Reader x, int length)
        throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex), x,
                                          new Integer(length)).asSQL();
    }

    public void updateCharacterStream(String columnName, Reader x, int length)
        throws SQLException {
        throw new NotImplementedException(columnName, x, new Integer(length))
            .asSQL();
    }

    public void updateClob(int columnIndex, Clob x) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex), x)
            .asSQL();
    }

    public void updateClob(String columnName, Clob x) throws SQLException {
        throw new NotImplementedException(columnName, x).asSQL();
    }

    public void updateDate(int columnIndex, Date x) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex), x)
            .asSQL();
    }

    public void updateDate(String columnName, Date x) throws SQLException {
        throw new NotImplementedException(columnName, x).asSQL();
    }

    public void updateDouble(int columnIndex, double x) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex),
                                          new Double(x)).asSQL();
    }

    public void updateDouble(String columnName, double x)
        throws SQLException {
        throw new NotImplementedException(columnName, new Double(x)).asSQL();
    }

    public void updateFloat(int columnIndex, float x) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex),
                                          new Float(x)).asSQL();
    }

    public void updateFloat(String columnName, float x) throws SQLException {
        throw new NotImplementedException(columnName, new Float(x)).asSQL();
    }

    public void updateInt(int columnIndex, int x) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex),
                                          new Integer(x)).asSQL();
    }

    public void updateInt(String columnName, int x) throws SQLException {
        throw new NotImplementedException(columnName, new Integer(x))
            .asSQL();
    }

    public void updateLong(int columnIndex, long x) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex),
                                          new Long(x)).asSQL();
    }

    public void updateLong(String columnName, long x) throws SQLException {
        throw new NotImplementedException(columnName, new Long(x)).asSQL();
    }

    public void updateNull(int columnIndex) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex)).asSQL();
    }

    public void updateNull(String columnName) throws SQLException {
        throw new NotImplementedException(columnName).asSQL();
    }

    public void updateObject(int columnIndex, Object x) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex), x)
            .asSQL();
    }

    public void updateObject(String columnName, Object x)
        throws SQLException {
        throw new NotImplementedException(columnName, x).asSQL();
    }

    public void updateObject(int columnIndex, Object x, int scale)
        throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex), x,
                                          new Integer(scale)).asSQL();
    }

    public void updateObject(String columnName, Object x, int scale)
        throws SQLException {
        throw new NotImplementedException(columnName, x, new Integer(scale))
            .asSQL();
    }

    public void updateRef(int columnIndex, Ref x) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex), x)
            .asSQL();
    }

    public void updateRef(String columnName, Ref x) throws SQLException {
        throw new NotImplementedException(columnName, x).asSQL();
    }

    public void updateRow() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public void updateShort(int columnIndex, short x) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex),
                                          new Short(x)).asSQL();
    }

    public void updateShort(String columnName, short x) throws SQLException {
        throw new NotImplementedException(columnName, new Short(x)).asSQL();
    }

    public void updateString(int columnIndex, String x) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex), x)
            .asSQL();
    }

    public void updateString(String columnName, String x)
        throws SQLException {
        throw new NotImplementedException(columnName, x).asSQL();
    }

    public void updateTime(int columnIndex, Time x) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex), x)
            .asSQL();
    }

    public void updateTime(String columnName, Time x) throws SQLException {
        throw new NotImplementedException(columnName, x).asSQL();
    }

    public void updateTimestamp(int columnIndex, Timestamp x)
        throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex), x)
            .asSQL();
    }

    public void updateTimestamp(String columnName, Timestamp x)
        throws SQLException {
        throw new NotImplementedException(columnName, x).asSQL();
    }

    public boolean wasNull() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    // @SuppressWarnings("unchecked")
    // public Object getObject(int columnIndex, Map x) throws SQLException {
    // throw new NotImplementedException(new Integer(columnIndex), x).asSQL();
    // }
    //
    // @SuppressWarnings("unchecked")
    // public Object getObject(String columnName, Map x) throws SQLException {
    // throw new NotImplementedException(columnName, x).asSQL();
    // }

    // --------------

    public int getHoldability() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex)).asSQL();
    }

    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        throw new NotImplementedException(columnLabel).asSQL();
    }

    public String getNString(int columnIndex) throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex)).asSQL();
    }

    public String getNString(String columnLabel) throws SQLException {
        throw new NotImplementedException(columnLabel).asSQL();
    }

    public Object getObject(int columnIndex, Map<String, Class<?>> map)
        throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex), map)
            .asSQL();
    }

    public Object getObject(String columnLabel, Map<String, Class<?>>map)
        throws SQLException {
        throw new NotImplementedException(columnLabel, map).asSQL();
    }

    public boolean isClosed() throws SQLException {
        throw new NotImplementedException().asSQL();
    }

    public void updateAsciiStream(int columnIndex, InputStream x, long length)
        throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex), x,
                                          new Long(length)).asSQL();
    }

    public void updateAsciiStream(String columnLabel, InputStream x,
                                  long length)
        throws SQLException {
        throw new NotImplementedException(columnLabel, x, new Long(length))
            .asSQL();
    }

    public void updateBinaryStream(int columnIndex, InputStream x, long length)
        throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex), x,
                                          new Long(length)).asSQL();
    }

    public void updateBinaryStream(String columnLabel, InputStream x,
                                   long length) throws SQLException {
        throw new NotImplementedException(columnLabel, x, new Long(length))
            .asSQL();
    }

    public void updateBlob(int columnIndex, InputStream inputStream,
                           long length)
        throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex),
                                          inputStream,
                                          new Long(length)).asSQL();
    }

    public void updateBlob(String columnLabel, InputStream inputStream,
                           long length) throws SQLException {
        throw new NotImplementedException(new Integer(columnLabel),
                                          inputStream,
                                          new Long(length)).asSQL();
    }

    public void updateCharacterStream(int columnIndex, Reader x, long length)
        throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex), x,
                                          new Long(length)).asSQL();
    }

    public void updateCharacterStream(String columnLabel, Reader reader,
                                      long length) throws SQLException {
        throw new NotImplementedException(columnLabel, reader,
                                          new Long(length)).asSQL();
    }

    public void updateClob(int columnIndex, Reader reader, long length)
        throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex), reader,
                                          new Long(length)).asSQL();
    }

    public void updateClob(String columnLabel, Reader reader, long length)
        throws SQLException {
        throw new NotImplementedException(columnLabel, reader,
                                          new Long(length)).asSQL();
    }

    public void updateNCharacterStream(int columnIndex, Reader x, long length)
        throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex), x,
                                          new Long(length)).asSQL();
    }

    public void updateNCharacterStream(String columnLabel, Reader reader,
                                       long length) throws SQLException {
        throw new NotImplementedException(columnLabel, reader,
                                          new Long(length)).asSQL();
    }

    public void updateNClob(int columnIndex, Reader reader, long length)
        throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex), reader,
                                          new Long(length)).asSQL();
    }

    public void updateNClob(String columnLabel, Reader reader, long length)
        throws SQLException {
        throw new NotImplementedException(columnLabel, reader,
                                          new Long(length)).asSQL();
    }

    public void updateNString(int columnIndex, String string)
        throws SQLException {
        throw new NotImplementedException(new Integer(columnIndex), string)
            .asSQL();
    }

    public void updateNString(String columnLabel, String string)
        throws SQLException {
        throw new NotImplementedException(columnLabel, string).asSQL();
    }

}
