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

import java.sql.SQLException;

import com.mysql.cluster.ndbj.SchemaError;
import com.mysql.cluster.ndbj.NdbDictionary;
import com.mysql.cluster.ndbj.NdbOperation;
import com.mysql.cluster.ndbj.NdbTable;
import com.mysql.cluster.ndbj.NdbOperation.AbortOption;
import com.mysql.cluster.ndbj.NdbTransaction.ExecType;

import testsuite.BaseNdbjTestCase;

public class InvalidSchemaObjectVersionTest extends BaseNdbjTestCase {
    private static final String tablename = "t_invalid";

    public InvalidSchemaObjectVersionTest(String arg0) {
        super(arg0);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        createTable(tablename, "(id int, name varchar(32), " +
                    " age int, primary key(id)) engine=ndbcluster");
    }

    /**
     * Test exception when parts of the schema are changed when
     * the needed table is in the ddl cache.
     */
    public void testInvalidSchema() throws SQLException {
        NdbDictionary dictionary = ndb.getDictionary();

        @SuppressWarnings("unused")
            NdbTable table = dictionary.getTable(tablename);

        // Drop the table via mysql to avoid the auto-invalidation done by
        // the test framework
        stmt.executeUpdate("drop table " + tablename);
        stmt.executeUpdate("create table " + tablename +
                           "(id int, name varchar(32), age int, primary key(id)) " +
                           "engine=ndbcluster");

        // setup an operation
        trans = ndb.startTransaction();
        NdbOperation op = trans.getInsertOperation(tablename);
        op.equalInt("id", 1);
        op.setString("name", "Monty");

        // execute the transaction, exception is thrown
        try {
            trans.execute(ExecType.Commit,AbortOption.AbortOnError,true);
            fail("SchemaError should be thrown");
        } catch (SchemaError e) {
            // invalidate the DDL cache
            dictionary.invalidateTable(tablename);
        }

        // verify that we can complete the operation now
        trans = ndb.startTransaction();
        op = trans.getInsertOperation(tablename);
        op.equalInt("id", 1);
        op.setString("name", "Monty");
        trans.execute(ExecType.Commit,AbortOption.AbortOnError,true);
    }
}
