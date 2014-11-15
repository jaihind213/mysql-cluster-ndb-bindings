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

import java.math.BigInteger;
import java.sql.Timestamp;

public abstract class NdbBaseOperationEqualsImpl extends NdbBaseOperationResultsImpl implements NdbBaseOperationEquals {

    @Deprecated
    public void equal(long columnId, BigInteger val) throws NdbApiException {
        equalUlong(columnId,val);
    }

    @Deprecated
    public void equal(long columnId, byte[] val) throws NdbApiException {
        equalBytes(columnId,val);
    }

    @Deprecated
    public void equal(long columnId, int val) throws NdbApiException {
        equalInt(columnId,val);
    }

    @Deprecated
    public void equal(long columnId, long val) throws NdbApiException {
        equalLong(columnId,val);
    }

    @Deprecated
    public void equal(long columnId, String val) throws NdbApiException {
        equalString(columnId,val);
    }

    @Deprecated
    public void equal(long columnId, Timestamp val) throws NdbApiException {
        equalTimestamp(columnId,val);
    }

    @Deprecated
    public void equal(String columnName, BigInteger val) throws NdbApiException {
        equalUlong(columnName,val);
    }

    @Deprecated
    public void equal(String columnName, byte[] val) throws NdbApiException {
        equalBytes(columnName,val);
    }

    @Deprecated
    public void equal(String columnName, int val) throws NdbApiException {
        equalInt(columnName,val);
    }

    @Deprecated
    public void equal(String columnName, long val) throws NdbApiException {
        equalLong(columnName,val);
    }

    @Deprecated
    public void equal(String columnName, String val) throws NdbApiException {
        equalString(columnName,val);
    }

    @Deprecated
    public void equal(String columnName, Timestamp val) throws NdbApiException {
        equalTimestamp(columnName,val);
    }

}
