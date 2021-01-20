/**
 * 报价单
 */
var pageCurr;
$(function() {
	setData();
	layui.use([ 'table', 'form', 'layedit', 'laydate', 'layer' ], function() {
		var form = layui.form, layer = layui.layer, laydate = layui.laydate, table = layui.table;
		form.render();
		// 按钮监听事件
		form.on('submit(saveData)', function(data) {
			var paramlist = data.field;
			paramlist["bsMaterial"] = GetCheckboxValues('material');
			paramlist["bsRequire"] = GetCheckboxValues('require');
			paramlist["bsChkOutItem"] = GetCheckboxValues('chkOutItem');
			paramlist["bsFunctionItem"] = GetCheckboxValues('functionItem');
			if (paramlist.id == null || paramlist.id == "") {
				// 新增
				saveData(paramlist);
			} else {
				console.log(paramlist)
				editData(paramlist);
			}
			return false;
		});
		form.on('select(bsProdTypeId)', function(data) {
			var elem = data.elem;
			var textId = elem.getAttribute("textId");
			$("#bsProdType").val(this.innerText)// 保存中文字符
			if ($("#bsDevType").val() != "") {
				doCheckProfit()
			}
		})
		form.on('select(bsDevType)', function(data) {
			if ($("#bsProdTypeId").val() != "") {
				doCheckProfit()
			}
		})
		form.verify({
			num : function(value) {
				if (/^\d+$/.test(value) == false && /^\d+\.\d+$/.test(value) == false) {
					return '只能输入数字';
				}
			}
		});

		// 日期选择器
		laydate.render({// 完成日期
			elem : '#bsFinishTime',
			trigger : 'click',
			min:0
		});
		if (quoteId) {// 存在quote为修改，不存在为新增
			var param = {
				"id" : quoteId
			};
			CoreUtil.sendAjax("/quote/getSingleAll", JSON.stringify(param), function(data) {
				// console.log(data.data)
				if (data.result) {
					form.val("itemForm", {
						"id" : data.data.id,
						"bsType" : data.data.bsType,
						"bsFinishTime" : data.data.bsFinishTime,
						"bsRemarks" : data.data.bsRemarks,
						"bsProd" : data.data.bsProd,
						"bsSimilarProd" : data.data.bsSimilarProd,
						"bsProdType" : data.data.bsProdType,
						"bsProdTypeId" : data.data.bsProdTypeId,
						"bsDevType" : data.data.bsDevType,
						"bsCustName" : data.data.bsCustName,
						"bsPosition" : data.data.bsPosition,
						"bsChkOut" : data.data.bsChkOut,
						"bsFunction" : data.data.bsFunction,
						"bsLevel" : data.data.bsLevel,
						"bsCustRequire" : data.data.bsCustRequire,
					});
					setCheckboxValues('material', data.data.bsMaterial)
					setCheckboxValues('require', data.data.bsRequire)
					setCheckboxValues('chkOutItem', data.data.bsChkOutItem)
					setCheckboxValues('functionItem', data.data.bsFunctionItem)
					form.render('checkbox');
				} else {
					layer.alert(data.msg)
				}
			}, "POST", false, function(res) {
				layer.alert("操作请求错误，请您稍后再试");
			});
		}
		layui.form.render('select');
	});
	
	//20210119-fyx-根据Status判断是否隐藏saveBtn
	if(Status == '1' ||Status == '99'){
		$('#saveBtn').addClass("layui-btn-disabled").attr("disabled",true);
	}else{
		$('#saveBtn').removeClass("layui-btn-disabled").attr("disabled",false)
	}
	
	//20210120-fyx-填写管理费
	$('#bsManageFee').val(Fee[0].paramValue);
});

// 将checkbox拼接为"value1,value2,value3"
function GetCheckboxValues(Name) {
	var result = [];
	$("[id='" + Name + "']:checkbox").each(function() {
		if ($(this).is(":checked")) {
			result.push($(this).attr("title"));
		}
	});
	return result.join(",");
}
// 将字符串拆分-并补充checkbox
function setCheckboxValues(Name, str) {// name checkbox控件id, str 字符串
	if (str != null) {
		var strs = new Array(); // 定义数组
		strs = str.split(",");
		for (var i = 0; i < strs.length; i++) {
			$("[id='" + Name + "']:checkbox").each(function() {
				if ($(this).attr("title") == strs[i]) {
					$(this).prop("checked", true);
				}
			});
		}
		// form.render();
	}
}

function setData() {
	$("#bsProdTypeId").empty();
	var data = prodType.data;
	for (var i = 0; i < data.length; i++) {
		if (i == 0) {
			$("#bsProdTypeId").append("<option value=''>请点击选择</option>");
		}
		$("#bsProdTypeId").append("<option value=" + data[i].id + ">" + data[i].productType + "</option>");
	}
	$("#bsDevType").empty();
	var data = Jitai
	for (var i = 0; i < data.length; i++) {
		if (i == 0) {
			$("#bsDevType").append("<option value=''>请点击选择</option>");
		}
		$("#bsDevType").append("<option value=" + data[i].subCode + ">" + data[i].subName + "</option>");
	}
	$("#bsType").empty();
	var data = QuoteType
	for (var i = 0; i < data.length; i++) {
		if (i == 0) {
			$("#bsType").append("<option value=''>请点击选择</option>");
		}
		$("#bsType").append("<option value=" + data[i].subCode + ">" + data[i].subName + "</option>");
	}
}

function saveData(obj) {
	CoreUtil.sendAjax("/quote/add", JSON.stringify(obj), function(data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				clean();
				layer.closeAll();
			});
		} else {
			layer.alert(data.msg);
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}

function editData(obj) {
	CoreUtil.sendAjax("/quote/edit", JSON.stringify(obj), function(data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
			});
		} else {
			layer.alert(data.msg);
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}

// 关闭报价单
function doCheckProfit() {
	var param = {
		"bsDevType" : $("#bsDevType").val(),// code
		"bsProdType" : $("#bsProdType").val()
	};
	CoreUtil.sendAjax("/quote/doCheckProfit", JSON.stringify(param), function(data) {
		if (data.result) {

		} else {
			layer.alert("利润率未维护")
		}
	}, "POST", false, function(res) {
		layer.alert("操作请求错误，请您稍后再试");
	});
}

function clean() {
	// console.log($('#itemForm')[0])
	$('#itemForm')[0].reset();
	layui.form.render();// 必须写
}
