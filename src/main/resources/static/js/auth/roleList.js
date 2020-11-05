

/**
 * 角色管理
 */
var pageCurr;
$(function() {
    layui.use(['form' ,'table'], function(){
        var table = layui.table
            ,form = layui.form;

        tableIns1 ="";


        tableIns=table.render({
            elem: '#roleList'
            ,url:context+'/sysRole/getList'
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
                ,{field:'roleCode', title:'编号',align:'center', width:120}
                ,{field:'roleName', title:'名称',align:'center', width:120}
                ,{field:'description', title: '描述',align:'center', minWidth:120}
                ,{field:'userCount', title: '用户数量',align:'center', width:120,
                 templet: '<div><a cursor: pointer; onclick="showUser({{d.id}})">{{ d.userCount }}</a></div>'}
                ,{field:'status', title:'状态',width:95,align:'center',templet:'#statusTpl'}
                ,{field:'lastupdateDate', title: '更新时间', align:'center',width:150}
                ,{field:'createDate', title: '添加时间',align:'center', width:150}
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


        //监听在职操作
        form.on('switch(isStatusTpl)', function(obj){
            setStatusUser(obj,this.value,this.name,obj.elem.checked);
        });
        //监听工具条
        table.on('tool(roleTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                //删除
                delRole(data,data.id,data.roleCode);
            } else if(obj.event === 'edit'){
                //编辑
                getRole(data,data.id);
            } else if(obj.event === 'setPerm'){
                //设置权限
                getPerm(data,data.id);
            }
        });
        //监听提交
        form.on('submit(addSubmit)', function(data){
            if(data.field.id==null || data.field.id==""){
                //新增角色
                addSubmit(data);
            }else{
                editSubmit(data);
            }
            return false;
        });
        form.on('submit(permSubmit)', function(data){
            //设置权限
            permSubmit(data);
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
function setStatusUser(obj,id,name,checked){
    var isStatus=checked ? 0 : 1;
    var userIsStatus=checked ? "正常":"禁用";
    //正常/禁用
    layer.confirm('您确定要把角色：'+name+'设置为'+userIsStatus+'状态吗？', {
        btn: ['确认','返回'] //按钮
    }, function(){
        $.ajax({
            type: "POST",
            data: { "id": id, "status": isStatus},
            url: context+"/sysRole/doStatus",
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
function openRole(id,title){
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
        content:$('#setRole'),
        end:function(){
            cleanRole();
        }
    });
}

//设置权限弹出框
function openPerm(id,title){
    if(id==null || id==""){
        $("#roleId").val("");
    }
    layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['550px','500px'],
        content:$('#setPerm'),
        end:function(){
            // cleanRole();
        }
    });
}

//添加角色
function addRole(){
    //清空弹出框数据
    cleanRole();
    //打开弹出框
    openRole(null,"添加角色");
}
//新增角色提交
function addSubmit(obj) {
    $.ajax({
        type: "POST",
        data: $("#roleForm").serialize(),
        url: context+"/sysRole/add",
        success: function (data) {
            if (data.result) {
                layer.alert("操作成功",function(){
                    layer.closeAll();
                    cleanRole();
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
function getRole(obj,id){
    $.ajax({
        type: "GET",
        data: { "id":id },
        url: context+"/sysRole/getRole",
        success: function (data) {
            if (data.result) {
                $("#id").val(data.data.id==null?'':data.data.id);
                $("#roleCode").val(data.data.roleCode==null?'':data.data.roleCode);
                $("#roleName").val(data.data.roleName==null?'':data.data.roleName);
                $("#description").val(data.data.description==null?'':data.data.description);
                openRole(id,"编辑角色")
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
        data: $("#roleForm").serialize(),
        url: context+"/sysRole/edit",
        success: function (data) {
            if (data.result) {
                layer.alert("操作成功",function(){
                    layer.closeAll();
                    cleanRole();
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
function delRole(obj,id,name) {
    if(id!=null){
        layer.confirm('您确定要删除'+name+'角色吗？', {
            btn: ['确认','返回'] //按钮
        }, function(){
            $.post(context+"/sysRole/delete",{"id":id},function(data){
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

//设置权限
function getPerm(obj,id){
    $.ajax({
        type: "POST",
        data: { "id":id },
        url: context+"/sysRole/getRolePerm",
        success: function (data) {
            if (data.result) {
                $("#roleId").val(id);
                // zTree 的参数配置
                var setting = {
                    check: {
                        enable: true,
                        chkStyle: "checkbox",
                        chkboxType:{ "Y":"ps", "N":"ps"}
                    },
                    data: {
                        simpleData: {
                            enable: true,
                            idKey: "id",
                            pIdKey: "pId",
                            rootPId: null
                        }
                    }
                };
                $.fn.zTree.init($("#treeDemo"), setting, data.data);
                openPerm(id,"设置权限");
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

//设置权限提交
function permSubmit(obj) {
    //获取选中的权限
    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
    var nodes = treeObj.getCheckedNodes(true);
    //选中的复选框ID
    var nodeIds =new Array();
    for (var i = 0; i < nodes.length; i++) {
        nodeIds.push(nodes[i].id);
    }
    //将权限ID转换成以“,”隔开的字符串数组
    var permIds = nodeIds.join(",");
    var roleId = $("#roleId").val();

    $.ajax({
        type: "POST",
        data: { "roleId":roleId, "permIds":permIds },
        url: context+"/sysRole/doRolePerm",
        success: function (data) {
            if (data.result) {
                layer.alert("操作成功",function(){
                    layer.closeAll();
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

//重新加载表格（搜索）
function load(obj){
    //重新加载table
    tableIns.reload({
        where: {
            roleCode:obj.field.codeSearch, roleName:obj.field.nameSearch,
            createdTimeStart:obj.field.createdTimeStart, createdTimeEnd:obj.field.createdTimeEnd,
            status:obj.field.statusSearch
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

function showUser(id){
    tableIns1 = table.render({
        elem : '#UserShowList',
        url : context + 'base/sysUser/getListByRoleId?in='+ id,
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
        },
            {
                field : 'taskNo',
                title : '生产工单',
                width:105,
            },
            {
                field : 'procOrder',
                title : '工序顺序号',
                width:105,
            },
            {
                field : 'procNo',
                title : '工序编码',
            },
            {
                field : 'procName',
                title : '工序名称',
            },
            {
                fixed : 'right',
                title : '操作',
                align : 'center',
                toolbar : '#optBar1',
                width :150,
            } ] ],
        done : function(res, curr, count) {
            // 如果是异步请求数据方式，res即为你接口返回的信息。
            // 如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
            // console.log(res);
            // 得到当前页码
            // console.log(curr);
            // 得到数据总量
            // console.log(count);
            pageCurr1 = curr;
        }
    });

    var index = layer.open({
        type : 1,
        title : "工单排产查看",
        fixed : true,
        resize : true,
        shadeClose : false,
        content : $('#SchemeInfo'),
        end : function() {
            // cleanTaskRoadParm();
            location.reload();
        }
    });
    layer.full(index);
}

//清空新增表单数据
function cleanRole(){
    $("#id").val("");
    $("#roleCode").val("");
    $("#roleName").val("");
    $("#description").val("");
}

