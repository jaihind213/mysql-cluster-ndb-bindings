/* -*- mode: c++; c-basic-offset: 2; indent-tabs-mode: nil; -*-
 *  vim:expandtab:shiftwidth=2:tabstop=2:smarttab:
 *
 *  ndb-bindings: Bindings for the NDB API
 *  Copyright (C) 2008 MySQL
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */


%include "globals.i"

%{
#include <stdio.h>
#include <vector>

#undef PACKAGE
#undef PACKAGE_NAME
#undef PACKAGE_STRING
#undef PACKAGE_TARNAME
#undef PACKAGE_VERSION
#undef VERSION

#include "config.h"

#include "ndb_init.h"
#include "mgmapi/mgmapi.h"
#include "mgmapi_debug.h"

  enum NdbException {
    NdbMgmException,
    IllegalConnectString,
    IllegalServerHandle,
    IllegalServerReply,
    IllegalNumberOfNodes,
    IllegalNodeStatus,
    OutOfMemory,
    ServerNotConnected,
    CouldNotConnectToSocket,
    BindAddressError,
    AllocIDError,
    AllocIDConfigMismatch,
    StartFailed,
    StopFailed,
    RestartFailed,
    CouldNotStartBackup,
    CouldNotAbortBackup,
    CouldNotEnterSingleUserMode,
    CouldNotExitSigleUserMode,
    UsageError
  };

  enum BackupStartOption {
    DontWait = 0,
    WaitUntilBackupStarted = 1,
    WaitUntilBackupCompleted = 2
  };


  %}


%include "ndb_constants.h"
 //%include "ndb_logevent.h"

#define MGM_LOGLEVELS CFG_MAX_LOGLEVEL - CFG_MIN_LOGLEVEL + 1
#define NDB_MGM_MAX_LOGLEVEL 15



%rename("NdbMgmSeverity") ndb_mgm_severity;
class ndb_mgm_severity {
public:
  ndb_mgm_event_severity category;
  unsigned int value;
};

%rename("NdbMgmLoglevel") ndb_mgm_loglevel;
class ndb_mgm_loglevel {
public:
  enum ndb_mgm_event_category category;
  unsigned int value;
};



%inline %{

  class NdbFilterItem {

  public:
    int level;
    ndb_mgm_event_category category;

    NdbFilterItem(int level=0,
                  ndb_mgm_event_category category=
                  NDB_MGM_ILLEGAL_EVENT_CATEGORY) {
      this->level=level;
      this->category=category;
    }
  };

  %}

/*
class NdbFilterItem {


public:
  int level;
  ndb_mgm_event_category category;

  NdbFilterItem(int level=0,
                ndb_mgm_event_category category=
                NDB_MGM_ILLEGAL_EVENT_CATEGORY);
};
*/

#if !defined(SWIGXML)
%include "std_vector.i"
%template(NdbFilterList) std::vector<NdbFilterItem>;
#endif


%rename("NdbMgm") ndb_mgm_handle;
%rename("NdbLogEvent") ndb_logevent;
%rename("NdbMgmReply") ndb_mgm_reply;
%rename("ClusterState") ndb_mgm_cluster_state;
%rename("NodeState") ndb_mgm_node_state;

%rename("NodeType") ndb_mgm_node_type;
%rename("NodeStatus") ndb_mgm_node_status;
%rename("NdbMgmError") ndb_mgm_error;
%rename("NdbLogEventSeverity") ndb_mgm_event_severity;
%rename("NdbLogEventCategory") ndb_mgm_event_category;
%rename("NdbLogEventType") Ndb_logevent_type;

