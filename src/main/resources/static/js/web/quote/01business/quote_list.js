/**
 * 报价单-列表
 */
var pageCurr;
$(function() {
	layui.use([ 'table', 'form', 'layedit', 'laydate', 'layer' ],
		function() {
			var form = layui.form, layer = layui.layer, laydate = layui.laydate, table = layui.table;
			tableIns = table.render({
				elem : '#listTable',
			//	url : context + '/base/line/getList',
				method : 'get' // 默认：get请求
				, toolbar: '#toolbar' //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
				,cellMinWidth : 80,
				height:'full-180'//固定表头&full-查询框高度
					,even:true,//条纹样式
				data : [],
				// height: 'full',
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
					}
				},
				cols : [ [ {
					type : 'numbers'
				},
				//,{type:'checkbox'}
				// ,{field:'id', title:'ID', width:80, unresize:true, sort:true}
				 {
					field : 'EMP_CODE',
					title : '工号',width : 100, sort: true
				},
				 {
					field : 'EMP_NAME',
					title : '员工姓名',width : 100,
					sort: true
				}, {
					field : 'LINER_NAME',
					title : '组长姓名', width : 100,sort: true
				}
				, {
					field : 'TASK_NO',
					title : '制令单号',
					 sort: true
						, width : 170
				}, {
					field : 'WORK_DATE',
					title : '工作日期',width : 140, sort: true
				},
					{
						field : 'TIME_BEGIN',
						title : '上线时间',width : 150, sort: true
					},
					{
						field : 'TIME_END',
						title : '下线时间',width : 150, sort: true
					},
					{
						field : 'ITEM_NO',
						title : '物料编号',width : 140, sort: true
					},
					{
						field : 'ITEM_NAME',
						title : '物料名称',width : 140, sort: true
					},


				] ],
				done : function(res, curr, count) {
					//
					pageCurr = curr;
				}
			});	
		});
});

