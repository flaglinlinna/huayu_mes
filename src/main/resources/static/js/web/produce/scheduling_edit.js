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
                ,{field:'bsCheckStatus', title:'状态', width:100, templet:'#statusTpl'}
                ,{field:'bsError', title:'错误信息', width:100}
                ,{field:'bsDepartCode', title:'部门', width:60}
                ,{field:'bsProduceTime', title:'日期', width:100}
                ,{field:'bsShift', title:'班次', width:60}
                ,{field:'bsCustomer', title:'客户', width:60}
                ,{field:'bsLine', title:'线别', width:70}
                ,{field:'bsOrderNo', title:'工单号', width:120}
                ,{field:'bsMtrialCode', title:'物料编码', width:120}
                ,{field:'bsMtrialDesc', title:'物料描述', width:120}
                ,{field:'bsProcCode', title:'加工工艺', width:100}
                ,{field:'bsRestNum', title:'工单残', width:80}
                ,{field:'bsPlanNum', title:'计划生产数量', width:110}
                ,{field:'bsPeopleNum', title:'用人量', width:80}
                ,{field:'bsCapacityNum', title:'产能', width:80}
                ,{field:'bsPlanHours', title:'预计工时(H/人)', width:120}
                ,{field:'bsActualNum', title:'实际生产数量', width:110}
                ,{field:'bsActualHours', title:'实际工时(H/人)', width:120}
                ,{field:'bsPlanPrice', title:'计划金额', width:100}
                ,{field:'bsActualPrice', title:'实际生产金额 ', width:120}
                ,{field:'bsRemark', title:'备注', width:120}
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
        form.on('submit(editBtn)', function(data){
            //新增
            doEdit();
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

        getScheduling();

    });
});

//渲染基本信息
function getScheduling(){
    $("#id").val(id);
    $("#bsDepartCode").val(scheduling.bsDepartCode);
    $("#bsShift").val(scheduling.bsShift);
    $("#bsProcCode").val(scheduling.bsProcCode);
    $("#bsOrderType").val(scheduling.bsOrderType);
    $("#bsUniqueOrderNo").val(scheduling.bsUniqueOrderNo);
    $("#bsOrderNo").val(scheduling.bsOrderNo);
    $("#bsStatus").val(scheduling.bsStatus);
    $("#bsMtrialCode").val(scheduling.bsMtrialCode);
    $("#bsMtrialDesc").val(scheduling.bsMtrialDesc);
    $("#bsPlanNum").val(scheduling.bsPlanNum);
    $("#bsLine").val(scheduling.bsLine);
    $("#bsCustomer").val(scheduling.bsCustomer);
    $("#bsProduceTime").val(scheduling.bsProduceTime);
    $("#bsPeopleNum").val(scheduling.bsPeopleNum);
    $("#bsPlanHours").val(scheduling.bsPlanHours);
    $("#bsCapacityNum").val(scheduling.bsCapacityNum);
    $("#bsActualNum").val(scheduling.bsActualNum);

    //渲染
    layui.form.render('select');
}

function doEdit(){
    $.ajax({
        type: "POST",
        data: $("#editForm").serialize(),
        url: context+"/produce/scheduling/edit",
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
