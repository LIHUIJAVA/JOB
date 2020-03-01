$.fn.pasteFromTable = function(cb) {     //make it a jquery function
    $(this).bind('paste', function(e) {
        
        e.preventDefault(); //消除默认粘贴
        
        //获取粘贴板数据
        var clipboardData = window.clipboardData || e.originalEvent.clipboardData, // IE || chrome  
                data = clipboardData.getData('Text'),
                //判断表格数据是使用\n还是\r分行，解析成行数组
                rowArr = (data.indexOf(String.fromCharCode(10)) > -1) ? 
                        data.split(String.fromCharCode(10)) : 
                        data.split(String.fromCharCode(13)),
                    //根据\t解析单元格
                    cellArr = rowArr.filter(function(item) { //兼容Excel行末\n，防止粘贴出多余空行
                  return (item !== "")
              }).map(function(item) {
                  return item. split(String.fromCharCode(9));
              });

//      ctrl_v(e,cellArr,cb)
		//输出至网页表格
		var tab = $(e.target).parents('table')[0],  //表格
	                td = $(e.target).parents('td'),    //当前单元格
	                startRow = td.parents('tr')[0].rowIndex, //当前单元格行数 
	                startCell = td[0].cellIndex, //当前单元格列数
	                rows = tab.rows.length;  //总行数
	
	        for (var i = 0; i < cellArr.length && startRow + i < rows; i++) {
	            var cells = tab.rows[startRow + i].cells.length;  //该行总列数
	            
	            for(var j = 0; j < cellArr[i].length && startCell + j < cells; j++) {
	                var cell = tab.rows[startRow + i].cells[startCell + j];
	                $(cell).find(':text').val(cellArr[i][j]);
	                if (cb) {cb(cell)};
	            }
	        }
        
    })
}

//function ctrl_v(e,cellArr,cb) {
//	//输出至网页表格
//	var tab = $(e.target).parents('table')[0],  //表格
//              td = $(e.target).parents('td'),    //当前单元格
//              startRow = td.parents('tr')[0].rowIndex, //当前单元格行数 
//              startCell = td[0].cellIndex, //当前单元格列数
//              rows = tab.rows.length;  //总行数
//
//      for (var i = 0; i < cellArr.length && startRow + i < rows; i++) {
//          var cells = tab.rows[startRow + i].cells.length;  //该行总列数
//          
//          for(var j = 0; j < cellArr[i].length && startCell + j < cells; j++) {
//              var cell = tab.rows[startRow + i].cells[startCell + j];
//              $(cell).find(':text').val(cellArr[i][j]);
//              if (cb) {cb(cell)};
//          }
//      }
//}
