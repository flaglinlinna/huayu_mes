/**
 * 卡机信息管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table' ], function() {
		var table = layui.table, form = layui.form;

		tableIns = table.render({
			elem : '#devList',
			url : context + '/produce/dev_clock/getList',
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
				field : 'devCode',
				title : '卡机编码',
				width : 120,
			}, {
				field : 'devName',
				title : '卡机名称',
				width : 120,
			}, {
				field : 'devIp',
				title : '卡机IP',
				width : 120,
			}, {
				field : 'devSeries',
				title : '卡机序列',
				width : 120,
			}, {
				field : 'lineId',
				title : '线别',
				width : 120,
			}, {
				field : 'devType',
				title : '卡机类型',
				width : 120,
			}, {
				field : 'enabled',
				title : '是否有效',
				width : 95,
				templet : '#statusTpl'
			}, {
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
				width: 140
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
		// 监听操作-是否有效
		form.on('switch(isStatusTpl)', function(obj) {
			setEnabled(obj, this.value, this.name, obj.elem.checked);
		});
		// 监听工具条
		table.on('tool(devTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delDevClock(data, data.id, data.devCode);
			} else if (obj.event === 'edit') {
				// 编辑
				getDevClock(data, data.id);
			}
		});
		// 监听测试连接
		form.on('submit(testLink)', function(data) {
			testLink(data);
			return false;
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
		// 编辑卡机信息
		function getDevClock(obj, id) {
			var param = {
				"id" : id
			};
			CoreUtil.sendAjax("/produce/dev_clock/getDevClock", JSON
					.stringify(param), function(data) {
				if (data.result) {
					form.val("devForm", {
						"id" : data.data.id,
						"devCode" : data.data.devCode,
						"devName" : data.data.devName,
						"devIp" : data.data.devIp,
						"devSeries" : data.data.devSeries,
						"devType" : data.data.devType
					});
					getLineList(data.data.lineId);
					openDevClock(id, "编辑卡机信息")
				} else {
					layer.alert(data.msg)
				}
			}, "POST", false, function(res) {
				layer.alert("操作请求错误，请您稍后再试");
			});
		}
		// 设置是否在线
		function setEnabled(obj, id, name, checked) {
			// setStatus(obj, this.value, this.name, obj.elem.checked);
			var isStatus = checked ? 1 : 0;
			var deaprtisStatus = checked ?  "有效":"无效";
			
			layer.confirm(
					'您确定要把卡机设备：' + name + '设置为' + deaprtisStatus + '状态吗？', {
						btn1 : function(index) {
							var param = {
								"id" : id,
								"enabled" : isStatus
							};
							CoreUtil.sendAjax("/produce/dev_clock/doEnabled", JSON
									.stringify(param), function(data) {
								if (data.result) {
									layer.alert("操作成功", function() {
										layer.closeAll();
										loadAll();
									});
								} else {
									layer.alert(data.msg);
								}
							}, "POST", false, function(res) {
								layer.alert("操作请求错误，请您稍后再试");
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
		
	});

});

// 新增编辑弹出框
function openDevClock(id, title) {
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
		content : $('#setDevClock'),
		end : function() {
			cleanDevClock();
		}
	});
	layer.full(index);
}

// 添加卡机信息
function addDevClock() {
	// 清空弹出框数据
	cleanDevClock();
	// 打开弹出框
	getLineList("");
	openDevClock(null, "添加卡机信息");
}

function exportDevClock(){
	var keywords = $("#keywordSearch").val();
	location.href = context + "/produce/dev_clock/exportList?keyword="+keywords;
}

//获取线体信息
function getLineList(id){
	CoreUtil.sendAjax("/produce/dev_clock/getLineList", "",
			function(data) {
				if (data.result) {
				$("#lineId").empty();
				var line=data.data;
				for (var i = 0; i < line.length; i++) {
					if(i==0){
						$("#lineId").append("<option value=''>请点击选择</option>");
					}
					$("#lineId").append("<option value=" + line[i].id+ ">" + line[i].lineName + "</option>");
					if(line[i].id==id){
						$("#lineId").val(line[i].id);
					}
				}					
				layui.form.render('select');

				} else {
					layer.alert(data.msg)
				}
			}, "POST", false, function(res) {
				layer.alert("操作请求错误，请您稍后再试");
			});
}
//测试连接
function testLink(obj) {
	CoreUtil.sendAjax("/produce/dev_clock/test", JSON.stringify(obj.field),
			function(data) {
	         	layer.alert(data.msg);
				/*if (data.result) {
					layer.alert("操作成功", function() {
						layer.closeAll();
						cleanDevClock();
						// 加载页面
						loadAll();
					});
				} else {
					layer.alert(data.msg, function() {
						layer.closeAll();
					});
				}*/
			}, "POST", false, function(res) {
				layer.alert(res.msg);
			});
}
// 新增卡机信息提交
function addSubmit(obj) {
	CoreUtil.sendAjax("/produce/dev_clock/add", JSON.stringify(obj.field),
			function(data) {
				if (data.result) {
					layer.alert("操作成功", function() {
						layer.closeAll();
						cleanDevClock();
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

// 编辑卡机信息提交
function editSubmit(obj) {
	CoreUtil.sendAjax("/produce/dev_clock/edit", JSON.stringify(obj.field),
			function(data) {
				if (data.result) {
					layer.alert("操作成功", function() {
						layer.closeAll();
						cleanDevClock();
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

// 删除卡机信息
function delDevClock(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除' + name + '卡机吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/produce/dev_clock/delete",
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
function cleanDevClock() {
	$('#devForm')[0].reset();
	layui.form.render();// 必须写
}
