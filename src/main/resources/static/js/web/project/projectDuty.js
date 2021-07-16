/**
 * 不良内容管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table' , 'laydate'], function() {
		var table = layui.table, form = layui.form ,laydate = layui.laydate;



		tableIns = table.render({
			elem : '#clientList',
			url : context + '/projectDuty/getList',
			method : 'get' // 默认：get请求
			,
			cellMinWidth : 80,
			height: 'full-80'
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
			cols : [
			// ,{field:'id', title:'ID', width:80, unresize:true, sort:true}
				[
					{rowspan: 2, colspan: 1, align : 'center',field : 'projectCode', title : '项目编码',width:120,  sort:true},
					{rowspan: 2, colspan: 1, align : 'center',field : 'projectName', title : '项目名称',width:120,  sort:true},
					{rowspan: 2, colspan: 1, align : 'center',field : 'manageName', title : '项目经理',width:120, },
					{rowspan: 1, colspan: 2, align : 'center',field : 'manageName', title : '蓝图阶段'},
					{rowspan:1, colspan: 2, align : 'center',field : 'manageName', title : '开发测试阶段'},
					{rowspan: 1, colspan: 2, align : 'center',field : 'manageName', title : '上线实施'},
					{rowspan: 1, colspan: 2, align : 'center',field : 'manageName', title : '验收结案阶段'},
					{rowspan: 2, colspan: 1, align : 'center',width:220,field : 'latestShow', title : '最新说明'},
					{rowspan: 2, colspan: 1,fixed : 'right',width:120, title : '操作', align : 'center', toolbar : '#optBar'}
				],
				[
					{field : 'startTime1',width:120,  title : '预计完成时间'},
					{field : 'endTime1', width:120, title : '实际完成时间'},
					{field : 'startTime2',width:120,  title : '预计完成时间'},
					{field : 'endTime2', width:120, title : '实际完成时间'},
					{field : 'startTime3', width:120, title : '预计完成时间'},
					{field : 'endTime3', width:120, title : '实际完成时间'},
					{field : 'startTime4', width:120, title : '预计完成时间'},
					{field : 'endTime4',width:120,  title : '实际完成时间'},
				// {field : 'lastupdateDate', title : '更新时间'},
				// {field : 'createDate', title : '添加时间',}
				// {fixed : 'right', title : '操作',width:120, align : 'center', toolbar : '#optBar'}
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

		//日期
		laydate.render({
			elem: '#startTime1',
			trigger:'click'
			// range: true
		});
		//日期
		laydate.render({
			elem: '#startTime2',
			trigger:'click'
			// range: true
		});

		laydate.render({
			elem: '#startTime3',
			trigger:'click'
			// range: true
		});
		laydate.render({
			elem: '#startTime4',
			trigger:'click'
			// range: true
		});
		laydate.render({
			elem: '#endTime1',
			trigger:'click'
			// range: true
		});
		laydate.render({
			elem: '#endTime2',
			trigger:'click'
			// range: true
		});
		laydate.render({
			elem: '#endTime3',
			trigger:'click'
			// range: true
		});
		laydate.render({
			elem: '#endTime4',
			trigger:'click'
			// range: true
		});
		// 监听工具条
		table.on('tool(clientTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delClient(data, data.id, data.projectCode);
			} else if (obj.event === 'edit') {
				// 编辑
				getClient(data, data.id);
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
		// 编辑不良内容
		function getClient(obj, id) {

			form.val("clientForm", {
				"id" : obj.id,
				"projectCode" : obj.projectCode,
				"projectName" : obj.projectName,
				"manageName" : obj.manageName,
				"startTime1" : obj.startTime1,
				"endTime1" : obj.endTime1,
				"startTime2" : obj.startTime2,
				"endTime2" : obj.endTime2,
				"startTime3" : obj.startTime3,
				"endTime3" : obj.endTime3,
				"startTime4" : obj.startTime4,
				"endTime4" : obj.endTime4,
				"latestShow" : obj.latestShow,
			});
			openClient(id, "编辑在建项目档案信息："+obj.projectName)

			// var param = {
			// 	"id" : id
			// };
			// CoreUtil.sendAjax("/projectDuty/getList", JSON.stringify(param),
			// 		function(data) {
			// 			if (data.result) {
			// 				form.val("clientForm", {
			// 					"id" : data.data.id,
			// 					"custNo" : data.data.custNo,
			// 					"custName" : data.data.custName,
			// 					"custNameS" : data.data.custNameS,
			// 				});
			// 				openClient(id, "编辑在建项目档案信息"+obj.projectName)
			// 			} else {
			// 				layer.alert(data.msg)
			// 			}
			// 		}, "POST", false, function(res) {
			// 			layer.alert("操作请求错误，请您稍后再试");
			// 		});
		}
	});

});

// 新增编辑弹出框
function openClient(id, title) {
	if (id == null || id == "") {
		$("#id").val("");
	}
	var index = layer.open({
		type: 1,
		title: title,
		fixed: false,
		resize: false,
		shadeClose: true,
		area: ['550px'],
		content: $('#setClient'),
		end: function () {
			cleanClient();
		}
	});
	layer.full(index);
}


// 添加不良内容
function addClient() {
	// 清空弹出框数据
	cleanClient();
	// 打开弹出框
	openClient(null, "添加在建项目档案信息");
}
// 新增不良内容提交
function addSubmit(obj) {
	CoreUtil.sendAjax("/projectDuty/add", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanClient();
				// 加载页面
				loadAll();
			});
		} else {
			layer.alert(data.msg, function(index) {
				layer.close(index);
			});
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}

// 编辑不良内容提交
function editSubmit(obj) {
	CoreUtil.sendAjax("/projectDuty/edit", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanClient();
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

// 删除不良内容
function delClient(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除项目编码为' + name + '的信息吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/projectDuty/delete", JSON.stringify(param),
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
function cleanClient() {
	$('#clientForm')[0].reset();
	layui.form.render();// 必须写
}
