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
								//console.log(res)
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
							}, {
								field : 'EMP_ID',
								title : 'ID',
								width : 100,
								sort : true
							}, {
								field : 'EMP_CODE',
								title : '工号',
								width : 100,
								sort : true
							}, {
								field : 'EMP_NAME',
								title : '姓名',
								width : 100,
								sort : true
							}, {
								field : 'TIME_BEGIN',
								title : '上线时间',
								width : 200
							}, {
								field : 'TIME_END',
								title : '下线时间',
								width : 200
							}] ],
							done : function(res, curr, count) {
								pageCurr = curr;
							}
						});

						empTableIns = table.render({
							elem : '#empTable',
							// ,url:context+'/interfaces/getRequestList'
							where : {},
							method : 'get',// 默认：get请求
							defaultToolbar : [],
							height : 'full-220'// 固定表头&full-查询框高度
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
								title : 'ID',
								width : 90
							}, {
								field : 'EMP_CODE',
								title : '工号',
								sort : true
							}, {
								field : 'EMP_NAME',
								title : '姓名',

								sort : true
							}, {
								field : 'TIME_BEGIN',
								title : '上线时间',

								sort : true
							}, {
								field : 'TIME_END',
								title : '下线时间',

								sort : true
							} ] ],
							done : function(res, curr, count) {
								pageCurr = curr;
							}
						});

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
							// console.log(empIdList)
							// console.log(data.field)

							saveData(data.field, empIdList);

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
									width : 140
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
									"AFF_ID":da[0].AFF_ID
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
								},  {
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
									width : 140
								},{
									field : 'CUST_NAME_S',
									title : '客户',
									width : 100
								}] ],
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
	function getEmpList(aff_id) {
		var params = {
			"aff_id" : aff_id
		}

		empTableIns.reload({
			url : context + '/produce/switch_staff/getTaskNoEmp',
			where : params,
			done : function(res1, curr, count) {
				//console.log(res1)
				pageCurr = curr;
			}
		})
	}
});

function add() {
	// 清空弹出框数据
	 //clean();
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
			 clean();
		}
	});
	layer.full(index);
}

function saveData(obj, empIdList) {
	 console.log(obj)

	
	var params = {
		"lastTaskNo_id" : obj.AFF_ID,
		"lastDatetimeEnd" : obj.lastDateEnd,
		"newTaskNo" : obj.newTaskNo,
		"newLineId" : obj.newLineId,
		"newHourType" : obj.newHourType,
		"newClassId" : obj.newClassId,
		"newDatetimeBegin" : obj.newTimeBegin,
		"empList" : empIdList
	}
	//console.log(params)
		tableIns.reload({
			url : context + '/produce/switch_staff/doSwitch',
			where : params,
			done : function(res1, curr, count) {
				 
				//console.log(res1)
				if(res1.code=="1"){
					layer.alert(res1.msg)
				}else{
					layer.alert("调整成功",function(){
						layer.closeAll();
						//clean();
					})
					
					
				}
				pageCurr = curr;
			}
		})
	
	/*
	CoreUtil.sendAjax("/produce/switch_staff/doSwitch", JSON.stringify(params),
			function(data) { 
		 console.log(data)
				if (data.result) {
					tableIns.reload({
						data : data.data.rows
					});
				} else {
					layer.alert(data.msg);
				}
			}, "POST", false, function(res) {
				layer.alert(res.msg);
			});*/
}

function clean() {
	//console.log($('#itemForm')[0])
	$('#itemForm')[0].reset();
	empTableIns.reload({
		url : '',//url置空重置数据表
		where : '',
		done : function(res1, curr, count) {
			//console.log(curr)
			pageCurr = curr;
		}
	})
	layui.form.render();// 必须写
}