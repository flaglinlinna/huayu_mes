/**
 * 新建报价单
 */
var pageCurr;
$(function() {
	setData();
	layui
			.use(
					[ 'table', 'form', 'layedit', 'laydate', 'layer' ],
					function() {
						var form = layui.form, layer = layui.layer, laydate = layui.laydate, table = layui.table;
						// 按钮监听事件
						
						
						
						form.on('submit(saveData)',function(data) {
						    var paramlist = data.field;
							paramlist["bsMaterial"] = GetCheckboxValues('material');
							paramlist["bsRequire"]= GetCheckboxValues('require');
							console.log(paramlist)
							saveData(paramlist);
						});
						
						// 日期选择器
						laydate.render({// 完成日期
							elem : '#bsFinishTime',
							trigger : 'click'
						});
					});
});

// 将checke拼接为"value1,value2,value3"
function GetCheckboxValues(Name) {
	var result = [];
	$("[id='" + Name + "']:checkbox").each(function() {
		if ($(this).is(":checked")) {
			result.push($(this).attr("title"));
		}
	});
	return result.join(",");
}

function setData() {
	$("#pkProfitProd").empty();
	var data = profitProdList.data;
	for (var i = 0; i < data.length; i++) {
		if (i == 0) {
			$("#pkProfitProd").append("<option value=''>请点击选择</option>");
		}
		$("#pkProfitProd").append(
				"<option value=" + data[i].id + ">" + data[i].productType
						+ "——" + data[i].itemType + "</option>");
	}
	// layui.form.render('select');
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

function clean() {
	// console.log($('#itemForm')[0])
	$('#itemForm')[0].reset();
	layui.form.render();// 必须写
}
