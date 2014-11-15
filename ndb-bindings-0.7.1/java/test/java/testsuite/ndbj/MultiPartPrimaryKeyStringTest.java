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

import com.mysql.cluster.ndbj.NdbApiException;
import com.mysql.cluster.ndbj.NdbIndexScanOperation;
import com.mysql.cluster.ndbj.NdbOperation;
import com.mysql.cluster.ndbj.NdbOperation.AbortOption;
import com.mysql.cluster.ndbj.NdbOperation.LockMode;
import com.mysql.cluster.ndbj.NdbTransaction.ExecType;
import com.mysql.cluster.ndbj.NdbScanFilter;
import com.mysql.cluster.ndbj.NdbScanOperation;
import com.mysql.cluster.ndbj.NdbDictionary;
import com.mysql.cluster.ndbj.NdbTable;

import java.sql.SQLException;

public class MultiPartPrimaryKeyStringTest extends BaseNdbjTestCase {
    private static final String tablename = "t_multipart_primary";
    private static final String col1Val = "jd";
    private static final String col2Val = "jp";
    private static final String col3Val = "jim";

    private static final int NUM_ITERATIONS = 500;
    private static final String col1 = "a";
    private static final String col2 = "b";
    private static final String col3 = "c";

    public MultiPartPrimaryKeyStringTest(String arg0) {
        super(arg0);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        createTable(tablename,
                    "(`" + col1 + "` VARCHAR(256) NOT NULL, `"
                    + col2 + "` VARCHAR(256) NOT NULL, `"
                    + col3 + "` VARBINARY(256), PRIMARY KEY(a,b)) ENGINE=ndb"
                    + " PARTITION BY KEY(a,b);");
    }

    public void testMultiPartPrimaryKey() throws NdbApiException, SQLException {
        NdbIndexScanOperation idxscanop;
        int numRes;

        ////////////////////////////////////////////////
        // insert many rows
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            String pk1, pk2;

            if (i > 0) {
                pk1 = col1Val + i;
                pk2 = col2Val + i;
            }
            else {
                pk1 = col1Val;
                pk2 = col2Val;
            }

            //TODO: Actually support sending multiple args to TC hint
            trans = ndb.startTransaction();
            NdbOperation op = trans.getInsertOperation(tablename);

            op.equalString(col1, pk1);
            op.equalString(col2, pk2);

            String col3Bytes = col3Val + i;
            op.setBytes(col3, col3Bytes.getBytes());

            trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
            trans.close();
        }

        ////////////////////////////////////////////////
        // lookup the rows by composite pk
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            // distribution hint is value of both columns in primary key
            // TODO: Do we need to actually support a (TableName,Val1,Val2) form here?
            trans = ndb.startTransaction();

            NdbOperation op = trans.getSelectOperation(tablename,
                                                       LockMode.LM_Exclusive);

            if (i > 0) {
                op.equalString(col1, col1Val + i);
                op.equalString(col2, col2Val + i);
            } else {
                op.equalString(col1, col1Val);
                op.equalString(col2, col2Val);
            }
            op.getValue(col3);

            rs = op.resultData();

            trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);

            // check the values
            assertEquals(true, rs.next());

            assertByteArrayEquals((col3Val + i).getBytes(), rs.getBytes(col3));

            assertEquals(false, rs.next());
            trans.close();
        }

        ////////////////////////////////////////////////
        // read the first row with a scan filter
        trans = ndb.startTransaction();

        NdbDictionary theDict = ndb.getDictionary();
        NdbTable theTable = theDict.getTable(tablename);
        NdbScanOperation scanop = trans.getSelectScanOperation(theTable,LockMode.LM_CommittedRead);

        scanop.getValue(col1);
        scanop.getValue(col2);
        scanop.getValue(col3);

        NdbScanFilter sf = scanop.getNdbScanFilter();

        sf.begin();
        // TODO: Fix naming issue
        // only true for the first row
        sf.cmpString(NdbScanFilter.BinaryCondition.COND_EQ, col1, col1Val);
        sf.end();

        rs = scanop.resultData();

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);

        numRes = 0;
        while(rs.next())
        {
            String c1 = rs.getString(col1);
            assertFalse(rs.wasNull());
            String c2 = rs.getString(col2);
            assertFalse(rs.wasNull());
            assertEquals(col1Val, c1);
            assertEquals(col2Val, c2);
            assertByteArrayEquals((col3Val + numRes).getBytes(), rs.getBytes(col3));
            assertFalse(rs.wasNull());
            numRes++;
        }
        assertEquals(numRes, 1);
        trans.close();

        ////////////////////////////////////////////////
        // again, read the first row with the composite key via the PK index
        trans = ndb.startTransaction();

        idxscanop = trans.getSelectIndexScanOperation("PRIMARY", tablename, LockMode.LM_Exclusive);

        idxscanop.equalString(col1, col1Val);
        idxscanop.equalString(col2, col2Val);
        idxscanop.getValue(col3);

        rs = idxscanop.resultData();

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);

        numRes = 0;
        while(rs.next())
        {
            assertByteArrayEquals((col3Val + numRes).getBytes(), rs.getBytes(col3));
            numRes++;
        }
        assertEquals(numRes, 1);
        trans.close();

        ////////////////////////////////////////////////
        // scan the index, but skip the first row
        trans = ndb.startTransaction();

        idxscanop = trans.getSelectIndexScanOperation("PRIMARY", tablename, LockMode.LM_Exclusive);

        idxscanop.whereGreaterThanEqualTo(col1, col1Val);
        idxscanop.whereGreaterThan(col2, col2Val);
        idxscanop.getValue(col3);

        rs = idxscanop.resultData();

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);

        numRes = 0;
        while(rs.next())
        {
            rs.getBytes(col3);
            /*byte col3Bytes[] = (col3Val + (numRes + 1)).getBytes();*/
            // TODO assert values here? dependent on index ordering
            numRes++;
        }
        assertEquals(numRes, NUM_ITERATIONS - 1);
        trans.close();
    }
}
