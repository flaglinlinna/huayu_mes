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
							"bsRemarks" : quote_data.bsRemarks,
							
							"cl_hardware" : quoteDetail.data.cl_hardware,
							"cl_molding" : quoteDetail.data.cl_molding,
							"cl_surface" : quoteDetail.data.cl_surface,
							"cl_packag" : quoteDetail.data.cl_packag,
							"lh_hardware" : quoteDetail.data.lh_hardware,
							"lh_molding" : quoteDetail.data.lh_molding,
							"lh_surface" : quoteDetail.data.lh_surface,
							"lh_packag" : quoteDetail.data.lh_packag,
							
							"lw_hardware" : quoteDetail.data.lw_hardware,
							"lw_molding" : quoteDetail.data.lw_molding,
							"lw_surface" : quoteDetail.data.lw_surface,
							"lw_packag" : quoteDetail.data.lw_packag,
							"hardware_all" : quoteDetail.data.hardware_all,
							"molding_all" : quoteDetail.data.molding_all,
							"surface_all" : quoteDetail.data.surface_all,
							"packag_all" : quoteDetail.data.packag_all,
							
							"wx_all" : quoteDetail.data.wx_all,
							"hou_loss_all" : quoteDetail.data.hou_loss_all,
							"mould_all" : quoteDetail.data.mould_all,
							"gl" : quoteDetail.data.gl,
							
						});
						layui.form.render('select');
					});
});

function clean() {
	// console.log($('#itemForm')[0])
	$('#itemForm')[0].reset();
	layui.form.render();// 必须写
}
