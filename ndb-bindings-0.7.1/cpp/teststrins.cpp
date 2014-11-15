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
			"test" );  // Object representing the database
  if (myNdb->init() == -1) { 
    APIERROR(myNdb->getNdbError());
    exit(-1);
  }

  const NdbDictionary::Dictionary* myDict= myNdb->getDictionary();
  const NdbDictionary::Table *myTable= myDict->getTable("mytablename");
/*  const NdbDictionary::Column *myCol = myTable->getColumn("ATTR");
  const Uint32 sizeInBytes = myCol->getLength();
  CHARSET_INFO* cs_info =  myCol->getCharset(); 
  std::cout << "size in bytes: " << sizeInBytes << std::endl; */
  //std::cout << "charset name: " << cs_info->csname <<std::endl;
  if (myTable == NULL)
    APIERROR(myDict->getNdbError());


  /**************************************************************************
   * Fill table with tuples, using auto_increment IDs                       *
   **************************************************************************/

  {
    NdbTransaction *myTransaction= myNdb->startTransaction();
    if (myTransaction == NULL) APIERROR(myNdb->getNdbError());
    
    NdbOperation *myOperation= myTransaction->getNdbOperation(myTable);
    if (myOperation == NULL) APIERROR(myTransaction->getNdbError());
      
    if (myOperation->insertTuple() == -1)
      APIERROR(myOperation->getNdbError());
      
    Uint64 auto_id = 0;
    if (myNdb->getAutoIncrementValue(myTable,auto_id,0) == -1)
      APIERROR(myNdb->getNdbError());
    
    std::cout << auto_id << std::endl;
    //char buf[31]; 
    //char mylen = 5;
    //strncpy(buf,&mylen,1);
    //std::cout << "whee" << buf << "whee" << std::endl;
    //strncpy(buf,strcat(&mylen,"hello"),30);
    //std::cout << "whee" << buf << "whee" << std::endl;
    if (myOperation->equal("ATTR1",(Uint32)auto_id) == -1)
      APIERROR(myOperation->getNdbError());

    std::cout << "before setvalue" << std::endl; 

    if (myOperation->setValue("ATTR2", "\005hello" ) == -1)
      APIERROR(myOperation->getNdbError());
    std::cout << "after setvalue" << std::endl; 
      
    if (myTransaction->execute( NdbTransaction::Commit ) == -1)
      APIERROR(myTransaction->getNdbError());
    
    myNdb->closeTransaction(myTransaction);
  }
    
}
