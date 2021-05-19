/**
 * 系统参数管理
 */
var pageCurr;
var pageCurr1;
var mid;
var index2;
$(function() {
	layui.use([ 'form', 'table' ], function() {
		var table = layui.table, form = layui.form;

		tableIns = table.render({
			elem : '#paramList',
			url : context + '/sysParam/getList',
			method : 'get' // 默认：get请求
			,
			cellMinWidth : 80
			,height:'full-110'//固定表头&full-查询框高度
		    ,even:true//条纹样式
			,page : true,
			limit: 30,
			limits: [30,50,100,200],
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
			},
			// ,{field:'id', title:'ID', width:80, unresize:true, sort:true}
			// , {
			// 	field : 'paramType',
			// 	title : '系统参数类型',
			// 	templet: function(d){
			// 		if(d.paramType=="1"){
			// 			 return  "用户级"
			// 		}else if(d.paramType==0){
			// 			return  "系统级"
			// 		}
			//       }
			// },
				{
				field : 'paramCode',
				title : '参数编码',width:180
			}, {
				field : 'paramName',
				title : '参数名称',width:200
			}, {
				field : 'paramValue',
				title : '参数值',width:150
			},
				{
					field : 'paramSort',
					title : '功能模块',width:150
				},
				{
				field : 'fmemo',
				title : '备注',width:150},
			//  {
			// 	field : 'lastupdateDate',
			// 	title : '更新时间'
			// }, {
			// 	field : 'createDate',
			// 	title : '添加时间',
			// },
				{
				fixed : 'right',
				title : '操作',
					width : 200,
				align : 'center',
				toolbar : '#optBar'
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

		tableIns1 = table.render({
			elem : '#iList',
			method : 'get' // 默认：get请求
			,cellMinWidth : 80
			,even:true,//条纹样式
			data : [],
			//height: 'full',
			page : true,
			request : {
				pageName : 'page' // 页码的参数名称，默认：page
				,
				limitName : 'rows' // 每页数据量的参数名，默认：limit
			},
			parseData : function(res) {
				if(!res.result){
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
					field : 'subCode',
					title : '快码编码',width : 120, sort: true,
				}, {
					field : 'subName',
					title : '快码名称',width : 160, sort: true
				},
				{
					field : 'forder',
					title : '快码顺序',width : 120, sort: true
				},
				// {
				// 	field : 'USER_NAME',
				// 	title : '操作人',
				// 	width : 100, sort: true
				// },
				{
					field : 'createDate',
					title : '创建时间',width : 150, sort: true
				},
				{
					title : '操作',
					align : 'center',
					width : 125,
					toolbar : '#optBar1'
				}

			] ],
			done : function(res, curr, count) {
				pageCurr1 = curr;
			}
		});

		table.on('rowDouble(paramTable)', function(obj){
			data = obj.data;
			mid = data.id;
			openSysParamSub(data.id,data.paramName);
			var param = {
				"id" : data.id
			};
			tableIns1.reload({
				url:context+'/sysParamSub/getList',
				where:param,
				done: function(res1, curr, count){
					pageCurr1=curr;
				}
			})
			loadSub(data.id);
			form.val("paramSubForm", {
				"paramType1" : data.paramType,
				"paramCode1" : data.paramCode,
				"paramName1" : data.paramName,
				"paramSort1" :data.paramSort,
				"paramValue1" : data.paramValue,
				"fmemo1" : data.fmemo
			});
			form.render();

		});

		// 监听工具条
		table.on('tool(paramTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delSysParam(data, data.id, data.paramName);
			} else if (obj.event === 'edit') {
				// 编辑
				getSysParam(data, data.id);
			}else if(obj.event ==='editSub'){
				//子参数管理
				mid = data.id;
				openSysParamSub(data.id,data.paramName);
				var param = {
					"id" : data.id
				};
				tableIns1.reload({
					url:context+'/sysParamSub/getList',
					where:param,
					done: function(res1, curr, count){
						pageCurr1=curr;
					}
				})
				loadSub(data.id);
				form.val("paramSubForm", {
					"paramType1" : data.paramType,
					"paramCode1" : data.paramCode,
					"paramName1" : data.paramName,
					"paramSort1" :data.paramSort,
					"paramValue1" : data.paramValue,
					"fmemo1" : data.fmemo
				});
				form.render();
			}
		});

		// 监听子表工具条
		table.on('tool(iTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delSysParamSub(data, data.id, data.subName);
			} else if (obj.event === 'edit') {
				// 编辑
				getSysParamSub(data, data.id);
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

		// 监听子参数提交
		form.on('submit(addSubmit1)', function(data) {
			if (data.field.id == null || data.field.id == "") {
				// 新增
				addSubSubmit(data);
			} else {
				editSubSubmit(data);
			}
			return false;
		});
		// 监听搜索框
		form.on('submit(searchSubmit)', function(data) {
			// 重新加载table
			load(data);
			return false;
		});

		// 编辑系统子参数
		function getSysParamSub(data, id) {
			form.val("paramSubForm", {
				"id" : data.id,
				"mid" : mid,
				"subCode" : data.subCode,
				"subName" : data.subName,
				"forder" :data.forder
			});
			openSysParamSub(id, "编辑系统子参数");
		}

		// 编辑系统参数
		function getSysParam(obj, id) {
			var param = {
				"id" : id
			};
			CoreUtil.sendAjax("/sysParam/getSysParam", JSON.stringify(param),
					function(data) {
						if (data.result) {
							form.val("paramForm", {
								"id" : data.data.id,
								"paramType" : data.data.paramType,
								"paramCode" : data.data.paramCode,
								"paramName" : data.data.paramName,
								"paramSort" :data.data.paramSort,
								"paramValue" : data.data.paramValue,
								"fmemo" : data.data.fmemo
							});
							openSysParam(id, "编辑系统参数")
						} else {
							layer.alert(data.msg)
						}
					}, "POST", false, function(res) {
						layer.alert("操作请求错误，请您稍后再试");
					});
			tableIns1.reload({
				url:context+'/sysParamSub/getList',
				where:param,
				done: function(res1, curr, count){
					pageCurr1=curr;
				}
			})
		}
	});

});


// 新增编辑弹出框
function openSysParam(id, title) {
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
		content : $('#setSysParam'),
		end : function() {
			cleanSysParam();
			changeButton(1);
			loadAll();
		}
	});
	layer.full(index);
}

// 新增编辑弹出框
function openSysParamSub(id, title) {
	if (id == null || id == "") {
		$("#id").val("");
	}
	index2=layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#setSysParamSub'),
		end : function() {
			cleanSysParamSub();
		}
	});
	layer.full(index2);
}

//新增编辑子参数弹出框
function openSysParamSub(id, title) {
	// if (id == null || id == "") {
	// 	$("#id").val("");
	// }
	var index=layer.open({
		type : 1,
		title : title +" 的参数信息",
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#setSysParamSub'),
		end : function() {
			cleanSysParamSub();
		}
	});
	layer.full(index);
}

// 添加系统参数
function addSysParam() {
	// 清空弹出框数据
	cleanSysParam();
	changeButton(0);
	// 打开弹出框
	openSysParam(null, "添加系统参数");
}

function changeButton(type) {
	if(type==0) {
		$('#subButton').prop("disabled", "disabled");
		$('#subButton').addClass("layui-btn-disabled");
	}else {
		$('#subButton').attr("disabled",false);
		$('#subButton').removeClass("layui-btn-disabled");
	}
}
// 添加系统子参数
function addSysParamSub() {
	// 清空弹出框数据
	cleanSysParamSub();
	// 打开弹出框
	openSysParamSub(null, "添加系统子参数");
}
// 新增系统参数提交
function addSubmit(obj) {
	CoreUtil.sendAjax("/sysParam/add", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			mid = data.data.id;
			// changeButton(1);
			layer.alert("操作成功", function() {
				layer.closeAll();
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

// 编辑系统参数提交
function editSubmit(obj) {
	CoreUtil.sendAjax("/sysParam/edit", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanSysParam();
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

// 新增系统子参数提交
function addSubSubmit(obj) {
	obj.field.mid = mid;
	CoreUtil.sendAjax("/sysParamSub/add", JSON.stringify(obj.field), function(
		data) {
		if (data.result) {
			layer.alert("操作成功",function (index) {
				layer.close(index);
				cleanSysParamSub();
				loadSub(mid);
			});
			// 	// 加载页面
			// loadSub(mid);

		} else {
			layer.alert(data.msg, function(index) {
				layer.close(index);
			});
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}

// 编辑系统子参数提交
function editSubSubmit(obj) {
	CoreUtil.sendAjax("/sysParamSub/edit", JSON.stringify(obj.field), function(
		data) {
		if (data.result) {

			layer.alert("操作成功");
			// 加载页面
			loadSub();
		} else {
			layer.alert(data.msg, function(index) {
				layer.close(index);
			});
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}

// 删除系统参数
function delSysParam(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除' + name + '系统参数吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/sysParam/delete", JSON.stringify(param),
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
								layer.alert(data, function() {
									layer.closeAll();
								});
							}
						}
					});
		});
	}
}
// 删除系统子参数
function delSysParamSub(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除快码名称:' + name + '的系统子参数吗？', {
			btn : [ '确认', '返回' ]
			// 按钮
		}, function() {
			CoreUtil.sendAjax("/sysParamSub/delete", JSON.stringify(param),
				function(data) {
					if (isLogin(data)) {
						if (data.result == true) {
							// 回调弹框
							layer.alert("删除成功！", function(index) {
								layer.close(index);
								// 加载load方法
								loadSub();
							});
						} else {
							layer.alert(data, function(index) {
								layer.close(index);
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

function loadSub(id) {
	tableIns1.reload({
		where : {
			id : id,
		},
		page : {
			curr : pageCurr1
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
function cleanSysParam() {
	$('#paramForm')[0].reset();
	layui.form.render();// 必须写
}

// 清空新增表单数据 子参数
function cleanSysParamSub() {
	// $('#paramSubForm')[0].reset();
	$('#subCode').val("");
	$('#subName').val("");
	$('#forder').val("");
	layui.form.render();// 必须写
}
