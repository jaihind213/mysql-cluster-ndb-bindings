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

%typemap(in) asynch_callback_t * cb {

  if (!PyCallable_Check($input)) {
    PyErr_SetString(PyExc_TypeError, "Need a callable object!");
    return NULL;
  }
  $1 = new asynch_callback_t;
  $1->obj = $input;
  Py_INCREF($input);
  $1->create_time=0;
 }

%{


  typedef struct
    asynch_callback_t
  {
    PyObject *obj;
    long long create_time;
  };

/* This function matches the prototype of the normal C callback
   function for our widget. However, we use the clientdata pointer
   for holding a reference to a Python callable object. */


  static void theCallBack(int result,
                          NdbTransaction *trans,
                          void *aObject)
  {
    PyObject *func, *arglist;
    PyObject *trans_obj = 0;

    asynch_callback_t * callback_data = (asynch_callback_t *)aObject;
    func = (PyObject *) callback_data->obj;   // Get Python callable
    trans_obj = SWIG_NewPointerObj(SWIG_as_voidptr(trans),
                                   SWIGTYPE_p_NdbTransaction, 0 |  0 );

    // Build argument list
    arglist = Py_BuildValue("(i,O)",result, trans_obj);

    delete callback_data;
    PyEval_CallObject(func,arglist);     // Call Python
    Py_DECREF(func);
    Py_DECREF(arglist);                  // Trash arglist

  }

%}
