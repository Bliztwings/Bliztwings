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
			+ path + "/organization/${id }/setPrivilege?isPrivilege=${isPrivilege}";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机构组织赋权限</title>

	<link type="text/css" href="<%=basePath%>/zui-1.5.0/css/zui.min.css" rel="stylesheet" />
	<link type="text/css" href="<%=basePath%>/zui-1.5.0/css/zui.css" rel="stylesheet" />
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/jquery/jquery.js"></script>
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/js/zui.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/js/zui.js"></script>
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/js/messager.js"></script>
	<script src="<%=basePath%>/js/jquery/jquery.form.js" type="text/javascript"></script>
	<script type="text/javascript"
		src="<%=basePath%>/js/jquery/jquery.json-2.2.js"></script>

<!--  art.dialog  -->
<link href="<%=basePath%>/js/artDialog4.1.7/skins/blue.css" rel="stylesheet" type="text/css"/>
<script src="<%=basePath%>/js/artDialog4.1.7/artDialog.js" type="text/javascript"></script>
<script src="<%=basePath%>/js/artDialog4.1.7/plugins/iframeTools.source.js" type="text/javascript"></script>	
<script type="text/javascript">

function showIsPrivilege(){
	location.href = '<%=basePath%>/organization/${id }/setPrivilege?isPrivilege=YES';	
}

function showNotPrivilege(){
	location.href = '<%=basePath%>/organization/${id }/setPrivilege?isPrivilege=NOT';	
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

function addPrivilege(roleId){
	
	var params = $("#formId").serialize();
	jQuery.ajax({
        url:"<%=basePath%>/organization/${id }/addPrivilege?isPrivilege=${isPrivilege}&roleId="+roleId,
        type:"POST",
        cache:false,
        async:true,
        dataType: "json",
        data: params,
        success:function(data, textStatus, jqXHR) {
        	 if(data){
            	  if(data.resultCode == 1){
                   	    art.dialog.alert(data.resultMsg,function(){                  	    	

	                   	    location.href = "<%=basePath%>/organization/${id }/setPrivilege?isPrivilege=${isPrivilege}";
                 	   });
            	  }else {
						art.dialog.alert(data.resultMsg);
      		            return false;
            	  }
        	 }
        },
        error:function(XMLHttpRequest, textStatus, errorThrown) {		        	
        	art.dialog.alert("添加机构组织角色出现异常，请稍后再试！");
    		return false;
        }
    });
}

function deletePrivilege(roleId){
	
	var params = $("#formId").serialize();
	jQuery.ajax({
        url:"<%=basePath%>/organization/${id }/deletePrivilege?isPrivilege=${isPrivilege}&roleId="+roleId,
        type:"POST",
        cache:false,
        async:true,
        dataType: "json",
        data: params,
        success:function(data, textStatus, jqXHR) {
        	 if(data){
            	  if(data.resultCode == 1){
                   	    art.dialog.alert(data.resultMsg,function(){

	                   	    location.href = "<%=basePath%>/organization/${id }/setPrivilege?isPrivilege=${isPrivilege}";
                 	   });
            	  }else {
						art.dialog.alert(data.resultMsg);
      		            return false;
            	  }
        	 }
        },
        error:function(XMLHttpRequest, textStatus, errorThrown) {		        	
        	art.dialog.alert("删除机构组织角色出现异常，请稍后再试！");
    		return false;
        }
    });
}

</script>
</head>
<body>

<ul class="nav nav-tabs">
  <li <c:if test="${isPrivilege=='YES'}">class="active"</c:if> onclick="showIsPrivilege();"><a data-tab href="#" data-target="#tabContent1" data-toggle="tab">已授权角色</a></li>
  <li <c:if test="${isPrivilege=='NOT'}">class="active"</c:if> onclick="showNotPrivilege();"><a data-tab href="#" data-target="#tabContent2" data-toggle="tab">未授权角色</a></li>

</ul>
	<form id="formId" name="formId" class="form-horizontal" role="form" method='get' action="<%=basePath%>/organization/${id }/setPrivilege?isPrivilege=${isPrivilege }"">
<div class="tab-content">
  <div class="tab-pane fade <c:if test="${isPrivilege=='YES'}">active in</c:if>" id="tabContent1">
			<!-- 搜索条件 -->

		<div  style="padding-left: 10px; margin-top: 5px;">

		<input id="id" name="id" type="hidden" value="${id }">
				<input id="isPrivilege" name="isPrivilege" type="hidden" value="${isPrivilege }">
	
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
<%-- 					    <!--  paginator -->
						<c:forEach items="${list}" var="user"> --%>

 						<!--  PageHelper -->
						<c:forEach items="${page.list}" var="role">
							<tr>
								<td>${role.id }</td>
								<td>${role.name }</td>
								<td>${role.description }</td>
								<td class='text-center'> 
									<button id="edit" type="button" class="btn btn-primary btn-xs"
										onclick="deletePrivilege('${role.id }');">
										<i class="icon-edit"></i> 移除权限
									</button>

								</td>
							</tr>
						</c:forEach>
					</tbody>					
					<!-- 同步表格 end  -->

					<tfoot>
						<td align="right" colspan="8">		
<%-- 						<!--  paginator -->		
						<jsp:include page="../../inc/page_paginator.jsp"> --%>
						
						<!--  PageHelper -->	
						<c:if test="${isPrivilege=='YES'}">
						<jsp:include page="../../inc/page_PageHelper.jsp">
						
								<jsp:param name="path" value="/organization/${id }/setPrivilege?isPrivilege=${isPrivilege }" />
						</jsp:include>
						</c:if>
						</td>
					</tfoot>
				</table>

			</div>
		</div>
		</article>

    
    
  </div>
  <div class="tab-pane fade <c:if test="${isPrivilege=='NOT'}">active in</c:if>" id="tabContent2">
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
<%-- 					    <!--  paginator -->
						<c:forEach items="${list}" var="user"> --%>

 						<!--  PageHelper -->
						<c:forEach items="${page.list}" var="role">
							<tr>
								<td>${role.id }</td>
								<td>${role.name }</td>
								<td>${role.description }</td>
								<td class='text-center'> 
									<button id="edit" type="button" class="btn btn-primary btn-xs"
										onclick="addPrivilege('${role.id }')">
										<i class="icon-edit"></i> 赋权限
									</button>

								</td>
							</tr>
						</c:forEach>
					</tbody>					
					<!-- 同步表格 end  -->
					
					<!-- 异步表格  start-->
<!-- 					<tbody id="tbodydata" >
					</tbody> -->
					<!-- 异步表格 end  -->
					<tfoot>
						<td align="right" colspan="8">		
<%-- 						<!--  paginator -->		
						<jsp:include page="../../inc/page_paginator.jsp"> --%>
						
						<!--  PageHelper -->	
												<c:if test="${isPrivilege=='NOT'}">
						<jsp:include page="../../inc/page_PageHelper.jsp">
						
								<jsp:param name="path" value="/organization/${id }/setPrivilege?isPrivilege=${isPrivilege }" />
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