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
						form.on('submit(saveData)', function(data) {
							
							 console.log(data.field)
							//saveData(data.field, empIdList);
						});
						// 日期选择器
						laydate.render({// 完成日期
							elem : '#bsFinishTime',
							trigger : 'click'
						});
					});
});

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
}

function clean() {
	// console.log($('#itemForm')[0])
	$('#itemForm')[0].reset();
	layui.form.render();// 必须写
}
