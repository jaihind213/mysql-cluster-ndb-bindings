from __future__ import with_statement
import sys,time,random,struct,math
from mysql.cluster import ndbapi
import mysql.cluster.ndbapi
import threading 

class PythonCallback(ndbapi.BaseCallback):

  def __init__(self, recAttr):
    self.recAttr=recAttr

  def __call__(self, *args, **kw): 
    self.callback(*args,**kw)

  def callback(self, ret, myTrans):
    #print "value = ", self.recAttr.getValue()
    x=self.recAttr.getValue()

import  MySQLdb

if len(sys.argv) != 3:
  print "Usage:\n\ttest.py NUM_OF_ITERATIONS NUM_OF_ROWS "
  sys.exit(1)

num_iter=int(sys.argv[1])
INSERT_NUM=int(sys.argv[2])
concurrent=1
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
   
try:
  connection = ndbapi.NdbClusterConnection();

  connection.connect(1,1,1)
except mysql.cluster.ndbapi.NdbApiException,e:
  print e
  sys.exit(-1)

if (connection.waitUntilReady(30,30)):
    print "Cluster was not ready within 30 secs."
    sys.exit(-1);


myNdb = connection.createNdb("test")

if (myNdb.init(1000) == -1):
    print myNdb.getNdbError().getMessage()
    sys.exit(-1)


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
   
    myOperation.equalUint64("ATTR1",auto_id);
    myOperation.setValue("ATTR2", i);
      
  ret = myTransaction.execute( ndbapi.Commit )
  if ret == -1:
    print myTransaction.getNdbError().getMessage()
  
  #myTransaction.close()

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
      
    
#myTransaction.close()


### Fetch ids using ndb

before_t = time.time()

threadlist=[]
connsem=threading.BoundedSemaphore(value=1)

def runit():
  mydata=threading.local()
  mydata.t=threading.currentThread()
  with connsem:
    mydata.ndb=connection.createNdb("test")
  if (mydata.ndb.init(4) == -1):
    print mydata.ndb.getNdbError().getMessage()
    sys.exit(-1)
  mydata.translist=[]

  for mydata.f in range(0,int(num_iter/concurrent)):

    mydata.id_num = ids[random.randrange(0,len(ids))]
    #print "starting new trans", mydata.f,mydata.t.getName()
    mydata.myTrans = mydata.ndb.startTransaction("mytablename","%s"%mydata.id_num)
    
    if mydata.myTrans is None:
        print mydata.ndb.getNdbError().getMessage()

    mydata.myOper = mydata.myTrans.getNdbOperation("mytablename")
    mydata.myOper.readTuple(ndbapi.NdbOperation.LM_Read)

    mydata.myOper.equalUint64("ATTR1",mydata.id_num)
            
    mydata.myRecAttr= mydata.myOper.getValue("ATTR2")

    mydata.cb =  PythonCallback(mydata.myRecAttr)
    
    if mydata.myTrans.executeAsynchPrepare( ndbapi.Commit , mydata.cb ) == -1:
        print mydata.myTrans.getNdbError().getMessage()
    mydata.translist.append(mydata.myTrans)
    if mydata.f%10==0:
      #print "polling"
      mydata.ndb.sendPollNdb(3000,20)
      for mydata.x in range(0,len(mydata.translist)):
        mydata.x=mydata.translist.pop()
        mydata.x.close()
        del(mydata.x)
  time.sleep(0.001)
  mydata.ndb.sendPollNdb(3000,4)

  for mydata.x in range(0,len(mydata.translist)):
    mydata.x=mydata.translist.pop()
    mydata.x.close()
    del(mydata.x)

for x in range(0,concurrent):
  thr=threading.Thread(target=runit,name="thread%s"%x)
  threadlist.append(thr)
  thr.start()

for x in threadlist:
  x.join()

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
