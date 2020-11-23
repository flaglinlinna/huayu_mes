/**
 * 过程检验日报表
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
			height:'full-110'//固定表头&full-查询框高度
				,even:true,//条纹样式
			data : [],
			height: 'full',
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
					"count" : res.data.Total,
					"msg" : res.msg,
					"data" : res.data.List,
					"code" : res.status
				// code值为200表示成功
				}
			},
			cols : [ [ {
				type : 'numbers'
			}, {
				field : 'FDATE',
				title : '检验日期',sort: true,
			}, {
				field : 'PROC_NAME',
				title : '工序名称',width : 130, sort: true
			},{
				field : 'QTY_DZC',
				title : '待转出数', width : 130, sort: true
			}, {
				field : 'LOT_DZC',
				title : '待转出批次',width : 130,  sort: true
			},{
				field : 'QTY_JYZ',
				title : '检验中数',width : 130,sort: true
			}, {
				field : 'LOT_JYZ',
				title : '检验中批次',
				width : 130, sort: true
			},{
				field : 'QTY_YJY',
				title : '已检验数', width : 130,sort: true
			},{
				field : 'LOT_YJY',
				title : '已检验批次', width : 130,sort: true
			}, {
				fixed : 'right',
				title : '操作',
				align : 'center',
				toolbar : '#optBar',
				width: 80
			} ] ],
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
				{field: 'ITEM_NAME_S', type:'input'},
				{field: 'ITEM_MODEL', type:'input'},
				{field: 'PROC_NAME', type:'input'},
				{field: 'NG_RATE', type:'input'},
				{field: 'COLOUR', type:'input'},
				{field: 'LOT_NO', type:'input'},
				{field: 'QTY_PROC', type:'input'},
				{field: 'SAMPLE_QTY', type:'input'},
				{field: 'DEFECT_NUM', type:'input'},
				{field: 'DEFECT_NAME', type:'input'},
				{field: 'USER_NAME', type:'input'},
				{field: 'LINER_NAME', type:'input'},
			],
			'done': function(filters){}
		})
		tableIns1 = table.render({
				elem : '#dataTable',
				method : 'get', // 默认：get请求
				toolbar : '#toolbar', // 开启工具栏，此处显示默认图标，可以自定义模板，详见文档
				cellMinWidth : 80,
				even : true,// 条纹样式
				data : [],
				height : 'full-90',
				page : true,
				request : {
					pageName : 'page', // 页码的参数名称，默认：page
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
						"count" : res.data.Total,
						"msg" : res.msg,
						"data" : res.data.List,
						"code" : res.status
					// code值为200表示成功
					}
				},
				cols : [ [  {
					type : 'numbers'
				},  {
					field : 'FDATE',
					title : '检验日期',width : 120,sort: true,
				}, {
					field : 'PROC_NAME',
					title : '工序名称',width : 110, sort: true
				},{
					field : 'ITEM_NO',
					title : '产品编码',width : 180, sort: true
				},{
					field : 'QTY_DZC',
					title : '待转出数', width : 110, sort: true
				}, {
					field : 'LOT_DZC',
					title : '待转出批次',width : 110,  sort: true
				},{
					field : 'QTY_JYZ',
					title : '检验中数',width : 110,sort: true
				}, {
					field : 'LOT_JYZ',
					title : '检验中批次',
					width : 110, sort: true
				},{
					field : 'QTY_YJY',
					title : '已检验数', width : 110,sort: true
				},{
					field : 'LOT_YJY',
					title : '已检验批次', width : 110,sort: true
				} ] ],
				done : function(res, curr, count) {
					//
					pageCurr = curr;
				}
			});
		// 监听工具条
			table.on('tool(listTable)', function(obj) {
				var data = obj.data;
				if (obj.event === 'detail') {
					// 删除
					//console.log(obj.data.ID)
					var param={
						"user_id":obj.data.RPT_BY,
						"dep_id":obj.data.DEPT_ID,
						"proc_no":obj.data.PROC_NO,
						"fdate":obj.data.FDATE
					}
					getDetail(param);
				} 
			});

		// 监听搜索框
		form.on('submit(searchSubmit)', function(data) {
			// 重新加载table
			getReport( data.field.dates,data.field.depart, data.field.num)
			return false;
		});
		//监听行双击查看事件
		table.on('rowDouble(listTable)', function(obj){
			var data = obj.data;
			var param={
					"user_id":obj.data.RPT_BY,
					"dep_id":obj.data.DEPT_ID,
					"proc_no":obj.data.PROC_NO,
					"fdate":obj.data.FDATE
				}
				getDetail(param);
		});
		//日期范围
		  laydate.render({
		    elem: '#dates',
		    trigger:'click'
		    ,range: true
		  });
		  /*tableSelect=tableSelect.render({
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
			});*/
		  
		  
		  layui.form.render('select');
	});

});
function getReport(dates,dept,item) {	
	var params = {
		"dates" : dates,
		"deptId" : dept,
		"itemNo" : item,
	}
	tableIns.reload({
		url:context+'/report/proc_day/getReport',
		where:params,
		done: function(res1, curr, count){
			pageCurr=curr;
		}
	})
}
function getDetail(params){
	tableIns1.reload({
		url:context+'/report/proc_day/getDetailReport',
		where:params,
		done: function(res1, curr, count){
			pageCurr=curr;
		}
	})
	openData("查看明细");
}
function openData(title) {

	var index=layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#getData'),
		
	});
	layer.full(index);
}
