package com.mysql.cluster.mgmj;

public class StopFailed extends NdbMgmException {

	protected static final long serialVersionUID = 1L;
	
	public StopFailed(String message) {
		super(message);
	}

}
