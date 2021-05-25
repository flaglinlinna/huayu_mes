/**
 * 报价单
 */
var pageCurr;
$(function() {
	layui.use([ 'table', 'form', 'layedit', 'laydate', 'layer' ], function() {
		var form = layui.form, layer = layui.layer, laydate = layui.laydate, table = layui.table, table1 = layui.table, table2 = layui.table;

		initRate();
		form.verify({
			num : function(value) {
				if (/^\d+$/.test(value) == false && /^\d+\.\d+$/.test(value) == false) {
					return '只能输入数字';
				}
			}
		});

		$('#bsManageFee').bind('keypress', function(event) {
			if (event.keyCode == "13") {
				if (/^\d+$/.test($('#bsManageFee').val()) == false && /^\d+\.\d+$/.test($('#bsManageFee').val()) == false) {
					layer.msg("请输入数字类型的管理费率");
					return false;
				}
				if($('#bsManageFee').val()){
					updateBsManageFee($('#bsManageFee').val());
				}
			}

		});

		$('#profitNet').bind('keypress', function(event) {
			if (event.keyCode == "13") {
				// alert('你输入的内容为：' + $('#barcode').val());
				if (/^\d+$/.test($('#profitNet').val()) == false && /^\d+\.\d+$/.test($('#profitNet').val()) == false) {
					layer.msg("请输入数字类型的净利润");
					return false;
				}
				if ($('#profitNet').val()) {

					updateProfitNet($('#profitNet').val());

					var bj_all = Number(quoteDetail.data.gl) + Number(quoteDetail.data.p_cb) + Number($('#profitNet').val());
					var ml = Number(quoteDetail.data.gl) + Number($('#profitNet').val());
					var ml_rate = ml / bj_all * 100;
					var profit_gs = $('#profitNet').val() / bj_all * 100;
					var gl_rate = Number($('#gl').val()) / bj_all *100;
					var p_cb_rate = Number($('#p_cb').val()) / bj_all *100;
					form.val("itemForm", {
						"bj_all" : bj_all.toFixed(5),
						"ml" : ml.toFixed(5),
						// "ml_rate" : ml_rate.toFixed(5) + "%",
						// "profit_gs" : profit_gs.toFixed(5) + "%"
					})
					$('#ml_rate').html(ml_rate.toFixed(2) + "%");
					$('#profit_gs').html(profit_gs.toFixed(2) + "%");
					$("#gl_rate").html(gl_rate.toFixed(2) + "%");
					$("#p_cb_rate").html(p_cb_rate.toFixed(2) + "%");
					layui.form.render('select');
				} else {
					layer.alert("请输入净利润!", function() {
						$('#profitNet').focus();
						layer.closeAll();
					});
				}
			}
		});

		$('#ml,#gl').bind('keypress', function(event) {
			if (event.keyCode == "13") {
				var bj_all = Number($('#gl').val()) + Number(quoteDetail.data.p_cb) + Number($('#profitNet').val());
				var ml_rate = Number($('#ml').val()) / bj_all * 100;
				var profit_gs = $('#profitNet').val() / bj_all * 100;
				form.val("itemForm", {
					"bj_all" : bj_all.toFixed(5),
					// "ml" : ml.toFixed(5),
					// "ml_rate" : ml_rate.toFixed(5) + "%",
					// "profit_gs" : profit_gs.toFixed(5) + "%"
				})
				$('#ml_rate').html(ml_rate.toFixed(2) + "%");
				$('#profit_gs').html(profit_gs.toFixed(2) + "%");
				layui.form.render('');
			}

		});
		var quote_data = quoteDetail.data.Quote;
		var bsBade = "";
		if(quote_data.bsBade!=null){
			bsBade=quote_data.bsBade==1?"是":"否"
		}
		form.val("itemForm", {
			"id" : quote_data.id,
			"bsCode" : quote_data.bsCode,
			"bsProjVer" : quote_data.bsProjVer,
			"bsProd":quote_data.bsProd,
			"bsProdType":quote_data.bsProdType,
			"bsStage":quote_data.bsStage,
			"bsLatest":quote_data.bsLatest==0?"否":"是",
			"createBy" : quote_data.createBy,
			"bsBade":bsBade,
			"bsManageFee":quote_data.bsManageFee,
			"bsProfitProd":quote_data.bsProfitProd,
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
			"bj_all" : quoteDetail.data.bj_all,
			"ml" : quoteDetail.data.ml,
			"profitNet" : quoteDetail.data.profitNet,
			"p_cb" : quoteDetail.data.p_cb,
			// "ml_rate" : quoteDetail.data.ml_rate,
			// "profit_gs" : quoteDetail.data.profit_gs,

		});
		// $('#ml_rate').html(quoteDetail.data.ml_rate.toFixed(2) + "%");
		// $('#profit_gs').html(quoteDetail.data.profit_gs.toFixed(2) + "%");
		// $('#p_cb_rate').html(quoteDetail.data.ml_rate.toFixed(2) + "%");
		// $('#gl_rate').html(quoteDetail.data.profit_gs.toFixed(2) + "%");
		layui.form.render('');

		$("a").click(function(obj) {
			getUrl($(this).attr("data-type"))
		})

		// 监听双击事件
		$(document).on('dblclick', function(obj) {
			// console.log(obj.target.id);
			var inputId = obj.target.id;
			getUrl(inputId);
		})

		function getUrl(inputId) {
			// 材料成本
			if (inputId == "cl_hardware") {
				getMaterDetail("hardware", "五金材料成本=材料单价(KG)/1000*(制品重(g)+水口重/穴数)/工序良率")
			} else if (inputId == "cl_molding") {
				getMaterDetail("molding", "注塑材料成本=材料单价(KG)/1000*(制品重(g)+水口重/穴数)/工序良率")
			} else if (inputId == "cl_surface") {
				getMaterDetail("surface", "表面处理材料成本=材料料单价(KG)/1000*材料用量/工序良率")
			} else if (inputId == "cl_packag") {
				getMaterDetail("packag", "组装材料成本=材料单价(PCS)*材料用量/工序良率")
			}
			// 人工和制费
			// else if (inputId == "lh_hardware" ) {
			// 	getProcessDetail("hardware", "五金人工/制费明细  (总人工费=人工工时费（元/H）*人数*成型周期(S）/3600 )")
			// } else if (inputId == "lh_molding" ) {
			// 	getProcessDetail("molding", "注塑人工/制费明细   (总人工费=人工费率（元/H）*人数*成型周期(S）/3600/ 穴数)")
			// } else if (inputId == "lh_surface" ) {
			// 	getProcessDetail("surface", "表面处理人工/制费明细   (总人工费=人数*费率/产能)")
			// } else if (inputId == "lh_packag" ) {
			// 	getProcessDetail("packag", "组装人工/制费明细   (总人工费=人数*费率/产能)")
			// }
			else if (inputId == "wx_all") {
				getProcessDetail("out", "外协加工费用明细 ")
			}
			else if (inputId == "mould_all") {
				getMouldDetail("模具费用明细");
			}

			//人工明细
			else if(inputId == "lh_hardware") {
				getProcessDetail("hardware", "五金人工成本=人工费用(元/H)*成型周期(S）/3600 *人数/工序良率","lh")
			} else if(inputId == "lh_molding") {
				getProcessDetail("molding", "注塑人工成本=人工费用(元/H)*人数*成型周期(S)/3600/穴数/工序良率","lh")
			} else if(inputId == "lh_surface") {
				getProcessDetail("surface", "表面处理人工成本=人数*人工费用(元/H)/产能(PCS/H)/工序良率","lh")
			} else if(inputId == "lh_packag") {
				getProcessDetail("packag", "组装人工成本=人数*人工费用(元/H)/产能(PCS/H)/工序良率","lh")
			}

			//制费明细
			else if(inputId == "lw_hardware") {
				getProcessDetail("hardware", "五金制费成本=制费工时费(元/H)*成型周期(S）/3600/工序良率","mh")
			} else if(inputId == "lw_molding") {
				getProcessDetail("molding", "注塑制费成本=总制费=制费工时费(元/H)*成型周期(S）/3600/穴数/工序良率","mh")
			} else if(inputId == "lw_surface") {
				getProcessDetail("surface", "表面处理制费成本=费率/产能(PCS/H)/工序良率","mh")
			} else if(inputId == "lw_packag") {
				getProcessDetail("packag", "组装制费成本=制费工时费(元/H)*/产能(PCS/H)/工序良率","mh")
			}
			
			//20210318-hjj-损料修改
			else if(inputId == "hou_loss_all"){
				getLossDetail( "损耗明细")
			}
		}

		// 材料价格明细
		tableIns = table.render({
			elem : '#materTable',
			// url : context + '/purchase/getQuoteList?quoteId='+quoteId,
			method : 'get' // 默认：get请求
			,
			cellMinWidth : 80,
			totalRow : true,
			height : 'full-95',// 固定表头&full-查询框高度
			// even:true,//条纹样式
			page : true,
			limit: 200,
			limits: [30,50,100,200],
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
			cols : [ [ 
			 {fixed:'left',type : 'numbers'},
			 // {field : 'bsType',width : 100,title : '类型',sort : true,
				//  templet : function(d) {
				// 	if (d.bsType == 'hardware') {// * 五金:hardware
				// 		return '五金'
				// 	} else if (d.bsType == 'molding') {// * 注塑:molding
				// 		return '注塑'
				// 	} else if (d.bsType == 'surface') {// * 表面处理:surface
				// 		return '表面处理'
				// 	} else if (d.bsType == 'packag') {// * 组装:packag
				// 		return '组装'
				// 	}
				// },totalRowText : "合计"},
			//{field : 'bsComponent',width : 120,title : '组件名称',sort : true},
			{fixed:'left',field : 'bsMaterName',width : 200,title : '材料名称',totalRowText : "合计"},
			//{field : 'bsMachiningType',title : '加工类型',width : 100,hide : true},/*(表面处理)*/
			//{field : 'bsColor',title : '配色工艺',width : 100,hide : true},/*(表面处理)*/
			{fixed:'left',field : 'bsModel',width : 200,title : '材料规格'},
			{field : 'bsAssess',width : 80,title : '材料单价'},
			{field : 'bsProQty',width : 90,title : '制品重(g)',totalRow : true,hide : true},
				{field : 'bsWaterGap',title : '水口量(g)',width : 90,hide : true}, /*(注塑)*/
				{field : 'bsCave',title : '穴数',width : 60,hide : true}, /*(注塑)*/
				{field : 'bsYield',width : 90,title : '工序良率%'},
				{field : 'bsFee',width : 90,title : '材料总价',
					// templet:function(d){
					// return Number(d.bsQty)*Number(d.bsAssess)/Number(d.bsRadix);
					// },
					totalRow : true},
				{field : 'bsQty',width : 100,title : '材料用量',totalRow : true},
			{field : 'bsUnit',width : 120,title : '材料用量单位'},
			{field : 'purchaseUnit',width : 80,title : '采购单位'},
				{field : 'bsGeneral',width : 80,title : '通用物料',	 templet:function(d){
						return d.bsGeneral =="0"?"否":"是";
					}},
			// {field : 'bsRadix',width : 80,title : '基数'},
			// {field : 'bsGear', width:80, title : '价格挡位', edit:'text',templet:'#selectGear'},
			// {field : 'bsRefer',width : 110,title : '参考价格'},

			// {field : 'bsExplain',width : 110,title : '采购说明'},
			// {field : 'fmemo',width : 110,title : '备注'},
			// {field : 'bsSupplier',width : 110,title : '供应商'}
			] ],
			done : function(res, curr, count) {
				pageCurr = curr;
			}
		});

		// 人工、制费明细
		tableIns1 = table1.render({
			elem : '#processTable',
			// url : context +
			// '/productProcess/getList?bsType='+bsType+'&quoteId='+quoteId,
			method : 'get', // 默认：get请求
			cellMinWidth : 80,
			totalRow : true,
			limit:200,
			limits: [200,50,100,500,1000,2000,5000],
			height : 'full-110',// 固定表头&full-查询框高度
			even : true,// 条纹样式
			page : true,
			request : {
				pageName : 'page',// 页码的参数名称，默认：page
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
			cols : [ [ 
			   {fixed:'left',type : 'numbers'},
			   {fixed:'left',field : 'bsName',width : 150,title : '零件名称',totalRowText : "合计"},
			   {fixed:'left',field : 'workcenterName',width : 150,title : '工作中心',
					templet : function(d) {
						if (d.proc != null) {
							if (d.proc.bjWorkCenter != null) {
								return d.proc.bjWorkCenter.workcenterName == null || undefined ? "" : d.proc.bjWorkCenter.workcenterName;
							} else {
								return "";
							}
						} else {
							return "";
						}
					}},

			   {fixed:'left',field : 'proc',width : 100,title : '工序名称',
				  templet : function(d) {
					if (d.proc != null) {
						return d.proc.procName == null || undefined ? "" : d.proc.procName;
					} else {
						return "";
					}
				}},
				{fixed:'left',field : 'bsOrder',width : 60,title : '顺序'},
			   // {field : 'procfmemo',width : 100,title : '工序说明',
				//   templet : function(d) {
				// 	if (d.proc != null) {
				// 		return d.proc.fmemo == null || undefined ? "" : d.proc.fmemo;
				// 	} else {
				// 		return "";
				// 	}
				// }},
				{fixed:'left',field : 'bsGroups',title : '损耗分组',width : 80},
				{field : 'bsModelType',title : '机台类型',width : 160,hide : true,templet:function (d) {
						if (d.bsTypeList != null) {
							var modelJson = JSON.parse(d.bsTypeList);
							if (modelJson != null && modelJson != "") {
								for (var i = 0; i < modelJson.length; i++) {
									if (d.bsModelType == modelJson[i].MODEL_CODE) {
										return modelJson[i].MODEL_NAME;
									}
								}
								return "";
							}
						}else {
							return "";
						}
					}},
				//{field : 'bsRadix',title : '基数',width : 90,hide : true},

				{field : 'bsFeeLh',title : '人工费(元/小时)',width : 120,hide : true},
				{field : 'bsUserNum',title : '人数',width : 70,hide : true},
				{field : 'bsFeeMh',title : '制造费(元/小时)',width : 120,hide : true},
				{field : 'bsCycle',title : '成型周期(S)',width : 100,hide : true},
				{field : 'bsCave',title : '穴数',width : 60,hide : true},
				{field : 'bsCapacity',title : '产能(个/小时)',width : 120,hide : true},
				{field : 'bsYield',title : '工序良率%',width : 100,hide : true},
				{field : 'bsHouYield',title : '后工序良率%',width : 100,hide : true},
				{field : 'bsLoss',title : '工序良率%',width : 90,hide : true},

				{field : 'bsFeeLhAll',title : '人工总费用',width : 90,totalRow : true,hide : true},
				{field : 'bsFeeMhAll',title : '制造总费用',width : 90,totalRow : true,hide : true},
				{field : 'bsFeeWxAll',title : '加工费(含损耗)',width : 120,totalRow : true,hide : true},

				// {field : 'bsFeeLhAll',title : '人工总费用',width : 130,totalRow : true,hide : true},
				// {field : 'bsFeeMhAll',title : '制费总费用',width : 130,totalRow : true,hide : true},

				{field : 'bsLossTheLh',title : '本工序损料(人工)',width : 150,totalRow : true,hide : true},
				{field : 'bsLossTheMh',title : '本工序损料(制费)',width : 150,totalRow : true,hide : true},
				{field : 'bsLossHouLh',title : '后工序损料(人工)',width : 150,totalRow : true,hide : true},
				{field : 'bsLossHouMh',title : '后工序损料(制费)',width : 150,totalRow : true,hide : true},
				{field : 'bsHeji',title : '工序损料合计',width : 120,totalRow : true,hide : true,templet: function (d) {
					   return (Number(d.bsLossTheLh)+Number(d.bsLossTheMh)+Number(d.bsLossHouLh)+Number(d.bsLossHouMh)).toFixed(4);
				      }
                 },
				//{field : 'bsHeji',title : '工序损料合计',width : 120,totalRow : true,hide : false},
				{field : 'fmemo',title : '备注',hide : true}
				] ],
			done : function(res, curr, count) {
				//pageCurr = curr;
			}
		});

		// 损耗明细
		tableIns3 = table1.render({
			elem : '#processLoseTable',
			// url : context +
			// '/productProcess/getList?bsType='+bsType+'&quoteId='+quoteId,
			method : 'get', // 默认：get请求
			// cellMinWidth : 80,
			totalRow : true,
			limit:200,
			limits: [200,50,100,500,1000,2000,5000],
			height : 'full-110',// 固定表头&full-查询框高度
			even : true,// 条纹样式
			page : true,
			request : {
				pageName : 'page',// 页码的参数名称，默认：page
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
			cols : [ [
				{type : 'numbers'},
				//{field : 'BS_ELEMENT',width : 120,title : '组件名称',sort : true,totalRowText : "合计"},
				{field : 'BS_ELEMENT',width : 90,title : '组件名称',templet:function (d) {
						if(d.BS_ORDER!=null){
							return d.BS_ELEMENT
						}else {
							return '小计'
						}
					}},
				{field : 'BS_NAME',width : 150,title : '零件名称'},
				{field : 'BS_LINK_NAME',width : 150,title : '所属零件'},
				{field : 'WORKCENTER_NAME',width : 110,title : '工作中心'
					// templet : function(d) {
					// 	if (d.proc != null) {
					// 		if (d.proc.bjWorkCenter != null) {
					// 			return d.proc.bjWorkCenter.workcenterName == null || undefined ? "" : d.proc.bjWorkCenter.workcenterName;
					// 		} else {
					// 			return "";
					// 		}
					// 	} else {
					// 		return "";
					// 	}
					// }
					},
				{field : 'PROC_NAME',width : 120,title : '工序名称'
					// templet : function(d) {
					// 	if (d.proc != null) {
					// 		return d.proc.procName == null || undefined ? "" : d.proc.procName;
					// 	} else {
					// 		return "";
					// 	}
					// }
					},
				{field : 'BS_ORDER',width : 60,title : '顺序'},
				{field : 'BS_GROUPS',title : '损耗分组',width : 90},
				{field : 'BS_MATER_COST',title : '材料成本',width : 88,totalRow : true},
				{field : 'BS_FEE_LH_ALL',title : '人工成本',width : 80,totalRow : true},
				{field : 'BS_FEE_MH_ALL',title : '制造成本',width : 80,totalRow : true},
				{field : 'BS_FEE_WX_ALL',title : '外协成本',width : 80,totalRow : true},
				{field : 'BS_COST',title : '成本合计',width : 90,totalRow : true},
				{field : 'BS_YIELD',title : '工序良率%',width : 90,templet:function (d) {
						return Number(d.BS_YIELD).toFixed(2);
					}},
				{field : 'BS_THE_LOSS',title : '工序损耗',width : 90,totalRow : true},
				{field : 'bsCostLoss',title : '工序成本(含损耗)',width : 130,totalRow : true,templet: function (d) {
						return (Number(d.BS_COST)+Number(d.BS_THE_LOSS)).toFixed(4);
					}},
				{field : 'BS_ALL_LOSS',title : '成本累计(含损耗)',width : 130},

			] ],
			done : function(res, curr, count) {
				//pageCurr = curr;
			}
		});

		// 模具费用明细
		tableIns2 = table2.render({
			elem : '#mouldTable',
			// url : context + '/quoteMould/getList?pkQuote='+ quoteId,
			method : 'get',// 默认：get请求
			// toolbar: '#toolbar',
			cellMinWidth : 80,
			totalRow : true,
			height : 'full-110',
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
			cols : [ [
			{type : 'numbers'},
			// {field:'id', title:'ID', width:80, unresize:true, sort:true},
			{field : 'bsName',title : '零件名称',width : 200,totalRowText : "合计:"},
			{field : 'bsMoCode',title : '模具编码',width : 200,templet : '<div>{{d.mjProcFee.productCode}}</div>'},
			{field : 'bsMoName',title : '模具名称',width : 200,templet : '<div>{{d.mjProcFee.productName}}</div>'},
			{field : 'bsMoFee',title : '模具成本',width : 120,templet : '<div>{{d.mjProcFee.feeAll}}</div>',totalRow : true},
			{field : 'stQuote',title : '参考报价',width : 120,templet : '<div>{{d.mjProcFee.stQuote}}</div>',totalRow : true},
			{field : 'bsActQuote',title : '实际报价',width : 120,totalRow : true}
			] ],
			done : function(res, curr, count) {
				// console.log(res)
				// totalCount=res.count
				pageCurr = curr;
			}
		});
		
		//详情按钮
		 $('#detailBtn').click(function(){
			 parent.layui.index.openTabsPage(context+'/quoteSum/toQuoteTree?quoteId='+quoteId,'报价汇总树');
		 })

		// $('#editBtn').click(function(){
		// 	// $(":input").attr("disabled","disabled");
		// 	$("#gl").removeAttr("readonly","readonly");
		// 	$('#gl').removeClass('layui-input grey').addClass('layui-input');
		// 	$("#ml").removeAttr("readonly","readonly");
		// 	$('#ml').removeClass('layui-input grey').addClass('layui-input');
		// 	layui.form.render('');
		// })

	});
});

//初始化比例
function initRate() {
	var bj_all = Number(quoteDetail.data.gl) + Number(quoteDetail.data.p_cb) + Number(quoteDetail.data.profitNet);
	var ml = Number(quoteDetail.data.gl) + Number(quoteDetail.data.profitNet);
	var ml_rate = ml / bj_all * 100;
	var profit_gs = Number(quoteDetail.data.profitNet) / bj_all * 100;
	var gl_rate = Number(quoteDetail.data.gl) / bj_all *100;
	var p_cb_rate = Number(quoteDetail.data.p_cb) / bj_all *100;
	$('#ml_rate').html(ml_rate.toFixed(2) + "%");
	$('#profit_gs').html(profit_gs.toFixed(2) + "%");
	$("#gl_rate").html(gl_rate.toFixed(2) + "%");
	$("#p_cb_rate").html(p_cb_rate.toFixed(2) + "%");
	// $('#hardware_all_rate').html((Number(quoteDetail.data.hardware_all)/Number(quoteDetail.data.p_cb)*100).toFixed(2)+"%");
	// $('#molding_all_rate').html((Number(quoteDetail.data.molding_all)/Number(quoteDetail.data.p_cb)*100).toFixed(2)+"%");
	// $('#surface_all_rate').html((Number(quoteDetail.data.surface_all)/Number(quoteDetail.data.p_cb)*100).toFixed(2)+"%");
	// $('#packag_all_rate').html((Number(quoteDetail.data.packag_all)/Number(quoteDetail.data.p_cb)*100).toFixed(2)+"%");
}

function getMouldDetail(title) {
	tableIns2.reload({
		url : context + '/quoteMould/getList?pkQuote=' + quoteId,
		done : function(res1, curr, count) {
			pageCurr = curr;
			merge(res1.data, [ 'bsName' ], [ 1, 1 ]);
			var feeAll = 0;
			var stQuote = 0;
			res1.data.forEach(function(item, index) {
				feeAll += item.mjProcFee.feeAll;
				stQuote += item.mjProcFee.stQuote;
			});
			$(".layui-table-total").find('tr').find('td[data-field="bsMoFee"]').find('div').html(feeAll);
			$(".layui-table-total").find('tr').find('td[data-field="stQuote"]').find('div').html(stQuote);
		}
	})
	var index = layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#mouldDetailDiv')
	});
	layer.full(index);
}

function getMaterDetail(bsType, title) {
	tableIns.reload({
		url : context + '/productMater/getList?quoteId=' + quoteId + '&bsType=' + bsType +'&bsAgent=0',
		done : function(res1, curr, count) {
			pageCurr = curr;
			res1.data.forEach(function(item, index) {
				// if (bsType == 'hardware') {// 五金
				// 	$('div[lay-id="materTable"]').find('thead').find('th[data-field="bsQty"]').removeClass("layui-hide");
				// 	$('div[lay-id="materTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsQty"]').removeClass("layui-hide");
				// 	$(".layui-table-total").find('tr').find('td[data-field="bsQty"]').removeClass("layui-hide");
				//
				// } else
				if (bsType == 'molding' ||bsType =='hardware') {// 注塑
					$('div[lay-id="materTable"]').find('thead').find('th[data-field="bsWaterGap"]').removeClass("layui-hide");
					$('div[lay-id="materTable"]').find('thead').find('th[data-field="bsCave"]').removeClass("layui-hide");
					$('div[lay-id="materTable"]').find('thead').find('th[data-field="bsProQty"]').removeClass("layui-hide");
					$('div[lay-id="materTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsWaterGap"]').removeClass("layui-hide");
					$('div[lay-id="materTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsCave"]').removeClass("layui-hide");
					$('div[lay-id="materTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsProQty"]').removeClass("layui-hide");
					$(".layui-table-total").find('tr').find('td[data-field="bsWaterGap"]').removeClass("layui-hide");
					$(".layui-table-total").find('tr').find('td[data-field="bsCave"]').removeClass("layui-hide");
					$(".layui-table-total").find('tr').find('td[data-field="bsProQty"]').removeClass("layui-hide");

				} else if (bsType == 'surface') {
					$('div[lay-id="materTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsMachiningType"]').removeClass("layui-hide");
					$('div[lay-id="materTable"]').find('thead').find('th[data-field="bsMachiningType"]').removeClass("layui-hide");
					$('div[lay-id="materTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsColor"]').removeClass("layui-hide");
					$('div[lay-id="materTable"]').find('thead').find('th[data-field="bsColor"]').removeClass("layui-hide");
					$('div[lay-id="materTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsQty"]').removeClass("layui-hide");
					$('div[lay-id="materTable"]').find('thead').find('th[data-field="bsQty"]').removeClass("layui-hide");
					$(".layui-table-total").find('tr').find('td[data-field="bsMachiningType"]').removeClass("layui-hide");
					$(".layui-table-total").find('tr').find('td[data-field="bsColor"]').removeClass("layui-hide");
					$(".layui-table-total").find('tr').find('td[data-field="bsQty"]').removeClass("layui-hide");
				} else if (bsType == 'packag') {
					$('div[lay-id="materTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsQty"]').removeClass("layui-hide");
					$('div[lay-id="materTable"]').find('thead').find('th[data-field="bsQty"]').removeClass("layui-hide");
					$(".layui-table-total").find('tr').find('td[data-field="bsQty"]').removeClass("layui-hide");
				}
			});
		}
	})
	var index = layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#materDetailDiv')
	});
	layer.full(index);
}

function getProcessDetail(bsType, title,lhType) {
	tableIns1.reload({
		url : context + '/productProcess/getList?bsType=' + bsType + '&quoteId=' + quoteId,
		done : function(res, curr, count) {
			pageCurr = curr;var onwanceTotal=0;
			res.data.forEach(function(item, index) {
				if(bsType == ''){
					/*$('div[lay-id="processTable"]').find('thead').find('th[data-field="bsFeeWxAll"]').removeClass("layui-hide");
					$('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsFeeWxAll"]').removeClass("layui-hide");*/
					$('div[lay-id="processTable"]').find('thead').find('th[data-field="bsLoss"]').removeClass("layui-hide");
					$('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsLoss"]').removeClass("layui-hide");
					$(".layui-table-total").find('tr').find('td[data-field="bsLoss"]').removeClass("layui-hide");
					
					$('div[lay-id="processTable"]').find('thead').find('th[data-field="bsLossHouLh"]').removeClass("layui-hide");
					$('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsLossHouLh"]').removeClass("layui-hide");
					$(".layui-table-total").find('tr').find('td[data-field="bsLossHouLh"]').removeClass("layui-hide");

					$('div[lay-id="processTable"]').find('thead').find('th[data-field="bsLossHouMh"]').removeClass("layui-hide");
					$('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsLossHouMh"]').removeClass("layui-hide");
					$(".layui-table-total").find('tr').find('td[data-field="bsLossHouMh"]').removeClass("layui-hide");

					/*$(".layui-table-total").find('tr').find('td[data-field="bsFeeWxAll"]').removeClass("layui-hide");*/

					$('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsYield"]').removeClass("layui-hide");
					$('div[lay-id="processTable"]').find('thead').find('th[data-field="bsYield"]').removeClass("layui-hide");
					$(".layui-table-total").find('tr').find('td[data-field="bsYield"]').removeClass("layui-hide");

					$('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsHouYield"]').removeClass("layui-hide");
					$('div[lay-id="processTable"]').find('thead').find('th[data-field="bsHouYield"]').removeClass("layui-hide");
					$(".layui-table-total").find('tr').find('td[data-field="bsHouYield"]').removeClass("layui-hide");

					$('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsLossTheLh"]').removeClass("layui-hide");
					$('div[lay-id="processTable"]').find('thead').find('th[data-field="bsLossTheLh"]').removeClass("layui-hide");
					$(".layui-table-total").find('tr').find('td[data-field="bsLossTheLh"]').removeClass("layui-hide");

					$('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsLossTheMh"]').removeClass("layui-hide");
					$('div[lay-id="processTable"]').find('thead').find('th[data-field="bsLossTheMh"]').removeClass("layui-hide");
					$(".layui-table-total").find('tr').find('td[data-field="bsLossTheMh"]').removeClass("layui-hide");


					$('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsFeeLhAll"]').removeClass("layui-hide");
					$('div[lay-id="processTable"]').find('thead').find('th[data-field="bsFeeLhAll"]').removeClass("layui-hide");
					$(".layui-table-total").find('tr').find('td[data-field="bsFeeLhAll"]').removeClass("layui-hide");

					$('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsFeeMhAll"]').removeClass("layui-hide");
					$('div[lay-id="processTable"]').find('thead').find('th[data-field="bsFeeMhAll"]').removeClass("layui-hide");
					$(".layui-table-total").find('tr').find('td[data-field="bsFeeMhAll"]').removeClass("layui-hide");

					$('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsHeji"]').removeClass("layui-hide");
					$('div[lay-id="processTable"]').find('thead').find('th[data-field="bsHeji"]').removeClass("layui-hide");
					$(".layui-table-total").find('tr').find('td[data-field="bsHeji"]').removeClass("layui-hide");
					
					
					onwanceTotal += Number(item.bsLossHouLh)+Number(item.bsLossTheMh)+Number(item.bsLossTheLh)+Number(item.bsLossHouMh);
				}else{

					if(lhType =="lh"&&item.bsType != 'out'){
						$(".layui-table-total").find('tr').find('td[data-field="bsFeeLh"]').removeClass("layui-hide");
						$('div[lay-id="processTable"]').find('thead').find('th[data-field="bsFeeLh"]').removeClass("layui-hide");
						$('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsFeeLh"]').removeClass("layui-hide");
						$(".layui-table-total").find('tr').find('td[data-field="bsFeeLhAll"]').removeClass("layui-hide");
						$('div[lay-id="processTable"]').find('thead').find('th[data-field="bsFeeLhAll"]').removeClass("layui-hide");
						$('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsFeeLhAll"]').removeClass("layui-hide");
						$('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsUserNum"]').removeClass("layui-hide");
						$('div[lay-id="processTable"]').find('thead').find('th[data-field="bsUserNum"]').removeClass("layui-hide");
						$(".layui-table-total").find('tr').find('td[data-field="bsUserNum"]').removeClass("layui-hide");
					} else if(lhType =="mh"&&item.bsType != 'out'){
						$('div[lay-id="processTable"]').find('thead').find('th[data-field="bsFeeMh"]').removeClass("layui-hide");
						$('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsFeeMh"]').removeClass("layui-hide");
						$(".layui-table-total").find('tr').find('td[data-field="bsFeeMh"]').removeClass("layui-hide");
						$('div[lay-id="processTable"]').find('thead').find('th[data-field="bsFeeMhAll"]').removeClass("layui-hide");
						$('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsFeeMhAll"]').removeClass("layui-hide");
						$(".layui-table-total").find('tr').find('td[data-field="bsFeeMhAll"]').removeClass("layui-hide");

						$('div[lay-id="processTable"]').find('thead').find('th[data-field="bsModelType"]').removeClass("layui-hide");
						$('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsModelType"]').removeClass("layui-hide");
						$(".layui-table-total").find('tr').find('td[data-field="bsModelType"]').removeClass("layui-hide");
					}

					if (item.bsType == 'out' ) {// 外协
						$('div[lay-id="processTable"]').find('thead').find('th[data-field="bsFeeWxAll"]').removeClass("layui-hide");
						$('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsFeeWxAll"]').removeClass("layui-hide");
						$('div[lay-id="processTable"]').find('thead').find('th[data-field="bsLoss"]').removeClass("layui-hide");
						$('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsLoss"]').removeClass("layui-hide");
						// $('div[lay-id="processTable"]').find('thead').find('th[data-field="bsLossHouLh"]').removeClass("layui-hide");
						// $('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsLossHouLh"]').removeClass("layui-hide");
						// $(".layui-table-total").find('tr').find('td[data-field="bsLossHouLh"]').removeClass("layui-hide");
						$(".layui-table-total").find('tr').find('td[data-field="bsFeeWxAll"]').removeClass("layui-hide");
						$(".layui-table-total").find('tr').find('td[data-field="bsLoss"]').removeClass("layui-hide");
					} else if (item.bsType == 'hardware') {// 五金
						$('div[lay-id="processTable"]').find('thead').find('th[data-field="bsCycle"]').removeClass("layui-hide");
						$('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsCycle"]').removeClass("layui-hide");
						$(".layui-table-total").find('tr').find('td[data-field="bsCycle"]').removeClass("layui-hide");
						// $('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsUserNum"]').removeClass("layui-hide");
						// $('div[lay-id="processTable"]').find('thead').find('th[data-field="bsUserNum"]').removeClass("layui-hide");
						// $(".layui-table-total").find('tr').find('td[data-field="bsUserNum"]').removeClass("layui-hide");
						$('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsYield"]').removeClass("layui-hide");
						$('div[lay-id="processTable"]').find('thead').find('th[data-field="bsYield"]').removeClass("layui-hide");
						$(".layui-table-total").find('tr').find('td[data-field="bsYield"]').removeClass("layui-hide");

					} else if (item.bsType == 'molding') {// 注塑
						$('div[lay-id="processTable"]').find('thead').find('th[data-field="bsCave"]').removeClass("layui-hide");
						$('div[lay-id="processTable"]').find('thead').find('th[data-field="bsCycle"]').removeClass("layui-hide");
						$('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsCave"]').removeClass("layui-hide");
						$('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsCycle"]').removeClass("layui-hide");
						// $('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsUserNum"]').removeClass("layui-hide");
						// $('div[lay-id="processTable"]').find('thead').find('th[data-field="bsUserNum"]').removeClass("layui-hide");
						$('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsYield"]').removeClass("layui-hide");
						$('div[lay-id="processTable"]').find('thead').find('th[data-field="bsYield"]').removeClass("layui-hide");

						$(".layui-table-total").find('tr').find('td[data-field="bsCave"]').removeClass("layui-hide");
						$(".layui-table-total").find('tr').find('td[data-field="bsCycle"]').removeClass("layui-hide");
						// $(".layui-table-total").find('tr').find('td[data-field="bsUserNum"]').removeClass("layui-hide");
						$(".layui-table-total").find('tr').find('td[data-field="bsYield"]').removeClass("layui-hide");
						// $(".layui-table-total").find('tr').find('td[data-field="bsFeeLh"]').removeClass("layui-hide");
						// $(".layui-table-total").find('tr').find('td[data-field="bsFeeMh"]').removeClass("layui-hide");



					} else if (item.bsType == 'surface') {
						$('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsCapacity"]').removeClass("layui-hide");
						$('div[lay-id="processTable"]').find('thead').find('th[data-field="bsCapacity"]').removeClass("layui-hide");
						// $('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsUserNum"]').removeClass("layui-hide");
						// $('div[lay-id="processTable"]').find('thead').find('th[data-field="bsUserNum"]').removeClass("layui-hide");
						$('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsYield"]').removeClass("layui-hide");
						$('div[lay-id="processTable"]').find('thead').find('th[data-field="bsYield"]').removeClass("layui-hide");
						$(".layui-table-total").find('tr').find('td[data-field="bsCapacity"]').removeClass("layui-hide");
						// $(".layui-table-total").find('tr').find('td[data-field="bsUserNum"]').removeClass("layui-hide");
						$(".layui-table-total").find('tr').find('td[data-field="bsYield"]').removeClass("layui-hide");


					} else if (item.bsType == 'packag') {
						$('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsCapacity"]').removeClass("layui-hide");
						$('div[lay-id="processTable"]').find('thead').find('th[data-field="bsCapacity"]').removeClass("layui-hide");
						// $('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsUserNum"]').removeClass("layui-hide");
						// $('div[lay-id="processTable"]').find('thead').find('th[data-field="bsUserNum"]').removeClass("layui-hide");
						$('div[lay-id="processTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsYield"]').removeClass("layui-hide");
						$('div[lay-id="processTable"]').find('thead').find('th[data-field="bsYield"]').removeClass("layui-hide");
						$(".layui-table-total").find('tr').find('td[data-field="bsCapacity"]').removeClass("layui-hide");
						// $(".layui-table-total").find('tr').find('td[data-field="bsUserNum"]').removeClass("layui-hide");
						$(".layui-table-total").find('tr').find('td[data-field="bsYield"]').removeClass("layui-hide");


					}
				}
				// console.log(res.data.length);
				
			});
			if(bsType == ''){
				onwanceTotal += Number(item.bsLossHouLh)+Number(item.bsLossTheMh)+Number(item.bsLossTheLh)+Number(item.bsLossHouMh);
				this.elem.next().find('.layui-table-total td[data-field="bsHeji"] .layui-table-cell').text(onwanceTotal.toFixed(4));
			}
			
			
		}
	})
	var index = layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#processDetailDiv')
	});
	layer.full(index);
}

function getLossDetail(title){
	tableIns3.reload({
		url : context + '/quoteSum/getSumList?quoteId=' + quoteId,
		done : function(res, curr, count) {
			// merge(res.data, [ 'BS_ELEMENT' ], [ 1,1]);
			// merge(res.data, [ 'BS_LINK_NAME' ], [ 3,3]);
			pageCurr = curr;
			var onwanceTotal = 0
			var bsTheLostTotal=0;
			var bsMaterTotal = 0;
			var bsLhAll = 0;
			var bsMhAll = 0;
			var bsWXAll = 0;
			var bsCost = 0; //成本合计
			var yieldTotal = 100;
			var tableIns3 = this.elem.next();
			res.data.forEach(function(item, index) {
				if(item.BS_ORDER!=null) {
					onwanceTotal += Number(item.BS_COST) + Number(item.BS_THE_LOSS);
					yieldTotal *= Number(item.BS_YIELD) / 100;
					bsCost += Number(item.BS_COST);
					bsTheLostTotal += Number(item.BS_THE_LOSS);
					bsMaterTotal +=Number(item.BS_MATER_COST);
					bsLhAll +=Number(item.BS_FEE_LH_ALL);
					bsMhAll +=Number(item.BS_FEE_MH_ALL);
					bsWXAll +=Number(item.BS_FEE_WX_ALL);

				}else {

				}

				if(item.BS_ORDER==null){
					tableIns3.find('tr[data-index=' + index + ']').find('td').css("background-color", "#FFFF00");
				}
				// console.log(onwanceTotal);
			});
			this.elem.next().find('.layui-table-total td[data-field="bsCostLoss"] .layui-table-cell').text(onwanceTotal.toFixed(4));
			this.elem.next().find('.layui-table-total td[data-field="BS_MATER_COST"] .layui-table-cell').text(bsMaterTotal.toFixed(4));
			this.elem.next().find('.layui-table-total td[data-field="BS_THE_LOSS"] .layui-table-cell').text(bsTheLostTotal.toFixed(4));
			this.elem.next().find('.layui-table-total td[data-field="BS_FEE_LH_ALL"] .layui-table-cell').text(bsLhAll.toFixed(2));
			this.elem.next().find('.layui-table-total td[data-field="BS_FEE_MH_ALL"] .layui-table-cell').text(bsMhAll.toFixed(2));
			this.elem.next().find('.layui-table-total td[data-field="BS_FEE_WX_ALL"] .layui-table-cell').text(bsWXAll.toFixed(2));
			this.elem.next().find('.layui-table-total td[data-field="BS_COST"] .layui-table-cell').text(bsCost.toFixed(2));

			this.elem.next().find('.layui-table-total td[data-field="BS_YIELD"] .layui-table-cell').text(yieldTotal.toFixed(2)+"%");
		}
	})
	var index = layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#processLossDiv')
	});
	layer.full(index);
}

// 修改净利润
function updateProfitNet(value) {
	var params = {
		"pkQuote" : quoteId,
		"profitNet" : value
	};
	CoreUtil.sendAjax("/quoteSum/updateProfitNet", JSON.stringify(params), function(data) {
		if (data.result) {
			layer.alert(data.msg, function(index) {
				layer.close(index);
				// cleanProdErr();
				// 加载页面
				// loadAll();
			});
		} else {
			layer.alert(data.msg);
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}

//修改管理费率
function updateBsManageFee(value) {
	var params = {
		"pkQuote" : quoteId,
		"bsManageFee" : value
	};
	CoreUtil.sendAjax("/quoteSum/updateBsManageFee", JSON.stringify(params), function(data) {
		if (data.result) {
			layer.alert(data.msg, function(index) {
				layer.close(index);
				window.location.reload();
				// cleanProdErr();
				// 加载页面
				// loadAll();
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

function merge(res, columsName, columsIndex) {
	var data = res;
	var mergeIndex = 0;// 定位需要添加合并属性的行数
	var mark = 1; // 这里涉及到简单的运算，mark是计算每次需要合并的格子数
	// var columsName = ['itemCode'];//需要合并的列名称
	// var columsIndex = [3];//需要合并的列索引值
	for (var k = 0; k < columsIndex.length; k++) { // 这里循环所有要合并的列
		var trArr = $(".layui-table-body>.layui-table").find("tr");// 所有行
		for (var i = 1; i < data.length; i++) { // 这里循环表格当前的数据
			var tdCurArr = trArr.eq(i).find("td").eq(columsIndex[k]);// 获取当前行的当前列
			var tdPreArr = trArr.eq(mergeIndex).find("td").eq(columsIndex[k]);// 获取相同列的第一列
			if (data[i][columsName[0]] === data[i - 1][columsName[0]]) { // 后一行的值与前一行的值做比较，相同就需要合并
				mark += 1;
				tdPreArr.each(function() {// 相同列的第一列增加rowspan属性
					$(this).attr("rowspan", mark);
				});
				tdCurArr.each(function() {// 当前行隐藏
					$(this).css("display", "none");
				});
			} else {
				mergeIndex = i;
				mark = 1;// 一旦前后两行的值不一样了，那么需要合并的格子数mark就需要重新计算
			}
		}
		mergeIndex = 0;
		mark = 1;
	}
}
