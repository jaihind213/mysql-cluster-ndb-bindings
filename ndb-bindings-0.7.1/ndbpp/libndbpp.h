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

#ifndef libndbpp_h
#define libndbpp_h 1

#include <my_global.h>
#include <my_sys.h>

#include <mysql.h>
#include <NdbApi.hpp>

#include <stdlib.h>
#include <string.h>

/* These get included in mysql.h. Not sure they should... */
#undef PACKAGE
#undef PACKAGE_NAME
#undef PACKAGE_STRING
#undef PACKAGE_TARNAME
#undef PACKAGE_VERSION
#undef VERSION

#include "config.h"

enum NdbException {
  NdbApiException,
  NdbApiPermanentException,
  NdbApiTemporaryException,
  NdbApiUnknownResult,
  ApplicationError,
  NoDataFound,
  ConstraintViolation,
  SchemaError,
  UserDefinedError,
  InsufficientSpace,
  TemporaryResourceError,
  NodeRecoveryError,
  OverloadError,
  TimeoutExpired,
  UnknownResultError,
  InternalError,
  FunctionNotImplemented,
  UnknownErrorCode,
  NodeShutdown,
  SchemaObjectExists,
  InternalTemporary
};

long long getMicroTime();

/* voidint is a way to mark a method that returns int for SWIG
   to tell it we want the wrapped function to return void, but
   we don't want to discard the return value out of hand because
   we need it at the wrapping layer to find errors */
typedef int voidint;
typedef Uint32 NdbTimestamp;


typedef struct st_byte {
  char * theString;
  int theLength;
} BYTES;

#ifdef _mysql_h
#ifndef _decimal_h

#define DECIMAL_BUFF 9

typedef int32 decimal_digit_t;

typedef struct st_decimal_t {
  int intg, frac, len;
  my_bool sign;
  decimal_digit_t *buf;
} decimal_t;

#define string2decimal(A,B,C) internal_str2dec((A), (B), (C), 0)
#define decimal_string_size(dec) (((dec)->intg ? (dec)->intg : 1) +     \
                                  (dec)->frac + ((dec)->frac > 0) + 2)
extern "C" {
  int decimal_size(int precision, int scale);
  int decimal_bin_size(int precision, int scale);
  int decimal2bin(decimal_t *from, char *to, int precision, int scale);
  int bin2decimal(char *from, decimal_t *to, int precision, int scale);
  int decimal2string(decimal_t *from, char *to, int *to_len,
                     int fixed_precision, int fixed_decimals,
                     char filler);
  int internal_str2dec(const char *from, decimal_t *to, char **end,
                       my_bool fixed);
}
#endif
#endif


class NdbDateTime
{
public:
  unsigned int  year, month, day, hour, minute, second;
  NdbDateTime();
};


typedef NdbDateTime NdbDate;
typedef NdbDateTime NdbTime;

/* We don't just typedef these right in the first place
   because there because that would mean NdbDictionary::Dictionary
   would get renamed to NdbDictionary at the C level, which
   wouldn't work out so well. */

typedef NdbDictionary::Object NdbDictObject;
typedef NdbDictionary::Table NdbDictTable;
typedef NdbDictionary::Column NdbDictColumn;
typedef NdbDictionary::Index NdbDictIndex;
typedef NdbDictionary::Dictionary NdbDictDictionary;
typedef NdbDictionary::Event NdbDictEvent;


int getColumnId(NdbOperation * op, const char* columnName);
char * ndbFormatString(const NdbDictColumn * theColumn,
                       const char* aString, size_t len);
Uint64 ndbFormatDateTime(const NdbDictColumn * theColumn, NdbDateTime * tm);
char * decimal2bytes(decimal_t * val);

NdbDateTime * createNdbDateTime(const NdbRecAttr * rec);


Int64 selectCount(Ndb * theNdb, const char * tbl);
Uint64 selectCountBig(Ndb * theNdb, const char * tbl, bool &selectCountError);

int interpretedIncrement(NdbOperation * theOp, Uint32 anAttrId, Uint64 aValue);
int interpretedDecrement(NdbOperation * theOp, Uint32 anAttrId, Uint64 aValue);

#endif
