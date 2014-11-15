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
import java.sql.Blob;
import java.sql.Clob;

/**
 * Represents a Blob Handle.
 *
 * Blob data is stored in 2 places:
 * (a) "header" and "inline bytes" stored in the blob attribute
 * (b) "blob parts" stored in a separate table NDB$BLOB_<tid>_<cid>
 *
 * <p>NdbBlob is a blob handle. To access blob data, the handle must
 *    be created using NdbOperation.getBlobHandle() in operation prepare phase.
 * The handle has following states:
 * <ul>
 * <li>prepared: before the operation is executed</li>
 * <li>active: after execute or next result but before transaction commit</li>
 * <li>closed: after transaction commit</li>
 * <li>invalid: after rollback or transaction close</li>
 * </ul>
 * In the prepare phase, NdbBlob methods getValue and setValue are used
 * to prepare a read or write of a blob value of known size.
 *
 * <p>Operation types:
 * <br>insertTuple must use setValue if blob column is non-nullable
 * readTuple with exclusive lock can also update existing value
 * updateTuple can overwrite with setValue or update existing value
 * writeTuple always overwrites and must use setValue if non-nullable
 * deleteTuple creates implicit non-accessible blob handles
 * scan with exclusive lock can also update existing value
 * scan "lock takeover" update op must do its own getBlobHandle
 *
 * @see com.mysql.cluster.ndbj.NdbOperation
 * @see com.mysql.cluster.ndbj.NdbIndexOperation
 * @see com.mysql.cluster.ndbj.NdbScanOperation
 * @see com.mysql.cluster.ndbj.NdbIndexScanOperation
 * @see com.mysql.cluster.ndbj.NdbTransaction
 * @see com.mysql.cluster.ndbj.Ndb
 * @see com.mysql.cluster.ndbj.NdbClusterConnection
 */
public interface NdbBlob extends Blob, Clob {

    /**
     * This method is called by a NdbResultSet and
     * must be called after calling execute() on the transaction object.
     * You must call getBlob on the Operation object in order
     * to be able to call getData() method.
     *
     * @return a reference to the internal byte buffer in the
     * NdbBlob object. A copy should be made of the byte array if
     * you intend to use it elsewhere.
     * @throws NdbApiException if
     *
     */
    public byte[] getData() throws NdbApiException;

    /**
     * getValue is called on a Blob to specify the size of the
     * byte array to be allocated to store the Blob. The byte array
     * must be large enough to hold the Blob.
     * <p>Requirements:
     * <li>The getBlobHandle() method must be called beforehand on the Operation object.</li>
     * <li>It must be called before the transaction is executed.</li>
     * <li>Data can only be fetched from the NdbBlob object using the getData() method -
     * you cannot use the readData() method.</li>
     * @throws NdbApiException
     */
    public void getValue(int len) throws NdbApiException;

    /**
     * Set blob value to NULL.
     */
    public void setNull() throws NdbApiException;

    /**
     * Used to set the value for a Blob.
     * @param data
     */
    public void setValue(byte[] data) throws NdbApiException;

    /**
     * Read at current position and set new position to first byte after the data read.
     * Following a successful invocation, data points to the data that was read, and it returns the number of bytes read.
     * A read past blob end returns actual number of bytes read.
     * @param data byte array to store blob data
     * @param bytesToRead number of bytes to read
     * @return the number of bytes that were read
     * @throws NdbApiException if numBytesToRead is a negative number or there was a problem when calling the native readData() method.
     */
    public long readData(byte[] data, long bytesToRead)
        throws NdbApiException;

    /**
     * Get current length of the blob in bytes.
     * <p>Use getNull to distinguish between length 0 blob and NULL blob.
     * @return int length of the blob in bytes.
     */
    public BigInteger getLength() throws NdbApiException;

    /**
     * Return true if blob is null, else false. Throws an NdbApiException
     * if the error = -1, which is undefined.	 
     * For non-event blob, undefined causes a state error.
     * @return true if the blob is null.
     */
    public boolean getNull() throws NdbApiException;

    /**
     * Gets the current state of the Blob as an enum (IDLE,  PREPARED, ACTIVE, CLOSED, INVALID)
     * @return Returns the state.
     * @see NdbBlob.State
     */
    public State getState() throws NdbApiException;

    public enum State {
        Idle(NdbjJNI.NdbBlobImpl_Idle_get()),
            Prepared(NdbjJNI.NdbBlobImpl_Prepared_get()),
            Active(NdbjJNI.NdbBlobImpl_Active_get()),
            Closed(NdbjJNI.NdbBlobImpl_Closed_get()),
            Invalid(NdbjJNI.NdbBlobImpl_Invalid_get());

        public final int swigValue() {
            return swigValue;
        }

        public static State swigToEnum(int swigValue) {
            State[] swigValues = State.class.getEnumConstants();
            if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
                return swigValues[swigValue];
            for (State swigEnum : swigValues)
                if (swigEnum.swigValue == swigValue)
                    return swigEnum;
            throw new IllegalArgumentException("No enum " + State.class + " with value " + swigValue);
        }

        private State() {
            this.swigValue = SwigNext.next++;
        }

        private State(int swigValue) {
            this.swigValue = swigValue;
            SwigNext.next = swigValue+1;
        }

        private final int swigValue;

        private static class SwigNext {
            private static int next = 0;
        }
    }

}
