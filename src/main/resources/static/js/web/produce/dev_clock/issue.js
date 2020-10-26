/**
 * 指纹下发/删除管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table' ], function() {
		var table = layui.table, form = layui.form;

		tableIns = table.render({
			elem : '#issueList',
			url : context + 'produce/issue/getList',
			method : 'get' // 默认：get请求
			,
			//cellMinWidth : 80,
			where:{ptype:ptype},
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
				field : 'description',
				title : '类型',
			}, {
				field : 'devIp',
				title : '卡机IP'
			}, {
				field : 'devName',
				title : '卡机名字',
				templet: function (d) {return d.devClock.devName}
			}, {
				field : 'empCode',
				title : '员工工号',
				templet: function (d) {return d.emp.empCode}
			} , {
				field : 'empName',
				title : '员工姓名',
				width:120,
				templet: function (d) {return d.emp.empName}
			},{
				field : 'fmemo',
				title : '操作结果'
			},{
				field : 'createDate',
				title : '操作时间',
				width:150
			},{
				field : 'userCode',
				title : '操作人',
				templet: function (d) {return d.createUser.userCode}
			}] ],
			/*cols : [ [ {
				type : 'numbers'
			}
			// ,{field:'id', title:'ID', width:80, unresize:true, sort:true}
			, {
				field : 'devCode',
				title : '卡机序号',
			}, {
				field : 'devName',
				title : '卡机名称',
			}, {
				field : 'devType',
				title : '卡机类型',
			}, {
				field : 'empCode',
				title : '员工工号'
			}, {
				field : 'empName',
				title : '员工姓名'
			}, {
				fixed : 'right',
				title : '操作',
				align : 'center',
				toolbar : '#optBar',
				width : 100
			} ] ],*/
			done : function(res, curr, count) {
				//console.log(res)
				pageCurr = curr;
				/*merge(res.data, [ 'devCode', 'devName', 'devType' ],
						[ 1, 2, 3 ]);*/
			}
		});
		tableEmp = table.render({
			elem : '#empList',
			method : 'post',// 默认：get请求
			page : true,
			height: 'full-220',
			request : {
				pageName : 'page',// 页码的参数名称，默认：page
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
				type : "checkbox"
			},

			{
				field : 'EMP_CODE',
				title : '员工工号'
			}, {
				field : 'EMP_NAME',
				title : '员工姓名'
			}, {
				field : 'EMP_TYPE',
				title : '员工类型'
			} ] ],
			data : []
		});
		tableDev = table.render({
			elem : '#devList',
			method : 'post',// 
			height: 'full-220',
			page : true,
			request : {
				pageName : 'page', // 页码的参数名称，默认：page
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
				type : "checkbox"
			},

			{
				field : 'devCode',
				title : '卡机编号',
			}, {
				field : 'devName',
				title : '卡机名称',
			}, {
				field : 'devType',
				title : '卡机类型',
			}, {
				field : 'devIp',
				title : '卡机IP',
				width:120
			} ] ],
			data : []
		});
		form.on('submit(search)', function(data) {
			// 重新加载table
			load(data);
			return false;
		});
		// 监听搜索框-卡机设备
		form.on('submit(searchDev)', function(data) {
			// 重新加载table
			loadDev(data);
			return false;
		});
		// 监听搜索框-卡机设备
		form.on('submit(searchEmp)', function(data) {
			// 重新加载table
			loadEmp(data);
			return false;
		});
		// 监听工具条
		table.on('tool(issueTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delIssue(data, data.id);
			} else if (obj.event === 'edit') {
				// 编辑
				//getIssue(data, data.id);
			}
		});
		// 监听提交
		form.on('submit(addSubmit)', function(data) {
			//console.log(ptype)
			if (data.field.id == null || data.field.id == "") {
				// 新增
				var devList = "";
				var empList = "";
				var dList = table.checkStatus('devList').data;// 被选中行的数据 id
				// 对应的值
				var eList = table.checkStatus('empList').data;// 被选中行的数据 id
				// 对应的值
				if (dList == "" || eList == "") {
					layer.msg('请选择卡机或员工数据', {
						time : 10000, // 10s后自动关闭
						btn : [ '确定' ]
					});
				}else{
					for (var i = 0; i < dList.length; i++) {// 获取被选中的行
						devList += dList[i].id + ";"// 用“；”分隔
					}
					for (var i = 0; i < eList.length; i++) {// 获取被选中的行
						empList += eList[i].ID + ";"// 用“；”分隔
					}
					
					addSubmit(devList, empList);
				}
				
			} else {
				// editSubmit(data);
			}
			return false;
		});
		
		$(document).on('click','#addBtn',function(){
				addIssue();
				return false;
			});
		
	});
});
// 新增编辑弹出框
function openIssue( title) {
	
	var index = layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : false,// 是否点击遮罩关闭
		area : [ '1000px' ],
		content : $('#setIssue'),
		macmin : true,// 弹出框全屏
		end : function() {
		}
	});
	layer.full(index);// 弹出框全屏
}

// 添加指纹下发信息
function addIssue(title) {
	// 清空弹出框数据
	cleanIssue();
	//getEmp();//
	//getDev();//表格后续操作
	// 打开弹出框
	if(ptype == '1'){
		openIssue("添加指纹下发信息");
    }else{
    	openIssue("删除指纹信息");
    }
	return false;
}
// 获取员工信息
function getEmp() {
	CoreUtil.sendAjax("produce/issue/getEmp", "", function(data) {
		if (data.result) {
			// var beSelected=data.data;
			// console.log(data.data.rows)
			tableEmp.reload({
				data : data.data.rows,
				done : function(res, curr, count) {
					pageCurr = curr;
					console.log(ptype)
					if(ptype == '1'){
						console.log(res.data.rows)
						for(j = 0,len=res.data.rows.length; j < len; j++) {
							res.data[j]["LAY_CHECKED"]='true';
					        //下面三句是通过更改css来实现选中的效果
					        var index= res.data[j]['LAY_TABLE_INDEX'];
					        $('tr[data-index=' + index + '] input[type="checkbox"]').prop('checked', true);
					        $('tr[data-index=' + index + '] input[type="checkbox"]').next().addClass('layui-form-checked');
						}
				    }
				}
			})
		} else {
			layer.alert(data.msg)
		}
		// console.log(data)
	}, "POST", false, function(res) {
		layer.alert("操作请求错误，请您稍后再试");
	});
}
// 获取卡机信息
function getDev() {
	CoreUtil.sendAjax("produce/issue/getDev", "", function(data) {
		if (data.result) {
			tableDev.reload({
				data : data.data.rows,
				done : function(res, curr, count) {
					// cleanIssue();//清空之前的选中

				}
			})
		} else {
			layer.alert(data.msg)
		}
		// console.log(data)
	}, "POST", false, function(res) {
		layer.alert("操作请求错误，请您稍后再试");
	});
}
function merge(res, columsName, columsIndex) {
	var data = res;
	var mergeIndex = 0;// 定位需要添加合并属性的行数
	var mark = 1; // 这里涉及到简单的运算，mark是计算每次需要合并的格子数
	// var columsName = ['itemCode'];//需要合并的列名称
	// var columsIndex = [3];//需要合并的列索引值

	for (var k = 0; k < columsName.length; k++) { // 这里循环所有要合并的列
		var trArr = $(".layui-table-body>.layui-table").find("tr");// 所有行
		for (var i = 1; i < data.length; i++) { // 这里循环表格当前的数据
			var tdCurArr = trArr.eq(i).find("td").eq(columsIndex[k]);// 获取当前行的当前列
			var tdPreArr = trArr.eq(mergeIndex).find("td").eq(columsIndex[k]);// 获取相同列的第一列

			if (data[i][columsName[k]] === data[i - 1][columsName[k]]) { // 后一行的值与前一行的值做比较，相同就需要合并
				mark += 1;
				tdPreArr.each(function() {// 相同列的第一列增加rowspan属性
					$(this).attr("rowspan", mark);
				});
				tdCurArr.each(function() {// 当前行隐藏
					$(this).css("display", "none");
				});
			} else {
				mergeIndex = i;
				mark = 1;// 一旦前后两行的值不一样了，那么需要合并的格子数mark就需要重新计算
			}
		}
		mergeIndex = 0;
		mark = 1;
	}
}

// 新增指纹下发信息提交
function addSubmit(devList, empList) {
	console.log(devList)
	console.log(empList)
	var params = {
		"devList" : devList,
		"empList" : empList
	};
	var url = "produce/issue/clear";
	if(ptype == '1'){
		url = "produce/issue/add"
	}
	console.log(ptype)
	console.log(url)
	CoreUtil.sendAjax(url, JSON.stringify(params), function(
			data) {
		if (data.result) {
			layer.alert(data.msg, function() {
				layer.closeAll();
				cleanIssue();
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
function delIssue(obj, id) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除该数据吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("produce/issue/delete", JSON.stringify(param),
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
// 重新加载表格-设备（搜索）
function loadDev(obj) {
	// 重新加载table
	tableDev.reload({
		url : context + 'produce/issue/getDev',
		where : {
			devKeyword : obj.field.devKeyword
		},
		page : {
			curr : pageCurr
		// 从当前页码开始
		}
	});
}
// 重新加载表格-员工（搜索）
function loadEmp(obj) {
	// 重新加载table
	console.log(obj)
	tableEmp.reload({
		url : context + 'produce/issue/getEmp',
		where : {
			empKeyword : obj.field.empKeyword
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
function cleanIssue() {
	$('#devSearch')[0].reset();
	tableDev.reload({
		url : context + 'produce/issue/getDev',
		where : {
			devKeyword : ""
		},
		page : {
			curr : pageCurr
		// 从当前页码开始
		}
	});
	$('#empSearch')[0].reset();
	tableEmp.reload({
		url : context + 'produce/issue/getEmp',
		where : {
			empKeyword : ""
		},
		page : {
			curr : pageCurr
		// 从当前页码开始
		},done : function(res, curr, count) {
			pageCurr = curr;
			if(ptype == '1'){
				for(j = 0,len=res.data.length; j < len; j++) {
					res.data[j]["LAY_CHECKED"]='true';
			        //下面三句是通过更改css来实现选中的效果
			        var index= res.data[j]['LAY_TABLE_INDEX'];
			        /* $(' tr[data-index=' + index + '] input[type="checkbox"]').prop('checked', true);
			        $('tr[data-index=' + index + '] input[type="checkbox"]').next().addClass('layui-form-checked');
			        */
			        var td = $('#empList').next().find("tr[data-index='"+index+"'] div.layui-form-checkbox");           
			         td.click();
				}
		    }
		}
	});
	$('#issueForm')[0].reset();
	layui.form.render();// 必须写
}

//重新加载表格（搜索）
function load(obj) {
	// 重新加载table
	tableIns.reload({
		where : {
			keyword : obj.field.keywordSearch,
			ptype:ptype
		},
		page : {
			curr : pageCurr
		// 从当前页码开始
		}
	});
}
