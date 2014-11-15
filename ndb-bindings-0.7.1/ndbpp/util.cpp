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

int getColumnId(NdbOperation * op, const char* columnName) {
  const NdbDictColumn * theColumn = op->getTable()->getColumn(columnName);
  return theColumn->getColumnNo();
}

char * ndbFormatString(const NdbDictColumn * theColumn,
                       const char* aString, size_t len) {

  // This method should be safe and do all the error checking.
  // There should be other methods in case you want to bypass checks.
  if (aString==0) {
    return 0;
  }
  if ((!theColumn) || (len>65535)) {
    return NULL;
  }

  switch(theColumn->getType()) {
  case NDB_TYPE_VARCHAR:
  case NDB_TYPE_VARBINARY:
    if (len>255) {
      return NULL;
    } else {
      // Need one space for the length
      char * buf = (char *)malloc((len+1));
      unsigned char lowb=len;
      buf[0]=lowb;
      memcpy(buf+1, aString, len);
      return buf;
    }
    break;
  case NDB_TYPE_LONGVARCHAR:
  case NDB_TYPE_LONGVARBINARY:
  {
    char* buff=(char *)malloc(len+2);

    short l = (short)len;
    /*
      We need to copy the length of our string into the first 2 bytes
      of the buffer.
      We take a bitwise AND of the 1st byte in the short 'l' and copy
      it int the 1st byte of our buffer.
    */

    buff[0]=(unsigned char) ((l & 0x00FF))>>0;
    /*
      We take a bitwise AND of the 2nd byte in the short 'l'
      and copy it into the 2nd byte of our buffer.
    */

    buff[1]= (unsigned char)((l & 0xFF00)>>8);
    memcpy(&buff[2],aString, l);
    return buff;
  }
  break;
  case NDB_TYPE_CHAR:
    if (len>255) {
      return NULL;
    } else {
      int colLength = theColumn->getLength();
      char * buf = (char *)malloc(colLength+1);
      memset(buf,32, colLength);
      memcpy(buf, aString, len);
      return buf;
    }
    break;
  case NDB_TYPE_BINARY:
    if (len>255) {
      return NULL;
    } else {
      int colLength = theColumn->getLength();
      char * buf = (char *)malloc(colLength+1);
      memset(buf, 0, colLength);
      memcpy(buf, aString, len);
      return buf;
    }
    break;
  default:
    return NULL;
  }
}

Uint64 ndbFormatDateTime(const NdbDictColumn * theColumn, NdbDateTime * tm) {

  // Returns 1 on failure. How much does that suck?

  char dt_buf[20];
  Uint64 val = 0;
  switch(theColumn->getType()) {
  case NDB_TYPE_DATETIME:
  case NDB_TYPE_TIMESTAMP:
  {
    snprintf(dt_buf, 20, "%04d%02d%02d%02d%02d%02d",
             tm->year, tm->month, tm->day, tm->hour, tm->minute, tm->second);
    val=strtoull(dt_buf, 0, 10);
  }
  break;
  case NDB_TYPE_TIME:
  {
    snprintf(dt_buf, 20, "%02d%02d%02d",
             tm->hour, tm->minute, tm->second);
    val=strtoull(dt_buf, 0, 10);
  }
  break;
  case NDB_TYPE_DATE:
  {
    printf("tm->month == %d\n",tm->month);
    val=(tm->year << 9) | ((tm->month) << 5) | tm->day;
    printf("val:%llu\n",val);
    int3store(dt_buf, val);
  }
  break;
  default:
    return 1;
  }
  return val;
}

char * decimal2bytes(decimal_t * val) {

  int theScale = val->frac;
  int thePrecision = (val->intg)+theScale;

  char * theValue = (char *) malloc(decimal_bin_size(thePrecision,
                                                     theScale));
  if (theValue == NULL) {
    return NULL;
  }
  decimal2bin(val, theValue, thePrecision, theScale);
  return theValue;
}

long long getMicroTime()
{
  struct timeval tTime;
  gettimeofday(&tTime, 0);
  long long microSeconds = (long long) tTime.tv_sec * 1000000 + tTime.tv_usec;
  return microSeconds;
}

Int64 selectCount(Ndb * theNdb, const char * tbl) {

  /**
   * This code is taken from the ndb_select_count.cpp utility program,
   * distributed with mysql-src.
   */
  Int64 OUTPUT = 0;
  const Uint32 codeWords= 1;
  Uint32 codeSpace[ codeWords ];
  NdbInterpretedCode code(NULL, // Table is irrelevant
                          &codeSpace[0],
                          codeWords);
  if ((code.interpret_exit_last_row() != 0) ||
      (code.finalise() != 0))
  {
    return -1;
  }


  NdbTransaction * countTrans = theNdb->startTransaction();
  if (!countTrans)
  {
    return -1;
  }

  NdbScanOperation *pOp = countTrans->getNdbScanOperation(tbl);
  if (!pOp) {
    countTrans->close();
    return -1;
  }


  if( pOp->readTuples(NdbScanOperation::LM_Dirty) ) {
    countTrans->close();
    return -1;
  }

  int check;
  check = pOp->setInterpretedCode(&code);
  if( check == -1 ) {
    countTrans->close();
    return -1;
  }

  Uint64 tmp;
  Uint32 row_size;
  pOp->getValue(NdbDictColumn::ROW_COUNT, (char*)&tmp);
  pOp->getValue(NdbDictColumn::ROW_SIZE, (char*)&row_size);
  check = countTrans->execute(NdbTransaction::NoCommit);
  if( check == -1 ) {
    countTrans->close();
    return -1;
  }

  int eof;
  while((eof = pOp->nextResult(true)) == 0){
    OUTPUT += tmp;
  }
  if (eof == -1) {
    countTrans->close();
    return -1;
  }

  if (countTrans)
  {
    countTrans->close();
  }

  // The return value 'ret' is the 'length' returned by select_count
  return OUTPUT;

};


Uint64 selectCountBig(Ndb * theNdb, const char * tbl, bool &selectCountError) {

  /**
   * This code is taken from the ndb_select_count.cpp utility program,
   * distributed with mysql-src.
   */
  Uint64 OUTPUT = 0;

  const Uint32 codeWords= 1;
  Uint32 codeSpace[ codeWords ];
  NdbInterpretedCode code(NULL, // Table is irrelevant
                          &codeSpace[0],
                          codeWords);
  if ((code.interpret_exit_last_row() != 0) ||
      (code.finalise() != 0))
  {
    return OUTPUT;
  }


  NdbTransaction * countTrans = theNdb->startTransaction();
  if (!countTrans)
  {
    return OUTPUT;
  }

  NdbScanOperation *pOp = countTrans->getNdbScanOperation(tbl);
  if (!pOp) {
    return OUTPUT;
  }


  if( pOp->readTuples(NdbScanOperation::LM_Dirty) ) {
    return OUTPUT;
  }

  int check;
  check = pOp->setInterpretedCode(&code);
  if( check == -1 ) {
    selectCountError = true;
  }

  Uint64 tmp;
  Uint32 row_size;
  pOp->getValue(NdbDictColumn::ROW_COUNT, (char*)&tmp);
  pOp->getValue(NdbDictColumn::ROW_SIZE, (char*)&row_size);
  check = countTrans->execute(NdbTransaction::NoCommit);
  if( check == -1 ) {
    countTrans->close();
    return OUTPUT;
  }

  int eof;
  while((eof = pOp->nextResult(true)) == 0){
    OUTPUT += tmp;
  }
  if (eof == -1) {
    countTrans->close();
    return OUTPUT;
  }
  selectCountError=false;

  if (countTrans)
  {
    countTrans->close();
  }

  // The return value 'ret' is the 'length' returned by select_count
  return OUTPUT;

};

int interpretedIncrement(NdbScanOperation * theOp,
                         Uint32 anAttrId, Uint64 aValue)
{

  /* Letting API allocate the space for this, since we don't
     have a place to hold on to any */
  NdbInterpretedCode code(theOp->getTable());

  if ((code.add_val(anAttrId, aValue) != 0) ||
      (code.interpret_exit_ok() != 0) ||
      (code.finalise() != 0))
  {
    return -1;
  }

  return theOp->setInterpretedCode(&code);

}

int interpretedDecrement(NdbScanOperation * theOp,
                         Uint32 anAttrId, Uint64 aValue)
{

  /* Letting API allocate the space for this, since we don't
     have a place to hold on to any */
  NdbInterpretedCode code(theOp->getTable());

  if ((code.sub_val(anAttrId, aValue) != 0) ||
      (code.interpret_exit_ok() != 0) ||
      (code.finalise() != 0))
  {
    return -1;
  }

  return theOp->setInterpretedCode(&code);

}
