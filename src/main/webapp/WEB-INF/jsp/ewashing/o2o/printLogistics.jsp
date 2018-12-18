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
	<title>物流单打印</title>

	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/jquery/jquery.js"></script>

	<!-- jquery print -->
	<script type="text/javascript" src="<%=basePath%>/js/jquery/jquery.jqprint-0.3.js"></script>
	<script type="text/javascript" src="<%=basePath%>/js/jquery/jquery-migrate-1.2.1.min.js"></script>

	<script type="text/javascript" src="<%=basePath%>/js/jquery/jquery.qrcode.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>/js/jquery/pako.js"></script>

<script type="text/javascript">

	jQuery(document).ready(function(){
		
		if('${order.appId}'=='wx614b63f73b55d7ef'){
			var orderStr='${orderStr}';
			encode(orderStr);
			var img = document.getElementById("image"); /// get image element
			var canvas = document.getElementsByTagName("canvas")[0]; /// get canvas element
			img.src = canvas.toDataURL();
		}
		
	});
	
	function print() {
		$("#ddd").jqprint({
			globalStyles : true,//是否包含父文档的样式，默认为true
			mediaPrint : false,//是否包含media='print'的链接标签。会被globalStyles选项覆盖，默认为false
			stylesheet : null,//外部样式表的URL地址，默认为null
			noPrintSelector : ".no-print",//不想打印的元素的jQuery选择器，默认为".no-print"
			importCSS : true,
			iframe : true,//是否使用一个iframe来替代打印表单的弹出窗口，true为在本页面进行打印，false就是说新开一个页面打印，默认为true
			append : null,//将内容添加到打印内容的后面
			prepend : null,//将内容添加到打印内容的前面，可以用来作为要打印内容
			deferred : $.Deferred()
		});
	}
	
	function encode(orderStr) {
		$("#code").html('');
		str = toUtf8(orderStr);
		$("#code").qrcode({
			render : "canvas", //table方式
			width : 200, //宽度
			height : 200, //高度
			text : str
		});
	}
	
	
	
	function toUtf8(str) {
		var out, i, len, c;
		out = "";
		len = str.length;
		for (i = 0; i < len; i++) {
			c = str.charCodeAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				out += str.charAt(i);
			} else if (c > 0x07FF) {
				out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
				out += String.fromCharCode(0x80 | ((c >> 6) & 0x3F));
				out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
			} else {
				out += String.fromCharCode(0xC0 | ((c >> 6) & 0x1F));
				out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
			}
		}
		return out;
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
		var currentdate = date.getFullYear() + seperator1 + month + seperator1
				+ strDate + " " + date.getHours() + seperator2
				+ date.getMinutes() + seperator2 + date.getSeconds();
		return currentdate;
	}
</script>
</head>

<body id="content" style="font-family: 宋体">
	<div style="padding-left: 10px; margin-top: 5px;">
		<input type="button" value="打印" onclick="print()"/>
	</div>
	<div id="ddd">
		<div align="center" style="text-align: center;font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">
			<c:if test="${order.appId=='wx614b63f73b55d7ef'}">
				萨维亚物流单
			</c:if>
						
			<c:if test="${order.appId=='wxc6097b5bf9ba801d'}">
				浣衣坊物流单
			</c:if>
		</div>
		<hr style="height:1px;border:none;border-top:1px dashed #0066CC;"/>
		<hr style="height:1px;border:none;border-top:1px dashed #0066CC;"/>
		<div  style="padding-left: 10px; margin-top: 5px;">
			<table cellpadding="0" cellspacing="0" border="0">
			
				<tr>
					<td width="10%" align="left" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">
						用户地址:${order.orderAddress}
					</td>
				</tr>
				
				<tr>
					<td width="10%" align="left" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">
						用户姓名:${order.memberName}
					</td>
				</tr>
				
				<tr>
					<td width="10%" align="left" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">
						手机号:${order.mobilePhone}
					</td>
				</tr>
				
				<tr>
					<td width="10%" align="left" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">
						取件人员:
					</td>
				</tr>
				
				<tr>
					<td width="10%" align="left" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">
						 服务站点:
					</td>
				</tr>
				
				<tr>
					<td width="10%" align="left" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">
						物流数量:
					</td>
				</tr>
				
				<tr>
					<td width="10%" align="left" style="font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;">
						用户签名:
					</td>
				</tr>
				
				<tr style="position: relative;top: 20px;">
					<td align="center">
						<c:if test="${order.appId=='wx614b63f73b55d7ef'}">
							<div id="code" style="display: none"></div>
							<img id="image" style="width: 200px;height: 200px" src="" />						
					    </c:if>
						
						<c:if test="${order.appId=='wxc6097b5bf9ba801d'}">
							<img id="image" style="width: 100px;height: 100px" src="<%=basePath%>/static/images/hyf.jpg" />
						</c:if>
						
						
						
						<%-- <c:if test="${order.appId=='wx614b63f73b55d7ef'}">
							<img id="image" style="width: 100px;height: 100px" src="<%=basePath%>/static/images/swy.jpg" />
						</c:if>
						
						<c:if test="${order.appId=='wxc6097b5bf9ba801d'}">
							<img id="image" style="width: 100px;height: 100px" src="<%=basePath%>/static/images/hyf.jpg" />
						</c:if> --%>
					</td>
				</tr>
			</table>
		</div>
		
		<%-- <br/>
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
		</div> --%>
		<%-- <br/>
		<hr style="height:1px;border:none;border-top:1px dashed #0066CC;"/>
		<div style="text-align: left;font-family: '宋体'; font-size: 5px; line-height: 20px; font-weight: bold;" align="left">
			操作人：${loginUserName}<br/>
			操作时间：${optDate}
		</div> --%>
	 </div>
   </body>
</html>