/**
 * 小码校验
 */
var pageCurr;
var tabledata = [];
$(function() {
	layui
			.use(
					[ 'table', 'form', 'layer', 'tableSelect', 'laydate' ],
					function() {
						var form = layui.form, layer = layui.layer, table = layui.table, tableSelect = layui.tableSelect, laydate = layui.laydate;
						sTableIns = table.render({// 返工扫描
							elem : '#scolTable',
							where : {},
							method : 'get',// 默认：get请求
							defaultToolbar : [],
							page : false,
							data : [],
							request : {
								pageName : 'page', // 页码的参数名称，默认：page
								limitName : 'rows' // 每页数据量的参数名，默认：limit
							},
							parseData : function(res) {// 可进行数据操作
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
								field : 'BOARD_BARCODE',
								title : '条码',
								width : 150
							}, {
								field : 'FTYPE',
								title : '类型',
								width : 150
							}, {
								field : 'LINER_NAME',
								title : '线长',
								width : 80
							}, {
								field : 'BOARD_ITEM',
								title : '物料编码',
								width : 170
							}, {
								field : 'TASK_NO',
								title : '制定单号',
								width : 350
							}, {
								field : 'FMEMO',
								title : '备注',
								width : 200
							}, {
								field : 'CREATE_DATE',
								title : '创建时间',
								width : 170
							} ] ],
							done : function(res, curr, count) {
								pageCurr = curr;
							}
						});

						hTableIns = table.render({// 返工历史
							elem : '#hcolTable',
							where : {},
							method : 'get',// 默认：get请求
							defaultToolbar : [],
							page : false,
							data : [],
							request : {
								pageName : 'page', // 页码的参数名称，默认：page
								limitName : 'rows' // 每页数据量的参数名，默认：limit
							},
							parseData : function(res) {// 可进行数据操作
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
								field : 'BOARD_BARCODE',
								title : '条码',
								width : 150
							}, {
								field : 'FTYPE',
								title : '类型',
								width : 150
							}, {
								field : 'LINER_NAME',
								title : '线长',
								width : 80
							}, {
								field : 'BOARD_ITEM',
								title : '物料编码',
								width : 170
							}, {
								field : 'TASK_NO',
								title : '制定单号',
								width : 350
							}, {
								field : 'FMEMO',
								title : '备注',
								width : 200
							}, {
								field : 'CREATE_DATE',
								title : '创建时间',
								width : 170
							} ] ],
							done : function(res, curr, count) {
								pageCurr = curr;
							}
						});

						sTableSelect = tableSelect.render({// 返工扫描-制令单
							elem : '#sTaskno',
							searchKey : 'keyword',
							checkedKey : 'id',
							searchPlaceholder : '试着搜索',
							table : {
								url : context + 'produce/rework/getTaskNo',
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
									title : '制令单号'
								} ] ],
								parseData : function(res) {
									// console.log(res)
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
								// console.log(data)
								var da = data.data;
								form.val("scanFrom", {
									"sTaskno" : da[0].TASK_NO
								});
								form.render();// 重新渲染
							}
						});

						hTableSelect = tableSelect.render({// 返工历史-制令单
							elem : '#hTaskno',
							searchKey : 'keyword',
							checkedKey : 'id',
							searchPlaceholder : '试着搜索',
							table : {
								url : context
										+ 'produce/rework/getReworkTaskNo',
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
									title : '制令单号'
								} ] ],
								parseData : function(res) {
									// console.log(res)
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
								// console.log(data)
								var da = data.data;
								$("#hTaskno").val(da[0].TASK_NO)
								// form.val("hisFrom", {
								// "hTaskno" : da[0].TASK_NO
								// });
								// form.render();// 重新渲染
							}
						});

						form.on('radio(isRecord)', function(data) {// 监听单选框
							// console.log(data)
							if (data.value == "0") {
								$('#sTaskno').val("");
								$('#sTaskno').attr("disabled", "disabled");
							} else {
								$('#sTaskno').val("");
								$('#sTaskno').removeAttr("disabled");
							}
						})
						// 日期选择器
						laydate.render({
							elem : '#hStartTime',
							type : 'date' // 默认，可不填
						});
						laydate.render({
							elem : '#hEndTime',
							type : 'date' // 默认，可不填
						});
						// 监听
						form.on('submit(searchSubmit)', function(data) {
							
								search(data.field);
							
							// console.log(data.field)
						});
					});

	$('#sBarcode').bind('keypress', function(event) {
		if (event.keyCode == "13") {
			reworkCode();
		}
	});
});
function reworkCode() {
	if ($("#sTaskno").val()==""&&$("input[name='isRecord']:checked").val()=="1") {
		layer.alert("请先选择制令单！")
		return false;
	}
	var params = {
		"taskNo" : $("#sTaskno").val(),
		"type" : $("input[name='isRecord']:checked").val(),
		"barcode" : $("#sBarcode").val(),
		"memo" : $("#sMemo").val()
	}
	// console.log(params);
	CoreUtil.sendAjax("produce/rework/subCode", JSON.stringify(params),
			function(data) {
				// console.log(data)
				if (data.result) {
					sTableIns.reload({
						data : data.data
					});
					layer.alert("扫描条码返工成功！");
				} else {
					layer.alert(data.msg);
				}
			}, "POST", false, function(res) {
				layer.alert(res.msg);
			});
}
function search(obj) {
	var params = {
		"startTime" : obj.hStartTime,
		"endTime" : obj.hEndTime,
		"taskNo" : obj.hTaskno,
		"barcode" : obj.hBarcode
	};

	CoreUtil.sendAjax("produce/rework/search", JSON.stringify(params),
			function(data) {
				console.log(data)
				if (data.result) {
					hTableIns.reload({
						data : data.data
					});
					//layer.alert("查询成功");
				} else {
					layer.alert(data.msg);
				}
			})
}
