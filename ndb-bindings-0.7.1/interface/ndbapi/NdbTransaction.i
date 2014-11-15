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

%delobject NdbTransaction::close;

class NdbTransaction {
  ~NdbTransaction();
  NdbTransaction(Ndb* aNdb);

public:

  enum ExecType {
    NoCommit=1,        ///< Execute the transaction as far as it has
                                ///< been defined, but do not yet commit it
    Commit=2,            ///< Execute and try to commit the transaction
    Rollback=3          ///< Rollback transaction
  };


  /**
   * The commit status of the transaction.
   */
  enum CommitStatusType {
    NotStarted,                   ///< Transaction not yet started
    Started,                      ///< <i>Missing explanation</i>
    Committed,                    ///< Transaction has been committed
    Aborted,                      ///< Transaction has been aborted
    NeedAbort                     ///< <i>Missing explanation</i>
  };


  const NdbError & getNdbError() const;

  %ndbexception("NdbApiException") {
    $action
      if (result==NULL) {
        NdbError err = arg1->getNdbError();
        NDB_exception_err(NdbApiException,err);
      }
  }
  Ndb* getNdb();

  NdbOperation* getNdbOperation(const class NdbDictTable* aTable);
  NdbOperation* getNdbOperation(const char* aTableName);

  /* These first two are deprecated */
  NdbIndexScanOperation* getNdbIndexScanOperation(const char* anIndexName,
                                                  const char* aTableName);
  NdbIndexScanOperation* getNdbIndexScanOperation(const NdbDictIndex *anIndex,
                                                  const NdbDictTable *aTable);
  NdbIndexScanOperation* getNdbIndexScanOperation(const NdbDictIndex *anIndex);

  NdbIndexOperation* getNdbIndexOperation(const char*  anIndexName,
                                          const char*  aTableName);
  NdbIndexOperation* getNdbIndexOperation(const NdbDictIndex *anIndex,
                                          const NdbDictTable *aTable);
  NdbIndexOperation* getNdbIndexOperation(const NdbDictIndex *anIndex);


  NdbScanOperation* getNdbScanOperation(const class NdbDictTable* aTable);
  NdbScanOperation* getNdbScanOperation(const char* aTableName);
  NdbOperation* getNdbErrorOperation();


  %ndbexception("NdbApiException,NdbApiTemporaryException,"
                "NdbApiPermanentException,"
                "SchemaError") {
    $action
      if (result < 0) {
        NdbError err = arg1->getNdbError();
        if (err.classification == NdbError::SchemaError) {
          NDB_exception_err(SchemaError,err);
        } else {
          switch (err.status) {
          case NdbError::TemporaryError:
            NDB_exception_err(NdbApiTemporaryException,err);
            break;
          case NdbError::PermanentError:
            // TODO: We should probably at least handle all the various
            // error classifications. mmm, thats going to suck
            // and then we should figure out how to do that all over the place
            NDB_exception_err(NdbApiPermanentException,err);
            break;
          default:
            NDB_exception_err(NdbApiException,err);
            break;
          }
        }
      }
  }
  int execute(NdbTransaction::ExecType execType,
              NdbOperation::AbortOption abortOption
              = NdbOperation::DefaultAbortOption,
              bool force = 0 );
  voidint refresh();
  int getNdbErrorLine();

  %ndbnoexception;

  Uint64 getTransactionId();
  CommitStatusType commitStatus();

};


%extend NdbTransaction {
public:

  %ndbnoexception;

  ~NdbTransaction() {
    if(self!=0)
      self->close();
  }

  void close() {
    if(self!=0)
      self->close();
  }

  bool isClosed() {
    return (self==0);
  }

  void executeAsynchPrepare(NdbTransaction::ExecType execType,
                            asynch_callback_t * cb,
                            NdbOperation::AbortOption abortOption
                            = NdbOperation::DefaultAbortOption) {
    cb->create_time=getMicroTime();
    self->executeAsynchPrepare(execType,theCallBack,(void *)cb, abortOption);
  }

  void executeAsynch(NdbTransaction::ExecType execType,
                     asynch_callback_t * cb,
                     NdbOperation::AbortOption abortOption
                     = NdbOperation::DefaultAbortOption,
                     bool forceSend=false) {
    cb->create_time=getMicroTime();
    self->executeAsynch(execType,theCallBack,(void *)cb,
                        abortOption, forceSend);
  }

  %ndbexception("NdbApiException,NdbApiTemporaryException,"
                "NdbApiPermanentException,") {
    $action
      if (result < 0) {
        NdbError err = arg1->getNdbError();
        switch (err.status) {
        case NdbError::TemporaryError:
          NDB_exception_err(NdbApiTemporaryException,err);
          break;
        case NdbError::PermanentError:
          NDB_exception_err(NdbApiPermanentException,err);
          break;
        default:
          NDB_exception_err(NdbApiException,err);
          break;
        }
      }
  }

  int executeNoCommit(NdbOperation::AbortOption abortOption =
                      NdbOperation::DefaultAbortOption) {
    return self->execute(NdbTransaction::NoCommit, abortOption,false);
  }
  int executeCommit(NdbOperation::AbortOption abortOption =
                    NdbOperation::DefaultAbortOption) {
    return self->execute(NdbTransaction::Commit, abortOption);
  }
  int executeRollback(NdbOperation::AbortOption abortOption =
                      NdbOperation::DefaultAbortOption) {
    return self->execute(NdbTransaction::Rollback, abortOption,false);
  }
  %ndbexception("NdbApiException") {
    $action
      if (result == NULL) {
        NdbError err = arg1->getNdbError();
        NDB_exception_err(NdbApiException,err);
      }
  }



  %ndbexception("NdbApiException") {
    $action
      if (result == NULL) {
        NdbError err = arg1->getNdbError();
        NDB_exception_err(NdbApiException,err);
      }
  }


  NdbOperation* getSelectOperation(const class NdbDictTable* aTable,
                                   NdbOperation::LockMode lockMode=NdbOperation::LM_Read)
  {
    NdbOperation * theOp = self->getNdbOperation(aTable);
    if (theOp != NULL)
    {
      theOp->readTuple(lockMode);
    }
    return theOp;
  }

  NdbOperation* getSelectOperation(const char* aTableName,
                                   NdbOperation::LockMode lockMode=NdbOperation::LM_Read)
  {
    NdbOperation * theOp = self->getNdbOperation(aTableName);
    if (theOp != NULL)
    {
      theOp->readTuple(lockMode);
    }
    return theOp;

  }

  NdbOperation* getUpdateOperation(const char* aTableName)
  {
    NdbOperation * theOp = self->getNdbOperation(aTableName);
    if (theOp != NULL)
    {
      theOp->updateTuple();
    }
    return theOp;
  }

  NdbOperation* getUpdateOperation(const class NdbDictTable* aTable)
  {
    NdbOperation * theOp = self->getNdbOperation(aTable);
    if (theOp != NULL)
    {
      theOp->updateTuple();
    }
    return theOp;
  }

  NdbOperation* getInsertOperation(const char* aTableName)
  {
    NdbOperation * theOp = self->getNdbOperation(aTableName);
    if (theOp != NULL)
    {
      theOp->insertTuple();
    }
    return theOp;
  }

  NdbOperation* getInsertOperation(const class NdbDictTable* aTable)
  {
    NdbOperation * theOp = self->getNdbOperation(aTable);
    if (theOp != NULL)
    {
      theOp->insertTuple();
    }
    return theOp;
  }

  NdbOperation* getWriteOperation(const char* aTableName)
  {
    NdbOperation * theOp = self->getNdbOperation(aTableName);
    if (theOp != NULL)
    {
      theOp->writeTuple();
    }
    return theOp;
  }

  NdbOperation* getWriteOperation(const class NdbDictTable* aTable)
  {
    NdbOperation * theOp = self->getNdbOperation(aTable);
    if (theOp != NULL)
    {
      theOp->writeTuple();
    }
    return theOp;
  }

  NdbOperation* getDeleteOperation(const char* aTableName)
  {
    NdbOperation * theOp = self->getNdbOperation(aTableName);
    if (theOp != NULL)
    {
      theOp->deleteTuple();
    }
    return theOp;
  }

  NdbOperation* getDeleteOperation(const class NdbDictTable* aTable)
  {
    NdbOperation * theOp = self->getNdbOperation(aTable);
    if (theOp != NULL)
    {
      theOp->deleteTuple();
    }
    return theOp;
  }

  NdbIndexOperation* getDeleteUniqueOperation(const class NdbDictIndex* anIndex,
                                              const class NdbDictTable* aTable)
  {
    NdbIndexOperation * theOp = self->getNdbIndexOperation(anIndex, aTable);
    if (theOp != NULL)
    {
      theOp->deleteTuple();
    }
    return theOp;
  }

  NdbIndexOperation* getDeleteUniqueOperation(const char* anIndexName,
                                              const char* aTableName)
  {
    NdbIndexOperation * theOp = self->getNdbIndexOperation(anIndexName, aTableName);
    if (theOp != NULL)
    {
      theOp->deleteTuple();
    }
    return theOp;
  }

  NdbIndexOperation* getDeleteUniqueOperation(const class NdbDictIndex* anIndex)
  {
    NdbIndexOperation * theOp = self->getNdbIndexOperation(anIndex);
    if (theOp != NULL)
    {
      theOp->deleteTuple();
    }
    return theOp;
  }
  
  NdbIndexOperation* getSelectUniqueOperation(const class NdbDictIndex* anIndex,
                                              const class NdbDictTable* aTable,
                                              NdbOperation::LockMode lockMode=NdbOperation::LM_Read)
  {
    NdbIndexOperation * theOp = self->getNdbIndexOperation(anIndex, aTable);
    if (theOp != NULL)
    {
      theOp->readTuple(lockMode);
    }
    return theOp;  
  }

  NdbIndexOperation* getSelectUniqueOperation(const char* anIndexName,
                                              const char* aTableName,
                                              NdbOperation::LockMode lockMode=NdbOperation::LM_Read)
  {
    NdbIndexOperation * theOp = self->getNdbIndexOperation(anIndexName, aTableName);
    if (theOp != NULL)
    {
      theOp->readTuple(lockMode);
    }
    return theOp;  
  }

  NdbIndexOperation* getUpdateUniqueOperation(const class NdbDictIndex* anIndex,
                                              const class NdbDictTable* aTable)
  {
    NdbIndexOperation * theOp = self->getNdbIndexOperation(anIndex, aTable);
    if (theOp != NULL)
    {
      theOp->updateTuple();
    }
    return theOp;
  }

  NdbIndexOperation* getUpdateUniqueOperation(const char* anIndexName,
                                              const char* aTableName)
  {
    NdbIndexOperation * theOp = self->getNdbIndexOperation(anIndexName, aTableName);
    if (theOp != NULL)
    {
      theOp->updateTuple();
    }
    return theOp;  
  }

  NdbScanOperation* getSelectScanOperation(const char* aTableName,
                                           NdbOperation::LockMode lockMode=NdbOperation::LM_Read,
                                           NdbScanOperation::ScanFlag scanFlag=(NdbScanOperation::ScanFlag)0,
                                           int parallel=0,
                                           int batch=0)
  {
    NdbScanOperation * theOp = self->getNdbScanOperation(aTableName);
    if (theOp != NULL)
    {
      theOp->readTuples(lockMode, scanFlag, (Uint32)parallel, (Uint32)batch);
    }
    return theOp;
  }

  NdbScanOperation* getSelectScanOperation(const class NdbDictTable* aTable,
                                           NdbOperation::LockMode lockMode=NdbOperation::LM_Read,
                                           NdbScanOperation::ScanFlag scanFlag=(NdbScanOperation::ScanFlag)0,
                                           int parallel=0,
                                           int batch=0)
  {
    NdbScanOperation * theOp = self->getNdbScanOperation(aTable);
    if (theOp != NULL)
    {
      theOp->readTuples(lockMode, scanFlag, (Uint32)parallel, (Uint32)batch);
    }
    return theOp;
  }

  NdbScanOperation* getSelectScanOperation(const class NdbDictTable* aTable,
                                           NdbOperation::LockMode lockMode,
                                           int scanFlag,
                                           int parallel,
                                           int batch)
  {
    NdbScanOperation * theOp = self->getNdbScanOperation(aTable);
    if (theOp != NULL)
    {
      theOp->readTuples(lockMode, (Uint32)scanFlag, (Uint32)parallel, (Uint32)batch);
    }
    return theOp;
  }

  NdbScanOperation* getSelectScanOperation(const char* aTableName,
                                           NdbOperation::LockMode lockMode,
                                           int scanFlag,
                                           int parallel,
                                           int batch)
  {
    NdbScanOperation * theOp = self->getNdbScanOperation(aTableName);
    if (theOp != NULL)
    {
      theOp->readTuples(lockMode, (Uint32)scanFlag, (Uint32)parallel, (Uint32)batch);
    }
    return theOp;
  }
  
  NdbIndexScanOperation* getSelectIndexScanOperation(const char* anIndexName,
                                                     const char* aTableName,
                                                     NdbOperation::LockMode lockMode=NdbOperation::LM_Read,
                                                     NdbScanOperation::ScanFlag scanFlag=(NdbScanOperation::ScanFlag)0,
                                                     int parallel=0,
                                                     int batch=0)
  {
    NdbIndexScanOperation * theOp = self->getNdbIndexScanOperation(anIndexName, aTableName);
    
    if (theOp != NULL)
    {
      theOp->readTuples(lockMode, scanFlag, (Uint32)parallel, (Uint32)batch);
    }
    return theOp;
  }

  NdbIndexScanOperation* getSelectIndexScanOperation(NdbDictIndex* anIndex,
                                                     NdbDictTable* aTable,
                                                     NdbOperation::LockMode lockMode=NdbOperation::LM_Read,
                                                     NdbScanOperation::ScanFlag scanFlag=(NdbScanOperation::ScanFlag)0,
                                                     int parallel=0,
                                                     int batch=0)
  {
    NdbIndexScanOperation * theOp = self->getNdbIndexScanOperation(anIndex, aTable);
    
    if (theOp != NULL)
    {
      theOp->readTuples(lockMode, scanFlag, (Uint32)parallel, (Uint32)batch);
    }
    return theOp;
  }
  
  NdbIndexScanOperation* getSelectIndexScanOperation(NdbDictIndex* anIndex,
                                                     NdbOperation::LockMode lockMode=NdbOperation::LM_Read,
                                                     NdbScanOperation::ScanFlag scanFlag=(NdbScanOperation::ScanFlag)0,
                                                     int parallel=0,
                                                     int batch=0)
  {
    NdbIndexScanOperation * theOp = self->getNdbIndexScanOperation(anIndex);
    
    if (theOp != NULL)
    {
      theOp->readTuples(lockMode, scanFlag, (Uint32)parallel, (Uint32)batch);
    }
    return theOp;
  }
  
  NdbIndexScanOperation* getSelectIndexScanOperation(const char* anIndexName,
                                                     const char* aTableName,
                                                     NdbOperation::LockMode lockMode,
                                                     int scanFlag,
                                                     int parallel,
                                                     int batch)
  {
    NdbIndexScanOperation * theOp = self->getNdbIndexScanOperation(anIndexName, aTableName);
    
    if (theOp != NULL)
    {
      theOp->readTuples(lockMode, (Uint32)scanFlag, (Uint32)parallel, (Uint32)batch);
    }
    return theOp;
  }
  
  NdbIndexScanOperation* getSelectIndexScanOperation(NdbDictIndex* anIndex,
                                                     NdbDictTable* aTable,
                                                     NdbOperation::LockMode lockMode,
                                                     int scanFlag,
                                                     int parallel,
                                                     int batch)
  {
    NdbIndexScanOperation * theOp = self->getNdbIndexScanOperation(anIndex, aTable);
    
    if (theOp != NULL)
    {
      theOp->readTuples(lockMode, (Uint32)scanFlag, (Uint32)parallel, (Uint32)batch);
    }
    return theOp;
  }

  NdbIndexScanOperation* getSelectIndexScanOperation(NdbDictIndex* anIndex,
                                                     NdbOperation::LockMode lockMode,
                                                     int scanFlag,
                                                     int parallel,
                                                     int batch)
  {
    NdbIndexScanOperation * theOp = self->getNdbIndexScanOperation(anIndex);
    
    if (theOp != NULL)
    {
      theOp->readTuples(lockMode, (Uint32)scanFlag, (Uint32)parallel, (Uint32)batch);
    }
    return theOp;
  }

  NdbOperation * getAtomicUpdateOperation(const class NdbDictTable* aTable)
  {
    NdbOperation * theOp = self->getNdbOperation(aTable);
    if (theOp != NULL)
    {
      theOp->interpretedUpdateTuple();
    }
    return theOp;
  }

  NdbOperation* getAtomicUpdateOperation(const char* aTableName)
  {
    NdbOperation * theOp = self->getNdbOperation(aTableName);
    if (theOp != NULL)
    {
      theOp->interpretedUpdateTuple();
    }
    return theOp;
  }

  %ndbexception("NdbApiException") {
    $action
      if (result==(Uint64)-1) {
        NdbError err = arg1->getNdbError();
        NDB_exception_err(NdbApiException,err);
      }
  }
  Uint64 getGCI() {
    Uint64 id = 0;
    int ret = self->getGCI(&id);
    if (ret == -1) {
      return (Uint64)-1;
    }
    return (Uint64)id;
  }

  %ndbnoexception;

  const NdbOperation * getNextCompletedOperation(const NdbOperation * op=NULL) {
    return self->getNextCompletedOperation(op);
  }

};
