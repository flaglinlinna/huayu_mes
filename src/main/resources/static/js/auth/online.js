/**
 * 用户管理
 */
var pageCurr;
$(function() {
    layui.use('table', function(){
        var table = layui.table
            ,form = layui.form;

        tableIns=table.render({
            elem: '#uesrList'
            ,url:context+'/online/getlist'
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
                    "msg":res.result,
                    "data":res.data.rows,
                    "code": res.status //code值为200表示成功
                }
            },
            cols: [[
                {type:'numbers'}
                ,{field:'id', title:'ID', width:80, unresize: true, sort: true}
                ,{field:'sessionId', title:'sessionId', width:120}
                ,{field:'bsCode', title:'账号', width:'8%'}
                ,{field:'bsName', title:'用户名称', width:'12%'}
                ,{field:'mobile', title:'手机号', width:'15%'}
                ,{field:'host', title: '登录IP', width:'15%'}
                ,{field:'startTimestamp', title: '登录时间', width:'18%'}
                ,{field:'lastAccessTime', title: '最后操作时间', width:'18%'}
                ,{fixed:'right', title:'操作',width:'10%',align:'center', toolbar:'#optBar'}
            ]]
            ,  done: function(res, curr, count){
            	$("[data-field='id']").css('display','none');
            	$("[data-field='sessionId']").css('display','none');
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
        form.on('switch(isJobTpl)', function(obj){
            //console.log(this.value + ' ' + this.name + '：'+ obj.elem.checked, obj.othis);
            setJobUser(obj,this.value,this.name,obj.elem.checked);
        });
        //监听工具条
        table.on('tool(userTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                delUser(data,data.id,data.bsName);
            }
        });
        //监听提交
        form.on('submit(userSubmit)', function(data){
            // TODO 校验
            formSubmit(data);
            return false;
        });

    });
    //搜索框
    layui.use(['form'], function(){
        var form = layui.form ,layer = layui.layer;
        //TODO 数据校验
        //监听搜索框
        form.on('submit(searchSubmit)', function(data){
            //重新加载table
            load(data);
            return false;
        });
    });
});
//设置用户是否离职
function setJobUser(obj,id,nameVersion,checked){
    var name=nameVersion.substring(0,nameVersion.indexOf("_"));
    var version=nameVersion.substring(nameVersion.indexOf("_")+1);
    var isJob=checked ? 0 : 1;
    var userIsJob=checked ? "在职":"离职";
    //是否离职
    layer.confirm('您确定要把用户：'+name+'设置为'+userIsJob+'状态吗？', {
        btn: ['确认','返回'] //按钮
    }, function(){
        $.post(context+"/user/setJobUser",{"id":id,"job":isJob,"version":version},function(data){
            if(isLogin(data)){
                if(data=="ok"){
                    //回调弹框
                    layer.alert("操作成功！",function(){
                        layer.closeAll();
                        //加载load方法
                        load(obj);
                    });
                }else{
                    layer.alert(data,function(){
                        layer.closeAll();
                        //加载load方法
                        load(obj);//自定义
                    });
                }
            }
        });
    }, function(){
        layer.closeAll();
        //加载load方法
        load(obj);
    });
}
//提交表单
function formSubmit(obj){
    var currentUser=$("#currentUser").html();
    if(checkRole()){
        if($("#id").val()==currentUser){
            layer.confirm('更新自己的信息后，需要您重新登录才能生效；您确定要更新么？', {
                btn: ['返回','确认'] //按钮
            },function(){
                layer.closeAll();
            },function() {
                layer.closeAll();//关闭所有弹框
                submitAjax(obj,currentUser);
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
        url: "/user/setUser",
        success: function (data) {
            if(isLogin(data)){
                if (data == "ok") {
                    layer.alert("操作成功",function(){
                        if($("#id").val()==currentUser){
                            //如果是自己，直接重新登录
                            parent.location.reload();
                        }else{
                            layer.closeAll();
                            cleanUser();
                            //$("#id").val("");
                            //加载页面
                            load(obj);
                        }
                    });
                } else {
                    layer.alert(data,function(){
                        layer.closeAll();
                        //加载load方法
                        load(obj);//自定义
                    });
                }
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试",function(){
                layer.closeAll();
                //加载load方法
                load(obj);//自定义
            });
        }
    });
}
function checkRole(){
    //选中的角色
    var array = new Array();
    var roleCheckd=$(".layui-form-checked");
    //获取选中的权限id
    for(var i=0;i<roleCheckd.length;i++){
        array.push($(roleCheckd.get(i)).prev().val());
    }
    //校验是否授权
    var roleIds = array.join(",");
    if(roleIds==null || roleIds==''){
        layer.alert("请您给该用户添加对应的角色！")
        return false;
    }
    $("#roleIds").val(roleIds);
    return true;
}


function delUser(obj,id,name) {
    var currentUser=$("#currentUser").html();
    var sessionId=obj.sessionId;
    if(null!=id){
        if(currentUser==id){
            layer.alert("对不起，您不能执行踢出自己的操作！");
        }else{
            layer.confirm('您确定要踢出'+name+'用户吗？', {
                btn: ['确认','返回'] //按钮
            }, function(){
            	alert(sessionId)
                $.post("/online/delete",{"id":id,"sessionId":sessionId},function(data){
                	if(data.result){
                        //回调弹框
                        layer.alert("踢出用户成功！",function(){
                            layer.closeAll();
                            //加载load方法
                            load(obj);//自定
                            /*//加载load方法
                            location.reload();//自定义
*/                        });
                    }else{
                        layer.alert(data.msg);//弹出错误提示
                    }
                	/*if(isLogin(data)){
                        if(data=="ok"){
                            //回调弹框
                            layer.alert("删除成功！",function(){
                                layer.closeAll();
                                //加载load方法
                                load(obj);//自定义
                            });
                        }else{
                            layer.alert(data,function(){
                                layer.closeAll();
                                //加载load方法
                                load(obj);//自定义
                            });
                        }
                    }*/
                });
            }, function(){
                layer.closeAll();
            });
        }
    }
}


function load(obj){
    //重新加载table
    tableIns.reload({
        where: {
            keyword:obj.field.uname
        }
        , page: {
            curr: pageCurr //从当前页码开始
        }
    });
}

function cleanUser(){
    //$("#id").val("");
    $("#username").val("");
}