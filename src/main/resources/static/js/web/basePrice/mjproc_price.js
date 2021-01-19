/**
 * 人工制费维护管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table', 'tableSelect' ],
					function() {
						var table = layui.table, form = layui.form, tableSelect = layui.tableSelect;

						tableIns = table.render({
							elem : '#colsList',
							url : context + '/basePrice/mjProcPrice/getList',
							method : 'get', // 默认：get请求
							cellMinWidth : 80,
							height:'full-70',//固定表头&full-查询框高度
							page : true,
							request : {
								pageName : 'page', // 页码的参数名称，默认：page
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
								field : 'procName',
								title : '工序',
							}, {
								field : 'fPrice',
								title : '工序费用',
							}, {
								field : 'enabled',
								title : '有效状态',
								templet : '#statusTpl',
								width : 95
							}, {
								field : 'createBy',
								title : '创建人',
								width : 80
							}, {
								field : 'createDate',
								title : '创建时间',
								width : 150
							}, {
								field : 'lastupdateBy',
								title : '更新人',
								width : 80
							}, {
								field : 'lastupdateDate',
								title : '更新时间',
								width : 150
							}, {
								fixed : 'right',
								title : '操作',
								align : 'center',
								toolbar : '#optBar',
								width : 120
							} ] ],
							done : function(res, curr, count) {
								pageCurr = curr;
							}
						});

						tableSelect = tableSelect
								.render({
									elem : '#procName',
									searchKey : 'keyword',
									checkedKey : 'id',
									searchPlaceholder : '试着搜索',
									table : {
										//width : 220,
										url : context+ '/basePrice/mjProcPrice/getProcList',
										method : 'get',
										cols : [ [ {
											type : 'radio'
										},// 多选 radio
										{
											field : 'id',
											title : 'id',
											width : 0,
											hide : true
										}, {
											field : 'PROC_NAME',
											title : '工序',
											width : 100
										},

										{
											field : 'WORKCENTER_NAME',
											title : '工作中心',
											width : 100
										}

										] ],
										page : true,
										request : {
											pageName : 'page' // 页码的参数名称，默认：page
											,
											limitName : 'rows' // 每页数据量的参数名，默认：limit
										},
										parseData : function(res) {
											console.log(res)
											if (!res.result) {
												// 可进行数据操作
												return {
													"count" : 0,
													"msg" : res.msg,
													"data" : [],
													"code" : res.status
												// code值为200表示成功
												}
											}
											return {
												"count" : res.data.Total,
												"msg" : res.msg,
												"data" : res.data.List,
												"code" : res.status
											// code值为200表示成功
											}
										},
									},
									done : function(elem, data) {
										// 选择完后的回调，包含2个返回值
										// elem:返回之前input对象；data:表格返回的选中的数据 []
										var da = data.data;
										console.log(da[0])
										form.val("itemForm", {
											"procName" : da[0].PROC_NAME,
										});
										form.render();// 重新渲染
									}
								});

						// 切换状态操作
						form.on('switch(isStatusTpl)', function(obj) {
							doStatus(obj, this.value, this.name,
									obj.elem.checked);
						});

						// 监听工具条
						table.on('tool(colsTable)', function(obj) {
							var data = obj.data;
							if (obj.event === 'del') {
								// 删除
								delData(data, data.id, data.procName);
							} else if (obj.event === 'edit') {
								// 编辑
								getData(data);
							}
						});
						// 监听提交
						form.on('submit(addSubmit)', function(data) {
							if (data.field.id == null || data.field.id == "") {
								// 新增
								addSubmit(data);
							} else {
								editSubmit(data);
							}
							return false;
						});
						// 监听搜索框
						form.on('submit(searchSubmit)', function(data) {
							// 重新加载table
							load(data);
							return false;
						});
						// 编辑价格维护
						function getData(obj) {

							form.val("itemForm", {
								"id" : obj.id,
								"procName" : obj.procName,
								"fPrice" : obj.fPrice
							});

							openData(obj.id, "编辑价格信息")
						}

						// 设置正常/禁用
						function doStatus(obj, id, name, checked) {
							var isStatus = checked ? 1 : 0;
							var deaprtisStatus = checked ? "正常" : "禁用";
							// 正常/禁用
							layer.confirm('您确定要把工序：' + name + '设置为'+ deaprtisStatus + '状态吗？',
											{
												btn1 : function(index) {
													var params = {
														"id" : id,
														"checkStatus" : isStatus
													};
													CoreUtil
															.sendAjax(
																	"/basePrice/mjProcPrice/doStatus",
																	JSON
																			.stringify(params),
																	function(
																			data) {
																		if (data.result) {
																			layer
																					.alert(
																							"操作成功",
																							function() {
																								layer
																										.closeAll();
																								loadAll();
																							});
																		} else {
																			layer
																					.alert(
																							data.msg,
																							function() {
																								layer
																										.closeAll();
																							});
																		}
																	},
																	"POST",
																	false,
																	function(
																			res) {
																		layer
																				.alert(
																						"操作请求错误，请您稍后再试",
																						function() {
																							layer
																									.closeAll();
																						});
																	});
												},
												btn2 : function() {
													obj.elem.checked = !isStatus;
													form.render();
													layer.closeAll();
												},
												cancel : function() {
													obj.elem.checked = !isStatus;
													form.render();
													layer.closeAll();
												}
											});
						}

					});

});

// 新增编辑弹出框
function openData(id, title) {

	if (id == null || id == "") {
		$("#id").val("");
	}
	var index = layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,

		content : $('#setForm'),
		end : function() {
			cleanData();
		}
	});
	layer.full(index);
}

// 新增价格维护
function add() {
	// 清空弹出框数据
	cleanData();
	// 打开弹出框
	openData(null, "添加价格信息");
}
// 新增价格维护的提交
function addSubmit(obj) {
	CoreUtil.sendAjax("/basePrice/mjProcPrice/add", JSON.stringify(obj.field),
			function(data) {
				if (data.result) {
					layer.alert("操作成功", function() {
						layer.closeAll();
						cleanData();
						// 加载页面
						loadAll();
					});
				} else {
					layer.alert(data.msg, function() {
						layer.closeAll();
					});
				}
			}, "POST", false, function(res) {
				layer.alert(res.msg);
			});
}

// 编辑价格维护的提交
function editSubmit(obj) {
	CoreUtil.sendAjax("/basePrice/mjProcPrice/edit", JSON.stringify(obj.field),
			function(data) {
				if (data.result) {
					layer.alert("操作成功", function() {
						layer.closeAll();
						cleanData();
						// 加载页面
						loadAll();
					});
				} else {
					layer.alert(data.msg, function() {
						layer.closeAll();
					});
				}
			}, "POST", false, function(res) {
				layer.alert(res.msg);
			});
}

// 删除价格维护
function delData(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除该名称为:' + name + ' 的价格信息吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/basePrice/mjProcPrice/delete", JSON
					.stringify(param), function(data) {
				if (isLogin(data)) {
					if (data.result == true) {
						// 回调弹框
						layer.alert("删除成功！", function() {
							layer.closeAll();
							// 加载load方法
							loadAll();
						});
					} else {
						layer.alert(data, function() {
							layer.closeAll();
						});
					}
				}
			});
		});
	}
}

// 重新加载表格（搜索）
function load(obj) {
	// 重新加载table
	tableIns.reload({
		where : {
			keyword : obj.field.keywordSearch
		},
		page : {
			curr : pageCurr
		// 从当前页码开始
		}
	});
}

// 重新加载表格（全部）
function loadAll() {
	// 重新加载table
	tableIns.reload({
		page : {
			curr : pageCurr
		// 从当前页码开始
		}
	});
}

// 清空新增表单数据
function cleanData() {
	$('#itemForm')[0].reset();
	layui.form.render();// 必须写
}
/*自定义处理数字*/
function zhzs(value) {
   value = value.replace(/[^\d]/g, '').replace(/^0{1,}/g, '');
   if (value != '')
      value = parseFloat(value).toFixed(2);
   else
      value = parseFloat(0).toFixed(2);
   return value;
}