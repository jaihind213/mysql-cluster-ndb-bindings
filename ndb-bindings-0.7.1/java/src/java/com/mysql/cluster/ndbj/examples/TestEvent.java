package com.mysql.cluster.ndbj.examples;

import java.sql.*;
import com.mysql.cluster.ndbj.*;


public class TestEvent { 


  static { 
    System.loadLibrary("ndbj");
  }


  public static void main(String argv[]) throws SQLException{ 

    if (argv.length<5) { 
      System.out.println("Usage:\n\tjava testevent <connstring> <database> <tablename> <eventname> <polltime>"); 
      System.exit(1);
    }

    String connectString=argv[0];
    String database=argv[1];
    String tableName=argv[2];
    String eventName=argv[3];
    int pollTime=Integer.parseInt(argv[4]);

    System.out.println("connecting to cluster");
    
    try { 
      NdbClusterConnection connection = NdbClusterConnection.create(connectString);
      
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

      Ndb myNdb = connection.createNdb( database , 4) ;



      NdbDictionary dict=myNdb.getDictionary();
      NdbTable table = dict.getTable(tableName);

      System.out.println("running tests:");
      
      
      NdbEventOperation eop = myNdb.createEventOperation(eventName);	 
      
      NdbColumn col=null;
      System.out.println("Number of Colums=" + table.getNoOfColumns());
      for(int i=0; i<table.getNoOfColumns(); i++)
	  {
	      col=table.getColumn(i);
	      String name=col.getName();
	      System.out.println("Setting up column : " + name); 
	      eop.getValue(name);	      	      
	      eop.getPreValue(name);
	  }
      NdbResultSet theResultSet = eop.resultData();
      NdbResultSet preResultSet = eop.preResultData();
     
      
      
      System.out.println("done");

      eop.execute();
      int i=0;

      int counter=0, evt=1000;
      
      while(i<10000)
	  {
	      System.out.println("polling");
	      int r = myNdb.pollEvents(pollTime);
	      if(r>0)
		  {

		      while((eop=myNdb.nextEvent())!= null)
			  {
			      if(eop.isOverrun()>0)
				  {
				      System.out.println("Fook");
				  }

			      if(!eop.isConsistent())
				  {
				      System.out.println("Fook");
				  }
			      	
			      NdbColumn colOne=table.getColumn(0);
				  String nameOne=colOne.getName();

			      NdbColumn colTwo=table.getColumn(0);
				  String nameTwo=colTwo.getName();
			      System.out.print("a_curr=" + 
			    		  theResultSet.getInt(nameOne));
			      System.out.print("a_pre=" + 
			    		  preResultSet.getInt(nameOne));
			      System.out.print("b_curr=" + 
			    		  theResultSet.getString(nameTwo));
			      System.out.print("b_pre=" + 
			    		  preResultSet.getInt(nameTwo));
	
			      System.out.print("\n");
			     

			      theResultSet.getString(nameTwo);
			 
			      counter++;
			  }

		      if(counter % evt == 0 )
			  {
			      System.out.println("received "+ evt +  "events");
			  }
		      
		  }
	      i++;
	      //      stopTime = System.currentTimeMillis()-startTime;
	  }
      myNdb.dropEventOperation(eop);
      
      
      
  }
  catch(Exception e)
      {
	  e.printStackTrace();
      }
  
  }
}

