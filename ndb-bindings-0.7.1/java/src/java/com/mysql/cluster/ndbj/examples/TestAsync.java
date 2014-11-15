package com.mysql.cluster.ndbj.examples;

import java.sql.*;

import java.util.Date;
import java.util.ArrayList;
import com.mysql.cluster.ndbj.*;



public class TestAsync { 

  private static Date beginTime;
  private static Date endTime;

  private static int num_iter = 0; 
  private static int INSERT_NUM = 0; 

  static { 
    System.loadLibrary("ndbj");
  }


  public static void main(String argv[]) throws SQLException{ 

    if (argv.length<2) { 
      System.out.println("Usage:\n\tjava test NUM_OF_ITERATIONS NUM_OF_ROWS "); 
      System.exit(1);
    }

    num_iter = Integer.parseInt(argv[0]);
    INSERT_NUM = Integer.parseInt(argv[1]);
    int BATCH_SIZE=100;


    /**************************************************************
     * Connect to mysql server and create table                   *
     **************************************************************/
    
    String table_name = "mytablename";
    try { 
      Class.forName("com.mysql.jdbc.Driver");
    } catch (ClassNotFoundException e) { 
      System.out.println("MySQL JDBC Driver not found"); 
    }

    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/test?user=root");

    System.out.println("Dropping and recreating schema");

    Statement s = conn.createStatement(); 

    
    try { 
      s.executeUpdate("DROP TABLE " + table_name);
    } catch (SQLException e) { 
    	System.out.println("Couldn't drop table: " + table_name);
    }
    try { 
      s.executeUpdate("CREATE TABLE " +
		      table_name + 
		      "    (ATTR1 INT UNSIGNED auto_increment not null," +
		      "     ATTR2 INT UNSIGNED NOT NULL," +
		      "     PRIMARY KEY (ATTR1) )" + 
		      "  ENGINE=NDBCLUSTER");
    } catch (SQLException e) { 
    	System.out.println("Couldn't create table: " + table_name);
    }
    

    /**************************************************************
     * Connect to ndb cluster                                     *
     **************************************************************/

    System.out.println("connecting to cluster");

  try { 
    NdbClusterConnection connection = NdbClusterConnection.create();

    try {
    if (connection.connect(5,3,true)==-1) { 
      System.out.println("Connect to cluster management server failed.");
      System.exit(-1);
    }
    } catch (Exception e) { 
      System.out.println(e.getMessage());
      System.exit(-1);
    }
      

    
    try { 
       connection.waitUntilReady(30,30); 
    } catch (Exception e) { 
        System.out.println("Cluster was not ready within 30 secs.");
    	System.exit(-1);
    }

    Ndb myNdb = connection.createNdb( "test" , 1024) ;
    NdbDictionary myDict = myNdb.getDictionary();
    NdbTable myTable = myDict.getTable(table_name);
    System.out.println("running tests:");

    /**************************************************************************
     * Fill table with tuples, using auto_increment IDs                       *
     **************************************************************************/

    beginTime = new Date(System.currentTimeMillis());


    for(int t=0;t<Math.ceil(INSERT_NUM/BATCH_SIZE);t++) { 

      NdbTransaction myTransaction = myNdb.startTransaction();

      int val = ((t+1)*BATCH_SIZE)-INSERT_NUM;
      int offset = 0;
      if ( val > 0 ) {
	offset = val; 
      }

      for(int i=0;i<BATCH_SIZE-offset;i++) { 


    	  NdbOperation myOperation = myTransaction.getInsertOperation(myTable);

    	  myOperation.equalInt("ATTR1",((t-1)*BATCH_SIZE)+i);
    	  myOperation.setLong("ATTR2", t*BATCH_SIZE+i);

      }      

      myTransaction.execute( NdbTransaction.ExecType.Commit, NdbOperation.AbortOption.AbortOnError, true ); 



      myTransaction.close();


    }
    endTime = new Date(System.currentTimeMillis());
    System.out.println("Insert time for " + INSERT_NUM + ":");
    System.out.println("   " + (endTime.getTime() - beginTime.getTime()) + "ms"); 


    /*********************************
     * Get list of ids
     *********************************/

    System.out.println();
    System.out.println("Getting list of ids");

    NdbTransaction myTransaction = myNdb.startTransaction();

    NdbScanOperation myScanOperation=myTransaction.getSelectScanOperation(table_name,NdbOperation.LockMode.LM_CommittedRead,0,0,0);
    
    myScanOperation.getValue("ATTR1");
    

	NdbResultSet rs = myScanOperation.resultData();
    myTransaction.executeNoCommit();

    ArrayList<Integer> ids = new ArrayList<Integer>();

    while (true) { 
      
      if (myScanOperation.nextResult(true) != 0) { 
	break;
      }
      
      int random_id = rs.getInt("ATTR1");
      ids.add(new Integer(random_id));
    }
      
    myTransaction.close();

    /*******************
     * Test NDB API Speed
     *******************/
    
    System.out.println("Testing NDBAPI speed");
 
    beginTime = new Date(System.currentTimeMillis());
    for(int x=0;x<num_iter;x++){ 
      
      int rand_id = (int) (Math.random() * ids.size() ) ; 
      int id_num = ids.get(rand_id).intValue();

      NdbTransaction myTrans = myNdb.startTransaction(); //(table_name,id_num);
      
      
      NdbOperation myOper = myTrans.getSelectOperation(table_name,NdbOperation.LockMode.LM_Read);
      
      myOper.equalInt("ATTR1",id_num);
      
      myOper.getValue("ATTR2"); 
      
      
      rs = myOper.resultData();
      
      myTrans.executeCommit(); 


      rs.getInt("ATTR2");
      myTrans.close();
    }
    endTime = new Date(System.currentTimeMillis());
    System.out.println("NDBAPI Execution time for " + num_iter + ": ");
    System.out.println("   "+(endTime.getTime() - beginTime.getTime())+"ms");
    s.close();
    

    System.out.println("Testing NDBAPI async speed");

    
    ArrayList<TestBaseCallback> cbs = new ArrayList<TestBaseCallback>();

    beginTime = new Date(System.currentTimeMillis());
    for(int x=0;x<num_iter;x++){ 
      
      int rand_id = (int) (Math.random() * ids.size() ) ; 
      int id_num = ids.get(rand_id).intValue();

      NdbTransaction myTrans = myNdb.startTransaction(); //table_name,id_num);
      
      
      NdbOperation myOper = myTrans.getSelectOperation(table_name,NdbOperation.LockMode.LM_Read);
      
      myOper.equalInt("ATTR1",id_num);
    
      myOper.getValue("ATTR2"); 

      rs = myOper.resultData();
      TestBaseCallback cb = new TestBaseCallback(myNdb,myTrans,rs);
      
      myTrans.executeAsynchPrepare(NdbTransaction.ExecType.Commit, cb, NdbOperation.AbortOption.AbortOnError);
      cbs.add(cb);
//      foo=myRecAttr.int32_value();
//      myNdb.closeTransaction(myTrans);
    }

    myNdb.sendPollNdb(5000);
    endTime = new Date(System.currentTimeMillis());
    System.out.println("NDBAPI Execution time for " + num_iter + ": ");
    System.out.println("   "+(endTime.getTime() - beginTime.getTime())+"ms");
    s.close();
    
  } catch ( NdbApiException e ) { 
    e.printStackTrace();
    System.exit(-1);
  }

  }
}
