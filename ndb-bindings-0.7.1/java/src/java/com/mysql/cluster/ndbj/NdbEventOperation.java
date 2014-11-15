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

import com.mysql.cluster.ndbj.NdbEvent.TableEvent;

public interface NdbEventOperation extends NdbEventOperationResults {


    /**
     * @see com.mysql.cluster.ndbj.NdbEventOperation#execute()
     */
    public void execute() throws NdbApiException;

    /**
     * @return the value
     * @see com.mysql.cluster.ndbj.NdbEventOperation#getAnyValue()
     */
    public long getAnyValue();


    /**
     * @return the event type
     * @see com.mysql.cluster.ndbj.NdbEventOperation#getEventType()
     */
    public TableEvent getEventType();

    /**
     * @return the global checkpoint identity
     * @see com.mysql.cluster.ndbj.NdbEventOperation#getGCI()
     */
    public BigInteger getGCI();

    /**
     * @return the latest global checkpoint identity
     * @see com.mysql.cluster.ndbj.NdbEventOperation#getLatestGCI()
     */
    public BigInteger getLatestGCI();


    /**
     * @return the state
     * @see com.mysql.cluster.ndbj.NdbEventOperation#getState()
     */
    public State getState();


    /**
     * @return the consistency state
     * @see com.mysql.cluster.ndbj.NdbEventOperation#isConsistent()
     */
    public boolean isConsistent();

    /**
     * @return the overrun state
     * @see com.mysql.cluster.ndbj.NdbEventOperation#isOverrun()
     */
    public int isOverrun() throws NdbApiException;

    /**
     * @param flag
     * @see com.mysql.cluster.ndbj.NdbEventOperation#mergeEvents(boolean)
     */
    public void mergeEvents(boolean flag);

    /**
     * @return whether the table fragmentation is changed
     * @see com.mysql.cluster.ndbj.NdbEventOperation#tableFragmentationChanged()
     */
    public boolean tableFragmentationChanged();

    /**
     * @return whether the table frame is changed
     * @see com.mysql.cluster.ndbj.NdbEventOperation#tableFrmChanged()
     */
    public boolean tableFrmChanged();

    /**
     * @return whether the table name is changed
     * @see com.mysql.cluster.ndbj.NdbEventOperation#tableNameChanged()
     */
    public boolean tableNameChanged();

    /**
     * @return whether the table range list is changed
     * @see com.mysql.cluster.ndbj.NdbEventOperation#tableRangeListChanged()
     */
    public boolean tableRangeListChanged();

    public NdbEventOperationImpl getImpl();

    public enum State {
        EO_CREATED,
        EO_EXECUTING,
        EO_DROPPED,
        EO_ERROR;

        public final int swigValue() {
            return swigValue;
        }

        public static State swigToEnum(int swigValue) {
            State[] swigValues = State.class.getEnumConstants();
            if (swigValue < swigValues.length && swigValue >= 0
                && swigValues[swigValue].swigValue == swigValue)
                return swigValues[swigValue];
            for (State swigEnum : swigValues)
                if (swigEnum.swigValue == swigValue)
                    return swigEnum;
            throw new IllegalArgumentException("No enum " + State.class +
                                               " with value " + swigValue);
        }

        private State() {
            this.swigValue = SwigNext.next++;
        }

        private final int swigValue;

        private static class SwigNext {
            private static int next = 0;
        }
    }

}
