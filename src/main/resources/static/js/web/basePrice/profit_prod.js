/**
 * 利润率维护管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table' ,'tableSelect'], function() {
		var table = layui.table, form = layui.form,tableSelect = layui.tableSelect;
		setData()
		tableSelect = tableSelect.render({
			elem : '#productType',
			searchKey : 'keyword',
			checkedKey : 'id',
			searchPlaceholder : '试着搜索',
			table : {
				url : context + '/basePrice/profitProd/getProdTypeList',
				method : 'get',
				// width:800,
				cols : [ [ {
					type : 'numbers',
					title : '序号'
				}, {
					type : 'radio'
				},
					{
						field : 'ID',
						title : 'id',
						width : 0,
						hide : true
					},
					{
						field : 'PRODUCT_TYPE',
						title : '类型',
						width : 120
					},
					{
						field : 'FMEMO',
						title : '备注',
						width : 120
					},
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
				form.val("itemForm", {
					"productType" : da[0].PRODUCT_TYPE,
				});
				form.render();// 重新渲染
				
			}
		});

		tableIns = table.render({
			elem : '#colsList',
			url : context + '/basePrice/profitProd/getList',
			method : 'get' // 默认：get请求
			,
			cellMinWidth : 80,
			height:'full-70',//固定表头&full-查询框高度
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
			},  {
				field : 'productType',
				title : '产品类型',
				width : 150
			},  {
				field : 'itemType',
				title : '机种型号',
				width : 150
			}, {
				field : 'profitRateGs',
				title : '毛利率%',
				width : 150
			},
				{
					field : 'fmemo',
					title : '备注',
					width : 150,
				},
				{
					field : 'enabled',
					title : '有效状态',
					templet : '#statusTpl',
					width : 95
				},
				{
				field : 'createBy',
				title : '创建人',
				width : 80
			}, {
				field : 'createDate',
				title : '创建时间',
				width : 150
			}, {
				field : 'lastupdateBy',
				title : '更新人',
				width : 80
			}, {
				field : 'lastupdateDate',
				title : '更新时间',
				width : 150
			}, {
				fixed : 'right',
				title : '操作',
				align : 'center',
				toolbar : '#optBar',
				width : 120
			} ] ],
			done : function(res, curr, count) {
				pageCurr = curr;
				merge(res.data,['productType'],[1]);
			}
		});
		// 切换状态操作
		form.on('switch(isStatusTpl)', function(obj) {
			doStatus(obj, this.value, this.name, obj.elem.checked);
		});

		//自定义验证规则
		form.verify({
			double: function(value){
				if(/^\d+$/.test(value)==false && /^\d+\.\d+$/.test(value)==false)
				{
					return '毛利率只能输入数字';
				}
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
		form.on('submit(addSubmit)', function(data) {
			//20210119-fyx-提示
			if(Number(data.field.profitRateGs) > 100){
				layer.open({
		              content: '确认设置毛利润为'+data.field.profitRateGs+'吗?'
		              ,btn: ['确定', '取消']
		              ,yes: function(index, layero){
		                //确定的回调
		            	  if (data.field.id == null || data.field.id == "") {
		      				// 新增
		      				addSubmit(data);
			      			} else {
			      				editSubmit(data);
			      			}
		              }
		              ,btn2: function(index, layero){
		              }
		            });
			}else{
				if (data.field.id == null || data.field.id == "") {
					// 新增
					addSubmit(data);
				} else {
					editSubmit(data);
				}
			}
			
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
				"productType" : obj.productType,
				"itemType" : obj.itemType,
				"rangePrice" : obj.rangePrice,
				"profitRateGs" : obj.profitRateGs,
				"fmemo" : obj.fmemo,
			});
			getTypeList2(obj.unitId);
			
			openData(obj.id, "编辑价格信息")
		}

		// 设置正常/禁用
		function doStatus(obj, id, name, checked) {
			var isStatus = checked ? 1 : 0;
			var deaprtisStatus = checked ? "正常" : "禁用";
			// 正常/禁用
			layer.confirm('您确定要把机种型号为：' + name + '设置为' + deaprtisStatus + '状态吗？',
					{
						btn1 : function(index) {
							var params = {
								"id" : id,
								"checkStatus" : isStatus
							};
							CoreUtil.sendAjax("/basePrice/profitProd/doStatus",
									JSON.stringify(params), function(data) {
										if (data.result) {
											layer.alert("操作成功", function() {
												layer.closeAll();
												loadAll();
											});
										} else {
											layer.alert(data.msg, function() {
												layer.closeAll();
											});
										}
									}, "POST", false, function(res) {
										layer.alert("操作请求错误，请您稍后再试",
												function() {
													layer.closeAll();
												});
									});
						},
						btn2 : function() {
							obj.elem.checked = !isStatus;
							form.render();
							layer.closeAll();
						},
						cancel : function() {
							obj.elem.checked = !isStatus;
							form.render();
							layer.closeAll();
						}
					});
		}

	});

});

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
	getTypeList2("");
	openData(null, "添加产品利润信息");
}
// 新增价格维护的提交
function addSubmit(obj) {
	CoreUtil.sendAjax("/basePrice/profitProd/add", JSON.stringify(obj.field),
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

function getTypeList2() {
	$("#productType").append(
		"<option value=" + 1 + ">" + "测试" + "</option>");
	layui.form.render('select');
}

function getTypeList(id) {
	CoreUtil.sendAjax("/basePrice/profitProd/getTypeList", "", function(data) {
		if (data.result) {
			$("#productType").empty();
			var list = data.data;
			for (var i = 0; i < list.length; i++) {
				if (i == 0) {
					$("#unitId").append("<option value=''>请选择</option>")
				}
				$("#productType").append(
						"<option value=" + list[i].id + ">" + list[i].unitCode
								+ "——" + list[i].unitName + "</option>")
				if(id==list[i].id){
					$("#productType").val(list[i].id);
				}
			}
			
			layui.form.render('select');
		} else {
			layer.alert(res.msg);
		}
	}, "GET", false, function(res) {
		layer.alert(res.msg);
	});
}

function setData(){
	$("#itemType").empty();
	var data = Jitai
	for (var i = 0; i < data.length; i++) {
		if (i == 0) {
			$("#itemType").append("<option value=''>请点击选择</option>");
		}
		$("#itemType").append(
				"<option value=" + data[i].subCode + ">" + data[i].subName+"</option>");
	}
}
// 编辑价格维护的提交
function editSubmit(obj) {
	CoreUtil.sendAjax("/basePrice/profitProd/edit", JSON.stringify(obj.field),
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
		layer.confirm('您确定要删除该型号为:' + name + ' 的产品利润信息吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/basePrice/profitProd/delete", JSON
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

// 清空新增表单数据
function cleanData() {
	$('#itemForm')[0].reset();
	layui.form.render();// 必须写
}

function merge(res,columsName,columsIndex) {
    //console.log(res)
    var data = res;
    var mergeIndex = 0;//定位需要添加合并属性的行数
    var mark = 1; //这里涉及到简单的运算，mark是计算每次需要合并的格子数
    //var columsName = ['itemCode'];//需要合并的列名称
    //var columsIndex = [3];//需要合并的列索引值

    for (var k = 0; k < columsName.length; k++) { //这里循环所有要合并的列
        var trArr = $(".layui-table-body>.layui-table").find("tr");//所有行
            for (var i = 1; i < data.length; i++) { //这里循环表格当前的数据
                var tdCurArr = trArr.eq(i).find("td").eq(columsIndex[k]);//获取当前行的当前列
                var tdPreArr = trArr.eq(mergeIndex).find("td").eq(columsIndex[k]);//获取相同列的第一列
                
                if (data[i][columsName[k]] === data[i-1][columsName[k]]) { //后一行的值与前一行的值做比较，相同就需要合并
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