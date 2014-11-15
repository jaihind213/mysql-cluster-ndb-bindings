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

%include "globals.i"
%include "config.h"
%include "ndb_constants.h"
%{
#include "libndbpp.h"
%}

%inline %{
  void dbugPush(const char * dbugString) {
    (void)dbugString;
    DBUG_PUSH(dbugString);
  }
  %}

%constant const char *PRIMARY_INDEX_NAME= "PRIMARY";

%rename(NdbObject) NdbDictObject;
%rename(NdbTable) NdbDictTable;
%rename(NdbColumn) NdbDictColumn;
%rename(NdbIndex) NdbDictIndex;
%rename(NdbDictionary) NdbDictDictionary;
%rename(NdbEvent) NdbDictEvent;

/* Do this here so we can override it in the Java interface */
%rename(NdbClusterConnection) Ndb_cluster_connection;


typedef void (* NdbAsynchCallback)(int, NdbTransaction*, void*);
typedef void (* NdbEventCallback)(NdbEventOperation*, Ndb*, void*);

//typedef const char * NdbDatetime;
//typedef const char * NdbDate;
//typedef const char * NdbTime;
typedef Uint32 NdbTimestamp;

long long getMicroTime();

// ndbFormatString mallocs memory. Return value must be free'd by calling code
%newobject ndbformatString;
%typemap(newfree) char * "if ($1) free($1);";

%ndbexception("NdbApiException") {
  $action
    if (result==NULL) {
      NDB_exception(NdbApiException,"Error Converting Argument Type!");
    }
 }
char * ndbFormatString(const NdbDictColumn * theColumn,
                       const char* aString, size_t len);

%ndbnoexception;
