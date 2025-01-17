/**
 * 小码校验
 */
var pageCurr;
var tabledata = [];
$(function() {
	getProc("");
	getType("");
	layui
		.use(
			[ 'table', 'form', 'layedit', 'tableSelect' ],
			function() {
				var form = layui.form, layer = layui.layer, layedit = layui.layedit, table = layui.table,table1 = layui.table, tableSelect = layui.tableSelect;
				tableIns = table.render({
					elem : '#colTable',
					// ,url:context+'/interfaces/getRequestList'
					where : {},
					height: 'full-245'
					,even:true,//条纹样式
					method : 'get',// 默认：get请求
					defaultToolbar : [],
					page : true,
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
					}, {
						field : 'id',
						title : 'id',
						width : 0,
						hide : true
					}, {
						field : 'ITEM_BARCODE',
						title : '箱号条码',
						width : 160,sort: true
					}, {
						field : 'PROD_DATE',
						title : '生产日期',
						width : 100,sort: true
					}, {
						field : 'LINE_MAN',
						title : '组长',
						width : 100,sort: true
					}, {
						field : 'ITEM_MODEL',
						title : '机型',
						width : 100,sort: true
					}, {
						field : 'QTY',
						title : '数量',
						width : 80,sort: true
					}, {
						field : 'COLOUR',
						title : '颜色',
						width : 100,sort: true
					}, {
						field : 'ITEM_NAME_S',
						title : '物料简称',
						width : 100,sort: true
					}, {
						field : 'TASK_NO',
						title : '生产制令单',
						width : 150,sort: true
					}, {
						field : 'USER_NAME',
						title : '送检人',
						width : 100,sort: true
					}, {
						field : 'SYSDATE',
						title : '送检时间',
						width : 160,sort: true
					} ] ],
					done : function(res, curr, count) {
						pageCurr = curr;
					}
				});
				// 监听

				//监听提交
				form.on('submit(hsearchSubmit)', function(data){
					hTableIns.reload({
						url:context+'/produce/transit/getHistoryList',
						where:data.field
					});
					return false;
				});
				hTableIns = table1.render({// 历史
					elem : '#hcolTable',
					where : {},
					method : 'get',// 默认：get请求
					defaultToolbar : [],
					page : true,
					data : [],
					height: 'full-210'
					,even:true,//条纹样式
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
					cols : [ [ {fixed:'left',
						type : 'numbers'
					}, {fixed:'left',
						field : 'LINE_NO',
						title : '组长',
						width : 80,sort: true
					},{fixed:'left',
						field : 'TASK_NO',
						title : '制令单号',
						width : 150,sort: true
					},
						{fixed:'left',
							field : 'ITEM_NO',
							title : '物料编码',width : 150,sort: true
						},{fixed:'left',
							field : 'ITEM_BARCODE',
							title : '条码',
							width : 160,sort: true
						},
						{
							field : 'ITEM_NAME',
							title : '物料描述',
							width : 180,
							sort: true
						},
						{
							field : 'PROC_NAME',
							title : '工序名称',
							width : 120,sort: true
						},
						{
							field : 'INSP_NAME',
							title : '送检类型',
							width : 100,sort: true
						},  {
							field : 'USER_NAME',
							title : '送检人',
							width : 90,sort: true
						},  {
							field : 'CREATE_DATE',
							title : '送检时间',
							width : 150,sort: true
						}] ],
					done : function(res, curr, count) {
						pageCurr = curr;
					}
				});
			});
	$('#barcode').bind('keypress', function(event) {
		if (event.keyCode == "13") {
			if ($('#barcode').val()) {
				if ($('#proc').val()) {
					checkBarcode($('#proc').val(), $('#barcode').val());
				}
			} else {
				layer.alert("请完善数据");
				return false;
			}
		}
	});

	function getType(keyword) {
		CoreUtil.sendAjax("/produce/transit/getType", keyword, function(data) {
			//console.log(data)
			if (data.result) {
				var da = data.data;
				// $("#in_type").empty();
				// for (var i = 0; i < da.length; i++) {
				// 	if (i == 0) {
				// 		$("#in_type").append("<option value=''>请点击选择</option>");
				// 	}
				// 	$("#in_type").append(
				// 			"<option value=" + da[i].INSP_CODE + ">"
				// 					+ da[i].INSP_NAME + "</option>");
				// }
				layui.form.render('select');
			} else {
				layer.alert(data.msg);
			}
		}, "GET", false, function(res) {
			layer.alert(res.msg);
		});
	}
});

function getProc(keyword) {
	CoreUtil.sendAjax("/produce/transit/getProc", keyword, function(data) {
		//console.log(data)
		if (data.result) {
			var da = data.data;
			$("#proc").empty();
			for (var i = 0; i < da.length; i++) {
				if (i == 0) {
					$("#proc").append("<option value=''>请点击选择</option>");
				}
				$("#proc").append(
					"<option value=" + da[i].PROC_NO + ">"
					+ da[i].PROC_NAME + "</option>");
			}
			//layui.form.render('select');
		} else {
			layer.alert(data.msg);
		}
	}, "GET", false, function(res) {
		layer.alert(res.msg);
	});
}

function getType1(keyword) {
	CoreUtil.sendAjax("/produce/transit/getType", keyword, function(data) {
		//console.log(data)
		if (data.result) {
			var da = data.data;
			// $("#in_type").empty();
			// for (var i = 0; i < da.length; i++) {
			// 	if (i == 0) {
			// 		$("#in_type").append("<option value=''>请点击选择</option>");
			// 	}
			// 	$("#in_type").append(
			// 			"<option value=" + da[i].INSP_CODE + ">"
			// 					+ da[i].INSP_NAME + "</option>");
			// }
			//layui.form.render('select');
		} else {
			layer.alert(data.msg);
		}
	}, "GET", false, function(res) {
		layer.alert(res.msg);
	});
}

function checkBarcode(proc, barcode) {
	var in_type = "";
	// var in_type = $("#in_type").val();
	// if(in_type==""){
	// 	layer.alert("请完善数据");
	// 	return false;
	// }
	var params = {
		"proc" : proc,
		"barcode" : barcode,
		"ptype":in_type
	}
	CoreUtil.sendAjax("/produce/transit/checkBarcode", JSON.stringify(params),
		function(data) {
			console.log(data)
			if (data.result) {
				playSaoMiaoMusic();
				console.log(proc + "|" + in_type + "|" + barcode);
				saveData(proc,in_type,barcode,data.data);
			} else {
				playMusic();
				layer.alert(data.msg,function () {
					$('#barcode').val('');
					$('#barcode').focus();
					layer.closeAll();
				});
			}
		}, "POST", false, function(res) {
			layer.alert(res.msg);
		});
}
function saveData(proc, type, barcode,dataList) {
	var params = {
		"proc" : proc,
		"type" : type,
		"barcode" : barcode
	}
	CoreUtil.sendAjax("/produce/transit/saveData", JSON.stringify(params),
		function(data) {
			if(data.result){
				tableIns.reload({
					data : dataList
				});
				layer.alert("条码数据保存成功!");
			}else {
				layer.alert(data.msg);
			}
			console.log(data)
			$("#barcode").val("")
		}, "POST", false, function(res) {
			layer.alert(res.msg);
		});
}
