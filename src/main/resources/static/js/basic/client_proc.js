/**
 * 客户通用工艺维护
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table' ], function() {
		var table = layui.table, form = layui.form;

		tableIns = table.render({
			elem : '#client_procList',
			url : context + 'base/client_proc/getList',
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
				field : 'pClientCode',
				title : '客户编号',
				width:120
			},{
				field : 'pClientName',
				title : '客户名称'
			}, {
				field : 'bsOrder',
				title : '工序顺序',
				width:90
			}, {
				field : 'pProcCode',
				title : '工序编号'
			}, {
				field : 'pProcName',
				title : '工序名称'
			}, {
				field : 'lastupdateDate',
				title : '更新时间',
				//templet:'<div>{{d.lastupdateDate?DateUtils.formatDate(d.lastupdateDate):""}}</div>'
			}, {
				field : 'createDate',
				title : '添加时间',
				//templet:'<div>{{d.createDate?DateUtils.formatDate(d.createDate):""}}</div>'
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
				merge(res.data,['pClientCode'],[1]);
				merge(res.data,['pClientName'],[2]);
			}
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
				field : 'bsCode',
				type:"checkbox"
			},
			
			{
				field : 'bsCode',
				title : '编码'
			}, {
				field : 'bsName',
				title : '名称',
			}] ],
			data:[]
		});	
		form.on('select(pkClient)', function(data){//监听选择事件
			var params={"client":$("#pkClient").val()}
			getProcByClient(params);
		})
		
		// 监听工具条
		table.on('tool(client_procTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delClientProc(data, data.id, data.pProcName);
			} else if (obj.event === 'edit') {
				// 编辑
				console.log("edit");
				//getClientProc(data, data.id);//未写
			}
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
	});
});
//添加不工艺流程
function addProc() {
	// 清空弹出框数据
	getProcList("");
	// 打开弹出框
	openProc(null, "添加工艺流程");
}

//根据客户信息获取工序数据
function getProcByClient(params){
	CoreUtil.sendAjax("base/client_proc/getClientItem", JSON.stringify(params), function(
			data) {
		if (data.result) {
			
			var beSelected=data.data;
			console.log(beSelected)
			
		} else {
			layer.alert(data.msg);
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}

function delClientProc(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除' + name + '工序吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("base/client_proc/delete", JSON.stringify(param),
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
	console.log(procIdlist)
	CoreUtil.sendAjax("base/client_proc/addItem", JSON.stringify(params), function(
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
	CoreUtil.sendAjax("base/client_proc/getProcList", "",
			function(data) {
				if (data.result) {
					tableProc.reload({
						data:data.data.process
					});
					$("#pkClient").empty();
					var c=data.data.client;
					for (var i = 0; i < c.length; i++) {
						if(i==0){
							$("#pkClient").append("<option value=''>请点击选择</option>");
						}
						$("#pkClient").append("<option value=" + c[i].id+ ">"+c[i].bsName+"</option>");
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