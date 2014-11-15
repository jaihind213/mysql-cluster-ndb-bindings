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

%typemap(javaimports) ndb_mgm_cluster_state %{
import java.util.ArrayList;
import java.util.Collection;
%}

%typemap(javacode) ndb_mgm_cluster_state  %{
  public Collection<NodeState> getNodeStates() {
    Collection<NodeState> nodeStates = new ArrayList<NodeState>(0);
    for (int i=0; i < getNoOfNodes(); i++) {
      nodeStates.add(getNodeState(i));
    }
    return nodeStates;
  }
  %}

