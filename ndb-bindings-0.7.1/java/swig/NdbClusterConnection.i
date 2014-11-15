/* -*- mode: c++; c-basic-offset: 2; indent-tabs-mode: nil; -*-
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

%rename Ndb_cluster_connection NdbClusterConnectionImpl;
%rename(realClose) Ndb_cluster_connection::close();

%typemap(javabase) Ndb_cluster_connection "NdbClusterConnection";

%javamethodmodifiers Ndb_cluster_connection::deleteAllNdbObjects "protected";

%typemap(javaimports) Ndb_cluster_connection %{
import java.util.ArrayList;
import java.util.List;
%}

%typemap(javacode) Ndb_cluster_connection %{

  public void close()
  {
    this.delete();
  }

  private List<NdbImpl> theNdbs = null;

  public Ndb createNdb(String aCatalogName, int maxThreads) throws NdbApiException {
    if (theNdbs==null)
      theNdbs = new ArrayList<NdbImpl>();
    NdbImpl theNewNdb = realCreateNdb(aCatalogName, maxThreads);
    if (theNewNdb != null )
      theNdbs.add(theNewNdb);
    theNdbs.add(theNewNdb);
    return theNewNdb;
  }
  public Ndb createNdb(String aCatalogName) throws NdbApiException {
    if (theNdbs==null)
      theNdbs = new ArrayList<NdbImpl>();
    NdbImpl theNewNdb = realCreateNdb(aCatalogName);
    if (theNewNdb != null )
      theNdbs.add(theNewNdb);
    theNdbs.add(theNewNdb);
    return theNewNdb;
  }
%}

%include "ndbapi/NdbClusterConnection.i"
