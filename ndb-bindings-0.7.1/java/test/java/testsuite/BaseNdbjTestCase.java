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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.cluster.ndbj.NdbClusterConnection;
import com.mysql.cluster.ndbj.NdbDictionary;
import com.mysql.cluster.ndbj.NdbTransaction;

public class BaseNdbjTestCase extends testsuite.BaseTestCase {

    protected static final String CONFIG_FILENAME="ndbj.props";
    protected static String MYSQLD_HOST="";
    protected static String NDB_MGMD_CONNECTSTRING="";
    protected static String DB_NAME="test";
    protected static String TABLE_NAME="";
    protected static String LIB_PATH = "";
    protected static String USERNAME  = "root";
    protected static String PASSWORD = "";
    protected static String CLUSTER_LOG="";
    protected static Connection MYSQL_CONN=null;

    protected static NdbClusterConnection ndbConn=null;
    protected com.mysql.cluster.ndbj.Ndb ndb=null;
    protected NdbTransaction trans=null;

    public BaseNdbjTestCase(String arg0) {

        super(arg0);
        /**
         * Set environment variables
         */

        File f =null;
        try {
            InputStream in=null;
            f = new File(CONFIG_FILENAME);
            if (f.exists()){
                if (f.canRead()){
                    in = new FileInputStream(f);
                }
                else {
                    System.out.println("Could not open " + CONFIG_FILENAME);
                    assertFalse(true);
                }
            }
            else {
                System.out.println("Could not find " + CONFIG_FILENAME);
                assertFalse(true);
            }

            Properties p = new Properties(System.getProperties());
            p.load(in);

            // set the system properties
            System.setProperties(p);

            BaseNdbjTestCase.MYSQLD_HOST = System.getProperty("ndbj.mysqld");
            if (BaseNdbjTestCase.MYSQLD_HOST == null) {
                System.out.println("ndbj.mysql not defined in configuration file: + " + CONFIG_FILENAME);
                assertFalse(true);
            }

            BaseNdbjTestCase.NDB_MGMD_CONNECTSTRING= System.getProperty("ndbj.connectstring");
            if (BaseNdbjTestCase.NDB_MGMD_CONNECTSTRING == null) {
                System.out.println("ndbj.connectstring not defined in configuration file: + " + CONFIG_FILENAME);
                assertFalse(true);
            }

            BaseNdbjTestCase.DB_NAME= System.getProperty("ndbj.testDatabase");
            if (BaseNdbjTestCase.DB_NAME == null) {
                System.out.println("ndbj.testDatabase was empty. Setting it to 'test' db.");
                BaseNdbjTestCase.DB_NAME = "test";
            }

            BaseNdbjTestCase.LIB_PATH= System.getProperty("ndbj.libpath");
            if (BaseNdbjTestCase.LIB_PATH == null) {
            }

            BaseNdbjTestCase.USERNAME = System.getProperty("ndbj.username");
            if (BaseNdbjTestCase.USERNAME == null) {
                System.out.println("ndbj.username was empty. Setting it to 'root'.");
            }

            BaseNdbjTestCase.PASSWORD = System.getProperty("ndbj.password");
            if (BaseNdbjTestCase.PASSWORD == null) {
                System.out.println("ndbj.password was empty. Setting it to empty string ''");
            }

            BaseNdbjTestCase.CLUSTER_LOG= System.getProperty("ndbj.clusterLog");
            if (BaseNdbjTestCase.CLUSTER_LOG== null) {
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setUp() throws Exception {
        /**
         *  Connect to cluster, done once for each Application
         */
        super.setUp();
        //System.out.println("Running test: "+this.getClass().getName());

        //System.out.println("Connecting to cluster...");
        if (ndbConn==null) {
            ndbConn = NdbClusterConnection.create(NDB_MGMD_CONNECTSTRING);

            ndbConn.connect(5,3,true);

            ndbConn.waitUntilReady(30,0);
        }
        /**
         *  Get a connection to NDB (once / thread)
         */
        ndb = ndbConn.createNdb("test",1024);

    }

    @Override
    public void tearDown() throws Exception {


        if (trans != null) {
            //trans.close();
            trans=null;
        }

        /* We don't want to redo this every time - cluster doesn't really
         * support that idea
         ndb.close();
         ndb=null;
         ndbConn.close();
         ndbConn = null;
        */

        super.tearDown();
    }

    /* (non-Javadoc)
     * @see testsuite.BaseTestCase#createTable(java.lang.String,
     *      java.lang.String)
     */
    @Override
    protected void createTable(String tableName, String columnsAndOtherStuff)
        throws SQLException {

        super.dropTable(tableName);
        super.createTable(tableName, columnsAndOtherStuff);
        NdbDictionary theDict = ndb.getDictionary();
        theDict.invalidateTable(tableName);
    }


    /* (non-Javadoc)
     * Doesn't use invalidateTable.
     * Really only for the Invalid Schema Object test.
     * TODO: Clarification on why?
     */
    protected void recreateTable(String tableName, String columnsAndOtherStuff)
        throws SQLException {
        super.createTable(tableName, columnsAndOtherStuff);
    }

    /**
     * Assert byte arrays are equal.
     */
    protected void assertByteArrayEquals(byte exp[], byte actual[]) {
        assertEquals("byte array length", exp.length, actual.length);
        for(int i = 0; i < exp.length; ++i)
            assertEquals("byte " + i, exp[i], actual[i]);
    }

    /**
     * Assert byte arrays are equal with length.
     */
    protected void assertByteArrayEquals(byte exp[],
                                         byte actual[], int length) {
        for(int i = 0; i < length; ++i)
            assertEquals("byte " + i, exp[i], actual[i]);
    }
}
