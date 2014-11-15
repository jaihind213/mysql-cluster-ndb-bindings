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

public abstract class BaseCallback {

    private long create_time;
    private long start_time;
    private long end_time;
    protected NdbTransaction theTrans = null;

    public BaseCallback(NdbTransaction trans) {
        create_time = 0;
        start_time = 0;
        end_time = 0;
        theTrans=trans;
    }

    protected final void jni_call_callback(int result, long transPtr, long createTime) {
        this.create_time = createTime;
        this.start_time = this.getMicroTime();

        if (NdbTransactionImpl.getCPtr((NdbTransactionImpl)theTrans) == transPtr) {
            this.callback(result);//),new NdbTransactionImpl(transPtr,false));
        } else {
            // Something is horrible wrong! The transaction we were passed is not the same as the
            // transaction we are storing. TODO: How do we deal with this?
            this.callback(result);
        }
        this.end_time = this.getMicroTime();
    };

    public abstract void callback(int result);

    //public void callback(int result, NdbTransaction myTrans) {}

    public final NdbError getNdbError() {
        return ((NdbTransactionImpl)theTrans).getNdbError();
    }
    public final long getMicroTime() {
        return NdbjJNI.getMicroTime();
    }

    public final long getElapsedTime() {
        return this.getMicroTime() - this.create_time;
    }

    public final long getExecuteTime() {
        return this.end_time - this.create_time;
    }

    public final long getCallbackTime() {
        return this.end_time - this.start_time;
    }
    public final long getCreateTime() {
        return this.create_time;
    }
    public final long getStartTime() {
        return this.start_time;
    }
};
