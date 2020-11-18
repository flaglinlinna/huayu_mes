/**
 * 人数差异统计表
 */
var pageCurr;
var localtableFilterIns;
$(function() {
	layui
			.use(
					[ 'form', 'table', 'tableFilter', 'laydate', 'tableSelect' ],
					function() {
						var table = layui.table, form = layui.form, tableFilter = layui.tableFilter, laydate = layui.laydate;

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
							},
							// ,{type:'checkbox'}
							// ,{field:'id', title:'ID', width:80,
							// unresize:true, sort:true}
							{
								field : 'ISERROR',
								title : '是否异常',
								width : 80,
								templet:'#statusTpl'	
							}, {
								field : 'ATT_DATE',
								title : '日期',
								sort : true
							}, {
								field : 'EMP_CODE',
								title : '工号',
								width : 80,
							}, {
								field : 'EMP_NAME',
								title : '姓名',
								width : 80,
							}, {
								field : 'DEPT_ID',
								title : '部门',
								width : 80,
							}, {
								field : 'CLASS_ID',
								title : '班次',
								width : 70,
								templet:function (d){	
				                	if(d.CLASS_ID=="1"){
				                		return "白班"
				                	}else if(d.CLASS_ID=="2"){
				                		return "晚班"
				                	}else{
				                		return "其他"
				                	}
				                }
							}, {
								field : 'ACT_HOURS',
								title : '正班时数',
								width : 80,
							}, {
								field : 'OVERTIME_COMM',
								title : '平时加班',
								width : 80,
							},{
								field : 'OVERTIME_HOLIDAY',
								title : '假日加班',
								width : 80,
							},{
								field : 'OVERTIME_YEAR',
								title : '法定加班',
								width : 80,
							},{
								field : 'OVERTIME_YEAR',
								title : '法定加班',
								width : 80,
							},{
								field : 'OVERTIME_YEAR',
								title : '法定加班',
								width : 80,
							}, {
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
							page : false,
							cols : [ [ {
								type : 'numbers'
							},
							{
								field : 'ISERROR',
								title : '是否异常',
								width : 100,sort : true,
								templet:'#statusTpl'	
							}, {
								field : 'TASK_NO',
								title : '制令单',width : 200,
								sort : true
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

						getEmpCode();

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
								"line_id" : "",
								"empCode" : data.field.empCode,
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
	     url:context+'/report/worktime/getList',
        where:params,
	     done: function(res1, curr, count){
              pageCurr=curr;
          }
	}) 
}

function getDetail(param){
	CoreUtil.sendAjax("/report/worktime/getListDetail", JSON.stringify(param),
			function(data) {
				if (data.result) {
					if (data.result) {
						tableIns1.reload({
							data : data.data
						});
						openData("查看明细")
					} else {
						layer.alert(data.msg);
					}
				} else {
					layer.alert(data.msg);
				}
			}, "POST", false, function(res) {
				layer.alert(res.msg);
			});
}


function getEmpCode() {
	CoreUtil.sendAjax("/report/worktime/getEmpCode", "",
			function(data) {
				// console.log(data)
				if (data.result) {
					if (data.result) {
						$("#empCode").empty();
						var list = data.data;
						for (var i = 0; i < list.length; i++) {
							if (i == 0) {
								$("#empCode").append(
										"<option value=''>请点击选择</option>");
							}
							$("#empCode").append(
									"<option value=" + list[i].EMP_CODE + ">"
											+ list[i].EMP_CODE + "——"
											+ list[i].EMP_NAME + "</option>");
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
