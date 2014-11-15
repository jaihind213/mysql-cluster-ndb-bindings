package com.mysql.cluster.ndbj.examples;

import com.mysql.cluster.ndbj.Ndb;
import com.mysql.cluster.ndbj.NdbApiException;
import com.mysql.cluster.ndbj.NdbClusterConnection;
import com.mysql.cluster.ndbj.NdbApiPermanentException;
import com.mysql.cluster.ndbj.NdbOperation;
import com.mysql.cluster.ndbj.NdbTransaction;
import com.mysql.cluster.ndbj.NdbOperation.AbortOption;
import com.mysql.cluster.ndbj.NdbTransaction.ExecType;

/**
 * 
 * Create a file called 'ndbj.props' in your home directory
 * with the following contents. It is assumed that you are running
 * a cluster on your localhost, and a mysql server on your localhost,
 * with the mysql server authentication information (username=root, password is empty).
 * Please modify this file to match your MySQL Cluster configuration. 
 * 
 * ndbj.connectstring=localhost
 * ndbj.mysqld=localhost
 * ndbj.username=root
 * ndbj.password=
 * ndbj.testDatabase=test
 * 
 * Run the program using the command:
 * >java com.mysql.cluster.ndbj.examples.InsertUsingPrimaryKey ${NDBJ_HOME}/ndbj_localhost.props
 * 
 * @author jdowling
 */
public class InsertUsingPrimaryKey {

	
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Correct Usage: prog <nbbj.props>");
			System.exit(-1);
		}
		
		InsertUsingPrimaryKey inserter = null;
		try {
			inserter = new InsertUsingPrimaryKey(args[0]);
			inserter.insertRow();
		} 
		catch (UnsatisfiedLinkError e) {
            System.out.println("Exception: You probably haven't set the LD_LIBRARY_PATH environment variable to include the location for 'libndbj.so'");
            System.out.println(e.toString());
			e.printStackTrace();
		}		
		catch (NdbApiPermanentException e) {
			System.out.println("Problem connecting to a cluster management server at: " + ExamplesConfigurator.CONNECTSTRING);
			e.printStackTrace();
		}		
		catch (NdbApiException e) {
			e.printStackTrace();
		}
		finally {			
			/**
			 * We close the connection, ndb and transaction objects when we are finished.
			 * We do not need to close operation objects, as they are owned by transaction objects (that handle their destruction).
			 */
			if (inserter != null) {
				inserter.destroy();
			}			
		}
	}

	
	private NdbClusterConnection conn = null;
	private Ndb ndb = null;
	private NdbTransaction 	trans=null;

	public InsertUsingPrimaryKey(String configFile) throws NdbApiException
	{
		/**
		 * Load the configuration file to enable binding to:
		 * (1) MySQL Server using ndbj.mysqld (We use the MySQL server to create a table in the database)
		 * (2) NDB Management Server (ndb_mgmd) using ndbj.connectstring
		 */
		ExamplesConfigurator.loadParamsFromPropertiesFile(configFile);

		/**
		 * This helper method drops any existing table in the database (ndbj.database) called 'ndbj_test'.
		 * It then creates a table called 'ndbj_test'.
		 */
		ExamplesConfigurator.DropAndCreateTable("ndbj_test",
				"(`id` INTEGER NOT NULL, `"
				+ "firstName` VARCHAR(256) NOT NULL, `"
				+ "lastName` VARCHAR(256) NOT NULL, "
				+"PRIMARY KEY(id)) ENGINE=ndb;");
	
		/**
		 * Connect to the management server using a NdbClusterConnectionImpl object
		 */
    	conn = NdbClusterConnection.create(ExamplesConfigurator.CONNECTSTRING);
    	
    	/**
    	 * If you cannot connect immediately to the management server, retry connecting to the up to 5 times, 
    	 * with a gap of 3 seconds between attempts, the 'true' sets output to verbose mode.
    	 */
	    conn.connect(5,3,true);
	    
		/**
		 * Wait 30 seconds for the cluster to be available, throw exception if it is not ready.
		 */
	    conn.waitUntilReady(30,0);
	
	    /**
         *  Get a connection to a NDB object that is used to create transactions.
         *  We're going to initialise the ndb object with more than the default
         *  number of transactions, 12. 
         */
	    ndb = conn.createNdb(ExamplesConfigurator.DB_NAME,12);		    	
        
	}
	
	public void destroy() 
	{
		if (ndb != null) {
			ndb.close();
		}
		if (conn != null) {
			conn.close();
		}		
	}
	
	public void insertRow() throws NdbApiException
	{
		try {
			
		    /**
		     * Create a transaction. A transaction is used to create operations. 
		     * It can hold many operations (in a batch) that are sent down to cluster when the transaction is executed.
		     */
		    trans = ndb.startTransaction();
		    
		    /**
		     * An NdbOperationRef object represents a Primary Key operation. We can read/write/update/delete a row using
		     * the supplied primary key.  
		     */
			NdbOperation op = trans.getInsertOperation("ndbj_test");
			
			/**
			 * We use the equal() method to declare the primary key value.
			 * Multiple equal() methods are called for multi-part primary keys.
			 * Here we have only a single-part primary key, called 'id'.
			 */
			op.equalInt("id", 1);
			
			/**
			 * We use the set<X>() methods to set the values for columns in the row we are going to insert.
			 * The column 'firstName' is of type 'VARCHAR' (a string), so we call the setString() method to set its value.
			 */
			op.setString("firstName", "Jim");
			
			/**
			 * The column 'lastName' is of type 'VARCHAR' (a string), so we call the setString() method to set its value.
			 */			
			op.setString("lastName", "Dowling");

			/**
			 * Then we can execute the transaction, which causes all operations that were 'gotten' from the transaction to
			 * be executed in the cluster. Here we have only a single operation.
			 * We also pass the 'commit' parameter to indicate that the transaction should be committed to the database.
			 */
			trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
			
			System.out.println("We have written a row containing (1, 'Jim', 'Dowling') to test.ndbj_test");
		} 
		catch (NdbApiException e) {
			/**
			 * Rethrow the exception to be caught by main()
			 */
			throw e;
		}
		finally {			
			/**
			 * We close the transaction object when we are finished.
			 * We do not need to close operation objects, as they are owned by transaction objects (that handle their destruction).
			 */
			if (trans != null) {
				trans.close();
			}
			
		}		
	}
	
}
