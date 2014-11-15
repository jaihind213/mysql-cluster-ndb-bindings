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

import javax.transaction.Transaction;

import com.mysql.cluster.ndbj.NdbOperation.AbortOption;

/**
 * A transaction (represented by an NdbTransaction object) belongs to an Ndb
 * object and is created using Ndb.startTransaction(). A transaction consists of
 * a list of operations (represented by NdbOperation, NdbScanOperation,
 * NdbIndexOperation, and NdbIndexScanOperation objects). Each operation access
 * exactly one table.
 *
 * After getting the NdbTransaction object, the first step is to get an
 * operation given the table name using one of the methods #getNdbOperation(),
 * #getNdbScanOperation(), #getNdbIndexOperation(), or
 * #getNdbIndexScanOperation(). Then the operation is defined. Several
 * operations can be defined on the same NdbTransaction object, they will in
 * that case be executed in parallell. When all operations are defined, the
 * #execute() method sends them to the NDB kernel for execution.
 *
 * The #execute() method returns when the NDB kernel has completed execution of
 * all operations defined before the call to #execute(). All allocated
 * operations should be properly defined before calling #execute().
 *
 * A call to execute() uses one out of three types of execution: -#
 * ExecType.NoCommit Executes operations without committing them. -#
 * ExecType.Commit Executes remaining operation and commits the complete
 * transaction -# ExecType.Rollback Rolls back the entire transaction.
 *
 * execute() is equipped with an extra error handling parameter. There are two
 * alternatives: -# AbortOption.AbortOnError (default). The transaction is
 * aborted if there are any error during the execution -#
 * AbortOption.AO_IgnoreError Continue execution of transaction even if
 * operation fails
 *
 * <br>
 * Sample Code:<br>
 * <code>
 *      NdbClusterConnection conn=null;<br>
 *      Ndb ndb=null;<br>
 *      NdbTransaction trans=null;<br>
 *      try {<br>
 *          conn = NdbClusterConnection.create("ndb-connectstring");<br>
 *          // Max num of concurrent transactions is set to 12 <br>
 *          ndb = conn.createNdb("database",12); <br>
 *          trans = ndb.startTransaction();<br>
 *          // use the transaction
 *      }<br>
 *      catch (NdbApiException e) {<br>
 *          // Handle Error
 *      }<br>
 *      finally {<br>
 *          // You must close the ndb object to free up native resources.<br>
 *          if  (trans != null)<br>
 *              trans.close();<br>
 *          if  (ndb != null)<br>
 *              ndb.close();<br>
 *          if (conn != null)<br>
 *              conn.close();<br>
 *      }<br>
 *</code>
 *
 * @see com.mysql.cluster.ndbj.Ndb
 * @see com.mysql.cluster.ndbj.NdbClusterConnection
 * @see com.mysql.cluster.ndbj.NdbOperation
 * @see com.mysql.cluster.ndbj.NdbIndexOperation
 * @see com.mysql.cluster.ndbj.NdbScanOperation
 * @see com.mysql.cluster.ndbj.NdbIndexScanOperation
 *
 */
public interface NdbTransaction extends Transaction, NdbOperationFactory {


	public enum ExecType {
		NoCommit(NdbjJNI.NdbTransactionImpl_NoCommit_get()),
		Commit(NdbjJNI.NdbTransactionImpl_Commit_get()),
		Rollback(NdbjJNI.NdbTransactionImpl_Rollback_get());
		public int type = 0;

		public int swigValue() {
			return this.type;
		}
		private ExecType(int type) {
			this.type = type;
		}
	}


	public enum CommitStatusType {
		Started,
		Committed,
		Aborted,
		NeedAbort;

		public int type = 0;

		public int swigValue() {
			return this.type;
		}

		private CommitStatusType() {
			this.type = SwigNext.next++;
		}
		
		private static class SwigNext {
			private static int next = 0;
		}
		public static CommitStatusType swigToEnum(int swigValue) {
			CommitStatusType[] swigValues = CommitStatusType.class.getEnumConstants();
			if (swigValue < swigValues.length && swigValue >= 0 
					&& swigValues[swigValue].swigValue() == swigValue)
				return swigValues[swigValue];
			for (CommitStatusType swigEnum : swigValues)
				if (swigEnum.swigValue() == swigValue)
					return swigEnum;
			throw new IllegalArgumentException("No enum " + CommitStatusType.class + " with value " + swigValue);
		}
	}
	

    /*
     * The transaction stores a list of Blob objects for the transactions.
     * Used internally in NDB/J.
     * @param blob object

     abstract void addNdbBlob(NdbBlob blob);*/
    /**
     * This method executes the transaction and all the operations that have
     * been added it. It either succeeds or fails.
     *
     * The execute method returns when the NDB kernel has completed execution
     * of all operations defined before the call to execute. All allocated
     * operations should be properly defined before calling execute. A call to
     * the execute method uses one out of three types of execution: Commit,
     * NoCommit, or Rollback. If an operation in the transaction fails, it is
     * aborted if abortOption is set to AbortOption.AbortOnError , and the
     * transaction continues excution after the failed operation if abortOption
     * is AbortOption.AO_ignoreError.
     * <p>
     * If forceSend is set to 0, the transaction is placed in a send buffer for
     * up to a maximum of 10 milliseconds, after which it is flushed to the
     * cluster. This can help improve system performance in high load, through
     * increased batching of transactions. If forceSend is set to 1, the
     * transaction is flushed immediately to the cluster. This improves
     * transaction performance, but may degrade system scalability (throughput
     * of the cluster in terms of number of transactions that can be executed
     * per time unit).
     *
     * @param execType
     *            specifies whether the transaction should be committed,
     *            not-committed or rolled-back. <br>
     *            ExecType.NoCommit : executes operations without committing
     *            them. <br>
     *            ExecType.Commit : executes remaining operation and commits
     *            the complete transaction <br>
     *            ExecType.Rollback : rolls back the entire transaction.
     * @param abortOption
     *            specifies whether the transaction should be aborted or not if
     *            one of its operations fails. <br>
     *            AbortOption.AbortOnError : (default) The transaction is
     *            aborted if there are any error during the execution <br>
     *            AbortOption.AO_IgnoreError : Continue execution of
     *            transaction even if operation fails
     * @param force
     *            <br>
     *            true : put the transaction in the send buffer <br>
     *            false : flush the send buffer so that the transaction
     *            execute immediately (improves transaction performance, may
     *            degrade system performance)
     * @throws NdbApiException
     *             <br>
     *             base exception. Catch this if you don't care about retrying
     *             transactions that failed because of temporary problems in
     *             the cluster.
     * @throws NdbApiTemporaryException
     *             <br>
     *             transaction failed. If this exception is caught, retry the
     *             transaction. The transaction failed due a temporary problem
     *             (e.g., a single node failed), and retrying the transaction
     *             may cause it to succeed.
     * @throws NdbApiPermanentException
     *             <br>
     *             transaction failed. if this exception is caught, the
     *             transaction should not be retried, as it failed due to some
     *             serious error
     * @throws NdbApiUserAndPermanentException
     *             <br>
     *             transaction failed. if this exception is caught, the
     *             transaction should not be retried, as it failed due to some
     *             serious (possibly user) error (such as deleting a row that
     *             doesn't exist).
     */
    int execute(ExecType execType, AbortOption abortOption, boolean force)
        throws NdbApiException;

    /**
     * This method executes the transaction and all the operations that have
     * been added it. It either succeeds or fails.
     *
     * The execute method returns when the NDB kernel has completed execution
     * of all operations defined before the call to execute. All allocated
     * operations should be properly defined before calling execute. A call to
     * the execute method uses one out of three types of execution: Commit,
     * NoCommit, or Rollback. If an operation in the transaction fails, it is
     * aborted if abortOption is set to AbortOption.AbortOnError , and the
     * transaction continues excution after the failed operation if abortOption
     * is AbortOption.AO_ignoreError.
     *
     * @param execType
     *            specifies whether the transaction should be committed,
     *            not-committed or rolled-back. <br>
     *            ExecType.NoCommit : executes operations without committing
     *            them. <br>
     *            ExecType.Commit : executes remaining operation and commits
     *            the complete transaction <br>
     *            ExecType.Rollback : rolls back the entire transaction.
     * @param abortOption
     *            specifies whether the transaction should be aborted or not if
     *            one of its operations fails. <br>
     *            AbortOption.AbortOnError : (default) The transaction is
     *            aborted if there are any error during the execution <br>
     *            AbortOption.AO_IgnoreError : Continue execution of
     *            transaction even if operation fails
     * @throws NdbApiException
     *             <br>
     *             base exception. Catch this if you don't care about retrying
     *             transactions that failed because of temporary problems in
     *             the cluster.
     * @throws NdbApiTemporaryException
     *             <br>
     *             transaction failed. If this exception is caught, retry the
     *             transaction. The transaction failed due a temporary problem
     *             (e.g., a single node failed), and retrying the transaction
     *             may cause it to succeed.
     * @throws NdbApiPermanentException
     *             <br>
     *             transaction failed. if this exception is caught, the
     *             transaction should not be retried, as it failed due to some
     *             serious error
     * @throws NdbApiUserAndPermanentException
     *             <br>
     *             transaction failed. if this exception is caught, the
     *             transaction should not be retried, as it failed due to some
     *             serious (possibly user) error (such as deleting a row that
     *             doesn't exist).
     */
    int execute(ExecType execType, AbortOption abortOption)
        throws NdbApiException;

    /**
     * This method executes the transaction and all the operations that have
     * been added it. It either succeeds or fails.
     *
     * The execute method returns when the NDB kernel has completed execution
     * of all operations defined before the call to execute. All allocated
     * operations should be properly defined before calling execute. A call to
     * the execute method uses one of three types of execution: Commit,
     * NoCommit, or Rollback. If an operation in the transaction fails, it is
     * aborted if abortOption is set to AbortOption.AbortOnError , and the
     * transaction continues excution after the failed operation if abortOption
     * is AbortOption.AO_ignoreError.
     *
     * @param execType
     *            specifies whether the transaction should be committed,
     *            not-committed or rolled-back. <br>
     *            ExecType.NoCommit : executes operations without committing
     *            them. <br>
     *            ExecType.Commit : executes remaining operation and commits
     *            the complete transaction <br>
     *            ExecType.Rollback : rolls back the entire transaction.
     * @throws NdbApiException
     *             <br>
     *             base exception. Catch this if you don't care about retrying
     *             transactions that failed because of temporary problems in
     *             the cluster.
     * @throws NdbApiTemporaryException
     *             <br>
     *             transaction failed. If this exception is caught, retry the
     *             transaction. The transaction failed due a temporary problem
     *             (e.g., a single node failed), and retrying the transaction
     *             may cause it to succeed.
     * @throws NdbApiPermanentException
     *             <br>
     *             transaction failed. if this exception is caught, the
     *             transaction should not be retried, as it failed due to some
     *             serious error
     * @throws NdbApiUserAndPermanentException
     *             <br>
     *             transaction failed. if this exception is caught, the
     *             transaction should not be retried, as it failed due to some
     *             serious (possibly user) error (such as deleting a row that
     *             doesn't exist).
     */
    int execute(ExecType execType) throws NdbApiException;

    /**
     * This method should only be used after the transaction has been executed,
     * but before the transaction has been closed.
     *
     * Transactions consist of groups of operations. When a transaction has
     * been executed, you can call this method to get the next completed
     * operation in a transaction after the supplied operation object. If null
     * is specified as a parameter, then the first completed operation object
     * is returned.
     *
     * @param anOperation
     *            an operation object in the transaction. If op is null, the
     *            first completed operation in the transaction is returned.
     * @return the next completed operation object in the transaction.
     * @throws NdbApiException
     *             if there is an error in the JNI library
     * @throws IllegalStateException
     *             if the close method has already been called on the object.
     */
    NdbOperation getNextCompletedOperation(NdbOperation anOperation);

    NdbOperation getNextCompletedOperation();

    /**
     * closes the NdbTransaction object. The object can no longer be used after
     * the close() method has been invoked. You have to create a new
     * NdbTransaction object using a Ndb.startTransaction() method.
     */
    void close();

    /**
     * checks if the transaction object is closed
     * @return true if closed, false if not closed
     */
    boolean isClosed();

    /**
     * Refresh
     * Update timeout counter of this transaction 
     * in the database. If you want to keep the transaction 
     * active in the database longer than the
     * transaction abort timeout.
     * @throws NdbApiException
     */
    void refresh() throws NdbApiException;

    /**
     * Prepare an asynchronous transaction.
     *
     *
     * @param execType indicates whether the operation should be 
     *            committed, rolled back, or left open
     *            {@link #execute(ExecType)}
     * @param callback
     *            A callback object. This handleCallback method of this object
     *            gets called when the transaction has been executed.
     * @param abortOption
     * @see #execute(ExecType, NdbOperation.AbortOption)
     */
    void executeAsynchPrepare(ExecType execType, BaseCallback callback,
                              AbortOption abortOption);

    /**
     * Prepare an asynchronous transaction.
     *
     *
     * @param execType indicates whether the operation should be 
     *            committed, rolled back, or left open
     *            {@link #execute(ExecType)}
     * @param callback
     *            A callback object. The handleCallback method of this object
     *            gets called when the transaction has been executed.
     */
    void executeAsynchPrepare(ExecType execType, BaseCallback callback);

    /**
     * Prepare an asynchronous transaction and send it immediately.
     *
     * @param execType indicates whether the operation should be 
     *            committed, rolled back, or left open
     *            {@link #execute(ExecType)}
     * @param callback
     *            A callback object. This handleCallback method of this object
     *            gets called when the transaction has been executed.
     * @param abortOption
     *            see {@link #execute(ExecType, NdbOperation.AbortOption)}
     * @param forceSend
     *            Send immediately skipping the adaptive send buffer
     */
	public void executeAsynch(NdbTransaction.ExecType execType, BaseCallback callback, NdbOperation.AbortOption abortOption, boolean forceSend);
    
	/**
     * Prepare an asynchronous transaction and send it immediately.
     *
     * @param execType indicates whether the operation should be 
     *            committed, rolled back, or left open
     *            {@link #execute(ExecType)}
     * @param callback
     *            A callback object. This handleCallback method of this object
     *            gets called when the transaction has been executed.
     * @param abortOption
     *            see {@link #execute(ExecType, NdbOperation.AbortOption)}
     */
	public void executeAsynch(NdbTransaction.ExecType execType, BaseCallback callback, NdbOperation.AbortOption abortOption);

	/**
     * Prepare an asynchronous transaction and send it immediately.
     *
     * @param execType indicates whether the operation should be 
     *            committed, rolled back, or left open
     *            {@link #execute(ExecType)}
     * @param callback
     *            A callback object. This handleCallback method of this object
     *            gets called when the transaction has been executed.
     */
	public void executeAsynch(NdbTransaction.ExecType execType, BaseCallback callback);
	
    int executeNoCommit(AbortOption abortOption) throws NdbApiException,
        NdbApiTemporaryException, NdbApiPermanentException;

    int executeNoCommit() throws NdbApiException,
        NdbApiTemporaryException, NdbApiPermanentException;

    int executeCommit(AbortOption abortOption) throws NdbApiException,
        NdbApiTemporaryException, NdbApiPermanentException;

    int executeCommit() throws NdbApiException,
        NdbApiTemporaryException, NdbApiPermanentException;

    int executeRollback(AbortOption abortOption) throws NdbApiException,
        NdbApiTemporaryException, NdbApiPermanentException;

    int executeRollback() throws NdbApiException,
        NdbApiTemporaryException, NdbApiPermanentException;

    /**
     * Get global checkpoint identity (GCI) of transaction.
     *
     * Each committed transaction belong to a GCI.  
     * The log for the committed transaction is saved on 
     * disk when a global checkpoint occurs.
     * 
     * Whether or not the global checkpoint with this GCI has been 
     * saved on disk or not cannot be determined by this method.
     *
     * By comparing the GCI of a transaction with the value 
     * last GCI restored in a restarted NDB Cluster one can determine
     * whether the transaction was restored or not.
     *
     * @return GCI
     * @throws NdbApiException
     *             if CGI is not available
     */
    public BigInteger getGCI() throws NdbApiException;

    /**
     * Get transaction identity.
     *
     * @return  Transaction id.
     */
    public BigInteger getTransactionId();
    
    /**
     * Get the commit status of the transaction.
     *
     * @return  The commit status of the transaction
     */
    public CommitStatusType commitStatus();

}
