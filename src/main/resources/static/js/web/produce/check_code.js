/**
 * 小码校验
 */
var pageCurr;
var tabledata = [];
$(function() {
	layui
			.use(
					[ 'table', 'form', 'layedit', 'tableSelect' ],
					function() {
						var form = layui.form, layer = layui.layer, layedit = layui.layedit, table = layui.table, tableSelect = layui.tableSelect;
						;

						tableIns = table.render({
							elem : '#colTable'
							// ,url:context+'/interfaces/getRequestList'
							,
							where : {},
							method : 'get' // 默认：get请求
							// ,toolbar: '#toolbar' //开启头部工具栏，并为其绑定左侧模板
							,
							defaultToolbar : [],
							page : false,
							data : [],
							request : {
								pageName : 'page' // 页码的参数名称，默认：page
								,
								limitName : 'rows' // 每页数据量的参数名，默认：limit
							},
							parseData : function(res) {

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
							},{
								field : 'taskNo',
								title : '制定单号',
								width : 350
							}, {
								field : 'barcode1',
								title : '条码1',
								width : 200
							}, {
								field : 'barcode2',
								title : '条码2',
								width : 200
							}, {
								field : 'result',
								title : '校验结果'
							} ] ],
							done : function(res, curr, count) {
								pageCurr = curr;
							}
						});
						tableSelect = tableSelect.render({
							elem : '#taskno',
							searchKey : 'keyword',
							checkedKey : 'id',
							searchPlaceholder : '试着搜索',
							table : {
								url : context + 'produce/check_code/getTaskNo',
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
									field : 'TASK_NO',
									title : '制令单号',
									width : 180
								}, {
									field : 'ITEM_NO',
									title : '物料编码',
									width : 150
								}, {
									field : 'LINER_NAME',
									title : '组长',
									width : 100
								}, {
									field : 'CUST_NAME_S',
									title : '客户(简称)',
									width : 100
								} ] ],
								parseData : function(res) {
									//console.log(res)
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
								// console.log(da[0].num)
								form.val("itemFrom", {
									"taskno" : da[0].TASK_NO,
									"itemcode" : da[0].ITEM_NO,
									"liner" : da[0].LINER_NAME,
									"cust" : da[0].CUST_NAME_S
								});
								form.render();// 重新渲染
							}
						});
					});

	$('#barcode').bind('keypress', function(event) {
		if (event.keyCode == "13") {
			$('#barcode1').val("");
			$('#barcode2').val("");
			if ($('#barcode').val()) {
				if ($('#taskno').val()) {
					subCode($('#taskno').val(), $('#barcode').val(), "")
				} else {
					layer.alert("请选择制令单号!");
				}

			} else {
				layer.alert("请先扫描条码!");
			}
		}
	});
	$('#barcode1').bind('keypress', function(event) {
		if (event.keyCode == "13") {
			document.getElementById("barcode2").focus();
		}
	});
	$('#barcode2').bind(
			'keypress',
			function(event) {
				if (event.keyCode == "13") {
					$('#barcode').val("");
					if ($('#barcode1').val() || $('#barcode2').val()) {
						if ($('#taskno').val()) {
							subCode($('#taskno').val(), $('#barcode1').val(),
									$('#barcode2').val())
						} else {
							layer.alert("请选择制令单号!");
						}
					} else {
						layer.alert("请先扫描条码!");
					}
				}
			});
});

function subCode(taskNo, barcode1, barcode2) {
	var params = {
		"taskNo" : taskNo,
		"barcode1" : barcode1,
		"barcode2" : barcode2
	}
	CoreUtil.sendAjax("produce/check_code/subCode", JSON.stringify(params),
			function(data) {
		console.log(data)
				if (data.result) {
					var dataT={
							taskNo:	taskNo,
							barcode1:barcode1,
							barcode2:barcode2,
							result:"校验成功"
					}
					tabledata.push(dataT);
					tableIns.reload({
						data : tabledata
					});
					$('#barcode').val("");
					$('#barcode1').val("");
					$('#barcode2').val("");
				} else {
					layer.alert(data.msg);
				}
				//console.log(tabledata)
			}, "POST", false, function(res) {
				layer.alert(res.msg);
			});
}
