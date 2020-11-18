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
							method : 'get' // 默认：get请求
							,
							toolbar : '#toolbar' // 开启工具栏，此处显示默认图标，可以自定义模板，详见文档
							,
							cellMinWidth : 80,
							height : 'full-180'// 固定表头&full-查询框高度
							,
							even : true,// 条纹样式
							data : [],
							height : 'full',
							page : false,
							request : {
								pageName : 'page' // 页码的参数名称，默认：page
								,
								limitName : 'rows' // 每页数据量的参数名，默认：limit
							},
							parseData : function(res) {
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
							}, {
								field : 'WORK_DATE',
								title : '日期',
								width : 130,
								sort : true
							}, {
								field : 'EMP_CODE',
								title : '工号',
							}, {
								field : 'EMP_NAME',
								title : '姓名',
							}, {
								field : 'DEPT_ID',
								title : '部门',
								width : 90,
							}, {
								field : 'HR_CARD1',
								title : '上班一（hr）',
								width : 120,
							}, {
								field : 'MES_CARD1',
								title : '上班一（mes）',
								width : 120,
							}, {
								field : 'HR_CARD2',
								title : '下班一（hr）',
								width : 120,
							}, {
								field : 'MES_CARD2',
								title : '下班一（mes）',
								width : 120,
							}, {
								field : 'HR_CARD3',
								title : '上班二（hr）',
								width : 120,
							}, {
								field : 'MES_CARD3',
								title : '上班二（mes）',
								width : 120,
							}, {
								field : 'HR_CARD4',
								title : '下班二（hr）',
								width : 120,
							}, {
								field : 'MES_CARD4',
								title : '下班二（mes）',
								width : 120,
							}, {
								field : 'HR_CARD5',
								title : '上班三（hr）',
								width : 120,
							}, {
								field : 'MES_CARD5',
								title : '上班三（mes）',
								width : 120,
							} , {
								field : 'HR_CARD6',
								title : '下班三（hr）',
								width : 120,
							}, {
								field : 'MES_CARD6',
								title : '下班三（mes）',
								width : 120,
							}, {
								field : 'HR_ACT_HOURS',
								title : '正班时数（hr）',
								width : 120,
							}, {
								field : 'MES_ACT_HOURS',
								title : '正班时数（mes）',
								width : 120,
							} , {
								field : 'HR_OVERTIME_COMM',
								title : '平时加班（hr）',
								width : 120,
							}, {
								field : 'MES_OVERTIME_COMM',
								title : '平时加班（mes）',
								width : 120,
							} , {
								field : 'HR_OVERTIME_HOLIDAY',
								title : '假日加班（hr）',
								width : 120,
							}, {
								field : 'MES_OVERTIME_HOLIDAY',
								title : '假日加班（mes）',
								width : 120,
							} , {
								field : 'HR_OVERTIME_YEAR',
								title : '法定加班（hr）',
								width : 120,
							}, {
								field : 'MES_OVERTIME_YEAR',
								title : '法定加班（mes）',
								width : 120,
							}  ] ],
							done : function(res, curr, count) {
								//
								pageCurr = curr;
							}
						});
						// localtableFilterIns = tableFilter.render({
						// 'elem' : '#listTable',
						// //'parent' : '#doc-content',
						// 'mode' : 'local',//本地过滤
						// 'filters' : [
						// {field: 'ITEM_NO', type:'input'},
						// {field: 'LINER_NAME', type:'input'},
						// {field: 'QTY_PLAN', type:'input'},
						// {field: 'QTY_DONE', type:'input'},
						// {field: 'QTY_PROC', type:'input'},
						// {field: 'CAPACITY', type:'input'},
						// {field: 'MANPOWER', type:'input'},
						// {field: 'HOUR_SDD', type:'input'},
						// {field: 'QTY_EMP', type:'input'},
						// ],
						// 'done': function(filters){}
						// });

						getEmpCode();

						// 监听搜索框
						form.on('submit(searchSubmit)', function(data) {
							// 重新加载table
							// console.log(data.field)
							var date = data.field.dates;
							var sdata = date.substring(0, date.indexOf(" "))
							var edata = date.substring(date.indexOf(" ") + 3,
									date.length);
							// console.log(sdata)
							// console.log(edata)

							var params = {
								"sdate" : sdata,
								"edate" : edata,
								"empCode" : data.field.empCode,
							};
							// console.log(params)
							getReport(params)
							return false;
						});
						// 日期范围
						laydate.render({
							elem : '#dates',
							trigger : 'click',
							range : true
						});

					});

});
function getReport(params) {
	CoreUtil.sendAjax("/report/differ/getList", JSON.stringify(params),
			function(data) {
				console.log(data)
				if (data.result) {
					if (data.result) {
						tableIns.reload({
							data : data.data.rows,
							done : function(res1, curr, count) {

								// //localtableFilterIns.reload();
							}
						});
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
	CoreUtil.sendAjax("/report/differ/getEmpCode", "",
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
