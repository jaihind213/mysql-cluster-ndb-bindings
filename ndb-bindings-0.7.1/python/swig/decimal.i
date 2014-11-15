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


/**
 * Typemap to convert a decimal_t* to a decimal.Decimal
 * $1 is a decimal_t *
 * $result is a PyObject representing a decimal.Decimal

 * fixed_precision is the total number of digits.
 * fixed_scale is the number after the decimal point
 * int decimal2string(decimal_t *from, char *to, int *to_len,
 *                      int fixed_precision, int fixed_decimals,
 *                      char filler);
 * theLen is the string length, and needs to account for the "."
 *   character
 **/
%typemap(out) decimal_t * {

  if ($1 == NULL) {
    return NULL;
  }
  int theLen = ($1->frac > 0) ? $1->intg+$1->frac+1 : $1->intg;
  if ($1->sign) {
    theLen++;
  }
  $result = PyString_FromStringAndSize(NULL,theLen);
  if ($result != NULL) {

    Py_INCREF($result);
    // PyString AsString returns a pointer to an internal string buffer
    // so we should only have one memcpy here
    char * destString = PyString_AsString($result);
    if (destString == NULL) {
      Py_DECREF($result);
      $result = NULL;
    } else {
      int ret = decimal2string($1, destString, &theLen,
                               theLen,$1->frac,'0');
      if (ret != 0){
        Py_DECREF($result);
        $result = NULL;
      }
    }
  }
 }

/* Internal method indent starts at 8 spaces. */
%pythonappend NdbRecAttr::getDecimal() %{
        val=decimal.Decimal(val)
    %}
/**
 * Typemap to convert a java.math.BigDecimal to a decimal_t *
 * $input is a jobject representing a java.math.BigDecimal
 * $1 is a decimal_t *
 **/
%typemap(in) decimal_t * {
  /**
   * $input should be a PyObject
   * a decimal can be gotten as a tuple by the method as_tuple()
   * which returns ( sign , (tuple_of_digits) , exponent)
   **/

  // decTuple is a new reference - we must delete it
  PyObject * decString = PyObject_CallMethod($input, (char *)"__str__", NULL);
  if (decString == NULL)
  {
    NDB_exception(NdbApiException,"Decimal argument conversion failed");
  }
  Py_INCREF(decString);

  Py_ssize_t string_size = PyString_Size(decString);
  /* Returns internal pointer to string buffer - we don't own it */
  const char * decimal_string = PyString_AsString(decString);

  $1 = (decimal_t *)malloc(sizeof(decimal_t));
  /* TODO: This sets len too high. We should calculate this for real */
  $1->len=string_size;
  $1->buf = (decimal_digit_t *)malloc(sizeof(decimal_digit_t)*string_size);
  char * end = (char *)(decimal_string+string_size);

  string2decimal(decimal_string,$1,&end);
  Py_DECREF(decString);

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
