/**
 * 排产导入
 */
var pageCurr;
var pageCurr2;
$(function () {
    layui.use(['form', 'table', 'laydate', 'upload'], function(){
        var table = layui.table
            ,form = layui.form
            ,laydate = layui.laydate
            ,upload = layui.upload;

        tableIns=table.render({
            elem: '#iList'
            ,url:context+'/produce/scheduling_temp/getList'
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
                ,{type:'checkbox'}
                // ,{field:'id', title:'ID', width:80, unresize:true, sort:true}
                // ,{field:'departName', title:'部门', width:60, templet:'<span>{{d.department ? d.department.bsName : ""}}<span>'}
                ,{field:'CHECK_STATUS', title:'校验结果', width:100, templet:'#statusTpl'}
                ,{field:'ENABLED', title:'生效状态', width:80, templet:'#enabledTpl'}
                ,{field:'ERROR_INFO', title:'错误信息', width:140}
                ,{field:'PROD_NO', title:'工单号', width:120}
                ,{field:'TASK_NO', title:'生产制令单', width:100,
                templet: '<div><a cursor: pointer; onclick="toSchedulingEdit({{d.TASK_ID}})">{{ d.TASK_NO }}</a></div>'}
                ,{field:'GROUP_NO', title:'组合', width:70}
                ,{field:'DEPT_NAME', title:'部门', width:70}
                ,{field:'CUST_NAME', title:'客户', width:80}
                ,{field:'LINER_NAME', title:'组长', width:70}
                ,{field:'ITEM_NO', title:'物料编码', width:150}
                ,{field:'ITEM_NAME', title:'物料描述', width:150}
                ,{field:'PROD_DATE', title:'生产日期', width:120}
                ,{field:'CLASS_NO', title:'班次', width:60}
                ,{field:'QTY_PLAN', title:'计划数量', width:100}
                ,{fixed:'right', title:'操作', width:80, align:'center', toolbar:'#optBar'}
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
        table.on('tool(iTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'edit'){
                //编辑
                //window.location = context + "/produce/scheduling/toSchedulingEdit?id=" + data.id;
                // var a = document.createElement('a');
                // a.setAttribute('lay-href', context + "/produce/scheduling/toSchedulingEdit?id=" + data.id);
                // a.setAttribute('lay-text', '排产编辑');
                // a.setAttribute('id', 'js_a');
                // if(document.getElementById('js_a')) {//防止反复添加
                //     document.body.removeChild(document.getElementById('js_a'));
                // }
                // document.body.appendChild(a);//创建
                // a.click();//点击
                return false;
            }
        });

        //监听搜索框
        form.on('submit(search)', function(data){
            //重新加载table
            load(data);
            return false;
        });
        //监听校验按钮
        form.on('submit(checkBtn)', function(data){
            //校验
            doCheckProc(table);
            return false;
        });
        //监听生效按钮
        form.on('submit(effectBtn)', function(data){
            //生效
            doEffect(table);
            return false;
        });
        //监听删除按钮
        form.on('submit(delBtn)', function(data){
            doDel(table);
            return false;
        });

        //导入
        upload.render({
            elem: '#uploadBtn'
            ,url: context + '/produce/scheduling_temp/doExcel'
            ,accept: 'file' //普通文件
            ,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
                layer.load(); //上传loading
            }
            ,done: function(res,index, upload){
                layer.closeAll('loading'); //关闭loading
                console.log(res);
                var ids = "";
                for(var i = 0;i<res.data.length;i++){
                    ids += res.data[i].id +",";
                }
                if(res.result){
                    $.ajax({
                        type: "POST",
                        data: {},
                        url: context+"/produce/scheduling_temp/doCheckProc?ids="+ids,
                        success: function (res) {
                            if (res.result) {
                                loadAll();//重新加载表格
                                layer.alert("导入成功！",function(index){
                                    layer.close(index);
                                });
                            } else {
                                layer.alert(res.msg,function(index){
                                    layer.close(index);
                                });
                            }
                        },
                        error: function () {
                            layer.alert("操作请求错误，请您稍后再试",function(){
                            });
                        }
                    });
                }else{
                    layer.alert(res.msg,function (index) {
                        layer.close(index);
                    });
                }
                $(window).resize();
            }
            ,error: function(index, upload){
                layer.closeAll('loading'); //关闭loading
            }
        });

    });
});

function toSchedulingEdit(d){
    var a = document.createElement('a');
    a.setAttribute('lay-href', context + "/produce/scheduling/toSchedulingEdit?id=" + d);
    a.setAttribute('lay-text', '排产编辑');
    a.setAttribute('id', 'js_a');
    if(document.getElementById('js_a')) {//防止反复添加
        document.body.removeChild(document.getElementById('js_a'));
    }
    document.body.appendChild(a);//创建
    a.click();//点击
    return false;
}

//校验
function doCheckProc(table){
    var tempIds = "";
    var checkStatus = table.checkStatus("iList");
    for(var i = 0; i < checkStatus.data.length; i++){
        if(i == 0){
            tempIds += checkStatus.data[i].ID;
        }else{
            tempIds += "," + checkStatus.data[i].ID;
        }
    }

    $.ajax({
        type: "POST",
        data: { "ids":tempIds },
        url: context+"/produce/scheduling_temp/doCheckProc",
        success: function (res) {
            if (res.result) {
                layer.alert("校验成功！",function(index){
                    loadAll();//重新加载表格
                    layer.close(index);
                });
            } else {
                layer.alert(res.msg,function(index){
                    layer.close(index);
                });
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试",function(){
            });
        }
    });
}

//删除
function doDel(table) {
    var tempIds = "";
    var checkStatus = table.checkStatus("iList");
    for(var i = 0; i < checkStatus.data.length; i++){
        if(i == 0){
            tempIds += checkStatus.data[i].ID;
        }else{
            tempIds += "," + checkStatus.data[i].ID;
        }
    }

    if(tempIds.length > 0){
        layer.confirm('您确定要删除吗？', {
            btn: ['确认','返回'] //按钮
        }, function(){
            $.ajax({
                type: "POST",
                data: { "ids": tempIds },
                url: context+"/produce/scheduling_temp/delete",
                success: function (data) {
                    if (data.result) {
                        layer.alert("删除成功",function(){
                            layer.closeAll();
                            loadAll();
                        });
                    } else {
                        layer.alert(data.msg,function(index){
                            layer.close(index);
                        });
                    }
                },
                error: function () {
                    layer.alert("操作请求错误，请您稍后再试",function(){
                    });
                }
            });
        }, function(){
            layer.closeAll();
        });
    }
}

//生效
function doEffect(table){
    var tempIds = "";
    var checkStatus = table.checkStatus("iList");
    for(var i = 0; i < checkStatus.data.length; i++){
        if(i == 0){
            tempIds += checkStatus.data[i].ID;
        }else{
            tempIds += "," + checkStatus.data[i].ID;
        }
    }

    $.ajax({
        type: "POST",
        data: { "ids":tempIds },
        url: context+"/produce/scheduling_temp/doEffect",
        success: function (res) {
            if (res.result) {
                layer.alert("校验成功！",function(index){
                    loadAll();//重新加载表格
                    layer.close(index);
                });
            } else {
                layer.alert(res.msg,function(index){
                    layer.close(index);
                });
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试",function(){
            });
        }
    });
}

//导出
function exportExcel(){
    //导出模板
    //location.href = context + "/produce/scheduling/getExcel";
	location.href = "../../excelFile/排产导入模板.xlsx";//从文件夹内直接提取
    return false;
}

//导入弹出框
function openUpload(title){
    var index = layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: false,//是否点击遮罩关闭
        area: ['1000px'],
        content:$('#uploadDiv'),
        macmin:true,//弹出框全屏
        end:function(){

        }
    });
    layer.full(index);//弹出框全屏
}

//确认保存临时数据
$("#save").click(function () {
    $.ajax({
        type: "POST",
        data: {},
        url: context+"/produce/scheduling/confirmTemp",
        success: function (data) {
            if (data.result) {
                layer.alert(data.msg,function(){
                    loadAll();
                    layer.closeAll();
                });
            } else {
                layer.alert(data.msg,function(index){
                    layer.close(index);
                });
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试",function(){
            });
        }
    });
});

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

