/**
 * 报价单-列表
 */
var pageCurr;
$(function() {
	layui.use([ 'table', 'form', 'layedit', 'laydate', 'layer','tableFilter' ],
		function() {
			var form = layui.form, layer = layui.layer, laydate = layui.laydate, table = layui.table,tableFilter = layui.tableFilter;
			tableIns = table.render({
				elem : '#listTable',
				url : context + '/quoteAll/getList',
				method : 'get' ,// 默认：get请求
				//, toolbar: '#toolbar' //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
				cellMinWidth : 80,
				height:'full-65',//固定表头&full-查询框高度
				even:true,//条纹样式
				data : [],
				page : true,
				limit: 50,
				limits: [50,100,200,300,500],
				request : {
					pageName : 'page', // 页码的参数名称，默认：page
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
				cols : [
					[
				{fixed:'left',type : 'numbers',	rowspan : 2,colspan : 1,},
				{fixed:'left',field : 'bsCode',title : '报价单编号',width : 150,sort: true,	rowspan : 2,colspan : 1,},
					{fixed:'left',field : 'bsProd',title : '产品型号',width : 120,sort: true,	rowspan : 2,colspan : 1,},
					{fixed:'left',fixed:'left',field : 'bsProjVer',title : '版本',width : 100,sort: true,	rowspan : 2,colspan : 1,},
					{field : 'bsStatus',title : '状态',width : 300,templet:'#statusTpl',sort: true,	rowspan : 2,colspan : 1,},
				{field : 'bsType',title : '报价类型', width : 120,templet:function (d) {
						 if(d.bsType=="YSBJ"){
							 return "衍生报价";
						 }else if(d.bsType =="XPBJ"){
							 return "新品报价"
						 }else {
							 return "";
						 }
				},sort: true,	rowspan : 2,colspan : 1},

				{field : 'bsCustName',title : '客户名称',width : 120,sort: true,	rowspan : 2,colspan : 1,},

				{field : 'bsProdType',title : '产品类型',width : 140, sort: true,	rowspan : 2,colspan : 1,},
				{field : 'bsDevType',title : '机种型号',width : 140, sort: true,	rowspan : 2,colspan : 1,},
				{field : 'bsFinishTime',title : '完成日期',sort: true, width : 140,	rowspan : 2,colspan : 1,},
				{field : 'bsSimilarProd',title : '相似型号',width : 150,sort: true,	rowspan : 2,colspan : 1,},

				{field : 'bsSimilarProd',title : '五金资料评估',align : 'center',width : 150,sort: true,	rowspan : 1,colspan : 4,},
				{field : 'bsSimilarProd',title : '注塑资料评估',align : 'center',width : 150,sort: true,	rowspan : 1,colspan : 4,},
				{field : 'bsSimilarProd',title : '表面处理资料评估',align : 'center',width : 150,sort: true,	rowspan : 1,colspan : 4,},
				{field : 'bsSimilarProd',title : '组装资料评估',align : 'center',width : 150,sort: true,	rowspan : 1,colspan : 4,},
				{field : 'bsRemarks',title : '报价备注',width : 170,sort: true,	rowspan : 2,colspan : 1},

				{field : 'bsPosition',title : '市场定位',width : 150,sort: true,	rowspan : 2,colspan : 1},
				{field : 'bsMaterial',title : '客户提供资料',width : 140, sort: true,	rowspan : 2,colspan : 1},
				{field : 'bsChkOutItem',title : '外观检验项',width : 140, sort: true,	rowspan : 2,colspan : 1},
				{field : 'bsChkOut',title : '外观检验',width : 150,sort: true,	rowspan : 2,colspan : 1},
				{field : 'bsFunctionItem',title : '功能性能项',width : 140,sort: true,	rowspan : 2,colspan : 1},
				{field : 'bsFunction',title : '功能性能',width : 140,sort: true,	rowspan : 2,colspan : 1},
				{field : 'bsRequire',title : '环保要求',width : 140,sort: true,	rowspan : 2,colspan : 1},
				{field : 'bsLevel',title : '防水防尘等级',width : 140,sort: true,	rowspan : 2,colspan : 1},
				{field : 'bsCustRequire',title : '客户其他要求',width : 200,sort: true,	rowspan : 2,colspan : 1},
				//{fixed : 'right',title : '操作',toolbar : '#optBar',width : 150}
				],[
						{field : 'B001',title : '材料是否报价',width : 150,sort: true,	rowspan : 1,colspan : 1},
						{field : 'B001Time',title : '材料填写时间',width : 150,sort: true,	rowspan : 1,colspan : 1,templet: function (d){
							if(d.B001Time>=4){
								return '<div class="progress_bar"> <span>'+d.B001Time+'</span></div>'
							}else {
								return '<div class="progress_bar2"> <span>'+d.B001Time+'</span></div>'
							}
							}},
						{field : 'C001',title : '工艺是否报价',width : 150,sort: true,	rowspan : 1,colspan : 1},
						{field : 'C001Time',title : '工艺填写时间',width : 150,sort: true,	rowspan : 1,colspan : 1,templet: function (d){
								if(d.C001Time>=4){
									return '<div class="progress_bar"> <span>'+d.C001Time+'</span></div>'
								}else {
									return '<div class="progress_bar2"> <span>'+d.C001Time+'</span></div>'
								}
							}},
						{field : 'B002',title : '材料需要报价',width : 150,sort: true,	rowspan : 1,colspan : 1},
						{field : 'B002Time',title : '材料填写时间',width : 150,sort: true,	rowspan : 1,colspan : 1,templet: function (d){
								if(d.B002Time>=4){
									return '<div class="progress_bar"> <span>'+d.B002Time+'</span></div>'
								}else {
									return '<div class="progress_bar2"> <span>'+d.B002Time+'</span></div>'
								}
							}},
						{field : 'C001',title : '工艺是否报价',width : 150,sort: true,	rowspan : 1,colspan : 1},
						{field : 'C002Time',title : '工艺填写时间',width : 150,sort: true,	rowspan : 1,colspan : 1,templet: function (d){
								if(d.C002Time>=4){
									return '<div class="progress_bar"> <span>'+d.C002Time+'</span></div>'
								}else {
									return '<div class="progress_bar2"> <span>'+d.C002Time+'</span></div>'
								}
							}},
						{field : 'B003',title : '材料是否报价',width : 150,sort: true,	rowspan : 1,colspan : 1},
						{field : 'B003Time',title : '材料填写时间',width : 150,sort: true,	rowspan : 1,colspan : 1,templet: function (d){
								if(d.B003Time>=4){
									return '<div class="progress_bar"> <span>'+d.B003Time+'</span></div>'
								}else {
									return '<div class="progress_bar2"> <span>'+d.B003Time+'</span></div>'
								}
							}},
						{field : 'C001',title : '工艺是否报价',width : 150,sort: true,	rowspan : 1,colspan : 1},
						{field : 'C003Time',title : '工艺填写时间',width : 150,sort: true,	rowspan : 1,colspan : 1,templet: function (d){
								if(d.C003Time>=4){
									return '<div class="progress_bar"> <span>'+d.C003Time+'</span></div>'
								}else {
									return '<div class="progress_bar2"> <span>'+d.C003Time+'</span></div>'
								}
							}},
						{field : 'B004',title : '材料是否报价',width : 150,sort: true,	rowspan : 1,colspan : 1},
						{field : 'B004Time',title : '材料填写时间',width : 150,sort: true,	rowspan : 1,colspan : 1,templet: function (d){
								if(d.B004Time>=4){
									return '<div class="progress_bar"> <span>'+d.B004Time+'</span></div>'
								}else {
									return '<div class="progress_bar2"> <span>'+d.B004Time+'</span></div>'
								}
							}},
						{field : 'C001',title : '工艺是否报价',width : 150,sort: true,	rowspan : 1,colspan : 1},
						{field : 'C004Time',title : '工艺填写时间',width : 150,sort: true,	rowspan : 1,colspan : 1,templet: function (d){
								if(d.C004Time>=4){
									return '<div class="progress_bar"> <span>'+d.C004Time+'</span></div>'
								}else {
									return '<div class="progress_bar2"> <span>'+d.C004Time+'</span></div>'
								}
							}},
					]
				],
				done : function(res, curr, count) {
					//
					pageCurr = curr;
					localtableFilterIns.reload();
				}
			});

			var localtableFilterIns = tableFilter.render({
				'elem' : '#listTable',
				'mode' : 'api',//服务端过滤
				'filters' : [
					{field: 'bsCode', type:'input'},
					{field: 'bsType', type:'checkbox', data:[{ "key":"YSBJ", "value":"衍生报价"},{ "key":"XPBJ", "value":"新品报价"}]},
					// {field: 'bsStatus', type:'checkbox', data:[{ "key":"0", "value":"进行中"},{ "key":"1", "value":"已完成"},{ "key":"99", "value":"已关闭"}]},
					// {field: 'bsFinishTime', type:'date'},
					{field: 'bsRemarks', type:'input'},
					{field: 'bsProd', type:'input'},
					{field: 'bsSimilarProd', type:'input'},
					{field: 'bsFinishTime', type:'date'},
					{field: 'bsDevType', type:'checkbox'},
					{field: 'bsProdType', type:'checkbox'},
					{field: 'bsCustName', type:'input'},
					{field: 'bsPosition', type:'checkbox'},
					{field: 'bsLevel', type:'checkbox'},
					{field: 'bsRequire', type:'checkbox', data:[{ "key":"RoHS", "value":"RoHS"},
							{ "key":"RECAH", "value":"RECAH"},
							{ "key":"PAHS", "value":"PAHS"},{ "key":"CA65", "value":"CA65"}
							,{ "key":"3BPA", "value":"3BPA"},{ "key":"HFS", "value":"HFS"}
							,{ "key":"无卤", "value":"无卤"},{ "key":"其他", "value":"其他"}]},
					{field: 'bsCustRequire', type:'input'}
				],
				'done': function(filters){}
			})
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
					var titleInfo = "("+data.bsCode.substring(data.bsCode.length-4)+")";
					parent.layui.index.openTabsPage(context+'/quoteSum/toQuoteSum?quoteId='+data.id,'报价单汇总详情'+titleInfo);
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

			form.on('submit(searchSubmit)', function(data) {
				// 重新加载table
				load(data);
				return false;
			});
			
			$("#_search_btn").click(function () {
				$(".searchDiv").toggle();
                //var val=$(this).attr("id");
            })
			$("#ul-list li").click(function () {
				$(this).addClass("current").siblings().removeClass();

				tableIns.reload({
					url:context + '/quoteAll/getList?status='+$("span:last",this).attr("data-status"),
				});
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

// 重新加载表格（搜索）
function load(obj) {
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