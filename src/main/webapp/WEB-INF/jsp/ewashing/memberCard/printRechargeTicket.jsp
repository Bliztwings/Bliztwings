<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ include file="/WEB-INF/jsp/inc/tag_css_js.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>  
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>打印充值小票</title>

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

	<!-- jquery print -->
	<script type="text/javascript" src="<%=basePath%>/js/jquery/jquery.jqprint-0.3.js"></script>
	<script type="text/javascript" src="<%=basePath%>/js/jquery/jquery-migrate-1.2.1.min.js"></script>
	
	
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

	jQuery(document).ready(function(){
			
	});
	
	function print() {
		$("#ddd").jqprint();
	}
	
</script>
</head>

<body id="content">
	<div style="padding-left: 10px; margin-top: 5px;">
		<input type="button" value="打印小票" onclick="print()"/>
	</div>
	<div id="ddd">
		<div  style="padding-left: 10px; margin-top: 5px;">
			<table cellpadding="0" cellspacing="0" border="0">
				<tr><td width="10%" align="center" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;"><h1>${member.storeName }</h1></td></tr>
				<tr><td width="10%" align="center" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;"><h1>会员充值凭证</h1></td></tr>
			</table>
		</div>
		<br/>
		<hr style="height:1px;border:none;border-top:1px dashed #0066CC;"/>
		<br/>
		
		<div  style="padding-left: 10px; margin-top: 5px;">
			<table cellpadding="0" cellspacing="0" border="0">
				<tr><td width="10%" align="left" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">姓名：${member.name } &nbsp;&nbsp;&nbsp;&nbsp;</td></tr>
				<tr><td width="10%" align="left" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">性别：<c:if test="${member.sex=='0'}">男</c:if><c:if test="${member.sex=='1'}">女</c:if> &nbsp;&nbsp;&nbsp;&nbsp;</td></tr>
				<tr><td width="10%" align="left" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">手机:${member.mobilePhone }</td></tr>
				<tr><td width="10%" align="left" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">电话:${member.telephone }</td></tr>
				<tr><td width="10%" align="left" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">地址:${member.address }</td></tr>
			</table>
		</div>
		<br/>
		<hr style="height:1px;border:none;border-top:1px dashed #0066CC;"/>
		<br/>
		
		<div  style="padding-left: 10px; margin-top: 5px;">
			<table cellpadding="0" cellspacing="0" border="0">
				<tr><td width="10%" align="left" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">会员卡号:${memberCard.cardNumber }</td></tr>
				<tr><td width="10%" align="left" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">本次充值金额:${memberCardFlowing.cashAmountOper }</td></tr>
				<tr><td width="10%" align="left" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">本次赠送金额:${memberCardFlowing.givedAmountOper }</td></tr>
				<!--<tr><td width="10%" align="left" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">卡现金余额:${memberCardFlowing.cashAmountAfter }</td></tr>
				<tr><td width="10%" align="left" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">卡赠送余额:${memberCardFlowing.givedAmountAfter }</td></tr>-->
				<tr><td width="10%" align="left" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">卡内总金额:${memberCardFlowing.cashAmountAfter+ memberCardFlowing.givedAmountAfter}</td></tr>
			</table>
		</div>
		<br/>
		<hr style="height:1px;border:none;border-top:1px dashed #0066CC;"/>
		<br/>
		
		<div  style="padding-left: 10px; margin-top: 5px;">
			<table cellpadding="0" cellspacing="0" border="0">
				<tr><td width="10%" align="left" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">店号:${store.storeCode }</td></tr>
				<tr><td width="10%" align="left" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">支付方式:
					<c:choose>
					   <c:when test="${memberCardFlowing.payedMethod=='cash' }">现金</c:when>
					   <c:when test="${memberCardFlowing.payedMethod=='alipay' }">支付宝</c:when>
					   <c:when test="${memberCardFlowing.payedMethod=='wechat' }">微信</c:when>
					   <c:when test="${memberCardFlowing.payedMethod=='blank' }">银行卡</c:when>
					   <c:otherwise> 
					   </c:otherwise>
					</c:choose>
				</td>
				</tr>
				<tr><td width="10%" align="left" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">经办人:${store.storeName }</td></tr>
				<tr><td width="10%" align="left" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">打印时间:<fmt:formatDate value="${currentTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td></tr>
			</table>
		</div>
		
	 </div>
   </body>
</html>