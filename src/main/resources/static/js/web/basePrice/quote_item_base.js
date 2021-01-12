/**
 * 报价项目设置管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table','tableSelect'  ], function() {
		var table = layui.table, form = layui.form,tableSelect = layui.tableSelect;

		tableIns = table.render({
			elem : '#procList',
			url : context + '/basePrice/quoteItemBase/getList',
			method : 'get' // 默认：get请求
			,
			cellMinWidth : 80,
			page : true,
			limit:20,
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
			}, {
				field : 'bsCode',
				title : '项目编码',
				width:120
			}, {
				field : 'bsName',
				title : '项目名称',
				width:120
			}, {
				field : 'bsPerson',
				title : '处理人',
				width:120
			},{
				fixed : 'right',
				title : '操作',
				align : 'center',
				toolbar : '#optBar',
				width:120
			}

			 ] ],
			done : function(res, curr, count) {

				pageCurr = curr;
			}
		});

		tableSelect = tableSelect.render({
			elem : '#bsPerson',
			searchKey : 'keyword',
			checkedKey : 'id',
			searchPlaceholder : '试着搜索',
			table : {
				url : context + '/sysUser/getList',
				method : 'get',
				cols : [ [ {
					type : 'numbers',
					title : '序号'
				}, {
					type : 'radio'
				},
					{
						field : 'ID',
						title : 'id',
						width : 0,
						hide : true
					},
					{
						field : 'userName',
						title : '姓名',
						width : 120
					},
					{
						field : 'roles',
						title : '角色',
						width : 240
					},
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
				form.val("procForm", {
					// "bsCheckBy" : da[0].userCode,
					"toDoBy" : da[0].id,
					"bsPerson":da[0].userName,
				});
				form.render();// 重新渲染
			}
		});
		


		// 监听工具条
		table.on('tool(procTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delProc(data, data.id, data.procName);
			} else if (obj.event === 'edit') {
				// 编辑
				getProc(data);
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
		function getProc(obj) {	
			// console.log(obj)
			form.val("procForm", {
						"id" : obj.id,
						"bsCode" : obj.bsCode,
						"bsName" : obj.bsName,
						"toDoBy" : obj.toDoBy,
						"bsPerson":obj.bsPerson
					});
					openProc(obj.id, "报价项目设置")
		}



	});

});

// 新增编辑弹出框
function openProc(id, title) {
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
		content : $('#setProc'),
		end : function() {
			cleanProc();
		}
	});
	layer.full(index);
}


// 新增工序维护的提交
function addSubmit(obj) {
	CoreUtil.sendAjax("/basePrice/proc/add", JSON.stringify(obj.field),
			function(data) {
				if (data.result) {
					layer.alert("操作成功", function() {
						layer.closeAll();
						cleanProc();
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
	CoreUtil.sendAjax("/basePrice/quoteItemBase/edit", JSON.stringify(obj.field),
			function(data) {
				if (data.result) {
					layer.alert("操作成功", function() {
						layer.closeAll();
						cleanProc();
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
function cleanProc() {
	$('#procForm')[0].reset();
	layui.form.render();// 必须写
}
