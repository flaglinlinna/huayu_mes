/**
 * 报价单-列表
 */
var pageCurr;
$(function() {
	layui.use([ 'table', 'form', 'layedit', 'laydate', 'layer' ,'treeTable'], function() {
		var form = layui.form, layer = layui.layer, laydate = layui.laydate, table = layui.table,treetable = layui.treeTable;
		
		var	re = treetable.render({
			elem: '#tree-table',
			//data: [{"id":1,"pid":0,"title":"1-1"},{"id":2,"pid":0,"title":"1-2"},{"id":3,"pid":0,"title":"1-3"},{"id":4,"pid":1,"title":"1-1-1"},{"id":5,"pid":1,"title":"1-1-2"},{"id":6,"pid":2,"title":"1-2-1"},{"id":7,"pid":2,"title":"1-2-3"},{"id":8,"pid":3,"title":"1-3-1"},{"id":9,"pid":3,"title":"1-3-2"},{"id":10,"pid":4,"title":"1-1-1-1"},{"id":11,"pid":4,"title":"1-1-1-2"}],
			data:permList,
			icon_key: 'bsItemCode',
			parent_key:'parenId',
			is_checkbox: false,
			end: function(e){
				form.render();
			},
			cols: [
				{key: 'bsItemCode',title: '物料编码',width: '100px'},
				{key: '',title: '机型',width: '100px',align: 'center',template: function(item){return ''}},
				{key: 'bsMaterName',title: '产品描述',width: '200px',align: 'center'},
				{title: '工作中心',width: '100px',align: 'center',template: function(r){
						return r.wc==null?'':r.wc.workcenterName;
				    }
				},
				{title: '数量',width: '100px',align: 'center',template: function(r){
						return r.bsQty==null?'':r.bsQty;
				    }
				},
				{title: '单位',width: '100px',align: 'center',template: function(r){
						return r.unit==null?'':r.unit.unitName;
				    }
				},
				{key: 'bsFeeItemAll',title: '材料费用',width: '100px',align: 'center'},
				{title: '单位',width: '100px',align: 'center',template: function(r){
						return r.unit==null?'':r.unit.unitName;
				    }
				},
				{title: '人工费用',width: '100px',align: 'center',template: function(r){
						return r.bsFeeLhAll==null?'':r.bsFeeLhAll;
				    }
				},
				{title: '制造费用',width: '100px',align: 'center',template: function(r){
						return r.bsFeeMhAll==null?'':r.bsFeeMhAll;
				    }
				},
				{title: '外协费用',width: '100px',align: 'center',template: function(r){
						return r.bsFeeOut==null?'':r.bsFeeOut;
				    }
				},
				{title: '合计',width: '100px',align: 'center',template: function(r){
						return r.bsFeeAll==null?'':r.bsFeeAll;
				    }
				},
			]
		});
		// 全部展开
		layui.$('.open-all').click(function(){
			treetable.openAll(re);
		})
		// 全部关闭
		layui.$('.close-all').click(function(){
			treetable.closeAll(re);
		})
		/*var	re = treetable.render({
			elem: '#tree-table',
			//url:'/quoteSum/getQuoteTreeList?quoteId='+quoteId,
			data: [{"id":1,"pid":0,"title":"1-1"},{"id":2,"pid":0,"title":"1-2"},{"id":3,"pid":0,"title":"1-3"},{"id":4,"pid":1,"title":"1-1-1"},{"id":5,"pid":1,"title":"1-1-2"},{"id":6,"pid":2,"title":"1-2-1"},{"id":7,"pid":2,"title":"1-2-3"},{"id":8,"pid":3,"title":"1-3-1"},{"id":9,"pid":3,"title":"1-3-2"},{"id":10,"pid":4,"title":"1-1-1-1"},{"id":11,"pid":4,"title":"1-1-1-2"}],
			//data:permList,
			icon_key: 'title',
			//parent_key:'parenId',
			is_checkbox: true,
			checked: {
				key: 'id',
				data: [0,1,4,10,11,5,2,6,7,3,8,9],
			},
			end: function(e){
				form.render();
			},
			cols: [
				{
					key: 'id',
					title: '名称',
					width: '100px',
					template: function(item){
						if(item.level == 0){
							return '<span style="color:red;">'+item.title+'</span>';
						}else if(item.level == 1){
							return '<span style="color:green;">'+item.title+'</span>';
						}else if(item.level == 2){
							return '<span style="color:#aaa;">'+item.title+'</span>';
						}
					}
				},
				{
					key: 'id',
					title: 'ID',
					width: '100px',
					align: 'center',
				},
				{
					key: 'parenId',
					title: '父ID',
					width: '100px',
					align: 'center',
				}
			]
		});*/
		/*var treeTable = treetable.render({
            elem: '#tableId'
            ,id:'tableTree'
            ,url:'/quoteSum/getQuoteTreeList?quoteId='+quoteId
            ,height: 'full-65'
            ,page: false,
            treeColIndex: 1,//树形图标显示在第几列
            treeSpid: 0,//最上级的父级id
            treeIdName: 'bsItemCode',//id字段的名称
            treePidName: 'parenId',//pid字段的名称
            treeDefaultClose: false,//是否默认折叠
            treeLinkage: true,//父级展开时是否自动展开所有子级
            
            tree: {
                iconIndex: 1,
                isPidData: true,
                idName: 'bsItemCode',
                pidName: 'parenId',
                
            },
            parseData : function(res) {
				if (!res.result) {
					return {
						"count" : 0,
						"msg" : res.msg,
						"data" : [],
						"code" : res.status
					}
				}
				return {
					"count" : res.data.length,
					"msg" : res.msg,
					"data" : res.data,
					"code" : res.status
				}
			}
            ,cols: [[
                {type:'checkbox'},
                {field:'bsItemCode',title: '物料编码',width:259},
                {field:'id',title:'id',width:120,sort:true},
                {field:'parenId',title:'parenId',width:120,sort:true},
                {field:'bsMaterName',title:'产品描述',width:120,sort:true},
            ]]
        });*/


	});
});


