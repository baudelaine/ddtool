#!/bin/bash
curl --cookie cookies.txt --cookie-jar cookies.txt http://localhost/ddtool/Reset
curl --cookie cookies.txt --cookie-jar cookies.txt --data @WebContent/js/model0.json http://localhost/ddtool/SendQuerySubjects
#jq --arg id "FKSSAMPLINGPLAN_SSAMPLEF" '(.[].relations[] | select(._id == $id).ref) = true' S_SAMPLE_ALIAS.json
