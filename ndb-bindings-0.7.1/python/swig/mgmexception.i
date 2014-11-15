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
      PyErr_NewException((char *)"mysql.cluster.mgmapi." #EXCPT ,\
                         PyExc_ ## EPARENT,NULL);\
    PyDict_SetItemString(d, #EXCPT ,PyExc_ ## EXCPT);\
    Py_INCREF(PyExc_ ## EXCPT); }
#define NEW_BASE_pyexcept(EXCPT) { \
    PyExc_ ## EXCPT = \
      PyErr_NewException((char *)"mysql.cluster.mgmapi." #EXCPT ,\
                         PyExc_Exception,NULL);\
    PyDict_SetItemString(d, #EXCPT ,PyExc_ ## EXCPT);\
    Py_INCREF(PyExc_ ## EXCPT); }

  PyObject * PyExc_NdbMgmException = NULL;
  PyObject * PyExc_IllegalConnectString = NULL;
  PyObject * PyExc_IllegalServerHandle = NULL;
  PyObject * PyExc_IllegalServerReply = NULL;
  PyObject * PyExc_IllegalNumberOfNodes = NULL;
  PyObject * PyExc_IllegalNodeStatus = NULL;
  PyObject * PyExc_OutOfMemory = NULL;
  PyObject * PyExc_ServerNotConnected = NULL;
  PyObject * PyExc_CouldNotConnectToSocket = NULL;
  PyObject * PyExc_BindAddressError = NULL;
  PyObject * PyExc_AllocIDError = NULL;
  PyObject * PyExc_AllocIDConfigMismatch = NULL;
  PyObject * PyExc_StartFailed = NULL;
  PyObject * PyExc_StopFailed = NULL;
  PyObject * PyExc_RestartFailed = NULL;
  PyObject * PyExc_CouldNotStartBackup = NULL;
  PyObject * PyExc_CouldNotAbortBackup = NULL;
  PyObject * PyExc_CouldNotEnterSingleUserMode = NULL;
  PyObject * PyExc_CouldNotExitSigleUserMode = NULL;
  PyObject * PyExc_UsageError = NULL;

#define NDB_exception(excp, msg) { ndb_throw_exception(excp,msg); SWIG_fail; }
// TODO: Need to support this form of exception
#define NDB_exception_err(excp, msg, err) { \
    ndb_throw_exception(excp,msg);\
    SWIG_fail; }

  void ndb_throw_exception(NdbException excp_mod, const char * msg) {

    PyObject * exception;

    switch (excp_mod) {

    case NdbMgmException:
      exception = PyExc_NdbMgmException;
      break;
    case IllegalConnectString:
      exception = PyExc_IllegalConnectString;
      break;
    case IllegalServerHandle:
      exception = PyExc_IllegalServerHandle;
      break;
    case IllegalServerReply:
      exception = PyExc_IllegalServerReply;
      break;
    case IllegalNumberOfNodes:
      exception = PyExc_IllegalNumberOfNodes;
      break;
    case IllegalNodeStatus:
      exception = PyExc_IllegalNodeStatus;
      break;
    case OutOfMemory:
      exception = PyExc_OutOfMemory;
      break;
    case ServerNotConnected:
      exception = PyExc_ServerNotConnected;
      break;
    case CouldNotConnectToSocket:
      exception = PyExc_CouldNotConnectToSocket;
      break;
    case BindAddressError:
      exception = PyExc_BindAddressError;
      break;
    case AllocIDError:
      exception = PyExc_AllocIDError;
      break;
    case AllocIDConfigMismatch:
      exception = PyExc_AllocIDConfigMismatch;
      break;
    case StartFailed:
      exception = PyExc_StartFailed;
      break;
    case StopFailed:
      exception = PyExc_StopFailed;
      break;
    case RestartFailed:
      exception = PyExc_RestartFailed;
      break;
    case CouldNotStartBackup:
      exception = PyExc_CouldNotStartBackup;
      break;
    case CouldNotAbortBackup:
      exception = PyExc_CouldNotAbortBackup;
      break;
    case CouldNotEnterSingleUserMode:
      exception = PyExc_CouldNotEnterSingleUserMode;
      break;
    case CouldNotExitSigleUserMode:
      exception = PyExc_CouldNotExitSigleUserMode;
      break;
    case UsageError:
      exception = PyExc_UsageError;
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
