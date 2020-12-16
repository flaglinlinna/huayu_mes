/**
 * 不良类别管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table','tableSelect' ], function() {
		var table = layui.table, form = layui.form,tableSelect = layui.tableSelect;

		tableIns = table.render({
			elem : '#workflowList',
			url : context + '/check/Workflow/getList',
			method : 'get' // 默认：get请求
			,
			cellMinWidth : 80,
			 toolbar: '#toolbar', //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
			height:'full-110'//固定表头&full-查询框高度
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
			cols : [ [ {
				type : 'numbers'
			}
			// ,{field:'id', title:'ID', width:80, unresize:true, sort:true}
			, {
				field : 'bsFlowCode',
				title : '编码',sort:true,width : 95
			}, {
				field : 'bsFlowName',
				title : '名称',width : 140,sort:true
			},
				{
					field : 'bsFlowDescribe',
					title : '备注',
					width : 260,
				},
				{
				field : 'checkStatus',
				title : '状态',
				width : 95,
				templet : '#statusTpl'
			}, {
				field : 'lastupdateDate',
				title : '更新时间',width : 145
			}, {
				field : 'createDate',
				title : '添加时间',width : 145,
			}, {
				fixed : 'right',
				title : '操作',
				align : 'center',
				toolbar : '#optBar',
					width : 200,
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

		tableIns1 = table.render({
			elem : '#iList',
			method : 'get' // 默认：get请求
			,cellMinWidth : 80
			,even:true,//条纹样式
			data : [],
			//height: 'full',
			page : false,
			limit:100,
			request : {
				pageName : 'page' // 页码的参数名称，默认：page
				,
				limitName : 'rows' // 每页数据量的参数名，默认：limit
			},
			parseData : function(res) {
				if(!res.result){
					return {
						"count" : 0,
						"msg" : res.msg,
						"data" : [],
						"code" : res.status
					}
				}
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
					field : 'bsCheckGrade',
					title : '步骤序号',width : 120, sort: true,
				}, {
					field : 'bsStepName',
					title : '步骤名称',width : 160, sort: true
				},
				{
					field : 'bsCheckName',
					title : '审批人',width : 120, sort: true
				},
				// {
				// 	field : 'USER_NAME',
				// 	title : '操作人',
				// 	width : 100, sort: true
				// },
				{
					field : 'createDate',
					title : '创建时间',width : 150, sort: true
				},
				{
					title : '操作',
					align : 'center',
					width : 125,
					toolbar : '#optBar1'
				}

			] ],
			done : function(res, curr, count) {
				pageCurr1 = curr;
			}
		});

		///sysUser/getList
		tableSelect = tableSelect.render({
			elem : '#bsCheckName',
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
						width : 120
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
				form.val("workflowStep", {
					"bsCheckBy" : da[0].userCode,
					"bsCheckId" : da[0].id,
					"bsCheckName":da[0].userName,
				});
				form.render();// 重新渲染
			}
		});

		// 监听在职操作
		form.on('switch(isStatusTpl)', function(obj) {
			setStatus(obj, this.value, this.name, obj.elem.checked);
		});
		// 监听工具条
		table.on('tool(workflowTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delDefect(data, data.id, data.bsFlowCode);
			} else if (obj.event === 'edit') {
				// 编辑
				getDefect(data, data.id);
			} else if (obj.event ==='setStep'){
				var param = {
					"mid" : data.id
				};
				tableIns1.reload({
					url:context+'/check/WorkflowStep/getList',
					where:param,
					done: function(res1, curr, count){
						pageCurr1=curr;
					}
				})
				openSetStep(data.id,data.bsFlowName);
			}
		});

		//监听
		table.on('tool(iTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delFlowStep(data, data.id, data.bsStepName);
			} else if (obj.event === 'edit') {
				// 编辑
				getDefect(data, data.id);
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

		form.on('submit(addSubmit1)', function(data) {
			if (data.field.id == null || data.field.id == "") {
				// 新增
				addSubmit1(data);
			} else {
				editSubmit1(data);
			}
			return false;
		});

		// 监听搜索框
		form.on('submit(searchSubmit)', function(data) {
			// 重新加载table
			load(data);
			return false;
		});
		// 编辑流程
		function getDefect(obj, id) {
			console.log(obj)
			form.val("defectForm", {
				"id" : obj.id,
				"bsFlowCode" : obj.bsFlowCode,
				"bsFlowName" : obj.bsFlowName,
				"bsFlowDescribe" : obj.bsFlowDescribe,
			});
			openDefect(id, "编辑不良类别")
		}
		//编辑步骤
		function getStep(obj, id) {
			console.log(obj)
			form.val("workflowStep", {
				"id" : obj.id,
				"bsCheckGrade" : obj.bsCheckGrade,
				"bsStepName" : obj.bsStepName,
				"bsCheckBy" : obj.bsCheckBy,
				"bsCheckId":obj.bsCheckId,
				"bsCheckName":obj.bsCheckName,
			});
			openWorkflowStep(id, "编辑步骤信息")
		}
		// 设置用户正常/禁用
		function setStatus(obj, id, name, checked) {
			// setStatus(obj, this.value, this.name, obj.elem.checked);
			var isStatus = checked ? 1 : 0;
			var deaprtisStatus = checked ? "正常":"禁用";
			// 正常/禁用

			layer.confirm(
					'您确定要把不良类别：' + name + '设置为' + deaprtisStatus + '状态吗？', {
						btn1 : function(index) {
							var param = {
								"id" : id,
								"checkStatus" : isStatus
							};
							CoreUtil.sendAjax("/check/Workflow/doStatus", JSON
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
					})
		}

		function setStep() {

		}
	});

});

function openSetStep(id,title) {
	$('#bsFlowId').val(id);
	var index=layer.open({
		type : 1,
		title : "设置 "+title +"的流程步骤",
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#setworkflowStep'),
		end : function() {
			cleanStep();
		}
	});
	layer.full(index);
}
// 新增编辑弹出框
function openDefect(id, title) {
	if (id == null || id == "") {
		$("#id").val("");
	}
	var index=layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#setDefect'),
		end : function() {
			cleanDefect();
		}
	});
	layer.full(index);
}

//编辑步骤信息
function openWorkflowStep(id ,title) {

}

// 添加不良类别
function addWorkflow() {
	// 清空弹出框数据
	cleanDefect();
	// 打开弹出框
	openDefect(null, "添加流程信息");
}
// 新增不良类别提交
function addSubmit(obj) {
	CoreUtil.sendAjax("/check/Workflow/add", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanDefect();
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

// 编辑不良类别提交
function editSubmit(obj) {
	CoreUtil.sendAjax("/check/Workflow/edit", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanDefect();
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

// 新增不良类别提交
function addSubmit1(obj) {
	CoreUtil.sendAjax("/check/WorkflowStep/add", JSON.stringify(obj.field), function(
		data) {
		if (data.result) {
			layer.alert("操作成功", function(index) {
				layer.close(index);
				cleanStep();
				// 加载页面
				loadStep();
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

// 编辑不良类别提交
function editSubmit1(obj) {
	CoreUtil.sendAjax("/check/WorkflowStep/edit", JSON.stringify(obj.field), function(
		data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanDefect();
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

// 删除流程
function delDefect(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除' + name + '流程信息吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/check/Workflow/delete", JSON.stringify(param),
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
								layer.alert(data.msg, function() {
									layer.closeAll();
								});
							}
						}
					});
		});
	}
}

// 删除步骤
function delFlowStep(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除' + name + '步骤吗？', {
			btn : [ '确认', '返回' ]
			// 按钮
		}, function() {
			CoreUtil.sendAjax("/check/WorkflowStep/delete", JSON.stringify(param),
				function(data) {
					if (isLogin(data)) {
						if (data.result == true) {
							// 回调弹框
							layer.alert("删除成功！", function(index) {
								layer.close(index);
								// 加载load方法
								loadStep();
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
// 重新加载表格 步骤
function loadStep() {
	// 重新加载table
	tableIns1.reload({
	});
}
// 清空新增表单数据
function cleanDefect() {
	$('#defectForm')[0].reset();
	layui.form.render();// 必须写
}

// 清空设置步骤内容
function cleanStep() {
	$('#workflowStep')[0].reset();
	layui.form.render();// 必须写
}
