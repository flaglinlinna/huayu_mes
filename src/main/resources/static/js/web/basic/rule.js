/**
 * 校验规则管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table','tableSelect' ], function() {
		var table = layui.table, form = layui.form,tableSelect = layui.tableSelect;

		tableSelect=tableSelect.render({
			elem : '#itemId',
			searchKey : 'keyword',
			checkedKey : 'id',
			searchPlaceholder : '内部编码搜索',
			table : {
				url : context + '/base/rule/getMtrialListPage',
				method : 'post',

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
				cols : [ [
					{ type: 'radio' },//单选  radio
					{
						field : 'id',
						title : 'id',
						width : 0,hide:true
					},
					{
						field : 'itemNo',
						title : '物料编码'
					},
					{
						field : 'itemName',
						title : '物料名称'
					}
				] ],
				page : true,
				request : {
					pageName : 'page' // 页码的参数名称，默认：page
					,
					limitName : 'rows' // 每页数据量的参数名，默认：limit
				},

			},
			done : function(elem, data) {
				var da=data.data;
				//选择完后的回调，包含2个返回值 elem:返回之前input对象；data:表格返回的选中的数据 []
				form.val("ruleForm", {
					"itemNo":da[0].itemNo,
					"itemId":da[0].itemNo,
					"itemName":da[0].itemName,
				});
				form.render();// 重新渲染
			}
		});

		tableIns = table.render({
			elem : '#ruleList',
			url : context + '/base/rule/getList',
			method : 'get' // 默认：get请求
			,
			cellMinWidth : 80,
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
			, {
				field : 'itemNo',
				title : '内部物料编号',
				width : 140

			}, {
				field : 'itemName',
				title : '物料描述',
				width : 400
			}, {
				field : 'itemNoCus',
				title : '客户物料编码',
				width : 130
			}, {
				field : 'itemNoInside',
				title : '华勤物料编码',
				width : 130
			}, {
				field : 'positionBegin',
				title : '开始位置',
				width : 80
			}, {
				field : 'positionEnd',
				title : '结束位置',
				width : 80
			}, {
				field : 'chkString',
				title : '验证数据',
				width : 170
			}, {
				field : 'barcodeLen',
				title : '条码长度',
				width : 80
			}, {
				field : 'lastupdateDate',
				title : '更新时间',
				templet:'<div>{{d.lastupdateDate?DateUtils.formatDate(d.lastupdateDate):""}}</div>',
				width : 150
			}, {
				field : 'createDate',
				title : '添加时间',
				templet:'<div>{{d.createDate?DateUtils.formatDate(d.createDate):""}}</div>',
				width : 150
			}, {
				fixed : 'right',
				title : '操作',
				align : 'center',
				toolbar : '#optBar',
				width : 120
			} ] ],
			done : function(res, curr, count) {
				// 如果是异步请求数据方式，res即为你接口返回的信息。
				// 如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
				// console.log(res);
				// 得到当前页码
				// console.log(curr);
				// 得到数据总量
				// console.log(count);
				pageCurr = curr;
			}
		});
		form.verify({
			  intValue: [
			   /^\+?[1-9][0-9]*$/
			    ,'此项数据应大于0且不含小数点'
			  ] 
			});  
		// form.on('select(itemNo)', function(obj){
		// 	var text=obj.elem[obj.elem.selectedIndex].text;
		// 	$("#itemNo").val(text);
		// 	var itemName=obj.value;
		// 	console.log(itemName)
		// 	itemName=itemName.slice(itemName.indexOf("=")+1);
		// 	$("#itemName").val(itemName);
		// });
		// 监听工具条
		table.on('tool(ruleTable)', function(obj) {
			var data = obj.data;
			console.log(obj)
			if (obj.event === 'del') {
				// 删除
				delBarcodeRule(data, data.id, data.itemNo);
			} else if (obj.event === 'edit') {
				// 编辑
				getBarcodeRule(data, data.id);
			}
		});
		// 监听提交
		form.on('submit(addSubmit)', function(data) {
			if (data.field.id == null || data.field.id == "") {
				// 新增
				addSubmit(data);
			} else {
				editSubmit(data);
			}
			return false;
		});
		// 监听搜索框
		form.on('submit(searchSubmit)', function(data) {
			// 重新加载table
			load(data);
			return false;
		});
		// 编辑校验规则
		function getBarcodeRule(obj, id) {
			var param = {
				"id" : id
			};
			CoreUtil.sendAjax("/base/rule/getBarcodeRule",
					JSON.stringify(param), function(data) {
						if (data.result) {
							form.val("ruleForm", {
								"id" : data.data.id,
								"itemId":data.data.itemNo,
								"itemNo" : data.data.itemNo,
								"itemName" : data.data.mtrial.itemName,
								"itemNoCus" : data.data.itemNoCus,
								"itemNoInside" : data.data.itemNoInside,
								"positionBegin" : data.data.positionBegin,
								"positionEnd" : data.data.positionEnd,
								"chkString" : data.data.chkString,
								"barcodeLen" : data.data.barcodeLen,
							});
							var item_id=data.data.itemId;
							$('#itemId').attr('ts-selected',data.data.itemId);
							openBarcodeRule(id, "编辑校验规则",item_id)
						} else {
							layer.alert(data.msg)
						}
					}, "POST", false, function(res) {
						layer.alert("操作请求错误，请您稍后再试");
					});
		}
	});
});

// 新增编辑弹出框
function openBarcodeRule(id, title,item_id) {
	if (id == null || id == "") {
		$("#id").val("");
	}
	// getMtrial(item_id);
	layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#setBarcodeRule'),
		end : function() {
			cleanBarcodeRule();
		}
	});
}

// 添加校验规则
function addBarcodeRule() {
	// 清空弹出框数据
	cleanBarcodeRule();
	// 打开弹出框
	openBarcodeRule(null, "添加校验规则",null);
}
// 新增校验规则提交
function addSubmit(obj) {
	// var str=obj.field.itemId
	var str= $('#itemId').attr('ts-selected');
	// str=str.slice(0,str.indexOf("="));
	obj.field.itemId=str;
	//console.log(obj)
	CoreUtil.sendAjax("/base/rule/add", JSON.stringify(obj.field),
			function(data) {
				if (data.result) {
					layer.alert("操作成功", function() {
						layer.closeAll();
						cleanBarcodeRule();
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

// 编辑校验规则提交
function editSubmit(obj) {
	// var str=obj.field.itemId
	// str=str.slice(0,str.indexOf("="));
	// obj.field.itemId=str;
	var str= $('#itemId').attr('ts-selected');
	obj.field.itemId=str;
	CoreUtil.sendAjax("/base/rule/edit", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanBarcodeRule();
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

// 删除校验规则
function delBarcodeRule(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除' + name + '校验规则吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/base/rule/delete", JSON.stringify(param),
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
// function getMtrial(item_id) {
// 	CoreUtil.sendAjax("/base/rule/getMtrialList", "", function(data) {
// 		if (data.result) {
// 			//console.log(data)
// 			$("#itemId").empty();
// 			var item=data.data;
// 			for (var i = 0; i < item.length; i++) {
// 				if(i==0){
// 					$("#itemId").append("<option value=''>请点击选择</option>");
// 				}
// 				var value=item[i].id+'='+item[i].itemName
// 				if(item[i].id==item_id){
// 					$("#itemId").append("<option selected value=" +value+">" + item[i].itemNo + "</option>");
// 					$("#itemName").val(item[i].itemName);
// 				}else{
// 					$("#itemId").append("<option  value=" +value+">" + item[i].itemNo + "</option>");
// 				}
// 			}
// 			layui.form.render('select');
// 		} else {
// 			layer.alert(data.msg)
// 		}
// 	})
// }

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
function cleanBarcodeRule() {
	$('#ruleForm')[0].reset();
	layui.form.render();// 必须写
}
