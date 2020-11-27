/**
 * 权限列表
 */
$(function() {
    //初始化treegrid 页面表格
    layui.config({
        base: context+'/treegrid/'
    }).use(['laytpl', 'treegrid'], function () {
        var laytpl = layui.laytpl,
            treegrid = layui.treegrid;
        treegrid.config.render = function (viewid, data) {
            var view = document.getElementById(viewid).innerHTML;
            return laytpl(view).render(data) || '';
        };

        var treeForm=treegrid.createNew({
            elem: 'permTable',
            view: 'view',
            data: { rows: permList },
            order: 'zindex',
            parentid: 'parentId',
            singleSelect: false
        });
        treeForm.build();

    });
    //操作
    layui.use([ 'form', 'table','tableSelect' ], function(){
        var form = layui.form,table = layui.table,tableSelect = layui.tableSelect;
        //监听提交
        form.on('submit(permSubmit)', function(data){
            //校验 TODO
            $.ajax({
                type: "POST",
                data: $("#permForm").serialize(),
                url: context+"/sysOrg/add",
                success: function (data) {
                    if (data.result) {
                        layer.alert("操作成功",function(){
                            layer.closeAll();
                        });
                    } else {
                        layer.alert(data.msg);
                    }
                },
                error: function (data) {
                    layer.alert("操作请求错误，请您稍后再试");
                }
            });
            return false;
        });
        form.render();
        
        tableSelect = tableSelect.render({// 返工扫描-制令单
			elem : '#leadBy',
			searchKey : 'keyword',
			checkedKey : 'id',
			searchPlaceholder : '试着搜索',
			table : {
				url:  context +'/base/linerImg/getEmpCode',
				method : 'get',
				cols : [ [
					{ type: 'radio' },
					, {
						field : 'ID',
						title : 'id',
						width : 0,hide:true
					}, {
						field : 'EMP_NAME',
						title : '员工姓名',
						width : 150
					}, {
						field : 'DEPT_NAME',
						title : '部门名称',
						width : 150
					},
					{
						field : 'EMP_CODE',
						title : '员工编号',
						width : 150
					},
				] ],
				page : true,
				request : {
					pageName : 'page', // 页码的参数名称，默认：page
					limitName : 'rows' // 每页数据量的参数名，默认：limit
				},
				parseData : function(res) {
					if(res.result){
						// 可进行数据操作
						return {
							"count" : res.data.Total,
							"msg" : res.msg,
							"data" : res.data.Rows,
							"code" : res.status
							// code值为200表示成功
						}
					}

				},
			},
			done : function(elem, data) {
				//选择完后的回调，包含2个返回值 elem:返回之前input对象；data:表格返回的选中的数据 []
				form.val("permForm", {
					"leadById":data.data[0].ID,
					"leadBy":data.data[0].EMP_NAME
				});
				form.render();// 重新渲染
			}
		});
    });

});

function edit(id,type){
    if(null!=id){
        $("#type").val(type);
        $("#id").val(id);
        $.get(context+"/sysOrg/getPerm",{"id":id},function(data) {
            if(data.result){
            	console.log(data)
            	$("#orgName").val(data.data.orgName);
                $("#orgCode").val(data.data.orgCode);
                $("#leadBy").val(data.data.leadBy);
                $("#mobile").val(data.data.mobile);
                $("#flevel").val(data.data.flevel);
                $("#zindex").val(data.data.zindex);
                $("#empNum").val(data.data.empNum);
                $("#description").val(data.data.description);
                $("#parentId").val(data.data.parentId);
                $("#leadById").val(data.data.leadById);
                
               data.data.istype==0?$("input[name='menuType']").val(0).checked:$("input[name='menuType']").val(1).checked;
              
               var index= layer.open({
                    type:1,
                    title: "部门编辑",
                    fixed:false,
                    resize :false,
                    shadeClose: true,
                    area: ['650px', '500px'],
                    content:$('#updatePerm'),
                    end:function(){
                        location.reload();
                    }
                });
               layer.full(index);//弹出框全屏
            }else{
                layer.alert(data.msg);
            }
        });
    }
}
//开通权限
function addPerm(pid,flag){
    if(null!=pid){
        //flag[0:开通权限；1：新增子节点权限]
        //type[0:编辑；1：新增]
        if(flag==0){
            $("#type").val(1);
            $("#parentId").val(0);
        }else{
            //设置父id
            $("#type").val(1);
            $("#parentId").val(pid);
        }
        var index =layer.open({
            type:1,
            title: "新增",
            fixed:false,
            resize :false,
            shadeClose: true,
            area: ['500px', '500px'],
            content:$('#updatePerm'),  //页面自定义的div，样式自定义
            end:function(){
                location.reload();
            }
        });
        layer.full(index);//弹出框全屏
    }
}

function del(id,name){
     //console.log("===删除id："+id);

    if(null!=id){
        layer.confirm('您确定要删除'+name+'权限吗？', {
            btn: ['确认','返回'] //按钮
        }, function(){
            $.post(context+"/sysOrg/delete",{"id":id},function(data){
            	//alert(data.result)
                if(data.result){
                    //回调弹框
                    layer.alert("删除成功！",function(){
                        layer.closeAll();
                        //加载load方法
                        location.reload();//自定义
                    });
                }else{
                    layer.alert(data.msg);//弹出错误提示
                }
            });
        }, function(){
            layer.closeAll();
        });
    }

}

//关闭弹框
function close(){
    layer.closeAll();
}