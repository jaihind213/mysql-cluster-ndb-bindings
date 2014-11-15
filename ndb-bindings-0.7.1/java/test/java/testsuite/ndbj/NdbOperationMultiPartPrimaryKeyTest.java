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
import com.mysql.cluster.ndbj.NdbOperation;
import com.mysql.cluster.ndbj.NdbOperation.AbortOption;
import com.mysql.cluster.ndbj.NdbOperation.LockMode;
import com.mysql.cluster.ndbj.NdbTransaction.ExecType;

import java.sql.SQLException;

/**
 *
 create table t_multipart_primary (a int, b int, c varbinary(20), PRIMARY KEY(a,b)) engine=ndb;
 insert into t_multipart_primary values(1,11,"jim.......");

 */
public class NdbOperationMultiPartPrimaryKeyTest extends BaseNdbjTestCase {

    private static final String col1Val="j";
    private static final String col2Val="d";
    private static final String col3Val="jim";

    private static final String col1="a";
    private static final String col2="b";
    private static final String col3="c";

    protected final static String tablename = "t_str_b";

    public NdbOperationMultiPartPrimaryKeyTest(String arg0) {
        super(arg0);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        createTable("t_str_b", "(`" + col1
                    + "` varchar(256) NOT NULL, "
                    +"`" + col2
                    +"` varbinary(256), `"
                    + col3
                    + "` varbinary(100), PRIMARY KEY(a,b)) ENGINE=ndb;");
    }


    public void testOperationMultiPartPrimaryKey() throws NdbApiException, SQLException {
        NdbOperation op;

        trans = ndb.startTransaction();

        op = trans.getInsertOperation(tablename);

        op.equalString(col1,col1Val);
        op.equalString(col2,col2Val);

        op.setBytes(col3,col3Val.getBytes());

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
        trans.close();

        trans = ndb.startTransaction();
        op = trans.getSelectOperation(tablename,LockMode.LM_Exclusive);

        op.equalString(col1,col1Val);
        op.equalString(col2,col2Val);
        op.getValue(col3);

        rs = op.resultData();

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);

        byte[] col3ValBytes = col3Val.getBytes();
        while(rs.next())
        {
            byte[] c = rs.getBytes(col3);
            for (int i=0; i < c.length; i++) {
                assertEquals(c[i],col3ValBytes[i]);
            }
        }
        trans.close();

        trans = ndb.startTransaction();

        op = trans.getUpdateOperation(tablename);

        op.equalString(col1,col1Val);
        op.equalString(col2,col2Val);
        op.setBytes(col3, (col3Val + "_jd").getBytes());

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
        trans.close();

        trans = ndb.startTransaction();

        op = trans.getDeleteOperation(tablename);

        op.equalString(col1,col1Val);
        op.equalString(col2,col2Val);

        trans.execute(ExecType.Commit, AbortOption.AbortOnError, true);
        trans.close();
    }
}
