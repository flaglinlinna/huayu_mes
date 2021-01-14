/**
 * 报价项目清单
 */
var pageCurr;
$(function() {
	layui.use([ 'table', 'form', 'layedit', 'laydate', 'layer' ],
		function() {var form = layui.form, layer = layui.layer, 
		laydate = layui.laydate, table = layui.table;
						// 按钮监听事件
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
	$("#header2").text("报价类型："+data_info.bsType)
	$("#header3").text("产品型号："+data_info.bsProd)
	$("#header4").text("创建人："+data_info.createBy)
	$("#header5").text("创建时间："+create)
}
function setTable(){
	var list = ItemList.data;
	var header=ItemList.data.quote
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
		html += '<tr><td class="td1" style="width: 20%; "><button type="button" class="el-button el-button--success el-button--mini is-plain" style="width: 75%; padding: 5px 0px;" onclick=toPage("'+link+'","'+arr.bsName+'")><span>' + arr.bsName + 
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

