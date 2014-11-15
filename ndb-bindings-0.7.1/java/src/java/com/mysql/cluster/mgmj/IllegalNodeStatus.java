package com.mysql.cluster.mgmj;

public class IllegalNodeStatus extends NdbMgmException {

	protected static final long serialVersionUID = 1L;
	
	public IllegalNodeStatus(String message) {
		super(message);
	}

}
