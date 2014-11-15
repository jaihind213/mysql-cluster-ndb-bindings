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

import java.util.Collection;

import testsuite.BaseMgmjTestCase;

import com.mysql.cluster.mgmj.ClusterState;
import com.mysql.cluster.mgmj.NodeState;

public class ClusterAndNodeStateTest extends BaseMgmjTestCase {


    public ClusterAndNodeStateTest(String arg0) {
        super(arg0);
    }


    /*
     * Test method for 'com.mysql.cluster.mgmj.NdbMgm.getClusterState()'
     */
    public void testGetClusterState() {
        ClusterState cs;
        Collection<NodeState> ns;
        try {
            cs= mgm.getStatus();
            ns = cs.getNodeStates();

            // version is used to test if a node is connected or not
            for (NodeState n : ns) {
                System.out.println(
                                   "ID="+n.getNodeID()
                                   +",addr="+n.getConnectAddress()
                                   +", type="+n.getNodeType()
                                   +", nodeGroup="+n.getNodeGroup()
                                   +", status="+n.getNodeStatus()
                                   +", startPhase="+n.getStartPhase()
                                   +", dynamicNodeID="+n.getDynamicID()
                                   +", version="+n.getVersion()
                                   +", connectCount="+n.getConnectCount()
                                   );
            }
            assertTrue(true);
        }
        catch (Exception e) {
            assertFalse(true);
        }

    }

}
