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
 * Mix-in class providing throwing stubs for JDBCv4 ResultSet extra methods.
 */
import java.io.Reader;
import java.sql.SQLException;
import java.sql.RowId;
import java.sql.SQLXML;
import java.sql.NClob;


public class ThrowingResultSetJDBC4 {

    public ThrowingResultSetJDBC4() {
        super();
    }

    public RowId getRowId(int arg0) throws SQLException {
        throw new NotImplementedException("getRowId");

    }

    public RowId getRowId(String arg0) throws SQLException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("getRowId");

    }

    public SQLXML getSQLXML(int arg0) throws SQLException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("getSQLXML");

    }

    public SQLXML getSQLXML(String arg0) throws SQLException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("getSQLXML");

    }

    public void updateNCharacterStream(int arg0, Reader arg1)
        throws SQLException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("updateNCharacterStream");

    }

    public void updateNCharacterStream(String arg0, Reader arg1)
        throws SQLException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("updateNCharacterStream");

    }

    public void updateNClob(int arg0, NClob arg1) throws SQLException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("updateNClob");

    }

    public void updateNClob(int arg0, Reader arg1) throws SQLException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("updateNClob");

    }

    public void updateNClob(String arg0, NClob arg1) throws SQLException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("updateNClob");

    }

    public void updateNClob(String arg0, Reader arg1) throws SQLException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("updateNClob");

    }

    public void updateRowId(int arg0, RowId arg1) throws SQLException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("updateRowId");

    }

    public void updateRowId(String arg0, RowId arg1) throws SQLException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("updateRowId");

    }

    public void updateSQLXML(int arg0, SQLXML arg1) throws SQLException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("updateSQLXML");

    }

    public void updateSQLXML(String arg0, SQLXML arg1) throws SQLException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("updateSQLXML");

    }

    public NClob getNClob(int arg0) throws SQLException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("getNClob");

    }

    public NClob getNClob(String arg0) throws SQLException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("getNClob");

    }

}
