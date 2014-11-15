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

import java.util.HashMap;
import java.util.Map;

public class NdbEventOperationResultsImpl
    extends NdbBaseOperationResultsImpl
    implements NdbEventOperationResults {

    /* (non-Javadoc)
     * @see com.mysql.cluster.ndbj.NdbOperationResultsImpl#getBlobHandle(long)
     */
    @Override
    public NdbBlob getBlobHandle(long columnId) throws NdbApiException {
        throw new RuntimeException("undefined class");
    }
    /* (non-Javadoc)
     * @see com.mysql.cluster.ndbj.NdbOperationResultsImpl#getBlobHandle(java.lang.String)
     */
    @Override
    public NdbBlob getBlobHandle(String columnName) throws NdbApiException {
        throw new RuntimeException("undefined class");
    }
    /* (non-Javadoc)
     * @see com.mysql.cluster.ndbj.NdbOperationResultsImpl#realGetValue(long)
     */
    @Override
    protected NdbRecAttr realGetValue(long columnId) throws NdbApiException {
        throw new RuntimeException("undefined class");
    }
    /* (non-Javadoc)
     * @see com.mysql.cluster.ndbj.NdbOperationResultsImpl#realGetValue(com.mysql.cluster.ndbj.NdbColumn)
     */
    @Override
    protected NdbRecAttr realGetValue(NdbColumn theColumn)
        throws NdbApiException {
        throw new RuntimeException("undefined class");
    }
    /* (non-Javadoc)
     * @see com.mysql.cluster.ndbj.NdbOperationResultsImpl#realGetValue(java.lang.String)
     */
    @Override
    protected NdbRecAttr realGetValue(String columnName)
        throws NdbApiException {
        throw new RuntimeException("undefined class");
    }

    protected Map<String,NdbRecAttr> preResultSet;
    protected Map<String,NdbBlob> preBlobResultSet;
    protected NdbRecAttr realGetPreValue(long columnId)
        throws NdbApiException {
        throw new RuntimeException("undefined class");
    }
    protected NdbRecAttr realGetPreValue(String columnName)
        throws NdbApiException {
        throw new RuntimeException("undefined class");
    }
    protected NdbBlob getPreBlobHandle(long columnId)
        throws NdbApiException {
        throw new RuntimeException("undefined class");
    }

    protected NdbBlob getPreBlobHandle(String columnName)
        throws NdbApiException{
        throw new RuntimeException("undefined class");
    }

    public NdbEventOperationResultsImpl() {
        super();
        preResultSet = new HashMap<String, NdbRecAttr>();
        preBlobResultSet = new HashMap<String,NdbBlob>();
    }
    /* (non-Javadoc)
     * @see com.mysql.cluster.ndbj.NdbEventOperationResults#getPreBlob(java.lang.String)
     */
    public void getPreBlob(String columnName) throws NdbApiException {
        NdbBlob theBlob = getPreBlobHandle(columnName);
        preBlobResultSet.put(columnName,theBlob);
    }

    /* (non-Javadoc)
     * @see com.mysql.cluster.ndbj.NdbEventOperationResults#getPreValue(java.lang.String)
     */
    public void getPreValue(String columnName) throws NdbApiException {

        NdbRecAttr attr = realGetPreValue(columnName);
        resultSet.put(columnName, attr);
    }

    public NdbResultSet preResultData() {
        return new NdbResultSetImpl((NdbOperation)this, preResultSet);
    }
}
