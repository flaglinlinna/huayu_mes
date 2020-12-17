/**
 * 报价项目清单
 */
var pageCurr;
$(function() {
	layui.use([ 'table', 'form', 'layedit', 'laydate', 'layer' ],
		function() {var form = layui.form, layer = layui.layer, 
		laydate = layui.laydate, table = layui.table;
						// 按钮监听事件
		setData();
		tableIns = table.render({
			elem : '#listTable',
			url : context + '/quote/getItemPage',
			method : 'get' // 默认：get请求
			//, toolbar: '#toolbar' //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
			,cellMinWidth : 80,
			height:'full-200',//固定表头&full-查询框高度
			//	,even:true,//条纹样式
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
					"count" : res.count,
					"msg" : res.msg,
					"data" : res.data,
					"code" : res.status
				}
			},
			cols : [ [ {
				type : 'numbers'
			},
			 {
				field : 'bsStatus',
				title : '状态',width : 120,
				templet:function (d) {
					if(d.bsStatus="0"){
						return "未开始"
					}else if(d.bsStatus="1"){
						return "未完成"
					}else if(d.bsStatus="2"){
						return "已完成"
					}
				}
			}, {
				field : 'bsCode',
				title : '项目编号', width : 100
			},
			 {
				field : 'bsName',
				title : '项目名称'
			}
			, {
				field : 'bsPerson',
				title : '待办人',width : 150
			},{
					fixed : 'right',
					title : '操作',
					align : 'center',
					toolbar : '#optBar',
					width : 120
				}
			] ],
			done : function(res, curr, count) {
				//
				pageCurr = curr;
			}
		});			
	});
});

function setData(){
	console.log(info)
	var data_info=info.data;
	$("#header1").text("报价单号："+data_info.bsCode)
	$("#header2").text("报价类型："+data_info.bsType)
	$("#header3").text("产品型号："+data_info.bsProd)
	$("#header4").text("创建时间："+data_info.createDate)
}
