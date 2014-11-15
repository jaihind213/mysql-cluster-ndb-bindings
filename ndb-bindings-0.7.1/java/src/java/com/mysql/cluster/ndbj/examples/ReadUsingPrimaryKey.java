package com.mysql.cluster.ndbj.examples;

import com.mysql.cluster.ndbj.Ndb;
import com.mysql.cluster.ndbj.NdbClusterConnection;
import com.mysql.cluster.ndbj.NdbApiException;
import com.mysql.cluster.ndbj.NdbApiPermanentException;
import com.mysql.cluster.ndbj.NdbOperation;
import com.mysql.cluster.ndbj.NdbResultSet;
import com.mysql.cluster.ndbj.NdbTransaction;
import com.mysql.cluster.ndbj.NdbOperation.AbortOption;
import com.mysql.cluster.ndbj.NdbTransaction.ExecType;


public class ReadUsingPrimaryKey {

	
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Correct Usage: prog <nbbj.props>");
			System.exit(-1);
		}
		
		ReadUsingPrimaryKey reader = null;
		try {
			reader = new ReadUsingPrimaryKey(args[0]);
			reader.readRow();
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
			if (reader != null) {
				reader.destroy();
			}			
		}
	}

	
	private NdbClusterConnection conn = null;
	private Ndb ndb = null;
	private NdbTransaction 	trans=null;

	public ReadUsingPrimaryKey(String configFile) throws NdbApiException
	{
		/**
		 * Load the configuration file to enable binding to:
		 * (1) MySQL Server using ndbj.mysqld (We use the MySQL server to create a table in the database)
		 * (2) NDB Management Server (ndb_mgmd) using ndbj.connectstring
		 */
		ExamplesConfigurator.loadParamsFromPropertiesFile(configFile);
	
		/**
		 * Connect to the management server using a NdbClusterConnectionRef object
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
	
	
	public void readRow() throws NdbApiException
	{
		try {
			
		    /**
		     * Create a transaction. A transaction is used to create operations. 
		     * It can hold many operations (in a batch) that are sent down to cluster when the transaction is executed.
		     */
		    trans = ndb.startTransaction();
		    
		    /**
		     * An NdbOperationRef object represents a Primary Key operation. We can read a row using the supplied primary key.
		     * 
		     * The operation will be used to read a row. We need to specify
			 * how we want to lock the row. Here we say LM_CommittedRead - read the
			 * last committed values for the row.  
		     */
			NdbOperation op = trans.getSelectOperation("ndbj_test",NdbOperation.LockMode.LM_CommittedRead);

			
			/**
			 * We use the equal() method to declare the primary key value for the row we want to read.
			 */
			op.equalInt("id", 1);
			
			/**
			 * We use the getValue() methods to indicate that we want to retrieve the value for a column in the NdbResultSet.
			 * Here we say that we will want to get the value of the column 'firstName'.
			 */
			op.getValue("firstName");
			
			/**
			 * Here we say that we will want to get the value of the column 'lastName'.
			 */
			op.getValue("lastName");
			
			/**
			 * We need to get a NdbResultSet for the operation in order to read the values of any columns returned if the row is found.
			 */
			NdbResultSet rs = op.resultData();


			/**
			 * Then we can execute the transaction, which causes all operations that were 'gotten' from the transaction to
			 * be executed in the cluster. Here we have only a single operation.
			 * We also pass the 'commit' parameter to indicate that the transaction should be committed to the database.
			 */
			trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
			
			/**
			 * This is a primary key operation, so we only expect a single row to be returned (or not returned).
			 * For scans, you normally call "while (rs.next) { ...."
			 */
			if (rs.next()) {
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				System.out.println("We have read a row with primary key '1' containing: (\"" + firstName + "\", \"" + lastName +"\")");
			}
			else {
				System.out.println("Something went wrong. Could not find the row using the supplied equals() value. Try using the mysql client \"select * from test_ndbj where id='X';\"");
			}
			
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
