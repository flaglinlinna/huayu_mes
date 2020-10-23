/**
 * 上线人员信息管理
 */
var pageCurr;
$(function() {
	layui
			.use(
					[ 'form', 'table', 'laydate' ],
					function() {
						var table = layui.table, form = layui.form, laydate = layui.laydate;

						tableIns = table
								.render({
									elem : '#onlineList',
									url : context + 'produce/online/getList',
									method : 'get', // 默认：get请求
									cellMinWidth : 80,
									page : true,
									request : {
										pageName : 'page',// 页码的参数名称，默认：page
										limitName : 'rows', // 每页数据量的参数名，默认：limit
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
									cols : [ [
											{
												type : 'numbers'
											}
											// ,{field:'id', title:'ID',
											// width:80, unresize:true,
											// sort:true}
											,
											{
												field : 'taskNo',
												title : '制令单号'
											},
											{
												field : 'hourType',
												title : '工时类型',
												width : 100
											},
											{
												field : 'lineName',
												title : '线体',
												width : 150
											},
											{
												field : 'lastupdateDate',
												title : '更新时间',
												width : 180,
												templet : '<div>{{d.lastupdateDate?DateUtils.formatDate(d.lastupdateDate):""}}</div>',
											},
											{
												field : 'createDate',
												title : '创建时间',
												width : 180,
												templet : '<div>{{d.createDate?DateUtils.formatDate(d.createDate):""}}</div>',
											}, {
												fixed : 'right',
												title : '操作',
												align : 'center',
												toolbar : '#optBar',
												width : 150
											} ] ],
									done : function(res, curr, count) {
										// 如果是异步请求数据方式，res即为你接口返回的信息。
										// 如果是直接赋值的方式，res即为：{data: [], count:
										// 99} data为当前页数据、count为数据总长度
										pageCurr = curr;
									}
								});
						tableEmp = table.render({
							elem : '#empList',
							method : 'post',// 默认：get请求
							page : false,
							request : {
								pageName : 'page',// 页码的参数名称，默认：page
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
								field : 'EMP_CODE',
								title : '员工工号',
								width : 195
							}, {
								field : 'EMP_NAME',
								title : '员工姓名',
								width : 195
							}, {
								field : 'DEV_IP',
								title : '设备IP',
								width : 150
							}, {
								field : 'TIME_BEGIN',
								title : '上线时间',
								width : 200
							}, {
								field : 'TIME_END',
								title : '下线时间',
								width : 200
							}, {
								title : '操作',
								align : 'center',
								toolbar : '#optBar1',
								width : 200
							} ] ],
							data : []
						});
						
						//日期选择器
						laydate.render({ 
						  elem: '#workDate',
						  type: 'date' //默认，可不填
						});
						
						// 监听工具条
						table.on('tool(onlineTable)', function(obj) {
							var data = obj.data;
							if (obj.event === 'del') {
								console.log(data)
								// 删除
								 delOnlineStaff(data, data.id, data.taskNo);
							} else if (obj.event === 'edit') {
								// 编辑
								getOnlineStaff(data, data.id);
							}
						});
						// 监听工具条
						table.on('tool(empTable)', function(obj) {
							var data = obj.data;
							if (obj.event === 'del') {
								// 删除
								 delVice(data,  data.EMP_NAME);
								//console.log(data)
							}
						});

						// 监听搜索框
						form.on('submit(searchSubmit)', function(data) {
							// 重新加载table
							load(data);
							return false;
						});

						// 监听提交
						form.on('submit(sumbitMain)', function(data) {
							editMain(data);
							return false;
						});

						// 编辑上线人员信息
						function getOnlineStaff(obj, id) {
							var param = {
								"id" : id
							};
							CoreUtil.sendAjax("produce/online/getMain", JSON
									.stringify(param), function(data) {
								if (data.result) {
									console.log(data)
									form.val("onlineForm", {
										"id" : data.data.id,
										"taskNo" : data.data.taskNo,
										"lineId" : data.data.line.id,
										"lineName" : data.data.line.lineName,
										"hourType" : data.data.hourType,
										"workDate":data.data.workDate
									});

									getClass(data.data.classId);
									getMainInfo(id);
								} else {
									layer.alert(data.msg)
								}
							}, "POST", false, function(res) {
								layer.alert("操作请求错误，请您稍后再试");
							});
						}
						// 编辑上线人员信息
						function getMainInfo(id) {
							var param = {
								"id" : id
							};
							CoreUtil.sendAjax("produce/online/getMainInfo",
									JSON.stringify(param), function(data) {
										if (data.result) {
											console.log(data)

											tableEmp.reload({
												data : data.data,
												done : function(res, curr,
														count) {
													pageCurr = curr;
												}
											})
											openOnlineStaff(id, "编辑上线人员信息")
										} else {
											layer.alert(data.msg)
										}
									}, "POST", false, function(res) {
										layer.alert("操作请求错误，请您稍后再试");
									});
						}
						function getClass(id) {
							//console.log(id)
							CoreUtil
									.sendAjax(
											"produce/online/getClassList",
											"",
											function(data) {
												if (data.result) {
													//console.log(data)
													$("#classId").empty();
													var da = data.data;
													for (var i = 0; i < da.length; i++) {
														if (i == 0) {
															$("#classId").append(
																			"<option value=''>请点击选择</option>");
														}
														$("#classId").append(
																		"<option value="
																				+ da[i].ID
																				+ ">"
																				+ da[i].CLASS_NAME
																				+ "</option>");
														if (da[i].ID == id) {
															$("#classId").val(da[i].ID);
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
					});
});
// 新增编辑弹出框
function openOnlineStaff(id, title) {
	if (id == null || id == "") {
		$("#id").val("");
	}

	var index = layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : false,// 是否点击遮罩关闭
		area : [ '1000px' ],
		content : $('#setOnlineStaff'),
		macmin : true,// 弹出框全屏
		end : function() {
			cleanOnlineStaff();
		}
	});
	layer.full(index);// 弹出框全屏
}
// 重新加载表格（搜索）
function load(obj) {
	console.log(obj)
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
function editMain(obj){
	CoreUtil.sendAjax("produce/online/editMain", JSON.stringify(obj.field),
			function(data) {
				if (data.result) {
					layer.alert("操作成功", function() {
						layer.closeAll();
						cleanOnlineStaff();
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

//删除副表
function delVice(obj,  name) {
	if (id != null) {
		var param = {
			"taskNo":obj.TASK_NO ,
			"devId":obj.DEV_ID ,
			"empId":obj.EMP_ID ,
			"beginTime":obj.TIME_BEGIN
		};
		layer.confirm('您确定要删除上线人员 ' + name + ' 吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("produce/online/deleteVice",
					JSON.stringify(param), function(data) {
						if (isLogin(data)) {
							if (data.result == true) {
								// 回调弹框
								layer.alert("删除成功！", function() {
									tableEmp.reload({
										page : {
											curr : pageCurr
										// 从当前页码开始
										}
									});
								});
							} else {
								layer.alert(data);
							}
						}
					});
		});
	}
}
//删除主表记录
function delOnlineStaff(obj, id, name){
	if (id != null) {
		var param = {
			"id":id 
		};
		layer.confirm('您确定要删除制令单 ' + name + ' 的记录吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("produce/online/deleteMain",
					JSON.stringify(param), function(data) {
						if (isLogin(data)) {
							if (data.result == true) {
								// 回调弹框
								layer.alert("删除成功！", function() {
									layer.closeAll();
									loadAll()
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
function cleanOnlineStaff() {
	$('#onlineForm')[0].reset();
	layui.form.render();// 必须写
}