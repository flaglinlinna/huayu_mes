/**
 * 生产投入
 */
var pageCurr;
var tabledata=[];
$(function() {
	layui.use(
			[ 'table', 'form', 'layedit', 'laydate', 'tableSelect' ],
			function() {
				var form = layui.form, layer = layui.layer, layedit = layui.layedit, table = layui.table, laydate = layui.laydate, tableSelect = layui.tableSelect, tableSelect1 = layui.tableSelect;
				;

				tableIns = table.render({
					elem : '#colTable'
					//,url:context+'/interfaces/getRequestList'
					,
					where : {},
					method : 'get' //默认：get请求
					//,toolbar: '#toolbar' //开启头部工具栏，并为其绑定左侧模板
					,
					defaultToolbar : [],
					cellMinWidth : 80,
					page : false,
					data : [],
					request : {
						pageName : 'page' //页码的参数名称，默认：page
						,
						limitName : 'rows' //每页数据量的参数名，默认：limit
					},
					parseData : function(res) {
						// 可进行数据操作
						return {
							"count" : res.data.total,
							"msg" : res.msg,
							"data" : res.data.rows,
							"code" : res.status
						//code值为200表示成功
						}
					},
					cols : [ [ {
						type : 'radio',
						width : 50
					},
					{
						field : 'ITEM_BARCODE',
						title : '箱内条码',
						width : 130
					},{
						field : 'ITEM_BARCODE',
						title : '箱外条码',
						width : 130
					}, {
						field : 'TYPE',
						title : '类型',
						width : 90
					}, {
						field : 'QUANTITY',
						title : '数量',
						width : 90
					},{
						field : 'TASK_NO',
						title : '指令单号'
					},{
						field : 'ITEM_BARCODE',
						title : '物料编号',
						width : 130
					},{
						field : 'CREATE_BY',
						title : '操作人',
						width : 80
					},{
						field : 'CREATE_DATE',
						title : '操作时间',
						width : 100
					}] ],
					done : function(res, curr, count) {
						pageCurr = curr;
					}
				});
				tableSelect=tableSelect.render({
					elem : '#num',
					searchKey : 'keyword',
					checkedKey : 'id',
					searchPlaceholder : '试着搜索',
					table : {
						url:  context +'product/getTaskNo',
						//url:  context +'base/prodproc/getProdList',
						method : 'get',
						cols : [ [
						{ type: 'radio' },//多选  radio
						, {
							field : 'id',
							title : 'id',
							width : 0,hide:true
						}, {
							field : 'TASK_NO',
							title : '制令单号',
							width : 180
						}, {
							field : 'ITEM_NO',
							title : '物料编码',
							width : 150
						},{
							field : 'ITEM_NAME',
							title : '物料描述',
							width : 240
						}, {
							field : 'LINER_NAME',
							title : '组长',
							width : 100
						},{
							field : 'QTY_PLAN',
							title : '制单数量',
							width : 80
						},{
							field : 'OTPT_QTY',
							title : '产出数量',
							width : 80
						},{
							field : 'ORDER_RATE',
							title : '达成率',
							width : 80
						}    ] ],
						page : false,
						request : {
							pageName : 'page' // 页码的参数名称，默认：page
							,
							limitName : 'rows' // 每页数据量的参数名，默认：limit
						},
						parseData : function(res) {
							if(res.result){
								// 可进行数据操作
								return {
									"count" : 0,
									"msg" : res.msg,
									"data" : res.data,
									"code" : res.status
								// code值为200表示成功
								}
							}
							
						},
					},
					done : function(elem, data) {
						//选择完后的回调，包含2个返回值 elem:返回之前input对象；data:表格返回的选中的数据 []
						var da=data.data;
						//console.log(da[0])
						form.val("itemFrom", {
							"num":da[0].TASK_NO,
							"mtrcode" : da[0].ITEM_NO,
							"mtrdescr" : da[0].ITEM_NAME,
							"qty" : da[0].QTY_PLAN,
							"linecode" : da[0].LINER_NAME,
							"inqty" : da[0].OTPT_QTY,
							"rate" : da[0].ORDER_RATE,
						});
						form.render();// 重新渲染
				}
				});
				
				tableSelect2=tableSelect1.render({
					elem : '#num2',
					searchKey : 'keyword',
					checkedKey : 'TASK_NO',
					searchPlaceholder : '试着搜索',
					table : {
						url:  context +'product/getHXTaskNo',
						//url:  context +'base/prodproc/getProdList',
						method : 'get',
						cols : [ [
						{ type: 'checkbox' },//多选  radio
						, {
							field : 'id',
							title : 'id',
							width : 0,hide:true
						}, {
							field : 'TASK_NO',
							title : '制令单号',
							width : 180
						}, {
							field : 'ITEM_NO',
							title : '物料编码',
							width : 150
						},{
							field : 'ITEM_NAME',
							title : '物料描述',
							width : 240
						}, {
							field : 'LINER_NAME',
							title : '组长',
							width : 100
						},{
							field : 'QTY_PLAN',
							title : '数量',
							edit:'text',style:'background: #98FB98;opacity: 0.3',
							width : 80
						}] ],
						page : false,
						request : {
							pageName : 'page' // 页码的参数名称，默认：page
							,
							limitName : 'rows' // 每页数据量的参数名，默认：limit
						},
						parseData : function(res) {
							if(res.result){
								// 可进行数据操作
								return {
									"count" : 0,
									"msg" : res.msg,
									"data" : res.data,
									"code" : res.status
								// code值为200表示成功
								}
							}
							
						},
					},
					done : function(elem, data) {
						//选择完后的回调，包含2个返回值 elem:返回之前input对象；data:表格返回的选中的数据 []
						var da=data.data;
						console.log(da)
						var str = "";
						da.forEach(function(d){  
						   if(d.QTY_PLAN){
							   str += d.TASK_NO+"@"+d.QTY_PLAN+",";
						   }else{
							   layer.alert("请在"+d.TASK_NO+"行输入数量!");
							   return false;
						   }
						});
						$('#num2').val(str)
				}
				});
				
				// 监听工具条
				table.on('tool(colTable)', function(obj) {
					console.log(obj)
					var data = obj.data;
					console.log(data)
					if (obj.event === 'del') {
						// 删除
						del(obj,data.ID, data.ITEM_BARCODE);
					}
				});
				//监听下拉框
				$("#hx_label").hide();
				$("#hx_div").hide();
				form.on('select(ptyle)', function(data){
					if(data.value == 1){
						 $("#hx_label").hide();
						 $("#hx_div").hide();
					}else{
						 $("#hx_label").show();
						 $("#hx_div").show();
					}
				});

			});	
	
	
	$('#nbarcode').bind('keypress',function(event){
        if(event.keyCode == "13") {
        	//alert('你输入的内容为：' + $('#barcode').val());
        	if(!$( "input[name='num']").val()){
        		layer.alert("请先选择制令单号!");
        		return false;
        	}
        	if($('#nbarcode').val()){
        		afterNei($('#nbarcode').val())
        	}else{
        		layer.alert("请先扫描内箱条码!");
        	}
        }
    });
	
	$('#wbarcode').bind('keypress',function(event){
        if(event.keyCode == "13") {
        	//alert('你输入的内容为：' + $('#barcode').val());
        	if(!$( "input[name='num']").val()){
        		layer.alert("请先选择制令单号!");
        		return false;
        	}
        	if(!$('#nbarcode').val()){
        		layer.alert("请先扫描内箱条码!");
        		return false;
        	}
        	if($('#wbarcode').val()){
        		afterWai($('#wbarcode').val())
        	}else{
        		layer.alert("请先扫描外箱条码!");
        	}
        }
    });
});

 function afterNei(barcode){
	 var params={"barcode":barcode,"task_no":$( "input[name='num']").val()}
		CoreUtil.sendAjax("product/afterNei", params, function(data) {
			
			if (data.result) {
				 $("#wbarcode").get(0).focus();
			}else{
				layer.alert(data.msg);
				$('#nbarcode').val('');
			}
		}, "GET", false, function(res) {
			layer.alert(res.msg);
		});
 }
 
 function afterWai(barcode){
	 var params={"wbarcode":barcode,"task_no":$( "input[name='num']").val(),"nbarcode":$('#nbarcode').val(),
			 "ptype":$("select[name='ptyle'] option:selected").val(),"hx":$('#num2').val()}
		CoreUtil.sendAjax("product/afterWai", params, function(data) {
			console.log(data)
			if (data.result) {
				$('#inqty').val(data.data.Qty);
				$('#rate').val(data.data.Rate);
				tableIns.reload({
					data:data.data.List
				});
				/*var ptype = "正常扫描";
				if($("select[name='ptyle'] option:selected").val()=='2'){
					ptype = "合箱扫描";
				}
				var temp={"nbarcode":$('#nbarcode').val(),
						"wbarcode":$('#wbarcode').val(),
						"ptype":ptype,
						"qty":0,
						"task_no":$( "input[name='num']").val(),
						"item_no":$( "input[name='mtrcode']").val()}	
				tabledata.push(temp)
				tableIns.reload({
					data:tabledata
				});*/
			}else{
				layer.alert(data.msg);
			}
		}, "GET", false, function(res) {
			layer.alert(res.msg);
		});
 }
 
 function selectChange(){
	 alert($("select[name='ptyle'] option:selected").val())
 }
 
