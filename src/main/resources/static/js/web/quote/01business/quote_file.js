/**
 * 外购件清单维护
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table', 'upload' ], function() {
		var table = layui.table, form = layui.form, upload = layui.upload;
		isComplete()
		tableIns = table.render({
			elem : '#productFileList',
			url : context + '/quoteFile/getList?pkQuote=' + quoteId,
			method : 'get', // 默认：get请求
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
			cols : [ [{type : 'numbers'},
					{field : 'bsFileName',title : '文件名称',templet : '<div><a style="cursor: pointer;color: blue;text-decoration:underline;" href="' + context
								+ '/file/get?fsFileId={{d.pkFileId}}" th:href="@{/file/get?fsFileId={{d.pkFileId}}}">{{ d.bsFileName==null?"":d.bsFileName }}</a></div>'},
					{field : 'createBy',title : '创建人',width : 200}, 
					{field : 'createDate',title : '创建时间',width : 200}, 
					{fixed : 'right',title : '操作',align : 'center',toolbar : '#optBar',width : 150
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

		// 自定义验证规则
		form.verify({
			double : function(value) {
				if (/^\d+$/.test(value) == false && /^\d+\.\d+$/.test(value) == false) {
					return '用量只能输入数字';
				}
			}
		});

		// 监听在职操作
		form.on('switch(isStatusTpl)', function(obj) {
			setStatus(obj, this.value, this.name, obj.elem.checked);
		});
		// 监听工具条
		table.on('tool(productFileTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delProdErr(data, data.id, data.bsFileName);
			} else if (obj.event === 'edit') {
				// 编辑
				getProdErr(data, data.id);
			}
		});

		// 监听搜索框
		form.on('submit(searchSubmit)', function(data) {
			// 重新加载table
			load(data);
			return false;
		});
	});
});

function isComplete() {
	if (iStatus == 2) {
		$("#loadbtn").addClass("layui-btn-disabled").attr("disabled", true)
		$("#savebtn").addClass("layui-btn-disabled").attr("disabled", true)
	}
}

// 上传控件
function InitUpload() {
	var InitUpload = layui.upload.render({
		elem : '#upload',
		url : context + '/file/upload',
		accept : 'file' // 普通文件
		,
		before : function(obj) { // obj参数包含的信息，跟 choose回调完全一致。
			layer.load(); // 上传loading
		},
		done : function(res, index, upload) {
			if (res.result == true) {
				// document.getElementById("filelist").innerHTML =
				// $("#filelist").html()+getExcField(_index,res.data)
				var params = {
					"bsFileName" : res.data.bsName,
					"pkFileId" : res.data.id
				}
				addSubmit(params);
				// $("#upload").remove();
				// InitUpload();
			}
			layer.closeAll('loading'); // 关闭loading

		},
		error : function(index, upload) {
			layer.closeAll('loading'); // 关闭loading
		}

	});
	$('#upload').click();
}



// 新增五金材料提交
function addSubmit(obj) {
	obj.pkQuote = quoteId;
	CoreUtil.sendAjax("/quoteFile/add", JSON.stringify(obj), function(data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				// 加载页面
				window.location.reload();
			});
		} else {
			layer.alert(data.msg);
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}
// 确认完成
function save() {
	console.log(quoteId, code)
	var param = {
		"quoteId" : quoteId,
		"code" : code
	};
	layer.confirm('一经提交则不得再修改，确定要提交吗？', {
		btn : [ '确认', '返回' ]
	}, function() {
		CoreUtil.sendAjax("/quoteFile/doStatus", JSON.stringify(param), function(data) {
			//console.log(data)
			if (isLogin(data)) {
				if (data.result == true) {
					// 回调弹框
					layer.alert("提交成功！");
					//刷新页面
					iStatus=2;
					isComplete();
					loadAll()
				} else {
					layer.alert(data);
				}
			}
		});
	});
}

// 删除五金材料
function delProdErr(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除文件：' + name + '的信息吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/quoteFile/delete", JSON.stringify(param), function(data) {
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
	$('#quoteBomForm')[0].reset();
	layui.form.render();// 必须写
}
