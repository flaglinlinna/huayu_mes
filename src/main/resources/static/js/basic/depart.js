/**
 * 角色管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table' ], function() {
		var table = layui.table, form = layui.form;

		tableIns = table.render({
			elem : '#departList',
			url : context + 'base/depart/getList',
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
			}
			// ,{field:'id', title:'ID', width:80, unresize:true, sort:true}
			, {
				field : 'bsCode',
				title : '编号',
				width : 120
			}, {
				field : 'bsName',
				title : '名称',
				width : 120
			}, {
				field : 'bsManager',
				title : '部门经理',
				Width : 120
			}, {
				field : 'bsManagerTel',
				title : '部门经理电话',
				Width : 120
			}, {
				field : 'bsStatus',
				title : '状态',
				width : 95,
				templet : '#statusTpl'
			}, {
				field : 'modifiedTime',
				title : '更新时间',
				width : 150
			}, {
				field : 'createdTime',
				title : '添加时间',
				width : 150
			}, {
				fixed : 'right',
				title : '操作',
				width : 150,
				align : 'center',
				toolbar : '#optBar'
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

		// 监听在职操作
		form.on('switch(isStatusTpl)', function(obj) {
			console.log("switch");
			setStatus(obj, this.value, this.name, obj.elem.checked);
		});
		// 监听工具条
		table.on('tool(departmentTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delDepartment(data, data.id, data.bsCode);
			} else if (obj.event === 'edit') {
				// 编辑
				getDepart(data, data.id);
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
			console.log(data)
			// 重新加载table
			load(data);
			return false;
		});

		// 编辑部门
		function getDepart(obj, id) {
			var param = {
				"id" : id
			};
			CoreUtil.sendAjax("base/depart/getDepart", JSON.stringify(param),
					function(data) {
						if (data.result) {
							form.val("departForm", {
								"id" : data.data.id,
								"bsCode" : data.data.bsCode,
								"bsName" : data.data.bsName,
								"bsManager" : data.data.bsManager,
								"bsManagerTel" : data.data.bsManagerTel
							});
							openDepartment(id, "编辑部门")
						}

						else {
							layer.alert(data.msg)
						}
					}, "POST", false, function(res) {
						layer.alert("操作请求错误，请您稍后再试");
					});
		}
		// 设置用户正常/禁用
		function setStatus(obj, id, name, checked) {
			var isStatus = checked ? 0 : 1;
			var deaprtisStatus = checked ? "正常" : "禁用";
			// 正常/禁用

			layer.confirm('您确定要把部门：' + name + '设置为' + deaprtisStatus + '状态吗？',
					{
						btn1 : function(index) {
							var param = {
								"id" : id,
								"bsStatus" : isStatus
							};
							CoreUtil.sendAjax("/base/depart/doStatus", JSON
									.stringify(param), function(data) {
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
								layer.alert("操作请求错误，请您稍后再试", function() {
									layer.closeAll();
								});
							});
						},
						btn2 : function() {
							obj.elem.checked = isStatus;
							form.render();
							layer.closeAll();
						},
						cancel : function() {
							obj.elem.checked = isStatus;
							form.render();
							layer.closeAll();
						}
					});
		}
	});
});

// 新增编辑弹出框
function openDepartment(id, title) {
	if (id == null || id == "") {
		$("#id").val("");
	}
	layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#setDepartment'),
		end : function() {
			cleanDepartment();
		}
	});
}

// 添加部门
function addDepartment() {
	// 清空弹出框数据
	cleanDepartment();
	// 打开弹出框
	openDepartment(null, "添加部门");
}
// 新增部门提交
function addSubmit(obj) {
	CoreUtil.sendAjax("base/depart/add", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanDepartment();
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

// 编辑部门提交
function editSubmit(obj) {
	CoreUtil.sendAjax("base/depart/edit", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanDepartment();
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

// 删除部门
function delDepartment(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除' + name + '部门吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("base/depart/delete", JSON.stringify(param),
					function(data) {
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
function cleanDepartment() {
	$('#departmentForm')[0].reset();
	layui.form.render();// 必须写
}
