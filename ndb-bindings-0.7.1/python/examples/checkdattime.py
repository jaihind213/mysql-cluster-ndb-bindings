from mysql.cluster import ndbapi
from datetime import datetime
import sys,time

mtdate=datetime.now()
c=ndbapi.NdbClusterConnection()
c.connect(1,1,1)
c.waitUntilReady(5,5)

ndb=c.createNdb('test')
ndb.init(1)
t=ndb.startTransaction()

d=ndb.getDictionary()
dt=d.getTable('testprojdt')
col=dt.getColumn('dt')
vst=ndbapi.ndbFormatDateTime(col,mtdate)
print repr(vst)

o=t.getNdbOperation(dt)
o.insertTuple()
o.equal('id',5)
o.setDatetime('dt',mtdate)
o.setTimestamp('ts',int(time.mktime(mtdate.timetuple())))
o.setNull("name")
t.execute(ndbapi.Commit)

