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

%module "mgmapi"

%include "mgmapi/mgmglobals.i"

%typemap(in) int filter[] { 
  // Input will be array of tuples
  int size = RARRAY($input)->len;
//  int filter[size*2+1];
  $1 = (int *) malloc((size*2+1)*sizeof(int));
  int i=0;
  for (i=0;i<size;i++) { 
    VALUE elem = rb_ary_entry($input,i);
    int elemsize = RARRAY(elem)->len;
    for (int j=0;j<elemsize;j++) { 
      $1[i++]=NUM2INT(rb_ary_entry(elem,j));
    }
  }
  //filter[i]=0; 
  $1[i]=0;
}

%{ 

#define NDB_exception(code,msg) do { ndb_raise_exception(code, msg); SWIG_fail; } while(0);

#define getExceptionMethod(excptype,eparent) \
  SWIGINTERN VALUE \
get ## excptype () { \
  static int init ## excptype = 0 ; \
  static VALUE rb_e ## excptype ; \
  VALUE rb_eparent = ndb_get_exception (eparent); \
  if (! init ## excptype ) { \
    init ## excptype = 1; \
    rb_e ## excptype = rb_define_class(#excptype , rb_eparent); \
  } \
  return rb_e ## excptype ; \
}

SWIGINTERN VALUE getNdbMgmException() {
  static int initNdbMgmException = 0 ;
  static VALUE rb_eNdbMgmException = NULL;
  if (! initNdbMgmException ) {
    initNdbMgmException = 1;
    rb_eNdbMgmException = rb_define_class("NdbMgmException", rb_eRuntimeError);
  }
  return rb_eNdbMgmException ;
}

VALUE ndb_get_exception(NdbException excpcode);

getExceptionMethod(IllegalConnectString,NdbMgmException)
getExceptionMethod(IllegalServerHandle,NdbMgmException)
getExceptionMethod(IllegalServerReply,NdbMgmException)
getExceptionMethod(IllegalNumberOfNodes,NdbMgmException)
getExceptionMethod(IllegalNodeStatus,NdbMgmException)
getExceptionMethod(OutOfMemory,NdbMgmException)
getExceptionMethod(ServerNotConnected,NdbMgmException)
getExceptionMethod(CouldNotConnectToSocket,NdbMgmException)
getExceptionMethod(BindAddressError,NdbMgmException)
getExceptionMethod(AllocIDError,NdbMgmException)
getExceptionMethod(AllocIDConfigMismatch,NdbMgmException)
getExceptionMethod(StartFailed,NdbMgmException)
getExceptionMethod(StopFailed,NdbMgmException)
getExceptionMethod(RestartFailed,NdbMgmException)
getExceptionMethod(CouldNotStartBackup,NdbMgmException)
getExceptionMethod(CouldNotAbortBackup,NdbMgmException)
getExceptionMethod(CouldNotEnterSingleUserMode,NdbMgmException)
getExceptionMethod(CouldNotExitSigleUserMode,NdbMgmException)
getExceptionMethod(UsageError,NdbMgmException)

void ndb_raise_exception(NdbException excpcode, const char * msg) {
  rb_raise(ndb_get_exception(excpcode),msg);
}


VALUE ndb_get_exception(NdbException excpcode) {

 VALUE exception;

 switch (excpcode) {
 case NdbMgmException:
   exception = getNdbMgmException();
   break;
 case IllegalConnectString:
   exception = getIllegalConnectString();
   break;
 case IllegalServerHandle:
   exception = getIllegalServerHandle();
   break;
 case IllegalServerReply:
   exception = getIllegalServerReply();
   break;
 case IllegalNumberOfNodes:
   exception = getIllegalNumberOfNodes();
   break;
 case IllegalNodeStatus:
   exception = getIllegalNodeStatus();
   break;
 case OutOfMemory:
   exception = getOutOfMemory();
   break;
 case ServerNotConnected:
   exception = getServerNotConnected();
   break;
 case CouldNotConnectToSocket:
   exception = getCouldNotConnectToSocket();
   break;
 case BindAddressError:
   exception = getBindAddressError();
   break;
 case AllocIDError:
   exception = getAllocIDError();
   break;
 case AllocIDConfigMismatch:
   exception = getAllocIDConfigMismatch();
   break;
 case StartFailed:
   exception = getStartFailed();
   break;
 case StopFailed:
   exception = getStopFailed();
   break;
 case RestartFailed:
   exception = getRestartFailed();
   break;
 case CouldNotStartBackup:
   exception = getCouldNotStartBackup();
   break;
 case CouldNotAbortBackup:
   exception = getCouldNotAbortBackup();
   break;
 case CouldNotEnterSingleUserMode:
   exception = getCouldNotEnterSingleUserMode();
   break;
 case CouldNotExitSigleUserMode:
   exception = getCouldNotExitSigleUserMode();
   break;
 case UsageError:
   exception = getUsageError();
   break;
 default:
   exception = rb_eRuntimeError;
   break;
  }
  return exception;
}

%}

//%apply ndb_logevent *OUTPUT { ndb_logevent *dst };

%include "mgmapi/mgmenums.i"

%include "mgmapi/NdbLogEvent.i"
%include "mgmapi/NdbLogEventManager.i"
%include "mgmapi/NdbMgm.i"

%include "mgmapi/ClusterState.i"
%include "mgmapi/NodeState.i"
%include "mgmapi/NdbMgmReply.i"

