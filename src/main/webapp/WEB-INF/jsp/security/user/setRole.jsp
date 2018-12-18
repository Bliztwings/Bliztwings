<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ page isELIgnored="false" %>     
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
	String realPath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/user/roleSetting?type=${type}";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置用户角色</title>

	<link type="text/css" href="<%=basePath%>/zui-1.5.0/css/zui.min.css" rel="stylesheet" />
	<link type="text/css" href="<%=basePath%>/zui-1.5.0/css/zui.css" rel="stylesheet" />
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/jquery/jquery.js"></script>
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/js/zui.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/js/zui.js"></script>
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/js/messager.js"></script>
	<script src="<%=basePath%>/js/jquery/jquery.form.js" type="text/javascript"></script>
	<script type="text/javascript" src="<%=basePath%>/js/jquery/jquery.json-2.2.js"></script>

<!--  art.dialog  -->
<link href="<%=basePath%>/js/artDialog4.1.7/skins/blue.css" rel="stylesheet" type="text/css"/>
<script src="<%=basePath%>/js/artDialog4.1.7/artDialog.js" type="text/javascript"></script>
<script src="<%=basePath%>/js/artDialog4.1.7/plugins/iframeTools.source.js" type="text/javascript"></script>	
<script type="text/javascript">

function roleSetting(type){
	var userId = '${userId}';
	if(type==1){
		location.href = '<%=basePath%>/user/roleSetting?type=1&userId='+userId;	
	}
	else{
		location.href = '<%=basePath%>/user/roleSetting?type=0&userId='+userId;	
	}
}


//搜索查询
function seach(){
	var params = $("#formId").serialize();
    location.href = "<%=realPath%>/?"+params;
}

// 清空查询参数
function resetSeach(){
	$("#name").val();
	location.href = "<%=realPath%>";
}

function addOrRemoveRole(roleId,type){
	var userId = '${userId}';
	if(type==1){
		//移除角色
		jQuery.ajax({
	        url:"<%=basePath%>/user/addOrRemoveRole?type=1&roleId="+roleId+"&userId="+userId,
	        type:"POST",
	        cache:false,
	        async:true,
	        dataType: "json",
	        success:function(data, textStatus, jqXHR) {
	        	 if(data){
	            	  if(data.resultCode == 1){
	                   	    art.dialog.alert(data.resultMsg,function(){                  	    	
		                   	    location.href = "<%=basePath%>/user/roleSetting?type=1&userId="+userId;
	                 	   });
	            	  }else {
							art.dialog.alert(data.resultMsg);
	      		            return false;
	            	  }
	        	 }
	        },
	        error:function(XMLHttpRequest, textStatus, errorThrown) {		        	
	        	art.dialog.alert("移除用户角色出现异常，请稍后再试！");
	    		return false;
	        }
	    });
	}
	else{
		//添加角色
		jQuery.ajax({
	        url:"<%=basePath%>/user/addOrRemoveRole?type=0&roleId="+roleId+"&userId="+userId,
	        type:"POST",
	        cache:false,
	        async:true,
	        dataType: "json",
	        success:function(data, textStatus, jqXHR) {
	        	 if(data){
	            	  if(data.resultCode == 1){
	                   	    art.dialog.alert(data.resultMsg,function(){                  	    	
		                   	    location.href = "<%=basePath%>/user/roleSetting?type=0&userId="+userId;
	                 	   });
	            	  }else {
							art.dialog.alert(data.resultMsg);
	      		            return false;
	            	  }
	        	 }
	        },
	        error:function(XMLHttpRequest, textStatus, errorThrown) {		        	
	        	art.dialog.alert("添加用户角色出现异常，请稍后再试！");
	    		return false;
	        }
	    });
	}
	
}
</script>
</head>
<body>

<ul class="nav nav-tabs">
  <li <c:if test="${type=='1'}">class="active"</c:if> onclick="roleSetting(1);"><a data-tab href="#" data-target="#tabContent1" data-toggle="tab">已授权角色</a></li>
  <li <c:if test="${type=='0'}">class="active"</c:if> onclick="roleSetting(0);"><a data-tab href="#" data-target="#tabContent2" data-toggle="tab">未授权角色</a></li>

</ul>
	<form id="formId" name="formId" class="form-horizontal" role="form" method='get' action="<%=basePath%>/user/roleSetting?type=${type }"">
<div class="tab-content">
  <div class="tab-pane fade <c:if test="${type=='1'}">active in</c:if>" id="tabContent1">
			<!-- 搜索条件 -->

		<div  style="padding-left: 10px; margin-top: 5px;">

		<input id="id" name="id" type="hidden" value="${id }">
				<input id="type" name="type" type="hidden" value="${type }">
	
		</div>
<br style="border: 1px;border-color: fff"/>
<article>

		<!-- 表格-->
		<div class="segment">
			<div id="dtFromData"></div>
			<div style="padding-left: 10px; padding-right: 10px;">
				<table class="table datatable table-bordered table-striped table-hover" id="datatable">
					<thead>
						<tr>
							<th class='text-center' data-col-class='text-center' date-type="number" style="width: 20%">ID</th>
							<th style="width: 25%"  class='text-primary'>角色名</th>
							<th class='text-center' style="width: 30%">描述</th>
							<th class='text-center' style="width: 25%">操作</th>
						</tr>
					</thead>
					<!-- 同步表格 -->
					<tbody>
						<c:forEach items="${page.list}" var="role">
							<tr>
								<td>${role.id }</td>
								<td>${role.name }</td>
								<td>${role.description }</td>
								<td class='text-center'> 
									<button id="edit" type="button" class="btn btn-primary btn-xs"
										onclick="addOrRemoveRole('${role.id }',1);">
										<i class="icon-edit"></i> 移除权限
									</button>

								</td>
							</tr>
						</c:forEach>
					</tbody>					
					<!-- 同步表格 end  -->

					<tfoot>
						<td align="right" colspan="8">		
						<c:if test="${type=='1'}">
						<jsp:include page="../../inc/page_PageHelper.jsp">
						
								<jsp:param name="path" value="/user/roleSetting?type=1" />
						</jsp:include>
						</c:if>
						</td>
					</tfoot>
				</table>

			</div>
		</div>
		</article>

    
    
  </div>
  <div class="tab-pane fade <c:if test="${type=='0'}">active in</c:if>" id="tabContent2">
  				<!-- 搜索条件 -->
		<div  style="padding-left: 10px; margin-top: 5px;">

		</div>
<br style="border: 1px;border-color: fff"/>
<article>
		
		<!-- 表格-->
		<div class="segment">
			<div id="dtFromData"></div>
			<div style="padding-left: 10px; padding-right: 10px;">
				<table class="table datatable table-bordered table-striped table-hover" id="datatable">
					<thead>
						<tr>
							<th class='text-center' data-col-class='text-center' date-type="number" style="width: 20%">ID</th>
							<th style="width: 25%"  class='text-primary'>角色名</th>

							<th class='text-center' style="width: 30%">描述</th>
							<th class='text-center' style="width: 25%">操作</th>
						</tr>
					</thead>
					<!-- 同步表格 -->
					<tbody>
 						<!--  PageHelper -->
						<c:forEach items="${page.list}" var="role">
							<tr>
								<td>${role.id }</td>
								<td>${role.name }</td>
								<td>${role.description }</td>
								<td class='text-center'> 
									<button id="edit" type="button" class="btn btn-primary btn-xs"
										onclick="addOrRemoveRole('${role.id }',0);">
										<i class="icon-edit"></i> 赋权限
									</button>

								</td>
							</tr>
						</c:forEach>
					</tbody>					
					<!-- 同步表格 end  -->
					
					<tfoot>
						<td align="right" colspan="8">		
						
						<!--  PageHelper -->	
						<c:if test="${type=='0'}">
							<jsp:include page="../../inc/page_PageHelper.jsp">
									<jsp:param name="path" value="/user/roleSetting?type=0" />
							</jsp:include>
						</c:if>
						</td>
					</tfoot>
				</table>

			</div>
		</div>
		</article>


  </div>

</div>
	</form>
</body>
</html>