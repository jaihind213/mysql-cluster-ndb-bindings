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
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.Transaction;

public abstract class NdbTransactionManager implements TransactionManager {

    private final ThreadLocal<NdbTransaction> context =
        new ThreadLocal<NdbTransaction>();

    public void begin() throws NotSupportedException, SystemException {

        NdbTransaction transaction = context.get();
        if (transaction != null) {
            throw new IllegalStateException("Can't begin - already have a " +
                                            "transaction");
        }
        try {
            transaction = startTransaction();
        } catch (NdbApiException e) {
            throw new SystemException(e.getMessage());
        }
        context.set(transaction);

    }

    public void commit() throws RollbackException, HeuristicMixedException,
        HeuristicRollbackException, SecurityException,
        IllegalStateException, SystemException {
        NdbTransaction transaction = getTransaction();
        transaction.commit();
    }


    public int getStatus() throws SystemException {

        NdbTransaction transaction = getTransaction();
        return transaction.getStatus();

    }

    public NdbTransaction getTransaction() throws SystemException {
        NdbTransaction transaction = context.get();
        if (transaction==null) {
            throw new IllegalStateException("no transaction to commit");
        }
        return transaction;
    }

    public void resume(Transaction arg0) throws InvalidTransactionException,
        IllegalStateException, SystemException {
        NdbTransaction transaction = context.get();
        if (transaction != null) {
            throw new IllegalStateException("Can't resume - already have a " +
                                            "transaction");
        } else if (arg0.getClass().isInstance(NdbTransaction.class)) {
            context.set((NdbTransaction)arg0);
        } else {
            throw new InvalidTransactionException("Ndb only manages " +
                                                  "NdbTransactions");
        }

    }

    public void rollback() throws IllegalStateException, SecurityException,
        SystemException {
        NdbTransaction transaction = getTransaction();
        transaction.rollback();
    }

    public void setRollbackOnly()
        throws IllegalStateException, SystemException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("setRollbackOnly");
    }

    public void setTransactionTimeout(int arg0) throws SystemException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("setTransactionTimeout");
    }

    public NdbTransaction suspend() throws SystemException {
        NdbTransaction transaction = getTransaction();
        context.remove();
        return transaction;
    }

    public abstract NdbTransaction startTransaction() throws NdbApiException;

}
