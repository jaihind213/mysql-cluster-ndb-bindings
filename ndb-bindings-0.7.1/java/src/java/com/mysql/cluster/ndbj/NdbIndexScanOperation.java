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

import java.sql.Timestamp;
import java.math.BigInteger;


/**
 * This interface describes operations that can be performed on an INDEX in a
 * table. <br>
 * Examples of index operations include:
 * <ul>
 * <li>equality (SQL-style: WHERE value='x'),
 * <li>greater-than (or equal-to) (SQL-style: WHERE value&gt;'x')
 * <li>less-than (or equal-to) (SQL-style: WHERE value&lt;'x')
 * </ul>
 * <br>
 * Equality operations on an index are set using operations
 * NdbBaseOperationEquals. <br>
 * The setBound methods are used to perform Greater-than and Less-than
 * operations on an index. <br>
 * The composition of bounds 
 *   (SQL-style 'AND':, e.g., WHERE value&gt;100 AND value &lt;200)
 * are set by calling setBound methods multiple times.
 * <p>
 * Note: A bound is an exclusive operation. A bound excludes rows that would
 * otherwise be returned. <br>
 * For example, a greater-than bound excludes rows that are greater than the
 * supplied value. This means that a greater-than bound returns rows that are
 * less than the supplied value! <br>
 * If you are setting a single bound, then whereXY methods are SQL-style naming
 * that are just wrappers that call setBounds methods.
 *
 * @see NdbBaseOperationEquals
 */
public interface NdbIndexScanOperation extends NdbScanOperation,
                                       NdbBaseOperationEquals {


    /**
     * BoundType is used to specify bounds for an Index Scan using a
     * NdbIndexScanOperation object. More than one bound may be specified on the
     * NdbIndexScanOperation object. However, there is an restriction on using
     * strict bounds (BoundLT and BoundGT). Strict bounds can only be used if it
     * is the last bound that is specified on an NdbIndexScanOperation object.
     * Non-strict bounds can be specified at any bound position (from first to
     * last bound). <br>
     * Another restriction on setbound is that when using a multi-part key with
     * a BoundLE or BoundLE on the first key attribute a bound on the 2nd
     * attribute that "points in the same direction" doesn't work. Only these
     * combinations work: EQ/*, LE/L*, GE/GT* Any LT/* or GT/* combination does
     * not work as documented, but LE/EQ, LE/GE, LE/GT and GE/EQ, GE/LE, GE/LT
     * do not work, too.
     *
     * BoundLT is a strict bound (Less than) BoundGT is a strict bound (greater
     * than) BoundLE is a non-strict bound (less tha or equal to) BoundGE is a
     * non-strict bound (greater than or equal to) BoundEQ is a non-strict bound
     * (equal to)
     *
     * In C++ NDB-API, these BoundTypes are defined in NdbIndexScanOperation.hpp
     */
    public enum BoundType {
        /**
         * Less than or equal to (<=). Non-strict bound.
         */
        BoundLE(0), // /< lower bound
        /**
         * Less than (<). Strict bound.
         */
        BoundLT(1), // /< lower bound, strict
        /**
         * Greater than or equal to (&gt;=). Non-strict bound.
         */
        BoundGE(2), // /< upper bound
        /**
         * Greater than (&gt;). Strict bound.
         */
        BoundGT(3), // /< upper bound, strict
        /**
         * Equal to (=). Non-strict bound.
         */
        BoundEQ(4); // /< equality
        public int type;

        private BoundType(int type) {
            this.type = type;
        }

        public int swigValue() {
            return this.type;
        }
    };


    /**
     *
     * @param columnId
     * @param Type
     * @param value
     */
    public void setBoundString(long columnId, BoundType Type, String value)
        throws NdbApiException;

    /**
     *
     * @param columnName
     * @param Type
     * @param value
     */
    public void setBoundString(String columnName, BoundType Type,
                                        String value) throws NdbApiException;

    /**
     *
     * @param columnId
     * @param Type
     * @param value
     */
    public void setBoundInt(long columnId, BoundType Type, int value)
        throws NdbApiException;

    /**
     *
     * @param columnName
     * @param Type
     * @param value
     */
    public void setBoundInt(String columnName, BoundType Type, int value)
        throws NdbApiException;

    /**
     *
     * @param columnId
     * @param Type
     * @param value
     */
    public void setBoundLong(long columnId, BoundType Type, long value)
        throws NdbApiException;

    /**
     *
     * @param columnName
     * @param Type
     * @param value
     */
    public void setBoundLong(String columnName, BoundType Type, long value)
        throws NdbApiException;

    /**
     *
     * @param columnId
     * @param Type
     * @param value
     */
    public void setBoundBytes(long columnId, BoundType Type, byte[] value)
        throws NdbApiException;

    /**
     *
     * @param columnName
     * @param Type
     * @param value
     * @throws NdbApiException
     */
    public void setBoundBytes(String columnName, BoundType Type,
                                       byte[] value) throws NdbApiException;

    /**
     * @param anAttrName
     * @param type
     * @param anInputDateTime
     * @throws NdbApiException
     */
    public void setBoundDatetime(String anAttrName, BoundType type,
                                 Timestamp anInputDateTime)
        throws NdbApiException;

    /**
     * @param anAttrName
     * @param type
     * @param val
     * @throws NdbApiException
     */
    public void setBoundDouble(String anAttrName, BoundType type, double val)
        throws NdbApiException;

    /**
     * @param anAttrName
     * @param type
     * @param val
     * @throws NdbApiException
     */
    public void setBoundFloat(String anAttrName, BoundType type, float val)
        throws NdbApiException;
    /**
     * @param anAttrId
     * @param type
     * @param anInputDateTime
     * @throws NdbApiException
     */
    public void setBoundDatetime(long anAttrId, BoundType type,
                                 Timestamp anInputDateTime)
        throws NdbApiException;

    /**
     * @param anAttrId
     * @param type
     * @param val
     * @throws NdbApiException
     */
    public void setBoundDouble(long anAttrId, BoundType type, double val)
        throws NdbApiException;

    /**
     * @param anAttrId
     * @param type
     * @param val
     * @throws NdbApiException
     */
    public void setBoundFloat(long anAttrId, BoundType type, float val)
        throws NdbApiException;


    public void whereGreaterThan(long columnId, double val)
        throws NdbApiException;

    public void whereGreaterThan(String columnName, int val)
        throws NdbApiException;

    public void whereGreaterThan(long columnId, int val)
        throws NdbApiException;

    public void whereGreaterThan(String columnName, long val)
        throws NdbApiException;

    public void whereGreaterThan(long columnId, long val)
        throws NdbApiException;

    public void whereGreaterThan(String columnName, float val)
        throws NdbApiException;

    public void whereGreaterThan(long columnId, float val)
        throws NdbApiException;

    public void whereGreaterThan(long columnId, BigInteger val)
        throws NdbApiException;

    public void whereGreaterThanEqualTo(String columnName, int val)
        throws NdbApiException;

    public void whereGreaterThanEqualTo(long columnId, int val)
        throws NdbApiException;

    public void whereGreaterThanEqualTo(String columnName, long val)
        throws NdbApiException;

    public void whereGreaterThanEqualTo(long columnId, long val)
        throws NdbApiException;

    public void whereGreaterThanEqualTo(String columnName, double val)
        throws NdbApiException;

    public void whereGreaterThanEqualTo(long columnId, double val)
        throws NdbApiException;

    public void whereGreaterThanEqualTo(String columnName, float val)
        throws NdbApiException;

    public void whereGreaterThanEqualTo(long columnId, float val)
        throws NdbApiException;

    public void whereGreaterThanEqualTo(long columnId, BigInteger val)
        throws NdbApiException;

    public void whereLessThan(String columnName, int val)
        throws NdbApiException;

    public void whereLessThan(long columnId, int val)
        throws NdbApiException;

    public void whereLessThan(String columnName, long val)
        throws NdbApiException;

    public void whereLessThan(long columnId, long val)
        throws NdbApiException;

    public void whereLessThan(String columnName, double val)
        throws NdbApiException;

    public void whereLessThan(long columnId, double val)
        throws NdbApiException;

    public void whereLessThan(String columnName, float val)
        throws NdbApiException;

    public void whereLessThan(long columnId, float val)
        throws NdbApiException;

    public void whereLessThan(long columnId, BigInteger val)
        throws NdbApiException;

    public void whereLessThanEqualTo(String columnName, int val)
        throws NdbApiException;

    public void whereLessThanEqualTo(long columnId, int val)
        throws NdbApiException;

    public void whereLessThanEqualTo(String columnName, long val)
        throws NdbApiException;

    public void whereLessThanEqualTo(long columnId, long val)
        throws NdbApiException;

    public void whereLessThanEqualTo(String columnName, double val)
        throws NdbApiException;

    public void whereLessThanEqualTo(long columnId, double val)
        throws NdbApiException;

    public void whereLessThanEqualTo(String columnName, float val)
        throws NdbApiException;

    public void whereLessThanEqualTo(long columnId, float val)
        throws NdbApiException;

    public void whereLessThanEqualTo(long columnId, BigInteger val)
        throws NdbApiException;

    public void whereGreaterThan(String anAttrName, String anInputString)
        throws NdbApiException;

    public void whereGreaterThan(long anAttrId, String anInputString)
        throws NdbApiException;

    public void whereGreaterThanEqualTo(String anAttrName,
                                        String anInputString)
        throws NdbApiException;

    public void whereGreaterThanEqualTo(long anAttrId, String anInputString)
        throws NdbApiException;
}
