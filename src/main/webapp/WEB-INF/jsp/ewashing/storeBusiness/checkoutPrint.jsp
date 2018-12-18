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
	<title>消费打印</title>

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


<script type="text/javascript">

	jQuery(document).ready(function(){
			
	});
	
	function print() {
		$("#ddd").jqprint();
	}
	
	//获取当前时间
	function getNowFormatDate() {
	    var date = new Date();
	    var seperator1 = "-";
	    var seperator2 = ":";
	    var month = date.getMonth() + 1;
	    var strDate = date.getDate();
	    if (month >= 1 && month <= 9) {
	        month = "0" + month;
	    }
	    if (strDate >= 0 && strDate <= 9) {
	        strDate = "0" + strDate;
	    }
	    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
	            + " " + date.getHours() + seperator2 + date.getMinutes()
	            + seperator2 + date.getSeconds();
	    return currentdate;
	}
</script>
</head>

<body id="content">
	<div style="padding-left: 10px; margin-top: 5px;">
		<input type="button" value="打印" onclick="print()"/>
	</div>
	<div id="ddd">
		<div align="center" style="text-align: center;font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">
			欢迎光临<br>
			${order.storeName}<br>
		</div>
		<hr style="height:1px;border:none;border-top:1px dashed #0066CC;"/>
		<hr style="height:1px;border:none;border-top:1px dashed #0066CC;"/>
		<div  style="padding-left: 10px; margin-top: 5px;">
			<table cellpadding="0" cellspacing="0" border="0">
			
				<tr>
					<td width="10%" align="left" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">
						门店名称:${order.storeName}
					</td>
				</tr>
				
				<tr>
					<td width="10%" align="left" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">
						订单编号:${order.orderCode}
					</td>
				</tr>
				
				<tr>
					<td width="10%" align="left" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">
						应收金额:${order.receivableAmount}元
					</td>
				</tr>
				
				<tr>
					<td width="10%" align="left" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">
						付款方式:
						<c:if test="${order.payGateWay=='1'}">现金支付</c:if>
						<c:if test="${order.payGateWay=='2'}">会员卡支付</c:if>
					</td>
				</tr>
				
				<tr>
					<td width="10%" align="left" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">
						会员名称:${order.memberName}
					</td>
				</tr>
				
				<tr>
					<td width="10%" align="left" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">
						会员手机号:${order.mobilePhone}
					</td>
				</tr>
			</table>
		</div>
		<br/>
		<hr style="height:1px;border:none;border-top:1px dashed #0066CC;"/>
		<br/>
		<div style="padding-left: 10px; padding-right: 10px;">
			<label style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">
				衣服信息：
			</label>
			<table cellpadding="0" cellspacing="0" border="1">
				<thead>
					<tr style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">
						<th align="center" data-width='10%'>衣物名称</th>
						<th align="center" data-width='10%'>颜色</th>
						<th align="center" data-width='10%'>品牌</th>
						<th align="center" data-width='10%'>附件</th>
						<th align="center" data-width='10%'>瑕疵</th>
						<th align="center" data-width='10%'>衣物价格</th>
					</tr>
				</thead>
				<!-- 同步表格 -->
				<tbody>
					<c:forEach items="${clothesList}" var="clothes">
						<tr style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">
						    <td class='text-center'>${clothes.clothesName}</td>
						    <td class='text-center'>${clothes.color}</td>
						    <td class='text-center'>${clothes.brand}</td>
						    <td class='text-center'>${clothes.attachment}</td>
						    <td class='text-center'>${clothes.flaw}</td>
						    <td class='text-center'>${clothes.price}元</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		
		<br/>
		<hr style="height:1px;border:none;border-top:1px dashed #0066CC;"/>
		<div style="text-align: left;font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;" align="left">
			操作人：${loginUserName}<br/>
			操作时间：${optDate}
		</div>
	 </div>
   </body>
</html>