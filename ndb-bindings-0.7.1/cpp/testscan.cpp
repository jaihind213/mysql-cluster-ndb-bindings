#include <stdio.h>
#include <string.h>
#include <malloc.h>
#include <math.h>
#include <ndbapi/NdbApi.hpp>


int main() { 

  ndb_init();
  Ndb_cluster_connection * conn = new Ndb_cluster_connection("127.0.0.1");
  conn->connect(5,3,1);
  conn->wait_until_ready(5,5);
  Ndb * ndb = new Ndb(conn, "test");
  ndb->init();
  const NdbDictionary::Dictionary* dict = ndb->getDictionary();
  const NdbDictionary::Table *t_scan_filter = dict->getTable("t_scan_filter");
  NdbTransaction *myTransaction= ndb->startTransaction();
  NdbScanOperation *myOperation= myTransaction->getNdbScanOperation(t_scan_filter);
  myOperation->readTuples(NdbOperation::LM_CommittedRead);

  NdbScanFilter sf(myOperation);
/*  char * theString = (char *)malloc(4);
  theString[0]=(unsigned char)2;
  memcpy(theString+1, "12",2);
  theString[3]=NULL; */
  char * theString = "12";
  if (sf.begin() < 0 ||
      sf.cmp(NdbScanFilter::COND_LIKE, 4, (void *) theString, (Uint32)2) < 0 ||
      //sf.eq(2,(Uint32)12) < 0 ||
      sf.end() < 0)
    {
      printf("Error: %s\n", myTransaction->getNdbError().message);
      ndb->closeTransaction(myTransaction);
      return -1;
    }

  NdbRecAttr * rec = myOperation->getValue("id",(char *)NULL);
  myTransaction->execute( NdbTransaction::Commit);
  int check;
  int vals = 0;
  while ((check = myOperation->nextResult(true)) == 0) { 
    //printf("The PK value is %d\n",rec->int32_value());
    vals++;
  }
  printf("found:%d\n",vals);
  ndb->closeTransaction(myTransaction);

  delete ndb; 
  delete conn;
  return 0;
}
