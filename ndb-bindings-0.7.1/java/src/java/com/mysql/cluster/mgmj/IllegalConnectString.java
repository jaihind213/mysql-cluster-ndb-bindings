package com.mysql.cluster.mgmj;

public class IllegalConnectString extends NdbMgmException {

	protected static final long serialVersionUID = 1L;
	
	public IllegalConnectString(String message) {
		super(message);
	}

}
