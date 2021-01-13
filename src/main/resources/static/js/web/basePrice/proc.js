/**
 * 工序维护管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table','tableSelect'  ], function() {
		var table = layui.table, form = layui.form,tableSelect = layui.tableSelect;

		tableIns = table.render({
			elem : '#procList',
			url : context + '/basePrice/proc/getList',
			method : 'get' // 默认：get请求
			,
			height:'full-70',//固定表头&full-查询框高度
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
			}, {
				field : 'procNo',
				title : '工序编号',
				width:90
			}, {
				field : 'procName',
				title : '工序名称',
			}, {
				field : 'workCenter',
				title : '工作中心名称',
			}, {
				field : 'checkStatus',
				title : '有效状态',
				templet : '#statusTpl',
				width:95
			},

			{
				field : 'createBy',
				title : '创建人',
				width:80
			}, {
				field : 'createDate',
				title : '创建时间',
			}, {
				field : 'lastupdateBy',
				title : '更新人',
				width:80
			}, {
				field : 'lastupdateDate',
				title : '更新时间',
			}, {
				fixed : 'right',
				title : '操作',
				align : 'center',
				toolbar : '#optBar',
				width:120
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
		
		tableSelect = tableSelect
		.render({
			elem : '#workcenterName',
			searchKey : 'keyword',
			checkedKey : 'id',
			searchPlaceholder : '试着搜索',
			table : {
				//width : 220,
				url : context
						+ '/basePrice/proc/getWorkCenterList',
				method : 'get',

				cols : [ [ {
					type : 'radio'
				},// 多选 radio
				, {
					field : 'ID',
					title : 'ID',
					width : 0,
					hide : true
				},

				{
					field : 'WORKCENTER_CODE',
					title : '工作中心编码',
					
				},

				{
					field : 'WORKCENTER_NAME',
					title : '工作中心',
					
				}

				] ],
				page : true,
				request : {
					pageName : 'page' // 页码的参数名称，默认：page
					,
					limitName : 'rows' // 每页数据量的参数名，默认：limit
				},
				parseData : function(res) {
					if (!res.result) {
						// 可进行数据操作
						return {
							"count" : 0,
							"msg" : res.msg,
							"data" : [],
							"code" : res.status
						// code值为200表示成功
						}
					}
					return {
						"count" : res.data.Total,
						"msg" : res.msg,
						"data" : res.data.List,
						"code" : res.status
					// code值为200表示成功
					}
				},
			},
			done : function(elem, data) {
				// 选择完后的回调，包含2个返回值
				// elem:返回之前input对象；data:表格返回的选中的数据 []
				var da = data.data;
				form.val("procForm", {
					"workcenterId" : da[0].ID,
					"workcenterName" : da[0].WORKCENTER_NAME,
				});
				form.render();// 重新渲染
			}
		});
		
		// 切换状态操作
		form.on('switch(isStatusTpl)', function(obj) {
			doStatus(obj, this.value, this.name, obj.elem.checked);
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
			console.log(obj)
			form.val("procForm", {
						"id" : obj.id,
						"procNo" : obj.procNo,
						"procName" : obj.procName,
						"workcenterName" : obj.workCenter,
						"workcenterId":obj.workCenterId
					});
					openProc(obj.id, "编辑工序信息")
		}

		// 设置正常/禁用
		function doStatus(obj, id, name, checked) {
			var isStatus = checked ? 1 : 0;
			var deaprtisStatus = checked ? "正常" : "禁用";
			// 正常/禁用
			layer.confirm(
					'您确定要把工序：' + name + '设置为' + deaprtisStatus + '状态吗？', {
						btn1 : function(index) {
							var params = {
								"id" : id,
								"checkStatus" : isStatus
							};
							CoreUtil.sendAjax("/basePrice/proc/doStatus",
									JSON.stringify(params), function(data) {
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
										layer.alert("操作请求错误，请您稍后再试",
												function() {
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

// 新增工序维护
function addProc() {
	// 清空弹出框数据
	cleanProc();
	// 打开弹出框
	openProc(null, "添加工序信息");
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
	CoreUtil.sendAjax("/basePrice/proc/edit", JSON.stringify(obj.field),
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

// 删除工序维护
function delProc(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除该名称为:' + name + ' 的工序信息吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/basePrice/proc/delete", JSON
					.stringify(param), function(data) {
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
function cleanProc() {
	$('#procForm')[0].reset();
	layui.form.render();// 必须写
}
