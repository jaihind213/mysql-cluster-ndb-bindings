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


%{
#define NEW_pyexcept(EXCPT,EPARENT) { \
    PyExc_ ## EXCPT = \
      PyErr_NewException((char *)"mysql.cluster.ndbapi." #EXCPT ,\
                         PyExc_ ## EPARENT,NULL);\
    PyDict_SetItemString(d, #EXCPT ,PyExc_ ## EXCPT);\
    Py_INCREF(PyExc_ ## EXCPT); }
#define NEW_BASE_pyexcept(EXCPT) { \
    PyExc_ ## EXCPT = \
      PyErr_NewException((char *)"mysql.cluster.ndbapi." #EXCPT ,\
                         PyExc_Exception,NULL);\
    PyDict_SetItemString(d, #EXCPT ,PyExc_ ## EXCPT);\
    Py_INCREF(PyExc_ ## EXCPT); }

  PyObject * PyExc_NdbApiException = NULL;
  PyObject * PyExc_NdbApiPermanentException = NULL;
  PyObject * PyExc_NdbApiTemporaryException = NULL;
  PyObject * PyExc_NdbApiUnknownResult = NULL;
  PyObject * PyExc_ApplicationError = NULL;
  PyObject * PyExc_NoDataFound = NULL;
  PyObject * PyExc_ConstraintViolation = NULL;
  PyObject * PyExc_SchemaError = NULL;
  PyObject * PyExc_UserDefinedError = NULL;
  PyObject * PyExc_InsufficientSpace = NULL;
  PyObject * PyExc_TemporaryResourceError = NULL;
  PyObject * PyExc_NodeRecoveryError = NULL;
  PyObject * PyExc_OverloadError = NULL;
  PyObject * PyExc_TimeoutExpired = NULL;
  PyObject * PyExc_UnknownResultError = NULL;
  PyObject * PyExc_InternalError = NULL;
  PyObject * PyExc_FunctionNotImplemented = NULL;
  PyObject * PyExc_UnknownErrorCode = NULL;
  PyObject * PyExc_NodeShutdown = NULL;
  PyObject * PyExc_SchemaObjectExists = NULL;
  PyObject * PyExc_InternalTemporary = NULL;



#define NDB_exception(excp, msg) { ndb_throw_exception(excp,msg); SWIG_fail; }
// TODO: Need to support this form of exception
#define NDB_exception_err(excp, err) { \
    ndb_throw_exception(excp,err.message);\
    SWIG_fail; }

  void ndb_throw_exception(NdbException excp_mod, const char * msg) {

    PyObject * exception;

    switch (excp_mod) {
    case NdbApiException:
      exception = PyExc_NdbApiException;
      break;
    case NdbApiPermanentException:
      exception = PyExc_NdbApiPermanentException;
      break;
    case NdbApiTemporaryException:
      exception = PyExc_NdbApiTemporaryException;
      break;
    case NdbApiUnknownResult:
      exception = PyExc_NdbApiUnknownResult;
      break;
    case ApplicationError:
      exception = PyExc_ApplicationError;
      break;
    case NoDataFound:
      exception = PyExc_NoDataFound;
      break;
    case ConstraintViolation:
      exception = PyExc_ConstraintViolation;
      break;
    case SchemaError:
      exception = PyExc_SchemaError;
      break;
    case UserDefinedError:
      exception = PyExc_UserDefinedError;
      break;
    case InsufficientSpace:
      exception = PyExc_InsufficientSpace;
      break;
    case TemporaryResourceError:
      exception = PyExc_TemporaryResourceError;
      break;
    case NodeRecoveryError:
      exception = PyExc_NodeRecoveryError;
      break;
    case OverloadError:
      exception = PyExc_OverloadError;
      break;
    case TimeoutExpired:
      exception = PyExc_TimeoutExpired;
      break;
    case UnknownResultError:
      exception = PyExc_UnknownResultError;
      break;
    case InternalError:
      exception = PyExc_InternalError;
      break;
    case FunctionNotImplemented:
      exception = PyExc_FunctionNotImplemented;
      break;
    case UnknownErrorCode:
      exception = PyExc_UnknownErrorCode;
      break;
    case NodeShutdown:
      exception = PyExc_NodeShutdown;
      break;
    case SchemaObjectExists:
      exception = PyExc_SchemaObjectExists;
      break;
    case InternalTemporary:
      exception = PyExc_InternalTemporary;
      break;
    default:
      exception = PyExc_RuntimeError;
    }
    //exception = PyExc_NdbApiException;
    if (exception == NULL) {
      exception = PyExc_RuntimeError;
    }
    PyErr_SetString(exception,msg);
    Py_INCREF(exception);
  }



  %}
