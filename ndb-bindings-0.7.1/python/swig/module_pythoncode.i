/* -*- mode: python; c-basic-offset: 2; indent-tabs-mode: nil; -*-
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

%pythoncode %{

    NdbApiException = _ndbapi.NdbApiException
    NdbApiPermanentException = _ndbapi.NdbApiPermanentException
    NdbApiTemporaryException = _ndbapi.NdbApiTemporaryException
    NdbApiUnknownResult = _ndbapi.NdbApiUnknownResult
    ApplicationError = _ndbapi.ApplicationError
    NoDataFound = _ndbapi.NoDataFound
    ConstraintViolation = _ndbapi.ConstraintViolation
    SchemaError = _ndbapi.SchemaError
    UserDefinedError = _ndbapi.UserDefinedError
    InsufficientSpace = _ndbapi.InsufficientSpace
    TemporaryResourceError = _ndbapi.TemporaryResourceError
    NodeRecoveryError = _ndbapi.NodeRecoveryError
    OverloadError = _ndbapi.OverloadError
    TimeoutExpired = _ndbapi.TimeoutExpired
    UnknownResultError = _ndbapi.UnknownResultError
    InternalError = _ndbapi.InternalError
    FunctionNotImplemented = _ndbapi.FunctionNotImplemented
    UnknownErrorCode = _ndbapi.UnknownErrorCode
    NodeShutdown = _ndbapi.NodeShutdown
    SchemaObjectExists = _ndbapi.SchemaObjectExists
    InternalTemporary = _ndbapi.InternalTemporary

    def connect(connectstring=None,no_retries=0,retry_delay_in_seconds=1,
                verbose=0,timeout_for_first_alive=1,
                timeout_after_first_alive=1,*args,**kwargs):
      """ Provide DBAPI 2.0 support """
      connection=NdbClusterConnection(connectstring)
      connection.connect(no_retries,retry_delay_in_seconds,verbose)
      connection.wait_until_ready(timeout_for_first_alive,timeout_after_first_alive)
      return connection


    value_lookup[NDB_TYPE_UNDEFINED]=NdbRecAttr.undefinedValue
    value_lookup[NDB_TYPE_TINYINT]=NdbRecAttr.getInt
    value_lookup[NDB_TYPE_TINYUNSIGNED]=NdbRecAttr.getUint
    value_lookup[NDB_TYPE_SMALLINT]=NdbRecAttr.getInt
    value_lookup[NDB_TYPE_SMALLUNSIGNED]=NdbRecAttr.getUint
    value_lookup[NDB_TYPE_MEDIUMINT]=NdbRecAttr.getInt
    value_lookup[NDB_TYPE_MEDIUMUNSIGNED]=NdbRecAttr.getUint
    value_lookup[NDB_TYPE_INT]=NdbRecAttr.getInt
    value_lookup[NDB_TYPE_UNSIGNED]=NdbRecAttr.getUint
    value_lookup[NDB_TYPE_BIGINT]=NdbRecAttr.getLong
    value_lookup[NDB_TYPE_BIGUNSIGNED]=NdbRecAttr.getUlong
    value_lookup[NDB_TYPE_FLOAT]=NdbRecAttr.getFloat
    value_lookup[NDB_TYPE_DOUBLE]=NdbRecAttr.getDouble
    value_lookup[NDB_TYPE_OLDDECIMAL]=NdbRecAttr.getDouble
    value_lookup[NDB_TYPE_CHAR]=NdbRecAttr.getString
    value_lookup[NDB_TYPE_VARCHAR]=NdbRecAttr.getString
    value_lookup[NDB_TYPE_BINARY]=NdbRecAttr.getBytes
    value_lookup[NDB_TYPE_VARBINARY]=NdbRecAttr.getBytes
    value_lookup[NDB_TYPE_DATETIME]=NdbRecAttr.getDatetime
    value_lookup[NDB_TYPE_DATE]=NdbRecAttr.getDate
    value_lookup[NDB_TYPE_BLOB]=NdbRecAttr.undefinedValue
    value_lookup[NDB_TYPE_TEXT]=NdbRecAttr.undefinedValue
    value_lookup[NDB_TYPE_BIT]=NdbRecAttr.undefinedValue
    value_lookup[NDB_TYPE_LONGVARCHAR]=NdbRecAttr.getString
    value_lookup[NDB_TYPE_LONGVARBINARY]=NdbRecAttr.getBytes
    value_lookup[NDB_TYPE_TIME]=NdbRecAttr.getTime
    value_lookup[NDB_TYPE_YEAR]=NdbRecAttr.undefinedValue
    value_lookup[NDB_TYPE_TIMESTAMP]=NdbRecAttr.getTimestamp
    value_lookup[NDB_TYPE_OLDDECIMALUNSIGNED]=NdbRecAttr.getDouble
    value_lookup[NDB_TYPE_DECIMAL]=NdbRecAttr.getDecimal
    value_lookup[NDB_TYPE_DECIMALUNSIGNED]=NdbRecAttr.getDecimal

    equal_lookup[NDB_TYPE_UNDEFINED]=NdbOperation.undefinedValue
    equal_lookup[NDB_TYPE_TINYINT]=NdbOperation.equalInt
    equal_lookup[NDB_TYPE_TINYUNSIGNED]=NdbOperation.equalUint
    equal_lookup[NDB_TYPE_SMALLINT]=NdbOperation.equalInt
    equal_lookup[NDB_TYPE_SMALLUNSIGNED]=NdbOperation.equalUint
    equal_lookup[NDB_TYPE_MEDIUMINT]=NdbOperation.equalInt
    equal_lookup[NDB_TYPE_MEDIUMUNSIGNED]=NdbOperation.equalUint
    equal_lookup[NDB_TYPE_INT]=NdbOperation.equalInt
    equal_lookup[NDB_TYPE_UNSIGNED]=NdbOperation.equalUint
    equal_lookup[NDB_TYPE_BIGINT]=NdbOperation.equalLong
    equal_lookup[NDB_TYPE_BIGUNSIGNED]=NdbOperation.equalUlong
    equal_lookup[NDB_TYPE_FLOAT]=NdbOperation.undefinedValue
    equal_lookup[NDB_TYPE_DOUBLE]=NdbOperation.undefinedValue
    equal_lookup[NDB_TYPE_OLDDECIMAL]=NdbOperation.undefinedValue
    equal_lookup[NDB_TYPE_CHAR]=NdbOperation.equalString
    equal_lookup[NDB_TYPE_VARCHAR]=NdbOperation.equalString
    equal_lookup[NDB_TYPE_BINARY]=NdbOperation.equalBytes
    equal_lookup[NDB_TYPE_VARBINARY]=NdbOperation.equalBytes
    equal_lookup[NDB_TYPE_DATETIME]=NdbOperation.equalDatetime
    equal_lookup[NDB_TYPE_DATE]=NdbOperation.undefinedValue
    equal_lookup[NDB_TYPE_BLOB]=NdbOperation.undefinedValue
    equal_lookup[NDB_TYPE_TEXT]=NdbOperation.undefinedValue
    equal_lookup[NDB_TYPE_BIT]=NdbOperation.undefinedValue
    equal_lookup[NDB_TYPE_LONGVARCHAR]=NdbOperation.equalString
    equal_lookup[NDB_TYPE_LONGVARBINARY]=NdbOperation.equalBytes
    equal_lookup[NDB_TYPE_TIME]=NdbOperation.undefinedValue
    equal_lookup[NDB_TYPE_YEAR]=NdbOperation.undefinedValue
    equal_lookup[NDB_TYPE_TIMESTAMP]=NdbOperation.equalTimestamp
    equal_lookup[NDB_TYPE_OLDDECIMALUNSIGNED]=NdbOperation.undefinedValue
    equal_lookup[NDB_TYPE_DECIMAL]=NdbOperation.equalDecimal
    equal_lookup[NDB_TYPE_DECIMALUNSIGNED]=NdbOperation.equalDecimal

    setval_lookup[NDB_TYPE_UNDEFINED]=NdbOperation.setUndefinedValue
    setval_lookup[NDB_TYPE_TINYINT]=NdbOperation.setInt
    setval_lookup[NDB_TYPE_TINYUNSIGNED]=NdbOperation.setUint
    setval_lookup[NDB_TYPE_SMALLINT]=NdbOperation.setInt
    setval_lookup[NDB_TYPE_SMALLUNSIGNED]=NdbOperation.setUint
    setval_lookup[NDB_TYPE_MEDIUMINT]=NdbOperation.setInt
    setval_lookup[NDB_TYPE_MEDIUMUNSIGNED]=NdbOperation.setUint
    setval_lookup[NDB_TYPE_INT]=NdbOperation.setInt
    setval_lookup[NDB_TYPE_UNSIGNED]=NdbOperation.setUint
    setval_lookup[NDB_TYPE_BIGINT]=NdbOperation.setLong
    setval_lookup[NDB_TYPE_BIGUNSIGNED]=NdbOperation.setUlong
    setval_lookup[NDB_TYPE_FLOAT]=NdbOperation.setFloat
    setval_lookup[NDB_TYPE_DOUBLE]=NdbOperation.setDouble
    setval_lookup[NDB_TYPE_OLDDECIMAL]=NdbOperation.setDouble
    setval_lookup[NDB_TYPE_CHAR]=NdbOperation.setString
    setval_lookup[NDB_TYPE_VARCHAR]=NdbOperation.setString
    setval_lookup[NDB_TYPE_BINARY]=NdbOperation.setBytes
    setval_lookup[NDB_TYPE_VARBINARY]=NdbOperation.setBytes
    setval_lookup[NDB_TYPE_DATETIME]=NdbOperation.setDatetime
    setval_lookup[NDB_TYPE_DATE]=NdbOperation.setDate
    setval_lookup[NDB_TYPE_BLOB]=NdbOperation.setUndefinedValue
    setval_lookup[NDB_TYPE_TEXT]=NdbOperation.setUndefinedValue
    setval_lookup[NDB_TYPE_BIT]=NdbOperation.setUndefinedValue
    setval_lookup[NDB_TYPE_LONGVARCHAR]=NdbOperation.setString
    setval_lookup[NDB_TYPE_LONGVARBINARY]=NdbOperation.setBytes
    setval_lookup[NDB_TYPE_TIME]=NdbOperation.setTime
    setval_lookup[NDB_TYPE_YEAR]=NdbOperation.setUndefinedValue
    setval_lookup[NDB_TYPE_TIMESTAMP]=NdbOperation.setTimestamp
    setval_lookup[NDB_TYPE_OLDDECIMALUNSIGNED]=NdbOperation.setDouble
    setval_lookup[NDB_TYPE_DECIMAL]=NdbOperation.setDecimal
    setval_lookup[NDB_TYPE_DECIMALUNSIGNED]=NdbOperation.setDecimal

    %}
