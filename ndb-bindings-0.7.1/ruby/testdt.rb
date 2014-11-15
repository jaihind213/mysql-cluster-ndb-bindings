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


myNdb = connection.createNdb("test")

myNdb.init(4) 

puts "running tests"

myTransaction = myNdb.startTransaction

myOperation = myTransaction.getNdbOperation("testprojdt")
myOperation.insert_tuple

puts "Inserting" , insertid

myOperation.equal("id",insertid)
myOperation.setString("name","monty")
myOperation.setTimestamp("ts",Time.now.to_i)
d=DateTime.now
puts d
myOperation.setDatetime("dt",d)

myTransaction.execute(Ndbapi::Commit)

myTransaction.close
