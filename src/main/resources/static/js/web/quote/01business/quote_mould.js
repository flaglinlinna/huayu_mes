/**
 * 产品工艺流程
 */
var pageCurr;
var totalCount=0;//表格记录数
$(function() {
	layui.use([ 'form', 'table' , 'tableSelect' ], function() {
		var table = layui.table, form = layui.form, tableSelect = layui.tableSelect;
		tableIns = table.render({
			elem : '#client_procList',
			url : context + '/quoteMould/getList?pkQuote='+ quoteId,
			method : 'get' // 默认：get请求
			, toolbar: '#toolbar',
			cellMinWidth : 80,
			height: 'full-65'
			,even:true,//条纹样式
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
				field : 'bsName',
				title : '组件名称',
				style:'background-color:#d2d2d2'
			},{
				field : 'bsMoCode',
				title : '模具编码',
				templet:'<div>{{d.mjProcFee.productCode}}</div>',
				style:'background-color:#d2d2d2'
			},{
				field : 'bsMoName',
				title : '模具名称',
				templet:'<div>{{d.mjProcFee.productName}}</div>',
				style:'background-color:#d2d2d2'
			}, {
				field : 'bsMoFee',
				title : '模具成本',
				templet:'<div>{{d.mjProcFee.feeAll}}</div>',
				style:'background-color:#d2d2d2'
			}, {
				field : 'stQuote',
				title : '参考报价',
				templet:'<div>{{d.mjProcFee.stQuote}}</div>',
				style:'background-color:#d2d2d2'
			},{
				field : 'bsActQuote',
				title : '实际报价',"edit":"number","event": "dataCol",
				width:80
			}, {
				fixed : 'right',
				title : '操作',
				width:120,
				align : 'center',
				toolbar : '#optBar'
			} ] ],
			done : function(res, curr, count) {
				console.log(res)
				totalCount=res.count
				pageCurr = curr;
				merge(res.data,['bsName',],[1,1]);
			}
		});
		/*
		tableSelect=tableSelect.render({
			elem : '#num',
			searchKey : 'keyword',
			checkedKey : 'id',
			searchPlaceholder : '试着搜索',
			table : {
				url:  context +'/quoteMould/getBomList',
				method : 'get',
				cols : [ [
				{ type: 'radio' },//多选 checkbox
				, {
					field : 'ID',
					title : 'id',
					width : 0,hide:true
				},{
					field : 'bsComponent',
					title : '零件名称',
					width : 120,
				},{
					field : 'bsElement',
					title : '组件名称',
					width : 110,
				},{
					field : 'bsMaterName',
					title : '材料名称',
					width : 130,
				},{
					field : 'bsModel',
					title : '材料规格',
					width : 150,
				}, ] ],
				page : false,
				request : {
					pageName : 'page', // 页码的参数名称，默认：page
					limitName : 'rows' // 每页数据量的参数名，默认：limit
				},
				parseData : function(res) {
					console.log(res)
					if(res.result){
						// 可进行数据操作
						return {
							"count" : res.data.total,
							"msg" : res.msg,
							"data" : res.data.rows,
							"code" : res.status
						// code值为200表示成功
						}
					}
					
				},
			},
			done : function(elem, data) {
				//选择完后的回调，包含2个返回值 elem:返回之前input对象；data:表格返回的选中的数据 []
				//console.log(data);
				var da=data.data;
				//console.log(da[0].num)
				var ids = '';var nos = "";
				data.data.forEach(function(element) {
					element_id = element.id;
					bsComponent = element.bsComponent;
				});
				form.val("clientProcForm", {
					"itemId":element_id,
					"num" : bsComponent
				});
				form.render();// 重新渲染
		}
		});*/
		// 监听搜索框
		form.on('submit(searchSubmit)', function(data) {
			// 重新加载table
			load(data);
			return false;
		});

		tableProc=table.render({
			elem : '#procList',
			limit: 20,
			method : 'get' ,// 默认：get请求			
			cols : [ [ {
				type : 'numbers'
			}
			// ,{field:'id', title:'ID', width:80, unresize:true, sort:true}
			, 
			{field : 'checkColumn',type:"checkbox",},
			{field : 'productCode',title : '产品编码', minWidth: 250},
			{field : 'productName',title : '产品名称', minWidth: 200},
			{field : 'stQuote',title : '参考报价', minWidth: 100},
			{field : 'feeAll',title : '评估总费用（含税）', minWidth: 200},
			{field : 'structureMj',title : '模具结构', minWidth: 200},
			//{type: 'toolbar',title: '操作',width: 160,align : 'center',toolbar: '#moveBar'}
			] ],
			data:[]
		});	
		
		
		//监听单元格编辑
		  table.on('edit(client_procTable)', function(obj){
		    var value = obj.value //得到修改后的值
		    ,data = obj.data //得到所在行所有键值
		    ,field = obj.field; //得到字段
		   // layer.msg('[ID: '+ data.id +'] ' + field + ' 字段更改为：'+ value);
		    var tr = obj.tr;
	        // 单元格编辑之前的值
	        var oldtext = $(tr).find("td[data-field='"+obj.field+"'] div").text();
		    if(field == 'bsActQuote'){
		    	//判断是否为数字
		    	if(isRealNum(value)){
		    		doActQuote(data.id,value);
		    	}else{
		    		layer.msg('请填写数字!');
		    		loadAll();
		    	}
		    }
		  });
		
		// 监听工具条
		table.on('tool(client_procTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delClientProc(data.id);
			} else if (obj.event === 'edit') {
				// 编辑
				//getClientProc(data, data.id);//未写
				//addProc(data.custId)
			}
		});
		table.on('tool(procTable)', function(obj) {
			var data = obj.data;
			var tbData = table.cache.procList; //是一个Array
			if (obj.event === 'moveUp') {
				// 上移
				var tr = $(this).parent().parent().parent();
				if ($(tr).prev().html() == null) {
			        layer.msg("已经是最顶部了");
			        return;
			    }else{
			        // 未上移前，记录本行和下一行的数据
			        var tem = tbData[tr.index()];
			        var tem2 = tbData[tr.prev().index()];
			 
			        // 将本身插入到目标tr之前
			        $(tr).insertBefore($(tr).prev());
			        // 上移之后，数据交换
			        tbData[tr.index()] = tem;
			        tbData[tr.next().index()] = tem2;
			    }

			} else if (obj.event === 'moveDown') {
				// 下移
				var tr = $(this).parent().parent().parent();
			    if ($(tr).next().html() == null) {
			        layer.msg("已经是最底部了");
			        return;
			    } else{
			        // 记录本行和下一行的数据
			        var tem = tbData[tr.index()];
			        var tem2 = tbData[tr.next().index()];
			        // 将本身插入到目标tr的后面
			        $(tr).insertAfter($(tr).next());
			        // 交换数据
			        tbData[tr.index()] = tem;
			        tbData[tr.prev().index()] = tem2;
			    }

			}
		});
		
		// 监听提交
		form.on('submit(addSubmit)', function(data) {
			var checkStatus = table.cache.procList;
			var procIdList="";
			$('#clientProcForm tbody tr td[data-field="checkColumn"] input[type="checkbox"]').each(function(i){
		        if ($(this).is(":checked")) {
		        	//fyx-202011-02
		        	var checks = $('tbody tr[data-index="'+i+'"] td[data-field="jobAttr"] input[type="checkbox"]:checked');

		        	procIdList += checkStatus[i].id+",";
				}
		    });
			if(data.field.num==""){
				alert("请选择零件")
				return false;
			}
			//console.log(data.field.num)
			//addSubmit(procIdList,data.field.itemId);
			addSubmit(procIdList,data.field.num);
			return false;
			
		});
		
		// 设置工序顺序
		function doActQuote(id, actQuote) {
			var param = {
					"id" : id,
					"actQuote" : actQuote
				};
				CoreUtil.sendAjax("/quoteMould/doActQuote", JSON
						.stringify(param), function(data) {
					layer.alert(data.msg, function() {
						layer.closeAll();
					});
					loadAll();
				}, "POST", false, function(res) {
					layer.alert("操作请求错误，请您稍后再试", function() {
						layer.closeAll();
					});
				});
		}
	});
});
//添加工艺流程
function add() {
	
	if(bomNameList.data<1){
		layer.alert("该报价单未维护外购件清单数据")
		return false;
	}
	// 获取初始化信息
	getAddList();
	// 打开弹出框
	openProc(null, "添加工艺流程");
}
function save(){
	if(totalCount==0){
		layer.alert("当前模块无数据，“确认提交”不可用")
		return false;
	}
	var param = {"quoteId" : quoteId ,"code":code};
	layer.confirm('一经提交则不得再修改，确定要提交吗？', {
		btn : [ '确认', '返回' ]
	}, function() {
		CoreUtil.sendAjax("/quoteMould/doStatus", JSON.stringify(param),
				function(data) {
			console.log(data)
					if (isLogin(data)) {
						if (data.result == true) {
							// 回调弹框
							layer.alert("提交成功！");
							loadAll();
						} else {
							layer.alert(data.msg);
						}
					}
				});
	});
}
//设定-不需要报价
function doNoNeed(){
	if(totalCount>0){
		layer.alert("当前模块已有数据，“不需要报价”不可用")
		return false;
	}
	
	var param = {"quoteId" : quoteId ,"code":code};
	layer.confirm('一经提交则不得再修改，确定要提交吗？', {
		btn : [ '确认', '返回' ]
	}, function() {
		CoreUtil.sendAjax("/quoteMould/doNoNeed", JSON.stringify(param),
				function(data) {
			console.log(data)
					if (isLogin(data)) {
						if (data.result == true) {
							// 回调弹框
							layer.alert("提交成功！");
							loadAll();
						} else {
							layer.alert(data.msg);
						}
					}
				});
	});
}

function delClientProc( id) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/quoteMould/delete", JSON.stringify(param),
					function(data) {
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

//新增工艺流程提交
function addSubmit(procIdlist,itemIds) {
	var params = {
			"proc":procIdlist,
			"itemId" : itemIds,
			"quoteId":quoteId
		};

	CoreUtil.sendAjax("/quoteMould/add", JSON.stringify(params), function(
			data) {
		//console.log(data)
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanProc();
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

//获取工序列表
function getAddList(){
	console.log(bomNameList)
	
	CoreUtil.sendAjax("/quoteMould/getMouldList", "",
			function(data) {
		console.log(data)
				if (data.result) {
					//工序表
					tableProc.reload({
						data:data.data,
						done : function(res, curr, count) {
							cleanProc();//清空之前的选中
						}
					});
				} else {
					layer.alert(data.msg)
				}
				//console.log(data)
			}, "GET", false, function(res) {
				layer.alert("操作请求错误，请您稍后再试");
			});
	//零件下拉框
	$("#num").empty();
	var bomlist=bomNameList.data;
	console.log(bomlist)
	for(var i=0;i<bomlist.length;i++){
		if(i==0){
				$("#num").append("<option value=''> 请选择</option>");  
		}
		if(bomlist[i]){//为空的不显示
			$("#num").append(
					"<option value='" + bomlist[i].BS_ELEMENT + "'>"
							+ bomlist[i].BS_ELEMENT +"</option>");
		} 
	}
	layui.form.render('select');
}
function merge(res,columsName,columsIndex) {
    var data = res;
    var mergeIndex = 0;//定位需要添加合并属性的行数
    var mark = 1; //这里涉及到简单的运算，mark是计算每次需要合并的格子数
    //var columsName = ['itemCode'];//需要合并的列名称
    //var columsIndex = [3];//需要合并的列索引值
    for (var k = 0; k < columsIndex.length; k++) { //这里循环所有要合并的列
        var trArr = $(".layui-table-body>.layui-table").find("tr");//所有行
            for (var i = 1; i < data.length; i++) { //这里循环表格当前的数据
                var tdCurArr = trArr.eq(i).find("td").eq(columsIndex[k]);//获取当前行的当前列
                var tdPreArr = trArr.eq(mergeIndex).find("td").eq(columsIndex[k]);//获取相同列的第一列
                if (data[i][columsName[0]] === data[i-1][columsName[0]]) { //后一行的值与前一行的值做比较，相同就需要合并
                	mark += 1;
                    tdPreArr.each(function () {//相同列的第一列增加rowspan属性
                        $(this).attr("rowspan", mark);
                    });
                    tdCurArr.each(function () {//当前行隐藏
                        $(this).css("display", "none");
                    });
                }else {
                    mergeIndex = i;
                    mark = 1;//一旦前后两行的值不一样了，那么需要合并的格子数mark就需要重新计算
                }
            }
        mergeIndex = 0;
        mark = 1;
    }
}
//新增编辑弹出框
function openProc(id, title) {
	if (id == null || id == "") {
		$("#id").val("");
	}
	var index = layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		//area : [ '800px','410px'],
		content : $('#setClientProc'),
		end : function() {
			cleanProc();
		}
	});
	layer.full(index);
}
//重新加载表格（搜索）
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

//重新加载表格（全部）
function loadAll() {
	// 重新加载table
	tableIns.reload({
		page : {
			curr : pageCurr
		// 从当前页码开始
		}
	});
}
//清空新增表单数据
function cleanProc() {
	$('#clientProcForm')[0].reset();
	layui.form.render();// 必须写
}