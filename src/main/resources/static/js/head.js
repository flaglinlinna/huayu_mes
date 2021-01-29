/**
 * 菜单
 * */
//获取路径uri
var pathUri=window.location.href;

// console.log("pathUrl:"+pathUri);
$(function(){
	var head_right_action = '<dd><a lay-href="'+context+'/sysUser/toUserInfo">基本资料</a></dd> '+
        '<dd><a lay-href="'+context+'/sysUser/toPassword">修改密码</a></dd> '+
       ' <hr> '+
        '<dd layadmin-event="logout" style="text-align: center;"><a href="'+context+'/logout">退出</a></dd>';
	$("#head_right_action").append(head_right_action);
	
	
    layui.use('element', function(){
        var element = layui.element;
        // 左侧导航区域（可配合layui已有的垂直导航）
        $.get(context+"/sysPermission/getUserPerms",function(data){
            if(data.result){
                getMenus(data.data);
                element.render('nav');
            }else{
                layer.alert(data.msg,function () {
                    //退出
                    window.location.href=context+"/logout";
                });
            }
        });
        
        //获取系统参数
        $.get(context+"/sysParam/getTitle",function(data){
            if(data.result){
                console.log(data.data);
            	if(data.data[0].paramValue){
            		$('#title_span').html(data.data[0].paramValue)
            	}
            	
            }
        });
    });
})
var getMenus=function(data){
    //回显选中
    var ul=$('<ul class="layui-nav layui-nav-tree icon-color" lay-shrink="all" id="LAY-system-side-menu" lay-filter="layadmin-system-side-menu" style="margin-top:0px;"> '+
    		' <li data-name="home" class="layui-nav-item layui-nav-itemed"> '+
    		' <a href="javascript:;" lay-tips="主页" lay-direction="2"> '+
    		'   <i class="layui-icon layui-icon-home"></i> '+
    		'   <cite>主页</cite> '+
    		' </a> '+
    		' <dl class="layui-nav-child"> '+
    		'  <dd data-name="console" class="layui-this"> '+
    		'     <a lay-href="/console">控制台</a>'+
    		'   </dd> '+
    		/*'  <dd data-name="console1" > '+
    		'     <a lay-href="'+context+'/console/toConsole1">控制台1</a> '+
    		'   </dd> '+*/
    		' </dl> '+
    		' </li> '+
    		' 	</ul>');
    for(var i=0;i < data.length;i++){
         
        var node=data[i];
        if( node.ISTYPE==0){
            if(node.PID==0){
                 
                var li=$("<li data-name='"+node.MENU_CODE+"'class='layui-nav-item' ></li>");
               /* //父级无page
                var a=$("<a class='' href='javascript:;' lay-tips='"+node.BS_NAME+"' lay-direction='2'><i class='layui-icon layui-icon-"+node.BS_CODE+"'><cite>"+node.BS_NAME+"</cite></i></a>");
                li.append(a);*/
                var a="";
                //获取子节点
                var childArry = getParentArry(node.ID, data);
                if(childArry.length>0){
                	 //父级无page
                    a=$("<a class='' href='javascript:;' lay-tips='"+node.MENU_NAME+"' lay-direction='2'><i class='layui-icon iconfont "+node.MENU_ICON+"'></i><cite>"+node.MENU_NAME+"</cite></a>");
                    li.append(a);
                    a.append("<span class='layui-nav-more'></span>");
                    var dl=$("<dl class='layui-nav-child'></dl>");
                    for (var y in childArry) {
                        var dd=$("<dd><a lay-href='"+context+childArry[y].PAGE_URL+"'>"+childArry[y].MENU_NAME+"</a></dd>");
                        //判断选中状态
                        if(pathUri.indexOf(childArry[y].PAGE_URL)>0){
                            li.addClass("layui-nav-itemed");
                            dd.addClass("layui-this")
                        }
                        //TODO 由于layui菜单不是规范统一的，多级菜单需要手动更改样式实现；
                        dl.append(dd);
                    }
                    li.append(dl);
                }else{
                	a=$("<a class='' lay-href='"+context+node.PAGE_URL+"'  lay-tips='"+node.MENU_NAME+"' lay-direction='2'><i class='layui-icon iconfont "+node.MENU_ICON+"'><cite>"+node.MENU_NAME+"</cite></i></a>");
                    li.append(a);
                }
                ul.append(li);
            }
        }
    }
    $(".layui-side-scroll").append(ul);
}


var getMenus1=function(data){
    //回显选中
    var ul=$("<ul class='layui-nav layui-nav-tree' lay-filter='test'></ul>");
    for(var i=0;i < data.length;i++){
        var node=data[i];
        if( node.istype==0){
            if(node.pId==0){
                var li=$("<li class='layui-nav-item' flag='"+node.id+"'></li>");
                //父级无page
                var a=$("<a class='' href='javascript:;'>"+node.bs_name+"</a>");
                li.append(a);
                //获取子节点
                var childArry = getParentArry(node.id, data);
                if(childArry.length>0){
                    a.append("<span class='layui-nav-more'></span>");
                    var dl=$("<dl class='layui-nav-child'></dl>");
                    for (var y in childArry) {
                        var dd=$("<dd><a href='"+childArry[y].page_url+"'>"+childArry[y].bs_name+"</a></dd>");
                        //判断选中状态
                        if(pathUri.indexOf(childArry[y].page_url)>0){
                            li.addClass("layui-nav-itemed");
                            dd.addClass("layui-this")
                        }
                        //TODO 由于layui菜单不是规范统一的，多级菜单需要手动更改样式实现；
                        dl.append(dd);
                    }
                    li.append(dl);
                }
                ul.append(li);
            }
        }
    }
    $(".layui-side-scroll").append(ul);
}
//根据菜单主键id获取下级菜单
//id：菜单主键id
//arry：菜单数组信息
function getParentArry(id, arry) {
     
    var newArry = new Array();
    for (var x in arry) {
        if (arry[x].PID == id)
            newArry.push(arry[x]);
    }
    return newArry;
}
//根据菜单主键id获取下级菜单（连接数据为MySql）
//id：菜单主键id
//arry：菜单数组信息
function getParentArryMySql(id, arry) {

    var newArry = new Array();
    for (var x in arry) {
        if (arry[x].pId == id)
            newArry.push(arry[x]);
    }
    return newArry;
}
function updateUsePwd(){
    layer.open({
        type:1,
        title: "修改密码",
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['450px'],
        content:$('#useDetail')
    });
}
//20201127-fyx-获取环境
