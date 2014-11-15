/* -*- mode: java; c-basic-offset: 4; indent-tabs-mode: nil; -*-
 *  vim:expandtab:shiftwidth=4:tabstop=4:smarttab:
 *
 *  ndb-bindings: Bindings for the NDB API
 *  Copyright (C) 2008 MySQL
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package testsuite.mgmj;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import testsuite.BaseMgmjTestCase;
import testsuite.mgmj.NdbStopCompletedListener;

import com.mysql.cluster.mgmj.ClusterState;
import com.mysql.cluster.mgmj.NodeType;
import com.mysql.cluster.mgmj.NdbFilterItem;
import com.mysql.cluster.mgmj.NdbLogEventCategory;
import com.mysql.cluster.mgmj.NdbLogEventManager;
import com.mysql.cluster.mgmj.NdbMgmException;
import com.mysql.cluster.mgmj.NodeState;

/**
 *
 * @version 1.0
 * @since	1.0
 */


public class EventMemoryListenerTest extends BaseMgmjTestCase {


    public EventMemoryListenerTest(String arg0) {
        super(arg0);
    }

    /*
     * Test method for 'com.mysql.mgmapi.NdbMgm.createLogEvent(int[])'
     */
    public void testCreateLogEvent() {

        List<NdbFilterItem> theList = new ArrayList<NdbFilterItem>();

        NdbLogEventCategory[] categories = {
            NdbLogEventCategory.NDB_MGM_EVENT_CATEGORY_BACKUP,
            NdbLogEventCategory.NDB_MGM_EVENT_CATEGORY_CHECKPOINT,
            NdbLogEventCategory.NDB_MGM_EVENT_CATEGORY_CONGESTION,
            NdbLogEventCategory.NDB_MGM_EVENT_CATEGORY_CONNECTION,
            NdbLogEventCategory.NDB_MGM_EVENT_CATEGORY_ERROR,
            NdbLogEventCategory.NDB_MGM_EVENT_CATEGORY_INFO,
            NdbLogEventCategory.NDB_MGM_EVENT_CATEGORY_NODE_RESTART,
            NdbLogEventCategory.NDB_MGM_EVENT_CATEGORY_SHUTDOWN,
            NdbLogEventCategory.NDB_MGM_EVENT_CATEGORY_STARTUP,
            NdbLogEventCategory.NDB_MGM_EVENT_CATEGORY_STATISTIC,
        };
        for (NdbLogEventCategory cat : categories) {
            NdbFilterItem theItem = new NdbFilterItem(15,cat);
            theList.add(theItem);
        }
        NdbLogEventManager evLogMgr;



        try {
            NdbStopCompletedListener restartListener = new NdbStopCompletedListener();
            NdbStopStartedListener stopStartedListener = new NdbStopStartedListener();
            NdbStartStartedListener startStartedListener = new NdbStartStartedListener();
            NodeFailedListener nodeFail = new NodeFailedListener();
            MemoryListener memEvt = new MemoryListener();
            TransactionReportListener transEvt = new TransactionReportListener();
            OperationsReportListener opEvt = new OperationsReportListener();
            evLogMgr = mgm.createNdbLogEventManager(theList);

            evLogMgr.registerListener(restartListener);
            evLogMgr.registerListener(stopStartedListener);
            evLogMgr.registerListener(startStartedListener);
            evLogMgr.registerListener(nodeFail);
            evLogMgr.registerListener(memEvt);
            evLogMgr.registerListener(transEvt);
            evLogMgr.registerListener(opEvt);

            SimpleDateFormat fSDateFormat = null;
            fSDateFormat =
                new SimpleDateFormat ("d MMM yyyy HH:mm:ss");

            ClusterState cs;
            Collection<NodeState> ns;
            try {
                cs= mgm.getStatus();
                ns = cs.getNodeStates();

                for (NodeState n : ns) {
                    if ( n.getNodeType() == NodeType.NDB_MGM_NODE_TYPE_NDB) {
                        mgm.dumpState(n.getNodeID(), 1000);
                    }
                }

            }
            catch (NdbMgmException e) {
                System.out.println("Exception when dumping memory: " + e.getMessage());
                assertFalse(true);
            }
            long old_t = System.currentTimeMillis();
            long new_t;
            long counter=0;
            for (int i=0; i < 1000; i++) {

                evLogMgr.pollEvents(10000);
                Date now = new Date ();
                String date_out = fSDateFormat.format (now);
                new_t = System.currentTimeMillis();
                counter += new_t - old_t;
                System.out.println(date_out
                                   + " [MgmServer] INFO\t Node "
                                   + " ( Num: " + i
                                   + ", time = "
                                   + counter  + ")" + ", since last: "
                                   + (new_t - old_t));
                old_t = new_t;
            }

            evLogMgr.unregisterListener(restartListener);
            evLogMgr.unregisterListener(stopStartedListener);
            evLogMgr.unregisterListener(startStartedListener);
            evLogMgr.unregisterListener(nodeFail);
            evLogMgr.unregisterListener(memEvt);
            evLogMgr.unregisterListener(transEvt);
            evLogMgr.unregisterListener(opEvt);
            assertTrue(true);
        }
        catch (NdbMgmException e) {
            System.out.println("Exception: " + e.toString());
            assertFalse(true);
        }

    }

}
