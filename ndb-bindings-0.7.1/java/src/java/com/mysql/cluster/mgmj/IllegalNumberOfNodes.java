package com.mysql.cluster.mgmj;

public class IllegalNumberOfNodes extends NdbMgmException {

	protected static final long serialVersionUID = 1L;
	
	public IllegalNumberOfNodes(String message) {
		super(message);
	}

}
