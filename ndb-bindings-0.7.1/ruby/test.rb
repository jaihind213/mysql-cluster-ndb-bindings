require 'mysql/cluster/ndbapi'

Ndbapi::ndb_init

num_iter=ARGV[0]
insert_num=ARGV[1]

BATCH_SIZE=100

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

puts "running tests"

myTransaction = myNdb.start_transaction

myOperation = myTransaction.get_ndb_operation("mytablename")
myOperation.insert_tuple
auto_id = myNdb.get_auto_increment_value("mytablename",BATCH_SIZE)

puts "Inserting" , auto_id

begin
  
  myOperation.equalInt32("ATTR1",auto_id)
  myOperation.set_int("ATTR2",10)

rescue NdbApiException => e
  puts "ERROR:" + e
  exit
end

myTransaction.execute(Ndbapi::Commit)

myTransaction.close
