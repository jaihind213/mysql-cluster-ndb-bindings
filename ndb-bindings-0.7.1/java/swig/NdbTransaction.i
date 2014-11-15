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

%typemap(javaimports) NdbTransaction %{
import java.util.Map;
import java.util.HashMap;
%}

%typemap(javainterfaces) NdbTransaction "NdbTransaction";
%typemap(javabase) NdbTransaction "NdbJtaTransaction";

%rename(realClose) NdbTransaction::close;

%typemap(jstype) NdbOperation * getAtomicUpdateOperation "NdbAtomicOperation";
%typemap(javaout) NdbOperation * getAtomicUpdateOperation {
  long cPtr = $jnicall;
  if (cPtr == 0) {
    return null;
  }

  $javaclassname theOperation = new $javaclassname(cPtr, $owner);
  createdOperations.put(new Long(cPtr),
                        theOperation);
  return new NdbAtomicOperationImpl(theOperation);

 }
%typemap(javaout) NdbOperation * getNextCompletedOperation {
 long cPtr = $jnicall;
 return (cPtr == 0) ? null : new $javaclassname(cPtr, $owner);
}
%rename(realGetNextCompletedOperation) NdbTransaction::getNextCompletedOperation;

%typemap(javacode) NdbTransaction %{
  private final Map<Long, NdbOperationImpl> createdOperations = new HashMap<Long, NdbOperationImpl>();
  public void close() {
    try { 
      this.realClose();
    } catch (Exception e) { 
    }
    this.swigCPtr=0;
  }
  public NdbOperation getNextCompletedOperation(NdbOperation op) { 
    return realGetNextCompletedOperation((NdbOperationImpl)op);
  }
  public NdbOperation getNextCompletedOperation() { 
    return realGetNextCompletedOperation();
  }
  %}



%javamethodmodifiers NdbTransaction::getNdbOperation "protected";

%javamethodmodifiers NdbTransaction::getNdbIndexOperation "protected";

%javamethodmodifiers NdbTransaction::getNdbScanOperation "protected";

%javamethodmodifiers NdbTransaction::getNdbIndexScanOperation "protected";

%javamethodmodifiers NdbTransaction::execute "@Override
  public";

%typemap(javain) NdbOperation * "NdbOperationImpl.getCPtr((NdbOperationImpl)$javainput)";

%typemap(javaout) NdbOperation *, NdbScanOperation *, 
                  NdbIndexOperation *, NdbScanOperation * {
  long cPtr = $jnicall; 
  if (cPtr == 0) { 
    return null; 
  }
  
  $javaclassname theOperation = new $javaclassname(cPtr, $owner);
  createdOperations.put(new Long(cPtr),
                        theOperation);
  return theOperation;
}

%include "ndbapi/NdbTransaction.i"

/* NdbTransaction methods are the only place where we need to do
   save the Operatoins. Reset typemap. */

%typemap(javaout) NdbOperation *, NdbScanOperation *, 
                  NdbIndexOperation *, NdbScanOperation * { 
 long cPtr = $jnicall;
 return (cPtr == 0) ? null : new $javaclassname(cPtr, $owner);
}
  
