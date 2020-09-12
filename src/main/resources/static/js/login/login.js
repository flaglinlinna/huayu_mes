layui.use(['layer', 'form', 'jquery'], function() {
    //let $ = layui.jquery,
	var $ = layui.jquery,
        layer = layui.layer,
        form = layui.form;
    
    form.on('submit(login)', function () {
    	var index = layer.load();
        $.ajax({
            type: 'get',
            url: nginx_url + '/login',
            data: {
                'username': $('#loginId').val(),
                'password': $('#password').val()
            },
            dataType: 'json',
            async: false,
         // 发送cookie
            xhrFields:{
            	withCredentials: true
            },
            success: function (result) {
                layer.close(index);
                if (result.result) {
                    $.cookie("loginId", result.data.userCode);
                    setTimeout(function() {
                        layer.msg('登录成功', {
                            time: 800,
                            icon: 1
                        }, function () {
                            window.location.href = 'index.html';
                        });
                    }, 800);
                }else{
                	layer.msg(result.msg, {icon: 2});
                }
            }
        });
        return false;
    });
});
