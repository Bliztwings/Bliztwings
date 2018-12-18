<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ include file="/WEB-INF/jsp/inc/tag_css_js.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改用户信息</title>
<link type="text/css" href="<%=basePath%>/zui-1.5.0/css/zui.min.css"
	rel="stylesheet" />
<script type="text/javascript"
	src="<%=basePath%>/zui-1.5.0/lib/jquery/jquery.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/zui-1.5.0/js/zui.min.js"></script>
<!-- form验证 -->
<link rel="stylesheet"
	href="<%=basePath%>/js/validationEngine/css/validationEngine.jquery.css"
	type="text/css" />
<!-- <script src="<%=basePath%>/js/jquery/jquery-1.7.2.min.js" type="text/javascript"></script> -->
<script
	src="<%=basePath%>/js/validationEngine/js/languages/jquery.validationEngine-zh_CN.js"
	type="text/javascript" charset="utf-8"></script>
<script
	src="<%=basePath%>/js/validationEngine/js/jquery.validationEngine-2.6.4.js"
	type="text/javascript" charset="utf-8"></script>

<!--  art.dialog  -->
<link href="<%=basePath%>/js/artDialog4.1.7/skins/blue.css"
	rel="stylesheet" type="text/css" />
<script src="<%=basePath%>/js/artDialog4.1.7/artDialog.js"
	type="text/javascript"></script>
<script
	src="<%=basePath%>/js/artDialog4.1.7/plugins/iframeTools.source.js"
	type="text/javascript"></script>
<style>
ul.ztree {
	margin-top: 10px;
	border: 1px solid #617775;
	background: #f0f6e4;
	width: 220px;
	height: 200px;
	overflow-y: scroll;
	overflow-x: auto;
}
</style>

<script type="text/javascript">
jQuery(document).ready(function(){
    jQuery("#formId").validationEngine('attach',{promptPosition: "centerRight"}); 
    jQuery("#formId2").validationEngine('attach',{promptPosition: "centerRight"}); 
    
    $("#info").click(function(){
		location.href = '<%=basePath%>/user/toPwdChange?type=info';
	});
	$("#pwd").click(function(){
		location.href = '<%=basePath%>/user/toPwdChange?type=pwd';
	});
});
</script>

</head>
<body style="margin:10px">
	<c:if test="${resultCode == 1}">
		<script type="text/javascript">
		art.dialog.alert('${resultMsg}',function(){
			artDialog.data("type", '${type}'); //将值存起来，供父页面读取  
			artDialog.data("resultCode", '${resultCode}'); //将值存起来，供父页面读取  
			art.dialog.close();
		});
		</script>
	</c:if>
	<c:if test="${resultCode == 0}">
		<script type="text/javascript">
		art.dialog.alert('${resultMsg}',function(){
			artDialog.data("type", '${type}'); //将值存起来，供父页面读取  
			artDialog.data("resultCode", '${resultCode}'); //将值存起来，供父页面读取  
			art.dialog.close();
		});
		</script>
	</c:if>
	
<ul class="nav nav-tabs">
	  <li id="info" <c:if test="${type=='info'}">class="active"</c:if>>
	  	<a href="#" data-target="#tabContent1" data-toggle="tab" <c:if test="${type=='info'}">class="active" style="font-weight:bold;color:black;"</c:if>>个人信息</a>
	  </li>
	  <li id="pwd" <c:if test="${type=='pwd'}">class="active"</c:if>>
	  	<a href="#" data-target="#tabContent2" data-toggle="tab" <c:if test="${type=='pwd'}">class="active" style="font-weight:bold;color:black;"</c:if>>修改密码</a>
	  </li>
</ul>

<div class="tab-content">
		  <div class="tab-pane <c:if test="${type=='info'}">active</c:if>" id="tabContent1">
		    	<div id="right" style="width:100%">
	       <div id="up" style="margin:10px">
	       			<form id="formId" name="formId" class="form-horizontal" role="form" method='post' action="<%=basePath%>/user/pwdChange?type=info">
				
				<div class="input-group" style="margin-bottom: 10px;">
					<span class="input-group-addon"
						style="width: 150px; text-align: right;">真实姓名：</span> <input
						id="realname" name="realname" value="${loginUser.realname}" type="text" 
						class="form-control validate[required,maxSize[64]] required"
						style="width: 300px;" /> <span class="glyphicon glyphicon-star"></span>
				</div>

				<div class="input-group" style="margin-bottom: 10px;">
					<span class="input-group-addon"
						style="width: 150px; text-align: right;">Email：</span> <input
						id="email" name="email" value="${loginUser.email}" type="text" 
						class="form-control validate[required,maxSize[64],custom[email]] required"
						style="width: 300px;" /> <span class="glyphicon glyphicon-star"></span>
				</div>
				
				<div class="input-group" style="margin-bottom: 10px;">
					<span class="input-group-addon"
						style="width: 150px; text-align: right;">手机号：</span> <input
						id="phone" name="phone" value="${loginUser.phone}" type="text" 
						class="form-control validate[required,maxSize[64],custom[phone]] required"
						style="width: 300px;" /> <span class="glyphicon glyphicon-star"></span>
				</div>

				<div class="form-group">
					<div class="col-md-offset-2 col-md-10">
						<input type='submit' id='saveBtn' class='btn btn-primary'
							value='保存' data-loading='稍候...' /> 
					</div>
				</div>
			</form>
	       </div>
	       
	 </div>
		  </div>
		  <div class="tab-pane <c:if test="${type=='pwd'}">active</c:if>" id="tabContent2">
		  		<div id="right" style="width:100%">
	       <div id="up" style="margin:10px">
	       			<form id="formId2" name="formId2" class="form-horizontal" role="form" method='post' action="<%=basePath%>/user/pwdChange?type=pwd">
				<div class="input-group" style="margin-bottom: 10px;">
					<span class="input-group-addon"
						style="width: 150px; text-align: right;">原密码：</span> <input
						id="oldPwd" name="oldPwd" value="" type="password" 
						class="form-control validate[required,maxSize[64]] required"
						style="width: 300px;" /> <span class="glyphicon glyphicon-star"></span>
				</div>

				<div class="input-group" style="margin-bottom: 10px;">
					<span class="input-group-addon"
						style="width: 150px; text-align: right;">新密码：</span> <input
						id="newPwd" name="newPwd" value="" type="password" 
						class="form-control validate[required,maxSize[64]] required"
						style="width: 300px;" /> <span class="glyphicon glyphicon-star"></span>
				</div>
				
				<div class="input-group" style="margin-bottom: 10px;">
					<span class="input-group-addon"
						style="width: 150px; text-align: right;">确认新密码：</span> <input
						id="renewPwd" name="renewPwd" value="" type="password" 
						class="form-control validate[required,maxSize[64],equals[newPwd]] required"
						style="width: 300px;" /> <span class="glyphicon glyphicon-star"></span>
				</div>

				<div class="form-group">
					<div class="col-md-offset-2 col-md-10">
						<input type='submit' id='saveBtn' class='btn btn-primary'
							value='保存' data-loading='稍候...' /> 
					</div>
				</div>
				
				 </form>
	       </div>
	       
	 </div>
		  </div>
	</div>
</body>
</html>