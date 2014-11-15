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

#ifndef NdbEventListener_h
#define NdbEventListener_h

#include <map>

#include "libmgmpp.h"

class NdbLogEventListener { 
public: 
  virtual ~NdbLogEventListener() {} 
  virtual void le_handleEvent(const ndb_logevent & event) {(void)event;}
};
class NdbLogEventCategoryListener { 
public: 
  virtual ~NdbLogEventCategoryListener() {} 
  virtual void le_handleEvent(const ndb_logevent & event) {(void)event;}
  virtual ndb_mgm_event_category getEventCategory() { return NDB_MGM_ILLEGAL_EVENT_CATEGORY; }
};
class NdbLogEventTypeListener { 
public: 
  virtual ~NdbLogEventTypeListener() {} 
  virtual void le_handleEvent(const ndb_logevent & event) {(void)event;}
  virtual Ndb_logevent_type getEventType() { return NDB_LE_ILLEGAL_TYPE; } 
  };


struct BaseEventWrapper { 
  int ret; 
  ndb_logevent * theEvent; 
};



class NdbLogEventManager { 

private: 
  
  ndb_logevent_handle * handle; 
  
  std::map<Ndb_logevent_type,NdbLogEventTypeListener *> evtListeners; 
  std::map<ndb_mgm_event_category,NdbLogEventCategoryListener *> evtCategoryListeners;
  
public: 
  
  NdbLogEventManager(ndb_logevent_handle * theHandle);
   
  ~NdbLogEventManager();

  const char * getMgmError();

  // Returns -1 on error, 0 otherwise
  int registerListener(NdbLogEventTypeListener * listener);
    
  // Returns -1 on error, 0 otherwise
  int registerListener(NdbLogEventCategoryListener * listener);

 
  bool unregisterListener(NdbLogEventTypeListener * listener);
  bool unregisterListener(NdbLogEventCategoryListener * listener);
    
  
  BaseEventWrapper * getLogEvent(unsigned timeout_in_milliseconds);
  int pollEvents(unsigned timeout_in_milliseconds);
  };
    

#endif
