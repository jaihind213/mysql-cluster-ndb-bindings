package com.mysql.cluster.ndbj.examples;

import com.mysql.cluster.ndbj.*;


class TestBaseCallback extends BaseCallback { 

  NdbResultSet myRs; 
  Ndb myNdb; 

  public TestBaseCallback(Ndb theNdb, NdbTransaction theTrans, NdbResultSet theRs) { 
	  super(theTrans);
	  this.myRs=theRs;
	  this.myNdb=theNdb; 
  }

  @Override
  public void callback(int result) { 
    //System.out.println("result " + result + " value: " + this.myRs.getInt("ATTR));
    try { 
    	System.out.println("value " + this.myRs.getInt("ATTR2"));
    } catch (NdbApiException e) { 
    	System.out.println("Got an exception in the callback: " + e.getMessage());
    }
  };
}
