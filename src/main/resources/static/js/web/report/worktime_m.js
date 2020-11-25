/**
 * 工时统计表（Mes）
 */
var pageCurr;
var localtableFilterIns;
$(function() {
	layui
			.use(
					[ 'form', 'table', 'tableFilter', 'laydate', 'tableSelect' ],
					function() {
						var table = layui.table, form = layui.form, tableFilter = layui.tableFilter, laydate = layui.laydate, tableSelect = layui.tableSelect;

						tableIns = table.render({
							elem : '#listTable',
							// url : context + '/base/line/getList',
							method : 'get', // 默认：get请求
							toolbar : '#toolbar', // 开启工具栏，此处显示默认图标，可以自定义模板，详见文档
							cellMinWidth : 80,
							//height : 'full-180',// 固定表头&full-查询框高度
							even : true,// 条纹样式
							data : [],
							height : 'full-60',
							page : true,
							limit:50,
							totalRow :true,
							limits:[50,100,200,500,1000,5000],
							request : {
								pageName : 'page', // 页码的参数名称，默认：page
								limitName : 'rows' // 每页数据量的参数名，默认：limit
							},
							parseData : function(res) {
							//	console.log(res)
								if (!res.result) {
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
							{
								field : 'WORK_DATE',
								title : '日期',
								sort : true,
								width : 110,
								totalRowText:"合计"
							},  {
								field : 'LINER_NAME',
								title : '组长',
								width : 80,
							},{
								field : 'ITEM_NO',
								title : '物料',
								width : 150,
							}, {
								field : 'ACT_HOURS',
								title : '正班时数',
								width : 80,totalRow: true
							}, {
								field : 'OVERTIME_COMM',
								title : '平时加班',
								width : 80,totalRow: true
							},{
								field : 'OVERTIME_HOLIDAY',
								title : '假日加班',
								width : 80,totalRow: true
							},{
								field : 'OVERTIME_YEAR',
								title : '法定加班',
								width : 80,totalRow: true
							},
							// 	{
							// 	field : 'OVERTIME_YEAR',
							// 	title : '法定加班',
							// 	width : 80,totalRow: true
							// },{
							// 	field : 'OVERTIME_YEAR',
							// 	title : '法定加班',
							// 	width : 80,
							// },
								{
								fixed : 'right',
								title : '操作',
								align : 'center',
								toolbar : '#optBar',
								width: 80
							} ] ],
							done : function(res, curr, count) {
								//
								pageCurr = curr;
							}
						});
						
						tableIns1 = table.render({
							elem : '#dataTable',
							method : 'get', // 默认：get请求
							toolbar : '#toolbar', // 开启工具栏，此处显示默认图标，可以自定义模板，详见文档
							cellMinWidth : 80,
							even : true,// 条纹样式
							data : [],
							height : 'full-60',
							page : true,
							limit:50,
							limits:[50,100,200,500,1000,5000],
							request : {
								pageName : 'page', // 页码的参数名称，默认：page
								limitName : 'rows' // 每页数据量的参数名，默认：limit
							},
							parseData : function(res) {
								console.log(res)
								if (!res.result) {
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
							}, {
								field : 'TASK_NO',
								title : '制令单',width : 150,
								sort : true
							},{
								field : 'EMP_NAME',
								title : '姓名',width : 90,sort : true
							},{
								field : 'EMP_CODE',
								title : '工号',width : 80,sort : true
							}, {
								field : 'ACT_HOURS',
								title : '正班时数',
								width : 100,sort : true
							}, {
								field : 'OVERTIME_COMM',
								title : '平时加班',
								width : 100,sort : true
							}, {
								field : 'OVERTIME_HOLIDAY',
								title : '假日加班',
								width : 100,sort : true
							}, {
								field : 'OVERTIME_YEAR',
								title : '法定加班',
								width : 100,sort : true
							}, {
								field : 'TIME_BEGIN',
								title : '上线时间',
								width : 150,sort : true
							}, {
								field : 'TIME_END',
								title : '下线时间',
								width : 150,sort : true
							} ] ],
							done : function(res, curr, count) {
								//
								pageCurr = curr;
							}
						});
						
						tableSelect=tableSelect.render({
							elem : '#num',
							searchKey : 'keyword',
							checkedKey : 'id',
							searchPlaceholder : '试着搜索',
							table : {
								url:  context +'/report/worktime_m/getItemList',
								method : 'get',
								cols : [ [
								{type : 'radio'}//多选  radio
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
									console.log(res)
									if(res.result){
										return {
											"count" :res.data.total,
											"msg" : res.msg,
											"data" : res.data.rows,
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
								
								form.val("searchFrom", {
									"num" : da[0].ITEM_NO
								});
								/*form.val("searchFrom", {
									"num":da[0].ITEM_NO
								});*/
								form.render();// 重新渲染
						}
						});
						getLinerList();
						

						// 监听搜索框
						form.on('submit(searchSubmit)', function(data) {
							// 重新加载table
							// console.log(data.field)
							var date = data.field.dates;
							var sdata = date.substring(0, date.indexOf(" "))
							var edata = date.substring(date.indexOf(" ") + 3,
									date.length);
							var params = {
								"sdate" : sdata,
								"edate" : edata,
								"itemCode" : data.field.num,
								"liner_id" : data.field.liner_id,
							};
							// console.log(params)
							getReport(params)
							return false;
						});
						// 监听工具条
						table.on('tool(listTable)', function(obj) {
							var data = obj.data;
							if (obj.event === 'detail') {
								// 删除
								//console.log(obj.data.ID)
								var param={
									"list_id":obj.data.ID
								}
								getDetail(param);
							} 
						});
						// 日期范围
						laydate.render({
							elem : '#dates',
							trigger : 'click',
							range : true
						});
					});

});

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

function getReport(params) {
	tableIns.reload({
	     url:context+'/report/worktime_m/getList',
        where:params,
	     done: function(res1, curr, count){
	    	// console.log(res1)
              pageCurr=curr;
          }
	}) 
}

function getDetail(param){
	CoreUtil.sendAjax("/report/worktime_m/getListDetail", JSON.stringify(param),
			function(data) {
		 console.log(data.data)
				if (data.result) {
					if (data.result) {
						tableIns1.reload({
							data : data.data.rows,
							done: function(res1, curr, count){
						    	  //console.log(res1)
					              pageCurr=curr;
					          }
						});
						openData("查看明细")
					} else {
						playMusic();
						layer.alert(data.msg);
					}
				} else {
					layer.alert(data.msg);
				}
			}, "POST", false, function(res) {
				layer.alert(res.msg);
			});
}


function getLinerList() {
	CoreUtil.sendAjax("/report/worktime_m/getLinerList", "",
			function(data) {
				//console.log(data)
				if (data.result) {
					if (data.result) {
						$("#liner_id").empty();
						var list = data.data;
						for (var i = 0; i < list.length; i++) {
							if (i == 0) {
								$("#liner_id").append(
										"<option value=''>请点击选择</option>");
							}
							$("#liner_id").append(
									"<option value=" + list[i].ID + ">"
											+ list[i].LEAD_BY + "</option>");
						}
						layui.form.render('select');
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
