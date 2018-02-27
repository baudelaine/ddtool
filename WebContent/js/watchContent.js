
$(document)
.ready(function() {
  getDatas();
})
.ajaxStart(function(){
    $("div#divLoading").addClass('show');
})
.ajaxStop(function(){
    $("div#divLoading").removeClass('show');
});

function getDatas(){
  var parms = localStorage.getItem('SQLQuery');
  console.log(parms)

  if(parms != 'null' && parms != null){
    $.ajax({
      type: 'POST',
      url: "GetSQLQuery",
      dataType: 'json',
      data: parms,

      success: function(datas) {
        console.log(datas);
        var cols = [];
        // cols.push({field: "PROJNO", title: "PROJNO"});
        $.each(datas.columns, function(i, column_name){
          var col = {};
          col.field = column_name;
          col.title = column_name;
          col.sortable = true;
          cols.push(col);
        })
        buildTable($('#table'), cols, datas.result);
        $("#query").text(datas.query);
        $("#schema").text(datas.schema);

      },
      error: function(data) {
        console.log(data);
      }
    });
  }
}

function buildTable($el, cols, datas) {

    $el.bootstrapTable({
        columns: cols,
        // url: "js/projectTable.json",
        data: datas,
        toolbar: $("#watchToolbar"),
        search: true,
        searchOnEnterKey: true,
        showRefresh: false,
        showColumns: true,
        showToggle: false,
        pagination: false,
        pageSize: 25,
        showPaginationSwitch: false,
        paginationVAlign: "both",
        detailView: false
    })

    var $tableHeaders = $el.find('thead > tr > th');
    console.log('$tableHeaders');
    console.log($tableHeaders.eq(3));
    var cols = $el.bootstrapTable('getOptions').columns[0];
    console.log(cols);
}
