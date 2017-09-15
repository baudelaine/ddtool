var curSelTable = "";
var curSelModFKTbl = "";
var curSelModPKTbl = "";

var relation = {
	_id: "",
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
	father_ids: []
};

$(document).ready(function() {
	$(document).ready(function(){
	    $("div.content").click(function(){
	        $("div#divLoading").addClass('show');
	    });
	});
	GetTables($('#tables'));
	buildRelsTable();

});

$(document)
.ajaxStart(function(){
    $("div#divLoading").addClass('show');
		$("div#modDivLoading").addClass('show');
})
.ajaxStop(function(){
    $("div#divLoading").removeClass('show');
		$("div#modDivLoading").removeClass('show');
});

$('#modFKTables').change(function () {
    selectedText = $(this).find("option:selected").text();
		$('#modFKTableAlias').val(selectedText);
		curSelModFKTbl = selectedText;
});

$('#modPKTables').change(function () {
    selectedText = $(this).find("option:selected").text();
		$('#modPKTableAlias').val(selectedText);
		curSelModPKTbl = selectedText;
});

$('#tables').change(function () {
    var selectedText = $(this).find("option:selected").text();
		$('#alias').val(selectedText);
    curSelTable = selectedText;
});

$('#newRowModal').on('show.bs.modal', function (e) {
  // do something...
	GetTables($('#modFKTables'));
	GetTables($('#modPKTables'));

})

function showalert(message, alerttype) {

    $('#alert_placeholder').append('<div id="alertdiv" class="alert ' +
		alerttype + ' input-sm"><span>' + message + '</span></div>')

    setTimeout(function() {

      $("#alertdiv").remove();

    }, 2500);
}

$("a[href='#querySubject']").on('shown.bs.tab', function(e) {
			$("#relsTable").hide();
			$("div#relsToolbar").hide();

});

$("a[href='#fields']").on('shown.bs.tab', function(e) {
			 $("#relsTable").hide();
 			$("div#relsToolbar").hide();
});

$("a[href='#reference']").on('shown.bs.tab', function(e) {
			$("#relsTable").show();
			$("div#relsToolbar").show();
			// $('#relsTable').bootstrapTable("filterBy", {type: ['Final', 'Ref'], key_type: ['F','P']});
 });

 // or even this one if we want the earlier event
 $("a[href='#final']").on('show.bs.tab', function(e) {
			$("#relsTable").show();
			$("div#relsToolbar").show();
			// $('#relsTable').bootstrapTable("filterBy", {type: 'Final', key_type: 'F'});
 });

function buildRelsTable(){

    var cols = [];
    cols.push({field:"checkBox", checkbox: "true"});
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
				uniqueId: "_id",
				idField: "index",
				toolbar: "#relsToolbar"
    });

		// $('#relsTable').bootstrapTable('hideColumn', 'ref');
}

function indexFormatter(value, row, index) {
	row.index = index;
  return index;
}

// !!!!!!!!!!! Don't remove !!!!!!!!!!!
$('#relsTable').bootstrapTable({});
// !!!!!!!!!!! Don't remove !!!!!!!!!!!

$('#relsTable').on('check.bs.table', function(row, $element){

	console.log("row");
	console.log(row);
	console.log("$element");
	console.log($element);

});

$('#relsTable').on('click-row.bs.table', function (e, row, $element) {
	// rowNum = $element.index() + 1;
	// console.log("rowNum=" + rowNum);
});


$('#relsTable').on('click-cell.bs.table', function(field, value, row, $element){

	console.log("field: " + field);
	console.log("value: " + value);

	if (value == "fin") {

		if ($element.father == false) {
			$element.father = true;
			$element.fin = true;
			GetAllKeys($element);
			return;
			// $element.isFather = true;
			// $element.final = '*FINAL*';

			// $('#relsTable').bootstrapTable('updateCell', {
			// 	row: $element.index,
			// 	field: value,
			// 	value: $element.final
			// });
		}

		if ($element.father == true) {

			RemoveImportedKeys($element);
			$element.father = false;
			$element.fin = false;
			SyncRelations();
			// $('#relsTable').bootstrapTable("filterBy", {type: 'Final', key_type: 'F'});
			$("a[href='#final']").tab('show');
			return;

			// $('#relsTable').bootstrapTable('updateCell', {
			// 	row: $element.index,
			// 	field: value,
			// 	value: $element.final
			// });
		}

	// $element.father = $element.father == false && true || false;
	// // $element.final = $element.final == '' && 'Checked' || '';
	// checkglyph = ['<a class="checked" href="javascript:void(0)" title="Checked">','<i class="glyphicon glyphicon-ok"></i>','</a>'].join('');
	// $element.fin = $element.fin == false && true || false;
	// refreshTable($('#relsTable'));

	}

});

function RemoveImportedKeys(o){

        indexes2rm = [];

        var recurse = function(o){
                tableData = $('#relsTable').bootstrapTable("getData");
                tableData.forEach(function(e){
                        if(e.father_ids.indexOf(o._id) > -1){
                                console.log("RemoveImportedKeys _id found: " + e._id);
                                console.log("RemoveImportedKeys father_ids.length: " + e.father_ids.length);
                                if(e.father_ids.length == 1){
                                        indexes2rm.push(e._id);
                                        console.log("RemoveImportedKeys push to indexes2rm: " + e._id);
                                }
                                if(e.father_ids.length > 1){
                                        e.father_ids.splice(e.father_ids.indexOf(o._id), 1);
                                        console.log("RemoveImportedKeys remove from father_ids: " + e._id);
                                }
                                return recurse(tableData[e.index]);
                        }
                        else {
                                return;
                        }
                });
        };
        recurse(o);

        $('#relsTable').bootstrapTable('remove', {
      field: '_id',
      values: indexes2rm
  });
}


function RemoveImportedKeysByIndex(o){

	indexes2rm = [];

	var recurse = function(o){
		tableData = $('#relsTable').bootstrapTable("getData");
		tableData.forEach(function(e){
			if(e.fatherIndexes.indexOf(o.index) > -1){
				console.log("RemoveImportedKeys index found: " + e.index);
				console.log("RemoveImportedKeys fatherIndexes.length: " + e.fatherIndexes.length);
				if(e.fatherIndexes.length == 1){
					indexes2rm.push(e.index);
					console.log("RemoveImportedKeys push to indexes2rm: " + e.index);
				}
				if(e.fatherIndexes.length > 1){
					e.fatherIndexes.splice(e.fatherIndexes.indexOf(o.index), 1);
					console.log("RemoveImportedKeys remove from fatherIndexes: " + e.index);
				}
				return recurse(tableData[e.index]);
			}
			else {
				return;
			}
		});
	};
	recurse(o);

	$('#relsTable').bootstrapTable('remove', {
      field: 'index',
      values: indexes2rm
  });
}


function refreshTable($table){
	var data = $table.bootstrapTable("getData");
	$table.bootstrapTable('load', data);
}

function DuplicateRow(){

	// $('#relsTable').bootstrapTable('filterBy', {});

	selections = $('#relsTable').bootstrapTable('getSelections');
	if (selections == "") {
		showalert("DuplicateRow(): no row selected.", "alert-warning");
		return;
	}

	$.each(selections, function(i, o){

		nextIndex = o.index + 1;
		console.log("nextIndex=" + nextIndex);
		var newRow = $.extend({}, o);
		newRow.checkBox = false;
		newRow.pktable_alias = "";
		newRow.index = nextIndex;
		console.log("newRow.index");
		console.log(newRow.index);

		$('#relsTable').bootstrapTable('insertRow', {index: nextIndex, row: newRow});

	});

	$('#relsTable').bootstrapTable('uncheckAll');


}

function AddRow(){

	var newRow = {};
	newRow.checkBox = false;
	newRow.fk_name = $('#modFKName').val();
	newRow.fktable_alias = $('#modFKTableAlias').val();
	newRow.fktable_name = curSelModFKTbl;
	newRow.pktable_alias = $('#modPKTableAlias').val();
	newRow.pktable_name = curSelModPKTbl;
	newRow.relashionship = $('#modRelashionship').val();
	newRow.type = "Final";
	newRow.final = "Final";
	newRow.seqs = [];

	nextIndex = $('#relsTable').bootstrapTable("getData").length;
	console.log("nextIndex=" + nextIndex);
	$('#relsTable').bootstrapTable('insertRow', {index: nextIndex, row: newRow});

	$('#newRowModal').modal('hide');

}

function RemoveRow(){

	var $table = $('#relsTable');

	selections = $table.bootstrapTable('getSelections');
	if (selections == "") {
		showalert("RemoveRow(): no row selected.", "alert-warning");
		return;
	}

  var indexes = $.map($table.bootstrapTable('getSelections'), function (row) {
      return row.index;
  });
  $table.bootstrapTable('remove', {
      field: 'index',
      values: indexes
  });

	$('#relsTable').bootstrapTable('uncheckAll');

}

function AppendSelections(){

	selections = $('#relsTable').bootstrapTable('getSelections');
	console.log("selections=" + selections);
	var success = "OK";

	console.log("selections=" + selections);
	if (selections == "") {
		showalert("AppendSelections(): no row selected.", "alert-warning");
		return;
	}

	var objs = []

	$.each(selections, function(k, v){

		var obj = JSON.stringify(v);
		objs += obj + "\r\n";

	});

	console.log("objs=" + objs);

	$.ajax({
		type: 'POST',
		url: "AppendSelections",
		dataType: 'json',
		data: objs,

		success: function(data) {
			success = "OK";
		},
		error: function(data) {
			console.log(data);
			success = "KO";
		}
	});

	if (success == "OK") {
		showalert("AppendSelections(): selections was sent to server.", "alert-success");
	}
	else {
		showalert("AppendSelections() failed.", "alert-danger");
	}

}

function SyncRelations(){

	// $('#relsTable').bootstrapTable("filterBy", {type: ['Final', 'Ref'], key_type: ['F','P']});
	var data = $('#relsTable').bootstrapTable('getData');

	var objs = [];

	$.each(data, function(k, v){
		var obj = JSON.stringify(v);
		objs += obj + "\r\n";
	});

	$.ajax({
		type: 'POST',
		url: "SyncRelations",
		dataType: 'json',
		data: objs,

		success: function(data) {
			$('#relsTable').bootstrapTable('load', data);
		},
		error: function(data) {
			showalert("SyncRelations() failed.", "alert-danger");
		}
	});

}

function LoadSelections(){

	selections = $('#relsTable').bootstrapTable('getSelections');
	console.log("selections=" + selections);
	var success = "OK";

	console.log("selections=" + selections);
	if (selections == "") {
		showalert("LoadSelections(): no row selected.", "alert-warning");
		return;
	}

	var objs = []

	$.each(selections, function(k, v){

		var obj = JSON.stringify(v);
		objs += obj + "\r\n";

	});

	console.log("objs=" + objs);

	$.ajax({
		type: 'POST',
		url: "LoadSelections",
		dataType: 'json',
		data: objs,

		success: function(data) {
			success = "OK";
		},
		error: function(data) {
			console.log(data);
			success = "KO";
		}
	});

	if (success == "OK") {
		showalert("LoadSelections(): selections was sent to server.", "alert-success");
	}
	else {
		showalert("LoadSelections() failed.", "alert-danger");
	}

}

function GetSelections() {

   $.ajax({
        type: 'POST',
        url: "GetSelections",
        dataType: 'json',

        success: function(data) {
			console.log("data=" + data);
			if ( data == "") {
				showalert("GetSelections(): no selections stored on server.", "alert-info");
				return;
			}
			$('#relsTable').bootstrapTable("load", data);
        },
        error: function(data) {
            console.log(data);
            showalert("GetSelections() failed.", "alert-danger");
        }

    });

}

function Reset() {

	var success = "OK";

	$.ajax({
        type: 'POST',
        url: "Reset",
        dataType: 'json',

        success: function(data) {
			success = "OK";
        },
        error: function(data) {
            console.log(data);
   			success = "KO";
        }

    });

	if (success == "KO") {
		showalert("Reset() failed.", "alert-danger");
	}

	location.reload(true);

}

function ClearSelections() {

	$('#relsTable').bootstrapTable("removeAll");

}

function GetAllKeys(relation) {

	$table_name = $('#tables').find("option:selected").text();
	if ($table_name == undefined || $table_name == 'Choose a table...') {
		showalert("GetAllKeys(): no table selected.", "alert-warning");
		return;
	}

	if(relation == undefined){
		var relation = {};
		relation.pktable_name = $('#tables').find("option:selected").text();
		relation.pktable_alias = $('#alias').val();
		relation.type = "Final";
		relation.father = false;
	}

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

			$('#relsTable').bootstrapTable('load', data);

  	},
      error: function(data) {
          console.log(data);
          showalert("GetAllKeys() failed.", "alert-danger");
    }

  });

	// $('#relsTable').bootstrapTable("filterBy", {type: 'Final', key_type: 'F'});
	$("a[href='#final']").tab('show');

}

function GetImportedKeys(father) {

	var table_name = "";

	if(father == undefined){
		table_name = $('#tables').find("option:selected").text();
	}
	else {
		table_name = father.pktable_name;
	}

	console.log("table_name=" + table_name);

	if (table_name == undefined || table_name == 'Choose a table...') {
		showalert("GetImportedKeys(): no table selected.", "alert-warning");
		return;
	}

  $.ajax({
    type: 'POST',
    url: "GetImportedKeys",
    dataType: 'json',
    data: "table=" + table_name,

    success: function(data) {

			if (data == "") {
				showalert("GetImportedKeys(): " + table_name + " has no importedKey.", "alert-info");
				return;
			}

			$.each(data, function(i, o){

				if(father != undefined){
					o.fktable_alias = father.pktable_alias;
				}
				else {
					o.fktable_alias = $('#alias').val();
				}

				nextIndex = alreadyExists(o);
				// console.log("nextIndex="+nextIndex);

				if (nextIndex > -1){
					o = $('#relsTable').bootstrapTable("getData")[nextIndex];
					if(father != undefined) {
						o.fatherIndexes.push(father.index);
						//Remove duplicate entry
						o.fatherIndexes = Array.from(new Set(o.fatherIndexes));
						//or
						// o.fatherIndexes = [...new Set(o.fatherIndexes)];
					}
					$('#relsTable').bootstrapTable('updateRow', {index: nextIndex, row: o});
					return;
				}

				if(father != undefined) {
					o.fatherIndexes.push(father.index);
					o.fatherIndexes = Array.from(new Set(o.fatherIndexes));
				}
				nextIndex = $('#relsTable').bootstrapTable("getData").length;
				console.log("nextIndex=" + nextIndex);

				$('#relsTable').bootstrapTable('insertRow', {index: nextIndex, row: o});

			});

  	},
      error: function(data) {
          console.log(data);
          showalert("GetImportedKeys() failed.", "alert-danger");
    }

  });

}

function alreadyExists(o) {

	tableData = $('#relsTable').bootstrapTable("getData");
	res = -1;

	tableData.forEach(function(entry) {
			oUID = o.fktable_alias+o.pktable_alias+o.type+o.key_type;
			entryUID = entry.fktable_alias+entry.pktable_alias+entry.type+entry.key_type;
			if ( oUID == entryUID) {
				console.log(entryUID + " already exists");
				// console.log("entry.index="+entry.index);
				res = entry.index;
				return;
			}
	});

	return res;
}

function TestDBConnection() {

    $.ajax({
        type: 'POST',
        url: "TestDBConnection",
        dataType: 'json',

        success: function(data) {
            console.log(data);
            showalert("TestDBConnection() was successfull.", "alert-success");
        },
        error: function(data) {
            console.log(data);
            showalert("TestDBConnection() failed.", "alert-danger");
        }

    });

}

function GetTables(table) {

    $.ajax({
        type: 'POST',
        url: "GetTables",
        dataType: 'json',

        success: function(data) {
            console.log(data);
            $.each(data, function(i, obj){
							//console.log(obj.name);
							table.append('<option class="fontsize">' + obj.name + '</option>');
			            });
			            table.selectpicker('refresh');
			        },
        error: function(data) {
            console.log(data);
            showalert("GetTables() failed.", "alert-danger");
        }
		});

}

function GetTableData(){
		var data = $('#relsTable').bootstrapTable("getData");
		console.log("data=");
		console.log(data);

}
