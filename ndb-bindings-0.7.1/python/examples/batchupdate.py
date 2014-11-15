import sys,time,random,struct,math
from mysql.cluster import ndbapi


import  MySQLdb

if len(sys.argv) < 3:
  print "Usage:\n\t%s NUM_OF_ITERATIONS NUM_OF_ROWS [BATCH_SIZE]" %(sys.argv[0])
  sys.exit(1)

num_iter=int(sys.argv[1])
INSERT_NUM=int(sys.argv[2])
BATCH_SIZE=1000
if len(sys.argv) == 4:
  BATCH_SIZE=int(sys.argv[3])

db = MySQLdb.connect(host="localhost",user="root",read_default_group="client")

cur=db.cursor()

print "Dropping and recreating schema\n"

cur.execute("CREATE DATABASE if not exists test")
cur.execute("USE test")

cur.execute("drop table if exists mytablename")

cur.execute("""
CREATE TABLE if not exists
  mytablename
   (id INT UNSIGNED not null auto_increment,
    val INT UNSIGNED NOT NULL,
    PRIMARY KEY USING HASH (id),
    KEY(val))
ENGINE=NDBCLUSTER 
""")


### Connect to cluster

print "connecting to cluster\n"

connection=None

try:
  connection = ndbapi.NdbClusterConnection()
  connection.connect(5,3,1)
except ndbapi.NdbApiException,e:
  sys.exit(-1)

connection.waitUntilReady(30,30)


myNdb = connection.createNdb("test",4)


print "running tests:\n"

### Fill db

ids=[]

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

    myOperation.equalUlong("id",auto_id)
    ids.append(auto_id)
    myOperation.setInt("val", i)

  myTransaction.execute( ndbapi.Commit )

  myTransaction.close()

after_t = time.time()
print "Insert time for %s: %s -- %s" % ( INSERT_NUM, after_t, before_t)
print "\t %s: %s per second" % ((after_t - before_t), INSERT_NUM/(after_t - before_t))


### Get list of ids


### Update ids using ndb

before_t = time.time()


for f in range(0,int(num_iter)):

  #Get a random id
  id_num = ids[random.randrange(0,len(ids))]
  myTrans = myNdb.startTransaction()

  myOper = myTrans.getNdbOperation("mytablename")
  myOper.writeTuple()

  myOper.equalUlong("id",id_num)

  myOper.setInt("val",random.randrange(0,200))


  myTrans.execute( ndbapi.Commit )
  myTrans.close()

after_t = time.time()
print "NDBAPI time for %s: %s -- %s" % ( num_iter, after_t, before_t)
print "\t %s: %s per second" % ((after_t - before_t), num_iter/(after_t - before_t))

### Update ids using ndb using batching

before_t = time.time()


for t in range(0,int(math.ceil((int(num_iter)*1.0)/(1.0*BATCH_SIZE)))):

  myTransaction= myNdb.startTransaction()
  val = ((t+1)*BATCH_SIZE)-num_iter
  offset = 0
  if ( val > 0 ):
    offset = val

  for i in range(0,BATCH_SIZE-offset):

    myOperation=myTransaction.getNdbOperation("mytablename")
    myOperation.updateTuple()

    myOperation.equalUlong("id",auto_id)
    myOperation.setInt("val", random.randrange(0,200))


  myTransaction.execute( ndbapi.Commit )

  myTransaction.close()


after_t = time.time()
print "Batched NDBAPI time for %s: %s -- %s" % ( num_iter, after_t, before_t)
print "\t %s: %s per second" % ((after_t - before_t), num_iter/(after_t - before_t))



# Do the same with MySQL

cur=db.cursor()

before_t = time.time()

for f in range(0,int(num_iter)):

    id_num = ids[random.randrange(0,len(ids))]
    new_val = random.randrange(0,200)
    cur.execute("update  mytablename set val=%s where id=%s" % (new_val,id_num))


after_t = time.time()
print "MySQL time for %s: %s -- %s" % ( num_iter, after_t, before_t)
print "\t %s: %s per second" % ((after_t - before_t), num_iter/(after_t - before_t))


#ndbapi.ndb_end()
