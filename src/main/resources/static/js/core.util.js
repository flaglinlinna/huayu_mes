/*CoreUtil*/
/*工具类，类似java静态工具类*/
var CoreUtil = (function () {
    var coreUtil = {};
    /*ajax请求*/
    coreUtil.sendAjax = function (url, params, ft, method, headers,noAuthorityFt,contentType, async) {
        var roleSaveLoading = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.1});
        $.ajax({
            url:  context+url,
            cache: false,
            async: async == undefined ? true : async,
            data: params,
            type: method == undefined ? "POST" : method,
            contentType: contentType == undefined ? 'application/json; charset=UTF-8': contentType ,
            dataType: "json",
            beforeSend: function(request) {
                /*if(headers == undefined){

                }else if(headers){
                    request.setRequestHeader("authorization", CoreUtil.getData("access_token"));
                    request.setRequestHeader("refresh_token", CoreUtil.getData("refresh_token"));
                }else {
                    request.setRequestHeader("authorization", CoreUtil.getData("access_token"));
                }*/

            },
            success: function (res) {
                top.layer.close(roleSaveLoading);
                if(ft!=null&&ft!=undefined){
                    ft(res);
                }
            },
            error:function (XMLHttpRequest, textStatus, errorThrown) {
                top.layer.close(roleSaveLoading);
               if(XMLHttpRequest.status==404){
                    top.window.location.href= context+"/index/404";
                }else{
                    layer.msg("服务器好像除了点问题！请稍后试试");
                }
            }
        });
    };
    
    /**/
   /* coreUtil.table = function (elem,url, where, method, toolbar,page,defaultToolbar,initCol) {
       // var roleSaveLoading = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.1});
        var tableIns=table.render({
            elem: '#'+elem
            ,url:context+url
            ,toolbar: '#'+toolbar //开启头部工具栏，并为其绑定左侧模板
            ,method: method?method:'get' //默认：get请求
            ,where:where
            ,cellMinWidth: 80,limit:20
            ,defaultToolbar:defaultToolbar
            //,even: true
            ,page: page?true:false,
            request: {
                pageName: 'page' //页码的参数名称，默认：page
                ,limitName: 'rows' //每页数据量的参数名，默认：limit
            },
            parseData: function (res) {
            	if(res.result){
            		return {
                   	 "code": res.code,//code值为200表示成功
                       "count": 0,
                       "msg":res.msg,
                       "data":[]
                   }
            	}
                // 可进行数据操作
                return {
                	 "code": 0,//code值为200表示成功
                    "count": res.count,
                    "msg":res.msg,
                    "data":res.data
                }
            },
            cols: [initCol]
            ,  done: function(res, curr, count){
                //如果是异步请求数据方式，res即为你接口返回的信息。
                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                pageCurr=curr;
                //merge(res);
            }
        });
        return tableIns;
    };*/
    /*表单数据封装成 json String*/
    coreUtil.formJson = function (frm) {  //frm：form表单的id
        var o = {};
        var a = $("#"+frm).serializeArray();
        $.each(a, function() {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [ o[this.name] ];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return JSON.stringify(o);
    };
    /*存入本地缓存*/
    coreUtil.setData = function(key, value){
        layui.data('LocalData',{
            key :key,
            value: value
        })
    };
    /*从本地缓存拿数据*/
    coreUtil.getData = function(key){
        var localData = layui.data('LocalData');
        return localData[key];
    };


    coreUtil.formattime=function (val) {
        var date=new Date(val);
        var year=date.getFullYear();
        var month=date.getMonth()+1;
        month=month>9?month:('0'+month);
        var day=date.getDate();
        day=day>9?day:('0'+day);
        var hh=date.getHours();
        hh=hh>9?hh:('0'+hh);
        var mm=date.getMinutes();
        mm=mm>9?mm:('0'+mm);
        var ss=date.getSeconds();
        ss=ss>9?ss:('0'+ss);
        var time=year+'-'+month+'-'+day+' '+hh+':'+mm+':'+ss;
        return time;
    };
    return coreUtil;
})(CoreUtil, window);
