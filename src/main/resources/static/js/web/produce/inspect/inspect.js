/**
 * 小码校验
 */

$(function() {

	layui
			.use(
					[ 'table', 'form', 'layedit', 'tableSelect' ],
					function() {
						var form = layui.form, layer = layui.layer, layedit = layui.layedit, table = layui.table, tableSelect = layui.tableSelect;

						//监听提交
				    	  form.on('submit(hsearchSubmit)', function(data){
				    		  hTableIns.reload({
				    			  url:context+'/produce/inspect/getHistoryList',
				                  where:data.field 
								});
				    	    return false;
				    	  });
						hTableIns = table.render({// 历史
							elem : '#hcolTable',
							where : {},
							method : 'get',// 默认：get请求
							defaultToolbar : [],
							page : true,
							data : [],
							height: 'full-150',
							request : {
								pageName : 'page', // 页码的参数名称，默认：page
								limitName : 'rows' // 每页数据量的参数名，默认：limit
							},
							parseData : function(res) {// 可进行数据操作
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
							}, {
								field : 'TASK_NO',
								title : '制定单号',
								width : 320,sort: true
							},{
								field : 'ITEM_BARCODE',
								title : '物料条码',
								width : 150,sort: true
							}, {
								field : 'ITEM_NO',
								title : '物料编码',
								width : 170,sort: true
							}, , {
								field : 'ITEM_NAME',
								title : '物料描述',
								width : 170,sort: true
							},{
								field : 'PROC_NAME',
								title : '工序名称',
								width : 150,sort: true
							}, {
								field : 'SAMPLE_QTY',
								title : '抽检总量(PCS)',
								width : 170,sort: true
							}, {
								field : 'QTY_DONE',
								title : '合格数量(PCS)',
								width : 180,sort: true
							}, {
								field : 'DEFECT_NUM',
								title : '不良数量(PCS)',
								width : 180,sort: true
							}, {
								field : 'CHK_RESULT',
								title : '检验结果',
								width : 100,sort: true
							}, {
								field : 'ORG_NAME',
								title : '责任部门',
								width : 80,sort: true
							}, {
								field : 'DEFECT_DET_LIST',
								title : '不良内容',
								width : 80,sort: true
							},  {
								field : 'LINE_NO',
								title : '组长',
								width : 90,sort: true
							}] ],
							done : function(res, curr, count) {
								pageCurr = curr;
							}
						});
						
						
						
					});

});

