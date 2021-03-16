/**
 * 投料检验
 */
var pageCurr;
var tabledata=[];
$(function() {
    layui.use(
        [ 'table', 'form', 'layedit', 'laydate', 'tableSelect' ],
        function() {
            var form = layui.form, layer = layui.layer, layedit = layui.layedit, table = layui.table,  table1 = layui.table,laydate = layui.laydate, tableSelect = layui.tableSelect;
            ;

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
                cols : [ [ {
                    type : 'radio',
                    width : 50
                },{
                    field : 'id',
                    title : 'id',
                    width : 0,hide:true
                },
                    {
                        field : 'ITEM_BARCODE',
                        title : '条码',
                        width : 175,sort: true
                    },{
                        field : 'ITEM_NO',
                        title : '物料编号',
                        align:	'center',
                        width : 170,sort: true
                    }, {
                        field : 'ITEM_NAME',
                        align:	'center',
                        width : 160,
                        title : '物料描述',sort: true
                    },
                    {
                        field : 'FEED_TYPE',
                        title : '类型',
                        align:	'center',
                        width : 90,sort: true
                    },
                    {
                        field : 'USER_NAME',
                        title : '操作人',
                        align:	'center',
                        width : 110,sort: true
                    },
                    {
                        field : 'QUANTITY',
                        title : '数量',
                        width : 90,sort: true
                    },{
                        field : 'CREATE_DATE',
                        title : '创建时间',
                        width : 150,sort: true
                    },  {
                        fixed : 'right',
                        title : '操作',
                        align : 'center',
                        toolbar : '#optBar',
                        width : 80
                    } ] ],
                done : function(res, curr, count) {
                    pageCurr = curr;
                }
            });
            tableSelect=tableSelect.render({
                elem : '#num',
                searchKey : 'keyword',
                checkedKey : 'id',
                searchPlaceholder : '试着搜索',
                table : {
                    url:  context +'/inputCheck/getTaskNo',
                    //url:  context +'base/prodproc/getProdList',
                    method : 'get',
                    width:800,
                    cols : [ [
                        { type: 'radio' },//多选  radio
                        , {
                            field : 'id',
                            title : 'id',
                            width : 0,hide:true
                        },
                        {field : 'WS_SECTION', title : '工段', width : 70},
                        {field : 'LINER_NAME', title : '组长', width : 70},
                        {field : 'PROD_DATE', title : '计划日期', width : 100,
                            templet:function (d) {
                                if(d.PROD_DATE!=null){
                                    return /\d{4}-\d{1,2}-\d{1,2}/g.exec(d.PROD_DATE)
                                }
                            }
                        },
                         {field : 'ITEM_NO', title : '物料编码', width : 190},
                        {field : 'ITEM_NAME', title : '物料描述', width : 240},
                        {field : 'FMEMO', title : '备注', width : 120},
                        {field : 'QTY_PLAN',title : '数量', width : 80
                        },{field : 'QUANTITY', title : '投入数量', width : 80},
                        {field : 'TASK_NO', title : '制令单号', width : 175,sort: true}
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
                        "num":da[0].TASK_NO,
                        "mtrcode" : da[0].ITEM_NO,
                        "mtrdescr" : da[0].ITEM_NAME,
                        "qty" : da[0].QTY_PLAN,
                        "linecode" : da[0].LINER_NAME,
                        "inqty" : da[0].QUANTITY,
                    });
                    form.render();// 重新渲染

                    getDetailByTask(da[0].TASK_NO);

                }
            });

            // 监听工具条
            table.on('tool(colTable)', function(obj) {
                console.log(obj)
                var data = obj.data;
                console.log(data)
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

            //监听提交
            form.on('submit(hsearchSubmit)', function(data){
                hTableIns.reload({
                    url:context+'/inputCheck/getHistoryList',
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
                cols : [ [ {
                    type : 'numbers'
                },{
                    field : 'TASK_NO',
                    title : '制令单号',
                    width : 175,sort: true
                }, {
                    field : 'ITEM_BARCODE',
                    title : '物料条码',
                    width : 175
                }, {
                    field : 'ITEM_NO',
                    title : '物料料号',
                    width : 175
                }, {
                    field : 'ITEM_NAME',
                    title : '物料名称',
                    width : 200
                } , {
                    field : 'QUANTITY',
                    title : '上料数量',
                    width : 120
                }, {
                    field : 'USER_NAME',
                    title : '上料人姓名',
                    width : 100
                }, {
                    field : 'CREATE_DATE',
                    title : '上料时间',
                    width : 150
                },
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
                getInfoBarcode($('#barcode').val(),$('#num').val())
            }else{
                layer.alert("请先扫描条码!",function () {
                    $('#barcode').focus();
                    layer.closeAll();
                });
            }
        }
    });
});

function getInfoBarcode(barcode,taskNo){
    var params = {
        "barcode" : barcode,
        "taskNo":taskNo
    }
    CoreUtil.sendAjax("/inputCheck/getInfoBarcode", params, function(data) {
        if (data.result) {
            playSaoMiaoMusic();
            $( "input[name='item_code']").val(data.data[0].ITEM_NO);
            $( "input[name='addqty']").val(data.data[0].QTY);
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

function getDetailByTask(taskNo){
    var params={"taskNo":taskNo}
    CoreUtil.sendAjax("/inputCheck/getDetailByTask", params, function(data) {
        console.log(data)
        if (data.result) {
            tableIns.reload({
                data:data.data
            });
            $('#barcode').val('');
            $('#item_code').val('');
            $('#addqty').val('');
        }else{
            layer.alert(data.msg);
        }
    }, "GET", false, function(res) {
        layer.alert(res.msg);
    });
}

function addPut(obj){
    var params={"barcode":obj.barcode,"task_no":obj.num,"item_no":obj.item_code,"qty":obj.addqty};
    CoreUtil.sendAjax("/inputCheck/addPut", params, function(data) {
        if (data.result) {
            $("#inqty").val(data.data.Qty);
            tableIns.reload({
                data:data.data.List
            });
            $('#barcode').focus();
        }else{
            playMusic();
            layer.alert(data.msg,function () {
                $('#barcode').focus();
                layer.closeAll();
            });
        }
        $( "input[name='barcode']").val('');
        $( "input[name='item_code']").val('');
        $( "input[name='addqty']").val('');
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
                console.log(data)
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

