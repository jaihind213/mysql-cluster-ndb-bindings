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


/**
 * This class represents a connection to a cluster of data nodes.
 * A NdbClusterConnection object is used to bind to
 * a cluster. It is needed to create Ndb objects, that in turn can be used to
 * create a NdbTransaction and NdbOperation objects.
 * <p>
 * The close() method must be called on a NdbClusterConnection object
 * when it is no longer needed, to free underlying resources.
 * <p>
 * When creating or using a NdbClusterConnection object,
 * errors are manifested as <code>NdbApiException</code>s.
 * <p>
 * More specialised exceptions that can be thrown are
 * NdbApiPermanentException and NdbApiTemporaryException that
 * denote a permanent problem when connecting to the cluster,
 * and a temporary problem when connecting to the cluster, respectively.
 * In the case of a temporary error, you can retry  connecting to the
 * cluster as it may succeed, for example, if the original problem was due to
 * a single node failure.
 * <br>
 * <br>Sample Code:<br>
 * <code>
 *    NdbClusterConnection conn=null;<br>
 *    try {<br>
 *      conn = NdbClusterConnection.create("ndb-connectstring");<br>
 *      // use the NdbClusterConnection object <br>
 *    }<br>
 *    catch (NdbApiPermanentException e) {<br>
 *      // Serious problem. Retrying connecting will not work.
 *    }<br>
 *    catch (NdbApiTemporaryException e) {<br>
 *      // We can retry connecting if we catch this exception
 *    }<br>
 *    catch (NdbApiException e) {<br>
 *      // Base Exception
 *    }<br>
 *    finally {<br>
 *      if (conn != null)<br>
 *        connRef.close();<br>
 *	}<br>
 * </code>
 *
 * <p>
 * NdbClusterConnection is not thread safe. In a multi-threaded application
 * it is intended that there be one or more global NdbClusterConnection and
 * that Ndb objects created from that be handed off to a thread for work.
 *
 *<p>
 * There is no restriction against instantiating multiple
 * NdbClusterConnection objects representing connections to the same or
 * different management servers in a single application, nor against using
 * these for creating multiple instances of the Ndb class.
 * <p>
 *  For example, it is entirely possible to perform application-level
 *  partitioning of data in such a manner that data
 *  meeting one set of criteria are "handed off" to one cluster via an
 *  Ndb object that makes use of an NdbClusterConnection
 *  object representing a connection to that cluster, while data not
 *  meeting those criteria
 *  (or perhaps a different set of criteria) can be sent to a
 *  different cluster through a different instance of Ndb
 *  that makes use of an NdbClusterConnection connected to the second cluster.
 *  <p>
 *  It is possible to extend this scenario to develop a single application
 *  that accesses an arbitrary number of clusters.
 *  However, in doing so, the following conditions and requirements
 *  must be kept in mind:
 *  <ul>
 *  <li>An NdbClusterConnection object "belongs" to a single management
 *  server whose hostname or IP address is used in instantiating this object
 *  (passed as the connectstring argument to its constructor);
 *  once the object is created,
 *  it cannot be used to initiate a connection to a different management
 *  server.
 *  <li>An Ndb object making use of this connection (NdbClusterConnection)
 *  cannot be re-used to connect to a different cluster management server
 *  (and thus to a different collection of data nodes making up a cluster).
 *  </ul>
 *  <p>
 *  Therefore, it is imperative in designing and implementing any application
 *  that accesses multiple clusters in a single session,
 *  that a separate set of NdbClusterConnection and Ndb objects be instantiated
 *  for connecting to each cluster management server,
 *  and that no confusion arises as to which of these is used to access
 *  which MySQL Cluster.
 *  <p>
 *  It is also important to keep in mind that no direct "sharing" of data
 *  or data nodes between different clusters is possible. A data node can
 *  belong to
 *  one and only one cluster, and any movement of data between clusters
 *  must be accomplished on the application level.
 *
 * @see Ndb
 * @see NdbTransaction
 */
public abstract class NdbClusterConnection {
    static int initResult = Ndbj.ndb_init();

    /**
     * One of the first methods typically called in a NDB/J application.
     * <br>It is a factory method used to create the top-level
     *     NdbClusterConnection object.
     * <br>Example Usage:
     * <code>
     *      <br>NdbClusterConnection conn=null;
     *      <br>try {
     *          <br>conn = NdbClusterConnection.create("ndb-connectstring");
     *          <br>// use the NdbClusterConnection object
     *      <br>}
     *      <br>catch (NdbClusterConnectionPermanentException e) {
     *      <br>// Serious problem. Retrying connecting will not work.
     *      <br>}
     * </code>
     * @param connectString the "--connectstring" used to connect to
     *        the cluster's management server (ndb_mgmd)
     * @return a NdbClusterConnection object
     * @throws NdbApiException if a null string is passed in, or a connection
     *         object could not be created successfully.
     */
    static public NdbClusterConnection create(String connectString)
        throws NdbApiException
        {
            return NdbClusterConnectionImpl.create(connectString);
        }

    /**
     * One of the first methods typically called in a NDB/J application.
     * <br>It is a factory method used to create the top-level
     *     NdbClusterConnection object.
     * <br>Called without an argument, sets the connect string to "localhost".
     * <br>Example Usage:
     * <code>
     *      <br>NdbClusterConnection conn=null;
     *      <br>try {
     *          <br>conn = NdbClusterConnection.create();
     *          <br>// use the NdbClusterConnection object
     *      <br>}
     *      <br>catch (NdbClusterConnectionPermanentException e) {
     *      <br>// Serious problem. Retrying connecting will not work.
     *      <br>}
     * </code>
     * @return a NdbClusterConnection object
     * @throws NdbApiException if a null string is passed in, or a connection
     *         object could not be created successfully.
     */
    static public NdbClusterConnection create()
        throws NdbApiException
        {
            return NdbClusterConnectionImpl.create();

        }

    /**
     * Connect to a cluster management server (ndb_mgmd)
     * @param retries specifies the number of retries to attempt
     * in the event of connection failure; a negative value will
     * result in the attempt to connect being repeated indefinitely
     * @param delay specifies how often retries should be performed
     * @param verbose specifies if the method should print a report
     * of its progess to stdout
     * @throws NdbApiException if parameters contain invalid values
     * (retries&lt;0 or delay&lt;0)
     * @throws NdbApiPermanentException if there is a permanent problem
     * in connecting to the management server.
     * @throws NdbApiTemporaryException if there is a temporary problem
     * in connecting to the management server. In this case, connect()
     * may be retried as it may succeed on the next attempt.
     */
    public abstract int connect(int retries, int delay, boolean verbose)
        throws NdbApiException, NdbApiPermanentException,
        NdbApiTemporaryException;
    /**
     * Connect to a cluster management server (ndb_mgmd)
     * @param retries specifies the number of retries to attempt
     * in the event of connection failure; a negative value will
     * result in the attempt to connect being repeated indefinitely
     * @param delay specifies how often retries should be performed
     * @throws NdbApiException if parameters contain invalid values
     * (retries&lt;0 or delay&lt;0)
     * @throws NdbApiPermanentException if there is a permanent problem
     * in connecting to the management server.
     * @throws NdbApiTemporaryException if there is a temporary problem
     * in connecting to the management server. In this case, connect()
     * may be retried as it may succeed on the next attempt.
     */
    public abstract int connect(int retries, int delay)
        throws NdbApiException, NdbApiPermanentException,
        NdbApiTemporaryException;
    /**
     * Connect to a cluster management server (ndb_mgmd)
     * @param retries specifies the number of retries to attempt
     * in the event of connection failure; a negative value will
     * result in the attempt to connect being repeated indefinitely
     * @throws NdbApiException if parameters contain invalid values
     * (retries&lt;0)
     * @throws NdbApiPermanentException if there is a permanent problem
     * in connecting to the management server.
     * @throws NdbApiTemporaryException if there is a temporary problem
     * in connecting to the management server. In this case, connect()
     * may be retried as it may succeed on the next attempt.
     */
    public abstract int connect(int retries)
        throws NdbApiException, NdbApiPermanentException,
        NdbApiTemporaryException;
    /**
     * Connect to a cluster management server (ndb_mgmd)
     * @throws NdbApiPermanentException if there is a permanent problem
     * in connecting to the management server.
     * @throws NdbApiTemporaryException if there is a temporary problem
     * in connecting to the management server. In this case, connect()
     * may be retried as it may succeed on the next attempt.
     */
    public abstract int connect()
        throws NdbApiPermanentException, NdbApiTemporaryException;

    /**
     * Set timeout
     *
     * Used as a timeout when talking to the management server,
     * helps limit the amount of time that we may block when connecting
     *
     * The default is 30 seconds.
     *
     * @param timeout millisecond timeout. Only increments of 1000 are
     *                really supported, with no gaurentees about calls
     *                completing in any hard amount of time.
     * @throws NdbApiException if unable to set the timeout
     */
    public abstract void setTimeout(int timeout) throws NdbApiException;

    /**
     * Set connection name
     *
     * A client can set a name on the connection, which will be printed
     * in the clusterlog.
     *
     * Useful for identifying an NDB API client, e.g., for debugging.
     *
     * @param name name to be printed to clusterlog
     *             (e.g., name of the NDB API client)
     * @throws NdbApiException if the name parameter is null.
     */
    public abstract void setName(String name) throws NdbApiException;

    /**
     * Wait for Cluster to become ready
     *
     * @param timeoutForFirstAlive Number of seconds to wait until each
     *                             node group has at least one live node
     * @param timeoutAfterFirstAlive Number of seconds to wait after all node
     *                               groups have at least one valid node
     * @throws NdbApiException if parameters contain invalid values
     * (timeoutForFirstAlive &lt; 0 or timeoutAfterFirstAlive &lt; 0)
     * @throws NdbApiPermanentException if no nodes responded within the
     *                                  specified timeoutForFirstAlive value
     * @throws NdbApiTemporaryException if some but not all nodes responded
     *                                  with the specified
     *                                  timeoutAfterFirstAlive value
     */
    public abstract void waitUntilReady(int timeoutForFirstAlive,
                                        int timeoutAfterFirstAlive)
        throws NdbApiException;

    /**
     * Create an Ndb object.
     *
     * @param dbName the name of the database with which the Ndb object
     *               will be associated
     * @param maxThreads the max number of simultaneous outstanding threads
     *                   the Ndb object can use during Asyncronous transactions
     * @return Ndb object
     * @throws NdbApiException if a Ndb object cannot be created.
     */
    public abstract Ndb createNdb(String dbName, int maxThreads)
        throws NdbApiException;

    /**
     * Create an Ndb object.
     *
     * @param dbName the name of the database with which the Ndb object
     *               will be associated
     * @return Ndb object
     * @throws NdbApiException if a Ndb object cannot be created.
     */
    public abstract Ndb createNdb(String dbName) throws NdbApiException;


    /**
     * The close method must be called on a NdbClusterConnection object when it
     * is no longer needed in order to free up its resources.
     */
    public abstract void close();



}
