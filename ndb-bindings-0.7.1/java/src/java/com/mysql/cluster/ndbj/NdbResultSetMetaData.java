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

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class NdbResultSetMetaData implements ResultSetMetaData {

    /* (non-Javadoc)
     * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
     */
    public boolean isWrapperFor(Class<?> arg0) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see java.sql.Wrapper#unwrap(java.lang.Class)
     */
    public <T> T unwrap(Class<T> arg0) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    NdbResultSetImpl theResultSet;

    protected NdbResultSetMetaData(NdbResultSetImpl resultSet) {
        theResultSet=resultSet;
    }

    public String getCatalogName(int arg0) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public String getColumnClassName(int arg0) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public int getColumnCount() throws SQLException {
        return theResultSet.resultSet.keySet().size();
    }

    public int getColumnDisplaySize(int arg0) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getColumnLabel(int arg0) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public String getColumnName(int arg0) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public int getColumnType(int arg0) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getColumnTypeName(int arg0) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public int getPrecision(int arg0) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getScale(int arg0) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getSchemaName(int arg0) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public String getTableName(int arg0) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isAutoIncrement(int arg0) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isCaseSensitive(int arg0) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isCurrency(int arg0) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isDefinitelyWritable(int arg0) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    public int isNullable(int arg0) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    public boolean isReadOnly(int arg0) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isSearchable(int arg0) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isSigned(int arg0) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isWritable(int arg0) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

}
