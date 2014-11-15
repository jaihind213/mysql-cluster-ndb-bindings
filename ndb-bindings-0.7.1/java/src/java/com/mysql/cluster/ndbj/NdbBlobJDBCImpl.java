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

import java.sql.Blob;
import java.sql.Clob;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.io.OutputStream;
import java.math.BigInteger;

public abstract class NdbBlobJDBCImpl {

    public abstract long readData(byte[] BYTE, long len)
        throws NdbApiException;
    public abstract BigInteger getLength() throws NdbApiException;

    private byte[] theBlob;

    public void getValue(int length) {
        theBlob = new byte[length];
    }

    public byte[] getData() throws NdbApiException {

        // TODO: Dear god someone fix the blobs
        this.length();
        if ( theBlob == null) {
            theBlob = new byte[(int)this.length()];
        }
        this.readData(theBlob, this.length());
        return theBlob;

    }

    /*
     * (non-Javadoc) *
     *
     * @see java.sql.Blob#free()
     */
    public void free() throws NdbApiException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Blob#getBinaryStream(long, long)
     */
    public InputStream getBinaryStream(long arg0, long arg1)
        throws NdbApiException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Clob#getCharacterStream(long, long)
     */
    public Reader getCharacterStream(long arg0, long arg1)
        throws NdbApiException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Clob#getAsciiStream()
     */
    public InputStream getAsciiStream() throws NdbApiException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Clob#getCharacterStream()
     */
    public Reader getCharacterStream() throws NdbApiException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Clob#getSubString(long, int)
     */
    public String getSubString(long arg0, int arg1) throws NdbApiException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Clob#position(java.sql.Clob, long)
     */
    public long position(Clob arg0, long arg1) throws NdbApiException {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Clob#position(java.lang.String, long)
     */
    public long position(String arg0, long arg1) throws NdbApiException {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Clob#setAsciiStream(long)
     */
    public OutputStream setAsciiStream(long arg0) throws NdbApiException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Clob#setCharacterStream(long)
     */
    public Writer setCharacterStream(long arg0) throws NdbApiException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Clob#setString(long, java.lang.String)
     */
    public int setString(long arg0, String arg1) throws NdbApiException {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Clob#setString(long, java.lang.String, int, int)
     */
    public int setString(long arg0, String arg1, int arg2, int arg3)
        throws NdbApiException {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Blob#getBinaryStream()
     */
    public InputStream getBinaryStream() throws NdbApiException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Blob#getBytes(long, int)
     */
    public byte[] getBytes(long arg0, int arg1) throws NdbApiException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Blob#length()
     */
    public long length() throws NdbApiException {
        return this.getLength().longValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Blob#position(byte[], long)
     */
    public long position(byte[] arg0, long arg1) throws NdbApiException {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Blob#position(java.sql.Blob, long)
     */
    public long position(Blob arg0, long arg1) throws NdbApiException {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Blob#setBinaryStream(long)
     */
    public OutputStream setBinaryStream(long arg0) throws NdbApiException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Blob#setBytes(long, byte[])
     */
    public int setBytes(long arg0, byte[] arg1) throws NdbApiException {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Blob#setBytes(long, byte[], int, int)
     */
    public int setBytes(long arg0, byte[] arg1, int arg2, int arg3)
        throws NdbApiException {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.sql.Blob#truncate(long)
     */
    public void truncate(long arg0) throws NdbApiException {
        // TODO Auto-generated method stub

    }

}
