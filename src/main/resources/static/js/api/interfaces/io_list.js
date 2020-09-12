/**
 * 接口管理
 */
var pageCurr;
$(function() {
    layui.use(['form', 'table', 'laydate'], function(){
        var table = layui.table
            ,form = layui.form
            ,laydate = layui.laydate;

        tableIns=table.render({
            elem: '#iList'
            ,url:context+'/interfaces/getList'
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
                ,{field:'bsCode', title:'编号', width:120}
                ,{field:'bsName', title:'名称', width:120}
                ,{field:'bsUrl', title:'地址', width:200}
                ,{field:'bsStatus', title:'状态',width:95,align:'center',templet:'#statusTpl'}
                ,{field:'bsRemark', title: '描述', minWidth:120}
                ,{field:'modifiedTime', title: '更新时间', width:150}
                ,{field:'createdTime', title: '添加时间', width:150}
                ,{fixed:'right', title:'操作', width:200, align:'center', toolbar:'#optBar'}
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

        //监听checkbox操作
        form.on('switch(isStatusTpl)', function(obj){
            setStatus(obj,this.value,this.name,obj.elem.checked);
        });
        //监听工具条
        table.on('tool(iTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                //删除
                delData(data,data.id,data.bsCode);
            } else if(obj.event === 'edit'){
                //编辑
                editData(data,data.id);
            } else if(obj.event === 'set'){
                //配置
                window.location = context+'/interfaces/toConfigure?id=' + data.id;
            }
        });
        //监听提交
        form.on('submit(add)', function(data){
            if(data.field.id==null || data.field.id==""){
                //新增角色
                doAdd(data);
            }else{
                doEdit(data);
            }
            return false;
        });

        //日期
        laydate.render({
            elem: '#createdTimeStart'
        });
        laydate.render({
            elem: '#createdTimeEnd'
        });
        laydate.render({
            elem: '#startDate'
        });
        laydate.render({
            elem: '#endDate'
        });

        //监听搜索框
        form.on('submit(search)', function(data){
            //重新加载table
            load(data);
            return false;
        });

        //监听下拉框
        form.on('select(bsType)', function (data) {
            if(data.value == "1"){
                $("#timingDiv").css("display", "block");
            }else{
                $("#timingDiv").css("display", "none");
            }
        })

    });
});

//新增
function addData(){
    $("#timingDiv").css("display", "block");//默认显示执行计划
    cleanData();//清空弹出框数据
    openData(null,"添加接口");//打开弹出框
}
//编辑
function editData(obj,id){
    $.ajax({
        type: "GET",
        data: { "id":id },
        url: context+"/interfaces/getInterfaces",
        success: function (data) {
            if (data.result) {
                $("#id").val(data.data.id==null?'':data.data.id);
                $("#bsCode").val(data.data.bsCode==null?'':data.data.bsCode);
                $("#bsName").val(data.data.bsName==null?'':data.data.bsName);
                if(data.data.bsStatus){
                    $("#bsStatus").prop("checked", true);
                }else{
                    $("#bsStatus").prop("checked", false);
                }
                $("#bsMethod").val(data.data.bsMethod==null?'':data.data.bsMethod);
                $("#bsUrl").val(data.data.bsUrl==null?'':data.data.bsUrl);
                $("#bsParam").val(data.data.bsParam==null?'':data.data.bsParam);
                $("#bsType").val(data.data.bsType==null?'':data.data.bsType);
                $("#startDate").val(data.data.startDate==null?'':data.data.startDate);
                $("#endDate").val(data.data.endDate==null?'':data.data.endDate);
                $("#timingCode").val(data.data.timingCode==null?'':data.data.timingCode);
                $("#bsRemark").val(data.data.bsRemark==null?'':data.data.bsRemark);
                //渲染
                if(data.data.bsType == "1"){
                    $("#timingDiv").css("display", "block");
                }else{
                    $("#timingDiv").css("display", "none");
                }
                layui.form.render('checkbox');
                layui.form.render('select');

                openData(id,"编辑接口");//打开弹出框
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

//新增或编辑弹出框
function openData(id,title){
    if(id==null || id==""){
        $("#id").val("");
    }
    layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['700px'],
        content:$('#addDiv'),
        end:function(){
            cleanData();
        }
    });
}

//清空新增或编辑表单数据
function cleanData(){
    $("#id").val("");
    $("#bsCode").val("");
    $("#bsName").val("");
    $("#bsStatus").prop("checked", true);
    $("#bsMethod").val("");
    $("#bsUrl").val("");
    $("#bsParam").val("");
    $("#bsType").val("");
    $("#startDate").val("");
    $("#endDate").val("");
    $("#timingCode").val("");
    $("#bsRemark").val("");

    //渲染
    layui.form.render('checkbox');
    layui.form.render('select');
}

//新增提交
function doAdd(obj) {
    $.ajax({
        type: "POST",
        data: $("#addForm").serialize(),
        url: context+"/interfaces/add",
        success: function (data) {
            if (data.result) {
                layer.alert("操作成功",function(){
                    layer.closeAll();
                    cleanData();
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

//编辑提交
function doEdit(obj) {
    $.ajax({
        type: "POST",
        data: $("#addForm").serialize(),
        url: context+"/interfaces/edit",
        success: function (data) {
            if (data.result) {
                layer.alert("操作成功",function(){
                    layer.closeAll();
                    cleanData();
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

//删除
function delData(obj,id,name) {
    if(id!=null){
        layer.confirm('您确定要删除'+name+'接口吗？', {
            btn: ['确认','返回'] //按钮
        }, function(){
            $.post(context+"/interfaces/delete",{"id":id},function(data){
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

//设置启用/禁用
function setStatus(obj,id,name,checked){
    var isStatus=checked ? 1 : 0;
    var dataIsStatus=checked ? "启用":"禁用";
    //正常/禁用
    layer.confirm('您确定要把接口：'+name+'设置为'+dataIsStatus+'状态吗？', {
        btn: ['确认','返回'] //按钮
    }, function(){
        $.ajax({
            type: "POST",
            data: { "id": id, "bsStatus": isStatus},
            url: context+"/interfaces/doStatus",
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

//重新加载表格（搜索）
function load(obj){
    //重新加载table
    tableIns.reload({
        where: {
            bsCode:obj.field.codeSearch, bsName:obj.field.nameSearch,
            createdTimeStart:obj.field.createdTimeStart, createdTimeEnd:obj.field.createdTimeEnd,
            bsStatus:obj.field.statusSearch
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

