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


typedef int voidint;

%init %{


  ndb_init();


  NEW_BASE_pyexcept(NdbMgmException);
  NEW_pyexcept(IllegalConnectString,NdbMgmException);
  NEW_pyexcept(IllegalServerHandle,NdbMgmException);
  NEW_pyexcept(IllegalServerReply,NdbMgmException);
  NEW_pyexcept(IllegalNumberOfNodes,NdbMgmException);
  NEW_pyexcept(IllegalNodeStatus,NdbMgmException);
  NEW_pyexcept(OutOfMemory,NdbMgmException);
  NEW_pyexcept(ServerNotConnected,NdbMgmException);
  NEW_pyexcept(CouldNotConnectToSocket,NdbMgmException);
  NEW_pyexcept(BindAddressError,NdbMgmException);
  NEW_pyexcept(AllocIDError,NdbMgmException);
  NEW_pyexcept(AllocIDConfigMismatch,NdbMgmException);
  NEW_pyexcept(StartFailed,NdbMgmException);
  NEW_pyexcept(StopFailed,NdbMgmException);
  NEW_pyexcept(RestartFailed,NdbMgmException);
  NEW_pyexcept(CouldNotStartBackup,NdbMgmException);
  NEW_pyexcept(CouldNotAbortBackup,NdbMgmException);
  NEW_pyexcept(CouldNotEnterSingleUserMode,NdbMgmException);
  NEW_pyexcept(CouldNotExitSigleUserMode,NdbMgmException);
  NEW_pyexcept(UsageError,NdbMgmException);

  %}

