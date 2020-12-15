/**
 * 在线人员调整
 */
var pageCurr;
var tabledata = [];
$(function() {
	getLineList();
	getClassList();
	layui
			.use(
					[ 'table', 'form', 'layedit', 'tableSelect', 'laydate',
							'layer' ],
					function() {
						var form = layui.form, layer = layui.layer, laydate = layui.laydate, table = layui.table, tableSelect = layui.tableSelect;
						tableIns = table.render({
							elem : '#colTable',
							// ,url:context+'/interfaces/getRequestList'
							where : {},
							method : 'get',// 默认：get请求
							defaultToolbar : [],
							height : 'full-140'// 固定表头&full-查询框高度
							,
							even : true,// 条纹样式
							page : true,
							limit : 50,
							limits : [ 50, 100, 200, 500, 1000, 5000 ],
							data : [],
							request : {
								pageName : 'page' // 页码的参数名称，默认：page
								,
								limitName : 'rows' // 每页数据量的参数名，默认：limit
							},
							parseData : function(res) {
								 console.log(res)
								if (!res.result) {
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
								field : 'TASK_NO_ON',
								title : '新制令单',
								width : 150,
								sort : true
							}, {
								field : 'TASK_NO_OFF',
								title : '原制令单',
								width : 150
							}, {
								field : 'TIME_ON',
								title : '上线时间',
								width : 150,
								sort : true
							}, {
								field : 'TIME_OFF',
								title : '下线时间',
								width : 150
							}, {
								field : 'LINE_NAM_ON',
								title : '上线线体',
								width : 100
							}, {
								field : 'LINE_NAM_OFF',
								title : '下线线体',
								width : 100
							}, {
								field : 'LINER_ON',
								title : '上线组长',
								width : 100
							}, {
								field : 'LINER_OFF',
								title : '下线组长',
								width : 100
							}, {
								field : 'HOUR_TYPE_ON',
								title : '上线工时类型',
								width : 120
							}, {
								field : 'HOUR_TYPE_OFF',
								title : '下线工时类型',
								width : 120
							}, {
								field : 'CLASS_ON',
								title : '上线班次',
								width : 80
							}, {
								field : 'CLASS_OFF',
								title : '下线班次',
								width : 80
							} ] ],
							done : function(res, curr, count) {
								pageCurr = curr;
							}
						});

						listTable1Ins = table.render({
							elem : '#listTable1',
							// ,url:context+'/interfaces/getRequestList'
							where : {},
							method : 'get',// 默认：get请求
							defaultToolbar : [],
							height : 'full-320'// 固定表头&full-查询框高度
							,
							even : true,// 条纹样式
							page : true,
							limit : 50,
							limits : [ 50, 100, 200, 500, 1000, 5000 ],
							data : [],
							request : {
								pageName : 'page' // 页码的参数名称，默认：page
								,
								limitName : 'rows' // 每页数据量的参数名，默认：limit
							},
							parseData : function(res) {
								// console.log(res)
								if (!res.result) {
									return {
										"count" : 0,
										"msg" : res.msg,
										"data" : [],
										"code" : res.status
									}
								}
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
								type : "checkbox"
							}, {
								field : 'EMP_ID',
								title : '零件名称',
								width : 120
							}, {
								field : 'EMP_CODE',
								title : '工序顺序',
								width : 120
							}, {
								field : 'EMP_NAME',
								title : '工序名称',
								width : 120
							}, {
								field : 'TIME_BEGIN',
								title : '工作中心',
								width : 120

							}, {
								field : 'TIME_END',
								title : '工序说明',
								width : 150
							}, {
								field : 'TIME_BEGIN',
								title : '机台类型',
								width : 120

							}, {
								field : 'TIME_END',
								title : '基数',
								width : 90
							}, {
								field : 'TIME_BEGIN',
								title : '人数',
								width : 90

							}, {
								field : 'TIME_END',
								title : '成型周期',
								width : 100
							} ] ],
							done : function(res, curr, count) {
								pageCurr = curr;
							}
						});

						// 按钮监听事件
						form.on('submit(saveData)', function(data) {
							var checkStatus = table.checkStatus('listTable1');
							if (checkStatus.data.length < 1) {
								layer.alert("请至少选择一位员工")
								return false;
							}
							var empIdList = '';
							var da = checkStatus.data;// console.log(checkStatus.data)
							// //获取选中行的数据
							for (var i = 0; i < checkStatus.data.length; i++) {
								empIdList += da[i].AFF_DET_ID + ','
							}
							empIdList = empIdList.substring(0,
									empIdList.length - 1)

							//console.log(data.field)
							  saveData(data.field, empIdList);

						});

						form.on('radio(switchType)', function(data) {
							var value = data.value;
							console.log(value)
							if (value == "下线") {
								$("#switchTask").hide();
								cleanInput()
							} else {
								$("#switchTask").show();
								cleanInput()
							}

						});

						form.on('submit(searchSubmit)', function(data) {
							// 重新加载table
							getList(data.field.dates, data.field.keyword)
							return false;
						});

						tableSelect1 = tableSelect.render({
							elem : '#lastTaskNo',
							searchKey : 'keyword',
							checkedKey : 'id',
							searchPlaceholder : '试着搜索',
							table : {
								url : context
										+ '/produce/switch_staff/getTaskNo',
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
									field : 'TASK_NO',
									title : '制令单号',
									width : 150
								}, {
									field : 'ITEM_NAME',
									title : '物料描述',
									width : 250
								}, {
									field : 'ITEM_NO',
									title : '物料编码',
									width : 170
								}, {
									field : 'LINER_NAME',
									title : '组长',
									width : 75
								}, {
									field : 'CUST_NAME_S',
									title : '客户',
									width : 100
								}, {
									field : 'WORK_DATE',
									title : '生产时间',
									width : 150
								} ] ],
								parseData : function(res) {
									// console.log(res)
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
								// 选择完后的回调，包含2个返回值
								// elem:返回之前input对象；data:表格返回的选中的数据 []
								var da = data.data;
								// console.log(da[0].num)
								form.val("itemForm", {
									"lastTaskNo" : da[0].TASK_NO,
									"AFF_ID" : da[0].AFF_ID
								});
								form.render();// 重新渲染
								getEmpList(da[0].AFF_ID);
							}
						});
						sTableSelect = tableSelect.render({
							elem : '#newTaskNo',
							searchKey : 'keyword',
							checkedKey : 'id',
							searchPlaceholder : '试着搜索',
							table : {
								url : context
										+ '/produce/switch_staff/getNewTaskNo',
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
									field : 'LINER_NAME',
									title : '组长',
									width : 75
								}, {
									field : 'TASK_NO',
									title : '制令单号',
									width : 150
								}, {
									field : 'ITEM_NAME',
									title : '物料描述',
									width : 250
								}, {
									field : 'ITEM_NO',
									title : '物料编码',
									width : 170
								}, {
									field : 'CUST_NAME_S',
									title : '客户',
									width : 100
								} ] ],
								parseData : function(res) {
									// console.log(res)
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
								// 选择完后的回调，包含2个返回值
								// elem:返回之前input对象；data:表格返回的选中的数据 []
								var da = data.data;
								// console.log(da[0].num)
								form.val("itemForm", {
									"newTaskNo" : da[0].TASK_NO,
									"new_itemcode" : da[0].ITEM_NO,
									"new_liner" : da[0].LINER_NAME,
									"new_customer" : da[0].CUST_NAME_S,

								});
								form.render();// 重新渲染
							}
						});
						// 日期选择器
						laydate.render({
							elem : '#dates',
							range: true,
							trigger : 'click'
						});
						// 日期选择器
						laydate.render({
							elem : '#finishDate',
							type : 'date', // 默认，可不填
						});
						laydate.render({
							elem : '#newTimeBegin',
							type : 'datetime', // 默认，可不填
							trigger : 'click' // 采用click弹出
						});
					});

	function getLineList() {
		CoreUtil.sendAjax("/produce/switch_staff/getLine", "", function(data) {
			// console.log(data)
			if (data.result) {
				$("#newLineId").empty();
				var datalist = data.data;
				for (var i = 0; i < datalist.length; i++) {
					if (i == 0) {
						$("#newLineId")
								.append("<option value=''> 请选择</option>");
					}
					$("#newLineId").append(
							"<option value=" + datalist[i].ID + ">"
									+ datalist[i].LINER_NAME + "——"
									+ datalist[i].LINE_NAME + "</option>");

				}
				// layui.form.render('select');
			} else {
				layer.alert(data.msg);
			}
		}, "GET", false, function(res) {
			layer.alert(res.msg);
		});

	}
	function getClassList() {
		// 获取班次
		CoreUtil.sendAjax("/produce/switch_staff/getClassType", "", function(
				data) {
			// console.log(data)
			if (data.result) {
				$("#newClassId").empty();
				var datalist = data.data;
				for (var i = 0; i < datalist.length; i++) {
					if (i == 0) {
						$("#newClassId").append(
								"<option value=''> 请选择</option>");
					}
					$("#newClassId").append(
							"<option value=" + datalist[i].id + ">"
									+ datalist[i].className + "</option>");
				}
				layui.form.render('select');
			} else {
				layer.alert(data.msg);
			}
		}, "GET", false, function(res) {
			layer.alert(res.msg);
		});
	}
	
});

function getEmpList(aff_id) {
	var params = {
		"aff_id" : aff_id
	}

	listTable1Ins.reload({
		url : context + '/produce/switch_staff/getTaskNoEmp',
		where : params,
		done : function(res1, curr, count) {
			// console.log(res1)
			pageCurr = curr;
		}
	})
}

function add() {
	// 清空弹出框数据
	// clean();
	// 打开弹出框
	open();
}

function open() {
	var index = layer.open({
		type : 1,
		title : '华誉精密产品报价需求单',
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#setTask'),
		end : function() {
			$("#switchTask").show();
			clean();
		}
	});
	layer.full(index);
}

function getList(dates, keyword) {
	var params = {
		"dates" : dates,
		"keyword" : keyword,
	}
	tableIns.reload({
		url : context + '/produce/switch_staff/getList',
		where : params,
		done : function(res1, curr, count) {
			pageCurr = curr;
		}
	})
	// localtableFilterIns.reload();
}

function saveData(obj, empIdList) {
	//console.log(obj)
	if (obj.switchType == "转单") {
		if (obj.newTaskNo == "" || obj.newLineId == "" || obj.newHourType == ""
				|| obj.newClassId == "" || obj.newTimeBegin == "") {
			layer.alert("请填写必需信息")
			return false;
		}
	}

	var params = {
		"lastTaskNo_id" : obj.AFF_ID,
		"lastDatetimeEnd" : obj.finishDate,
		"newTaskNo" : obj.newTaskNo,
		"newLineId" : obj.newLineId,
		"newHourType" : obj.newHourType,
		"newClassId" : obj.newClassId,
		"newDatetimeBegin" : obj.newTimeBegin,
		"empList" : empIdList,
		"switchType" : obj.switchType
	}
	console.log(params)
	tableIns.reload({
		url : context + '/produce/switch_staff/doSwitch',
		where : params,
		done : function(res1, curr, count) {

			console.log(res1)
			if (res1.code == "1") {
				layer.alert(res1.msg)
			} else {			
				layer.alert("调整成功")
				getEmpList(obj.AFF_ID)
			}
			pageCurr = curr;
		}
	})
}

function clean() {
	// console.log($('#itemForm')[0])
	$('#itemForm')[0].reset();
	listTable1Ins.reload({
		url : '',// url置空重置数据表
		where : '',
		done : function(res1, curr, count) {
			// console.log(curr)
			pageCurr = curr;
		}
	})
	layui.form.render();// 必须写
}
function cleanInput() {
	$("#lastTaskNo").val("")
	$("#finishDate").val("")
	$("#AFF_ID").val("")
	$("#newTaskNo").val("")
	$("#new_itemcode").val("")
	$("#new_liner").val("")
	$("#new_customer").val("")
	$("#newHourType").val("")
	$("#newClassId").val("")
	$("#newLineId").val("")
	$("#newTimeBegin").val("")

	listTable1Ins.reload({
		url : '',// url置空重置数据表
		where : '',
		done : function(res1, curr, count) {
			// console.log(curr)
			pageCurr = curr;
		}
	})
	layui.form.render();// 必须写
}
