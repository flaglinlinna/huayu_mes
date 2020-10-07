/**
 *指纹下发管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table'], function() {
		var table = layui.table, form = layui.form;

		tableIns = table.render({
			elem : '#issueList',
			url : context + 'produce/issue/getList',
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
				field : 'devCode',
				title : '卡机序号',
			},{
				field : 'devName',
				title : '卡机名称',
			},{
				field : 'devType',
				title : '卡机类型',
			}, {
				field : 'empCode',
				title : '员工工号'
			},{
				field : 'empName',
				title : '员工姓名'
			},  {
				field : 'fmemo',
				title : '备注',
			} , {
				fixed : 'right',
				title : '操作',
				align : 'center',
				toolbar : '#optBar',
				width: 100
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
		tableEmp=table.render({
			elem : '#empList',
			method : 'get' ,// 默认：get请求	
			page : true,
			request : {
				pageName : 'page' // 页码的参数名称，默认：page
				,
				limitName : 'rows' // 每页数据量的参数名，默认：limit
			},
			cols : [ [ {
				type : 'numbers'
			}
			// ,{field:'id', title:'ID', width:80, unresize:true, sort:true}
			, 
			{
				type:"checkbox"
			},
			
			{
				field : 'empCode',
				title : '员工工号'
			}, {
				field : 'empName',
				title : '员工姓名'
			}, {
				field : 'empType',
				title : '员工类型'
			}] ],
			data:[]
		});	
		tableDev=table.render({
			elem : '#devList',
			method : 'get' ,// 默认：get请求			
			page : true,
			request : {
				pageName : 'page' // 页码的参数名称，默认：page
				,
				limitName : 'rows' // 每页数据量的参数名，默认：limit
			},
			cols : [ [ {
				type : 'numbers'
			}
			// ,{field:'id', title:'ID', width:80, unresize:true, sort:true}
			, 
			{
				type:"checkbox"
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
			},{
				field : 'devIp',
				title : '卡机IP',
			}] ],
			data:[]
		});
		// 监听工具条
		table.on('tool(issueTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delIssue(data, data.id);
			} else if (obj.event === 'edit') {
				// 编辑
				getIssue(data, data.id);
			}
		});
		// 监听提交
		form.on('submit(addSubmit)', function(data) {
			if (data.field.id == null || data.field.id == "") {
				// 新增
				console.log(data)
				//addSubmit(data);
			} else {
				editSubmit(data);
			}
			return false;
		});
	});
});
// 新增编辑弹出框
function openIssue(id, title) {
	if (id == null || id == "") {
		$("#id").val("");
	}	
	var index = layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: false,//是否点击遮罩关闭
        area: ['1000px'],
        content:$('#setIssue'),
        macmin:true,//弹出框全屏
        end:function(){

        }
    });
    layer.full(index);//弹出框全屏
}

// 添加指纹下发信息
function addIssue() {
	// 清空弹出框数据
	cleanIssue();
	getEmp();
	getDev();
	// 打开弹出框
	openIssue(null, "添加指纹下发信息");
}
// 获取员工信息
function getEmp() {
	CoreUtil.sendAjax("produce/issue/getEmp", "", function(data) {
		if (data.result) {
			var beSelected=data.data;
			//console.log(beSelected)
			tableEmp.reload({
				data:data.data,
				done : function(res, curr, count) {
					cleanIssue();//清空之前的选中
					if(id != ''){
						//getSelected(id)
					}
				}
			})
		} else {
			layer.alert(data.msg)
		}
		//console.log(data)
	}, "POST", false, function(res) {
		layer.alert("操作请求错误，请您稍后再试");
	});
}
// 获取卡机信息
function getDev() {
	CoreUtil.sendAjax("produce/issue/getDev", "", function(data) {
		if (data.result) {
			tableDev.reload({
				data:data.data,
				done : function(res, curr, count) {
					cleanIssue();//清空之前的选中
					if(id != ''){
						//getSelected(id)
					}
				}
			})
		} else {
			layer.alert(data.msg)
		}
		//console.log(data)
	}, "POST", false, function(res) {
		layer.alert("操作请求错误，请您稍后再试");
	});
}
// 新增指纹下发信息提交
function addSubmit(obj) {
	CoreUtil.sendAjax("produce/issue/add", JSON.stringify(obj.field),
			function(data) {
				if (data.result) {
					layer.alert("操作成功", function() {
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
			CoreUtil.sendAjax("produce/issue/delete",
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
	$('#issueForm')[0].reset();
	layui.form.render();// 必须写
}
