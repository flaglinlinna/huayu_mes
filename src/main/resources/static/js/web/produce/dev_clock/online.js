/**
 * 上线人员信息管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table','laydate'], function() {
		var table = layui.table, form = layui.form, laydate = layui.laydate;

		tableIns = table.render({
			elem : '#onlineList',
			url : context + 'produce/online/getList',
			method : 'get', // 默认：get请求
			cellMinWidth : 80,
			page : true,
			request : {
				pageName : 'page' ,// 页码的参数名称，默认：page
				limitName : 'rows', // 每页数据量的参数名，默认：limit
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
				field : 'taskNo',
				title : '制令单号'
			},{
				field : 'hourType',
				title : '工时类型',
				width:100
			}, {
				field : 'lineName',
				title : '线体',
				width:150
			}, {
				field : 'lastupdateDate',
				title : '更新时间',
				width:180,
				templet:'<div>{{d.lastupdateDate?DateUtils.formatDate(d.lastupdateDate):""}}</div>',
			}, {
				field : 'createDate',
				title : '创建时间',
				width:180,
				templet:'<div>{{d.createDate?DateUtils.formatDate(d.createDate):""}}</div>',
			} , {
				fixed : 'right',
				title : '操作',
				align : 'center',
				toolbar : '#optBar',
				width: 150
			} ] ],
			done : function(res, curr, count) {
				// 如果是异步请求数据方式，res即为你接口返回的信息。
				// 如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
				pageCurr = curr;
			}
		});
		// 监听工具条
		table.on('tool(onlineTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				//delOnlineStaff(data, data.id, data.taskNo);
			} else if (obj.event === 'edit') {
				// 编辑
				getOnlineStaff(data, data.id);
				
			}
		});
		
		// 监听搜索框
		form.on('submit(searchSubmit)', function(data) {
			// 重新加载table
			load(data);
			return false;
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
		
		// 编辑上线人员信息
		function getOnlineStaff(obj, id) {
			var param = {
				"id" : id
			};
			CoreUtil.sendAjax("produce/online/getMain", JSON
					.stringify(param), function(data) {
				if (data.result) {
					console.log(data)
//					form.val("devForm", {
//						"id" : data.data.id
//					});
					getMainInfo(id);
					//openDevClock(id, "编辑卡机信息")
				} else {
					layer.alert(data.msg)
				}
			}, "POST", false, function(res) {
				layer.alert("操作请求错误，请您稍后再试");
			});
		}
		// 编辑上线人员信息
		function getMainInfo(id) {
			var param = {
				"id" : id
			};
			CoreUtil.sendAjax("produce/online/getMainInfo", JSON
					.stringify(param), function(data) {
				if (data.result) {
					console.log(data)
//					form.val("devForm", {
//						"id" : data.data.id
//					});		
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
function openOnlineStaff(id, title) {
	if (id == null || id == "") {
		$("#id").val("");
	}
	
	var index = layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : false,// 是否点击遮罩关闭
		area : [ '1000px' ],
		content : $('#setOnlineStaff'),
		macmin : true,// 弹出框全屏
		end : function() {
			cleanOnlineStaff();
		}
	});
	layer.full(index);// 弹出框全屏
}
//重新加载表格（搜索）
function load(obj) {
	console.log(obj)
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
//// 添加上线人员信息
//function addOnlineStaff() {
//	// 清空弹出框数据
//	cleanOnlineStaff();
//	getEmp();
//	getDev();
//	// 打开弹出框
//	openOnlineStaff(null, "添加上线人员信息");
//}



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
function cleanOnlineStaff() {
	$('#onlineForm')[0].reset();
	layui.form.render();// 必须写
}