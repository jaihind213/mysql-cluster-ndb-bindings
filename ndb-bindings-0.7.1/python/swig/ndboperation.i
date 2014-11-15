/* -*- mode: python; c-basic-offset: 2; indent-tabs-mode: nil; -*-
 *  vim:expandtab:shiftwidth=2:tabstop=2:smarttab:
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

%extend NdbOperation {

  %pythoncode %{

def undefinedValue(self,col,val):
  return self.equalBytes(col,val)

def setUndefinedValue(self,col,val):
  self.setBytes(col,val)

def equal(self, col, val=None):

  if val is None:
    self.equalNull(col)
  col_type=self.getTable().getColumn(col).getType()
  equal_lookup[col_type](self,col,val)

def setValue(self, col, val=None):

  if val is None:
    self.setNull(col)
  col_type=self.getTable().getColumn(col).getType()
  setval_lookup[col_type](self,col,val)
            %}

 };
