<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
	String realPath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "store";
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>门店收衣</title>

	<link type="text/css" href="<%=basePath%>/zui-1.5.0/css/zui.min.css" rel="stylesheet" />
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/jquery/jquery.js"></script>
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/js/zui.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/datatable/zui.datatable.js"></script>
	<link type="text/css" href="<%=basePath%>/zui-1.5.0/lib/datatable/zui.datatable.css" rel="stylesheet" />
	<script src="<%=basePath%>/js/jquery/jquery.form.js" type="text/javascript"></script>
	<script type="text/javascript" src="<%=basePath%>/js/jquery/jquery.json-2.2.js"></script>
	
	<!--  art.dialog  -->
	<link href="<%=basePath%>/js/artDialog4.1.7/skins/blue.css" rel="stylesheet" type="text/css" />
	<script src="<%=basePath%>/js/artDialog4.1.7/artDialog.js" type="text/javascript"></script>
	<script src="<%=basePath%>/js/artDialog4.1.7/plugins/iframeTools.source.js" type="text/javascript"></script>
		
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
</style>


<script type="text/javascript">

	//$('#datatable').datatable({fixedHeaderOffset: 41, checkable: true});
	jQuery(document).ready(function(){
		
		
	});
	
	function chooseArea(){
		var areaId =$("#handonArea").val();
		jQuery.ajax({
			url : "<%=basePath%>/storeBusiness/queryHandonNoByArea",
			type : "post",
			cache : false,
			dataType : "json",
			data : {'areaId':areaId},
			success : function(data, textStatus) {
				if (data.resultCode == "0") {
					art.dialog.alert(data.resultMsg);
				} else {
					
					if(data.list!=null){
						// 动态生成html
						$("#handonNoHtml").html();
						var str ="";
						str+='<select id="handonNo" class="form-control validate[required,maxSize[16]] required" style="width: 80px;">';
							for(var i =0;i<data.list.length;i++){
								str+='<option value='+data.list[i].id+'>'+data.list[i].handOnNo+'</option>';
							}
						str+="</select>";
						$("#handonNoHtml").html(str);
					}
				}
			}
		});
	}
	
	//  查询
	function query(){
		var params = $("#formId").serialize();
	    location.href = "<%=basePath%>/storeBusiness/sign?"+params;
	}
	
	var flag=1;
	function checkAll(){
		var pkids = document.getElementsByName("checkbox");
		if(flag==1){
			flag=2;
			for ( var j = 0; j < pkids.length; j++) {
				if (pkids.item(j).checked == false) {
					pkids.item(j).checked = true;
				}
			}
		}else{
			if(flag==2){
				flag=1;
				for ( var j = 0; j < pkids.length; j++) {
					if (pkids.item(j).checked == true) {
						pkids.item(j).checked = false;
					}
				}
			}
		}
	}
	
	// 动态生成待上挂信息
	var head ="";
	head+='<table class="table datatable table-bordered  table-hover" id="datatable">';
	head+='<thead>';
	head+='<tr>';
	head+='<th class="text-center" data-col-class="text-center" data-width="10%">出厂单号</th>';
	head+='<th class="text-center" data-col-class="text-center" data-width="10%">出厂日期</th>';
	head+='<th class="text-center" data-col-class="text-center" data-width="10%">订单号</th>';
	head+='<th class="text-center" data-col-class="text-center" data-width="10%">衣物条码</th>';
	head+='<th class="text-center" data-col-class="text-center" data-width="10%">衣物名称</th>';
	head+='<th class="text-center" data-col-class="text-center" data-width="10%">颜色</th>';
	head+='<th class="text-center" data-col-class="text-center" data-width="10%">品牌</th>';
	head+='</tr>';
	head+='</thead>';
	head+='<tbody>';
	// 上挂
	function sign(){
		
		var count =0;
		var arrays =[];
		var ids =[];
		var pkids = document.getElementsByName("checkbox");
		for ( var j = 0; j < pkids.length; j++) {
			if (pkids.item(j).checked == true) {
				count+=1;
				if(!ids.contains(pkids.item(j).value.split("#")[0])){
					arrays.push(pkids.item(j).value);
					ids.push(pkids.item(j).value.split("#")[0]);
				}
			}
		}
		if(count==0){
			art.dialog.alert('请选择送洗衣服!');
			return;
		}
		
		// 动态生成待上挂信息
		var content =head;
		for(var j =0;j<arrays.length;j++){
			var contetx =arrays[j].split("#");
			content+='<tr>';
			content+='<td class="text-center">'+contetx[1]+'</td>';
			content+='<td class="text-center">'+contetx[2]+'</td>';
			content+='<td class="text-center">'+contetx[3]+'</td>';
			content+='<td class="text-center">'+contetx[4]+'</td>';
			content+='<td class="text-center">'+contetx[5]+'</td>';
			content+='<td class="text-center">'+contetx[6]+'</td>';
			content+='<td class="text-center">'+contetx[7].replace("'","")+'</td>';
			content+='</tr>';
		}
		content+='</tbody>';
		
		$("#chooseContent").val(arrays);
		$("#chooseHtml").html();
		$("#chooseHtml").html(content);
		$("#chooseHandOnCount").html("<em>选定签收衣物(共"+arrays.length+"件)</em>");
	}
	
	function cancel(){
		$("#chooseHtml").html();
		$("#chooseHtml").html(head);
		$("#chooseHandOnCount").html("<em>选定签收衣物(共0件)</em>");
		$("#chooseContent").val("");

	}
	
	Array.prototype.contains =function (needle){
		for(i in this){
			if(this[i] == needle) return true;
		}
		return false;
	}
	
	// 确认上挂
	function confirmHandOn(){
		
		var clothes =$("#chooseContent").val();
		jQuery.ajax({
			url : "<%=basePath%>/storeBusiness/confirmSign",
			type : "post",
			cache : false,
			dataType : "json",
			data : {'clothes':clothes},
			success : function(data, textStatus) {
				if (data.resultCode == "0") {
					art.dialog.alert(data.resultMsg);
				} else {
					art.dialog.alert(data.resultMsg,function(){
						art.dialog.close();
						query();
					});
				}
			}
		});
		
		
	}
</script>
</head>

<!-- 异步表格   <body onload="ajaxReload()"> -->
<body id="content">

	<div class="nav_tit">
		<em>待签收衣物(共${waitSignCount}件衣服)</em>
	</div>
						
	<!-- 搜索条件 -->
	<form id="formId" name="formId" class="form-horizontal" role="form" method='get' action="<%=basePath%>/storeBusiness/sign">
		<input type="hidden" value="" id="chooseContent"/>
		<div  style="padding-left: 10px; margin-top: 5px;">
			<table cellpadding="0" cellspacing="0" border="0" width="100%" >
				<tr>
					<td width="10%">
						<div class="input-group" >
							<span class="input-group-addon" style="width:20px; text-align: left;">会员名称：</span> 
							<input id="memberName" name="memberName" value="${memberName}" type="text" class="form-control" style="width: 150px;"> <!-- <span class="glyphicon glyphicon-star"></span> -->
						</div>
					</td>
					
					<td width="10%">
						<div class="input-group" >
							<span class="input-group-addon" style="width:20px; text-align: left;">手机号码：</span> 
							<input id="mobilePhone" name="mobilePhone" value="${mobilePhone}" type="text" class="form-control" style="width: 150px;"> <!-- <span class="glyphicon glyphicon-star"></span> -->
						</div>
					</td>
					
					<td width="10%">
						<div class="input-group" >
							<span class="input-group-addon" style="width:20px; text-align: left;">衣物条码：</span> 
							<input id="barCode" name="barCode" value="${barCode}" type="text" class="form-control" style="width: 150px;"> <!-- <span class="glyphicon glyphicon-star"></span> -->
						</div>
					</td>
					<td width="100px" align="left">
						<div class="input-group" style="padding-left: 10px;">
							<button class="btn btn-primary" type="button" id="transLogQueryBtn" onclick="query()" style="margin-right: 10px;">查  询</button>
						</div>
					</td>
				</tr>
				
			</table>
		</div>
		<br style="border: 1px;border-color: fff"/>
		<article>
			<div class="segment">
				<div id="dtFromData"></div>
				<div style="padding-left: 10px; padding-right: 10px;">
					<table class="table datatable table-bordered  table-hover" id="datatable">
						<thead>
							<tr>
								<th class='text-center' data-col-class='text-center' data-width='5%'><input type="checkbox" onclick="checkAll()"/></th>
								<th class='text-center' data-col-class='text-center' data-width='10%'>出厂单号</th>
								<th class='text-center' data-col-class='text-center' data-width='10%'>出厂日期</th>
								<th class='text-center' data-col-class='text-center' data-width='10%'>会员名称</th>
								<th class='text-center' data-col-class='text-center' data-width='10%'>会员手机号</th>
								<th class='text-center' data-col-class='text-center' data-width='10%'>订单号</th>
								<th class='text-center' data-col-class='text-center' data-width='10%'>衣物条码</th>
								<th class='text-center' data-col-class='text-center' data-width='10%'>衣物名称</th>
								<th class='text-center' data-col-class='text-center' data-width='10%'>颜色</th>
								<th class='text-center' data-col-class='text-center' data-width='10%'>品牌</th>
							</tr>
						</thead>
						<!-- 同步表格 -->
						<tbody>
	 						<!--  PageHelper -->
							<c:forEach items="${list}" var="handon">
								<tr>
									<td><input type="checkbox" name="checkbox" value="${handon.id}#${handon.leaveFactoryNumber}#${handon.leaveDateStr}#${handon.orderCode}#${handon.barCode}#${handon.clothesName}#${handon.color}#${handon.brand}'"/></td>
									<td>${handon.leaveFactoryNumber}</td>
									<td>${handon.leaveDateStr}</td>
									<td>${handon.memberName}</td>
									<td>${handon.mobilePhone}</td>
									<td>${handon.orderCode}</td>
								    <td>${handon.barCode}</td>
								    <td>${handon.clothesName}</td>
								    <td>${handon.color}</td>
								    <td>${handon.brand}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</article>
		
		<div style="padding-left: 10px; margin-top: 5px;margin-bottom: 10px;">
			<table cellpadding="0" cellspacing="0" border="0">
				<tr>
					
					<td align="left" style="position:relative;padding-left: 10px;">
						<button class="btn btn-primary" type="button" id="transLogQueryBtn" onclick="sign()" style="margin-right: 10px;">选定签收</button>
					</td>
					
					<td align="left" style="position:relative;padding-left: 10px;">
						<button class="btn btn-primary" type="button" id="transLogQueryBtn" onclick="cancel()" style="margin-right: 10px;">取 消</button>
					</td>
				</tr>
			</table>
		</div>
	</form>
	<div class="nav_tit" id="chooseHandOnCount">
		<em>选定签收衣物(共0件衣服)</em>
	</div>
	
	<article>
		<!-- 表格-->
		<div class="segment">
			<div id="dtFromData"></div>
			<div style="padding-left: 10px; padding-right: 10px;" id="chooseHtml">
				<table class="table datatable table-bordered  table-hover" id="datatable">
					<thead>
						<tr>
							<th class='text-center' data-col-class='text-center' data-width='10%'>出厂单号</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>出厂日期</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>订单号</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>衣物条码</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>衣物名称</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>颜色</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>品牌</th>
						</tr>
					</thead>
					<!-- 同步表格 -->
					<tbody>
 						<!--  PageHelper -->
						<c:forEach items="${page.list}" var="handon">
							<tr>
								<td>${handon.leaveFactoryNumber}</td>
								<td>${handon.leaveDateStr}</td>
								<td>${handon.orderCode}</td>
							    <td>${handon.barCode}</td>
							    <td>${handon.clothesName}</td>
							    <td>${handon.color}</td>
							    <td>${handon.brand}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</article>
	
	<div style="padding-left: 10px; margin-top: 5px;margin-bottom: 10px;">
		<table cellpadding="0" cellspacing="0" border="0">
			<tr>
				<button class="btn btn-primary" type="button" id="transLogQueryBtn" onclick="confirmHandOn()" style="margin-right: 10px;">确认签收</button>
			</tr>
		</table>
	</div>
</body>

<script type="text/javascript">
$('table.datatable').datatable({	
	sortable: false,
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