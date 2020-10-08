/**
 * 不良内容管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table' ], function() {
		var table = layui.table, form = layui.form;
		tableIns = table.render({
			elem : '#prodprocList',
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
			, {
				field : 'bsClient',
				title : '客户'
			}, {
				field : 'bsName',
				title : '物料编码'
			},{
				field : 'bsName',
				title : '物料描述'
			}
			, {
				field : 'proc',
				title : '工序'
			}, {
				field : 'atribt',
				title : '过程属性',
			}, {
				field : 'createDate',
				title : '创建时间',
			},{
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
		tableAddInfo=table.render({
			elem : '#procList',
			method : 'get' ,// 默认：get请求			
			cols : [ [ {
				type : 'numbers'
			}
			// ,{field:'id', title:'ID', width:80, unresize:true, sort:true}
			, {
				field : 'bsCode',
				title : '编码',
				width: 150
			}, {
				field : 'bsName',
				title : '名称',
				width: 150
			}, {
				field : 'bsStatus',
				title : '状态',
				templet : '#CheckSelect',
				width: 150
			} ] ],
			data:[]
		});	

		
	});
});


//添加工作中心
function addProc() {
	// 清空弹出框数据
	//cleanCenter();
	//GET Info
	
	CoreUtil.sendAjax("base/prodproc/getData", "",
			function(data) {
				if (data.result) {
				tableAddInfo.reload({
					data:data.data.WoProc
				});
				$("#mtrial_id").empty();
				var mtl=data.data.Mtrial;
				for (var i = 0; i < mtl.length; i++) {
					if(i==0){
						$("#mtrial_id").append("<option value=''>请点击选择</option>");
					}
					$("#mtrial_id").append("<option value=" + mtl[i].id+ ">" + mtl[i].bsName + "</option>");
					//console.log(mtl[i].bsName)
				}			
				layui.form.render('select');
				openProc(null, "添加工序");
				} else {
					layer.alert(data.msg)
				}
				//console.log(data)
			}, "POST", false, function(res) {
				layer.alert("操作请求错误，请您稍后再试");
			});
	// 打开弹出框
}
//新增编辑弹出框
function openProc(id, title) {
//	if (id == null || id == "") {
//		$("#id").val("");
//	}
	layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		area : ['700px', '500px'],
		content : $('#setProc'),
		end : function() {
			//cleanCenter();
		},
		success: function() {
			
		}
	});
}



