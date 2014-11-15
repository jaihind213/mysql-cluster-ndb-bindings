require 'mysql/cluster/mgmapi'

# This program doesn't actually do much yet

Mgmapi::ndb_init
h=Mgmapi::ndb_mgm_create_handle
Mgmapi::ndb_mgm_set_connectstring(h,"127.0.0.1")
Mgmapi::ndb_mgm_connect(h,1,1,1)
a=[15,Mgmapi::NDB_MGM_EVENT_CATEGORY_STATISTIC]
le=Mgmapi::ndb_mgm_create_logevent_handle(h,[a])
if le==0
  puts "crap"
end

conf=Mgmapi::ndb_mgm_get_configuration(h,0)

event=Mgmapi::NdbLogevent.new

run=true
while run
  ret = Mgmapi::ndb_logevent_get_next(le,event,5000)
  if ret != 0
    run=false
  end
  puts event.type
  puts event.source_nodeid
end


