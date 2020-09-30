/**
 * 接口管理
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
            ,url:context+'/produce/scheduling/getProcessLst'
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
                ,{field:'', title:'工序顺序', width:100}
                ,{field:'', title:'工序编号', width:100}
                ,{field:'', title:'工序名称', width:100}
                ,{field:'', title:'过程属性', width:100}
                ,{field:'', title:'作业人员', width:100}
                ,{fixed:'right', title:'操作', align:'center', toolbar:'#optBar'}
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

        tableIns2=table.render({
            elem: '#iList2'
            ,url:context+'/produce/scheduling/getProcessLst'
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
                ,{field:'', title:'物料编号', width:100}
                ,{field:'', title:'物料描述', width:100}
                ,{field:'', title:'用量', width:100}
                ,{field:'', title:'作业员', width:100}
                ,{fixed:'right', title:'操作', align:'center', toolbar:'#optBar'}
            ]]
            ,done: function(res, curr, count){
                //如果是异步请求数据方式，res即为你接口返回的信息。
                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                //console.log(res);
                //得到当前页码
                //console.log(curr);
                //得到数据总量
                //console.log(count);
                pageCurr2=curr;
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
            //doEdit();
            return false;
        });

        getScheduling();

    });
});

//渲染基本信息
function getScheduling(){
    $("#id").val(id);
    $("#taskNo").val(scheduling.taskNo);
    $("#prodNo").val(scheduling.prodNo);
    $("#produceState").val(scheduling.produceState);
    $("#itemNo").val(scheduling.itemNo);
    $("#itemName").val(scheduling.itemName);
    $("#groupNo").val(scheduling.groupNo);
    $("#custName").val(scheduling.custName);
    $("#linerName").val(scheduling.linerName);
    $("#prodDate").val(scheduling.prodDate);
    $("#deptName").val(scheduling.deptName);
    $("#classNo").val(scheduling.classNo);
    $("#qtyPlan").val(scheduling.qtyPlan);

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
