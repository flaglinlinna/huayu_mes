/**
 * 报价单
 */
var pageCurr;
$(function() {

	layui
			.use(
					[ 'table', 'form', 'layedit', 'laydate', 'layer' ],
					function() {
						var form = layui.form, layer = layui.layer, laydate = layui.laydate, table = layui.table;

						form.verify({
							num : function(value) {
								if (/^\d+$/.test(value) == false
										&& /^\d+\.\d+$/.test(value) == false) {
									return '只能输入数字';
								}
							}
						});

						var quote_data = quoteDetail.data.Quote;
						var param = {
							"user_id" : quote_data.createBy
						};
						CoreUtil.sendAjax("/quote/findUserName", JSON
								.stringify(param), function(data) {
							if (data.result) {
								form.val("itemForm", {
									"createBy_name" : data.data,					
								});
							}else{
								layer.alert(res.msg);
							}
						}, "POST", false, function(res) {
							layer.alert("操作请求错误，请您稍后再试");
						});

						form.val("itemForm", {
							"id" : quote_data.id,
							"bsCode" : quote_data.bsCode,
							"bsProjVer" : quote_data.bsProjVer,
							"createBy" : quote_data.createBy,
							"createBy_name" : createBy_name,
							"bsFinishTime" : quote_data.bsFinishTime,
							"bsDevType" : quote_data.bsDevType,
							"bsCustName" : quote_data.bsCustName,
							"bsRemarks" : quote_data.bsRemarks
						});
						layui.form.render('select');
					});
});

function clean() {
	// console.log($('#itemForm')[0])
	$('#itemForm')[0].reset();
	layui.form.render();// 必须写
}
