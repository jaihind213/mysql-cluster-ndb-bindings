



#include <mysql.h>
#include <NdbApi.hpp>

// Used for cout
#include <stdio.h>
#include <iostream>
#include <time.h>


#define NUMBER_OF_TEST_ITERATIONS  1000000
#define NUMBER_OF_ELEMENT_INSERTIONS 25

#define NUM_TRANSACTIONS             500
#define TRANSACTIONS_TIMEOUT         10
#define FORCE_SEND                   1

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TABLE METADATA
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

#define PROBTABLE  "OutOfConnectionProblemTable"

int PROBTABLE_FIELD1;

int myTransactionCount      = 0;
int myTotalTransactionCount = 0;


#define PRINT_ERROR(code,msg) \
  std::cout << "Error in " << __FILE__ << ", line: " << __LINE__ \
            << ", code: " << code \
            << ", msg: " << msg << "." << std::endl
#define MYSQLERROR(mysql) { \
  PRINT_ERROR(mysql_errno(&mysql),mysql_error(&mysql)); \
  exit(-1); }
#define APIERROR(error) { \
  PRINT_ERROR(error.code,error.message); \
  exit(-1); }

int random_id = 0;

#define NUM_ITER 1000
#define INSERT_NUM 1000

typedef struct
async_callback_t
{
  Ndb *ndb;
  int cnt; 
};
static void callBack(int result,
                     NdbTransaction *trans,
                     void *aObject)
{
  async_callback_t *cbData((async_callback_t *)aObject);
  int tid= cbData->cnt;
  myTransactionCount--;
  if (result < 0)
  {
    if(trans!=0)
    {
      std::cout << "error!! Error code: " << trans->getNdbError().code
           << "Err message: " << trans->getNdbError().message
           << std::endl;
      trans->close();
      return;
    }
  }
  if(trans!=0)
  {
    trans->close();
  }
  free(cbData); 
}

int cnt=0;
Ndb * ndb = NULL; 

NdbTransaction * startCountedTransaction() {
  myTransactionCount++;
  myTotalTransactionCount++;
  
  return ndb->startTransaction();
}

int pollNdb(int aMillisecondNumber, int minNoOfEventsToWakeup) {
  int numberOfCallbacks = ndb->pollNdb(aMillisecondNumber, minNoOfEventsToWakeup);
  
  std::cout << "Poll produced " << numberOfCallbacks << " callbacks (" << myTransactionCount; 
  std::cout << " txs outstanding) [Total " <<  myTotalTransactionCount << "]" <<std::endl;
  
  return numberOfCallbacks;
}

int sendPollNdb(int aMillisecondNumber, int minNoOfEventsToWakeup, int forceSend) {
  ndb->sendPreparedTransactions(forceSend);
  
  return pollNdb(aMillisecondNumber, minNoOfEventsToWakeup);
}

void generateRequestTxs(int theLastBatch) {
  
  bool  success = false;
  
  NdbOperation * orderOp;
  async_callback_t * callback_data; 
  
  
  for (int elementCount = 1;
       elementCount < (NUMBER_OF_ELEMENT_INSERTIONS) + 1;
       elementCount++) {
    
    cnt++;
    NdbTransaction * trans = startCountedTransaction();
    
    orderOp = trans->getNdbOperation(PROBTABLE);
    
    orderOp->insertTuple();
    
    orderOp->equal(PROBTABLE_FIELD1, cnt);
    orderOp->setValue("a", cnt);
    orderOp->setValue("b", cnt);
    orderOp->setValue("c", cnt);
    orderOp->setValue("d", cnt);
    orderOp->setValue("e", cnt);
    orderOp->setValue("f", cnt);
    orderOp->setValue("g", cnt);
    
    callback_data = (async_callback_t *)malloc(sizeof(async_callback_t));
    
    
    callback_data->ndb=ndb; 
    callback_data->cnt=cnt;
    
    
    trans->executeAsynchPrepare(NdbTransaction::Commit,&callBack,
				(void *)callback_data, 
				NdbOperation::AbortOnError);
    


    success = true;
    
  }
  
}


int main()
{
  ndb_init();
  MYSQL mysql;

  /**************************************************************
   * Connect to mysql server and create table                   *
   **************************************************************/

  {
    if ( !mysql_init(&mysql) ) {
      std::cout << "mysql_init failed\n";
      exit(-1);
    }
    mysql_options(&mysql,MYSQL_READ_DEFAULT_GROUP,"client");
    if ( !mysql_real_connect(&mysql, "localhost", "root", "", "",
			     3306, NULL, 0) )
      MYSQLERROR(mysql);

    if (mysql_query(&mysql, "USE test") != 0) MYSQLERROR(mysql);

    if (mysql_query(&mysql, 
		    "CREATE TABLE if not exists "
		    PROBTABLE
		    " (field1 INT UNSIGNED NOT NULL, "
		    "a integer, "
		    "b integer, "
		    "c integer, "
		    "d integer, "
		    "e int, "
		    "f integer, "
		    "g integer, "
		    "PRIMARY KEY(field1)) ENGINE=ndb "
		    "PARTITION BY KEY (field1);"))
      MYSQLERROR(mysql);
  }

  /**************************************************************
   * Connect to ndb cluster                                     *
   **************************************************************/

  Ndb_cluster_connection *cluster_connection=
    new Ndb_cluster_connection(); // Object representing the cluster

  if (cluster_connection->connect(5,3,1))
  {
    std::cout << "Connect to cluster management server failed.\n";
    exit(-1);
  }

  if (cluster_connection->wait_until_ready(30,30))
  {
    std::cout << "Cluster was not ready within 30 secs.\n";
    exit(-1);
  }


  ndb = new Ndb( cluster_connection,
			"test" );  // Object representing the database
  if (ndb->init(1000) == -1) { 
    APIERROR(ndb->getNdbError());
    exit(-1);
  }

  const NdbDictionary::Dictionary* myDict= ndb->getDictionary();
  const NdbDictionary::Table *myTable= myDict->getTable(PROBTABLE);
  PROBTABLE_FIELD1    = myTable->getColumn("field1")->getColumnNo();
  
  if (myTable == NULL)
    APIERROR(myDict->getNdbError());


  
  for (int iteration = 0;
       iteration < NUMBER_OF_TEST_ITERATIONS;
       iteration++) {
    generateRequestTxs(iteration);
    
    sendPollNdb(TRANSACTIONS_TIMEOUT, NUM_TRANSACTIONS, FORCE_SEND);	
  }

  std::cout << "Performing cooldown" << std::endl;

  pollNdb(100000, myTransactionCount);	


  std::cout << "Test completed with no exceptions" << std::endl;

  

}
