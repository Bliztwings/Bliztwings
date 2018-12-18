<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
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

//$('#datatable').datatable({fixedHeaderOffset: 41, checkable: true});

function add(obj){

	art.dialog.open( '<%=basePath%>/user/preCreate', {
				title : '添加用户',
				width : 600,
				height : 400,
				lock : true
	});

}

// 搜索查询
function seach(){
	var params = $("#formId").serialize();
    location.href = "<%=basePath%>/session/list?"+params;
}

function kickOff(username){
	art.dialog.confirm('确实要踢出该用户吗？', function () {
		jQuery.ajax({
			url : "<%=basePath%>/session/kickOff",
			type : "post",
			cache : false,
			dataType : "json",
			data : {'username':username},
			success : function(data, textStatus) {
				if (data.resultCode == "0") {
					//art.dialog.alert('删除失败');
					art.dialog.alert(data.resultMsg);
					return false;
				} else {
					//art.dialog.alert('删除成功');
					art.dialog.alert(data.resultMsg,function(){
						location.reload();
					});
				}
			},
		});
	}, function () {
	});
}

// 清空查询参数
function resetSeach(){
	$("#username").val();
	location.href = "<%=basePath%>/session/list";
}


function showResponse(resultData) {
	var trStr ="";
	if(resultData == undefined || resultData =="" ||  resultData == null || resultData.length<=0){
		//数据为空，
		var td_i = $("#datatable thead tr th").size();
		trStr="<tr><td colspan='"+td_i+"'>没有找到任何相关记录</td></tr>";
	}else{
		//得到要填充的字符串
		for(var i=0;i<resultData.length;i++){
			user=resultData[i];
			trStr += '<tr>';
			trStr += '<td style="width: 100px;">'+ user.id +'</td>';
			trStr += '<td>'+ user.username +'</td>';
			trStr += '<td>'+ user.realname +'</td>';
			trStr += '<td>'+ user.email +'</td>';
			trStr += '<td>'+ user.phone +'</td>';
			trStr += '<td>'+ user.orgId +'</td>';
			trStr += '<td>'+ user.createTime +'</td>';

			if( user.status == "enabled"){
				trStr += '<td>可用</td>';
			}else{ 
				trStr += '<td>不可用</td>';
			}
			/* 表格按钮  */
/* 			trStr += '<td><div class="yanshi_newultda">'
					+ '<span class="icon_chakan"> <a href="javascript:showView('+d.pkid+')" title="查看">查看</a></span>' 
					+ '<span class="icon_Modify"> <a href="javascript:preUpdate('+d.pkid+')" title="修改">修改</a></span>'
					+ '<span class="icon_del"> <a href="javascript:del('+d.pkid+')" title="删除">删除</a> </span>'
				    + '</div></td>'; */
		    trStr +='</tr>';
		}
		
	}
	//进行表格填充
	$(".datatable-rows .datatable-rows-span .datatable-wrapper table tbody").html(trStr);
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
		<em>会话管理</em>
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
		<!-- 表格-->
		<div class="segment">
			<div id="dtFromData"></div>
			<div style="padding-left: 10px; padding-right: 10px;">
				<table class="table datatable table-bordered table-striped table-hover" id="datatable">
					<thead>
						<tr>
							<th data-width='50' class='text-primary'>序号</th>
							<th data-width='100' class='text-primary'>登录名</th>
							<th data-width='100' class='text-primary'>登录时间</th>
							<th data-width='100' class='text-primary'>登录IP</th>
							<th data-width='80' class='text-primary'>是否当前用户</th>
							<th data-width='100' class='text-primary'>操作</th>
						</tr>
					</thead>
					<!-- 同步表格 -->
					<tbody>
<%-- 					    <!--  paginator -->
						<c:forEach items="${list}" var="user"> --%>

 						<!--  PageHelper -->
						<c:forEach items="${userList}" var="var" varStatus="status">
							<tr>
								<td>${status.index+1}</td>
								<td>${var.username}</td>
								<td><fmt:formatDate value="${var.loginTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td>${var.ip}</td>
								<td>
									<c:choose>
									       <c:when test="${var.isCurr}">
									       是
									       </c:when>
									       <c:otherwise>
									       </c:otherwise>
									</c:choose>
								</td>
								<td>
									<c:if test="${!var.isCurr && var.username!='admin'}">
										<shiro:hasPermission name="session:kickoff">
										<button id="edit" type="button" class="btn btn-success btn-xs"
											onclick="kickOff('${var.username}')">
											<i class="icon-remove"></i> 踢出
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
				</table>

			</div>
		</div>
		</article>
	</form>
</body>

<script type="text/javascript">

$('table.datatable').datatable({	
	sortable: true,
	//checkable: true,
//	checkedClass: "checked",
	fixedHeaderOffset: 0
	
});




</script>
</html>