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
        form.on('select(itemId)', function(data){
            var itemId = data.value;
            for(var i = 0; i < itemList.length; i++){
                if(itemId == itemList[i].id){
                    $("#itemName").val(itemList[i].itemName);
                    break;
                }
            }
            return false;
        });
        laydate.render({
            elem: '#prodDate'
        });

        tableIns=table.render({
            elem: '#iList'
            ,url:context+'/produce/scheduling/getProcessList'
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
        form.on('submit(editSubmit1)', function(data){
            //编辑-工艺维护
            doEditProcess(data.field);
            return false;
        });
        form.on('submit(saveProcess)', function(data){
            //保存-工艺维护
            saveProcess(table);
            return false;
        });

        tableIns2=table.render({
            elem: '#iList2'
            ,url:context+'/produce/scheduling/getItemList'
            ,method: 'get' //默认：get请求
            ,where:{ mid:id }
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
                ,{field:'itemNo', title:'物料编号', width:150}
                ,{field:'itemName', title:'物料描述', width:250, templet:'<span>{{d.mtrial ? d.mtrial.itemName : ""}}</span>'}
                ,{field:'itemQty', title:'用量', width:100}
                ,{field:'empName', title:'作业员', width:100, templet:'<span>{{ d.employee ? d.employee.empName : "" }}</span>'}
                ,{fixed:'right', title:'操作', align:'center', toolbar:'#optBar2'}
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
        //监听工具条-工单组件
        table.on('tool(iTable2)', function(obj){
            var data = obj.data;
            if(obj.event === 'edit2'){
                //编辑
                getItem(data,data.id);
            }
        });
        form.on('submit(editSubmit2)', function(data){
            //编辑-工单组件
            doEditItem();
            return false;
        });
     // 监听勾选过程属性操作
		form.on('checkbox(isStatusTpl)', function(obj) {
			 //当前元素
            var data = $(obj.elem);
			var rowIndex = data.parents('tr').first().attr("data-index");
			//console.log(rowIndex)
			if(obj.elem.checked){
				//res.data[rowIndex]["LAY_CHECKED"]='true';
                $('tbody tr[data-index="'+rowIndex+'"] td[data-field="checkColumn"] input[type="checkbox"]').prop('checked', true);
                $('tbody tr[data-index="'+rowIndex+'"] td[data-field="checkColumn"] input[type="checkbox"]').next().addClass('layui-form-checked');
			}else{
			    $('tbody tr[data-index="'+rowIndex+'"] td[data-field="checkColumn"] input[type="checkbox"]').prop('checked', false);
	            $('tbody tr[data-index="'+rowIndex+'"] td[data-field="checkColumn"] input[type="checkbox"]').next().removeClass('layui-form-checked');
			}
			
		});
		//监听行选择事件---根据顺序（错误），应当获取行号【未修改】
	   table.on('checkbox(iTable)', function(obj){
			if(!obj.checked){	 
				  obj.data.jobAttr = 0;
				  var rowIndex=$(obj.tr).attr("data-index");
				  console.log(rowIndex)
				  $('tbody tr[data-index="'+rowIndex+'"] td[data-field="jobAttr"] input[type="checkbox"]').prop('checked', false);
				 $('tbody tr[data-index="'+rowIndex+'"] td[data-field="jobAttr"] input[type="checkbox"]').next().removeClass('layui-form-checked');
			  }
			});		
        getScheduling();

    });
});

//渲染基本信息
function getScheduling(){
    var optionHtml = '<option value=""></option>';
    var optionHtml2 = '<option value=""></option>';
    var optionHtml3 = '';
    var optionHtml4 = '<option value=""></option>';
    //添加物料列表
    for(var i = 0; i < itemList.length; i++){
        optionHtml += '<option value="'+itemList[i].id+'">'+itemList[i].itemNo+'</option>';
    }
    $("#itemId").html(optionHtml);
    //添加客户信息
    for(var i = 0; i < clientList.length; i++){
        optionHtml2 += '<option value="'+clientList[i].id+'">'+clientList[i].custName+'</option>';
    }
    $("#custId").html(optionHtml2);
    //添加组长信息
    
    var lineData=lineList[2];
    for(var i = 0; i < lineData.length; i++){
        optionHtml3 += '<option value="'+lineData[i].ORG_NAME+'">'+lineData[i].ORG_NAME+'</option>';
    }
    $("#linerName").html(optionHtml3);
    //添加部门信息
    for(var i = 0; i < orgList.length; i++){
        optionHtml4 += '<option value="'+orgList[i].id+'">'+orgList[i].orgName+'</option>';
    }
    $("#deptId").html(optionHtml4);

    $("#id").val(id);
    $("#taskNo").val(scheduling.taskNo);
    $("#prodNo").val(scheduling.prodNo);
    $("#produceState").val(scheduling.produceState);
    $("#itemId").val(scheduling.itemId);
    $("#itemName").val(scheduling.itemName);
    $("#groupNo").val(scheduling.groupNo);
    $("#custId").val(scheduling.custId);
    $("#linerName").val(scheduling.linerName);
    $("#prodDate").val(scheduling.prodDate);
    $("#deptId").val(scheduling.deptId);
    $("#classNo").val(scheduling.classNo);
    $("#qtyPlan").val(scheduling.qtyPlan);

    //渲染
    layui.form.render('select');
}

//编辑
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

//获取编辑信息-工艺维护
function getProcess(obj, id){
    var optionHtml = '<option value=""></option>';
    //添加作业员列表
    for(var i = 0; i < employeeList.length; i++){
        optionHtml += '<option value="'+employeeList[i].id+'">'+employeeList[i].empName+'</option>';
    }
    $("#empId1").html(optionHtml);

    $("#processId1").val(id);
    $("#mid1").val(obj.mid);
    $("#procOrder1").val(obj.procOrder);
    // $("#jobAttr1").val(obj.jobAttr);
    if(obj.jobAttr==1){
        $("#jobAttr1").prop("checked", true);
    }else{
        $("#jobAttr1").prop("checked", false);
    }
    $("#empId1").val(obj.empId);
    $("#procNo1").val(obj.procNo);
    $("#procName1").val(obj.procName);

    //渲染
    layui.form.render('select');
    layui.form.render('checkbox');

    //打开弹出框
    openProcess("编辑工艺");
}
//编辑弹出框-工艺维护
function openProcess(title){
    layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['600px'],
        content:$('#editDiv1'),
        end:function(){
            cleanProcess();
            loadAll1();
        }
    });
}
//清空编辑框数据-工艺维护
function cleanProcess(){
    $("#processId1").val("");
    $("#mid1").val("");
    $("#procOrder1").val("");
    $("#jobAttr1").prop("checked", false);
    $("#empId1").val("");
    $("#procNo1").val("");
    $("#procName1").val("");

    //渲染
    layui.form.render('select');
    layui.form.render('checkbox');
}
//编辑-工艺维护
function doEditProcess(obj){
    //checkbox转换
    if(!obj.jobAttr){
        obj.jobAttr = 0;
    }
    CoreUtil.sendAjax("/produce/scheduling/editProcess",JSON.stringify(obj),function (res) {
        if (res.result == true) {
            layer.alert("编辑成功",function(){
                layer.closeAll();
                loadAll1();
            });
        } else {
            layer.alert(res.msg,function(){
                layer.closeAll();
            });
        }
    },"POST",false,function (res) {
        layer.alert("操作请求错误，请您稍后再试",function(){
            layer.closeAll();
        });
    });

    // $.ajax({
    //     type: "POST",
    //     data: $("#editForm1").serialize(),
    //     url: context+"/produce/scheduling/editProcess",
    //     success: function (res) {
    //         if (res.result) {
    //             layer.alert("编辑成功",function(){
    //                 layer.closeAll();
    //                 loadAll1();
    //             });
    //         } else {
    //             layer.alert(res.msg,function(){
    //                 layer.closeAll();
    //             });
    //         }
    //     },
    //     error: function () {
    //         layer.alert("操作请求错误，请您稍后再试",function(){
    //             layer.closeAll();
    //         });
    //     }
    // });
}

//保存工艺
function saveProcess(table) {
    //获取选中工艺
    var processIds = ""; //lst-获取的jobAttr有问题
   // var processIds1 = "";
   // var checkStatus = table.checkStatus("iList");
    var checkStatus = table.cache.iList;
    $('tbody tr td[data-field="checkColumn"] input[type="checkbox"]').each(function(i){
        if ($(this).is(":checked")) {
        	//fyx-202011-02
        	var checks = $('tbody tr[data-index="'+i+'"] td[data-field="jobAttr"] input[type="checkbox"]:checked');
        	if(checks.length == 1){
        		processIds += checkStatus[i].procId+"@1,";
        	}else{
        		processIds += checkStatus[i].procId+"@0,";
        	}
			    //操作
        	//processIds += checkStatus[i].procId+",";
        	//processIds += checkStatus[i].procId+"@"+checkStatus[i].jobAttr+",";
			  }
         });
    console.log(processIds)

   /* for(var i = 0; i < checkStatus.data.length; i++){
        if(i == 0){
            processIds += checkStatus.data[i].procId;
        }else{
            processIds += "," + checkStatus.data[i].procId;
        }
    }*/

    var param = {
        mid: $("#id").val(),
        processIds: processIds
    }

    $.ajax({
        type: "POST",
        data: param,
        url: context+"/produce/scheduling/saveProcessProc",
        success: function (res) {
            if (res.result) {
                layer.alert("保存成功！",function(){
                    layer.closeAll();
                    loadAll1();
                });
            } else {
                layer.alert(res.msg,function(){
                    layer.closeAll();
                });
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试！",function(){
                layer.closeAll();
            });
        }
    });
}

//获取编辑信息-工单组件
function getItem(obj, id){
    var optionHtml = '<option value=""></option>';
    //添加作业员列表
    for(var i = 0; i < employeeList.length; i++){
        optionHtml += '<option value="'+employeeList[i].id+'">'+employeeList[i].empName+'</option>';
    }
    $("#empId2").html(optionHtml);

    $("#itemId2").val(id);
    $("#mid2").val(obj.mid);
    $("#itemNo2").val(obj.itemNo);
    $("#itemName2").val(obj.mtrial ? obj.mtrial.itemName : "");
    $("#itemQty2").val(obj.itemQty);
    $("#empId2").val(obj.empId);

    //渲染
    layui.form.render('select');

    //打开弹出框
    openItem("编辑工单组件");
}
//编辑弹出框-工单组件
function openItem(title){
    layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['600px'],
        content:$('#editDiv2'),
        end:function(){
            cleanItem();
        }
    });
}
//清空编辑框数据-工单组件
function cleanItem(){
    $("#itemId2").val("");
    $("#mid2").val("");
    $("#itemNo2").val("");
    $("#itemName2").val("");
    $("#itemQty2").val("");
    $("#empId2").val("");

    //渲染
    layui.form.render('select');
}
//编辑-工单组件
function doEditItem(){
    $.ajax({
        type: "POST",
        data: $("#editForm2").serialize(),
        url: context+"/produce/scheduling/editItem",
        success: function (res) {
            if (res.result) {
                layer.alert("编辑成功",function(){
                    layer.closeAll();
                    loadAll2();
                });
            } else {
                layer.alert(res.msg,function(){
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

//重新加载表格-工艺维护（全部）
function loadAll1(){
    //重新加载table
    tableIns.reload({
        page: {
            curr: pageCurr //从当前页码开始
        }
    });
}
//重新加载表格-工单组件（全部）
function loadAll2(){
    //重新加载table
    tableIns2.reload({
        page: {
            curr: pageCurr2 //从当前页码开始
        }
    });
}

