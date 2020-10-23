/**
 * 缺陷管理
 */
var pageCurr;
$(function() {
    layui.use(['table','form','laydate'], function(){
        var table = layui.table
            ,form = layui.form
            ,layer = layui.layer
            ,laydate = layui.laydate;

        tableIns=table.render({
            elem: '#iList'
            ,url:context+'/sysDefect/getList'
            ,method: 'get' //默认：get请求
            ,cellMinWidth: 80
            ,page: true,
            request: {
                pageName: 'page' //页码的参数名称，默认：page
                ,limitName: 'rows' //每页数据量的参数名，默认：limit
            },
            parseData: function (res) {
                // 可进行数据操作
                return {
                    "count": res.data.total,
                    "msg":res.msg,
                    "data":res.data.rows,
                    "code": res.status //code值为200表示成功
                }
            },
            cols: [[
                {type:'numbers'}
                ,{field:'moduleName', title:'模块名称', width:120}
                ,{field:'priority', title:'优先级', width:80,templet:function (d){	
                	if(d.priority=="1"){
                		return "高"
                	}else if(d.priority=="2"){
                		return "中"
                	}else if(d.priority=="3"){
                		return "低"
                	}
                }}
                ,{field:'status', title:'状态', width:80}
                ,{field:'descript', title:'问题描述', width:450}
                ,{field:'offerName', title: '提出人', width:100}
                ,{field:'offerDate', title: '提出日期', width:100}
                ,{field:'handlerName', title:'处理人',width:100}
                ,{field:'handlerDate', title: '解决日期', minWidth:120}
                ,{field:'remark', title: '备注', width:150}
                ,{fixed:'right', title:'操作',width:120,align:'center', toolbar:'#optBar'}
            ]]
            ,  done: function(res, curr, count){
                //如果是异步请求数据方式，res即为你接口返回的信息。
                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                //console.log(res);
                //得到当前页码
                //console.log(curr);
                //得到数据总量
                //console.log(count);
                pageCurr=curr;
            }
        });

        //监听工具条
        table.on('tool(iTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                //删除
            	del(data)
            } else if(obj.event === 'edit'){
                //编辑
                edit(data);
            }
        });
        //校验
        form.verify({
			  intValue: [
			   /^\+?[1-9][0-9]*$/
			    ,'此项数据应大于0且不含小数点'
			  ] 
			}); 
        //监听提交
        form.on('submit(add)', function(data){
            debugger;
            var obj = data.field;
            // 新增或编辑
            if(obj.id){
                doEdit(obj);
            }else{
                doAdd();
            }
            return false;
        });
        //日期
        laydate.render({
            elem: '#offerDate'
        });
        laydate.render({
            elem: '#handlerDate'
        });
        //监听搜索框
        form.on('submit(searchSubmit)', function(data){
            //重新加载table
            load(data);
            return false;
        });

    });
});

//新增编辑弹出框
function openAdd(id,title){
    if(id==null || id==""){
        $("#id").val("");
    }
    layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['550px'],
        content:$('#addDiv'),
        end:function(){
            clean();
        }
    });
}

//新增
function add(){
    clean();
    openAdd(null,"新增");
}
function doAdd(){
    $.ajax({
        type: "POST",
        data: $("#addForm").serialize(),
        url: context+"/sysDefect/add",
        success: function (res) {
            if (res.result == true) {
                layer.alert("操作成功",function(){
                    layer.closeAll();
                    loadAll();
                });
            } else {
                layer.alert(res.msg,function(){
                    layer.closeAll();
                });
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试",function(){
                layer.closeAll();
            });
        }
    });
}

//编辑
function edit(obj){
    clean();
    $("#id").val(obj.id);
    $("#moduleName").val(obj.moduleName);
    $("#priority").val(obj.priority);
    $("#status").val(obj.status);
    $("#descript").val(obj.descript);
    $("#offerName").val(obj.offerName);
    $("#offerDate").val(obj.offerDate);
    $("#handlerName").val(obj.handlerName);
    $("#handlerDate").val(obj.handlerDate);
    $("#remark").val(obj.remark);
    openAdd(obj.id,"编辑");
}

function del(obj){
	layer.confirm('您确定要删除吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			$.ajax({
		        type: "POST",
		        data: {"id" : obj.id},
		        url: context+"/sysDefect/delete",
		        success: function (res) {
		            if (res.result == true) {
		                layer.alert("操作成功",function(){
		                    layer.closeAll();
		                    loadAll();
		                });
		            } else {
		                layer.alert(res.msg,function(){
		                    layer.closeAll();
		                });
		            }
		        },
		        error: function () {
		            layer.alert("操作请求错误，请您稍后再试",function(){
		                layer.closeAll();
		            });
		        }
		    });
		});
}
function doEdit(obj){
    $.ajax({
        type: "POST",
        data: $("#addForm").serialize(),
        url: context+"/sysDefect/edit",
        success: function (res) {
            if (res.result == true) {
                layer.alert("操作成功",function(){
                    layer.closeAll();
                    loadAll();
                });
            } else {
                layer.alert(res.msg,function(){
                    layer.closeAll();
                });
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试",function(){
                layer.closeAll();
            });
        }
    });
}

//重新加载表格（搜索）
function load(obj){
    //重新加载table
    tableIns.reload({
        where: {
            keyword:obj.field.keyword
        }
        , page: {
            curr: pageCurr //从当前页码开始
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

//清空新增表单数据
function clean(){
    $('#addForm')[0].reset();
    layui.form.render();// 必须写
}

