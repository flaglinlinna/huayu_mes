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
						var form = layui.form, layer = layui.layer, layedit = layui.layedit, table = layui.table,
							table1 = layui.table, tableSelect = layui.tableSelect,tableSelect1 = layui.tableSelect;
						var laydate = layui.laydate;
						layui.form.render();
						// 日期选择器
						laydate.render({
							elem : '#hStartTime',
							type : 'date' // 默认，可不填
						});
						laydate.render({
							elem : '#hEndTime',
							type : 'date' // 默认，可不填
						});


						tableIns = table.render({
							elem : '#colTable'
							// ,url:context+'/interfaces/getRequestList'
							,
							where : {},
							method : 'get' // 默认：get请求
							// ,toolbar: '#toolbar' //开启头部工具栏，并为其绑定左侧模板
							,
							defaultToolbar : [],
							height:'full-380'//固定表头&full-查询框高度
							,even:true,//条纹样式
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
							cols : [ [
								{fixed: "left", type : 'numbers'},
								{fixed: "left", field : 'itemCode', title : '产品编码', width : 150},
								{fixed: "left", field : 'linerName', title : '组长', width : 70, sort : true},
								{fixed: "left", field : 'barcode1', title : '条码1', width : 230, sort : true},
								{fixed: "left", field : 'taskNo', title : '制令单号', width : 150, sort : true},
								{field : 'scanTime', title : '扫描时间', width : 150, sort : true},
								{field : 'result', title : '扫描结果', width : 90},
								{field : 'barcode2', title : '条码2', width : 230, sort : true}
								] ],
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
										width:750,
										url : context
												+ '/produce/check_code/getTaskNo',
										method : 'get',
										
										cols : [ [ {type : 'radio'},// 多选 radio
										 {field : 'id', title : 'id', width : 0, hide : true},
											{field : 'WS_SECTION', title : '工段', width : 70},
											{field : 'LINER_NAME', title : '组长', width : 70},
											{field : 'PROD_DATE', title : '计划日期', width : 100,
												templet:function (d) {
													if(d.PROD_DATE!=null){
														return /\d{4}-\d{1,2}-\d{1,2}/g.exec(d.PROD_DATE)
													}
												}
											},
											 {field : 'ITEM_NO', title : '物料编码', width : 170},
											{field : 'ITEM_NAME', title : '物料描述', width : 240},
											{field : 'FMEMO', title : '备注', width : 120},
											{field : 'CUST_NAME_S', title : '客户(简称)', width : 100},
											{field : 'TASK_NO', title : '制令单号', width : 180, sort : true},
										] ],
										page : false,
										request : {
											pageName : 'page' // 页码的参数名称，默认：page
											,
											limitName : 'rows' // 每页数据量的参数名，默认：limit
										},
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
										// 选择完后的回调，包含2个返回值
										// elem:返回之前input对象；data:表格返回的选中的数据 []
										var da = data.data;
										// console.log(da[0].num)
										form.val("itemFrom", {
											"taskno" : da[0].TASK_NO,
											"itemcode" : da[0].ITEM_NO,
											"liner" : da[0].LINER_NAME,
											// "cust" : da[0].CUST_NAME_S
										});
										form.render();// 重新渲染
									}
								});

						initSelect();

						tableSelect1 = tableSelect1.render({
							elem : '#itemcode',
							searchKey : 'keyword',
							checkedKey : 'ITEM_NO',
							searchPlaceholder : '试着搜索',
							page : true,
							request : {
								pageName : 'page' // 页码的参数名称，默认：page
								,
								limitName : 'rows' // 每页数据量的参数名，默认：limit
							},
							table : {
								url : context + '/produce/check_code/getItemCode',
								method : 'get',
								cols : [ [ {
									type : 'radio'
								},// 多选 radio
									{
										field : 'ITEM_NO',
										title : '物料编号',
										width : 150,
										sort : true
									}, {
										field : 'ITEM_NAME',
										title : '物料描述',
										width : 400
									},
									{
										field : 'ITEM_NAME_S',
										title : '物料简称',
										width : 110,
										sort : true
									},
								] ],
								parseData : function(res) {
									//console.log(res)
									if (res.result) {
										// 可进行数据操作
										return {
											"count" : res.data.total,
											"msg" : res.msg,
											"data" : res.data.rows,
											"code" : res.status
											// code值为200表示成功
										}
									}
								},
							},
							done : function(elem, data) {
								var da = data.data;
								form.val("itemFrom", {
									"itemcode" : da[0].ITEM_NO,
									// "itemName":da[0].ITEM_NAME,
								});
								form.render();// 重新渲染
							}
						});

						// 监听提交
						form.on('submit(hsearchSubmit)', function(data) {
							data.field.errorFlag = $("#errorFlag").prop('checked')?1:0
							hTableIns.reload({
								url : context
										+ '/produce/check_code/getHistoryList',
								where : data.field,
								done : function(res, curr, count) {
									pageCurr = curr;
									if(data.field.errorFlag ==1){
										res.data.forEach(function (item, index) {
											$('div[lay-id="hcolTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="FMEMO"]').removeClass("layui-hide");
											$('div[lay-id="hcolTable"]').find('thead').find('th[data-field="FMEMO"]').removeClass("layui-hide");
										});
										}
								}
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
							height:'full-80'//固定表头&full-查询框高度
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
							cols : [ [
								{fixed:'left', type : 'numbers'},
								{fixed : 'left', field:"ITEM_NO", title : '产品编码', width : 140},
								{fixed : 'left', field : 'LINER_NAME', title : '组长', width : 70},
								{fixed : 'left', field : 'BARCODE_S_1', title : '条码1', width : 220},
								{fixed : 'left', field : 'TASK_NO', title : '制令单号', width : 150},
								{field : 'CHK_RESULT', title : '扫描结果', width : 100},
								{field : 'CREATE_DATE', title : '扫描时间', width : 145},
								{field : 'USER_NAME', title : '扫描人', width : 70},
								{field:"FMEMO", title : '备注', width : 300,hide: true},
								{field : 'BARCODE_S_2', title : '条码2', width : 200}
								] ],
							done : function(res, curr, count) {
								pageCurr = curr;
							}
						});

					});

	$('#barcode').bind('keypress', function(event) {
		if (event.keyCode == "13") {
			$('#barcode1').val("");
			$('#barcode2').val("");
			if ($('#barcode').val()) {
				if ($('#itemcode').val()||$('#taskno').val()) {
					subCode($('#taskno').val(), $('#barcode').val(), "")
					$('#barcode').val("");
				} else {
					layer.alert("请选择制令单号或产品编码!");
				}

			} else {
				layer.alert("请先扫描条码!");
			}
		}
	});
	$('#barcode1').bind('keypress', function(event) {
		if (event.keyCode == "13") {
			$('#barcode').val("");
			$('#barcode2').val("");
			$('#barcode2').focus();
		}
	});
	$('#barcode2').bind(
			'keypress',
			function(event) {
				if (event.keyCode == "13") {
					$('#barcode').val("");
					if ($('#barcode1').val() != ""
							&& $('#barcode2').val() != "") {
						if ($('#taskno').val()) {
							subCode($('#taskno').val(), $('#barcode1').val(),
									$('#barcode2').val())
									$('#barcode').val("");
							$('#barcode1').val("");
							$('#barcode2').val("");
						} else {
							layer.alert("请选择制令单号!");
						}
					} else {
						layer.alert("请先扫描条码!");
					}
				}
			});
});

function initSelect() {
	CoreUtil.sendAjax("/produce/check_code/getLiner", "",
		function(data) {
			if (data.result) {
				$("#liner").empty();
				var linerList = data.data;
				for (var i = 0; i < linerList.length; i++) {
					if (i == 0) {
						$("#liner").append("<option value=''> 请选择组长</option>");
					}
					$("#liner").append(
						"<option value='" + linerList[i].LEAD_BY + "'>"
						+ linerList[i].LEAD_BY + "</option>");
				}
				layui.form.render('select');
			}else {
				layer.alert(res.msg);
			}

		}, "GET", false, function(res) {
			layer.alert(res.msg);
		});
}

function subCode(taskNo, barcode1, barcode2) {
	var itemCode = $('#itemcode').val();
	var linerName = $('#liner').val();
	var params = {
		"taskNo" : taskNo,
		"barcode1" : barcode1,
		"barcode2" : barcode2,
		"itemCode":itemCode,
		"linerName":linerName
	}
	var nowTime = new Date().format('yyyy-MM-dd hh:mm:ss');
	// nowTime.format('yyyy-MM-dd HH:mm:ss');
	CoreUtil.sendAjax("/produce/check_code/subCode", JSON.stringify(params),
			function(data) {
				//console.log(data)
				if (data.result) {
					playSaoMiaoMusic();
					var dataT = {
						taskNo : taskNo,
						barcode1 : barcode1,
						barcode2 : barcode2,
						itemCode:itemCode,
						linerName:linerName,
						scanTime:nowTime,
						result : "扫描成功"
					}
					$("#barNum").val(data.data);
					tabledata.push(dataT);
					tableIns.reload({
						data : tabledata
					});
					
				} else {
					playMusic();
					layer.alert(data.msg, function(index) {
						if(barcode2==""){
							$("#barcode").focus();
						}else{
							$("#barcode1").focus();
						}
						layer.close(index);
					});
				}
				if(barcode2==""){
					$("#barcode").focus();
				}else{
					$("#barcode1").focus();
				}
			}, "POST", false, function(res) {
				layer.alert(res.msg);
			});

}

Date.prototype.format = function(fmt) {
	var o = {
		"M+" : this.getMonth()+1,                 //月份
		"d+" : this.getDate(),                    //日
		"h+" : this.getHours(),                   //小时
		"m+" : this.getMinutes(),                 //分
		"s+" : this.getSeconds(),                 //秒
		"q+" : Math.floor((this.getMonth()+3)/3), //季度
		"S"  : this.getMilliseconds()             //毫秒
	};
	if(/(y+)/.test(fmt)) {
		fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
	}
	for(var k in o) {
		if(new RegExp("("+ k +")").test(fmt)){
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
		}
	}
	return fmt;
}