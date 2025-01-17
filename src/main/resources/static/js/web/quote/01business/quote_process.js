/**
 * 产品工艺流程
 */
var pageCurr,localtableFilterIns;
var totalCount = 0;// 表格记录数
var materNameFlag = 0;
$(function() {
	layui.use([ 'form', 'table', 'tableSelect','tableFilter'  ], function() {
		var table = layui.table, table1 = layui.table,form = layui.form, tableSelect = layui.tableSelect,
			tableSelect1 = layui.tableSelect,tableFilter = layui.tableFilter
			,tableSelect3 = layui.tableSelect,tableSelect4 = layui.tableSelect,tableSelect5 = layui.tableSelect;
		isComplete()
		tableIns = table.render({
			elem : '#client_procList',
			url : context + '/quoteProcess/getList?pkQuote='+ quoteId,
			method : 'get', // 默认：get请求
			cellMinWidth : 80,
			// toolbar: '#toolbar',
			height : 'full-65',
			even : true,// 条纹样式
			page : true,
			limit:30,
			limits: [30,50,100,200,300],
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
				{fixed : 'left',type:'checkbox'},
				{fixed : 'left',type : 'numbers'},
			{fixed : 'left',field:'id',title:'ID', width:80, hide:true},
			{fixed : 'left',field : 'bsElement',width : 150,sort:true,title : '组件名称',style : 'background-color:#d2d2d2',
				templet: function (d) {
					if (d.bsElement != null) {
						var parms = d.pkQuote+","+d.id+",'"+d.quote.bsCode+"'"+",'"+d.bsElement+"'";
						return '<div><a  style="text-decoration:underline;color:blue;cursor: pointer;"; onclick="toBomPage('+parms+')">' + d.bsElement + '</a></div>'
					} else {
						return '';
					}
				}},
			{fixed : 'left',field : 'bsName',width : 200,sort:true,title : '零件名称',style : 'background-color:#d2d2d2'},
			{fixed : 'left',field : 'bsLinkName',width : 180,sort:true,title : '所属零件',style : 'background-color:#ffffff',templet :  '#selectLink'},
			{field : 'itemType',title : '物料类型',width : 120,sort:true,style : 'background-color:#d2d2d2'},
			{field : 'workCenter',title : '工作中心',width : 120,sort:true,templet :
				function(d){if(d.bjWorkCenter!=null){
					return d.bjWorkCenter.workcenterName
						}else {
					return "";
				}
					},
				style : 'background-color:#d2d2d2'},
				{field : 'pkProc',title : '工序名称',width : 180,sort:true,templet :  '#selectProc',style : 'background-color:#ffffff'},
				{field : 'bsOrder',title : '顺序',width : 60,"edit" : "number","event" : "dataCol",sort:true,style : 'background-color:#ffffff'},
			// {field : 'bsFeeLh',title : '是否已维护人工制费',width : 140,style : 'background-color:#d2d2d2',align : 'center',
			// 	templet : function(d) {
			// 		if(d.proc.bjWorkCenter.bsCode !="out") {
			// 			if (d.bsFeeLh == null || d.bsFeeLh == '') {
			// 				return "<div class='orange'>否</div>"
			// 			} else {
			// 				return "<div class='green'>是</div>"
			// 			}
			// 		}else {
			// 			return "<div class='green'>一</div>"
			// 		}
			// }},

				// ,templet : '#selectBsMaterName'
				{field : 'bsGroups',width : 130,title : '损耗分组',"edit" : "text",style : 'background-color:#ffffff'},
				{field : 'bsMaterName',width : 220,title : '材料名称',
					templet: function (d){
						if(d.bsMaterName!=null){
							return 	'<div><a  style="text-decoration:underline;color:blue;cursor: pointer;"; onclick="openBomEdit('+d.pkQuoteBom+')">'+d.bsMaterName+'</a></div>'
						}else {
							return '';
						}
					}
						},
				// ,templet : '#selectBsMaterName',,templet : '#selectBsGroups'
			// 	templet : function(d) {
			// 		if (d.fmemo == null) {return ""} else {return d.fmemo}
			// 	}},
			{field : 'bsModel',title : '材料规格',width : 240,style : 'background-color:#d2d2d2;overflow:hidden !important',
					templet : function(d) {
						if (d.quoteBom == null||d.bsMaterName==null) {
							return ""
						}else if(d.quoteBom.bsModel != null) {
							return  "<div>"+d.quoteBom.bsModel+"</div>"
						}else {
							return ""
						}}
			},
			// {field : 'bsjianche',title : '检测项目',"edit" : "text",width : 150,style : 'background-color:#ffffff'},
			{field : 'fmemo',title : '备注',"edit" : "text",width : 200,style : 'background-color:#ffffff'},
			// {fixed : 'right',title : '操作',width : 100,align : 'center',toolbar : '#optBar',style : 'background-color:#ffffff'}
			] ],
			done : function(res, curr, count) {
				// $(".layui-table-body, .layui-table-box, .layui-table-cell").css('overflow', 'visible');
				totalCount = res.count
				localtableFilterIns.reload();
				pageCurr = curr;
				var tableView = this.elem.next(); // 当前表格渲染之后的视图
				//merge(res.data, [ 'bsElement'], [3,3]);
				//merge(res.data, [ 'bsName'], [4,4]);

				res.data.forEach(function (item, index) {
					// console.log(item);
					if(nowStatus ==1){
						tableView.find('tr[data-index=' + index + ']').find('td').data('edit',false).css("background-color", "#d2d2d2")
						$("select[name='selectBsMaterName']").attr("disabled","disabled");
						$("select[name='selectBsGroups']").attr("disabled","disabled");
						$("select[name='selectProc']").attr("disabled","disabled");
						form.render('select');
					}
				});
				form.render();//刷新表单

			}
		});

		// 损耗明细模拟
		tableIns3 = table1.render({
			elem : '#processLoseTable',
			// url : context +
			// '/productProcess/getList?bsType='+bsType+'&quoteId='+quoteId,
			method : 'get', // 默认：get请求
			cellMinWidth : 150,
			// totalRow : true,
			limit:200,
			limits: [200,50,100,500,1000,2000,5000],
			// height : 'full-110',// 固定表头&full-查询框高度
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
				{field : 'BS_ELEMENT',width : 150,title : '组件名称',templet:function (d) {
						if(d.BS_ORDER!=null){
							return d.BS_ELEMENT
						}else {
							return '小计'
						}
					},style:"overflow:hidden !important"},
				{field : 'BS_NAME',width : 180,title : '零件名称',style:"overflow:hidden !important"},
				{field : 'BS_LINK_NAME',width : 150,title : '所属零件',style:"overflow:hidden !important"},
				{field : 'WORKCENTER_NAME',width : 130,title : '工作中心',style:"overflow:hidden !important"},
				{field : 'PROC_NAME',width : 150,title : '工序名称',style:"overflow:hidden !important"},
				{field : 'BS_ORDER',width : 100,title : '顺序'},
				{field : 'BS_GROUPS',title : '损耗分组',width : 150}
			] ],
			done : function(res, curr, count) {
				localtableFilterIns.reload();
				//pageCurr = curr;
			}
		});

		tableSelect = tableSelect.render({
			elem : '#num',
			searchKey : 'keyword',
			checkedKey : 'id',
			searchPlaceholder : '试着搜索',
			table : {
				url : context + '/quoteProcess/getBomList?quoteId='+quoteId,
				method : 'get',
				width:800,
				cols : [ [
					{type : 'numbers', title : '序号'},
					{type : 'radio'},
					{field : 'ID', title : 'id', width : 0, hide : true},
					{field : 'BS_ELEMENT', title : '组件名称', width : 160,style:"overflow:hidden !important"},
					{field : 'BS_COMPONENT', title : '零件名称', width : 200,style:"overflow:hidden !important"},
					{field : 'WORKCENTER_NAME', title : '工作中心', width : 160,style:"overflow:hidden !important"},
					// {field : 'BS_MATER_NAME', title : '材料名称', width : 160},
				] ],
				page : true,
				request : {
					pageName : 'page' // 页码的参数名称，默认：page
					,
					limitName : 'rows' // 每页数据量的参数名，默认：limit
				},
				parseData : function(res) {
					if (res.result) {
						// 可进行数据操作
						return {
							"count" : res.data.total,
							"msg" : res.msg,
							"data" : res.data.rows,
							"code" : res.status
							// code值为200表示成功
						}
					}
				},
			},
			done : function(elem, data) {
				var da = data.data;
				console.log(da[0]);
				form.val("clientProcForm", {
					"num" : da[0].BS_COMPONENT,
					"bsElement":da[0].BS_ELEMENT,
					"wcName":da[0].WORKCENTER_NAME,
					"bsBomId":da[0].ID
				});
				form.render();// 重新渲染

				if(da[0].BS_COMPONENT){
					getAddList(da[0].PK_BJ_WORK_CENTER);
					getListByQuoteAndName(da[0].BS_COMPONENT);
				}else{
					tableProcCheck.reload({
						data:[]
					});
				}
			}
		});


		tableSelect3 = tableSelect3.render({
			elem : '#BjWorkCenter',
			searchKey : 'keyword',
			checkedKey : 'id',
			searchPlaceholder : '内部编码搜索',
			table : {
				url : context + '/basePrice/workCenter/getList',
				method : 'get',
				parseData : function(res) {// 可进行数据操作
					return {
						"count" : res.data.total,
						"msg" : res.msg,
						"data" : res.data.rows,
						"code" : res.status
						// code值为200表示成功
					}
				},
				cols : [ [ {type : 'radio'},// 单选 radio
					{field : 'id',title : 'id',width : 0,hide : true},
					{type : 'numbers'},
					{field : 'workcenterCode',title : '工作中心编号',},
					{field : 'workcenterName',title : '工作中心名称',},
					{field : 'fmemo',title : '备注'} ] ],
				page : true,
				request : {
					pageName : 'page', // 页码的参数名称，默认：page
					limitName : 'rows' // 每页数据量的参数名，默认：limit
				},

			},
			done : function(elem, data) {
				var da = data.data;
				// 选择完后的回调，包含2个返回值
				// elem:返回之前input对象；data:表格返回的选中的数据 []
				form.val("quoteBomForm", {
					"pkBjWorkCenter" : da[0].id,
					"BjWorkCenter" : da[0].workcenterName,
				});
				form.render();// 重新渲染
			}
		});

		tableSelect4 = tableSelect4.render({
			elem : '#ItemTypeWg',
			searchKey : 'keyword',
			checkedKey : 'ID',
			searchPlaceholder : '关键字搜索',
			table : {
				url : context + '/basePrice/itemTypeWg/getList',
				method : 'get',
				parseData : function(res) {// 可进行数据操作
					return {
						"count" : res.data.total,
						"msg" : res.msg,
						"data" : res.data.rows,
						"code" : res.status
						// code值为200表示成功
					}
				},
				cols : [ [ {type : 'radio'},// 单选 radio
					{type : 'numbers'},
					{field : 'id',title : 'id',width : 0,hide : true},
					{field : 'itemType',title : '物料类型',width : 150},
					{field : 'fmemo',title : '备注',width : 150} ] ],
				page : true,
				request : {
					pageName : 'page', // 页码的参数名称，默认：page
					limitName : 'rows' // 每页数据量的参数名，默认：limit
				},
			},
			done : function(elem, data) {// elem:返回之前input对象；data:表格返回的选中的数据
				// []
				var da = data.data;
				form.val("quoteBomForm", {
					"pkItemTypeWg" : da[0].id,
					"ItemTypeWg" : da[0].itemType
				});
				form.render();// 重新渲染
			}
		});

		tableSelect5 = tableSelect5.render({
			elem : '#Unit',
			searchKey : 'keyword',
			checkedKey : 'id',
			searchPlaceholder : '关键字搜索',
			table : {
				url : context + '/basePrice/unit/getList',
				method : 'get',
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
				cols : [ [ {type : 'radio'},// 单选 radio
					{field : 'id',title : 'id',width : 0,hide : true},
					{type : 'numbers'},
					{field : 'unitCode',title : '单位编码'},
					{field : 'unitName',title : '单位名称',} ] ],
				page : true,
				request : {
					pageName : 'page', // 页码的参数名称，默认：page
					limitName : 'rows' // 每页数据量的参数名，默认：limit
				},
			},
			done : function(elem, data) {
				var da = data.data;
				// 选择完后的回调，包含2个返回值
				// elem:返回之前input对象；data:表格返回的选中的数据 []
				form.val("quoteBomForm", {
					"pkUnit" : da[0].id,
					"Unit" : da[0].unitCode
				});
				form.render();// 重新渲染
			}
		});

		//监听机台类型下拉选择 并修改
		form.on('select(selectBsMaterName)', function (data) {
			//获取当前行tr对象
			var elem = data.othis.parents('tr');
			//第一列的值是Guid，取guid来判断
			var Guid= elem.first().find('td').eq(1).text();
			//选择的select对象值；
			var selectValue = data.value;
			updateBsMaterName(Guid,selectValue);
		})

		form.on('select(selectProc)', function (data) {
			//获取当前行tr对象
			var elem = data.othis.parents('tr').attr('data-index');
			//第一列的值是Guid，取guid来判断
			// var Guid= elem.first().find('td').eq(1).text();
			//选择的select对象值；
			// var selectValue = data.value;
			layui.table.cache['client_procList'][elem].pkProc = data.value;
			// updateProc(Guid,selectValue);
		})

		form.on('select(selectLink)', function (data) {
			//获取当前行tr对象
			var elem = data.othis.parents('tr').attr('data-index');
			console.log(data.value);
			//第一列的值是Guid，取guid来判断
			// var Guid= elem.first().find('td').eq(1).text();
			//选择的select对象值；
			// var selectValue = data.value;
			layui.table.cache['client_procList'][elem].bsLinkName = data.value;

			// updateProc(Guid,selectValue);
		})

		form.on('select(selectBsGroups)', function (data) {
			//获取当前行tr对象
			var elem = data.othis.parents('tr');
			//第一列的值是Guid，取guid来判断
			var Guid= elem.first().find('td').eq(1).text();
			//选择的select对象值；
			var selectValue = data.value;
			updateBsGroups(Guid,selectValue);
		})

		// 工作中心列表
		cTableSelect = tableSelect1.render({
			elem : '#wcName',
			searchKey : 'keyword',
			checkedKey : 'id',
			searchPlaceholder : '试着搜索',
			table : {
				// width : 220,
				url : context
					+ '/basePrice/workCenter/getList',
				method : 'get',
				cols : [ [ {
					type : 'radio'
				},// 多选 radio
					, {field : 'id', title : 'ID', width : 0, hide : true},
					{field : 'workcenterCode', title : '工作中心编码',},
					{field : 'workcenterName', title : '工作中心',}
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
				form.val("clientProcForm", {
					"wcName" : da[0].workcenterName,
				});
				getAddList(da[0].id);
				form.render();// 重新渲染
			}
		});


		var tip_index = 0;
		$(document).on('mouseover', '#save-btn', function(data) {
			tip_index = layer.tips("<span style='font-size:13px;line-height:20px;'>如果有未维护基础信息的人工制费费用，请先联系IT部填写好基础信息后再确认完成</span>", ($(this)), {
				tips : [ 3, '5CBA59' ],
				time : 0,
				time : 0,
				area : [ '200px' ]
			});

		}).on('mouseleave', '#save-btn', function() {
			layer.close(tip_index);
		});

		// 监听搜索框
		form.on('submit(searchSubmit)', function(data) {
			// 重新加载table
			load(data);
			return false;
		});

		tableProc = table.render({
			elem : '#procList',
			limit : 50,
			height : 'full-110',
			page:true,
			request : {
				pageName : 'page' // 页码的参数名称，默认：page
				,
				limitName : 'rows' // 每页数据量的参数名，默认：limit
			},
			parseData : function(res) {
				if(!res.result){
					return {
						"count" : 0,
						"msg" : res.msg,
						"data" : [],
						"code" : res.status
					}
				}
				// 可进行数据操作
				return {
					"count" : res.data.total,
					"msg" : res.msg,
					"data" : res.data.rows,
					"code" : res.status
					// code值为200表示成功
				}
			},
			method : 'get',// 默认：get请求
			cols : [ [ {type : 'numbers'}
			, {field : 'checkColumn',type:"checkbox"},
			/*{field : 'procOrder',title : '序号',width:80}, */
			// {field : 'PROC_NO',title : '工序编码', minWidth: 80},
			{field : 'PROC_NAME',title : '工序名称', minWidth: 100}, 
			// {field : 'WORKCENTER_NAME',title : '工作中心', minWidth: 120},
			{field : 'STATUS',title : '是否维护人工制费', minWidth: 140,templet:function (d){
			if(d.STATUS=="0"){
				return "否"
			}else if(d.STATUS=="1"){
				return "是"
			}else {
				return "一"
			}
			}},
			{type: 'toolbar',title: '操作',width: 70,align : 'center',toolbar: '#clickBar'
	        }] ],
			data:[]
		});
		
		tableProcCheck=table.render({
			elem : '#procListCheck',
			limit: 500,
			height : 'full-110',
			method : 'get' ,// 默认：get请求			
			cols : 
				[ [
					// {type : 'numbers'},
				    {field : 'checkColumn',type:"checkbox"},
			        {field : 'bsName',title : '零件名称',style:'background-color:#d2d2d2'},
			        // {field : 'procNo',title : '工序编码',templet:'<div>{{d.proc.procNo}}</div>',style:'background-color:#d2d2d2'},
			        {field : 'procName',title : '工序名称',minWidth:100,templet:function (d){
			        	if(d.proc!=null){
			        		return d.proc.procName
						}else {
			        		return  "";
						}
							// '<div>{{d.proc.procName}}</div>'
						},style:'background-color:#d2d2d2'},
			        {field : 'workCenter',title : '工作中心',minWidth:100,templet:function (d){
							if(d.pkWorkCenter!=null){
								return d.bjWorkCenter.workcenterName
							}else {
								return  "";
							}
							// '<div>{{d.proc.procName}}</div>'
						},style:'background-color:#d2d2d2'},
			        {field : 'bsOrder',title : '工序顺序',"edit":"number","event": "dataCol",minWidth:80,style : 'background-color:#ffffff'},
			        {type: 'toolbar',title: '操作',width: 70,align : 'center',toolbar: '#optBar'}
			] ],
			data:[]
		});	
		
		
		//监听单元格编辑
		//   table.on('edit(client_procTable)', function(obj){
		//     var value = obj.value //得到修改后的值
		//     ,data = obj.data //得到所在行所有键值
		//     ,field = obj.field; //得到字段
		//    // layer.msg('[ID: '+ data.id +'] ' + field + ' 字段更改为：'+ value);
		//     var tr = obj.tr;
	    //     // 单元格编辑之前的值
	    //     var oldtext = $(tr).find("td[data-field='"+obj.field+"'] div").text();
		//     if(field == 'bsOrder'){
		//     	//判断是否为数字
		//     	if(isRealNum(value)){
		//     		doProcOrder(data.id,value);
		//     	}else{
		//     		layer.msg('请填写数字!');
		//     		loadAll();
		//     	}
		//     }
		//     if(field == 'fmemo'){
		//     	//console.log(data.id,value)
		//     	doFmemo(data.id,value)
		//     }
		//   });
		  table.on('edit(procListCheck)', function(obj){
			    var value = obj.value //得到修改后的值
			    ,data = obj.data //得到所在行所有键值
			    ,field = obj.field; //得到字段
			   // layer.msg('[ID: '+ data.id +'] ' + field + ' 字段更改为：'+ value);
			    var tr = obj.tr;
		        // 单元格编辑之前的值
		        var oldtext = $(tr).find("td[data-field='"+obj.field+"'] div").text();
			    if(field == 'bsOrder'){
			    	//判断是否为数字
			    	if(isRealNum(value)){
			    		doProcOrder(data.id,value);
			    	}else{
			    		layer.msg('请填写数字!');
			    		loadAll();
			    	}
			    }
			  });


		// 监听单元格编辑
		// table.on('edit(client_procTable)', function(obj) {
		// 	var value = obj.value // 得到修改后的值
		// 	, data = obj.data // 得到所在行所有键值
		// 	, field = obj.field; // 得到字段
		// 	// layer.msg('[ID: '+ data.id +'] ' + field + ' 字段更改为：'+ value);
		// 	var tr = obj.tr;
		// 	// 单元格编辑之前的值
		// 	var oldtext = $(tr).find("td[data-field='" + obj.field + "'] div").text();
		// 	if (field == 'bsOrder') {
		// 		// 判断是否为数字
		// 		if (isRealNum(value)) {
		// 			doProcOrder(data.id, value);
		// 		} else {
		// 			layer.msg('请填写数字!');
		// 			loadAll();
		// 		}
		// 	}
		// 	if (field == 'fmemo') {
		// 		// console.log(data.id,value)
		// 		doFmemo(data.id, value)
		// 	}
		// });

		localtableFilterIns = tableFilter.render({
			'elem' : '#client_procList',
			'mode' : 'local',//服务端过滤
			'filters' : [
				{field: 'bsElement', type:'checkbox'},
				{field: 'bsName', type:'checkbox'},
				{field: 'itemType', type:'checkbox'},
				{field: 'workCenter', type:'checkbox'},
				{field: 'bsGroups', type:'checkbox'},
				{field: 'bsMaterName', type:'input'},
			],
			'done': function(filters){
			}
		})

		// 监听工具条
		table.on('tool(client_procTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delClientProc(data.id,'list',"");
			} else if (obj.event === 'edit') {
				// 编辑
				// getClientProc(data, data.id);//未写
				// addProc(data.custId)
			}
		});
		table.on('tool(procListCheck)', function(obj) {
			var data = obj.data;
			var tbData = table.cache.procListCheck; //是一个Array
			if (obj.event === 'del') {
				// 删除
				delClientProc(data.id,'in',data.bsName);
			} 
			if (obj.event === 'moveUp') {
				// 上移
				var tr = $(this).parent().parent().parent();
				if ($(tr).prev().html() == null) {
					layer.msg("已经是最顶部了");
					return;
				} else {
					// 未上移前，记录本行和下一行的数据
					var tem = tbData[tr.index()];
					var tem2 = tbData[tr.prev().index()];

					// 将本身插入到目标tr之前
					$(tr).insertBefore($(tr).prev());
					// 上移之后，数据交换
					tbData[tr.index()] = tem;
					tbData[tr.next().index()] = tem2;
				}

			} else if (obj.event === 'moveDown') {
				// 下移
				var tr = $(this).parent().parent().parent();
				if ($(tr).next().html() == null) {
					layer.msg("已经是最底部了");
					return;
				} else {
					// 记录本行和下一行的数据
					var tem = tbData[tr.index()];
					var tem2 = tbData[tr.next().index()];
					// 将本身插入到目标tr的后面
					$(tr).insertAfter($(tr).next());
					// 交换数据
					tbData[tr.index()] = tem;
					tbData[tr.prev().index()] = tem2;
				}
			}
		});

		table.on('tool(procTable)', function(obj) {
			 var checkValue=$("#num").val();
			 var bsElement=$("#bsElement").val();
			var bsBomId=$("#bsBomId").val();
			 if(checkValue){
				 var data = obj.data;
					var tbData = table.cache.procList; //是一个Array
					if (obj.event == 'doClick') {
						addSubmit(data.ID,checkValue,bsElement,bsBomId);
					}
			 }else{
				 layer.msg('请先选择零件', {
		              time: 20000, //20s后自动关闭
		              btn: ['知道了']
		            });
			 }
			
			/*if (obj.event === 'moveUp') {
				// 上移
				var tr = $(this).parent().parent().parent();
				if ($(tr).prev().html() == null) {
			        layer.msg("已经是最顶部了");
			        return;
			    }else{
			        // 未上移前，记录本行和下一行的数据
			        var tem = tbData[tr.index()];
			        var tem2 = tbData[tr.prev().index()];
			 
			        // 将本身插入到目标tr之前
			        $(tr).insertBefore($(tr).prev());
			        // 上移之后，数据交换
			        tbData[tr.index()] = tem;
			        tbData[tr.next().index()] = tem2;
			    }

			} else if (obj.event === 'moveDown') {
				// 下移
				var tr = $(this).parent().parent().parent();
			    if ($(tr).next().html() == null) {
			        layer.msg("已经是最底部了");
			        return;
			    } else{
			        // 记录本行和下一行的数据
			        var tem = tbData[tr.index()];
			        var tem2 = tbData[tr.next().index()];
			        // 将本身插入到目标tr的后面
			        $(tr).insertAfter($(tr).next());
			        // 交换数据
			        tbData[tr.index()] = tem;
			        tbData[tr.prev().index()] = tem2;
			    }

			}*/
		});

		form.on('submit(dels)', function() {
			var checkdata = layui.table.checkStatus("procListCheck").data;
			var ids = "";
			var bsName = "";
			for(var i = 0;i<checkdata.length;i++){
				ids = ids+ checkdata[i].id+',';
			}
			if(ids ==""){
				return false;
			}else {
				bsName = checkdata[0].bsName;
			}
			delClientProc(ids,"in",bsName);
			return false;
		});

		// 监听提交
		form.on('submit(addSubmit)', function(data) {
			var bsElement=$("#bsElement").val();
			var bsBomId=$("#bsBomId").val();
			var checkStatus = table.cache.procList;
			var procIdList = "";
			$('#clientProcForm tbody tr td[data-field="checkColumn"] input[type="checkbox"]').each(function(i) {
				if ($(this).is(":checked")) {
					// fyx-202011-02
					var checks = $('tbody tr[data-index="' + i + '"] td[data-field="jobAttr"] input[type="checkbox"]:checked');
					procIdList += checkStatus[i].ID + ",";
				}
			});
			if (data.field.num == "") {
				layer.msg('请先选择零件', {
		              time: 20000, //20s后自动关闭
		              btn: ['知道了']
		            });
				return false;
			}
			console.log(data.field)
			// addSubmit(procIdList,data.field.itemId);

			addSubmit(procIdList, data.field.num,bsElement,bsBomId);

			//提交后清空勾选
			$('#clientProcForm tbody tr td[data-field="checkColumn"] input[type="checkbox"]').prop('checked',false);
			//取消全选框的勾选
			$("input[type='checkbox'][name='layTableCheckbox']").prop('checked', false);
			form.render('checkbox');
			return false;

		});

		//零件切换
		// form.on('select(num)', function(data){
		// 	if(data.value){
		// 		getListByQuoteAndName(data.value);
		// 	}else{
		// 		tableProcCheck.reload({
		// 			data:[]
		// 		});
		// 	}
		// });


		// 设置工序顺序
		function doProcOrder(id, procOrder) {
			var param = {
				"id" : id,
				"procOrder" : procOrder
			};
			CoreUtil.sendAjax("/quoteProcess/doProcOrder", JSON.stringify(param), function(data) {
				if(data.result ==false){
					layer.alert(data.msg, function(index) {
						layer.close(index);
					});
				}else {
					loadAll();
				}
			}, "POST", false, function(res) {
				layer.alert("操作请求错误，请您稍后再试", function() {
					layer.closeAll();
				});
			});
		}

		// 监听提交
		form.on('submit(addBomSubmit)', function(data) {
			editBomSubmit(data);
			return false;
		});

		// 填写备注
		function doFmemo(id, fmemo) {
			var param = {
				"id" : id,
				"fmemo" : fmemo
			};
			CoreUtil.sendAjax("/quoteProcess/doFmemo", JSON.stringify(param), function(data) {
				if(data.result ==false){
					layer.alert(data.msg, function() {
						layer.closeAll();
						loadAll();
					});
				}else {
					loadAll();
				}
			}, "POST", false, function(res) {
				layer.alert("操作请求错误，请您稍后再试", function() {
					layer.closeAll();
				});
			});
		}
	});
});

// 编辑外购件清单信息提交
function editBomSubmit(obj) {
	CoreUtil.sendAjax("/quoteBom/edit", JSON.stringify(obj.field), function(data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				// cleanProdErr();
				layui.form.render();// 必须写
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

function toBomPage(pkQuote,id,bsCode,bsElement){
	console.log(bsCode);
	var titleInfo = "("+bsCode.substring(bsCode.length-4)+")";
	var link="/quoteBom/toQuoteBom?bsElement="+bsElement+"&quoteId="+pkQuote+"&code="+"A001";
	parent.layui.index.openTabsPage(link,bsElement+titleInfo);
}

// 编辑Bom的弹出框
function openBomEdit(id, title) {
	var params = {
		"id" : id,
	};
	CoreUtil.sendAjax("/quoteBom/getDetail", JSON.stringify(params), function(data) {
		if (data.result) {
			var obj = data.data;
			layui.form.val("quoteBomForm", {
				"id" : obj.id,
				"bsComponent" : obj.bsComponent,
				"bsElement" : obj.bsElement,
				"pkBjWorkCenter" : obj.pkBjWorkCenter,
				"BjWorkCenter" : obj.pkBjWorkCenter==null?"":obj.wc.workcenterName,
				"pkItemTypeWg" : obj.pkItemTypeWg,
				"ItemTypeWg" : obj.pkItemTypeWg==null?"":obj.itp.itemType,
				"bsMaterName" : obj.bsMaterName,
				"bsModel" : obj.bsModel,
				"bsExplain" : obj.bsExplain,
				"bsGroups":obj.bsGroups,
				"fmemo" : obj.fmemo,
				"bsProQty" : obj.bsProQty,
				"pkUnit" : obj.pkUnit,
				"Unit" : obj.pkUnit==null?"":obj.pkUnit.unitName,
				"purchaseUnit": obj.purchaseUnit,
				"bsAgent":obj.bsAgent,
				"bsQty":obj.bsQty,
			});
			layui.form.render();// 必须写
		} else {
			layer.alert(data.msg);
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});

	var index = layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#setQuoteBom'),
		end : function() {
			$('#quoteBomForm')[0].reset();
			layui.form.render();// 必须写
		}
	});
	layer.full(index);
}

//更新材料名称
function updateBsMaterName(id,bomId) {
	var param = {
		"id":id,
		"bomId":bomId
	}
	CoreUtil.sendAjax("/quoteProcess/doBsMaterName", JSON.stringify(param),
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

// 导出数据
function exportExcel() {
	location.href = context + "/quoteProcess/exportExcel?pkQuote=" + quoteId;
}


function saveTable(refresh) {
	var dates = layui.table.cache['client_procList'];
	// var tableList = new Array();
	// console.log(dates);
	// for(var i=0;i<dates.length;i++){
	// 	tableList.push({"id":dates[i].id,
	// 		"pkQuoteBom": dates[i].pkQuoteBom})
	// }
	// let tableList = dates.map(items=>{
	// 	return {
	// 		id: items.id,
	// 		pkProc: items.pkProc,
	// 		bsOrder:items.bsOrder
	// 	}
	// })
	// console.log(tableList);
	CoreUtil.sendAjax("/quoteProcess/saveTable", JSON.stringify(dates),
		function(data) {
			if (isLogin(data)) {
				if (data.result == true) {
					if(data.data!=null){
						layer.confirm('损耗分组:'+ data.data +'不在外购清单中，这会导致损耗明细中的材料成本归集不到，是否继续。', {
							btn : [ '继续', '返回' ]
							// 按钮
						}, function() {
							CoreUtil.sendAjax("/quoteProcess/saveTableAgain", JSON.stringify(dates), function(data) {
								if (isLogin(data)) {
									if (data.result == true) {
										// 回调弹框
										if(refresh){
											layer.closeAll();
											loadAll();
										}
										materNameFlag = 1;
									} else {
										layer.alert(data, function() {
											layer.closeAll();
										});
									}
								}
							});
						});
					}else {
						if(refresh){
							loadAll();
						}
						materNameFlag = 1;
					}
				} else {
					layer.alert(data.msg, function() {
						layer.closeAll();
						// return false;
						// loadAll();
						materNameFlag = 0;
					});
				}
			}
		});
}

function delTable(){
	// var dates = layui.table.cache['client_procList'];
	var data = layui.table.checkStatus('client_procList').data;
	if(data.length == 0){
		layer.msg("请先勾选数据!");
	}else{
		var id="";
		for(var i = 0; i < data.length; i++) {
			id += data[i].id+",";
		}
		// delLine(id);
		delClientProc(id,"list",null);
	}
}

function updateProc(id,prodId) {
	var param = {
		"id":id,
		"prodId":prodId
	}
	CoreUtil.sendAjax("/quoteProcess/doProc", JSON.stringify(param),
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

function updateBsGroups(id,bsGroups) {
	var param = {
		"id":id,
		"bsGroups":bsGroups
	}
	CoreUtil.sendAjax("/quoteProcess/doBsGroups", JSON.stringify(param),
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

function isComplete(){
		console.log(nowStatus);
	if (nowStatus == 1) {
		$("#addbtn").addClass("layui-btn-disabled").attr("disabled", true)
		$("#savebtn").addClass("layui-btn-disabled").attr("disabled", true)
		$("#editListBtn").addClass("layui-btn-disabled").attr("disabled", true)
		$("#delListBtn").addClass("layui-btn-disabled").attr("disabled", true)
	}
}

// 添加工艺流程
function addProc() {

	if (bomNameList.data < 1) {
		layer.alert("该报价单未维护外购件清单数据")
		return false;
	}
	// 获取初始化信息
	// getAddList("");
	tableProcCheck.reload({
		data:[]
	});
	// 打开弹出框
	openProc(null, "添加工艺流程");
}
function saveProc() {
	if (totalCount == 0) {
		layer.alert("当前模块无数据，“确认提交”不可用")
		return false;
	}
	var dates = layui.table.cache['client_procList'];
	var param = {
		"quoteId" : quoteId,
		"code" : code,
		"dates":dates
	};
	layer.confirm('一经提交则不得再修改，确定要提交吗？', {
		btn : [ '确认', '返回' ]
	}, function() {
		CoreUtil.sendAjax("/quoteProcess/doStatus", JSON.stringify(param), function(data) {
			if (data.result == true) {
				layer.alert("确认完成成功", function() {
					// layer.closeAll();
					if(data.data =="1"){
						//项目完成，关闭上一级项目标签页
						// var thisUrl= context +"/productMater/toProductMater?bsType="+bsType+"&quoteId="+quoteId+"&bsCode="+bsCode;
						var srcUrl = context + '/quote/toQuoteItem?quoteId='+quoteId+"&style=item";
						console.log(srcUrl);
						($(window.parent.document).find(('li[lay-id="'+srcUrl+'"]'))).find(".layui-tab-close").trigger("click")
					}
					var thisUrl= context +"/quoteProcess/toQuoteProcess?quoteId="+quoteId+"&code="+code;
					($(window.parent.document).find(('li[lay-id="'+thisUrl+'"]'))).find(".layui-tab-close").trigger("click")
				});
			} else {
				layer.alert(data.msg, function(index){
					layer.close(index);
					// time : 2000, // 2s后自动关闭
					// btn : [ '知道了' ]
				});
			}
		});
	});
}

//取消完成
function cancelStatus() {
	var param = {
		"quoteId" : quoteId,
		"code" : code
	};
	layer.confirm('确认取消确认完成吗？', {
		btn : [ '确认', '返回' ]
	}, function() {
		CoreUtil.sendAjax("/quoteProcess/cancelStatus", JSON.stringify(param), function(data) {
			if (isLogin(data)) {
				if (data.result == true) {
					layer.alert(data.msg, function() {
						layer.closeAll();
						window.location.reload();
					});
				} else {
					layer.msg(data.msg, {
						time : 2000, // 2s后自动关闭
						btn : [ '知道了' ]
					});
				}
			}
		});
	});
}

function delClientProc(id,type,bsName) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/quoteProcess/delete", JSON.stringify(param), function(data) {
				if (data.result == true) {
					if(type == 'list'){
						// 回调弹框
						layer.alert("删除成功！", function() {
							layer.closeAll();
							// 加载load方法
							loadAll();
						});
					}else{
						// 回调弹框
						layer.alert("删除成功！", function(index) {
							layer.close(index);
							getListByQuoteAndName(bsName);
						});
					}
					
				} else {
					layer.alert(data, function() {
						layer.closeAll();
					});
				}
			});
		});
	}
}

// 新增工艺流程提交
function addSubmit(procIdlist, itemIds,bsElement,bomId) {
	var params = {
		"proc" : procIdlist,
		"itemId" : itemIds,
		"quoteId" : quoteId,
		"bsElement":bsElement,
		"bsBomId":bomId
	};

	CoreUtil.sendAjax("/quoteProcess/add", JSON.stringify(params), function(data) {

		if (data.result) {
			getListByQuoteAndName(itemIds);
			/*layer.alert("操作成功", function() {
				layer.closeAll();
				cleanProc();
				// 加载页面
				loadAll();
			});*/
		} else {
			layer.alert(data.msg);
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}

function getListByQuoteAndName(name){
	CoreUtil.sendAjax("/quoteProcess/getListByQuoteAndName", {'quoteId':quoteId,'name':name}, function(data) {
		tableProcCheck.reload({
			data:data.data,
			done : function(res, curr, count) {
				//提交后清空勾选
				$('#clientProcForm tbody tr td[data-field="checkColumn"] input[type="checkbox"]').prop('checked',false);
				//取消全选框的勾选
				$("input[type='checkbox'][name='layTableCheckbox']").prop('checked', false);
				layui.form.render('checkbox');
			}
		});
	}, "GET", false, function(res) {
		layer.alert("操作请求错误，请您稍后再试", function() {
			layer.closeAll();
		});
	});
}

// 获取工序列表
function getAddList(val) {
	// console.log(bomNameList)
	tableProc.reload({
		// data : data.data,
		url:context + "/quoteProcess/getAddList",
		where : {
			"pkWcId" :val
		},
		page : {
			curr : 0
			// 从当前页码开始
		},
		done : function(res, curr, count) {
			// cleanProc();// 清空之前的选中
		}
	});

}

//损耗明细模拟
function openDetail() {
	tableIns3.reload({
		url : context + '/quoteProcess/getSumList?quoteId=' + quoteId,
		done : function(res, curr, count) {
			var tableIns3 = this.elem.next();
			res.data.forEach(function(item, index) {
				if(item.BS_ORDER==null){
					tableIns3.find('tr[data-index=' + index + ']').find('td').css("background-color", "#FFFF00");
				}
			});
		}
	})
	var index = layer.open({
		type : 1,
		title : "损耗明细模拟",
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#processLossDiv')
	});
	layer.full(index);
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
// 新增编辑弹出框
function openProc(id, title) {
	if (id == null || id == "") {
		$("#id").val("");
	}
	var index = layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		// area : [ '800px','410px'],
		content : $('#setClientProc'),
		end : function() {
			cleanProc();
			loadAll();
		}
	});
	layer.full(index);
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

function loadSelected(){
	tableProcCheck.reload({
		page : {
			curr : pageCurr
			// 从当前页码开始
		}
	});
}

// 重新加载表格（全部）
function loadAll() {
	var scrollTop;
	var scrollLeft;
	var layuitable = null;
	var dev_obj = $("#table_and_page_div_id")//定位到表格
	if (dev_obj != null) {//防止未获取到表格对象
		layuitable =dev_obj[0].getElementsByClassName("layui-table-main");//定位到layui-table-main对象
	}
	if (layuitable != null && layuitable.length > 0) {
		scrollTop =layuitable[0].scrollTop; //layuitable获取到的是class=layui-table-main的集合，所以直接获取其中的scrollTop属性。
		scrollLeft=layuitable[0].scrollLeft;
	}
	// 重新加载table
	tableIns.reload({
		page : {
			curr : pageCurr
		// 从当前页码开始
		}
		,done: function (res, curr, count) {
			//滚轮控制
			totalCount = res.count;
			pageCurr=curr;
			dev_obj = $("#table_and_page_div_id")//定位到表格
			if (dev_obj != null) {
				layuitable =dev_obj[0].getElementsByClassName("layui-table-main");
			}
			if (layuitable != null && layuitable.length > 0) {//将属性放回去
				layuitable[0].scrollTop = scrollTop;
				layuitable[0].scrollLeft = scrollLeft;
			}

			var tableView = this.elem.next(); // 当前表格渲染之后的视图
			//erge(res.data, [ 'bsElement'], [3,3]);
			//merge(res.data, [ 'bsName'], [4,4]);
			// $(".layui-table-body, .layui-table-box, .layui-table-cell").css('overflow', 'visible');

			res.data.forEach(function (item, index) {
				// console.log(item);
				if(nowStatus ==1){
					tableView.find('tr[data-index=' + index + ']').find('td').data('edit',false).css("background-color", "#d2d2d2")
					$("select[name='selectBsMaterName']").attr("disabled","disabled");
					$("select[name='selectBsGroups']").attr("disabled","disabled");
					$("select[name='selectProc']").attr("disabled","disabled");
					layui.form.render('select');
				}
			});
			layui.form.render();//刷新表单

		}
	});
}
// 清空新增表单数据
function cleanProc() {
	$('#clientProcForm')[0].reset();
	layui.form.render();// 必须写
}