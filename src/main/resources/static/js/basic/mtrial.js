/**
 * 物料管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table' ], function() {
		var table = layui.table, form = layui.form;

		tableIns = table.render({
			elem : '#mtrialList',
			url : context + 'base/mtrial/getList',
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
				title : '物料编码'
			}, {
				field : 'bsName',
				title : '物料名称'
			}, {
				field : 'bsType',
				title : '物料类别'
			}, {
				field : 'bsUnit',
				title : '物料单位'
			}, {
				field : 'bsStatus',
				title : '状态',
				width : 95,
				templet : '#statusTpl'
			}, {
				field : 'modifiedTime',
				title : '更新时间'
			}, {
				field : 'createdTime',
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

		// 监听操作
		form.on('switch(isStatusTpl)', function(obj) {
			setStatus(obj, this.value, this.name, obj.elem.checked);
		});
		// 监听工具条
		table.on('tool(mtrialTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delMtrial(data, data.id, data.bsCode);
			} else if (obj.event === 'edit') {
				// 编辑
				console.log("edit");
				getMtrial(data, data.id);
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
		// 编辑物料信息
		function getMtrial(obj, id) {
			var param = {
				"id" : id
			};
			CoreUtil.sendAjax("base/mtrial/getMtrial", JSON.stringify(param),
					function(data) {
						if (data.result) {
							form.val("mtrialForm", {
								"id" : data.data.id,
								"bsCode" : data.data.bsCode,
								"bsName" : data.data.bsName,
								"bsType" : data.data.bsType,
								"bsUnit" : data.data.bsUnit
							});
							openMtrial(id, "编辑物料信息")
						} else {
							layer.alert(data.msg)
						}
					}, "POST", false, function(res) {
						layer.alert("操作请求错误，请您稍后再试");
					});
		}
		// 设置用户正常/禁用
		function setStatus(obj, id, name, checked) {
			// setStatus(obj, this.value, this.name, obj.elem.checked);
			var isStatus = checked ? 0 : 1;
			var deaprtisStatus = checked ? "正常" : "禁用";
			// 正常/禁用

			layer.confirm(
					'您确定要把物料：' + name + '设置为' + deaprtisStatus + '状态吗？', {
						btn1 : function(index) {
							var param = {
								"id" : id,
								"bsStatus" : isStatus
							};
							CoreUtil.sendAjax("/base/mtrial/doStatus", JSON
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
					})
		}
	});

});

// 新增编辑弹出框
function openMtrial(id, title) {
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
		content : $('#setMtrial'),
		end : function() {
			cleanMtrial();
		}
	});
}

// 添加物料信息
function addMtrial() {
	// 清空弹出框数据
	cleanMtrial();
	// 打开弹出框
	openMtrial(null, "添加物料信息");
}
// 新增物料信息提交
function addSubmit(obj) {
	CoreUtil.sendAjax("base/mtrial/add", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanMtrial();
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

// 编辑物料信息提交
function editSubmit(obj) {
	CoreUtil.sendAjax("base/mtrial/edit", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanMtrial();
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

// 删除物料信息
function delMtrial(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除' + name + '物料吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("base/mtrial/delete", JSON.stringify(param),
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
function cleanMtrial() {
	$('#mtrialForm')[0].reset();
	layui.form.render();// 必须写
}
