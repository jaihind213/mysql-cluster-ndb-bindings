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

%module Ndbj


%rename NdbBlob NdbBlobImpl;
%rename NdbClusterConnection NdbClusterConnectionImpl;
%rename NdbEventOperation NdbEventOperationImpl;
%rename NdbIndexOperation NdbIndexOperationImpl;
%rename NdbIndexScanOperation NdbIndexScanOperationImpl;
%rename NdbOperation NdbOperationImpl;
%rename NdbRecAttr NdbRecAttrImpl;
%rename Ndb NdbImpl;
%rename NdbScanFilter NdbScanFilterImpl;
%rename NdbScanOperation NdbScanOperationImpl;
%rename NdbTransaction NdbTransactionImpl;


%typemap(in) (Uint32 anAttrId) {
  // JDBC does column IDs 1 indexed. NDB API does them 0 indexed.
  $1 = $input-1;
 }


%include "arrays_java.i"
%include "decimal.i"

%include "ndbapi/ndbglobals.i"
%include "various.i"
%include "enums.swg"


%include "asynch_callback_t.i"
%include "ndb_exception.i"

%include "voidint.i"

%include "NdbDateTime.i"

%include "timestamp.i"
%include "byte_array.i"

%include "names_array.i"

%include "input_string.i"

%typemap(javacode) SWIGTYPE %{
  @Override
  public boolean equals(Object obj) {
    boolean equal = false;
    if (obj instanceof $javaclassname)
      equal = ((($javaclassname)obj).swigCPtr == this.swigCPtr);
    return equal;
  }
  @Override
  public int hashCode() {
     return (int)getCPtr(this);
  }
%}

%pragma(java) jniclasscode=%{
       static {

                        try {
                                System.loadLibrary("ndbj");
                        } catch (UnsatisfiedLinkError e) {
                                throw new RuntimeException("Native code library failed to load. \n" + e);
                        }
        }
%}

%typemap(in) SWIGTYPE * %{
  /* Prevent SEGV on null pointers */
  if ($input == 0) {
    NDB_exception(NdbApiException,"Object is NULL. This state is usually encountered when using an object after it has been closed");
    return $null;
  }
  $1=*($1_ltype*)&$input;
%}

%typemap(jstype) NdbScanOperation::ScanFlag "NdbScanOperation.ScanFlag"
%typemap(javaout) NdbScanOperation::ScanFlag {
  return NdbScanOperation.ScanFlag.swigToEnum
    (ndbjJNI.NdbScanOperationImpl_getScanFlag(swigCPtr, this));
 }

%include "NdbClusterConnection.i"
%include "Ndb.i"
%include "NdbDictionary.i"
%include "AbortOption.i"
%include "ExecType.i"
%include "CommitStatusType.i"
%include "LockMode.i"
%include "NdbTransaction.i"
%include "NdbOperation.i"
%include "NdbScanOperation.i"
%include "NdbIndexOperation.i"
%include "NdbIndexScanOperation.i"
%include "NdbEventOperation.i"

%include "NdbRecAttr.i"
%include "NdbBlob.i"

%include "NdbScanFilter.i"

%include "NdbError.i"
