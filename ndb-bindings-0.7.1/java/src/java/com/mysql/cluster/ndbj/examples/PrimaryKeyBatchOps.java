package com.mysql.cluster.ndbj.examples;

import java.sql.Timestamp;
import java.util.ArrayList;

import com.mysql.cluster.ndbj.Ndb;
import com.mysql.cluster.ndbj.NdbApiException;
import com.mysql.cluster.ndbj.NdbClusterConnection;
import com.mysql.cluster.ndbj.NdbApiPermanentException;
import com.mysql.cluster.ndbj.NdbOperation;
import com.mysql.cluster.ndbj.NdbResultSet;
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
 * >java com.mysql.cluster.ndbj.examples.PrimaryKeyBatchOps ${NDBJ_HOME}/ndbj_localhost.props
 * 
 * @author jdowling
 */
public class PrimaryKeyBatchOps {

	
	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("Correct Usage: prog <nbbj.props> <NumberOfRowsToInsert>");
			System.exit(-1);
		}
		
		PrimaryKeyBatchOps batchOps = null;
		try {
			batchOps = new PrimaryKeyBatchOps(args[0], Integer.parseInt(args[1]));
			batchOps.writeRows();
			batchOps.readRows();
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
			if (batchOps != null) {
				batchOps.destroy();
			}			
		}
	}

	
	private NdbClusterConnection conn = null;
	private Ndb ndb = null;
	private NdbTransaction 	trans=null;
	private Timestamp timeStamp;
	private final int numOfRowsToInsert;
	
	public PrimaryKeyBatchOps(String configFile, int numRowsToInsert) throws NdbApiException
	{
		numOfRowsToInsert=numRowsToInsert;
		/**
		 * Load the configuration file to enable binding to:
		 * (1) MySQL Server using ndbj.mysqld (We use the MySQL server to create a table in the database)
		 * (2) NDB Management Server (ndb_mgmd) using ndbj.connectstring
		 */
		ExamplesConfigurator.loadParamsFromPropertiesFile(configFile);

		/**
		 * This helper method drops any existing table in the database (ndbj.database) called 'ndbj_multi_pk'.
		 * It then creates a table called 'ndbj_multi_pk'.
		 */
		ExamplesConfigurator.DropAndCreateTable("ndbj_multi_pk",
				"(`id` INTEGER NOT NULL, `"
				+ "arrivalTime` TIMESTAMP NOT NULL, `"
				+ "firstName` VARCHAR(256) NOT NULL, `"
				+ "lastName` VARCHAR(256) NOT NULL, "
				+"PRIMARY KEY(id,arrivalTime), "
				+ "INDEX lastname_idx(lastName)) ENGINE=ndb;");
	
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

	    timeStamp = new Timestamp(System.currentTimeMillis());
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
	
	public void writeRows() throws NdbApiException
	{
		try {
			
		    /**
		     * Create a transaction. A transaction is used to create operations. 
		     * It can hold many operations (in a batch) that are sent down to cluster when the transaction is executed.
		     */
		    trans = ndb.startTransaction();
		    
		    for (int i=0;i<numOfRowsToInsert;i++) {
			    /**
			     * An NdbOperationRef object represents a Primary Key operation. We can read/write/update/delete a row using
			     * the supplied primary key.  
			     */
				NdbOperation op = trans.getInsertOperation("ndbj_multi_pk");
	
				
				/**
				 * Here we have a multi-part primary key, so we call the equal() method once for
				 * each part of the primary key.
				 */
				op.equalInt("id", i);
				
				op.equalTimestamp("arrivalTime", timeStamp);
				
				/**
				 * We use the set<X>() methods to set the values for columns in the row we are going to insert.
				 * The column 'firstName' is of type 'VARCHAR' (a string), so we call the setString() method to set its value.
				 */
				op.setString("firstName", "Jim " + Integer.toString(i));
				
				/**
				 * The column 'lastName' is of type 'VARCHAR' (a string), so we call the setString() method to set its value.
				 */			
				op.setString("lastName", "Dowling "+ Integer.toString(i%10));
		    }
		    
		    // Lets insert some more rows, with the same 'id' but a different Timestamp
		    
		    for (int i=0;i<numOfRowsToInsert;i++) {
				NdbOperation op = trans.getInsertOperation("ndbj_multi_pk");	
				op.equalInt("id", i);
				op.equalTimestamp("arrivalTime", new Timestamp(System.currentTimeMillis()));
				op.setString("firstName", "Jim");
				op.setString("lastName", "Dowling");
		    }
		    
		    /**
			 * Then we can execute the transaction, which causes all operations that were created using the transaction object to
			 * be executed in the cluster. Here we have a batch consisting of (NUM_OF_ROWS_TO_INSERT*2) operations.
			 * We also pass the 'commit' parameter to indicate that the transaction should be committed to the database.
			 */
			trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
			
			System.out.println("We have written a lot of row containing (0.." + numOfRowsToInsert + ", Some timestamp, 'Jim', 'Dowling') to test.ndbj_multi_pk");
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
	
	public void readRows() throws NdbApiException
	{
		try {
			
		    /**
		     * Create a transaction. A transaction is used to create operations. 
		     * It can hold many operations (in a batch) that are sent down to cluster when the transaction is executed.
		     */
		    trans = ndb.startTransaction();
		    
		    ArrayList<NdbResultSet> rsList= new ArrayList<NdbResultSet>(numOfRowsToInsert); 
		    for (int i=0; i<numOfRowsToInsert; i++) {
		    	
			    /**
			     * An NdbOperation object represents a Primary Key operation. We can read a row using the supplied primary key.
			     * 
			     * The operation will be used to read a row. We need to specify
				 * how we want to lock the row. Here we say LM_CommittedRead - read the
				 * last committed values for the row.  
			     */
				NdbOperation op = trans.getSelectOperation("ndbj_multi_pk",NdbOperation.LockMode.LM_CommittedRead);
	

				/**
				 * Here we have a multi-part primary key, and we call equal() on both parts of the primary key. 
				 */
				op.equalInt("id", i);
				op.equalTimestamp("arrivalTime", timeStamp);
				
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
				if (rs != null) {
					rsList.add(rs);
				}
		    }

			/**
			 * Then we can execute the transaction, which causes all operations that were 'gotten' from the transaction to
			 * be executed in the cluster. Here we have only a single operation.
			 * We also pass the 'commit' parameter to indicate that the transaction should be committed to the database.
			 */
			trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
			
			/**
			 * Loop through all the NdbResultSet objects in the ArrayList
			 */
			int i=0;
			for (NdbResultSet rs : rsList) {
                if (rs.next()) {
    				String firstName = rs.getString("firstName");
                    // You call rs.wasNull() after calling rs.get<>() to check if the column value was SQL-NULL
                    if (rs.wasNull()) {
                        System.out.println("Value read was SQL-NULL");
                    }
    				String lastName = rs.getString("lastName");
                    if (rs.wasNull()) {
                        System.out.println("Value read was SQL-NULL");
                    }
    				System.out.println("Read row '" + i++ + "': (\"" + firstName + "\", \"" + lastName +"\")");
                }
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
