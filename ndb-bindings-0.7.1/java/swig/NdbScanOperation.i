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

%typemap(javaclassmodifiers) enum NdbScanOperation::ScanFlag "protected enum";
%typemap(jstype) NdbScanOperation::ScanFlag "NdbScanOperation.ScanFlag"
%typemap(javaout) NdbScanOperation::ScanFlag {
  return NdbScanOperation.ScanFlag.swigToEnum
    (ndbjJNI.NdbScanOperationImpl_getScanFlag(swigCPtr, this));
 }

%typemap(javainterfaces) NdbScanOperation "NdbScanOperation";

%javamethodmodifiers NdbScanOperation::getBlobHandle "@Override
  public";
%javamethodmodifiers NdbScanOperation::getNdbError "@Override
  public";
%javamethodmodifiers NdbScanOperation::getNdbScanFilter "protected";
%rename(realGetNdbScanFilter) getNdbScanFilter();

%typemap(javaimports) NdbScanOperation "import java.util.EnumSet;"
%typemap(javacode) NdbScanOperation %{

  public NdbScanFilter getNdbScanFilter() throws NdbApiException {
    return new NdbScanFilterImpl(this);
  }

  public void readTuples(NdbOperation.LockMode mode,
                         NdbScanOperation.ScanFlag scanFlag, long parallel,
                         long batch) throws NdbApiException {
    readTuples(mode,scanFlag.type,parallel,batch);
  }

  public void readTuples(NdbOperation.LockMode mode,
                         EnumSet<NdbScanOperation.ScanFlag> scanFlag,
                         long parallel,
                         long batch) throws NdbApiException {
    long flagVal = 0;
    for (NdbScanOperation.ScanFlag s : scanFlag) {
      flagVal = flagVal | s.type;
    }
    readTuples(mode,flagVal,parallel,batch);
  }


  %}

%include "ndbapi/NdbScanOperation.i"
