<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path;
	String realPath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "store";
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>添加衣物</title>
	<link type="text/css" href="<%=basePath%>/zui-1.5.0/css/zui.min.css" rel="stylesheet" />
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/jquery/jquery.js"></script>
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/js/zui.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/datatable/zui.datatable.js"></script>
	<link type="text/css" href="<%=basePath%>/zui-1.5.0/lib/datatable/zui.datatable.css" rel="stylesheet" />
	<script src="<%=basePath%>/js/jquery/jquery.form.js" type="text/javascript"></script>
	<script type="text/javascript" src="<%=basePath%>/js/jquery/jquery.json-2.2.js"></script>
	<script type="text/javascript" src="<%=basePath%>/js/ieplugin.js"></script>
	
	<!--  art.dialog  -->
	<link href="<%=basePath%>/js/artDialog4.1.7/skins/blue.css" rel="stylesheet" type="text/css" />
	<script src="<%=basePath%>/js/artDialog4.1.7/artDialog.js" type="text/javascript"></script>
	<script src="<%=basePath%>/js/artDialog4.1.7/plugins/iframeTools.source.js" type="text/javascript"></script>
		
	<link href="<%=basePath%>/js/ewashing/addClothes.css" rel="stylesheet" type="text/css" />
	
	<!--  Ztree  -->
    <link rel="stylesheet" href="<%=basePath%>/zui-1.5.0/lib/datetimepicker/datetimepicker.min.css">
    <script src="<%=basePath%>/zui-1.5.0/lib/datetimepicker/datetimepicker.min.js"></script>
    
    <!-- vue1.0 -->
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/js/vue1.0/iview.css">
    <script type="text/javascript" src="<%=basePath%>/js/vue1.0/vue.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/js/vue1.0/vue-resource.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/js/vue1.0/iview.min.js"></script>

	<style type="text/css">
		.nav_tit {
			height: 40px;
			font: 14px/40px 'Microsoft Yahei';
			color: #000;
			padding-left: 25px;
			background-image: url(<%=basePath%>/common/images/public/next.gif);
			background-repeat: no-repeat;
			background-position: 10px 13px;
			background-color: #EDF6FA;
			border-bottom: 1px double #D7E4EA;
		}
		.nav_tit em {
			height: 40px;
			font: 14px/40px 'Microsoft Yahei';
			color: #000;
			float: left;
		}
		.span {
			border-bottom: solid 1px #000;
			border-right: solid 1px #000;
			border-left: solid 1px #000;
			border-top: solid 1px #000;
			margin-left: 20px;
			line-height: 100px;
		}
		.nav_tit em {
			height: 40px;
			font: 14px/40px 'Microsoft Yahei';
			color: #000;
			float: left;
		}
		a {
			background-color: #ddeedd;
		}
		.black_overlay {
			display: none;
			position: absolute;
			top: 0%;
			left: 0%;
			width: 100%;
			height: 100%;
			background-color: black;
			z-index: 1001;
			-moz-opacity: 0.8;
			opacity: .80;
			filter: alpha(opacity = 80);
		}
		.white_content {
			display: none;
			position: relative;
			width: 30%;
			height: 30%;
			margin-left:38%;
			margin-bottom:35%;
			border: 2px solid lightblue;
			background-color: white;
			z-index: 1002;
			overflow: auto;
		}
		.white_content_small {
			display: none;
			position: absolute;
			top: 20%;
			left: 30%;
			width: 40%;
			height: 50%;
			border: 2px solid lightblue;
			background-color: white;
			z-index: 1002;
			overflow: auto;
		}
</style>


<script type="text/javascript">

	jQuery(document).ready(function(){
		// 初始选中第一个面板
		$("#tabType").val(1);
		var dataQueryKey =$("#dataQueryKey").val();
		// 加载所以数据
		for(var i =1;i<6; i++){
			getDate(i,dataQueryKey);
		}
		// 隐藏其他记录,显示第一个tab
		$("#dataHtml2").hide();
		$("#dataHtml3").hide();
		$("#dataHtml4").hide();
		$("#dataHtml5").hide();
		// 定位光标到条码输入区
		$("#barCode").focus();
	});
	
	

 	// 监听文本值
	function monitorText(){
		var tabNo =$("#tabType").val();
		var dataQueryKey =$("#dataQueryKey").val();
		getDate(tabNo,dataQueryKey);
	}
 	
	
	// ajax 循环获取tab 数据
	function getDate(tabNo,dataQueryKey){
		
		var serviceType=$('input:radio[name="serviceType"]:checked').val();
		
		jQuery.ajax({
			url : "<%=basePath%>/data/queryDataByType",
			type : "post",
			cache : false,
			dataType : "json",
			data : {'dataType':tabNo,'queryKey':dataQueryKey,'serviceType':serviceType},
			success : function(data, textStatus) {
				if(data.dataList!=null){
					$("#dataHtml"+tabNo).html("");
					var tableStr =$("#dataHtml"+tabNo);
					var count =data.dataList.length%3;
					var row =0;
					if(count==0){
						row =parseInt(data.dataList.length/3);
					}else{
						row =parseInt(data.dataList.length/3)+1;
					}
					var serviceType=$('input:radio[name="serviceType"]:checked').val();
					for(var i=0;i<row;i++){
						var begin =i*3;
						var end =i*3+3;
						var array =data.dataList.slice(begin,end);
						var price="";
						console.log('--len='+array.length);
						for(var j=0;j<array.length;j++){
							var dataName=array[j].dataName;
							var url ="http://ouanv8ds6.bkt.clouddn.com/ewashing/"+array[j].imagePath;

							if(tabNo=='1'){
								if(serviceType=='1'){
									price=array[j].price;
								}
								if(serviceType=='2'){
									price=array[j].commonPrice
								}
								if(serviceType=='3'){
									price=array[j].luxuriesPrice
								}
								if(jmz.GetLength(dataName)>6){
									dataName ="<div style='padding-top: 30px;' ><p>"+dataName.substring(0,6)+"</p><p>"+dataName.substring(6,jmz.GetLength(dataName))+"</p></div>";
									tableStr.append('<div id="'+array[j].id+'" class="onmouseOver" onclick="selectBox(\''+array[j].dataName+'\',\''+array[j].id+'\',\''+array[j].price+'\',\''+array[j].commonPrice+'\',\''+array[j].luxuriesPrice+'\',this)" style="border: 1px solid;border-color:#d3d3d3;width: 100px;height: 100px;text-align: center;vertical-align: middle;cursor: pointer;float: left; margin-top: 5px;  margin-left: 8px;">'+dataName+'<font color="red">('+price+')</font>'+'</div>');
								}
								else{
									tableStr.append('<div id="'+array[j].id+'" class="onmouseOver" onclick="selectBox(\''+array[j].dataName+'\',\''+array[j].id+'\',\''+array[j].price+'\',\''+array[j].commonPrice+'\',\''+array[j].luxuriesPrice+'\',this)" style="border: 1px solid;border-color:#d3d3d3;width: 100px;height: 100px;text-align: center;vertical-align: middle;cursor: pointer;float: left; margin-top: 5px;line-height: 100px;  margin-left: 8px;">'+dataName+'<font color="red">('+price+')</font>'+'</div>');
								}
								
							}
							else if(tabNo=='2')
							{
								if(jmz.GetLength(dataName)>6){
									dataName ="<div style='padding-top: 10px;' ><p>"+dataName.substring(0,6)+"</p><p>"+dataName.substring(6,jmz.GetLength(dataName))+"</p></div>";
									tableStr.append('<div id="'+array[j].id+'"  class="onmouseOver" onclick="selectBox(\''+array[j].dataName+'\',\''+array[j].id+'\',\''+array[j].price+'\',\''+array[j].commonPrice+'\',\''+array[j].luxuriesPrice+'\',this)" style="border: 1px solid;border-color:#d3d3d3;width: 100px;height: 50px;text-align: center;vertical-align: middle;cursor: pointer;float: left; margin-top: 5px;  margin-left: 8px;font-size:18px">'+dataName+'</div>');
								}else{
									var bjColor="#C8EBFA";
									tableStr.append('<div id="'+array[j].id+'"   class="onmouseOver" onclick="selectBox(\''+array[j].dataName+'\',\''+array[j].id+'\',\''+array[j].price+'\',\''+array[j].commonPrice+'\',\''+array[j].luxuriesPrice+'\',this)" style="background-color:'+bjColor+';border: 1px solid;border-color:#d3d3d3;width: 100px;height: 50px;text-align: center;vertical-align: middle;line-height: 50px;cursor: pointer;float: left; margin-top: 5px;  margin-left: 8px;font-size:18px">'+dataName+'</div>');
								}
							}
							else{
								if(jmz.GetLength(dataName)>6){
									dataName ="<div style='padding-top: 30px;' ><p>"+dataName.substring(0,6)+"</p><p>"+dataName.substring(6,jmz.GetLength(dataName))+"</p></div>";
									tableStr.append('<div id="'+array[j].id+'"  class="onmouseOver" onclick="selectBox(\''+array[j].dataName+'\',\''+array[j].id+'\',\''+array[j].price+'\',\''+array[j].commonPrice+'\',\''+array[j].luxuriesPrice+'\',this)" style="border: 1px solid;border-color:#d3d3d3;width: 100px;height: 100px;text-align: center;vertical-align: middle;cursor: pointer;float: left; margin-top: 5px;  margin-left: 8px;">'+dataName+'</div>');
								}else{
									tableStr.append('<div id="'+array[j].id+'"  class="onmouseOver" onclick="selectBox(\''+array[j].dataName+'\',\''+array[j].id+'\',\''+array[j].price+'\',\''+array[j].commonPrice+'\',\''+array[j].luxuriesPrice+'\',this)" style="border: 1px solid;border-color:#d3d3d3;width: 100px;height: 100px;text-align: center;vertical-align: middle;line-height: 100px;cursor: pointer;float: left; margin-top: 5px;  margin-left: 8px;">'+dataName+'</div>');
								}
							}
						}
						tableStr.append("\n\r")
					}
					$("#dataHtml"+tabNo).append(tableStr);
				}
			}
		});
	}
	
	
	
	var jmz = {};
	jmz.GetLength = function(str) {
	    ///<summary>获得字符串实际长度，中文2，英文1</summary>
	    ///<param name="str">要获得长度的字符串</param>
	    var realLength = 0, len = str.length, charCode = -1;
	    for (var i = 0; i < len; i++) {
	        charCode = str.charCodeAt(i);
	        if (charCode >= 0 && charCode <= 128) realLength += 1;
	        else realLength += 2;
	    }
	    return realLength;
	};
	// 判断box 是否选中
	function isCheck(id) {
		// 当前已选择的信息
		var css = $("#" + id).attr("style");
		if (css.indexOf("background-color: rgb(17, 10, 61)") >= 0) {
			return true;
		} else {
			return false;
		}
	}

	// 选择box
	var attach = {};
	var attachList = [];
	function selectBox(text, id, price, commonPrice, luxuriesPrice, obj) {

		// 设置三种服务的金额
		$("#price").val(commonPrice);
		$("#commonPrice").val(commonPrice);
		$("#luxuriesPrice").val(luxuriesPrice);

		// 需要先选择服务类型，才能确定价格
		var serviceType = $('input:radio[name="serviceType"]:checked').val();
		if (serviceType == null || serviceType == '') {
			art.dialog.alert("请选择服务类型！");
			return;
		}

		var barCode = $("#barCode").val();
		barCode = 1;
		if (barCode == null || barCode == '') {
			art.dialog.alert("请先设置衣服条码！");
			return;
		}

		// 当前选项卡的index
		var currentTabType = parseInt($("#tabType").val());
		// 当前已选择的信息
		var currentSelect = $("#content_div").val();

		if (isCheck(id) == true) {
			obj.style.backgroundColor = "white";
		} else {
			// 增加没有的
			obj.style.backgroundColor = "#110a3d";
		}

		if (currentTabType == 1) {
			// 已经存在
			var clothesName = $("#clothesName").val();
			if (clothesName != null && clothesName != '') {
				$("#" + clothesName).css({
					"backgroundColor" : "#ffffff"
				});
				$("#clothesName").val("");
				$("#t_name").html("");
			}
			
			// 设置新的值
			if(clothesName!=id){
				$("#clothesName").val(id);
				$("#t_name").html('<font color=red>'+"【"+text+"】"+'</font>');
			}
			
			
			if (serviceType == '1') {
				$("#sumAmount").val(price);
			}
			if (serviceType == '2') {
				$("#sumAmount").val(commonPrice);
			}
			if (serviceType == '3') {
				$("#sumAmount").val(luxuriesPrice);
			}
		}
		if (currentTabType == 2) {
			var color = $("#color").val();
			// 已经存在
			if (color != null && color != '') {
				$("#" + color).css({
					"backgroundColor" : "#ffffff"
				});
				//去掉之前的值
				$("#color").val("");
				$("#t_color").html("");
			}
			
			// 设置新的值
			if(color!=id){
				$("#color").val(id);
				$("#t_color").html('<font color=red>'+"【"+text+"】"+'</font>');
			}
		}
		if (currentTabType == 3) {
			var brand = $("#brand").val();
			if (brand != null && brand != '') {
				$("#" + brand).css({
					"backgroundColor" : "#ffffff"
				});
				//去掉之前的值
				$("#brand").val("");
				$("#t_brand").html("");
			}
			if(brand!=id){
				$("#brand").val(id);
				$("#t_brand").html('<font color=red>'+"【"+text+"】"+'</font>');
			}
			
		}
		if (currentTabType == 4) {
						
			var flaw = $("#flaw").val();
			if (flaw == null || flaw == '') {
				$("#flaw").val(id);
				$("#t_flaw").html('<font color=red>'+"【"+text+"】"+'</font>');
			} else {
				if (flaw.indexOf(id) != -1) {
					$("#flaw").val(flaw.replace(id, ''));
					var oldtext =$("#t_flaw").text();
					oldtext =oldtext.replace("【"+text+"】","");
					$("#t_flaw").html('<font color=red>'+oldtext+'</font>');
				} else {
					$("#flaw").val(flaw + "," + id);
					var oldtext =$("#t_flaw").text();
					oldtext =oldtext+"【"+text+"】";
					$("#t_flaw").html('<font color=red>'+oldtext+'</font>');
				}
			}
		}
		if (currentTabType == 5) {

			if (isCheck(id) == true) {
				ShowDiv('MyDiv', 'fade', id, text);
				$("#attachBarCode").focus();
			}
			var attachment = $("#attachment").val();
			if (attachment == null || attachment == '') {
				$("#attachment").val(id);
				$("#t_attach").html('<font color=red>'+"【"+text+"】"+'</font>');
			} else {
				
				if (attachment.indexOf(id) != -1) {
					$("#attachment").val(attachment.replace(id, ''));
					var oldtext =$("#t_attach").text();
					oldtext =oldtext.replace("【"+text+"】","");
					$("#t_attach").html('<font color=red>'+oldtext+'</font>');
					remove(id);
				} else {
					$("#attachment").val(attachment + "," + id);
					var oldtext =$("#t_attach").text();
					oldtext =oldtext+"【"+text+"】";
					$("#t_attach").html('<font color=red>'+oldtext+'</font>');
				}
				
				/* if (attachment.indexOf(id) != -1) {
					$("#attachment").val(attachment.replace(id, ''));
					remove(id);
				} else {
					$("#attachment").val(attachment + "," + id);
				} */
			}
		}
	}

	//弹出隐藏层
	function ShowDiv(show_div, bg_div, id, boxText) {
		document.getElementById(show_div).style.display = 'block';
		document.getElementById(bg_div).style.display = 'block';
		var bgdiv = document.getElementById(bg_div);
		bgdiv.style.width = "100px;";
		$("#" + bg_div).height("50px;");
		// 先清空，再赋值
		$("#boxId").val("");
		$("#boxText").val("");
		$("#boxId").val(id);
		$("#boxText").val(boxText);
	};
	//关闭弹出层
	function CloseDiv(show_div, bg_div) {
		var attachBarcode = $("#attachBarCode").val();
		var boxId = $("#boxId").val();
		attach = {};
		attach.id = boxId;
		attach.attachBarCode = attachBarcode;
		attachList.push(attach);
		if (attachBarcode == null || attachBarcode == '') {
			$("#attachBarCode").focus();
			$("#errormessage").html("附件条码不能为空");
		} else {
			$("#attachBarCode").val("");
			$("#boxId").val("");
			document.getElementById(show_div).style.display = 'none';
			document.getElementById(bg_div).style.display = 'none';
		}
	};

	// 取消选择
	function cancleDiv(show_div, bg_div) {
		$("#errormessage").html("");
		$("#attachBarCode").val("");
		document.getElementById(show_div).style.display = 'none';
		document.getElementById(bg_div).style.display = 'none';
		var boxId = $("#boxId").val();
		var boxText = $("#boxText").val();

		// 删除由于触发select 而设置的附件
		var oldtext =$("#t_attach").text();
		oldtext =oldtext.replace("【"+boxText+"】","");
		$("#t_attach").html('<font color=red>'+oldtext+'</font>');
		
		var attachment = $("#attachment").val();
		$("#attachment").val(attachment.replace(boxId, ''));
		remove(boxId);
		$("#" + boxId).css("background-color", "#ffffff");
	}

	function remove(id) {
		attachList = [];
		for (var i = 0; i < attachList.length; i++) {
			var attach = attachList[i];
			if (id != attach.id) {
				attachList.push(attach);
			}
		}
	}
	// 选择服务类型
	function chooseService() {
		// 刷新价格
		getDate(1);
		$("#dataHtml1").html("");
		var tabType = $("#tabType").val();
		for (var i = 1; i < 6; i++) {
			if (i == tabType) {
				$("#dataHtml" + tabType).show();
			} else {
				$("#dataHtml" + i).hide();
			}
		}
		$("#sumAmount").val("0");
	}

	function cancle(){
		$('#saveCloseBut').removeAttr("disabled"); 
	}
	// 保存衣服
	function saveClose() {

		//setTimout(cancle(),5000); 
		
		var barCode = $("#barCode").val();
		var urgency = $("#urgency").val();
		var serviceType = $('input:radio[name="serviceType"]:checked').val();
		var sumAmount = $("#sumAmount").val();
		barCode = '1';
		if (barCode == null || barCode == '') {
			art.dialog.alert("条码不能为空！");
			// 定位光标到条码输入区
			$("#barCode").focus();
			return;
		}

		if (urgency == null || urgency == '') {
			art.dialog.alert("紧急程度不能为空！");
			return;
		}
		if (serviceType == null || serviceType == '') {
			art.dialog.alert("请选择服务类型！");
			return;
		}

		if (sumAmount === null || sumAmount == '') {
			art.dialog.alert("费用不能为空！");
			return;
		}
		
		if(!$.isNumeric(sumAmount)){
			art.dialog.alert("金额格式不正确！");
			return;
		}
		
		var sumAmountFloat =parseFloat(sumAmount);
		if(sumAmountFloat<=0){
			art.dialog.alert("收款金额必须大于0！");
			return;
		}

		$("#saveCloseBut").attr("disabled","disabled");//设置button不可用 ;
		
		$("#attachList").val(JSON.stringify(attachList));
		var params = $("#formId").serialize();

		jQuery.ajax({
			url : "<%=basePath%>/storeBusiness/saveSoringClothes?"+params,
			type : "post",
			cache : false,
			dataType : "json",
			success : function(data, textStatus) {
				if (data.resultCode == "0") {
					art.dialog.alert(data.resultMsg);
				} else {
					art.dialog.close();
				}
			}
		});
	}

	// 关闭窗口 
	function closeWin() {
		art.dialog.close();
	}

	// 根据tab类型获取数据
	function choose(tabNo) {
		
		$("#dataQueryKey").val("");
		$("#tabType").val(tabNo);
		$("#queryBox").hide();
		for(var i=1;i<6;i++){
			if(i==tabNo){
				if(tabNo =='3'){
					$("#queryBox").show();
				}
				$("#dataHtml"+tabNo).show();
			}else{
				$("#dataHtml"+i).hide();
			}
		}
	}
	
	
	/** 拍照功能 **/
	function takePhoto(){
		
		var nvshell =$.NV('shell');
		// 判断浏览器类型
		if(nvshell=='搜狗浏览器' || nvshell =='360se' || nvshell =='ie'){
			art.dialog.open( '<%=basePath%>/storeBusiness/takePhoto', {
				title : '衣服拍照',
				width : '90%',
				height : 500,
				lock : false,
				close:function (){
					var imagePath = art.dialog.data('imagePath'); // 读取子窗口返回的数据  
					$("#imagePath").val(imagePath);
				}
			});
		}else{
			art.dialog.alert("目前只支持IE浏览器");
		}
	}
	
	function isIe(){
		
		if (!!window.ActiveXObject || "ActiveXObject" in window){   
	        //是  
	         return true;
	        }else{  
	        //不是   
	        return false;
	    }  
		
	   /* return ("ActiveXObject" in window); */
	}
	// 快速查询
	function queryBox(){
		var tabType =$("#tabType").val();
		var dataQueryKey =$("#dataQueryKey").val();
		if(tabType ==null){
			art.dialog.alert("请选择一种类型");
			return ;
		}
		getDate(tabType,dataQueryKey);
	}
	
	function addBranch(){
		
		var brandTxt = $("#brandAdd").val();
		var tabType =$("#tabType").val();
		if(brandTxt ==null || brandTxt ==''){
			art.dialog.alert("品牌名称不能为空");
			return ;
		}
		
		var dataType ='3';
		jQuery.ajax({
			url : "<%=basePath%>/data/saveDataAjax?",
			type : "post",
			cache : false,
			dataType : "json",
			data : {'dataName':brandTxt,'dataType':dataType},
			success : function(data, textStatus) {
				if (data.resultCode == "0") {
					art.dialog.alert(data.resultMsg);
				} else {
					art.dialog.alert(data.resultMsg,function(){
						$("#brandAdd").val("")
						getDate(tabType,null);
					});
					
				}
			}
		});
		
	}
</script>
</head>

<body id="content">

	<c:if test="${resultCode == 1}">
	   <script type="text/javascript">
	        art.dialog.alert('${resultMsg}',function(){
	     		art.dialog.top.reload();
	       		art.dialog.close();
			});
	  
		</script>
	</c:if>
	<c:if test="${resultCode == 0}">
	   <script type="text/javascript">
	       art.dialog.alert('${resultMsg}');
		</script>
	</c:if>
	
	<!-- 搜索条件 -->
	<form id="formId" name="formId" class="form-horizontal" role="form" method='post' action="<%=basePath%>/storeBusiness/saveClothes">
		<div  style="padding-left: 10px; margin-top: 5px;">
			<input type="hidden" id="tabType"/>
			<input type="hidden" id="cardNumber" value="${cardNumber}" name="cardNumber"/>
			<input type="hidden" id="memberId" value="${memberId}" name="memberId"/>
			<input type="hidden" id="clothesName" value="${clothesName}" name="clothesName"/>
			<input type="hidden" id="color" value="${color}" name="color"/>
			<input type="hidden" id="brand" value="${brand}" name="brand"/>
			<input type="hidden" id="attachment" value="${attachment}" name="attachment"/>
			<input type="hidden" id="attachList" value="" name="attachList"/>
			<input type="hidden" id="flaw" value="${flaw}" name="flaw"/>
			<input type="hidden" id="orderCode" value="${orderCode}" name="orderCode"/>
			<input type="hidden" id="queryKey" value="${queryKey}" name="queryKey"/>
			
			<input type="hidden" id="price" value=""/>
			<input type="hidden" id="commonPrice" value=""/>
			<input type="hidden" id="luxuriesPrice" value=""/>
			<input type="hidden" id ="imagePath" value="${imagePath}" name="imagePath"/>
			
			<table cellpadding="0" cellspacing="0" border="2" width="100%" style="border-color: #e8e8e8;" >
				<tr>
					<td width="7%" align="center" style="line-height: 3">
						衣物条码:
					</td>
					<td width="8%" align="left">
						<input type="text" style="margin-left: 5px;height: 30px" name="barCode" id="barCode"/>
					</td>
					
					<td width="7%"  align="center">
						紧急程度:
					</td>
					<td width="8%" align="left">
						<select name="urgency" id="urgency">
							<option value="0">一般</option>
							<option value="1">紧急</option>
						</select>
					</td>
					
					<td width="6%" align="center">
						价格:
					</td>
					
					<td width="100px" align="left">
						<input type="text" name="sumAmount" id="sumAmount" style="height: 30px" value="0" />元
					</td>
				</tr>
				
				<tr>
					<td width="7%" align="center" style="line-height: 3">
						服务类型:
					</td>
					<td align="left" colspan="7">
					    &nbsp;<input type="radio" checked="checked"  name="serviceType"  onclick="chooseService()" value="2" style="vertical-align:middle; margin-top:0;"/> 普洗
						&nbsp;<input type="radio" onclick="chooseService()"  name="serviceType" value="1" style="vertical-align:middle; margin-top:0;"/> 单烫&nbsp;
						
						<input type="radio" name="serviceType" onclick="chooseService()" value="3" style="vertical-align:middle; margin-top:0;"/> 尚品洗涤&nbsp;
					</td>
				</tr>
				
				<tr>
					<td width="7%" align="center" style="line-height: 3">
						已选内容:
					</td>
					<td align="left" colspan="9">
						<!-- <textarea readonly="readonly" rows="4" cols="150" name="content" id="content_div" contenteditable="false"></textarea> -->
						<table style="padding-left: 2px;">
							<tr>
								<td>衣物名称:</td>
								<td id="t_name"></td>
							</tr>
							<tr>
								<td>衣物颜色:</td>
								<td id="t_color"></td>
							</tr>
							<tr>
								<td>衣物品牌:</td>
								<td id="t_brand"></td>
							</tr>
							<tr>
								<td>衣物瑕疵:</td>
								<td id="t_flaw"></td>
							</tr>
							<tr>
								<td>衣物附件:</td>
								<td id="t_attach"></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
		<div class="input-group" style="padding-left: 10px;margin-top: 10px;">
			<button class="btn btn-primary" type='button' onclick="saveClose()" id="saveCloseBut" style="margin-right: 10px;">保  存</button>
			<button class="btn btn-primary" type="button" onclick="closeWin()" style="margin-right: 10px;">关  闭</button>
			<!-- <button class="btn btn-primary" type="button" onclick="takePhoto()" style="margin-right: 10px;">拍  照</button> -->
		</div>
		<div style="position: relative;padding-left: 10px;width: 80%;margin-top: 10px;">
			<ul class="nav nav-tabs" id="myTab" >
				<li id="choose1" class="active"><a href="#identifier" data-toggle="tab" onclick="choose(1)">衣物名称</a></li>
				<li id="choose2" ><a href="#identifier" data-toggle="tab" onclick="choose(2)">衣物颜色</a></li>
				<li id="choose3"><a href="#identifier" data-toggle="tab" onclick="choose(3)">衣物品牌</a></li>
				<li id="choose4"><a href="#identifier" data-toggle="tab" onclick="choose(4)">衣物瑕疵</a></li>
				<li id="choose5"><a href="#identifier" data-toggle="tab" onclick="choose(5)">衣物附件</a></li>
				<!-- <li id="choose6"><a href="#identifier" data-toggle="tab" onclick="choose(6)">附加服务</a></li> -->
			</ul>
		</div>
		
		<div style="position: relative;padding-left: 15px;margin-top: 10px;">
			<div style="float: left;">
				<input type="text" name="dataQueryKey" id ="dataQueryKey" onkeyup="monitorText()"  style="height: 30px"/>
			    <button class="btn btn-primary" type="button" onclick="queryBox()" style="margin-right: 10px;">快速查询</button>
			</div>
			
			<div style="float: left;display: none;position: relative;margin-bottom: 20px;" id="queryBox">
				<input type="text" name="brandAdd" id ="brandAdd" style="height: 30px"/>
				<button class="btn btn-primary" type="button" onclick="addBranch()" style="margin-right: 10px;">快速新增品牌</button>
			</div>
		</div>
		<br><br><br>
		<div id="dataHtml1" style="position: relative;padding-left: 10px;width: 100%;margin-top: 10px;"></div>
		<div id="dataHtml2" style="position: relative;padding-left: 10px;width: 100%;margin-top: 10px;"></div>
		<div id="dataHtml3" style="position: relative;padding-left: 10px;width: 100%;margin-top: 10px;"></div>
		<div id="dataHtml4" style="position: relative;padding-left: 10px;width: 100%;margin-top: 10px;"></div>
		<div id="dataHtml5" style="position: relative;padding-left: 10px;width: 100%;margin-top: 10px;"></div>
		<div id="dataHtml6" style="position: relative;padding-left: 10px;width: 100%;margin-top: 10px;"></div>
		
	</form>
	
	<div id="fade" class="black_overlay">
	</div>
	<div id="MyDiv" class="white_content">
		<div style="text-align: right; cursor: default; height: 100px;">
			<input type="hidden" id="boxId"/>
			<input type="hidden" id="boxText"/>
			<input type="hidden" id="attach"/>
			<span style="font-size: 16px;cursor: pointer;" onclick="CloseDiv('MyDiv','fade')">添加</span>
			<span style="font-size: 16px;cursor: pointer;" onclick="cancleDiv('MyDiv','fade')">取消</span>
			<div style="position: relative;margin-top: 10px;padding-right: 130px;">
				附件条码：<input type="text"  id="attachBarCode" style="height: 30px"/>
			</div>
			<div style="position: relative;margin-top: -20px;padding-right: 30px;">
				<span style="color: red" id="errormessage"></span>
			</div>
		</div>
	</div>
</body>

<script type="text/javascript">
$('table.datatable').datatable({	
	sortable: true,
	checkable: false,
	storage: false,
    //checkedClass: "checked",
	fixedHeaderOffset: 0
	
});

var total = parseInt('${page.total}');
var pageNum = parseInt('${page.pageNum}');
var pageSize = parseInt('${page.pageSize}');


var vm = new Vue({
    el: '#content',
    data: {
        pagination: {
        	total : total,
        	pageNum : pageNum,
        	pageSize : pageSize
        },
    },
	methods: {
		    search: function(nextPage) {
		    	var paras = $("#formId").serialize();
		    	var pageParas = '&pageNum='+nextPage+'&pageSize='+this.pagination.pageSize;
		    	location.href = '<%=basePath%>/store/list?'+paras+pageParas;
		    },
		    doSearch: function(){
		    	var paras = $("#formId").serialize();
		    	location.href = '<%=basePath%>/store/list?'+paras;
		    },
		    resetSearch : function(){
		    	location.href = '<%=basePath%>/store/list';
		    }
	}
});   
</script>
</html>