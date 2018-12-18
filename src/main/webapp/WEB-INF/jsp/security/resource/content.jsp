<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
	String realPath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/resource/content";
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
function createMenuOrButton(){
	art.dialog.open( '<%=basePath%>/resource/toCreateMenuOrButton?parentId=${parentId}&level=${level}', {
				title : '创建子菜单或按钮',
				width : 600,
				height : 400,
				lock : true
	});
}

function editMenuOrButton(id,level,parentId){
	art.dialog.open( '<%=basePath%>/resource/toEditMenuOrButton?id='+id+'&level='+level+'&parentId='+parentId, {
				title : '修改菜单或按钮',
				width : 600,
				height : 400,
				lock : true
	});
}

function removeMenuOrButton(id,type){
	var desc = type=='menu'? '菜单':'按钮';

	art.dialog.confirm('确实要删除该'+desc+'吗？', function () {
		jQuery.ajax({
			url : "<%=basePath%>/resource/removeMenuOrButton",
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
						parent.location.reload();
					});
				}
			},
		});
	}, function () {
	});
}

//搜索查询
function seach(){
	var name = $("#name").val();
	location.href = "<%=basePath%>/resource/content?parentId=${parentId}&level=${level}&name="+name;
}

// 清空查询参数
function resetSeach(){
	$("#name").val();
	location.href = "<%=basePath%>/resource/content?parentId=${parentId}&level=${level}";
}

function onEnterDown() {
	var type = '${type}';
	if (event.keyCode == 13) {
		event.returnValue = false;
		if(type=='trans'){
			$("#queryTransByKeyword").click();
		}
		else{
			$("#queryJobByKeyword").click();
		}
	}
}

</script>
</head>
<!-- 异步表格   <body onload="ajaxReload()"> -->
<body onkeydown="onEnterDown();">
	    <form id="formId" name="formId" class="form-horizontal" role="form" method='get' action="<%=basePath%>/resource/content?parentId=${parentId}">
	       			<div  style="padding-left: 10px; margin-top: 5px;">
						<table cellpadding="0" cellspacing="0" border="0" width="100%" >
							<tr>
								<td width="25%">
									<div class="input-group" >
										<span class="input-group-addon"
											style="width: 80px; text-align: left;">名称：</span> 
											<input id="name" name="name" value="${name}"
											type="text" class="form-control validate[maxSize[64]] "
											 style="width: 300px;"> <span
											class="glyphicon glyphicon-star"></span>
									</div>
								</td>
								<td width="50%" align="right">
									<div class="input-group" style="padding-right: 10px;">
										<button class="btn btn-primary" type="button" id="queryTransByKeyword" onclick="seach()" style="margin-right: 10px;">查  询</button>
										<button class="btn btn-primary" type="button" id="reloadDatatable" onclick="resetSeach()">重   置</button>
									</div>
								</td>
							</tr>
						</table>
					</div>
	       			<!-- 按钮组 -->
					<div class="btn-group" style="padding-left: 10px; padding-bottom: 5px; padding-top: 10px;">
						<shiro:hasPermission name="resource:create">
						<button id="create" type="button" class="btn btn-primary"
							onclick="createMenuOrButton()">
							<i class="icon-plus"></i> 创建子菜单或按钮
						</button>
						</shiro:hasPermission>
					</div>
					<!-- 表格-->
					<div class="segment" style="width:100%">
						<div id="dtFromData"></div>
						<div style="padding-left: 10px; padding-right: 10px; width:100%">
							<table class="table datatable table-bordered table-hover" id="datatable_trans">
								<thead>
									<tr>
										<th class='text-center' data-col-class='text-center' data-width='20%'>名称</th>
										<c:if test="${level==1}">
										<th class='text-center' data-col-class='text-center' data-width='20%'>URL</th>
										</c:if>
										<th class='text-center' data-col-class='text-center' data-width='20%'>类型</th>
										<c:if test="${level!=0}">
										<th class='text-center' data-col-class='text-center' data-width='20%'>权限代码</th>
										</c:if>
										<th class='text-center' data-col-class='text-center' data-width='40%'>操作</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${list}" var="config">
										<tr>
											<td>${config.name}</td>
											<c:if test="${level==1}">
											<td>${config.url}</td>
											</c:if>
											<td>
												<c:if test="${level==0}">一级菜单</c:if>
												<c:if test="${level==1}">二级菜单</c:if>
												<c:if test="${level==2}">按钮</c:if>
											</td>
											<c:if test="${level!=0}">
											<td>${config.permission}</td>
											</c:if>
											<td> 
												<shiro:hasPermission name="resource:update">
												<button id="edit" type="button" class="btn btn-success btn-xs"
													onclick="editMenuOrButton('${config.id}','${level}','${parentId}')">
													<i class="icon-edit"></i> 修改
												</button>
												</shiro:hasPermission>
												<shiro:hasPermission name="resource:delete">
												<button id="edit" type="button" class="btn btn-success btn-xs"
													onclick="removeMenuOrButton('${config.id}')">
													<i class="icon-remove"></i> 删除
												</button>
												</shiro:hasPermission>
											</td>
										</tr>
									</c:forEach>
								</tbody>	
							</table>
						</div>
					</div>
	</form>
</body>

<script type="text/javascript">

$('table.datatable').datatable({	
	sortable: true,
	//checkable: true,
	storage: false,
//	checkedClass: "checked",
	fixedHeaderOffset: 0
	
});




</script>
</html>