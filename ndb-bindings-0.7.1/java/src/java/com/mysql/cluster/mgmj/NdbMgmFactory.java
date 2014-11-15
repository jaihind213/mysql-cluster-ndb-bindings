package com.mysql.cluster.mgmj;

public class NdbMgmFactory {


  public static NdbMgm createNdbMgm(String connectString) throws NdbMgmException {
     NdbMgmImpl mgmHandle = new NdbMgmImpl(connectString);
     if ((mgmHandle == null) || (NdbMgmImpl.getCPtr(mgmHandle) == 0) ) {
    	 throw new NdbMgmException("Couldn't allocate the NdbMgm object");
     }
     return mgmHandle;
  }

  public static NdbMgm createNdbMgm() throws NdbMgmException {
	  NdbMgmImpl mgmHandle = new NdbMgmImpl();
	 if ((mgmHandle == null) || (NdbMgmImpl.getCPtr(mgmHandle) == 0) ) {
    	 throw new NdbMgmException("Couldn't allocate the NdbMgm object");
     }
     return mgmHandle;
  }

}
