/**
 * 物料通用价格维护管理
 */
var pageCurr;
var _index = 0;
$(function() {
	layui.use([ 'form', 'table','upload' ], function() {
		var table = layui.table, form = layui.form ,upload = layui.upload;

		tableIns = table.render({
			elem : '#colsList',
			url : context + '/basePrice/customQs/getList',
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
			},  {
				field : 'custName',
				title : '客户名称',
				width : 150
			},  {
				field : 'qsNo',
				title : '品质标准编号',
				width : 150
			}, {
				field : 'qsName',
				title : '品质标准名称',
				width : 150
			},
				{
					field : 'fmemo',
					title : '品质标准说明',
					width : 150,
				},
                {
                    field : 'fftp',
                    title : '品质标准附件',
                    width : 150,
                    templet: '<div><a style="cursor: pointer;color: blue;text-decoration:underline;" href="'+context+'/file/get?fsFileId={{d.fileId}}" th:href="@{/file/get?fsFileId={{d.fileId}}}">{{ d.fftp==null?"":d.fftp }}</a></div>'
                },
                {
                    field : 'qsType',
                    title : '品质标准类型',
                    width : 150,
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
			}
		});


		// 监听工具条
		table.on('tool(colsTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delData(data, data.id, data.qsNo);
			} else if (obj.event === 'edit') {
				// 编辑
				getData(data);
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
		// 编辑价格维护
		function getData(obj) {
            document.getElementById("filelist").innerHTML = "";
			form.val("itemForm", {
				"id" : obj.id,
				"custName" : obj.custName,
				"qsNo" : obj.qsNo,
				"qsName" : obj.qsName,
				"qsType" : obj.qsType,
				"fmemo" : obj.fmemo,
                "fftp":obj.fftp,
                "fileId":obj.fileId,
			});
			var filedata = {
			    "id":obj.fileId,
                "bsName":obj.fftp,
                "bsContentType":"stp",
            }
			if(obj.fileId!=null){
                document.getElementById("filelist").innerHTML = $("#filelist").html()+getExcFieldBefore(filedata,obj.id,"/basePrice/customQs/delFile");
            }

			openData(obj.id, "编辑价格信息")
		}

        //上传控件
        upload.render({
            elem: '#upload'
            ,url: context+'/file/upload'
            ,accept: 'file' //普通文件
            ,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致。
                layer.load(); //上传loading
            }
            ,done: function(res,index, upload){
                layer.closeAll('loading'); //关闭loading
                if(res.result == true){
                    document.getElementById("filelist").innerHTML = $("#filelist").html()+getExcField(_index,res.data)
                    $('#fileId').val(res.data.id);
					$('#fftp').val(res.data.bsName);
                }
            }
            ,error: function(index, upload){
                layer.closeAll('loading'); //关闭loading
            }
        });

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
	getTypeList("");
	openData(null, "添加客户品质标准信息");
}
// 新增价格维护的提交
function addSubmit(obj) {
	CoreUtil.sendAjax("/basePrice/customQs/add", JSON.stringify(obj.field),
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



function getTypeList(id) {
	CoreUtil.sendAjax("/basePrice/customQs/getQsType", "", function(data) {
		if (data.result) {
			$("#qsType").empty();
			var list = data.data;
			for (var i = 0; i < list.length; i++) {
				if (i == 0) {
					$("#qsType").append("<option value=''>请选择</option>")
				}
				$("#qsType").append(
						"<option value=" + list[i].id + ">" + list[i].unitCode
								+ "——" + list[i].unitName + "</option>")
				if(id==list[i].id){
					$("#qsType").val(list[i].id);
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

// 编辑价格维护的提交
function editSubmit(obj) {
	CoreUtil.sendAjax("/basePrice/customQs/edit", JSON.stringify(obj.field),
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
		layer.confirm('您确定要删除该编号为:' + name + ' 的品质标准信息吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/basePrice/customQs/delete", JSON
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
