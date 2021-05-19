/**
 * 各线每日生产报表
 */
var pageCurr;var localtableFilterIns;
$(function() {
	layui.use([ 'form', 'table','tableFilter','laydate', 'tableSelect' ], function() {
		var table = layui.table, form = layui.form,tableFilter = layui.tableFilter,laydate = layui.laydate, tableSelect = layui.tableSelect;

		tableIns = table.render({
			elem : '#listTable',
		//	url : context + '/base/line/getList',
			method : 'get' // 默认：get请求
			, toolbar: '#toolbar' //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
			,cellMinWidth : 80,
			height:'full-80',//固定表头&full-查询框高度
				even:true,//条纹样式
			data : [],
			totalRow :true,
			//height: 'full',
			page : false,
			/*request : {
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
					"count" : res.data.Total,
					"msg" : res.msg,
					"data" : res.data.List,
					"code" : res.status
				// code值为200表示成功
				}
			},*/
			cols : [ [ {fixed:'left',
				type : 'numbers'
			},
				{fixed:'left',
					field : 'DEPT_NAME',
					title : '部门', width : 100,sort: true,totalRowText:"合计"
				},
				{fixed:'left',
					field : 'LINER_NAME',
					title : '组长',width : 80, sort: true
				},
				{fixed:'left',
					field : 'TASK_NO',
					title : '制令单号', width : 150,sort: true
				},
				{fixed:'left',
					field : 'ITEM_NO',
					title : '物料编码',width : 150,sort: true
				},
				{
					field : 'PROD_DATE',
					title : '生产日期',width : 120,sort: true,
					templet:function (d) {
						if(d.PROD_DATE!=null){
							return /\d{4}-\d{1,2}-\d{1,2}/g.exec(d.PROD_DATE)
						}
					}
				},
				{
					field : 'CLASS_NO',
					title : '班次',width : 80,sort: true
				},
				{
				field : 'PRODUCE_STATE',
				title : '生产状态',width : 90
			},
				{
				field : 'QTY_PLAN',
				title : '计划数量', width : 100,totalRow: true
			},
				 {
				field : 'QTY_LAST',
				title : '工单余量',width : 100,totalRow: true
			},{
				field : 'QTY_DONE',
				title : '完工数量', width : 90,totalRow: true
			},{
				field : 'EMP_NUM',
				title : '应勤人数',width : 90,totalRow: true
			}, {
				field : 'EMP_ACT',
				title : '出勤人数', width : 90,totalRow: true
			}, {
				field : 'EMP_ST',
				title : '标准人力', width : 90,totalRow: true
			}, {
				field : 'CAP_ST',
				title : '标准产能',width : 90,totalRow: true
			}, {
				field : 'HOUR_ST',
				title : '标准工时',width : 90,totalRow: true
			}, {
				field : 'HOUR_ACT',
				title : '实际工时',width : 90,totalRow: true
			},{
				field : 'PROD_NO',
				title : '工单号',width : 130
			},

				{
					field : 'CUST_NAME',
					title : '客户名称',width : 120
				},
				{
					field : 'ITEM_NAME',
					title : '物料全称',width : 150
				}
			] ],
			done : function(res, curr, count) {
				//localtableFilterIns.reload();
				//pageCurr = curr;
			}
		});	
		 localtableFilterIns = tableFilter.render({
			'elem' : '#listTable',
			//'parent' : '#doc-content',
			'mode' : 'local',//本地过滤
			'filters' : [
				{field: 'CREATE_DATE', type:'date'},
				{field: 'CUST_NAME_S', type:'input'},
				{field: 'ITEM_NAME_S', type:'input'},
				{field: 'ITEM_MODEL', type:'input'},
				{field: 'PROC_NAME', type:'input'},
				{field: 'NG_RATE', type:'input'},
				{field: 'COLOUR', type:'input'},
				{field: 'LOT_NO', type:'input'},
				{field: 'QTY_PROC', type:'input'},
				{field: 'SAMPLE_QTY', type:'input'},
				{field: 'DEFECT_NUM', type:'input'},
				{field: 'DEFECT_NAME', type:'input'},
				{field: 'USER_NAME', type:'input'},
				//{field: 'LINER_NAME', type:'input'},
			],
			'done': function(filters){}
		})

		// 监听搜索框
		form.on('submit(searchSubmit)', function(data) {
			// 重新加载table
			getReport( data.field.dates,data.field.depart, data.field.num)
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
		  tableSelect=tableSelect.render({
				elem : '#num',
				searchKey : 'keyword',
				checkedKey : 'id',
				searchPlaceholder : '试着搜索',
				table : {
					url:  context +'/report/line_day/getItemList',
					method : 'get',
					cols : [ [
					{ type: 'checkbox' },//多选  radio
					, {
						field : 'id',
						title : 'id',
						width : 0,hide:true
					}, {
						field : 'ITEM_NO',
						title : '物料编码',
						width : 170
					},{
						field : 'ITEM_NAME',
						title : '物料描述',
						width : 240
					}, {
						field : 'ITEM_NAME_S',
						title : '物料简称',
						width : 100
					},{
						field : 'ITEM_MODEL',
						title : '物料类别',
						width : 80
					}] ],
					page : true,
					request : {
						pageName : 'page' // 页码的参数名称，默认：page
						,
						limitName : 'rows' // 每页数据量的参数名，默认：limit
					},
					parseData : function(res) {
						if(res.result){
							return {
								"count" :res.data.Total,
								"msg" : res.msg,
								"data" : res.data.List,
								"code" : res.status
							}
						}
						return {
							"count" :0,
							"msg" : res.msg,
							"data" : [],
							"code" : res.status
						}
						
					},
				},
				done : function(elem, data) {
					//选择完后的回调，包含2个返回值 elem:返回之前input对象；data:表格返回的选中的数据 []
					var da=data.data;
					var nos = "";
					da.forEach(function(element) {
						nos += element.ITEM_NO+",";
					});
					form.val("searchFrom", {
						"num" : nos
					});
					/*form.val("searchFrom", {
						"num":da[0].ITEM_NO
					});*/
					form.render();// 重新渲染
			}
			});
		  
		  
		  layui.form.render('select');
	});

});
function getReport(dates,dept,item) {	
	var params = {
		"dates" : dates,
		"deptId" : dept,
		"itemNo" : item,
	}
	 CoreUtil.sendAjax("/report/line_day/getList", params,
	 		function(data) {
	 			console.log(data.data.length)
	 			if (data.result) {
	 				if (data.result) {
	 					tableIns.reload({
	 						data : data.data,
	 						limit: data.data.length, //显示的数量
	 						done: function(res1, curr, count){
	 							localtableFilterIns.reload();
	 							// merge(data.data,['LINER_NAME'],[2]);
	 						}
	 					});
	 				} else {
	 					layer.alert(data.msg);
	 				}
	 			} else {
	 				layer.alert(data.msg);
	 			}
	 		}, "GET", false, function(res) {
	 			layer.alert(res.msg);
	 		});
	/*tableIns.reload({
		url:context+'/report/line_day/getList',
		where:params,
		done: function(res1, curr, count){
			pageCurr=curr;
		}
	})*/
}
function merge(res,columsName,columsIndex) {
    console.log(res)
    var data = res;
    var mergeIndex = 0;//定位需要添加合并属性的行数
    var mark = 1; //这里涉及到简单的运算，mark是计算每次需要合并的格子数
    //var columsName = ['itemCode'];//需要合并的列名称
    //var columsIndex = [3];//需要合并的列索引值

    for (var k = 0; k < columsName.length; k++) { //这里循环所有要合并的列
        var trArr = $(".layui-table-body>.layui-table").find("tr");//所有行
            for (var i = 1; i < data.length; i++) { //这里循环表格当前的数据
                var tdCurArr = trArr.eq(i).find("td").eq(columsIndex[k]);//获取当前行的当前列
                var tdPreArr = trArr.eq(mergeIndex).find("td").eq(columsIndex[k]);//获取相同列的第一列
                
                if (data[i][columsName[k]] === data[i-1][columsName[k]]) { //后一行的值与前一行的值做比较，相同就需要合并
                    mark += 1;
                    tdPreArr.each(function () {//相同列的第一列增加rowspan属性
                        $(this).attr("rowspan", mark);
                    });
                    tdCurArr.each(function () {//当前行隐藏
                        $(this).css("display", "none");
                    });
                }else {
                    mergeIndex = i;
                    mark = 1;//一旦前后两行的值不一样了，那么需要合并的格子数mark就需要重新计算
                }
            }
        mergeIndex = 0;
        mark = 1;
    }
}
