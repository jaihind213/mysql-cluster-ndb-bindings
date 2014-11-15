package com.mysql.cluster.mgmj;

import java.util.Collection;

public interface ClusterState {

	public abstract Collection<NodeState> getNodeStates();

	public abstract int getNoOfNodes();

	public abstract NodeState getNodeState(int nodeId);

}