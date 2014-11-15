#     ndb-bindings: Bindings for the NDB API
#       Copyright (C) 2006 MySQL, Inc.
    
#       This program is free software; you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation; either version 2 of the License, or 
#     (at your option) any later version.
    
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.

#     You should have received a copy of the GNU General Public License
#     along with this program; if not, write to the Free Software
#     Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA


use lib 'blib/arch';
use lib 'blib';
use lib 'blib/lib';

use ndbapi;
use DBI;
use POSIX qw( ceil floor );
use Time::HiRes qw( gettimeofday tv_interval );

$num_iter = shift;
$INSERT_NUM = shift;
do { print "Usage:\n\ttest.pl NUM_OF_ITERATIONS NUM_OF_ROWS\n";
  exit -1; }  unless $INSERT_NUM;
$BATCH_SIZE = 100;

### Connect to cluster

print "Dropping and recreating scehma\n\n";

$dbh = DBI->connect("dbi:mysql:host=localhost;read_default_group=client","root","" );

$dbh->do("CREATE DATABASE if not exists test");
$dbh->do("USE test");

$dbh->do("DROP TABLE if exists mytablename");
$dbh->do("CREATE TABLE
  mytablename
   (ATTR1 INT UNSIGNED not null auto_increment,
    ATTR2 INT UNSIGNED NOT NULL,
    PRIMARY KEY USING HASH (ATTR1)
   )
ENGINE=NDBCLUSTER
");

print "connecting to cluster\n";

# TODO: Shouldn't we make this automatic? 
ndbapi::ndb_init();
$connection = new ndbapi::NdbClusterConnection();


if ($connection->connect(1,1,1)) {

    print "Connect to cluster management server failed.\n";
    exit -1;
}


if ($connection->waitUntilReady(30,30)) {
    print "Cluster was not ready within 30 secs.\n";
    exit -1;
} else { print "cluster ready\n"; }

$myNdb = $connection->createNdb("test");


if ($myNdb->init(4) == -1) {
    print $myNdb->getNdbError()->getMessage();

    exit -1;
}

print "running tests: \n";


my $before_time = [gettimeofday];

my $auto_id=0;

foreach my $t (1..(ceil($INSERT_NUM/$BATCH_SIZE))) {

    my $myTransaction = $myNdb->startTransaction();

    my $val = $t*$BATCH_SIZE-$INSERT_NUM;
    my $offset = ($val > 0 ) ? $val : 0;

    foreach my $i (1..($BATCH_SIZE-$offset)) {

      my $myOperation = $myTransaction->getInsertOperation("mytablename");

      my $auto_id = $myNdb->getAutoIncrementValue("mytablename",$BATCH_SIZE);
      #$auto_id++;
      #print "$t - $i - $auto_id - $myOperation\n";

      $myOperation->equalLong("ATTR1", $auto_id);
      $myOperation->setLong("ATTR2",$i);

    }

    my $ret = $myTransaction->execute($ndbapi::Commit);

    if ($ret == -1) {
      print $myTransaction->getNdbError()->getMessage(), "\n";
    }

    $myTransaction->close();

}

my $elapsed = tv_interval ( $before_time );

print "Insert time for $INSERT_NUM:\t$elapsed\n";


## Fetch list of ids. We could do this when we build it
## But then we couldn't test Scan Operations

my @ids;

my $myTransaction = $myNdb->startTransaction();


my $myScanOperation=$myTransaction->getSelectScanOperation("mytablename",
                                     $ndbapi::NdbOperation::LM_CommittedRead);

print $myTransaction->getNdbError()->getMessage() unless $myScanOperation;


my $myRecAttr=$myScanOperation->getValue("ATTR1");

$myTransaction->execute($ndbapi::NoCommit);

while ($myScanOperation->nextResult() == 0) {
  
  my $random_id = $myRecAttr->getInt();
  push(@ids,$random_id);
  
}

$myTransaction->close();


#Fetch records using NDBAPI

my $before_time = [gettimeofday];

foreach my $f (0..$num_iter) {

      my $myTransaction = $myNdb->startTransaction();
      if (!$myTransaction) {
        print $myNdb->getNdbError()->getMessage(); }

      my $myOper = $myTransaction->getSelectOperation("mytablename",
                                      $NdbOperation::LockMode::LM_Read);

      my $id_num = $ids[rand($INSERT_NUM)];

      $myOper->equalUlong("ATTR1",$id_num);

      my $myRecAttr = $myOper->getValue("ATTR2");

     
      $myTransaction->execute( $ndbapi::Commit );

      $foo=$myRecAttr->getInt();

      $myTransaction->close();

    }

my $elapsed = tv_interval ( $before_time );
print "Fetch time for NDBAPI - $num_iter:\t$elapsed\n";

#Fetch records using DBD::mysql

my $before_time = [gettimeofday];

my $sth = $dbh->prepare("select ATTR2 from mytablename where ATTR1=?");

foreach my $f (0..$num_iter) {

    my $id_num = $ids[rand($INSERT_NUM)];

    $sth->bind_param(1,$id_num);
    $sth->execute();
    while ($sth->fetch()) {
#
    }

}

my $elapsed = tv_interval ( $before_time );
print "Fetch time for DBD::mysql - $num_iter:\t$elapsed\n";

