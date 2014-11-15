import sys,time,random,struct,math
from mysql.cluster import ndbapi

import  MySQLdb

if len(sys.argv) != 3:
  print "Usage:\n\ttestjoin.py NUM_OF_ITERATIONS NUM_OF_ROWS "
  sys.exit(1)

num_iter=int(sys.argv[1])
INSERT_NUM=int(sys.argv[2])
BATCH_SIZE=1000

db = MySQLdb.connect(host="localhost",user="root",read_default_group="client",db="test")

cur=db.cursor()

print "Dropping and recreating schema\n"

cur.execute("drop table if exists mytablename")

cur.execute("""
CREATE TABLE if not exists
  mytablename
   (ATTR1 INT UNSIGNED not null auto_increment,
    ATTR2 INT UNSIGNED NOT NULL,
    PRIMARY KEY (ATTR1),
    KEY ATTR2 (ATTR2))
ENGINE=NDBCLUSTER 
""")

print "create"
cur.execute("drop table if exists myothertable")

cur.execute("""
CREATE TABLE if not exists
  myothertable
   (ATTR2 INT UNSIGNED not null,
    ATTR3 INT UNSIGNED NOT NULL,
    PRIMARY KEY USING HASH (ATTR2))
ENGINE=NDBCLUSTER 
""")


### Connect to cluster

print "connecting to cluster\n"
   
try:
  connection = ndbapi.NdbClusterConnection();

  connection.connect(1,1,1)
except ndbapi.NdbApiException,e:
  print e
  sys.exit(-1)

if (connection.waitUntilReady(30,30)):
    print "Cluster was not ready within 30 secs."
    sys.exit(-1);


myNdb = connection.createNdb("test")

if (myNdb.init(4) == -1):
    print myNdb.getNdbError().getMessage()
    sys.exit(-1)


print "running tests:\n"

myDict=myNdb.getDictionary()
myTable=myDict.getTable("mytablename")
myAttr2Idx=myDict.getIndex("ATTR2","mytablename")

### Fill db


before_t = time.time()

for t in range(0,INSERT_NUM):
  
  myTransaction= myNdb.startTransaction()

  myOperation=myTransaction.getNdbOperation("mytablename")
  myOperation.insertTuple()
  auto_id = myNdb.getAutoIncrementValue("mytablename",INSERT_NUM)

  myOperation.equalUlong("ATTR1",auto_id);
  myOperation.setInt("ATTR2",t%10);

  #for i in range(0,BATCH_SIZE):

  attr3id=random.randrange(0,INSERT_NUM*BATCH_SIZE)
  
  myOperation=myTransaction.getNdbOperation("myothertable")
  myOperation.insertTuple()
  
  myOperation.equalUlong("ATTR2",auto_id);
  myOperation.setInt("ATTR3", attr3id);
      
  ret = myTransaction.execute( ndbapi.Commit )
  if ret == -1:
    print myTransaction.getNdbError().getMessage()
  
  myTransaction.close()

after_t = time.time()
print "Insert time for %s: %s -- %s" % ( INSERT_NUM*2, after_t, before_t)
print "\t %s" % (after_t - before_t)


### Get list of ids
myTransaction = myNdb.startTransaction()

myScanOperation=myTransaction.getNdbScanOperation("mytablename");


if myScanOperation is None:
    print myTransaction.getNdbError().getMessage()


if myScanOperation.readTuples(ndbapi.NdbOperation.LM_CommittedRead):
    print myScanOperation.getNdbError().getMessage()


    
myRecAttr=myScanOperation.getValue("ATTR1");
    
    
myTransaction.execute(ndbapi.NoCommit);

ids=[]
while 1:
      
    if (myScanOperation.nextResult(True) != 0) :
        break
      
    random_id = myRecAttr.getValue()
    ids.append(random_id)
      
    
myTransaction.close()


### Fetch ids using ndb

before_t = time.time()


for f in range(0,int(num_iter)):

    id_num = random.randrange(0,10)
    myTrans = myNdb.startTransaction()
    
    myScanOperation=myTransaction.getNdbIndexScanOperation(myAttr2Idx)
    myScanOperation.readTuples(ndbapi.NdbOperation.LM_CommittedRead)

    myScanOperation.equalUlong("ATTR2",id_num)
    myRecAttr= myScanOperation.getValue("ATTR1")

    
    myTrans.execute( ndbapi.Commit )

    myJoinTrans=myNdb.startTransaction()

    attrs=[]
    while 1:
      myJoinOperation=myJoinTrans.getNdbOperation("myothertable")
      myJoinOperation.readTuple(ndbapi.NdbOperation.LM_CommittedRead)
      myJoinOperation.equalUlong("ATTR2",myRecAttr.getValue())
      attrs.append(myJoinOperation.getValue("ATTR3"))
      if (myScanOperation.nextResult(True) != 0) :
        break
      
      
    myJoinTrans.execute(ndbapi.Commit)
    for attr in attrs:
      foo=attr.getValue()

    myJoinTrans.close()  
      
    myTrans.close()

after_t = time.time()
print "NDBAPI time for %s: %s -- %s" % ( num_iter, after_t, before_t)
print "\t %s" % (after_t - before_t)



cur=db.cursor()

before_t = time.time()

for f in range(0,int(num_iter)):

    id_num = random.randrange(0,10)

    cur.execute("select t1.ATTR1, t2.ATTR3 from mytablename t1, myothertable t2 where t1.ATTR1=t2.ATTR2 and t1.ATTR2=%s" % id_num)
    res=cur.fetchall()

after_t = time.time()

after_t = time.time()
print "MySQL time for %s: %s -- %s" % ( num_iter, after_t, before_t)
print "\t %s" % (after_t - before_t)

#ndbapi.ndb_end()
