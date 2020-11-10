/**
 * 检验批次报表(FQC)
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
			{type:'checkbox'}
			// ,{field:'id', title:'ID', width:80, unresize:true, sort:true}
			, {
				field : 'CREATE_DATE',
				title : '检查时间',width : 120, sort: true,
			}, {
				field : 'CUST_NAME_S',
				title : '客户', sort: true
			},
			{
				field : 'ITEM_MODEL',
				title : '机型', sort: true
			}, {
				field : 'PROC_NAME',
				title : '名称', sort: true
			}
			, {
				field : '',
				title : '颜色',
				width : 95, sort: true
			}, {
				field : 'QTY_PROC',
				title : '批次数量',width : 100, sort: true
			}, {
				field : 'SAMPLE_QTY',
				title : '抽检数量', width : 100,sort: true
			}, {
				field : 'DEFECT_NUM',
				title : '不良数', width : 100,sort: true
			}, {
				field : '',
				title : '不良率(%)',width : 120, sort: true
			}, {
				field : '',
				title : '不良原因',width : 100, sort: true
			}, {
				field : 'USER_NAME',
				title : '作业员', width : 100,sort: true
			}, {
				field : 'LINE_NO',
				title : '责任科/班长',width : 120, sort: true
			}, {
				field : '',
				title : 'FQC', sort: true
			}] ],
			done : function(res, curr, count) {
				//localtableFilterIns.reload();
				pageCurr = curr;
			}
		});	
		 localtableFilterIns = tableFilter.render({
			'elem' : '#listTable',
			//'parent' : '#doc-content',
			'mode' : 'local',//本地过滤
			'filters' : [
				{field: 'CREATE_DATE', type:'date'},
				{field: 'CUST_NAME_S', type:'input'},
				{field: 'ITEM_MODEL', type:'input'},
				{field: 'PROC_NAME', type:'input'},
				{field: 'QTY_PROC', type:'input'},
				{field: 'SAMPLE_QTY', type:'input'},
				{field: 'DEFECT_NUM', type:'input'},
				{field: 'USER_NAME', type:'input'},
				{field: 'LINE_NO', type:'input'},
			],
			'done': function(filters){}
		})

		// 监听搜索框
		form.on('submit(searchSubmit)', function(data) {
			// 重新加载table
			getReport( data.field.dates,data.field.depart, data.field.num)
			return false;
		});
		//日期范围
		  laydate.render({
		    elem: '#dates',
		    trigger:'click'
		    ,range: true
		  });
		  tableSelect=tableSelect.render({
				elem : '#num',
				searchKey : 'keyword',
				checkedKey : 'id',
				searchPlaceholder : '试着搜索',
				table : {
					url:  context +'/report/batch/getItemList',
					//url:  context +'base/prodproc/getProdList',
					method : 'get',
					cols : [ [
					{ type: 'radio' },//多选  radio
					, {
						field : 'id',
						title : 'id',
						width : 0,hide:true
					}, {
						field : 'ITEM_NO',
						title : '物料编码',
						width : 150
					},{
						field : 'ITEM_NAME',
						title : '物料描述',
						width : 240
					}, {
						field : 'ITEM_NAME_S',
						title : '物料简称',
						width : 100
					},{
						field : 'ITEM_MODEL',
						title : '物料类别',
						width : 80
					}] ],
					page : true,
					request : {
						pageName : 'page' // 页码的参数名称，默认：page
						,
						limitName : 'rows' // 每页数据量的参数名，默认：limit
					},
					parseData : function(res) {
						if(res.result){
							return {
								"count" :res.data.Total,
								"msg" : res.msg,
								"data" : res.data.List,
								"code" : res.status
							}
						}
						return {
							"count" :0,
							"msg" : res.msg,
							"data" : [],
							"code" : res.status
						}
						
					},
				},
				done : function(elem, data) {
					//选择完后的回调，包含2个返回值 elem:返回之前input对象；data:表格返回的选中的数据 []
					var da=data.data;
					//console.log(da[0].num)
					form.val("searchFrom", {
						"num":da[0].ITEM_NO
					});
					form.render();// 重新渲染
			}
			});
		  
		  
		  layui.form.render('select');
	});

});
function getReport(dates,dept,item) {	
	var params = {
		"dates" : dates,
		"deptId" : dept,
		"itemNo" : item,
	}
	CoreUtil.sendAjax("/report/batch/getCheckBatchReport", JSON.stringify(params),
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

