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

%module "mgmapi"

%include "mgmapi/mgmglobals.i"
%{
#undef SWIG_exception
#define SWIG_exception(code, msg) do { SWIG_Error(code, msg); SWIG_fail; } while(0)

#define NDB_exception(excp, msg) do { sv_setpvf(GvSV(PL_errgv),#excp " %s\n", msg); SWIG_fail; } while (0)

#include <my_global.h>

%}

%include "mgmapi/mgmenums.i"

%include "mgmapi/NdbLogEvent.i"
%include "mgmapi/NdbLogEventManager.i"
%include "mgmapi/NdbMgm.i"

%include "mgmapi/ClusterState.i"
%include "mgmapi/NodeState.i"
%include "mgmapi/NdbMgmReply.i"

