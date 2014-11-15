/* Copyright (C) 2003 MySQL AB

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA */

// 
//  ndbapi_simple_index.cpp: Using secondary indexes in NDB API
//
//  Correct output from this program is:
//
//  ATTR1 ATTR2
//    0    10
//    1     1
//    2    12
//  Detected that deleted tuple doesn't exist!
//    4    14
//    5     5
//    6    16
//    7     7
//    8    18
//    9     9

#include <mysql.h>
#include <NdbApi.hpp>

// Used for cout
#include <stdio.h>
#include <iostream>
#include <time.h>


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

#define NUM_ITER 10
#define INSERT_NUM 1

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

    mysql_query(&mysql, "DROP DATABASE TEST_DB_1");
    mysql_query(&mysql, "CREATE DATABASE TEST_DB_1");
    if (mysql_query(&mysql, "USE TEST_DB_1") != 0) MYSQLERROR(mysql);

    if (mysql_query(&mysql, 
		    "CREATE TABLE if not exists"
		    "  MYTABLENAME"
		    "    (ATTR1 INT UNSIGNED auto_increment,"
		    "     ATTR2 varchar(32) NOT NULL,"
		    "     PRIMARY KEY USING HASH (ATTR1)) "
//		    "     UNIQUE MYINDEXNAME USING HASH (ATTR2))"
		    "  ENGINE=NDBCLUSTER"))
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

  Ndb* myNdb = new Ndb( cluster_connection,
			"TEST_DB_1" );  // Object representing the database
  if (myNdb->init() == -1) { 
    APIERROR(myNdb->getNdbError());
    exit(-1);
  }

  const NdbDictionary::Dictionary* myDict= myNdb->getDictionary();
  const NdbDictionary::Table *myTable= myDict->getTable("MYTABLENAME");
  const NdbDictionary::Column *myCol = myTable->getColumn("ATTR1");
  //const Uint32 sizeInBytes = myCol->m_attrSize * myCol->m_arraySize;
  if (myTable == NULL)
    APIERROR(myDict->getNdbError());



  /**************************************************************************
   * Fill table with tuples, using auto_increment IDs                       *
   **************************************************************************/

  {
    std::time_t before_t = std::time(0);
    for (int i = 0; i < INSERT_NUM; i++) {
      NdbTransaction *myTransaction= myNdb->startTransaction();
      if (myTransaction == NULL) APIERROR(myNdb->getNdbError());
      
      NdbOperation *myOperation= myTransaction->getNdbOperation(myTable);
      if (myOperation == NULL) APIERROR(myTransaction->getNdbError());
      
      myOperation->insertTuple();
      
      Uint64 auto_id = 0;
      if (myNdb->getAutoIncrementValue(myTable,auto_id,0) == -1)
	APIERROR(myNdb->getNdbError());
    
        
      myOperation->equal("ATTR1",auto_id);
      if (myOperation->setValue("ATTR2", "hello", (Uint32)495) == -1)
	APIERROR(myOperation->getNdbError());
      
      if (myTransaction->execute( NdbTransaction::Commit ) == -1)
	APIERROR(myTransaction->getNdbError());
      
      myNdb->closeTransaction(myTransaction);
    }
    
    std::time_t after_t = std::time(0);
    std::cout << "Insert time for " << INSERT_NUM << " "; 
    std::cout << after_t << " -- " << before_t;
    std::cout << " = " << after_t - before_t << std::endl;
   
  }

  /*****************************************
   * Fill ID array                         *
   *****************************************/

  
  int ids[INSERT_NUM];
  
  
  {
    NdbTransaction *myTransaction= myNdb->startTransaction();
    
    if (myTransaction == NULL) APIERROR(myNdb->getNdbError());
    
    NdbScanOperation *myScanOperation=
      myTransaction->getNdbScanOperation(myTable);

    if (myScanOperation == NULL) 
      APIERROR(myTransaction->getNdbError());
    
    if (myScanOperation->readTuples(NdbOperation::LM_CommittedRead))  
      APIERROR(myScanOperation->getNdbError());
    
    
    NdbRecAttr *myRecAttr= myScanOperation->getValue("ATTR1");
    
    
    myTransaction->execute(NdbTransaction::NoCommit);
    
    for (int i = 0; i < INSERT_NUM; i++) {
      
      if (myScanOperation->nextResult(true) != 0) 
	break; 
      
      random_id = myRecAttr->u_32_value();
      ids[i]=random_id;
      
    }
    
    myNdb->closeTransaction(myTransaction);
  
  }



  /*******************
   * Test NDB API Speed
   *******************/
  {
    std::time_t before_t = std::time(0);
    char foo;
    for (int i = 0 ; i<NUM_ITER; i++) { 
      NdbTransaction *myTrans = myNdb->startTransaction();
	if (myTrans == NULL) APIERROR(myNdb->getNdbError());
      
      NdbOperation *myOper = myTrans->getNdbOperation(myTable);
      myOper->readTuple(NdbOperation::LM_Read);
      int id_num = ids[rand()%1000];
      //      std::cout << id_num << std::endl;
      myOper->equal("ATTR1",0);
      NdbRecAttr *myRecAttr= myOper->getValue("ATTR2", NULL);
      if(myTrans->execute( NdbTransaction::Commit ) == -1)
	  APIERROR(myTrans->getNdbError());
      //int len = myRecAttr->arraySize();
      //std::cout << "len: " << len << std::endl;
      char *strval = myRecAttr->aRef();
      foo=myRecAttr->char_value();
	std::cout << "foo:" << foo << std::endl;
	std::cout << "aref: " << strval << std::endl;
      myNdb->closeTransaction(myTrans);
      
    }
    std::time_t after_t = std::time(0);
    std::cout << "NDBAPI time for " << INSERT_NUM << " "; 
    std::cout << after_t << " -- " << before_t;
    std::cout << " = " << after_t - before_t << std::endl;


  }
}
