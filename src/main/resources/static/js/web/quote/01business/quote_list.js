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
				url : context + '/quote/getList',
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
					field : 'bsStatus',
					title : '状态',width : 100
				},
				 {
					field : 'bsCode',
					title : '报价单编号',width : 150,
					sort: true
				}, {
					field : 'bsType',
					title : '报价类型', width : 100
				}
				, {
					field : 'bsFinishTime',
					title : '完成日期',
					 sort: true
					, width : 140
				}, {
					field : 'bsRemarks',
					title : '报价备注',width : 170
				},
					{
						field : 'bsProd',
						title : '产品型号',width : 120
					},
					{
						field : 'bsSimilarProd',
						title : '相似型号',width : 150
					},
					{
						field : 'bsDevType',
						title : '机种型号',width : 140, sort: true
					},
					{
						field : 'bsProdType',
						title : '产品类型',width : 140, sort: true
					},{
						field : 'bsCustName',
						title : '客户名称',width : 120
					},
					{
						field : 'bsPosition',
						title : '市场定位',width : 150
					},
					{
						field : 'bsMaterial',
						title : '客户提供资料',width : 140, sort: true
					},
					{
						field : 'bsChkOutItem',
						title : '外观检验项',width : 140, sort: true
					},{
						field : 'bsChkOut',
						title : '外观检验',width : 150
					},
					{
						field : 'bsFunctionItem',
						title : '功能性能项',width : 140
					},
					{
						field : 'bsFunction',
						title : '功能性能',width : 140
					},{
						field : 'bsRequire',
						title : '环保要求',width : 140
					},
					{
						field : 'bsLevel',
						title : '防水防尘等级',width : 140
					},{
						field : 'bsCustRequire',
						title : '客户其他要求',width : 200
					}
				] ],
				done : function(res, curr, count) {
					//
					pageCurr = curr;
				}
			});	
		});
});

