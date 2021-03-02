/**
 * 人工制费维护管理
 */
var pageCurr;
var _index = 0;
var fileId = "";
$(function() {
	layui.use([ 'form', 'table', 'tableSelect' ,'upload'],
			function() {
				var table = layui.table, form = layui.form, tableSelect = layui.tableSelect,
					tableSelect1 = layui.tableSelect,upload = layui.upload,upload2 = layui.upload;
						tableIns = table.render({
							elem : '#colsList',
							url : context + '/basePrice/baseFee/getList',
							method : 'get' // 默认：get请求
							,
							cellMinWidth : 80,
							height:'full-70',//固定表头&full-查询框高度
							page : true,
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
							cols : [ [ {type : 'numbers'}, 
							           // {field : 'enabled',title : '有效状态',templet : '#statusTpl',width : 95},
							           {field : 'workcenter',title : '工作中心',width : 140},
							           {field : 'procName',title : '工序',width : 100},
							           {field : 'mhType',title : '机台类型',width : 160},
							           {field : 'feeLh',title : '人工费用（元/小时）',width : 150}, 
							           {field : 'feeMh',title : '制造费用（元/小时）',width : 150},
							           {field : 'createBy',title : '创建人',width : 80}, 
							           {field : 'createDate',title : '创建时间',width : 150}, 
							           {field : 'lastupdateBy',title : '更新人',width : 80}, 
							           {field : 'lastupdateDate',title : '更新时间',width : 150}, 
							           {fixed : 'right',title : '操作',align : 'center',toolbar : '#optBar',width : 120} ] ],
							done : function(res, curr, count) {
								pageCurr = curr;
							}
						});
						// 工序列表
						procTableSelect = tableSelect.render({
							elem : '#procName',
							searchKey : 'keyword',
							checkedKey : 'id',
							searchPlaceholder : '试着搜索',
							table : {
								// width : 220,
								url : context
										+ '/basePrice/baseFee/getProcList',
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
									field : 'PROC_NAME',
									title : '工序'
								},

								{
									field : 'WORKCENTER_NAME',
									title : '工作中心'
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
								form.val("itemForm", {
									"procName" : da[0].PROC_NAME,
									"procId" : da[0].ID,
									"workcenterId" : da[0].WORKCENTER_ID,
									"workcenterName" : da[0].WORKCENTER_NAME
								});
								form.render();// 重新渲染

								// 工作中心列表
								// cTableSelect = tableSelect.render({
								// 	elem : '#workcenterName',
								// 	searchKey : 'keyword',
								// 	checkedKey : 'id',
								// 	searchPlaceholder : '试着搜索',
								// 	table : {
								// 		// width : 220,
								// 		url : context
								// 				+ '/basePrice/baseFee/getWorkCenterList',
								// 		method : 'get',
								//
								// 		cols : [ [ {
								// 			type : 'radio'
								// 		},// 多选 radio
								// 		, {
								// 			field : 'ID',
								// 			title : 'ID',
								// 			width : 0,
								// 			hide : true
								// 		},
								//
								// 		{
								// 			field : 'WORKCENTER_CODE',
								// 			title : '工作中心编码',
								//
								// 		},
								//
								// 		{
								// 			field : 'WORKCENTER_NAME',
								// 			title : '工作中心',
								// 		}
								//
								// 		] ],
								// 		page : true,
								// 		request : {
								// 			pageName : 'page' // 页码的参数名称，默认：page
								// 			,
								// 			limitName : 'rows' // 每页数据量的参数名，默认：limit
								// 		},
								// 		parseData : function(res) {
								// 			if (!res.result) {
								// 				// 可进行数据操作
								// 				return {
								// 					"count" : 0,
								// 					"msg" : res.msg,
								// 					"data" : [],
								// 					"code" : res.status
								// 				// code值为200表示成功
								// 				}
								// 			}
								// 			return {
								// 				"count" : res.data.Total,
								// 				"msg" : res.msg,
								// 				"data" : res.data.List,
								// 				"code" : res.status
								// 			// code值为200表示成功
								// 			}
								// 		},
								// 	},
								// 	done : function(elem, data) {
								// 		// 选择完后的回调，包含2个返回值
								// 		// elem:返回之前input对象；data:表格返回的选中的数据 []
								// 		var da = data.data;
								// 		form.val("itemForm", {
								// 			"workcenterId" : da[0].ID,
								// 			"workcenterName" : da[0].WORKCENTER_NAME,
								// 		});
								// 		form.render();// 重新渲染
								// 	}
								// });
								// 机台类型列表
								typeTableSelect = tableSelect1.render({
									elem : '#mhType',
									searchKey : 'keyword',
									checkedKey : 'id',
									searchPlaceholder : '试着搜索',
									table : {
										url :getModelTypeUrl(),
										method : 'get',
										cols : [ [ {type : 'radio'},
											{field : 'ID', title : 'ID', width : 0, hide : true},
											{field : 'modelCode', title : '机台编码'},
											{field : 'modelName', title : '机台描述'},
											{field : 'workCenterCode', title : '工作中心编码'},
											{field : 'workCenterName', title : '工作中心名称'},
										] ],
										page : true,
										request : {
											pageName : 'page' // 页码的参数名称，默认：page
											,
											limitName : 'rows' // 每页数据量的参数名，默认：limit
										},
										parseData : function(res) {
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
												"count" : res.data.total,
												"msg" : res.msg,
												"data" : res.data.rows,
												"code" : res.status
												// code值为200表示成功
											}
										},
									},
									done : function(elem, data) {
										// 选择完后的回调，包含2个返回值
										// elem:返回之前input对象；data:表格返回的选中的数据 []
										var da = data.data;
										form.val("itemForm", {
											"mhType" : da[0].modelName,
											"workcenterId":da[0].workCenterId,
											"workcenterName":da[0].workCenterName,
										});
										form.render();// 重新渲染
									}
								});
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
						form.on('submit(addSubmit)', function(obj) {					
								var params = {
										"input1" : obj.field.procId,
										"input2" : obj.field.workcenterId,
									};
								CoreUtil.sendAjax("/basePrice/baseFee/doCheckInfo",JSON.stringify(params),
								function(data) {
											if (data.result) {										
												if (obj.field.id == null || obj.field.id == "") {
													// 新增
													addSubmit(obj);
												} else {
													editSubmit(obj);
												}								
											} else {									
												layer.alert(data.msg);						
											}
										},"POST",false,function(res) {		
											layer.alert("操作请求错误，请您稍后再试");
											return false;
										 });
										
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
								"workcenterId" : obj.workcenterId,
								"workcenterName" : obj.workcenter,
								"procName" : obj.procName,
								"procId" : obj.procId,
								"mhType" : obj.mhType,
								"feeLh" : obj.feeLh,
								"feeMh" : obj.feeMh,
								"fileId" : obj.fileId
							});

							//获取附件文件
							CoreUtil.sendAjax("/basePrice/baseFee/getFileList?customId="+obj.id,"",
								function(data) {
									console.log(data);
									for(var i =0;i<data.data.length; i++){
										document.getElementById("filelist").innerHTML = $("#filelist").html()+getExcFieldBefore(data.data[i],data.data[i].qsFileId,"/basePrice/baseFee/delFile");
									}
								}, "GET", false, function(res) {
									layer.alert(res.msg);
								});

							openData(obj.id, "编辑价格信息")
						}

				//上传控件
				upload2.render({
					elem: '#upload2'
					,url: context+'/file/upload'
					,accept: 'file' //普通文件
					,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致。
						layer.load(); //上传loading
					}
					,done: function(res,index, upload){
						layer.closeAll('loading'); //关闭loading
						if(res.result == true){
							document.getElementById("filelist").innerHTML = $("#filelist").html()+getExcField(_index,res.data);
							_index++;
							fileId +=res.data.id +",";

						}
						$('#fileId').val(fileId);
					}
					,error: function(index, upload){
						layer.closeAll('loading'); //关闭loading
					}
				});

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
											CoreUtil.sendAjax("/basePrice/baseFee/doStatus",JSON.stringify(params),
												function(data) {
													if (data.result) {
														layer.alert("操作成功",function() {
																	layer.closeAll();
																	loadAll();
															});
													} else {
														layer.alert(data.msg,function() {
																layer.closeAll();
															});
													}
												},"POST",false,function(res) {
													layer.alert("操作请求错误，请您稍后再试",function() {
																layer.closeAll();
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

				// 导入
				upload.render({
					elem : '#upload',
					url : context + '/basePrice/baseFee/doExcel',
					accept : 'file' // 普通文件
					,

					before : function(obj) { // obj参数包含的信息，跟 choose回调完全一致，可参见上文。
						layer.load(); // 上传loading
					},
					done : function(res, index, upload) {
						layer.closeAll('loading'); // 关闭loading
						layer.alert(res.msg, function(index) {
							layer.close(index);
							loadAll();
						});

					},
					error : function(index, upload) {
						layer.alert("操作请求错误，请您稍后再试", function() {
						});
						layer.closeAll('loading'); // 关闭loading
						layer.close(index);
					}
				});
	});
});

// 导出数据
function exportExcel() {
	location.href = "../../excelFile/人工制费维护模板.xlsx";//从文件夹内直接提取
}

function  getModelTypeUrl() {
	console.log($('#workcenterId').val());
	return context + '/basePrice/modelType/getList?workCenterId='+$('#workcenterId').val();
}

// 新增编辑弹出框
function openData(id, title) {
	// console.log(Fee);
	if (id == null || id == "") {
		$("#id").val("");
		CoreUtil.sendAjax("/basePrice/baseFee/getFeeParam", "",
			function(data) {
				if (data.result) {
					$('#feeLh').val(data.data[0].paramValue);
				}
			}, "POST", false, function(res) {
				layer.alert(res.msg);
			});

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
	CoreUtil.sendAjax("/basePrice/baseFee/add", JSON.stringify(obj.field),
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
	CoreUtil.sendAjax("/basePrice/baseFee/edit", JSON.stringify(obj.field),
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
			CoreUtil.sendAjax("/basePrice/baseFee/delete", JSON
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
	_index = 0;
	document.getElementById("filelist").innerHTML = "";
	layui.form.render();// 必须写
}




