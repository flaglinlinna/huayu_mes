/**
 * 系统参数管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table' ], function() {
		var table = layui.table, form = layui.form;

		tableIns = table.render({
			elem : '#paramList',
			url : context + '/sysParam/getList',
			method : 'get' // 默认：get请求
			,
			cellMinWidth : 80
			,height:'full-80'//固定表头&full-查询框高度
		    ,even:true//条纹样式
			,page : true,
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
				field : 'paramType',
				title : '系统参数类型',
				templet: function(d){
					if(d.paramType=="1"){
						 return  "用户级"
					}else if(d.paramType==0){
						return  "系统级"
					}  
			      }
			},  {
				field : 'paramCode',
				title : '参数编码'
			}, {
				field : 'paramName',
				title : '参数名称'
			}, {
				field : 'paramValue',
				title : '参数值'
			},
				{
					field : 'paramSort',
					title : '功能模块'
				},
				{
				field : 'fmemo',
				title : '备注',
			}, {
				field : 'lastupdateDate',
				title : '更新时间'
			}, {
				field : 'createDate',
				title : '添加时间',
			}, {
				fixed : 'right',
				title : '操作',
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

		// 监听工具条
		table.on('tool(paramTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delSysParam(data, data.id, data.paramName);
			} else if (obj.event === 'edit') {
				// 编辑
				getSysParam(data, data.id);
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
		// 监听搜索框
		form.on('submit(searchSubmit)', function(data) {
			// 重新加载table
			load(data);
			return false;
		});
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
		}
	});
	layer.full(index);
}

// 添加系统参数
function addSysParam() {
	// 清空弹出框数据
	cleanSysParam();
	// 打开弹出框
	openSysParam(null, "添加系统参数");
}
// 新增系统参数提交
function addSubmit(obj) {
	CoreUtil.sendAjax("/sysParam/add", JSON.stringify(obj.field), function(
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
function cleanSysParam() {
	$('#paramForm')[0].reset();
	layui.form.render();// 必须写
}
