/**
 * 线体管理
 */
var pageCurr,_index=0;
$(function() {
	layui.use([ 'form', 'table','tableFilter','upload' ], function() {
		var table = layui.table, form = layui.form,tableFilter = layui.tableFilter,upload = layui.upload;

		tableIns = table.render({
			elem : '#listTable',
			url : context + '/app/getList',
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
					"count" : res.data.total,
					"msg" : res.msg,
					"data" : res.data.rows,
					"code" : res.status
				// code值为200表示成功
				}
			},
			cols : [ [ {
				type : 'numbers'
			},
			{type:'checkbox'}
			// ,{field:'id', title:'ID', width:80, unresize:true, sort:true}
			, {
				field : 'versionNo',
				title : '版本号', sort: true,filter: true
			}, {
				field : 'versionUrl',
				title : '版本文件', sort: true
			}, {
				field : 'lastupdateDate',width : 145,
				title : '更新时间', sort: true
			}, {
				field : 'createDate',width : 145,
				title : '添加时间', sort: true
			}, {
				fixed : 'right',
				title : '操作',
				align : 'center',
				toolbar : '#optBar'
			} ] ],
			done : function(res, curr, count) {
				localtableFilterIns.reload();
				pageCurr = curr;
			}
		});
		var demoListView = $('#demoList')
		var localtableFilterIns = tableFilter.render({
			'elem' : '#listTable',
			//'parent' : '#doc-content',
			'mode' : 'api',//服务端过滤
			'filters' : [
				{field: 'versionNo', type:'input'},
				{field: 'lastupdateDate', type:'date'},
				{field: 'createDate', type:'date'},
				{field: 'versionUrl', type:'input'},
			],
			'done': function(filters){}
		})
		//上传控件
        upload.render({
            elem: '#upload'
            ,url: context+'/file/uploadAppFile'
            ,accept: 'file' //普通文件
            //,exts: 'apk' //只允许上apk
            ,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致。
                layer.load(); //上传loading
                demoListView.empty();
                //读取本地文件
                obj.preview(function(index, file, result){
                  var tr = $(['<tr id="upload-'+ index +'">'
                    ,'<td>'+ file.name +'</td>'
                    ,'<td>'+ (file.size/1024).toFixed(1) +'kb</td>'
                    ,'<td>等待上传</td>'
                    ,'<td>'
                    ,'<button class="layui-btn layui-btn-xs demo-reload layui-hide">重传</button>'
                    ,'<button class="layui-btn layui-btn-xs layui-btn-danger demo-delete">删除</button>'
                    ,'</td>'
                  ,'</tr>'].join(''));
                  
                  //单个重传
                  tr.find('.demo-reload').on('click', function(){
                    obj.upload(index, file);
                  });
                  
                  //删除
                  tr.find('.demo-delete').on('click', function(){
                    tr.remove();
                    uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                  });
                  
                  demoListView.append(tr);
                });
            }
            ,done: function(res,index, upload){
                layer.closeAll('loading'); //关闭loading
                
                if(res.result == true){
                	var tr = demoListView.find('tr#upload-'+index)
                    ,tds = tr.children();
                    tds.eq(2).html('<span style="color: #5FB878;">上传成功</span>');
                    //tds.eq(3).html(''); //清空操作
                    $("#versionUrl").val(res.data);
                }else{
                	var tr = demoListView.find('tr#upload-'+index)
                    ,tds = tr.children();
                    tds.eq(2).html('<span style="color: #FF5722;">上传失败</span>');
                    //tds.eq(3).find('.demo-reload').removeClass('layui-hide'); //显示重传
                    $("#versionUrl").val("");
                }
            }
            ,error: function(index, upload){
                layer.closeAll('loading'); //关闭loading
            }
        });
		
		//头工具栏事件
		table.on('toolbar(listTable)', function(obj){
		    var checkStatus = table.checkStatus(obj.config.id);
		    switch(obj.event){
		      case 'doAdd':
		    	  demoListView.empty();
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
		        		id += data[i].id+",";
		        		console.log(data[i])
		        	}
		        	delLine(id);
		        }
		        
		      break;
		    };
		  });

		// 监听工具条
		table.on('tool(listTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				
				layer.confirm('您确定要删除' + data.lineNo + '线体吗？', {
					btn : [ '确认', '返回' ]
				// 按钮
				}, function() {
					delLine(data.id);
				});
			} else if (obj.event === 'edit') {
				// 编辑
				edite(data);
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


	});

});
function edite(data){
	form.val("lineForm", {
		"id" : data.id,
		"lineNo" : data.data.lineNo,
		"lineName" : data.data.lineName,
		"linerCode" : data.data.linerCode,
		"linerName" : data.data.linerName,
	});
	openLine(id, "编辑线体")
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
		area : [ '750px' ],
		content : $('#setLine'),
		end : function() {
			cleanLine();
		}
	});
	layer.full(index);//弹出框全屏
}

// 添加线体
function addLine() {
	// 清空弹出框数据
	cleanLine();
	// 打开弹出框
	openLine(null, "添加");
}
// 新增
function addSubmit(obj) {
	
	CoreUtil.sendAjax("/app/add", JSON.stringify(obj.field),
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

// 编辑线体提交
function editSubmit(obj) {
	CoreUtil.sendAjax("/app/edit", JSON.stringify(obj.field), function(
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
// 删除线体
function delLine(id) {
		var param = {
			"id" : id
		};
		CoreUtil.sendAjax("/app/delete", JSON.stringify(param),
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
