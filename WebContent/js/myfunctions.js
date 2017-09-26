
var datas = [];
var tables = [];
var modelList = [];
var $tableList = $('#tables');
var $datasTable = $('#DatasTable');
var $navTab = $('#navTab');
var $refTab = $("a[href='#Reference']");
var $finTab = $("a[href='#Final']");
var $qsTab = $("a[href='#QuerySubject']");
var activeTab = "Final";
var $activeSubDatasTable;
var $newRowModal = $('#newRowModal');
var $modelListModal = $('#modModelList');
// var url = "js/PROJECT.json";

var relationCols = [];
// relationCols.push({field:"checkbox", checkbox: "true"});
relationCols.push({field:"index", title: "index", formatter: "indexFormatter", sortable: false});
relationCols.push({field:"_id", title: "_id", sortable: true});
relationCols.push({field:"key_name", title: "key_name", sortable: true});
relationCols.push({field:"key_type", title: "key_type", sortable: true});
relationCols.push({field:"pktable_name", title: "pktable_name", sortable: true});
relationCols.push({field:"pktable_alias", title: "pktable_alias", sortable: true, editable: {type: "text"}});
relationCols.push({field:"relationship", title: "relationship", editable: {type: "textarea", rows: 4}});
relationCols.push({field:"fin", title: "fin", formatter: "boolFormatter", align: "center"});
relationCols.push({field:"ref", title: "ref", formatter: "boolFormatter", align: "center"});
relationCols.push({field:"duplicate", title: '<i class="glyphicon glyphicon-duplicate"></i>', formatter: "duplicateFormatter", align: "center"});
relationCols.push({field:"remove", title: '<i class="glyphicon glyphicon-trash"></i>', formatter: "removeFormatter", align: "center"});
// relationCols.push({field:"operate", title: "operate", formatter: "operateRelationFormatter", align: "center", events: "operateRelationEvents"});

// relationCols.push({field:"linker", formatter: "boolFormatter", align: "center", title: "linker"});
// relationCols.push({field:"linker_ids", title: "linker_ids"});

var newRelationCols = [];

newRelationCols.push();

var qsCols = [];
// qsCols.push({field:"checkbox", checkbox: "true"});
qsCols.push({field:"index", title: "index", formatter: "indexFormatter", sortable: false});
qsCols.push({field:"_id", title: "_id", sortable: true});
qsCols.push({field:"table_name", title: "table_name", sortable: true});
qsCols.push({field:"table_alias", title: "table_alias", editable: false, sortable: true});
qsCols.push({field:"type", title: "type", sortable: true});
qsCols.push({field:"visible", title: "visible", formatter: "boolFormatter", align: "center", sortable: false});
qsCols.push({field:"filter", title: "filter", editable: {type: "textarea"}, sortable: true});
qsCols.push({field:"label", title: "label", editable: {type: "textarea"}, sortable: true});
qsCols.push({field:"recurseCount", title: '<i class="glyphicon glyphicon-repeat" title="Set recurse count"></i>', editable: {
  type: "select",
  value: 1,
  // source: [
  //   {value: 1, text: 1},
  //   {value: 2, text: 2},
  //   {value: 3, text: 3},
  //   {value: 4, text: 4},
  //   {value: 5, text: 5}
  //   ],
  source: function(){
    var result = [];
    for(var i = 1; i < 21; i++){
      var option = {};
      option.value = i;
      option.text = i;
      result.push(option);
    }
    return result;
  },
  align: "center"}
});
qsCols.push({field:"addPKRelation", title: '<i class="glyphicon glyphicon-magnet" title="Add PK relation(s)"></i>', formatter: "addPKRelationFormatter", align: "center"});
qsCols.push({field:"addRelation", title: '<i class="glyphicon glyphicon-plus-sign" title="Add new relation"></i>', formatter: "addRelationFormatter", align: "center"});

var fieldCols = [];
fieldCols.push({field:"index", title: "index", formatter: "indexFormatter", sortable: false});
fieldCols.push({field:"field_name", title: "field_name", sortable: true });
fieldCols.push({field:"label", title: "label", editable: {type: "text"}, sortable: true});
fieldCols.push({field:"traduction", title: "traduction", formatter: "boolFormatter", align: "center", sortable: false});
fieldCols.push({field:"visible", title: "visible", formatter: "boolFormatter", align: "center", sortable: false});
fieldCols.push({field:"field_type", title: "field_type", editable: false, sortable: true});
fieldCols.push({field:"timezone", title: "timezone", formatter: "boolFormatter", align: "center", sortable: false});


$(document)
.ready(function() {
  $('#DatasToolbar').hide();
  ChooseTable($tableList);
  buildTable($datasTable, qsCols, datas, true);

})
.ajaxStart(function(){
    $("div#divLoading").addClass('show');
		$("div#modDivLoading").addClass('show');
})
.ajaxStop(function(){
    $("div#divLoading").removeClass('show');
		$("div#modDivLoading").removeClass('show');
});

$tableList.change(function () {
    var selectedText = $(this).find("option:selected").val();
		$('#alias').val(selectedText);
});

$navTab.on('shown.bs.tab', function(event){
    activeTab = $(event.target).text();         // active tab
		console.log("activeTab=" + activeTab);
    previousTab = $(event.relatedTarget).text();  // previous tab
		console.log("previousTab=" + previousTab);
});

$qsTab.on('shown.bs.tab', function(e) {
  buildTable($datasTable, qsCols, datas, true, fieldCols, "fields");
  $datasTable.bootstrapTable('showColumn', 'visible');
  $datasTable.bootstrapTable('showColumn', 'filter');
  $datasTable.bootstrapTable('showColumn', 'label');
  $datasTable.bootstrapTable('hideColumn', 'operate');
  $datasTable.bootstrapTable('hideColumn', 'addRelation');
  $datasTable.bootstrapTable('hideColumn', 'addPKRelation');
  $datasTable.bootstrapTable('hideColumn', 'recurseCount');
});

$finTab.on('shown.bs.tab', function(e) {
  buildTable($datasTable, qsCols, datas, true, relationCols, "relations");
  // $datasTable.bootstrapTable("filterBy", {type: ['Final'], key_type: ['F', 'C']});
  // $datasTable.bootstrapTable('hideColumn', 'fin');
  // $datasTable.bootstrapTable('showColumn', 'ref');
  $datasTable.bootstrapTable("filterBy", {type: ['Final']});
  $datasTable.bootstrapTable('showColumn', 'operate');
  $datasTable.bootstrapTable('hideColumn', 'visible');
  $datasTable.bootstrapTable('hideColumn', 'filter');
  $datasTable.bootstrapTable('hideColumn', 'label');
  $datasTable.bootstrapTable('hideColumn', 'recurseCount');
  $datasTable.bootstrapTable('showColumn', 'addRelation');
  $datasTable.bootstrapTable('hideColumn', 'addPKRelation');
});

$refTab.on('shown.bs.tab', function(e) {
  buildTable($datasTable, qsCols, datas, true, relationCols, "relations");
  // $datasTable.bootstrapTable("filterBy", {});
  // $datasTable.bootstrapTable("filterBy", {type: ['Final', 'Ref'], key_type: ['F','P', 'C']});
  // $datasTable.bootstrapTable('hideColumn', 'fin');
  // $datasTable.bootstrapTable('showColumn', 'ref');
  $datasTable.bootstrapTable('showColumn', 'operate');
  $datasTable.bootstrapTable('hideColumn', 'visible');
  $datasTable.bootstrapTable('hideColumn', 'filter');
  $datasTable.bootstrapTable('hideColumn', 'label');
  $datasTable.bootstrapTable('showColumn', 'addPKRelation');
  $datasTable.bootstrapTable('showColumn', 'addRelation');
  $datasTable.bootstrapTable('showColumn', 'recurseCount');
});

$datasTable.on('editable-save.bs.table', function (editable, field, row, oldValue, $el) {
  if(field == "pktable_alias"){
    var newValue = row.pktable_alias;
    if($activeSubDatasTable != undefined){
      updateCell($activeSubDatasTable, row.index, 'relationship', row.relationship.split("[" + oldValue + "]").join("[" + newValue + "]"));
    }
  }
});

$datasTable.on('reset-view.bs.table', function(){
  console.log("++++++++++++++on passe dans reset-view");
  if($activeSubDatasTable != undefined){
    var v = $activeSubDatasTable.bootstrapTable('getData');
    console.log("+++++++++++ $activeSubDatasTable");
    console.log(v);
    var $tableRows = $activeSubDatasTable.find('tbody tr');
    console.log("++++++++++ $tableRows");
    console.log($tableRows);
    $.each(v, function(i, row){
      // console.log("row.ref");
      // console.log(row.ref);
      console.log("row.fin");
      console.log(row.fin);
      if(row.fin == true || row.ref == true){
        $tableRows.eq(i).find('a').eq(0).editable('disable');
        // $tableRows.eq(i).find('a').editable('disable');
      }
    });
  }
});

$datasTable.on('expand-row.bs.table', function (index, row, $detail) {
  console.log("index: ");
  console.log(index);
  console.log("row: ");
  console.log(row);
  console.log("$detail: ");
  console.log($detail);

});

$newRowModal.on('show.bs.modal', function (e) {
  // do something...
	// ChooseQuerySubject($('#modQuerySubject'));
  $('#modPKTables').empty();
  $('#modPKColumn').empty();
  $('#modPKColumn').selectpicker('refresh');
  $.each(tables, function(i, obj){
    var option = '<option class="fontsize" value=' + obj.name + '>' + obj.name + ' (' + obj.keyCount + ') (' + obj.seqCount + ')' + '</option>';
    $('#modPKTables').append(option);
  });
  $('#modPKTables').selectpicker('refresh');
	// ChooseTable($('#modPKTables'));
  // $(this)
  // .find('.modal-body')
  // .load("sqel.html", function(){
  //});

})

$modelListModal.on('shown.bs.modal', function() {
    $(this).find('.modal-body').empty();
    var models = modelList;
    var list = '<div class="list-group">';

    $.each(models, function(i, obj){
        list += '<a href="#" class="list-group-item" onClick="OpenModel(' + obj.id + '); return false;">' + obj.name + '</a>';
    });
    list += '<div class="list-group">';
    $(this).find('.modal-body').append(list);

  });

$('#modPKTables').change(function () {
    var selectedText = $(this).find("option:selected").val();
		$('#modPKTableAlias').val(selectedText);
    var newText = 'CK_' + $('#modQuerySubject').text().split(" - ")[0] + '_' + selectedText;
    $('#modKeyName').val(newText);
    // var relationship = $('#modRelationship').text() + "[" + selectedText + "].[]";
    // $('#modRelationship').val(relationship);
    ChooseField($('#modPKColumn'), selectedText);
});

$('#modPKTableAlias').change(function () {
    var newText = 'CK_' + $('#modQuerySubject').text().split(" - ")[0] + '_' + $('#modPKTableAlias').val();
    $('#modKeyName').val(newText);
    // var relationship = $('#modRelationship').text() + "[" + $('#modPKTableAlias').val() + "].[]";
    // $('#modRelationship').val(relationship);
});

window.operateRelationEvents = {
    'click .duplicate': function (e, value, row, index) {
      console.log("+++++ on entre dans click .duplicate");
      console.log(e);
      console.log(value);
      console.log(row);
      console.log(index);

        // alert('You click duplicate action, row: ' + JSON.stringify(row));
        $activeSubDatasTable.bootstrapTable("filterBy", {});
        nextIndex = row.index + 1;
        console.log("nextIndex=" + nextIndex);
        var newRow = $.extend({}, row);
        newRow.checkbox = false;
        newRow.pktable_alias = "";
        newRow.fin = false;
        newRow.ref = false;
        newRow.relationship = newRow.relationship.replace(/ = \[FINAL\]\./g, " = ");
        newRow.relationship = newRow.relationship.replace(/ = \[REF\]\./g, " = ");
        console.log("newRow");
        console.log(newRow);
        $activeSubDatasTable.bootstrapTable('insertRow', {index: nextIndex, row: newRow});
        console.log("+++++ on sort de click .duplicate");

    },
    'click .remove': function (e, value, row, index) {
        $activeSubDatasTable.bootstrapTable('remove', {
            field: 'index',
            values: [row.index]
        });
    }
};

window.operateQSEvents = {
    'click .addRelation': function (e, value, row, index) {

      console.log("index=" + index);
      // $datasTable.bootstrapTable('expandAllRows');
      $datasTable.bootstrapTable('expandRow', index);

      console.log("++++++++++++++on passe dans window.operateQSEvents.add");
      if($activeSubDatasTable != ""){

        var v = $activeSubDatasTable.bootstrapTable('getData');
        console.log("+++++++++++ $activeSubDatasTable");

        console.log(v);
        $newRowModal.modal('toggle');
        var qs = row.table_alias + ' - ' + row.type + ' - ' + row.table_name;
        // $('#modQuerySubject').selectpicker('val', qs);

        $('#modQuerySubject').text(qs);
        $('#modKeyName').val("CK_" + row.table_alias);
        $('#modPKTableAlias').val("");
        // $('#modRelathionship').val("[" + row.type.toUpperCase() + "].[" + row.table_alias + "].[] = ");
      }

    },
    'click .expandAllQS': function (e, value, row, index) {
      $datasTable.bootstrapTable("expandAllRows")
    },
    'click .collapseAllQS': function (e, value, row, index) {
      $datasTable.bootstrapTable("collapseAllRows")
    }
};

function operateRelationFormatter(value, row, index) {
    return [
        '<a class="duplicate" href="javascript:void(0)" title="Duplicate">',
        '<i class="glyphicon glyphicon-duplicate"></i>',
        '</a>  ',
        '<a class="remove" href="javascript:void(0)" title="Remove">',
        '<i class="glyphicon glyphicon-trash"></i>',
        '</a>'
    ].join('');
}

function operateQSFormatter(value, row, index) {
    return [
        '<a class="addRelation" href="javascript:void(0)" title="Add Relation">',
        '<i class="glyphicon glyphicon-plus-sign"></i>',
        '</a>  ',
        '<a class="expandAllQS" href="javascript:void(0)" title="Expand all QS">',
        '<i class="glyphicon glyphicon-resize-full"></i>',
        '</a>  ',
        '<a class="collapseAllQS" href="javascript:void(0)" title="Collapse all QS">',
        '<i class="glyphicon glyphicon-resize-small"></i>',
        '</a>'
    ].join('');
}

function addPKRelationFormatter(value, row, index) {
    return [
        '<a class="addPKRelation" href="javascript:void(0)" title="Add PK relation(s)">',
        '<i class="glyphicon glyphicon-magnet"></i>',
        '</a>'
    ].join('');
}

function addRelationFormatter(value, row, index) {
    return [
        '<a class="addRelation" href="javascript:void(0)" title="Add new relation">',
        '<i class="glyphicon glyphicon-plus-sign"></i>',
        '</a>'
    ].join('');
}

function boolFormatter(value, row, index) {

  // console.log("****** VALUE *********" + value);
  //
  // if(value == undefined){
  //   value = false;
  // }
  var icon = value == true ? 'glyphicon-check' : 'glyphicon-unchecked'
  if(value == undefined){
      console.log("****** VALUE *********" + value);
      console.log(row);
      icon = 'glyphicon-unchecked';
  }
  return [
    '<a href="javascript:void(0)">',
    '<i class="glyphicon ' + icon + '"></i> ',
    '</a>'
  ].join('');
}

function duplicateFormatter(value, row, index) {
  return [
      '<a class="duplicate" href="javascript:void(0)" title="Duplicate">',
      '<i class="glyphicon glyphicon-duplicate"></i>',
      '</a>'
  ].join('');
}

function removeFormatter(value, row, index) {
  return [
      '<a class="remove" href="javascript:void(0)" title="Remove">',
      '<i class="glyphicon glyphicon-trash"></i>',
      '</a>'
  ].join('');
}

function indexFormatter(value, row, index) {
  row.index = index;
  return index;
}

function recurseCountFormatter(value, row, index) {
  return 1;
}

function modValidate(){

  if ($("#modPKTables").find("option:selected").text() == 'Choose a pktable...') {
    showalert("modValidate()", "No pktable selected.", "alert-warning", "bottom");
    return;
  }

  if ($("#modPKColumn").find("option:selected").text() == 'Choose a pkcolumn...') {
    showalert("modValidate()", "No pkcolumn selected.", "alert-warning", "bottom");
    return;
  }

  if ($("#modColumn").find("option:selected").text() == 'Choose a column...') {
    showalert("modValidate()", "No column selected.", "alert-warning", "bottom");
    return;
  }

  if( $activeSubDatasTable == undefined){
    showalert("modValidate()", "$activeSubDatasTable not initialized", "alert-danger", "bottom");
  }

  var relation = {};
  var seq = {};

  relation.ref = null;
  relation.table_name = $('#modQuerySubject').text().split(" - ")[2];
  relation.key_name = $('#modKeyName').val();
  relation.fk_name = "";
  relation.pk_name = "";
  relation.key_type = $('#modKeyType').find("option:selected").text();
  relation.pktable_name = $('#modPKTables').find("option:selected").val();
  relation.pktable_alias = $('#modPKTableAlias').val();
  relation.fin = false;
  relation.ref = false;
  relation.withPK = false;
  seq.column_name = $('#modColumn').find("option:selected").text();
  seq.pkcolumn_name = $('#modPKColumn').find("option:selected").text();
  seq.key_seq = 1;
  relation.seqs = [];
  relation.seqs.push(seq);
  relation._id = $('#modKeyName').val() + relation.key_type;
  relation.relationship = "[" + $('#modQuerySubject').text().split(" - ")[1].toUpperCase() + "].[" + $('#modQuerySubject').text().split(" - ")[0] +
    "].[" + seq.column_name + "] = [" + relation.pktable_alias + "].[" + seq.pkcolumn_name + "]";

  var data = $datasTable.bootstrapTable("getData");

  var qs = $('#modQuerySubject').text().split(" - ")[0] + $('#modQuerySubject').text().split(" - ")[1]

  $.each(data, function(i, obj){
    //console.log(obj.name);
    if(obj._id.match(qs)){
      if(obj.relations.length == 0){
        obj.relations.push(relation);
      }
      console.log("+++ obj +++");
      console.log(obj);
    }
  });

  AddRow($activeSubDatasTable, relation);

  $newRowModal.modal('toggle');

}

function expandTable($detail, cols, data, parentData) {
    $subtable = $detail.html('<table></table>').find('table');
    console.log("expandTable.data=");
    console.log(data);
    $activeSubDatasTable = $subtable;
    buildSubTable($subtable, cols, data, parentData);
}

function buildSubTable($el, cols, data, parentData){

  console.log("activeTab=" + activeTab);

  $el.bootstrapTable("filterBy", {});

  $el.bootstrapTable({
      columns: cols,
      // url: url,
      data: data,
      showToggle: false,
      search: false,
      checkboxHeader: false,
      idField: "index",
      onClickCell: function (field, value, row, $element){

        if(field.match("traduction|visible|timezone|fin|ref")){
          var newValue = value == false ? true : false;

          console.log($(this).bootstrapTable("getData"));

          console.log("row.index=" + row.index);
          console.log("field=" + field);
          console.log("newValue=" + newValue);

          if(field == "fin" && newValue == true){
            row.relationship = row.relationship.replace(/ = /g, " = [FINAL].")
          }
          if(field == "fin" && newValue == false){
            row.relationship = row.relationship.replace(/ = \[FINAL\]\./g, " = ")
          }
          if(field == "ref" && newValue == true){
            row.relationship = row.relationship.replace(/ = /g, " = [REF].")
          }
          if(field == "ref" && newValue == false){
            row.relationship = row.relationship.replace(/ = \[REF\]\./g, " = ")
          }

          updateCell($el, row.index, field, newValue);

          if(field == "fin" && newValue == true){
            GetQuerySubjects(row.pktable_name, row.pktable_alias, "Final");
          }

          if(field == "ref" && newValue == true){
            GetQuerySubjects(row.pktable_name, row.pktable_alias, "Ref");
          }

        }

        if(field.match("duplicate")){
          // $activeSubDatasTable.bootstrapTable("filterBy", {});
          nextIndex = row.index + 1;
          console.log("nextIndex=" + nextIndex);
          var newRow = $.extend({}, row);
          newRow.checkbox = false;
          newRow.pktable_alias = "";
          newRow.fin = false;
          newRow.ref = false;
          newRow.relationship = newRow.relationship.replace(/ = \[FINAL\]\./g, " = ");
          newRow.relationship = newRow.relationship.replace(/ = \[REF\]\./g, " = ");
          console.log("newRow");
          console.log(newRow);
          $el.bootstrapTable('insertRow', {index: nextIndex, row: newRow});
        }

        if(field.match("remove")){
          $el.bootstrapTable('remove', {
              field: 'index',
              values: [row.index]
          });
        }

      }
  });

  if(activeTab == "Reference"){
    // $el.bootstrapTable("filterBy", {key_type: ['F','P', 'C']});
    $el.bootstrapTable('hideColumn', 'fin');
    $el.bootstrapTable('showColumn', 'ref');
  }

  if(activeTab == "Final"){
    // $el.bootstrapTable("filterBy", {key_type: ['F', 'C']});
    $el.bootstrapTable('hideColumn', 'ref');
    $el.bootstrapTable('showColumn', 'fin');
  }

}

function buildTable($el, cols, data) {

    $el.bootstrapTable({
        columns: cols,
        // url: url,
        // data: data,
        search: false,
				showRefresh: false,
				showColumns: false,
				showToggle: false,
				pagination: false,
				showPaginationSwitch: false,
        idField: "index",
				// toolbar: "#DatasToolbar",
        detailView: true,
        onClickCell: function (field, value, row, $element){
          if(field == "visible"){
            var newValue = value == false ? true : false;

            console.log($el.bootstrapTable("getData"));

            console.log("row.index=" + row.index);
            console.log("field=" + field);
            console.log("newValue=" + newValue);

            updateCell($el, row.index, field, newValue);

          }

          if(field.match("addRelation")){
            $el.bootstrapTable('expandRow', row.index);

            console.log("++++++++++++++on passe dans window.operateQSEvents.add");
            if($activeSubDatasTable != undefined){
              $newRowModal.modal('toggle');
              var qs = row.table_alias + ' - ' + row.type + ' - ' + row.table_name;
              // $('#modQuerySubject').selectpicker('val', qs);

              $('#modQuerySubject').text(qs);
              $('#modKeyName').val("CK_" + row.table_alias);
              $('#modPKTableAlias').val("");
              ChooseField($('#modColumn'), row._id);
            }
          }
          if(field.match("addPKRelation")){
            $el.bootstrapTable('expandRow', row.index);
            GetPKRelations(row.table_name, row.table_alias, row.type);
          }


        },
        onExpandRow: function (index, row, $detail) {
          if(activeTab == "Final" || activeTab == "Reference"){
            expandTable($detail, relationCols, row.relations, row);
          }
          else{
            expandTable($detail, fieldCols, row.fields, row);
          }
        }
    });

    $el.bootstrapTable("filterBy", {});
    $el.bootstrapTable('hideColumn', 'visible');
    $el.bootstrapTable('hideColumn', 'filter');
    $el.bootstrapTable('hideColumn', 'label');
    $el.bootstrapTable('hideColumn', 'recurseCount');
    $el.bootstrapTable('hideColumn', 'addPKRelation');


    if(activeTab == "Reference"){
      // $el.bootstrapTable("filterBy", {type: ['Final', 'Ref']});
    }

    if(activeTab == "Final"){
      //$el.bootstrapTable("filterBy", {type: ['Final']});
    }

    if(activeTab == "Query Subject"){
    }
}

function updateCell($table, index, field, newValue){

  $table.bootstrapTable("updateCell", {
    index: index,
    field: field,
    value: newValue
  });

}

function updateRow($table, index, row){

  $table.bootstrapTable("updateRow", {
    index: index,
    row: row
  });

}

function AddRow($table, row){

  $table.bootstrapTable("filterBy", {});

	nextIndex = $table.bootstrapTable("getData").length;
	console.log("nextIndex=" + nextIndex);
	$table.bootstrapTable('insertRow', {index: nextIndex, row: row});


}

function GetQuerySubjectsWithPK(){
  GetQuerySubjects(null, null, null, true);
}

function GetPKRelations(table_name, table_alias, type){

  var parms = "table=" + table_name + "&alias=" + table_alias + "&type=" + type;

	console.log("calling GetQuerySubjectsWithPK with: " + parms);

  $.ajax({
    type: 'POST',
    url: "GetPKRelations",
    dataType: 'json',
    data: parms,

    success: function(data) {
			console.log(data);
			if (data.length == 0) {
				showalert("GetPKRelations()", table_name + " has no PK.", "alert-info", "bottom");
				// return;
			}

      if($activeSubDatasTable != undefined){
        $.each(data, function(i, obj){
          console.log("on passe dans la boucle");
          AddRow($activeSubDatasTable, obj);
        });
      }

      // $.each($datasTable.bootstrapTable("getData"), function(i, obj){
      //   console.log("on passe dans la boucle");
      //
      //   if(obj._id == table_alias + type){
      //     console.log("on passe dans le IF");
      //     obj.relations.push(data);
      //     return;
      //   }
      //
      // });

			// $datasTable.bootstrapTable('append', data);
      // datas = $datasTable.bootstrapTable("getData");


  	},
      error: function(data) {
          console.log(data);
          showalert("GetPKRelations()", "Operation failed.", "alert-danger", "bottom");
    }

  });

}

function GetQuerySubjects(table_name, table_alias, type) {

	var table_name, table_alias, type, linker_id;

	if (table_name == undefined){
		table_name = $tableList.find("option:selected").val();
	}

  if (table_name == 'Choose a table...') {
		showalert("GetQuerySubjects()", "No table selected.", "alert-warning", "bottom");
		return;
	}

	if(table_alias == undefined){
		table_alias = $('#alias').val();
	}

	if(type == undefined){
		type = 'Final';
	}

  var qsAlreadyExist = false;

  $.each($datasTable.bootstrapTable("getData"), function(i, obj){
		//console.log(obj.name);
    if(obj._id == table_alias + type){
      qsAlreadyExist = true;
      showalert("GetQuerySubjects()", table_alias + type + " already exists.", "alert-info", "bottom");
    }
  });

  if(qsAlreadyExist){
    return;
  }

	var parms = "table=" + table_name + "&alias=" + table_alias + "&type=" + type;

	console.log("calling GetKeys with: " + parms);

  $.ajax({
    type: 'POST',
    url: "GetQuerySubjects",
    dataType: 'json',
    data: parms,

    success: function(data) {
			console.log(data);
			if (data[0].relations.length == 0) {
				showalert("GetQuerySubjects()", table_name + " has no key.", "alert-info", "bottom");
				// return;
			}

			$datasTable.bootstrapTable('append', data);
      datas = $datasTable.bootstrapTable("getData");


  	},
      error: function(data) {
          console.log(data);
          showalert("GetQuerySubjects()", "Operation failed.", "alert-danger", "bottom");
    }

  });

  if(activeTab == 'Final'){
    // $('#DatasTable').bootstrapTable("filterBy", {type: 'Final', key_type: 'F'});
  }
  if(activeTab == 'Reference'){
    // $('#DatasTable').bootstrapTable("filterBy", {type: ['Final', 'Ref'], key_type: ['F', 'P']});
  }

}

function ChooseQuerySubject(table) {

	table.empty();

  var data = $datasTable.bootstrapTable("getData");

  $.each(data, function(i, obj){
		//console.log(obj.name);
		table.append('<option class="fontsize">' + obj._id + ' - ' + obj.table_name +'</option>');
  });
  table.selectpicker('refresh');

}

function ChooseTable(table) {

	table.empty();

    $.ajax({
        type: 'POST',
        url: "Scan",
        dataType: 'json',

        success: function(data) {
            console.log(data);
            data.sort(function(a, b) {
              // return parseInt(b.keyCount) - parseInt(a.keyCount);
              return parseInt(b.seqCount) - parseInt(a.seqCount);
            });
            tables = data;
            $.each(data, function(i, obj){
							//console.log(obj.name);
              var option = '<option class="fontsize" value=' + obj.name + '>' + obj.name + ' (' + obj.keyCount + ') (' + obj.seqCount + ')' + '</option>';
							table.append(option);
              // $('#modPKTables').append(option);
              // table.append('<option class="fontsize" value=' + obj.name + '>' + obj.name + '</option>');
			      });
			      table.selectpicker('refresh');
            // $('#modPKTables').selectpicker('refresh');
			  },
        error: function(data) {
            console.log(data);
            showalert("ChooseTable()", "Operation failed.", "alert-danger", "bottom");
        }
		});

}

function ChooseField(table, id){
  table.empty();

  var data = $datasTable.bootstrapTable('getData');
  $.each(data, function(i, obj){
    if(obj._id == id){
      console.log("!!!!!!!!!!" + obj._id);
      $.each(obj.fields, function(j, field){
        table.append('<option class="fontsize">' + field.field_name + '</option>');
      });
      table.selectpicker('refresh');
    }
  });

  if( table.has('option').length == 0 ) {
    console.log("!!!!!!!!!! NO VALUE FOUND FOR " + id);
    $.ajax({
        type: 'POST',
        url: "GetFields",
        dataType: 'json',
        data: "table=" + id,

        success: function(data) {
            console.log(data);
            $.each(data, function(j, field){
              table.append('<option class="fontsize">' + field + '</option>');
            });
            table.selectpicker('refresh');
            showalert("ChooseField()", "ChooseField was successfull.", "alert-success", "bottom");
        },
        error: function(data) {
            console.log(data);
            showalert("ChooseField()", "ChooseField failed.", "alert-danger", "bottom");
        }

    });

  }

}

function showalert(title, message, alerttype, area) {

    // $('#alert_placeholder').append('<div id="alertdiv" class="alert ' +
		// alerttype + ' input-sm"><span>' + message + '</span></div>')
    //
    // setTimeout(function() {
    //
    //   $("#alertdiv").remove();
    //
    // }, 2500);

    // if($('#alertmsg').length){
    $('#alertmsg').remove();
    // }

    if(area == undefined){
      area = "top";
    }

    var $newDiv = $('<div/>')
       .attr( 'id', 'alertmsg' )
       .html(
          '<h4>' + title + '</h4>' +
          '<p>' +
          message +
          '</p>'
        )
       .addClass('alert ' + alerttype + ' flyover flyover-' + area);
    $('#alert_placeholder').append($newDiv);

    if ( !$('#alertmsg').is( '.in' ) ) {
      $('#alertmsg').addClass('in');

      setTimeout(function() {
         $('#alertmsg').removeClass('in');
      }, 3200);
    }
}

function TestDBConnection() {

    $.ajax({
        type: 'POST',
        url: "TestDBConnection",
        dataType: 'json',

        success: function(data) {
            console.log(data);
            showalert("TestDBConnection()", "Connection to database was successfull.", "alert-success", "bottom");
        },
        error: function(data) {
            console.log(data);
            showalert("TestDBConnection()", "Connection to database failed.", "alert-danger", "bottom");
        }

    });

}

function Publish(){

	var data = $datasTable.bootstrapTable('getData');

  if (data.length == 0) {
    showalert("Publish()", "Nothing to publish.", "alert-warning", "bottom");
    return;
  }

	// var objs = [];
  //
	// $.each(data, function(k, v){
	// 	var obj = JSON.stringify(v);
	// 	objs += obj + "\r\n";
	// });

	$.ajax({
		type: 'POST',
		url: "SendQuerySubjects",
		dataType: 'json',
		data: JSON.stringify(data),

		success: function(data) {
			$('#DatasTable').bootstrapTable('load', data);
		},
		error: function(data) {
			showalert("SyncRelations() failed.", "alert-danger");
		}
	});

}

function SaveModel(){

	var data = $datasTable.bootstrapTable('getData');

  if (data.length == 0) {
    showalert("SaveModel()", "Nothing to save.", "alert-warning", "bottom");
    return;
  }

	// var objs = [];
  //
	// $.each(data, function(k, v){
	// 	var obj = JSON.stringify(v);
	// 	objs += obj + "\r\n";
	// });

	$.ajax({
		type: 'POST',
		url: "SaveModel",
		dataType: 'json',
		data: JSON.stringify(data),

		success: function(data) {
			showalert("SaveModel()", "Model saved successfully.", "alert-success", "bottom");
		},
		error: function(data) {
			showalert("SaveModel()", "Saving model failed.", "alert-danger", "bottom");
		}
	});

}

function GetModelList(){

  $modelListModal.modal('toggle');

	$.ajax({
		type: 'POST',
		url: "GetModelList",
		dataType: 'json',

		success: function(data) {
      modelList = data;
      data.sort(function(a, b) {
        return b - a;
      });
      console.log("modelList");
      console.log(modelList);
			showalert("GetModelList()", "Model list get successfull.", "alert-success", "bottom");

		},
		error: function(data) {
			showalert("GetModelList()", "Getting model list failed.", "alert-danger", "bottom");
		}
	});


}

function OpenModel(id){

  var modelName;

  $.each(modelList, function(i, obj){
    if(obj.id == id){
      modelName = obj.name;
    }
  });

  console.log("modelName=" + modelName);

	$.ajax({
		type: 'POST',
		url: "OpenModel",
		dataType: 'json',
    data: "model=" + modelName,

		success: function(data) {
      $datasTable.bootstrapTable("load", data);
			showalert("OpenModel()", "Model opened successfully.", "alert-success", "bottom");

		},
		error: function(data) {
			showalert("OpenModel()", "Opening model failed.", "alert-danger", "bottom");
		}
	});

  $modelListModal.modal('toggle');


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
		showalert("Reset()", "Operation failed.", "alert-danger", "bottom");
	}

	location.reload(true);

}

function GetTableData(){
		var data = $datasTable.bootstrapTable("getData");
		console.log("data=");
		console.log(JSON.stringify(data));
    console.log(data);

}

function RemoveAll(){
  $datasTable.bootstrapTable("removeAll");
}

function ExpandAll(){
  $datasTable.bootstrapTable('expandAllRows');
}

// function refreshTable($table){
// 	var data = $table.bootstrapTable("getData");
// 	$table.bootstrapTable('load', data);
// }
