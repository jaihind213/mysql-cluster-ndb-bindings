package com.mysql.cluster.mgmj;

public class ServerNotConnected extends NdbMgmException {

	protected static final long serialVersionUID = 1L;
	
	public ServerNotConnected(String message) {
		super(message);
	}

}
