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

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.xa.XAResource;

import com.mysql.cluster.ndbj.NdbOperation.AbortOption;
import com.mysql.cluster.ndbj.NdbTransaction.ExecType;

public abstract class NdbJtaTransaction implements Transaction {

    private Synchronization synchronization = null;
    private boolean rollbackOnly = false;
    private boolean inCommit = false;

    public abstract int execute(ExecType execType, AbortOption abortOption,
                                boolean force) throws NdbApiException;
    public abstract int execute(ExecType execType, AbortOption abortOption)
        throws NdbApiException;
    public abstract int execute(ExecType execType) throws NdbApiException;


    public void commit() throws RollbackException, HeuristicMixedException,
        HeuristicRollbackException, SecurityException,
        IllegalStateException, SystemException {
        inCommit = true;
        int retVal = -1;
        if (synchronization != null)
            synchronization.beforeCompletion();
        try {
            if (rollbackOnly) {
                throw new RollbackException("Rollback requested");
            }
            retVal = execute(ExecType.Commit,AbortOption.AbortOnError,false);
        } catch (NdbApiException e) {
            rollback();
            throw new RollbackException("Transaction failed and " +
                                        "was rolled back: " + e.getMessage());
        } catch (RollbackException rbE) {
            rollback();
            throw rbE;
        } finally {
            if (synchronization != null)
                synchronization.afterCompletion(retVal);
            inCommit = false;
        }

    }

    public boolean delistResource(XAResource arg0, int arg1)
        throws IllegalStateException, SystemException {
        // TODO Auto-generated method stub
        // return false;
        throw new NotImplementedException("delistResource");

    }

    public boolean enlistResource(XAResource arg0) throws RollbackException,
        IllegalStateException, SystemException {
        // TODO Auto-generated method stub
        // return false;
        throw new NotImplementedException("enlistResource");

    }

    public int getStatus() throws SystemException {
        // TODO Auto-generated method stub
        // return 0;
        throw new NotImplementedException("getStatus");

    }

    public void registerSynchronization(Synchronization theSynchronization)
        throws RollbackException, IllegalStateException, SystemException {
        synchronization = theSynchronization;
    }

    public void rollback() throws IllegalStateException, SystemException {
        int retVal = -1;
        if (!inCommit && synchronization != null) {
            synchronization.beforeCompletion();
        }
        try {
            retVal = execute(ExecType.Rollback,AbortOption.AbortOnError,false);
        } catch (NdbApiException e) {
            throw new SystemException("Couldn't rollback - " +
                                      "something is seriously wrong: " +
                                      e.getMessage());
        } finally {
            if (!inCommit && synchronization != null) {
                synchronization.afterCompletion(retVal);
            }
        }
    }

    public void setRollbackOnly()
        throws IllegalStateException, SystemException {
        rollbackOnly = true;
    }

}
