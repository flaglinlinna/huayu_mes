/**
 * 各部UPPH日报表
 */
var pageCurr;var localtableFilterIns;
$(function() {
	layui.use([ 'form', 'table','tableFilter','laydate', 'tableSelect' ], function() {
		var table = layui.table, form = layui.form,tableFilter = layui.tableFilter,laydate = layui.laydate, tableSelect = layui.tableSelect;

		tableIns = table.render({
			elem : '#listTable',
			method : 'get' // 默认：get请求
			, toolbar: '#toolbar' //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
			,cellMinWidth : 80,
			height:'full-110'//固定表头&full-查询框高度
				,even:true,//条纹样式
			data : [],
			// height: 'full',
			// page : true,
			limit:100,
			totalRow :true,
			request : {
				pageName : 'page' // 页码的参数名称，默认：page
				,
				limitName : 'rows' // 每页数据量的参数名，默认：limit
			},
			parseData : function(res) {
				if(!res.result){
					return {
						"count" : 0,
						"msg" : res.msg,
						"data" : [],
						"code" : res.status
					} 
				}
				// 可进行数据操作
				return {
					// "count" : res.data.Total,
					"msg" : res.msg,
					"data" : res.data,
					"code" : res.status
				// code值为200表示成功
				}
			},
			cols : [
				[
					{ rowspan: 1, title: '基本信息',align:'center',colspan: 6},
					{ rowspan: 1, title: '人力及工时状况',align:'center',colspan: 9},
					{ rowspan: 1, title: '投入产出',align:'center', colspan: 6},
					{ rowspan: 1, title: '品质良率',align:'center', colspan: 4}
				],
				[ {
				type : 'numbers'
			},{
					field : 'LINER_NAME',
					title : '组长',width : 90,  sort: true
				},
					{
						field : 'PROD_DATE',
						title : '产出时间', width : 110,sort: true
					},
					{
				field : 'ITEM_NO',
				title : '物料编码',width : 150,sort: true,totalRowText:"合计"
			},{
					field : 'ITEM_NAME',
					title : '物料名称',width : 150, sort: true
				},
				{
				field : 'CLASS_NO',
				title : '班别', width : 90, sort: true
			}, {
				field : 'CAPACITY',
				title : '标准UPH',width : 100,sort: true,
			},
				{
				field : 'MANPOWER',
				title : '标配人力', width : 100,sort: true,totalRow: true
			},
				{
				field : 'HOUR_SDD',
				title : '标准工时', width : 100,sort: true,totalRow: true
			},
				{
					field : 'DONE_SDD',
					title : '实际小时产能', width : 130,sort: true,totalRow: true
				},
				{
					field : 'EMP_QTY',
					title : '投入人力', width : 110,sort: true,totalRow: true
				},
				{
					field : 'ACT_HOURS',
					title : '投入工时', width : 110,sort: true,totalRow: true
				},
				{
					field : 'ABNORMAL_HOURS',
					title : '损失工时', width : 110,sort: true,totalRow: true
				},
				{
					field : 'NT_HOURS',
					title : '有效工时', width : 110,sort: true,totalRow: true
				},
				{
					field : 'YK_HOURS',
					title : '亏赚工时', width : 110,sort: true,totalRow: true
				},
					{
						field : 'PLAN_IN_QTY',
						title : '计划投入', width : 110,sort: true,totalRow: true
					},
					{
						field : 'ACT_IN_QTY',
						title : '实际投入', width : 110,sort: true,totalRow: true
					},
					{
						field : 'IN_RATE',
						title : '投入达标率', width : 120,sort: true
					},
				{
					field : 'QTY_PLAN',
					title : '计划产出', width : 110,sort: true,totalRow: true
				},
				{
					field : 'QTY_DONE',
					title : '实际产出', width : 110,sort: true,totalRow: true
				},
					{
						field : 'OUT_RATE',
						title : '产出达标率', width : 120,sort: true
					},

				{
					field : 'NG_QTY_A',
					title : '来料不良数', width : 120,sort: true,totalRow: true
				},
				{
					field : 'NG_QTY_B',
					title : '制程不良数', width : 120,sort: true,totalRow: true
				},
				{
					field : 'NG_QTY_C',
					title : '首备测数', width : 110,sort: true,totalRow: true
				},
				{
					field : 'NG_QTY',
					title : '总不良数', width : 110,sort: true,totalRow: true
				},
					// {
					// 	field : 'PROD_DATE',
					// 	title : '分析原因', width : 150,sort: true
					// },

			] ],
			done : function(res, curr, count) {
				//localtableFilterIns.reload();
				pageCurr = curr;
			}
		});	
		 localtableFilterIns = tableFilter.render({
			'elem' : '#listTable',
			//'parent' : '#doc-content',
			'mode' : 'local',//本地过滤
			'filters' : [
				{field: 'PROD_DATE', type:'date'},
				{field: 'LINER_NAME', type:'input'},
				{field: 'CLASS_NO', type:'radio'},
				{field: 'ITEM_NO', type:'input'},
				{field: 'ITEM_NAME', type:'input'},
				// {field: 'NG_RATE', type:'input'},
				// {field: 'COLOUR', type:'input'},
				// {field: 'LOT_NO', type:'input'},
				// {field: 'QTY_PROC', type:'input'},
				// {field: 'SAMPLE_QTY', type:'input'},
				// {field: 'DEFECT_NUM', type:'input'},
				// {field: 'DEFECT_NAME', type:'input'},
				// {field: 'USER_NAME', type:'input'},
				// {field: 'LINER_NAME', type:'input'},
			],
			'done': function(filters){}
		})
		tableIns1 = table.render({
				elem : '#dataTable',
				method : 'get', // 默认：get请求
				toolbar : '#toolbar', // 开启工具栏，此处显示默认图标，可以自定义模板，详见文档
				cellMinWidth : 80,
				even : true,// 条纹样式
				data : [],
				height : 'full-90',
				page : true,
				request : {
					pageName : 'page', // 页码的参数名称，默认：page
					limitName : 'rows' // 每页数据量的参数名，默认：limit
				},
				parseData : function(res) {
					if(!res.result){
						return {
							"count" : 0,
							"msg" : res.msg,
							"data" : [],
							"code" : res.status
						} 
					}
					// 可进行数据操作
					return {
						"count" : res.data.Total,
						"msg" : res.msg,
						"data" : res.data.List,
						"code" : res.status
					// code值为200表示成功
					}
				},
				cols : [ [  {
					type : 'numbers'
				},  {
					field : 'FDATE',
					title : '检验日期',width : 120,sort: true,
				}, {
					field : 'PROC_NAME',
					title : '工序名称',width : 110, sort: true
				},{
					field : 'ITEM_NO',
					title : '产品编码',width : 180, sort: true
				},{
					field : 'QTY_DZC',
					title : '待转出数', width : 110, sort: true
				}, {
					field : 'LOT_DZC',
					title : '待转出批次',width : 110,  sort: true
				},{
					field : 'QTY_JYZ',
					title : '检验中数',width : 110,sort: true
				}, {
					field : 'LOT_JYZ',
					title : '检验中批次',
					width : 110, sort: true
				},{
					field : 'QTY_YJY',
					title : '已检验数', width : 110,sort: true
				},{
					field : 'LOT_YJY',
					title : '已检验批次', width : 110,sort: true
				} ] ],
				done : function(res, curr, count) {
					//
					pageCurr = curr;
				}
			});


		// 监听搜索框
		form.on('submit(searchSubmit)', function(data) {
			// 重新加载table
			getReport( data.field.dates,data.field.keyword)
			return false;
		});

		//日期范围
		  laydate.render({
		    elem: '#dates',
		    trigger:'click'
		    ,range: true
			  ,
			  value:getDateRange(1,0)
		  });

		//获得日期区间 几天前和几天后
		function getDateRange(beforeDay,afterDay){
			//当前日期
			var day1 = new Date();
			day1.setDate(day1.getDate() - beforeDay);
			var day2 = new Date();
			day2.setDate(day2.getDate() + afterDay);
			return day1.getFullYear()+"-" + (day1.getMonth()+1) + "-" + day1.getDate()+" - "
				+day2.getFullYear()+"-" + (day2.getMonth()+1) + "-" + day2.getDate();
		}

		  layui.form.render('select');
	});

});
function getReport(dates,keyword) {
	var params = {
		"dates" : dates,
		"keyword" : keyword,
	}
	tableIns.reload({
		url:context+'/report/rpt_upph/getReport',
		where:params,
		done: function(res1, curr, count){
			pageCurr=curr;
		}
	})
	localtableFilterIns.reload();
}

