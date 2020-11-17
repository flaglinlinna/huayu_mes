/**
 * 生产异常
 */
var pageCurr;
$(function() {
	layui
			.use(
					[ 'form', 'tableSelect', 'table', 'laydate' ],
					function() {
						var table = layui.table, form = layui.form, tableSelect = layui.tableSelect, laydate = layui.laydate;

						tableIns = table.render({// 历史
							elem : '#cardList',
							where : {},
							method : 'get',// 默认：get请求
							defaultToolbar : [],
							page : true,
							url : context + '/abnormalProduct/getList',
							height:'full-80'//固定表头&full-查询框高度
							,even:true,//条纹样式
							request : {
								pageName : 'page', // 页码的参数名称，默认：page
								limitName : 'rows' // 每页数据量的参数名，默认：limit
							},
							parseData : function(res) {// 可进行数据操作
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
										},{
											field : 'TASK_NO',
											title : '制令单',
											width : 200
										},{
											field : 'FTIME',
											title : '登记时间',
											width : 150
										},{
											field : 'FTYPE',
											title : '登记类型',
											width : 90
										},{
											field : 'FOR_REASON',
											title : '异常原因',
											width : 200
										},{
											field : 'FTIME_LONG',
											title : '异常时长',
											width : 90
										},{
											field : 'ITEM_NO',
											title : '物料编号',
											width : 150
										},{
											field : 'ITEM_NAME',
											title : '物料描述',
											width : 350
										},{
											field : 'CUST_NAME',
											title : '客户名称',
											width : 90
										},{
											field : 'DESCRIPTION',
											title : '异常描述',
											width : 200
										}, {
											fixed : 'right',
											title : '操作',
											align : 'center',
											toolbar : '#optBar',
											width : 120
										} ] ],
							done : function(res, curr, count) {
								pageCurr = curr;
							}
						});
						taskTableSelect = tableSelect.render({// 返工历史-制令单
							elem : '#taskNo',
							searchKey : 'keyword',
							checkedKey : 'id',
							searchPlaceholder : '试着搜索',
							table : {
								url : context + '/abnormalProduct/getTaskNo',
								method : 'get',
								cols : [ [ {
									type : 'radio'
								},// 多选 radio
								{
									field : 'TASK_NO',
									title : '制令单号',
									width : 280
								}, {
									field : 'CUST_NAME_S',
									title : '客户简称',
									width : 180
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

						

						// 日期选择器
						laydate.render({
							elem : '#ftime',
							trigger : 'click',
							type : 'datetime', // 默认，可不填
							done : function(value, date, endDate) {
							}
						});
						// 监听工具条
						table.on('tool(cardTable)', function(obj) {
							var data = obj.data;
							if (obj.event === 'del') {
								// 删除
								del(data.ID);
							} else if (obj.event === 'edit') {
								// 编辑
								edite(data);
							}
						});
						
						form.on('select(ftype)', function(data) {
							if(data.value == '解除'){
								$("#time_label").show();//显示div
								$("#time_div").show();
								$("#ftimeLong").attr("lay-verify","required");
							}else{
								$("#time_label").hide();//显示div
								$("#time_div").hide();
								$("#ftimeLong").attr("lay-verify","");
							}
							form.render('select');//select是固定写法 不是选择器
							return false;
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
							CoreUtil.sendAjax("/abnormalProduct/edit", JSON
									.stringify(obj.field), function(data) {
								if (data.result) {
									layer.alert("操作成功", function() {
										layer.closeAll();
										clean();
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
						function edite(obj) {
							form.val("cardForm",{
										"id" : obj.ID,
										"taskNo" : obj.TASK_NO,
										"description" : obj.DESCRIPTION,
										"forReason" : obj.FOR_REASON,
										"ftime" : obj.FTIME,
										"ftimeLong" : obj.FTIME_LONG,
										"ftype" : obj.FTYPE,
									});
							getTaskInfo(obj.TASK_NO);
							getReasonSelect(obj.FOR_REASON);
							open(id,"编辑")
						}
						function getTaskInfo(taskNo) {
							// console.log(taskNo)
							var params = {
								"taskNo" : taskNo
							};
							CoreUtil.sendAjax(
											"/produce/abnormal/getTaskNoInfo",
											JSON.stringify(params),
											function(data) {
												//console.log(data)
												var da=data.data
												console.log(da)
												if (data.result) {
													form.val(
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
function add() {
	// 清空弹出框数据
	clean();
	getReasonSelect("");
	// 打开弹出框
	open(null, "新增");
}

function getReasonSelect(editReason) {
	CoreUtil.sendAjax("/abnormalProduct/getErrorInfo", "", function(data) {
		if (data.result) {
				$("#forReason").empty();
				var forReason = data.data;
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
function open(id, title) {
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
			clean();
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
	CoreUtil.sendAjax("/abnormalProduct/add", JSON.stringify(obj.field),
			function(data) {
				if (data.result) {
					layer.alert("操作成功", function() {
						layer.closeAll();
						clean();
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

function del(id) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/abnormalProduct/delete", JSON.stringify(param),
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
function clean() {
	$('#cardForm')[0].reset();
	layui.form.render();// 必须写
}
