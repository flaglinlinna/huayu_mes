/**
 * 制造部工艺管理
 * 五金:hardware
 * 注塑:molding
 * 表面处理:surface
 * 组装:packag
 * 外协:out
 */
var pageCurr;
var _index = 0;
var fileId = "";
$(function() {
	layui.use([ 'form', 'table','upload','tableSelect' ], function() {
		var table = layui.table, table2 = layui.table,form = layui.form,upload = layui.upload,tableSelect = layui.tableSelect,
			tableSelect1 = layui.tableSelect,tableSelect2 = layui.tableSelect,tableSelect3 = layui.tableSelect,upload2 =layui.upload;
		isComplete()
		tableIns = table.render({
			elem : '#listTable',
			url : context + '/productProcess/getList?bsType='+bsType+'&quoteId='+quoteId,
			method : 'get', // 默认：get请求
			cellMinWidth : 80,
			//toolbar: '#toolbar',
			height:'full-65',//固定表头&full-查询框高度
			even:true,//条纹样式
			page : true,
			limit:50,
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
			cols : [ [ {type : 'numbers',style:'background-color:#d2d2d2'},
				{field:"id",title:"ID",hide:true},
				{field : 'bsName', width:150, title : '零件名称',sort:true,style:'background-color:#d2d2d2'},
				{field : 'bsOrder',width:90, title : '工艺顺序',sort:true,style:'background-color:#d2d2d2'},
				{field : 'proc', width:120, title : '工序名称',style:'background-color:#d2d2d2',
					templet:function (d) {
						if(d.proc!=null){
							return d.proc.procName==null||undefined?"":d.proc.procName;
						}else {
							return "";
						}
					}},
				// {field : 'procfmemo', width:100, title : '工序说明',style:'background-color:#d2d2d2',
				// 	templet:function (d) {
				// 		if(d.proc!=null){
				// 			return d.proc.fmemo==null||undefined?"":d.proc.fmemo;
				// 		}else {
				// 			return "";
				// 		}
				// 	}},
				{field : 'workcenterName', width:120, title : '工作中心',style:'background-color:#d2d2d2'
					,templet:function (d) {
						if(d.proc!=null){
							if(d.proc.bjWorkCenter!=null){
								return d.proc.bjWorkCenter.workcenterName==null||undefined?"":d.proc.bjWorkCenter.workcenterName;
							}else {
								return "";
							}
						}else {
							return "";
						}
					}},
				/*{field : 'bsModelType', width:100, title : '机台类型',width:90,},*/
				{field : 'bsModelType',width : 200,title : '机台类型',templet : '#selectModelType',style : 'background-color:#ffffff'},
				/*{field : 'bsRadix', title : '基数<span style="color:red;font-size:12px;">*</span>',width:90,edit:'text',style : 'background-color:#ffffff'},*/
				{field : 'bsUserNum', title : '人数<span style="color:red;font-size:12px;">*</span>',width:60,edit:'text',hide:true,style : 'background-color:#ffffff'},
				{field : 'bsCycle', title : '成型周期(S)<span style="color:red;font-size:12px;">*</span>', width:90,edit:'text', hide:true,style : 'background-color:#ffffff'},
				{field : 'bsYield', title : '工序良率%<span style="color:red;font-size:12px;">*</span>', width:90,edit:'text',hide:true,style : 'background-color:#ffffff'},
				{field : 'bsLoss', title : '损耗率<span style="color:red;font-size:12px;">*</span>', width:80,edit:'text',hide:true,style : 'background-color:#ffffff'},
				{field : 'bsCave', title : '穴数<span style="color:red;font-size:12px;">*</span>',edit:'text',width:50, hide:true,style : 'background-color:#ffffff'},
				{field : 'bsCapacity', title : '产能(个/小时)<span style="color:red;font-size:12px;">*</span>',edit:'text',width:105, hide:true,style : 'background-color:#ffffff'},
				{field : 'bsFeeWxAll', title : '外协价格<span style="color:red;font-size:12px;">*</span>',edit:'text',width:120, hide:true,style : 'background-color:#ffffff'},
				//{field : 'fmemo', title : '备注',edit:'text',style : 'background-color:#ffffff'},
				// {fixed : 'right', title : '操作', align : 'center',width:120, toolbar : '#optBar'}
				] ],
			done : function(res, curr, count) {
				pageCurr = curr;

				var tableIns = this.elem.next(); // 当前表格渲染之后的视图
				layui.each(res.data, function(i, item){
					if(bsType!="out") {
						if (item.bsStatus == 1) {
							tableIns.find('tr[data-index=' + i + ']').find('td').data('edit', false).css("background-color", "#d2d2d2");
							$("select[name='selectModelType']").attr("disabled", "disabled");
							form.render('select');
						}
					}else {
						if(iStatus>=2){
							tableIns.find('tr[data-index=' + i + ']').find('td').data('edit', false).css("background-color", "#d2d2d2");
							$("select[name='selectModelType']").attr("disabled", "disabled");
							form.render('select');
						}
					}
				});

				//根据不同的类型显示不同的字段
				res.data.forEach(function (item, index) {
					if(bsType == 'out'){//外协
						$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsFeeWxAll"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsFeeWxAll"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsLoss"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsLoss"]').removeClass("layui-hide");
					}else if(bsType == 'hardware'){//五金
						$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsCycle"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsCycle"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsUserNum"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsUserNum"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsYield"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsYield"]').removeClass("layui-hide");
					}else if(bsType == 'molding'){//注塑
						$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsCave"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsCycle"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsCave"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsCycle"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsUserNum"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsUserNum"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsYield"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsYield"]').removeClass("layui-hide");
					}else if(bsType == 'surface'){
						$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsCapacity"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsCapacity"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsUserNum"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsUserNum"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsYield"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsYield"]').removeClass("layui-hide");
					}else if(bsType == 'packag'){
						$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsCapacity"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsCapacity"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsUserNum"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsUserNum"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsYield"]').removeClass("layui-hide");
						$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsYield"]').removeClass("layui-hide");
					}
				});
			}
		});

		tableIns2 = table.render({
			elem : '#uploadList',
			// url : context + '/productProcess/getList?bsType='+bsType+'&quoteId='+quoteId,
			method : 'get', // 默认：get请求
			cellMinWidth : 80,
			height:'full-110',//固定表头&full-查询框高度
			even:true,//条纹样式
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
				// {type: "checkbox"},
				{type : 'numbers',width:50,style:'background-color:#d2d2d2'},
				{field : 'checkStatus', width:100, title : '状态',sort:true,style:'background-color:#d2d2d2',templet: '#checkStatus'},
				// {field : 'enabled', width:100, title : '是否导入',sort:true,style:'background-color:#d2d2d2', templet: '#enabledTpl'},
				{field : 'errorInfo', width:150, title : '错误信息',sort:true,style:'background-color:#d2d2d2'},
				{field : 'bsName', width:150, title : '零件名称',sort:true,style:'background-color:#d2d2d2'},
				{field : 'bsOrder',width:150, title : '工艺顺序',sort:true,style:'background-color:#d2d2d2'},
				{field : 'proc', width:150, title : '工序名称',style:'background-color:#d2d2d2',
					templet:function (d) {
						if(d.proc!=null){
							return d.proc.procName==null||undefined?"":d.proc.procName;
						}else {
							return "";
						}
					}},
				// {field : 'procfmemo', width:100, title : '工序说明',style:'background-color:#d2d2d2',
				// 	templet:function (d) {
				// 		if(d.proc!=null){
				// 			return d.proc.fmemo==null||undefined?"":d.proc.fmemo;
				// 		}else {
				// 			return "";
				// 		}
				// 	}},
				{field : 'workcenterName', width:100, title : '工作中心',style:'background-color:#d2d2d2'
					,templet:function (d) {
						if(d.proc!=null){
							if(d.proc.bjWorkCenter!=null){
								return d.proc.bjWorkCenter.workcenterName==null||undefined?"":d.proc.bjWorkCenter.workcenterName;
							}else {
								return "";
							}
						}else {
							return "";
						}
					}},
				{field : 'bsModelType', width:100, title : '机台类型',edit:'text'},
				// {field : 'bsRadix', title : '基数',width:80,edit:'text'},
				{field : 'bsUserNum', title : '人数',width:80,edit:'text'},
				{field : 'bsCycle', title : '成型周期(S)', width:120,edit:'text', hide:true},
				{field : 'bsYield', title : '工序良率%', width:120,edit:'text'},
				{field : 'bsCave', title : '穴数',edit:'text', width:80, hide:true},
				{field : 'bsCapacity', title : '产能(个/小时)',edit:'text', width:105, hide:true},
				{field : 'bsLoss', title : '损耗率<span style="color:red;font-size:12px;">*</span>', width:80,edit:'text',hide:true},
				{field : 'bsFeeWxAll', title : '外协价格<span style="color:red;font-size:12px;">*</span>',edit:'text',width:120, hide:true},

				// {field : 'fmemo', title : '备注',width:120,edit:'text'},
				// {fixed : 'right', title : '操作', align : 'center',width:120, toolbar : '#optBar'}
				] ],
			done : function(res, curr, count) {
				pageCurr = curr;
				// if(bsType == 'out'){//外协
				// 	$('div[lay-id="uploadList"]').find('thead').find('th[data-field="bsFeeWxAll"]').removeClass("layui-hide");
				// 	$('div[lay-id="uploadList"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsFeeWxAll"]').removeClass("layui-hide");
				// }else if(bsType == 'hardware'){//五金
				// 	$('div[lay-id="uploadList"]').find('thead').find('th[data-field="bsCycle"]').removeClass("layui-hide");
				// 	$('div[lay-id="uploadList"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsCycle"]').removeClass("layui-hide");
				// }else if(bsType == 'molding'){//注塑
				// 	$('div[lay-id="uploadList"]').find('thead').find('th[data-field="bsCave"]').removeClass("layui-hide");
				// 	$('div[lay-id="uploadList"]').find('thead').find('th[data-field="bsCycle"]').removeClass("layui-hide");
				// 	$('div[lay-id="uploadList"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsCave"]').removeClass("layui-hide");
				// 	$('div[lay-id="uploadList"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsCycle"]').removeClass("layui-hide");
				//
				// }else if(bsType == 'surface'){
				// 	$('div[lay-id="uploadList"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsCapacity"]').removeClass("layui-hide");
				// 	$('div[lay-id="uploadList"]').find('thead').find('th[data-field="bsCapacity"]').removeClass("layui-hide");
				// }else if(bsType == 'packag'){
				// 	$('div[lay-id="uploadList"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsCapacity"]').removeClass("layui-hide");
				// 	$('div[lay-id="uploadList"]').find('thead').find('th[data-field="bsCapacity"]').removeClass("layui-hide");
				// }

			}
		});

		//上传控件
		upload2.render({
			elem: '#upload2'
			,url: context+'/file/upload'
			,accept: 'file' //普通文件
			,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致。
				layer.load(); //上传loading
			}
			,done: function(res,index, upload){
				layer.closeAll('loading'); //关闭loading
				if(res.result == true){
					document.getElementById("filelist").innerHTML = "";
					document.getElementById("filelist").innerHTML = $("#filelist").html()+getExcField(_index,res.data);
					_index++;
					fileId +=res.data.id;
				}
				$('#fileId').val(fileId);
				$('#fileName').val(res.data.bsName);
			}
			,error: function(index, upload){
				layer.closeAll('loading'); //关闭loading
			}
		});

		tableSelect=tableSelect.render({
			elem : '#procName',
			searchKey : 'keyword',
			checkedKey : 'id',
			searchPlaceholder : '关键字搜索',
			table : {
				url : context + '/productProcess/getProcListByType?bsType='+bsType,
				method : 'get',
				parseData : function(res) {
					// 可进行数据操作
					return {
						"count" : res.data.length,
						"msg" : res.msg,
						"data" : res.data,
						"code" : res.status
						// code值为200表示成功
					}
				},
				cols : [ [
					{ type: 'radio' },//单选  radio
					{field : 'id', title : 'id', width : 0,hide:true},
					{type : 'numbers'},
					{field : 'PROC_NO', title : '工序编号' },
					{field : 'PROC_NAME', title : '工序名称' },
					{field : 'WORKCENTER_NAME', title : '工作中心名称' },
					{field : 'STATUS', title : '人工制费',templet:function (d){
							if(d.STATUS=="1"){
								return "已维护";
							}else {
								return "未维护"
							}
						} },
				] ],
				page : false,
				request : {
					pageName : 'page', // 页码的参数名称，默认：page
					limitName : 'rows' // 每页数据量的参数名，默认：limit
				},

			},
			done : function(elem, data) {
				var da=data.data;
				//选择完后的回调，包含2个返回值 elem:返回之前input对象；data:表格返回的选中的数据 []
				form.val("productProcessForm", {
					"pkProc":da[0].ID,
					"procName":da[0].PROC_NAME
				});
				form.render();// 重新渲染
			}
		});

		// 机台类型列表
		typeTableSelect = tableSelect3.render({
			elem : '#bsModelName',
			searchKey : 'keyword',
			checkedKey : 'id',
			searchPlaceholder : '试着搜索',
			table : {
				// width : 220,
				url : context + '/basePrice/modelType/getList?bsType='+bsType,
				method : 'get',
				cols : [ [ {type : 'radio'},
					{field : 'ID', title : 'ID', width : 0, hide : true},
					{field : 'modelCode', title : '机台编码'},
					{field : 'modelName', title : '机台描述'},
					{field : 'workCenterName', title : '工作中心'}
				] ],
				page : true,
				request : {
					pageName : 'page' // 页码的参数名称，默认：page
					,
					limitName : 'rows' // 每页数据量的参数名，默认：limit
				},
				parseData : function(res) {
					if (!res.result) {
						// 可进行数据操作
						return {
							"count" : 0,
							"msg" : res.msg,
							"data" : [],
							"code" : res.status
							// code值为200表示成功
						}
					}
					return {
						"count" : res.data.total,
						"msg" : res.msg,
						"data" : res.data.rows,
						"code" : res.status
						// code值为200表示成功
					}
				},
			},
			done : function(elem, data) {
				// 选择完后的回调，包含2个返回值
				// elem:返回之前input对象；data:表格返回的选中的数据 []
				var da = data.data;
				form.val("productProcessForm", {
					"bsModelName":da[0].modelName,
					"bsModelType" : da[0].modelCode
				});
				form.render();// 重新渲染
			}
		});



		// setData();
		// positiveNum
		//自定义验证规则
		form.verify({
			double: function(value){
				if(/^\d+$/.test(value)==false && /^\d+\.\d+$/.test(value)==false && value!="" && value!=null)
				{
					return '只能输入数字';
				}
			},
			positiveNum:function (value) {
				if(/^\d+$/.test(value)==false && /^\d+\.\d+$/.test(value)==false || value<=0){
					return '基数只能输入数字且大于0';
				}

			}
		});


		table.on('edit(listTable)',function (obj) {
			console.log(obj.field);//当前编辑列名
			var bsRadix = obj.data.bsRadix;
			var bsUserNum = obj.data.bsUserNum;
			var bsYield = obj.data.bsYield;
			var bsCave = obj.data.bsCave;
			var bsCycle = obj.data.bsCycle;
			var bsLoss = obj.data.bsLoss;
			if(obj.field =="bsRadix") {
				if (/^\d+$/.test(bsRadix) == false && /^\d+\.\d+$/.test(bsRadix) == false || bsRadix <= 0) {
					layer.msg("基数必填且只能输入数字且大于0");
					// loadAll();
					return false;
				}
			}else if(obj.field =="bsUserNum") {
				if (/^\d+$/.test(bsUserNum) == false && /^\d+\.\d+$/.test(bsUserNum) == false) {
					layer.msg("人数只能输入数字");
					// loadAll();
					return false;
				}
			}else if(obj.field =="bsYield") {
				if (/^\d+$/.test(bsYield) == false && /^\d+\.\d+$/.test(bsYield) == false) {
					layer.msg("工序良率只能输入数字");
					// loadAll();
					return false;
				}else if(Number(bsYield)>100){
					layer.msg("工序良率不能大于100");
					loadAll();
					return false;
				}
			}else if(obj.field =="bsCycle") {
				if (/^\d+$/.test(bsCycle) == false && /^\d+\.\d+$/.test(bsCycle) == false) {
					layer.msg("成型周期只能输入数字");
					// loadAll();
					return false;
				}
			}else if(obj.field =="bsCave") {
				if (/^\d+$/.test(bsCave) == false && /^\d+\.\d+$/.test(bsCave) == false) {
					layer.msg("穴数只能输入数字");
					// loadAll();
					return false;
				}
			}else if(obj.field =="bsLoss") {
				if (/^\d+$/.test(bsLoss) == false && /^\d+\.\d+$/.test(bsLoss) == false && bsLoss != "" && bsLoss != null) {
					layer.msg("损耗率只能输入数字");
					// loadAll();
					return false;
				}
			}
			//20210423 去除编辑刷新，统一按钮保存
			// obj.field = obj.data;
			// editSubmit(obj);
		})

		initSelect();

		table.on('edit(uploadTable)',function (obj) {
			//console.log(obj.field);//当前编辑列名
			//var bsRadix = obj.data.bsRadix;
			var bsUserNum = obj.data.bsUserNum;
			var bsYield = obj.data.bsYield;
			var bsCave = obj.data.bsCave;
			var bsCycle = obj.data.bsCycle;
			/*if(obj.field =="bsRadix") {
				if (/^\d+$/.test(bsRadix) == false && /^\d+\.\d+$/.test(bsRadix) == false || bsRadix <= 0) {
					layer.msg("基数必填且只能输入数字且大于0");
					loadAll2();
					return false;
				}
			}else*/ 
			if(obj.field =="bsUserNum") {
				if (/^\d+$/.test(bsUserNum) == false && /^\d+\.\d+$/.test(bsUserNum) == false && bsUserNum != "" && bsUserNum != null) {
					layer.msg("人数只能输入数字");
					loadAll2();
					return false;
				}
			}else if(obj.field =="bsYield") {
				if (/^\d+$/.test(bsYield) == false && /^\d+\.\d+$/.test(bsYield) == false && bsYield != "" && bsYield != null) {
					layer.msg("工序良率只能输入数字");
					loadAll2();
					return false;
				}else if(Number(bsYield)>100){
					layer.msg("工序良率不能大于100");
					loadAll();
					return false;
				}else if(Number(data.field.bsYield)<=0){
					layer.msg("工序良率不能小于0");
					loadAll();
					return false;
				}
			}else if(obj.field =="bsCycle") {
				if (/^\d+$/.test(bsCycle) == false && /^\d+\.\d+$/.test(bsCycle) == false && bsCycle != "" && bsCycle != null) {
					layer.msg("成型周期只能输入数字");
					loadAll2();
					return false;
				}
			}else if(obj.field =="bsCave") {
				if (/^\d+$/.test(bsCave) == false && /^\d+\.\d+$/.test(bsCave) == false) {
					layer.msg("穴数只能输入数字且不能为空");
					loadAll2();
					return false;
				}
			}
			obj.field = obj.data;
			editSubmitTemp(obj);
		})

		// 监听在职操作
		form.on('switch(isStatusTpl)', function(obj) {
			setStatus(obj, this.value, this.name, obj.elem.checked);
		});
		// 监听工具条
		table.on('tool(listTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delProdErr(data, data.id, data.bsName);
			} else if (obj.event === 'edit') {
				// 编辑
				getProdErr(data, data.id);
			}
		});

		//监听导入临时表的工具条
		table.on('tool(uploadTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delProcessTemp(data, data.id, data.bsName);
			} else if (obj.event === 'edit') {
				// 编辑
				// getProdErr(data, data.id);
				getProcessTemp(data, data.id);
			}
		});

		// 监听提交
		form.on('submit(addSubmit)', function(data) {

			if(bsType=="hardware"){
				if(data.field.bsCycle==""||data.field.bsYield==""||data.field.bsUserNum==""){
					layer.msg("请输入所有带*的必填项");
					return false;
				}else if(Number(data.field.bsYield)>100){
					layer.msg("工序良率不能大于100");
					loadAll();
					return false;
				}else if(Number(data.field.bsYield)<=0){
					layer.msg("工序良率不能小于0");
					loadAll();
					return false;
				}
			}else if(bsType=="molding"){
				if(data.field.bsCycle==""||data.field.bsYield==""||data.field.bsUserNum==""||data.field.bsCave==""){
					layer.msg("请输入所有带*的必填项");
					return false;
				}else if(Number(data.field.bsYield)>100){
					layer.msg("工序良率不能大于100");
					loadAll();
					return false;
				}else if(Number(data.field.bsYield)<=0){
					layer.msg("工序良率不能小于0");
					loadAll();
					return false;
				}
			}else if(bsType=="surface"){
				if(data.field.bsYield==""||data.field.bsUserNum==""||data.field.bsCapacity ==""){
					layer.msg("请输入所有带*的必填项");
					return false;
				}else if(Number(data.field.bsYield)>100){
					layer.msg("工序良率不能大于100");
					loadAll();
					return false;
				}else if(Number(data.field.bsYield)<=0){
					layer.msg("工序良率不能小于0");
					loadAll();
					return false;
				}
			}else if(bsType=="packag"){
				if(data.field.bsYield==""||data.field.bsUserNum==""||data.field.bsCapacity ==""){
					layer.msg("请输入所有带*的必填项");
					return false;
				}else if(Number(data.field.bsYield)>100){
					layer.msg("工序良率不能大于100");
					loadAll();
					return false;
				}else if(Number(data.field.bsYield)<=0){
					layer.msg("工序良率不能小于0");
					loadAll();
					return false;
				}
			}else if(bsType=="out"){
				if(data.field.bsLoss==""||data.field.bsFeeWxAll==""){
					layer.msg("请输入所有带*的必填项");
					return false;
				}
			}

			if (data.field.id == null || data.field.id == "") {
				// 新增
				addSubmit(data);
			} else {
				editSubmit(data);
			}
			return false;
		});

		// 监听提交
		form.on('submit(addSubmit1)', function(data) {
			editSubmitTemp(data);
			return false;
		});

		// 监听搜索框
		form.on('submit(searchSubmit)', function(data) {
			// 重新加载table
			load(data);
			return false;
		});
		// 编辑工艺
		function getProdErr(obj, id) {
			var modelJson = JSON.parse(obj.bsTypeList);
			var modelName = "";
			$("#bsModelType").empty();
			if(modelJson!=null&&modelJson!="") {
				for (var i = 0; i < modelJson.length; i++) {
					$("#bsModelType").append(
						"<option value=" + modelJson[i].MODEL_CODE + ">" + modelJson[i].MODEL_NAME + "</option>")
					if (modelJson[i].MODEL_CODE == obj.bsModelType) {
						// modelName = modelJson[i].MODEL_NAME
						$("#bsModelType").val(modelJson[i].MODEL_CODE);
					}

				}
			}

			layui.form.render('select');

			var procName="";
			if(obj.proc!=null){
				procName=obj.proc.procName
			}
			form.val("productProcessForm", {
				"id" : obj.id,
				"bsName" : obj.bsName,
				"procName" : procName,
				"pkProc":obj.pkProc,
				"bsOrder" : obj.bsOrder,
				"bsModelName":modelName,
				"bsModelType" : obj.bsModelType,
				"bsRadix" : obj.bsRadix,
				"fmemo" : obj.fmemo,
				"bsUserNum":obj.bsUserNum,
				"bsCave":obj.bsCave,
				"bsCapacity":obj.bsCapacity,
				"bsCycle":obj.bsCycle,
				"bsYield":obj.bsYield,
				"bsLoss":obj.bsLoss,
				"bsFeeWxAll":obj.bsFeeWxAll
			});
			if(obj.fileId!=null) {
				var params = {
					"id": obj.fileId,
					"bsName": obj.fileName,
					"qsFileId": obj.id,
					"bsContentType": "stp"
				}
				document.getElementById("filelist").innerHTML = $("#filelist").html() + getExcFieldBefore(params, params.qsFileId, "/productProcess/delFile");
			}
			//获取附件文件
			// CoreUtil.sendAjax("/productProcess/getFileList?customId="+obj.id,"",
			// 	function(data) {
			// 		console.log(data);
			// 		for(var i =0;i<data.data.length; i++){
			// 			document.getElementById("filelist").innerHTML = $("#filelist").html()+getExcFieldBefore(data.data[i],data.data[i].qsFileId,"/productProcess/delFile");
			// 		}
			// 	}, "GET", false, function(res) {
			// 		layer.alert(res.msg);
			// 	});

			openProdErr(id, "编辑工艺信息")
		};

		function getProcessTemp(obj, id) {
			var procName="";
			if(obj.proc!=null){
				procName=obj.proc.procName
			}
			form.val("productProcessTempForm", {
				"id" : obj.id,
				"bsName" : obj.bsName,
				"procName" : procName,
				"pkProc":obj.pkProc,
				"bsOrder" : obj.bsOrder,
				"bsModelType" : obj.bsModelType,
				"bsRadix" : obj.bsRadix,
				"fmemo" : obj.fmemo,
				"bsUserNum":obj.bsUserNum,
				"bsCave":obj.bsCave,
				"bsCapacity":obj.bsCapacity,
				"bsCycle":obj.bsCycle,
				"bsYield":obj.bsYield,
			});
			openProcessTemp(id, "编辑工艺信息")
		};

		//导入
		upload.render({
			elem: '#upload'
			,url: context + '/productProcessTemp/importExcel'
			,accept: 'file' //普通文件
			,data: {
				bsType: function(){
					return bsType;
				},
				pkQuote: function(){
					return quoteId;
				}
			}
			,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
				layer.load(); //上传loading
			}
			,done: function(res,index, upload){
				layer.closeAll('loading'); //关闭loading
				layer.alert(res.msg, function (index) {
					layer.close(index);
					loadAll();
				});

			}
			,error: function(index, upload){
				layer.alert("操作请求错误，请您稍后再试",function(){
				});
				layer.closeAll('loading'); //关闭loading
				layer.close(index);
			}
		});

		//监听机台类型下拉选择 并修改
		form.on('select(selectModelType)', function (data) {
			//获取当前行tr对象
			// var elem = data.othis.parents('tr');
			//第一列的值是Guid，取guid来判断
			// var Guid= elem.first().find('td').eq(1).text();
			//选择的select对象值；
			// var selectValue = data.value;
			// updateModelType(Guid,selectValue);

			var elem = data.othis.parents('tr').attr('data-index');
			layui.table.cache['listTable'][elem].bsModelType = data.value;
		})
	});
});
function isComplete() {
	if (iStatus >= 2) {
		$("#addbtn").addClass("layui-btn-disabled").attr("disabled", true)
		// $("#exportbtn").addClass("layui-btn-disabled").attr("disabled", true)
		$("#loadbtn").addClass("layui-btn-disabled").attr("disabled", true)
		$("#savebtn").addClass("layui-btn-disabled").attr("disabled", true)
		$("#editListBtn").addClass("layui-btn-disabled").attr("disabled", true)
	}
}

function initSelect() {
	$("#bsName").empty();
	var bomlist=bomNameList.data;
	for(var i=0;i<bomlist.length;i++){
		if(i==0){
			$("#bsName").append("<option value=''> 请选择</option>");
		}
		$("#bsName").append(
			"<option value='" + bomlist[i].BS_COMPONENT + "'>"
			+ bomlist[i].BS_COMPONENT +"</option>");
	}
	layui.form.render();
}

function saveTable() {
	var dates = layui.table.cache['listTable'];
	// console.log(dates);
	CoreUtil.sendAjax("/productProcess/saveTable", JSON.stringify(dates),
		function(data) {
			if (isLogin(data)) {
				if (data.result == true) {
					loadAll();
				} else {
					layer.alert(data.msg, function() {
						layer.closeAll();
						loadAll();
					});
				}
			}
		});
}

function initSelectTemp() {
	$("#bsName1").empty();
	var bomlist=bomNameList.data;
	for(var i=0;i<bomlist.length;i++){
		if(i==0){
			$("#bsName1").append("<option value=''> 请选择</option>");
		}
		$("#bsName1").append(
			"<option value=" + bomlist[i].BS_COMPONENT + ">"
			+ bomlist[i].BS_COMPONENT +"</option>");
	}
	layui.form.render();
}


function uploadChecked() {
	var params = {
		"pkQuote": quoteId,
		"bsType":bsType
	};
	CoreUtil.sendAjax("/productProcess/uploadCheck", JSON.stringify(params), function(
		data) {
		if (data.result) {
			layer.alert(data.data, function() {
				layer.closeAll();
				cleanProdErr();
				// 加载页面
				loadAll();
			});
		} else {
			layer.alert(data.msg);
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}


//模板下载
function  downloadExcel() {
	if(bsType =="hardware"){
		location.href = "../../excelFile/五金工艺模板.xlsx";
	}else if(bsType =="molding"){
		location.href = "../../excelFile/注塑工艺模板.xlsx";
	}else if(bsType =="surface"){
		location.href = "../../excelFile/表面处理工艺模板.xlsx";
	}else if(bsType =="packag"){
		location.href = "../../excelFile/组装工艺模板.xlsx";
	}
}

//导出数据
function exportExcel() {
	location.href = context + "/productProcess/exportExcel?bsType="+bsType+"&pkQuote="+quoteId;
}

function selectDiv() {
	if(bsType=="hardware"){
		$("#bsCaveDiv").hide();
		$("#bsCapacityDiv").hide();
		$("#bsLossDiv").hide();
		$("#bsFeeWxAllDiv").hide();
	} else if(bsType=="molding"){
		$("#bsCapacityDiv").hide();
		$("#bsFeeWxAllDiv").hide();
		$("#bsLossDiv").hide();
	}else if(bsType=="surface"){
		$("#bsCycleDiv").hide();
		$("#bsCaveDiv").hide();
		$("#bsFeeWxAllDiv").hide();
		$("#bsLossDiv").hide();
	}else if(bsType=="packag"){
		$("#bsCycleDiv").hide();
		$("#bsCaveDiv").hide();
		$("#bsLossDiv").hide();
		$("#bsFeeWxAllDiv").hide();
	}else if(bsType=="out"){
		$("#bsYieldDiv").hide();
		$("#bsCycleDiv").hide();
		$("#bsCaveDiv").hide();
		$("#bsCapacityDiv").hide();
		$("#bsUserNumDiv").hide();
	}
}


// 新增编辑弹出框
function openProdErr(id, title) {

	if (id == null || id == "") {
		$("#id").val("");
		$('#bsName').removeProp("disabled");
		$('#procName').removeProp("disabled");
	}else {
		$('#bsName').prop("disabled","disabled");
		$('#procName').prop("disabled","disabled");
	}
	if(bsType =="surface"){
		console.log("111111111");
		$('#fileDiv1').show();
		$('#fileDiv2').show();
	}
	layui.form.render('');
	selectDiv();
	var index=layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#setProdErr'),
		end : function() {
			cleanProdErr();
		}
	});
	layer.full(index);
}

function openProcessTemp(id, title) {
	if (id == null || id == "") {
		$("#id").val("");
	}
	initSelectTemp();
	var index2=layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#setProcessTemp'),
		end : function() {
			cleanProcessTemp();
		}
	});
	layer.full(index2);
}

// 添加工艺
function addHardware() {
	// 清空弹出框数据
	cleanProdErr();
	// 打开弹出框
	openProdErr(null, "添加工艺信息");
}

// function setData(){
// 	$("#bsModelType").empty();
// 	var data = Jitai
// 	for (var i = 0; i < data.length; i++) {
// 		if (i == 0) {
// 			$("#bsModelType").append("<option value=''>请点击选择</option>");
// 		}
// 		$("#bsModelType").append(
// 			"<option value=" + data[i].subCode + ">" + data[i].subName+"</option>");
// 	}
// }

function Confirm(){
	var params = {
		"id" : quoteId,
		"bsType":bsType,
		"bsCode":bsCode
	};
	layer.confirm('一经提交则不得再修改，确定要提交吗？', {
		btn : [ '确认', '返回' ]
	}, function() {
		saveTable();
		CoreUtil.sendAjax("/productProcess/doStatus", JSON.stringify(params), function(
			data) {
			if (data.result) {
				layer.alert("确认完成成功", function() {
					// layer.closeAll();
					//项目完成，关闭上一级项目标签页
					layer.closeAll();
					// parent.layui.admin.events.closeThisTabs();
					if(data.data =="1") {
						var srcUrl = context + '/quoteProdect/toProductItem?quoteId=' + quoteId + "&style=" + bsType;
						console.log(srcUrl);
						($(window.parent.document).find(('li[lay-id="' + srcUrl + '"]'))).find(".layui-tab-close").trigger("click")
					}
					var thisUrl = "";
					if(bsType!="out") {
						 thisUrl = context + "/productProcess/toProductProcess?bsType=" + bsType + "&quoteId=" + quoteId + "&bsCode=" + bsCode;
					}else{
						thisUrl = context + "/productProcess/toProductProcess?bsType=" + bsType + "&quoteId=" + quoteId;
					}
					($(window.parent.document).find(('li[lay-id="' + thisUrl + '"]'))).find(".layui-tab-close").trigger("click")


				});
			} else {
				layer.alert(data.msg);
			}
		}, "POST", false, function(res) {
			layer.alert(res.msg);
		});
	});
}

function cancelConfirm(){
	var params = {
		"id" : quoteId,
		"bsType":bsType,
		"bsCode":bsCode
	};
	layer.confirm('一经提交则不得再修改，确定要提交吗？', {
		btn : [ '确认', '返回' ]
	}, function() {
		CoreUtil.sendAjax("/productProcess/cancelStatus", JSON.stringify(params), function(
			data) {
			if (data.result) {
				layer.alert(data.msg, function() {
					layer.closeAll();
					// //刷新页面
					// iStatus=2;
					// isComplete();
					// loadAll()
					window.location.reload();
				});
			} else {
				layer.alert(data.msg);
			}
		}, "POST", false, function(res) {
			layer.alert(res.msg);
		});
	});
}

// 添加导入页
function openUpload() {
	tableIns2.reload({
		url:context + '/productProcessTemp/getList?bsType='+bsType+'&quoteId='+quoteId,
		done: function(res1, curr, count){
			pageCurr=curr;
			res1.data.forEach(function (item, index) {
				if(bsType == 'hardware'){//五金
					$('div[lay-id="uploadList"]').find('thead').find('th[data-field="bsCycle"]').removeClass("layui-hide");
					$('div[lay-id="uploadList"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsCycle"]').removeClass("layui-hide");
					$('div[lay-id="uploadList"]').find('thead').find('th[data-field="bsLoss"]').removeClass("layui-hide");
					$('div[lay-id="uploadList"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsLoss"]').removeClass("layui-hide");
				}else if(bsType == 'molding'){//注塑
					$('div[lay-id="uploadList"]').find('thead').find('th[data-field="bsCave"]').removeClass("layui-hide");
					$('div[lay-id="uploadList"]').find('thead').find('th[data-field="bsCycle"]').removeClass("layui-hide");
					$('div[lay-id="uploadList"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsCave"]').removeClass("layui-hide");
					$('div[lay-id="uploadList"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsCycle"]').removeClass("layui-hide");
				}else if(bsType == 'surface'){
					$('div[lay-id="uploadList"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsCapacity"]').removeClass("layui-hide");
					$('div[lay-id="uploadList"]').find('thead').find('th[data-field="bsCapacity"]').removeClass("layui-hide");
				}else if(bsType == 'packag'){
					$('div[lay-id="uploadList"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsCapacity"]').removeClass("layui-hide");
					$('div[lay-id="uploadList"]').find('thead').find('th[data-field="bsCapacity"]').removeClass("layui-hide");
				}else if(bsType == 'out'){
					$('div[lay-id="uploadList"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsUserNum"]').addClass("layui-hide");
					$('div[lay-id="uploadList"]').find('thead').find('th[data-field="bsUserNum"]').addClass("layui-hide");
					$('div[lay-id="uploadList"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsYield"]').addClass("layui-hide");
					$('div[lay-id="uploadList"]').find('thead').find('th[data-field="bsYield"]').addClass("layui-hide");

					$('div[lay-id="uploadList"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsFeeWxAll"]').removeClass("layui-hide");
					$('div[lay-id="uploadList"]').find('thead').find('th[data-field="bsFeeWxAll"]').removeClass("layui-hide");
					$('div[lay-id="uploadList"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsLoss"]').removeClass("layui-hide");
					$('div[lay-id="uploadList"]').find('thead').find('th[data-field="bsLoss"]').removeClass("layui-hide");

				}
			});
		}
	})
	// 打开弹出框
	var index=layer.open({
		type : 1,
		title : "导入工艺信息",
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#uploadDiv')
	});
	layer.full(index);
}

// 新增五金工艺提交
function addSubmit(obj) {
	obj.field.bsType = bsType;
	obj.field.pkQuote = quoteId;
	CoreUtil.sendAjax("/productProcess/add", JSON.stringify(obj.field), function(
		data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanProdErr();
				// 加载页面
				loadAll();
			});
		} else {
			layer.alert(data.msg);
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}
//更新机台类型
function updateModelType(id,modelCode) {
	var param = {
		"id":id,
		"modelCode":modelCode
	}
	CoreUtil.sendAjax("/productProcess/updateModelType", JSON.stringify(param),
		function(data) {
			if (isLogin(data)) {
				if (data.result == true) {
					// 回调弹框
					// 加载load方法
					loadAll();
					/*layer.alert("修改机台类型成功！", function() {
						layer.closeAll();
						// 加载load方法
						loadAll();
					});*/
				} else {
					layer.alert(data.msg, function() {
						layer.closeAll();
					});
				}
			}
		});
}

// 编辑工艺提交
function editSubmit(obj) {
	CoreUtil.sendAjax("/productProcess/edit", JSON.stringify(obj.field), function(
		data) {
		if (data.result) {
			layer.closeAll();
			cleanProdErr();
			// 加载页面
			loadAll();
		} else {
			layer.alert(data.msg);
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}

// 编辑临时表数据提交
function editSubmitTemp(obj) {
	CoreUtil.sendAjax("/productProcessTemp/edit", JSON.stringify(obj.field), function(
		data) {
		if (data.result) {
			layer.alert("操作成功", function(index) {
				layer.close(index);
				// cleanProdErr();
				// 加载页面
				loadAll2();
				// layer.close(layer.index);
			});
		} else {
			layer.alert(data.msg);
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}

// 删除五金工艺
function delProdErr(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除工艺：' + name + '吗？', {
			btn : [ '确认', '返回' ]
			// 按钮
		}, function() {
			CoreUtil.sendAjax("/productProcess/delete", JSON.stringify(param),
				function(data) {
					if (isLogin(data)) {
						if (data.result == true) {
							// 回调弹框
							layer.alert("删除成功！", function() {
								layer.closeAll();
								// 加载load方法
								loadAll();
							});
						} else {
							layer.alert(data, function() {
								layer.closeAll();
							});
						}
					}
				});
		});
	}
}

// 删除临时表
function delProcessTemp(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除临时导入数据：' + name + '吗？', {
			btn : [ '确认', '返回' ]
			// 按钮
		}, function() {
			CoreUtil.sendAjax("/productProcessTemp/delete", JSON.stringify(param),
				function(data) {
					if (isLogin(data)) {
						if (data.result == true) {
							// 回调弹框
							layer.alert("删除成功！", function(index) {
								layer.close(index);
								// 加载load方法
								loadAll2();
							});
						} else {
							layer.alert(data, function(index) {
								layer.close(index);
							});
						}
					}
				});
		});
	}
}

// 重新加载表格（搜索）
function load(obj) {
	// 重新加载table
	tableIns.reload({
		where : {
			keyword : obj.field.keywordSearch
		},
		page : {
			curr : pageCurr
			// 从当前页码开始
		}
	});
}

// 重新加载表格（全部）
function loadAll() {
	// 重新加载table
	var scrollTop=0;
	var scrollLeft=0;
	var layuitable = null;
	var dev_obj = $("#table_and_page_div_id")//定位到表格
	if (dev_obj != null) {//防止未获取到表格对象
		layuitable =dev_obj[0].getElementsByClassName("layui-table-main");//定位到layui-table-main对象
	}
	if (layuitable != null && layuitable.length > 0) {
		scrollTop =layuitable[0].scrollTop; //layuitable获取到的是class=layui-table-main的集合，所以直接获取其中的scrollTop属性。
		scrollLeft=layuitable[0].scrollLeft;
	}
	tableIns.reload({
		page : {
			curr : pageCurr
			// 从当前页码开始
		},done: function (res, curr, count) {
			//滚轮控制

			pageCurr = curr;

			var tableIns = this.elem.next(); // 当前表格渲染之后的视图
			layui.each(res.data, function(i, item){
				if(item.bsStatus=="1"){
					tableIns.find('tr[data-index=' + i + ']').find('td').data('edit',false).css("background-color", "#d2d2d2");
					$("select[name='selectModelType']").attr("disabled","disabled");
					form.render('select');
				}
			});

			//根据不同的类型显示不同的字段
			res.data.forEach(function (item, index) {
				if(bsType == 'out'){//外协
					$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsFeeWxAll"]').removeClass("layui-hide");
					$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsFeeWxAll"]').removeClass("layui-hide");
					$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsLoss"]').removeClass("layui-hide");
					$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsLoss"]').removeClass("layui-hide");
				}else if(bsType == 'hardware'){//五金
					$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsCycle"]').removeClass("layui-hide");
					$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsCycle"]').removeClass("layui-hide");
					$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsUserNum"]').removeClass("layui-hide");
					$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsUserNum"]').removeClass("layui-hide");
					$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsYield"]').removeClass("layui-hide");
					$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsYield"]').removeClass("layui-hide");
				}else if(bsType == 'molding'){//注塑
					$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsCave"]').removeClass("layui-hide");
					$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsCycle"]').removeClass("layui-hide");
					$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsCave"]').removeClass("layui-hide");
					$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsCycle"]').removeClass("layui-hide");
					$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsUserNum"]').removeClass("layui-hide");
					$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsUserNum"]').removeClass("layui-hide");
					$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsYield"]').removeClass("layui-hide");
					$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsYield"]').removeClass("layui-hide");
				}else if(bsType == 'surface'){
					$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsCapacity"]').removeClass("layui-hide");
					$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsCapacity"]').removeClass("layui-hide");
					$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsUserNum"]').removeClass("layui-hide");
					$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsUserNum"]').removeClass("layui-hide");
					$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsYield"]').removeClass("layui-hide");
					$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsYield"]').removeClass("layui-hide");
				}else if(bsType == 'packag'){
					$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsCapacity"]').removeClass("layui-hide");
					$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsCapacity"]').removeClass("layui-hide");
					$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsUserNum"]').removeClass("layui-hide");
					$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsUserNum"]').removeClass("layui-hide");
					$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsYield"]').removeClass("layui-hide");
					$('div[lay-id="listTable"]').find('thead').find('th[data-field="bsYield"]').removeClass("layui-hide");
				}
			});

			dev_obj = $("#table_and_page_div_id")//定位到表格
			if (dev_obj != null) {
				layuitable =dev_obj[0].getElementsByClassName("layui-table-main");
			}
			if (layuitable != null && layuitable.length > 0) {//将属性放回去
				layuitable[0].scrollTop = scrollTop;
				layuitable[0].scrollLeft = scrollLeft;
			}
		}
	});
	tableIns2.reload({
		page : {
			curr : pageCurr
			// 从当前页码开始
		}
	});
}

function loadAll2() {
	// 重新加载table
	tableIns2.reload({
		page : {
			curr : pageCurr
			// 从当前页码开始
		}
	});
}

// 清空新增表单数据
function cleanProdErr() {
	$('#productProcessForm')[0].reset();
	_index = 0;
	document.getElementById("filelist").innerHTML = "";
	layui.form.render();// 必须写
}

// 清空新增表单数据
function cleanProcessTemp() {
	$('#productProcessTempForm')[0].reset();
	layui.form.render();// 必须写
}
