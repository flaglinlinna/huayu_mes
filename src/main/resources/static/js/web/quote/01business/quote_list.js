/**
 * 报价单-列表
 */
var pageCurr;
$(function() {
	layui.use([ 'table', 'form', 'layedit', 'laydate', 'layer','tableFilter'  ],
		function() {
			var form = layui.form, layer = layui.layer, laydate = layui.laydate, table = layui.table,tableFilter = layui.tableFilter;
			tableIns = table.render({
				elem : '#listTable',
				url : context + '/quote/getList?quoteId='+quoteId+'&status=',
				method : 'get' // 默认：get请求
				//, toolbar: '#toolbar' //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
				,cellMinWidth : 80,
				height:'full-65'//固定表头&full-查询框高度
					,even:true,//条纹样式
				data : [],
				// height: 'full',
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
						if(item.STATUS == '0'){
							$('#in-num').text('进行中('+item.NUMS+')');
						}else if(item.STATUS == '1'){
							$('#over-num').text('已完成('+item.NUMS+')');
						}else if(item.STATUS == '3'){
							$('#unCommit-num').text('待提交审批('+item.NUMS+')');
						}else if(item.STATUS == '4'){
							$('#approval-num').text('审批中('+item.NUMS+')');
						}else if(item.STATUS == '99'){
							$('#close-num').text('已关闭('+item.NUMS+')');
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
				cols : [ [ {fixed:'left',type : 'numbers'},
				 {fixed:'left',field : 'bsCode',title : '报价单编号',width : 140,sort: true},
					{fixed:'left',field : 'bsProd',title : '产品型号',width : 110,sort: true},
					{fixed:'left',field : 'bsProjVer',title : '版本',width : 100,sort: true},
				 {fixed:'left',field : 'bsStatus',title : '状态',width : 90,templet:function (d) {
						if(d.bsStatus=="0"){
							return "进行中"
						}else if(d.bsStatus=="1"){
							return "已完成"
						}else if(d.bsStatus=="3"){
							return "待提交审批"
						}else if(d.bsStatus=="4"){
							return "审批中"
						}else if(d.bsStatus=="5"){
							return "被驳回"
						} else if(d.bsStatus=="99"){
							return "已关闭"
						}
				},sort: true},
					// {field : 'bsLatest',title : '是否最新',width : 100,sort: true,templet:function (d) {
					// 		if(d.bsLatest == "1"){
					// 			return "是";
					// 		}else {
					// 			return "否";
					// 		}
					// 	}},
					{field : 'bsProdType',title : '产品类型',width : 110, sort: true},
					{field : 'bsCustManage',title : '客户经理',width : 120,sort: true},
					{field : 'userName',title : '创建人',width : 90, sort: true},
				{field : 'bsCustName',title : '客户名称',width : 120,sort: true},
					{field : 'bsType',title : '报价类型', width : 120,templet:function (d) {
							if(d.bsType=="YSBJ"){
								return "衍生报价";
							}else if(d.bsType =="XPBJ"){
								return "新品报价"
							}else {
								return "";
							}
						},sort: true},
					{field : 'bsStage',title : '报价阶段',width : 100,sort: true},
				// {field : 'bsDevType',title : '机种型号',width : 140, sort: true},
				{field : 'bsFinishTime',title : '完成日期',sort: true, width : 140,sort: true},
					{field : 'bsPosition',title : '市场定位',width : 120,sort: true},
					{field : 'bsManageFee',title : '管理费率(%)',width : 120,sort: true},
				{field : 'bsSimilarProd',title : '相似型号',width : 150,sort: true},
				{field : 'bsRemarks',title : '报价备注',width : 170,sort: true},

				{field : 'bsMaterial',title : '客户提供资料',width : 140, sort: true},
				{field : 'bsChkOutItem',title : '外观检验项',width : 140, sort: true},
				{field : 'bsChkOut',title : '外观检验',width : 150,sort: true},
				{field : 'bsFunctionItem',title : '功能性能项',width : 140,sort: true},
				{field : 'bsFunction',title : '功能性能',width : 140,sort: true},
				{field : 'bsRequire',title : '环保要求',width : 140,sort: true},
				{field : 'bsLevel',title : '防水防尘等级',width : 140,sort: true},
				{field : 'bsCustRequire',title : '客户其他要求',width : 200,sort: true},
				// {field : 'bsBade',title : '是否中标',width : 140,sort: true},
				// {field : 'bsProjVer',title : '版本',width : 140,sort: true},

				// {field : 'bsStage',title : '报价阶段',width : 200,sort: true},
				{field : 'createDate',title : '创建时间',width : 145, sort: true},
				{fixed : 'right',title : '操作',toolbar : '#optBar',width : 280}
				] ],
				done : function(res, curr, count) {
					localtableFilterIns.reload();
					pageCurr = curr;
					res.data.forEach(function (item, index) {
						$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsStatus"]').css('color', '#fff');
						if(item.bsStatus == 0){
							$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsStatus"]').css('background-color', '#7ED321');
						}else if(item.bsStatus=="1"){
							$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsStatus"]').css('background-color', '#F5A623');
							// console.log($('div[lay-id="listTable"]').find("div[class='layui-table-body']").find('tr[data-index="' + index + '"]').find('td[data-field="25"]').find(['a[name="pushBtn"]']));
							$('div[lay-id="listTable"]').find("div[class='layui-table-fixed layui-table-fixed-r']").find("div[class='layui-table-body']").find('tr[data-index="' + index + '"]').find('td[data-field="25"]').find('a[name="pushBtn"]').html("取消下发");
						}else if(item.bsStatus=="3"){
							$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsStatus"]').css('background-color', '#6495ED');
						}else if(item.bsStatus=="4"){
							$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsStatus"]').css('background-color', '#4169E1');
						}else if(item.bsStatus=="5"){
							$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsStatus"]').css('background-color', '#FF0000');
						} else if(item.bsStatus=="99"){
							$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsStatus"]').css('background-color', '#979797');
						}
					});
				}
			});	
			var localtableFilterIns = tableFilter.render({
				'elem' : '#listTable',
				'mode' : 'api',//服务端过滤
				'filters' : [
					{field: 'bsCode', type:'input'},
					{field: 'bsType', type:'checkbox', data:[{ "key":"YSBJ", "value":"衍生报价"},{ "key":"XPBJ", "value":"新品报价"}]},
					// {field: 'bsStatus', type:'checkbox', data:[{ "key":"0", "value":"进行中"},{ "key":"1", "value":"已完成"},{ "key":"99", "value":"已关闭"}]},
					{field: 'bsFinishTime', type:'date'},
					{field: 'bsRemarks', type:'input'},
					{field: 'bsProd', type:'input'},
					{field: 'bsSimilarProd', type:'input'},
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
					{field: 'bsCustRequire', type:'input'},
					{field: 'userName', type:'input'},
					// {field: 'createDate', type:'date'},
				],
				'done': function(filters){
				}
			})
			// 监听工具条
			table.on('tool(listTable)', function(obj) {
				var data = obj.data;
				if (obj.event === 'del') {// 关闭				
					layer.confirm('您确定要关闭报价单：【' + data.bsCode + '】吗？', {
						btn : [ '确认', '返回' ]
					}, function() {
						del(data.id);
					});	
				} else if (obj.event === 'edit') {// 编辑
					parent.layui.index.openTabsPage(context+'/quote/toQuoteAdd?quoteId='+data.id+'&status='+data.bsStatus,'修改报价单');
				}else if(obj.event === 'view') {
					var titleInfo = "("+data.bsCode.substring(data.bsCode.length-4)+")";
					var srcUrl = context+'/quote/toQuoteItem?quoteId='+data.id+'&style=item';
					parent.layui.index.openTabsPage(context + '/quote/toQuoteItem?quoteId=' + data.id + '&style=item', '报价项目清单'+titleInfo);
					//刷新
					($(window.parent.document).find(('iframe[src="'+srcUrl+'"]'))).attr('src',srcUrl);
				}
				else if(obj.event === 'check'){
					//先判断报价单是否被关闭
					if(data.bsStatus == "99"){
						layer.msg('报价单已关闭，不允许审批!', {
							time: 5000, //2s后自动关闭
							btn: ['知道了']
						});
					}else {
						//先判断是否填写完成资料-fyx-20210105
						if (data.bsStatusCheck > 1) {
							layer.open({
								type: 2,
								title: '报价单核查审批('+data.bsCode+")",
								area: ['600px', '480px'],
								fixed: false,
								maxmin: true,
								//content: '../../views/iframe/check.html',
								content: context + '/check/toCheck',
								success: function (layero, index) {
									// 获取子页面的iframe
									var iframe = window['layui-layer-iframe' + index];
									// 向子页面的全局函数child传参，流程编码
									if (data.bsStatus == '1') {
										//报价单销售部已完成，传入end ,让提交按钮失效
										iframe.child("QUOTE_NEW", data.id, 'end');
									} else {
										iframe.child("QUOTE_NEW", data.id, 'check');
									}
								}, end: function () {
									loadAll();
								}
							});
						} else {
							layer.msg('资料未填写完毕，不允许审批!', {
								time: 5000, //2s后自动关闭
								btn: ['知道了']
							});
						}
					}
				}else if(obj.event ==='copy'){
					//复制报价单
					parent.layui.index.openTabsPage(context+'/quote/toQuoteAdd?quoteId='+data.id+'&bsCopyId='+data.id,'复制报价单');
				}else if(obj.event ==='push'){
					if(data.bsStatus == "99"){
						layer.msg('报价单已关闭，不允许下发修改!', {
							time: 5000, //2s后自动关闭
							btn: ['知道了']
						});
					}else if(data.bsStatus==1) {
						layer.confirm('你确认取消下发：【' + data.bsCode + '】的信息吗？', {
							btn: ['确认', '返回']
						}, function () {
							push(data.id);
						});
					}else {
						layer.confirm('您确认信息填写完全并下发：【' + data.bsCode + '】的信息吗？', {
							btn: ['确认', '返回']
						}, function () {
							push(data.id);
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
                    //alert($("span:last",this).attr("data-status"));
                    tableIns.reload({
                		url:context + '/quote/getList?status='+$("span:last",this).attr("data-status")
                	});
             })

			form.on('submit(searchSubmit)', function(data) {
				// 重新加载table
				load(data);
				return false;
			});

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

//关闭报价单
function del(id){
	var param = {
			"id" : id,
			"bsStatus":'99'
		};
	CoreUtil.sendAjax("/quote/doStatus", JSON.stringify(param),
			function(data) {
				if (data.result) {
					layer.alert("操作成功",function(){
						layer.closeAll();
						loadAll();
					})
				} else {
					layer.alert(data.msg)
				}
			}, "POST", false, function(res) {
				layer.alert("操作请求错误，请您稍后再试");
			});
}

//下推报价单
function push(id){
	var param = {
		"id" : id,
		// "bsStatus":'99'
	};
	CoreUtil.sendAjax("/quote/doPush", JSON.stringify(param),
		function(data) {
			if (data.result) {
				layer.alert(data.msg,function(){
					layer.closeAll();
					loadAll();
				})
			} else {
				layer.alert(data.msg)
			}
		}, "POST", false, function(res) {
			layer.alert("操作请求错误，请您稍后再试");
		});
}



function loadAll() {
	// 重新加载table
	tableIns.reload({
		page : {
			curr : pageCurr
		// 从当前页码开始
		}
	});
}
