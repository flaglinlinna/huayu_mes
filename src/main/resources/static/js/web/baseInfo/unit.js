/**
 * 基本单位维护管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table' ], function() {
		var table = layui.table, form = layui.form;

		tableIns = table.render({
			elem : '#unitList',
			url : context + 'baseInfo/unit/getList',
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
				},
				{
					field : 'unitCode',
					title : '单位编码',
				},
				{
					field : 'unitName',
					title : '单位名称',
				},
				{
				field : 'createBy',
				title : '创建人',
				},
				{
				field : 'createDate',
				title : '创建时间',
				},
				{
				field : 'lastupdateBy',
				title : '上次修改人',
				},
				{
				field : 'lastupdateDate',
				title : '上次修改时间',
				},
				{
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

		// 监听工具条
		table.on('tool(unitTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delUnit(data, data.id, data.unitName);
			} else if (obj.event === 'edit') {
				// 编辑
				getUnit(data, data.id);
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
		// 编辑基本单位维护
		function getUnit(obj, id) {
			var param = {
				"id" : id
			};
			CoreUtil.sendAjax("/baseInfo/unit/getUnitDetail", JSON.stringify(param),
					function(data) {
						if (data.result) {
							form.val("unitForm", {
									"id": data.data.id,
									"unitCode": data.data.unitCode,
									"unitName": data.data.unitName,

							});
							openUnit(id, "编辑基本单位信息")
						} else {
							layer.alert(data.msg)
						}
					}, "POST", false, function(res) {
						layer.alert("操作请求错误，请您稍后再试");
					});
		}
	});

});

// 新增编辑弹出框
function openUnit(id, title) {
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
		content : $('#setUnit'),
		end : function() {
			cleanUnit();
		}
	});
}

// 新增基本单位维护
function addUnit() {
	// 清空弹出框数据
	cleanUnit();
	// 打开弹出框
	openUnit(null, "添加基本单位信息");
}
// 新增基本单位维护的提交
function addSubmit(obj) {
	CoreUtil.sendAjax("/baseInfo/unit/add", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanUnit();
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

// 编辑基本单位维护的提交
function editSubmit(obj) {
	CoreUtil.sendAjax("/baseInfo/unit/edit", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanUnit();
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

// 删除基本单位维护
function delUnit(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除该名称为:' + name + ' 的基本单位信息吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/baseInfo/unit/delete", JSON.stringify(param),
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
function cleanUnit() {
	$('#unitForm')[0].reset();
	layui.form.render();// 必须写
}
