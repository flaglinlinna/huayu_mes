/**
 * 追溯报表
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
			height:'full-80'//固定表头&full-查询框高度
				,even:true,//条纹样式
			data : [],
			//height: 'full',
			page : true,
			/*request : {
				pageName : 'page' // 页码的参数名称，默认：page
				,
				limitName : 'rows' // 每页数据量的参数名，默认：limit
			},*/
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
				field : 'LINER_NAME',
				title : '组长',width : 80, sort: true,
			}, {
				field : 'TASK_NO',
				title : '制令单',width : 160, sort: true
			},
			{
				field : 'PROC_NAME',
				title : '工序名称',width : 120, sort: true
			}, {
				field : 'CLASS_NO',
				title : '班次', sort: true
			}
			, {
				field : 'ITEM_BARCODE',
				title : '条码', width : 160,sort: true
			}, {
				field : 'ITEM_NO',
				title : '物料编码', width : 160,sort: true
			}, {
				field : 'ITEM_MODEL',
				title : '机型',width : 100, sort: true
			}, {
				field : 'QUANTITY',
				title : '投料/产出/送检/检验数(PCS)', width : 200,sort: true
			}, {
				field : 'SAMPLE_QTY',
				title : '抽检总数(PCS)',width : 120, sort: true
			}, {
				field : 'QTY_PROC',
				title : '产出/中转送检/检验总数(PCS)',width : 220, sort: true
			}, {
				field : 'QTY_DONE',
				title : '抽检合格数(PCS)',width : 120, sort: true
			}, {
					field : 'CHK_RESULT',
					title : '检验结果',width : 120, sort: true
				},
				{
				field : 'DEFECT_NUM',
				title : '抽检不良数(PCS)',width : 120, sort: true
			}, {
					field : 'ITEM_NAME',
					title : '物料名称',width : 150, sort: true
				},
				{
					field : 'SCAN_TYPE',
					title : '扫描类型',width : 120, sort: true
				}, {
					field : 'TASK_SCAN_TYPE',
					title : '主/副制令单',width : 120, sort: true
				}, {
					field : 'TASK_NO_M',
					title : '主制令单',width : 120, sort: true
				},
				{
				field : 'ORG_NAME',
				title : '部门名称',width : 120, sort: true
			},{
					field : 'USER_NAME',
					title : '操作人',
					width : 100, sort: true
				}, {
					field : 'CREATE_DATE',
					title : '操作时间',width : 150, sort: true
				}
				// 	{
			// 	field : 'DEFECT_NAME',
			// 	title : '不良内容',width : 120, sort: true
			// }, {
			// 	field : 'QTY',
			// 	title : '数量',width : 120, sort: true
			// },
				] ],
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
				{field: 'LINER_NAME', type:'input'},
				{field: 'TASK_NO', type:'input'},
				{field: 'PROC_NAME', type:'input'},
				{field: 'CLASS_NO', type:'input'},
				{field: 'USER_NAME', type:'input'},
				{field: 'CREATE_DATE', type:'date'},
				{field: 'ITEM_BARCODE', type:'input'},
				{field: 'ITEM_NO', type:'input'},
				{field: 'ITEM_NAME', type:'input'},
				
				{field: 'ITEM_MODEL', type:'input'},
				{field: 'QUANTITY', type:'input'},
				{field: 'SAMPLE_QTY', type:'input'},
				{field: 'QTY_PROC', type:'input'},
				{field: 'QTY_DONE', type:'input'},
				{field: 'DEFECT_NUM', type:'input'},
				{field: 'CHK_RESULT', type:'input'},
				{field: 'ORG_NAME', type:'input'},
				{field: 'DEFECT_NAME', type:'input'},
				{field: 'QTY', type:'input'},
				{field: 'SCAN_TYPE', type:'input'},
				{field: 'TASK_SCAN_TYPE', type:'input'},
				{field: 'TASK_NO_M', type:'input'},
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
		    ,range: true
		  });

	});

});
function getReport(params) {	
	CoreUtil.sendAjax("/report/back/getList", params,
			function(data) {
				//console.log(data)
				if (data.result) {
					if (data.result) {
						tableIns.reload({
							data : data.data.List,
							done: function(res1, curr, count){
								localtableFilterIns.reload();
							},

							page : {
								curr : pageCurr
								// 从当前页码开始
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

