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

%typemap(in) asynch_callback_t * cb
%{
  $1 = new asynch_callback_t;
  $1->env = jenv;
  $1->obj = jenv->NewGlobalRef($input);
  $1->create_time = 0;
  %}


%typemap(jni)    asynch_callback_t * "jobject"
%typemap(jtype)  asynch_callback_t * "Object"
%typemap(jstype) asynch_callback_t * "BaseCallback"
%typemap(javain) asynch_callback_t * "$javainput"

 // C++ Code implementing the Callback
%{

  struct
    asynch_callback_t
  {
    JNIEnv *env;
    jobject obj;
    long long create_time;
  };



  static void theCallBack(int result,
                          NdbTransaction *trans,
                          void *aObject)
  {

    asynch_callback_t * callback_data = (asynch_callback_t *)aObject;

    JNIEnv *jenv = callback_data->env;

    jclass callablecls = jenv->GetObjectClass((callback_data->obj));

    jmethodID mid = jenv->GetMethodID(callablecls,
                                      "jni_call_callback","(IJJ)V");

    if ((mid == NULL) || (mid == 0)) {
      jenv->DeleteGlobalRef(callback_data->obj);
      jclass clazz = jenv->FindClass("java/lang/RuntimeException");
      jenv->ThrowNew(clazz,"Invalid exception class specified");
    }

    jenv->CallVoidMethod(callback_data->obj,mid,(jlong)result,
                         trans,callback_data->create_time);
    jenv->DeleteGlobalRef(callback_data->obj);
    delete callback_data; // Clean up the handle containing the callback info
  }

  %}



%typemap(in) event_callback_t * cb
%{

  callback_data = new event_callback_t;
  callback_data->env = jenv;
  callback_data->obj = $input;

  $1 = callback_data;
  %}

