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

public abstract class NdbBaseOperationResultsImpl implements NdbBaseOperationResults {


    protected NdbResultSet theResultSet;
    protected Map<String,NdbRecAttr> resultSet;
    protected Map<String,NdbBlob> blobResultSet;

    protected abstract NdbRecAttr realGetValue(long columnId) throws NdbApiException;
    protected abstract NdbRecAttr realGetValue(String columnName) throws NdbApiException;
    protected abstract NdbRecAttr realGetValue(NdbColumn theColumn) throws NdbApiException;

    public abstract NdbBlob getBlobHandle(long columnId) throws NdbApiException;
    public abstract NdbBlob getBlobHandle(String columnName) throws NdbApiException;


    public NdbBaseOperationResultsImpl() {
        super();
        resultSet = new HashMap<String, NdbRecAttr>();
        blobResultSet = new HashMap<String,NdbBlob>();
        theResultSet = new NdbResultSetImpl();
    }

    public void getValue(long columnId) throws NdbApiException {

        Long id = new Long(columnId-1);
        NdbRecAttr attr = realGetValue(columnId);
        resultSet.put(id.toString(), attr);
    }

    /* (non-Javadoc)
     * @see com.mysql.cluster.ndbj.NdbOperation#getValue(java.lang.String)
     */
    public void getValue(String columnName) throws NdbApiException {

        NdbRecAttr attr = realGetValue(columnName);
        resultSet.put(columnName, attr);
    }

    public void getValue(NdbColumn theColumn) throws NdbApiException {
        NdbRecAttr attr = realGetValue(theColumn);
        resultSet.put(theColumn.getName(),attr);
    }
    public NdbResultSet resultData() {
        return new NdbResultSetImpl((NdbOperation)this, resultSet);
    }

    public void getBlob(long columnId) throws NdbApiException {

        Long id = new Long(columnId-1);
        NdbBlob theBlob = getBlobHandle(columnId);
        blobResultSet.put(id.toString(),theBlob);
    }
    public void getBlob(String columnName) throws NdbApiException {

        NdbBlob theBlob = getBlobHandle(columnName);
        blobResultSet.put(columnName,theBlob);
    }
    public void getBlob(long columnId, int length) throws NdbApiException {

        Long id = new Long(columnId-1);
        NdbBlob theBlob = getBlobHandle(columnId);
        theBlob.getValue(length);
        blobResultSet.put(id.toString(),theBlob);
    }
    public void getBlob(String columnName, int length) throws NdbApiException {

        NdbBlob theBlob = getBlobHandle(columnName);
        theBlob.getValue(length);
        blobResultSet.put(columnName,theBlob);
    }

    /**
     * getNdbBlobHandle is deprecated. Please use getBlobHandle instead
     * @param columnName
     * @return
     * @throws NdbApiException
     */
    @Deprecated
    public NdbBlob getNdbBlobHandle(String columnName) throws NdbApiException {
        return getBlobHandle(columnName);
    }

    /**
     * getNdbBlobHandle is deprecated. Please use getBlobHandle instead
     * @param columnName
     * @return
     * @throws NdbApiException
     */
    @Deprecated
    public NdbBlob getNdbBlobHandle(long columnId) throws NdbApiException {
        return getBlobHandle(columnId);
    }

}
