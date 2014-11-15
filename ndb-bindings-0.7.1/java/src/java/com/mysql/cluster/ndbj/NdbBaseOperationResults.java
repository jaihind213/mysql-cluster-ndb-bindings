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

package com.mysql.cluster.ndbj;

public interface NdbBaseOperationResults {

    /**
     * Call getValue after having called the equal method to specify a column you
     * would like to retrieve from the NdbResultSet.
     * <br> The columnId is used to specify the column, where 1 is the first column in the table.
     * @param columnId integer position (offset) of column number in schema definition (columnId starts from  position '1' for the first column in a schema).
     * @throws NdbApiException if the columnId < 1; if a NdbRecAttrImpl could not be created to hold the column.
     *
     */
    public void getValue(long columnId) throws NdbApiException;

    /**
     * Call getValue after having called the equal method to specify a column you
     * would like to retrieve from the NdbResultSet.
     * <br> The columnName is used to specify the column, where 1 is the first column in the table.
     * <code>
     * <br>NdbOperation op = trans.getNdbOperation("someTable");
     * <br>op.equals("pkValue","X");
     * <br>op.getValue("myInt");
     * <br>NdbResultSet rs = op.resultData();
     * trans.execute(NdbTransaction.ExecType.NoCommit, NdbTransaction.AbortOption.AbortOnError, 1);
     *
     * if (rs.next()) {
     *   int res = rs.getInt();
     * }
     * </code>
     * @param columnName Name of the Column
     * @throws NdbApiException if there was a problem in the cluster when selecting this column.
     * @throws NdbApiRuntimeException If a bad columnName is entered
     */
    public void getValue(String columnName) throws NdbApiException;

    /**
     * Gets the result data an a NdbResultSet object that is populated only after
     * the transaction has committed. Use the {@link NdbResultSet#wasNull()} method to test if the NdbResultSet contents
     * is valid.
     * @return the NdbResultSet object that can be used to access column values
     */
    public NdbResultSet resultData();

    public void getBlob(long columnId) throws NdbApiException;

    public void getBlob(String columnName) throws NdbApiException;

    public void getBlob(long columnId, int length) throws NdbApiException;

    public void getBlob(String columnName, int length) throws NdbApiException;

}
