package com.mysql.cluster.mgmj;

import java.util.List;

abstract class NdbMgmFilterListImpl {

  abstract public NdbLogEventManagerImpl createNdbLogEventManager(NdbFilterList filter) throws NdbMgmException;
 
  public NdbLogEventManagerImpl createNdbLogEventManager(List<NdbFilterItem> filterList) 
    throws NdbMgmException {
    
    NdbFilterList theList = new NdbFilterList();
    for (NdbFilterItem theItem : filterList) { 
      theList.add(theItem);
    }
    return createNdbLogEventManager(theList);
  }
}
