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


%module(directors="1") Mgmj

%include "enums.swg"

%pragma(java) jniclasscode=%{
   static {
     try {
       System.loadLibrary("mgmj");
     } catch (UnsatisfiedLinkError e) {
       throw new RuntimeException("Native code library failed to load. \n" + e);
     }
     Mgmj.ndb_init();
   }
   %}

 /*
  * These next two raise the protection of getCPtr from protected
  * to public, because otherwise events and listeners don't work.
  */
%typemap(javabody) SWIGTYPE %{
  private long swigCPtr;
  protected boolean swigCMemOwn;

  public $javaclassname(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr($javaclassname obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }
%}

%typemap(javabody_derived) SWIGTYPE %{
  private long swigCPtr;

  public $javaclassname(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr($javaclassname obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }
%}

%include "swig/mgm_exception.i"

%include "swig/BackupWrapper.i"

%include "swig/BaseEventWrapper.i"

%include "swig/ClusterState.i"

%include "swig/node_list.i"

%include "mgmapi/mgmglobals.i"


%rename(NdbLogEventImpl) ndb_logevent;
%typemap(javainterfaces) ndb_logevent "NdbLogEvent";
%rename(NdbLogEventManagerImpl) NdbLogEventManager;
%typemap(javainterfaces) NdbLogEventManager "NdbLogEventManager";
%rename(NdbMgmReplyImpl) ndb_mgm_reply;
%typemap(javainterfaces) ndb_mgm_reply "NdbMgmReply";
%rename(ClusterStateImpl) ndb_mgm_cluster_state;
%typemap(javainterfaces) ndb_mgm_cluster_state "ClusterState";
%rename(NodeStateImpl) ndb_mgm_node_state;
%typemap(javainterfaces) ndb_mgm_node_state "NodeState";

%include "mgmapi/mgmenums.i"
%include "mgmapi/NdbLogEvent.i"
%include "mgmapi/NdbLogEventManager.i"
%include "swig/NdbMgm.i"

%include "mgmapi/ClusterState.i"
%include "mgmapi/NodeState.i"
%include "mgmapi/NdbMgmReply.i"
