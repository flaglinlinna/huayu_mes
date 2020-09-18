/**
 * 生产投入
 */
var pageCurr;
$(function() {
	layui.use(
			[ 'table', 'form', 'layedit', 'laydate', 'tableSelect' ],
			function() {
				var form = layui.form, layer = layui.layer, layedit = layui.layedit, table = layui.table, laydate = layui.laydate, tableSelect = layui.tableSelect;
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
					page : true,
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
						type : 'checkbox',
						width : 50
					},
					{
						field : 'bsName',
						title : '条码'
					},{
						field : 'bsName',
						title : '物料编号'
					}, {
						field : 'bsValue',
						title : '物料描述',
						width : 250
					}, {
						field : 'bsType',
						title : '数量',
						width : 100
					}, {
						field : 'bsType',
						title : '创建人'
					},{
						field : 'bsType',
						title : '创建时间'
					},  {
						field : 'isRequired',
						title : '操作',
						width : 90,
						toolbar : '#optBar'
					} ] ],
					done : function(res, curr, count) {
						pageCurr = curr;
					}
				});

				tableSelect.render({
					elem : '#num',
					searchKey : 'my',
					checkedKey : 'id',
					searchPlaceholder : '自定义文字和name',
					table : {
						//url: 'table.json',
						cols : [ [
						//{ type: 'checkbox' },//多选  radio
						{
							type : 'radio'
						}, {
							field : 'num',
							title : '制令单号',
							width : 110
						}, {
							field : 'mtrcode',
							title : '物料编码',
							width : 110
						},{
							field : 'mtrdescr',
							title : '物料描述',
							width : 240
						}, {
							field : 'linecode',
							title : '拉别',
							width : 60
						},{
							field : 'qty',
							title : '数量',
							width : 60
						},{
							field : 'inqty',
							title : '投入数量',
							width : 80
						}  ] ],
						data : [ {
							"num" : "Z000000006",
							"mtrcode" : "M0000000001",
							"mtrdescr":"测试物料BBBBBBBBBBB",
							"linecode" : "S1",
							"qty":"2000",
							"inqty":"200"
						}, {
							"num" : "Z000000078",
							"mtrcode" : "M0000000301",
							"mtrdescr":"测试物料AAAAAAAAAAAA",
							"linecode" : "S6",
							"qty":"5000",
							"inqty":"200"
						} ],
					},
					done : function(elem, data) {
						//选择完后的回调，包含2个返回值 elem:返回之前input对象；data:表格返回的选中的数据 []
						console.log(data);
						var da=data.data;
						//console.log(da[0].num)
						form.val("itemFrom", {
							"num":da[0].num,
							"mtrcode" : da[0].mtrcode,
							"mtrdescr" : da[0].mtrdescr,
							"qty" : da[0].qty,
							"inqty" : da[0].inqty,
							"linecode":da[0].linecode
						});
						form.render();// 重新渲染
					}
				});
			});	
});

