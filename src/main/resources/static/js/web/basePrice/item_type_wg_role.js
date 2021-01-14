/**
 * 物料通用价格维护管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table','tableSelect' ], function() {
		var table = layui.table, form = layui.form, tableSelect = layui.tableSelect;

		tableIns = table.render({
			elem : '#colsList',
			url : context + '/basePrice/itemTypeWgRole/getList',
			method : 'get' // 默认：get请求
			,
			cellMinWidth : 80,
			height:'full-70',//固定表头&full-查询框高度
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
			},  {
				field : 'itemType',
				title : '物料类型',
				width : 200
			},  {
				field : 'fmemo',
				title : '备注',
			}, {
				field : 'createBy',
				title : '创建人',
				width : 80
			}, {
				field : 'createDate',
				title : '创建时间',
				width : 150
			}, {
				field : 'lastupdateBy',
				title : '更新人',
				width : 80
			}, {
				field : 'lastupdateDate',
				title : '更新时间',
				width : 150
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

		// tableIns=table.render({
		// 	elem: '#roleList'
		// 	,url:context+'/sysRole/getList'
		// 	,method: 'get' //默认：get请求
		// 	,cellMinWidth: 80
		// 	,height:'full-160'//固定表头&full-查询框高度
		// 	,even:true//条纹样式
		// 	,page: true,
		// 	request: {
		// 		pageName: 'page' //页码的参数名称，默认：page
		// 		,limitName: 'rows' //每页数据量的参数名，默认：limit
		// 	},
		// 	parseData: function (res) {
		// 		// 可进行数据操作
		// 		return {
		// 			"count": res.data.total,
		// 			"msg":res.msg,
		// 			"data":res.data.rows,
		// 			"code": res.status //code值为200表示成功
		// 		}
		// 	},
		// 	// response:{
		// 	//     statusName: 'status' //数据状态的字段名称，默认：code
		// 	//     ,statusCode: 200 //成功的状态码，默认：0
		// 	//     ,countName: 'count' //数据总数的字段名称，默认：count
		// 	//     ,dataName: 'data' //数据列表的字段名称，默认：data
		// 	// },
		// 	cols: [[
		// 		{type:'numbers'}
		// 		// ,{field:'id', title:'ID', width:80, unresize:true, sort:true}
		// 		,{field:'roleCode', title:'编号',align:'center', width:120,sort:true}
		// 		,{field:'roleName', title:'名称',align:'center', width:140,sort:true}
		// 		,{field:'description', title: '描述',align:'center', minWidth:140,sort:true}
		// 		,{field:'userCount', title: '用户数量',align:'center', width:120,sort:true,
		// 			templet: '<div><a  style="text-decoration:underline;color:blue;cursor: pointer;"; onclick="showUser({{d.id}})">{{ d.userCount }}</a></div>'}
		// 		,{field:'status', title:'状态',width:95,align:'center',templet:'#statusTpl'}
		// 		,{field:'lastupdateDate', title: '更新时间', align:'center',width:150}
		// 		,{field:'createDate', title: '添加时间',align:'center', width:150}
		// 		,{fixed:'right', title:'操作', width:200, align:'center', toolbar:'#optBar'}
		// 	]]
		// 	,done: function(res, curr, count){
		// 		//如果是异步请求数据方式，res即为你接口返回的信息。
		// 		//如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
		// 		//console.log(res);
		// 		//得到当前页码
		// 		//console.log(curr);
		// 		//得到数据总量
		// 		//console.log(count);
		// 		pageCurr=curr;
		// 	}
		// });

		tableSelect = tableSelect.render({
				elem : '#itemType',
				searchKey : 'keyword',
				checkedKey : 'id',
				searchPlaceholder : '试着搜索',
				table : {
					//width : 220,
					url : context
						+ '/basePrice/itemTypeWg/getList',
					method : 'get',

					cols : [ [ {type : 'radio'},// 多选 radio
						{field : 'itemType', title : '物料类型', width : 200},
						{field : 'fmemo', title : '备注'}
					] ],
					page : true,
					request : {
						pageName : 'page' // 页码的参数名称，默认：page
						,
						limitName : 'rows' // 每页数据量的参数名，默认：limit
					},
					parseData : function(res) {
						if (!res.result) {
							// 可进行数据操作
							return {
								"count" : 0,
								"msg" : res.msg,
								"data" : [],
								"code" : res.status
								// code值为200表示成功
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
				},
				done : function(elem, data) {
					// 选择完后的回调，包含2个返回值
					// elem:返回之前input对象；data:表格返回的选中的数据 []
					var da = data.data;
					form.val("itemForm", {
						"itemType" : da[0].itemType
					});
					form.render();// 重新渲染
					//刷新角色table
				}
			});

		// 监听工具条
		table.on('tool(colsTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delData(data, data.id, data.itemType);
			} else if (obj.event === 'edit') {
				// 编辑
				getData(data);
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
		// 编辑价格维护
		function getData(obj) {
			
			form.val("itemForm", {
				"id" : obj.id,
				"fmemo" : obj.fmemo,
				"itemType" : obj.itemType
			});

			openData(obj.id, "外购物料类型")
		}


	});

});



// 新增编辑弹出框
function openData(id, title) {
	if (id == null || id == "") {
		$("#id").val("");
	}
	var index = layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		content : $('#setForm'),
		end : function() {
			cleanData();
		}
	});
	layer.full(index);
}

// 新增价格维护
function add() {
	// 清空弹出框数据
	cleanData();
	// 打开弹出框
	openData(null, "添加外购物料类型信息");
}

// 新增价格维护的提交
function addSubmit(obj) {
	CoreUtil.sendAjax("/basePrice/itemTypeWg/add", JSON.stringify(obj.field),
			function(data) {
				if (data.result) {
					layer.alert("操作成功", function() {
						layer.closeAll();
						cleanData();
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



// 编辑价格维护的提交
function editSubmit(obj) {
	CoreUtil.sendAjax("/basePrice/itemTypeWg/edit", JSON.stringify(obj.field),
			function(data) {
				if (data.result) {
					layer.alert("操作成功", function() {
						layer.closeAll();
						cleanData();
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

// 删除价格维护
function delData(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除该类型为:' + name + ' 的外购物料类型信息吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/basePrice/itemTypeWg/delete", JSON
					.stringify(param), function(data) {
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
function cleanData() {
	$('#itemForm')[0].reset();
	layui.form.render();// 必须写
}
