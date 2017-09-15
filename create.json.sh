#!/bin/bash
curl --cookie cookies.txt --cookie-jar cookies.txt http://localhost/ddtool/Reset
curl --cookie cookies.txt --cookie-jar cookies.txt --data "table=PROJECT&alias=PROJECT_ALIAS&type=Final" http://localhost/ddtool/GetQuerySubjects
#curl --cookie cookies.txt --cookie-jar cookies.txt --data "table=S_BATCH&alias=S_BATCH&type=Final&qs_id=S_SAMPLEFinal&r_id=FKSBATCH_SSAMPLEF&pk=false" http://localhost/ddtool/GetQuerySubjects | jshon -S
# curl --cookie cookies.txt --cookie-jar cookies.txt --data "table=SYSUSER&alias=SYSUSER&type=Ref&qs_id=S_BATCHFinal&r_id=FKSYSUSER_SBATCH_RCF&pk=false" http://localhost/ddtool/GetQuerySubjects | jshon -S
# curl --cookie cookies.txt --cookie-jar cookies.txt --data "table=SYSUSER&alias=SYSUSER&type=Ref&qs_id=S_BATCHFinal&r_id=FKSYSUSER_SBATCHF&pk=false" http://localhost/ddtool/GetQuerySubjects | jshon -S
#curl --cookie cookies.txt --cookie-jar cookies.txt --data "table=SYSUSER&alias=SYSUSER&type=Ref&qs_id=S_SAMPLEFinal&r_id=FKSYSUSER_SSAMPLEF&pk=false" http://localhost/ddtool/GetQuerySubjects | jshon -S
# curl --cookie cookies.txt --cookie-jar cookies.txt --data "table=SYSUSER&alias=SYSUSER&type=Ref&qs_id=S_SAMPLEFinal&r_id=FKSYSUSER_SSAMPLE_SUBF&pk=false" http://localhost/ddtool/GetQuerySubjects | jshon -S
#curl --cookie cookies.txt --cookie-jar cookies.txt --data "table=REAGENTLOT&alias=REAGENTLOT&type=Ref&qs_id=S_SAMPLEFinal&r_id=FKREAGENTLOT_SSAMPLE&pk=false" http://localhost/ddtool/GetQuerySubjects | jshon -S
# curl --cookie cookies.txt --cookie-jar cookies.txt --data "table=DEPARTMENT&alias=DEPARTMENT&type=Ref&qs_id=S_BATCHFinal&r_id=FKDEPARTMENT_SBATCHF&pk=false" http://localhost/ddtool/GetQuerySubjects | jshon -S
# curl --cookie cookies.txt --cookie-jar cookies.txt --data "table=DEPARTMENT&alias=DEPARTMENT&type=Ref&qs_id=SYSUSERRef&r_id=FKDEPARTMENT_SYSUSER_DDF&pk=false" http://localhost/ddtool/GetQuerySubjects | jshon -S > all.json
# curl --cookie cookies.txt --cookie-jar cookies.txt --data "table=DEPARTMENT&alias=DEPARTMENT&type=Ref&qs_id=SYSUSERRef&r_id=FK_DEPARTMENT_SYSUSER_BDF&pk=false" http://localhost/ddtool/GetQuerySubjects | jshon -S
#curl --cookie cookies.txt --cookie-jar cookies.txt --data "table=REAGENTTYPE&alias=REAGENTTYPE&type=Ref&qs_id=REAGENTLOTRef&r_id=FKREAGENTTYPE_REAGENTLOT&pk=false" http://localhost/ddtool/GetQuerySubjects | jshon -S
#curl --cookie cookies.txt --cookie-jar cookies.txt --data "table=PARAMLIST&alias=PARAMLIST&type=Ref&qs_id=REAGENTTYPERef&r_id=FKPARAMLIST_REAGENTTYPE&pk=false" http://localhost/ddtool/GetQuerySubjects | jshon -S > all.json
# curl --cookie cookies.txt --cookie-jar cookies.txt http://localhost/ddtool/SendQuerySubjects
#jq --arg id "FKSSAMPLINGPLAN_SSAMPLEF" '(.[].relations[] | select(._id == $id).ref) = true' S_SAMPLE_ALIAS.json
