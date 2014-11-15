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

/* Not using directors with ruby - they don't seem to work and the 
   custom code is really clean anyway. */
%module ndbapi

%include "ndbapi/ndbglobals.i"

%typemap(in) (asynch_callback_t * cb) { 

  $1 = new asynch_callback_t; 
  $1->obj = $input; 
  $1->create_time = 0;
 }
%typemap(in) (const char* anInputString, size_t len) {

  $1 = STR2CSTR($input);
  $2 = (size_t)RSTRING($input)->len;
 }

%typecheck(SWIG_TYPECHECK_POINTER) MYSQL_TIME* (const char* klass) {
        // do typecheck using kind_of? DateTime
//        static VALUE rb_cDateTime = rb_const_get( rb_cObject,
//						  "DateTime" );
//	$1 = rb_obj_is_kind_of( $input, rb_cDateTime );

	// or... better yet, do it with duck typing... any class
        // will work, as long as these are defined...
	$1 = ( rb_respond_to( $input, rb_intern("day") ) &&
	       rb_respond_to( $input, rb_intern("month") ) &&
	       rb_respond_to( $input, rb_intern("year") ) &&
               rb_respond_to( $input, rb_intern("hour") ) &&
               rb_respond_to( $input, rb_intern("min") ) &&
               rb_respond_to( $input, rb_intern("sec") ) );
}

%typemap(in,numinputs=1) (MYSQL_TIME * anInputDateTime) { 

  // How do I check that we're getting a Ruby DateTime?!!?

    MYSQL_TIME * dt = (MYSQL_TIME *)malloc(sizeof(MYSQL_TIME));
    if (dt==NULL) 
      NDB_exception(NdbApiException,"Failed to allocate a MYSQL_TIME");
    dt->year = NUM2INT(rb_funcall($input,rb_intern("year"),0));
    dt->month = NUM2INT(rb_funcall($input,rb_intern("month"),0));
    dt->day = NUM2INT(rb_funcall($input,rb_intern("day"),0));
    dt->hour = NUM2INT(rb_funcall($input,rb_intern("hour"),0));
    dt->minute = NUM2INT(rb_funcall($input,rb_intern("min"),0));
    dt->second = NUM2INT(rb_funcall($input,rb_intern("sec"),0));

    $1 = dt;      

 }

%typemap(out) NdbTimestamp { 

}
%{ 

/* Override the SWIG_InitRuntime macro with our own, so we can inject
   ndb_init() into the init sequence */
#undef SWIG_InitRuntime
#define SWIG_InitRuntime()                              NDB_Ruby_InitRuntime() 

void
NDB_Ruby_InitRuntime()
{
  SWIG_Ruby_InitRuntime();
  ndb_init();
}

#define NDB_exception(code,msg) do { ndb_raise_exception(code, msg); SWIG_fail; } while(0);
#define NDB_exception_err(code,err) do { ndb_raise_exception(code, err.message); SWIG_fail; } while(0);

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

SWIGINTERN VALUE getNdbApiException() {
  static int initNdbApiException = 0 ;
  static VALUE rb_eNdbApiException = NULL;
  if (! initNdbApiException ) {
    initNdbApiException = 1;
    rb_eNdbApiException = rb_define_class("NdbApiException", rb_eRuntimeError);
  }
  return rb_eNdbApiException ;
}

VALUE ndb_get_exception(NdbException excpcode);

getExceptionMethod(NdbApiPermanentException,NdbApiException);
getExceptionMethod(NdbApiTemporaryException,NdbApiException);
getExceptionMethod(NdbApiUnknownResult,NdbApiException);
getExceptionMethod(ApplicationError,NdbApiPermanentException);
getExceptionMethod(NoDataFound,NdbApiPermanentException);
getExceptionMethod(ConstraintViolation,NdbApiPermanentException);
getExceptionMethod(SchemaError,NdbApiPermanentException);
getExceptionMethod(UserDefinedError,NdbApiPermanentException);
getExceptionMethod(InsufficientSpace,NdbApiTemporaryException);
getExceptionMethod(TemporaryResourceError,NdbApiTemporaryException);
getExceptionMethod(NodeRecoveryError,NdbApiTemporaryException);
getExceptionMethod(OverloadError,NdbApiTemporaryException);
getExceptionMethod(TimeoutExpired,NdbApiTemporaryException);
getExceptionMethod(UnknownResultError,NdbApiUnknownResult);
getExceptionMethod(InternalError,NdbApiPermanentException);
getExceptionMethod(FunctionNotImplemented,NdbApiPermanentException);
getExceptionMethod(UnknownErrorCode,NdbApiUnknownResult);
getExceptionMethod(NodeShutdown,NdbApiTemporaryException);
getExceptionMethod(SchemaObjectExists,NdbApiPermanentException);
getExceptionMethod(InternalTemporary,NdbApiTemporaryException);

void ndb_raise_exception(NdbException excpcode, const char * msg) {
  rb_raise(ndb_get_exception(excpcode),msg);
}

VALUE ndb_get_exception(NdbException excpcode) {

 VALUE exception;

 switch (excpcode) {
 case NdbApiException:
   exception = getNdbApiException();
   break;
 case NdbApiPermanentException:
   exception = getNdbApiPermanentException();
   break;
 case NdbApiTemporaryException:
   exception = getNdbApiTemporaryException();
   break;
 case NdbApiUnknownResult:
   exception = getNdbApiUnknownResult();
   break;
 case ApplicationError:
   exception = getApplicationError();
   break;
 case NoDataFound:
   exception = getNoDataFound();
   break;
 case ConstraintViolation:
   exception = getConstraintViolation();
   break;
 case SchemaError:
   exception = getSchemaError();
   break;
 case UserDefinedError:
   exception = getUserDefinedError();
   break;
 case InsufficientSpace:
   exception = getInsufficientSpace();
   break;
 case TemporaryResourceError:
   exception = getTemporaryResourceError();
   break;
 case NodeRecoveryError:
   exception = getNodeRecoveryError();
   break;
 case OverloadError:
   exception = getOverloadError();
   break;
 case TimeoutExpired:
   exception = getTimeoutExpired();
   break;
 case UnknownResultError:
   exception = getUnknownResultError();
   break;
 case InternalError:
   exception = getInternalError();
   break;
 case FunctionNotImplemented:
   exception = getFunctionNotImplemented();
   break;
 case UnknownErrorCode:
   exception = getUnknownErrorCode();
   break;
 case NodeShutdown:
   exception = getNodeShutdown();
   break;
 case SchemaObjectExists:
   exception = getSchemaObjectExists();
   break;
 case InternalTemporary:
   exception = getInternalTemporary();
   break;
 default:
   exception = rb_eRuntimeError;
   break;
 }
 return exception;
}

typedef struct
asynch_callback_t
{  
  VALUE obj;
  long long create_time;
};
static void theCallBack(int result, 
                        NdbTransaction *trans,
                        void *aObject)
{
  asynch_callback_t * callback_data = (asynch_callback_t *)aObject;

  VALUE cb_obj = callback_data->obj;
  VALUE trans_obj = SWIG_NewPointerObj(SWIG_as_voidptr(trans), SWIGTYPE_p_NdbTransaction, 0 |  0 );
  rb_funcall(cb_obj,rb_intern("call"),1,trans_obj); //,trans);
  delete callback_data; 
  
}
 %}

%include "ndbapi/NdbClusterConnection.i"
%include "ndbapi/Ndb.i"
%include "ndbapi/NdbDictionary.i"
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




