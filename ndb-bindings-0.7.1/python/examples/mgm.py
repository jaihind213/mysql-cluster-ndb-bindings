from mysql.cluster import mgmapi
from mysql.cluster import events
from mysql.cluster import listeners

mgm = mgmapi.NdbMgmFactory.createNdbMgm("localhost")
mgm.setConnectTimeout(4000)
mgm.connect(5,3,True)

theList = mgmapi.NdbFilterList()
theItem = mgmapi.NdbFilterItem(15,mgmapi.NDB_MGM_EVENT_CATEGORY_STATISTIC)
theList.push_back(theItem)
manager=mgm.createNdbLogEventManager(theList) 

class TransReportListener(listeners.TransReportCountersTypeListener):

    def handleEvent(self, event):
        #theEvent = events.TransReportCounters(event)
        print "Handler Trans count: ",theEvent.transCount
        print "Handler Read count: ", theEvent.readCount
        print "Handler Scan count: ", theEvent.scanCount
        print "Handler Range count: ", theEvent.rangeScanCount

theListener=TransReportListener()
manager.registerListener(theListener)

while(True): 
    manager.pollEvents(5000)
    #event = manager.getLogEvent(5000)
    #if event is not None: 
    #    if event.getEventType() == mgmapi.NDB_LE_TransReportCounters:
    #        theEvent = events.TransReportCounters(event)
    #        print "Trans count: ",theEvent.transCount
    #        print "Read count: ", theEvent.readCount
    #        print "Scan count: ", theEvent.scanCount
    #        print "Range count: ", theEvent.rangeScanCount
    #    print "Got an Event from Node %s\t%s" % (event.getSourceNodeId(),event.getEventType())
