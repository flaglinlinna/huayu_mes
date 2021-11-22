/**
 * 月中标率报表
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table' ], function() {
		var table = layui.table, form = layui.form;

		tableIns = table.render({
			elem : '#prodTypList',
			url : context + '/quote/getMonBadeList',
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
			},{
				field : 'MONTHCHAR',
				title : '月份',
				width:120
			},{
				field : 'ALLCOUNT',
				title : '报价申请数',
				width:95
			},

			{
				field : 'BADECOUNT',
				title : '报价中标数',
				width:80,templet: function (d){
					if(d.BADECOUNT==null){
						return "0"
					}else {
						return d.BADECOUNT
					}
		}

			}, {
				field : 'MONPERCENT',
				title : '中标率',
					width:80,templet: function (d){
			if(d.MONPERCENT==null){
				return "0%"
			}else {
				return d.MONPERCENT
			}
		}
			},
				 ] ],
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
		// 切换状态操作
		form.on('switch(isStatusTpl)', function(obj) {
			doStatus(obj, this.value, this.name, obj.elem.checked);
		});

		// 监听工具条
		table.on('tool(prodTypTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delProdTyp(data, data.id, data.productType);
			} else if (obj.event === 'edit') {
				// 编辑
				getProdTyp(data);
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
		// 编辑产品类型维护
		function getProdTyp(obj) {	
			form.val("prodTypForm", {
						"id" : obj.id,
						"productType" : obj.productType,
					});
					openProdTyp(obj.id, "编辑产品类型信息")
		}

		// 设置正常/禁用
		function doStatus(obj, id, name, checked) {
			var isStatus = checked ? 1 : 0;
			var deaprtisStatus = checked ? "正常" : "禁用";
			// 正常/禁用
			layer.confirm(
					'您确定要把产品类型：' + name + '设置为' + deaprtisStatus + '状态吗？', {
						btn1 : function(index) {
							var params = {
								"id" : id,
								"checkStatus" : isStatus
							};
							CoreUtil.sendAjax("/basePrice/prodTyp/doStatus",
									JSON.stringify(params), function(data) {
										if (data.result) {
											layer.alert("操作成功", function() {
												layer.closeAll();
												loadAll();
											});
										} else {
											layer.alert(data.msg, function() {
												layer.closeAll();
											});
										}
									}, "POST", false, function(res) {
										layer.alert("操作请求错误，请您稍后再试",
												function() {
													layer.closeAll();
												});
									});
						},
						btn2 : function() {
							obj.elem.checked = !isStatus;
							form.render();
							layer.closeAll();
						},
						cancel : function() {
							obj.elem.checked = !isStatus;
							form.render();
							layer.closeAll();
						}
					});
		}

	});

});

// 新增编辑弹出框
function openProdTyp(id, title) {
	if (id == null || id == "") {
		$("#id").val("");
	}
	var index = layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#setProdTyp'),
		end : function() {
			cleanProdTyp();
		}
	});
	layer.full(index);
}

// 新增产品类型维护
function addProdTyp() {
	// 清空弹出框数据
	cleanProdTyp();
	// 打开弹出框
	openProdTyp(null, "添加产品类型信息");
}
// 新增产品类型维护的提交
function addSubmit(obj) {
	CoreUtil.sendAjax("/basePrice/prodTyp/add", JSON.stringify(obj.field),
			function(data) {
				if (data.result) {
					layer.alert("操作成功", function() {
						layer.closeAll();
						cleanProdTyp();
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

// 编辑产品类型维护的提交
function editSubmit(obj) {
	CoreUtil.sendAjax("/basePrice/prodTyp/edit", JSON.stringify(obj.field),
			function(data) {
				if (data.result) {
					layer.alert("操作成功", function() {
						layer.closeAll();
						cleanProdTyp();
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

// 删除产品类型维护
function delProdTyp(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除该名称为:' + name + ' 的产品类型信息吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/basePrice/prodTyp/delete", JSON
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
function cleanProdTyp() {
	$('#prodTypForm')[0].reset();
	layui.form.render();// 必须写
}
