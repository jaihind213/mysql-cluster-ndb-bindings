package com.mysql.cluster.mgmj;

public class CouldNotConnectToSocket extends NdbMgmException {

	protected static final long serialVersionUID = 1L;
	
	public CouldNotConnectToSocket(String message) {
		super(message);
	}

}
