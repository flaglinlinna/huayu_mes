/**
 * 检验批数及退货报表
 */
var pageCurr;
$(function() {
	layui
			.use(
					[ 'form', 'table', 'tableFilter', 'laydate'],
					function() {
						var table = layui.table, form = layui.form, laydate = layui.laydate;

						tableIns = table.render({
							elem : '#listTable',
							// url : context + '/base/line/getList',
							method : 'get' // 默认：get请求
							,
							toolbar : '#toolbar' // 开启工具栏，此处显示默认图标，可以自定义模板，详见文档
							,
							cellMinWidth : 80,
							height : 'full-60'// 固定表头&full-查询框高度
							,
							even : true,// 条纹样式
							data : [],
							// height: 'full',
							page : true,
							limit:50,
							limits:[50,100,200,500,1000,5000],
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
								field : 'LINE_NAME',
								title : '产线组长',
								width : 90
							}, {
								field : 'FQC_NAME',
								title : 'FQC',
								width : 90
							}, {
								field : 'FDESC',
								title : '项目',
								width : 110
							}, {
								field : 'F1',
								title : 'F1',
								width : 80
							}, {
								field : 'F2',
								title : 'F2',
								width : 80
							}, {
								field : 'F3',
								title : 'F3',
								width : 80
							}, {
								field : 'F4',
								title : 'F4',
								width : 80
							}, {
								field : 'F5',
								title : 'F5',
								width : 80
							}, {
								field : 'F6',
								title : 'F6',
								width : 80
							}, {
								field : 'F7',
								title : 'F7',
								width : 80
							}, {
								field : 'F8',
								title : 'F8',
								width : 80
							}, {
								field : 'F9',
								title : 'F9',
								width : 80
							}, {
								field : 'F10',
								title : 'F10',
								width : 80
							}, {
								field : 'F11',
								title : 'F11',
								width : 80
							}, {
								field : 'F12',
								title : 'F12',
								width : 80
							}, {
								field : 'F13',
								title : 'F13',
								width : 80
							}, {
								field : 'F14',
								title : 'F14',
								width : 80
							}, {
								field : 'F15',
								title : 'F15',
								width : 80
							}, {
								field : 'F16',
								title : 'F16',
								width : 80
							}, {
								field : 'F17',
								title : 'F17',
								width : 80
							}, {
								field : 'F18',
								title : 'F18',
								width : 80
							}, {
								field : 'F19',
								title : 'F19',
								width : 80
							}, {
								field : 'F20',
								title : 'F20',
								width : 80
							},{
								field : 'F21',
								title : 'F21',
								width : 80
							}, {
								field : 'F22',
								title : 'F22',
								width : 80
							}, {
								field : 'F23',
								title : 'F23',
								width : 80
							}, {
								field : 'F24',
								title : 'F24',
								width : 80
							}, {
								field : 'F25',
								title : 'F25',
								width : 80
							}, {
								field : 'F26',
								title : 'F26',
								width : 80
							}, {
								field : 'F27',
								title : 'F27',
								width : 80
							}, {
								field : 'F28',
								title : 'F28',
								width : 80
							}, {
								field : 'F29',
								title : 'F29',
								width : 80
							}, {
								field : 'F30',
								title : 'F30',
								width : 80
							},{
								field : 'F31',
								title : 'F31',
								width : 80
							}, {
								field : 'FSUM',
								title : 'FSUM',
								width : 80
							} ] ],
							done : function(res, curr, count) {
								// localtableFilterIns.reload();
								pageCurr = curr;
							}
						});

						// 监听搜索框
						form.on('submit(searchSubmit)', function(data) {
							// 重新加载table
							getReport(data.field.dates, data.field.depart)
							return false;
						});
						// 日期范围
						laydate.render({
							elem : '#dates',
							trigger : 'click',
							type : 'month'
						});
					});

});
function getReport(dates, dept) {
	var params = {
		"month" : dates,
		"deptId" : dept
	}
	tableIns.reload({
		url : context + '/report/check_return/getList',
		where : params,
		done : function(res1, curr, count) {
			console.log(res1)
			pageCurr = curr;
		}
	})
}
