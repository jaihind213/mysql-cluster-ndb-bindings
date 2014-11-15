require 'mysql/cluster/ndbapi'

class NdbCallback 
  def initialize(myRecAttr)
    @myRecAttr=myRecAttr
  end
  def call(trans)
    puts @myRecAttr.getInt
  end
end  

Ndbapi::ndb_init

num_iter=ARGV[0]
insert_num=ARGV[1]

BATCH_SIZE=10

puts "Connecting to cluster"

#connection=Ndbapi::NdbClusterConnection.create()
connection=Ndbapi::NdbClusterConnection.create("127.0.0.1")

begin
  connection.connect(1,1,1)
  connection.waitUntilReady(30, 30)

rescue NdbApiException => e
  puts "ERROR:" + e
  exit 
end

myNdb = connection.createNdb("test")

myNdb.init(4) 

puts "running tests"

myTransaction = myNdb.startTransaction

myOperation = myTransaction.getNdbOperation("mytablename")
myOperation.insertTuple
auto_id = myNdb.get_autoIncrementValue("mytablename",BATCH_SIZE)

puts "Inserting" , auto_id

myOperation.equal("ATTR1",auto_id)
myOperation.setValue("ATTR2",1234)

myTransaction.execute(Ndbapi::Commit)

myTransaction.close

myTransaction = myNdb.startTransaction

myOperation = myTransaction.getNdbOperation("mytablename")
myOperation.read_tuple(Ndbapi::NdbOperation::LM_Read)

myOperation.equal("ATTR1", auto_id)
myRecAttr = myOperation.getValue("ATTR2")

cb=NdbCallback.new(myRecAttr)

myTransaction.executeAsynchPrepare(Ndbapi::Commit,cb)

myNdb.sendPollNdb(3000,1)

