/**
 * 来料不良录入
 */
var pageCurr;
var tabledata=[];
$(function() {
    layui.use(
        [ 'table', 'form', 'layedit', 'laydate', 'tableSelect' ],
        function() {
            var form = layui.form, layer = layui.layer, layedit = layui.layedit, table = layui.table,
                table1 = layui.table,laydate = layui.laydate, tableSelect = layui.tableSelect
                ,tableSelect1 = layui.tableSelect,tableSelect2 = layui.tableSelect;
            ;

            //日期选择器
            laydate.render({
                elem: '#procDate',
                type: 'date' //默认，可不填
            });

            tableIns = table.render({
                elem : '#colTable'
                //,url:context+'/interfaces/getRequestList'
                ,
                where : {},
                method : 'get' //默认：get请求
                //,toolbar: '#toolbar' //开启头部工具栏，并为其绑定左侧模板
                ,
                defaultToolbar : [],
                cellMinWidth : 80,
                height:'full-375'//固定表头&full-查询框高度
					,even:true,//条纹样式
                page : false,
                data : [],
                request : {
                    pageName : 'page' //页码的参数名称，默认：page
                    ,
                    limitName : 'rows' //每页数据量的参数名，默认：limit
                },
                parseData : function(res) {
                    // 可进行数据操作
                    return {
                        "count" : res.data.total,
                        "msg" : res.msg,
                        "data" : res.data.rows,
                        "code" : res.status
                        //code值为200表示成功
                    }
                },
                cols : [ [
                    {
                        type : 'numbers'
                    },  {
                        field : 'ITEM_NO',
                        title : '物料料号',
                        width : 160
                    },
                    {
                        field : 'ITEM_NAME',
                        title : '物料名称',
                        width : 230
                    } ,
                    {
                        field : 'DEFECT_NAME',
                        title : '不良内容',
                        width : 120
                    },
                    {
                        field : 'DEFECT_DET_QTY',
                        title : '不良数量',
                        width : 100
                    }, {
                        field : 'USER_NAME',
                        title : '录入人姓名',
                        width : 100
                    }, {
                        field : 'CREATE_DATE',
                        title : '录入时间',
                        width : 150
                    },
                    {
                        field : 'DEPT_NAME',
                        title : '来料部门',
                        width : 120,sort: true
                    },
                    {
                        field : 'VENDER_NAME',
                        title : '供应商名称',
                        width : 200
                    } ,
                ] ],
                done : function(res, curr, count) {
                    pageCurr = curr;
                }
            });



            tableSelect=tableSelect.render({
                elem : '#itemNo',
                searchKey : 'keyword',
                checkedKey : 'id',
                searchPlaceholder : '试着搜索',
                table : {
                    url:  context +'/produce/badMaterial/getOrg',
                    method : 'get',
                    // width:800,
                    cols : [ [
                        { type: 'radio' },//多选  radio
                        , {
                            field : 'id',
                            title : 'id',
                            width : 0,hide:true
                        },  {
                            field : 'ITEM_NO',
                            title : '物料编码',
                            width : 170
                        },{
                            field : 'ITEM_NAME',
                            title : '物料名称',
                            width : 240
                        }, {
                            field : 'ITEM_NAME_S',
                            title : '物料简称',
                            width : 240
                        },
                    ] ],
                    page : false,
                    request : {
                        pageName : 'page' // 页码的参数名称，默认：page
                        ,
                        limitName : 'rows' // 每页数据量的参数名，默认：limit
                    },
                    parseData : function(res) {
                        if(res.result){
                            // 可进行数据操作
                            return {
                                "count" :res.data.total,
                                "msg" : res.msg,
                                "data" : res.data.rows,
                                "code" : res.status
                                // code值为200表示成功
                            }
                        }

                    },
                },
                done : function(elem, data) {
                    //选择完后的回调，包含2个返回值 elem:返回之前input对象；data:表格返回的选中的数据 []
                    var da=data.data;
                    //console.log(da[0].num)
                    form.val("itemFrom", {
                        "itemNo":da[0].ITEM_NO,
                        "mtrdescr" : da[0].ITEM_NAME,
                    });
                    form.render();// 重新渲染

                    // getDetailByTask(da[0].TASK_NO);

                }
            });

            tableSelect2=tableSelect2.render({
                elem : '#taskNo',
                searchKey : 'keyword',
                checkedKey : 'id',
                searchPlaceholder : '试着搜索',
                table : {
                    url:  context +'/produce/badMaterial/getTaskNo',
                    method : 'get',
                    width:800,
                    cols : [ [
                        { type: 'radio' },//多选  radio
                        , {
                            field : 'id',
                            title : 'id',
                            width : 0,hide:true
                        },
                        {
                            field : 'LINER_NAME',
                            title : '组长',
                            width : 70
                        },
                        {
                            field : 'PROD_DATE',
                            title : '计划日期',
                            width : 100,
                            templet:function (d) {
                                if(d.PROD_DATE!=null){
                                    return /\d{4}-\d{1,2}-\d{1,2}/g.exec(d.PROD_DATE)
                                }
                            }
                        },
                        {
                            field : 'ITEM_NO',
                            title : '物料编码',
                            width : 170
                        },{
                            field : 'ITEM_NAME',
                            title : '物料描述',
                            width : 240
                        }, {
                            field : 'QTY_PLAN',
                            title : '数量',
                            width : 80
                        },{
                            field : 'QUANTITY',
                            title : '投入数量',
                            width : 80
                        },
                        {
                            field : 'TASK_NO',
                            title : '制令单号',
                            width : 175,sort: true
                        },] ],
                    page : true,
                    request : {
                        pageName : 'page' // 页码的参数名称，默认：page
                        ,
                        limitName : 'rows' // 每页数据量的参数名，默认：limit
                    },
                    parseData : function(res) {
                        if(res.result){
                            // 可进行数据操作
                            return {
                                "count" :0,
                                "msg" : res.msg,
                                "data" : res.data,
                                "code" : res.status
                                // code值为200表示成功
                            }
                        }

                    },
                },
                done : function(elem, data) {
                    //选择完后的回调，包含2个返回值 elem:返回之前input对象；data:表格返回的选中的数据 []
                    var da=data.data;
                    //console.log(da[0].num)
                    form.val("itemFrom", {
                        "taskNo":da[0].TASK_NO,
                    });
                    form.render();// 重新渲染
                }
            });

            tableSelect=tableSelect1.render({
                elem : '#supplier',
                searchKey : 'keyword',
                checkedKey : 'ID',
                searchPlaceholder : '试着搜索',
                table : {
                    url:  context +'/produce/badMaterial/getSupplier',
                    method : 'get',
                    // width:800,
                    cols : [ [
                        { type: 'radio' },//多选  radio
                        , {
                            field : 'ID',
                            title : 'id',
                            width : 0,hide:true
                        }, {
                            field : 'VENDER_NAME',
                            title : '供应商名称',
                            align:'center',
                            width : 250,sort: true
                        } ] ],
                    page : false,
                    request : {
                        pageName : 'page' // 页码的参数名称，默认：page
                        ,
                        limitName : 'rows' // 每页数据量的参数名，默认：limit
                    },
                    parseData : function(res) {
                        if(res.result){
                            // 可进行数据操作
                            return {
                                "count" :res.data.total,
                                "msg" : res.msg,
                                "data" : res.data.rows,
                                "code" : res.status
                                // code值为200表示成功
                            }
                        }

                    },
                },
                done : function(elem, data) {
                    //选择完后的回调，包含2个返回值 elem:返回之前input对象；data:表格返回的选中的数据 []
                    var da=data.data;
                    //console.log(da[0].num)
                    form.val("itemFrom", {
                        "supplier":da[0].VENDER_NAME,
                        "supplierId":da[0].ID
                    });
                    form.render();// 重新渲染

                    // getDetailByTask(da[0].TASK_NO);

                }
            });
            form.verify({
				num: function(value){
					if(/^\d+$/.test(value)==false && /^\d+\.\d+$/.test(value)==false)
					{
						return '只能输入数字';
					}
				}
			});
            // 监听工具条
            table.on('tool(colTable)', function(obj) {
                //console.log(obj)
                var data = obj.data;
                //console.log(data)
                if (obj.event === 'del') {
                    // 删除
                    del(obj,data.ID, data.ITEM_BARCODE);
                }
            });

            // 监听
            form.on('submit(confirmSubmit)', function(data) {
                //console.log(data.field)
                addPut(data.field)
            });

            getDefectSelect();
            getOrgSelect();

            //监听提交
            form.on('submit(hsearchSubmit)', function(data){
                hTableIns.reload({
                    url:context+'/produce/badMaterial/getHistoryList',
                    where:data.field
                });
                return false;
            });
            hTableIns = table1.render({// 历史
                elem : '#hcolTable',
                where : {},
                method : 'get',// 默认：get请求
                defaultToolbar : [],
                page : true,
                data : [],
                height:'full-80'//固定表头&full-查询框高度
					,even:true,//条纹样式
                request : {
                    pageName : 'page', // 页码的参数名称，默认：page
                    limitName : 'rows' // 每页数据量的参数名，默认：limit
                },
                parseData : function(res) {// 可进行数据操作
                    return {
                        "count" : res.data.total,
                        "msg" : res.msg,
                        "data" : res.data.rows,
                        "code" : res.status
                        // code值为200表示成功
                    }
                },
                cols : [ [
                    {
                    type : 'numbers'
                },  {
                    field : 'ITEM_NO',
                    title : '物料料号',
                    width : 160
                },
                    {
                    field : 'ITEM_NAME',
                    title : '物料名称',
                    width : 230
                } ,
                    {
                        field : 'DEFECT_NAME',
                        title : '不良内容',
                        width : 120
                    },
                    {
                    field : 'DEFECT_DET_QTY',
                    title : '不良数量',
                    width : 100
                }, {
                    field : 'USER_NAME',
                    title : '录入人姓名',
                    width : 100
                }, {
                    field : 'CREATE_DATE',
                    title : '录入时间',
                    width : 150
                },
                    {
                        field : 'DEPT_NAME',
                        title : '来料部门',
                        width : 120,sort: true
                    },
                    {
                        field : 'VENDER_NAME',
                        title : '供应商名称',
                        width : 200
                    } ,
                ] ],
                done : function(res, curr, count) {
                    pageCurr = curr;
                }
            });
        });


    $('#barcode').bind('keypress',function(event){
        if(event.keyCode == "13") {
            //alert('你输入的内容为：' + $('#barcode').val());
            if($('#barcode').val()){
                getInfoBarcode($('#barcode').val())
            }else{
                layer.alert("请先扫描条码!",function () {
                    $('#barcode').focus();
                    layer.closeAll();
                });
            }
        }
    });
});


function getDefectSelect() {
    CoreUtil.sendAjax("/produce/badMaterial/getBadList", "", function(data) {
        if (data.result) {
            if (data.data) {
                $("#defect").empty();
                var pclass = data.data;
                for (var i = 0; i < pclass.length; i++) {
                    $("#defect").append(
                        "<option value=" + pclass[i].DEFECT_CODE + ">"
                        + pclass[i].DEFECT_NAME + "</option>");
                }
                layui.form.render('select');
            }


        } else {
            layer.alert(data.msg);
        }
    }, "GET", false, function(res) {
        layer.alert(res.msg);
    });
    return false;
}
function getOrgSelect() {
    CoreUtil.sendAjax("/produce/badMaterial/getDept", "", function(data) {
        if (data.result) {
            if (data.data) {
                $("#org").empty();
                var pclass = data.data;
                //console.log(pclass)
                for (var i = 0; i < pclass.length; i++) {
                	if(i==0){
                		$("#org").append(
                                "<option value=''>请选择</option>");
                	}
                    $("#org").append(
                        "<option value=" + pclass[i].ID + ">"
                        + pclass[i].DEPT_NAME + "</option>");
                }
                layui.form.render('select');
            }
        } else {
            layer.alert(data.msg);
        }
    }, "GET", false, function(res) {
        layer.alert(res.msg);
    });
    return false;
}

function getInfoBarcode(barcode){
    var params = {
        "barcode" : barcode,
    }
    CoreUtil.sendAjax("/produce/badMaterial/getDetailByBarcode", params, function(data) {
        if (data.result) {
        	//console.log(data.data[0])
            playSaoMiaoMusic();
            $( "input[name='itemNo']").val(data.data[0].ITEM_NO);
            $( "input[name='procDate']").val(data.data[0].PROD_DATE);
            $( "input[name='mtrdescr']").val(data.data[0].ITEM_NAME);
            $("#org").val(data.data[0].DEPID);
            $( "input[name='supplier']").val(data.data[0].VENDER); 
            $( "input[name='supplierId']").val(data.data[0].VENDERID);
            $( "input[name='lotNo']").val(data.data[0].BATCHNO);
            layui.form.render('select');
            $('#itemNo').focus();
            // supplier
        }else{
            playMusic();
            layer.alert(data.msg,function () {
                $('#barcode').val('');
                $('#item_code').val('');
                $('#addqty').val('');
                $('#barcode').focus();
                layer.closeAll();
            });
            // $('#barcode').val('');
        }
    }, "GET", false, function(res) {
        layer.alert(res.msg);
    });
}

// function getDetailByTask(taskNo){
//     var params={"taskNo":taskNo}
//     CoreUtil.sendAjax("/inputCheck/getDetailByTask", params, function(data) {
//         console.log(data)
//         if (data.result) {
//             tableIns.reload({
//                 data:data.data
//             });
//             $('#barcode').val('');
//             $('#item_code').val('');
//             $('#addqty').val('');
//         }else{
//             layer.alert(data.msg);
//         }
//     }, "GET", false, function(res) {
//         layer.alert(res.msg);
//     });
// }
//录入来料不良信息
function addPut(obj){
    // console.log(obj);
    //var supplierId = $('#supplier').attr('ts-selected');
    var params={
        "barcode":obj.barcode,
        "itemNo":obj.itemNo,
        "lotNo":obj.lotNo,
        "defectQty":obj.inqty,
        "deptId":obj.org,
        "defectCode":obj.defect,
        "venderId":obj.supplierId,
        "procDate":obj.procDate,
        "taskNo":obj.taskNo
    };

    CoreUtil.sendAjax("/produce/badMaterial/saveMaterial", params, function(data) {
        if (data.result) {
            // layer.alert("保存成功！");
            tableIns.reload({
                data:data.data
            });
        }else{
            // playMusic();
            layer.alert(data.msg,function () {
                $('#barcode').focus();
                layer.closeAll();
            });
        }
        // $( "input[name='barcode']").val('');
        // $( "input[name='item_code']").val('');
        // $( "input[name='addqty']").val('');
    }, "GET", false, function(res) {
        layer.alert(res.msg);
    });
}

//删除
function del(obj,id, barcode) {
    if (id != null) {
        var params={"barcode":id};
        layer.confirm('您确定要删除条码' + barcode + '吗？', {
            btn : [ '确认', '返回' ]
            // 按钮
        }, function() {
            CoreUtil.sendAjax("/inputCheck/delete", params, function(data) {
               // console.log(data)
                if (data.result == true) {
                    // 回调弹框
                    layer.alert("删除成功！", function() {
                        $("#inqty").val(data.data);
                        obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
                        layer.closeAll();
                    });
                } else {
                    layer.alert(data, function() {
                        layer.closeAll();
                    });
                }
            }, "GET", false, function(res) {
                layer.alert(res.msg);
            });
        });
    }
}

