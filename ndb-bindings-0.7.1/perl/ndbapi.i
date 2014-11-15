// -*- mode: c++ -*-
/*  ndb-bindings: Bindings for the NDB API
    Copyright (C) 2006 MySQL, Inc.
    
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or 
    (at your option) any later version.
    
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/

%module "ndbapi"


%include "ndbapi/ndbglobals.i"

typedef int voidint;

%{

#define NDB_exception(excp, msg) do { sv_setpvf(GvSV(PL_errgv),#excp " %s\n", msg); SWIG_fail; } while (0)
#define NDB_exception_err(excp, err) do { sv_setpvf(GvSV(PL_errgv),#excp " %s\n", err.message); SWIG_fail; } while (0)

#include <my_global.h>

%}
%include "perl_callback_typemap.i"

%feature("shadow") Ndb_cluster_connection::createNdb(const char* aCatalogName="", const char* aSchemaName="def")
%{
   sub createNdb {
        my $self=$_[0];
        my $ndb=ndbapic::NdbClusterConnection_createNdb(@_);
        my $t=tied(%{$ndb});
        $ndbapi::ndb_bindings_owned{$t}=$self;
        return $ndb;
    } 
%}
%include "ndbapi/NdbClusterConnection.i"




%include "ndbapi/Ndb.i"
%include "ndbapi/NdbDictionary.i"
%include "ndbapi/NdbTransaction.i"
%include "perl_extend_ndbtransaction_async.i"
%include "ndbapi/NdbOperation.i"
%include "ndbapi/NdbScanOperation.i"
%include "ndbapi/NdbIndexOperation.i"
%include "ndbapi/NdbIndexScanOperation.i"
%include "ndbapi/NdbEventOperation.i"
 //%include "perl_ndbrecattr_typemap.i" 
%include "ndbapi/NdbRecAttr.i"
%include "perl_extend_ndbrecattr.i"
%include "ndbapi/NdbError.i"
%include "ndbapi/NdbBlob.i"
%include "ndbapi/NdbScanFilter.i"
