/**
 * 物料通用价格维护管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table', 'tableSelect' ], function() {
		var table = layui.table, form = layui.form, tableSelect = layui.tableSelect;

		tableIns = table.render({
			elem : '#colsList',
			url : context + '/basePrice/priceComm/getList',
			method : 'get' // 默认：get请求
			,
			cellMinWidth : 80,
			//toolbar: '#toolbar',
			height:'full-70'//固定表头&full-查询框高度
			,even:true,//条纹样式
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
			cols : [ [
				{type : 'numbers'},
				{field : 'enabled', title : '有效状态', templet : '#statusTpl', width : 95},
				{field : 'itemNo', title : '物料编号', width : 150},
				{field : 'itemName', title : '物料名称', width : 150},
				{field : 'rangePrice', title : '价格档位', width : 100},
				{field : 'priceUn', title : '单价', width : 80},
				{field : 'unit', title : '单位', width : 80},
				{field : 'alternativeSuppliers', title : '备选供应商', width : 150},
				{field : 'createBy', title : '创建人', width : 80},
				{field : 'createDate', title : '创建时间', width : 150},
				{field : 'lastupdateBy', title : '更新人', width : 80},
				{field : 'lastupdateDate', title : '更新时间', width : 150},
				{fixed : 'right', title : '操作', align : 'center', toolbar : '#optBar', width : 120}
				] ],
			done : function(res, curr, count) {
				// 如果是异步请求数据方式，res即为你接口返回的信息。
				// 如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
				// console.log(res);
				// 得到当前页码
				// console.log(curr);
				// 得到数据总量
				// console.log(count);
				pageCurr = curr;
			}
		});

		tableSelect = tableSelect.render({
			elem : '#itemNo',
			searchKey : 'keyword',
			checkedKey : 'id',
			searchPlaceholder : '试着搜索',
			table : {
				url : context + '/basePrice/priceComm/getItemList',
				method : 'get',
				// width:800,
				cols : [ [
					{type : 'numbers', title : '序号'},
					{type : 'radio'},
					{field : 'ID', title : 'id', width : 0, hide : true},
					{field : 'ITEM_NO', title : '物料编码', minWidth : 160},
					{field : 'ITEM_NAME', title : '物料名称', minWidth : 160},
					// {field : 'WORKCENTER_NAME', title : '工作中心', width : 160},
				] ],
				page : true,
				request : {
					pageName : 'page' // 页码的参数名称，默认：page
					,
					limitName : 'rows' // 每页数据量的参数名，默认：limit
				},
				parseData : function(res) {
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
				form.val("itemForm", {
					"itemNo" : da[0].ITEM_NO,
					"itemName":da[0].ITEM_NAME
				});
				form.render();// 重新渲染

			}
		});

		// 切换状态操作
		form.on('switch(isStatusTpl)', function(obj) {
			doStatus(obj, this.value, this.name, obj.elem.checked);
		});

		// 监听工具条
		table.on('tool(colsTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delData(data, data.id, data.itemName);
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
				"itemName" : obj.itemName,
				"itemNo":obj.itemNo,
				"rangePrice" : obj.rangePrice,
				"priceUn" : obj.priceUn,
				"alternativeSuppliers" : obj.alternativeSuppliers,
			});
			getUnitList(obj.unitId);
			
			openData(obj.id, "编辑价格信息")
		}

		// 设置正常/禁用
		function doStatus(obj, id, name, checked) {
			var isStatus = checked ? 1 : 0;
			var deaprtisStatus = checked ? "正常" : "禁用";
			// 正常/禁用
			layer.confirm('您确定要把物料：' + name + '设置为' + deaprtisStatus + '状态吗？',
					{
						btn1 : function(index) {
							var params = {
								"id" : id,
								"checkStatus" : isStatus
							};
							CoreUtil.sendAjax("/basePrice/priceComm/doStatus",
									JSON.stringify(params), function(data) {
										if (data.result) {
											layer.alert("操作成功", function() {
												layer.closeAll();
												loadAll();
											});
										} else {
											layer.alert(data.msg, function() {
												layer.closeAll();
											});
										}
									}, "POST", false, function(res) {
										layer.alert("操作请求错误，请您稍后再试",
												function() {
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
	getUnitList("");
	openData(null, "添加价格信息");
}
// 新增价格维护的提交
function addSubmit(obj) {
	CoreUtil.sendAjax("/basePrice/priceComm/add", JSON.stringify(obj.field),
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

function getUnitList(id) {
	CoreUtil.sendAjax("/basePrice/priceComm/getUnitList", "", function(data) {
		if (data.result) {
			$("#unitId").empty();
			var list = data.data;
			for (var i = 0; i < list.length; i++) {
				if (i == 0) {
					$("#unitId").append("<option value=''>请选择</option>")
				}
				$("#unitId").append(
						"<option value=" + list[i].id + ">" + list[i].unitCode
								+ "——" + list[i].unitName + "</option>")
				if(id==list[i].id){
					$("#unitId").val(list[i].id);
				}
			}
			
			layui.form.render('select');
		} else {
			layer.alert(res.msg);
		}
	}, "GET", false, function(res) {
		layer.alert(res.msg);
	});
}

// 编辑价格维护的提交
function editSubmit(obj) {
	CoreUtil.sendAjax("/basePrice/priceComm/edit", JSON.stringify(obj.field),
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
			CoreUtil.sendAjax("/basePrice/priceComm/delete", JSON
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
