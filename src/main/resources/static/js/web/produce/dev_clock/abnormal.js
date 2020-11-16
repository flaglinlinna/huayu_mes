/**
 * 异常工时数据信息管理
 */
var pageCurr;
$(function() {
	layui
			.use(
					[ 'form', 'tableSelect', 'table', 'laydate' ],
					function() {
						var table = layui.table, form = layui.form, tableSelect = layui.tableSelect, laydate = layui.laydate;
						tableIns = table
								.render({
									elem : '#cardList',
									url : context + '/produce/abnormal/getList',
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
										if(res.status == 1){
								              return {
								                     "code": res.status,//code值为200表示成功
								                       "count": 0,
								                       "msg":res.msg,
								                       "data":[]
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
									cols : [ [
											{
												type : 'numbers'
											}
											// ,{field:'id', title:'ID',
											// width:80, unresize:true,
											// sort:true}
											,
											{
												field : 'EMP_CODE',
												title : '员工工号',
												width : 80
											},
											{
												field : 'EMP_NAME',
												title : '员工姓名',
												width : 100
											},
											{
												field : 'TIME_BEGIN',
												title : '开始时间',
												width : 150
											},
											{
												field : 'TIME_END',
												title : '结束时间',
												width : 150
											},
											{
												field : 'DURATION',
												title : '时长',
												width : 90
											},
											{
												field : 'DESCRIPTION',
												title : '异常描述',
												width : 200
											},
											{
												field : 'FOR_REASON',
												title : '异常原因',
												width : 200
											},
											{
												field : 'LINER_NAME',
												title : '线长',
												width : 90
											},
											{
												field : 'TASK_NO',
												title : '制令单',
												width : 350
											},
											{
												field : 'ITEM_NO',
												title : '物料编号',
												width : 150
											},
											,
											{
												field : 'ITEM_NAME',
												title : '物料描述',
												width : 350
											},
											
											{
												field : 'CUST_NAME_S',
												title : '客户名称',
												width : 90
											},
											 {
												fixed : 'right',
												title : '操作',
												align : 'center',
												toolbar : '#optBar',
												width : 120
											} ] ],
									done : function(res, curr, count) {
										// 如果是异步请求数据方式，res即为你接口返回的信息。
										// 如果是直接赋值的方式，res即为：{data: [], count:
										// 99} data为当前页数据、count为数据总长度
										pageCurr = curr;
									}
								});

						empTableSelect = tableSelect.render({// 返工历史-制令单
							elem : '#empCode',
							searchKey : 'keyword',
							checkedKey : 'id',
							searchPlaceholder : '试着搜索',
							table : {
								url : context + '/produce/abnormal/getEmpInfo',
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
									title : '工号',
									width : 100
								}, {
									field : 'EMP_NAME',
									title : '姓名',
									width : 100
								} ] ],
								parseData : function(res) {
									console.log(res)
									if (res.result) {
										if(res.status == 1){
								              return {
								                     "code": res.status,//code值为200表示成功
								                       "count": 0,
								                       "msg":res.msg,
								                       "data":[]
								                   }
								             }
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
								// console.log(data)
								var da = data.data;
								// $("#empId").val(da[0].id)
								form.val("cardForm", {
									"empId" : da[0].ID,
									"empCode" : da[0].EMP_CODE,
									"empName" : da[0].EMP_NAME,
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
								url : context + '/produce/abnormal/getTaskNo',
								method : 'get',
								cols : [ [ {
									type : 'radio'
								},// 多选 radio
								{
									field : 'TASK_NO',
									title : '制令单号',
									width : 400
								}, {
									field : 'CUST_NAME_S',
									title : '客户简称',
									width : 80
								}, {
									field : 'ITEM_NAME',
									title : '物料描述',
									width : 200
								}, {
									field : 'ITEM_NO',
									title : '物料编号',
									width : 150
								}, {
									field : 'LINER_NAME',
									title : '线长',
									width : 100
								} ] ],
								parseData : function(res) {	
									if (res.result) {
										if(res.status == 1){
								              return {
								                     "code": res.status,//code值为200表示成功
								                       "count": 0,
								                       "msg":res.msg,
								                       "data":[]
								                   }
								             }
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
								// console.log(data)
								var da = data.data;
								form.val("cardForm", {
									"taskNo" : da[0].TASK_NO,
									"itemName" : da[0].ITEM_NAME,
									"itemNo" : da[0].ITEM_NO,
									"custNameS" : da[0].CUST_NAME_S,
									"linerName" : da[0].LINER_NAME
								});
								form.render();// 重新渲染
							}
						});

						getReasonSelect("");

						// 日期选择器
						laydate.render({
							elem : '#timeBegin',
							trigger : 'click',
							type : 'datetime', // 默认，可不填
							done : function(value, date, endDate) {
								// 年月日时间被切换时都会触发。回调返回三个参数，分别代表：生成的值、日期时间对象、结束的日期时间对象
								$('#timeBegin').change();
								// console.log(value); // 得到日期生成的值，如：2017-08-18
								setTimeout(function() {
									if ($("#timeEnd").val() != "") {
										//alert("begin")
										calTime();
									}
								}, 150);
							}
						});
						laydate.render({
							elem : '#timeEnd',
							trigger : 'click',
							type : 'datetime', // 默认，可不填
							done : function(value, date, endDate) {
								// 年月日时间被切换时都会触发。回调返回三个参数，分别代表：生成的值、日期时间对象、结束的日期时间对象
								$('#timeEnd').change();
								// console.log(value); // 得到日期生成的值，如：2017-08-18
								setTimeout(function() {
									if ($("#timeBegin").val() != "") {
										//alert("end")
										calTime();
									}
								}, 150);
							}
						});
						// 监听工具条
						table.on('tool(cardTable)', function(obj) {
							var data = obj.data;
							if (obj.event === 'del') {
								// 删除
								delAbnormalHours(data, data.ID, data.EMP_NAME);
							} else if (obj.event === 'edit') {
								// 编辑
								console.log(data)
								getAbnormalHours(data, data.ID);
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
							setStatus(obj, this.value, this.name,
									obj.elem.checked);
						});

						// 监听提交
						form.on('submit(addSubmit)', function(data) {
							if (data.field.id == null || data.field.id == "") {
								// 新增
								// console.log(data)
								addSubmit(data);
							} else {
								editSubmit(data);
							}
							return false;
						});

						// 监听异常工时查询
						form.on('submit(searchDev)', function(data) {
							loadDev(data.field.keywordDev);
							return false;
						});
						// 编辑异常工时信息提交
						function editSubmit(obj) {
							CoreUtil.sendAjax("/produce/abnormal/edit", JSON
									.stringify(obj.field), function(data) {
								if (data.result) {
									layer.alert("操作成功", function() {
										layer.closeAll();
										cleanAbnormalHours();
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
						function getAbnormalHours(obj, id) {
							var param = {
								"id" : id
							};
							CoreUtil
									.sendAjax(
											"/produce/abnormal/getAbnormalHours",
											JSON.stringify(param),
											function(data) {
												 console.log(data)
												if (data.result) {
													form
															.val(
																	"cardForm",
																	{
																		"id" : data.data.id,
																		"empId" : data.data.empId,
																		"empCode" : data.data.employee.empCode,
																		"empName" : data.data.employee.empName,
																		"taskNo" : data.data.taskNo,
																		"description" : data.data.description,
																		"duration" : data.data.duration,
																		"forReason" : data.data.forReason,
																		"timeBegin" : data.data.timeBegin,
																		"timeEnd" : data.data.timeEnd,

																	});
													$('#empCode').attr(
															"disabled",
															"disabled");
													getTaskInfo(data.data.taskNo);
													getReasonSelect(data.data.forReason);
													openAbnormalHours(id,
															"编辑异常工时信息")
												} else {
													layer.alert(data.msg)
												}
											}, "POST", false, function(res) {
												layer.alert("操作请求错误，请您稍后再试");
											});
						}
						function getTaskInfo(taskNo) {
							// console.log(taskNo)
							var params = {
								"taskNo" : taskNo
							};
							CoreUtil
									.sendAjax(
											"/produce/abnormal/getTaskNoInfo",
											JSON.stringify(params),
											function(data) {
												//console.log(data)
												var da=data.data
												console.log(da)
												if (data.result) {
													form
															.val(
																	"cardForm",
																	{
																		"itemName" : da[0].ITEM_NAME,
																		"itemNo" : da[0].ITEM_NO,
																		"custNameS" : da[0].CUST_NAME_S,
																		"linerName" : da[0].LINER_NAME,
																	});
												} else {
													layer.alert(data.msg)
												}
											}, "POST", false, function(res) {
												layer.alert("操作请求错误，请您稍后再试");
											});
						}
					});

});
// 添加异常工时数据信息
function addAbnormalHours() {
	// 清空弹出框数据
	cleanAbnormalHours();
	// 打开弹出框
	$('#empCode').removeAttr("disabled");
	openAbnormalHours(null, "添加异常工时数据信息");
}

function getReasonSelect(editReason) {
	CoreUtil.sendAjax("/base/abnormal/getList", "", function(data) {
		console.log(data)
		if (data.result) {
				$("#forReason").empty();
				var forReason = data.data.rows;
				for (var i = 0; i < forReason.length; i++) {
					if(forReason[i].abnormalType ==editReason) {
						$("#forReason").append(
							"<option value=" + forReason[i].ID + "  selected='selected'>"
							+ forReason[i].ERR_NAME + "</option>");
					}else{
						$("#forReason").append(
								"<option value=" + forReason[i].ID + ">"
								+ forReason[i].ERR_NAME + "</option>");
					}
				}

				layui.form.render('select');
		} else {
			layer.alert(data.msg);
		}
	}, "GET", false, function(res) {
		layer.alert(res.msg);
	});
	return false;
}
// 新增编辑弹出框
function openAbnormalHours(id, title) {
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
		content : $('#setAbnormalHours'),
		end : function() {
			cleanAbnormalHours();
		}
	});
	layer.full(index);
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

// 新增异常工时数据信息提交
function addSubmit(obj) {
	CoreUtil.sendAjax("/produce/abnormal/add", JSON.stringify(obj.field),
			function(data) {
				if (data.result) {
					layer.alert("操作成功", function() {
						layer.closeAll();
						cleanAbnormalHours();
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

function delAbnormalHours(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除' + name + '的异常工时数据吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/produce/abnormal/delete", JSON.stringify(param),
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
								layer.alert(data);
							}
						}
					});
		});
	}
}
function calTime() { // 计算开始与结束的时长
	var start = $("#timeBegin").val();
	var end = $("#timeEnd").val();
	
	var timeBegin=new Date(start);
	var timeEnd =new Date(end);
	
	//var timeBegin = new Date(start.replace(/-/g, "-"));
	//var timeEnd = new Date(end.replace(/-/g, "-"));

	var diff = (timeEnd.getTime() - timeBegin.getTime()) / (1000 * 60);// 单位 分钟
	console.log(diff)
	if (diff < 0) {
		layer.alert("请选择或输入正确的开始/结束时间");
		$("#duration").val("");
		return false;
	}
	$("#duration").val(diff.toFixed(2));
	
	/*
	var chkStart=checkDatetime(start);
	var chkEnd=checkDatetime(end);
	
	console.log(chkStart);
	console.log(chkEnd);
	if(chkStart&&chkEnd){
		
	}else{
		layer.alert("输入的日期格式不正确，请检查！")
		return false;
	}*/
}

function checkDatetime(str){
	var reg = /^(\d+)-(\d{1,2})-(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/;
	var r = str.match(reg);
	if(r==null)return false;
	r[2]=r[2]-1;
	var d= new Date(r[1], r[2],r[3], r[4],r[5], r[6]);
	if(d.getFullYear()!=r[1])return false;
	if(d.getMonth()!=r[2])return false;
	if(d.getDate()!=r[3])return false;
	if(d.getHours()!=r[4])return false;
	if(d.getMinutes()!=r[5])return false;
	if(d.getSeconds()!=r[6])return false;
	return true;
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
function cleanAbnormalHours() {
	$('#cardForm')[0].reset();
	layui.form.render();// 必须写
}
