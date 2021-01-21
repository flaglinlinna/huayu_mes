/**
 * 制造部材料管理 五金:hardware 注塑:molding 表面处理:surface 组装:packag
 */
var pageCurr;
var localtableFilterIns;
$(function() {
	layui.use([ 'form', 'table', 'upload', 'tableSelect' ], function() {
		var table = layui.table, form = layui.form, upload = layui.upload,
		tableSelect1 = layui.tableSelect, tableSelect = layui.tableSelect;
		isComplete()
		// hardwareList
		tableIns = table.render({
			elem : '#listTable',
			url : context + '/productMater/getList?bsType=' + bsType + '&quoteId=' + quoteId,
			method : 'get',// 默认：get请求
			cellMinWidth : 80,
			// toolbar: '#toolbar',
			height : 'full-65',// 固定表头&full-查询框高度
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
			cols : [ [
			  {type : 'numbers'},
			  {field:"id",title:"ID",hide:true},
			  // {field:"bsStatus",title:"bsStatus",hide:true},
			  {field : 'bsComponent',title : '零件名称',sort : true,style : 'background-color:#d2d2d2'},
			  {field : 'bsMachiningType',title : '加工类型<span style="color:red;font-size:12px;">*</span>',width : 100,hide : true,edit : 'text',style : 'background-color:#ffffff' /* (表面处理)*/},
			  {field : 'bsColor',title : '配色工艺<span style="color:red;font-size:12px;">*</span>',width : 100,hide : true,edit : 'text',style : 'background-color:#ffffff' /* (表面处理)*/},
			  {field : 'bsMaterName',title : '材料名称',sort : true,style : 'background-color:#d2d2d2'},
			  {field : 'bsModel',title : '规格',style : 'background-color:#d2d2d2'},
			  {field : 'bsQty',width : 100,title : '用量<span style="color:red;font-size:12px;">*</span>',hide : true,edit : 'text',style : 'background-color:#ffffff'},
			  {field : 'bsProQty',width : 100,title : '制品重<span style="color:red;font-size:12px;">*</span>',hide : true,edit : 'text',style : 'background-color:#ffffff'},
				{field : 'bsUnit',width : 120,title : '单位',templet : '#selectUnit',style : 'background-color:#ffffff'},
			  /*{field : 'bsRadix',width : 80,title : '基数<span style="color:red;font-size:12px;">*</span>',edit : 'text',style : 'background-color:#ffffff'},*/
			  {field : 'bsWaterGap',title : '水口量<span style="color:red;font-size:12px;">*</span>',width : 100,hide : true,edit : 'text',style : 'background-color:#ffffff' /*(注塑)*/},
			  {field : 'bsCave',title : '穴数<span style="color:red;font-size:12px;">*</span>',width : 100,hide : true,edit : 'text',style : 'background-color:#ffffff' /* (注塑)*/}, 
			  {field : 'bsSupplier',title : '备选供应商',edit : 'text',style : 'background-color:#ffffff'},
			  {field : 'fmemo',title : '备注',width : 120,edit : 'text',style : 'background-color:#ffffff'},
			  {fixed : 'right',title : '操作',align : 'center',width : 120,toolbar : '#optBar'} 
			  ] ],
			done : function(res, curr, count) {
				pageCurr = curr;

				var tableIns = this.elem.next(); // 当前表格渲染之后的视图
				layui.each(res.data, function(i, item){
					if(item.bsStatus=="1"){
						tableIns.find('tr[data-index=' + i + ']').find('td').data('edit',false).css("background-color", "#d2d2d2");
						 $("select[name='selectUnit']").attr("disabled","disabled");
						form.render('select');
					}
				});

				res.data.forEach(function(item, index) {
					if (bsType == 'hardware') {// 五金
						$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsQty"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsQty"]').removeClass("layui-hide");
					} else if (bsType == 'molding') {// 注塑
						$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsWaterGap"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsCave"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsProQty"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsWaterGap"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsCave"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsProQty"]').removeClass("layui-hide");

					} else if (bsType == 'surface') {
						$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsMachiningType"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsMachiningType"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsColor"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsColor"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsQty"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsQty"]').removeClass("layui-hide");
					} else if (bsType == 'packag') {
						$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsQty"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsQty"]').removeClass("layui-hide");
					}
				});
			}
		});

		//监听机台类型下拉选择 并修改
		form.on('select(selectUnit)', function (data) {
			//获取当前行tr对象
			var elem = data.othis.parents('tr');
			//第一列的值是Guid，取guid来判断
			var Guid= elem.first().find('td').eq(1).text();
			// var bsStatus= elem.first().find('td').eq(2).text();
			//选择的select对象值；
			var selectValue = data.value;
			updateUnit(Guid,selectValue);

		})

		tableIns2 = table.render({
			elem : '#uploadList',
			// url : context +
			// '/productMater/getList?bsType='+bsType+'&quoteId='+quoteId,
			method : 'get', // 默认：get请求
			cellMinWidth : 80,
			// toolbar: '#toolbar',
			height : 'full-110',// 固定表头&full-查询框高度
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
			cols : [ [ 
			   {type : 'numbers'}, 
			   {field : 'checkStatus',width : 100,title : '状态',sort : true,style : 'background-color:#d2d2d2',templet : '#checkStatus'},
			// {field : 'enabled', width:100, title :'是否导入',sort:true,style:'background-color:#d2d2d2', templet: '#enabledTpl'},
			   {field : 'errorInfo',width : 150,title : '错误信息',sort : true,style : 'background-color:#d2d2d2'},
			   {field : 'bsComponent',width : 140,title : '零件名称',sort : true},
			   {field : 'bsMachiningType',title : '加工类型',width : 100,hide : true /*(表面处理)*/},
			   {field : 'bsColor',title : '配色工艺',width : 100,hide : true /*(表面处理)*/},
			   {field : 'bsMaterName',width : 140,title : '材料名称',sort : true},
			   {field : 'bsModel',width : 160,title : '规格'},
			   {field : 'bsQty',width : 100,title : '用量',hide : true},
			   {field : 'bsProQty',width : 100,title : '制品量',hide : true},
			   {field : 'bsUnit',width : 80,title : '单位',},
			   //{field : 'bsRadix',width : 80,title : '基数',},
			   {field : 'bsWaterGap',title : '水口量',width : 100,hide : true /*(注塑)*/},
			   {field : 'bsCave',title : '穴数',width : 100,hide : true /*(注塑)*/},
			   {field : 'bsSupplier',title : '备选供应商',width : 100},
			   {field : 'fmemo',title : '备注',width : 120},
			// {fixed : 'right', title : '操作', align : 'center', width:120, toolbar : '#optBar'}
			] ],
			done : function(res1, curr, count) {
				pageCurr = curr;
			}
		});

		tableSelect = tableSelect.render({
			elem : '#bsUnit',
			searchKey : 'keyword',
			checkedKey : 'id',
			searchPlaceholder : '关键字搜索',
			table : {
				url : context + '/basePrice/unit/getList',
				method : 'get',
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
				cols : 
				[ [{type : 'radio'},// 单选 radio
				   {field : 'id',title : 'id',width : 0,hide : true}, 
				   {type : 'numbers'}, 
				   {field : 'unitCode',title : '单位编码'},
				   {field : 'unitName',title : '单位名称'} ] ],
				page : true,
				request : {
					pageName : 'page' ,// 页码的参数名称，默认：page
					limitName : 'rows' // 每页数据量的参数名，默认：limit
				},
			},
			done : function(elem, data) {
				var da = data.data;
				// 选择完后的回调，包含2个返回值 elem:返回之前input对象；data:表格返回的选中的数据 []
				form.val("hardwareForm", {
					"pkUnit" : da[0].id,
					"bsUnit" : da[0].unitName
				});
				form.render();// 重新渲染
			}
		});

		table.on('edit(hardwareTable)', function(obj) {
			console.log(obj.field);// 当前编辑列名
			var bsRadix = obj.data.bsRadix;
			var bsQty = obj.data.bsQty;
			var bsWaterGap = obj.data.bsWaterGap;
			var bsCave = obj.data.bsCave;
			var bsProQty = obj.data.bsProQty;
			// var bsLoss = obj.data.bsLoss;
			/*if (obj.field == "bsRadix") {
				if (/^\d+$/.test(bsRadix) == false || bsRadix <= 0) {
					layer.msg("基数必填且只能输入整数且大于0");
					loadAll();
					return false;
				}
			} else*/
			if (obj.field == "bsQty") {
				if (/^\d+$/.test(bsQty) == false && /^\d+\.\d+$/.test(bsQty) == false) {
					layer.msg("用量只能输入数字");
					loadAll();
					return false;
				}
			} else if (obj.field == "bsWaterGap") {
				if (/^\d+$/.test(bsWaterGap) == false && /^\d+\.\d+$/.test(bsWaterGap) == false) {
					layer.msg("水口量只能输入数字");
					loadAll();
					return false;
				}
			} else if (obj.field == "bsProQty") {
				if (/^\d+$/.test(bsProQty) == false && /^\d+\.\d+$/.test(bsProQty) == false && bsProQty != "" && bsProQty != null) {
					layer.msg("制品重只能输入数字");
					loadAll();
					return false;
				}
			} else if (obj.field == "bsCave") {
				if (/^\d+$/.test(bsCave) == false && /^\d+\.\d+$/.test(bsCave) == false || bsCave == 0) {
					layer.msg("穴数只能输入数字且不能为0");
					loadAll();
					return false;
				}
			} else if (obj.field == "bsLoss") {
				if (/^\d+$/.test(bsLoss) == false && /^\d+\.\d+$/.test(bsLoss) == false && bsLoss != "" && bsLoss != null) {
					layer.msg("损耗率只能输入数字");
					loadAll();
					return false;
				}
			}
			obj.field = obj.data;
			editSubmit(obj);
		})

		initSelect();

		// 自定义验证规则
		form.verify({
			double : function(value) {
				// 可为空
				// console.log(value);
				if (/^\d+$/.test(value) == false && /^\d+\.\d+$/.test(value) == false && value != "" && value != null) {
					return '只能输入数字';
				}
			}
		});

		// 监听在职操作
		form.on('switch(isStatusTpl)', function(obj) {
			setStatus(obj, this.value, this.name, obj.elem.checked);
		});
		// 监听工具条
		table.on('tool(hardwareTable)', function(obj) {
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

			if (bsType == "hardware") {
				if (data.field.bsQty == "") {
					layer.msg("请输入所有带*的必填项");
					return false;
				}
			} else if (bsType == "molding") {
				if (data.field.bsProQty == "" || data.field.bsCave == "" || data.field.bsWaterGap == "") {
					layer.msg("请输入所有带*的必填项");
					return false;
				}
			} else if (bsType == "surface") {
				if (data.field.bsColor == "" || data.field.bsMachiningType == "") {
					layer.msg("请输入所有带*的必填项");
					return false;
				}
			} else if (bsType == "packag") {
				if (data.field.bsQty == "") {
					layer.msg("请输入所有带*的必填项");
					return false;
				}
			}

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
				"bsProQty" : obj.bsProQty,
				"bsSupplier" : obj.bsSupplier,
				"fmemo" : obj.fmemo,
				"bsWaterGap" : obj.bsWaterGap,
				"bsCave" : obj.bsCave,
				"bsMachiningType" : obj.bsMachiningType,
				"bsColor" : obj.bsColor,
			});
			openProdErr(id, "编辑材料信息")
		}
		;

		// 导入
		upload.render({
			elem : '#upload',
			url : context + '/productMaterTemp/importExcel',
			accept : 'file' // 普通文件
			,
			data : {
				bsType : function() {
					return bsType;
				},
				pkQuote : function() {
					return quoteId;
				}
			},
			before : function(obj) { // obj参数包含的信息，跟 choose回调完全一致，可参见上文。
				layer.load(); // 上传loading
			},
			done : function(res, index, upload) {
				layer.closeAll('loading'); // 关闭loading
				layer.alert(res.msg, function(index) {
					layer.close(index);
					loadAll2();
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

function isComplete() {
	if (iStatus == 2) {
		$("#addbtn").addClass("layui-btn-disabled").attr("disabled", true)
		$("#exportbtn").addClass("layui-btn-disabled").attr("disabled", true)
		$("#loadbtn").addClass("layui-btn-disabled").attr("disabled", true)
		$("#savebtn").addClass("layui-btn-disabled").attr("disabled", true)
	}
}

function uploadChecked() {
	var params = {
		"pkQuote" : quoteId,
		"bsType" : bsType
	};
	CoreUtil.sendAjax("/productMaterTemp/uploadMater", JSON.stringify(params), function(data) {
		if (data.result) {
			layer.alert(data.msg, function() {
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

// 导出数据
function exportExcel() {
	location.href = context + "/productMater/exportExcel?bsType=" + bsType + "&pkQuote=" + quoteId;
}

function Confirm() {
	var params = {
		"id" : quoteId,
		"bsType" : bsType,
		"bsCode" : bsCode
	};
	layer.confirm('一经提交则不得再修改，确定要提交吗？', {
		btn : [ '确认', '返回' ]
	}, function() {
		CoreUtil.sendAjax("/productMater/doStatus", JSON.stringify(params), function(data) {
			if (data.result) {
				layer.alert("确认完成成功", function() {
					layer.closeAll();
					// 刷新页面
					iStatus=2;
					isComplete();
					loadAll()
				});
			} else {
				layer.alert(data.msg);
			}
		}, "POST", false, function(res) {
			layer.alert(res.msg);
		});
	});
}

//更新单位
function updateUnit(id,unitId) {
	var param = {
		"id":id,
		"unitId":unitId
	}
	CoreUtil.sendAjax("/productMater/updateUnit", JSON.stringify(param),
		function(data) {
			if (isLogin(data)) {
				if (data.result == true) {
					// 回调弹框
					//layer.closeAll();
					// 加载load方法
					loadAll();
					/*layer.alert("修改单位成功！", function() {
						layer.closeAll();
						// 加载load方法
						loadAll();
					});*/
				} else {
					layer.alert(data.msg, function() {
						layer.closeAll();
					});
				}
			}
		});
}

// 打开导入页
function openUpload() {
	tableIns2.reload({
		url : context + '/productMaterTemp/getList?quoteId=' + quoteId + '&bsType=' + bsType,
		done : function(res1, curr, count) {
			pageCurr = curr;
			res1.data.forEach(function(item, index) {
				if (bsType == 'hardware') {// 五金
					$('div[lay-id="uploadList"]').find('thead').find('th[data-field="bsQty"]').removeClass("layui-hide");
					$('div[lay-id="uploadList"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsQty"]').removeClass("layui-hide");
				} else if (bsType == 'molding') {// 注塑
					$('div[lay-id="uploadList"]').find('thead').find('th[data-field="bsWaterGap"]').removeClass("layui-hide");
					$('div[lay-id="uploadList"]').find('thead').find('th[data-field="bsCave"]').removeClass("layui-hide");
					$('div[lay-id="uploadList"]').find('thead').find('th[data-field="bsProQty"]').removeClass("layui-hide");
					$('div[lay-id="uploadList"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsWaterGap"]').removeClass("layui-hide");
					$('div[lay-id="uploadList"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsCave"]').removeClass("layui-hide");
					$('div[lay-id="uploadList"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsProQty"]').removeClass("layui-hide");

				} else if (bsType == 'surface') {
					$('div[lay-id="uploadList"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsMachiningType"]').removeClass("layui-hide");
					$('div[lay-id="uploadList"]').find('thead').find('th[data-field="bsMachiningType"]').removeClass("layui-hide");
					$('div[lay-id="uploadList"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsColor"]').removeClass("layui-hide");
					$('div[lay-id="uploadList"]').find('thead').find('th[data-field="bsColor"]').removeClass("layui-hide");
					$('div[lay-id="uploadList"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsQty"]').removeClass("layui-hide");
					$('div[lay-id="uploadList"]').find('thead').find('th[data-field="bsQty"]').removeClass("layui-hide");
				} else if (bsType == 'packag') {
					$('div[lay-id="uploadList"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsQty"]').removeClass("layui-hide");
					$('div[lay-id="uploadList"]').find('thead').find('th[data-field="bsQty"]').removeClass("layui-hide");
				}
			});
		}
	})
	// 打开弹出框
	var index = layer.open({
		type : 1,
		title : "导入信息",
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#uploadDiv')
	});
	layer.full(index);
}

// 模板下载
function downloadExcel() {
	if (bsType == "hardware") {
		location.href = "../../excelFile/五金材料模板.xlsx";
	} else if (bsType == "molding") {
		location.href = "../../excelFile/注塑材料模板.xlsx";
	} else if (bsType == "surface") {
		location.href = "../../excelFile/表面处理材料模板.xlsx";
	} else if (bsType == "packag") {
		location.href = "../../excelFile/组装材料模板.xlsx";
	}
}

function selectDiv() {
	if (bsType == "hardware") {
		$("#bsProQtyDiv").hide();
		$("#bsWaterGapDiv").hide();
		$("#bsCaveDiv").hide();
		$("#bsColorDiv").hide();
		$("#bsMachiningTypeDiv").hide();

	} else if (bsType == "molding") {
		$("#bsQtyDiv").hide();
		$("#bsColorDiv").hide();
		$("#bsMachiningTypeDiv").hide();
	} else if (bsType == "surface") {
		$("#bsProQtyDiv").hide();
		$("#bsWaterGapDiv").hide();
		$("#bsCaveDiv").hide();
	} else if (bsType == "packag") {
		$("#bsProQtyDiv").hide();
		$("#bsWaterGapDiv").hide();
		$("#bsCaveDiv").hide();
		$("#bsColorDiv").hide();
		$("#bsMachiningTypeDiv").hide();
	}
}

// 新增编辑弹出框
function openProdErr(id, title) {
	if (id == null || id == "") {
		$("#id").val("");
	}
	selectDiv();
	var index = layer.open({
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

function initSelect() {
	$("#bsComponent").empty();
	var bomlist = bomNameList.data;
	for (var i = 0; i < bomlist.length; i++) {
		if (i == 0) {
			$("#bsComponent").append("<option value=''> 请选择</option>");
		}
		$("#bsComponent").append("<option value='" + bomlist[i].BS_COMPONENT + "'>" + bomlist[i].BS_COMPONENT + "</option>");
	}
	layui.form.render();
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
	CoreUtil.sendAjax("/productMater/add", JSON.stringify(obj.field), function(data) {
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
	CoreUtil.sendAjax("/productMater/edit", JSON.stringify(obj.field), function(data) {
		if (data.result) {
			/*layer.alert("操作成功", function() {
				layer.closeAll();
				cleanProdErr();
				// 加载页面
				loadAll();
			});//2021-01-21 lst删除*/
			layer.closeAll();
			cleanProdErr();
			// 加载页面
			loadAll();
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
			CoreUtil.sendAjax("/productMater/delete", JSON.stringify(param), function(data) {
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
