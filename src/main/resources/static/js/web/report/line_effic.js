/**
 * 各线效率明细报表
 */
var pageCurr;var localtableFilterIns;
$(function() {
	layui.use([ 'form', 'table','tableFilter','laydate', 'tableSelect' ], function() {
		var table = layui.table, form = layui.form,tableFilter = layui.tableFilter,laydate = layui.laydate, tableSelect = layui.tableSelect;

		tableIns = table.render({
			elem : '#listTable',
		//	url : context + '/base/line/getList',
			method : 'get' // 默认：get请求
			, toolbar: '#toolbar' //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
			,cellMinWidth : 80,
            even:true,//条纹样式
			data : [],
			height: 'full-70',
			page : true,
			request : {
				pageName : 'page' // 页码的参数名称，默认：page
				,
				limitName : 'rows' // 每页数据量的参数名，默认：limit
			},
			parseData : function(res) {
				if(!res.result){
					return {
						"count" : 0,
						"msg" : res.msg,
						"data" : [],
						"code" : res.status
					} 
				}
				// 可进行数据操作
				return {
					"count" : res.data.total,
					"msg" : res.msg,
					"data" : res.data.rows,
					"code" : res.status
				// code值为200表示成功
				}
			},
			cols : [ [ /*{
				type : 'numbers'
			},*/
			 {
				field : 'WORK_TYPE',
				title : '班别',width : 120, sort: true,
			}, {
				field : 'ITEM_NO',
				title : '物料编码',width : 140, sort: true
			},
			{
				field : 'ITEM_TYPE',
				title : '机型', sort: true
			}, {
				field : 'LINER_NAME',
				title : '组线', sort: true
			}, {
				field : 'QTY_PLAN',
				title : '计划生产数',
				width : 130, sort: true
			}, {
				field : 'QTY_ACT',
				title : '实际生产数',width : 120, sort: true
			}, {
				field : 'STD_CAPACITY',
				title : '标准产能', width : 100,sort: true
			}, {
				field : 'STD_MANPOWER',
				title : '标准人力', width : 100,sort: true
			}, {
				field : 'STD_HOURS',
				title : '标准工时',width : 120, sort: true
			}, {
				field : 'STD_UPPH',
				title : '标准upph',width : 120, sort: true
			}, {
				field : 'ACT_MANPOWER',
				title : '实际用人', width : 100,sort: true
			}, {
				field : 'ACT_HOURS_H',
				title : '实际工时(H)',width : 120, sort: true
			}, {
				field : 'ACT_CAPACITY',
				title : '实际产能',width : 120, sort: true
			}, {
				field : 'ACT_UPPH',
				title : '实际upph',width : 120, sort: true
			}, {
				field : 'ABNORMAL_HOURS',
				title : '异常工时',width : 120, sort: true
			}, {
				field : 'ACT_HOURS',
				title : '实际生产工时',width : 120, sort: true
			}, {
				field : 'YK_HOURS',
				title : '盈亏工时 ',width : 120, sort: true
			}, {
				field : 'EFFICIENCY',
				title : '效率',width : 120, sort: true
			}, {
				field : 'LAR_VALUE',
				title : 'LAR值',width : 120, sort: true
			}] ],
			done : function(res, curr, count) {
				//
				pageCurr = curr;
				
			}
		});	
		 localtableFilterIns = tableFilter.render({
			'elem' : '#listTable',
			//'parent' : '#doc-content',
			'mode' : 'local',//本地过滤
			'filters' : [
				{field: 'ITEM_NO', type:'input'},
				{field: 'LINER_NAME', type:'input'},
				{field: 'QTY_PLAN', type:'input'},
				{field: 'QTY_DONE', type:'input'},
				{field: 'QTY_PROC', type:'input'},
				{field: 'CAPACITY', type:'input'},
				{field: 'MANPOWER', type:'input'},
				{field: 'HOUR_SDD', type:'input'},
				{field: 'QTY_EMP', type:'input'},
			],
			'done': function(filters){}
		})

		// 监听搜索框
		form.on('submit(searchSubmit)', function(data) {
			// 重新加载table
			getReport(data.field)
			return false;
		});
		//日期范围
		  laydate.render({
		    elem: '#dates',
		    trigger:'click'
		    //,range: true
		  });

	});

});
function getReport(params) {	
	CoreUtil.sendAjax("/report/line_effic/getList", params,
			function(data) {
				if (data.result) {
					if (data.result) {
						tableIns.reload({
							data : data.data,
							done: function(res1, curr, count){
								localtableFilterIns.reload();
								merge(data.data,['WORK_TYPE','LINER_NAME'],[0,3]);
							}
						});
					} else {
						layer.alert(data.msg);
					}
				} else {
					layer.alert(data.msg);
				}
			}, "GET", false, function(res) {
				layer.alert(res.msg);
			});
}
function merge(res,columsName,columsIndex) {
    console.log(res)
    var data = res;
    var mergeIndex = 0;//定位需要添加合并属性的行数
    var mark = 1; //这里涉及到简单的运算，mark是计算每次需要合并的格子数
    //var columsName = ['itemCode'];//需要合并的列名称
    //var columsIndex = [3];//需要合并的列索引值

    for (var k = 0; k < columsName.length; k++) { //这里循环所有要合并的列
        var trArr = $(".layui-table-body>.layui-table").find("tr");//所有行
            for (var i = 1; i < data.length; i++) { //这里循环表格当前的数据
                var tdCurArr = trArr.eq(i).find("td").eq(columsIndex[k]);//获取当前行的当前列
                var tdPreArr = trArr.eq(mergeIndex).find("td").eq(columsIndex[k]);//获取相同列的第一列
                
                if (data[i][columsName[k]] === data[i-1][columsName[k]]) { //后一行的值与前一行的值做比较，相同就需要合并
                    mark += 1;
                    tdPreArr.each(function () {//相同列的第一列增加rowspan属性
                        $(this).attr("rowspan", mark);
                    });
                    tdCurArr.each(function () {//当前行隐藏
                        $(this).css("display", "none");
                    });
                }else {
                    mergeIndex = i;
                    mark = 1;//一旦前后两行的值不一样了，那么需要合并的格子数mark就需要重新计算
                }
            }
        mergeIndex = 0;
        mark = 1;
    }
}

