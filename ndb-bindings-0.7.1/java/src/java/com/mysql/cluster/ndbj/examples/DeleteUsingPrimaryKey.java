package com.mysql.cluster.ndbj.examples;

import com.mysql.cluster.ndbj.Ndb;
import com.mysql.cluster.ndbj.NdbClusterConnection;
import com.mysql.cluster.ndbj.NdbApiException;
import com.mysql.cluster.ndbj.NdbApiPermanentException;
import com.mysql.cluster.ndbj.NdbOperation;
import com.mysql.cluster.ndbj.NdbTransaction;
import com.mysql.cluster.ndbj.NdbOperation.AbortOption;
import com.mysql.cluster.ndbj.NdbTransaction.ExecType;

/**
 * This program assumes you have just run the InsertAndReadUsingPrimaryKey program, i.e., that
 * you have created a table called 'ndbj_test' in the 'test' database, and there is a row with
 * id='1'.
 * <p>
 * To test this program, run a mysql client:
 * <br>>mysql -u root test
 * <br>mysql>select * from ndbj_test where id='1';
 * <br>You will see the original contents of the row
 * <br>Run the program: 
 * <br>>java com.mysql.cluster.ndbj.examples.UpdateUsingPrimaryKey ~/ndbj.props
 *  * <br>mysql>select * from ndbj_test where id='1';
 * <br>See the updated contents of the row
 * <br>
 * <p>Create a file called 'ndbj.props' in your home directory
 * with the following contents. It is assumed that you are running
 * a cluster on your localhost, and a mysql server on your localhost,
 * with the mysql server authentication information (username=root, password is empty).
 * Please modify this file to match your MySQL Cluster configuration. 
 * 
 * <p>ndbj.connectstring=localhost
 * ndbj.mysqld=localhost
 * ndbj.username=root
 * ndbj.password=
 * ndbj.testDatabase=test
 * 
 * <p>Run the program using the command:
 * >java com.mysql.cluster.ndbj.examples.DeleteUsingPrimaryKey ${NDBJ_HOME}/ndbj_localhost.props
 * 
 * @author jdowling
 */
public class DeleteUsingPrimaryKey {

	
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Correct Usage: prog <nbbj.props>");
			System.exit(-1);
		}
		
		DeleteUsingPrimaryKey deleter = null;
		try {
			deleter = new DeleteUsingPrimaryKey(args[0]);
			deleter.deleteRow();
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
			if (deleter != null) {
				deleter.destroy();
			}			
		}
	}

	
	private NdbClusterConnection conn = null;
	private Ndb ndb = null;
	private NdbTransaction 	trans=null;

	public DeleteUsingPrimaryKey(String configFile) throws NdbApiException
	{
		/**
		 * Load the configuration file to enable binding to:
		 * (1) MySQL Server using ndbj.mysqld (We use the MySQL server to create a table in the database)
		 * (2) NDB Management Server (ndb_mgmd) using ndbj.connectstring
		 */
		ExamplesConfigurator.loadParamsFromPropertiesFile(configFile);
	
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
	
	
	public void deleteRow() throws NdbApiException
	{
		try {
			
		    /**
		     * Create a transaction. A transaction is used to create operations. 
		     * It can hold many operations (in a batch) that are sent down to cluster when the transaction is executed.
		     */
		    trans = ndb.startTransaction();
		    
		    /**
		     * An NdbOperation object represents a Primary Key operation. We can read a row using the supplied primary key.  
		     */
			NdbOperation op = trans.getDeleteOperation("ndbj_test");
			
			/**
			 * We use the equal() method to declare the primary key value for the row we want to read.
			 */
			op.equalInt("id", 1);
			

			/**
			 * Then we can execute the transaction, which causes all operations that were 'gotten' from the transaction to
			 * be executed in the cluster. Here we have only a single operation.
			 * We also pass the 'commit' parameter to indicate that the transaction should be committed to the database.
			 */
			trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
			
				System.out.println("Deleted a row");			
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
