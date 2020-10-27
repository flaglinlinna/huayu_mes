/**
 * 员工信息管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table' ,'laydate'], function() {
		var table = layui.table, form = layui.form,laydate = layui.laydate;

		tableIns = table.render({
			elem : '#employeeList',
			url : context + 'base/employee/getList',
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
				field : 'empCode',
				title : '员工工号',
				width:100,
				sort: true,
			}, {
				field : 'empName',
				title : '员工姓名',
				width:120,
				sort: true,
			}, {
				field : 'empType',
				title : '员工类型',
				width:100
			}, {
				field : 'empIdNo',
				title : '身份证',
				width:150
			}, {
				field : 'empStatus',
				title : '员工状态',
				templet : '#statusTpl',
				align : 'center',
				width:100
			}, {
				field : 'joinDate',
				title : '入职日期',
				width:110,
				sort: true,
			}, {
				field : 'leaveDate',
				title : '离职日期',
				width:110,
				sort: true,
			}, {
				field : 'deptId',
				title : '部门ID',
				width:90
			} , {
				field : 'lastupdateDate',
				title : '更新时间',
				width:150
			}, {
				field : 'createDate',
				title : '添加时间',
				width:150
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
		form.on('switch(isStatusTpl)', function(obj) {//修改员工状态
			//console.log(obj, this.value, this.name, obj.elem.checked);
			setStatus(obj, this.value, this.name, obj.elem.checked);
		});
		// 监听工具条
		table.on('tool(employeeTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delEmployee(data, data.id, data.custNo);
			} else if (obj.event === 'edit') {
				// 编辑
				getEmployee(data, data.id);
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
		//日期选择器
		laydate.render({ 
		  elem: '#joinDate',
		  type: 'date' //默认，可不填
		});
		laydate.render({ 
			  elem: '#leaveDate',
			  type: 'date' //默认，可不填
			});
		// 编辑员工信息
		function getEmployee(obj, id) {
			var param = {
				"id" : id
			};
			CoreUtil.sendAjax("base/employee/getEmployee", JSON
					.stringify(param), function(data) {
				if (data.result) {
					form.val("employeeForm", {
						"id" : data.data.id,
						"empCode" : data.data.empCode,
						"empName" : data.data.empName,
						"empIdNo" : data.data.empIdNo,
						"joinDate" : data.data.joinDate,
						"leaveDate" : data.data.leaveDate,
						"empType" : data.data.empType,
						"deptId" : data.data.deptId,
					});
					openEmployee(id, "编辑员工信息")
				} else {
					layer.alert(data.msg)
				}
			}, "POST", false, function(res) {
				layer.alert("操作请求错误，请您稍后再试");
			});
		}
		// 设置员工状态
		function setStatus(obj, id, name, checked) {
			var empStatus = checked ? 1 : 0;
			var deaprtisStatus = checked ? "在职" : "离职";
			// 正常/禁用
			layer.confirm('您确定要把员工：' + name + '状态设置为' + deaprtisStatus + '状态吗？',
					{
						btn1 : function(index) {
							var param = {
								"id" : id,
								"empStatus" : empStatus
							};
							CoreUtil.sendAjax("/base/employee/doStatus", JSON
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
							obj.elem.checked = empStatus;
							form.render();
							layer.closeAll();
						},
						cancel : function() {
							obj.elem.checked = empStatus;
							form.render();
							layer.closeAll();
						}
					});
		}
	});

});

// 新增编辑弹出框
function openEmployee(id, title) {
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
		content : $('#setEmployee'),
		end : function() {
			cleanEmployee();
		}
	});
}

// 添加员工信息
function addEmployee() {
	// 清空弹出框数据
	cleanEmployee();
	// 打开弹出框
	openEmployee(null, "添加员工信息");
}
// 新增员工信息提交
function addSubmit(obj) {
	CoreUtil.sendAjax("base/employee/add", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanEmployee();
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

// 编辑员工信息提交
function editSubmit(obj) {
	CoreUtil.sendAjax("base/employee/edit", JSON.stringify(obj.field),
			function(data) {
				if (data.result) {
					layer.alert("操作成功", function() {
						layer.closeAll();
						cleanEmployee();
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

// 删除员工信息
function delEmployee(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除' + name + '员工吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("base/employee/delete", JSON.stringify(param),
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
function cleanEmployee() {
	$('#employeeForm')[0].reset();
	layui.form.render();// 必须写
}
