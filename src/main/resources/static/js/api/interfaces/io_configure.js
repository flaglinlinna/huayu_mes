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
            ,url:context+'/interfaces/getRequestList'
            ,where: { interId:interId }
            ,method: 'get' //默认：get请求
            ,toolbar: '#toolbar' //开启头部工具栏，并为其绑定左侧模板
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
                {type: 'checkbox', width: 50}
                ,{field:'bsName', title:'参数名称'}
                ,{field:'bsValue', title:'参数值'}
                ,{field:'bsType', title:'参数类型'}
                ,{field:'isRequired', title:'是否必填', width:90, toolbar:'#optBar'}
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

        //监听工具条
        table.on('toolbar(iTable)', function(obj){
            var checkStatus = table.checkStatus(obj.config.id);
            var data = checkStatus.data;
            if(obj.event === 'add'){
                //新增
                addData();
            } else if(obj.event === 'edit'){
                //编辑
                if(data.length == 1){
                    editData(data[0],data[0].id);
                }else{
                    layer.alert("请选择其中一条数据！",function(){
                        layer.closeAll();
                    });
                }
            } else if(obj.event === 'del'){
                //删除
                delData(data);
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

        //监听下拉框
        form.on('select(bsType)', function (data) {
            if(data.value == "1"){
                $("#timingDiv").css("display", "block");
            }else{
                $("#timingDiv").css("display", "none");
            }
        });

        //渲染接口基本信息
        getInterfaces();

    });
});

//渲染接口基本信息
function getInterfaces(){
    $("#bsMethodShow").val(interfaces.bsMethod);
    $("#bsUrlShow").val(interfaces.bsUrl);

    //渲染
    layui.form.render('select');
}

//新增
function addData(){
    cleanData();//清空弹出框数据
    openData(null,"新增");//打开弹出框
}
//编辑
function editData(obj,id){
    $.ajax({
        type: "GET",
        data: { "id":id },
        url: context+"/interfaces/getRequest",
        success: function (data) {
            if (data.result) {
                $("#id").val(data.data.id==null?'':data.data.id);
                $("#bsName").val(data.data.bsName==null?'':data.data.bsName);
                $("#bsType").val(data.data.bsType==null?'':data.data.bsType);
                $("#isRequired").val(data.data.isRequired==null?'':data.data.isRequired);
                $("#bsValue").val(data.data.bsValue==null?'':data.data.bsValue);
                //渲染
                layui.form.render('select');

                openData(id,"编辑");//打开弹出框
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
    $("#interId").val(interId);
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
    $("#bsName").val("");
    $("#bsType").val("");
    $("#bsDescpt").val("");
    $("#isRequired").val("");
    $("#bsValue").val("");

    //渲染
    layui.form.render('checkbox');
    layui.form.render('select');
}

//新增提交
function doAdd(obj) {
    $.ajax({
        type: "POST",
        data: $("#addForm").serialize(),
        url: context+"/interfaces/addRequest",
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
        url: context+"/interfaces/editRequest",
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
function delData(list) {
    if(list != null && list.length > 0){
        //转换ID数据
        var idsStr = "";
        for(var i = 0; i < list.length; i++){
            if(list[i] && list[i].id){
                if(i == 0){
                    idsStr += "" + list[i].id;
                }else{
                    idsStr += "," + list[i].id;
                }
            }
        }
        //删除
        layer.confirm('您确定要删除吗？', {
            btn: ['确认','返回'] //按钮
        }, function(){
            $.post(context+"/interfaces/deleteRequestAll",{"idsStr":idsStr},function(data){
                if(data.result==true){
                    //回调弹框
                    layer.alert("删除成功！",function(){
                        layer.closeAll();
                        //加载load方法
                        loadAll();
                    });
                }else{
                    layer.alert(data.msg,function(){
                        layer.closeAll();
                    });
                }
            });
        }, function(){
            layer.closeAll();
        });
    }else{
        layer.alert("请选择需要删除的数据！",function(){
            layer.closeAll();
        });
    }
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

