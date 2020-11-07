/**
 * 线体管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table','tableFilter','laydate' ], function() {
		var table = layui.table, form = layui.form,tableFilter = layui.tableFilter,laydate = layui.laydate;

		tableIns = table.render({
			elem : '#listTable',
		//	url : context + '/base/line/getList',
			method : 'get' // 默认：get请求
			, toolbar: '#toolbar' //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
			,cellMinWidth : 80,
			data : [],
			height: 'full',
			page : true,
			request : {
				pageName : 'page' // 页码的参数名称，默认：page
				,
				limitName : 'rows' // 每页数据量的参数名，默认：limit
			},
			parseData : function(res) {
				if(!res.result){
					return {
						"count" : 0,
						"msg" : res.msg,
						"data" : [],
						"code" : res.status
					} 
				}
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
			},
			{type:'checkbox'}
			// ,{field:'id', title:'ID', width:80, unresize:true, sort:true}
			, {
				field : 'lineNo',
				title : '线体编码', sort: true,filter: true
			}, {
				field : 'lineName',
				title : '线体名称', sort: true
			},
			{
				field : 'linerCode',
				title : '线长工号', sort: true
			}, {
				field : 'linerName',
				title : '线长姓名', sort: true
			}
			, {
				field : 'checkStatus',
				title : '状态',
				width : 95,
				templet : '#statusTpl', sort: true
			}, {
				field : 'lastupdateDate',
				title : '更新时间', sort: true
			}, {
				field : 'createDate',
				title : '添加时间', sort: true
			}, {
				fixed : 'right',
				title : '操作',
				align : 'center',
				toolbar : '#optBar'
			} ] ],
			done : function(res, curr, count) {
				//localtableFilterIns.reload();
				pageCurr = curr;
			}
		});	

		// 监听搜索框
		form.on('submit(searchSubmit)', function(data) {
			// 重新加载table
			console.log(data);
			//getReport(begin, end,dept,item)
			return false;
		});
		//日期选择器
		laydate.render({ 
		  elem: '#beginDate',
		  type: 'date' //默认，可不填
		});
		laydate.render({ 
			  elem: '#endDate',
			  type: 'date' //默认，可不填
			});
		// 获取部门
		function getDept() {
			CoreUtil.sendAjax("report/batch/getDeptInfo",
					function(data) {
						if (data.result) {
							
						} else {
							layer.alert(data.msg)
						}
					}, "POST", false, function(res) {
						layer.alert("操作请求错误，请您稍后再试");
					});
		}
	});

});

function getReport(begin, end,dept,item) {	
	var params = {
		"beginTime" : begin,
		"endTime" : end,
		"deptId" : dept,
		"itemNo" : item,
	}
	CoreUtil.sendAjax("/report/batch/getCheckBatchReport", JSON.stringify(params),
			function(data) {
				console.log(data)
				if (data.result) {
					if (data.result) {
						tableIns.reload({
							data : data.data
						});
					} else {
						layer.alert(data.msg);
					}
				} else {
					layer.alert(data.msg);
				}
			}, "POST", false, function(res) {
				layer.alert(res.msg);
			});
}

