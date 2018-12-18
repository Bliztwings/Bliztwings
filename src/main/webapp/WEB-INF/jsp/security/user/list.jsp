<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
	String realPath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "user";
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>用户列表</title>

	<link type="text/css" href="<%=basePath%>/zui-1.5.0/css/zui.min.css" rel="stylesheet" />
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/jquery/jquery.js"></script>
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/js/zui.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/datatable/zui.datatable.js"></script>
	<link type="text/css" href="<%=basePath%>/zui-1.5.0/lib/datatable/zui.datatable.css" rel="stylesheet" />
	<script src="<%=basePath%>/js/jquery/jquery.form.js" type="text/javascript"></script>
	<script type="text/javascript"
		src="<%=basePath%>/js/jquery/jquery.json-2.2.js"></script>
	
	<!--  art.dialog  -->
	<link href="<%=basePath%>/js/artDialog4.1.7/skins/blue.css"
		rel="stylesheet" type="text/css" />
	<script src="<%=basePath%>/js/artDialog4.1.7/artDialog.js"
		type="text/javascript"></script>
	<script
		src="<%=basePath%>/js/artDialog4.1.7/plugins/iframeTools.source.js"
		type="text/javascript"></script>
		
<style type="text/css">
.nav_tit{height:40px;font:14px/40px 'Microsoft Yahei';color:#000;padding-left:25px;background-image: url(<%=basePath%>/common/images/public/next.gif);background-repeat: no-repeat;
	background-position: 10px 13px;background-color: #EDF6FA;border-bottom: 1px double #D7E4EA;}
.nav_tit em{height:40px;font:14px/40px 'Microsoft Yahei';color:#000;float:left;}
</style>
<script type="text/javascript">
function handlerAdd(obj){
	art.dialog.open( '<%=basePath%>/user/preCreate', {
				title : '添加用户',
				width : 600,
				height : 400,
				lock : true,
				close:function (){
					seach();
				}
	});
}

function editUser(id){
	art.dialog.open( '<%=basePath%>/user/preUpdate?id='+id, {
				title : '修改用户信息',
				width : 600,
				height : 400,
				lock : true,
				close:function (){
					seach();
				}
	});
}

function roleSetting(id){
	art.dialog.open( '<%=basePath%>/user/roleSetting?userId='+id, {
				title : '设置用户角色',
				width : 600,
				height : 400,
				lock : true
	});
}

function removeUser(id){
	art.dialog.confirm('确实要删除该用户吗？', function () {
		jQuery.ajax({
			url : "<%=basePath%>/user/remove",
			type : "post",
			cache : false,
			dataType : "json",
			data : {'id':id},
			success : function(data, textStatus) {
				if (data.resultCode == "0") {
					art.dialog.alert(data.resultMsg);
				} else {
					art.dialog.alert(data.resultMsg,function(){
						location.reload();
					});
				}
			},
		});
	}, function () {
	});
}


// 搜索查询
function seach(){
	var params = $("#formId").serialize();
    location.href = "<%=basePath%>/user?"+params;
}

// 清空查询参数
function resetSeach(){
	$("#username").val();
	$("#realname").val();
	location.href = "<%=basePath%>/user";
}

// 分配角色
function setRoleForUser(id){
	art.dialog.open( '<%=basePath%>/user/setRoleForUser?id='+id, {
		title : '分配角色',
		width : 700,
		height : 400,
		lock : true
	});
}

function stopOrStartUser(id, flag){
	if(flag==1){
		art.dialog.confirm('确实要启用该用户吗？', function () {
			jQuery.ajax({
				url : "<%=basePath%>/user/stopOrStartUser",
				type : "post",
				cache : false,
				dataType : "json",
				data : {'id':id,'status':flag},
				success : function(data, textStatus) {
					if (data.resultCode == "0") {
						art.dialog.alert(data.resultMsg);
					} else {
						art.dialog.alert(data.resultMsg,function(){
							location.reload();
						});
					}
				},
			});
		}, function () {
		});
	}
	else{
		art.dialog.confirm('确实要停用该用户吗？', function () {
			jQuery.ajax({
				url : "<%=basePath%>/user/stopOrStartUser",
				type : "post",
				cache : false,
				dataType : "json",
				data : {'id':id,'status':flag},
				success : function(data, textStatus) {
					if (data.resultCode == "0") {
						art.dialog.alert(data.resultMsg);
					} else {
						art.dialog.alert(data.resultMsg,function(){
							location.reload();
						});
					}
				},
			});
		}, function () {
		});
	}
}

</script>
</head>
<body>
<c:if test="${resultCode == 1}">
   <script type="text/javascript">
   		art.dialog.alert('${resultMsg}',function(){
        	  art.dialog.close();
    	    });
	</script>
</c:if>
<c:if test="${resultCode == 0}">
   <script type="text/javascript">
  		 art.dialog.alert('${resultMsg}');
	</script>
</c:if>


	<div class="nav_tit">
		<em>用户管理</em>
	</div>
			<!-- 搜索条件 -->
	<form id="formId" name="formId" class="form-horizontal" role="form" method='get' action="<%=basePath%>/user">
		<div  style="padding-left: 10px; margin-top: 5px;">

			<table cellpadding="0" cellspacing="0" border="0" width="100%" >
				<tr>
					<td width="25%">
						<div class="input-group" >
							<span class="input-group-addon"
								style="width: 80px; text-align: left;">登录名称：</span> 
								<input id="username" name="username" value="${username }"
								type="text" class="form-control validate[maxSize[16]] "
								 style="width: 120px;"> <span
								class="glyphicon glyphicon-star"></span>
						</div>
					</td>
					<td width="25%">
						<div class="input-group" >
							<span class="input-group-addon"
								style="width: 80px; text-align: left;">真实名称：</span> 
								<input id="realname" name="realname" value="${securityUser.realname }"
								type="text" class="form-control validate[maxSize[16]] "
								 style="width: 120px;"> <span
								class="glyphicon glyphicon-star"></span>
						</div>
					</td>

					<td width="50%" align="right">
						<div class="input-group" style="padding-right: 10px;">
							<button class="btn btn-primary" type="button" id="reloadDatatable" onclick="seach()" style="margin-right: 10px;">查  询</button>
							<button class="btn btn-primary" type="button" id="reloadDatatable" onclick="resetSeach()">重   置</button>
						</div>
					</td>
				</tr>
				
			</table>
	
		</div>
<br style="border: 1px;border-color: fff"/>
<article>
		<!-- 按钮组 -->
		<div class="btn-group"
			style="padding-left: 10px; padding-bottom: 5px; padding-top: 10px;">
			<shiro:hasPermission name="user:create">
			<button id="create" type="button" class="btn btn-primary"
				onclick="handlerAdd(this)">
				<i class="icon-plus"></i> 添加
			</button>
			</shiro:hasPermission>
		</div>
		<!-- 表格-->
		<div class="segment">
			<div id="dtFromData"></div>
			<div style="padding-left: 10px; padding-right: 10px;">
				<table class="table datatable table-bordered table-striped table-hover" id="datatable">
					<thead>
						<tr>
							<th data-width='50'>序号</th>
							<th data-width='100'>登录名</th>
							<th data-width='100'>真实名称</th>
							<th data-width='150'>用户类型</th>
							<th data-width='100'>状态</th>
							<th data-width='200'>操作</th>
						</tr>
					</thead>
					<!-- 同步表格 -->
					<tbody>
 						<!--  PageHelper -->
						<c:forEach items="${page.list}" var="user" varStatus="status">
							<tr>
								<td>${status.index+1}</td>
								<td>${user.username }</td>
								<td>${user.realname }</td>
								<td>
									<c:if test="${user.username=='admin'}">
									超级管理员
									</c:if>
									<c:if test="${user.userType=='1'}">
									门店用户（${user.storeName }）
									</c:if>
									<c:if test="${user.userType=='2'}">
									工厂用户
									</c:if>
									<c:if test="${user.userType=='3'}">
									普通管理员
									</c:if>
									
									<c:if test="${user.userType=='4'}">
									o2o管理员
									</c:if>
								</td>
								<td>
									<c:if test="${user.status=='enabled'}">启用
									</c:if>
									<c:if test="${user.status=='disabled'}">停用
									</c:if>
								</td>
								<td>
									<c:if test="${user.username!='admin'}">
										<shiro:hasPermission name="user:update">
											<button id="edit" type="button" class="btn btn-success btn-xs"
												onclick="editUser('${user.id}')">
												<i class="icon-edit"></i> 修改
											</button>
										</shiro:hasPermission>
										<shiro:hasPermission name="user:delete">
											<button id="edit" type="button" class="btn btn-danger btn-xs"
												onclick="removeUser('${user.id}')">
												<i class="icon-remove"></i> 删除
											</button>
										</shiro:hasPermission>
									
										<c:if test="${user.status=='enabled'}">
											<shiro:hasPermission name="user:stop">
												<button id="edit" type="button" class="btn btn-danger btn-xs"
													onclick="stopOrStartUser('${user.id}',0)">
													<i class="icon-edit"></i> 停用
												</button>
											</shiro:hasPermission>
										</c:if>
										<c:if test="${user.status=='disabled'}">
											<shiro:hasPermission name="user:start">
												<button id="edit" type="button" class="btn btn-danger btn-xs"
													onclick="stopOrStartUser('${user.id}',1)">
													<i class="icon-edit"></i> 启用
												</button>
											</shiro:hasPermission>
										</c:if>
										
										<shiro:hasPermission name="user:start">
											<button id="edit" type="button" class="btn btn-danger btn-xs"
												onclick="setRoleForUser('${user.id}',1)">
												<i class="icon-edit"></i> 分配角色
											</button>
										</shiro:hasPermission>
											
									</c:if>
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
						<!--  PageHelper -->	
						<jsp:include page="../../inc/page_PageHelper.jsp">
								<jsp:param name="path" value="/user" />
						</jsp:include></td>
					</tfoot>
				</table>

			</div>
		</div>
		</article>
	</form>
</body>
<script type="text/javascript">
	$('table.datatable').datatable({
		sortable : true,
		//checkable : true,
		//checkedClass: "active",
		storage: false,
		fixedHeaderOffset : 0,
	});
</script>
</html>