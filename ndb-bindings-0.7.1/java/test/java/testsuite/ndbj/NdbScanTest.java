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

package testsuite.ndbj;

import testsuite.BaseNdbjTestCase;

import com.mysql.cluster.ndbj.NdbOperation;
import com.mysql.cluster.ndbj.NdbScanOperation;

import com.mysql.cluster.ndbj.NdbOperation.AbortOption;
import com.mysql.cluster.ndbj.NdbOperation.LockMode;
import com.mysql.cluster.ndbj.NdbTransaction.ExecType;

import java.sql.SQLException;

/**
 *
 * Create this table in the 'test' database, in order to run this unit-test:
 *
 * GARAGE | CREATE TABLE `GARAGE` (
 *   `REG_NO` int(10) unsigned NOT NULL DEFAULT '0',
 *     `BRAND` char(20) NOT NULL DEFAULT '',
 *       `COLOR` char(20) NOT NULL DEFAULT ''
 *       ) ENGINE=ndbcluster DEFAULT CHARSET=latin1 PARTITION BY KEY ()
 *
 * LOCK TABLES `GARAGE` WRITE;
 * INSERT INTO `GARAGE` VALUES (0,'Mercedes\0\0\0\0\0\0\0\0\0\0\0\0','Blue\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0'),(13,'Toyota\0\0\0\0\0\0\0\0\0\0\0\0\0\0','Pink\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0'),(5,'BMW\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0','Black\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0'),(12,'Toyota\0\0\0\0\0\0\0\0\0\0\0\0\0\0','Pink\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0'),(1,'Mercedes\0\0\0\0\0\0\0\0\0\0\0\0','Blue\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0'),(9,'BMW\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0','Black\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0'),(8,'BMW\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0','Black\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0'),(2,'Mercedes\0\0\0\0\0\0\0\0\0\0\0\0','Blue\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0'),(3,'Mercedes\0\0\0\0\0\0\0\0\0\0\0\0','Blue\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0'),(10,'Toyota\0\0\0\0\0\0\0\0\0\0\0\0\0\0','Pink\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0'),(11,'Toyota\0\0\0\0\0\0\0\0\0\0\0\0\0\0','Pink\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0'),(4,'Mercedes\0\0\0\0\0\0\0\0\0\0\0\0','Blue\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0'),(14,'Toyota\0\0\0\0\0\0\0\0\0\0\0\0\0\0','Pink\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0'),(7,'BMW\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0','Black\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0'),(6,'BMW\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0','Black\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0');
 * UNLOCK TABLES;

 *
 */
public class NdbScanTest extends BaseNdbjTestCase {

    private static final int NUM_ROWS=10;
    private static final String tablename="t_scan_op";
    private static final String col1="REG_NO";
    private static final String col2="BRAND";
    private static final String col3="COLOR";
    private static final String col2Val="bmw";
    private static final String col3Val="blue";

    public NdbScanTest(String arg0) {
        super(arg0);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        createTable(tablename,
                    "(`REG_NO` int(10) unsigned NOT NULL DEFAULT '0' PRIMARY KEY,"
                    +"`BRAND` char(20) NOT NULL DEFAULT '',"
                    +"`COLOR` char(20) NOT NULL DEFAULT ''"
                    +") ENGINE=ndb;"
                    );
    }

    public final void testScan() throws SQLException {
        trans = ndb.startTransaction();

        for (int i=0;i<NUM_ROWS;i++)
        {
            NdbOperation op = trans.getInsertOperation(tablename);

            op.equalInt(col1,i);

            op.setString(col2,col2Val);
            op.setString(col3,col3Val);
        }
        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
        trans.close();

        trans = ndb.startTransaction();

        NdbScanOperation sop = trans.getSelectScanOperation(tablename,LockMode.LM_CommittedRead);

        sop.getValue(col2);
        sop.getValue(col3);

        rs = sop.resultData();

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);

        int rowCount=0;
        while(rs.next())
        {
            /*
             * getString(columnId)
             */
            String testVal = rs.getString(col2);
            assertEquals(testVal, col2Val);
            rowCount++;
        }
        sop.close();
        assertEquals(rowCount,NUM_ROWS);

        trans.close();
    }
}
