/**
 * 校验规则管理
 */
var pageCurr;
var frule = "";
var fyearFlag = -1;
var fmonthFlag = -1;
var fdayFlag = -1;
var fserialNumFlag = -1;
var fserialLenFlag = -1;
$(function() {
	layui.use([ 'form', 'table','tableSelect' ], function() {
		var table = layui.table, form = layui.form,tableSelect1 = layui.tableSelect,
			tableSelect = layui.tableSelect;

		tableSelect=tableSelect.render({
			elem : '#itemId',
			searchKey : 'keyword',
			checkedKey : 'ID',
			searchPlaceholder : '内部编码搜索',
			table : {
				url : context + '/base/rule/getMtrialListPage',
				method : 'post',

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
					{ type: 'radio' },//单选  radio
					{
						field : 'ID',
						title : 'id',
						width : 0,hide:true
					},
					{
						field : 'ITEM_NO',
						title : '物料编码'
					},
					{
						field : 'ITEM_NAME',
						title : '物料名称'
					},
					{
						field : 'ITEM_MODEL',
						title : '机型'
					},
				] ],
				page : true,
				request : {
					pageName : 'page' // 页码的参数名称，默认：page
					,
					limitName : 'rows' // 每页数据量的参数名，默认：limit
				},

			},
			done : function(elem, data) {
				var da=data.data;
				//选择完后的回调，包含2个返回值 elem:返回之前input对象；data:表格返回的选中的数据 []
				form.val("ruleForm", {
					"itemNo":da[0].ITEM_NO,
					"itemId":da[0].ITEM_NO,
					"itemModel":da[0].ITEM_MODEL,
					"itemName":da[0].ITEM_NAME,
				});
				form.render();// 重新渲染
			}
		});

		tableSelect1=tableSelect1.render({
			elem : '#custName',
			searchKey : 'keyword',
			checkedKey : 'id',
			searchPlaceholder : '客户信息搜索',
			table : {
				url : context + '/base/rule/getCustomerList',
				method : 'post',
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
					{ type: 'radio' },//单选  radio
					{
						field : 'ID',
						title : 'id',
						width : 0,hide:true
					},
					{
						field : 'CUST_NAME_S',
						title : '客户简称'
					},
					{
						field : 'CUST_NAME',
						title : '客户名称'
					},
				] ],
				page : true,
				request : {
					pageName : 'page' // 页码的参数名称，默认：page
					,
					limitName : 'rows' // 每页数据量的参数名，默认：limit
				},

			},
			done : function(elem, data) {
				var da=data.data;
				//选择完后的回调，包含2个返回值 elem:返回之前input对象；data:表格返回的选中的数据 []
				form.val("ruleForm", {
					"custName":da[0].CUST_NAME,
					"custId":da[0].ID,
				});
				form.render();// 重新渲染
			}
		});

		//下拉框初始化
		getBarList();



		tableIns = table.render({
			elem : '#ruleList',
			url : context + '/base/rule/getList',
			method : 'get' // 默认：get请求
			,
			cellMinWidth : 80,
			height: 'full-80'
			,even:true,//条纹样式
			page : true,
			toolbar: '#toolbar', //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
			height: 'full',
			request : {
				pageName : 'page' // 页码的参数名称，默认：page
				,
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
				{type:'checkbox'},
				// ,{field:'id', title:'ID', width:80, unresize:true, sort:true}
				 {field : 'ITEM_NO', title : '物料编号', width : 140, sort: true},
				// {field : 'CUST_NAME', title : '客户名称', width : 140,},
				{field : 'ITEM_NAME', title : '物料描述', sort: true, width : 240},
				{field : 'ITEM_NO_CUS', title : '客户物料编码', width : 120 ,sort: true},
				{field : 'FSAMPLE', title : '条码样例', width : 440 ,sort: true},
				// {field : 'FIX_VALUE', title : '固定值', width : 80},
				// {field : 'YEAR_NAME', title : '年', width : 80},
				// {field : 'MONTH_NAME', title : '月', width : 80},
				// {field : 'DAY_NAME', title : '日', width : 80 ,sort: true},
			// 	{
			// 	field : 'lastupdateDate',
			// 	title : '更新时间',
			// 	templet:'<div>{{d.lastupdateDate?DateUtils.formatDate(d.lastupdateDate):""}}</div>',
			// 	width : 150
			// },
				{field : 'CREATE_DATE', title : '添加时间',
				templet:'<div>{{d.CREATE_DATE?DateUtils.formatDate(d.CREATE_DATE):""}}</div>',
				width : 150},
				// {field : 'SERIAL_NAME', title : '流水号', width : 100 ,sort: true},
				// {field : 'SERIAL_LEN', title : '流水号位数', width : 120 ,sort: true},
				// SERIAL_NUM 流水号
				{
				// fixed : 'right',
				title : '操作', align : 'center', toolbar : '#optBar', width : 120}
			] ],
			done : function(res, curr, count) {
				// 如果是异步请求数据方式，res即为你接口返回的信息。
				// 如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
				// console.log(res);
				// 得到当前页码
				// console.log(curr);
				// 得到数据总量
				// console.log(count);
				pageCurr = curr;
			}
		});
		form.verify({
			  intValue: [
			   /^\+?[1-9][0-9]*$/
			    ,'此项数据应大于0且不含小数点'
			  ] ,
				chkString:function(value){
			  	if(($("#positionBegin").val()==null)){
					return "请先输入开始位置";
				}
				if(($("#positionEnd").val()==null)){
					return "请先输入结束位置";
				}
			  	var chklength = value.length;
				var position = $("#positionEnd").val()-$("#positionBegin").val()+1;
				if(chklength!=position){
					return "验证数据长度应为:"+position+"位";
				}
				},

			});  



		//头工具栏事件
		table.on('toolbar(ruleTable)', function(obj){
			var checkStatus = table.checkStatus(obj.config.id);
			switch(obj.event){
				case 'doAdd':
					addBarcodeRule();
					break;
				case 'doDelete':
					var data = checkStatus.data;
					console.log(data)
					if(data.length == 0){
						layer.msg("请先勾选数据!");
					}else{
						var id="";
						for(var i = 0; i < data.length; i++) {
							id += data[i].id+",";
							console.log(data[i])
						}
						delBarcodeRule("", id, "选中的");
					}
					break;
			};
		});


		// 监听工具条
		table.on('tool(ruleTable)', function(obj) {
			var data = obj.data;
			// console.log(obj)
			if (obj.event === 'del') {
				// 删除
				delBarcodeRule(data, data.ID, data.ITEM_NO);
			} else if (obj.event === 'edit') {
				// 编辑
				getBarcodeRule(data, data.ID);
			}
		});

		table.on('rowDouble(ruleTable)', function(obj){
			getBarcodeRule(obj.data, obj.data.ID);
		});

		// 监听提交
		form.on('submit(addSubmit)', function(data) {
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
		// 编辑校验规则
		function getBarcodeRule(obj, id) {
			// console.log(obj);
			form.val("ruleForm", {
				"id" : obj.ID,
				"itemId":obj.ITEM_NO,
				"itemNo" : obj.ITEM_NO,
				"fmemo":obj.FMEMO,
				"custId":obj.CUST_ID,
				"itemName" : obj.ITEM_NAME,
				"itemModel":obj.ITEM_MODEL,
				"itemNoCus" : obj.ITEM_NO_CUS,
				// "fyear" :obj.FYEAR,
				// "fmonth" : obj.FMONTH,
				// "fday" : obj.FDAY,
				// "serialNum":obj.SERIAL_NUM,
				// "serialLen":obj.SERIAL_LEN,
				// "fixValue" : obj.FIX_VALUE,
				// "fsample" :obj.FSAMPLE,
				"custName":obj.CUST_NAME,
			});
			openBarcodeRule(id, "编辑校验规则",obj.ID)
		}


		$(document).on('click','#setBarcodeRule',function (obj) {
			if($("#serialNum").val()&&!$("#serialLen").val()){
				layer.msg("请先输入流水号位数");
			}
		})

		$("#serialLen").on("click", function () {
			if(!$("#serialNum").val()){
				layer.msg("请先选中流水号");
				$("#serialNum").focus();
				$("#serialLen").val("");
			}
		});

		//流水号位数 输入框
		$("#serialLen").blur(function(){
			if($("#serialLen").val()) {
					if (fserialLenFlag == -1) {
						var fruleArr = frule.split("+");
						fserialLenFlag = getSpiltLength(frule);
						fruleArr[fserialLenFlag] = "位数(" + $('#serialLen').val() + ")";
						frule = fruleArr.join("+");
					} else {
						var fruleArr = frule.split("+");
						fruleArr[fserialLenFlag] = "位数(" + $('#serialLen').val() + ")";
						frule = fruleArr.join("+");
					}
					showFsample();
			}
		});

		//监听年选择框
		form.on('select(yearFsample)', function(){
			if(fyearFlag ==-1){
				var fruleArr = frule.split("+");
				fyearFlag = getSpiltLength(frule);
				fruleArr[fyearFlag] = "年("+$('select[name="fyear"] option:selected').text()+")";
				frule = fruleArr.join("+");
			}else {
				var fruleArr = frule.split("+");
				fruleArr[fyearFlag] = "年("+$('select[name="fyear"] option:selected').text()+")";
				frule = fruleArr.join("+");
			}
			showFsample();
		});

		form.on('select(monthFsample)', function(){
			if(fmonthFlag ==-1){
				var fruleArr = frule.split("+");
				fmonthFlag = getSpiltLength(frule);
				fruleArr[fmonthFlag] = "月("+$('select[name="fmonth"] option:selected').text()+")";
				frule = fruleArr.join("+");
			}else {
				var fruleArr = frule.split("+");
				fruleArr[fmonthFlag] = "月("+$('select[name="fmonth"] option:selected').text()+")";
				frule = fruleArr.join("+");
			}
			showFsample();
		});

		form.on('select(dayFsample)', function(){
			if(fdayFlag ==-1){
				var fruleArr = frule.split("+");
				fdayFlag = getSpiltLength(frule);
				fruleArr[fdayFlag] = "日("+$('select[name="fday"] option:selected').text()+")";
				frule = fruleArr.join("+");
			}else {
				var fruleArr = frule.split("+");
				fruleArr[fdayFlag] = "日("+$('select[name="fday"] option:selected').text()+")";
				frule = fruleArr.join("+");
			}
			showFsample();
		});

		form.on('select(serialNumFsample)', function(){
			if(fserialNumFlag ==-1){
				var fruleArr = frule.split("+");
				fserialNumFlag = getSpiltLength(frule);
				fruleArr[fserialNumFlag] = "流水号("+$('select[name="serialNum"] option:selected').text()+")";
				frule = fruleArr.join("+");
			}else {
				var fruleArr = frule.split("+");
				fruleArr[fserialNumFlag] = "流水号("+$('select[name="serialNum"] option:selected').text()+")";
				frule = fruleArr.join("+");
			}
			showFsample();
		});
	});
});

	function showFsample() {
		if(frule.substr(0,1)=="+"){
			frule = frule.substr(1);
		}
		$('#fsample').val(frule);
	}

	function getSpiltLength(value) {
		if(value ==""||value==null){
			return 0;
		}else {
			// console.log( value.split("+").length);
			return value.split("+").length;
		}
	}

// 固定值添加按钮
function getFsample() {
	if($('#fyear').val()&&fyearFlag!=-1){
		$('#fyear').attr("disabled","disabled");
	}
	if($('#fmonth').val()&&fmonthFlag!=-1){
		$('#fmonth').attr("disabled","disabled");
	}
	if($('#fday').val()&&fdayFlag!=-1){
		$('#fday').attr("disabled","disabled");
	}
	if($('#serialNum').val()&&fserialNumFlag!=-1){
		$('#serialNum').attr("disabled","disabled");
	}
	if($('#serialLen').val()&&fserialLenFlag!=-1){
		$('#serialLen').attr("disabled","disabled");
	}

	if($('#fixValue').val()) {
		frule = frule + "+" + $('#fixValue').val();
	}
	if(frule.substr(0,1)=="+"){
		frule = frule.substr(1);
	}
	$('#fsample').val(frule);
	layui.form.render('select');
}

//年、月、日、流水号 下拉框
function getBarList() {
	CoreUtil.sendAjax("/base/rule/getBarList",
		JSON.stringify(""), function(data) {
			if (data.result) {
				if (data.data.fyear) {
					$("#fyear").empty();
					var fyear = data.data.fyear;
					$("#fyear").append(
						"<option value=" + "" + ">"
						+ "请选择年" + "</option>");
					for (var i = 0; i < fyear.length; i++) {
						$("#fyear").append(
							"<option value=" + fyear[i].FCODE + ">"
							+ fyear[i].FNAME + "</option>");
					}
					layui.form.render('select');
				}
				if (data.data.fmonth) {
					$("#fmonth").empty();
					var fmonth = data.data.fmonth;
					$("#fmonth").append(
						"<option value=" + "" + ">"
						+ "请选择月" + "</option>");
					for (var i = 0; i < fmonth.length; i++) {
						$("#fmonth").append(
							"<option value=" + fmonth[i].FCODE + ">"
							+ fmonth[i].FNAME + "</option>");
					}
					layui.form.render('select');
				}
				if (data.data.fday) {
					$("#fday").empty();
					var fday = data.data.fday;
					$("#fday").append(
						"<option value=" + "" + ">"
						+ "请选择日" + "</option>");
					for (var i = 0; i < fday.length; i++) {
						$("#fday").append(
							"<option value=" + fday[i].FCODE + ">"
							+ fday[i].FNAME + "</option>");
					}
					layui.form.render('select');
				}
				if (data.data.fserialNum) {
					$("#serialNum").empty();
					var fserialNum = data.data.fserialNum;
					$("#serialNum").append(
						"<option value=" + "" + ">"
						+ "请选择流水号" + "</option>");
					for (var i = 0; i < fserialNum.length; i++) {
						$("#serialNum").append(
							"<option value=" + fserialNum[i].FCODE + ">"
							+ fserialNum[i].FNAME + "</option>");
					}
					layui.form.render('select');
				}

			} else {
				layer.alert(data.msg)
			}
		}, "POST", false, function(res) {
			layer.alert("操作请求错误，请您稍后再试");
		});
}

// 新增编辑弹出框
function openBarcodeRule(id, title,item_id) {
	if (id == null || id == "") {
		$("#id").val("");
		$('#itemId').attr('disabled',false);
		$('#itemId').css('background','');
		// style="background: #efefef;"
	}else {
		$('#itemId').attr('disabled','disabled');
		$('#itemId').css('background'," #efefef");
	}
	// getMtrial(item_id);
	var index=layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#setBarcodeRule'),
		end : function() {
			cleanBarcodeRule();
		}
	});
	layer.full(index);
}

// 添加校验规则
function addBarcodeRule() {
	// 清空弹出框数据
	cleanBarcodeRule();
	// 打开弹出框
	openBarcodeRule(null, "添加校验规则",null);
}

function  addFixValue() {
	if(!$('#fixValue').val()){
		layer.msg("请先输入固定值！");
		return ;
	}
	getFsample();
	$('#fixValue').val('');
}

// 新增校验规则提交
function addSubmit(obj) {
	// var str=obj.field.itemId
	var str= $('#itemId').attr('ts-selected');
	// str=str.slice(0,str.indexOf("="));
	obj.field.itemId=str;
	//console.log(obj)
	console.log(obj);
	CoreUtil.sendAjax("/base/rule/addByProc", JSON.stringify(obj.field),
			function(data) {
				if (data.result) {
					layer.alert("操作成功", function() {
						layer.closeAll();
						cleanBarcodeRule();
						// 加载页面
						loadAll();
					});
				} else {
					layer.alert(data.msg, function(index) {
						layer.close(index);
					});
				}
			}, "POST", false, function(res) {
				layer.alert(res.msg);
			});
}

// 编辑校验规则提交
function editSubmit(obj) {
	// var str=obj.field.itemId
	// str=str.slice(0,str.indexOf("="));
	// obj.field.itemId=str;
	// var str= $('#itemId').attr('ts-selected');
	// obj.field.itemId=str;
	var str= $('#itemId').attr('ts-selected');
	// str=str.slice(0,str.indexOf("="));
	obj.field.itemId=str;
	CoreUtil.sendAjax("/base/rule/edit", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanBarcodeRule();
				// 加载页面
				loadAll();
			});
		} else {
			layer.alert(data.msg, function(index) {
				layer.close(index);
			});
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}

// 删除校验规则
function delBarcodeRule(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除物料为' + name + '的校验规则吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/base/rule/delete", JSON.stringify(param),
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
// function getMtrial(item_id) {
// 	CoreUtil.sendAjax("/base/rule/getMtrialList", "", function(data) {
// 		if (data.result) {
// 			//console.log(data)
// 			$("#itemId").empty();
// 			var item=data.data;
// 			for (var i = 0; i < item.length; i++) {
// 				if(i==0){
// 					$("#itemId").append("<option value=''>请点击选择</option>");
// 				}
// 				var value=item[i].id+'='+item[i].itemName
// 				if(item[i].id==item_id){
// 					$("#itemId").append("<option selected value=" +value+">" + item[i].itemNo + "</option>");
// 					$("#itemName").val(item[i].itemName);
// 				}else{
// 					$("#itemId").append("<option  value=" +value+">" + item[i].itemNo + "</option>");
// 				}
// 			}
// 			layui.form.render('select');
// 		} else {
// 			layer.alert(data.msg)
// 		}
// 	})
// }

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
function cleanBarcodeRule() {
	frule = "";
	$('#ruleForm')[0].reset();
	$('#fyear').attr("disabled",false);
	$('#fmonth').attr("disabled",false);
	$('#fday').attr("disabled",false);
	$('#serialNum').attr("disabled",false);
	$('#serialLen').attr("disabled",false);

	layui.form.render();// 必须写
}
