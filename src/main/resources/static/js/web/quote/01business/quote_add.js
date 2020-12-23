/**
 * 报价单
 */
var pageCurr;
$(function() {
	setData();
	layui.use([ 'table', 'form', 'layedit', 'laydate', 'layer' ],
		function() {var form = layui.form, layer = layui.layer, laydate = layui.laydate, table = layui.table;
						// 按钮监听事件		
						form.on('submit(saveData)',function(data) {
						    var paramlist = data.field;
							paramlist["bsMaterial"] = GetCheckboxValues('material');
							paramlist["bsRequire"]= GetCheckboxValues('require');
							paramlist["bsChkOutItem"]=GetCheckboxValues('chkOutItem');
							paramlist["bsFunctionItem"]=GetCheckboxValues('functionItem');
							if (paramlist.id == null || paramlist.id == "") {
				               // 新增
							   saveData(paramlist);
			                } else {
			                   editData(paramlist);
			                }
			                return false;			
						});
						form.verify({
							num: function(value){
								if(/^\d+$/.test(value)==false && /^\d+\.\d+$/.test(value)==false)
								{
									return '只能输入数字';
								}
							}
						});
						
						// 日期选择器
						laydate.render({// 完成日期
							elem : '#bsFinishTime',
							trigger : 'click'
						});
						if(quoteId){//存在quote为修改，不存在为新增
							var param = {"id" : quoteId};
							CoreUtil.sendAjax("/quote/getSingleAll", JSON.stringify(param),function(data) {
								if (data.result) {	
											form.val("itemForm", {
												"id" : data.data.id,
												"bsType" : data.data.bsType,
												"bsFinishTime" : data.data.bsFinishTime,
												"bsRemarks" : data.data.bsRemarks,
												"bsProd" : data.data.bsProd,
												"bsSimilarProd" : data.data.bsSimilarProd,
												"pkProfitProd" : data.data.pkProfitProd,
												"bsCustName" : data.data.bsCustName,
												"bsPosition" : data.data.bsPosition,
												"bsChkOut" : data.data.bsChkOut,
												"bsFunction" : data.data.bsFunction,
												"bsLevel" : data.data.bsLevel,
												"bsCustRequire" : data.data.bsCustRequire,
											});
											setCheckboxValues('material',data.data.bsMaterial)
											setCheckboxValues('require',data.data.bsRequire)
											setCheckboxValues('chkOutItem',data.data.bsChkOutItem)
											setCheckboxValues('functionItem',data.data.bsFunctionItem)
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
//将字符串拆分-并补充checkbox
function setCheckboxValues(Name,str) {//name checkbox控件id, str 字符串
	if(str!=null){
		var strs= new Array(); //定义数组
		strs=str.split(",");
		for(var i=0;i<strs.length;i++){
			$("[id='" + Name + "']:checkbox").each(function() {
				if($(this).attr("title")==strs[i]){
					$(this).prop("checked", true);
				}	
			});
		}
	}
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

function clean() {
	// console.log($('#itemForm')[0])
	$('#itemForm')[0].reset();
	layui.form.render();// 必须写
}
