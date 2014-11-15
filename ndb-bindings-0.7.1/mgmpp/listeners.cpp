// -*- mode: c++ -*- 
/*  ndb-bindings: Bindings for the NDB API
    Copyright (C) 2006 MySQL, Inc.
    
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or 
    (at your option) any later version.
    
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/


#include "libmgmpp.h"
#include "listeners.hpp"

Ndb_logevent_type  ArbitResultTypeListener::getEventType() {
  return NDB_LE_ArbitResult;
}
void  ArbitResultTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent = new ArbitResult(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}



Ndb_logevent_type   ArbitStateTypeListener::getEventType() {
  return NDB_LE_ArbitState;
}
void   ArbitStateTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent = new ArbitState(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}



Ndb_logevent_type  BackupAbortedTypeListener::getEventType() {
  return NDB_LE_BackupAborted;
}
void  BackupAbortedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new BackupAborted(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}

Ndb_logevent_type  BackupCompletedTypeListener::getEventType() {
  return NDB_LE_BackupCompleted;
}
void  BackupCompletedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new BackupCompleted(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  BackupFailedToStartTypeListener::getEventType() {
  return NDB_LE_BackupFailedToStart;
}
void  BackupFailedToStartTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new BackupFailedToStart(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  BackupStartedTypeListener::getEventType() {
  return NDB_LE_BackupStarted;
}
void  BackupStartedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new BackupStarted(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  CmRegConfTypeListener::getEventType() {
  return NDB_LE_CM_REGCONF;
}
void  CmRegConfTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new CmRegConf(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}

Ndb_logevent_type  CmRegRefTypeListener::getEventType() {
  return NDB_LE_CM_REGREF;
}
void  CmRegRefTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new CmRegRef(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  CommunicationClosedTypeListener::getEventType() {
  return NDB_LE_CommunicationClosed;
}
void  CommunicationClosedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new CommunicationClosed(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  CommunicationOpenedTypeListener::getEventType() {
  return NDB_LE_CommunicationOpened;
}
void  CommunicationOpenedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new CommunicationOpened(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  ConnectedApiVersionTypeListener::getEventType() {
  return NDB_LE_ConnectedApiVersion;
}
void  ConnectedApiVersionTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new ConnectedApiVersion(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  CreateLogBytesTypeListener::getEventType() {
  return NDB_LE_CreateLogBytes;
}
void  CreateLogBytesTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new CreateLogBytes(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  DeadDueToHeartbeatTypeListener::getEventType() {
  return NDB_LE_DeadDueToHeartbeat;
}
void  DeadDueToHeartbeatTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new DeadDueToHeartbeat(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  ConnectedTypeListener::getEventType() {
  return NDB_LE_Connected;
}
void  ConnectedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new Connected(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}

Ndb_logevent_type  DisconnectedTypeListener::getEventType() {
  return NDB_LE_Disconnected;
}
void  DisconnectedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new Disconnected(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  EventBufferStatusTypeListener::getEventType() {
  return NDB_LE_EventBufferStatus;
}
void  EventBufferStatusTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new EventBufferStatus(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  FindNeighboursTypeListener::getEventType() {
  return NDB_LE_FIND_NEIGHBOURS;
}
void  FindNeighboursTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new FindNeighbours(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  GCPTakeoverCompletedTypeListener::getEventType() {
  return NDB_LE_GCP_TakeoverCompleted;
}
void  GCPTakeoverCompletedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new GCPTakeoverCompleted(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  GCPTakeoverStartedTypeListener::getEventType() {
  return NDB_LE_GCP_TakeoverStarted;
}
void  GCPTakeoverStartedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new GCPTakeoverStarted(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  GlobalCheckpointCompletedTypeListener::getEventType() {
  return NDB_LE_GlobalCheckpointCompleted;
}
void  GlobalCheckpointCompletedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new GlobalCheckpointCompleted(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  GlobalCheckpointStartedTypeListener::getEventType() {
  return NDB_LE_GlobalCheckpointStarted;
}
void  GlobalCheckpointStartedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new GlobalCheckpointStarted(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  InfoEventTypeListener::getEventType() {
  return NDB_LE_InfoEvent;
}
void  InfoEventTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new InfoEvent(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  JobStatisticTypeListener::getEventType() {
  return NDB_LE_JobStatistic;
}
void  JobStatisticTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new JobStatistic(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  LCPFragmentCompletedTypeListener::getEventType() {
  return NDB_LE_LCPFragmentCompleted;
}
void  LCPFragmentCompletedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new LCPFragmentCompleted(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  LCPStoppedInCalcKeepGciTypeListener::getEventType() {
  return NDB_LE_LCPStoppedInCalcKeepGci;
}
void  LCPStoppedInCalcKeepGciTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new LCPStoppedInCalcKeepGci(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  LCPTakeoverCompletedTypeListener::getEventType() {
  return NDB_LE_LCP_TakeoverCompleted;
}
void  LCPTakeoverCompletedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new LCPTakeoverCompleted(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  LCPTakeoverStartedTypeListener::getEventType() {
  return NDB_LE_LCP_TakeoverStarted;
}
void  LCPTakeoverStartedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new LCPTakeoverStarted(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  LocalCheckpointCompletedTypeListener::getEventType() {
  return NDB_LE_LocalCheckpointCompleted;
}
void  LocalCheckpointCompletedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new LocalCheckpointCompleted(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  LocalCheckpointStartedTypeListener::getEventType() {
  return NDB_LE_LocalCheckpointStarted;
}
void  LocalCheckpointStartedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new LocalCheckpointStarted(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  MemoryUsageTypeListener::getEventType() {
  return NDB_LE_MemoryUsage;
}
void  MemoryUsageTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new MemoryUsage(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  MissedHeartbeatTypeListener::getEventType() {
  return NDB_LE_MissedHeartbeat;
}
void  MissedHeartbeatTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new MissedHeartbeat(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  NdbStartCompletedTypeListener::getEventType() {
  return NDB_LE_NDBStartCompleted;
}
void  NdbStartCompletedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new NdbStartCompleted(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  NdbStartStartedTypeListener::getEventType() {
  return NDB_LE_NDBStartStarted;
}
void  NdbStartStartedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new NdbStartStarted(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  NdbStopAbortedTypeListener::getEventType() {
  return NDB_LE_NDBStopAborted;
}
void  NdbStopAbortedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new NdbStopAborted(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  NdbStopCompletedTypeListener::getEventType() {
  return NDB_LE_NDBStopCompleted;
}
void  NdbStopCompletedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new NdbStopCompleted(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  NdbStopForcedTypeListener::getEventType() {
  return NDB_LE_NDBStopForced;
}
void  NdbStopForcedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new NdbStopForced(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  NdbStopStartedTypeListener::getEventType() {
  return NDB_LE_NDBStopStarted;
}
void  NdbStopStartedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new NdbStopStarted(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  NodeFailCompletedTypeListener::getEventType() {
  return NDB_LE_NodeFailCompleted;
}
void  NodeFailCompletedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new NodeFailCompleted(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  NodeFailReportedTypeListener::getEventType() {
  return NDB_LE_NODE_FAILREP;
}
void  NodeFailReportedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new NodeFailReported(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  NRCopyDictTypeListener::getEventType() {
  return NDB_LE_NR_CopyDict;
}
void  NRCopyDictTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new NRCopyDict(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  NRCopyDistrTypeListener::getEventType() {
  return NDB_LE_NR_CopyDistr;
}
void  NRCopyDistrTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new NRCopyDistr(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  NRCopyFragDoneTypeListener::getEventType() {
  return NDB_LE_NR_CopyFragDone;
}
void  NRCopyFragDoneTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new NRCopyFragDone(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  NRCopyFragsCompletedTypeListener::getEventType() {
  return NDB_LE_NR_CopyFragsCompleted;
}
void  NRCopyFragsCompletedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new NRCopyFragsCompleted(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  NRCopyFragsStartedTypeListener::getEventType() {
  return NDB_LE_NR_CopyFragsStarted;
}
void  NRCopyFragsStartedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new NRCopyFragsStarted(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  OperationReportCountersTypeListener::getEventType() {
  return NDB_LE_OperationReportCounters;
}
void  OperationReportCountersTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new OperationReportCounters(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  ReceiveBytesStatisticTypeListener::getEventType() {
  return NDB_LE_ReceiveBytesStatistic;
}
void  ReceiveBytesStatisticTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new ReceiveBytesStatistic(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  SendBytesStatisticTypeListener::getEventType() {
  return NDB_LE_SendBytesStatistic;
}
void  SendBytesStatisticTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new SendBytesStatistic(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  SentHeartbeatTypeListener::getEventType() {
  return NDB_LE_SentHeartbeat;
}
void  SentHeartbeatTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new SentHeartbeat(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  SingleUserTypeListener::getEventType() {
  return NDB_LE_SingleUser;
}
void  SingleUserTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new SingleUser(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  StartLogTypeListener::getEventType() {
  return NDB_LE_StartLog;
}
void  StartLogTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new StartLog(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  StartPhaseCompletedTypeListener::getEventType() {
  return NDB_LE_StartPhaseCompleted;
}
void  StartPhaseCompletedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new StartPhaseCompleted(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  StartREDOLogTypeListener::getEventType() {
  return NDB_LE_StartREDOLog;
}
void  StartREDOLogTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new StartREDOLog(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  StartReportTypeListener::getEventType() {
  return NDB_LE_StartReport;
}
void  StartReportTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new StartReport(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  STTORRYRecievedTypeListener::getEventType() {
  return NDB_LE_STTORRYRecieved;
}
void  STTORRYRecievedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new STTORRYRecieved(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  TableCreatedTypeListener::getEventType() {
  return NDB_LE_TableCreated;
}
void  TableCreatedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new TableCreated(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  TransporterErrorTypeListener::getEventType() {
  return NDB_LE_TransporterError;
}
void  TransporterErrorTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new TransporterError(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  TransporterWarningTypeListener::getEventType() {
  return NDB_LE_TransporterWarning;
}
void  TransporterWarningTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new TransporterWarning(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  TransReportCountersTypeListener::getEventType() {
  return NDB_LE_TransReportCounters;
}
void  TransReportCountersTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new TransReportCounters(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}


Ndb_logevent_type  UndoLogBlockedTypeListener::getEventType() {
  return NDB_LE_UndoLogBlocked;
}
void  UndoLogBlockedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new UndoLogBlocked(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}

Ndb_logevent_type  UNDORecordsExecutedTypeListener::getEventType() {
  return NDB_LE_UNDORecordsExecuted;
}
void  UNDORecordsExecutedTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new UNDORecordsExecuted(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}

Ndb_logevent_type  
WarningTypeListener::getEventType() {
  return NDB_LE_WarningEvent;
}
void  WarningTypeListener::le_handleEvent(const ndb_logevent & event) {
//  ndb_logevent &theEvent = *event;
  wrappedEvent=new Warning(event);
  handleEvent(wrappedEvent);
  delete wrappedEvent;
}

