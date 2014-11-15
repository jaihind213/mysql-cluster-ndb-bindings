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
import com.mysql.cluster.mgmj.NdbFilterItem;
import com.mysql.cluster.mgmj.NdbLogEventCategory;
import com.mysql.cluster.mgmj.NdbLogEventManager;
import com.mysql.cluster.mgmj.NdbMgmException;

/**
 *
 * @version 1.0
 * @since	1.0
 */


public class EventTransactionCountsTest extends BaseMgmjTestCase {

    private static final int TIMEOUT_MS=100;
    private static final int NUM_ITERATIONS=100;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(EventTransactionCountsTest.class);
    }

    public EventTransactionCountsTest(String arg0) {
        super(arg0);
    }

    /*
     * Test method for 'com.mysql.mgmapi.NdbMgm.createLogEvent(int[])'
     */
    public void testCreateLogEvent() {

        List<NdbFilterItem> theList = new ArrayList<NdbFilterItem>();

        theList.add(new NdbFilterItem(1,NdbLogEventCategory.NDB_MGM_EVENT_CATEGORY_CHECKPOINT));
        theList.add(new NdbFilterItem(15,NdbLogEventCategory.NDB_MGM_EVENT_CATEGORY_NODE_RESTART));
        theList.add(new NdbFilterItem(8,NdbLogEventCategory.NDB_MGM_EVENT_CATEGORY_STATISTIC));

        NdbLogEventManager evLogMgr;


        try {
            TransactionReportListener transEvt = new TransactionReportListener();
            OperationsReportListener opEvt = new OperationsReportListener();
            evLogMgr = mgm.createNdbLogEventManager(theList);

            evLogMgr.registerListener(transEvt);
            evLogMgr.registerListener(opEvt);
            long old_t = System.currentTimeMillis();
            long new_t;
            long counter=0;
            for (int i=0; i < NUM_ITERATIONS; i++) {
                evLogMgr.pollEvents(TIMEOUT_MS);
                new_t = System.currentTimeMillis();
                counter += new_t - old_t;
                System.out.println("Event : " + i + " (" + counter  + ")" + ", since last: " + (new_t - old_t));
                old_t = new_t;
            }

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
