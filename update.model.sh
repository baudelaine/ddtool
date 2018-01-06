#!/bin/bash

for var in $(cat datas)
	do
		TAB=$(echo $var | cut -d';' -f1)
		COL=$(echo $var | cut -d';' -f2)
		SIZE=$(echo $var | cut -d';' -f3)
		NULLABLE=$(echo $var | cut -d';' -f4)
	
		echo $TAB
		echo $COL
		echo $SIZE

		jq "(.[] | select(.table_name == $TAB) | .fields[] | select(.field_name == $COL)).field_size = $SIZE" model.json | sponge model.json

	done


#jq '.[].fields[] += {"field_size": 0, "nullable":"NO"}'  model-2018-0-4-20-55-43.json | tee model.json

#jq -r '.[] | "\"" + .tabName + "\";\"" + .colName + "\";" + "\(.colSize)" + ";\"" + .isNullable + "\""' schema.json

#jq --arg tab "SOCPF" --arg col "SOMAIL" --arg size 25 '(.[] | select(.table_name == $tab) | select(.fields[].field_name == $col).fields[].field_size) = $size' model.json 


exit 0
