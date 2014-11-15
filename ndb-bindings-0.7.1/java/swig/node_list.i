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


/* int * typemap */

/* These 3 typemaps tell SWIG what JNI and Java types to use */
%typemap(jni) (int no_of_nodes, const int * node_list) "jintArray"
%typemap(jtype) (int no_of_nodes, const int * node_list) "int[]"
%typemap(jstype)(int no_of_nodes, const int * node_list) "int[]"

/* This tells SWIG to treat int * as a special case when used as a parameter
   in a function call */
%typemap(in) (int no_of_nodes, const int * node_list) {
    $1 = jenv->GetArrayLength($input);
    jboolean mode = false;
    $2= (int *)jenv->GetIntArrayElements($input,&mode);
 }

/* This cleans up the memory we malloc'd before the function call */
%typemap(freearg) (int no_of_nodes, const int * node_list)  {
  jenv->ReleaseIntArrayElements($input,(jint *)$2,0);
 }

%typemap(argout) (int no_of_nodes, const int * node_list) {

 }
