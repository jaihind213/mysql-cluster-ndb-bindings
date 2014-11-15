#include "libmgmpp.h"
#include "events.hpp"

//BaseEvent::BaseEvent()  {}

BaseEvent::BaseEvent(const ndb_logevent & theEvent) {
  theType = theEvent.type;
  theSourceNodeId = theEvent.source_nodeid;
  theEventTime = theEvent.time;
  theCategory = theEvent.category;
  theSeverity = theEvent.severity;
}
 
Ndb_logevent_type BaseEvent::getType() { 
  return theType;
}
int BaseEvent::getSourceNodeId() { 
  return theSourceNodeId;
}
Uint32 BaseEvent::getEventTime() { 
  return theEventTime;
}
ndb_mgm_event_category BaseEvent::getEventCategory() { 
  if (theCategory == 2) {
    return NDB_MGM_ILLEGAL_EVENT_CATEGORY; 
  }
  return theCategory; 
}

ndb_mgm_event_severity BaseEvent::getEventSeverity() { 
  return theSeverity; 
}


Connected::Connected(const ndb_logevent & theEvent) : BaseEvent(theEvent) { 
  event=theEvent;
  node=theEvent.Connected.node;
}

Disconnected::Disconnected(const ndb_logevent & theEvent) : BaseEvent(theEvent) { 
  event=theEvent; 
  node=theEvent.Disconnected.node;
}


CommunicationOpened::CommunicationOpened(const ndb_logevent & theEvent) : BaseEvent(theEvent) { 
  event=theEvent; 
  node=event.CommunicationOpened.node;
}

CommunicationClosed::CommunicationClosed(const ndb_logevent & theEvent) : BaseEvent(theEvent) { 
  event=theEvent; 
  node=event.CommunicationClosed.node;
}

ConnectedApiVersion::ConnectedApiVersion(const ndb_logevent & theEvent) : BaseEvent(theEvent) { 
  event=theEvent; 
  node=event.ConnectedApiVersion.node;
  node=event.ConnectedApiVersion.version;
}
  

GlobalCheckpointStarted::GlobalCheckpointStarted(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  gci=event.GlobalCheckpointStarted.gci;
}

GlobalCheckpointCompleted::GlobalCheckpointCompleted(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  gci=event.GlobalCheckpointCompleted.gci;
}


LocalCheckpointStarted::LocalCheckpointStarted(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  lci = event.LocalCheckpointStarted.lci;
  keepGci = event.LocalCheckpointStarted.keep_gci;
}

LocalCheckpointCompleted::LocalCheckpointCompleted(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  lci=event.LocalCheckpointCompleted.lci;
}

LCPStoppedInCalcKeepGci::LCPStoppedInCalcKeepGci(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  data=event.LCPStoppedInCalcKeepGci.data;
}

LCPFragmentCompleted::LCPFragmentCompleted(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  node=event.LCPFragmentCompleted.node;
  tableId=event.LCPFragmentCompleted.table_id;
  fragmentId=event.LCPFragmentCompleted.fragment_id;
}
UndoLogBlocked::UndoLogBlocked(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  accCount=event.UndoLogBlocked.acc_count;
  tupCount=event.UndoLogBlocked.tup_count;
}

NdbStartStarted::NdbStartStarted(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  version=event.NDBStartStarted.version;
}

NdbStartCompleted::NdbStartCompleted(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  version=event.NDBStartCompleted.version;
}

STTORRYRecieved::STTORRYRecieved(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
}

StartPhaseCompleted::StartPhaseCompleted(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  phase=event.StartPhaseCompleted.phase;
  startType=event.StartPhaseCompleted.starttype;
}

CmRegConf::CmRegConf(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  ownId=event.CM_REGCONF.own_id;
  presidentId=event.CM_REGCONF.president_id;
  dynamicId=event.CM_REGCONF.dynamic_id;
}

CmRegRef::CmRegRef(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  ownId=event.CM_REGREF.own_id;
  otherId=event.CM_REGREF.other_id;
  cause=event.CM_REGREF.cause;
}


FindNeighbours::FindNeighbours(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  ownId=event.FIND_NEIGHBOURS.own_id;
  leftId=event.FIND_NEIGHBOURS.left_id;
  rightId=event.FIND_NEIGHBOURS.right_id;
  dynamicId=event.FIND_NEIGHBOURS.dynamic_id;
}

NdbStopStarted::NdbStopStarted(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  stopType=event.NDBStopStarted.stoptype;
}

NdbStopCompleted::NdbStopCompleted(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  action=event.NDBStopCompleted.action;
  sigNum=event.NDBStopCompleted.signum;
}

NdbStopForced::NdbStopForced(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  action=event.NDBStopForced.action;
  signum=event.NDBStopForced.signum;
  error=event.NDBStopForced.error;
  sphase=event.NDBStopForced.sphase;
  extra=event.NDBStopForced.extra;
}

NdbStopAborted::NdbStopAborted(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
}

    StartREDOLog::StartREDOLog(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
      event=theEvent;
      node=event.StartREDOLog.node;
      keepGci=event.StartREDOLog.keep_gci;
      completedGci=event.StartREDOLog.completed_gci;
      restorableGci=event.StartREDOLog.restorable_gci;
    }

    StartLog::StartLog(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
      event=theEvent;
      logPart=event.StartLog.log_part;
      startMb=event.StartLog.start_mb;
      stopMb=event.StartLog.stop_mb;
      gci=event.StartLog.gci;
    }

    UNDORecordsExecuted::UNDORecordsExecuted(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
      event=theEvent;
      block=event.UNDORecordsExecuted.block;
      data1=event.UNDORecordsExecuted.data1;
      data2=event.UNDORecordsExecuted.data2;
      data3=event.UNDORecordsExecuted.data3;
      data4=event.UNDORecordsExecuted.data4;
      data5=event.UNDORecordsExecuted.data5;
      data6=event.UNDORecordsExecuted.data6;
      data7=event.UNDORecordsExecuted.data7;
      data8=event.UNDORecordsExecuted.data8;
      data9=event.UNDORecordsExecuted.data9;
      data10=event.UNDORecordsExecuted.data10;
    }

    NRCopyDict::NRCopyDict(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
      event=theEvent;
    }

    NRCopyDistr::NRCopyDistr(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
      event=theEvent;
    }

    NRCopyFragsStarted::NRCopyFragsStarted(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
      event=theEvent;
      destNode=event.NR_CopyFragsStarted.dest_node;
    }

    NRCopyFragDone::NRCopyFragDone(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
      event=theEvent;
      destNode=event.NR_CopyFragDone.dest_node;
      tableId=event.NR_CopyFragDone.table_id;
      fragmentId=event.NR_CopyFragDone.fragment_id;
    }

    NRCopyFragsCompleted::NRCopyFragsCompleted(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
      event=theEvent;
      destNode=event.NR_CopyFragsCompleted.dest_node;
    }


    NodeFailCompleted::NodeFailCompleted(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
      event=theEvent;
      block=event.NodeFailCompleted.block;
      failedNode=event.NodeFailCompleted.failed_node;
      completingNode=event.NodeFailCompleted.completing_node; 
    }

    NodeFailReported::NodeFailReported(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
      event=theEvent;
      failedNode=event.NODE_FAILREP.failed_node;
      failureState=event.NODE_FAILREP.failure_state;
    }

    ArbitState::ArbitState(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
      event=theEvent;
      code=event.ArbitState.code;
      arbitNode=event.ArbitState.arbit_node;
      ticket0=event.ArbitState.ticket_0;
      ticket1=event.ArbitState.ticket_1;
    }

    ArbitResult::ArbitResult(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
      event=theEvent;
      code=event.ArbitResult.code; 
      arbitNode=event.ArbitResult.arbit_node;
      ticket0=event.ArbitResult.ticket_0;
      ticket1=event.ArbitResult.ticket_1;
    }

GCPTakeoverStarted::GCPTakeoverStarted(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
}

GCPTakeoverCompleted::GCPTakeoverCompleted(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
}

LCPTakeoverStarted::LCPTakeoverStarted(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
}

LCPTakeoverCompleted::LCPTakeoverCompleted(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  state=event.LCP_TakeoverCompleted.state;
}

TransReportCounters::TransReportCounters(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  transCount=event.TransReportCounters.trans_count;
  commitCount=event.TransReportCounters.commit_count;
  readCount=event.TransReportCounters.read_count;
  simpleReadCount=event.TransReportCounters.simple_read_count;
  writeCount=event.TransReportCounters.write_count;
  attrinfoCount=event.TransReportCounters.attrinfo_count;
  concOpCount=event.TransReportCounters.conc_op_count;
  abortCount=event.TransReportCounters.abort_count;
  scanCount=event.TransReportCounters.scan_count;
  rangeScanCount=event.TransReportCounters.range_scan_count;
}

OperationReportCounters::OperationReportCounters(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  ops=event.OperationReportCounters.ops;
}

TableCreated::TableCreated(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  tableId=event.TableCreated.table_id;
}

JobStatistic::JobStatistic(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  meanLoopCount=event.JobStatistic.mean_loop_count;
}

SendBytesStatistic::SendBytesStatistic(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  toNode=event.SendBytesStatistic.to_node;
  meanSentBytes=event.SendBytesStatistic.mean_sent_bytes;
}

ReceiveBytesStatistic::ReceiveBytesStatistic(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  fromNode=event.ReceiveBytesStatistic.from_node;
  meanReceivedBytes=event.ReceiveBytesStatistic.mean_received_bytes;
}

MemoryUsage::MemoryUsage(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  gth=event.MemoryUsage.gth;
  pageSizeKb=event.MemoryUsage.page_size_kb;
  pagesUsed=event.MemoryUsage.pages_used;
  pagesTotal=event.MemoryUsage.pages_total;
  block=event.MemoryUsage.block;
}

TransporterError::TransporterError(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  toNode=event.TransporterError.to_node;
  code=event.TransporterError.code;
}

TransporterWarning::TransporterWarning(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  toNode=event.TransporterWarning.to_node;
  code=event.TransporterWarning.code;
}

MissedHeartbeat::MissedHeartbeat(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  node=event.MissedHeartbeat.node;
  count=event.MissedHeartbeat.count;
}

DeadDueToHeartbeat::DeadDueToHeartbeat(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  node=event.DeadDueToHeartbeat.node;
}

Warning::Warning(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
}

SentHeartbeat::SentHeartbeat(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  node=event.SentHeartbeat.node;
}

CreateLogBytes::CreateLogBytes(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  node=event.CreateLogBytes.node;
}

InfoEvent::InfoEvent(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
}


EventBufferStatus::EventBufferStatus(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  usage=event.EventBufferStatus.usage;
  alloc=event.EventBufferStatus.alloc;
  max=event.EventBufferStatus.max;
  applyGciL=event.EventBufferStatus.apply_gci_l;
  applyGciH=event.EventBufferStatus.apply_gci_h;
  latestGciL=event.EventBufferStatus.latest_gci_l;
  latestGciH=event.EventBufferStatus.latest_gci_h;
}


BackupStarted::BackupStarted(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  startingNode=event.BackupStarted.starting_node;
  backupId=event.BackupStarted.backup_id;
}

BackupFailedToStart::BackupFailedToStart(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  startingNode=event.BackupFailedToStart.starting_node;
  error=event.BackupFailedToStart.error;
}

BackupCompleted::BackupCompleted(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  startingNode=event.BackupCompleted.starting_node;
  backupId=event.BackupCompleted.backup_id; 
  startGci=event.BackupCompleted.start_gci;
  stopGci=event.BackupCompleted.stop_gci;
  numRecords=event.BackupCompleted.n_records; 
  numLogRecords=event.BackupCompleted.n_log_records;
  numBytes=event.BackupCompleted.n_bytes;
  numLogBytes=event.BackupCompleted.n_log_bytes;
}

BackupAborted::BackupAborted(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  startingNode=event.BackupAborted.starting_node;
  backupId=event.BackupAborted.backup_id;
  error=event.BackupAborted.error;
}

SingleUser::SingleUser(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  eventType=event.SingleUser.type;
  nodeId=event.SingleUser.node_id;
}

StartReport::StartReport(const ndb_logevent & theEvent) : BaseEvent(theEvent) {
  event=theEvent;
  reportType=event.StartReport.report_type;
  remainingTime=event.StartReport.remaining_time;
  bitmaskSize=event.StartReport.bitmask_size;
  //bitmaskData[0]=event.StartReport.bitmask_data[0];
}

