/**
 * 补卡数据信息管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form','tableSelect', 'table','laydate'], function() {
		var table = layui.table, form = layui.form,
		tableSelect = layui.tableSelect, laydate = layui.laydate;
		tableIns = table.render({
			elem : '#cardList',
			url : context + '/produce/patch/getList',
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
				width:80
			},{
				field : 'empName',
				title : '员工姓名',
				width:100
			}, {
				field : 'className',
				title : '班次',
				width:80
			}, {
				field : 'cardType',
				title : '卡点类型',
				width:80
			}, {
				field : 'taskNo',
				title : '制令单',
				width:350
			}, {
				field : 'hourType',
				title : '工时类型',
				width:80
			},{
				field : 'signTime',
				title : '签卡时间',
				width:100
			},{
				field : 'signDate',
				title : '签卡日期',
				width:100
			},{
				field : 'lineName',
				title : '线体',
				width:120
			},{
				field : 'lastupdateDate',
				title : '更新时间',
				templet:'<div>{{d.lastupdateDate?DateUtils.formatDate(d.lastupdateDate):""}}</div>',
				width : 150,
			}, {
				field : 'createDate',
				title : '添加时间',
				templet:'<div>{{d.createDate?DateUtils.formatDate(d.createDate):""}}</div>',
				width : 150,
			}, {
				fixed : 'right',
				title : '操作',
				align : 'center',
				toolbar : '#optBar',
				width: 120
			} ] ],
			done : function(res, curr, count) {
				// 如果是异步请求数据方式，res即为你接口返回的信息。
				// 如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
				pageCurr = curr;
			}
		});
		
		empTableSelect = tableSelect.render({// 返工历史-制令单
			elem : '#empCode',
			searchKey : 'keyword',
			checkedKey : 'id',
			searchPlaceholder : '试着搜索',
			table : {
				url : context
						+ '/produce/patch/getEmpInfo',
				method : 'get',
				cols : [ [ {
					type : 'radio'
				},// 多选 radio
				, {
					field : 'id',
					title : 'id',
					width : 0,
					hide : true
				}, {
					field : 'EMP_CODE',
					title : '工号'
				}, {
					field : 'EMP_NAME',
					title : '姓名'
				} ] ],
				parseData : function(res) {
					//console.log(res)
					if (res.result) {
						// 可进行数据操作
						return {
							"count" : 0,
							"msg" : res.msg,
							"data" : res.data,
							"code" : res.status
						// code值为200表示成功
						}
					}

				},
			},
			done : function(elem, data) {
				//console.log(data)
				var da = data.data;
				//$("#empId").val(da[0].id)
				 form.val("cardForm", {
				 "empId" : da[0].ID,
				 "empCode":da[0].EMP_CODE,
				 "empName":da[0].EMP_NAME,
				 });
				 form.render();// 重新渲染
			}
		});
		
		taskTableSelect = tableSelect.render({// 返工历史-制令单
			elem : '#taskNo',
			searchKey : 'keyword',
			checkedKey : 'id',
			searchPlaceholder : '试着搜索',
			table : {
				url : context
						+ '/produce/patch/getTaskNo',
				method : 'get',
				cols : [ [ {
					type : 'radio'
				},// 多选 radio
				{
					field : 'TASK_NO',
					title : '制令单号',
					width:300
				},{
					field : 'CLASS_NAME',
					title : '班次',
					width:60
				}, {
					field : 'HOUR_TYPE',
					title : '工时类型',
					width:80
				}, {
					field : 'WORK_DATE',
					title : '生产时间',
					templet:'<div>{{d.WORK_DATE.substring(0,d.WORK_DATE.indexOf(" "))}}</div>',
					width:100
				}, {
					field : 'LINE_NAME',
					title : '线体',
					width:120
				}] ],
				parseData : function(res) {
					console.log(res)
					if (res.result) {
						// 可进行数据操作
						return {
							"count" : 0,
							"msg" : res.msg,
							"data" : res.data,
							"code" : res.status
						// code值为200表示成功
						}
					}

				},
			},
			done : function(elem, data) {
				console.log(data)
				var da = data.data;
				var wd=da[0].WORK_DATE.substring(0,da[0].WORK_DATE.indexOf(" "))
				 form.val("cardForm", {
				 "taskNo":da[0].TASK_NO,
				 "hourType" : da[0].HOUR_TYPE,
				 "workDate":wd,
				 "lineName":da[0].LINE_NAME,
				 "lineId":da[0].LINE_ID,
				 "className":da[0].CLASS_NAME,
				 "classId":da[0].CLASS_ID,
				 });
				 form.render();// 重新渲染
			}
		});
		
		// 日期选择器
		laydate.render({
			elem : '#signDate',trigger: 'click',
			type : 'date' // 默认，可不填
		});
		laydate.render({
			elem : '#signTime', trigger: 'click',
			type : 'time' // 默认，可不填
		});
		// 监听工具条
		table.on('tool(cardTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delPatchCard(data, data.id, data.empCode);
			} else if (obj.event === 'edit') {
				// 编辑
				getPatchCard(data, data.id);
			}
		});
		
		// 监听搜索框
		form.on('submit(searchSubmit)', function(data) {
			// 重新加载table
			load(data);
			return false;
		});
		// 监听switch操作
		form.on('switch(isStatusTpl)', function(obj) {
			setStatus(obj, this.value, this.name, obj.elem.checked);
		});
		
		// 监听提交
		form.on('submit(addSubmit)', function(data) {
			if (data.field.id == null || data.field.id == "") {
				// 新增
				//console.log(data)
				addSubmit(data);
			} else {
				editSubmit(data);
			}
			return false;
		});
		
		// 监听补卡查询
		form.on('submit(searchDev)', function(data) {
			loadDev(data.field.keywordDev);
			return false;
		});
		// 编辑补卡信息提交
		function editSubmit(obj) {
			CoreUtil.sendAjax("/produce/patch/edit", JSON.stringify(obj.field),
					function(data) {
						if (data.result) {
							layer.alert("操作成功", function() {
								layer.closeAll();
								cleanPatchCard();
								// 加载页面
								loadAll();
							});
						} else {
							layer.alert(data.msg);
						}
					}, "POST", false, function(res) {
						layer.alert(res.msg);
					});
		}
		function getPatchCard(obj, id) {
			console.log(obj)
			var param = {
				"id" : id
			};
			CoreUtil.sendAjax("/produce/patch/getPatchCard", JSON
					.stringify(param), function(data) {
				console.log(data)
				if (data.result) {
					form.val("cardForm", {
						"id" : data.data.id,
						"empId" : data.data.empId,
						"empCode" : data.data.employee.empCode,
						"empName" : data.data.employee.empName,
						"taskNo" : data.data.taskNo,
						"className" : data.data.classType.className,
						"classId" : data.data.classId,
						"hourType" : data.data.hourType,
						"workDate" : data.data.workDate,
						"lineId" : data.data.lineId,
						"lineName" : data.data.line.lineName,
						"cardType" : data.data.cardType,
						"signDate" : data.data.signDate,
						"signTime" : data.data.signTime,
					});
					$('#empCode').attr("disabled","disabled");
					openPatchCard(id, "编辑补卡信息")
				} else {
					layer.alert(data.msg)
				}
			}, "POST", false, function(res) {
				layer.alert("操作请求错误，请您稍后再试");
			});
		}
	});
});
//添加补卡数据信息
function addPatchCard() {
	// 清空弹出框数据
	cleanPatchCard();
	// 打开弹出框
	$('#empCode').removeAttr("disabled");
	openPatchCard(null, "添加补卡数据信息");
}
// 新增编辑弹出框
function openPatchCard(id, title) {
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
		content : $('#setPatchCard'),
		end : function() {
			cleanPatchCard();
		}
	});
}
//重新加载表格（搜索）
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

// 新增补卡数据信息提交
function addSubmit(obj) {
	CoreUtil.sendAjax("/produce/patch/add", JSON.stringify(obj.field),
			function(data) {
				if (data.result) {
					layer.alert("操作成功", function() {
						layer.closeAll();
						cleanPatchCard();
						// 加载页面
						loadAll();
					});
				} else {
					layer.alert(data.msg);
				}
			}, "POST", false, function(res) {
				layer.alert(res.msg);
			});
}

function delPatchCard(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除' + name + '补卡数据吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/produce/patch/delete",
					JSON.stringify(param), function(data) {
						if (isLogin(data)) {
							if (data.result == true) {
								// 回调弹框
								layer.alert("删除成功！", function() {
									layer.closeAll();
									// 加载load方法
									loadAll();
								});
							} else {
								layer.alert(data);
							}
						}
					});
		});
	}
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
function cleanPatchCard() {
	$('#cardForm')[0].reset();
	layui.form.render();// 必须写
}
