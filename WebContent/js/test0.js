var relation = {
	_id: "",
	_ref: null,
	fktable_name: "",
	fktable_alias: "",
	type: "",
	key_name: "",
	key_type: "",
	pktable_name: "",
	pktable_alias: "",
	relashionship: "",
	fin: false,
	ref: false,
	father: false,
	father_ids: [],
	seqs: [],
	checkbox: false
};

$(document).ready(function() {
	buildRelsTable();

});

// !!!!!!!!!!! Don't remove !!!!!!!!!!!
$('#relsTable').bootstrapTable({});
// !!!!!!!!!!! Don't remove !!!!!!!!!!!


function buildRelsTable(){

    var cols = [];
    cols.push({field:"checkbox", checkbox: "true"});
		cols.push({field:"index", title: "index", formatter: "indexFormatter", sortable: false});
		cols.push({field:"_id", title: "_id", sortable: false});
    cols.push({field:"fktable_name", title: "fktable_name", sortable: false });
		cols.push({field:"fktable_alias", title: "fktable_alias", editable: false, sortable: false});
		cols.push({field:"type", title: "type", sortable: false});
    cols.push({field:"key_name", title: "key_name", sortable: false});
		cols.push({field:"key_type", title: "key_type"});
		cols.push({field:"pktable_name", title: "pktable_name", sortable: false});
		cols.push({field:"pktable_alias", title: "pktable_alias", sortable: false, editable: {
			 	type: "text"
			}
		});
		cols.push({field:"relashionship", title: "relashionship", editable: {type: "textarea"}});
		cols.push({field:"fin", title: "fin", align: "center"});
		cols.push({field:"ref", title: "ref", align: "center"});
		cols.push({field:"father", title: "father"});
		cols.push({field:"father_ids", title: "father_ids"});

    $('#relsTable').bootstrapTable("destroy").bootstrapTable({
        columns: cols,
				search: true,
				showRefresh: true,
				showColumns: true,
				showToggle: true,
				pagination: false,
				showPaginationSwitch: true,
				// uniqueId: "_id",
				// idField: "index",
				toolbar: "#relsToolbar"
    });

		// $('#relsTable').bootstrapTable('hideColumn', 'ref');
}

function indexFormatter(value, row, index) {
	row.index = index;
  return index;
}

function GetAllKeys(relation) {


	var relation = {};
	relation.pktable_name = 'PROJECT';
	relation.pktable_alias = 'PROJECTWWW';
	relation.type = "Final";
	relation.father = false;


	// console.log("table_name=" + relation.pktable_name);
	// console.log("table_alias=" + relation.pktable_alias);
	// console.log("type=" + relation.type);
	// console.log("father=" + relation.father);

  $.ajax({
    type: 'POST',
    url: "GetAllKeys",
    dataType: 'json',
    data: JSON.stringify(relation),

    success: function(data) {

			if (data == "") {
				showalert("GetAllKeys(): " + table_name + " has no key.", "alert-info");
				return;
			}

      console.log("data");
      console.log(data);

			$('#relsTable').bootstrapTable('load', data);

  	},
      error: function(data) {
          console.log(data);
          showalert("GetAllKeys() failed.", "alert-danger");
    }

  });

	// $('#relsTable').bootstrapTable("filterBy", {type: 'Final', key_type: 'F'});

}
