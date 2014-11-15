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

import com.mysql.cluster.ndbj.NdbIndexScanOperation;
import com.mysql.cluster.ndbj.NdbOperation;
import com.mysql.cluster.ndbj.NdbOperation.AbortOption;
import com.mysql.cluster.ndbj.NdbOperation.LockMode;
import com.mysql.cluster.ndbj.NdbTransaction.ExecType;

import java.sql.SQLException;

public class NdbOperationByteLongTest extends BaseNdbjTestCase {
    private final static String tablename = "t_bytes";
    private final static String col1 = "id";
    private final static String col2 = "name";
    private final static String col3 = "num";

    public NdbOperationByteLongTest(String arg0) {
        super(arg0);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        createTable(tablename,
                    "(id VARBINARY(256) PRIMARY KEY, name VARBINARY(256),"
                    + "num BIGINT) ENGINE=ndb;");
    }

    /**
     * PK encoding for tests.
     */
    byte[] getPkID(int j) {
        byte[] pkID = new byte[5];
        pkID[0] = (byte) ((j >> 24) & 0x000000FF);
        pkID[1] = (byte) ((j >> 16) & 0x0000FF);
        pkID[2] = (byte) ((j >> 8) & 0x00FF);
        pkID[3] = (byte) (j & 0xFF);
        pkID[4] = (byte) -22; // 0xFF
        return pkID;
    }

    /**
     * Test byte array insert+read.
     */
    public void testInsertAndReadBytesAndLong() throws SQLException {

        NdbOperation op;
        NdbIndexScanOperation scanop;
        byte pkID1[] = getPkID(1);

        // insert some rows
        trans = ndb.startTransaction();

        for (int j = 0; j < 10; j++) {
            op = trans.getInsertOperation(tablename);

            op.equalBytes(col1, getPkID(j));
            op.setBytes(col2, "jim1".getBytes());
            op.setLong(col3, (long) j * 65536);
        }

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
        trans.close();

        // lookup the first row via pk and check the data
        trans = ndb.startTransaction();

        op = trans.getSelectOperation(tablename, LockMode.LM_Exclusive);

        op.equalBytes(col1, pkID1);
        op.getValue(col1);
        op.getValue(col2);
        op.getValue(col3);

        rs = op.resultData();

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);

        while(rs.next()) {
            rs.getBytes(col1);

            assertByteArrayEquals("jim1".getBytes(), rs.getBytes(col2));
            assertEquals(65536, rs.getLong(col3));
        }
        trans.close();

        // lookup the first row via index
        trans = ndb.startTransaction();
        scanop = trans.getSelectIndexScanOperation("PRIMARY",tablename,LockMode.LM_Exclusive);

        scanop.equalBytes(col1, pkID1);

        scanop.getValue(col1);
        scanop.getValue(col2);
        scanop.getValue(col3);

        rs = scanop.resultData();

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);

        int k = 1;
        while(rs.next()) {
            rs.getBytes(col1);
            long longVal = rs.getLong(col3);

            assertByteArrayEquals(("jim" + k).getBytes(), rs.getBytes(col2));
            assertEquals((long) k*65536, longVal);
            k++;
        }
        scanop.close();
        trans.close();
    }
}
