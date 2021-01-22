/**
 * 产品工艺流程
 */
var pageCurr;
var totalCount = 0;// 表格记录数
$(function() {
	layui.use([ 'form', 'table', 'tableSelect' ], function() {
		var table = layui.table, form = layui.form, tableSelect = layui.tableSelect;
		isComplete()
		tableIns = table.render({
			elem : '#client_procList',
			url : context + '/quoteProcess/getList?pkQuote='+ quoteId,
			method : 'get', // 默认：get请求
			cellMinWidth : 80,
			height : 'full-65',
			even : true,// 条纹样式
			page : true,
			request : {
				pageName : 'page', // 页码的参数名称，默认：page
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
			cols : [ [ {type : 'numbers'},
			// {field:'id', title:'ID', width:80, unresize:true, sort:true},
			{field : 'bsName',title : '零件名称',style : 'background-color:#d2d2d2'},
			{field : 'procNo',title : '工序编码',templet : '<div>{{d.proc.procNo}}</div>',style : 'background-color:#d2d2d2'},
			{field : 'procName',title : '工序名称',templet : '<div>{{d.proc.procName}}</div>',style : 'background-color:#d2d2d2'},
			{field : 'workCenter',title : '工作中心',templet : '<div>{{d.proc.bjWorkCenter.workcenterName}}</div>',style : 'background-color:#d2d2d2'},
			{field : 'bsOrder',title : '工序顺序',"edit" : "number","event" : "dataCol",width : 80,style : 'background-color:#ffffff'},
			{field : 'bsFeeLh',title : '是否已维护人工制费',width : 140,style : 'background-color:#d2d2d2',align : 'center',
				templet : function(d) {
					if (d.bsFeeLh == null || d.bsFeeLh == '') {return "<div class='orange'>否</div>"} 
					else {return "<div class='green'>是</div>"}
			}}, 
			{field : 'fmemo',title : '备注',"edit" : "number","event" : "dataCol",style : 'background-color:#ffffff',
				templet : function(d) {
					if (d.fmemo == null) {return ""} else {return d.fmemo}
			}}, 
			{fixed : 'right',title : '操作',width : 100,align : 'center',toolbar : '#optBar'
			} ] ],
			done : function(res, curr, count) {
				// console.log(res)
				totalCount = res.count
				pageCurr = curr;
				merge(res.data, [ 'bsName', ], [ 1, 1 ]);
			}
		});

		tableSelect = tableSelect.render({
			elem : '#num',
			searchKey : 'keyword',
			checkedKey : 'id',
			searchPlaceholder : '试着搜索',
			table : {
				url : context + '/quoteProcess/getBomList?quoteId='+quoteId,
				method : 'get',
				// width:800,
				cols : [ [
					{type : 'numbers', title : '序号'},
					{type : 'radio'},
					{field : 'ID', title : 'id', width : 0, hide : true},
					{field : 'BS_ELEMENT', title : '组件名称', width : 160},
					{field : 'BS_COMPONENT', title : '零件名称', width : 160},
					{field : 'WORKCENTER_NAME', title : '工作中心', width : 160},
				] ],
				page : true,
				request : {
					pageName : 'page' // 页码的参数名称，默认：page
					,
					limitName : 'rows' // 每页数据量的参数名，默认：limit
				},
				parseData : function(res) {
					if (res.result) {
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
				var da = data.data;
				form.val("clientProcForm", {
					"num" : da[0].BS_COMPONENT,
					"bsElement":da[0].BS_ELEMENT,
					"wcName":da[0].WORKCENTER_NAME
				});
				form.render();// 重新渲染

				if(da[0].BS_COMPONENT){
					getAddList(da[0].PK_BJ_WORK_CENTER);
					getListByQuoteAndName(da[0].BS_COMPONENT);
				}else{
					tableProcCheck.reload({
						data:[]
					});
				}
			}
		});


		var tip_index = 0;
		$(document).on('mouseover', '#save-btn', function(data) {
			tip_index = layer.tips("<span style='font-size:13px;line-height:20px;'>如果有未维护基础信息的人工制费费用，请先联系IT部填写好基础信息后再确认完成</span>", ($(this)), {
				tips : [ 3, '5CBA59' ],
				time : 0,
				time : 0,
				area : [ '200px' ]
			});

		}).on('mouseleave', '#save-btn', function() {
			layer.close(tip_index);
		});

		// 监听搜索框
		form.on('submit(searchSubmit)', function(data) {
			// 重新加载table
			load(data);
			return false;
		});

		tableProc = table.render({
			elem : '#procList',
			limit : 20,
			page:true,
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
			method : 'get',// 默认：get请求
			cols : [ [ {type : 'numbers'}
			, {field : 'checkColumn',type:"checkbox"},
			/*{field : 'procOrder',title : '序号',width:80}, */
			// {field : 'PROC_NO',title : '工序编码', minWidth: 80},
			{field : 'PROC_NAME',title : '工序名称', minWidth: 100}, 
			// {field : 'WORKCENTER_NAME',title : '工作中心', minWidth: 120},
			{field : 'STATUS',title : '是否维护人工制费', minWidth: 140,templet:'<div>{{d.STATUS=="0"?"否":"是"}}</div>'},
			{type: 'toolbar',title: '操作',width: 70,align : 'center',toolbar: '#clickBar'
	        }] ],
			data:[]
		});	
		
		tableProcCheck=table.render({
			elem : '#procListCheck',
			limit: 20,
			method : 'get' ,// 默认：get请求			
			cols : 
				[ [
					// {type : 'numbers'},
				    {field : 'checkColumn',type:"checkbox"},
			        // {field : 'bsName',title : '零件名称',style:'background-color:#d2d2d2'},
			        // {field : 'procNo',title : '工序编码',templet:'<div>{{d.proc.procNo}}</div>',style:'background-color:#d2d2d2'},
			        {field : 'procName',title : '工序名称',minWidth:100,templet:'<div>{{d.proc.procName}}</div>',style:'background-color:#d2d2d2'},
			        // {field : 'workCenter',title : '工作中心',minWidth:100,templet:'<div>{{d.proc.bjWorkCenter.workcenterName}}</div>',style:'background-color:#d2d2d2'},
			        {field : 'bsOrder',title : '工序顺序',"edit":"number","event": "dataCol",minWidth:80,style : 'background-color:#ffffff'},
			        {type: 'toolbar',title: '操作',width: 70,align : 'center',toolbar: '#optBar'}
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
		    if(field == 'bsOrder'){
		    	//判断是否为数字
		    	if(isRealNum(value)){
		    		doProcOrder(data.id,value);
		    	}else{
		    		layer.msg('请填写数字!');
		    		loadAll();
		    	}
		    }
		    if(field == 'fmemo'){
		    	//console.log(data.id,value)
		    	doFmemo(data.id,value)
		    }
		  });
		  table.on('edit(procListCheck)', function(obj){
			    var value = obj.value //得到修改后的值
			    ,data = obj.data //得到所在行所有键值
			    ,field = obj.field; //得到字段
			   // layer.msg('[ID: '+ data.id +'] ' + field + ' 字段更改为：'+ value);
			    var tr = obj.tr;
		        // 单元格编辑之前的值
		        var oldtext = $(tr).find("td[data-field='"+obj.field+"'] div").text();
			    if(field == 'bsOrder'){
			    	//判断是否为数字
			    	if(isRealNum(value)){
			    		doProcOrder(data.id,value);
			    	}else{
			    		layer.msg('请填写数字!');
			    		loadAll();
			    	}
			    }
			  });


		// 监听单元格编辑
		table.on('edit(client_procTable)', function(obj) {
			var value = obj.value // 得到修改后的值
			, data = obj.data // 得到所在行所有键值
			, field = obj.field; // 得到字段
			// layer.msg('[ID: '+ data.id +'] ' + field + ' 字段更改为：'+ value);
			var tr = obj.tr;
			// 单元格编辑之前的值
			var oldtext = $(tr).find("td[data-field='" + obj.field + "'] div").text();
			if (field == 'bsOrder') {
				// 判断是否为数字
				if (isRealNum(value)) {
					doProcOrder(data.id, value);
				} else {
					layer.msg('请填写数字!');
					loadAll();
				}
			}
			if (field == 'fmemo') {
				// console.log(data.id,value)
				doFmemo(data.id, value)
			}
		});


		// 监听工具条
		table.on('tool(client_procTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delClientProc(data.id,'list',"");
			} else if (obj.event === 'edit') {
				// 编辑
				// getClientProc(data, data.id);//未写
				// addProc(data.custId)
			}
		});
		table.on('tool(procListCheck)', function(obj) {
			var data = obj.data;
			var tbData = table.cache.procListCheck; //是一个Array
			if (obj.event === 'del') {
				// 删除
				delClientProc(data.id,'in',data.bsName);
			} 
			if (obj.event === 'moveUp') {
				// 上移
				var tr = $(this).parent().parent().parent();
				if ($(tr).prev().html() == null) {
					layer.msg("已经是最顶部了");
					return;
				} else {
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
				} else {
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

		table.on('tool(procTable)', function(obj) {
			 var checkValue=$("#num").val();
			 var bsElement=$("#bsElement").val();
			 if(checkValue){
				 var data = obj.data;
					var tbData = table.cache.procList; //是一个Array
					if (obj.event == 'doClick') {
						addSubmit(data.ID,checkValue,bsElement);
					}
			 }else{
				 layer.msg('请先选择零件', {
		              time: 20000, //20s后自动关闭
		              btn: ['知道了']
		            });
			 }
			
			/*if (obj.event === 'moveUp') {
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

			}*/
		});

		form.on('submit(dels)', function() {
			var checkdata = layui.table.checkStatus("procListCheck").data;
			var ids = "";
			var bsName = "";
			for(var i = 0;i<checkdata.length;i++){
				ids = ids+ checkdata[i].id+',';
			}
			if(ids ==""){
				return false;
			}else {
				bsName = checkdata[0].bsName;
			}
			delClientProc(ids,"in",bsName);
			return false;
		});

		// 监听提交
		form.on('submit(addSubmit)', function(data) {
			var bsElement=$("#bsElement").val();
			var checkStatus = table.cache.procList;
			var procIdList = "";
			$('#clientProcForm tbody tr td[data-field="checkColumn"] input[type="checkbox"]').each(function(i) {
				if ($(this).is(":checked")) {
					// fyx-202011-02
					var checks = $('tbody tr[data-index="' + i + '"] td[data-field="jobAttr"] input[type="checkbox"]:checked');
					procIdList += checkStatus[i].ID + ",";
				}
			});
			if (data.field.num == "") {
				layer.msg('请先选择零件', {
		              time: 20000, //20s后自动关闭
		              btn: ['知道了']
		            });
				return false;
			}
			console.log(data.field)
			// addSubmit(procIdList,data.field.itemId);

			addSubmit(procIdList, data.field.num,bsElement);

			//提交后清空勾选
			$('#clientProcForm tbody tr td[data-field="checkColumn"] input[type="checkbox"]').prop('checked',false);
			//取消全选框的勾选
			$("input[type='checkbox'][name='layTableCheckbox']").prop('checked', false);
			form.render('checkbox');
			return false;

		});

		//零件切换
		// form.on('select(num)', function(data){
		// 	if(data.value){
		// 		getListByQuoteAndName(data.value);
		// 	}else{
		// 		tableProcCheck.reload({
		// 			data:[]
		// 		});
		// 	}
		// });


		// 设置工序顺序
		function doProcOrder(id, procOrder) {
			var param = {
				"id" : id,
				"procOrder" : procOrder
			};
			CoreUtil.sendAjax("/quoteProcess/doProcOrder", JSON.stringify(param), function(data) {
				loadAll();
			}, "POST", false, function(res) {
				layer.alert("操作请求错误，请您稍后再试", function() {
					layer.closeAll();
				});
			});
		}
		// 填写备注
		function doFmemo(id, fmemo) {
			var param = {
				"id" : id,
				"fmemo" : fmemo
			};
			CoreUtil.sendAjax("/quoteProcess/doFmemo", JSON.stringify(param), function(data) {
				loadAll();
			}, "POST", false, function(res) {
				layer.alert("操作请求错误，请您稍后再试", function() {
					layer.closeAll();
				});
			});
		}
	});
});

function isComplete() {
	if (iStatus == 2) {
		$("#addbtn").addClass("layui-btn-disabled").attr("disabled", true)
		$("#savebtn").addClass("layui-btn-disabled").attr("disabled", true)
	}
}

// 添加工艺流程
function addProc() {

	if (bomNameList.data < 1) {
		layer.alert("该报价单未维护外购件清单数据")
		return false;
	}
	// 获取初始化信息
	getAddList("");
	tableProcCheck.reload({
		data:[]
	});
	// 打开弹出框
	openProc(null, "添加工艺流程");
}
function saveProc() {
	if (totalCount == 0) {
		layer.alert("当前模块无数据，“确认提交”不可用")
		return false;
	}
	// console.log(quoteId)
	var param = {
		"quoteId" : quoteId,
		"code" : code
	};
	layer.confirm('一经提交则不得再修改，确定要提交吗？', {
		btn : [ '确认', '返回' ]
	}, function() {
		CoreUtil.sendAjax("/quoteProcess/doStatus", JSON.stringify(param), function(data) {
			if (data.result == true) {
				// 回调弹框
				layer.alert("提交成功！");
				//刷新页面
				iStatus=2;
				isComplete();
				loadAll()
			} else {
				layer.msg(data.msg, {
					time : 2000, // 2s后自动关闭
					btn : [ '知道了' ]
				});
			}
		});
	});
}

function delClientProc(id,type,bsName) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/quoteProcess/delete", JSON.stringify(param), function(data) {
				if (data.result == true) {
					if(type == 'list'){
						// 回调弹框
						layer.alert("删除成功！", function() {
							layer.closeAll();
							// 加载load方法
							loadAll();
						});
					}else{
						// 回调弹框
						layer.alert("删除成功！", function(index) {
							layer.close(index);
							getListByQuoteAndName(bsName);
						});
					}
					
				} else {
					layer.alert(data, function() {
						layer.closeAll();
					});
				}
			});
		});
	}
}

// 新增工艺流程提交
function addSubmit(procIdlist, itemIds,bsElement) {
	var params = {
		"proc" : procIdlist,
		"itemId" : itemIds,
		"quoteId" : quoteId,
		"bsElement":bsElement
	};

	CoreUtil.sendAjax("/quoteProcess/add", JSON.stringify(params), function(data) {

		if (data.result) {
			getListByQuoteAndName(itemIds);
			/*layer.alert("操作成功", function() {
				layer.closeAll();
				cleanProc();
				// 加载页面
				loadAll();
			});*/
		} else {
			layer.alert(data.msg);
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}

function getListByQuoteAndName(name){
	CoreUtil.sendAjax("/quoteProcess/getListByQuoteAndName", {'quoteId':quoteId,'name':name}, function(data) {
		tableProcCheck.reload({
			data:data.data,
			done : function(res, curr, count) {
				//提交后清空勾选
				$('#clientProcForm tbody tr td[data-field="checkColumn"] input[type="checkbox"]').prop('checked',false);
				//取消全选框的勾选
				$("input[type='checkbox'][name='layTableCheckbox']").prop('checked', false);
				layui.form.render('checkbox');
			}
		});
	}, "GET", false, function(res) {
		layer.alert("操作请求错误，请您稍后再试", function() {
			layer.closeAll();
		});
	});
}

// 获取工序列表
function getAddList(val) {
	// console.log(bomNameList)
	tableProc.reload({
		// data : data.data,
		url:context + "/quoteProcess/getAddList",
		where : {
			"pkWcId" :val
		},
		done : function(res, curr, count) {
			// cleanProc();// 清空之前的选中
		}
	});

}
function merge(res, columsName, columsIndex) {
	var data = res;
	var mergeIndex = 0;// 定位需要添加合并属性的行数
	var mark = 1; // 这里涉及到简单的运算，mark是计算每次需要合并的格子数
	// var columsName = ['itemCode'];//需要合并的列名称
	// var columsIndex = [3];//需要合并的列索引值
	for (var k = 0; k < columsIndex.length; k++) { // 这里循环所有要合并的列
		var trArr = $(".layui-table-body>.layui-table").find("tr");// 所有行
		for (var i = 1; i < data.length; i++) { // 这里循环表格当前的数据
			var tdCurArr = trArr.eq(i).find("td").eq(columsIndex[k]);// 获取当前行的当前列
			var tdPreArr = trArr.eq(mergeIndex).find("td").eq(columsIndex[k]);// 获取相同列的第一列
			if (data[i][columsName[0]] === data[i - 1][columsName[0]]) { // 后一行的值与前一行的值做比较，相同就需要合并
				mark += 1;
				tdPreArr.each(function() {// 相同列的第一列增加rowspan属性
					$(this).attr("rowspan", mark);
				});
				tdCurArr.each(function() {// 当前行隐藏
					$(this).css("display", "none");
				});
			} else {
				mergeIndex = i;
				mark = 1;// 一旦前后两行的值不一样了，那么需要合并的格子数mark就需要重新计算
			}
		}
		mergeIndex = 0;
		mark = 1;
	}
}
// 新增编辑弹出框
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
		// area : [ '800px','410px'],
		content : $('#setClientProc'),
		end : function() {
			cleanProc();
			loadAll();
		}
	});
	layer.full(index);
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
function cleanProc() {
	$('#clientProcForm')[0].reset();
	layui.form.render();// 必须写
}