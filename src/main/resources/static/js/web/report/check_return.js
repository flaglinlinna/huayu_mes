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
								title : '1号',
								width : 80
							}, {
								field : 'F2',
								title : '2号',
								width : 80
							}, {
								field : 'F3',
								title : '3号',
								width : 80
							}, {
								field : 'F4',
								title : '4号',
								width : 80
							}, {
								field : 'F5',
								title : '5号',
								width : 80
							}, {
								field : 'F6',
								title : '6号',
								width : 80
							}, {
								field : 'F7',
								title : '7号',
								width : 80
							}, {
								field : 'F8',
								title : '8号',
								width : 80
							}, {
								field : 'F9',
								title : '9号',
								width : 80
							}, {
								field : 'F10',
								title : '10号',
								width : 80
							}, {
								field : 'F11',
								title : '11号',
								width : 80
							}, {
								field : 'F12',
								title : '12号',
								width : 80
							}, {
								field : 'F13',
								title : '13号',
								width : 80
							}, {
								field : 'F14',
								title : '14号',
								width : 80
							}, {
								field : 'F15',
								title : '15号',
								width : 80
							}, {
								field : 'F16',
								title : '16号',
								width : 80
							}, {
								field : 'F17',
								title : '17号',
								width : 80
							}, {
								field : 'F18',
								title : '18号',
								width : 80
							}, {
								field : 'F19',
								title : '19号',
								width : 80
							}, {
								field : 'F20',
								title : '20号',
								width : 80
							},{
								field : 'F21',
								title : '21号',
								width : 80
							}, {
								field : 'F22',
								title : '22号',
								width : 80
							}, {
								field : 'F23',
								title : '23号',
								width : 80
							}, {
								field : 'F24',
								title : '24号',
								width : 80
							}, {
								field : 'F25',
								title : '25号',
								width : 80
							}, {
								field : 'F26',
								title : '26号',
								width : 80
							}, {
								field : 'F27',
								title : '27号',
								width : 80
							}, {
								field : 'F28',
								title : '28号',
								width : 80
							}, {
								field : 'F29',
								title : '29号',
								width : 80
							}, {
								field : 'F30',
								title : '30号',
								width : 80
							},{
								field : 'F31',
								title : '31号',
								width : 80
							}, {
								field : 'FSUM',
								title : '合计',
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
