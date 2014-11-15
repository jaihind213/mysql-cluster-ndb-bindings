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

package testsuite;


import com.mysql.cluster.mgmj.NdbMgmException;
import com.mysql.cluster.mgmj.NdbMgmFactory;
import com.mysql.cluster.mgmj.NdbMgm;

import junit.framework.TestCase;

public class BaseMgmjTestCase extends TestCase {

    protected static final String CONFIG_FILENAME="ndbj.props";
    protected static String NDB_MGMD_CONNECTSTRING="";
    protected static String DB_NAME="test";
    protected static String TABLE_NAME="";
    protected static String LIB_PATH = "";
    protected static String USERNAME  = "root";
    protected static String PASSWORD = "";
    protected static String CLUSTER_LOG="";

    protected static NdbMgm mgm=null;

    static {
        System.loadLibrary("ndbj");
    }

    public BaseMgmjTestCase(String arg0) {
        super(arg0);

    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        /**
         *  Connect to cluster, done once for each Application
         */

        try {
            mgm = NdbMgmFactory.createNdbMgm(NDB_MGMD_CONNECTSTRING);
            mgm.connect(0,0,true);
        }
        catch (NdbMgmException e) {
            System.err.println("failed initialisation");
            assertFalse(true);
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mgm=null;
    }

}
