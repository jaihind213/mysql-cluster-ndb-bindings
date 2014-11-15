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

%module "libmgmapi"

%include "mgmapi/mgmglobals.i"

%include "mgmapi/mgmenums.i"



%{

#include "zend_exceptions.h"

// # define SWIG_exception(code, msg) { mt_zend_throw_exception(NULL, msg, code TSRMLS_CC); }
# define NDB_exception(code, msg) { zend_throw_exception(NULL, (char *)msg, code TSRMLS_CC); }
# define NDB_exception_err(code, msg, err) { zend_throw_exception(NULL, (char *)msg, code TSRMLS_CC); }


//ZEND_API zval * mt_zend_throw_exception(zend_class_entry *exception_ce, const char *message, long code TSRMLS_DC);

ZEND_API zval  * mt_zend_throw_exception(zend_class_entry *exception_ce, const char *message, long code TSRMLS_DC)
{
  char * mt_message = (char *)malloc(sizeof(message));
  strcpy(mt_message,message);
  zval * retval = zend_throw_exception(exception_ce,mt_message,code);
  free(mt_message);
  return retval; }
%}

