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

#include "libndbpp.h"

NdbDateTime::NdbDateTime() {
  year=0;
  month=0;
  day=0;
  hour=0;
  minute=0;
  second=0;
}

NdbDateTime * createNdbDateTime(const NdbRecAttr * rec) {
  NdbDateTime * dt = new NdbDateTime();
  int int_date = -1, int_time = -99;
  unsigned long long datetime;

  switch(rec->getType()) {
  case NdbDictionary::Column::Datetime :
    datetime = rec->u_64_value();
    int_date = datetime / 1000000;
    int_time = datetime - (unsigned long long) int_date * 1000000;
    break;
  case NdbDictionary::Column::Time :
    int_time = sint3korr(rec->aRef());
    break;
  case NdbDictionary::Column::Date :
    int_date = uint3korr(rec->aRef());
    dt->day = (int_date & 31);      // five bits
    dt->month  = (int_date >> 5 & 15); // four bits
    dt->year = (int_date >> 9);
    return dt;
  default:
    delete dt;
    return NULL;
  }

  if(int_time != -99) {
    dt->hour = int_time/10000;
    dt->minute  = int_time/100 % 100;
    dt->second  = int_time % 100;
  }
  if(int_date != -1) {
    dt->year = int_date/10000 % 10000;
    dt->month  = int_date/100 % 100;
    dt->day = int_date % 100;
  }
  return dt;

}
