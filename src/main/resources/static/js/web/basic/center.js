/**
 * 工作中心管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table' ], function() {
		var table = layui.table, form = layui.form;

		tableIns = table.render({
			elem : '#centerList',
			url : context + 'base/center/getList',
			method : 'get' // 默认：get请求
			,
			cellMinWidth : 80,
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
				field : 'bsCode',
				title : '工作中心编码'
			}, {
				field : 'bsName',
				title : '工作中心名称'
			}, {
				field : 'bsStatus',
				title : '状态',
				width : 95,
				templet : '#statusTpl'
			}, {
				field : 'lastupdateDate',
				title : '更新时间'
			}, {
				field : 'createDate',
				title : '添加时间',
			}, {
				fixed : 'right',
				title : '操作',
				align : 'center',
				toolbar : '#optBar'
			} ] ],
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

		// 监听在职操作
		form.on('switch(isStatusTpl)', function(obj) {
			setStatus(obj, this.value, this.name, obj.elem.checked);
		});
		// 监听工具条
		table.on('tool(centerTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delCenter(data, data.id, data.bsCode);
			} else if (obj.event === 'edit') {
				// 编辑
				getCenter(data, data.id);
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
			load(data);
			return false;
		});
		// 编辑工作中心
		function getCenter(obj, id) {
			var param = {
				"id" : id
			};
			CoreUtil.sendAjax("base/center/getWorkCenter", JSON.stringify(param),
					function(data) {
						if (data.result) {
							form.val("centerForm", {
								"id" : data.data.id,
								"bsCode" : data.data.bsCode,
								"bsName" : data.data.bsName,
							});
							openCenter(id, "编辑工作中心")
						} else {
							layer.alert(data.msg)
						}
					}, "POST", false, function(res) {
						layer.alert("操作请求错误，请您稍后再试");
					});
		}
		// 设置用户正常/禁用
		function setStatus(obj, id, name, checked) {
			// setStatus(obj, this.value, this.name, obj.elem.checked);
			var isStatus = checked ? 0 : 1;
			var deaprtisStatus = checked ? "正常" : "禁用";
			// 正常/禁用

			layer.confirm(
					'您确定要把工作中心：' + name + '设置为' + deaprtisStatus + '状态吗？', {
						btn1 : function(index) {
							var param = {
								"id" : id,
								"bsStatus" : isStatus
							};
							CoreUtil.sendAjax("/base/center/doStatus", JSON
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
							obj.elem.checked = isStatus;
							form.render();
							layer.closeAll();
						},
						cancel : function() {
							obj.elem.checked = isStatus;
							form.render();
							layer.closeAll();
						}
					})
		}
	});
	
	//触发事件
	  var active = {
	    add: function(){
	      var that = this; 
	      //多窗口模式，层叠置顶
	      layer.open({
	        type: 2 //此处以iframe举例
	        ,title: '当你选择该窗体时，即会在最顶端'
	        ,area: ['390px', '260px']
	        ,shade: 0
	        ,maxmin: true
	        ,offset: [ //为了演示，随机坐标
	          Math.random()*($(window).height()-300)
	          ,Math.random()*($(window).width()-390)
	        ] 
	        ,content: '//layer.layui.com/test/settop.html'
	        ,btn: ['继续弹出', '全部关闭'] //只是为了演示
	        ,yes: function(){
	          $(that).click(); 
	        }
	        ,btn2: function(){
	          layer.closeAll();
	        }
	        
	        ,zIndex: layer.zIndex //重点1
	        ,success: function(layero){
	          layer.setTop(layero); //重点2
	        }
	      });
	    }
	  };
	  
	  $('#addBtn').on('click', function(){
		    var othis = $(this), method = othis.data('method');
		    active[method] ? active[method].call(this, othis) : '';
		  });

});

// 新增编辑弹出框
function openCenter(id, title) {
	if (id == null || id == "") {
		$("#id").val("");
	}
	layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
        shade: 0,
        maxmin: true,
        //btn: ['保存', '全部关闭'],
        //btnAlign: 'c' , //按钮居中
		area : [ '550px' ],
		content : $('#setCenter'),
		end : function() {
			cleanCenter();
		}
	  /*,btn1: function(){
        alert('1')
      }
	  ,btn2: function(){
        alert('2')
      }*/
	});
}

// 添加工作中心
function addCenter() {
	// 清空弹出框数据
	cleanCenter();
	// 打开弹出框
	openCenter(null, "添加工作中心");
}
// 新增工作中心提交
function addSubmit(obj) {
	CoreUtil.sendAjax("base/center/add", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanCenter();
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

// 编辑工作中心提交
function editSubmit(obj) {
	CoreUtil.sendAjax("base/center/edit", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanCenter();
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

// 删除工作中心
function delCenter(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除' + name + '工作中心吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("base/center/delete", JSON.stringify(param),
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

// 清空新增表单数据
function cleanCenter() {
	$('#centerForm')[0].reset();
	layui.form.render();// 必须写
}
