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

#define NDB_exception(excp, msg) { ndb_throw_exception(jenv, #excp, msg);  }

  void ndb_throw_exception(JNIEnv *jenv, const char * excp, const char *msg) {
    const char * prefix = "com/mysql/cluster/mgmj/";
    char * exception = (char *)malloc(strlen(excp)+strlen(prefix)+1);
    strcpy(exception,prefix);
    strcat(exception,excp);

    jclass clazz = jenv->FindClass(exception);
    jenv->ThrowNew(clazz,msg);
}
%}
