/**
 * 卡点数据信息管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table','laydate'], function() {
		var table = layui.table, form = layui.form, laydate = layui.laydate;

		tableIns = table.render({
			elem : '#cardList',
			url : context + 'produce/card_data/getList',
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
				title : '员工工号'
			},{
				field : 'empName',
				title : '员工姓名'
			}, {
				field : 'devIp',
				title : '卡机IP',
			}, {
				field : 'cardDate',
				title : '打卡日期',
			}, {
				field : 'cardTime',
				title : '打卡时间',
			}, {
				field : 'fstatus',
				title : '卡点状态',
				width : 95,
				templet : '#statusTpl'
			}, {
				field : 'fmemo',
				title : '备注',
			} , {
				fixed : 'right',
				title : '操作',
				align : 'center',
				toolbar : '#optBar',
				width: 100
			} ] ],
			done : function(res, curr, count) {
				// 如果是异步请求数据方式，res即为你接口返回的信息。
				// 如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
				pageCurr = curr;
			}
		});
		tableDev = table.render({
			elem : '#devTable',
			method : 'post',// 
			height: '400',
			page : true,
			request : {
				pageName : 'page', // 页码的参数名称，默认：page
				limitName : 'rows' // 每页数据量的参数名，默认：limit
			},
			parseData : function(res) {
				console.log(res)
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
				type : "checkbox"
			},

			{
				field : 'devCode',
				title : '卡机编号',
			}, {
				field : 'devName',
				title : '卡机名称',
			}, {
				field : 'devType',
				title : '卡机类型',
			}, {
				field : 'devIp',
				title : '卡机IP',
			} ] ],
			data : []
		});
		// 日期选择器
		laydate.render({
			elem : '#cardDate',trigger: 'click',
			type : 'date' // 默认，可不填
		});
		laydate.render({
			elem : '#cardTime', trigger: 'click',
			type : 'time' // 默认，可不填
		});
		// 监听工具条
		table.on('tool(cardTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delCardData(data, data.id, data.empCode);
			} else if (obj.event === 'edit') {
				// 编辑
				getCardData(data, data.id);
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
				addSubmit(data);
			} else {
				editSubmit(data);
			}
			return false;
		});
		
		// 监听卡机查询
		form.on('submit(searchDev)', function(data) {
			loadDev(data.field.keywordDev);
			return false;
		});
		
		// 监听卡机同步
		form.on('submit(updateData)', function(data) {
			//loadDev(data.field.keywordDev);
			var devList = "";
			var dList = table.checkStatus('devTable').data;// 被选中行的数据 id
			if(dList == ""){
				layer.msg('请先选择卡机', {
					time : 10000, // 10s后自动关闭
					btn : [ '确定' ]
				});
			}else{
				for (var i = 0; i < dList.length; i++) {// 获取被选中的行
					devList += dList[i].id + ";"// 用“;”分隔
				}
				updateData(devList,'1');
			}
			return false;
		});
		
		// 监听卡机同步并清除数据
		form.on('submit(updateAndDelData)', function(data) {
			//loadDev(data.field.keywordDev);
			var devList = "";
			var dList = table.checkStatus('devTable').data;// 被选中行的数据 id
			if(dList == ""){
				layer.msg('请先选择卡机', {
					time : 10000, // 10s后自动关闭
					btn : [ '确定' ]
				});
			}else{
				for (var i = 0; i < dList.length; i++) {// 获取被选中的行
					devList += dList[i].id + ";"// 用“;”分隔
				}
				updateData(devList,'2');
			}
			return false;
		});
		//设置用户正常/禁用
		function setStatus(obj, id, name, checked) {
			// setStatus(obj, this.value, this.name, obj.elem.checked);
			var isStatus = checked ? 1 : 0;
			var deaprtisStatus = checked ? "有效":"无效";
			// 正常/禁用

			layer.confirm(
					'您确定要把' + name + '的卡点信息设置为' + deaprtisStatus + '状态吗？', {
						btn1 : function(index) {
							var param = {
								"id" : id,
								"checkStatus" : isStatus
							};
							CoreUtil.sendAjax("/produce/card_data/doStatus", JSON
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
	});
});
// 新增编辑弹出框
function openCardData(id, title) {
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
		content : $('#setCardData'),
		end : function() {
			cleanCardData();
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
// 添加卡点数据信息
function addCardData() {
	// 清空弹出框数据
	cleanCardData();
	getEmp();
	getDev();
	// 打开弹出框
	openCardData(null, "添加卡点数据信息");
}
// 获取员工信息
function getEmp() {
	CoreUtil.sendAjax("produce/card_data/getEmp", "", function(data) {
		if (data.result) {
			$("#empId").empty();
			var emp = data.data;
			//console.log(emp)
			for (var i = 0; i < emp.length; i++) {
				if (i == 0) {
					$("#empId").append("<option value=''>请点击选择</option>");
				}
				$("#empId").append(
						"<option value=" + emp[i].id + ">" + emp[i].empCode
								+ "——" + emp[i].empName + "</option>");
			}
			layui.form.render('select');

		} else {
			layer.alert(data.msg)
		}
		//console.log(data)
	}, "POST", false, function(res) {
		layer.alert("操作请求错误，请您稍后再试");
	});
}
// 获取卡机信息
function getDev() {
	CoreUtil.sendAjax("produce/card_data/getDev", "", function(data) {
		if (data.result) {
			$("#devClockId").empty();
			var dev = data.data;
			//console.log(dev)
			for (var i = 0; i < dev.length; i++) {
				if (i == 0) {
					$("#devClockId").append("<option value=''>请点击选择</option>");
				}
				$("#devClockId").append(
						"<option value=" + dev[i].id + ">" + dev[i].devIp
								+ "</option>");
			}
			layui.form.render('select');

		} else {
			layer.alert(data.msg)
		}
		//console.log(data)
	}, "POST", false, function(res) {
		layer.alert("操作请求错误，请您稍后再试");
	});
}
// 新增卡点数据信息提交
function addSubmit(obj) {
	CoreUtil.sendAjax("produce/card_data/add", JSON.stringify(obj.field),
			function(data) {
				if (data.result) {
					layer.alert("操作成功", function() {
						layer.closeAll();
						cleanCardData();
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

function delCardData(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除' + name + '卡点数据吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("produce/card_data/delete",
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
function cleanCardData() {
	$('#cardForm')[0].reset();
	layui.form.render();// 必须写
}

//同步卡机信息
function update() {
	loadDev('');
	layer.open({
		type : 1,
		title : '手动同步卡机数据',
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '800px' ],
		content : $('#getCardData'),
		end : function() {
			loadDev('');
		}
	});
}
//重新加载表格-设备（搜索）
function loadDev(keyword) {
	// 重新加载table
	tableDev.reload({
		url : context + 'produce/issue/getDev',
		where : {
			devKeyword : keyword
		},
		page : {
			curr : pageCurr
		// 从当前页码开始
		}
	});
}

function updateData(devIds,stype){
	CoreUtil.sendAjax("produce/card_data/updateData", {"devIds" : devIds,"stype":stype}, function(
			data) {
		if (data.result) {
			layer.alert(data.msg, function() {
				layer.closeAll();
				// 加载页面
				loadAll();
			});
		} else {
			layer.alert(data.msg, function() {
				layer.closeAll();
			});
		}
	}, "GET", false, function(res) {
		layer.alert(res.msg);
	});
}