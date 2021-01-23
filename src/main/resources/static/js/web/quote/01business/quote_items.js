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
	var list_temp = ItemList.data;
	var list=[];
	var header=ItemList.data.quote	
	for(var i=0;i<list_temp.length;i++){//项目顺序排序-2021-01-23
		var temp=list_temp[i]
		if(temp.bsCode=="A001"){
			list.splice(0, 0, temp)
		}else if(temp.bsCode=="A002"){
			list.splice(1, 0, temp)
		}else if(temp.bsCode=="A003"){
			list.splice(2, 0, temp)
		}else if(temp.bsCode=="A004"){
			list.splice(3, 0, temp)
		}
	}
	var html = "";
	for (var j = 0; j < list.length; j++) {
		var arr = list[j];
		
		var link="";
		
		var beg = checkNull(arr.bsBegTime)
		var end = checkNull(arr.bsEndTime) 
		
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
		}else if(arr.bsStatus=="3"){
			status= "不需要报价"
		}
		
		if(arr.bsCode=="A001"){//外购件清单
			link="/quote/toQuoteBom?quoteId="+arr.quote.id+"&code="+arr.bsCode
		}else if(arr.bsCode=="A002"){//"产品资料"
			link="/quoteFile/toProductFile?quoteId="+arr.quote.id+"&code="+arr.bsCode
		}else if(arr.bsCode=="A003"){//"模具清单"
			link="/quoteMould/toQuoteMould?quoteId="+arr.quote.id+"&code="+arr.bsCode
		}else if(arr.bsCode=="A004"){//"工艺流程"
			link="/quoteProcess/toQuoteProcess?quoteId="+arr.quote.id+"&code="+arr.bsCode
		}
		html += '<tr><td class="td1" style="width: 20%; "><button type="button" class="el-button el-button--success el-button--mini is-plain" style="width: 75%; padding: 5px 0px;" onclick=toPage("'+link+'","'+arr.bsName+'")><span>' + arr.bsName + 
		'</span></button></td><td  class="td1 '+status_color1+'" style="width: 20%;"><span class="circle '+status_color2+'"></span>' +  status + 
		'</td><td class="td1" style="width: 20%;">' + arr.bsPerson
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

