package com.mysql.cluster.ndbj.examples;

import com.mysql.cluster.ndbj.Ndb;
import com.mysql.cluster.ndbj.NdbApiException;
import com.mysql.cluster.ndbj.NdbBlob;
import com.mysql.cluster.ndbj.NdbClusterConnection;
import com.mysql.cluster.ndbj.NdbApiPermanentException;
import com.mysql.cluster.ndbj.NdbOperation;
import com.mysql.cluster.ndbj.NdbResultSet;
import com.mysql.cluster.ndbj.NdbTransaction;
import com.mysql.cluster.ndbj.NdbOperation.AbortOption;
import com.mysql.cluster.ndbj.NdbTransaction.ExecType;


public class PrimaryKeyOpBlob {

	
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Correct Usage: prog <nbbj.props>");
			System.exit(-1);
		}
		
		PrimaryKeyOpBlob blobber = null;
		try {
			blobber = new PrimaryKeyOpBlob(args[0]);
			blobber.insertRow();
			blobber.readRowKnownLengthOfBlob();
			blobber.readRowUnknownLengthOfBlob();
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
			if (blobber != null) {
				blobber.destroy();
			}			
		}
	}

	
	private NdbClusterConnection conn = null;
	private Ndb ndb = null;
	private NdbTransaction 	trans=null;

	public PrimaryKeyOpBlob(String configFile) throws NdbApiException
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
				+ "data` BLOB NOT NULL, "
				+"PRIMARY KEY(id)) ENGINE=ndb;");
	
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

			NdbBlob bh = op.getBlobHandle("data");
			bh.setValue("Rob..".getBytes());
			
			/**
			 * Then we can execute the transaction, which causes all operations that were 'gotten' from the transaction to
			 * be executed in the cluster. Here we have only a single operation.
			 * We also pass the 'commit' parameter to indicate that the transaction should be committed to the database.
			 */
			trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
			
			System.out.println("Wrote a row containing (1, 'Rob...') to test.ndbj_blob");
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
	
	public void readRowKnownLengthOfBlob() throws NdbApiException
	{
		try {
			
			System.out.println("Reading a Blob where we know the amount of memory required to store the Blob (efficient).");
			
		    /**
		     * Create a transaction. A transaction is used to create operations. 
		     * It can hold many operations (in a batch) that are sent down to cluster when the transaction is executed.
		     */
		    trans = ndb.startTransaction();
		    
		    /**
		     * An NdbOperation object represents a Primary Key operation. We can read a row using the supplied primary key.  
		     */
			NdbOperation op = trans.getSelectOperation("ndbj_test",NdbOperation.LockMode.LM_CommittedRead);
			
			/**
			 * We use the equal() method to declare the primary key value for the row we want to read.
			 */
			op.equalInt("id", 1);
			

			int blobSize = "Rob..".length();
			op.getBlob("data",blobSize);

			NdbResultSet rs = op.resultData();

			/**
			 * Then we can execute the transaction, which causes all operations that were 'gotten' from the transaction to
			 * be executed in the cluster. Here we have only a single operation.
			 * We also pass the 'commit' parameter to indicate that the transaction should be committed to the database.
			 */
			trans.execute(ExecType.Commit,
					AbortOption.AbortOnError, true);
			
			/**
			 * This is a primary key operation, so we only expect a single row to be returned (or not returned).
			 * For scans, you normally call "while (rs.next) { ...."
			 */
            
			if (rs.next()) {
                NdbBlob blob = rs.getBlob("data");
				byte[] buf = blob.getData();

				System.out.println("Read the Blob Value:");
				for (int i = 0; i < blobSize; i++) {
					System.out.print((char) buf[i]);
				}
				System.out.println();
			}
			else {
				System.out.println("Something went wrong. Could not find the row using the supplied equals() value. Try using the mysql client \"select * from test_blob where id='X';\"");
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
	
	public void readRowUnknownLengthOfBlob() throws NdbApiException
	{
		try {
			System.out.println("Reading a Blob where we do not know the amount of memory required to store the Blob.");		
		    /**
		     * Create a transaction. A transaction is used to create operations. 
		     * It can hold many operations (in a batch) that are sent down to cluster when the transaction is executed.
		     */
		    trans = ndb.startTransaction();
		    
		    /**
		     * An NdbOperationRef object represents a Primary Key operation. We can read a row using the supplied primary key.  
		     */
			NdbOperation op = trans.getSelectOperation("ndbj_test",NdbOperation.LockMode.LM_CommittedRead);

			
			/**
			 * We use the equal() method to declare the primary key value for the row we want to read.
			 */
			op.equalInt("id", 1);
			

			op.getBlob("data");

			NdbResultSet rs = op.resultData();
			NdbBlob b = rs.getBlob("data");


			/**
			 * Then we can execute the transaction, which causes all operations that were 'gotten' from the transaction to
			 * be executed in the cluster. Here we have only a single operation.
			 * We also pass the 'commit' parameter to indicate that the transaction should be committed to the database.
			 */
			trans.execute(ExecType.NoCommit,
					AbortOption.AbortOnError, true);
			
			/**
			 * This is a primary key operation, so we only expect a single row to be returned (or not returned).
			 * For scans, you normally call "while (rs.next) { ...."
			 */
			if (rs.next()) {
				// TODO: Need to figure out how to deal with these Uint64 values
				int len = b.getLength().intValue();
				System.out.println("Length of Blob : " + len);
				byte[] buf = new byte[len];
				b.readData(buf, len);

				System.out.println("Read the Blob Value:");
				for (int i = 0; i < len; i++) {
					System.out.print((char) buf[i]);
				}
				System.out.println();
			}
			else {
				System.out.println("Something went wrong. Could not find the row using the supplied equals() value. Try using the mysql client \"select * from test_blob where id='X';\"");
			}

			trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);

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
