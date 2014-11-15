from mysql.cluster import ndbapi

conn=ndbapi.NdbClusterConnection()

conn.connect(5,3,1)
conn.waitUntilReady(30,30)


