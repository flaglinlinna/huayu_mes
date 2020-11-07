/**
 * 缺陷管理
 */
var pageCurr;
var _index = 0;
$(function() {
    layui.use(['table','form','laydate','upload'], function(){
        var table = layui.table
            ,form = layui.form
            ,layer = layui.layer
            ,laydate = layui.laydate
            ,upload = layui.upload;

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
                ,{field:'priority', title:'优先级',sort: true, width:90,templet:function (d){
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
                ,{fixed:'right', title:'操作',width:180,align:'center', toolbar:'#optBar'}
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
            } else if(obj.event === 'file'){
                //附件管理
                fileView(data.id);
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
                    document.getElementById("filelist").innerHTML = $("#filelist").html()+getExcField(_index,res.data)
                    _index++;
                }
            }
            ,error: function(index, upload){
                layer.closeAll('loading'); //关闭loading
            }
        });

        //上传控件
        upload.render({
            elem: "#upload2"
            ,url: context + "/file/upload"
            ,accept: "file" //普通文件
            ,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致。
                layer.load(); //上传loading
            }
            ,done: function(res,index, upload){
                layer.closeAll("loading"); //关闭loading
                if(res.result == true){
                    //文件上传成功后再添加文件关联信息表
                    $.ajax({
                        type: "POST",
                        data: { "defectId":$("#defectId").val(), "fileId":res.data.id, "fileName":res.data.bsName },
                        url: context+"/sysDefect/addFile",
                        success: function (res2) {
                            if (res2.result == true){
                                //显示新添加的文件
                                document.getElementById("filelist2").innerHTML = $("#filelist2").html()+getExcFieldBefore(res.data,$('#defectId').val(),'/sysDefect/delFile');
                            } else {
                                layer.alert(res2.msg,function(index){
                                    layer.close(index);
                                });
                            }
                        },
                        error: function(index, upload){
                            layer.alert("操作请求错误，请您稍后再试",function(index){
                                layer.close(index);
                            });
                        }
                    });
                }
            }
            ,error: function(index, upload){
                layer.closeAll('loading'); //关闭loading
            }
        });

    });
});

//新增编辑弹出框
function openAdd(id,title){
    if(id==null || id==""){
        $("#id").val("");
    }
    var index = layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['550px'],
        content:$('#addDiv'),
        macmin:true,//弹出框全屏
        end:function(){
            clean();
        }
    });
    layer.full(index);//弹出框全屏
}

//新增
function add(){
    clean();
    $("#itemDiv1").css("display", "block");
    $("#itemDiv2").css("display", "block");
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
    $("#itemDiv1").css("display", "none");
    $("#itemDiv2").css("display", "none");
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
    _index = 0;
    document.getElementById("filelist").innerHTML = "";
    layui.form.render();// 必须写
}

//附件管理弹出框
function openFileView(id,title){
    if(id==null || id==""){
        $("#defectId").val("");
    }
    layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['700px'],
        content:$('#fileDiv'),
        end:function(){
        }
    });
}
//附件管理
function fileView(id){
    $("#defectId").val(id);
    $.ajax({
        type: "POST",
        data: { defectId:id },
        url: context+"/sysDefect/getFile",
        success: function (res) {
            if (res.result == true) {
                document.getElementById("filelist2").innerHTML = "";
                for(var i = 0; i < res.data.length; i++){
                    document.getElementById("filelist2").innerHTML = $("#filelist2").html()+getExcFieldBefore(res.data[i],id,"/sysDefect/delFile");
                }
                openFileView(id,"附件管理");
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

