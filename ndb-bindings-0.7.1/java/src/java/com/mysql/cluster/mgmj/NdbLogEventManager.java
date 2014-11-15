package com.mysql.cluster.mgmj;

public interface NdbLogEventManager {

	public abstract boolean unregisterListener(NdbLogEventTypeListener listener);

	public abstract boolean unregisterListener(
			NdbLogEventCategoryListener listener);

	public abstract int registerListener(NdbLogEventTypeListener listener)
			throws NdbMgmException;

	public abstract int registerListener(NdbLogEventCategoryListener listener)
			throws NdbMgmException;

	public abstract NdbLogEvent getLogEvent(long timeout_in_milliseconds)
			throws NdbMgmException;

	public abstract int pollEvents(long timeout_in_milliseconds)
			throws NdbMgmException;

}