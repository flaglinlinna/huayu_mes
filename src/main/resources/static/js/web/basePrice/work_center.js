/**
 * 工作中心维护管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table' ], function() {
		var table = layui.table, form = layui.form;

		tableIns = table.render({
			elem : '#workCenterList',
			url : context + '/basePrice/workCenter/getList',
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
				field : 'bsCode',
				title : '工作中心大类',width:130,
				templet:function(d){
					if(d.bsCode=="hardware"){
						return "五金"
					}else if(d.bsCode=="molding"){
						return "注塑"
					}else if(d.bsCode=="surface"){
						return "表面处理"
					}else if(d.bsCode=='packag'){
						return "组装"
					}else if(d.bsCode=='out'){
						return "外协"
					}else{
						return ""
					}
				}
			}, {
				field : 'workcenterCode',
				title : '工作中心编号',width:130
			}, {
				field : 'workcenterName',
				title : '工作中心名称',width:130
			}, {
				field : 'fmemo',
				title : '备注',width:130
			}, {
				field : 'checkStatus',
				title : '有效状态',width:95,
				templet : '#statusTpl'
			},
			{
				field : 'createBy',
				title : '创建人',width:100
			}, {
				field : 'createDate',
				title : '创建时间',width:150
			}, {
				field : 'lastupdateBy',
				title : '更新人',width:100
			}, {
				field : 'lastupdateDate',
				title : '更新时间',width:150
			}, {
				fixed : 'right',
				title : '操作',
				align : 'center',
				toolbar : '#optBar',
				width:130
			} ] ],
			done : function(res, curr, count) {
				console.log(res)
				pageCurr = curr;
				merge(res.data,['bsCode',],[1,1]);
			}
		});
		// 切换状态操作
		form.on('switch(isStatusTpl)', function(obj) {
			doStatus(obj, this.value, this.name, obj.elem.checked);
		});

		// 监听工具条
		table.on('tool(workCenterTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delWorkCenter(data, data.id, data.workcenterName);
			} else if (obj.event === 'edit') {
				// 编辑
				getWorkCenter(data, data.id);
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
		// 编辑工作中心维护
		function getWorkCenter(obj, id) {
			var param = {
				"id" : id
			};
			CoreUtil.sendAjax("/basePrice/workCenter/getWorkCenterDetail", JSON
					.stringify(param), function(data) {
				if (data.result) {
					form.val("workCenterForm", {
						"id" : data.data.id,
						"bsCode" : data.data.bsCode,
						"workcenterCode" : data.data.workcenterCode,
						"workcenterName" : data.data.workcenterName,
					});
					openWorkCenter(id, "编辑工作中心信息")
				} else {
					layer.alert(data.msg)
				}
			}, "POST", false, function(res) {
				layer.alert("操作请求错误，请您稍后再试");
			});
		}

		// 设置正常/禁用
		function doStatus(obj, id, name, checked) {
			var isStatus = checked ? 1 : 0;
			var deaprtisStatus = checked ? "正常" : "禁用";
			// 正常/禁用
			layer.confirm(
					'您确定要把工作中心：' + name + '设置为' + deaprtisStatus + '状态吗？', {
						btn1 : function(index) {
							var params = {
								"id" : id,
								"checkStatus" : isStatus
							};
							CoreUtil.sendAjax("/basePrice/workCenter/doStatus",
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
function openWorkCenter(id, title) {
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
		content : $('#setWorkCenter'),
		end : function() {
			cleanWorkCenter();
		}
	});
	layer.full(index);
}

// 新增工作中心维护
function addWorkCenter() {
	// 清空弹出框数据
	cleanWorkCenter();
	// 打开弹出框
	openWorkCenter(null, "添加工作中心信息");
}
// 新增工作中心维护的提交
function addSubmit(obj) {
	CoreUtil.sendAjax("/basePrice/workCenter/add", JSON.stringify(obj.field),
			function(data) {
				if (data.result) {
					layer.alert("操作成功", function() {
						layer.closeAll();
						cleanWorkCenter();
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

// 编辑工作中心维护的提交
function editSubmit(obj) {
	CoreUtil.sendAjax("/basePrice/workCenter/edit", JSON.stringify(obj.field),
			function(data) {
				if (data.result) {
					layer.alert("操作成功", function() {
						layer.closeAll();
						cleanWorkCenter();
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

// 删除工作中心维护
function delWorkCenter(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除该名称为:' + name + ' 的工作中心信息吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/basePrice/workCenter/delete", JSON
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
function merge(res,columsName,columsIndex) {
    var data = res;
    var mergeIndex = 0;//定位需要添加合并属性的行数
    var mark = 1; //这里涉及到简单的运算，mark是计算每次需要合并的格子数
    //var columsName = ['itemCode'];//需要合并的列名称
    //var columsIndex = [3];//需要合并的列索引值
    for (var k = 0; k < columsIndex.length; k++) { //这里循环所有要合并的列
        var trArr = $(".layui-table-body>.layui-table").find("tr");//所有行
            for (var i = 1; i < data.length; i++) { //这里循环表格当前的数据
                var tdCurArr = trArr.eq(i).find("td").eq(columsIndex[k]);//获取当前行的当前列
                var tdPreArr = trArr.eq(mergeIndex).find("td").eq(columsIndex[k]);//获取相同列的第一列
                if (data[i][columsName[0]] === data[i-1][columsName[0]]) { //后一行的值与前一行的值做比较，相同就需要合并
                	mark += 1;
                    tdPreArr.each(function () {//相同列的第一列增加rowspan属性
                        $(this).attr("rowspan", mark);
                    });
                    tdCurArr.each(function () {//当前行隐藏
                        $(this).css("display", "none");
                    });
                }else {
                    mergeIndex = i;
                    mark = 1;//一旦前后两行的值不一样了，那么需要合并的格子数mark就需要重新计算
                }
            }
        mergeIndex = 0;
        mark = 1;
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
function cleanWorkCenter() {
	$('#workCenterForm')[0].reset();
	layui.form.render();// 必须写
}
