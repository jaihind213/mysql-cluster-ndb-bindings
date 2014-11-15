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

%include "enumtypesafe.swg"


%include "mgmapi/mgmglobals.i"

%typemap(out) (BaseEventWrapper *) {
  $result=$1->theEvent;
 }
//%typemap(imtype) (BaseEventWrapper *) "BaseEvent";
%typemap(cstype) (BaseEventWrapper *) "NdbLogEvent";
%typemap(ctype) (BaseEventWrapper *) "ndb_logevent *";

%typemap(csout) (BaseEventWrapper *) {
  IntPtr cPtr = $imcall;
  NdbLogEvent ret = (cPtr == IntPtr.Zero) ? null :  new NdbLogEvent(cPtr,true);
  if (mgmapiPINVOKE.SWIGPendingException.Pending) throw mgmapiPINVOKE.SWIGPendingException.Retrieve();
  return ret;

 }

%typemap(out) (BackupWrapper *) {
  $result=$1->backupId;
  free($1->theReply);
  free($1);
 }
%typemap(imtype) (BackupWrapper *) "long";
%typemap(cstype) (BackupWrapper *) "long";
%typemap(ctype) (BackupWrapper *) "unsigned int";

%typemap(csout) (BackupWrapper *) {
  return $imcall;
 }



%{
#define NDB_exception(excp,msg) { SWIG_CSharpSetPendingExceptionCustom(excp,msg); }
#define NDB_exception_err(excp,msg,err) { SWIG_CSharpSetPendingExceptionCustom(excp,msg); }

%}

enum NdbException {
    BaseRuntimeError,
    NdbApiException,
    BlobUndefinedException,
    NdbApiPermanentException,
    NdbApiRuntimeException,
    NdbApiTemporaryException,
    NdbApiTimeStampOutOfBoundsException,
    NdbApiUserErrorPermanentException,
    NdbClusterConnectionPermanentException,
    NdbClusterConnectionTemporaryException,
    NoSuchColumnException,
    NoSuchIndexException,
    NoSuchTableException,
    MgmApiException,
};

%insert(runtime) %{
  // Code to handle throwing of C# CustomApplicationException from C/C++ code.
  // The equivalent delegate to the callback, CSharpExceptionCallback_t, is CustomExceptionDelegate
  // and the equivalent customExceptionCallback instance is customDelegate
  typedef void (SWIGSTDCALL* CSharpExceptionCallback_t)(int excp, const char *);
  CSharpExceptionCallback_t customExceptionCallback = NULL;

  extern "C" SWIGEXPORT
  void SWIGSTDCALL CustomExceptionRegisterCallback(CSharpExceptionCallback_t customCallback) {
    customExceptionCallback = customCallback;
  }

  // Note that SWIG detects any method calls named starting with
  // SWIG_CSharpSetPendingException for warning 845
  static void SWIG_CSharpSetPendingExceptionCustom(int excp, const char *msg) {
    customExceptionCallback(excp, msg);
  }


%}

%pragma(csharp) imclasscode=%{


  class CustomExceptionHelper {
    // C# delegate for the C/C++ customExceptionCallback
    public delegate void CustomExceptionDelegate(int excp, string message);

    static CustomExceptionDelegate customDelegate =
                                   new CustomExceptionDelegate(SetPendingCustomException);

    [DllImport("$dllimport", EntryPoint="CustomExceptionRegisterCallback")]
    public static extern
           void CustomExceptionRegisterCallback(CustomExceptionDelegate customCallback);

    static void SetPendingCustomException(int excp, string message) {
      // switch the exception classes on excp here
        if (excp == NdbException.NdbApiException.swigValue) {
            SWIGPendingException.Set(new NdbApiException(message));
        } else if (excp == (int)NdbException.BlobUndefinedException.swigValue) {
            SWIGPendingException.Set(new BlobUndefinedException(message));
        } else if (excp == (int)NdbException.NdbApiPermanentException.swigValue) {
            SWIGPendingException.Set(new NdbApiPermanentException(message));
        } else if (excp == (int)NdbException.NdbApiRuntimeException.swigValue) {
            SWIGPendingException.Set(new NdbApiRuntimeException(message));
        } else if (excp == (int)NdbException.NdbApiTemporaryException.swigValue) {
            SWIGPendingException.Set(new NdbApiTemporaryException(message));
        } else if (excp == (int)NdbException.NdbApiTimeStampOutOfBoundsException.swigValue) {
            SWIGPendingException.Set(new NdbApiTimeStampOutOfBoundsException(message));
        } else if (excp == (int)NdbException.NdbApiUserErrorPermanentException.swigValue) {
            SWIGPendingException.Set(new NdbApiUserErrorPermanentException(message));
        } else if (excp == (int)NdbException.NdbClusterConnectionPermanentException.swigValue) {
            SWIGPendingException.Set(new NdbClusterConnectionPermanentException(message));
        } else if (excp == (int)NdbException.NdbClusterConnectionTemporaryException.swigValue) {
            SWIGPendingException.Set(new NdbClusterConnectionTemporaryException(message));
        } else if (excp == (int)NdbException.NoSuchColumnException.swigValue) {
            SWIGPendingException.Set(new NoSuchColumnException(message));
        } else if (excp == (int)NdbException.NoSuchIndexException.swigValue) {
            SWIGPendingException.Set(new NoSuchIndexException(message));
        } else if (excp == (int)NdbException.NoSuchTableException.swigValue) {
            SWIGPendingException.Set(new NoSuchTableException(message));
        } else { 
            Console.WriteLine("DEFAULT EXCEPTION REACHED!");
        }
    }

    static CustomExceptionHelper() {
      CustomExceptionRegisterCallback(customDelegate);
    }
  }
  static CustomExceptionHelper exceptionHelper = new CustomExceptionHelper();

  
%}


%include "mgmapi/mgmenums.i"


%include "mgmapi/NdbLogEvent.i"
%include "mgmapi/NdbLogEventManager.i"
%include "mgmapi/NdbMgm.i"

%include "mgmapi/ClusterState.i"
%include "mgmapi/NodeState.i"
%include "mgmapi/NdbMgmReply.i"

