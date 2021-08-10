/**
 * 各部UPPH日报表
 */
var pageCurr;var localtableFilterIns;
$(function() {
	layui.use([ 'form', 'table','tableFilter','laydate', 'tableSelect' ], function() {
		var table = layui.table, form = layui.form,tableFilter = layui.tableFilter,laydate = layui.laydate,
			tableSelect = layui.tableSelect,tableSelect2 = layui.tableSelect,tableSelect3 = layui.tableSelect;

		tableIns = table.render({
			elem : '#listTable',
			method : 'get' // 默认：get请求
			, toolbar: '#toolbar' //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
			,cellMinWidth : 80,
			height:'full-110'//固定表头&full-查询框高度
				,even:true,//条纹样式
			data : [],
			// height: 'full',
			page : true,
			limit:100,
			limits: [100,200,300,500],
			// totalRow :true,
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
				return {
					"count" : res.data.Total,
					"msg" : res.msg,
					"data" : res.data.List,
					"code" : res.status
					// code值为200表示成功
				}
				// 可进行数据操作
				// return {
				// 	// "count" : res.data.Total,
				// 	"msg" : res.msg,
				// 	"data" : res.data,
				// 	"code" : res.status
				// // code值为200表示成功
				// }
			},
			cols : [ [
					{type : 'numbers'},
					{field : 'ITEM_NO', title : '产品编码',  width : 130,},
					{field : 'LINER_NAME', title : '组长', width : 80,},
				    {field : 'TASK_NO', title : '制令单号', width : 150,},
					{field : 'FDAY', title : '统计日期', sort : true, width : 140,},
					{field : 'TIME_BG', title : '开始时间段', width : 100,},
					{field : 'TIME_END', title : '结束时间段', width : 100,},
					{field : 'TIME_NUM', title : '实际分钟数',  width : 100,},
					{field : 'TIME_BG_ACT', title : '上班时间', width : 80,},
					{field : 'TIME_END_ACT', title : '下班时间', width : 80,},
					{field : 'QTY_T_TAR', title : '投入目标',  width : 80,},
					{field : 'QTY_T_ACT', title : '投入实际', width : 80,},
					{field : 'RATE_T', title : '投入达成率', width : 150,},
					{field : 'QTY_C_TAR', title : '产出目标',  width : 110,},
					{field : 'QTY_C_ACT', title : '产出实际', width : 80,},
					{field : 'RATE_C', title : '产出达成率', width : 110,},
					{field : 'QTY_NG', title : '在线不良数', width : 110,},
					{field : 'RATE_OK', title : '在线良率', width : 100,},

				 ] ],
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
				{field: 'PROD_DATE', type:'date'},
				{field: 'LINER_NAME', type:'input'},
				{field: 'CLASS_NO', type:'radio'},
				{field: 'ITEM_NO', type:'input'},
				{field: 'ITEM_NAME', type:'input'},
				// {field: 'NG_RATE', type:'input'},
				// {field: 'COLOUR', type:'input'},
				// {field: 'LOT_NO', type:'input'},
				// {field: 'QTY_PROC', type:'input'},
				// {field: 'SAMPLE_QTY', type:'input'},
				// {field: 'DEFECT_NUM', type:'input'},
				// {field: 'DEFECT_NAME', type:'input'},
				// {field: 'USER_NAME', type:'input'},
				// {field: 'LINER_NAME', type:'input'},
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

		tableSelect = tableSelect.render({
			elem : '#taskNo',
			searchKey : 'keyword',
			checkedKey : 'id',
			searchPlaceholder : '试着搜索',
			table : {
				url : context + '/verify/getTaskNo',
				// url: context +'base/prodproc/getProdList',
				method : 'get',
				width : 800,
				cols : [ [
					{fixed:'left',type:'numbers',title:"序号"},
					{fixed:'left',
						type : 'radio',

					},// 多选 radio
					{fixed:'left',field : 'id', title : 'id', width : 0, hide : true},
					{fixed:'left',field : 'PROD_DATE', title : '生产日期', width : 100, sort : true,
						templet:function (d) {
							if(d.PROD_DATE!=null){
								return /\d{4}-\d{1,2}-\d{1,2}/g.exec(d.PROD_DATE)
							}
						}
					},
					{fixed:'left',field : 'LINER_NAME', title : '组长', width : 70, sort : true},
					{fixed:'left',field : 'ITEM_NO', title : '物料编码', width : 145, sort : true},
					{field : 'TASK_NO', title : '制令单号', width : 150, sort : true},
					{field : 'WS_SECTION', title : '工段', width : 60},
					{field : 'FMEMO', title : '备注', width : 80},
					{field : 'CUST_NAME_S', title : '客户', width : 80, sort : true} ,
					{field : 'ITEM_NAME', title : '物料描述', width : 260, sort : true}] ],
				page : false,
				request : {
					pageName : 'page' // 页码的参数名称，默认：page
					,
					limitName : 'rows' // 每页数据量的参数名，默认：limit
				},
				parseData : function(res) {
					if (res.result) {
						// 可进行数据操作
						return {
							"count" : 0,
							"msg" : res.msg,
							"data" : res.data,
							"code" : res.status
							// code值为200表示成功
						}
					}
				},
			},
			done : function(elem, data) {
				// 选择完后的回调，包含2个返回值
				// elem:返回之前input对象；data:表格返回的选中的数据 []
				var da = data.data;
				form.val("searchFrom", {
					"num" : da[0].TASK_NO,
					"itemNo" : da[0].ITEM_NO,
					"taskNo":da[0].TASK_NO,
					"linerName":da[0].LINER_NAME
				});
				form.render();// 重新渲染
			}
		});

		empTableSelect = tableSelect2.render({// 返工历史-制令单
			elem : '#linerName',
			searchKey : 'keyword',
			checkedKey : 'id',
			searchPlaceholder : '试着搜索',
			page:true,
			table : {
				url : context
					+ '/produce/patch/getEmpInfo',
				width:750,
				method : 'get',
				cols : [ [ {
					type : 'radio'
				},// 多选 radio
					, {
						field : 'id',
						title : 'id',
						width : 0,
						hide : true
					}, {
						field : 'EMP_CODE',
						title : '工号',
						width:100
					}, {
						field : 'EMP_NAME',
						title : '姓名',width:120
					} ] ],
				parseData : function(res) {
					//console.log(res)
					if (res.result) {
						// 可进行数据操作
						return {
							"count" : res.data.total,
							"msg" : res.msg,
							"data" : res.data.rows,
							"code" : res.status
							// code值为200表示成功
						}
					}
				},
			},
			done : function(elem, data) {
				//console.log(data)
				var da = data.data;
				//$("#empId").val(da[0].id)
				form.val("searchFrom", {
					"linerName":da[0].EMP_NAME,
				});
				form.render();// 重新渲染
			}
		});

		tableSelect3 = tableSelect3.render({
			elem: '#itemNo',
			searchKey: 'keyword',
			checkedKey: 'ITEM_NO',
			searchPlaceholder: '试着搜索',
			page: true,
			request: {
				pageName: 'page' // 页码的参数名称，默认：page
				,
				limitName: 'rows' // 每页数据量的参数名，默认：limit
			},
			table: {
				url: context + '/produce/check_code/getItemCode',
				method: 'get',
				cols: [[
					{type: 'radio'},// 多选 radio
					{field: 'ITEM_NO', title: '产品编号', width: 150, sort: true},
					{field: 'ITEM_NAME', title: '物料描述', width: 400},
					{field: 'ITEM_NAME_S', title: '物料简称', width: 110, sort: true},
				]],
				parseData: function (res) {
					//console.log(res)
					if (res.result) {
						// 可进行数据操作
						return {
							"count": res.data.total,
							"msg": res.msg,
							"data": res.data.rows,
							"code": res.status
							// code值为200表示成功
						}
					}
				},
			},
			done: function (elem, data) {
				var da = data.data;
				form.val("searchFrom", {
					"itemNo": da[0].ITEM_NO,
					// "itemName":da[0].ITEM_NAME,
				});
				form.render();// 重新渲染
			}
		});

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
			  ,
			  value:getDateRange(1,0)
		  });

		//获得日期区间 几天前和几天后
		function getDateRange(beforeDay,afterDay){
			//当前日期
			var day1 = new Date();
			day1.setDate(day1.getDate() - beforeDay);
			var day2 = new Date();
			day2.setDate(day2.getDate() + afterDay);
			return day1.getFullYear()+"-" + (day1.getMonth()+1) + "-" + day1.getDate()+" - "
				+day2.getFullYear()+"-" + (day2.getMonth()+1) + "-" + day2.getDate();
		}

		  layui.form.render('select');
	});

});
function getReport(dates) {
	// var params = {
	// 	"dates" : dates,
	// 	"keyword" : keyword,
	// }
	layer.load();
	tableIns.reload({
		url:context+'/report/rpt_hour/getReport',
		where:dates,
		done: function(res1, curr, count){
			layer.closeAll('loading'); //关闭loading
			pageCurr=curr;
		}
	})
	localtableFilterIns.reload();
}

function exportExcel(){
	if(!$('#dates').val()){
		layer.alert("请输入开始时间和结束时间");
		return false;
	}
	layer.load();
	$('#exportBtn').addClass("layui-btn-disabled").attr("disabled",true);
	location.href = context + "/report/rpt_hour/export?"+$("#searchFrom").serialize()+"&rows=99999";

	setTimeout(function(){
		layer.closeAll('loading'); //关闭loading
		$('#exportBtn').removeClass("layui-btn-disabled").attr("disabled", false);
	},5000);
}
