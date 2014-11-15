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
import com.mysql.cluster.mgmj.NdbMgmFactory;
import com.mysql.cluster.mgmj.NdbMgm;

public class MgmNodeFailoverTest extends BaseMgmjTestCase {

    public MgmNodeFailoverTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(MgmNodeFailoverTest.class);
    }


    /*
     * Test method for 'com.mysql.cluster.mgmj.NdbMgmFactory.recreateNdbMgm(int)'
     */
    public void testRecreateNdbMgm() {

        // I start the ndb_mgmd running on two ports on localhost
        /**
         * #!/bin/bash
         * export PATH=$PATH:/usr/local/mysql/bin
         * ndb_mgmd -c "nodeid=1;host=localhost" -f /var/lib/mysql-cluster/config.ini
         * ndb_mgmd -c "nodeid=2;host=localhost:1187" -f /var/lib/mysql-cluster/config.ini
         * ndbd -c "localhost;localhost:1187"
         * ndbd -c "localhost;localhost:1187"
         * /etc/init.d/mysql.server start
         */
        String connectString="localhost:26097;localhost;localhost:26098";
        int hostNo=2;
        NdbMgm mgmt=null;
        try {
            boolean isConnected=false;
            mgmt = NdbMgmFactory.createNdbMgm(connectString);
            try {
                mgmt.connect(0,0,true);
            }
            catch (NdbMgmException e) {
                System.out.println("Could not connect to mgm host id="+hostNo);
            }
            isConnected = mgmt.isConnected();
            System.out.println("Connected to managment server, id="+
                               hostNo + ". IsConnected="
                               + isConnected);
        }
        catch (NdbMgmException e) {
            System.out.println("Problem with mgmt object");
            assertFalse(true);
        }

        /**
         * Reconnect to ndb_mgmd by calling
         */
        try {
            boolean isConnected=false;
            if (mgmt == null)
                throw new NdbMgmException("Null mgmt object");
            mgmt.connect(1,10,false);
            isConnected = mgmt.isConnected();
            System.out.println("Re-connecting to managment server, id="+
                               hostNo + ". IsConnected="
                               + isConnected);
            assertEquals(isConnected,true);
        }
        catch (NdbMgmException e) {
            System.out.println("Problem with mgmt object");
            assertFalse(true);
        }
        finally {
            if (mgmt != null) {
                mgmt=null;
            }
        }
    }

    public void testNdbErrorMsg() {

        String connectString="localhost:26097;localhost;localhost:26098";
        NdbMgm mgmt=null;
        try {
            mgmt = NdbMgmFactory.createNdbMgm(connectString);
            mgmt.connect(0,0,true);
            int ids[] = new int[1];
            // try and stop a ndbd with ID=25
            ids[0]=25;
            mgmt.stop(ids);
            assertFalse(true);
        }
        catch (NdbMgmException e) {
            System.out.println("Expected Error!");
            System.out.println(e.getMessage());
            assertTrue(true);

        }

    }

}
