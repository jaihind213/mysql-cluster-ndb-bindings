import sys,time,random,struct,math
from mysql.cluster import ndbapi
#import mysql.cluster.ndbapi


import  MySQLdb

if len(sys.argv) != 3:
  print "Usage:\n\ttest.py NUM_OF_ITERATIONS NUM_OF_ROWS "
  sys.exit(1)

num_iter=int(sys.argv[1])
INSERT_NUM=int(sys.argv[2])
BATCH_SIZE=1000

db = MySQLdb.connect(host="localhost",user="root",read_default_group="client")

cur=db.cursor()

print "Dropping and recreating schema\n"

cur.execute("CREATE DATABASE if not exists test")
cur.execute("USE test")

cur.execute("drop table if exists mytablename")

cur.execute("""
CREATE TABLE if not exists
  mytablename
   (ATTR1 INT UNSIGNED not null auto_increment,
    ATTR2 INT UNSIGNED NOT NULL,
    PRIMARY KEY USING HASH (ATTR1))
ENGINE=NDBCLUSTER 
""")


### Connect to cluster

print "connecting to cluster\n"
   
connection=None

try:
  connection = ndbapi.NdbClusterConnection()
  print "connection %s" % connection
  connection.connect(5,3,1)
except ndbapi.NdbApiException,e:
  print "hoolollie!"
  print e
  sys.exit(-1)

connection.waitUntilReady(30,30)


myNdb = connection.createNdb("test",4)


print "running tests:\n"

### Fill db


before_t = time.time()

for t in range(0,int(math.ceil((int(INSERT_NUM)*1.0)/(1.0*BATCH_SIZE)))):
  
  myTransaction= myNdb.startTransaction()
  val = ((t+1)*BATCH_SIZE)-INSERT_NUM
  offset = 0
  if ( val > 0 ):
    offset = val

  for i in range(0,BATCH_SIZE-offset):
    
    myOperation=myTransaction.getNdbOperation("mytablename")
    myOperation.insertTuple()
    auto_id = myNdb.getAutoIncrementValue("mytablename",BATCH_SIZE)
   
    myOperation.equalUlong("ATTR1",auto_id);
    myOperation.setInt("ATTR2", i);
      
  ret = myTransaction.execute( ndbapi.Commit )
  if ret == -1:
    print myTransaction.getNdbError().getMessage()
  
  myTransaction.close()

after_t = time.time()
print "Insert time for %s: %s -- %s" % ( INSERT_NUM, after_t, before_t)
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

    id_num = ids[random.randrange(0,len(ids))]
    myTrans = myNdb.startTransaction() #"mytablename",int("%s"%id_num))
    if myTrans is None:
        print myNdb.getNdbError().getMessage()

    myOper = myTrans.getNdbOperation("mytablename")
    myOper.readTuple(ndbapi.NdbOperation.LM_Read)

    myOper.equalUlong("ATTR1",id_num)
            
    myRecAttr= myOper.getValue("ATTR2")

    
    if myTrans.execute( ndbapi.Commit ) == -1:
        print myTrans.getNdbError().getMessage()
    foo=myRecAttr.getValue()
    myTrans.close()
      

after_t = time.time()
print "NDBAPI time for %s: %s -- %s" % ( num_iter, after_t, before_t)
print "\t %s" % (after_t - before_t)



cur=db.cursor()

before_t = time.time()

for f in range(0,int(num_iter)):

    id_num = ids[random.randrange(0,len(ids))]

    cur.execute("select ATTR2 from mytablename where ATTR1=%s" % (id_num))
    res=cur.fetchall()

after_t = time.time()

after_t = time.time()
print "MySQL time for %s: %s -- %s" % ( num_iter, after_t, before_t)
print "\t %s" % (after_t - before_t)

#ndbapi.ndb_end()
