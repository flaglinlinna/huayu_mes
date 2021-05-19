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
			height:'full-110'//固定表头&full-查询框高度
				,even:true,//条纹样式
			data : [],
			// height: 'full',
			page : true,
			totalRow :true,
			limit:50,
			limits:[50,100,200,500,1000,5000],
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
			cols : [
				[
					{fixed:'left',
				type : 'numbers'
			},

			 {fixed:'left',
				field : 'CREATE_DATE',
				title : '检验日期',width : 100, sort: true,totalRowText:"合计"
			}, {fixed:'left',
					field : 'ITEM_MODEL',
					title : '机型',width : 110, sort: true
				}, {fixed:'left',
					field : 'LINER_NAME',
					title : '组长',width : 90, sort: true
				},{fixed:'left',
					field : 'PROC_NAME',
					title : '工序名称',width : 100, sort: true
				},
					{
				field : 'CUST_NAME_S',
				title : '客户',width : 100, sort: true
			},
				{
					field : 'ITEM_NAME_S',
					title : '物料简称'
				,width : 120,
				sort: true
				}
			, {
				field : 'COLOUR',
				title : '颜色',
				width : 75, sort: true
			},
				{
					field : 'LOT_NO',
					title : '虚拟批次', width : 100,sort: true
				},
				{
				field : 'QTY_PROC',
				title : '批次数量',width : 100, sort: true,totalRow: true
			}, {
				field : 'SAMPLE_QTY',
				title : '抽检数量', width : 100,sort: true,totalRow: true
			}, {
				field : 'DEFECT_NUM',
				title : '不良数', width : 100,sort: true,totalRow: true
			}, {
				field : 'NG_RATE',
				title : '不良率',width : 120, sort: true
			}, {
				field : 'DEFECT_NAME',
				title : '不良原因',width : 140, sort: true
			}, {
				field : 'USER_NAME',
				title : '检验员', width : 100,sort: true
			},
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
	// CoreUtil.sendAjax("/report/batch/getCheckBatchReport", JSON.stringify(params),
	// 		function(data) {
	// 			console.log(data)
	// 			if (data.result) {
	// 				if (data.result) {
	// 					tableIns.reload({
	// 						data : data.data.List,
	// 						done: function(res1, curr, count){
	// 							localtableFilterIns.reload();
	// 						},
	// 						page : {
	// 							curr : pageCurr
	// 							// 从当前页码开始
	// 						}
	// 					});
	// 				} else {
	// 					layer.alert(data.msg);
	// 				}
	// 			} else {
	// 				layer.alert(data.msg);
	// 			}
	// 		}, "GET", false, function(res) {
	// 			layer.alert(res.msg);
	// 		});
	tableIns.reload({
		url:context+'/report/batch/getCheckBatchReport',
		where:params,
		done: function(res1, curr, count){
			pageCurr=curr;
		}
	})
}

