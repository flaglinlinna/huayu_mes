/**
 * 不良内容管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table' ], function() {
		var table = layui.table, form = layui.form;

		tableIns = table.render({
			elem : '#defdetailList',
			url : context + 'base/defdetail/getList',
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
			}
			// ,{field:'id', title:'ID', width:80, unresize:true, sort:true}
			,{
				field : 'pbsCode',
				title : '不良类别编号'
			},{
				field : 'pbsName',
				title : '不良类别名称'
			}, {
				field : 'bsCode',
				title : '不良内容编码'
			}, {
				field : 'bsName',
				title : '不良内容名称'
			}, {
				field : 'bsStatus',
				title : '状态',
				width : 95,
				templet : '#statusTpl'
			}, {
				field : 'modifiedTime',
				title : '更新时间',
				templet:'<div>{{d.modifiedTime?DateUtils.formatDate(d.modifiedTime):""}}</div>'
			}, {
				field : 'createdTime',
				title : '添加时间',
				templet:'<div>{{DateUtils.formatDate(d.createdTime)}}</div>'
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
				merge(res.data,['pbsCode'],[1]);
			}
		});

		// 监听在职操作
		form.on('switch(isStatusTpl)', function(obj) {
			setStatus(obj, this.value, this.name, obj.elem.checked);
		});
		// 监听工具条
		table.on('tool(defdetailTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delDef(data, data.id, data.bsCode);
			} else if (obj.event === 'edit') {
				// 编辑
				getDef(data, data.id);
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
			//console.log(data)
			// 重新加载table
			load(data);
			return false;
		});
		// 编辑不良内容
		function getDef(obj, id) {
			
			var param = {
				"id" : id
			};
			CoreUtil.sendAjax("base/defdetail/getDefectiveDetail", JSON.stringify(param),
					function(data) {
						if (data.result) {
							
							form.val("defdetailForm", {
								"id" : data.data.id,
								"bsCode" : data.data.bsCode,
								"bsName" : data.data.bsName,
								//"pkDefective":
							});
							getDefectiveList(data.data.pkDefective);
							
							//layui.form.render('select');
							openDef(id, "编辑不良内容")
						} else {
							layer.alert(data.msg)
						}
					}, "POST", false, function(res) {
						layer.alert("操作请求错误，请您稍后再试");
					});
		}
		// 设置用户正常/禁用
		function setStatus(obj, id, name, checked) {
			// setStatus(obj, this.value, this.name, obj.elem.checked);
			var isStatus = checked ? 0 : 1;
			var deaprtisStatus = checked ? "正常" : "禁用";
			// 正常/禁用

			layer.confirm(
					'您确定要把不良内容：' + name + '设置为' + deaprtisStatus + '状态吗？', {
						btn1 : function(index) {
							var param = {
								"id" : id,
								"bsStatus" : isStatus
							};
							CoreUtil.sendAjax("/base/defdetail/doStatus", JSON
									.stringify(param), function(data) {
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
								layer.alert("操作请求错误，请您稍后再试", function() {
									layer.closeAll();
								});
							});
						},
						btn2 : function() {
							obj.elem.checked = isStatus;
							form.render();
							layer.closeAll();
						},
						cancel : function() {
							obj.elem.checked = isStatus;
							form.render();
							layer.closeAll();
						}
					})
		}
	});

});

// 新增编辑弹出框
function openDef(id, title) {
	if (id == null || id == "") {
		$("#id").val("");
	}
	layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px','310px'],
		content : $('#setDef'),
		end : function() {
			cleanDef();
		}
	});
}

function merge(res,columsName,columsIndex) {
    
    var data = res;
    var mergeIndex = 0;//定位需要添加合并属性的行数
    var mark = 1; //这里涉及到简单的运算，mark是计算每次需要合并的格子数
    //var columsName = ['itemCode'];//需要合并的列名称
    //var columsIndex = [3];//需要合并的列索引值

    for (var k = 0; k < columsName.length; k++) { //这里循环所有要合并的列
        var trArr = $(".layui-table-body>.layui-table").find("tr");//所有行
            for (var i = 1; i < data.length; i++) { //这里循环表格当前的数据
                var tdCurArr = trArr.eq(i).find("td").eq(columsIndex[k]);//获取当前行的当前列
                var tdPreArr = trArr.eq(mergeIndex).find("td").eq(columsIndex[k]);//获取相同列的第一列
                
                if (data[i][columsName[k]] === data[i-1][columsName[k]]) { //后一行的值与前一行的值做比较，相同就需要合并
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


// 添加不良内容
function addDef() {
	// 清空弹出框数据
	cleanDef();
	getDefectiveList("");
	// 打开弹出框
	openDef(null, "添加不良内容");
}
// 新增不良内容提交
function addSubmit(obj) {
	CoreUtil.sendAjax("base/defdetail/add", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanDef();
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
//获取不良类别
function getDefectiveList(id){
	CoreUtil.sendAjax("base/defdetail/getDefectiveList", "",
			function(data) {
				if (data.result) {
				$("#pkDefective").empty();
				var bad=data.data;
				//console.log(data)
				for (var i = 0; i < bad.length; i++) {
					if(i==0){
						$("#pkDefective").append("<option value=''>请点击选择</option>");
					}
					$("#pkDefective").append("<option value=" + bad[i].id+ ">" + bad[i].bsName + "</option>");
					if(bad[i].id==id){
						$("#pkDefective").val(bad[i].id);
					}
				}					
				layui.form.render('select');

				} else {
					layer.alert(data.msg)
				}
				console.log(data)
			}, "POST", false, function(res) {
				layer.alert("操作请求错误，请您稍后再试");
			});
}
// 编辑不良内容提交
function editSubmit(obj) {
	CoreUtil.sendAjax("base/defdetail/edit", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanDef();
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

// 删除不良内容
function delDef(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除' + name + '不良内容吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("base/defdetail/delete", JSON.stringify(param),
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
function cleanDef() {
	$('#defdetailForm')[0].reset();
	layui.form.render();// 必须写
}
