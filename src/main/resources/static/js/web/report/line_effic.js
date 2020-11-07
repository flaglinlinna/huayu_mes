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
			data : [],
			height: 'full',
			page : false,
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
			cols : [ [ {
				type : 'numbers'
			},
			//,{type:'checkbox'}
			// ,{field:'id', title:'ID', width:80, unresize:true, sort:true}
			 {
				field : '',
				title : '班别',width : 120, sort: true,
			}, {
				field : 'ITEM_NO',
				title : '物料编码',width : 140, sort: true
			},
			{
				field : '',
				title : '机型', sort: true
			}, {
				field : 'LINER_NAME',
				title : '组线', sort: true
			}
			, {
				field : 'QTY_PLAN',
				title : '计划生产数',
				width : 130, sort: true
			}, {
				field : 'QTY_DONE',
				title : '实际生产数',width : 120, sort: true
			}, {
				field : 'CAPACITY',
				title : '标准产能', width : 100,sort: true
			}, {
				field : 'MANPOWER',
				title : '标准人力', width : 100,sort: true
			}, {
				field : 'HOUR_SDD',
				title : '标准工时',width : 120, sort: true
			}, {
				field : 'UPPH_SDD',
				title : '标准upph',width : 120, sort: true
			}, {
				field : 'QTY_EMP',
				title : '实际用人', width : 100,sort: true
			}, {
				field : 'HOUR_ACT',
				title : '实际工时(H)',width : 120, sort: true
			}, {
				field : 'HOUR_UPPH',
				title : '实际产能',width : 120, sort: true
			}, {
				field : '',
				title : '实际upph',width : 120, sort: true
			}, {
				field : 'DURATION',
				title : '异常工时',width : 120, sort: true
			}, {
				field : 'HOUR_ACT_ALL',
				title : '实际生产工时',width : 120, sort: true
			}, {
				field : 'HOUR_DFE',
				title : '盈亏工时 ',width : 120, sort: true
			}, {
				field : 'EFFICIENCY',
				title : '效率',width : 120, sort: true
			}, {
				field : '',
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
		    elem: '#dates'
		    ,range: true
		  });

	});

});
function getReport(params) {	
	CoreUtil.sendAjax("/report/line_effic/getList", params,
			function(data) {
				console.log(data)
				if (data.result) {
					if (data.result) {
						tableIns.reload({
							data : data.data,
							done: function(res1, curr, count){
								localtableFilterIns.reload();
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

