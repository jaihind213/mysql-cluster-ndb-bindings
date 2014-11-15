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

public class NdbOperationTest extends BaseNdbjTestCase {

    private static final String col1="int_col";
    private static final String col2="varcharlong_col";
    private static final String col3="float_col";
    private static final String col4="double_col";
    private static final String col5="long_col";
    private static final String col6="char_col";
    private static final String col7="varchar_col";
    private static final String col8="varbinary_col";
    private static final String col9="varbinarylong_col";
    private static final String col10="timestamp_col";
    private static final String col11="binary_col";
    private static final String col12="short_col";

    private final static String tablename = "t_ops";

    public NdbOperationTest(String arg0) {
        super(arg0);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        createTable(tablename, "(`"
                    + col1 + "` INT PRIMARY KEY, `"
                    + col2 + "` VARCHAR(256), `"
                    + col3 + "` FLOAT, `"
                    + col4 + "` DOUBLE, `"
                    + col5 + "` BIGINT, `"
                    + col6 + "` CHAR(20), `"
                    + col7 + "` VARCHAR(255) CHARACTER SET utf8, `"
                    + col8 + "` VARBINARY(255), `"
                    + col9 + "` VARBINARY(256), `"
                    + col10 + "` TIMESTAMP, `"
                    + col11 + "` BINARY(20), `"
                    + col12 + "` SMALLINT"
                    + ") ENGINE=ndb;");
    }

    public void testOperation() throws NdbApiException, SQLException {
        String varcharlong_val="Evelyn";
        // 240+ chars causes crash, TODO test+fix
        String varchar_val= "Paddy";//"0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";
        byte[] varbinary_val="Maeve".getBytes(); // "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789".getBytes();
        String char_val="Linda";
        int 	int_val = 100;
        byte[] varbinarylong_val="Aoife".getBytes();
        byte[] binary_val="Rob".getBytes();
        float float_val = 1.11F;
        double double_val = 2.22D;
        long long_val = 1233456789L;
        Timestamp timestamp_val = new Timestamp(System.currentTimeMillis());
        short short_val = 1974;
        NdbOperation op;

        trans = ndb.startTransaction();
        op = trans.getInsertOperation(tablename);
        op.equalInt(col1,int_val);
        op.setString(col2,varcharlong_val);
        op.setFloat(col3, float_val);
        op.setDouble(col4, double_val);
        op.setLong(col5, long_val);
        // TODO: Fix character sets - this one is Latin1
        op.setString(col6,char_val);
        // TODO: Fix character sets - this one is utf8
        op.setString(col7, varchar_val);
        op.setBytes(col8, varbinary_val);
        op.setBytes(col9, varbinarylong_val);
        // Timestamp objects stored in NDB lose nano-second accuracy
        timestamp_val.setNanos(0);
        op.setTimestamp(col10, timestamp_val);
        op.setBytes(col11, binary_val);
        op.setInt(col12, short_val);
        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
        trans.close();

        trans = ndb.startTransaction();
        op = trans.getSelectOperation(tablename,LockMode.LM_Exclusive);
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

        while(rs.next())
        {
            int id = rs.getInt(col1);
            assertEquals(id, int_val);

            String name = rs.getString(col2);
            assertEquals(name,varcharlong_val);

            float f = rs.getFloat(col3);
            assertTrue(float_val > f - 0.001 && float_val < f + 0.001);

            double d = rs.getDouble(col4);
            assertTrue(double_val > d - 0.001 && double_val < d + 0.001);

            long l = rs.getInt(col5);
            assertEquals(l,long_val);

            String c = rs.getString(col6);
            assertEquals(c,char_val);

            String vc = rs.getString(col7);
            assertEquals(vc,varchar_val);

            byte[] vb = rs.getBytes(col8);
            int k=0;
            for (byte b: vb) {
                assertEquals(b,varbinary_val[k++]);
            }
            byte[] lvb = rs.getBytes(col9);
            int m=0;
            for (byte b: lvb) {
                assertEquals(b,varbinarylong_val[m++]);
            }

            Timestamp t = rs.getTimestamp(col10);
            // ndb does not store timestamps to nanosecond accuracy
            timestamp_val.setNanos(0);
            assertEquals(t, timestamp_val);

            byte[] bin = rs.getBytes(col11);
            assertEquals(20, bin.length);
            assertByteArrayEquals(binary_val, bin, 3);
        }
        trans.close();

        trans = ndb.startTransaction();

        op = trans.getUpdateOperation(tablename);

        op.equalInt(col1,int_val);

        varcharlong_val="jim";
        op.setString(col2,varcharlong_val);

        float_val += 4.111;
        op.setFloat(col3, float_val);

        double_val -= 5.000;
        op.setDouble(col4, double_val);

        long_val +=88888888;
        op.setLong(col5, long_val);

        // TODO: Fix character sets - this one is Latin1
        char_val="Grönqvist";
        op.setString(col6,char_val);

        // TODO: Fix character sets - this one is utf8
        varchar_val="Dowling";
        op.setString(col7, varchar_val);

        varbinary_val="Lyons".getBytes();
        op.setBytes(col8, varbinary_val);

        varbinarylong_val = "Lucan Sarsfields".getBytes();
        op.setBytes(col9, varbinarylong_val);

        timestamp_val.setTime(System.currentTimeMillis());
        op.setTimestamp(col10, timestamp_val);

        binary_val = "Spånga".getBytes();
        op.setBytes(col11, binary_val);

        short_val+=3;
        op.setInt(col12, short_val);

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
        trans.close();

        trans = ndb.startTransaction();

        op = trans.getDeleteOperation(tablename);

        op.equalInt(col1,int_val);

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
        trans.close();
    }
}
