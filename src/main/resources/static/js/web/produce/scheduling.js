/**
 * 接口管理
 */
var pageCurr;
$(function () {
    layui.use(['form', 'table', 'laydate'], function(){
        var table = layui.table
            ,form = layui.form
            ,laydate = layui.laydate;

        tableIns=table.render({
            elem: '#iList'
            ,url:context+'/produce/scheduling/getList'
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
                // ,{field:'id', title:'ID', width:80, unresize:true, sort:true}
                ,{field:'departName', title:'部门', width:60, templet:'<span>{{d.department ? d.department.bsName : ""}}<span>'}
                ,{field:'bsProduceTime', title:'日期', width:100}
                ,{field:'bsShift', title:'班次', width:60}
                ,{field:'bsCustomer', title:'客户', width:60}
                ,{field:'bsLine', title:'线别', width:70}
                ,{field:'bsOrderNo', title:'工单号', width:120}
                ,{field:'mtrialCode', title:'物料编码', width:120, templet:'<span>{{d.mtrial ? d.mtrial.bsCode : ""}}<span>'}
                ,{field:'mtrialDesc', title:'物料描述', width:120, templet:'<span>{{d.mtrial ? d.mtrial.bsName : ""}}<span>'}
                ,{field:'procName', title:'加工工艺', width:100, templet:'<span>{{d.woProc ? d.woProc.bsName : ""}}<span>'}
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

        //监听工具条
        table.on('tool(iTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                //删除
                doDel(data.id);
            } else if(obj.event === 'edit'){
                //编辑
                //window.location = context + "/produce/scheduling/toSchedulingEdit?id=" + data.id;
            }
        });

        //监听搜索框
        form.on('submit(search)', function(data){
            //重新加载table
            load(data);
            return false;
        });


    });
});

//删除
function doDel(id) {
    if(id!=null){
        layer.confirm('您确定要删除吗？', {
            btn: ['确认','返回'] //按钮
        }, function(){
            $.ajax({
                type: "POST",
                data: { "id": id },
                url: context+"/produce/scheduling/delete",
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

//重新加载表格（搜索）
function loadAll(){
    //重新加载table
    tableIns.reload({
        page: {
            curr: pageCurr //从当前页码开始
        }
    });
}