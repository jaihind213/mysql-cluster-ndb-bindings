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

%typemap(jni) NdbDateTime * "jobject"
%typemap(jtype) NdbDateTime * "Object"
%typemap(jstype) NdbDateTime * "java.sql.Timestamp"
%typemap(javain) NdbDateTime * "Util.timestampToCalendar($javainput)"
%typemap(javaout) NdbDateTime * { return (java.sql.Timestamp)$jnicall; }

%typemap(jni) NdbTime * "jobject"
%typemap(jtype) NdbTime * "Object"
%typemap(jstype) NdbTime * "java.sql.Time"
%typemap(javain) NdbTime * "Util.timeToCalendar($javainput)"
%typemap(javaout) NdbTime * { return new java.sql.Time(((java.sql.Timestamp)$jnicall).getTime()); }

%typemap(jni) NdbDate * "jobject"
%typemap(jtype) NdbDate * "Object"
%typemap(jstype) NdbDate * "java.sql.Date"
%typemap(javain) NdbDate * "Util.dateToCalendar($javainput)"
%typemap(javaout) NdbDate * { return new java.sql.Date(((java.sql.Timestamp)$jnicall).getTime()); }

%typemap(in) NdbDateTime *, NdbDate *, NdbTime * {
  NdbDateTime * dt = new NdbDateTime();
  jclass calendar_class = jenv->FindClass("java/util/Calendar");
  jmethodID getID = jenv->GetMethodID(calendar_class,"get","(I)I");
  jfieldID yearID = jenv->GetStaticFieldID(calendar_class,"YEAR","I");
  jfieldID monthID = jenv->GetStaticFieldID(calendar_class,"MONTH","I");
  jfieldID dayID = jenv->GetStaticFieldID(calendar_class,"DAY_OF_MONTH","I");
  jfieldID hourID = jenv->GetStaticFieldID(calendar_class,"HOUR_OF_DAY","I");
  jfieldID minuteID = jenv->GetStaticFieldID(calendar_class,
                                                    "MINUTE","I");
  jfieldID secondID = jenv->GetStaticFieldID(calendar_class,
                                                    "SECOND","I");
  jint yearfield = jenv->GetStaticIntField(calendar_class,yearID);
  jint monthfield = jenv->GetStaticIntField(calendar_class,monthID);
  jint dayfield = jenv->GetStaticIntField(calendar_class,dayID);
  jint hourfield = jenv->GetStaticIntField(calendar_class,hourID);
  jint minutefield = jenv->GetStaticIntField(calendar_class,minuteID);
  jint secondfield = jenv->GetStaticIntField(calendar_class,secondID);
  jint yearval = jenv->CallIntMethod($input,getID,yearfield);
  jint monthval = jenv->CallIntMethod($input,getID,monthfield);
  jint dayval = jenv->CallIntMethod($input,getID,dayfield);
  jint hourval = jenv->CallIntMethod($input,getID,hourfield);
  jint minuteval = jenv->CallIntMethod($input,getID,minutefield);
  jint secondval = jenv->CallIntMethod($input,getID,secondfield);

  dt->year = (uint)yearval;
  dt->month = (uint)monthval+1;
  dt->day = (uint)dayval;
  dt->hour = (uint)hourval;
  dt->minute = (uint)minuteval;
  dt->second = (uint)secondval;
  $1 = dt;
 }

%typemap(out) NdbDateTime *, NdbDate *, NdbTime * {
  jclass timestamp_class = jenv->FindClass("java/sql/Timestamp");
  jmethodID timestamp_ctr =
    jenv->GetMethodID(timestamp_class,"<init>",
                            "(IIIIIII)V");
  assert(timestamp_class && timestamp_ctr);
  $result = jenv->NewObject(timestamp_class,timestamp_ctr,
                            $1->year - 1900, $1->month - 1, $1->day,
                            $1->hour, $1->minute, $1->second, 0);
 }


%typemap(freearg) (NdbDateTime *) {
  delete $1;
 }
