layui.use('layer', function(){
        var $ = layui.$
            ,layer = layui.layer
            ,index = parent.layer.getFrameIndex(window.name); //获取窗口索引

        var active = {

            submit: function(){
                var radioVal = $('input:radio[name="checkRadio"]:checked').val();
                var cc = $('#desc').val();
                var json = {"bsRecordId":ppid,"bsStepCheckStatus":radioVal,"bsCheckComments":cc,"bsCheckCode":wn}
                $.ajax({
                    url: context+'/check/doCheck',
                    type: "post",
                    data: json,
                    dataType: "json",
                    success: function (res) {
                        console.log(res)
                        if(res.result===true){
                        	layer.msg(res.msg, {
                        	     btn: [ '知道了']
                        	     ,yes: function(index1, layero){
                        	         layer.close(index1);
                        	    	 parent.layer.close(index);
                        	     }
                        	});

                        }else{
                            layer.msg(res.msg);
                        }
                    }

                });
            },

            //防御单位提交审核
            submit2: function(){
                var radioVal = $('input:radio[name="checkRadio"]:checked').val();
                var cc = $('#desc').val();
                var json = {"bsRecordId":ppid,"bsStepCheckStatus":1,"bsCheckComments":cc,"bsCheckCode":wn}
                $.ajax({
                    url: context+'/check/doCheck',
                    type: "post",
                    data: json,
                    dataType: "json",
                    success: function (res) {
                        console.log(res)
                        if(res.result===true){
                        	layer.msg(res.msg, function () {
                        		parent.layer.close(index);
                        	});
                            //parent.doLoadAll();//调用父页面重新加载表格的方法
                        }else{
                            layer.msg("当前用户在该步骤无审批权限或已提交审批！");
                        }
                    }

                });
            }

            //在内部关闭iframe
            ,close: function(set){
                parent.layer.close(index);
            }
        }

        $('.layui-btn-container .layui-btn').on('click', function(){

            var othis = $(this)
                ,type = othis.data('type');
            active[type] && active[type].call(this);
        });
        $('.ap-foot .layui-btn').on('click', function(){

            var othis = $(this)
                ,type = othis.data('type');

            active[type] && active[type].call(this);
        });

    });
    function getInfo(json){
        $.ajax({
            url: context+'/check/getInfo',
            type: "post",
            data: json,
            dataType: "json",
            success: function (res) {
                console.log(res);
                if(res.result===true){

                    var list = res.data;

                    if(list.grade == '1'){
                        $("#th").attr("disabled","disabled");//失效
                    }

                    if(list.data.length == 0){
                        document.getElementById('timeline').innerHTML = "";
                    }else{
                        var timeHtml = '';
                        for(var i=0; i<list.data.length; i++){
                            if(CheckIsNullOrEmpty(list.data[i].lastupdateDate)){
                                timeHtml += '<li class="layui-timeline-item">'+ getIcon(list.data[i].bsStepCheckStatus)+
                                    /*  '<i class="layui-icon '+getIcon(list.data[i].bsStepCheckStatus)+'"></i>'+ */
                                    ' <div class="layui-timeline-content layui-text">'+
                                    '  <div class="layui-timeline-title">'+
                                    '   <span style="padding-right:20px;">'+'['+getRadio(list.data[i].bsStepCheckStatus)+']'+list.data[i].lastupdateDate+'</span>  '+list.data[i].bsCheckName+','+list.data[i].bsCheckComments+
                                    '  </div>'+
                                    ' </div>'+
                                    ' </li>';
                            }
                        }
                        document.getElementById('timeline').innerHTML = timeHtml;
                    }
                    
                    //20201228-fyx-流程信息 flow_info
                    if(list.Lw.length == 0){
                    	document.getElementById('flow_info').innerHTML = "请先配置流程步骤!";
                    }else{
                    	var info = "审批:";
                    	for(var i=0; i<list.Lw.length; i++){
                    		if( i == 0){
                    			info += list.Lw[i].bsStepName+"("+list.curBy+")>";
                    		}else{
                    			info += list.Lw[i].bsStepName+"("+list.Lw[i].bsCheckName+")>";
                    		}	
                    	}
                    	info= info.substring(0,info.length - 1);
                    	document.getElementById('flow_info').innerHTML = info;
                    }
                    

                }else{
                    layer.msg(res.msg);
                }
                // parent.layer.iframeAuto(index);
            }

        });
    }
    var ppid;
    var wn='';
    var status='end';
    function child(wname,pid,bsStatus) {
    	console.log()
        //接受流程类型参数
        ppid = pid;
        wn = wname;
        status = bsStatus;
        getInfo({"id":pid,"wname":wname});
        if(status == 'end'){
        	$('#summit-btn').addClass("layui-btn-disabled").attr("disabled",true);//失效
        }else{
        	$('#summit-btn').removeClass("layui-btn-disabled").attr("disabled",false);//有效
        }
    }

    function getRadio(status){
        if(status == 1){
            return "通过"
        }else if(status == 2){
            return "驳回"
        }
    }
    function CheckIsNullOrEmpty(value) {
        //正则表达式用于判斷字符串是否全部由空格或换行符组成
        var reg = /^\s*$/
        //返回值为true表示不是空字符串
        return (value != null && value != undefined && !reg.test(value))
    }
    function getIcon(status){
        if(status == 1){
            return "<i class='layui-icon iconfont icon-extend-queren' style='position:absolute;left:-2px;color:#7CCD7C'></i>"
        }else{
            return "<i class='layui-icon iconfont icon-extend-tuihui ' style='position:absolute;left:-2px;color:#EE6A50'></i>"
        }
    }
