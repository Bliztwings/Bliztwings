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
			+ path + "storeBusiness";
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
</style>


<script type="text/javascript">
	//$('#datatable').datatable({fixedHeaderOffset: 41, checkable: true});
	jQuery(document).ready(function() {

	});

	function saveClose() {
		jQuery("#formId").submit();
	}

	function closeWin() {
		art.dialog.close();
	}
	
	function doPayment(type){
		
		var cardNumber =$("#cardNumber").val();
		var memberId =$("#memberId").val();
		var orderCode =$("#orderCode").val();
		var orderId =$("#orderId").val();
		var arrears =$("#arrears").val();
		//1 :现金 2：会员卡
		jQuery.ajax({
			url : "<%=basePath%>/storeBusiness/doPayment",
			type : "post",
			cache : false,
			dataType : "json",
			data : {'payType':type,'cardNumber':cardNumber,'memberId':memberId,'orderId':orderId,'orderCode':orderCode,'arrears':arrears},
			success : function(data, textStatus) {
				
				if (data.resultCode == "0") {
					art.dialog.alert(data.resultMsg);
				} else {
					//  打印小票
					art.dialog.open( '<%=basePath%>/storeBusiness/checkoutPrint?payType='+type+'&orderCode='+orderCode, {
						title : '支付信息',
						width : 300,
						height : 500,
						lock : false,
						close:function (){
							closeWin();						
						}
					});
				}
			}
		});
		
	}
</script>
</head>

<!-- 异步表格   <body onload="ajaxReload()"> -->
<body id="content">
	<!-- 搜索条件 -->
	<form id="formId" name="formId" class="form-horizontal" role="form" method='post' action="<%=basePath%>/storeBusiness/saveClothes">
		<div  style="padding-left: 10px; margin-top: 40px;">
			<input type="hidden" id="cardNumber" value="${cardNumber}" name="cardNumber"/>
			<input type="hidden" id="memberId" value="${memberId}" name="memberId"/>
			<input type="hidden" id="orderCode" value="${orderCode}" name="orderCode"/>
			<input type="hidden" id="orderId" value="${orderCode}" name="orderId"/>
			<input type="hidden" id="arrears" value="${arrears}" name="arrears"/>
			
			<c:if test="${cardNumber != null && cardNumber !=''}">
				<div class="payWay" onclick="doPayment(1)" onmouseover="this.style.backgroundColor='#aca8d4'" 
					onmouseout="this.style.backgroundColor='#71cd8b'" style="background-color: #71cd8b;cursor: pointer;width: 100%;height: 50px;line-height: 50px;text-align: center;font-family: serif;color: #ffffff">
					现金支付
				</div>
				
				<div class="payWay1" onclick="doPayment(2)" onmouseover="this.style.backgroundColor='#aca8d4'" 
					onmouseout="this.style.backgroundColor='#36bde5'" style="background-color: #36bde5;cursor: pointer;width: 100%;height: 50px;line-height: 50px;text-align: center;font-family: serif;color: #ffffff">
					会员卡支付
				</div>
			</c:if>
			
			<c:if test="${cardNumber == null || cardNumber ==''}">
				<div class="payWay" onclick="doPayment(1)" onmouseover="this.style.backgroundColor='#aca8d4'" 
					onmouseout="this.style.backgroundColor='#71cd8b'" style="background-color: #71cd8b;cursor: pointer;width: 100%;height: 50px;line-height: 50px;text-align: center;font-family: serif;color: #ffffff">
					现金支付
				</div>
				
				<div  class="payWay1" style="background-color: gray;width: 100%;height: 50px;line-height: 50px;text-align: center;font-family: serif;color: #ffffff">
					会员卡支付
				</div>
			</c:if>
			
			
		</div>
	</form>
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