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

class NdbOperation {

  NdbOperation(Ndb* aNdb, Type aType = PrimaryKeyAccess);
  virtual ~NdbOperation();

public:

  enum Type {
    PrimaryKeyAccess = 0,
    UniqueIndexAccess = 1,
    TableScan = 2,
    OrderedIndexScan = 3,
  };

  enum LockMode {
    LM_Read = 0,
    LM_Exclusive = 1,
    LM_CommittedRead = 2,
    LM_Dirty = 2
  };

  enum AbortOption {
    DefaultAbortOption = -1,///< Use default as specified by op-type
    AbortOnError = 0,       ///< Abort transaction on failed operation
    AO_IgnoreError = 2      ///< Transaction continues on failed operation
  };

  %ndbnoexception;

  // These are not expected to fail
  const NdbError & getNdbError() const;
  int getNdbErrorLine();
  const char* getTableName() const;

  void setPartitionId(Uint32 id);
  Uint32 getPartitionId() const;

  const NdbDictTable * getTable() const;

  %ndbexception("NdbApiException") {
    $action
      if (result == NULL) {
        NdbError err = arg1->getNdbError();
        NDB_exception_err(NdbApiException,err);
      }
  }

  NdbTransaction* getNdbTransaction();

  virtual NdbBlob* getBlobHandle(const char* anAttrName);
  virtual NdbBlob* getBlobHandle(Uint32 anAttrId);

  %ndbexception("NdbApiException") {
    $action
      if (result==-1) {
        NdbError err = arg1->getNdbError();
        NDB_exception_err(NdbApiException,err);
      }
  }


  %rename(equalInt) equal(const char *, Int32);
  %rename(equalInt) equal(Uint32, Int32);
  voidint equal(const char* anAttrName, Int32 aValue);
  voidint equal(Uint32 anAttrId, Int32 aValue);

  %rename(equalUint) equal(const char *, Uint32);
  %rename(equalUint) equal(Uint32, Uint32);
  voidint equal(const char* anAttrName, Uint32 aValue);
  voidint equal(Uint32 anAttrId, Uint32 aValue);

  %rename(equalLong) equal(const char *, Int64);
  %rename(equalLong) equal(Uint32, Int64);
  voidint equal(const char* anAttrName, Int64 aValue);
  voidint equal(Uint32 anAttrId, Int64 aValue);

  %rename(equalUlong) equal(const char *, Uint64);
  %rename(equalUlong) equal(Uint32, Uint64);

  voidint equal(const char* anAttrName, Uint64 aValue);
  voidint equal(Uint32 anAttrId, Uint64 aValue);


  virtual voidint readTuple(LockMode);
  virtual voidint insertTuple();
  virtual voidint writeTuple();
  virtual voidint updateTuple();
  virtual voidint deleteTuple();


  %rename(increment)   incValue(const char* anAttrName, Uint32 aValue);
  %rename(increment)   incValue(const char* anAttrName, Uint64 aValue);
  %rename(increment)   incValue(Uint32 anAttrId, Uint32 aValue);
  %rename(increment)   incValue(Uint32 anAttrId, Uint64 aValue);
  %rename(decrement)   subValue(const char* anAttrName, Uint32 aValue);
  %rename(decrement)   subValue(const char* anAttrName, Uint64 aValue);
  %rename(decrement)   subValue(Uint32 anAttrId, Uint32 aValue);
  %rename(decrement)   subValue(Uint32 anAttrId, Uint64 aValue);
  voidint   incValue(const char* anAttrName, Uint32 aValue);
  voidint   incValue(const char* anAttrName, Uint64 aValue);
  voidint   incValue(Uint32 anAttrId, Uint32 aValue);
  voidint   incValue(Uint32 anAttrId, Uint64 aValue);
  voidint   subValue(const char* anAttrName, Uint32 aValue);
  voidint   subValue(const char* anAttrName, Uint64 aValue);
  voidint   subValue(Uint32 anAttrId, Uint32 aValue);
  voidint   subValue(Uint32 anAttrId, Uint64 aValue);


  %ndbnoexception;

};


%extend NdbOperation {


public:

  %ndbexception("NdbApiException") {
    $action
      if (result==NULL) {
        NdbError err = arg1->getNdbError();
        NDB_exception_err(NdbApiException,err);
      }
  }

  NdbRecAttr* getValue(const char* anAttrName) {
    return self->getValue(anAttrName,NULL);
  }
  NdbRecAttr* getValue(Uint32 anAttrId) {
    return self->getValue(anAttrId,NULL);
  }
  NdbRecAttr* getValue(const NdbDictColumn* col) {
    return self->getValue(col,NULL);
  }


  const char * getColumnCharsetName(const char* columnName) {
    const NdbDictColumn * theColumn = self->getTable()->getColumn(columnName);
    const CHARSET_INFO * csinfo = theColumn->getCharset();
    if (csinfo == NULL) {
      return "latin1";
    }
    return csinfo->csname;
  }

  %ndbexception("NdbApiException") {
    $action
      if (result==-1) {
        NdbError err = arg1->getNdbError();
        NDB_exception_err(NdbApiException,err);
      }
  }

  voidint equalNull(const NdbDictColumn * theColumn) {
    return self->equal(theColumn->getName(), (char*)0);
  }
  voidint equalNull(const char* anAttrName) {
    return self->equal(anAttrName, (char*)0);
  }
  voidint equalNull(Uint32 anAttrId) {
    return self->equal(anAttrId, (char*)0);
  }

  voidint equalInt(const NdbDictColumn * theColumn, Int32 theValue) {
    return self->equal(theColumn->getName(), theValue);
  }
  voidint equalUint(const NdbDictColumn * theColumn, Uint32 theValue) {
    return self->equal(theColumn->getName(), theValue);
  }
  voidint equalLong(const NdbDictColumn * theColumn, Int64 theValue) {
    return self->equal(theColumn->getName(), theValue);
  }
  voidint equalUlong(const NdbDictColumn * theColumn, Uint64 theValue) {
    return self->equal(theColumn->getName(), theValue);
  }
  voidint equalShort(const NdbDictColumn * theColumn, short theValue) {
    return self->equal(theColumn->getName(), theValue);
  }
  voidint equalShort(const char* anAttrName, short theValue) {
    return self->equal(anAttrName, (Int32)theValue);
  }
  voidint equalShort(Uint32 anAttrId, short theValue) {
    return self->equal(anAttrId, (Int32)theValue);
  }

  voidint setBytes(const char* anAttrName, const char* BYTE, size_t len) {
    const NdbDictColumn * theColumn = self->getTable()->getColumn(anAttrName);
    char * stringVal = ndbFormatString(theColumn,BYTE,len);
    if (stringVal == NULL)
      return -1;
    int retval = self->setValue(anAttrName,stringVal);
    free(stringVal);
    return retval;
  }
  voidint setBytes(Uint32 anAttrId, const char* BYTE, size_t len) {
    const NdbDictColumn * theColumn = self->getTable()->getColumn(anAttrId);
    char * stringVal = ndbFormatString(theColumn,BYTE,len);
    if (stringVal == NULL)
      return -1;
    int retval = self->setValue(anAttrId,stringVal);
    free(stringVal);
    return retval;
  }

  voidint setString(const char* anAttrName,
                    const char* anInputString, size_t len) {
    const NdbDictColumn * theColumn = self->getTable()->getColumn(anAttrName);
    char * stringVal = ndbFormatString(theColumn,anInputString,len);
    if (stringVal == NULL)
      return -1;
    int retval = self->setValue(anAttrName,stringVal);
    free(stringVal);
    return retval;
  }
  voidint setString(Uint32 anAttrId,
                    const char* anInputString, size_t len) {
    const NdbDictColumn * theColumn = self->getTable()->getColumn(anAttrId);
    char * stringVal = ndbFormatString(theColumn,anInputString,len);
    if (stringVal == NULL)
      return -1;
    int retval = self->setValue(anAttrId,stringVal);
    free(stringVal);
    return retval;
  }

  voidint equalString(const char* anAttrName,
                      const char* anInputString, size_t len) {
    const NdbDictColumn * theColumn = self->getTable()->getColumn(anAttrName);
    char * stringVal = ndbFormatString(theColumn,anInputString,len);
    if (stringVal == NULL)
      return -1;
    int retval = self->equal(anAttrName,stringVal);
    free(stringVal);
    return retval;
  }
  voidint equalString(Uint32 anAttrId,
                      const char* anInputString, size_t len) {
    const NdbDictColumn * theColumn = self->getTable()->getColumn(anAttrId);
    char * stringVal = ndbFormatString(theColumn,anInputString,len);
    if (stringVal == NULL)
      return -1;
    int retval = self->equal(anAttrId,stringVal);
    free(stringVal);
    return retval;
  }

  voidint setShort(const char* anAttrName, short intVal) {
    return self->setValue(anAttrName,(Int32)intVal);
  }
  voidint setShort(Uint32 anAttrId, short intVal) {
    return self->setValue(anAttrId,(Int32)intVal);
  }

  voidint setInt(const char* anAttrName, Int32 intVal) {
    return self->setValue(anAttrName,intVal);
  }
  voidint setInt(Uint32 anAttrId, Int32 intVal) {
    return self->setValue(anAttrId,intVal);
  }

  voidint setUint(const char* anAttrName, Uint32 intVal) {
    return self->setValue(anAttrName,intVal);
  }
  voidint setUint(Uint32 anAttrId, Uint32 intVal) {
    return self->setValue(anAttrId,intVal);
  }

  voidint setLong(const char* anAttrName, Int64 intVal) {
    return self->setValue(anAttrName,intVal);
  }
  voidint setLong(Uint32 anAttrId, Int64 intVal) {
    return self->setValue(anAttrId,intVal);
  }

  voidint setUlong(const char* anAttrName, Uint64 intVal) {
    return self->setValue(anAttrName,intVal);
  }
  voidint setUlong(Uint32 anAttrId, Uint64 intVal) {
    return self->setValue(anAttrId,intVal);
  }

  voidint setDouble(const char* anAttrName, double intVal) {
    return self->setValue(anAttrName,intVal);
  }
  voidint setDouble(Uint32 anAttrId, double intVal) {
    return self->setValue(anAttrId,intVal);
  }

  voidint setFloat(const char* anAttrName, float intVal) {
    return self->setValue(anAttrName,intVal);
  }
  voidint setFloat(Uint32 anAttrId, float intVal) {
    return self->setValue(anAttrId,intVal);
  }

  voidint setDecimal(Uint32 anAttrId, decimal_t * dec) {

    const NdbDictColumn * theColumn =  self->getTable()->getColumn(anAttrId);
    int theScale = theColumn->getScale();
    int thePrecision = theColumn->getPrecision();


    char * theValue = (char *) malloc(decimal_bin_size(thePrecision,
                                                       theScale));
    decimal2bin(dec, theValue, thePrecision, theScale);
    int ret = self->setValue(anAttrId,theValue);
    free(theValue);
    return ret;
  }
  voidint setDecimal(const char* anAttrName, decimal_t * dec) {

    const NdbDictColumn * theColumn = self->getTable()->getColumn(anAttrName);

    int theScale = theColumn->getScale();
    int thePrecision = theColumn->getPrecision();

    char * theValue = (char *) malloc(decimal_bin_size(thePrecision,
                                                       theScale));
    decimal2bin(dec, theValue, thePrecision, theScale);
    int ret = self->setValue(anAttrName,theValue);
    free(theValue);
    return ret;
  }

  voidint setDatetime(const char* anAttrName, NdbDateTime * anInputDateTime) {

    const NdbDictColumn * theColumn = self->getTable()->getColumn(anAttrName);

    Uint64 dtval = ndbFormatDateTime(theColumn,anInputDateTime);
    if (dtval == 1) {
      return dtval;
    }
    return self->setValue(anAttrName,dtval);

  }

  voidint setDatetime(Uint32 anAttrId, NdbDateTime * anInputDateTime) {

    const NdbDictColumn * theColumn = self->getTable()->getColumn(anAttrId);

    Uint64 dtval = ndbFormatDateTime(theColumn,anInputDateTime);
    if (dtval == 1) {
      return dtval;
    }
    return  self->setValue(anAttrId,dtval);
  }

  voidint setDate(const char* anAttrName, NdbDate * anInputDateTime) {

    const NdbDictColumn * theColumn = self->getTable()->getColumn(anAttrName);

    Uint64 dtval = ndbFormatDateTime(theColumn,anInputDateTime);
    if (dtval == 1) {
      return dtval;
    }
    return self->setValue(anAttrName,dtval);

  }

  voidint setDate(Uint32 anAttrId, NdbDate * anInputDateTime) {

    const NdbDictColumn * theColumn = self->getTable()->getColumn(anAttrId);

    Uint64 dtval = ndbFormatDateTime(theColumn,anInputDateTime);
    if (dtval == 1) {
      return dtval;
    }
    return  self->setValue(anAttrId,dtval);
  }

  voidint setTime(const char* anAttrName, NdbTime * anInputDateTime) {

    const NdbDictColumn * theColumn = self->getTable()->getColumn(anAttrName);

    Uint64 dtval = ndbFormatDateTime(theColumn,anInputDateTime);
    if (dtval == 1) {
      return dtval;
    }
    return self->setValue(anAttrName,dtval);

  }

  voidint setTime(Uint32 anAttrId, NdbTime * anInputDateTime) {

    const NdbDictColumn * theColumn = self->getTable()->getColumn(anAttrId);

    Uint64 dtval = ndbFormatDateTime(theColumn,anInputDateTime);
    if (dtval == 1) {
      return dtval;
    }
    return  self->setValue(anAttrId,dtval);
  }

  voidint setTimestamp(const char* anAttrName, NdbTimestamp anInputTimestamp) {
    return self->setValue(anAttrName,anInputTimestamp);
  }
  voidint setTimestamp(Uint32 anAttrId, NdbTimestamp anInputTimestamp) {
    return  self->setValue(anAttrId,anInputTimestamp);
  }


  voidint setNull(const char * anAttrName) {
    return self->setValue(anAttrName,(char *)0);
  }
  voidint setNull(Uint32 anAttrId) {
    return self->setValue(anAttrId,(char *)0);
  }


  voidint equalBytes(const NdbDictColumn * theColumn,
                     const char* BYTE, size_t len) {
    char * stringVal = ndbFormatString(theColumn,BYTE,len);
    if (stringVal == NULL)
      return -1;
    int retval = self->equal(theColumn->getName(),stringVal);
    free(stringVal);
    return retval;
  }
  voidint equalBytes(const char* anAttrName,
                     const char* BYTE, size_t len) {
    const NdbDictColumn * theColumn = self->getTable()->getColumn(anAttrName);
    char * stringVal = ndbFormatString(theColumn,BYTE,len);
    if (stringVal == NULL)
      return -1;
    int retval = self->equal(anAttrName,stringVal);
    free(stringVal);
    return retval;
  }
  voidint equalBytes(Uint32 anAttrId,
                     const char* BYTE, size_t len) {
    const NdbDictColumn * theColumn = self->getTable()->getColumn(anAttrId);
    char * stringVal = ndbFormatString(theColumn,BYTE,len);
    if (stringVal == NULL)
      return -1;
    int retval = self->equal(anAttrId,stringVal);
    free(stringVal);
    return retval;
  }

  voidint equalDatetime(const NdbDictColumn * theColumn,
                        NdbDateTime * anInputDateTime) {

    Uint64 dtval = ndbFormatDateTime(theColumn,anInputDateTime);
    if (dtval == 1)
      return dtval;
    return self->equal(theColumn->getName(),dtval);
  }
  voidint equalDatetime(const char* anAttrName,
                        NdbDateTime * anInputDateTime) {

    const NdbDictColumn * theColumn = self->getTable()->getColumn(anAttrName);

    Uint64 dtval = ndbFormatDateTime(theColumn,anInputDateTime);
    if (dtval == 1)
      return dtval;
    return self->equal(anAttrName,dtval);
  }
  voidint equalDatetime(Uint32 anAttrId, NdbDateTime * anInputDateTime) {

    const NdbDictColumn * theColumn = self->getTable()->getColumn(anAttrId);

    Uint64 dtval = ndbFormatDateTime(theColumn,anInputDateTime);
    if (dtval == 1)
      return dtval;
    return  self->equal(anAttrId,dtval);
  }

  voidint equalTimestamp(const NdbDictColumn * theColumn,
                         NdbTimestamp anInputTimestamp) {
    return self->equal(theColumn->getName(),anInputTimestamp);
  }
  voidint equalTimestamp(const char* anAttrName,
                         NdbTimestamp anInputTimestamp) {
    return self->equal(anAttrName,anInputTimestamp);
  }
  voidint equalTimestamp(Uint32 anAttrId,
                         NdbTimestamp anInputTimestamp) {
    return  self->equal(anAttrId,anInputTimestamp);
  }

  voidint equalDecimal(const NdbDictColumn * theColumn, decimal_t * decVal) {

    const int prec = theColumn->getPrecision();
    const int scale = theColumn->getScale();

    char * theValue = (char *) malloc(decimal_bin_size(prec, scale));
    decimal2bin(decVal, theValue, prec, scale);
    int ret = self->equal(theColumn->getName(),theValue);
    free(theValue);
    return ret;
  }
  voidint equalDecimal(Uint32 anAttrId, decimal_t * decVal) {
    const NdbDictColumn * theColumn = self->getTable()->getColumn(anAttrId);

    const int prec = theColumn->getPrecision();
    const int scale = theColumn->getScale();

    char * theValue = (char *) malloc(decimal_bin_size(prec, scale));
    decimal2bin(decVal, theValue, prec, scale);
    int ret = self->equal(anAttrId,theValue);
    free(theValue);
    return ret;
  }
  voidint equalDecimal(const char * anAttrName, decimal_t * decVal) {
    const NdbDictColumn * theColumn = self->getTable()->getColumn(anAttrName);

    const int prec = theColumn->getPrecision();
    const int scale = theColumn->getScale();

    char * theValue = (char *) malloc(decimal_bin_size(prec, scale));
    decimal2bin(decVal, theValue, prec, scale);
    int ret = self->equal(anAttrName,theValue);
    free(theValue);
    return ret;
  }

  voidint getColumnId(const char* columnName)
  {
    return getColumnId(self,columnName);
  }



  %ndbnoexception;


};

