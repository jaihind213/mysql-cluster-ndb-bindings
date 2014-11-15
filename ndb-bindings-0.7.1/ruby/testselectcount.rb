require 'mysql/cluster/ndbapi'
require 'date'

Ndbapi::ndb_init

insertid=ARGV[0].to_i

puts "Connecting to cluster"

connection=Ndbapi::NdbClusterConnection.create("127.0.0.1")

begin
  connection.connect(1,1,1)
  connection.waitUntilReady(30, 30)

rescue NdbApiException => e
  puts "ERROR:" + e
  exit 
end


myNdb = connection.create_ndb("test")

myNdb.init(4) 

print "running tests"

myTransaction = myNdb.start_transaction

selCount = myTransaction.select_count("testprojdt")
print "Count: ",selCount,"\n"

myOperation = myTransaction.get_ndb_operation("testprojdt")
myOperation.read_tuple(Ndbapi::NdbOperation::LM_Read)

puts "Reading" , insertid

myOperation.equalUint("id",insertid)
ra=myOperation.get_value("ts")

myTransaction.execute(Ndbapi::Commit)

puts ra.get_uint()

myTransaction.close
