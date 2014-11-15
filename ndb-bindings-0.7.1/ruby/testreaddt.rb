require 'mysql/cluster/ndbapi'
require 'date'

Ndbapi::ndb_init

insertid=ARGV[0].to_i

puts "Connecting to cluster"

connection=Ndbapi::NdbClusterConnection("127.0.0.1")

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

myOperation = myTransaction.getNdbOperation("testprojdt")
myOperation.readTuple(Ndbapi::NdbOperation::LM_Read)

puts "Reading" , insertid

myOperation.equalInt("id",insertid)
ra=myOperation.getValue("ts")

myTransaction.execute(Ndbapi::Commit)

puts ra.getUint()

myTransaction.close
