/* -*- mode: c++; c-basic-offset: 2; indent-tabs-mode: nil; -*-
 *  vim:expandtab:shiftwidth=2:tabstop=2:smarttab:
 *
 *  ndb-bindings: Bindings for the NDB API
 *  Copyright (C) 2008 MySQL
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

%module(directors="1") "Listeners"

%feature("director");    
%typemap(javaimports) SWIGTYPE "
import com.mysql.cluster.mgmj.*;
import com.mysql.cluster.mgmj.events.*;
"

%pragma(java) jniclassimports=%{
import com.mysql.cluster.mgmj.events.*;
%}

%typemap("javapackage") BaseEvent,BaseEvent *,BaseEvent & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") Connected,Connected *,Connected & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") Disconnected,Disconnected *,Disconnected & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") CommunicationOpened,CommunicationOpened *,CommunicationOpened & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") CommunicationClosed,CommunicationClosed *,CommunicationClosed & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") ConnectedApiVersion,ConnectedApiVersion *,ConnectedApiVersion & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") GlobalCheckpointStarted,GlobalCheckpointStarted *,GlobalCheckpointStarted & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") GlobalCheckpointCompleted,GlobalCheckpointCompleted *,GlobalCheckpointCompleted & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") LocalCheckpointStarted,LocalCheckpointStarted *,LocalCheckpointStarted & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") LocalCheckpointCompleted,LocalCheckpointCompleted *,LocalCheckpointCompleted & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") LCPStoppedInCalcKeepGci,LCPStoppedInCalcKeepGci *,LCPStoppedInCalcKeepGci & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") LCPFragmentCompleted,LCPFragmentCompleted *,LCPFragmentCompleted & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") UndoLogBlocked,UndoLogBlocked *,UndoLogBlocked & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") NdbStartStarted,NdbStartStarted *,NdbStartStarted & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") NdbStartCompleted,NdbStartCompleted *,NdbStartCompleted & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") STTORRYRecieved,STTORRYRecieved *,STTORRYRecieved & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") StartPhaseCompleted,StartPhaseCompleted *,StartPhaseCompleted & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") CmRegConf,CmRegConf *,CmRegConf & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") CmRegRef,CmRegRef *,CmRegRef & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") FindNeighbours,FindNeighbours *,FindNeighbours & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") NdbStopStarted,NdbStopStarted *,NdbStopStarted & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") NdbStopCompleted,NdbStopCompleted *,NdbStopCompleted & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") NdbStopForced,NdbStopForced *,NdbStopForced & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") NdbStopAborted,NdbStopAborted *,NdbStopAborted & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") StartREDOLog,StartREDOLog *,StartREDOLog & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") StartLog,StartLog *,StartLog & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") UNDORecordsExecuted,UNDORecordsExecuted *,UNDORecordsExecuted & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") NRCopyDict,NRCopyDict *,NRCopyDict & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") NRCopyDistr,NRCopyDistr *,NRCopyDistr & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") NRCopyFragsStarted,NRCopyFragsStarted *,NRCopyFragsStarted & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") NRCopyFragDone,NRCopyFragDone *,NRCopyFragDone & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") NRCopyFragsCompleted,NRCopyFragsCompleted *,NRCopyFragsCompleted & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") NodeFailCompleted,NodeFailCompleted *,NodeFailCompleted & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") NodeFailReported,NodeFailReported *,NodeFailReported & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") ArbitState,ArbitState *,ArbitState & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") ArbitResult,ArbitResult *,ArbitResult & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") GCPTakeoverStarted,GCPTakeoverStarted *,GCPTakeoverStarted & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") GCPTakeoverCompleted,GCPTakeoverCompleted *,GCPTakeoverCompleted & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") LCPTakeoverStarted,LCPTakeoverStarted *,LCPTakeoverStarted & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") LCPTakeoverCompleted,LCPTakeoverCompleted *,LCPTakeoverCompleted & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") TransReportCounters,TransReportCounters *,TransReportCounters & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") OperationReportCounters,OperationReportCounters *,OperationReportCounters & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") TableCreated,TableCreated *,TableCreated & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") JobStatistic,JobStatistic *,JobStatistic & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") SendBytesStatistic,SendBytesStatistic *,SendBytesStatistic & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") ReceiveBytesStatistic,ReceiveBytesStatistic *,ReceiveBytesStatistic & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") MemoryUsage,MemoryUsage *,MemoryUsage & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") TransporterError,TransporterError *,TransporterError & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") TransporterWarning,TransporterWarning *,TransporterWarning & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") MissedHeartbeat,MissedHeartbeat *,MissedHeartbeat & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") DeadDueToHeartbeat,DeadDueToHeartbeat *,DeadDueToHeartbeat & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") Warning,Warning *,Warning & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") SentHeartbeat,SentHeartbeat *,SentHeartbeat & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") CreateLogBytes,CreateLogBytes *,CreateLogBytes & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") InfoEvent,InfoEvent *,InfoEvent & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") EventBufferStatus,EventBufferStatus *,EventBufferStatus & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") BackupStarted,BackupStarted *,BackupStarted & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") BackupFailedToStart,BackupFailedToStart *,BackupFailedToStart & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") BackupCompleted,BackupCompleted *,BackupCompleted & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") BackupAborted,BackupAborted *,BackupAborted & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") SingleUser,SingleUser *,SingleUser & "com.mysql.cluster.mgmj.events";
%typemap("javapackage") StartReport,StartReport *,StartReport & "com.mysql.cluster.mgmj.events";


%include "mgmapi/listeners.i"


