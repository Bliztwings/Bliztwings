<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
	String realPath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/role";
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>角色列表</title>

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
function add(obj){
	art.dialog.open( '<%=basePath%>/role/preCreate', {
				title : '添加角色',
				width : 600,
				height : 400,
				lock : true,
				close:function (){
					seach();
				}
	});
}


function preEdit(id){
	art.dialog.open( '<%=basePath%>/role/preUpdate?id='+id, {
				title : '修改角色',
				width : 600,
				height : 400,
				lock : true,
				close:function (){
					seach();
				}
	});
}

// 搜索查询
function seach(){
	var params = $("#formId").serialize();
    location.href = "<%=realPath%>/?"+params;
}

// 清空查询参数
function resetSeach(){
	$("#name").val();
	location.href = "<%=realPath%>";
}

function removeRole(id){
	art.dialog.confirm('确实要删除该角色吗？', function () {
		jQuery.ajax({
			url : "<%=basePath%>/role/remove",
			type : "post",
			cache : false,
			dataType : "json",
			data : {'id':id},
			success : function(data, textStatus) {
				if (data.resultCode == "0") {
					art.dialog.alert(data.resultMsg);
					return false;
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

function permissionSetting(id){
	art.dialog.open( '<%=basePath%>/role/toSetPermission?id='+id, {
		title : '角色权限设置',
		width : 550,
		height : 450,
		lock : true
	});
}
</script>
</head>
<!-- 异步表格   <body onload="ajaxReload()"> -->
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
		<em>角色管理</em>
	</div>
			<!-- 搜索条件 -->
	<form id="formId" name="formId" class="form-horizontal" role="form" method='get' action="<%=basePath%>/role">
		<div  style="padding-left: 10px; margin-top: 5px;">

			<table cellpadding="0" cellspacing="0" border="0" width="100%" >
				<tr>
					<td width="25%">
						<div class="input-group" >
							<span class="input-group-addon"
								style="width: 80px; text-align: left;">角色名称：</span> 
								<input id="name" name="name" value="${name }"
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
			<shiro:hasPermission name="role:create">
			<button id="create" type="button" class="btn btn-primary"
				onclick="add(this)">
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
							<th class='text-center' data-col-class='text-center' date-type="number" style="width: 2%">序号</th>
							<th style="width: 35%"  class='text-primary'>角色名</th>
							<th class='text-center' style="width: 35%">描述</th>
							<th class='text-center' style="width: 20%">操作</th>
						</tr>
					</thead>
					<!-- 同步表格 -->
					<tbody>
 						<!--  PageHelper -->
						<c:forEach items="${page.list}" var="role" varStatus="status">
							<tr>
								<td>${status.index+1}</td>
								<td>${role.name }</td>
								<td>${role.description }</td>
								<td class='text-center'> 
									<c:if test="${role.name!='系统管理员'}">
									<shiro:hasPermission name="role:setPermission">
									<button id="edit" type="button" class="btn btn-success btn-xs"
										onclick="permissionSetting('${role.id}')">
										<i class="icon-asterisk"></i> 权限设置
									</button>
									</shiro:hasPermission>
									<shiro:hasPermission name="role:update">
									<button id="edit" type="button" class="btn btn-primary btn-xs"
										onclick="preEdit('${role.id}')">
										<i class="icon-edit"></i> 修改
									</button>
									</shiro:hasPermission>
									<shiro:hasPermission name="role:delete">
									<button type="button" class="btn btn-primary btn-xs" 
										onclick="removeRole('${role.id}')">
										<i class="icon-remove"></i> 删除
									</button>
									</shiro:hasPermission>
									</c:if>
								</td>
							</tr>
						</c:forEach>
					</tbody>					
					<!-- 同步表格 end  -->
					<tfoot>
						<td align="right" colspan="8">		
						<!--  PageHelper -->	
						<jsp:include page="../../inc/page_PageHelper.jsp">
								<jsp:param name="path" value="/role" />
						</jsp:include>
						
						</td>
					</tfoot>
				</table>

			</div>
		</div>
		</article>
	</form>
</body>

<script type="text/javascript">
$('table.datatable').datatable({	
	sortable: true,
//	checkable: true,
//	checkedClass: "checked",
	fixedHeaderOffset: 0
	
});
</script>
</html>