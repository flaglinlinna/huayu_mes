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
				url : context + '/quoteAll/getList',
				method : 'get' // 默认：get请求
				//, toolbar: '#toolbar' //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
				,cellMinWidth : 80,
				height:'full-65'//固定表头&full-查询框高度
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
					//填写数据
					
					var num_list = res.data.Nums;
					var all = 0;
					res.data.Nums.forEach(function (item, index) {
						if(item.STATUS == '2'){
							$('#over-num').text('已完成('+item.NUMS+')');
						}else{
							$('#in-num').text('进行中('+item.NUMS+')');
						}
						all = Number(all)+Number(item.NUMS);
					});
					$('#all-num').text('全部('+all+')');
					// 可进行数据操作
					return {
						"count" : res.data.List.total,
						"msg" : res.msg,
						"data" : res.data.List.rows,
						"code" : res.status
					}
				},
				cols : [ [ 
				           {type : 'numbers'},
							 
				 {field : 'bsCode',title : '报价单编号',width : 150,sort: true}, 
				 {field : 'bsType',title : '报价类型', width : 100},
				 {field : 'bsStatus',title : '状态',width : 341,templet:'#statusTpl'}, 
				 {
					field : 'bsFinishTime',
					title : '计划完成日期',
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
					},{field : 'bsLevel',title : '防水防尘等级',width : 140},
					  {field : 'bsCustRequire',
						title : '客户其他要求',width : 200}, {
						fixed : 'right',
						title : '操作',
						//align : 'center',
						toolbar : '#optBar',
						width : 150
					}
				] ],
				done : function(res, curr, count) {
					//
					pageCurr = curr;
				}
			});	
			// 监听工具条
			table.on('tool(listTable)', function(obj) {
				var data = obj.data;
				if (obj.event === 'del') {
					// 关闭
					//del(data, data.id, data.custNo);
				} else if (obj.event === 'edit') {
					// 编辑
					open("编辑项目资料")
				}else if(obj.event === 'view'){
					parent.layui.index.openTabsPage(context+'/quoteSum/toQuoteSum?quoteId='+data.id,'报价单汇总详情');
				}else if(obj.event === 'check'){
					//先判断是否填写完成资料-暂时未校验-20201218-fyx
					if(true){
						layer.open({
		                    type: 2,
		                    title:'审批',
		                    area: ['750px', '550px'],
		                    fixed: false,
		                    maxmin: true,
		                    //content: '../../views/iframe/check.html',
		                    content: context+'/check/toCheckQuote',
		                    success: function (layero, index) {
		                    	 // 获取子页面的iframe
		                        var iframe = window['layui-layer-iframe' + index];
		                        // 向子页面的全局函数child传参，流程编码
		                        iframe.child("QUOTE",data.id,data.bsStatus);
		                    },end: function () {
		                    	tableIns.reload({
		                    	});
		                    }
		                  });
					}
				}
			});
			
			$("#_search_btn").click(function () {
				$(".searchDiv").toggle();
                //var val=$(this).attr("id");
            })
            $("#ul-list li").click(function () {
                    $(this).addClass("current").siblings().removeClass();
                })
		});
});
//编辑项目弹出框
function open(title) {
	
	var index =layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#setItemPage'),
		end : function() {
			
		}
	});
	layer.full(index)
}

