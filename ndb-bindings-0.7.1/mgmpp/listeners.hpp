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

#ifndef listeners_h
#define listeners_h

#include "libmgmpp.h"
#include "events.hpp"
#include "NdbEventListener.hpp"

class ArbitResultTypeListener : public NdbLogEventTypeListener {

private:
  
  ArbitResult * wrappedEvent;
	
public:		    
    
  virtual void handleEvent(ArbitResult * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};



class  ArbitStateTypeListener : public NdbLogEventTypeListener {

private:

  ArbitState * wrappedEvent;

public:

  virtual void handleEvent(ArbitState * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};



class BackupAbortedTypeListener : public NdbLogEventTypeListener {

private:

  BackupAborted * wrappedEvent;

public:

  virtual void handleEvent(BackupAborted * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class BackupCompletedTypeListener : public NdbLogEventTypeListener {

private:

  BackupCompleted * wrappedEvent;

public:

  virtual void handleEvent(BackupCompleted * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class BackupFailedToStartTypeListener : public NdbLogEventTypeListener {

private:

  BackupFailedToStart * wrappedEvent;

public:

  virtual void handleEvent(BackupFailedToStart * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class BackupStartedTypeListener : public NdbLogEventTypeListener {

private:

  BackupStarted * wrappedEvent;

public:

  virtual void handleEvent(BackupStarted * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class CmRegConfTypeListener : public NdbLogEventTypeListener {

private:

  CmRegConf * wrappedEvent;

public:

  virtual void handleEvent(CmRegConf * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class CmRegRefTypeListener : public NdbLogEventTypeListener {

private:

  CmRegRef * wrappedEvent;

public:

  virtual void handleEvent(CmRegRef * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class CommunicationClosedTypeListener : public NdbLogEventTypeListener {

private:

  CommunicationClosed * wrappedEvent;

public:

  virtual void handleEvent(CommunicationClosed * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class CommunicationOpenedTypeListener : public NdbLogEventTypeListener {

private:

  CommunicationOpened * wrappedEvent;

public:

  virtual void handleEvent(CommunicationOpened * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class ConnectedApiVersionTypeListener : public NdbLogEventTypeListener {

private:

  ConnectedApiVersion * wrappedEvent;

public:

  virtual void handleEvent(ConnectedApiVersion * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class CreateLogBytesTypeListener : public NdbLogEventTypeListener {

private:

  CreateLogBytes * wrappedEvent;

public:

  virtual void handleEvent(CreateLogBytes * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class DeadDueToHeartbeatTypeListener : public NdbLogEventTypeListener {

private:

  DeadDueToHeartbeat * wrappedEvent;

public:

  virtual void handleEvent(DeadDueToHeartbeat * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class ConnectedTypeListener : public NdbLogEventTypeListener {

private:

  Connected * wrappedEvent;

public:

  virtual void handleEvent(Connected * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};
class DisconnectedTypeListener : public NdbLogEventTypeListener {

private:

  Disconnected * wrappedEvent;

public:

  virtual void handleEvent(Disconnected * event) {(void)event;} ;
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class EventBufferStatusTypeListener : public NdbLogEventTypeListener {

private:

  EventBufferStatus * wrappedEvent;

public:

  virtual void handleEvent(EventBufferStatus * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class FindNeighboursTypeListener : public NdbLogEventTypeListener {

private:

  FindNeighbours * wrappedEvent;

public:

  virtual void handleEvent(FindNeighbours * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class GCPTakeoverCompletedTypeListener : public NdbLogEventTypeListener {

private:

  GCPTakeoverCompleted * wrappedEvent;

public:

  virtual void handleEvent(GCPTakeoverCompleted * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class GCPTakeoverStartedTypeListener : public NdbLogEventTypeListener {

private:

  GCPTakeoverStarted * wrappedEvent;

public:

  virtual void handleEvent(GCPTakeoverStarted * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class GlobalCheckpointCompletedTypeListener : public NdbLogEventTypeListener {

private:

  GlobalCheckpointCompleted * wrappedEvent;

public:

  virtual void handleEvent(GlobalCheckpointCompleted * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class GlobalCheckpointStartedTypeListener : public NdbLogEventTypeListener {

private:

  GlobalCheckpointStarted * wrappedEvent;

public:

  virtual void handleEvent(GlobalCheckpointStarted * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class InfoEventTypeListener : public NdbLogEventTypeListener {

private:

  InfoEvent * wrappedEvent;

public:

  virtual void handleEvent(InfoEvent * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class JobStatisticTypeListener : public NdbLogEventTypeListener {

private:

  JobStatistic * wrappedEvent;

public:

  virtual void handleEvent(JobStatistic * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class LCPFragmentCompletedTypeListener : public NdbLogEventTypeListener {

private:

  LCPFragmentCompleted * wrappedEvent;

public:

  virtual void handleEvent(LCPFragmentCompleted * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class LCPStoppedInCalcKeepGciTypeListener : public NdbLogEventTypeListener {

private:

  LCPStoppedInCalcKeepGci * wrappedEvent;

public:

  virtual void handleEvent(LCPStoppedInCalcKeepGci * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class LCPTakeoverCompletedTypeListener : public NdbLogEventTypeListener {

private:

  LCPTakeoverCompleted * wrappedEvent;

public:

  virtual void handleEvent(LCPTakeoverCompleted * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class LCPTakeoverStartedTypeListener : public NdbLogEventTypeListener {

private:

  LCPTakeoverStarted * wrappedEvent;

public:

  virtual void handleEvent(LCPTakeoverStarted * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class LocalCheckpointCompletedTypeListener : public NdbLogEventTypeListener {

private:

  LocalCheckpointCompleted * wrappedEvent;

public:

  virtual void handleEvent(LocalCheckpointCompleted * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class LocalCheckpointStartedTypeListener : public NdbLogEventTypeListener {

private:

  LocalCheckpointStarted * wrappedEvent;

public:

  virtual void handleEvent(LocalCheckpointStarted * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class MemoryUsageTypeListener : public NdbLogEventTypeListener {

private:

  MemoryUsage * wrappedEvent;

public:

  virtual void handleEvent(MemoryUsage * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class MissedHeartbeatTypeListener : public NdbLogEventTypeListener {

private:

  MissedHeartbeat * wrappedEvent;

public:

  virtual void handleEvent(MissedHeartbeat * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class NdbStartCompletedTypeListener : public NdbLogEventTypeListener {

private:

  NdbStartCompleted * wrappedEvent;

public:

  virtual void handleEvent(NdbStartCompleted * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class NdbStartStartedTypeListener : public NdbLogEventTypeListener {

private:

  NdbStartStarted * wrappedEvent;

public:

  virtual void handleEvent(NdbStartStarted * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class NdbStopAbortedTypeListener : public NdbLogEventTypeListener {

private:

  NdbStopAborted * wrappedEvent;

public:

  virtual void handleEvent(NdbStopAborted * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class NdbStopCompletedTypeListener : public NdbLogEventTypeListener {

private:

  NdbStopCompleted * wrappedEvent;

public:

  virtual void handleEvent(NdbStopCompleted * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class NdbStopForcedTypeListener : public NdbLogEventTypeListener {

private:

  NdbStopForced * wrappedEvent;

public:

  virtual void handleEvent(NdbStopForced * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class NdbStopStartedTypeListener : public NdbLogEventTypeListener {

private:

  NdbStopStarted * wrappedEvent;

public:

  virtual void handleEvent(NdbStopStarted * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class NodeFailCompletedTypeListener : public NdbLogEventTypeListener {

private:

  NodeFailCompleted * wrappedEvent;

public:

  virtual void handleEvent(NodeFailCompleted * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class NodeFailReportedTypeListener : public NdbLogEventTypeListener {

private:

  NodeFailReported * wrappedEvent;

public:

  virtual void handleEvent(NodeFailReported * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class NRCopyDictTypeListener : public NdbLogEventTypeListener {

private:

  NRCopyDict * wrappedEvent;

public:

  virtual void handleEvent(NRCopyDict * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class NRCopyDistrTypeListener : public NdbLogEventTypeListener {

private:

  NRCopyDistr * wrappedEvent;

public:

  virtual void handleEvent(NRCopyDistr * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class NRCopyFragDoneTypeListener : public NdbLogEventTypeListener {

private:

  NRCopyFragDone * wrappedEvent;

public:

  virtual void handleEvent(NRCopyFragDone * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class NRCopyFragsCompletedTypeListener : public NdbLogEventTypeListener {

private:

  NRCopyFragsCompleted * wrappedEvent;

public:

  virtual void handleEvent(NRCopyFragsCompleted * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class NRCopyFragsStartedTypeListener : public NdbLogEventTypeListener {

private:

  NRCopyFragsStarted * wrappedEvent;

public:

  virtual void handleEvent(NRCopyFragsStarted * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class OperationReportCountersTypeListener : public NdbLogEventTypeListener {

private:

  OperationReportCounters * wrappedEvent;

public:

  virtual void handleEvent(OperationReportCounters * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class ReceiveBytesStatisticTypeListener : public NdbLogEventTypeListener {

private:

  ReceiveBytesStatistic * wrappedEvent;

public:

  virtual void handleEvent(ReceiveBytesStatistic * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class SendBytesStatisticTypeListener : public NdbLogEventTypeListener {

private:

  SendBytesStatistic * wrappedEvent;

public:

  virtual void handleEvent(SendBytesStatistic * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class SentHeartbeatTypeListener : public NdbLogEventTypeListener {

private:

  SentHeartbeat * wrappedEvent;

public:

  virtual void handleEvent(SentHeartbeat * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class SingleUserTypeListener : public NdbLogEventTypeListener {

private:

  SingleUser * wrappedEvent;

public:

  virtual void handleEvent(SingleUser * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class StartLogTypeListener : public NdbLogEventTypeListener {

private:

  StartLog * wrappedEvent;

public:

  virtual void handleEvent(StartLog * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class StartPhaseCompletedTypeListener : public NdbLogEventTypeListener {

private:

  StartPhaseCompleted * wrappedEvent;

public:

  virtual void handleEvent(StartPhaseCompleted * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class StartREDOLogTypeListener : public NdbLogEventTypeListener {

private:

  StartREDOLog * wrappedEvent;

public:

  virtual void handleEvent(StartREDOLog * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class StartReportTypeListener : public NdbLogEventTypeListener {

private:

  StartReport * wrappedEvent;

public:

  virtual void handleEvent(StartReport * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class STTORRYRecievedTypeListener : public NdbLogEventTypeListener {

private:

  STTORRYRecieved * wrappedEvent;

public:

  virtual void handleEvent(STTORRYRecieved * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class TableCreatedTypeListener : public NdbLogEventTypeListener {

private:

  TableCreated * wrappedEvent;

public:

  virtual void handleEvent(TableCreated * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class TransporterErrorTypeListener : public NdbLogEventTypeListener {

private:

  TransporterError * wrappedEvent;

public:

  virtual void handleEvent(TransporterError * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class TransporterWarningTypeListener : public NdbLogEventTypeListener {

private:

  TransporterWarning * wrappedEvent;

public:

  virtual void handleEvent(TransporterWarning * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class TransReportCountersTypeListener : public NdbLogEventTypeListener {

private:

  TransReportCounters * wrappedEvent;

public:

  virtual void handleEvent(TransReportCounters * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class UndoLogBlockedTypeListener : public NdbLogEventTypeListener {

private:

  UndoLogBlocked * wrappedEvent;

public:

  virtual void handleEvent(UndoLogBlocked * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class UNDORecordsExecutedTypeListener : public NdbLogEventTypeListener {

private:

  UNDORecordsExecuted * wrappedEvent;

public:

  virtual void handleEvent(UNDORecordsExecuted * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};

class WarningTypeListener : public NdbLogEventTypeListener {


  Warning * wrappedEvent;
 
public:

  virtual void handleEvent(Warning * event) {(void)event;};
  void le_handleEvent(const ndb_logevent & event);
  Ndb_logevent_type getEventType();

};
 
#endif
 
