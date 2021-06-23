/**
 * 报价项目清单
 */
var pageCurr;
$(function() {
	layui.use([ 'table', 'form', 'layedit', 'laydate', 'layer' ],
		function() {var form = layui.form, layer = layui.layer, 
		laydate = layui.laydate, table = layui.table;
						// 按钮监听事件

		tableIns = table.render({
			elem : '#productFileList',
			url : context + '/quoteFile/getList?pkQuote=' + quoteId,
			method : 'get', // 默认：get请求
			cellMinWidth : 80,
			// toolbar: '#toolbar',
			height : 'full-65',// 固定表头&full-查询框高度
			even : true,// 条纹样式
			page : true,
			limit:20,
			request : {
				pageName : 'page', // 页码的参数名称，默认：page
				limitName : 'rows' // 每页数据量的参数名，默认：limit
			},
			parseData : function(res) {
				// 可进行数据操作
				return {
					"count" : res.data.total,
					"msg" : res.msg,
					"data" : res.data.rows,
					"code" : res.status
					// code值为200表示成功
				}
			},
			cols : [ [{type : 'numbers'},
				{field : 'bsFileName',title : '文件名称',templet : '<div><a style="cursor: pointer;color: blue;text-decoration:underline;" href="' + context
						+ '/file/get?fsFileId={{d.pkFileId}}" th:href="@{/file/get?fsFileId={{d.pkFileId}}}">{{ d.bsFileName==null?"":d.bsFileName }}</a></div>'},
				{field : 'createBy',title : '创建人',width : 200},
				{field : 'createDate',title : '创建时间',width : 200},
				// {fixed : 'right',title : '操作',align : 'center',toolbar : '#optBar',width : 150}
				] ],
			done : function(res, curr, count) {
				// 如果是异步请求数据方式，res即为你接口返回的信息。
				// 如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
				// console.log(res);
				// 得到当前页码
				// console.log(curr);
				// 得到数据总量
				// console.log(count);
				pageCurr = curr;
			}
		});

		setData();
		setTable();
		form.on('submit(saveData)',function() {
			saveData()
		});
	});
});

function saveDate(){}

function setData(){
	var data_info=info.data;
	var create=checkNull(data_info.createDate)
	$("#header1").text("报价单号："+data_info.bsCode)
	$("#header3").text("产品型号："+data_info.bsProd)
	$("#header4").text("创建人："+data_info.createBy)
	$("#header5").text("创建时间："+create)
	for(var i=0;i<QuoteType.length;i++){
		if(QuoteType[i].subCode==data_info.bsType){
			$("#header2").text("报价类型："+QuoteType[i].subName)//取文字
		}
	}
}
function setTable(){
	var list = ItemList.data;
	var header=ItemList.data.quote

	var data_info=info.data;
	var titleInfo = "("+data_info.bsCode.substring(data_info.bsCode.length-4)+")";
	//console.log(ItemList)
	var html = "";
	for (var j = 0; j < list.length; j++) {
		var arr = list[j];
		//console.log(arr)
		var link="";
		
		var beg = checkNull(arr.bsBegTime)
		var end = checkNull(arr.bsEndTime) 
		var person= checkNull(arr.bsPerson) 
		
		var status=""
		var status_color1="blue"//状态颜色
		var	status_color2 ="bgblue"
		if(arr.bsStatus=="0"){
			status= "未开始"
			status_color1="black"
			status_color2 ="bgblack"
		}else if(arr.bsStatus=="1"){
			status= "进行中"
			status_color1="green"
			status_color2 ="bggreen"
		}else if(arr.bsStatus=="2"){
			status= "已完成"
			status_color1="orange"
			status_color2 ="bgorange"
		}
		if(arr.bsCode.indexOf("B00") != -1 ){
			//材料
			link = "/productMater/toProductMater?bsType="+Style+"&quoteId="+arr.quote.id+"&bsCode="+arr.bsCode;
		}else if(arr.bsCode.indexOf("C00") != -1){
			link = "/productProcess/toProductProcess?bsType="+Style+"&quoteId="+arr.quote.id+"&bsCode="+arr.bsCode;
		}
		html += '<tr><td class="td1" style="width: 20%; "><button type="button" class="el-button el-button--success el-button--mini is-plain" style="width: 75%; padding: 5px 0px;" onclick=toPage("'+link+'","'+arr.bsName+titleInfo+'")><span>' + arr.bsName +
		'</span></button></td><td  class="td1 '+status_color1+'" style="width: 20%;"><span class="circle '+status_color2+'"></span>' +  status + 
		'</td><td class="td1" style="width: 20%;">' + person
				+ '</td><td class="td1" style="width: 20%;">'+beg+
				'</td><td class="td1" style="width: 20%;">'+end+'</td></tr>';
	}
	$("#tableList").empty();
	$("#tableList").append(html);
}
function checkNull(str){
	if(str==null){
		return ""
	}
	return str;
}
function toPage(link,title){
	parent.layui.index.openTabsPage(link,title);
}

