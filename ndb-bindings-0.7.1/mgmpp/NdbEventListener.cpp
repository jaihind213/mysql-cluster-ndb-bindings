// -*- mode: c++ -*- 
/*  ndb-bindings: Bindings for the NDB API
    Copyright (C) 2006 MySQL, Inc.
    
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or 
    (at your option) any later version.
    
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/


#include <map>
#include <stdlib.h>

#include "NdbEventListener.hpp"
  
  
NdbLogEventManager::NdbLogEventManager(ndb_logevent_handle * theHandle) { 
  this->handle=theHandle;
}

NdbLogEventManager::~NdbLogEventManager() {
  ndb_mgm_destroy_logevent_handle(&handle);
}

const char * NdbLogEventManager::getMgmError() { 
  return ndb_logevent_get_latest_error_msg(this->handle); 
}

// Returns -1 on error, 0 otherwise
int NdbLogEventManager::registerListener(NdbLogEventTypeListener * listener) { 
  Ndb_logevent_type theType = listener->getEventType();
  
  std::map<Ndb_logevent_type,NdbLogEventTypeListener *>::iterator it; 
  it=evtListeners.find(theType);
  if (it==evtListeners.end()) { 
    // We don't have this one yet
    evtListeners[theType]=listener;
    return 0;
  }       
  // Must unregisterListener first
  return -1;
}

// Returns -1 on error, 0 otherwise
int NdbLogEventManager::registerListener(NdbLogEventCategoryListener * listener) { 
  ndb_mgm_event_category theCategory = listener->getEventCategory();
  
  
  std::map<ndb_mgm_event_category,NdbLogEventCategoryListener *>::iterator it; 
  it=evtCategoryListeners.find(theCategory);
  
  if(it==evtCategoryListeners.end()) { 
    evtCategoryListeners[theCategory]=listener;
    return 0; 
  } 
  return -1;
}

bool NdbLogEventManager::unregisterListener(NdbLogEventTypeListener * listener) 
{ 
  evtListeners.erase(evtListeners.find(listener->getEventType()));
  return true;
}

bool NdbLogEventManager::unregisterListener(NdbLogEventCategoryListener * listener) 
{ 
  evtCategoryListeners.erase
    (
     evtCategoryListeners.find(listener->getEventCategory())
     );
  return true;
}


int NdbLogEventManager::pollEvents(unsigned timeout_in_milliseconds)
{
  ndb_logevent event; 
  int ret = 0;
  ret = ndb_logevent_get_next(handle,&(event),timeout_in_milliseconds);

  if (ret > 0) { 
    // We got an event
    std::map<Ndb_logevent_type,NdbLogEventTypeListener *>::iterator evtit; 
    evtit=evtListeners.find(event.type);
    if (evtit!=evtListeners.end()) {
      ((*evtit).second)->le_handleEvent(event);
    }

    std::map<ndb_mgm_event_category,NdbLogEventCategoryListener *>::iterator catit; 
    catit=evtCategoryListeners.find(event.category);
    
    if(catit!=evtCategoryListeners.end()) { 
      ((*catit).second)->le_handleEvent(event);
    }
  }
  return ret;

}

BaseEventWrapper * NdbLogEventManager::getLogEvent(unsigned timeout_in_milliseconds) 
{ 
  
  //TODO: replace malloc with a pinned byte-array, until get_next_event()
  // is called, then unpin it
  
  /*ndb_logevent* event = (ndb_logevent*) malloc (sizeof(ndb_logevent));
    if (event==0) { 
    return NULL; 
    }*/
  ndb_logevent * event = (ndb_logevent*) malloc (sizeof(ndb_logevent));
  if (event==0) { 
    return NULL; 
  }
  BaseEventWrapper * eventWrapper = (BaseEventWrapper *)malloc(sizeof(BaseEventWrapper)); 
  
  //int r= ndb_logevent_get_next(handle,&(*event),timeout_in_milliseconds);
  eventWrapper->ret = ndb_logevent_get_next(handle,&(*event),timeout_in_milliseconds);
  
  eventWrapper->theEvent = NULL;
  //TODO: Figure out how to deal with no event properly
  if (eventWrapper->ret > 0) { 
    eventWrapper->theEvent = event;
  }
  
  return eventWrapper; 
}
      


    

