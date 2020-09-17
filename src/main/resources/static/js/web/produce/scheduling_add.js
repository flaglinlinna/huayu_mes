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
            ,url:context+'/scheduling/getList'
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
                ,{field:'bsCustomer', title:'客户', width:120}
                ,{field:'bsLine', title:'线别', width:120}
                ,{field:'bsOrderNo', title:'工单号', width:120}
                ,{field:'bsMtrialId', title:'物料编码', width:120}
                ,{field:'bsMtrialId', title:'物料描述', width:120}
                ,{field:'bsProcId', title:'加工工艺', width:120}
                ,{field:'bsRestNum', title:'工单残', width:120}
                ,{field:'bsPlanNum', title:'计划生产数量', width:120}
                ,{field:'bsPeopleNum', title:'用人量', width:120}
                ,{field:'bsCapacityNum', title:'产能', width:120}
                ,{field:'bsPlanHours', title:'预计工时(H/人)', width:120}
                ,{field:'bsActualNum', title:'实际生产数量', width:120}
                ,{field:'bsActualHours', title:'实际工时(H/人)', width:120}
                ,{field:'bsPlanPrice', title:'计划金额', width:120}
                ,{field:'bsActualPrice', title:'实际生产金额 ', width:120}
                ,{field:'bsRemark', title:'备注', width:120}
                // ,{field:'bsStatus', title:'状态',width:95,align:'center',templet:'#statusTpl'}
                // ,{field:'modifiedTime', title: '更新时间', width:150}
                // ,{field:'createdTime', title: '添加时间', width:150}
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

        //监听搜索框
        form.on('submit(search)', function(data){
            //重新加载table
            load(data);
            return false;
        });
        form.on('submit(exportBtn)', function(data){
            //导出模板
            location.href = context + "/scheduling/getExcel";
            return false;
        });


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