/**
 * 用户管理
 */
var pageCurr;
var tableSelect1;
$(function() {
    layui.use('table', function(){
        var table = layui.table
            ,form = layui.form;

        tableIns=table.render({
            elem: '#uesrList'
            ,url:context+'/sysUser/getList'
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
                // ,{field:'id', title:'ID', width:80, unresize: true, sort: true}
                ,{field:'userCode', title:'账号', width:120}
                ,{field:'mobile', title:'手机号', width:120}
                //,{field:'realName', title:'真实名称', width:100}
                ,{field:'userName', title:'名称', width:100}
                ,{field:'email', title: '邮箱', width:180}
                ,{field:'sex', title: '性别', width:60}
                ,{field:'status', title:'状态',width:95,align:'center',templet:'#statusTpl'}
                ,{field:'registerSrc', title: '注册来源', minWidth:100}
                ,{field:'createDate', title: '添加时间', width:150}
                ,{fixed:'right', title:'操作',width:200,align:'center', toolbar:'#optBar'}
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

        //监听在职操作
        form.on('switch(isStatusTpl)', function(obj){
            setStatusUser(obj,this.value,this.name,obj.elem.checked);
        });
        //监听工具条
        table.on('tool(userTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                //删除
                delUser(data,data.id,data.userName);
            } else if(obj.event === 'edit'){
                //编辑
                getUserAndRoles(data,data.id);
            } else if(obj.event === 'setPass'){
                //修改密码
                setPassword(data,data.id,data.userName);
            }
        });
        //监听提交
        form.on('submit(userSubmit)', function(data){
            // 新增或修改
            formSubmit(data);
            return false;
        });
        form.on('submit(passwordSubmit)', function(data){
            // 修改密码
            doSetPass(data);
            return false;
        });
    });
    //搜索框
    layui.use(['form','laydate','tableSelect'], function(){
        var form = layui.form ,layer = layui.layer
            ,laydate = layui.laydate;
        tableSelect1 = layer.tableSelect;
        // //日期
        // laydate.render({
        //     elem: '#createdTimeStart'
        // });
        // laydate.render({
        //     elem: '#createdTimeEnd'
        // });
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
    layer.confirm('您确定要把用户：'+name+'设置为'+userIsStatus+'状态吗？', {
        btn: ['确认','返回'] //按钮
    }, function(){
        $.ajax({
            type: "POST",
            data: { "id": id, "status": isStatus},
            url: context+"/sysUser/doStatus",
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

//新增或编辑提交表单
function formSubmit(obj){
    var currentUser=$("#currentUser").html();
    if(checkRole()){
        if($("#id").val()==currentUser){
            layer.confirm('更新自己的信息后，需要您重新登录才能生效；您确定要更新么？', {
                btn: ['确认','返回'] //按钮
            },function(){
                layer.closeAll();//关闭所有弹框
                submitAjax(obj,currentUser);
            },function() {
                layer.closeAll();//关闭所有弹框
            });
        }else{
            submitAjax(obj,currentUser);
        }
    }
}
function submitAjax(obj,currentUser){
    var a = $("#roleIds").val();
    $.ajax({
        type: "POST",
        data: $("#userForm").serialize(),
        url: context+"/sysUser/add",
        success: function (data) {
            if(isLogin(data)){
                if (data.result == true) {
                    layer.alert("操作成功",function(){
                        if($("#id").val()==currentUser){
                            //如果是自己，直接重新登录
                            parent.location.reload();
                        }else{
                            layer.closeAll();
                            cleanUser();
                            //加载页面
                            loadAll();
                        }
                    });
                } else {
                    layer.alert(data.msg,function(){
                        layer.closeAll();
                    });
                }
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试",function(){
                layer.closeAll();
            });
        }
    });
}
//检验是否选择角色
function checkRole(){
    //选中的角色
    var array = new Array();
    var arrayName = new Array();
    var roleCheckd=$(".layui-form-checked");
    //获取选中的权限id
    for(var i=0;i<roleCheckd.length;i++){
        array.push($(roleCheckd.get(i)).prev().val());
        arrayName.push($(roleCheckd.get(i)).prev().attr("title"));
    }
    //校验是否授权
    var roleIds = array.join(",");
    var roleNames = arrayName.join(",")
    if(roleIds==null || roleIds==''){
        layer.alert("请您给该用户添加对应的角色！")
        return false;
    }
    $("#roleIds").val(roleIds);
    $("#roleNames").val(roleNames);
    return true;
}

//新增编辑弹出框
function openUser(id,title){
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
        content:$('#setUser'),
        end:function(){
            cleanUser();
        }
    });
}

//修改密码弹出框
function openPassword(id,title){
    if(id==null || id==""){
        return false;
    }
    layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['550px'],
        content:$('#setPassword'),
        end:function(){
            cleanPassword();
        }
    });
}

//修改密码
function setPassword(obj,id,name) {
    if(obj.status){
        layer.alert("该用户已经禁用，不可进行密码修改；</br>  如需修改密码，请设置为<font style='font-weight:bold;' color='green'>正常</font>状态。");
    }else{
        $("#userId").val(id);
        openPassword(id,"修改密码");
    }
}
function doSetPass(obj){
    var currentUser=$("#currentUser").html();
    if($("#id").val()==currentUser){
        layer.confirm('更新自己的信息后，需要您重新登录才能生效；您确定要更新么？', {
            btn: ['返回','确认'] //按钮
        },function(){
            layer.closeAll();
        },function() {
            layer.closeAll();//关闭所有弹框
            doSetPassAjax(obj,currentUser);
        });
    }else{
        doSetPassAjax(obj,currentUser);
    }
}
function doSetPassAjax(obj,currentUser){
    $.ajax({
        type: "POST",
        data: { "id":$("#userId").val(), "password":$("#password").val(), "rePassword":$("#rePassword").val() },
        url: context+"/sysUser/doSetPassword",
        success: function (data) {
            if (data.result == true) {
                layer.alert("操作成功",function(){
                    if($("#id").val()==currentUser){
                        //如果是自己，直接重新登录
                        parent.location.reload();
                    }else{
                        layer.closeAll();
                        cleanPassword();
                        //加载页面
                        loadAll();
                    }
                });
            } else {
                layer.alert(data.msg,function(){
                    layer.closeAll();
                });
            }
        }
    });
}

//新增用户-获取用户信息
function addUser(){

    // tableSelect=tableSelect1.render({
    //     elem : '#num',
    //     searchKey : 'keyword',
    //     checkedKey : 'id',
    //     searchPlaceholder : '试着搜索',
    //     table : {
    //         url:  context +'/sysRole/getRoles',
    //         method : 'get',
    //         cols : [ [
    //             { type: 'radio' },//多选  radio
    //             , {
    //                 field : 'id',
    //                 title : 'id',
    //                 width : 0,hide:true
    //             }, {
    //                 field : 'TASK_NO',
    //                 title : '制令单号',
    //                 width : 180,sort: true
    //             }, {
    //                 field : 'ITEM_NO',
    //                 title : '物料编码',
    //                 width : 150,sort: true
    //             },{
    //                 field : 'ITEM_NAME',
    //                 title : '物料描述',
    //                 width : 240,sort: true
    //             }, {
    //                 field : 'LINER_NAME',
    //                 title : '组长',
    //                 width : 100,sort: true
    //             },{
    //                 field : 'QTY_PLAN',
    //                 title : '制单数量',
    //                 width : 80,sort: true
    //             },{
    //                 field : 'OTPT_QTY',
    //                 title : '产出数量',
    //                 width : 80,sort: true
    //             },{
    //                 field : 'ORDER_RATE',
    //                 title : '达成率',
    //                 width : 80,sort: true
    //             }    ] ],
    //         page : false,
    //         request : {
    //             pageName : 'page' // 页码的参数名称，默认：page
    //             ,
    //             limitName : 'rows' // 每页数据量的参数名，默认：limit
    //         },
    //         parseData : function(res) {
    //             if(res.result){
    //                 // 可进行数据操作
    //                 return {
    //                     "count" : 0,
    //                     "msg" : res.msg,
    //                     "data" : res.data,
    //                     "code" : res.status
    //                     // code值为200表示成功
    //                 }
    //             }
    //
    //         },
    //     },
    //     done : function(elem, data) {
    //         //选择完后的回调，包含2个返回值 elem:返回之前input对象；data:表格返回的选中的数据 []
    //         var da=data.data;
    //         //console.log(da[0])
    //         form.val("itemFrom", {
    //             "num":da[0].TASK_NO,
    //             "mtrcode" : da[0].ITEM_NO,
    //             "mtrdescr" : da[0].ITEM_NAME,
    //             "qty" : da[0].QTY_PLAN,
    //             "linecode" : da[0].LINER_NAME,
    //             "inqty" : da[0].OTPT_QTY,
    //             "rate" : da[0].ORDER_RATE,
    //         });
    //         form.render();// 重新渲染
    //
    //         getDetailByTask(da[0].TASK_NO);
    //     }
    // });

    $.get(context+"/sysRole/getRoles",function(data){
        if(isLogin(data)){
            if(data.result==true && data.data!=null){
            	cleanUser();
//                $("#roleIds").val("");
//                $("#id").val("");
//                $("#version").val("");
//                $("#userCode").val("");
//                $("#userName").val("");
//                $("#mobile").val("");
//                $("#email").val("");
//                $("#sex").val("");
//                //显示角色数据
//                $("#roleDiv").empty();
                $.each(data.data, function (index, item) {
                    // <input type="checkbox" name="roleId" title="发呆" lay-skin="primary"/>
                    var roleInput=$("<input type='checkbox' name='roleId' value="+item.id+" title="+item.roleName+" lay-skin='primary'/>");
                    //未选中
                    /*<div class="layui-unselect layui-form-checkbox" lay-skin="primary">
                        <span>发呆</span><i class="layui-icon">&#xe626;</i>
                        </div>*/
                    //选中
                    // <div class="layui-unselect layui-form-checkbox layui-form-checked" lay-skin="primary">
                    // <span>写作</span><i class="layui-icon">&#xe627;</i></div>
                    var div=$("<div class='layui-unselect layui-form-checkbox' lay-skin='primary'>" +
                        "<span>"+item.roleName+"</span><i class='layui-icon'>&#xe626;</i>" +
                        "</div>");
                    $("#roleDiv").append(roleInput).append(div);
                })
                openUser(null,"开通用户");
                //重新渲染下form表单 否则复选框无效
                layui.form.render('checkbox');
            }else{
                //弹出错误提示
                layer.alert("获取角色数据有误，请您稍后再试",function () {
                    layer.closeAll();
                });
            }
        }
    });
}

//编辑用户-获取用户信息
function getUserAndRoles(obj,id) {
    //如果已经禁用，提醒不可编辑
    if(obj.bsStatus){
        layer.alert("该用户已经禁用，不可进行编辑；</br>  如需编辑，请设置为<font style='font-weight:bold;' color='green'>正常</font>状态。");
    }else{
        //回显数据
        $.get(context+"/sysUser/getUserAndRoles",{"id":id},function(data){
            if(isLogin(data)){
                if(data.result==true && data.data.user!=null){
                    var existRole = new Array();
                    // var existRole='';
                    if(data.data.user.userRoles !=null ){
                        $.each(data.data.user.userRoles, function (index, item) {
                            existRole.push(item.roleId);
                            // existRole+=item.roleId+',';
                        });
                    }
                    $("#roleIds").val("");
                    $("#id").val(data.data.user.id==null?'':data.data.user.id);
                    $("#userCode").val(data.data.user.userCode==null?'':data.data.user.userCode);
                    $("#userName").val(data.data.user.userName==null?'':data.data.user.userName);
                    $("#realName").val(data.data.user.realName==null?'':data.data.user.realName);
                    $("#mobile").val(data.data.user.mobile==null?'':data.data.user.mobile);
                    $("#email").val(data.data.user.email==null?'':data.data.user.email);
                    $("#sex").val(data.data.user.sex==null?'':data.data.user.sex);
                    //显示角色数据
                    $("#roleDiv").empty();
                    $.each(data.data.roles, function (index, item) {
                        var roleInput=$("<input type='checkbox' name='roleId' value="+item.id+" title="+item.roleName+" lay-skin='primary'/>");
                        var div=$("<div class='layui-unselect layui-form-checkbox' lay-skin='primary'>" +
                            "<span>"+item.userName+"</span><i class='layui-icon'>&#xe626;</i>" +
                            "</div>");
                        if(existRole.length > 0 && existRole.indexOf(item.id)>=0){
                            roleInput=$("<input type='checkbox' name='roleId' value="+item.id+" title="+item.roleName+" lay-skin='primary' checked='checked'/>");
                            div=$("<div class='layui-unselect layui-form-checkbox  layui-form-checked' lay-skin='primary'>" +
                                "<span>"+item.roleName+"</span><i class='layui-icon'>&#xe627;</i>" +
                                "</div>");
                        }
                        // if(existRole!='' && existRole.indexOf(item.id)>=0){
                        //      roleInput=$("<input type='checkbox' name='roleId' value="+item.id+" title="+item.userName+" lay-skin='primary' checked='checked'/>");
                        //      div=$("<div class='layui-unselect layui-form-checkbox  layui-form-checked' lay-skin='primary'>" +
                        //         "<span>"+item.userName+"</span><i class='layui-icon'>&#xe627;</i>" +
                        //         "</div>");
                        // }
                        $("#roleDiv").append(roleInput).append(div);
                    });
                    openUser(id,"设置用户");
                    //重新渲染下form表单中的复选框 否则复选框选中效果无效
                    layui.form.render('checkbox');
                    layui.form.render('select');
                }else{
                    //弹出错误提示
                    layer.alert(data.msg,function () {
                        layer.closeAll();
                    });
                }
            }
        });
    }
}

//删除用户
function delUser(obj,id,name) {
    var currentUser=$("#currentUser").html();
    if(null!=id){
        if(currentUser==id){
            layer.alert("对不起，您不能执行删除自己的操作！");
        }else{
            layer.confirm('您确定要删除'+name+'用户吗？', {
                btn: ['确认','返回'] //按钮
            }, function(){
                $.post(context+"/sysUser/delete",{"id":id},function(data){
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
}

//重新加载表格（搜索）
function load(obj){
    //重新加载table
    tableIns.reload({
        where: {
            userCode:obj.field.codeSearch, userName:obj.field.nameSearch,
            mobile:obj.field.mobileSearch, bsStatus:obj.field.statusSearch,
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
function cleanUser(){
//    $("#id").val("");
//    $("roleIds").val("");
//    $("roleNames").val("");
//    $("#userCode").val("");
//    $("#userName").val("");
//    $("#realName").val("");
//    $("#mobile").val("");
//    $("#email").val("");
//    $("#sex").val("");
	 $("#roleDiv").empty();
	$('#userForm')[0].reset();
	layui.form.render();// 必须写
}

//清空修改密码表单数据
function cleanPassword(){
    $("#userId").val("");
    $("#password").val("");
    $("#rePassword").val("");
}
