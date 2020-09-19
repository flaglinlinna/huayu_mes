/**
 * 工时管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table' ], function() {
		var table = layui.table, form = layui.form;

		tableIns = table.render({
			elem : '#hoursList',
			url : context + 'base/hours/getList',
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
				title : '产品编码'
			}, {
				field : 'bsStdHrs',
				title : '标准工时'
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
		// 监听工具条
		table.on('tool(hoursTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delHours(data, data.id, data.bsCode);
			} else if (obj.event === 'edit') {
				// 编辑
				console.log("edit");
				getHours(data, data.id);
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
		
//		form.verify({
//			  hours: [
//			   /^([1-9][0-9]*)+(.[0-9]{1,2})?$/
//			    ,'标准工时为大于1的正数'
//			  ] 
//			});    
		
		// 编辑产品工时
		function getHours(obj, id) {
			var param = {
				"id" : id
			};
			CoreUtil.sendAjax("base/hours/getHours", JSON.stringify(param),
					function(data) {
						if (data.result) {
							form.val("hoursForm", {
								"id" : data.data.id,
								"bsCode" : data.data.bsCode,
								"bsStdHrs" : data.data.bsStdHrs,
							});
							openHours(id, "编辑产品工时")
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
function openHours(id, title) {
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
		content : $('#setHours'),
		end : function() {
			cleanHours();
		}
	});
}

// 添加产品工时
function addHours() {
	// 清空弹出框数据
	cleanHours();
	// 打开弹出框
	openHours(null, "添加产品工时");
}
// 新增产品工时提交
function addSubmit(obj) {
	CoreUtil.sendAjax("base/hours/add", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanHours();
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

// 编辑产品工时提交
function editSubmit(obj) {
	CoreUtil.sendAjax("base/hours/edit", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanHours();
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

// 删除产品工时
function delHours(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除' + name + '产品工时吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("base/hours/delete", JSON.stringify(param),
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
function cleanHours() {
	$('#hoursForm')[0].reset();
	layui.form.render();// 必须写
}
