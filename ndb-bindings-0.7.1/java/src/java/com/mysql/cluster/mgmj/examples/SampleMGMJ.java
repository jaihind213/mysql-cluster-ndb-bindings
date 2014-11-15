package com.mysql.cluster.mgmj.examples;

import java.util.ArrayList;
import java.util.List;

import com.mysql.cluster.mgmj.*;
import com.mysql.cluster.mgmj.events.*;
import com.mysql.cluster.mgmj.listeners.*;

public class SampleMGMJ {

	/**
	 * SampleMGMJ demonstrates how to monitor events taking place
	 * inside MySQL Cluster.
	 * <br>
	 * SampleMGMJ connects to one or more (for redundancy) management servers 
	 * as specified by the connectString as an argument, and then starts to
	 * subscribe to a number of events.
	 * <br>
	 * E.g, sampleMGMJ "host1:1186;host2:1186"  
	 * would connect sampleMGMJ to host1:1186. If host1 fails,
	 * then sampleMGMJ will reconnect to the management server on host2:1186
	 * and continue subscribing to events.
	 */

	public static void main(String[] args) throws NdbMgmException {

		String connectString="127.0.0.1";
		if(args.length == 1)
		{
			connectString=args[0];
		}

		/*
		 * start monitoring forever..
		 */
		while(true) { 
			try {

				/*
				 * A short sleep before trying to reconnect if there
				 * has been a failure (exception)
				 */
				try {
					Thread.sleep(50);

				} catch(InterruptedException ie){

					break;
				}
				NdbMgm mgm = NdbMgmFactory.createNdbMgm(connectString);

				mgm.setConnectTimeout(4000);
				mgm.connect(5, 3, true);
				List<NdbFilterItem> theList = new ArrayList<NdbFilterItem>();
				NdbFilterItem theItem1 = new NdbFilterItem(15,NdbLogEventCategory.NDB_MGM_EVENT_CATEGORY_STATISTIC);
				NdbFilterItem theItem2 = new NdbFilterItem(15,NdbLogEventCategory.NDB_MGM_EVENT_CATEGORY_CONNECTION);
				NdbFilterItem theItem3 = new NdbFilterItem(15,NdbLogEventCategory.NDB_MGM_EVENT_CATEGORY_NODE_RESTART);

				theList.add(theItem1);
				theList.add(theItem2);
				theList.add(theItem3);


				NdbLogEventManager manager = mgm.createNdbLogEventManager(theList);
				TransReportListener theListener1 = new TransReportListener();
				ConnectedListener theListener2 = new ConnectedListener();
				DisconnectListener theListener3 = new DisconnectListener();
				NodeFailReportedListener theListener4 = new NodeFailReportedListener();
				CommunicationOpenedListener theListener5 = new CommunicationOpenedListener();
				CommunicationClosedListener theListener6 = new CommunicationClosedListener();
				StartPhaseListener theListener7 = new StartPhaseListener();

				NRCopyFragDoneListener theListener8 = new NRCopyFragDoneListener();

				System.out.println("Registering listener");
				manager.registerListener(theListener1);
				manager.registerListener(theListener2);
				manager.registerListener(theListener3);
				manager.registerListener(theListener4);
				manager.registerListener(theListener5);
				manager.registerListener(theListener6);
				manager.registerListener(theListener7);
				manager.registerListener(theListener8);
				System.out.println("Done");

				ClusterState cs = mgm.getStatus();
				for(int i=0; i< cs.getNoOfNodes(); i++) {
					NodeState ns = cs.getNodeState(i);

					NodeStatus theStatus = ns.getNodeStatus();
					String statusMsg = "";
					if (theStatus == NodeStatus.NDB_MGM_NODE_STATUS_STARTED) {
						statusMsg = " connected address " +  ns.getConnectAddress();
					} else { 
						statusMsg = " " + theStatus.toString();
					}
					NodeType type= ns.getNodeType();			
					System.out.println("Type: " + type.toString() + 
							" Version " 
							+ (short)(ns.getVersion()>>16 & 0xFF)
							+ "." 
							+ (short)(ns.getVersion()>>8 & 0xFF)
							+ "." 
							+ (short)(ns.getVersion()>>0 & 0xFF)
							+ statusMsg);

				}
				/**
				 * get events until the connection
				 * to the management server fails.
				 * If it does, then we have to 
				 * exit (break) the loop and reconnect.
				 */
				while(true) { 	
					try {
						manager.pollEvents(5000); 

					} catch(NdbMgmException e){
						// break and then reconnect
						break; 				    
					}
				}

			} catch(NdbMgmException e){
				System.out.println("Something happened");

			}
		}
	}
}

class TransReportListener extends TransReportCountersTypeListener {

	/* (non-Javadoc)
	 * @see com.mysql.cluster.mgmj.NdbLogEventCategoryListener#handleEvent(com.mysql.cluster.mgmj.NdbLogEvent)
	 */
	@Override
	public void handleEvent(TransReportCounters event) {
		System.out.println("Node " + event.getSourceNodeId() +
				": Listner Trans count:"+ event.getTransCount());

		System.out.println("Node " + event.getSourceNodeId() + 
				": Listner Read count:"+event.getReadCount());
		System.out.println("Node " + event.getSourceNodeId() + 
				": Listner Scan count:"+event.getScanCount());
		System.out.println("Node " + event.getSourceNodeId() + 
				": Listner Range Scan count:"
				+event.getRangeScanCount());
	} 


}



class DisconnectListener extends DisconnectedTypeListener {
	/* (non-Javadoc)
	 * @see com.mysql.cluster.mgmj.NdbLogEventCategoryListener#handleEvent(com.mysql.cluster.mgmj.NdbLogEvent)
	 */
	@Override
	public void handleEvent(Disconnected event) {
		System.out.println("Disconnected node:" + event.getNode());
	} 
}



class ConnectedListener extends ConnectedTypeListener {

	@Override
	public void handleEvent(Connected event) {
		System.out.println("Connected node:" + event.getNode());
	} 

}



class CommunicationOpenedListener extends CommunicationOpenedTypeListener {

	/* (non-Javadoc)
	 * @see com.mysql.cluster.mgmj.NdbLogEventCategoryListener#handleEvent(com.mysql.cluster.mgmj.NdbLogEvent)
	 */
	@Override
	public void handleEvent(CommunicationOpened event) {
		System.out.println("Comm opened to node:" + event.getNode() + " reported by " + event.getSourceNodeId());
	} 
}



class CommunicationClosedListener extends CommunicationClosedTypeListener {

	/* (non-Javadoc)
	 * @see com.mysql.cluster.mgmj.NdbLogEventCategoryListener#handleEvent(com.mysql.cluster.mgmj.NdbLogEvent)
	 */
	@Override
	public void handleEvent(CommunicationClosed event) {
		System.out.println("Comm closed to node:" + event.getNode());
	} 
}


class NodeFailReportedListener extends NodeFailReportedTypeListener {
	/* (non-Javadoc)
	 * @see com.mysql.cluster.mgmj.NdbLogEventCategoryListener#handleEvent(com.mysql.cluster.mgmj.NdbLogEvent)
	 */
	@Override
	public void handleEvent(NodeFailReported event) {
		System.out.println("Failed node:" + event.getFailedNode());
	} 
}


class NRCopyFragDoneListener extends NRCopyFragDoneTypeListener
{
	@Override
	public void handleEvent(NRCopyFragDone event) {
		System.out.println("Node " + event.getSourceNodeId() +
				": done copying table " + event.getTableId() +
				" of fragment: " + event.getFragmentId() + 
				" to node: " +  event.getDestNode());	
	}
}


class StartPhaseListener extends StartPhaseCompletedTypeListener {
	/* (non-Javadoc)
	 * @see com.mysql.cluster.mgmj.NdbLogEventCategoryListener#handleEvent(com.mysql.cluster.mgmj.NdbLogEvent)
	 */
	@Override
	public void handleEvent(StartPhaseCompleted event) {
		System.out.println("Node " + event.getSourceNodeId()  + 
				": start phase" + event.getPhase());
	} 
}
