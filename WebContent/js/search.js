var searchCols = [];
searchCols.push({field:"table_name", title: "Table<br>Name", sortable: true, searchable: false});
searchCols.push({field:"table_type", title: "Table<br>Type", sortable: true, searchable: false});
searchCols.push({field:"table_remarks", title: "Table<br>Label", sortable: true, searchable: false});
searchCols.push({field:"table_description", title: "Table<br>Description", sortable: true, searchable: false});

searchCols.push({field:"table_primaryKeyFieldsCount", title: 'FPK<br><i class="glyphicon glyphicon-question-sign"></i>', titleTooltip: "Number of fields within primary key", sortable: true, searchable: false});
searchCols.push({field:"table_importedKeysCount", title: 'IPK<br><i class="glyphicon glyphicon-question-sign"></i>', titleTooltip: "Number of primary keys imported", sortable: true, searchable: false});
searchCols.push({field:"table_importedKeysSeqCount", title: 'IPKS<br><i class="glyphicon glyphicon-question-sign"></i>', titleTooltip: "Number of sequence within primary keys imported", sortable: true, searchable: false});
searchCols.push({field:"table_exportedKeysCount", title: 'EFK<br><i class="glyphicon glyphicon-question-sign"></i>', titleTooltip: "Number of foreign keys exported", sortable: true, searchable: false});
searchCols.push({field:"table_exportedKeysSeqCount", title: 'EFKS<br><i class="glyphicon glyphicon-question-sign"></i>', titleTooltip: "Number of sequence within foreign keys exported", sortable: true, searchable: false});
searchCols.push({field:"table_indexesCount", title: 'Indexes<br><i class="glyphicon glyphicon-question-sign"></i>', titleTooltip: "Number of indexed fields", sortable: true, searchable: false});
searchCols.push({field:"table_recCount", title: "Select<br>count(*)", sortable: true, searchable: false});

searchCols.push({field:"column_name", title: "Column<br>Name", sortable: true, searchable: false});
searchCols.push({field:"column_type", title: "Column<br>Type", sortable: true, searchable: false});
searchCols.push({field:"column_remarks", title: "Column<br>Label", sortable: true, searchable: false});
searchCols.push({field:"column_description", title: "Column<br>Description", sortable: true, searchable: false});
searchCols.push({field:"column_isPrimaryKey", title: "Primary<br>Key", formatter: "isPrimaryKeyFormatter", sortable: true, searchable: false});
searchCols.push({field:"column_isIndexed", title: "Indexed", formatter: "isIndexedFormatter", sortable: true, searchable: false});
searchCols.push({field:"column_isNullable", title: "Nullable", sortable: true, searchable: false});

function isPrimaryKeyFormatter(value, row, index) {
  var icon = value == true ? 'glyphicon-star' : ''
  if(value == undefined){
      icon = '';
  }
  return [
    '<a href="javascript:void(0)">',
    '<i class="glyphicon ' + icon + '"></i> ',
    '</a>'
  ].join('');
}

function isIndexedFormatter(value, row, index) {
  var icon = value == true ? 'glyphicon-star-empty' : ''
  if(value == undefined){
      icon = '';
  }
  return [
    '<a href="javascript:void(0)">',
    '<i class="glyphicon ' + icon + '"></i> ',
    '</a>'
  ].join('');
}

$(document)
.ready(function() {
  buildTable($('#searchTable'), searchCols);
  ChooseTable($('#searchSelect'));
  ChooseColumn($('#searchColumnSelect'));
  checkDBMD();
  // GetDBMD($('#searchTable'));
})
.ajaxStart(function(){
    $("div#divLoading").addClass('show');
})
.ajaxStop(function(){
    $("div#divLoading").removeClass('show');
});

$('#dynamicModal').on('shown.bs.modal', function(){
    console.log("dynamicModal shown.bs.modal");
    $(this).find('.modal-header').find('.modal-title').empty();
    $(this).find('.modal-body').empty();
    $(this).find('.modal-footer').empty();
    var list = '<div class="container-fluid"><div class="row"><form role="form"><div class="form-group">';
    list += '<input id="searchinput" class="form-control" type="search" placeholder="Search..." /></div>';
    list += '<div id="searchlist" class="list-group">';

    var title = "<h4>Choose a table and display its content.</h4>"
    var tables;

    var dbmd = localStorage.getItem('dbmd');

    if('null' != dbmd && dbmd!= null){
      console.log("dbmd loaded from cache...");
      dbmd = JSON.parse(localStorage.getItem('dbmd'));

      tables = dbmd;
    }
    else {
        $.ajax({
        type: 'POST',
        url: "GetTables",
        dataType: 'json',
        async: true,
        success: function(data){
          tables = data;
        }
      });
    }

    console.log(tables);

    $.each(tables, function(i, obj){
      list += '<a href="#" id="' + i +'" class="list-group-item"><span>' + obj.table_name + ' - ' + obj.table_remarks + ' - ' + obj.table_stats + '</span></a>';
    });
    list += '</div></form><script>';
    list += '$("#searchlist").btsListFilter("#searchinput", {itemChild: "span", initial: false, casesensitive: false,});';
    list += '$(".list-group a").click(function(){console.log($(this).attr("id")); WatchContent($(this).attr("id"))})';
    list += '</script>';
    $(this).find('.modal-header').find('.modal-title').append(title);
    $(this).find('.modal-body').append(list);

});

function openDynamicModal(){
  $('#dynamicModal').modal('toggle');
}

function WatchContent(table){
  var query = "select * from " + table;
  console.log(query);
  var parms = {query: query};
  localStorage.setItem('SQLQuery', JSON.stringify(parms));
  window.open("watchContent.html");
}

function OpenQueryModal(){
  $('#queryModal').modal('toggle');
  GetLabelsQueries();
}

function GetLabelsQueries(){

  $.ajax({
    type: 'POST',
    url: "GetLabelsQueries",
    dataType: 'json',

    success: function(queries) {
      console.log(queries);
      $('#tableLabel').val(queries.tlQuery);
      $('#tableDescription').val(queries.tdQuery);
      $('#columnLabel').val(queries.clQuery);
      $('#columnDescription').val(queries.cdQuery);
    },
    error: function(data) {
      console.log(data);
    }
  });

}

function GetLabels(){

  var parms = {};
  parms.tables = $('#searchSelect').val();
  parms.tlQuery = $('#tableLabel').val();
  parms.tdQuery = $('#tableDescription').val();
  parms.clQuery = $('#columnLabel').val();
  parms.cdQuery = $('#columnDescription').val();

  // console.log("parms");
  // console.log(JSON.stringify(parms));

  $.ajax({
    type: 'POST',
    url: "GetLabels",
    dataType: 'json',
    data: JSON.stringify(parms),

    success: function(labels) {
      console.log(labels);

      var tables = Object.keys(labels);
      var dbmd = JSON.parse(localStorage.getItem('dbmd'));

      $.each(tables, function(i, table){
        if(dbmd[table]){
          dbmd[table].table_remarks = labels[table].table_remarks;
          dbmd[table].table_description = labels[table].table_description;
          $.each(labels[table].columns, function(j, column){
            if(dbmd[table].columns[j]){
              dbmd[table].columns[j].column_remarks = column.column_remarks;
              dbmd[table].columns[j].column_description = column.column_description;
            }
          })
        }
      })

      loadDBMD(dbmd);
      console.log(dbmd);

    },
    error: function(data) {
      console.log(data);
    }
  });

  $('#queryModal').modal('toggle');

}

function Filter(){
  var selection = $('#searchSelect').val();
  console.log(selection);

  $('#searchTable').bootstrapTable("filterBy", {});
  var datas = $('#searchTable').bootstrapTable("getData");
  console.log(datas);

  $.each(datas, function(i, row){
    row.filtered = true;
    $.each(selection, function(j, table){
      if(row.table_name.match(table)){
        row.filtered = false;
        return false;
      }
    });
  });

  selection = $('#searchColumnSelect').val();
  console.log(selection);
  var cols = $('#searchTable').bootstrapTable('getOptions').columns[0];
  console.log(cols);

  $.each(cols, function(i, col){
    col.searchable = false;
    $.each(selection, function(j, colName){
      if(col.field.match(colName)){
        col.searchable = true;
        return false;
      }
    });
  });

  $('#searchTable').bootstrapTable('destroy');
  buildTable($('#searchTable'), cols);
  $('#searchTable').bootstrapTable('load', datas);
  $('#searchTable').bootstrapTable("filterBy", {filtered: false});

}

function checkDBMD(){

  var dbmd = localStorage.getItem('dbmd');

  if(dbmd != 'null'){
    dbmd = JSON.parse(localStorage.getItem('dbmd'));
    console.log("dbmd loaded from cache...");
    GetDBMD($('#searchTable'));
  }
  else{
    bootbox.confirm({
      title: "No database metadata found in cache.",
      message: "You have to load database metadata to use this tool ? This could take few minutes.",
      buttons: {
        cancel: {
            label: 'Cancel',
            className: 'btn btn-default'
        },
        confirm: {
            label: 'Confirm',
            className: 'btn btn-primary'
        }
      },
      callback: function (result) {
          console.log(result);
          if(result){
              GetDBMD($('#searchTable'));
          }
      }
    });
  }

}

function GetDBMD(table) {

  var dbmd = localStorage.getItem('dbmd');

  if(dbmd != 'null'){
    dbmd = JSON.parse(localStorage.getItem('dbmd'));
    console.log("dbmd loaded from cache...");
    loadDBMD(dbmd);
  }
  else {
      $.when(
        $.ajax({
          type: 'POST',
          url: "GetDBMD",
          dataType: 'json',
          async: true,
          success: function(data) {
            // dbmd = data;
          }
        })
      )
      .then(
        function(data){
          dbmd = data;
          loadDBMD(dbmd);
        }
      );
  }

}

function loadDBMD(dbmd){

  var datas = [];

  $.each(dbmd, function(i, table){
    $.each(table.columns, function(j, column){
      var field = {};
      field.table_name = table.table_name;
      field.table_type = table.table_type;
      field.table_remarks = table.table_remarks;
      field.table_description = table.table_description;
      field.table_recCount = table.table_recCount;
      field.table_importedKeysCount = table.table_importedKeysCount;
      field.table_exportedKeysCount = table.table_exportedKeysCount;
      field.table_importedKeysSeqCount = table.table_importedKeysSeqCount;
      field.table_exportedKeysSeqCount = table.table_exportedKeysSeqCount;
      field.table_primaryKeyFieldsCount = table.table_primaryKeyFieldsCount;
      field.table_stats = table.table_stats;
      field.table_indexesCount = table.table_indexesCount;
      field.column_name = column.column_name;
      field.column_type = column.column_type;
      field.column_remarks = column.column_remarks;
      field.column_description = column.column_description;
      field.column_isPrimaryKey = column.column_isPrimaryKey;
      field.column_isIndexed = column.column_isIndexed;
      field.column_size = column.column_size;
      field.column_isNullable = column.column_isNullable;
      field.filtered = column.filtered;
      datas.push(field);
    })
  });

  console.log(dbmd);
  localStorage.setItem('dbmd', JSON.stringify(dbmd));
  console.log(datas);
  $('#searchTable').bootstrapTable("load", datas);
  ChooseTable($('#searchSelect'), '2');

}

function SaveDBMD(){

  var dbmd = localStorage.getItem('dbmd');

  if(dbmd != 'null'){
    $.ajax({
      type: 'POST',
      url: "SaveDBMD",
      dataType: 'json',
      data: dbmd,

      success: function(data) {
        ShowAlert("SaveDBMD()", "Database metadata saved successfully.", "alert-success", "bottom");
      },
      error: function(data) {
        ShowAlert("SaveDBMD()", "Saving database metadata failed.", "alert-danger", "bottom");
      }
    });
  }

}


function buildTable($el, cols) {

    $el.bootstrapTable({
        columns: cols,
        // url: url,
        // data: data,
        toolbar: $('#searchToolbar'),
        search: true,
        searchOnEnterKey: true,
        showRefresh: false,
        showColumns: true,
        showToggle: false,
        pagination: true,
        pageSize: 25,
        showPaginationSwitch: true,
        paginationVAlign: "both",
        detailView: false
    })

    var $tableHeaders = $el.find('thead > tr > th');
    console.log('$tableHeaders');
    console.log($tableHeaders.eq(3));
    var cols = $el.bootstrapTable('getOptions').columns[0];
    console.log(cols);
    $.each(cols, function(i, col){
      if(col.title.match('Table Type')){
        col.searchable = false;
      };
    });
    console.log(cols);
}

function ChooseColumn(table){
  table.empty();

  $.each($('#searchTable').bootstrapTable('getOptions').columns[0], function(i, obj){
    if(!obj.title.match("glyphicon-question-sign")){
      var option = '<option class="fontsize" value=' + obj.field + '>' + obj.title + '</option>';
      table.append(option);
    }
  });
  table.selectpicker('refresh');
}

function SortOnStats(){

  bootbox.prompt({
      title: "Sort tables list.",
      inputType: 'select',
      buttons: {
        cancel: {
            label: 'Cancel',
            className: 'btn btn-default'
        },
        confirm: {
            label: 'Confirm',
            className: 'btn btn-primary'
        }
      },
      inputOptions: [
          {
              text: 'Sort by...',
              value: '',
          },
          {
              text: '...alphabetic table name (ASC).',
              value: '0',
          },
          {
              text: '...number of fields within primary key (DESC).',
              value: '1',
          },
          {
              text: '...number of primary keys imported (DESC).',
              value: '2',
          },
          {
              text: '...number of sequence within primary keys imported (DESC).',
              value: '3',
          },
          {
              text: '...number of foreign keys exported (DESC).',
              value: '4',
          },
          {
              text: '...number of sequence within foreign keys exported (DESC).',
              value: '5',
          },
          {
              text: '...number of indexed fields (DESC).',
              value: '6',
          },
          {
              text: '...number of records (DESC).',
              value: '7',
          }
      ],
      callback: function (result) {
          console.log(result);
          ChooseTable($('#searchSelect'), result);
          console.log(result);
      }
  });

}

function ChooseTable(table, sort) {

  table.empty();

  var dbmd = localStorage.getItem('dbmd');

  if('null' != dbmd && dbmd!= null){
    console.log("dbmd loaded from cache...");
    dbmd = JSON.parse(localStorage.getItem('dbmd'));

    var tables = Object.values(dbmd);

    console.log(tables);
    console.log(sort);

    switch(sort){
      case "0":
        tables.sort(function(a, b){
          return a.table_name.localeCompare(b.table_name);
        });
        break;
      case "1":
        tables.sort(function(a, b){return b.table_primaryKeyFieldsCount - a.table_primaryKeyFieldsCount});
        break;
      case "2":
        tables.sort(function(a, b){return b.table_importedKeysCount - a.table_importedKeysCount});
        break;
      case "3":
        tables.sort(function(a, b){return b.table_importedKeysSeqCount - a.table_importedKeysSeqCount});
        break;
      case "4":
        tables.sort(function(a, b){return b.table_exportedKeysCount - a.table_exportedKeysCount});
        break;
      case "5":
        tables.sort(function(a, b){return b.table_exportedKeysSeqCount - a.table_exportedKeysSeqCount});
        break;
      case "6":
        tables.sort(function(a, b){return b.table_indexesCount - a.table_indexesCount});
        break;
      case "7":
        tables.sort(function(a, b){return b.table_recCount - a.table_recCount});
        break;
      default:
        tables.sort(function(a, b){return b.table_primaryKeyFieldsCount - a.table_primaryKeyFieldsCount});
    }

    $.each(tables, function(i, obj){
      //console.log(obj.name);
      var option = '<option class="fontsize" value="' + obj.table_name + '" data-subtext="' + obj.table_remarks +  ' ' + obj.table_stats + '">'
       + obj.table_name + '</option>';
      table.append(option);
      // $('#modPKTables').append(option);
      // table.append('<option class="fontsize" value=' + obj.name + '>' + obj.name + '</option>');
    });
    table.selectpicker('refresh');
    // $('#modPKTables').selectpicker('refresh');
    // localStorage.setItem('tables', JSON.stringify(tables));

  }
  else {
      $.ajax({
      type: 'POST',
      url: "GetTables",
      dataType: 'json',
      async: true,
      success: function(tables) {
        $.each(tables, function(i, obj){
          var option = '<option class="fontsize" value="' + obj.name + '">' + obj.name + '</option>';
          table.append(option);
        });
        table.selectpicker('refresh');
      }
    });
  }
}

function ShowAlert(title, message, alertType, area) {

    $('#alertmsg').remove();

    var timeout = 5000;

    if(area == undefined){
      area = "bottom";
    }
    if(alertType.match('warning')){
      area = "bottom";
      timeout = 10000;
    }
    if(alertType.match('danger')){
      area = "bottom";
      timeout = 30000;
    }

    var $newDiv;

    if(alertType.match('alert-success|alert-info')){
      $newDiv = $('<div/>')
       .attr( 'id', 'alertmsg' )
       .html(
          '<h4>' + title + '</h4>' +
          '<p>' +
          message +
          '</p>'
        )
       .addClass('alert ' + alertType + ' flyover flyover-' + area);
    }
    else{
      $newDiv = $('<div/>')
       .attr( 'id', 'alertmsg' )
       .html(
          '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' +
          '<h4>' + title + '</h4>' +
          '<p>' +
          '<strong>' + message + '</strong>' +
          '</p>'
        )
       .addClass('alert ' + alertType + ' alert-dismissible flyover flyover-' + area);
    }

    $('#Alert').append($newDiv);

    if ( !$('#alertmsg').is( '.in' ) ) {
      $('#alertmsg').addClass('in');

      setTimeout(function() {
         $('#alertmsg').removeClass('in');
      }, timeout);
    }
}
