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

%typemap(jni) decimal_t * "jobject"
%typemap(jtype) decimal_t * "Object"
%typemap(jstype) decimal_t * "java.math.BigDecimal"
%typemap(javain) decimal_t * "$javainput"
%typemap(javaout) decimal_t * { return new java.math.BigDecimal((String)$jnicall); }


/**
 * Typemap to convert a decimal_t* to a java.math.BigDecimal
 * $1 is a decimal_t *
 * $result is a jobject representing a java.math.BigDecimal
 **/
%typemap(out) decimal_t * {

  if ($1 == NULL) {
    return NULL;
  }
  int theSize = ($1->frac > 0) ? $1->intg+$1->frac+1 : $1->intg;
  if ($1->sign) {
    theSize++;
  }

  char * theBuff = (char *)malloc(theSize);
  decimal2string($1,theBuff,&theSize,theSize,$1->frac,'0');

  $result = jenv->NewStringUTF((const char *)theBuff);

  free(theBuff);
 }

/**
 * Typemap to convert a java.math.BigDecimal to a decimal_t *
 * $input is a jobject representing a java.math.BigDecimal
 * $1 is a decimal_t *
 **/
%typemap(in) decimal_t * {

  static jclass bigdecimal_class = jenv->FindClass("java/math/BigDecimal");

  static jmethodID toPlainString =
    jenv->GetMethodID(bigdecimal_class, "toPlainString",
                      "()Ljava/lang/String;");

  jstring decString = (jstring)jenv->CallObjectMethod($input, toPlainString);
  const char * decimal_string = jenv->GetStringUTFChars(decString,NULL);
  jsize string_size = jenv->GetStringUTFLength(decString);

  $1 = (decimal_t *)malloc(sizeof(decimal_t));
  /* TODO: This sets len too high. We should calculate this for real */
  $1->len = string_size; 
  $1->buf = (decimal_digit_t *)malloc(sizeof(decimal_digit_t)*string_size);
  char * end = (char *)(decimal_string+string_size);

  /* TODO: Handle errors here */
  string2decimal(decimal_string,$1,&end);

  jenv->ReleaseStringUTFChars(decString, decimal_string);

 }

%typemap(freearg) decimal_t * { 

    if ($1)
  {
    if ($1->buf)
    {
      free($1->buf);
    }
    free($1);
  }
 }

