/**
 * 权限列表
 */
$(function() {
	layui.use('upload', function(){
/*	  layui.config({
		    base: '../../../layuiadmin/' //静态资源所在路径
		  }).extend({
		    index: 'lib/index' //主入口模块
		  }).use(['index', 'upload'], function(){*/
		  var upload = layui.upload;
		   
		  //执行实例
		  var uploadInst = upload.render({
		    elem: '#test1' //绑定元素
		    ,url: '/upload/' //上传接口
		    ,done: function(res){
		      //上传完毕回调
		    }
		    ,error: function(){
		      //请求异常回调
		    }
		  });
		//同时绑定多个元素，并将属性设定在元素上
		    upload.render({
		      elem: '.test-upload-demoMore'
		      ,before: function(){
		        layer.tips('接口地址：'+ this.url, this.item, {tips: 1});
		      }
		      ,done: function(res, index, upload){
		        var item = this.item;
		        //console.log(item); //获取当前触发上传的元素，layui 2.1.0 新增
		      }
		    })
		});

});
