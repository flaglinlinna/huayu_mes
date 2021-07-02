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
							'layer' ,'tableFilter'],
					function() {
						var form = layui.form, layer = layui.layer, laydate = layui.laydate, table = layui.table, tableSelect = layui.tableSelect,tableFilter = layui.tableFilter;
						form.render();
						tableIns = table.render({
							elem : '#colTable',
							// ,url:context+'/interfaces/getRequestList'
							where : {},
							method : 'get',// 默认：get请求
							defaultToolbar : [],
							height : 'full-70'// 固定表头&full-查询框高度
							,
							even : true,// 条纹样式
							page : true,
							limit : 200,
							limits : [ 200, 500, 800,1000, 5000 ],
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
							cols : [ [ {fixed:'left',
								type : 'numbers'
							},

							// A.TASK_NO TASK_NO_ON, --新制令单
							// A.SIGN_DATE || ' ' || A.SIGN_TIME TIME_ON,--上线时间
							// C.LINE_NAME LINE_nam_ON,--上线线体
							// E.LINER_NAME LINER_ON,--上线组长
							// a.hour_type hour_type_on,--上线工时类型
							// G.Class_Name class_on,--上线班次
							// B.TASK_NO TASK_NO_OFF,--原制令单
							// B.SIGN_DATE || ' ' || B.SIGN_TIME TIME_OFF,--下线时间
							// d.line_name line_nam_off,--下线线体
							// f.LINER_NAME LINER_off,--下线组长
							// b.hour_type hour_type_off,--下线工时类型
							// H.Class_Name class_OFF--下线班次
							{fixed:'left',
								field : 'TASK_NO_ON',
								title : '新制令单',
								width : 150,
								sort : true
							}, {fixed:'left',
								field : 'TASK_NO_OFF',
								title : '原制令单',
								width : 150,
								sort : true
							}, {
								field : 'TIME_ON',
								title : '上线时间',
								width : 145,
								sort : true
							}, {
								field : 'TIME_OFF',
								title : '下线时间',
								width : 145,
								sort : true
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
								width : 110
							}, {
								field : 'HOUR_TYPE_OFF',
								title : '下线工时类型',
								width : 110
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

						localtableFilterIns = tableFilter.render({
							'elem' : '#colTable',
							'mode' : 'local',//本地过滤
							'filters' : [
								{field: 'TASK_NO_ON', type:'checkbox'},
								{field: 'TASK_NO_OFF', type:'checkbox'},
								{field: 'LINE_NAM_ON', type:'checkbox'},
								{field: 'LINE_NAM_OFF', type:'checkbox'},
								{field: 'LINER_ON', type:'checkbox'},
								{field: 'LINER_OFF', type:'checkbox'},
							],
							'done': function(filters){
							}
						})

						empTableIns = table.render({
							elem : '#empTable',
							// ,url:context+'/interfaces/getRequestList'
							where : {},
							method : 'get',// 默认：get请求
							defaultToolbar : [],
							height : 'full-70'// 固定表头&full-查询框高度
							,
							even : true,// 条纹样式
							page : true,
							limit : 200,
							limits : [ 200, 500, 800, 1000, 5000 ],
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
							},  {
								field : 'EMP_CODE',
								title : '工号',
								width : 100,
								sort : true
							}, {
								field : 'EMP_NAME',
								title : '姓名',
								width : 100,
								sort : true
							}, {field : 'TIME_BEGIN', title : '上线时间', width : 145,sort : true},
								{field : 'TIME_BEGIN_HD', title : '统一上线时间', width : 145,sort : true},
								{field : 'TIME_END', title : '下线时间', width : 145,sort : true},
								{field : 'TIME_END_HD', title : '统一下线时间',width : 145, sort : true},
								{
									field : 'EMP_ID',
									title : 'ID',
									width : 80
								}] ],
							done : function(res, curr, count) {
								localtableFilterIns.reload();
								pageCurr = curr;
							}
						});

						localtableFilterIns = tableFilter.render({
							'elem' : '#empTable',
							'mode' : 'local',//本地过滤
							'filters' : [
								{field: 'EMP_CODE', type:'input'},
								{field: 'EMP_NAME', type:'input'},
								{field: 'TIME_BEGIN_HD', type:'checkbox'},
								{field: 'TIME_END_HD', type:'checkbox'},
							],
							'done': function(filters){
							}
						})

						// 按钮监听事件
						form.on('submit(saveData)', function(data) {
							var checkStatus = table.checkStatus('empTable');
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
								width:950,
								url : context
										+ '/produce/switch_staff/getTaskNo',
								method : 'get',
								cols : [ [ {fixed:'left',
									type : 'radio'
								},// 多选 radio
								 {fixed:'left',field : 'id', title : 'id', width : 0, hide : true},
									{fixed:'left',field : 'WORK_DATE', title : '统一上线日期',templet:function (d) {
											if(d.WORK_DATE!=null){
												return d.WORK_DATE.slice(0,4)+"-"+ d.WORK_DATE.slice(5,7)+"-"+d.WORK_DATE.slice(8,10)
											}
										}, width : 110},
									{fixed:'left',field : 'LINER_NAME', title : '组长', width : 70},
									{fixed:'left',field : 'ITEM_NO', title : '物料编码', width : 145},
									{field : 'TASK_NO', title : '制令单号', width : 150},
									{field : 'LINE_NAME', title : '线体', width : 80},
									{field : 'HOUR_TYPE', title : '工时类型', width : 80},
									{field : 'CLASS_NAME', title : '班次', width : 60},
									{field : 'WS_SECTION', title : '工段', width : 60},
									{field : 'FMEMO', title : '备注', width : 80},
									{field : 'CUST_NAME_S', title : '客户', width : 80},
									{field : 'ITEM_NAME', title : '物料描述', width : 160}
									 ] ],
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
								width:750,
								url : context
										+ '/produce/switch_staff/getNewTaskNo',
								method : 'get',
								cols : [ [ {fixed:'left',
									type : 'radio'
								},// 多选 radio
								{fixed:'left',field : 'id', title : 'id', width : 0, hide : true},
								{fixed:'left',field : 'LINER_NAME', title : '组长', width : 70},
								{fixed:'left',field : 'ITEM_NO', title : '物料编码', width : 145},
								{field : 'TASK_NO', title : '制令单号', width : 150},
								{field : 'WS_SECTION', title : '工段', width : 60},
								{field : 'FMEMO', title : '备注', width : 80},
								{field : 'CUST_NAME_S', title : '客户', width : 80},
								{field : 'ITEM_NAME', title : '物料描述', width : 250}
									] ],
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
							elem : '#lastDateEnd',
							type : 'datetime', // 默认，可不填
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

	empTableIns.reload({
		url : context + '/produce/switch_staff/getTaskNoEmp',
		where : params,
		done : function(res1, curr, count) {
			// console.log(res1)
			localtableFilterIns.reload();
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
		title : '在线调整',
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
			localtableFilterIns.reload();
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
		"lastTaskNo":obj.lastTaskNo,
		"lastTaskNo_id" : obj.AFF_ID,
		"lastDatetimeEnd" : obj.lastDateEnd,
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
			localtableFilterIns.reload();
			pageCurr = curr;
		}
	})
}

function clean() {
	// console.log($('#itemForm')[0])
	$('#itemForm')[0].reset();
	empTableIns.reload({
		url : '',// url置空重置数据表
		where : '',
		done : function(res1, curr, count) {
			// console.log(curr)
			localtableFilterIns.reload();
			pageCurr = curr;
		}
	})
	layui.form.render();// 必须写
}
function cleanInput() {
	$("#lastTaskNo").val("")
	$("#lastDateEnd").val("")
	$("#AFF_ID").val("")
	$("#newTaskNo").val("")
	$("#new_itemcode").val("")
	$("#new_liner").val("")
	$("#new_customer").val("")
	$("#newHourType").val("")
	$("#newClassId").val("")
	$("#newLineId").val("")
	$("#newTimeBegin").val("")

	empTableIns.reload({
		url : '',// url置空重置数据表
		where : '',
		done : function(res1, curr, count) {
			// console.log(curr)
			localtableFilterIns.reload();
			pageCurr = curr;
		}
	})
	layui.form.render();// 必须写
}
