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
<!-- -->
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
<script src="<%=basePath%>/js/artDialog4.1.7/plugins/iframeTools.source.js"
	type="text/javascript"></script>

<!--  Ztree  -->
<link rel="stylesheet" href="<%=basePath%>/js/JQuery zTree v3.5.15/css/css.css">
<link rel="stylesheet" href="<%=basePath%>/js/JQuery zTree v3.5.15/css/zTreeStyle/zTreeStyle.css">
<!--  <script src="<%=basePath%>/js/jquery/jquery-1.11.0.min.js"></script>   -->
<script src="<%=basePath%>/js/JQuery zTree v3.5.15/js/jquery.ztree.all-3.5.js"></script>
	
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
    
    $("#userType").change(function(){
    	if($(this).val()=='1'){
    		$("#storeDiv").show();
    	}
    	else{
    		$("#storeDiv").hide();
    	}
    });
    
    if($("#userType").val()=='1'){
		$("#storeDiv").show();
	}
	else{
		$("#storeDiv").hide();
	}
});
</script>
</head>
<body>
	<c:if test="${resultCode == 1}">
		<script type="text/javascript">
			alert('${resultMsg}');
			art.dialog.top.reload();
			art.dialog.close();
		</script>
	</c:if>
	<c:if test="${resultCode == 0}">
		<script type="text/javascript">
			alert('${resultMsg}');
		</script>
	</c:if>


	<article> <section id='form' class="page-section">
	<div class='panel' style="padding-left: 10px">
		<div class='panel-body' style="padding-left: 10px">
			<form id="formId" name="formId" class="form-horizontal" role="form"
				method='post' action="<%=basePath%>/user/update">
				<input type='hidden' name='id' id='id' value='${securityUser.id}' />
				<div class="input-group" style="margin-bottom: 10px;">
					<span class="input-group-addon"
						style="width: 150px; text-align: right;">用户名：</span> <input
						id="username" name="username" value="${securityUser.username }"
						class="form-control validate[required,maxSize[64]] required"
						type="text" readonly style="width: 300px;"></input> <span
						class="glyphicon glyphicon-star"></span>
				</div>
				
				<div class="input-group" style="margin-bottom: 10px;">
					<span class="input-group-addon"
						style="width: 150px; text-align: right;">真实姓名：</span> <input
						id="realname" name="realname" value="${securityUser.realname }"
						type="text"
						class="form-control validate[required,maxSize[64]] required"
						style="width: 300px;"></input> <span
						class="glyphicon glyphicon-star"></span>
				</div>

				<div class="input-group" style="margin-bottom: 10px;">
					<span class="input-group-addon"
						style="width: 150px; text-align: right;">电话号码：</span> <input
						id="phone" name="phone"
						value="${securityUser.phone}" type="text"
						class="form-control validate[custom[phone],maxSize[50]]"
						style="width: 300px;"></input> <span
						class="glyphicon glyphicon-star"></span>
				</div>
				
				<div class="input-group" style="margin-bottom: 10px;">
					<span class="input-group-addon"
						style="width: 150px; text-align: right;">邮箱：</span> <input
						id="email" name="email"
						value="${securityUser.email}" type="text"
						class="form-control validate[custom[email],maxSize[50]]"
						style="width: 300px;"></input> <span
						class="glyphicon glyphicon-star"></span>
				</div>
				
				<div class="input-group" style="margin-bottom: 10px;">
			      <span class="input-group-addon" style="width:150px;text-align: right;">选择用户类型：</span>
			      		<select id="userType" name="userType" class="form-control validate[required] required" style="width: 300px;">
			      			<option value="">请选择用户类型</option>
			                <option value="1" <c:if test="${securityUser.userType=='1'}">selected</c:if>>门店用户</option>
			                <option value="2" <c:if test="${securityUser.userType=='2'}">selected</c:if>>工厂用户</option>
			                <option value="3" <c:if test="${securityUser.userType=='3'}">selected</c:if>>普通管理员</option>
			            </select>
			      <span class="glyphicon glyphicon-star"></span>
			    </div>
			    
			    <div id="storeDiv" class="input-group" style="margin-bottom: 10px;display:none">
			      <span class="input-group-addon" style="width:150px;text-align: right;">选择门店：</span>
			      		<select id="storeId" name="storeId" class="form-control validate[required] required" style="width: 300px;">
			                <option value="">请选择门店</option>
			                <c:forEach items="${storeList}" var="dto" varStatus="status">
			                <option value="${dto.id}" <c:if test="${securityUser.storeId==dto.id}">selected</c:if>>${dto.storeName}</option>
			                </c:forEach>
			            </select>
			      <span class="glyphicon glyphicon-star"></span>
			    </div>
				 
				<div class="form-group">
					<div class="col-md-offset-2 col-md-10">
						<input type='submit' id='submit' class='btn btn-primary'
							value='保存' data-loading='稍候...' /> 
					</div>
				</div>
			</form>
		</div>
	</div>
	</section> 
	</article>
</body>
</html>