
var pageCurr;
$(function() {
	layui.use([ 'form', 'table','upload','tableSelect' ], function() {
		var table = layui.table, form = layui.form,upload = layui.upload,tableSelect = layui.tableSelect;

		tableIns = table.render({
			elem : '#productPriceList',
			url : context + '/purchase/getQuoteList?quoteId='+quoteId,
			method : 'get' // 默认：get请求
			,
			cellMinWidth : 80,
			//toolbar: '#toolbar',
			height:'full-95'//固定表头&full-查询框高度
			//,even:true,//条纹样式
			,page : true,
			request : {
				pageName : 'page' // 页码的参数名称，默认：page
				,
				limitName : 'rows' // 每页数据量的参数名，默认：limit
			},
			parseData : function(res) {
				console.log(res.data.rows)
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
			    {type : 'numbers',style:'background-color:#d2d2d2'}, 
				{field: 'bsType', width: 100, title: '类型', sort: true, style:'background-color:#d2d2d2',
				// * 五金:hardware
				// * 注塑:molding
				// * 表面处理:surface
				// * 组装:packag
				templet: function (d) {
						if (d.bsType == 'hardware') {
							return '五金'
						} else if (d.bsType == 'molding') {
							return '注塑'
						}else if (d.bsType == 'surface') {
							return '表面处理'
						}else if (d.bsType == 'packag') {
							return '组装'
						}
					}
				},
				{field : 'bsComponent',width:150,title : '零/组件名称',sort:true,style:'background-color:#d2d2d2'}, 
				{field : 'bsMaterName',width:120, title : '材料名称',sort:true,style:'background-color:#d2d2d2'},
				{field : 'bsModel', width:150, title : '材料规格',style:'background-color:#d2d2d2'},
				{field : 'bsQty', width:80, title : '用量',style:'background-color:#d2d2d2'},
				{field : 'bsProQty', width:100, title : '制品重',style:'background-color:#d2d2d2'},
				{field : 'bsMachiningType', title : '加工类型',width:100,style:'background-color:#d2d2d2'},//(表面处理)
				{field : 'bsColor', title : '配色工艺',width:100,style:'background-color:#d2d2d2'},//(表面处理)
				{field : 'bsWaterGap', title : '水口量',width:100,style:'background-color:#d2d2d2'}, //(注塑)
				{field : 'bsCave', title : '穴数',width:100,style:'background-color:#d2d2d2'}, //(注塑)
				{field : 'bsUnit', width:80, title : '单位',style:'background-color:#d2d2d2'},
				{field : 'bsRadix', width:80, title : '基数',style:'background-color:#d2d2d2'},
				{field : 'bsGeneral', width:120, title : '是否通用物料',style:'background-color:#d2d2d2'},
				{field : 'bsGear', width:120, title : '价格挡位', templet: '#selectGear'},
				/*{field : 'bsGear', width:120, title : '价格挡位',templet: function (d) {
					if(d.bsPriceList){
						var list = $.parseJSON( d.bsPriceList );
					}
                    // 模板的实现方式也是多种多样，这里简单返回固定的
                    return '<select name="city" lay-filter="testSelect" lay-verify="required" data-value="' + d.bsGear + '" >\n' +
                        '        <option value=""></option>\n' +
                        '        <option value="18000">北京</option>\n' +
                        '        <option value="20000">上海</option>\n' +
                        '        <option value="20001">广州</option>\n' +
                        '        <option value="20002">深圳</option>\n' +
                        '        <option value="20003">杭州</option>\n' +
                        '      </select>';
                }},*/
				{field : 'bsRefer', width:110, title : '参考价格',style:'background-color:#d2d2d2'},
				{field : 'bsAssess', width:110, title : '评估价格', edit:'number'	},
				{field : 'bsExplain', width:110, title : '采购说明'},
				{field : 'fmemo', width:110, title : '备注', edit:'text'},
				{field : 'bsSupplier', width:110, title : '供应商', edit:'text'}
				] ],
			done : function(res, curr, count) {
				pageCurr = curr;

			}
		});

		tableIns2 = table.render({
			elem : '#uploadList',
			// url : context + '/productProcess/getList?bsType='+bsType+'&quoteId='+quoteId,
			method : 'get' // 默认：get请求
			,
			cellMinWidth : 80,
			height:'full-110'//固定表头&full-查询框高度
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
			cols : [ [
				{type : 'numbers',style:'background-color:#d2d2d2'},
				{field : 'checkStatus', width:100, title : '状态',sort:true,style:'background-color:#d2d2d2',templet: '#checkStatus'},
				{field : 'errorInfo', width:150, title : '错误信息',sort:true,style:'background-color:#d2d2d2'},
				{field: 'bsType', width: 100, title: '类型', sort: true, style:'background-color:#d2d2d2'},
				{field : 'bsComponent',width:150,title : '零/组件名称',sort:true,style:'background-color:#d2d2d2'},
				{field : 'bsMaterName',width:120, title : '材料名称',sort:true,style:'background-color:#d2d2d2'},
				{field : 'bsModel', width:150, title : '材料规格',style:'background-color:#d2d2d2'},
				{field : 'bsQty', width:80, title : '用量',style:'background-color:#d2d2d2'},
				{field : 'bsUnit', width:80, title : '单位',style:'background-color:#d2d2d2'},
				{field : 'bsRadix', width:80, title : '基数',style:'background-color:#d2d2d2'},
				{field : 'bsGeneral', width:120, title : '是否通用物料',style:'background-color:#d2d2d2'},
				{field : 'bsGear', width:80, title : '价格挡位', edit:'text',templet: '#selectGear'},
				{field : 'bsRefer', width:110, title : '参考价格',style:'background-color:#d2d2d2'},
				{field : 'bsAssess', width:110, title : '评估价格', edit:'number'	},
				{field : 'bsExplain', width:110, title : '采购说明'},
				{field : 'fmemo', width:110, title : '备注', edit:'text'},
				{field : 'bsSupplier', width:110, title : '供应商', edit:'text'},
				// {fixed : 'right', title : '操作', align : 'center',width:120, toolbar : '#optBar'}
				] ],
			done : function(res, curr, count) {
				pageCurr = curr;
			}
		});
		
		//下拉框监听事件
		form.on('select(roleIdSelect)', function(data) {
			alert('1');
			return;
			//获取行tr对象
			var elem = data.othis.parents('tr');
	        //获取第一列的值，第一列为ID列，
			var id = elem.first().find('td').eq(1).text();
	        //选择的select对象值；
	        var selectValue=data.value;
			//处理字段更新的逻辑
		});

		//自定义验证规则
		form.verify({
			double: function(value){
				if(/^\d+$/.test(value)==false && /^\d+\.\d+$/.test(value)==false && value!="" && value!=null)
				{
					return '用量/制品量 只能输入数字';
				}
			}
		});

		table.on('edit(productPriceTable)',function (obj) {
			var bsAssess = obj.data.bsAssess;
			if(/^\d+$/.test(bsAssess)==false && /^\d+\.\d+$/.test(bsAssess)==false && bsAssess!="" && bsAssess!=null)
			{
				layer.msg("评估价格只能输入数字");
				loadAll();
				return false;
			}
			obj.field = obj.data;
			editSubmit(obj);
		})

		// 监听在职操作
		form.on('switch(isStatusTpl)', function(obj) {
			setStatus(obj, this.value, this.name, obj.elem.checked);
		});
		// 监听工具条
		table.on('tool(productPriceList)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delProdErr(data, data.id, data.bsComponent);
			} else if (obj.event === 'edit') {
				// 编辑
				getProdErr(data, data.id);
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
		// 编辑五金材料
		function getProdErr(obj, id) {
			form.val("hardwareForm", {
				"id" : obj.id,
				"bsComponent" : obj.bsComponent,
				"bsMaterName" : obj.bsMaterName,
				"bsModel" : obj.bsModel,
				"bsQty" : obj.bsQty,
				"bsUnit" : obj.bsUnit,
				"bsRadix" : obj.bsRadix,
				"bsSupplier" : obj.bsSupplier,
				"fmemo" : obj.fmemo,
				"bsWaterGap":obj.bsWaterGap,
				"bsCave":obj.bsCave,
				"bsMachiningType":obj.bsMachiningType,
				"bsColor":obj.bsColor,
			});
			openProdErr(id, "编辑材料信息")
		};

		//导入 到临时表
		upload.render({
			elem: '#upload'
			,url: context + '/productMaterTemp/importByPurchase'
			,accept: 'file' //普通文件
			,data: {
				pkQuote: function(){
					return quoteId;
				}
			}
			,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
				layer.load(); //上传loading
			}
			,done: function(res,index, upload){
				layer.closeAll('loading'); //关闭loading
				layer.alert(res.msg, function (index) {
					layer.close(index);
					loadAll2();
				});

			}
			,error: function(index, upload){
				layer.alert("操作请求错误，请您稍后再试",function(){
				});
				layer.closeAll('loading'); //关闭loading
				layer.close(index);
			}
		});

	});

});

function uploadChecked() {
    var params = {
        "pkQuote": quoteId,
    };
    CoreUtil.sendAjax("/productMaterTemp/confirmUpload", JSON.stringify(params), function(
        data) {
        if (data.result) {
            layer.alert("操作成功", function() {
                layer.closeAll();
                // cleanProdErr();
                // 加载页面
                loadAll();
            });
        } else {
            layer.alert(data.msg);
        }
    }, "POST", false, function(res) {
        layer.alert(res.msg);
    });
}

// 打开导入页
function openUpload() {
	tableIns2.reload({
		url:context + '/productMaterTemp/getList?quoteId='+quoteId+'&bsPurchase='+0,
		done: function(res1, curr, count){
			pageCurr=curr;
		}
	})
	// 打开弹出框
	var index=layer.open({
		type : 1,
		title : "导入采购填报价格",
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#uploadDiv')
	});
	layer.full(index);
}


function confirm(){
	var params = {
		"id" : quoteId,
	};
	layer.confirm('一经提交则不得再修改，确定要提交吗？', {
		btn : [ '确认', '返回' ]
	}, function() {
		CoreUtil.sendAjax("/purchase/doStatus", JSON.stringify(params), function(
			data) {
			if (data.result) {
				layer.alert("确认完成成功", function() {
					layer.closeAll();
					// cleanProdErr();
					// 加载页面
					loadAll();
				});
			} else {
				layer.alert(data.msg);
			}
		}, "POST", false, function(res) {
			layer.alert(res.msg);
		});
	});
}

// 新增编辑弹出框
function openProdErr(id, title) {
	if (id == null || id == "") {
		$("#id").val("");
	}
	var index=layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#setProdErr'),
		end : function() {
			cleanProdErr();
		}
	});
	layer.full(index);
}

// 添加五金材料
function addHardware() {
	// 清空弹出框数据
	cleanProdErr();
	// 打开弹出框
	openProdErr(null, "添加材料");
}
// 新增五金材料提交
function addSubmit(obj) {
	obj.field.bsType = bsType;
	obj.field.pkQuote = quoteId;
	CoreUtil.sendAjax("/productMater/add", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanProdErr();
				// 加载页面
				loadAll();
			});
		} else {
			layer.alert(data.msg);
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}

// 编辑五金材料提交
function editSubmit(obj) {
	CoreUtil.sendAjax("/purchase/edit", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				// cleanProdErr();
				// 加载页面
				loadAll();
			});
		} else {
			layer.alert(data.msg);
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}

// 删除五金材料
function delProdErr(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除材料：' + name + '吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/productMater/delete", JSON.stringify(param),
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
// 重新加载表格（全部）
function loadAll2() {
	// 重新加载table
	tableIns2.reload({
		page : {
			curr : pageCurr
			// 从当前页码开始
		}
	});
}

// 清空新增表单数据
function cleanProdErr() {
	$('#hardwareForm')[0].reset();
	layui.form.render();// 必须写
}

function exportExcel() {
	location.href = context + "/purchase/exportExcel?pkQuote="+quoteId;
}
