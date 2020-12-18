/**
 * 五金材料管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table','upload' ], function() {
		var table = layui.table, form = layui.form,upload = layui.upload;

		tableIns = table.render({
			elem : '#hardwareList',
			url : context + '/productMater/getList',
			method : 'get' // 默认：get请求
			,
			cellMinWidth : 80,
			toolbar: '#toolbar',
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
			cols : [ [ {
				type : 'numbers'
			}
			// ,{field:'id', title:'ID', width:80, unresize:true, sort:true}
			, {
				field : 'bsComponent',
				title : '零件名称',sort:true
			}, {
				field : 'bsMaterName',
				title : '材料名称',sort:true
			},
			{
				field : 'bsModel',
				title : '规格'
			}, {
				field : 'bsQty',
				title : '用量',
			}, {
					field : 'bsUnit',
					title : '单位',
				},
				{
					field : 'bsRadix',
					title : '基数',
				},
				{
					field : 'bsSupplier',
					title : '供应商',
				},
				{
					field : 'bsWaterGap',
					title : '水口量',
					//(注塑)
				},
				{
					field : 'bsCave',
					title : '穴数',
					//(注塑)
				},
				{
					field : 'bsMachiningType',
					title : '加工类型',
					//(表面处理)
				},
				{
					field : 'bsColor',
					title : '配色工艺',
					//(表面处理)
				},
				{
					field : 'fmemo',
					title : '备注',
				},
				{
				fixed : 'right',
				title : '操作',
				align : 'center',
				toolbar : '#optBar'
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

		//自定义验证规则
		form.verify({
			double: function(value){
				if(/^\d+$/.test(value)==false && /^\d+\.\d+$/.test(value)==false)
				{
					return '用量只能输入数字';
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
			});
			openProdErr(id, "编辑五金材料信息")
		};

		//导入
		upload.render({
			elem: '#upload'
			,url: context + '/hardware/importExcel'
			,accept: 'file' //普通文件
			,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
				layer.load(); //上传loading
			}
			,done: function(res,index, upload){
				layer.closeAll('loading'); //关闭loading
				layer.alert(res.msg, function (index) {
					layer.close(index);
					loadAll();
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

//模板下载
function  downloadExcel() {
	location.href = "../../excelFile/五金材料模板.xlsx";
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
	openProdErr(null, "添加五金材料");
}
// 新增五金材料提交
function addSubmit(obj) {
	CoreUtil.sendAjax("/hardware/add", JSON.stringify(obj.field), function(
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
	CoreUtil.sendAjax("/hardware/edit", JSON.stringify(obj.field), function(
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

// 删除五金材料
function delProdErr(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除五金材料：' + name + '吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/hardware/delete", JSON.stringify(param),
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

// 清空新增表单数据
function cleanProdErr() {
	$('#hardwareForm')[0].reset();
	layui.form.render();// 必须写
}
