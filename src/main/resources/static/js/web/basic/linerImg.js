/**
 * 组长铁三角管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table','laydate','tableFilter','tableSelect' ], function() {
		var table = layui.table, form = layui.form,tableFilter = layui.tableFilter,tableSelect = layui.tableSelect
		tableSelect1 = layui.tableSelect,tableSelect2 = layui.tableSelect, laydate = layui.laydate;

		tableIns = table.render({
			elem : '#listTable',
			url : context + '/base/linerImg/getList',
			method : 'get' // 默认：get请求
			, toolbar: '#toolbar' //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
			,cellMinWidth : 80
			,height:'full-80'//固定表头&full-查询框高度
		    ,even:true//条纹样式
			,page : true,
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
					"count" : res.data.Total,
					"msg" : res.msg,
					"data" : res.data.Rows,
					"code" : res.status
				// code值为200表示成功
				}
			},
			cols : [ [ {
				type : 'numbers'
			},
			{type:'checkbox'}
			// ,{field:'ID', title:'ID', width:80,  hidde unresize:true, sort:true}
			,
			{
				field : 'NAME_PE',
				title : '工程员名称', sort: true,
			},
				{
					field : 'IMG_FLAG_PE',
					title : '工程员照片', sort: true,
					templet: function (d) {
						if(d.IMG_FLAG_PE==0){
							return '无'
						}else {
							return '有'
						}
					}
				},

				{
					field : 'NAME_QC',
					title : 'QC名称', sort: true,
				},
				{
					field : 'IMG_FLAG_QC',
					title : 'QC照片', sort: true,
					templet: function (d) {
						if(d.IMG_FLAG_QC==0){
							return '无'
						}else {
							return '有'
						}
					}
				},
				{
					field : 'LINE_NAME',
					title : '生产线', sort: true
				},
				{
					field : 'NAME_LINER',
					title : '组长姓名', sort: true
				},
				{
					field : 'IMG_FLAG_LINER',
					title : '组长照片', sort: true,
					templet: function (d) {
						if(d.IMG_FLAG_LINER==0){
							return '无'
						}else {
							return '有'
						}
					}
				}
			, {
				field : 'ENABLED',
				title : '状态',
				width : 95,
				templet : '#statusTpl', sort: true
			}, {
				field : 'LASTUPDATE_DATE',width : 145,
				title : '更新时间', sort: true
			}, {
				field : 'CREATE_DATE',width : 145,
				title : '添加时间', sort: true
			}, {
				fixed : 'right',
				title : '操作',
					width : 120,
				align : 'center',
				toolbar : '#optBar'
			} ] ],
			done : function(res, curr, count) {
				localtableFilterIns.reload();
				pageCurr = curr;
			}
		});
		
		var localtableFilterIns = tableFilter.render({
			'elem' : '#listTable',
			//'parent' : '#doc-content',
			'mode' : 'api',//服务端过滤
			'filters' : [
				/*{field: 'lineNo', type:'checkbox'},*/
				{field: 'lineNo', type:'input'},
				{field: 'lastupdateDate', type:'date'},
				{field: 'checkStatus', type:'radio'},
				{field: 'linerName', type:'input'},
				{field: 'createDate', type:'date'},
				{field: 'linerCode', type:'input'},
				{field: 'lineName', type:'input'},
				/*{field: 'id', type:'input'},
				{field: 'date', type:'date'},
				{field: 'username', type:'checkbox', url:'json/filter.json'},
				{field: 'sex', type:'radio'},
				{field: 'class', type:'checkbox', data:[{ "key":"12", "value":"十二班"}]}*/
			],
			'done': function(filters){}
		})

		//组长
		tableSelect=tableSelect.render({
			elem : '#orgName',
			searchKey : 'keyword',
			checkedKey : 'id',
			searchPlaceholder : '试着搜索',
			table : {
				url:  context +'/base/linerImg/getDeptInfo',
				method : 'get',
				cols : [ [
					{ type: 'radio' },
					, {
						field : 'ID',
						title : 'id',
						width : 0,hide:true
					}, {
						field : 'LEAD_BY',
						title : '名称',
						width : 150
					}, {
						field : 'EMP_IMG',
						title : '照片',
						width : 150,
						templet:function (d){
							if(d.EMP_IMG==null){
								return "无"
							}else{
								return "有"
							}
						}
					}
					] ],
				// page : true,
				// request : {
				// 	pageName : 'page', // 页码的参数名称，默认：page
				// 	limitName : 'rows' // 每页数据量的参数名，默认：limit
				// },
				parseData : function(res) {
					if(res.result){
						// 可进行数据操作
						return {
							"count" : res.total,
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
				//console.log(data);
				var da=data.data[0];
				form.val("lineForm", {
					"orgName":da.LEAD_BY,
					"orgIdLiner":da.ORG_ID,
					"empIdLiner" : da.EMP_ID,
				});
				form.render();// 重新渲染
			}
		});

		//QC
		tableSelect1=tableSelect1.render({
			elem : '#empIdQcName',
			searchKey : 'keyword',
			checkedKey : 'id',
			searchPlaceholder : '试着搜索',
			table : {
				url:  context +'/base/linerImg/getEmpCode',
				method : 'get',
				cols : [ [
					{ type: 'radio' },
					, {
						field : 'ID',
						title : 'id',
						width : 0,hide:true
					}, {
						field : 'EMP_NAME',
						title : '员工姓名',
						width : 150
					}, {
						field : 'DEPT_NAME',
						title : '部门名称',
						width : 150
					},
					{
						field : 'EMP_CODE',
						title : '员工编号',
						width : 150
					},
					{
						field : 'EMP_IMG',
						title : '照片情况',
						width : 150,
						templet:function (d){
							if(d.EMP_IMG==null){
								return "无"
							}else{
								return "有"
							}
						}
					},
				] ],
				page : true,
				request : {
					pageName : 'page', // 页码的参数名称，默认：page
					limitName : 'rows' // 每页数据量的参数名，默认：limit
				},
				parseData : function(res) {
					if(res.result){
						// 可进行数据操作
						return {
							"count" : res.data.Total,
							"msg" : res.msg,
							"data" : res.data.Rows,
							"code" : res.status
							// code值为200表示成功
						}
					}

				},
			},
			done : function(elem, data) {
				//选择完后的回调，包含2个返回值 elem:返回之前input对象；data:表格返回的选中的数据 []
				console.log(data.data[0]);
				form.val("lineForm", {
					"empIdQcName":data.data[0].EMP_NAME,
					"EMP_IMG":data.data[0].EMP_IMG,
					"empIdQc" : data.data[0].ID
				});
				form.render();// 重新渲染
			}
		});

		getLineSelect('');



		//工程
		tableSelect2=tableSelect2.render({
			elem : '#empIdPeName',
			searchKey : 'keyword',
			checkedKey : 'id',
			searchPlaceholder : '试着搜索',
			table : {
				url:  context +'/base/linerImg/getEmpCode',
				method : 'get',
				cols : [ [
					{ type: 'radio' },
					, {
						field : 'ID',
						title : 'id',
						width : 0,hide:true
					}, {
						field : 'EMP_NAME',
						title : '员工姓名',
						width : 150
					}, {
						field : 'DEPT_NAME',
						title : '部门名称',
						width : 150
					},
					{
						field : 'EMP_CODE',
						title : '员工编号',
						width : 150
					},
					{
						field : 'EMP_IMG',
						title : '照片情况',
						width : 150,
						templet:function (d){
							if(d.EMP_IMG==null){
								return "无"
							}else{
								return "有"
							}
						}
					},
				] ],
				// page : true,
				// request : {
				// 	pageName : 'page', // 页码的参数名称，默认：page
				// 	limitName : 'rows' // 每页数据量的参数名，默认：limit
				// },
				parseData : function(res) {
					if(res.result){
						// 可进行数据操作
						return {
							"count" : res.data.Total,
							"msg" : res.msg,
							"data" : res.data.Rows,
							"code" : res.status
							// code值为200表示成功
						}
					}

				},
			},
			done : function(elem, data) {
				//选择完后的回调，包含2个返回值 elem:返回之前input对象；data:表格返回的选中的数据 []
				console.log(data.data[0]);
				form.val("lineForm", {
					"empIdPeName":data.data[0].EMP_NAME,
					"EMP_IMG":data.data[0].EMP_IMG,
					"empIdPe" : data.data[0].ID
				});
				form.render();// 重新渲染
			}
		});

		//头工具栏事件
		table.on('toolbar(listTable)', function(obj){
		    var checkStatus = table.checkStatus(obj.config.id);
		    switch(obj.event){
		      case 'doAdd':
		    	  addLine();
		      break;
		      case 'doDelete':
		        var data = checkStatus.data;
		        console.log(data)
		        if(data.length == 0){
		        	layer.msg("请先勾选数据!");
		        }else{
		        	var id="";
		        	for(var i = 0; i < data.length; i++) {
		        		id += data[i].ID+",";
		        		console.log(data[i])
		        	}
		        	delLine(id);
		        }
		        
		      break;
		    };
		  });

		// 监听状态操作
		form.on('switch(isStatusTpl)', function(obj) {
			setStatus(obj, this.value, this.name, obj.elem.checked);
		});

		//日期范围
		laydate.render({
			elem: '#dates',
			trigger:'click'
			,range: true
		});

		// 监听工具条
		table.on('tool(listTable)', function(obj) {
			var data = obj.data;
			console.log(data);
			if (obj.event === 'del') {
				// 删除
				layer.confirm('您确定要删除这条记录吗？', {
					btn : [ '确认', '返回' ]
				// 按钮
				}, function() {
					delLine(data.ID);
				});
			} else if (obj.event === 'edit') {
				// 编辑
				getLine(data, data.ID);
			}
		});
		// 监听提交
		form.on('submit(addSubmit)', function(data) {
			console.log(data);
			if (data.field.id == null || data.field.id == "") {
				// 新增
				addSubmit(data);
			} else {
				editSubmit(data);
			}
			return false;
		});
		// 监听搜索框
		form.on('submit(searchSubmit)', function(data) {
			// 重新加载table
			load(data);
			return false;
		});
		// 编辑组长铁三角
		function getLine(obj, id) {
			console.log(obj);
			form.val("lineForm", {
				"id" : obj.ID,
				"orgIdLiner" : obj.ORG_ID_LINER,
				"empIdLiner" : obj.EMP_ID_LINER,
				"orgName" : obj.NAME_LINER,
				"empIdQcName" : obj.NAME_QC,
				"empIdQc" : obj.EMP_ID_QC,
				"imgQc" : obj.IMG_QC,
				"empIdPeName" : obj.NAME_PE,
				"empIdPe" : obj.EMP_ID_PE,
				"imgPe" : obj.IMG_PE,
				"lineId" : obj.LINE_ID,
			});
			openLine(id, "编辑组长铁三角")

		}

		// 设置正常/禁用
		function setStatus(obj, id, name, checked) {
			var isStatus = checked ? 1 : 0;
			var deaprtisStatus = checked ? "有效":"无效";
			// 正常/禁用
			layer.confirm('您确定要把组长铁三角：' + name + '设置为' + deaprtisStatus + '状态吗？',
					{
						btn1 : function(index) {
							var param = {
								"id" : id,
								"checkStatus" : isStatus
							};
							CoreUtil.sendAjax("/base/linerImg/doStatus", JSON
									.stringify(param), function(data) {
								if (data.result) {
									layer.alert("操作成功", function() {
										layer.closeAll();
										loadAll();
									});
								} else {
									layer.alert(data.msg, function() {
										layer.closeAll();
									});
								}
							}, "POST", false, function(res) {
								layer.alert("操作请求错误，请您稍后再试", function() {

									layer.closeAll();
								});
							});
						},
						btn2 : function() {
							obj.elem.checked = !isStatus;
							form.render();
							layer.closeAll();
						},
						cancel : function() {
							obj.elem.checked = !isStatus;
							form.render();
							layer.closeAll();
						}
					});
		}

	});

});

function getLineSelect(name) {
	CoreUtil.sendAjax("/base/linerImg/getLine", {}, function(data) {
		if (data.result) {
				$("#lineId").empty();
				var pline = data.data;
				console.log(data);
				for (var i = 0; i < pline.length; i++) {
					if (i == 0) {
						$("#lineId").append("<option value=''>请选择生产线</option>");
					}
					$("#lineId").append(
						"<option value=" + pline[i].ID + ">"
						+ pline[i].LINE_NAME + "</option>");
				}
				layui.form.render('select');

		} else {
			layer.alert(data.msg);
		}
	}, "GET", false, function(res) {
		layer.alert(res.msg);
	});
}


// 新增编辑弹出框
function openLine(id, title) {
	if (id == null || id == "") {
		$("#id").val("");
	}
	var index=layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#setLine'),
		end : function() {
			cleanLine();
		}
	});
	layer.full(index);//弹出框全屏
}

// 添加组长铁三角
function addLine() {
	// 清空弹出框数据
	cleanLine();
	// 打开弹出框
	openLine(null, "添加组长铁三角");
}
// 新增组长铁三角提交
function addSubmit(obj) {
	CoreUtil.sendAjax("/base/linerImg/add", JSON.stringify(obj.field),
			function(data) {
				if (data.result) {
					layer.alert("操作成功", function() {
						layer.closeAll();
						cleanLine();
						// 加载页面
						loadAll();
					});
				} else {
					layer.alert(data.msg, function() {
						layer.closeAll();
					});
				}
			}, "POST", false, function(res) {
				layer.alert(res.msg);
			});
}

// 编辑组长铁三角提交
function editSubmit(obj) {
	CoreUtil.sendAjax("/base/linerImg/edit", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanLine();
				// 加载页面
				loadAll();
			});
		} else {
			layer.alert(data.msg, function() {
				layer.closeAll();
			});
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}
function doDelete(){
	/*var check_id = tableIns.checkStatus('#lineList');
	alert(check_id)*/
	var checkStatus = tableIns.checkStatus("lineList");
}
// 删除组长铁三角
function delLine(id) {
		var param = {
			"id" : id
		};
		CoreUtil.sendAjax("/base/linerImg/delete", JSON.stringify(param),
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
	tableIns.reload({
		page : {
			curr : pageCurr
		// 从当前页码开始
		}
	});
}

// 清空新增表单数据
function cleanLine() {
	$('#lineForm')[0].reset();
	layui.form.render();// 必须写
}
