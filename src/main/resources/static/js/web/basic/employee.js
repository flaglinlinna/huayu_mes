/**
 * 员工信息管理
 */
var pageCurr;
var employeeId;
var employeeName;
var employeeCode;
$(function() {
	layui.use([ 'form', 'table' ,'laydate','upload'], function() {
		var table = layui.table, form = layui.form,laydate = layui.laydate,upload = layui.upload;
		layui.form.render('select');
		tableIns = table.render({
			elem : '#employeeList',
			url : context + '/base/employee/getList',
			method : 'get' // 默认：get请求
			,
			cellMinWidth : 80,
			height:'full-110'//固定表头&full-查询框高度
			,even:true,//条纹样式
			page : true,
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
			cols : [ [ {
				type : 'numbers'
			}
			// ,{field:'id', title:'ID', width:80, unresize:true, sort:true}
			, {
				field : 'empCode',
				title : '员工工号',
				width:100,
				sort: true,
			}, {
				field : 'empName',
				title : '员工姓名',
				width:120,
				sort: true,
			}, {
				field : 'empType',
				title : '员工类型',
				width:120
			}, {
				field : 'empIdNo',
				title : '身份证',
				width:170
			}, {
				field : 'empStatus',
				title : '员工状态',
				templet : '#statusTpl',
				align : 'center',
				width:100
			}, {
				field : 'joinDate',
				title : '入职日期',
				width:110,
				sort: true,
			}, {
				field : 'leaveDate',
				title : '离职日期',
				width:110,
				sort: true,
			}, {
				field : 'deptName',
				title : '一级部门',
				width:90
			} ,
				{
					field : 'deptName1',
					title : '二级部门',
					width:90
				} ,

				{
					field : 'fingerNum',
					title : '指纹个数',
					width:80
				} ,
				{
				field : 'lastupdateDate',
				title : '更新时间',
				width:150
			}, {
				field : 'createDate',
				title : '添加时间',
				width:150
			}
			, {
				fixed : 'right',
				title : '操作',
				align : 'center',
				toolbar : '#optBar'
			}
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

		var uploadInst = upload.render({
			elem: '#upload2'
			,url: context + "/base/employee/uploadImg" //改成您自己的上传接口
			,before: function(obj){
				//预读本地文件示例，不支持ie8
				this.data = {
					employeeId: function(){
							return  employeeId;
						},
					employeeCode:function(){
						return  employeeCode;
					},
				},
				obj.preview(function(index, file, result){
					$('#demo1').attr('src', result); //图片链接（base64）
				});
			}
			,done: function(res){
				//如果上传失败
				if(res.code > 0){
					var demoText = $('#demoText');
					demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
					demoText.find('.demo-reload').on('click', function(){
						uploadInst.upload();
					});
				}else {
					var demoText = $('#demoText');
					demoText.html('<span style="color: #FF5722;">上传成功</span>');
					
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


		form.on('switch(isStatusTpl)', function(obj) {//修改员工状态
			//console.log(obj, this.value, this.name, obj.elem.checked);
			//setStatus(obj, this.value, this.name, obj.elem.checked);//此功能取消
		});
		// 监听工具条
		table.on('tool(employeeTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delEmployee(data, data.id, data.custNo);
			} else if (obj.event === 'edit') {
				// 编辑
				getEmployee(data, data.id);
			} else  if(obj.event === 'uploadImg'){
				console.log(data)
				employeeCode = data.empCode;
				var demoText = $('#demoText');
				if(data.empImg){
					//已经上传
					$('#demo1').attr('src',  context + "/base/employee/viewByUrl?empImg="+data.empImg);
					demoText.html('<a id="del_file_btn" class="layui-btn layui-btn-xs layui-btn-danger">删除</a>');
				}else{
					$('#demo1').attr('src', "");
					demoText.html('<span style="color: #FF5722;">请上传照片</span>');
				}
				
				uploadImg(data.id,data.empName)
			}
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
			load(data.field);
			return false;
		});
		//日期选择器
		laydate.render({ 
		  elem: '#joinDate',
		  type: 'date' //默认，可不填
		});
		laydate.render({ 
			  elem: '#leaveDate',
			  type: 'date' //默认，可不填
			});
		
		form.on('select(empStatus)', function(data){
			//console.log(data)
			var search_par = {'keywordSearch':$('#keywordSearch').val(),'empStatus':data.value};
			load(search_par)
			/*if(data.value == 1){
				$("#searchSessionNum").attr("disabled","true");
				form.render('select');
			}else{
				$("#searchSessionNum").removeAttr("disabled");
				form.render('select');//select是固定写法 不是选择器
			}*/
		});
		
		$(document).on('click','#del_file_btn',function(){
			deleteImg(employeeId);
		});

		function deleteImg(id) {
			//清空数据库图片字段
			if (id != null) {
				var param = {
					"id" : id
				};
				layer.confirm('您确定要删除' + employeeName + '的照片吗？', {
					btn : [ '确认', '返回' ]
					// 按钮
				}, function() {
					CoreUtil.sendAjax("/base/employee/deleteImg", JSON.stringify(param),
						function(data) {
							if (isLogin(data)) {
								if (data.result == true) {
									// 回调弹框
									layer.alert("删除成功！", function(index) {
										layer.closeAll();
										// 加载load方法
										// loadAll();
									});
								} else {
									layer.alert(data, function(index) {
										layer.closeAll();
									});
								}
							}
						});
				});
			}
		}
		
		// 编辑员工信息
		function getEmployee(obj, id) {
			var param = {
				"id" : id
			};
			CoreUtil.sendAjax("/base/employee/getEmployee", JSON
					.stringify(param), function(data) {
				if (data.result) {
					form.val("employeeForm", {
						"id" : data.data.id,
						"empCode" : data.data.empCode,
						"empName" : data.data.empName,
						"empIdNo" : data.data.empIdNo,
						"joinDate" : data.data.joinDate,
						"leaveDate" : data.data.leaveDate,
						"empType" : data.data.empType,
						"deptId" : data.data.deptId,
					});
					openEmployee(id, "编辑员工信息")
				} else {
					layer.alert(data.msg)
				}
			}, "POST", false, function(res) {
				layer.alert("操作请求错误，请您稍后再试");
			});
		}
	});

});

function uploadImg(id,title) {
	employeeId = id,
	employeeName = title;
	var index = layer.open({
		type : 1,
		title : "上传"+title+"的图片",
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#fileDiv'),
		end : function() {
			// cleanEmployee();
		}
	});
	layer.full(index);
	
}

// 新增编辑弹出框
function openEmployee(id, title) {
	if (id == null || id == "") {
		$("#id").val("");
	}
	layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#setEmployee'),
		end : function() {
			cleanEmployee();
		}
	});
}

$("#getData").click(function(){
	
	layer.confirm('是否执行同步操作？',
			{
				btn1 : function(index) {
					CoreUtil.sendAjax("/base/employee/getUpdateData","", function(data) {
						console.log(data)
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
						layer.alert(res.msg);
					});
				}	
			});
});

// 添加员工信息
function addEmployee() {
	// 清空弹出框数据
	cleanEmployee();
	// 打开弹出框
	openEmployee(null, "添加员工信息");
}
// 新增员工信息提交
function addSubmit(obj) {
	CoreUtil.sendAjax("/base/employee/add", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanEmployee();
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

// 编辑员工信息提交
function editSubmit(obj) {
	CoreUtil.sendAjax("/base/employee/edit", JSON.stringify(obj.field),
			function(data) {
				if (data.result) {
					layer.alert("操作成功", function() {
						layer.closeAll();
						cleanEmployee();
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

// 删除员工信息
function delEmployee(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除' + name + '员工吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/base/employee/delete", JSON.stringify(param),
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
			keyword : obj.keywordSearch,
			empStatus:obj.empStatus
		},
		/*page : {
			curr : pageCurr
		// 从当前页码开始
		}*/
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
function cleanEmployee() {
	$('#employeeForm')[0].reset();
	layui.form.render();// 必须写
}
