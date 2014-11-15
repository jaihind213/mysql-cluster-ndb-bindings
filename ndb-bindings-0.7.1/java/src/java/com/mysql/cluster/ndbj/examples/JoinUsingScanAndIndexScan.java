package com.mysql.cluster.ndbj.examples;

import java.util.ArrayList;

import com.mysql.cluster.ndbj.Ndb;
import com.mysql.cluster.ndbj.NdbApiException;
import com.mysql.cluster.ndbj.NdbClusterConnection;
import com.mysql.cluster.ndbj.NdbApiPermanentException;
import com.mysql.cluster.ndbj.NdbIndexScanOperation;
import com.mysql.cluster.ndbj.NdbOperation;
import com.mysql.cluster.ndbj.NdbResultSet;
import com.mysql.cluster.ndbj.NdbScanOperation;
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
 * >java com.mysql.cluster.ndbj.examples.ReadRowsUsingOrderedIndexOnPrimaryKey ${NDBJ_HOME}/ndbj_localhost.props
 * 
 * @author jdowling
 */
public class JoinUsingScanAndIndexScan {

	
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Correct Usage: prog <nbbj.props>");
			System.exit(-1);
		}
		
		JoinUsingScanAndIndexScan operations = null;
		try {
			operations = new JoinUsingScanAndIndexScan(args[0]);
			operations.insertRows();
			operations.joinOnScanAndOrderedIndexMerge();
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
			if (operations != null) {
				operations.destroy();
			}			
		}
	}

	
	private NdbClusterConnection conn = null;
	private Ndb ndb = null;
	private NdbTransaction 	trans=null;
	
	public JoinUsingScanAndIndexScan(String configFile) throws NdbApiException
	{
		/**
		 * Load the configuration file to enable binding to:
		 * (1) MySQL Server using ndbj.mysqld (We use the MySQL server to create a table in the database)
		 * (2) NDB Management Server (ndb_mgmd) using ndbj.connectstring
		 */
		ExamplesConfigurator.loadParamsFromPropertiesFile(configFile);
	
		ExamplesConfigurator.DropAndCreateTable("ndbj_join_a",
				"(`id` INTEGER NOT NULL, `"
				+ "value` INTEGER NOT NULL,"
				+"PRIMARY KEY(id)) ENGINE=ndb;");

		ExamplesConfigurator.DropAndCreateTable("ndbj_join_b",
				"(`id` INTEGER NOT NULL, `"
				+ "id_a` INTEGER NOT NULL, `"
				+ "value` INTEGER NOT NULL,"
				+ "INDEX idx_id_a(id_a),"
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
	
	public void insertRows() throws NdbApiException
	{
		try {
			
		    /**
		     * Create a transaction. A transaction is used to create operations. 
		     * It can hold many operations (in a batch) that are sent down to cluster when the transaction is executed.
		     */
		    trans = ndb.startTransaction();
		    
		    for (int i=0;i<7;i++) {
			    /**
			     * An NdbOperationRef object represents a Primary Key operation. We can read/write/update/delete a row using
			     * the supplied primary key.  
			     */
				NdbOperation op = trans.getInsertOperation("ndbj_join_a");
	
				
				op.equalInt("id", i);
								
				/**
				 * We use the set<X>() methods to set the values for columns in the row we are going to insert.
				 * The column 'firstName' is of type 'VARCHAR' (a string), so we call the setString() method to set its value.
				 */
				op.setInt("value", 46);				
		    }
		    
		    // Lets insert some more rows, with the same 'id' but a different Timestamp
		    
		    for (int i=0;i<5;i++) {
				NdbOperation op = trans.getInsertOperation("ndbj_join_b");	
				op.equalInt("id", i);
				op.setInt("id_a", i);				
				op.setInt("value", i);				
		    }
		    
		    /**
			 * Then we can execute the transaction, which causes all operations that were created using the transaction object to
			 * be executed in the cluster. Here we have a batch consisting of (NUM_OF_ROWS_TO_INSERT*2) operations.
			 * We also pass the 'commit' parameter to indicate that the transaction should be committed to the database.
			 */
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
	

	public void joinOnScanAndOrderedIndexMerge() throws NdbApiException
	{
		try {

			System.out.println("Extracting all of the 'id' attributes from table 'ndbj_join_a' using a scan operation.");
			
		    /**
		     * Create a transaction. A transaction is used to create operations. 
		     * It can hold many operations (in a batch) that are sent down to cluster when the transaction is executed.
		     */
		    trans = ndb.startTransaction();
		    
		    NdbScanOperation op_a = trans.getSelectScanOperation("ndbj_join_a", NdbOperation.LockMode.LM_CommittedRead, 0, 0, 0);
			
			op_a.getValue("id");
			NdbResultSet rs_a = op_a.resultData();

			
			/**
			 * Then we can execute the transaction, which executes the scan operation that reads 
			 * be executed in the cluster. Here we have only a single operation.
			 * We also pass the 'commit' parameter to indicate that the transaction should be committed to the database.
			 */
			trans.execute(ExecType.NoCommit, AbortOption.AbortOnError, true);
			
			ArrayList<NdbResultSet> rs_b_list = new ArrayList<NdbResultSet>();

			while (rs_a.next() != false) {		
			    /**
			     * An NdbOperationRef object represents a Primary Key operation. We can read a row using the supplied primary key.  
			     * 
				 * This operation read rows from table 'b' using the ordered index
				 * on the primary key. We need to specify how we want to lock the row. Here we say LM_CommittedRead - 
				 * read the last committed values for the row.columnId
				 */

				NdbIndexScanOperation op_b = trans.getSelectIndexScanOperation("idx_id_a","ndbj_join_b",
						NdbOperation.LockMode.LM_Read, NdbScanOperation.ScanFlag.DESCENDING, 0, 0);
				
				/**
				 * Here we have a multi-part primary key, and we call equal() on both parts of the primary key. 
				 */
				int id = rs_a.getInt("id");
				System.out.println("ndbj_join_a.id=" + id);
				op_b.equalInt("id_a", id);
				
				/**
				 * We use the getValue() methods to indicate that we want to retrieve the value for a column in the NdbResultSet.
				 * Here we say that we will want to get the value of the column 'firstName'.
				 */
				op_b.getValue("id_a");
				op_b.getValue("value");
			
				rs_b_list.add(op_b.resultData());
			}			
			/**
			 * We need to get a NdbResultSet for the operation in order to read the values of any columns returned if the row is found.
			 */
			System.out.println("Looking in table 'ndbj_join_b' for all 'id_a' attributes that match the extracted 'id' attributes");
			System.out.println("Used the index 'idx_id_a' on 'ndbj_join_b' to improve the speed of searching the table for matches");


			/**
			 * Then we can execute the transaction, which causes all operations that were 'gotten' from the transaction to
			 * be executed in the cluster. Here we have only a single operation.
			 * We also pass the 'commit' parameter to indicate that the transaction should be committed to the database.
			 */
			trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
			
			/**
			 * This batch of index scan operations returned an ArrayList of NdbResultSet objects.
			 * We iterate through the NdbResultSet objects.
			 */
			for (NdbResultSet rs : rs_b_list) {
				if (rs.next() != false) {
					int value = rs.getInt("value");
					int id_a = rs.getInt("id_a");
					System.out.println("Found ndbj_join_b('id_a','value') = (\"" + id_a + "\", \"" + value +"\")");
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
