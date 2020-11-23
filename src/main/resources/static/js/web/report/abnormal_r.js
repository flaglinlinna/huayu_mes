/**
 * 工时异常统计表
 */
var pageCurr;var localtableFilterIns;
$(function() {
	layui.use([ 'form', 'table','tableFilter','laydate', 'tableSelect' ], function() {
		var table = layui.table, form = layui.form,tableFilter = layui.tableFilter,laydate = layui.laydate, tableSelect = layui.tableSelect;

		tableIns = table.render({
			elem : '#listTable',
		//	url : context + '/base/line/getList',
			method : 'get' // 默认：get请求
			, toolbar: '#toolbar' //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
			,cellMinWidth : 80,
			height:'full-180'//固定表头&full-查询框高度
				,even:true,//条纹样式
			data : [],
			height: 'full',
			page : false,
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
			//,{type:'checkbox'}
			// ,{field:'id', title:'ID', width:80, unresize:true, sort:true}
			 {
				field : 'EMP_CODE',
				title : '工号',width : 140, sort: true
			},
			 {
				field : 'EMP_NAME',
				title : '员工姓名',width : 130,
				sort: true
			}, {
				field : 'LINER_NAME',
				title : '组长姓名', width : 100,sort: true
			}
			, {
				field : 'TASK_NO',
				title : '制令单号',
				 sort: true
					, width : 200
			}, {
				field : 'WOEK_DATE',
				title : '工作日期',width : 140, sort: true
			},
				{
					field : 'TIME_BEGIN',
					title : '上线时间',width : 160, sort: true
				},
				{
					field : 'TIME_END',
					title : '下线时间',width : 160, sort: true
				},

			] ],
			done : function(res, curr, count) {
				//
				pageCurr = curr;
			}
		});	
//		 localtableFilterIns = tableFilter.render({
//			'elem' : '#listTable',
//			//'parent' : '#doc-content',
//			'mode' : 'local',//本地过滤
//			'filters' : [
//				{field: 'ITEM_NO', type:'input'},
//				{field: 'LINER_NAME', type:'input'},
//				{field: 'QTY_PLAN', type:'input'},
//				{field: 'QTY_DONE', type:'input'},
//				{field: 'QTY_PROC', type:'input'},
//				{field: 'CAPACITY', type:'input'},
//				{field: 'MANPOWER', type:'input'},
//				{field: 'HOUR_SDD', type:'input'},
//				{field: 'QTY_EMP', type:'input'},
//			],
//			'done': function(filters){}
//		});
		getLiner();
		getEmpCode();
		
		tableSelect = tableSelect.render({// 返工扫描-制令单
			elem : '#taskno',
			searchKey : 'keyword',
			checkedKey : 'id',
			searchPlaceholder : '试着搜索',
			table : {
				url : context + '/report/abnormal_r/getTaskNo',
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
					field : 'TASK_NO',
					title : '制令单号',sort: true
				}, {
					field : 'ITEM_NO',
					title : '物料编码',sort: true
				}, {
					field : 'CUST_NAME_S',
					title : '客户简称'
				} ] ],
				parseData : function(res) {
					//console.log(res)
					if (res.result) {
						// 可进行数据操作
						return {
							"count" : 0,
							"msg" : res.msg,
							"data" : res.data,
							"code" : res.status
						// code值为200表示成功
						}
					}

				},
			},
			done : function(elem, data) {
				// console.log(data)
				var da = data.data;
				form.val("searchFrom", {
					"taskno" : da[0].TASK_NO
				});
				form.render();// 重新渲染
			}
		});
		
		// 监听搜索框
		form.on('submit(searchSubmit)', function(data) {
			// 重新加载table
			console.log(data.field)
			var date=data.field.dates;
			var sdata = date.substring(0, date.indexOf(" "))
			var edata = date.substring(date.indexOf(" ") + 3, date.length);
			console.log(sdata)
			console.log(edata)
			
			var params = {
					"sdate" : sdata,
					"edate":edata,
					"empCode" : data.field.empCode,
					"liner" : data.field.liner,
					"taskno" : data.field.taskno
				};
			console.log(params)
			getReport(params)
			return false;
		});
		//日期范围
		  laydate.render({
		    elem: '#dates',
		    trigger:'click'
		    ,range: true
		  });

	});

});
function getReport(params) {	
	CoreUtil.sendAjax("/report/abnormal_r/getList", JSON.stringify(params),
			function(data) {
				console.log(data)
				if (data.result) {
					if (data.result) {
						tableIns.reload({
							data : data.data.rows,
							done: function(res1, curr, count){
								
								////localtableFilterIns.reload();
							}
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

function getLiner(){
	CoreUtil.sendAjax("/report/abnormal_r/getLiner", "",
			function(data) {
				//console.log(data)
				if (data.result) {
					if (data.result) {
						$("#liner").empty();
						var list=data.data;
						for (var i = 0; i < list.length; i++) {
							if(i==0){
								$("#liner").append("<option value=''>请点击选择</option>");
							}
							$("#liner").append("<option value=" + list[i].ID+ ">" + list[i].LEAD_BY + "</option>");
						}				
						layui.form.render('select');
					} else {
						layer.alert(data.msg);
					}
				} else {
					layer.alert(data.msg);
				}
			}, "GET", false, function(res) {
				layer.alert(res.msg);
			});
}
function getEmpCode(){
	CoreUtil.sendAjax("/report/abnormal_r/getEmpCode", "",
			function(data) {
				//console.log(data)
				if (data.result) {
					if (data.result) {
						$("#empCode").empty();
						var list=data.data;
						for (var i = 0; i < list.length; i++) {
							if(i==0){
								$("#empCode").append("<option value=''>请点击选择</option>");
							}
							$("#empCode").append("<option value=" + list[i].EMP_CODE+ ">"+ list[i].EMP_CODE+"——"+ list[i].EMP_NAME + "</option>");
						}				
						layui.form.render('select');
					} else {
						layer.alert(data.msg);
					}
				} else {
					layer.alert(data.msg);
				}
			}, "GET", false, function(res) {
				layer.alert(res.msg);
			});
}
