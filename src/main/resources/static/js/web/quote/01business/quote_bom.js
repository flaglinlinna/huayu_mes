/**
 * 外购件清单维护
 */
var pageCurr;
$(function() {
	layui
			.use(
					[ 'form', 'table', 'upload', 'tableSelect' ],
					function() {
						var table = layui.table, table1 = layui.table, form = layui.form, upload = layui.upload, tableSelect = layui.tableSelect, tableSelect1 = layui.tableSelect
							, tableSelect2 = layui.tableSelect, tableSelect3 = layui.tableSelect;
						isComplete()
						tableSelect = tableSelect.render({
							elem : '#BjWorkCenter',
							searchKey : 'keyword',
							checkedKey : 'id',
							searchPlaceholder : '内部编码搜索',
							table : {
								url : context + '/basePrice/workCenter/getList',
								method : 'get',
								parseData : function(res) {// 可进行数据操作
									return {
										"count" : res.data.total,
										"msg" : res.msg,
										"data" : res.data.rows,
										"code" : res.status
									// code值为200表示成功
									}
								},
								cols : [ [ {type : 'radio'},// 单选 radio
								{field : 'id',title : 'id',width : 0,hide : true}, 
								{type : 'numbers'}, 
								{field : 'workcenterCode',title : '工作中心编号',}, 
								{field : 'workcenterName',title : '工作中心名称',},
								{field : 'fmemo',title : '备注'} ] ],
								page : true,
								request : {
									pageName : 'page', // 页码的参数名称，默认：page
									limitName : 'rows' // 每页数据量的参数名，默认：limit
								},

							},
							done : function(elem, data) {
								var da = data.data;
								// 选择完后的回调，包含2个返回值
								// elem:返回之前input对象；data:表格返回的选中的数据 []
								form.val("quoteBomForm", {
									"pkBjWorkCenter" : da[0].id,
									"BjWorkCenter" : da[0].workcenterName,
								});
								form.render();// 重新渲染
							}
						});

						tableSelect1 = tableSelect1.render({
							elem : '#ItemTypeWg',
							searchKey : 'keyword',
							checkedKey : 'ID',
							searchPlaceholder : '关键字搜索',
							table : {
								url : context + '/basePrice/itemTypeWg/getList',
								method : 'get',
								parseData : function(res) {// 可进行数据操作
									return {
										"count" : res.data.total,
										"msg" : res.msg,
										"data" : res.data.rows,
										"code" : res.status
									// code值为200表示成功
									}
								},
								cols : [ [ {type : 'radio'},// 单选 radio
								{type : 'numbers'},
								{field : 'id',title : 'id',width : 0,hide : true},
								{field : 'itemType',title : '物料类型',width : 150},
								{field : 'fmemo',title : '备注',width : 150} ] ],
								page : true,
								request : {
									pageName : 'page', // 页码的参数名称，默认：page
									limitName : 'rows' // 每页数据量的参数名，默认：limit
								},
							},
							done : function(elem, data) {// elem:返回之前input对象；data:表格返回的选中的数据
															// []
								var da = data.data;
								form.val("quoteBomForm", {
									"pkItemTypeWg" : da[0].id,
									"ItemTypeWg" : da[0].itemType
								});
								form.render();// 重新渲染
							}
						});

						tableSelect2 = tableSelect2.render({
							elem : '#Unit',
							searchKey : 'keyword',
							checkedKey : 'id',
							searchPlaceholder : '关键字搜索',
							table : {
								url : context + '/basePrice/unit/getList',
								method : 'get',
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
								cols : [ [ {type : 'radio'},// 单选 radio
								{field : 'id',title : 'id',width : 0,hide : true}, 
								{type : 'numbers'},
								{field : 'unitCode',title : '单位编码'}, 
								{field : 'unitName',title : '单位名称',} ] ],
								page : true,
								request : {
									pageName : 'page', // 页码的参数名称，默认：page
									limitName : 'rows' // 每页数据量的参数名，默认：limit
								},
							},
							done : function(elem, data) {
								var da = data.data;
								// 选择完后的回调，包含2个返回值
								// elem:返回之前input对象；data:表格返回的选中的数据 []
								form.val("quoteBomForm", {
									"pkUnit" : da[0].id,
									"Unit" : da[0].unitName
								});
								form.render();// 重新渲染
							}
						});

						tableSelect3 = tableSelect3.render({
							elem : '#purchaseUnit',
							searchKey : 'keyword',
							checkedKey : 'id',
							searchPlaceholder : '关键字搜索',
							table : {
								url : context + '/basePrice/unit/getList',
								method : 'get',
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
								cols : [ [ {type : 'radio'},// 单选 radio
									{field : 'id',title : 'id',width : 0,hide : true},
									{type : 'numbers'},
									{field : 'unitCode',title : '单位编码'},
									{field : 'unitName',title : '单位名称',} ] ],
								page : true,
								request : {
									pageName : 'page', // 页码的参数名称，默认：page
									limitName : 'rows' // 每页数据量的参数名，默认：limit
								},
							},
							done : function(elem, data) {
								var da = data.data;
								// 选择完后的回调，包含2个返回值
								// elem:返回之前input对象；data:表格返回的选中的数据 []
								form.val("quoteBomForm", {
									"purchaseUnit" : da[0].unitCode
								});
								form.render();// 重新渲染
							}
						});

						tableIns = table.render({
							elem : '#quoteBomList',
							url : context + '/quoteBom/getQuoteBomList?pkQuote=' + quoteId,
							method : 'get' // 默认：get请求
							,
							cellMinWidth : 80,
							// toolbar: '#toolbar',
							height : 'full-65',// 固定表头&full-查询框高度
							// even : true,// 条纹样式
							page : true,
							limit:50,
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
							cols : [ [
							{fixed : 'left',type : 'numbers'},
							{field:'id', title:'ID', width:80,hide:true},
							{fixed : 'left',field : 'bsElement',title : '组件名称',sort : true,width : 120,edit: "text"},
							{fixed : 'left',field : 'bsComponent',title : '零件名称',sort : true,width : 150,edit: "text"},
							{fixed : 'left',field : 'wc',title : '工作中心',sort : true,width : 120,
								templet : function(d) {
									if (d.wc != null) {return d.wc.workcenterName;} 
									else {return "";}
								},style : 'background-color:#d2d2d2'
							},
							// {field : 'bsItemCode',title : '材料编码',sort:true,width:120},
							{field : 'itp',title : '物料类型',sort : true,width : 100,
								templet : function(d) {
									if (d.itp != null) {return d.itp.itemType;} 
									else {return "";}
							},style : 'background-color:#d2d2d2'},
							{field : 'bsGroups',title : '损耗分组',width : 120,"edit" : "text"},
							{field : 'bsMaterName',title : '材料名称',sort : true,width : 200,edit: "text"},
							{field : 'bsModel',title : '材料规格',width : 200,edit: "text"},

							{field : 'bsQty',title : 'BOM用量',width : 90,edit: "text"},
							// {field : 'bsProQty',title : '制品重(g)',width : 90},
							{field : 'unit',title : 'BOM用量单位',width : 110,
								templet : function(d) {
									if (d.unit != null) {return d.unit.unitCode;}
									else {return "";}
							},style : 'background-color:#d2d2d2'},
							// {field : 'bsWaterGap',title : '水口重(g)',width : 90},
							// {field : 'bsCave',title : '穴数',width : 80},
							{field : 'purchaseUnit',title : '采购单位',width : 80,style : 'background-color:#d2d2d2'},
							{field : 'bsAgent',title : '客户代采',width : 80,templet:function (d) {
									if(d.bsAgent=="1"){
										return "是"
									}else {
										return "否"
									}
								},style : 'background-color:#d2d2d2'},
							{field : 'productRetrial',title : '制造评估重审',templet : '#statusTpl',width : 110},
							// {field : 'purchaseRetrial',title : '采购重申',templet : '#statusTpl1',width : 120},
							// {field : 'outRetrial',title : '外协重审',templet : '#statusTpl2',width : 120},
							{field : 'fmemo',title : '工艺说明',width : 200,edit: "text"},
							{fixed : 'right',title : '操作',align : 'center',toolbar : '#optBar',width : 120}
							] ],
							done : function(res, curr, count) {
								// 如果是异步请求数据方式，res即为你接口返回的信息。
								// 如果是直接赋值的方式，res即为：{data: [], count: 99}
								// data为当前页数据、count为数据总长度
								// console.log(res);
								// 得到当前页码
								// console.log(curr);
								// 得到数据总量
								// console.log(count);
								// form.render('select');
								// var tableView = this.elem.next();
								// tableView.find('tr[data-index=' + index + ']').find('td').data('edit',false).css("background-color", "#d2d2d2")
								pageCurr = curr;
								var tableIns = this.elem.next(); // 当前表格渲染之后的视图
								layui.each(res.data, function(i, item){
								if(item.bsStatus=='1') {
									tableIns.find('tr[data-index=' + i + ']').find('td').data('edit', false).css("background-color", "#d2d2d2")
								}
								});
							}
						});


						//监听单元格编辑
						table.on('edit(quoteBomTable)', function(obj){
							var value = obj.value //得到修改后的值
								,data = obj.data //得到所在行所有键值
								,field = obj.field; //得到字段
							var tr = obj.tr;
						// 	// 单元格编辑之前的值
						// 	// var oldtext = $(tr).find("td[data-field='"+obj.field+"'] div").text();
							if(field == 'bsQty'){
								//console.log(data.id,value)
								// dobsGroups(data.id,value)
								var bsQty = data.bsQty;
								if (/^\d+$/.test(bsQty) == false && /^\d+\.\d+$/.test(bsQty) == false) {
									layer.msg("Bom用量只能输入数字");
									return false;
								}
							}
						});

						//监听机台类型下拉选择 并修改
						form.on('select(selectRetrial)', function (data) {
							//获取当前行tr对象
							var elem = data.othis.parents('tr');
							//第一列的值是Guid，取guid来判断
							var id= elem.first().find('td').eq(1).text();
							var product= elem.first().find('td').eq(13).text();
							var purchase= elem.first().find('td').eq(14).text();
							var out= elem.first().find('td').eq(15).text();

							console.log(elem.first().find('td'));
							// console.log(elem.first().find('td').eq(13).value());

							//选择的select对象值；
							var selectValue = data.value;
							// console.log(Guid,selectValue);
							// updateRetrial(id,product,purchase,out);
						})

						form.on('switch()', function(obj) {
							// console.log(this.name);
							var elem = obj.othis.parents('tr').attr('data-index');
							console.log(elem);
							var checked = obj.elem.checked ? "1" : "0";
							layui.table.cache['quoteBomList'][elem].productRetrial = checked;
							// updateRetrial(this.value, this.name, checked);
						});

						tableIns1 = table1.render({
							elem : '#uploadList',
							// url : context +
							// '/quoteBom/getQuoteBomList?pkQuote='+ quoteId,
							method : 'get', // 默认：get请求
							cellMinWidth : 80,
							height : 'full-110',// 固定表头&full-查询框高度
							even : true,// 条纹样式
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
							cols : [ [
							    {type : 'numbers'},
								{type:'checkbox'},
							    {field : 'checkStatus',width : 100,title : '状态',sort : true,style : 'background-color:#d2d2d2',templet : '#checkStatus'},
								{field : 'errorInfo',width : 150,title : '错误信息',sort : true,style : 'background-color:#d2d2d2'},
								{field : 'bsElement',title : '组件名称',sort : true,width : 120}, 
								{field : 'bsComponent',title : '零件名称',sort : true,width : 200},
								{field : 'wc',title : '材料耗用工作中心',sort : true,width : 145,
									templet : function(d) {
									if (d.wc != null) {return d.wc.workcenterName;}
									else {return "";}
								}},
							 // {field : 'bsItemCode',title :'材料编码',sort:true,width:120},
							    {field : 'itp',title : '物料类型',sort : true,width : 120,
									templet : function(d) {
									if (d.itp != null) {return d.itp.itemType;} 
									else {return "";}
								}},
								{field : 'bsMaterName',title : '材料名称',sort : true,width : 150},
								{field : 'bsModel',title : '材料规格',width : 200},
								{field : 'bsExplain',title : '采购说明',width : 200},
								{field : 'bsAgent',title : '是否客户代采',width : 120,templet:function (d) {
										if(d.bsAgent=="1"){
											return "是"
										}else {
											return "否"
										}
									}},
								{field : 'fmemo',title : '工艺说明',width : 200},
								{field : 'bsQty',title : 'BOM用量',width : 90},
								// {field : 'bsProQty',title : '制品量',width : 90},
								{field : 'unit',title : 'BOM用量单位',width : 100,templet : function(d) {
									if (d.unit != null) {return d.unit.unitCode;}
									else {return "";}
								}},
								{field : 'purchaseUnit',title : '采购单位',width : 80},
								// {field : 'bsWaterGap',title : '水口重(g)',width : 80},
								// {field : 'bsCave',title : '穴数',width : 80},
							// , {fixed : 'right',title : '操作',align :'center',toolbar : '#optBar',width:120}
							] ],
							done : function(res, curr, count) {
								// 如果是异步请求数据方式，res即为你接口返回的信息。
								// 如果是直接赋值的方式，res即为：{data: [], count: 99}
								// data为当前页数据、count为数据总长度
								// console.log(res);
								// 得到当前页码
								// console.log(curr);
								// 得到数据总量
								// console.log(count);
								pageCurr = curr;
							}
						});

						// 自定义验证规则
						form.verify({
							double : function(value) {
								if (/^\d+$/.test(value) == false && /^\d+\.\d+$/.test(value) == false && value != "" && value != null) {
									return '只能输入数字类型';
								}
							}
						});


						// 监听工具条
						table.on('tool(quoteBomTable)', function(obj) {
							var data = obj.data;
							if (obj.event === 'del') {
								// 删除
								delProdErr(data, data.id, data.bsElement);
							} else if (obj.event === 'edit') {
								// 编辑
								getProdErr(data, data.id);
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
						// 编辑五金材料
						function getProdErr(obj, id) {
							var workcenterName = "";
							var itemType = "";
							var unitName = "";
							if (obj.wc != null) {
								workcenterName = obj.wc.workcenterName;
							}
							if (obj.itp != null) {
								itemType = obj.itp.itemType;
							}
							if (obj.unit != null) {
								unitName = obj.unit.unitName;
							}
							form.val("quoteBomForm", {
								"id" : obj.id,
								"bsComponent" : obj.bsComponent,
								"bsElement" : obj.bsElement,
								"pkBjWorkCenter" : obj.pkBjWorkCenter,
								"BjWorkCenter" : workcenterName,
								"pkItemTypeWg" : obj.pkItemTypeWg,
								"ItemTypeWg" : itemType,
								"bsMaterName" : obj.bsMaterName,
								"bsModel" : obj.bsModel,
								"bsExplain" : obj.bsExplain,
								"bsGroups":obj.bsGroups,
								"fmemo" : obj.fmemo,
								"bsProQty" : obj.bsProQty,
								"pkUnit" : obj.pkUnit,
								"Unit" : unitName,
								"purchaseUnit": obj.purchaseUnit,
								// "bsRadix" : obj.bsRadix,
								// "bsCave":obj.bsCave,
								// "bsWaterGap":obj.bsWaterGap,
								"bsAgent":obj.bsAgent,
								"bsQty":obj.bsQty,
							});
							openProdErr(id, "编辑外购件清单信息")
						}
						;

						// 导入
						upload.render({
							elem : '#upload',
							url : context + '/quoteBomTemp/importExcel',
							accept : 'file' // 普通文件
							,
							data : {
								pkQuote : function() {
									return quoteId;
								}
							},
							before : function(obj) { // obj参数包含的信息，跟
								// choose回调完全一致，可参见上文。
								layer.load(); // 上传loading
							},
							done : function(res, index, upload) {
								layer.closeAll('loading'); // 关闭loading
								layer.alert(res.msg, function(index) {
									layer.close(index);
									loadAll2();
								});

							},
							error : function(index, upload) {
								layer.alert("操作请求错误，请您稍后再试", function(index) {
								});
								layer.closeAll('loading'); // 关闭loading
								layer.close(index);
							}
						});
					});

});

function isComplete() {
	if (iStatus == 2) {
		$("#addbtn").addClass("layui-btn-disabled").attr("disabled", true)
		// $("#exportbtn").addClass("layui-btn-disabled").attr("disabled", true)
		$("#loadbtn").addClass("layui-btn-disabled").attr("disabled", true)
		$("#savebtn").addClass("layui-btn-disabled").attr("disabled", true)
		$("#editListBtn").addClass("layui-btn-disabled").attr("disabled", true)

	}
}

// 模板下载
function downloadExcel() {
	location.href = "../../excelFile/外购件清单模板.xlsx";
}

// 导出数据
function exportExcel() {
	location.href = context + "/quoteBom/exportExcel?pkQuote=" + quoteId;
}

function  dobsGroups(id,value){
	var params = {
		"id" : id,
		"bsGroups":value,
		// "outRetrial":outRetrial,
	};
	CoreUtil.sendAjax("/quoteBom/updateBsGroups", JSON.stringify(params), function(data) {
		if (data.result) {
			// layer.alert("修改成功",function () {
			// 	layer.closeAll();
				loadAll();
			// });
		} else {
			layer.alert(data.msg);
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}

function saveTable() {
	var dates = layui.table.cache['quoteBomList'];
	// console.log(dates);
	CoreUtil.sendAjax("/quoteBom/saveTable", JSON.stringify(dates),
		function(data) {
			if (isLogin(data)) {
				if (data.result == true) {
					// loadAll();
				} else {
					layer.alert(data.msg, function() {
						layer.closeAll();
						// loadAll();
					});
				}
			}
		});
}

function updateRetrial(id,type,value){
	var params = {
		"id" : id,
		"type":type,
		"value":value,
		// "outRetrial":outRetrial,
	};
	CoreUtil.sendAjax("/quoteBom/updateRetrial", JSON.stringify(params), function(data) {
		if (data.result) {
				layer.alert("修改成功",function () {
					layer.closeAll();
					loadAll();
				});
		} else {
			layer.alert(data.msg);
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}

// 打开导入页
function openUpload() {
	tableIns1.reload({
		url : context + '/quoteBomTemp/getList?quoteId=' + quoteId,
		done : function(res1, curr, count) {
			pageCurr = curr;
		}
	})
	// 打开弹出框
	var index = layer.open({
		type : 1,
		title : "导入外购件清单信息",
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#uploadDiv')
	});
	layer.full(index);
}

// 确定导入
function uploadChecked() {
	var params = {
		"pkQuote" : quoteId,
	};
	CoreUtil.sendAjax("/quoteBomTemp/uploadChecked", JSON.stringify(params), function(data) {
		if (data.result) {
			layer.alert(data.msg, function() {
				layer.closeAll();
				cleanProdErr();
				// 加载页面
				loadAll();
			});
		} else {
			layer.alert(data.msg);
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}

// 新增编辑弹出框
function openProdErr(id, title) {
	if (id == null || id == "") {
		$("#id").val("");
	}
	var index = layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#setQuoteBom'),
		end : function() {
			cleanProdErr();
		}
	});
	layer.full(index);
}

// 添加五金材料
function addQuoteBom() {
	// 清空弹出框数据
	cleanProdErr();
	// 打开弹出框
	openProdErr(null, "添加外购件清单信息");
}
// 新增五金材料提交
function addSubmit(obj) {
	obj.field.pkQuote = quoteId;
	CoreUtil.sendAjax("/quoteBom/add", JSON.stringify(obj.field), function(data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanProdErr();
				// 加载页面
				loadAll();
			});
		} else {
			layer.alert(data.msg);
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}

// 编辑五金材料提交
function editSubmit(obj) {
	CoreUtil.sendAjax("/quoteBom/edit", JSON.stringify(obj.field), function(data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanProdErr();
				// 加载页面
				loadAll();
			});
		} else {
			layer.alert(data.msg);
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}

// 删除选中的临时表
function deleteTemp() {
	var checkStatus = layui.table.checkStatus('uploadList').data;
	var ids ="";
	for(var i = 0;i<checkStatus.length;i++){
		ids+=checkStatus[i].id+","
	}
	console.log(checkStatus);
	var params={
		"ids":ids
	}
	CoreUtil.sendAjax("/quoteBomTemp/deleteTemp", JSON.stringify(params), function(data) {
		if (data.result) {
			layer.alert("删除成功", function(index) {
				// layer.closeAll();
				// cleanProdErr();
				// 加载页面
				layer.close(index);
				loadAll2();
			});
		} else {
			layer.alert(data.msg);
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}


// 删除五金材料
function delProdErr(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除外购件：' + name + '的信息吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/quoteBom/deleteQuoteBom", JSON.stringify(param), function(data) {
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

function save() {
	saveTable();
	// console.log(quoteId,code)
	var param = {
		"quoteId" : quoteId,
		"code" : code
	};
	layer.confirm('一经提交则不得再修改，确定要提交吗？', {
		btn : [ '确认', '返回' ]
	}, function() {
		// saveTable();
		CoreUtil.sendAjax("/quoteBom/doStatus", JSON.stringify(param), function(data) {
			if (isLogin(data)) {
				if (data.result == true) {
					// parent.layui.admin.events.closeThisTabs();
					layer.alert("确认完成成功", function() {
						layer.closeAll();
						if(data.data =="1"){
							//项目完成，关闭上一级项目标签页
							// var thisUrl= context +"/productMater/toProductMater?bsType="+bsType+"&quoteId="+quoteId+"&bsCode="+bsCode;
							var srcUrl = context + '/quote/toQuoteItem?quoteId='+quoteId+"&style=item";
							console.log(srcUrl);
							($(window.parent.document).find(('li[lay-id="'+srcUrl+'"]'))).find(".layui-tab-close").trigger("click")
						}
						var thisUrl= context +"/quote/toQuoteBom?quoteId="+quoteId+"&code="+code;
						($(window.parent.document).find(('li[lay-id="'+thisUrl+'"]'))).find(".layui-tab-close").trigger("click")
					});
				} else {
					layer.msg(data.msg, {
						time : 2000, // 2s后自动关闭
						btn : [ '知道了' ]
					});
				}
			}
		});
	});
}

function cancelStatus() {
	var param = {
		"quoteId" : quoteId,
		"code" : code
	};
	layer.confirm('取消完成会清除工艺流程,确认取消确认完成吗？', {
		btn : [ '确认', '返回' ]
	}, function() {
		CoreUtil.sendAjax("/quoteBom/cancelStatus", JSON.stringify(param), function(data) {
			if (isLogin(data)) {
				if (data.result == true) {
					layer.alert(data.msg, function() {
						layer.closeAll();
						window.location.reload();
					});
				} else {
					layer.msg(data.msg, {
						time : 2000, // 2s后自动关闭
						btn : [ '知道了' ]
					});
				}
			}
		});
	});
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

function loadAll2() {
	// 重新加载table
	tableIns1.reload({
		page : {
			curr : pageCurr
		// 从当前页码开始
		}
	});
}

// 清空新增表单数据
function cleanProdErr() {
	$('#quoteBomForm')[0].reset();
	layui.form.render();// 必须写
}
