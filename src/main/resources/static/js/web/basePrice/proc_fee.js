/**
 * 模具成本维护
 */
var pageCurr;
var _index = 0;
var fileId = "";
$(function() {
	layui.use([ 'form', 'table','upload','tableSelect','upload' ], function() {
		var table = layui.table, form = layui.form,upload = layui.upload,upload2 = layui.upload,
			upload3 = layui.upload,
			tableSelect = layui.tableSelect,tableSelect1 = layui.tableSelect,
			tableSelect2 = layui.tableSelect,upload = layui.upload;

		tableIns = table.render({
			elem : '#iList',
			url : context + '/basePrice/procFee/getList',
			method : 'get' // 默认：get请求
			,
			cellMinWidth : 80,
			//toolbar: '#toolbar',
			height:'full-70'//固定表头&full-查询框高度
			,even:true,//条纹样式
			page : true,
			limit: 50,
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
			cols : [ [ {fixed:'left',
				type : 'numbers'
			}
			// ,{field:'id', title:'ID', width:80, unresize:true, sort:true}
			, {fixed:'left',field : 'productCode',title : '模具编号',width:150},

			  {fixed:'left',field : 'productName',title : '产品',width:180},
			  {field : 'numHole',title : '穴数',width:60},
			  {field : 'structureMj',title : '模具结构',width:100},
			  {field : 'mjPrice',title : '模具报价价格(未税)',width:140},
			  {field : 'feeType1',title : '材料成本(未税)',width:120},
			  {field : 'feeType2',title : '制造成本(未税)',width:120},
			  {field : 'feeType3',title : '外发纹理费用(未税)',width:140},
			  // {field : 'feeType4',title : '热流道费用',width:130},
			  // {field : 'feeProc',title : '评估总费用(未税)',width:100},
			  {field : 'stQuote',title : '参考报价',width:100},
			  {field : 'feeAll',title : '评估总费用(未税)',width:130},
			  {field : 'fmemo',title : '备注',width:100},
				{field:'img', title:'图片预览'  , width:150,templet:function (d) {
						if(d.fimg!=null) {
							// return '<div><a href="/file/get?fsFileId=' + d.fimg + '" target="_black" title="点击查看大图"><img  src="/file/get?fsFileId=' + d.fimg + '" /></div>'
							return '<div><img  src="/file/get?fsFileId=' + d.fimg + '" /></div>';
						}else {
							return "";
						}
					}},
			  {fixed : 'right',title : '操作',align : 'center',toolbar : '#optBar',width:120}
			] ],
			done : function(res, curr, count) {
				console.log(res)
				hoverOpenImg();
				// hoverOpenImg2();
				pageCurr = curr;
			}
		});

		var uploadInst = upload.render({
			elem: '#upload2'
			,url: context + '/file/upload' //改成您自己的上传接口
			,before: function(obj){
				//预读本地文件示例，不支持ie8
				// this.data = {
				// 	employeeId: function(){
				// 		return  employeeId;
				// 	},
				// 	employeeCode:function(){
				// 		return  employeeCode;
				// 	},
				// },
				// 	layer.load(); // 上传loading
					obj.preview(function(index, file, result){
						$('#demo1').attr('src', result); //图片链接（base64）
					});
			}
			,done: function(res){
				//如果上传失败
				if(!res.result){
					var demoText = $('#demoText');
					demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
					demoText.find('.demo-reload').on('click', function(){
						uploadInst.upload();
					});
				}else {
					var demoText = $('#demoText');
					demoText.html('<span style="color: #FF5722;">上传成功</span>');
					$('#fimg').val(res.data.id);

				}
				//上传成功
			}
			,error: function(){
				//演示失败状态，并实现重传
				var demoText = $('#demoText');
				demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
				demoText.find('.demo-reload').on('click', function(){
					uploadInst.upload();
				});
			}
		});


		//上传控件
		upload.render({
			elem: '#upload'
			,url: context+'/file/upload'
			,accept: 'file' //普通文件
			,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致。
				layer.load(); //上传loading
			}
			,done: function(res,index, upload){
				layer.closeAll('loading'); //关闭loading
				if(res.result == true){
					document.getElementById("filelist").innerHTML = $("#filelist").html()+getExcField(_index,res.data);
					_index++;
					fileId +=res.data.id +",";

				}
				$('#fileId').val(fileId);
			}
			,error: function(index, upload){
				layer.closeAll('loading'); //关闭loading
			}
		});

		// 导入
		upload3.render({
			elem : '#upload3',
			url : context + '/basePrice/procFee/doExcel',
			accept : 'file' // 普通文件
			,

			before : function(obj) { // obj参数包含的信息，跟 choose回调完全一致，可参见上文。
				layer.load(); // 上传loading
			},
			done : function(res, index, upload) {
				layer.closeAll('loading'); // 关闭loading
				layer.alert(res.msg, function(index) {
					layer.close(index);
					loadAll();
				});

			},
			error : function(index, upload) {
				layer.alert("操作请求错误，请您稍后再试", function() {
				});
				layer.closeAll('loading'); // 关闭loading
				layer.close(index);
			}
		});

		//自定义验证规则
		form.verify({
			num: function(value){
				if(/^\d+$/.test(value)==false && /^\d+\.\d+$/.test(value)==false)
				{
					return '只能输入数字';
				}
			}
		});
		// 监听工具条
		table.on('tool(iTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delItemData(data, data.id, data.productName);
			} else if (obj.event === 'edit') {
				// 编辑
				getItemData(data, data.id);
			}
		});
		// 监听提交
		form.on('submit(addSubmit)', function(data) {	
			console.log(data)
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
		// 编辑
		function getItemData(obj, id) {
			form.val("itemForm", {
				"id" : obj.id,
				"fimg" : obj.fimg,
				"productName" : obj.productName,
				"numHole" : obj.numHole,
				"structureMj": obj.structureMj,
				"mjPrice" : obj.mjPrice,
				"feeType1": obj.feeType1,
				"feeType2": obj.feeType2,
				"feeType3": obj.feeType3,
				"feeType4": obj.feeType4,
				"feeProc":obj.feeProc,
				"feeAll":obj.feeAll,
				"stQuote":obj.stQuote,
				"fmemo" : obj.fmemo,
			});
			if(obj.fimg){
				$('#demo1').attr('src',  context + "/file/view?fsFileId="+obj.fimg);
			}

			//获取客户品质标准文件
			CoreUtil.sendAjax("/basePrice/procFee/getFileList?customId="+obj.id,"",
				function(data) {
					console.log(data);
					for(var i =0;i<data.data.length; i++){
						document.getElementById("filelist").innerHTML = $("#filelist").html()+getExcFieldBefore(data.data[i],data.data[i].qsFileId,"/basePrice/procFee/delFile");
					}
				}, "GET", false, function(res) {
					layer.alert(res.msg);
				});

			openPage(id, "编辑模具成本信息")
		};	
	});
	$("#feeType1,#feeType2,#feeType3,#feeType4,#feeProc").on("input",function(e){
		 //console.log(e.delegateTarget.value); //获取input输入的值
		 calAll();
	});
});

function hoverOpenImg() {
	var img_show = null; // tips提示
	$('td img').hover(function () {
		var kd = $(this).width();
		kd1 = kd * 3;          //图片放大倍数
		kd2 = kd * 3 + 30;       //图片放大倍数
		var img = "<img class='img_msg' src='" + $(this).attr('src') + "' style='width:" + kd1 + "px;' />";
		img_show = layer.tips(img, this, {
			tips: [2, 'rgba(41,41,41,.5)']
			, area: [kd2 + 'px']
		});
	}, function () {
		layer.close(img_show);
	});
	$('td img').attr('style', 'max-width:70px;display:block!important');
}



// 新增编辑弹出框
function openPage(id, title) {
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
		content : $('#setItem'),
		end : function() {
			clean();
		}
	});
	layer.full(index);
}

//导出
function exportExcel(){
	//导出模板
	location.href = context + "/basePrice/procFee/export";
	// location.href = "../../excelFile/排产导入模板.xlsx";//从文件夹内直接提取
	return false;
}

// 添加五金材料
function add() {
	// 清空弹出框数据
	clean();
	// 打开弹出框
	openPage(null, "添加模具成本信息");
}
// 新增五金材料提交
function addSubmit(obj) {
	CoreUtil.sendAjax("/basePrice/procFee/add", JSON.stringify(obj.field), function(
			data) {
		console.log(data)
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				loadAll();
			});
		} else {
			layer.alert(data.msg);
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}

// 编辑
function editSubmit(obj) {
	CoreUtil.sendAjax("/basePrice/procFee/edit", JSON.stringify(obj.field), function(
			data) {
		console.log(data)
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				loadAll();
			});
		} else {
			layer.alert(data.msg);
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}

// 删除
function delItemData(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除产品：' + name + '的模具成本信息吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/basePrice/procFee/delete", JSON.stringify(param),
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
function calAll(){
	var fee1=$("#feeType1").val()==""?0:parseFloat($("#feeType1").val())
	var fee2=$("#feeType2").val()==""?0:parseFloat($("#feeType2").val())
	var fee3=$("#feeType3").val()==""?0:parseFloat($("#feeType3").val())
	// var fee4=$("#feeType4").val()==""?0:parseFloat($("#feeType4").val())
	var fee4=0
	// var fee_proc=$("#feeProc").val()==""?0:parseFloat($("#feeProc").val())
	var fee_proc = 0
	var all= fee1+fee2+fee3+fee4+fee_proc
	$("#feeAll").val(all)
}
// 清空新增表单数据
function clean() {
	$('#itemForm')[0].reset();
	_index = 0;
	document.getElementById("filelist").innerHTML = "";
	layui.form.render();// 必须写
}
