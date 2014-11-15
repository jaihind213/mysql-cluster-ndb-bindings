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

import java.util.ArrayList;
import java.util.List;

import testsuite.BaseMgmjTestCase;
import testsuite.mgmj.NdbStopCompletedListener;

import com.mysql.cluster.mgmj.NdbFilterItem;
import com.mysql.cluster.mgmj.NdbLogEventCategory;
import com.mysql.cluster.mgmj.NdbLogEventManager;
import com.mysql.cluster.mgmj.NdbMgmException;

/**
 *
 * @version 1.0
 * @since	1.0
 */


public class EventLogListenerTest extends BaseMgmjTestCase {

    private static final int TIMEOUT_MS=100;
    private static final int NUM_ITERATIONS=100;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(EventLogListenerTest.class);
    }

    public EventLogListenerTest(String arg0) {
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
            NdbStopCompletedListener restartEvt = new NdbStopCompletedListener();
            evLogMgr = mgm.createNdbLogEventManager(theList);
            NodeFailedListener nodeFail = new NodeFailedListener();
            evLogMgr.registerListener(restartEvt);
            evLogMgr.registerListener(nodeFail);
            for (int i=0;i<NUM_ITERATIONS;i++) {
                evLogMgr.pollEvents(TIMEOUT_MS);
            }
            evLogMgr.unregisterListener(restartEvt);
            evLogMgr.unregisterListener(nodeFail);
            System.out.println("Main Test thread exited");
            assertTrue(true);
        }
        catch (NdbMgmException e) {
            assertFalse(true);
        }

    }

}
