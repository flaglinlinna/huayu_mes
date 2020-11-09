/**
 * 客户通用工艺维护
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table' ], function() {
		var table = layui.table, form = layui.form;

		tableIns = table.render({
			elem : '#listTable',
			url : context + '/base/client_proc/getList',
			method : 'get' // 默认：get请求
			,
			toolbar: '#toolbar', //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
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
			,{
				field : 'custId',
				title : '客户Id',
				width:0,
				hide:true
			}
			,{
				field : 'custNo',
				title : '客户编号',
				sort: true,
				width:100
			},{
				field : 'custName',
				title : '客户名称',
				sort: true,
				width:150
			}, {
				field : 'procOrder',
				title : '工序顺序',
				sort: true,
				width:100
			}, {
				field : 'procNo',
				title : '工序编号',
				sort: true,
				width:100
			}, {
				field : 'procName',
				title : '工序名称',
				sort: true,
				width:100
			}, {
				field : 'jobAttr',
				title : '过程属性',
				templet : '#statusTpl',
				sort: true,
				width:95,
				align : 'center'
				//type:"checkbox"
			}, {
				field : 'lastupdateDate',
				title : '更新时间',
				sort: true,
				width:150,
				templet:'<div>{{d.lastupdateDate?DateUtils.formatDate(d.lastupdateDate):""}}</div>'
			}, {
				field : 'createDate',
				title : '添加时间',
				sort: true,
				width:150,
				templet:'<div>{{d.createDate?DateUtils.formatDate(d.createDate):""}}</div>'
			}, {
				fixed : 'right',
				title : '操作',
				align : 'center',
				toolbar : '#optBar',
				width:130,
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
				//console.log(res)
				for(var i =0;i<res.data.length;i++){
						if(res.data[i].jobAttr==0){
							//这句才是真正选中，通过设置关键字LAY_CHECKED为true选中，这里只对第一行选中
					        res.data[i]["LAY_CHECKED"]='true';
							//更改css来实现选中的效果
							//$('tbody tr[data-index=' + i + '] input[type="checkbox"]').prop('checked', true);
							$('tbody tr[data-index="'+i+'"]  div.layui-form-checkbox').addClass('layui-form-checked');
						}		
				}
				merge(res.data,['custNo','custName'],[2,3]);
			}
		});
		
		
		form.on('checkbox(isStatusTpl)', function(obj) {//修改过程属性
			setStatus(obj, this.value, this.name, obj.elem.checked);
		});
		// 监听搜索框
		form.on('submit(searchSubmit)', function(data) {
			// 重新加载table
			load(data);
			return false;
		});
		tableProc=table.render({
			elem : '#procList',
			method : 'get' ,// 默认：get请求			
			cols : [ [ {
				type : 'numbers'
			}
			// ,{field:'id', title:'ID', width:80, unresize:true, sort:true}
			, 
			{
				type:"checkbox"
			},
			
			{
				field : 'procNo',
				title : '编码',
				
			}, {
				field : 'procName',
				title : '名称',
			}] ],
			data:[]
		});	
		
		form.on('select(pkClient)', function(data){//监听选择事件
			//var params={"client":$("#pkClient").val()}
			getProcByClient($("#pkClient").val());
		})
		
		// 监听工具条
		table.on('tool(listTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				
				layer.confirm('您确定要删除' + data.procName + '工序信息吗？', {
					btn : [ '确认', '返回' ]
				// 按钮
				}, function() {
					delClientProc( data.id );
				});
				
				
			} else if (obj.event === 'edit') {
				// 编辑
				//getClientProc(data, data.id);//未写
				addProc(data.custId)
			}
		});
		//头工具栏事件
		table.on('toolbar(listTable)', function(obj){
		    var checkStatus = table.checkStatus(obj.config.id);
		    switch(obj.event){
		      case 'doAdd':
		    	  addProc();
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
		        	delClientProc(id);
		        }
		        
		      break;
		    };
		  });
		
		// 监听提交
		form.on('submit(addSubmit)', function(data) {			
			if (data.field.id == null || data.field.id == "") {
				// 新增
				var procIdList="";
				var cList=table.checkStatus('procList').data;//被选中行的数据  id 对应的值
				for(var i=0;i<cList.length;i++){//获取被选中的行
					procIdList+=cList[i].id+";"//工序的ID序列，用“；”分隔
				}
				var client=data.field.pkClient;
				addSubmit(procIdList,client);
			} else {
				//editSubmit(data);//未写
			}
			return false;
		});
		
		// 设置过程属性
		function setStatus(obj, id, name, checked) {
			var jobAttr = checked ? 0 : 1;
			var deaprtisStatus = checked ?  "勾选":"不勾选";
			layer.confirm('您确定要把工序：' + name + '过程属性设置为' + deaprtisStatus + '状态吗？',
					{
						btn1 : function(index) {
							var param = {
								"id" : id,
								"jobAttr" : jobAttr
							};
							CoreUtil.sendAjax("/base/client_proc/doJobAttr", JSON
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
							obj.elem.checked = jobAttr;
							form.render();
							layer.closeAll();
						},
						cancel : function() {
							obj.elem.checked = jobAttr;
							form.render();
							layer.closeAll();
						}
					});
		}
	});
});
//添加不工艺流程
function addProc(id) {
	// 清空弹出框数据
	getProcList(id);
	// 打开弹出框
	openProc(null, "添加工艺流程");
}

//根据客户信息获取工序数据
function getProcByClient(params){
	var params={"client":params}
	CoreUtil.sendAjax("/base/client_proc/getClientItem", JSON.stringify(params), function(
			data) {
		if (data.result) {
			var beSelected=data.data;
			tableProc.reload({
				done : function(res, curr, count) {
					for(var i =0;i<res.data.length;i++){
						for(var j=0;j<beSelected.length;j++){
							if(res.data[i].id == beSelected[j].procId){
								//这句才是真正选中，通过设置关键字LAY_CHECKED为true选中，这里只对第一行选中
						        res.data[i]["LAY_CHECKED"]='true';
								//更改css来实现选中的效果
								//$('tbody tr[data-index=' + i + '] input[type="checkbox"]').prop('checked', true);
								$('tbody tr[data-index="'+i+'"]  div.layui-form-checkbox').addClass('layui-form-checked');
							}
							
						}
					}
				}
			})
		} else {
			layer.alert(data.msg);
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}


function delClientProc( id) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除' + name + '工序吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/base/client_proc/delete", JSON.stringify(param),
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

//新增工艺流程提交
function addSubmit(procIdlist,client) {
	var params = {
			"proc":procIdlist,
			"client" : client
		};

	CoreUtil.sendAjax("/base/client_proc/addItem", JSON.stringify(params), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanProc();
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

//获取客户，工序信息
function getProcList(id){
	CoreUtil.sendAjax("/base/client_proc/getProcList", "",
			function(data) {
				if (data.result) {
					tableProc.reload({
						data:data.data.process,
						done : function(res, curr, count) {
							cleanProc();//清空之前的选中
							if(id != ''&&id !=undefined){
								getProcByClient(id)
							}
						}
					});
					$("#pkClient").empty();
					var c=data.data.client;
					//console.log(c)
					for (var i = 0; i < c.length; i++) {
						if(i==0){
							$("#pkClient").append("<option value=''>请点击选择</option>");
						}
						if(id != ''){
							if(c[i].id == id){
								$("#pkClient").append("<option value=" + c[i].id+ " selected>"+c[i].custName+"</option>");
							}else{
								$("#pkClient").append("<option value=" + c[i].id+ ">"+c[i].custName+"</option>");
							}
						}else{
							$("#pkClient").append("<option value=" + c[i].id+ ">"+c[i].custName+"</option>");
						}
					}			
					layui.form.render('select');
				} else {
					layer.alert(data.msg)
				}
				//console.log(data)
			}, "POST", false, function(res) {
				layer.alert("操作请求错误，请您稍后再试");
			});
}
function merge(res,columsName,columsIndex) {
    //console.log(res)
    var data = res;
    var mergeIndex = 0;//定位需要添加合并属性的行数
    var mark = 1; //这里涉及到简单的运算，mark是计算每次需要合并的格子数
    //var columsName = ['itemCode'];//需要合并的列名称
    //var columsIndex = [3];//需要合并的列索引值

    for (var k = 0; k < columsName.length; k++) { //这里循环所有要合并的列
        var trArr = $(".layui-table-body>.layui-table").find("tr");//所有行
            for (var i = 1; i < data.length; i++) { //这里循环表格当前的数据
                var tdCurArr = trArr.eq(i).find("td").eq(columsIndex[k]);//获取当前行的当前列
                var tdPreArr = trArr.eq(mergeIndex).find("td").eq(columsIndex[k]);//获取相同列的第一列
                
                if (data[i][columsName[k]] === data[i-1][columsName[k]]) { //后一行的值与前一行的值做比较，相同就需要合并
                    mark += 1;
                    tdPreArr.each(function () {//相同列的第一列增加rowspan属性
                        $(this).attr("rowspan", mark);
                    });
                    tdCurArr.each(function () {//当前行隐藏
                        $(this).css("display", "none");
                    });
                }else {
                    mergeIndex = i;
                    mark = 1;//一旦前后两行的值不一样了，那么需要合并的格子数mark就需要重新计算
                }
            }
        mergeIndex = 0;
        mark = 1;
    }
}
//新增编辑弹出框
function openProc(id, title) {
	if (id == null || id == "") {
		$("#id").val("");
	}
	layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '650px','410px'],
		content : $('#setClientProc'),
		end : function() {
			cleanProc();
		}
	});
}
//重新加载表格（搜索）
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

//重新加载表格（全部）
function loadAll() {
	// 重新加载table
	tableIns.reload({
		page : {
			curr : pageCurr
		// 从当前页码开始
		}
	});
}
//清空新增表单数据
function cleanProc() {
	$('#clientProcForm')[0].reset();
	layui.form.render();// 必须写
}