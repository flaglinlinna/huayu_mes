/**
 * 排产导入主表
 */
var pageCurr;
var pageCurr2;
$(function () {
    layui.use(['form', 'table', 'laydate', 'upload'], function(){
        var table = layui.table
            ,form = layui.form
            ,laydate = layui.laydate
            ,upload = layui.upload;

        laydate.render({
            elem: '#prodDate',
            trigger: 'click'
        });

        // getDeptSelect();

        // 监听提交
        form.on('submit(addSubmit)', function(data) {
            console.log(data);
            if (data.field.id == null || data.field.id == "") {
                // 新增
                addSubmit(data);
            } else {
                editSubmit(data);
            }
            return false;
        });

        tableIns=table.render({
            elem: '#iList'
            ,url:context+'/produce/schedulingMain/getList'
            ,method: 'get' //默认：get请求
            ,cellMinWidth: 80
            ,page: true
            , toolbar: '#toolbar' //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
            ,height:'full-130'//固定表头&full-查询框高度
            ,even:true,//条纹样式
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
                ,{field:'ID_NO', title:'表单号', width:120}
                ,{field:'DEPT_NAME', title:'部门名称', width:120}
                ,{field:'PROD_DATE', title:'生产日期', width:140}
                ,{field:'CLASS_NAME', title:'班次名称', width:140}
                ,{field:'FENABLE', title:'是否生效', width:120,align:'center',templet:'#statusTp1'}
                ,{field:'FENABLE_BY', title:'生效人',width:120, widstatusth:140}
                ,{field:'FENABLE_DATE', title:'生效时间', width:150}
                ,{field:'CREATE_DATE', title:'创建时间', width:150}
                ,{fixed:'right', title:'操作', width:120, align:'center', toolbar:'#optBar'}
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

        table.on('rowDouble(iTable)', function(obj){
            //标注选中样式
            obj.tr.addClass('layui-table-click').siblings().removeClass('layui-table-click');
            var a = document.createElement('a');
            a.setAttribute('lay-href', context + "/produce/schedulingMain/toSchedulingMainAdd?id=" + obj.data.ID);
            a.setAttribute('lay-text', '排产导入编辑');
            a.setAttribute('id', 'js_a');
            if(document.getElementById('js_a')) {//防止反复添加
                document.body.removeChild(document.getElementById('js_a'));
            }
            document.body.appendChild(a);//创建
            a.click();//点击
            return false;
        });

        //监听工具条
        table.on('tool(iTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                //删除
                doDel(data);
            } else if(obj.event === 'edit'){
                //编辑
                var a = document.createElement('a');
                a.setAttribute('lay-href', context + "/produce/schedulingMain/toSchedulingMainAdd?id=" + data.ID);
                a.setAttribute('lay-text', '排产编辑');
                a.setAttribute('id', 'js_a');
                if(document.getElementById('js_a')) {//防止反复添加
                    document.body.removeChild(document.getElementById('js_a'));
                }
                document.body.appendChild(a);//创建
                a.click();//点击
                return false;
            }
        });

        //监听搜索框
        form.on('submit(search)', function(data){
            //重新加载table
            load(data);
            return false;
        });
        //监听checkbox操作
        form.on('switch(isStatusTpl)', function(obj){
            setStatus(obj,this.value,this.name,obj.elem.checked);
        });

        // form.on('select(deptId)', function(obj){
        //     $("#deptName").val(obj.elem[obj.elem.selectedIndex].text);
        // });

    });
});

//设置启用/禁用
function setStatus(obj,id,name,checked){
    var isStatus=checked ? 1 : 0;
    var dataIsStatus=checked ? "生效":"不生效";
    //启用/禁用
    layer.confirm('您确定要把接口：'+name+'设置为'+dataIsStatus+'状态吗？', {
        btn: ['确认','返回'] //按钮
    }, function(){
        $.ajax({
            type: "POST",
            data: { "id": id, "status": isStatus},
            url: context+"/produce/schedulingMain/doStatus",
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

//新增排产导入
function doAdd() {
    var a = document.createElement('a');
    a.setAttribute('lay-href', context + "/produce/schedulingMain/toSchedulingMainAdd");
    a.setAttribute('lay-text', '排产新增');
    a.setAttribute('id', 'js_a');
    if(document.getElementById('js_a')) {//防止反复添加
        document.body.removeChild(document.getElementById('js_a'));
    }
    document.body.appendChild(a);//创建
    a.click();//点击
    return false;

    // var index = layer.open({
    //     type:1,
    //     title: "新增排产导入",
    //     fixed:false,
    //     resize :false,
    //     shadeClose: false,//是否点击遮罩关闭
    //     area: ['1000px'],
    //     content:$('#addSchedulingMainForm'),
    //     macmin:true,//弹出框全屏
    //     end:function(){
    //
    //     }
    // });
    // layer.full(index);//弹出框全屏
}

function getDeptSelect() {
    $.ajax({
        type: "post",
        data: {},
        url: context+"/produce/schedulingMain/getDeptSelect",
        success: function (res) {
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

//删除
function doDel(obj) {
    if(obj.ID!=null){
        if(obj.FENABLE != 1){
            layer.confirm('您确定要删除吗？', {
                btn: ['确认','返回'] //按钮
            }, function(){
                $.ajax({
                    type: "POST",
                    data: { "id": obj.ID },
                    url: context+"/produce/schedulingMain/delete",
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
                            layer.closeAll();
                        });
                    }
                });
            }, function(){
                layer.closeAll();
            });
        }else{
            layer.alert("该排产已生效，无法删除",function(){
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
