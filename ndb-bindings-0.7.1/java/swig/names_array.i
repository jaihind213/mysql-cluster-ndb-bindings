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

/* char ** typemap */

/* This tells SWIG to treat char ** as a special case when used as a parameter
   in a function call */
%typemap(in) (unsigned noOfNames, const char ** names) {
  Uint32 i = 0;
  $1 = jenv->GetArrayLength($input);
  $2 = (char **) malloc(($1+1));
  /* make a copy of each string */
  for (i = 0; i<$1; i++) {
    jstring j_string = (jstring)(jenv->GetObjectArrayElement($input, i));
    const char * c_string = jenv->GetStringUTFChars(j_string, 0);
    $2[i] = (char *)malloc(strlen((c_string)+1));
    strcpy($2[i], c_string);
    jenv->ReleaseStringUTFChars(j_string, c_string);
    jenv->DeleteLocalRef(j_string);
  }
  $2[i] = 0;
 }

/* This cleans up the memory we malloc'd before the function call */
%typemap(freearg) (unsigned noOfNames, const char ** names)  {
  Uint32 i;
  for (i=0; i<$1-1; i++)
    free($2[i]);
  free($2);
 }

/* This allows a C function to return a char ** as a Java String array */
%typemap(out) char ** {
  Uint32 i;
  Uint32 len=0;
  jstring temp_string;
  const jclass clazz = jenv->FindClass("java/lang/String");

  while ($1[len]) len++;
  jresult = jenv->NewObjectArray(len, clazz, NULL);
  /* exception checking omitted */

  for (i=0; i<len; i++) {
    temp_string = jenv->NewStringUTF(*result++);
    jenv->SetObjectArrayElement(jresult, i, temp_string);
    jenv->DeleteLocalRef(temp_string);
  }
 }

/* These 3 typemaps tell SWIG what JNI and Java types to use */
%typemap(jni) (unsigned noOfNames, const char ** names) "jobjectArray"
%typemap(jtype) (unsigned noOfNames, const char ** names) "String[]"
%typemap(jstype)(unsigned noOfNames, const char ** names) "String[]"

 /* These 2 typemaps handle the conversion of the jtype to jstype typemap type
    and visa versa */
%typemap(javain) (unsigned noOfNames, const char ** names) "$javainput"
%typemap(javaout) char ** {
  return $jnicall;
 }

