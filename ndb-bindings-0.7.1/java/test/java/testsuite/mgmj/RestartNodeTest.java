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


import testsuite.BaseMgmjTestCase;

import com.mysql.cluster.mgmj.NdbMgmException;

public class RestartNodeTest extends BaseMgmjTestCase {

    private static final int NODE_ID = 1;
    private static final int NODE_ID_TWO = 2;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(RestartNodeTest.class);
    }

    public RestartNodeTest(String arg0) {
        super(arg0);
    }

    /*
     * Test method for 'com.mysql.cluster.mgmj.NdbMgm.restart2(int, int[], int, int, int)'
     */
    public final void testRestartAndWaitNoInitial() {
        try {
            mgm.restart(new int[]{NODE_ID},false,true,false);
            System.out.println("Restarting ndb, and putting it in waiting state");
            assertTrue(true);
        }
        catch (NdbMgmException e) {
            System.out.println(e.toString());
            assertFalse(true);
        }

    }

    /*
     * Test method for 'com.mysql.cluster.mgmj.NdbMgm.start(int, int[])'
     */
    public final void testStart() {

        try {
            wait(3000);
        }
        catch (InterruptedException e) {
            System.out.println("Awakend prematurely");
        }
        catch (IllegalMonitorStateException e) {
            System.out.println("current thread is not owner of the object's monitor");
        }

        try {
            mgm.start(new int[]{NODE_ID});
            assertTrue(true);
            System.out.println("Moving ndb from waiting state to started state");
        }
        catch (NdbMgmException e) {
            System.out.println(e.toString());
            assertFalse(true);

        }

    }

    /*
     * Test method for 'com.mysql.cluster.mgmj.NdbMgm.restart(int, int[])'
     */
    public final void testRestart() {
        try {
            mgm.restart(new int[]{NODE_ID_TWO});
            assertTrue(true);
            System.out.println("Restarting ndb fully");
        }
        catch (NdbMgmException e) {
            System.out.println(e.toString());
            assertFalse(true);
        }

    }


}
