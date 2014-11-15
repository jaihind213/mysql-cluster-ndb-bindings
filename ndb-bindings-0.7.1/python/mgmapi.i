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

%module(directors="1") mgmapi

%typemap(out) (BaseEventWrapper *) %{
  $result = SWIG_NewPointerObj(SWIG_as_voidptr(result->theEvent),
                               SWIGTYPE_p_ndb_logevent,
                               SWIG_POINTER_OWN |  0 );
  %}

%pythoncode %{

    NdbMgmException = _mgmapi.NdbMgmException
    IllegalConnectString = _mgmapi.IllegalConnectString
    IllegalServerHandle = _mgmapi.IllegalServerHandle
    IllegalServerReply = _mgmapi.IllegalServerReply
    IllegalNumberOfNodes = _mgmapi.IllegalNumberOfNodes
    IllegalNodeStatus = _mgmapi.IllegalNodeStatus
    OutOfMemory = _mgmapi.OutOfMemory
    ServerNotConnected = _mgmapi.ServerNotConnected
    CouldNotConnectToSocket = _mgmapi.CouldNotConnectToSocket
    BindAddressError = _mgmapi.BindAddressError
    AllocIDError = _mgmapi.AllocIDError
    AllocIDConfigMismatch = _mgmapi.AllocIDConfigMismatch
    StartFailed = _mgmapi.StartFailed
    StopFailed = _mgmapi.StopFailed
    RestartFailed = _mgmapi.RestartFailed
    CouldNotStartBackup = _mgmapi.CouldNotStartBackup
    CouldNotAbortBackup = _mgmapi.CouldNotAbortBackup
    CouldNotEnterSingleUserMode = _mgmapi.CouldNotEnterSingleUserMode
    CouldNotExitSigleUserMode = _mgmapi.CouldNotExitSigleUserMode
    UsageError = _mgmapi.UsageError

  %}

extern int ndb_init(void);


%include "mgmapi/mgmglobals.i"

%include "swig/mgminit.i"
%include "swig/mgmexception.i"

%include "mgmapi/mgmenums.i"
%include "mgmapi/NdbLogEvent.i"
%include "mgmapi/NdbLogEventManager.i"
%include "mgmapi/NdbMgm.i"
%include "mgmapi/ClusterState.i"
%include "mgmapi/NodeState.i"
%include "mgmapi/NdbMgmReply.i"

