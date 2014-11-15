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

class NdbScanOperation : public NdbOperation {

protected:
  NdbScanOperation(Ndb* aNdb,
                   NdbOperation::Type aType = NdbOperation::TableScan);
  virtual ~NdbScanOperation();

public:

  enum ScanFlag {
    SF_TupScan = (1 << 16),     // scan TUP order
    SF_DiskScan = (2 << 16),    // scan in DISK order
    SF_OrderBy = (1 << 24),     // index scan in order
    SF_Descending = (2 << 24),  // index scan in descending order
    SF_ReadRangeNo = (4 << 24), // enable @ref get_range_no
    SF_MultiRange = (8 << 24),  // scan is part of multi-range scan
    SF_KeyInfo = 1              // request KeyInfo to be sent back
  };

  %ndbexception("NdbApiException") {
    $action
      if (result==-1) {
        NdbError err = arg1->getNdbError();
        NDB_exception_err(NdbApiException,err);
      }
  }

  virtual voidint readTuples(LockMode lock_mode=LM_Read, Uint32 scan_flags=0,
                             Uint32 parallel=0, Uint32 batch=0);

  inline voidint readTuples(int parallell){
    return readTuples(LM_Read, 0, parallell);
  }

  inline voidint readTuplesExclusive(int parallell = 0){
    return readTuples(LM_Exclusive, 0, parallell);
  }

  int nextResult(bool fetchAllowed = true, bool forceSend = false);
  voidint deleteCurrentTuple();
  voidint deleteCurrentTuple(NdbTransaction* takeOverTransaction);

  %ndbexception("NdbApiException") {
    $action
      if (result==NULL) {
        NdbError err = arg1->getNdbError();
        NDB_exception_err(NdbApiException,err);
      }
  }

  NdbBlob* getBlobHandle(const char* anAttrName);
  NdbBlob* getBlobHandle(Uint32 anAttrId);
  NdbOperation* lockCurrentTuple();
  NdbOperation* lockCurrentTuple(NdbTransaction* lockTrans);
  NdbOperation* updateCurrentTuple();
  NdbOperation* updateCurrentTuple(NdbTransaction* updateTrans);

  %ndbnoexception;

  const NdbError & getNdbError() const;

  void close(bool forceSend = false, bool releaseOp = false);
};

%extend NdbScanOperation {

  %ndbexception("NdbApiException") {
    $action
      if (result==NULL) {
        NdbError err = arg1->getNdbError();
        NDB_exception_err(NdbApiException,err);
      }
  }

  NdbScanFilter * getNdbScanFilter() {
    return new NdbScanFilter(self);
  }

  %ndbexception("NdbApiException") {
    $action
      if (result==-1) {
        NdbError err = arg1->getNdbError();
        NDB_exception_err(NdbApiException,err);
      }
  }

  voidint increment(NdbDictColumn * theColumn, Uint32 aValue)
  {
    return interpretedIncrement(self, theColumn->getAttrId(), (Uint64)aValue);
  }
  voidint increment(NdbDictColumn * theColumn, Uint64 aValue)
  {
    return interpretedIncrement(self, theColumn->getAttrId(), aValue);
  }
  voidint increment(const char* aColumnName, Uint32 aValue)
  {
    return interpretedIncrement(self, getColumnId(self, aColumnName),
                                (Uint64)aValue);
  }
  voidint increment(const char* aColumnName, Uint64 aValue)
  {
    return interpretedIncrement(self, getColumnId(self, aColumnName), aValue);
  }

  voidint decrement(NdbDictColumn * theColumn, Uint32 aValue)
  {
    return interpretedDecrement(self, theColumn->getAttrId(), (Uint64)aValue);
  }
  voidint decrement(NdbDictColumn * theColumn, Uint64 aValue)
  {
    return interpretedDecrement(self, theColumn->getAttrId(), aValue);
  }
  voidint decrement(const char* aColumnName, Uint32 aValue)
  {
    return interpretedDecrement(self, getColumnId(self, aColumnName),
                                (Uint64)aValue);
  }
  voidint decrement(const char* aColumnName, Uint64 aValue)
  {
    return interpretedDecrement(self, getColumnId(self, aColumnName),
                                aValue);
  }

  %ndbnoexception

};
