/**
 * 线体管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table' ], function() {
		var table = layui.table, form = layui.form;

		tableIns = table.render({
			elem : '#lineList',
			url : context + 'base/line/getList',
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
				field : 'lineNo',
				title : '线体编码'
			}, {
				field : 'lineName',
				title : '线体名称'
			},
			{
				field : 'linerCode',
				title : '线长工号'
			}, {
				field : 'linerName',
				title : '线长姓名'
			}
			, {
				field : 'checkStatus',
				title : '状态',
				width : 95,
				templet : '#statusTpl'
			}, {
				field : 'lastupdateDate',
				title : '更新时间'
			}, {
				field : 'createDate',
				title : '添加时间',
			}, {
				fixed : 'right',
				title : '操作',
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
			setStatus(obj, this.value, this.name, obj.elem.checked);
		});
		// 监听工具条
		table.on('tool(lineTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delLine(data, data.id, data.lineNo);
			} else if (obj.event === 'edit') {
				// 编辑
				getLine(data, data.id);
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
		// 编辑线体
		function getLine(obj, id) {
			var param = {
				"id" : id
			};
			CoreUtil.sendAjax("base/line/getLine", JSON.stringify(param),
					function(data) {
						if (data.result) {
							form.val("lineForm", {
								"id" : data.data.id,
								"lineNo" : data.data.lineNo,
								"lineName" : data.data.lineName,
								"linerCode" : data.data.linerCode,
								"linerName" : data.data.linerName,
							});
							openLine(id, "编辑线体")
						} else {
							layer.alert(data.msg)
						}
					}, "POST", false, function(res) {
						layer.alert("操作请求错误，请您稍后再试");
					});
		}

		// 设置正常/禁用
		function setStatus(obj, id, name, checked) {
			var isStatus = checked ? 1 : 0;
			var deaprtisStatus = checked ? "正常":"禁用";
			// 正常/禁用
			layer.confirm('您确定要把线体：' + name + '设置为' + deaprtisStatus + '状态吗？',
					{
						btn1 : function(index) {
							var param = {
								"id" : id,
								"checkStatus" : isStatus
							};
							CoreUtil.sendAjax("/base/line/doStatus", JSON
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

// 设置用户正常/禁用
function setStatus(obj, id, name, checked) {
	var isStatus = checked ? 0 : 1;
	var deaprtisStatus = checked ? "正常" : "禁用";
	// 正常/禁用
	layer.confirm('您确定要把线体：' + name + '设置为' + deaprtisStatus + '状态吗？', {
		btn : [ '确认', '返回' ]
	// 按钮
	}, function() {
		var param = {
			"id" : id,
			"checkStatus" : isStatus
		};
		CoreUtil.sendAjax("/base/line/doStatus", JSON.stringify(param),
				function(data) {
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
	});
}

// 新增编辑弹出框
function openLine(id, title) {
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
		content : $('#setLine'),
		end : function() {
			cleanLine();
		}
	});
}

// 添加线体
function addLine() {
	// 清空弹出框数据
	cleanLine();
	// 打开弹出框
	openLine(null, "添加线体");
}
// 新增线体提交
function addSubmit(obj) {
	CoreUtil.sendAjax("base/line/add", JSON.stringify(obj.field),
			function(data) {
				if (data.result) {
					layer.alert("操作成功", function() {
						layer.closeAll();
						cleanLine();
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

// 编辑线体提交
function editSubmit(obj) {
	CoreUtil.sendAjax("base/line/edit", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanLine();
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

// 删除线体
function delLine(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除' + name + '线体吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("base/line/delete", JSON.stringify(param),
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
function cleanLine() {
	$('#lineForm')[0].reset();
	layui.form.render();// 必须写
}
