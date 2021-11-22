/**
 * 制造部包装运输费管理
 * 五金:hardware
 * 注塑:molding
 * 表面处理:surface
 * 组装:packag
 * 外协:out
 */
var pageCurr;
var _index = 0;
var fileId = "";
var tipsVal;
$(function() {
	layui.use([ 'form', 'table','upload','tableSelect' ], function() {
		var table = layui.table, table2 = layui.table,form = layui.form,upload = layui.upload,tableSelect = layui.tableSelect,
			tableSelect1 = layui.tableSelect,tableSelect2 = layui.tableSelect,tableSelect3 = layui.tableSelect,upload2 =layui.upload;
		isComplete()
		tableIns = table.render({
			elem : '#listTable',
			// url : context + '/productProcess/getList?bsType='+bsType+'&quoteId='+quoteId,
			url : context + '/quoteSum/getFreightList?quoteId=' + quoteId,
			method : 'get', // 默认：get请求
			cellMinWidth : 80,
			//toolbar: '#toolbar',
			height:'full-65',//固定表头&full-查询框高度
			even:true,//条纹样式
			page : true,
			totalRow : true,
			limit: 50,
			limits: [50,100,200,300,500],
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
				{field : 'bsElement',width : 300,title : '组件名称',totalRowText : "合计"},
				{field : 'bsFreight',width : 90,title : '包装运输费','edit':'text',totalRow : true},
				{field : 'fileName',title : '附件信息',width : 450,templet : '<div><a style="cursor: pointer;color: blue;text-decoration:underline;" href="' + context
						+ '/file/get?fsFileId={{d.fileId}}" th:href="@{/file/get?fsFileId={{d.fileId}}}">{{ d.fileName==null?"":d.fileName }}</a></div>'},

				{fixed : 'right', title : '操作', align : 'center',width:160, toolbar : '#optBar'}
			] ],
			done : function(res, curr, count) {
				pageCurr = curr;
				var tableIns = this.elem.next(); // 当前表格渲染之后的视图
				layui.each(res.data, function(i, item){
					console.log(iStatus.data);
				if(iStatus.data=='2'){
					tableIns.find('tr[data-index=' + i + ']').find('td').data('edit', false).css("background-color", "#d2d2d2");
					// $("select[name='selectModelType']").attr("disabled", "disabled");
					layui.form.render('select');
				}
				});
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
			}
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
				var params = {
					"fileName" : "",
					"fileId" : "",
					"id": data.id
				}
				addSubmit(params);
			} else if (obj.event === 'edit') {
				// 编辑
				uploadFile(data.id);
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


	});
});
function isComplete() {
	if (iStatus >= 2) {
		$("#savebtn").addClass("layui-btn-disabled").attr("disabled", true)
	}
}


function Confirm(){
	// saveTable();
	var dates = layui.table.cache['listTable'];
	var params = {
		"id" : quoteId,
		"bsType":"freight",
		"bsCode":"freight",
		"dates":dates,
	};
	layer.confirm('一经提交则不得再修改，确定要提交吗？', {
		btn : [ '确认', '返回' ]
	}, function() {
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

// 上传控件
function uploadFile(id) {
	// console.log(id);
	var InitUpload = layui.upload.render({
		elem : '#upload',
		url : context + '/file/upload',
		accept : 'file' // 普通文件
		,
		number:10,
		multiple:true,
		before : function(obj) { // obj参数包含的信息，跟 choose回调完全一致。
			layer.load(); // 上传loading
		},
		done : function(res, index, upload) {

			if (res.result == true) {
				// document.getElementById("filelist").innerHTML =
				// $("#filelist").html()+getExcField(_index,res.data)
				console.log(res.data);
				var params = {
					"fileName" : res.data.bsName,
					"fileId" : res.data.id,
					"id": id
				}
				addSubmit(params);
				// $("#upload").remove();
				// InitUpload();
			}
			layer.closeAll('loading'); // 关闭loading

		},
		error : function(index, upload) {
			layer.closeAll('loading'); // 关闭loading
		}

	});
	$('#upload').click();
}

function addSubmit(obj) {
	CoreUtil.sendAjax("/productProcess/editFileId",JSON.stringify(obj), function(
		data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				// cleanDefect();
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

//重新加载表格（全部）
function loadAll(){
	//重新加载table
	tableIns.reload({
		page: {
			curr: pageCurr //从当前页码开始
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

