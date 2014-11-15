package com.mysql.cluster.mgmj;

public interface NdbLogEvent {

	public abstract NdbLogEventType getEventType();

	public abstract NdbLogEventCategory getEventCategory();

	public abstract NdbLogEventSeverity getEventSeverity();

	public abstract long getSourceNodeId();

	public abstract long getEventTime();

	public abstract long getEventLevel();

}