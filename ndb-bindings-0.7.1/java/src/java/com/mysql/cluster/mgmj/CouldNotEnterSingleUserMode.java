package com.mysql.cluster.mgmj;

public class CouldNotEnterSingleUserMode extends NdbMgmException {

	protected static final long serialVersionUID = 1L;
	
	public CouldNotEnterSingleUserMode(String message) {
		super(message);
	}

}
