layui.use('layer', function () {
    var $ = layui.$
        , layer = layui.layer
        , index = parent.layer.getFrameIndex(window.name); //获取窗口索引

    var active = {

        submit: function () {
            var radioVal = $('input:radio[name="checkRadio"]:checked').val();
            var cc = $('#desc').val();
            var json = {"bsRecordId": ppid, "bsStepCheckStatus": radioVal, "bsCheckComments": cc, "bsCheckCode": wn}
            layer.load(); // 开启loading，前端防止重复点击
            $.ajax({
                url: context + '/check/doCheck',
                type: "post",
                data: json,
                dataType: "json",
                success: function (res) {
                    layer.closeAll('loading'); // 关闭loading
                    console.log(res)
                    if (res.result === true) {
                        parent.layer.close(index);
                        layer.msg(res.msg, {
                            btn: ['知道了']
                            , yes: function (index1, layero) {
                                layer.close(index1);
                            }
                        });
                        // var srcUrl = context + '/console';
                        // ($(window.parent.document).find(('iframe[src="'+srcUrl+'"]'))).attr('src',srcUrl);

                    } else {
                        layer.msg(res.msg);
                    }
                }

            });
        },

        //防御单位提交审核
        submit2: function () {
            var radioVal = $('input:radio[name="checkRadio"]:checked').val();
            var cc = $('#desc').val();
            var json = {"bsRecordId": ppid, "bsStepCheckStatus": 1, "bsCheckComments": cc, "bsCheckCode": wn}
            $.ajax({
                url: context + '/check/doCheck',
                type: "post",
                data: json,
                dataType: "json",
                success: function (res) {
                    console.log(res)
                    if (res.result === true) {
                        layer.msg(res.msg, function () {
                            parent.layer.close(index);
                        });
                        //parent.doLoadAll();//调用父页面重新加载表格的方法
                    } else {
                        layer.msg("当前用户在该步骤无审批权限或已提交审批！");
                    }
                }

            });
        }

        //在内部关闭iframe
        , close: function (set) {
            parent.layer.close(index);
        }
    }

    $('.layui-btn-container .layui-btn').on('click', function () {

        var othis = $(this)
            , type = othis.data('type');
        active[type] && active[type].call(this);
    });
    $('.ap-foot .layui-btn').on('click', function () {

        var othis = $(this)
            , type = othis.data('type');

        active[type] && active[type].call(this);
    });

});

function getInfo(json) {
    $.ajax({
        url: context + '/check/getInfo',
        type: "post",
        data: json,
        dataType: "json",
        success: function (res) {
            console.log(res);
            if (res.result === true) {
                var list = res.data;
                if (list.grade == '1') {
                    $("#summit-btn").html("发起")
                }

                if(json.wname!='QUOTE_NEW'){

                }else {
                    if (list.grade == '1') {
                        $("#th").attr("disabled", "disabled");//失效

                    }
                }

                var timeHtml = '';
                var nowHtml =''; //当前的步骤流程信息显示在最后面
                for (var i = 0; i < list.data.length; i++) {
                    //更新时间不为空则为下一步需要审批的步骤
                    if (CheckIsNullOrEmpty(list.data[i].lastupdateDate)) {
                        timeHtml += '<li class="layui-timeline-item">' + getIcon(list.data[i].bsStepCheckStatus) +
                            /*  '<i class="layui-icon '+getIcon(list.data[i].bsStepCheckStatus)+'"></i>'+ */
                            ' <div class="layui-timeline-content layui-text">' +
                            '  <div class="layui-timeline-title">' +
                            '   <span style="padding-right:20px;">' + '[' + getRadio(list.data[i].bsStepCheckStatus) + ']' + list.data[i].lastupdateDate + '</span>  ' + list.data[i].bsCheckName + ',' + list.data[i].bsCheckComments +
                            '  </div>' +
                            ' </div>' +
                            ' </li>';
                    }else {
                            nowHtml += '<li class="layui-timeline-item">' + getIcon(3) +
                            /*  '<i class="layui-icon '+getIcon(list.data[i].bsStepCheckStatus)+'"></i>'+ */
                            ' <div class="layui-timeline-content layui-text">' +
                            '  <div class="layui-timeline-title">' +
                            '   <span style="padding-right:20px;">' + '[' + getRadio(3) + ']'+'</span>  ' +"等待"+ list.data[i].bsCheckName + "审核" +
                            '  </div>' +
                            ' </div>' +
                            ' </li>';
                    }
                }

                if(list.data.length>1) {
                    if (list.data[list.data.length - 1].bsStepCheckStatus == 2) {
                        nowHtml += '<li class="layui-timeline-item">' + getIcon(3) +
                            /*  '<i class="layui-icon '+getIcon(list.data[i].bsStepCheckStatus)+'"></i>'+ */
                            ' <div class="layui-timeline-content layui-text">' +
                            '  <div class="layui-timeline-title">' +
                            '   <span style="padding-right:20px;">' + '[' + getRadio(3) + ']' + '</span>  ' + "等待业务员重新发起提交" +
                            '  </div>' +
                            ' </div>' +
                            ' </li>';
                    }
                }

                //20201228-fyx-流程信息 flow_info
                if (list.Lw.length == 0) {
                    document.getElementById('flow_info').innerHTML = "请先配置流程步骤!";
                } else {
                    var info = "审批:";
                    console.log(list);
                    for (var i = 0; i < list.Lw.length; i++) {
                        if (i == 0) {
                            // info += list.Lw[i].bsStepName+"("+list.curBy+")>";
                            info += list.Lw[i].bsStepName + " >";
                            if(timeHtml==''&&nowHtml==''){
                                nowHtml += '<li class="layui-timeline-item">' + getIcon(3) +
                                    /*  '<i class="layui-icon '+getIcon(list.data[i].bsStepCheckStatus)+'"></i>'+ */
                                    ' <div class="layui-timeline-content layui-text">' +
                                    '  <div class="layui-timeline-title">' +
                                    '   <span style="padding-right:20px;">' + '[' + getRadio(3) + ']'+'</span>  ' +"等待"+ list.Lw[i].bsStepName +
                                    '  </div>' +
                                    ' </div>' +
                                    ' </li>';
                            }
                        } else {
                            info += list.Lw[i].bsStepName + "(" + list.Lw[i].bsCheckName + ")>";
                            // if(list.Lc[i]!=null){
                            //     info += list.Lw[i].bsStepName + "(" + list.Lc[i].bsCheckName + ")>";
                            // }else {
                            //     info += list.Lw[i].bsStepName + "(" + list.Lw[i].bsCheckName + ")>";
                            // }
                        }
                    }
                    info = info.substring(0, info.length - 1);
                    document.getElementById('flow_info').innerHTML = info;
                }
                document.getElementById('timeline').innerHTML = timeHtml+nowHtml;

            } else {
                layer.msg(res.msg);
            }
            // parent.layer.iframeAuto(index);
        }

    });
}

var ppid;
var wn = '';
var status = 'end';

function child(wname, pid, bsStatus) {
    console.log(bsStatus)
    //接受流程类型参数
    ppid = pid;
    wn = wname;
    status = bsStatus;
    getInfo({"id": pid, "wname": wname});
    console.log(pid+"---"+wname);
    if (status == 'end') {
        $('#summit-btn').addClass("layui-btn-disabled").attr("disabled", true);//失效
    } else {
        $('#summit-btn').removeClass("layui-btn-disabled").attr("disabled", false);//有效
    }
}

//判断是否完全确认完成
function radioSelect(complete){
    if(!complete){
        $("#agree").attr("disabled", "disabled");//失效
        $("#th").attr("checked", true);//失效
    }
    // $("#agree").attr("disabled", "disabled");//失效

}

function getRadio(status) {
    if (status == 1) {
        return "通过"
    } else if (status == 2) {
        return "驳回"
    }else if (status == 3) {
        return "等待审核"
    }
}

function CheckIsNullOrEmpty(value) {
    //正则表达式用于判斷字符串是否全部由空格或换行符组成
    var reg = /^\s*$/
    //返回值为true表示不是空字符串
    return (value != null && value != undefined && !reg.test(value))
}

function getIcon(status) {
    if (status == 1) {
        return "<i class='layui-icon iconfont icon-extend-queren' style='position:absolute;left:-2px;color:#7CCD7C'></i>"
    } else if(status ==2){
        return "<i class='layui-icon iconfont icon-extend-tuihui ' style='position:absolute;left:-2px;color:#EE6A50'></i>"
    }else {
        return "<i class='layui-icon iconfont icon-extend-baogongtongji- ' style='position:absolute;left:-2px;color:#FFFF00'></i>"
    }
}
