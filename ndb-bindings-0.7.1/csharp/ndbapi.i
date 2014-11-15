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

%module ndbapi

%include "enumtypesafe.swg"



%typemap(cscode) NdbTransaction %{
  private Ndb ndbReference;
  internal void addReference(Ndb ndbref) {
    ndbReference = ndbref;
  }

  %}

%typemap(csout, excode=SWIGEXCODE) NdbTransaction* Ndb::startTransaction {
  IntPtr cPtr = $imcall;$excode
                          $csclassname ret = null;
  if (cPtr != IntPtr.Zero) {
    ret = new $csclassname(cPtr, $owner);
    ret.addReference(this);
  }
  return ret;
 }

%typemap(cstype) NdbDateTime * "System.DateTime";
%typemap(csin) NdbDateTime * "NdbDateTimeHelper.systemDTtoNdbDT($csinput)";
%typemap(csout, excode=SWIGEXCODE) NdbDateTime * {
  IntPtr cPtr = $imcall;$excode
                          $csclassname ret = null;
  if (cPtr != IntPtr.Zero) {
    ret = new $csclassname(cPtr, $owner);
  }
  return new System.DateTime((int)ret.year, (int)ret.month, (int)ret.day,
                             (int)ret.hour, (int)ret.minute, (int)ret.second);

 }

class NdbDateTime
{
public:
  unsigned int  year, month, day, hour, minute, second;
  NdbDateTime();
};


/*
  %typemap(in) (MYSQL_TIME *) {
  if (PyDateTime_Check($input)) {
  MYSQL_TIME * dt = (MYSQL_TIME *)malloc(sizeof(MYSQL_TIME));
  if (dt==NULL)
  NDB_exception(NdbApiException,"Failed to allocate a MYSQL_TIME");
  dt->year = PyDateTime_GET_YEAR($input);
  dt->month = PyDateTime_GET_MONTH($input);
  dt->day = PyDateTime_GET_DAY($input);
  dt->hour = PyDateTime_DATE_GET_HOUR($input);
  dt->minute = PyDateTime_DATE_GET_MINUTE($input);
  dt->second = PyDateTime_DATE_GET_SECOND($input);
  $1 = dt;
  } else {
  NDB_exception(NdbApiException,"DateTime argument required!");
  }
  }
  %typemap(cstype) MYSQL_TIME * "System.DateTime";
  %typemap(csin) MYSQL_TIME * "MYSQL_TIME.getCPtr(MYSQL_TIME.getMYSQL_TIME($csinput))"

  %typemap(cscode) MYSQL_TIME %{
  public static MYSQL_TIME getMYSQL_TIME(System.DateTime aDateTime) {
  MYSQL_TIME myTime = new MYSQL_TIME();
  myTime.year = (uint)aDateTime.Year;
  myTime.month = (uint)aDateTime.Month;
  myTime.day = (uint)aDateTime.Day;
  myTime.hour = (uint)aDateTime.Hour;
  myTime.minute = (uint)aDateTime.Minute;
  myTime.second = (uint)aDateTime.Second;
  return myTime;
  }
  %}

*/
//%typemap(cstype) (const char* anInputString, size_t len) "string";
//%typemap(imtype) (const char* anInputString, size_t len) "string";
//%typemap(csin) (const char* anInputString, size_t len) "$csinput, $csinput.Length"

%typemap(in) (const char* anInputString, size_t len) {
  $1=$input;
  $2=strlen($input);
}

%typemap(freearg) (MYSQL_TIME *) {
  free((MYSQL_TIME *) $1);
}

%typemap(csfinalize) Ndb_cluster_connection %{
  ~$csclassname() {
    Dispose();
    ndbapi.ndb_end(0);
  }
  %}



%typemap(csfinalize) NdbTransaction %{
  ~$csclassname() {
    // This is only if we haven't managed to kill this guy yet.
    // Don't depend on this.
    this.close();
    Dispose();
  }
  %}

%typemap(csfinalize) Ndb %{
  ~$csclassname() {
    Dispose();
  }
  %}


%{
#define NDB_exception(excp,msg) { \
    SWIG_CSharpSetPendingExceptionCustom(excp,msg); }
#define NDB_exception_err(excp,err) { \
    SWIG_CSharpSetPendingExceptionCustom(excp,err.message); }
  %}

%insert(runtime) %{
  // Code to handle throwing of C# CustomApplicationException from C/C++ code.
  // The equivalent delegate to the callback, CSharpExceptionCallback_t, is
  // CustomExceptionDelegate and the equivalent customExceptionCallback
  // instance is customDelegate
  typedef void (SWIGSTDCALL* CSharpExceptionCallback_t)(int excp,
                                                        const char *);
  CSharpExceptionCallback_t customExceptionCallback = NULL;

  extern "C" SWIGEXPORT
    void SWIGSTDCALL
    CustomExceptionRegisterCallback(CSharpExceptionCallback_t customCallback)
  {
    customExceptionCallback = customCallback;
  }

  // Note that SWIG detects any method calls named starting with
  // SWIG_CSharpSetPendingException for warning 845
  static void SWIG_CSharpSetPendingExceptionCustom(int excp, const char *msg) {
    customExceptionCallback(excp, msg);
  }


  %}


%typemap(ctype) (asynch_callback_t *) "AsynchCallback_t"
%typemap(cstype) asynch_callback_t * "BaseCallback"
%typemap(imtype) asynch_callback_t * "AsynchCallbackDelegate"
 /*%typemap(imtype) asynch_callback_t *
   "System.Runtime.InteropServices.HandleRef" */
%typemap(csin) asynch_callback_t * "$csinput.registerTransactionHook(this)"


%pragma(csharp) imclasscode=%{

  class CustomExceptionHelper {
    // C# delegate for the C/C++ customExceptionCallback
    public delegate void CustomExceptionDelegate(int excp, string message);

    static CustomExceptionDelegate customDelegate =
      new CustomExceptionDelegate(SetPendingCustomException);

    [DllImport("$dllimport", EntryPoint="CustomExceptionRegisterCallback")]
    public static extern
    void
    CustomExceptionRegisterCallback(CustomExceptionDelegate customCallback);

    static void SetPendingCustomException(int excp, string message) {
      // switch the exception classes on excp here
      if (excp == NdbException.NdbApiException.swigValue) {
        SWIGPendingException.Set(new NdbApiException(message));
      } else if (excp == (int)NdbException.NdbApiPermanentException.swigValue) {
        SWIGPendingException.Set(new NdbApiPermanentException(message));
      } else if (excp == (int)NdbException.NdbApiTemporaryException.swigValue) {
        SWIGPendingException.Set(new NdbApiTemporaryException(message));
      } else if (excp == (int)NdbException.NdbApiUnknownResult.swigValue) {
        SWIGPendingException.Set(new NdbApiUnknownResult(message));
      } else if (excp == (int)NdbException.ApplicationError.swigValue) {
        SWIGPendingException.Set(new ApplicationError(message));
      } else if (excp == (int)NdbException.NoDataFound.swigValue) {
        SWIGPendingException.Set(new NoDataFound(message));
      } else if (excp == (int)NdbException.ConstraintViolation.swigValue) {
        SWIGPendingException.Set(new ConstraintViolation(message));
      } else if (excp == (int)NdbException.SchemaError.swigValue) {
        SWIGPendingException.Set(new SchemaError(message));
      } else if (excp == (int)NdbException.UserDefinedError.swigValue) {
        SWIGPendingException.Set(new UserDefinedError(message));
      } else if (excp == (int)NdbException.InsufficientSpace.swigValue) {
        SWIGPendingException.Set(new InsufficientSpace(message));
      } else if (excp == (int)NdbException.TemporaryResourceError.swigValue) {
        SWIGPendingException.Set(new TemporaryResourceError(message));
      } else if (excp == (int)NdbException.NodeRecoveryError.swigValue) {
        SWIGPendingException.Set(new NodeRecoveryError(message));
      } else if (excp == (int)NdbException.OverloadError.swigValue) {
        SWIGPendingException.Set(new OverloadError(message));
      } else if (excp == (int)NdbException.TimeoutExpired.swigValue) {
        SWIGPendingException.Set(new TimeoutExpired(message));
      } else if (excp == (int)NdbException.UnknownResultError.swigValue) {
        SWIGPendingException.Set(new UnknownResultError(message));
      } else if (excp == (int)NdbException.InternalError.swigValue) {
        SWIGPendingException.Set(new InternalError(message));
      } else if (excp == (int)NdbException.FunctionNotImplemented.swigValue) {
        SWIGPendingException.Set(new FunctionNotImplemented(message));
      } else if (excp == (int)NdbException.UnknownErrorCode.swigValue) {
        SWIGPendingException.Set(new UnknownErrorCode(message));
      } else if (excp == (int)NdbException.NodeShutdown.swigValue) {
        SWIGPendingException.Set(new NodeShutdown(message));
      } else if (excp == (int)NdbException.SchemaObjectExists.swigValue) {
        SWIGPendingException.Set(new SchemaObjectExists(message));
      } else if (excp == (int)NdbException.InternalTemporary.swigValue) {
        SWIGPendingException.Set(new InternalTemporary(message));
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


%include "ndbapi/ndbglobals.i"

%{
#include <stdio.h>

  typedef void (SWIGSTDCALL* AsynchCallback_t)(int results,
                                               NdbTransaction * trans);

  typedef struct
    asynch_callback_t
  {
    AsynchCallback_t theCallback;
    long long create_time;
  };




  static void theCallBack(int result,
                          NdbTransaction *trans,
                          void *aObject)
  {
  }
  %}

  enum NdbException {
    NdbApiException,
    NdbApiPermanentException,
    NdbApiTemporaryException,
    NdbApiUnknownResult,
    ApplicationError,
    NoDataFound,
    ConstraintViolation,
    SchemaError,
    UserDefinedError,
    InsufficientSpace,
    TemporaryResourceError,
    NodeRecoveryError,
    OverloadError,
    TimeoutExpired,
    UnknownResultError,
    InternalError,
    FunctionNotImplemented,
    UnknownErrorCode,
    NodeShutdown,
    SchemaObjectExists,
    InternalTemporary,
  };

typedef enum
{
  ndberror_st_success = 0,
  ndberror_st_temporary = 1,
  ndberror_st_permanent = 2,
  ndberror_st_unknown = 3
} ndberror_status_enum;

typedef enum
{
  ndberror_cl_none = 0,
  ndberror_cl_application = 1,
  ndberror_cl_no_data_found = 2,
  ndberror_cl_constraint_violation = 3,
  ndberror_cl_schema_error = 4,
  ndberror_cl_user_defined = 5,
  ndberror_cl_insufficient_space = 6,
  ndberror_cl_temporary_resource = 7,
  ndberror_cl_node_recovery = 8,
  ndberror_cl_overload = 9,
  ndberror_cl_timeout_expired = 10,
  ndberror_cl_unknown_result = 11,
  ndberror_cl_internal_error = 12,
  ndberror_cl_function_not_implemented = 13,
  ndberror_cl_unknown_error_code = 14,
  ndberror_cl_node_shutdown = 15,
  ndberror_cl_configuration = 16,
  ndberror_cl_schema_object_already_exists = 17,
  ndberror_cl_internal_temporary = 18
} ndberror_classification_enum;


%include "ndbapi/NdbClusterConnection.i"
%include "ndbapi/NdbDictionary.i"
%include "ndbapi/Ndb.i"



%include "ndbapi/NdbTransaction.i"



%include "ndbapi/NdbOperation.i"
%include "ndbapi/NdbScanOperation.i"
%include "ndbapi/NdbIndexOperation.i"
%include "ndbapi/NdbIndexScanOperation.i"
%include "ndbapi/NdbEventOperation.i"
%include "ndbapi/NdbRecAttr.i"
%include "ndbapi/NdbError.i"

%include "ndbapi/NdbBlob.i"
%include "ndbapi/NdbScanFilter.i"


