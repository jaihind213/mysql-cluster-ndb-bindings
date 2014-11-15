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

import java.sql.Timestamp;

import testsuite.BaseNdbjTestCase;

import com.mysql.cluster.ndbj.NdbApiException;
import com.mysql.cluster.ndbj.NdbOperation;
import com.mysql.cluster.ndbj.NdbOperation.AbortOption;
import com.mysql.cluster.ndbj.NdbOperation.LockMode;
import com.mysql.cluster.ndbj.NdbTransaction.ExecType;


import java.sql.SQLException;

public class NdbOperationIDTest extends BaseNdbjTestCase {
    private static final String col1  = "int_col";
    private static final String col2  = "varcharlong_col";
    private static final String col3  = "float_col";
    private static final String col4  = "double_col";
    private static final String col5  = "long_col";
    private static final String col6  = "char_col";
    private static final String col7  = "varchar_col";
    private static final String col8  = "varbinary_col";
    private static final String col9  = "varbinarylong_col";
    private static final String col10 = "timestamp_col";
    private static final String col11 = "binary_col";
    private static final String col12 = "short_col";

    private static final int col1ID  = 1;
    private static final int col2ID  = 2;
    private static final int col3ID  = 3;
    private static final int col4ID  = 4;
    private static final int col5ID  = 5;
    private static final int col6ID  = 6;
    private static final int col7ID  = 7;
    private static final int col8ID  = 8;
    private static final int col9ID  = 9;
    private static final int col10ID = 10;
    private static final int col11ID = 11;
    private static final int col12ID = 12;

    protected int 	int_val = 100;
    protected String varcharlong_val="Evelyn";
    // 240+ chars causes crash
    protected String varchar_val= "Paddy";//"0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";
    protected byte[] varbinary_val="Maeve".getBytes(); // "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789".getBytes();
    protected byte[] varbinarylong_val="Aoife".getBytes();
    protected byte[] binary_val="Rob".getBytes();
    protected String char_val="Linda";
    protected float float_val = 1.11F;
    protected double double_val = 2.22D;
    protected long long_val = 1233456789L;
    protected Timestamp timestamp_val = new Timestamp(System.currentTimeMillis());
    protected short short_val = 1974;

    protected final static String tablename = "t_ops";

    public NdbOperationIDTest(String arg0) {
        super(arg0);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        createTable(tablename, "(`"
                    + col1 + "` INT not null PRIMARY KEY, `"
                    + col2 + "` VARCHAR(256), `"
                    + col3 + "` FLOAT, `"
                    + col4 + "` DOUBLE, `"
                    + col5 + "` BIGINT, `"
                    + col6 + "` CHAR(20), `"
                    + col7 + "` VARCHAR(255), `" // CHARACTER SET utf8
                    + col8 + "` VARBINARY(255), `"
                    + col9 + "` VARBINARY(256), `"
                    + col10 + "` TIMESTAMP, `"
                    + col11 + "` BINARY(20), `"
                    + col12 + "` SMALLINT"
                    + ") ENGINE=ndb;");
    }

    /**
     * All type tests.
     *
     * @todo rename to something more meaningful
     */
    public void testOperationID() throws NdbApiException, SQLException {
        NdbOperation op;

        /////////////////////////////////////////////////
        // insert test row
        trans = ndb.startTransaction();

        op = trans.getInsertOperation(tablename);
        op.equalInt(col1ID,int_val);
        op.setString(col2ID,varcharlong_val);
        op.setFloat(col3ID, float_val);
        op.setDouble(col4ID, double_val);
        op.setLong(col5ID, long_val);
        // TODO: Fix character sets - this one is latin1
        op.setString(col6ID,char_val);
        // TODO: Fix character sets - this one is utf8
        op.setString(col7ID, varchar_val);
        op.setBytes(col8ID, varbinary_val);
        op.setBytes(col9ID, varbinarylong_val);
        // Timestamp objects stored in NDB lose nano-second accuracy
        timestamp_val.setNanos(0);
        op.setTimestamp(col10ID, timestamp_val);
        op.setBytes(col11ID, binary_val);
        op.setInt(col12ID, short_val);

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
        trans.close();

        /////////////////////////////////////////////////
        // read row and check values
        trans = ndb.startTransaction();

        op = trans.getSelectOperation(tablename,LockMode.LM_CommittedRead);

        op.equalInt(col1,int_val);
        op.getValue(col1);
        op.getValue(col2);
        op.getValue(col3);
        op.getValue(col4);
        op.getValue(col5);
        op.getValue(col6);
        op.getValue(col7);
        op.getValue(col8);
        op.getValue(col9);
        op.getValue(col10);
        op.getValue(col11);
        op.getValue(col12);

        rs = op.resultData();

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);

        assertTrue(rs.next());

        // check values
        int id = rs.getInt(col1);
        assertEquals(id, int_val);

        String name = rs.getString(col2);
        assertEquals(name,varcharlong_val);

        float f = rs.getFloat(col3);
        assertEquals(f, float_val, 0.001);

        double d = rs.getDouble(col4);
        assertEquals(d, double_val, 0.0001);

        long l = rs.getInt(col5);
        assertEquals(l,long_val);

        String c = rs.getString(col6);
        assertEquals(c,char_val);

        String vc = rs.getString(col7);
        assertEquals(vc,varchar_val);

        assertByteArrayEquals(varbinary_val, rs.getBytes(col8));

        assertByteArrayEquals(varbinarylong_val, rs.getBytes(col9));

        Timestamp t = rs.getTimestamp(col10);
        // ndb does not store timestamps to nanosecond accuracy
        timestamp_val.setNanos(0);
        assertEquals(timestamp_val, t);

        byte b[] = rs.getBytes(col11);
        assertEquals(20, b.length);
        assertByteArrayEquals(binary_val, b, 3);

        short sh = rs.getShort(col12);
        assertEquals(sh,short_val);
        // done

        assertFalse(rs.next());
        trans.close();

        /////////////////////////////////////////////////
        // update the values TODO, assert updated values
        trans = ndb.startTransaction();

        op = trans.getUpdateOperation(tablename);
        op.equalInt(col1, int_val);

        varcharlong_val = "jim";
        op.setString(col2, varcharlong_val);

        float_val += 4.111;
        op.setFloat(col3, float_val);

        double_val -= 5.000;
        op.setDouble(col4, double_val);

        long_val += 88888888;
        op.setLong(col5, long_val);

        // TODO: Fix character sets - this one is Latin1
        char_val = "Grönqvist";
        op.setString(col6, char_val);

        // TODO: Fix character sets - this one is utf8
        varchar_val = "Dowling";
        op.setString(col7, varchar_val);

        varbinary_val = "Lyons".getBytes();
        op.setBytes(col8, varbinary_val);

        varbinarylong_val = "Lucan Sarsfields".getBytes();
        op.setBytes(col9, varbinarylong_val);

        timestamp_val.setTime(System.currentTimeMillis());
        op.setTimestamp(col10, timestamp_val);

        binary_val = "Spånga".getBytes();
        op.setBytes(col11, binary_val);

        short_val += 3;
        op.setInt(col12, short_val);

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
        trans.close();
    }
}
