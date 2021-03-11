/**
 * 虚拟物料对应
 */
var pageCurr;
var totalCount = 0;// 表格记录数
$(function() {
	layui.use([ 'form', 'table','upload', 'tableSelect' ], function() {
		var table = layui.table, form = layui.form, upload = layui.upload, tableSelect = layui.tableSelect;

		// isComplete()
		tableIns = table.render({
			elem : '#client_procList',
			url : context + '/quoteItem/getItemList?pkQuote=' + quoteId,
			method : 'get',// 默认：get请求
			// , toolbar: '#toolbar',
			cellMinWidth : 80,
			height : 'full-65',
			// even : true,// 条纹样式
			page : true,
			limit:20,
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
			cols : [ [ {type : 'numbers'},
				{field : 'bsItemCode',title : '虚拟料号',width: 160,style : 'background-color:#d2d2d2',align: 'center'},
				{field : 'bsItemCodeReal',title : '实际料号',edit:'text',minWidth: 120,align: 'center',style : 'background-color:#ffffff'},
				{field : 'bsMaterName',title : '物料描述',minWidth: 160,style : 'background-color:#d2d2d2',align: 'center'},
				{field : 'workcenterName',title : '工作中心',minWidth: 120,style : 'background-color:#d2d2d2',align: 'center',templet:function (d) {
						if(d.wc!=null){
							return d.wc.workcenterName;
						}else {
							return "";
						}
					}},
				// {fixed : 'right',title : '操作',width : 100,align : 'center',toolbar : '#optBar'}
			] ],
			done : function(res, curr, count) {
				// console.log(res)
				totalCount = res.count
				pageCurr = curr;
				// merge(res.data, [ 'bsName', ], [ 1, 1 ]);
			}
		});

		table.on('edit(client_procList)',function (obj) {
			console.log(obj.data);
			var bsItemCodeReal = obj.data.bsItemCodeReal;
			var id = obj.data.id;
			editBsItemCodeReal(id,bsItemCodeReal);
			// return false;
		})

		// 监听搜索框
		form.on('submit(searchSubmit)', function(data) {
			// 重新加载table
			load(data);
			return false;
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
			if (field == 'bsActQuote') {
				// 判断是否为数字
				if (isRealNum(value)) {
					doActQuote(data.id, value);
				} else {
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
				// getClientProc(data, data.id);//未写
				// addProc(data.custId)
			}
		});


		// 监听提交
		form.on('submit(addSubmit)', function(data) {
			var checkStatus = table.cache.procList;
			var procIdList = "";
			$('#clientProcForm tbody tr td[data-field="checkColumn"] input[type="checkbox"]').each(function(i) {
				if ($(this).is(":checked")) {
					// fyx-202011-02
					var checks = $('tbody tr[data-index="' + i + '"] td[data-field="jobAttr"] input[type="checkbox"]:checked');

					procIdList += checkStatus[i].id + ",";
				}
			});
			if (data.field.num == "") {
				layer.msg('请先选择组件', {
		              time: 20000, //20s后自动关闭
		              btn: ['知道了']
		            });
				return false;
			}
			// console.log(data.field.num)
			// addSubmit(procIdList,data.field.itemId);
			addSubmit(procIdList, data.field.num);
			return false;

		});

		// 导入
		upload.render({
			elem : '#upload',
			url : context + '/quoteItem/importExcel',
			accept : 'file' // 普通文件
			,
			before : function(obj) { // obj参数包含的信息，跟 choose回调完全一致，可参见上文。
				layer.load(); // 上传loading
			},
			done : function(res, index, upload) {
				layer.closeAll('loading'); // 关闭loading
				layer.alert(res.msg, function(index) {
					layer.close(index);
					loadAll();
				});

			},
			error : function(index, upload) {
				layer.alert("操作请求错误，请您稍后再试", function() {
				});
				layer.closeAll('loading'); // 关闭loading
				layer.close(index);
			}
		});


	});
});


// 导出数据
function exportExcel() {
	location.href = context + "/quoteItem/exportExcel?pkQuote=" + quoteId;
}

// 编辑实际料号
function editBsItemCodeReal(id,codeReal) {
	var params = {
		"id":id,
		"bsItemCodeReal":codeReal
	};
	CoreUtil.sendAjax("/quoteItem/editBsItemCodeReal", JSON.stringify(params), function(
		data) {
		if (data.result) {
			loadAll();
		} else {
			layer.alert(data.msg);
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}

function delClientProc(id) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/quoteMould/delete", JSON.stringify(param), function(data) {
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

// 新增工艺流程提交
function addSubmit(procIdlist, itemIds) {
	var params = {
		"proc" : procIdlist,
		"itemId" : itemIds,
		"quoteId" : quoteId
	};

	CoreUtil.sendAjax("/quoteMould/add", JSON.stringify(params), function(data) {
		// console.log(data)
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