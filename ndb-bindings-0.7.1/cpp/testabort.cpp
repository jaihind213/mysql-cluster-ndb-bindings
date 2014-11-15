/* To turn on tracing, either reconfigure mysql with --ndbcluster --with-ndb-api-trace, or edit TransporterFacade.cpp and uncomment API_TRACE
 * Then export API_SIGNAL_LOG=location.to.trace.log.file
 */

#include <stdio.h>
#include <iostream>
#include <string.h>
#include <malloc.h>
#include <math.h>
#include <mysql.h>
#include <ndbapi/NdbApi.hpp>

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


int main() { 

  ndb_init();
  MYSQL mysql;


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

    if (mysql_query(&mysql, "drop table if exists t_ops") != 0)
      MYSQLERROR(mysql);
    if (mysql_query(&mysql,
                    "CREATE TABLE"
                    "  t_ops"
                    "(`id` INT PRIMARY KEY, `col2` int not null)"
                    "ENGINE=ndb;"))
      MYSQLERROR(mysql);
  }

  Ndb_cluster_connection * conn = new Ndb_cluster_connection("127.0.0.1");
  conn->connect(5,3,1);
  conn->wait_until_ready(5,5);
  Ndb * ndb = new Ndb(conn, "test");
  ndb->init();
  const NdbDictionary::Dictionary* dict = ndb->getDictionary();
  const NdbDictionary::Table *t_ops = dict->getTable("t_ops");

  int id = 0;

  int NUM_INSERTS = 10;

  const char * col1 = "id";
  const char * col2 = "col2";

  NdbTransaction *myTransaction= ndb->startTransaction();


  for (int x=1;x<=NUM_INSERTS;x++) {

    NdbOperation *myOperation=
      myTransaction->getNdbOperation(t_ops);
    if (myOperation == NULL)
      APIERROR(myTransaction->getNdbError());

    if (myOperation->insertTuple())
      APIERROR(myOperation->getNdbError());

    if (myOperation->equal(col1,x))
      APIERROR(myOperation->getNdbError());

    if (myOperation->setValue(col2,x))
      APIERROR(myOperation->getNdbError());

  }

  int x = 1;

  NdbOperation *myOperation=
    myTransaction->getNdbOperation(t_ops);
  if (myOperation == NULL)
    APIERROR(myTransaction->getNdbError());

  if (myOperation->insertTuple())
    APIERROR(myOperation->getNdbError());

  if (myOperation->equal(col1,x))
    APIERROR(myOperation->getNdbError());

  if (myOperation->setValue(col2,x))
    APIERROR(myOperation->getNdbError());

  if (myTransaction->execute( NdbTransaction::Commit))
    APIERROR(myTransaction->getNdbError());

  ndb->closeTransaction(myTransaction);
  delete ndb;
  delete conn;
  return 0;
}
