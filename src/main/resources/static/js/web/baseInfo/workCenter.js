/**
 * 工作中心维护管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table' ], function() {
		var table = layui.table, form = layui.form;

		tableIns = table.render({
			elem : '#workCenterList',
			url : context + 'baseInfo/workCenter/getList',
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
						field : 'workcenterCode',
						title : '工作中心编号',
					},
					{
						field : 'workcenterName',
						title : '工作中心名称',
					},
					{
						field : 'fmemo',
						title : '备注',
					},
					{
						field : 'checkStatus',
						title : '有效状态',
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
				title : '更新人',
				},
				{
				field : 'lastupdateDate',
				title : '更新时间',
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
		table.on('tool(workCenterTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delWorkCenter(data, data.id, data.workcenterName);
			} else if (obj.event === 'edit') {
				// 编辑
				getWorkCenter(data, data.id);
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
		// 编辑工作中心维护
		function getWorkCenter(obj, id) {
			var param = {
				"id" : id
			};
			CoreUtil.sendAjax("baseInfo/workCenter/getWorkCenterDetail", JSON.stringify(param),
					function(data) {
						if (data.result) {
							form.val("workCenterForm", {
									"workcenterCode": data.data.workcenterCode,
									"workcenterName": data.data.workcenterName,
							});
							openWorkCenter(id, "编辑工作中心信息")
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
function openWorkCenter(id, title) {
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
		content : $('#setWorkCenter'),
		end : function() {
			cleanWorkCenter();
		}
	});
}

// 新增工作中心维护
function addWorkCenter() {
	// 清空弹出框数据
	cleanWorkCenter();
	// 打开弹出框
	openWorkCenter(null, "添加工作中心信息");
}
// 新增工作中心维护的提交
function addSubmit(obj) {
	CoreUtil.sendAjax("baseInfo/workCenter/add", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanWorkCenter();
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

// 编辑工作中心维护的提交
function editSubmit(obj) {
	CoreUtil.sendAjax("baseInfo/workCenter/edit", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanWorkCenter();
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

// 删除工作中心维护
function delWorkCenter(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除该名称为:' + name + ' 的工作中心信息吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("baseInfo/workCenter/delete", JSON.stringify(param),
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
function cleanWorkCenter() {
	$('#workCenterForm')[0].reset();
	layui.form.render();// 必须写
}
