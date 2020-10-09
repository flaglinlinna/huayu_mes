/**
 * 生产投入
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
							cellMinWidth : 80,
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
								type : 'radio',
								width : 50
							}, {
								field : 'id',
								title : 'id',
								width : 0,
								hide : true
							}, {
								field : 'ITEM_BARCODE',
								title : '条码',
								width : 120
							}, {
								field : 'ITEM_NO',
								title : '物料编号',
								width : 150
							}, {
								field : 'ITEM_NAME',
								title : '物料描述'
							}, {
								field : 'QUANTITY',
								title : '数量',
								width : 90
							}, {
								field : 'CREATE_DATE',
								title : '创建时间',
								width : 150
							}, {
								fixed : 'right',
								title : '操作',
								align : 'center',
								toolbar : '#optBar',
								width : 80
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
									field : 'ITEM_NAME',
									title : '物料描述',
									width : 240
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
									console.log(res)
									if (res.result) {
										// 可进行数据操作
										return {
											"count" : 1000,
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

						// 监听工具条
						table.on('tool(colTable)', function(obj) {
							console.log(obj)
							var data = obj.data;
							console.log(data)
							if (obj.event === 'del') {
								// 删除
								del(obj, data.ID, data.ITEM_BARCODE);
							}
						});

						// 监听
						form.on('submit(confirmSubmit)', function(data) {
							console.log(data.field)
							addPut(data.field)
						});
					});

	$('#barcode').bind('keypress', function(event) {
		if (event.keyCode == "13") {
			// alert('你输入的内容为：' + $('#barcode').val());
			if ($('#barcode').val()) {
				subCode($('#barcode').val())
			} else {
				layer.alert("请先扫描条码!");
			}
		}
	});
});

function subCode(barcode) {
	var params = {
		"barcode" : barcode
	}
	CoreUtil.sendAjax("produce/check_code/subCode", params, function(data) {
		console.log(data)
		if (data.result) {
			
		} else {
			layer.alert(data.msg);
		}
	}, "GET", false, function(res) {
		layer.alert(res.msg);
	});
}





function addPut(obj) {
	var params = {
		"barcode" : obj.barcode,
		"task_no" : obj.num,
		"item_no" : obj.item_code,
		"qty" : obj.addqty
	};
	CoreUtil.sendAjax("input/addPut", params, function(data) {
		console.log(data)
		if (data.result) {
			$("#inqty").val(data.data.Qty);
			tableIns.reload({
				data : data.data.List
			});
		} else {
			layer.alert(data.msg);
		}
		$("input[name='barcode']").val('');
		$("input[name='item_code']").val('');
		$("input[name='addqty']").val('');
	}, "GET", false, function(res) {
		layer.alert(res.msg);
	});
}

// 删除
function del(obj, id, barcode) {
	if (id != null) {
		var params = {
			"barcode" : id
		};
		layer.confirm('您确定要删除条码' + barcode + '吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("input/delete", params, function(data) {
				console.log(data)
				if (data.result == true) {
					// 回调弹框
					layer.alert("删除成功！", function() {
						$("#inqty").val(data.data);
						obj.del(); // 删除对应行（tr）的DOM结构，并更新缓存
						layer.closeAll();
					});
				} else {
					layer.alert(data, function() {
						layer.closeAll();
					});
				}
			}, "GET", false, function(res) {
				layer.alert(res.msg);
			});
		});
	}
}
