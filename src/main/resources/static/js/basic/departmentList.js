

/**
 * 角色管理
 */
var pageCurr;
$(function() {
    layui.use(['form' ,'table'], function(){
        var table = layui.table
            ,form = layui.form;

        tableIns=table.render({
            elem: '#departmentList'
            ,url:context+'/department/getList'
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
            // response:{
            //     statusName: 'status' //数据状态的字段名称，默认：code
            //     ,statusCode: 200 //成功的状态码，默认：0
            //     ,countName: 'count' //数据总数的字段名称，默认：count
            //     ,dataName: 'data' //数据列表的字段名称，默认：data
            // },
            cols: [[
                {type:'numbers'}
                // ,{field:'id', title:'ID', width:80, unresize:true, sort:true}
                ,{field:'deCode', title:'编号', width:120}
                ,{field:'deName', title:'名称', width:120}
                ,{field:'deManager', title: '部门经理', Width:120}
                ,{field:'deManagerTel', title: '部门经理电话', Width:120}
                ,{field:'deStatus', title:'状态',width:95,templet:'#statusTpl'}
                ,{field:'modifiedTime', title: '更新时间', width:150}
                ,{field:'createdTime', title: '添加时间', width:150}
                ,{fixed:'right', title:'操作', width:150, align:'center', toolbar:'#optBar'}
            ]]
            ,done: function(res, curr, count){
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

        //监听在职操作
        form.on('switch(isStatusTpl)', function(obj){
        	console.log("switch");
            setStatus(obj,this.value,this.name,obj.elem.checked);
        });
        //监听工具条
        table.on('tool(departmentTable)', function(obj){
        	console.log("table.on(tool(departmentTable)");
            var data = obj.data;
            if(obj.event === 'del'){
                //删除
                delDepartment(data,data.id,data.deCode);
            } else if(obj.event === 'edit'){
                //编辑
                getDepartment(data,data.id);
            }
        });
        //监听提交
        form.on('submit(addSubmit)', function(data){
            if(data.field.id==null || data.field.id==""){
                //新增
               addSubmit(data);
            }else{
                editSubmit(data);
            }
            return false;
        });
    });
    //搜索框
    layui.use(['form','laydate'], function(){
        var form = layui.form ,layer = layui.layer
            ,laydate = layui.laydate;
        //日期
        laydate.render({
            elem: '#createdTimeStart'
        });
        laydate.render({
            elem: '#createdTimeEnd'
        });

        //监听搜索框
        form.on('submit(searchSubmit)', function(data){
            //重新加载table
            load(data);
            return false;
        });
    });
});

//设置用户正常/禁用
function setStatus(obj,id,name,checked){
    var isStatus=checked ? 0 : 1;
    var userIsStatus=checked ? "正常":"禁用";
    //正常/禁用
    layer.confirm('您确定要把部门：'+name+'设置为'+userIsStatus+'状态吗？', {
        btn: ['确认','返回'] //按钮
    }, function(){
        $.ajax({
            type: "POST",
            data: { "id": id, "deStatus": isStatus},
            url: context+"/department/doStatus",
            success: function (data) {
                if (data.result == true) {
                    layer.alert("操作成功",function(){
                        layer.closeAll();
                        loadAll();
                    });
                } else {
                    layer.alert(data.msg,function(){
                        layer.closeAll();
                    });
                }
            }
        });
    }, function(){
        layer.closeAll();
        loadAll();
    });
}

//新增编辑弹出框
function openDepartment(id,title){
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
        content:$('#setDepartment'),
        end:function(){
            cleanDepartment();
        }
    });
}

//添加角色
function addDepartment(){
    //清空弹出框数据
    cleanDepartment();
    //打开弹出框
    openDepartment(null,"添加部门");
}
//新增角色提交
function addSubmit(obj) {
    $.ajax({
        type: "POST",
        data: $("#departmentForm").serialize(),
        url: context+"/department/add",
        success: function (data) {
            if (data.result) {
                layer.alert("操作成功",function(){
                    layer.closeAll();
                    cleanDepartment();
                    //加载页面
                    loadAll();
                });
            } else {
                layer.alert(data.msg,function(){
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

//编辑角色
function getDepartment(obj,id){
    $.ajax({
        type: "GET",
        data: { "id":id },
        url: context+"/department/getDepartment",
        success: function (data) {
            if (data.result) {
                $("#id").val(data.data.id==null?'':data.data.id);
                $("#deCode").val(data.data.deCode==null?'':data.data.deCode);
                $("#deName").val(data.data.deCode==null?'':data.data.deName);
                $("#deManager").val(data.data.deManager==null?'':data.data.deManager);
                $("#deManagerTel").val(data.data.deManagerTel==null?'':data.data.deManagerTel);
                openDepartment(id,"编辑部门")
            } else {
                layer.alert(data.msg,function(){
                });
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试",function(){
            });
        }
    });
}
//编辑角色提交
function editSubmit(obj) {
    $.ajax({
        type: "POST",
        data: $("#departmentForm").serialize(),
        url: context+"/department/edit",
        success: function (data) {
            if (data.result) {
                layer.alert("操作成功",function(){
                    layer.closeAll();
                    cleanDepartment();
                    //加载页面
                    loadAll();
                });
            } else {
                layer.alert(data.msg,function(){
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

//删除角色
function delDepartment(obj,id,name) {
    if(id!=null){
        layer.confirm('您确定要删除'+name+'角色吗？', {
            btn: ['确认','返回'] //按钮
        }, function(){
            $.post(context+"/department/delete",{"id":id},function(data){
                if(isLogin(data)){
                    if(data.result==true){
                        //回调弹框
                        layer.alert("删除成功！",function(){
                            layer.closeAll();
                            //加载load方法
                            loadAll();
                        });
                    }else{
                        layer.alert(data,function(){
                            layer.closeAll();
                        });
                    }
                }
            });
        }, function(){
            layer.closeAll();
        });
    }
}


//重新加载表格（搜索）
function load(obj){
    //重新加载table
    tableIns.reload({
        where: {
        	keyword:obj.field.keywordSearch
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
function cleanDepartment(){
    $("#id").val("");
    $("#deCode").val("");
    $("#deName").val("");
    $("#deManager").val("");
    $("#deManagerTel").val("");
}

