/**
 * 产品工艺流程
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table' , 'tableSelect' ], function() {
		var table = layui.table, form = layui.form, tableSelect = layui.tableSelect;

		tableIns = table.render({
			elem : '#client_procList',
			url : context + 'base/prodproc/getList',
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
			,{
				field : 'itemId',
				title : '物料Id',
				width:0,
				hide:true
			}
			,{
				field : 'itemNo',
				title : '物料编码',
				width:140
			},{
				field : 'itemName',
				title : '物料名称',
				templet:'<div>{{d.mtrial.itemName}}</div>'
			}, {
				field : 'procOrder',
				title : '工序顺序',"edit":"number","event": "dataCol",
				width:80
			}, {
				field : 'procNo',
				title : '工序编号',
				width:80,
				templet:'<div>{{d.process.procNo}}</div>'
			}, {
				field : 'procName',
				title : '工序名称',
				width:130,
				templet:'<div>{{d.process.procName}}</div>'
			}, {
				field : 'jobAttr',
				title : '过程属性',
				templet : '#statusTpl',
				width:90,
				align : 'center'
			},{
				fixed : 'right',
				title : '操作',
				width:120,
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
				//console.log(res)
				/*for(var i =0;i<res.data.length;i++){
						if(res.data[i].jobAttr==0){
							//这句才是真正选中，通过设置关键字LAY_CHECKED为true选中，这里只对第一行选中
					        res.data[i]["LAY_CHECKED"]='true';
							//更改css来实现选中的效果
							//$('tbody tr[data-index=' + i + '] input[type="checkbox"]').prop('checked', true);
							$('tbody tr[data-index="'+i+'"]  div.layui-form-checkbox').addClass('layui-form-checked');
						}		
				}*/
				merge(res.data,['itemNo','mtrial.itemName'],[2,3]);
			}
		});
		
		tableSelect=tableSelect.render({
			elem : '#num',
			searchKey : 'keyword',
			checkedKey : 'id',
			searchPlaceholder : '试着搜索',
			table : {
				url:  context +'base/prodproc/getProdList',
				method : 'get',
				cols : [ [
				{ type: 'checkbox' },//多选  radio
				, {
					field : 'id',
					title : 'id',
					width : 0,hide:true
				}
                , {
					field : 'itemNo',
					title : '物料编码',
					width : 110
				},{
					field : 'itemName',
					title : '物料描述',
					width : 240
				}, {
					field : 'itemUnit',
					title : '单位',
					width : 60
				} ] ],
				page : true,
				request : {
					pageName : 'page' // 页码的参数名称，默认：page
					,
					limitName : 'rows' // 每页数据量的参数名，默认：limit
				},
				parseData : function(res) {
					console.log(res)
					if(res.result){
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
				//选择完后的回调，包含2个返回值 elem:返回之前input对象；data:表格返回的选中的数据 []
				//console.log(data);
				var da=data.data;
				//console.log(da[0].num)
				var ids = '';var nos = "";
				data.data.forEach(function(element) {
					ids += element.id+",";
					nos += element.itemNo+",";
				});
				form.val("clientProcForm", {
					"itemId":ids,
					"num" : nos
				});
				form.render();// 重新渲染
		}
		});
		
		form.on('checkbox(isStatusTpl)', function(obj) {//修改过程属性
			console.log(obj);
			setStatus(obj, this.value, this.name, obj.elem.checked);
		});
		// 监听搜索框
		form.on('submit(searchSubmit)', function(data) {
			console.log(data)
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
				title : '编码'
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
		//监听单元格编辑
		  table.on('edit(client_procTable)', function(obj){
			  console.log(obj)
		    var value = obj.value //得到修改后的值
		    ,data = obj.data //得到所在行所有键值
		    ,field = obj.field; //得到字段
		   // layer.msg('[ID: '+ data.id +'] ' + field + ' 字段更改为：'+ value);
		    var tr = obj.tr;
	        // 单元格编辑之前的值
	        var oldtext = $(tr).find("td[data-field='"+obj.field+"'] div").text();
		    if(field == 'procOrder'){
		    	//判断是否为数字
		    	if(isRealNum(value)){
		    		doProcOrder(data.id,value);
		    	}else{
		    		layer.msg('请填写数字!');
		    		loadAll();
		    	}
		    }
		  });
		
		// 监听工具条
		table.on('tool(client_procTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delClientProc(data.id);
			} else if (obj.event === 'edit') {
				// 编辑
				//getClientProc(data, data.id);//未写
				addProc(data.custId)
			}
		});
		
		// 监听提交
		form.on('submit(addSubmit)', function(data) {	
			console.log(data)
			if (data.field.id == null || data.field.id == "") {
				// 新增
				var procIdList="";
				var cList=table.checkStatus('procList').data;//被选中行的数据  id 对应的值
				//console.log(cList.length)
				for(var i=0;i<cList.length;i++){//获取被选中的行
					procIdList+=cList[i].id+","//工序的ID序列，用“；”分隔
				}
				addSubmit(procIdList,data.field.itemId,data.field.num);
			} else {
				//editSubmit(data);//未写
			}
			return false;
		});
		
		// 设置过程属性
		function setStatus(obj, id, name, checked) {
			var jobAttr = checked ? 0 : 1;
			var deaprtisStatus = checked ? "勾选" : "不勾选";
			// 正常/禁用
			layer.confirm('您确定要把工序：' + name + '过程属性设置为' + deaprtisStatus + '状态吗？',
					{
						btn1 : function(index) {
							var param = {
								"id" : id,
								"jobAttr" : jobAttr
							};
							CoreUtil.sendAjax("/base/prodproc/doJobAttr", JSON
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
		
		// 设置过程属性
		function doProcOrder(id, procOrder) {
			var param = {
					"id" : id,
					"procOrder" : procOrder
				};
				CoreUtil.sendAjax("/base/prodproc/doProcOrder", JSON
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
		}
	});
});
//添加不工艺流程
function addProc(id) {
	// 获取初始化信息
	getAddList(id);
	// 打开弹出框
	openProc(null, "添加工艺流程");
}

//根据客户信息获取工序数据
function getProcByClient(params){
	var params={"client":params}
	CoreUtil.sendAjax("base/client_proc/getClientItem", JSON.stringify(params), function(
			data) {
		if (data.result) {
			var beSelected=data.data;
			console.log(beSelected)
			tableProc.reload({
				done : function(res, curr, count) {
					console.log(res)
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
		layer.confirm('您确定要删除吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("base/prodproc/delete", JSON.stringify(param),
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
function addSubmit(procIdlist,itemIds,itemNos) {
	var params = {
			"proc":procIdlist,
			"itemIds" : itemIds,
			"itemNos":itemNos
		};

	CoreUtil.sendAjax("base/prodproc/add", JSON.stringify(params), function(
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
function getAddList(id){
	console.log(id)
	CoreUtil.sendAjax("base/prodproc/getAddList", "",
			function(data) {
		console.log(data)
				if (data.result) {
					//工序表
					tableProc.reload({
						data:data.data.Process,
						done : function(res, curr, count) {
							cleanProc();//清空之前的选中
							console.log(id)
							if(id != ''){
								getProcByClient(id)
							}
						}
					});
					$("#pkClient").empty();
					var c=data.data.Client;
					//console.log(c)
					for (var i = 0; i < c.length; i++) {
						if(i==0){
							$("#pkClient").append("<option value=''>请点击选择</option>");
						}
						if(id != ''){
							if(c[i].id == id){
								$("#pkClient").append("<option value=" + c[i][0]+ " selected>"+c[i][1]+"</option>");
							}else{
								$("#pkClient").append("<option value=" + c[i][0]+ ">"+c[i][1]+"</option>");
							}
						}else{
							$("#pkClient").append("<option value=" + c[i][0]+ ">"+c[i][1]+"</option>");
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
    var data = res;
    var mergeIndex = 0;//定位需要添加合并属性的行数
    var mark = 1; //这里涉及到简单的运算，mark是计算每次需要合并的格子数
    //var columsName = ['itemCode'];//需要合并的列名称
    //var columsIndex = [3];//需要合并的列索引值
    for (var k = 0; k < columsIndex.length; k++) { //这里循环所有要合并的列
        var trArr = $(".layui-table-body>.layui-table").find("tr");//所有行
            for (var i = 1; i < data.length; i++) { //这里循环表格当前的数据
                var tdCurArr = trArr.eq(i).find("td").eq(columsIndex[k]);//获取当前行的当前列
                var tdPreArr = trArr.eq(mergeIndex).find("td").eq(columsIndex[k]);//获取相同列的第一列
                if (data[i][columsName[0]] === data[i-1][columsName[0]]) { //后一行的值与前一行的值做比较，相同就需要合并
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
		area : [ '750px','410px'],
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