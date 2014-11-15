package com.mysql.cluster.ndbj.examples;

import com.mysql.cluster.ndbj.*;
import java.sql.*;

/**
 * You need to create a the following table in the "test" database:
 * <br><code>
 * mysql-client>create table ndbj_hello 
 * (`id` INTEGER NOT NULL, 
 * `firstName` VARCHAR(255) NOT NULL, 
 * `lastName` VARCHAR(255) NOT NULL,
 * PRIMARY KEY(id)
 * ) ENGINE=ndb;
 * </code>
 * 
 * <BR>Run the program using the command:
 * >java com.mysql.cluster.ndbj.examples.HelloWorldInsert
 * 
 * <BR>You can test the result of the program in the "test" database:
 * <br><code>
 * mysql-client>SELECT * FROM ndbj_hello; 
 * </code>
 * 
 * @author jdowling
 */
public class HelloWorldInsert {


	public static void main(String[] args) throws SQLException {

		/*
        if (args.length != 1) {
            System.err.println("Correct Usage: prog <connectstring>");
            System.exit(-1);
        }*/
        
        String table_name = "ndbj_hello";
        try { 
          Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) { 
          System.out.println("MySQL JDBC Driver not found"); 
        }

        Connection sqlconn = DriverManager.getConnection("jdbc:mysql://localhost/test?user=root");

        System.out.println("Dropping and recreating schema");

        Statement s = sqlconn.createStatement(); 

        s.executeUpdate("DROP TABLE if exists " + table_name);
        s.executeUpdate("CREATE TABLE if not exists " +
    		      table_name + 
    		      "    (id INT not null," +
    		      "     firstName varchar(255) not null, " +
    		      "     lastName  varchar(255) NOT NULL," +
    		      "     PRIMARY KEY (id) )" + 
    		      "  ENGINE=NDBCLUSTER");
        
		NdbClusterConnection conn = null;
		Ndb ndb = null;
		NdbTransaction 	trans=null;
		try {
			/**
			 * Connect to the management server using a NdbClusterConnectionImpl object
			 */
        	conn = NdbClusterConnection.create();
        	
        	/**
        	 * If you cannot connect immediately to the management server, retry connecting to the up to 5 times, 
        	 * with a gap of 3 seconds between attempts, the 'true' sets output to verbose mode.
        	 */
        	if (conn.connect(5,3,true)!=0) { 
		      System.out.println("problem with connect method");
		    }
		    
			/**
			 * Wait 30 seconds for the cluster to be available, throw exception if it is not ready.
			 */
		    conn.waitUntilReady(30,30);
		    
		    /**
             *  Get a connection to a NDB object that is used to create transactions 
	         */
		    ndb = conn.createNdb("test",4);
		   
		            
		    /**
		     * Create a transaction. A transaction is used to create operations. 
		     * It can hold many operations (in a batch) that are sent down to cluster when the transaction is executed.
		     */
		    trans = ndb.startTransaction();
		    
		    /**
		     * An NdbOperationRef object represents a Primary Key operation. We can read/write/update/delete a row using
		     * the supplied primary key.  
		     */
			NdbOperation op = trans.getInsertOperation(table_name);
			
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
			trans.execute(NdbTransaction.ExecType.Commit, NdbOperation.AbortOption.AbortOnError, true);
			System.out.println("Done");
		} 
        /**
         * If you haven't
         */
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
			if (trans != null) {
				trans.close();
			}
			if (ndb != null) {
				ndb.close();
			}
			if (conn != null) {
				conn.close();
			}
			
		}
	}

}
