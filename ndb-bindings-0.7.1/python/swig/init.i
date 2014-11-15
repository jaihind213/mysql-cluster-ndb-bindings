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


typedef int voidint;

%init %{


  ndb_init();

  PyDateTime_IMPORT;

  NEW_BASE_pyexcept(NdbApiException);
  NEW_pyexcept(NdbApiPermanentException,NdbApiException);
  NEW_pyexcept(NdbApiTemporaryException,NdbApiException);
  NEW_pyexcept(NdbApiUnknownResult,NdbApiException);
  NEW_pyexcept(ApplicationError,NdbApiPermanentException);
  NEW_pyexcept(NoDataFound,NdbApiPermanentException);
  NEW_pyexcept(ConstraintViolation,NdbApiPermanentException);
  NEW_pyexcept(SchemaError,NdbApiPermanentException);
  NEW_pyexcept(UserDefinedError,NdbApiPermanentException);
  NEW_pyexcept(InsufficientSpace,NdbApiTemporaryException);
  NEW_pyexcept(TemporaryResourceError,NdbApiTemporaryException);
  NEW_pyexcept(NodeRecoveryError,NdbApiTemporaryException);
  NEW_pyexcept(OverloadError,NdbApiTemporaryException);
  NEW_pyexcept(TimeoutExpired,NdbApiTemporaryException);
  NEW_pyexcept(UnknownResultError,NdbApiUnknownResult);
  NEW_pyexcept(InternalError,NdbApiPermanentException);
  NEW_pyexcept(FunctionNotImplemented,NdbApiPermanentException);
  NEW_pyexcept(UnknownErrorCode,NdbApiUnknownResult);
  NEW_pyexcept(NodeShutdown,NdbApiTemporaryException);
  NEW_pyexcept(SchemaObjectExists,NdbApiPermanentException);
  NEW_pyexcept(InternalTemporary,NdbApiTemporaryException);

  %}

