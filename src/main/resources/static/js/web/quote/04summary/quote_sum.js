/**
 * 报价单
 */
var pageCurr;
$(function() {

	layui.use([ 'table', 'form', 'layedit', 'laydate', 'layer' ],
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
						form.val("itemForm", {
							"id" : quote_data.id,
							"bsCode" : quote_data.bsCode,
							"bsProjVer" : quote_data.bsProjVer,
							"createBy" : quote_data.createBy,
							"createBy_name" : quote_data.user.userName,
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
