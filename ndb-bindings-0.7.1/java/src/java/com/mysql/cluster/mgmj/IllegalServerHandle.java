package com.mysql.cluster.mgmj;

public class IllegalServerHandle extends NdbMgmException {

	protected static final long serialVersionUID = 1L;
	
	public IllegalServerHandle(String message) {
		super(message);
	}

}
