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

%runtime %{

#include <datetime.h>

  %}

%typemap(in) (MYSQL_TIME *) {
  if (PyDateTime_Check($input)) {
    MYSQL_TIME * dt = (MYSQL_TIME *)malloc(sizeof(MYSQL_TIME));
    if (dt==NULL)
      NDB_exception(NdbApiException,"Failed to allocate a MYSQL_TIME");
    dt->year = PyDateTime_GET_YEAR($input);
    dt->month = PyDateTime_GET_MONTH($input);
    dt->day = PyDateTime_GET_DAY($input);
    dt->hour = PyDateTime_DATE_GET_HOUR($input);
    dt->minute = PyDateTime_DATE_GET_MINUTE($input);
    dt->second = PyDateTime_DATE_GET_SECOND($input);
    $1 = dt;
  } else {
    NDB_exception(NdbApiException,"DateTime argument required!");
  }
 }

%typemap(freearg) (MYSQL_TIME *) {
  free((MYSQL_TIME *) $1);
 }
