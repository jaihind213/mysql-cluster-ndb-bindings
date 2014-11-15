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


%typemap(javainterfaces) NdbScanFilter "NdbScanFilter";
%typemap(jstype) NdbScanFilter::Group "NdbScanFilter.Group"
%typemap(javaout) NdbScanFilter::Group {
  return NdbScanFilter.Group.swigToEnum
    (ndbjJNI.NdbScanFilterImpl_getGroup(swigCPtr, this));
 }
%typemap(jstype) NdbScanFilter::BinaryCondition "NdbScanFilter.BinaryCondition"
%typemap(javaout) NdbScanFilter::BinaryCondition {
  return NdbScanFilter.BinaryCondition.swigToEnum
    (ndbjJNI.NdbScanFilterImpl_getBinaryCondition(swigCPtr, this));
 }

%include "ndbapi/NdbScanFilter.i"
