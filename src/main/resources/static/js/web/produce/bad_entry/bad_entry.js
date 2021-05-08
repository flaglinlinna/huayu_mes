/**
 * 小码校验
 */
var pageCurr;
var tabledata = [];
$(function() {
	getBadInfo("", "NA");
	layui
			.use(
					[ 'table', 'form', 'layedit', 'tableSelect' ],
					function() {
						var form = layui.form, layer = layui.layer, layedit = layui.layedit, table = layui.table, tableSelect = layui.tableSelect;
						tableIns = table.render({
							elem : '#colTable',
							// ,url:context+'/interfaces/getRequestList'
							where : {},
							method : 'get',// 默认：get请求
							defaultToolbar : [],
							height:'full-300'//固定表头&full-查询框高度
							,even:true,//条纹样式
							page : true,
							data : [],
							limit:100,
							limits:[100,200,500,1000,2000],
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
								field : 'BOARD_BARCODE',
								title : '小条码',
								width : 300,
								sort: true
							}, {
								field : 'BOARD_ITEM',
								title : '物料编码',
								width : 140
							}, {
								field : 'TASK_NO',
								title : '制令单',
								width : 150
							}, {
								field : 'DEFECT_NUM',
								title : '数量',
								width : 55
							}, {
								field : 'DEFECT_NAME',
								title : '不良现象',
								width : 110,sort: true
							}, {
								field : 'CREATE_DATE',
								title : '创建时间',
								width : 145
							}, {
								fixed : 'right',
								title : '操作',
								align : 'center',
								toolbar : '#optBar',
								width : 90
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
								width:750,
								url : context + '/produce/bad_entry/getTaskNo',
								method : 'get',
								cols : [ [ {
									type : 'radio'
								},// 多选 radio
								{field : 'id', title : 'id', width : 0, hide : true},
									{field : 'WS_SECTION', title : '工段', width : 70},
									{field : 'LINER_NAME', title : '组长', width : 70},
									{
										field : 'PROD_DATE',
										title : '计划日期',
										width : 100,
										templet:function (d) {
											if(d.PROD_DATE!=null){
												return /\d{4}-\d{1,2}-\d{1,2}/g.exec(d.PROD_DATE)
											}
										}
									},
									{field : 'ITEM_NO', title : '物料编码', width : 170},
									{field : 'ITEM_NAME', title : '物料描述', width : 250},
									{field : 'FMEMO', title : '备注', width : 120},
									{field : 'TASK_NO', title : '制令单号', width : 180},
								] ],
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
									"itemName" : da[0].ITEM_NAME
								});
								form.render();// 重新渲染
								getDetailByTask(da[0].TASK_NO);
							}
						});

						// 监听工具条
						table.on('tool(colTable)', function(obj) {
							var data = obj.data;
							if (obj.event === 'del') {
								// 删除
								// console.log(data)
								del(obj, data.ID, data.BOARD_BARCODE==null?"":data.BOARD_BARCODE);
							}
						});

						// 监听
//						form.on('submit(saveDef)', function(data) {
//							if (data.field.inputCode == "") {
//								layer.alert("请选择不良信息！");
//							} else {
//								//console.log(data.field)
//								saveBad(data.field)
//							}
//							
//						});
						form.on('select(defcode)', function(data) {
							//console.log(data)
							$("#defcode1").val("");
						});

						form.on('select(selectCode)',function(data) { // 选择移交单位 赋值给input框
											var select_text = data.elem[data.elem.selectedIndex].text;
											var str=select_text;
											//console.log(str.substring(0,5))
											//console.log(str.substring(7,str.length))
											$("#inputCode").val(str.substring(0,str.indexOf("——")));
											$("#selectCode").next().find("dl").css({"display" : "none"});
											$("#defName").val(str.substring(7,str.length));
											form.render();
										});
						
						$('#inputCode').bind('input propertychange',function() {
											var value = $("#inputCode").val();
											$("#selectCode").val(value);
											form.render();
											$("#selectCode").next().find("dl")
													.css({"display" : "block"});
											var dl = $("#selectCode").next().find("dl").children();
											var j = -1;
											for (var i = 0; i < dl.length; i++) {
												if (dl[i].innerHTML.indexOf(value) <= -1) {
													dl[i].style.display = "none";
													j++;
												}
												if (j == dl.length - 1) {
													$("#selectCode").next().find("dl").css({"display" : "none"});
												}
											}
										});
						
						//监听提交
				    	  form.on('submit(hsearchSubmit)', function(data){
				    		  hTableIns.reload({
				    			  url:context+'/produce/bad_entry/getHistoryList',
				                  where:data.field 
								});
				    	    return false;
				    	  });
						hTableIns = table.render({// 历史
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
								console.log(res)
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
								field : 'BOARD_BARCODE',
								title : '小条码',
								width : 300
							}, {fixed:'left',
								field : 'BOARD_ITEM',
								title : '物料编码',
								width : 150
							}, {fixed:'left',
								field : 'TASK_NO',
								title : '制令单号',
								width : 150
							},{
								field : 'DEFECT_NAME',
								title : '不良现象',
								width : 110
							}, {
								field : 'DEFECT_NUM',
								title : '数量',
								width : 60
							}, {
								field : 'USER_NAME',
								title : '录入人',
								width : 80
							},  {
								field : 'CREATE_DATE',
								title : '录入时间',
								width : 150
							}] ],
							done : function(res, curr, count) {
								pageCurr = curr;
							}
						});
						
						$('#barcode,#qty').bind('keypress', function(event) {
							if (event.keyCode == "13") {
								if ($('#qty').val()) {
									if ($('#taskno').val()) {
										
										if ($('#inputCode').val()) {
											var obj = form.val("itemFrom")
											checkBarCode($('#taskno').val(), $('#barcode').val(),obj);
											return false;	
										} else {
											layer.alert("请选择不良信息！");
										}
										
									} else {
										$('#taskno').val('');
										layer.alert("请选择制令单号!");
									}
								} else {
									if(!$('#barcode').val()) {
										layer.alert("请先输入小条码或者数量!");
									}else {
										if ($('#taskno').val()) {
											if ($('#inputCode').val()) {
												var obj = form.val("itemFrom")
												checkBarCode($('#taskno').val(), $('#barcode').val(),obj);
												return false;
											} else {
												layer.alert("请选择不良信息！");
											}
										} else {
											$('#taskno').val('');
											layer.alert("请选择制令单号!");
										}
									}
								}
							}
						});	
						
					});
	

	$('#inputCode').bind('keypress', function(event) {
		if (event.keyCode == "13") {
			getBadInfo($('#inputCode').val(), "0")
		}
	});
});

function getBadInfo(keyword, type) {
	var params = {
		"keyword" : keyword
	}
	CoreUtil
			.sendAjax(
					"/produce/bad_entry/getBadInfo",
					JSON.stringify(params),
					function(data) {
						//console.log(data)
						if (data.result) {
							var def = data.data;
							if (type == "NA") {
								 //console.log(def)
								$("#selectCode").empty();
								for (var i = 0; i < def.length; i++) {
									if (i == 0) {
										$("#selectCode").append("<option value=''>请点击选择</option>");
									}
									$("#selectCode").append(
											"<option value="
													+ def[i].DEFECT_CODE + ">"
													+ def[i].DEFECT_CODE
													+ "——"
													+ def[i].DEFECT_NAME
													+ "</option>");
								}
								layui.form.render('select');
							} else {
								if(def.length==0){
									$("#defName").val("无此不良代码数据");
									return false;
								}else{$("#defName").val(def[0].DEFECT_NAME);}
								
							}
						} else {
							layer.alert(data.msg);
						}
					}, "POST", false, function(res) {
						layer.alert(res.msg);
					});
}
function getDetailByTask(taskNo){
	 var params={"taskNo":taskNo}
		CoreUtil.sendAjax("/produce/bad_entry/getDetailByTask", params, function(data) {
			//console.log(data)
			if (data.result) {
				tableIns.reload({
					data:data.data,
					done : function(res1, curr, count) {
						var allNum = 0;
						res1.data.forEach(function(item, index) {
							allNum += Number(item.DEFECT_NUM);
						});
						// $(".layui-table-total").find('tr').find('td[data-field="bsMoFee"]').find('div').html(feeAll);
						$("#allNum").val(allNum);

					}
				});
			}else{
				layer.alert(data.msg);
			}
		}, "GET", false, function(res) {
			layer.alert(res.msg);
		});
}
function checkBarCode(taskNo, barcode,obj) {
	var params = {
		"taskNo" : taskNo,
		"barcode" : barcode,
	}
	CoreUtil.sendAjax("/produce/bad_entry/checkBarCode", JSON.stringify(params),
			function(data) {
				// console.log(data)
				if (data.result) {
					playSaoMiaoMusic();
					var q = data.data;
					console.log(q);
					console.log(q[0]);
					console.log(q[0].qty);
					if(q[0].qty==0&&!$('#barcode').val()){
						// $("#qty").val(q[0].qty);
					}else {
						$("#qty").val(q[0].qty);
					}
					//console.log(obj)
					saveBad(obj)
				} else {
					playMusic();
					layer.alert(data.msg);
					$('#barcode').val('');
				}
			}, "POST", false, function(res) {
				layer.alert(res.msg);
			});
}
function saveBad(obj) {
	var str=obj.inputCode;
	//console.log(str)
	if(str.length<4){
		layer.alert("请输入正确的不良代码");
		return false;
	}
	var params = {
		"taskNo" : obj.taskno,
		"barcode" : obj.barcode,
		"qty" : $("#qty").val(),
		"defCode" : str,
		"memo" : ""
	}
	CoreUtil.sendAjax("/produce/bad_entry/saveBad", JSON.stringify(params),
			function(data) {
				//console.log(data)
				if (data.result) {
					tableIns.reload({
						data : data.data,
						done : function(res1, curr, count) {
							var allNum = 0;
							res1.data.forEach(function(item, index) {
								allNum += Number(item.DEFECT_NUM);
							});
							// $(".layui-table-total").find('tr').find('td[data-field="bsMoFee"]').find('div').html(feeAll);
							$("#allNum").val(allNum);

						}
					});
					$("#qty").val("");
					$("#barcode").val("");
					$("#barcode").focus();
				} else {
					playMusic();
					layer.alert(data.msg,function () {
						$("#qty").val("");
						$("#barcode").val("");
						$("#barcode").focus();
						layer.closeAll();
					});
				}
			}, "POST", false, function(res) {
				layer.alert(res.msg);
			});
}
function del(obj, id, code) {
	//console.log(id)
	if (id != null) {
		var params = {
			"recordId" : id
		};
		layer.confirm('您确定要删除条码' + code + '吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/produce/bad_entry/deleteBad", JSON
					.stringify(params), function(data) {
				//console.log(data)
				if (data.result == true) {
					// 回调弹框
					tableIns.reload({
						data : data.data,
						done : function(res1, curr, count) {
							var allNum = 0;
							res1.data.forEach(function(item, index) {
								allNum += Number(item.DEFECT_NUM);
							});
							$("#allNum").val(allNum);
						}
					});
					layer.alert("删除成功！");
				} else {
					layer.alert(data.msg);
				}
			}, "POST", false, function(res) {
				layer.alert(res.msg);
			});
		});
	}
}
