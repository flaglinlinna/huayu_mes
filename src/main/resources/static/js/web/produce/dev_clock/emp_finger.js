/**
 * 指纹登记信息管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table' ], function() {
		var table = layui.table, form = layui.form;

		tableIns = table.render({
			elem : '#empList',
			url : context + 'produce/emp_finger/getList',
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
			, {
				field : 'empCode',
				title : '员工工号',
				width : 100,
			}, {
				field : 'empName',
				title : '员工姓名',
				width : 100,
			}, {
				field : 'fingerIdx',
				title : '手指序号',
				width : 80,
			}, {
				field : 'templateStr',
				title : '指纹模板',
			
			},/* {
				field : 'lastupdateDate',
				title : '更新时间',
				width : 145,
				templet:'<div>{{d.lastupdateDate?DateUtils.formatDate(d.lastupdateDate):""}}</div>',
			},*/ {
				field : 'createDate',
				title : '添加时间',
				width : 145,
				templet:'<div>{{d.createDate?DateUtils.formatDate(d.createDate):""}}</div>',
			}, {
				fixed : 'right',
				title : '操作',
				align : 'center',
				width : 120,
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
				merge(res.data,['empCode','empName'],[1,2]);
			}
		});
		// 监听工具条
		table.on('tool(empTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delEmpFinger(data, data.id, data.empName);
			} else if (obj.event === 'edit') {
				// 编辑
				console.log("edit");
				getEmpFinger(data, data.id);
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
			console.log(data)
			// 重新加载table
			load(data);
			return false;
		});
		// 编辑指纹登记信息
		function getEmpFinger(obj, id) {
			var param = {
				"id" : id
			};
			CoreUtil.sendAjax("produce/emp_finger/getEmpFinger", JSON
					.stringify(param), function(data) {
				if (data.result) {
					form.val("empForm", {
						"id" : data.data.id,
						"templateStr" : data.data.templateStr,
						"fingerIdx" : data.data.fingerIdx,
					});
					getEmpList(data.data.empId);
					openEmpFinger(id, "编辑指纹登记信息")
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
function openEmpFinger(id, title) {
	if (id == null || id == "") {
		$("#id").val("");
	}
	layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#setEmpFinger'),
		end : function() {
			cleanEmpFinger();
		}
	});
}

// 添加指纹登记信息
function addEmpFinger() {
	// 清空弹出框数据
	cleanEmpFinger();
	// 打开弹出框
	getEmpList("");
	openEmpFinger(null, "添加指纹登记信息");
}
//获取线体信息
function getEmpList(id){
	CoreUtil.sendAjax("produce/emp_finger/getEmpList", "",
			function(data) {
				if (data.result) {
				$("#empId").empty();
				var emp=data.data;
				console.log(emp)
				for (var i = 0; i < emp.length; i++) {
					if(i==0){
						$("#empId").append("<option value=''>请点击选择</option>");
					}
					$("#empId").append("<option value=" + emp[i].id+ ">" + emp[i].empName + "</option>");
					if(emp[i].id==id){
						$("#empId").val(emp[i].id);
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

// 新增指纹登记信息提交
function addSubmit(obj) {
	CoreUtil.sendAjax("produce/emp_finger/add", JSON.stringify(obj.field),
			function(data) {
				if (data.result) {
					layer.alert("操作成功", function() {
						layer.closeAll();
						cleanEmpFinger();
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

// 编辑指纹登记信息提交
function editSubmit(obj) {
	CoreUtil.sendAjax("produce/emp_finger/edit", JSON.stringify(obj.field),
			function(data) {
				if (data.result) {
					layer.alert("操作成功", function() {
						layer.closeAll();
						cleanEmpFinger();
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

// 删除指纹登记信息
function delEmpFinger(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除' + name + '指纹登记信息吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("produce/emp_finger/delete",
					JSON.stringify(param), function(data) {
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
function merge(res,columsName,columsIndex) {
    //console.log(res)
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
function cleanEmpFinger() {
	$('#empForm')[0].reset();
	layui.form.render();// 必须写
}
