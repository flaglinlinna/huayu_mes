/**
 * 人工制费维护管理
 */
var pageCurr,localtableFilterIns;
$(function() {
	layui.use([ 'form', 'table', 'tableSelect','upload','tableFilter' ],
		function() {
			var table = layui.table, form = layui.form, tableSelect = layui.tableSelect,upload = layui.upload,tableFilter = layui.tableFilter;
			tableIns = table.render({
				elem : '#colsList',
				url : context + '/basePrice/modelType/getList',
				method : 'get' // 默认：get请求
				,
				cellMinWidth : 80,
				height:'full-70',//固定表头&full-查询框高度
				page : true,
				limit: 100,
				limits: [50,100,200,300,500],
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
				cols : [ [ {type : 'numbers'},
					{field : 'workCenterName',title : '工作中心',minWidth : 150,sort: true,align: "center"},
					// {field : 'procName',title : '工序',width : 100},
					{field : 'modelCode',title : '机台编码',minWidth : 120,sort: true,align: "center"},
					{field : 'modelName',title : '机台描述',minWidth : 200,sort: true,align: "center"},
					{field : 'createBy',title : '创建人',width : 100},
					{field : 'createDate',title : '创建时间',width : 150},
					{field : 'lastupdateBy',title : '更新人',width : 100},
					{field : 'lastupdateDate',title : '更新时间',width : 150},
					{fixed : 'right',title : '操作',align : 'center',toolbar : '#optBar',width : 120} ] ],
				done : function(res, curr, count) {
					localtableFilterIns.reload();
					pageCurr = curr;
				}
			});

			localtableFilterIns = tableFilter.render({
				'elem' : '#colsList',
				'mode' : 'api',//服务器过滤
				'filters' : [
					{field: 'workCenterName', type:'checkbox'},
					{field: 'modelName', type:'input'},
				],
				'done': function(filters){
				}
			})

			// 工序列表
			procTableSelect = tableSelect.render({
				elem : '#procName',
				searchKey : 'keyword',
				checkedKey : 'id',
				searchPlaceholder : '试着搜索',
				table : {
					// width : 220,
					url : context
						+ '/basePrice/baseFee/getProcList',
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
							field : 'PROC_NAME',
							title : '工序'
						},

						{
							field : 'WORKCENTER_NAME',
							title : '工作中心'
						}

					] ],
					page : true,
					request : {
						pageName : 'page' // 页码的参数名称，默认：page
						,
						limitName : 'rows' // 每页数据量的参数名，默认：limit
					},
					parseData : function(res) {
						console.log(res)
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
							"count" : res.data.Total,
							"msg" : res.msg,
							"data" : res.data.List,
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
						"procName" : da[0].PROC_NAME,
						"procId" : da[0].ID,
						"workcenterId" : da[0].WORKCENTER_ID,
						"workcenterName" : da[0].WORKCENTER_NAME
					});
					form.render();// 重新渲染
				}
			});
			// 工作中心列表
			cTableSelect = tableSelect.render({
				elem : '#workcenterName',
				searchKey : 'keyword',
				checkedKey : 'id',
				searchPlaceholder : '试着搜索',
				table : {
					// width : 220,
					url : context
						+ '/basePrice/workCenter/getList',
					method : 'get',

					cols : [ [ {
						type : 'radio'
					},// 多选 radio
						, {
							field : 'ID',
							title : 'ID',
							width : 0,
							hide : true
						},

						{
							field : 'workcenterCode',
							title : '工作中心编码',

						},

						{
							field : 'workcenterName',
							title : '工作中心',
						}

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
						"pkWorkcenter" : da[0].id,
						"workcenterName" : da[0].workcenterName,
					});
					form.render();// 重新渲染
				}
			});


			// 监听工具条
			table.on('tool(colsTable)', function(obj) {
				var data = obj.data;
				if (obj.event === 'del') {
					// 删除
					delData(data, data.id, data.modelName);
				} else if (obj.event === 'edit') {
					// 编辑
					getData(data);
				}
			});
			// 监听提交
			form.on('submit(addSubmit)', function(obj) {
				if (obj.field.id == null || obj.field.id == "") {
					// 新增
					addSubmit(obj);
				} else {
					editSubmit(obj);
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
					"pkWorkcenter" : obj.workCenterId,
					"modelCode" : obj.modelCode,
					"modelName" : obj.modelName,
					"workcenterName":obj.workCenterName
				});
				openData(obj.id, "编辑类型信息")
			}

			// 导入
			upload.render({
				elem : '#upload',
				url : context + '/basePrice/modelType/doExcel',
				accept : 'file' // 普通文件
				,

				before : function(obj) { // obj参数包含的信息，跟 choose回调完全一致，可参见上文。
					layer.load(); // 上传loading
				},
				done : function(res, index, upload) {
					layer.closeAll('loading'); // 关闭loading
					layer.alert(res.msg, function(index) {
						layer.close(index);
						loadAll();
					});

				},
				error : function(index, upload) {
					layer.alert("操作请求错误，请您稍后再试", function() {
					});
					layer.closeAll('loading'); // 关闭loading
					layer.close(index);
				}
			});


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
	openData(null, "添加价格信息");
}
// 新增价格维护的提交
function addSubmit(obj) {
	CoreUtil.sendAjax("/basePrice/modelType/add", JSON.stringify(obj.field),
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

// 导出数据
function exportExcel() {
	location.href = context + "/basePrice/modelType/export";
	// location.href = "../../excelFile/机台类型维护模板.xlsx";//从文件夹内直接提取
}

// 编辑价格维护的提交
function editSubmit(obj) {
	CoreUtil.sendAjax("/basePrice/modelType/edit", JSON.stringify(obj.field),
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
		layer.confirm('您确定要删除该名称为:' + name + ' 的机台类型信息吗？', {
			btn : [ '确认', '返回' ]
			// 按钮
		}, function() {
			CoreUtil.sendAjax("/basePrice/modelType/delete", JSON
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
