/**
 * 接口管理
 */
var pageCurr;
$(function () {
    layui.use(['form', 'table', 'laydate', 'upload'], function(){
        var table = layui.table
            ,form = layui.form
            ,laydate = layui.laydate
            ,upload = layui.upload;

        tableIns=table.render({
            elem: '#iList'
            ,url:context+'/produce/scheduling/getTempList'
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
                ,{field:'checkStatus', title:'状态', width:100, templet:'#statusTpl'}
                ,{field:'errorInfo', title:'错误信息', width:100}
                ,{field:'groupNo', title:'组合', width:60}
                ,{field:'custName', title:'客户', width:60}
                ,{field:'linerName', title:'线别', width:70}
                ,{field:'prodDate', title:'日期', width:100}
                ,{field:'deptName', title:'组装部', width:80}
                ,{field:'classNo', title:'班次', width:60}
                ,{field:'prodNo', title:'工单号', width:120}
                ,{field:'itemNo', title:'物料编码', width:120}
                ,{field:'itemName', title:'物料描述', width:150}
                ,{field:'qtyPlan', title:'计划生产数量', width:110}
                //,{fixed:'right', title:'操作', width:200, align:'center', toolbar:'#optBar'}
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

        //监听搜索框
        form.on('submit(search)', function(data){
            //重新加载table
            load(data);
            return false;
        });
        form.on('submit(addBtn)', function(data){
            //新增
            doAdd();
            return false;
        });
        form.on('submit(exportBtn)', function(data){
            //导出模板
            location.href = context + "/produce/scheduling/getExcel";
            return false;
        });
        form.on('submit(importBtn)', function(data){
            //导入弹出框
            openUpload('导入');
            return false;
        });

        //导入
        upload.render({
            elem: '#upload'
            ,url: context + '/produce/scheduling/doExcel'
            ,accept: 'file' //普通文件
            ,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
                layer.load(); //上传loading
            }
            ,done: function(res,index, upload){
                layer.closeAll('loading'); //关闭loading
                if(res.result){
                    loadAll();//重新加载表格
                    layer.alert(res.msg,function (index) {
                        layer.close(index);
                    })
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

//导入弹出框
function openUpload(title){
    //deleteTempAll();
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

//重新加载表格（搜索）
function loadAll(){
    //重新加载table
    tableIns.reload({
        page: {
            curr: pageCurr //从当前页码开始
        }
    });
};

//确认保存临时数据
$("#save").click(function () {
    $.ajax({
        type: "POST",
        data: {},
        url: context+"/produce/scheduling/confirmTemp",
        success: function (data) {
            if (data.result) {
                layer.alert(data.msg,function(){
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

//根据当前登录用户删除临时表所有数据
function deleteTempAll(){
    $.ajax({
        url:context + '/produce/scheduling/deleteTempAll',
        type:"POST",
        data:{},
        success:function(data){
            //重新获取临时列表数据
            loadAll();
        },
        error:function(e){
            layer.alert("操作请求错误，请您稍后再试",function(){
            });
        }
    });
}

function doAdd(){
    $.ajax({
        type: "POST",
        data: $("#addForm").serialize(),
        url: context+"/produce/scheduling/add",
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
