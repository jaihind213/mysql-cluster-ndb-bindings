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
import javax.transaction.TransactionManager;

/**
 *
 * An NDB database handle
 *
 * <br>An <code>Ndb</code> object is used to create transaction objects for
 * use on a specific database.
 * They are constructed using: a <code>NdbClusterConnection</code> object;
 * the name of the database schema to be accessed;
 * and the maximum number of simultaneous transactions that will be created
 * using the <code>Ndb</code> object.
 * <br>You must call the close() method on the <code>Ndb</code> object when
 * you have finished using it to free resources.
 * <br><code>Ndb</code> objects are not thread-safe. Howver, it is possible to
 * implement a multi-threaded application using a single, non-shared
 * <code>Ndb</code> object per thread or by providing synchronised access to a
 * shared <code>Ndb</code> object.
 * <br>A single application can support a maximum of 128 <code>Ndb</code>
 * objects.
 *
 * <br>Sample Code:<br>
 * <code>
 *      NdbClusterConnection conn=null;<br>
 *      Ndb ndb=null;<br>
 *      try {<br>
 *          conn = NdbClusterConnection.create("ndb-connectstring");<br>
 *          // Max num of concurrent transactions is set to 12 <br>
 *          ndb = conn.createNdb("database",12); <br>
 *      }<br>
 *      catch (NdbApiException e) {<br>
 *          // Handle Error
 *      }<br>
 *       finally {<br>
 *           // You must close the ndb object to free up native resources.<br>
 *          if  (ndb != null)<br>
 *              ndb.close();<br>
 *          if (conn != null)<br>
 *              conn.close();<br>
 *       }<br>
 * }<br>
 *</code>
 * @see      NdbClusterConnection
 * @see      NdbTransaction
 */

public interface Ndb extends TransactionManager {

    /**
     * init is deprecated. Please specify max transaction in
     * NdbClusterConnection.createNdb() instead.
     * @param maxNumberOfTransactions The maximum number of parallel
     * NdbTransaction objects that can be handled by this instance of Ndb.
     * The maximum permitted value for maxNoOfTransactions is 1024;
     * @throws NdbApiException if an error occured in the native library
     * @deprecated Pass in the number of Transactions to the
     *             Constructor instead
     */
    @Deprecated
    public int init(int maxNumberOfTransactions) throws NdbApiException;

    /**
     * Creates a NdbTransaction object that can be used to create
     * an NdbOperation object for reading, inserting and updating
     * rows in a schema.
     * When no schema is specfied, the transaction object will be started
     * on a random node in the cluster.
     * @return an NdbTransaction object
     * @throws NdbApiException if an error occured in the native library
     * @see NdbTransaction
     * @see NdbOperation
     */
    public NdbTransaction startTransaction() throws NdbApiException;

    /**
     * Creates a NdbTransaction object that can be used to create
     * an NdbOperation object for reading, inserting and updating
     * rows in a schema.
     * The schema name and distribution key provide a hint as to where to
     * start the transaction.
     * The transaction should start on the node where the data is located,
     * removing a potential
     * network hop from the transaction coordinator to the data.
     *
     * @param tableName table name used for deciding which node to run the
     *        Transaction Coordinator on
     * @param distkey partition key corresponding to table
     * @return an NdbTransaction object
     * @throws NdbApiException if an error occured in the native library
     * @see NdbTransaction
     */
    public NdbTransaction startTransaction(String tableName, String distkey)
        throws NdbApiException;

    /**
     *
     * Creates a NdbTransaction object that can be used to create
     * an NdbOperation object for reading, inserting and updating
     * rows in a schema.
     * The schema name and distribution key provide a hint as to where to
     * start the transaction.
     * The transaction should start on the node where the data is located,
     * removing a potential network hop from the transaction coordinator
     * to the data.
     *
     * @param table Table object used for deciding which node to run the
     *        Transaction Coordinator on
     * @param distkey partition key corresponding to table
     * @return an NdbTransaction object
     * @throws NdbApiException if an error occured in the native library
     * @see NdbTransaction
     */
    public NdbTransaction startTransaction(NdbTable table, String distkey)
        throws NdbApiException;

    /**
     *
     * Creates a NdbTransaction object that can be used to create
     * an NdbOperation object for reading, inserting and updating
     * rows in a schema.
     * The schema name and distribution key provide a hint as to where to
     * start the transaction.
     * The transaction should start on the node where the data is located,
     * removing a potential network hop from the transaction coordinator
     * to the data.
     *
     * @param tableName schema name used for deciding which node to run the
     *        Transaction Coordinator on
     * @param distkey partition key corresponding to table
     * @return an NdbTransaction object
     * @throws NdbApiException if an error occured in the native library
     * @see NdbTransaction
     */
    public NdbTransaction startTransaction(String tableName, int distkey)
        throws NdbApiException;

    /**
     *
     * Creates a NdbTransaction object that can be used to create
     * an NdbOperation object for reading, inserting and updating
     * rows in a schema.
     * The schema name and distribution key provide a hint as to where to
     * start the transaction.
     * The transaction should start on the node where the data is located,
     * removing a potential network hop from the transaction coordinator to
     * the data.
     *
     * @param table Table object used for deciding which node to run the
     *        Transaction Coordinator on
     * @param distkey partition key corresponding to table
     * @return an NdbTransaction object
     * @throws NdbApiException if an error occured in the native library
     * @see NdbTransaction
     */
    public NdbTransaction startTransaction(NdbTable table, int distkey)
        throws NdbApiException;

    /**
     * Call this method when the Ndb object is no longer required
     * to free up memory allocated in the underlying native library.
     */
    public void close();

    /**
     * Call this to fetch an auto increment value from the named table.
     * @param aTableName The table name from which to fetch
     * @param cacheSize The number of values to prefetch and cache
     *        saving subsequent round trips.
     * @return The auto increment value
     * @throws NdbApiException
     */
    public long getAutoIncrementValue(String aTableName, long cacheSize)
        throws NdbApiException;

    /**
     * Call this to fetch an auto increment value from the named table.
     * @param myTable The table object from which to fetch
     * @param cacheSize The number of values to prefetch and cache saving
     *        subsequent round trips.
     * @return The auto increment value
     * @throws NdbApiException
     */
    public long getAutoIncrementValue(NdbTable myTable, long cacheSize)
        throws NdbApiException;

    /**
     * Call this to fetch an auto increment value from the named table.
     * @param aTableName The table name from which to fetch
     * @param cacheSize The number of values to prefetch and cache
     *        saving subsequent round trips.
     * @return The auto increment value
     * @throws NdbApiException
     */
    public BigInteger getBigAutoIncrementValue(String aTableName, long cacheSize)
        throws NdbApiException;

    /**
     * Call this to fetch an auto increment value from the named table.
     * @param myTable The table object from which to fetch
     * @param cacheSize The number of values to prefetch and cache saving
     *        subsequent round trips.
     * @return The auto increment value
     * @throws NdbApiException
     */
    public BigInteger getBigAutoIncrementValue(NdbTable myTable, long cacheSize)
        throws NdbApiException;

    /**
     * Fetch a reference to the Data Dictionary.
     * @return NdbDictionary object representing the NdbDataDictionary
     * @throws NdbApiException
     */
    public NdbDictionary getDictionary() throws NdbApiException;

    /**
     * Creates an NdbEventOperation for use in responding to events
     * @param eventName Name of the event to respond to
     * @return NdbEventOperation
     * @throws NdbApiException
     */
    public NdbEventOperation createEventOperation(String eventName)
        throws NdbApiException;

    /**
     * Returns an event operation that has data after a pollEvents
     * @return an event operations that has data, NULL if no events left with
     * data
     */
    public NdbEventOperation nextEvent();

    /**
     * Destroys an NdbEventOperation
     * @param eventOp Underlying NdbEventOperation to drop
     * @throws NdbApiException
     */
    public void dropEventOperation(NdbEventOperation eventOp)
        throws NdbApiException;

    /**
     * Wait for an event to occur. Will return as soon as an event
     * is detected on any of the created events.
     * @param aMillisecondNumber How many seconds to wait for an event before
     *        timing out
     * @return number of events available
     * @throws NdbApiException on failure
     */
    public int pollEvents(int aMillisecondNumber) throws NdbApiException;

    /**
     * This is a send-poll variant that first calls
     * Ndb.sendPreparedTransactions and then Ndb.pollNdb. It is however
     * somewhat faster than calling the methods separately, since some
     * mutex-operations are avoided. See documentation of Ndb.pollNdb and
     * Ndb.sendPreparedTransactions for more details.
     *
     * @param aMillisecondNumber
     *            Timeout specifier Polling without wait is achieved by setting
     *            the millisecond timer to zero.
     * @param minNoOfEventsToWakeup
     *            Minimum number of transactions which has to wake up before the
     *            poll-call will return. If minNoOfEventsToWakeup is set to a
     *            value larger than 1 then this is the minimum number of
     *            transactions that need to complete before the poll-call will
     *            return. Setting it to zero means that one should wait for all
     *            outstanding transactions to return before waking up.
     * @param forceSend
     *            When operations should be sent to NDB Kernel.
     *            0: non-force, adaptive algorithm notices it (default);
     *            1: force send, adaptive algorithm notices it;
     *            2: non-force, adaptive algorithm does not notice the send.
     * @return Number of transactions polled.
     * @see #sendPreparedTransactions(int)
     * @see #pollNdb(int,int)
     */
    public int sendPollNdb(int aMillisecondNumber, int minNoOfEventsToWakeup,
                           int forceSend) throws NdbApiException;

    /**
     * This is a send-poll variant that first calls
     * Ndb.sendPreparedTransactions and then Ndb.pollNdb. It is however
     * somewhat faster than calling the methods separately, since some
     * mutex-operations are avoided. See documentation of Ndb.pollNdb and
     * Ndb.sendPreparedTransactions for more details.
     *
     * @param aMillisecondNumber
     *            Timeout specifier Polling without wait is achieved by setting
     *            the millisecond timer to zero.
     * @param minNoOfEventsToWakeup
     *            Minimum number of transactions which has to wake up before
     *            the poll-call will return. If minNoOfEventsToWakeup is set to
     *            a value larger than 1 then this is the minimum number of
     *            transactions that need to complete before the poll-call will
     *            return. Setting it to zero means that one should wait for all
     *            outstanding transactions to return before waking up.
     * @return Number of transactions polled.
     * @see #sendPreparedTransactions(int)
     * @see #pollNdb(int,int)
     */
    public int sendPollNdb(int aMillisecondNumber, int minNoOfEventsToWakeup)
        throws NdbApiException;

    /**
     * This is a send-poll variant that first calls sendPreparedTransactions
     * and then pollNdb. It is however somewhat faster than calling the
     * methods separately, since some mutex-operations are avoided. See
     * documentation of pollNdb and sendPreparedTransactions for more
     * details.
     *
     * @param aMillisecondNumber
     *            Timeout specifier Polling without wait is achieved by setting
     *            the millisecond timer to zero.
     * @return Number of transactions polled.
     * @see #sendPreparedTransactions(int)
     * @see #pollNdb(int,int)
     */
    public int sendPollNdb(int aMillisecondNumber) throws NdbApiException;

    /**
     * This send method will send all prepared database operations. The default
     * method is to do it non-force and instead use the adaptive algorithm.
     * The second option is to force the sending and finally
     * There is the third alternative which is also non-force but also
     * making sure that the adaptive algorithm do not notice the send. In
     * this case the sending will be performed on a cyclical 10 millisecond
     * event.
     *
     * @param forceSend When operations should be sent to NDB Kernel.
     *          - 0: non-force, adaptive algorithm notices it (default);
     *          - 1: force send, adaptive algorithm notices it;
     *          - 2: non-force, adaptive algorithm do not notice the send.
     */
    public void sendPreparedTransactions(int forceSend);

    /**
     * This send method will send all prepared database operations. The default
     * method is to do it non-force and instead use the adaptive algorithm. 
     * @see #sendPreparedTransactions(int)
     */
    public void sendPreparedTransactions();

    /**
     * Wait for prepared transactions.
     * Will return as soon as at least 'minNoOfEventsToWakeUp'
     * of them have completed, or the maximum time given as timeout has passed.
     *
     * @param aMillisecondNumber
     *        Maximum time to wait for transactions to complete. Polling
     *        without wait is achieved by setting the timer to zero.
     *        Time is expressed in milliseconds.
     * @param minNoOfEventsToWakeup Minimum number of transactions
     *            which has to wake up before the poll-call will return.
     *            If minNoOfEventsToWakeup is
     *            set to a value larger than 1 then this is the minimum
     *            number of transactions that need to complete before the
     *            poll will return.
     *            Setting it to zero means that one should wait for all
     *            outstanding transactions to return before waking up.
     * @return Number of transactions polled.
     */
    public int pollNdb(int aMillisecondNumber, int minNoOfEventsToWakeup)
        throws NdbApiException;

    /**
     * Wait for prepared transactions.
     * Will return as soon as the maximum time given as timeout has passed.
     *
     * @param aMillisecondNumber
     *        Maximum time to wait for transactions to complete. Polling
     *        without wait is achieved by setting the timer to zero.
     *        Time is expressed in milliseconds.
     * @return Number of transactions polled.
     */
    public int pollNdb(int aMillisecondNumber) throws NdbApiException;

    /**
     * Wait for prepared transactions.
     * Will return as many transactions as have completed.
     *
     * @return Number of transactions polled.
     */
    public int pollNdb() throws NdbApiException;
    
    /**
     * Returns the number of rows in the supplied table name.
     * Produces a table scan and a network rountrip.
      *
     * @param tableName name of Table to count
     * @return number of rows in table
     * @throws NdbApiException
     */
    BigInteger selectCountBig(String tableName) throws NdbApiException;
    
    /**
     * Returns the number of rows in the supplied NdbTable.
     * Produces a table scan and a network rountrip.
     *
     * @param theTable table to count
     * @return number of rows in table
     * @throws NdbApiException
     */
	public BigInteger selectCountBig(NdbTable theTable) throws NdbApiException;
 
    /**
     * Returns the number of rows in the supplied table name.
     * Produces a table scan and a network rountrip.
      *
     * @param tableName name of Table to count
     * @return number of rows in table
     * @throws NdbApiException
     */
    long selectCount(String tableName) throws NdbApiException;

    /**
     * Returns the number of rows in the supplied NdbTable.
     * Produces a table scan and a network rountrip.
     *
     * @param theTable table to count
     * @return number of rows in table
     * @throws NdbApiException
     */
	public long selectCount(NdbTable theTable) throws NdbApiException;
	
    public NdbTransaction startTransaction(String aTableName, short keyData)
        throws NdbApiException
        ;
    public NdbTransaction startTransaction(String aTableName, long keyData)
        throws NdbApiException;

    public NdbTransaction startTransaction(NdbTable table, short keyData)
        throws NdbApiException;

    public NdbTransaction startTransaction(NdbTable table, long keyData)
        throws NdbApiException;

	public NdbTransactionImpl startTransactionBig(String aTableName, long keyData) throws NdbApiException;

	public NdbTransactionImpl startTransactionBig(NdbTable table, long keyData) throws NdbApiException;



}
