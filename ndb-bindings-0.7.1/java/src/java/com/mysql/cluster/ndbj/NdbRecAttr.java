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
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;


public interface NdbRecAttr {

    public String aRef() throws NdbApiException ;

    public char getChar() throws NdbApiException ;

    public double getDouble() throws NdbApiException ;

    public float getFloat() throws NdbApiException ;

    public int getColType() throws NdbApiException ;

    public int getInt() throws NdbApiException ;

    public long getLong() throws NdbApiException ;

    public int isNULL() throws NdbApiException ;

    public short getShort() throws NdbApiException ;

    public long getUint() throws NdbApiException ;

    public BigInteger getUlong() throws NdbApiException ;

    public short getUchar() throws NdbApiException ;

    public int getUshort() throws NdbApiException ;

    public Timestamp getDatetime() throws NdbApiException ;

    public String getString() throws NdbApiException ;

    public byte[] getBytes() throws NdbApiException ;
    
    public byte[] getStringBytes() throws NdbApiException ;

    public Timestamp getTimestamp() throws NdbApiException ;

    public Date getDate() throws NdbApiException;

    public Time getTime() throws NdbApiException;

    public BigDecimal getDecimal() throws NdbApiException;

    public NdbColumn.Type getType();
}
