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

%pythoncode %{
import decimal
value_lookup=dict()
equal_lookup=dict()
setval_lookup=dict()
  %}

%include "ndbapi/ndbglobals.i"


%include "swig/datetime.i"
%include "swig/decimal.i"


%typemap(in) (const char* anInputString, size_t len) {
  /* Check that we are getting a string */
  if (PyString_Check($input)) {
    // We are going to try not copying this string, since it's not going to
    // be modified and we're going to copy it in the main function
    $1=PyString_AsString($input);
    $2=PyString_Size($input);
  } else {
    NDB_exception(NdbApiException,"Couldn't convert argument");
  }
 }





%include "swig/init.i"

%include "swig/callback.i"
%include "swig/exception.i"

// %feature("shadow") Ndb_cluster_connection::createNdb(const char*, Int32) %{
%feature("shadow") Ndb_cluster_connection::createNdb %{
  def createNdb(self, *args):
    ret=$action(self,*args)
    ret.__ndb_cluster_connection_ref=(self)
    return ret
    %}

%include "ndbapi/NdbClusterConnection.i"


%include "ndbapi/Ndb.i"
%include "ndbapi/NdbDictionary.i"

%include "ndbapi/NdbTransaction.i"



%include "ndbapi/NdbOperation.i"
%include "swig/ndboperation.i"

%include "ndbapi/NdbScanOperation.i"
%include "ndbapi/NdbIndexOperation.i"
%include "ndbapi/NdbIndexScanOperation.i"
%include "ndbapi/NdbEventOperation.i"


%include "ndbapi/NdbRecAttr.i"
%include "swig/recattr_getvalue.i"


%include "ndbapi/NdbError.i"
%include "ndbapi/NdbBlob.i"
%include "ndbapi/NdbScanFilter.i"

%include "swig/module_pythoncode.i"
