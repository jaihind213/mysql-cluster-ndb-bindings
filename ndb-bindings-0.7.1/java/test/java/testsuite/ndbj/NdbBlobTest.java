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

import com.mysql.cluster.ndbj.NdbBlob;
import com.mysql.cluster.ndbj.NdbIndexScanOperation;
import com.mysql.cluster.ndbj.NdbOperation;
import com.mysql.cluster.ndbj.NdbOperation.AbortOption;
import com.mysql.cluster.ndbj.NdbOperation.LockMode;
import com.mysql.cluster.ndbj.NdbScanOperation.ScanFlag;
import com.mysql.cluster.ndbj.NdbTransaction.ExecType;

import java.sql.SQLException;

public class NdbBlobTest extends BaseNdbjTestCase {

    private static final String tablename = "t_blob";
    private static final String col1 = "a";
    private static final String col2 = "b";

    public NdbBlobTest(String arg0) {
        super(arg0);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        createTable(tablename,
                    "(`" + col1 + "` INT PRIMARY KEY, `"
                    + col2 + "` BLOB) ENGINE=ndb;");
    }

    /**
     * Various blob testing.
     */
    public final void testBlob() throws SQLException {
        NdbBlob b;
        NdbOperation op;
        NdbIndexScanOperation scanop;
        int len;
        byte buf[];
        String input[] = {"ROB...", "ROB...ROB...", "ROB...ROB...ROB..."};

        ////////////////////////////////////////////
        // begin by inserting some rows with blobs
        for (int i = 0; i < 3; i++) {
            trans = ndb.startTransaction();
            op = trans.getInsertOperation(tablename);
            op.equalInt(col1, i);
            b = op.getBlobHandle(col2);
            if (i == 0)
                b.setNull();
            else
                b.setValue(input[i].getBytes());

            trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);

            trans.close();
        }

        ////////////////////////////////////////////
        // scan the index and check the blob values, knowing blob length
        trans = ndb.startTransaction();
        scanop = trans.getSelectIndexScanOperation("PRIMARY", tablename, LockMode.LM_Exclusive);

        scanop.setBoundInt(col1, NdbIndexScanOperation.BoundType.BoundLE, 0);
        scanop.getBlob(col2, 8000); // <--- Knowledge of Blob Length
        scanop.getValue(col1);

        rs = scanop.resultData();

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);

        while (rs.next()) {
            int id = rs.getInt(col1);
            b = (NdbBlob)rs.getBlob(col2);
            if (id == 0) {
                assertEquals(b.getNull(), true);
                continue;
            }

            if (!b.getNull())
                // TODO: Fix BLOB getLength()
                assertByteArrayEquals(input[id].getBytes(), b.getData());
        }
        trans.close();

        ////////////////////////////////////////////
        // check a single value via pk, not knowing length
        trans = ndb.startTransaction();

        op = trans.getSelectOperation(tablename);

        op.equalInt(col1, 1);
        op.getBlob(col2);

        rs = op.resultData();
        b = (NdbBlob)rs.getBlob(col2);

        trans.execute(ExecType.NoCommit,
                      AbortOption.AbortOnError, true);

        // TODO: Fix BLOB getLength()
        len = b.getLength().intValue();
        buf = new byte[len];
        b.readData(buf, len);
        assertByteArrayEquals(input[1].getBytes(), buf);

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
        trans.close();

        ////////////////////////////////////////////
        // check a single value via pk, knowing blob length
        trans = ndb.startTransaction();

        op = trans.getSelectOperation(tablename);

        op.equalInt(col1, 1);
        op.getBlob(col2, 8000); // <--- Knowledge of Blob Length

        rs = op.resultData();
        b = (NdbBlob)rs.getBlob(col2);

        trans.execute(ExecType.NoCommit, AbortOption.AbortOnError, true);

        assertFalse(b.getNull());
        buf = b.getData();
        // TODO: Fix BLOB getLength()
        /*for (int i = 0; i < b.getLength().intValue(); i++)
          System.out.print((char) buf[i]); */
        assertByteArrayEquals(input[1].getBytes(), buf);

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
        trans.close();

        ////////////////////////////////////////////
        // get blob handle via pk, not knowing length
        trans = ndb.startTransaction();
        op = trans.getSelectOperation(tablename);
        op.equalInt(1, 1);

        b = op.getBlobHandle(col2);

        trans.execute(ExecType.NoCommit, AbortOption.AbortOnError, true);

        // TODO: Fix BLOB getLength()
        len = b.getLength().intValue();

        buf = new byte[len];
        b.readData(buf, len);
        assertByteArrayEquals(input[1].getBytes(), buf);
        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);

        trans.close();

        ////////////////////////////////////////////
        // get blob handle via pk, knowing blob length
        trans = ndb.startTransaction();

        op = trans.getSelectOperation(tablename);

        op.equalInt(1, 1);
        b = op.getBlobHandle(col2);

        len = 8000; // <--- knowledge!!!

        b.getValue(len);

        trans.execute(ExecType.NoCommit, AbortOption.AbortOnError, true);

        assertFalse(b.getNull());
        buf = b.getData();
        // TODO: Fix BLOB getLength()
        assertEquals(8000, buf.length);
        // only test the first known bytes of the data
        assertEquals(input[1], new String(buf, 0, input[1].length()));

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
        trans.close();

        ////////////////////////////////////////////
        // update blob via pk
        trans = ndb.startTransaction();
        op = trans.getUpdateOperation(tablename);
        op.equalInt(col1, 2);

        b = op.getBlobHandle(col2);

        byte bb2[] = "frazer clement45".getBytes();

        b.setValue(bb2);

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
        trans.close();

        ////////////////////////////////////////////
        // insert another row with a blob
        trans = ndb.startTransaction();
        op = trans.getInsertOperation(tablename);
        op.equalInt(col1, 9);
        b = op.getBlobHandle(col2);
        byte bb[] = "frazer clement2".getBytes();
        b.setValue(bb);

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
        trans.close();

        // assert this was entered
        assertEquals(new Long(1), (Long)getSingleValueWithQuery("select count(*) from " + tablename + " where " +
                                                                col2 + " = '" + new String(bb) + "'"));

        ////////////////////////////////////////////
        // delete the previously inserted row
        trans = ndb.startTransaction();
        op = trans.getDeleteOperation(tablename);
        op.equalInt(col1, 9);

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
        trans.close();

        ////////////////////////////////////////////
        // delete all rows via scan
        trans = ndb.startTransaction();
        scanop = trans.getSelectIndexScanOperation( "PRIMARY", tablename, LockMode.LM_Exclusive, 
        		                                    ScanFlag.ORDER_BY, 10, 10);

        scanop.setBoundInt(col1, NdbIndexScanOperation.BoundType.BoundLE, 1);
        scanop.getValue(col1);

        rs = scanop.resultData();

        trans.execute(ExecType.NoCommit, AbortOption.AbortOnError, true);

        while (rs.next()) {
            int scanCheck;
            do {
                int id = rs.getInt(col1);
                rs.deleteRow();
            } while ((scanCheck = scanop.nextResult(false)) == 0);

            // commit the currently deleted rows
            if (scanCheck == 2 || scanCheck == 1)
                trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
        }
        trans.close();
    }
}
