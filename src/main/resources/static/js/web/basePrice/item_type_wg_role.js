/**
 * 物料通用价格维护管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table','tableSelect' ], function() {
		var table = layui.table,table1 = layui.table, form = layui.form, tableSelect = layui.tableSelect;

		tableIns = table.render({
			elem : '#colsList',
			url : context + '/basePrice/itemTypeWgRole/getList',
			method : 'get' // 默认：get请求
			,
			cellMinWidth : 80,
			height:'full-70',//固定表头&full-查询框高度
			page : true,
			limit: 50,
			limits: [50,100,200,300,500],
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
			},  {
				field : 'itemType',
				title : '物料类型',
                align : 'center',
			},  {
				field : 'roleName',
				title : '角色',
				// width : 200,
                align : 'center',
			}, {
				field : 'createBy',
				title : '创建人',
				width : 200
			}, {
				field : 'createDate',
				title : '创建时间',
				width : 200
			}, {
				fixed : 'right',
				title : '操作',
				align : 'center',
				toolbar : '#optBar',
				width : 120
			} ] ],
			done : function(res, curr, count) {
				pageCurr = curr;
				merge(res.data, [ 'itemType', ], [ 1, 1 ]);
			}
		});

		tableIns1=table.render({
			elem: '#roleList'
			,url:context+'/sysRole/getList'
			,method: 'get' //默认：get请求
			,cellMinWidth: 80
			,height:'full-160'//固定表头&full-查询框高度
			,even:true//条纹样式
			,page: true,
            limit:20,
			request: {
				pageName: 'page' //页码的参数名称，默认：page
				,limitName: 'rows' //每页数据量的参数名，默认：limit
			},
			parseData: function (res) {
				// 可进行数据操作
				return {
					"count": res.data.total,
					"msg":res.msg,
					"data":res.data.rows,
					"code": res.status //code值为200表示成功
				}
			},
			cols: [[
				{field : 'checkColumn',type:'checkbox'}
				,{field:'roleCode', title:'编号',align:'center', width:120,sort:true}
				,{field:'roleName', title:'名称',align:'center', width:140,sort:true}
				,{field:'description', title: '描述',align:'center', width:240,sort:true}
			]]
			,done: function(res, curr, count){
				pageCurr=curr;
			}
		});

		tableSelect = tableSelect.render({
				elem : '#itemType',
				searchKey : 'keyword',
				checkedKey : 'id',
				searchPlaceholder : '试着搜索',
				table : {
					//width : 220,
					url : context
						+ '/basePrice/itemTypeWg/getList',
					method : 'get',

					cols : [ [ {type : 'radio'},// 多选 radio
						{field : 'itemType', title : '物料类型', width : 200},
						{field : 'fmemo', title : '备注'}
					] ],
					page : true,
					request : {
						pageName : 'page' // 页码的参数名称，默认：page
						,
						limitName : 'rows' // 每页数据量的参数名，默认：limit
					},
					parseData : function(res) {
						if (!res.result) {
							// 可进行数据操作
							return {
								"count" : 0,
								"msg" : res.msg,
								"data" : [],
								"code" : res.status
								// code值为200表示成功
							}
						}
						return {
							"count" : res.data.total,
							"msg" : res.msg,
							"data" : res.data.rows,
							"code" : res.status
							// code值为200表示成功
						}
					},
				},
				done : function(elem, data) {
					// 选择完后的回调，包含2个返回值
					// elem:返回之前input对象；data:表格返回的选中的数据 []
					var da = data.data;
					form.val("itemForm", {
						"itemType" : da[0].itemType,
						"itemTypeId":da[0].id
					});
					form.render();// 重新渲染
					//刷新角色table
					getRoleListByType(da[0].id);
				}
			});

		// 监听工具条
		table.on('tool(colsTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delData(data, data.id, data.itemType);
			} else if (obj.event === 'edit') {
				// 编辑
				getData(data);
			}
		});
		// 监听提交
		form.on('submit(addSubmit)', function() {
			var ids = "";
			var itemTypeId = $('#itemTypeId').val();
            var checkStatus = table.cache.roleList;
            $('#itemForm tbody tr td[data-field="checkColumn"] input[type="checkbox"]').each(function(i){
                if ($(this).is(":checked")) {
                    ids += checkStatus[i].id +",";
                }
            });

			addSubmit(itemTypeId,ids);
			return false;
		});
		// 监听搜索框
		form.on('submit(searchSubmit)', function(data) {
			// 重新加载table
			load(data);
			return false;
		});
		// 编辑价格维护
		function getData(obj) {
			
			form.val("itemForm", {
				"id" : obj.id,
				"itemTypeId" : obj.pkItemTypeWg,
				"itemType" : obj.itemType
			});
			getRoleListByType(obj.pkItemTypeWg);
			openData(obj.id, "外购物料类型")
		}


	});

});

function getRoleListByType(id) {
	CoreUtil.sendAjax("/basePrice/itemTypeWgRole/getListByWgId?wgId="+id, "",
		function(data) {
			if (data.result) {
				tableIns1.reload({
					done : function(res, curr, count) {
						for(var q=0;q<res.data.length;q++){
							for(var j =0;j<data.data.length;j++){
								if(data.data[j].pkSysRole == res.data[q].id){
									$('tbody tr[data-index="'+q+'"] td[data-field="checkColumn"] input[type="checkbox"]').prop('checked', true);
									$('tbody tr[data-index="'+q+'"] td[data-field="checkColumn"] input[type="checkbox"]').next().addClass('layui-form-checked');
								}
							}
						}
					}
				})
			} else {
				layer.alert(data.msg, function() {
					layer.closeAll();
				});
			}
		}, "GET", false, function(res) {
			layer.alert(res.msg);
		});

}

// 新增编辑弹出框
function openData(id, title) {
	if (id == null || id == "") {
		$("#id").val("");
	}
	var index = layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		content : $('#setForm'),
		end : function() {
			cleanData();
		}
	});
	layer.full(index);
}

// 新增价格维护
function add() {
	// 清空弹出框数据
	cleanData();
	// 打开弹出框
	openData(null, "添加外购物料类型信息");
}

// 新增价格维护的提交
function addSubmit(pkItemTypeWg,roleIds) {
	var params = {
		"pkItemTypeWg":pkItemTypeWg,
		"roleIds" : roleIds
	};
	CoreUtil.sendAjax("/basePrice/itemTypeWgRole/add", JSON.stringify(params), function(
		data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				// cleanProc();
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



// 编辑价格维护的提交
function editSubmit(obj) {
	CoreUtil.sendAjax("/basePrice/itemTypeWg/edit", JSON.stringify(obj.field),
			function(data) {
				if (data.result) {
					layer.alert("操作成功", function() {
						layer.closeAll();
						cleanData();
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

// 删除价格维护
function delData(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除该类型为:' + name + ' 的外购物料类型信息吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/basePrice/itemTypeWgRole/delete", JSON
					.stringify(param), function(data) {
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

// 清空新增表单数据
function cleanData() {
	$('#itemForm')[0].reset();
	layui.form.render();// 必须写
}
