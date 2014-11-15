package com.mysql.cluster.mgmj;

public class RestartFailed extends NdbMgmException {

	protected static final long serialVersionUID = 1L;
	
	public RestartFailed(String message) {
		super(message);
	}

}
