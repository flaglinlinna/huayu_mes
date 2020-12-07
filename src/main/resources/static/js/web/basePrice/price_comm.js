/**
 * 物料通用价格维护管理
 * 方法调用未测试-2020-12-07
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table' ], function() {
		var table = layui.table, form = layui.form;

		tableIns = table.render({
			elem : '#colsList',
			url : context + '/basePrice/priceComm/getList',
			method : 'get' // 默认：get请求
			,
			cellMinWidth : 80,
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
			cols : [ [ {
				type : 'numbers'
			},{
				field : 'productType',
				title : '工序名称',
			},{
				field : 'enabled',
				title : '有效状态',
				templet : '#statusTpl',
				width:95
			},

			{
				field : 'createBy',
				title : '创建人',
				width:80
			}, {
				field : 'createDate',
				title : '创建时间',
			}, {
				field : 'lastupdateBy',
				title : '更新人',
				width:80
			}, {
				field : 'lastupdateDate',
				title : '更新时间',
			}, {
				fixed : 'right',
				title : '操作',
				align : 'center',
				toolbar : '#optBar',
				width:120
			} ] ],
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
		// 编辑工序维护
		function getData(obj) {	
			form.val("setForm", {
						"id" : obj.id,
						"productType" : obj.productType,
					});
					openData(obj.id, "编辑工序信息")
		}

		// 设置正常/禁用
		function doStatus(obj, id, name, checked) {
			var isStatus = checked ? 1 : 0;
			var deaprtisStatus = checked ? "正常" : "禁用";
			// 正常/禁用
			layer.confirm(
					'您确定要把工序：' + name + '设置为' + deaprtisStatus + '状态吗？', {
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
		area : [ '550px' ],
		content : $('#setForm'),
		end : function() {
			cleanData();
		}
	});
	layer.full(index);
}

// 新增工序维护
function addData() {
	// 清空弹出框数据
	cleanData();
	// 打开弹出框
	openData(null, "添加工序信息");
}
// 新增工序维护的提交
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

// 编辑工序维护的提交
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

// 删除工序维护
function delData(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除该名称为:' + name + ' 的工序信息吗？', {
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
	$('#setForm')[0].reset();
	layui.form.render();// 必须写
}
