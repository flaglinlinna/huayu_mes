/**
 * 排产导入从表
 */
var pageCurr;
$(function () {
    layui.use(['form', 'table', 'laydate', 'upload','tableSelect'], function(){
        var table = layui.table
            ,form = layui.form
            ,laydate = layui.laydate
            ,upload = layui.upload
            ,tableSelect = layui.tableSelect
            ,tableSelect2 = layui.tableSelect;

        // 监听提交
        form.on('submit(addSubmit)', function(data) {
            if (data.field.id == null || data.field.id == "") {
                // 新增
                addSubmit(data);
            } else {
            }
            return false;
        });
        laydate.render({
            elem: '#prodDate'
        });

        form.on('select(deptId)', function(obj){
            $("#deptName").val(obj.elem[obj.elem.selectedIndex].text);
        });

        tableIns=table.render({
            elem: '#iTable'
            // ,url:context+''
            ,method: 'get' //默认：get请求
            ,where:{ mid:id }
            ,cellMinWidth: 80
            ,page: false,
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
                ,{type:"checkbox", field:'checkColumn'}
                ,{field:'procOrder', title:'工序顺序', width:100, sort:true}
                ,{field:'procNo', title:'工序编号', width:100}
                ,{field:'procName', title:'工序名称', width:150}
                ,{field:'jobAttr', title:'过程属性', width:100, templet:'#statusTpl'}
                ,{field:'empName', title:'作业人员', width:150}
                ,{fixed:'right', title:'操作', align:'center', toolbar:'#optBar'}
                //,{fixed:'right', title:'操作', width:200, align:'center', toolbar:'#optBar'}
            ]]
            ,done: function(res, curr, count){
                pageCurr=curr;
                for(var i = 0; i < res.data.length; i++){
                    if(res.data[i].isCheck == "1"){
                        res.data[i]["LAY_CHECKED"]='true';
                        $('tbody tr[data-index="'+i+'"] td[data-field="checkColumn"] input[type="checkbox"]').prop('checked', true);
                        $('tbody tr[data-index="'+i+'"] td[data-field="checkColumn"] input[type="checkbox"]').next().addClass('layui-form-checked');
                        // $('tbody tr[data-index="'+i+'"] td[data-field="checkColumn"] div.layui-form-checkbox').addClass('layui-form-checked');
                        // $('tbody tr[data-index="'+i+'"]  div.layui-form-checkbox').addClass('layui-form-checked');
                    }
                }
            }
        });
        //监听工具条-工艺维护
        table.on('tool(iTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'edit1'){
                //编辑
                getProcess(data,data.id);
            }
        });
        form.on('submit(saveProcess)', function(data){
            //保存-工艺维护
            saveProcess(table);
            return false;
        });

        //getScheduling();
        getDeptSelect();

    });
});

function getDeptSelect() {
    $.ajax({
        type: "post",
        data: {},
        url: context+"/produce/schedulingMain/getDeptSelect",
        success: function (res) {
            // console.log(res);
            $("#deptId").empty();
            if (res.result) {
                console.log(res.data.rows);
                var itemList = res.data.rows;
                for(var i = 0; i < itemList.length; i++){
                    if(i==0){
                        $("#deptId").append("<option value=''>请点击选择</option>");
                    }
                    $("#deptId").append( '<option value="'+itemList[i].ID+'">'+itemList[i].ORG_NAME+'</option>');
                }
                layui.form.render('select');
            } else {
                layer.alert(res.msg,function(index){
                });
            }
        },
        error: function () {
        }
    });
}

//新增排产导入的保存
function addSubmit(obj) {
    console.log(obj.field);
    if(!obj.field.bsStatus){
        obj.field.bsStatus = 0;
    }
    CoreUtil.sendAjax("/produce/schedulingMain/add", JSON.stringify(obj.field),
        function(data) {
            if (data.result) {
                $("#id").val(data.data.id);
                layer.alert("操作成功", function() {
                    layer.closeAll();
                });
            } else {
                layer.alert(data.msg);
            }
        }, "POST", false, function(res) {
            layer.alert(res.msg);
        });
}

//重新加载表格-工艺维护（全部）
function loadAll1(){
    //重新加载table
    tableIns.reload({
        page: {
            curr: pageCurr //从当前页码开始
        }
    });
}

