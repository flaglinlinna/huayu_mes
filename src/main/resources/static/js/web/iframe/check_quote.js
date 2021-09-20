var step=1;
layui.use('layer', function(){
        var $ = layui.$
            ,layer = layui.layer
            ,index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        
        low_flag = xmSelect.render({
    	    el: '#low_flag',
    	    name:'level',
    	    tips: '请选择退回操作部门',
    	    data: [
    	        {name: '制造部-五金', value: 'hardware'},
    	        {name: '制造部-注塑', value: 'molding'},
    	        {name: '制造部-表面处理', value: 'surface'},
    	        {name: '制造部-组装', value: 'packag'},
    	        {name: '采购部', value: 5},
    	        {name: '外协部', value: 6},
    	    ]
    	}) 

        var active = {

            submit: function(){
                var radioVal = $('input:radio[name="checkRadio"]:checked').val();
                if(step == '1'){
                	if(radioVal == '2'){
                		var strs = low_flag.getValue('valueStr');
                		if(strs.length == 0){
                			layer.msg('请选择驳回处理的部门', {
                       	     btn: [ '知道了']
                         	});
                		}
                    	//alert(low_flag.getValue('valueStr'));
                    }
                }
                var cc = $('#desc').val();
                var strs = low_flag.getValue('valueStr');
                var json = {"bsRecordId":ppid,"bsStepCheckStatus":radioVal,"bsCheckComments":cc,"bsCheckCode":wn,"bsCheckDes":strs}
                
                $.ajax({
                    url: context+'/check/doCheckQuote',
                    type: "post",
                    data: json,
                    dataType: "json",
                    success: function (res) {
                        if(res.result===true){
                            parent.layer.close(index);
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
                    var workFlow = res.data.Lw;
                    for(var i =0;i<res.data.Lw.length;i++){
                        if(workFlow[i].bsCheckGrade ==1){
                            $("#span-1").html(workFlow[i].bsStepName)
                        }else if(workFlow[i].bsCheckGrade ==2){
                            $("#span-2").html(workFlow[i].bsStepName+"("+workFlow[i].bsCheckName+")")
                        }else if(workFlow[i].bsCheckGrade ==3){
                            $("#span-3").html(workFlow[i].bsStepName+"("+workFlow[i].bsCheckName+")")
                        }
                    }
                    var list = res.data;
                    step = list.grade;
                    if(list.grade == '1'){
                        //$("#th").attr("disabled","disabled");//失效
                    	$("#low_flag").show();
                    }else{
                    	$("#low_flag").hide();
                    }
                    //隐藏
                    if(list.grade == '2'){
                    	$("#end-btn").show();
                    	$("#end-span").show();
                    	$("#sure-span").text("总经理审批");
                    }else{
                    	$("#end-btn").hide();
                    	$("#end-span").hide();
                    	$("#sure-span").text("同意");
                    }
                    //end
                    
                  //已完成
                    if(status == '2'){
                    	setLiColor(4)
                    }else{
                    	setLiColor(list.grade)
                    }
                    var timeHtml = '';
                    var nowHtml =''; //当前的步骤流程信息显示在最后面
                    if(list.data.length == 0){
                        if(timeHtml==""){
                            timeHtml += '<li class="layui-timeline-item">' + getIcon(3) +
                                /*  '<i class="layui-icon '+getIcon(list.data[i].bsStepCheckStatus)+'"></i>'+ */
                                ' <div class="layui-timeline-content layui-text">' +
                                '  <div class="layui-timeline-title">' +
                                '   <span style="padding-right:20px;">' + '[' + getRadio(3) + ']'+'</span>  ' +"等待发起审核" +
                                '  </div>' +
                                ' </div>' +
                                ' </li>';
                        }
                        document.getElementById('timeline').innerHTML = timeHtml;
                    }else{
                        for(var i=0; i<list.data.length; i++){
                            if(CheckIsNullOrEmpty(list.data[i].lastupdateDate)){
                                timeHtml += '<li class="layui-timeline-item">'+ getIcon(list.data[i].bsStepCheckStatus)+
                                    /*  '<i class="layui-icon '+getIcon(list.data[i].bsStepCheckStatus)+'"></i>'+ */
                                    ' <div class="layui-timeline-content layui-text">'+
                                    '  <div class="layui-timeline-title">'+
                                    '   <span style="padding-right:20px;">'+'['+getRadio(list.data[i].bsStepCheckStatus)+']'+list.data[i].createDate+'</span>  '+list.data[i].bsCheckName+','+list.data[i].bsCheckComments+
                                    '  </div>'+
                                    ' </div>'+
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
                        if(nowHtml==""){
                            nowHtml = '<li class="layui-timeline-item">' + getIcon(3) +
                                /*  '<i class="layui-icon '+getIcon(list.data[i].bsStepCheckStatus)+'"></i>'+ */
                                ' <div class="layui-timeline-content layui-text">' +
                                '  <div class="layui-timeline-title">' +
                                '   <span style="padding-right:20px;">' + '[' + getRadio(3) + ']'+'</span>  ' +"等待发起审核" +
                                '  </div>' +
                                ' </div>' +
                                ' </li>';
                        }


                        document.getElementById('timeline').innerHTML = timeHtml+nowHtml;
                    }

                }else{
                    layer.msg(res.msg);
                }
                // parent.layer.iframeAuto(index);
            }

        });
    }
    //设置流程信息
    function setLiColor(grade){
    	if(grade == '1'){
    		$("#li-1").attr("class","s-cur");
    		$("#li-2").attr("class","s-cur-next");
    		$("#li-3").attr("class","");
    	}else if(grade == '2'){
    		$("#li-1").attr("class","s-finish");
    		$("#li-2").attr("class","s-cur");
    		$("#li-3").attr("class","s-cur-next");
    	}else if(grade == '3'){
    		$("#li-1").attr("class","s-finish");
    		$("#li-2").attr("class","s-finish");
    		$("#li-3").attr("class","s-cur");
    	}else if(grade == '4'){
    		//已经全部完成
    		$("#li-1").attr("class","s-finish");
    		$("#li-2").attr("class","s-finish");
    		$("#li-3").attr("class","s-finish");
    	}
    }
    
    var ppid;
    var wn='';
    var status=0;
    function child(wname,pid,bsStatus) {
    	console.log()
        //接受流程类型参数
        ppid = pid;
        wn = wname;
        status = bsStatus;
        getInfo({"id":pid,"wname":wname});

        //已完成
        if(status == '2'){
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
    function getIcon(status){
        if(status == 1){
            return "<i class='layui-icon iconfont icon-extend-queren' style='position:absolute;left:-2px;color:#7CCD7C'></i>"
        }else if(status == 2){
            return "<i class='layui-icon iconfont icon-extend-tuihui ' style='position:absolute;left:-2px;color:#EE6A50'></i>"
        }else {
            return "<i class='layui-icon iconfont icon-extend-baogongtongji- ' style='position:absolute;left:-2px;color:#FFFF00'></i>"
        }
    }
