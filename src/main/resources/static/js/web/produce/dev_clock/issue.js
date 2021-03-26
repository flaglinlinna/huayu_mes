/**
 * 指纹下发/删除管理
 */
var pageCurr,tableEmp,localtableFilterIns;
$(function() {
	layui.use([ 'form', 'table','tableFilter'  ], function() {
		var table = layui.table, form = layui.form,tableFilter = layui.tableFilter;

		tableIns = table.render({
			elem : '#issueList',
			url : context + '/produce/issue/getList',
			method : 'get' // 默认：get请求
			,
			//cellMinWidth : 80,
			height:'full-80'//固定表头&full-查询框高度
			,even:true,//条纹样式
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
					width:90,
			}, {
				field : 'devIp',
				title : '卡机IP',
					width:120,
			}, {
				field : 'devName',
				title : '卡机名字',
					width:110,
				templet: function (d) {return d.devClock.devName}
			}, {
					field : 'lineName',
					title : '线体名称',
					width:100,
					templet: function (d) {return d.devClock.line.lineName}
				},
				{
				field : 'empCode',
				title : '员工工号',
					width:100,
				templet: function (d) {return d.emp.empCode}
			} , {
				field : 'empName',
				title : '员工姓名',
				width:100,
				templet: function (d) {return d.emp.empName}
			},{
				field : 'fmemo',
					width:100,
				title : '操作结果'
			},{
				field : 'createDate',
				title : '操作时间',
				width:150
			},{
				field : 'userCode',
				title : '操作人',
					width:100,
				templet: function (d) {return d.createUser.userCode}
			}] ],
			done : function(res, curr, count) {
				pageCurr = curr;
			}
		});
		tableEmp = table.render({
			elem : '#empList',
			method : 'post',// 默认：get请求
			url : context + '/produce/issue/getEmp',
			page : true,
			height: 'full-220',
			limit:20,
			limits: [20,50,100,500,1000,2000,5000],
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
				type : 'numbers',
				width:40
			}, {
				type : 'checkbox',
				width:40
			},
			{
				field : 'EMP_CODE',
				title : '员工工号',
				width:80
			}, {
				field : 'EMP_NAME',
				title : '员工姓名',
					width:90
			}, {
					field : 'dept_name',
					title : '部门名称',
					width:110
				},{
					field : 'create_time',
					title : '指纹录入时间',
					width:160
				},
				{
					field : 'EMP_TYPE',
					title : '员工类型',
					width:110
				},
			] ],
			done : function(res, curr, count) {
				localtableFilterIns.reload();
				pageCurr = curr;
				if(ptype == '1'){
					for(j = 0,len=res.data.length; j < len; j++) {
						res.data[j]["LAY_CHECKED"]='true';
				        //下面三句是通过更改css来实现选中的效果
				        var index= res.data[j]['LAY_TABLE_INDEX'];
				        var td = $('#empList').next().find("tr[data-index='"+index+"'] div.layui-form-checkbox");           
				         td.click();
					}
			    }
			}
		});
		localtableFilterIns = tableFilter.render({
			'elem' : '#empList',
			'parent' : '#setIssue',
			'mode' : 'api',//服务端过滤
			'filters' : [
				{field: 'dept_name', type:'checkbox'},
				{field: 'create_time', type:'date'}
			],
			'done': function(filters){}
		})
		
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
				width:130,
			}, {
				field : 'devName',
				title : '卡机名称',
					width:120,
			}, {
				field : 'devType',
				title : '卡机类型',
					width:90,
			},
				{
					field : 'isOnline',
					title : '在线状态',
					templet:'#statusTp2',
					width : 80,
				},
				{
					field : 'lineName',
					title : '线别名称',
					width:120,
					templet: function (d) {return d.line.lineName}
				},
				{
				field : 'devIp',
				title : '卡机IP',
				width:110
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

		form.on('submit(searchLeave)', function(data) {
			// 重新加载table
			searchLeave();
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

	// 打开弹出框
	if(ptype == '1'){
		openIssue("添加指纹下发信息");
    }else{
    	openIssue("删除指纹信息");
    }
	return false;
}


// 新增指纹下发信息提交
function addSubmit(devList, empList) {
	console.log(devList)
	console.log(empList)
	var params = {
		"devList" : devList,
		"empList" : empList
	};
	var url = "/produce/issue/clear";
	if(ptype == '1'){
		url = "/produce/issue/add"
	}

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
			CoreUtil.sendAjax("/produce/issue/delete", JSON.stringify(param),
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
		url : context + '/produce/issue/getDev',
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
	tableEmp.reload({
		//url : context + '/produce/issue/getEmp',
		where : {
			empKeyword : obj.field.empKeyword
		},
		page : {
			curr : pageCurr
		// 从当前页码开始
		}
	});
}

function  searchLeave (){
	tableEmp.reload({
		//url : context + '/produce/issue/getEmp',
		where : {
			type : 1
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
		url : context + '/produce/issue/getDev',
		where : {
			devKeyword : ""
		},
		page : {
			curr : pageCurr
		// 从当前页码开始
		}
	});
	$('#empSearch')[0].reset();
	/*tableEmp.reload({
		url : context + '/produce/issue/getEmp',
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
			        var td = $('#empList').next().find("tr[data-index='"+index+"'] div.layui-form-checkbox");           
			         td.click();
				}
		    }
		}
	});*/
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
