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
			+ path + "member";
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>门店列表</title>

	<link type="text/css" href="<%=basePath%>/zui-1.5.0/css/zui.min.css" rel="stylesheet" />
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/jquery/jquery.js"></script>
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/js/zui.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/datatable/zui.datatable.js"></script>
	<link type="text/css" href="<%=basePath%>/zui-1.5.0/lib/datatable/zui.datatable.css" rel="stylesheet" />
	<script src="<%=basePath%>/js/jquery/jquery.form.js" type="text/javascript"></script>
	<script type="text/javascript" src="<%=basePath%>/js/jquery/jquery.json-2.2.js"></script>
	
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
	art.dialog.open( '<%=basePath%>/member/beforeAdd', {
				title : '添加会员',
				width : 600,
				height : 500,
				lock : true,
				close:function (){
					seach();
				}
	});
}

function editMember(id){
	art.dialog.open( '<%=basePath%>/member/beforeUpdate?id='+id, {
				title : '修改会员信息',
				width : 600,
				height : 500,
				lock : true,
				close:function (){
					seach();
				}
	});
}

function distributionCard(id){
	art.dialog.open( '<%=basePath%>/member/beforeDistributionCard?id='+id, {
		title : '会员发卡',
		width : 650,
		height : 300,
		lock : true,
		close:function (){
			seach();
		}
	});
}

function historyConsumption(mobilePhone){
	art.dialog.open( '<%=basePath%>/orderQuery/payRecord?mobilePhone='+mobilePhone, {
		title : '历史消费',
		width : '90%',
		height : 700,
		lock : true,
		close:function (){
			seach();
		}
	});
}



function delMember(id) {
	art.dialog.confirm('确实要删除该会员吗？', function () {
		jQuery.ajax({
			url : "<%=basePath%>/member/delMember",
			type : "post",
			cache : false,
			dataType : "json",
			data : {'id':id},
			success : function(data, textStatus) {
				if (data.resultCode == "1") {
					art.dialog.alert(data.resultMsg,function(){
						seach();
					});
				} else {
					art.dialog.alert(data.resultMsg);
					return false;
				}
			}
		});
	}, function () {
	});
}

// 搜索查询
function seach(){
	var params = $("#formId").serialize();
    location.href = "<%=basePath%>/member/list?"+params;
}

// 清空查询参数
function resetSeach(){
	$("#storeName").val();
	$("#storeNo").val();
	location.href = "<%=basePath%>/member/list";
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
		<em>会员管理</em>
	</div>
	<!-- 搜索条件 -->
	<form id="formId" name="formId" class="form-horizontal" role="form" method='get' action="<%=basePath%>/member/list">
	<div  style="padding-left: 10px; margin-top: 5px;">
		<table cellpadding="0" cellspacing="0" border="0" width="100%" >
			<tr>
				<td width="15%">
					<div class="input-group" >
						<span class="input-group-addon" style="width:20px; text-align: left;">会员名称：</span> 
						<input id="name" name="name" value="${member.name }" type="text" class="form-control" style="width: 150px;">
						<span class="glyphicon glyphicon-star"></span>
					</div>
				</td>
				
				<td width="15%">
					<div class="input-group" >
						<span class="input-group-addon" style="width:20px; text-align: left;">手机号码：</span> 
						<input id="mobilePhone" name="mobilePhone" value="${member.mobilePhone }" type="text" class="form-control" style="width: 150px;">
						<span class="glyphicon glyphicon-star"></span>
					</div>
				</td>
				
				<td width="15%">
					<div class="input-group" >
						<span class="input-group-addon" style="width:20px; text-align: left;">固定电话：</span> 
						<input id="telephone" name="telephone" value="${member.telephone }" type="text" class="form-control" style="width: 150px;">
						<span class="glyphicon glyphicon-star"></span>
					</div>
				</td>
				
				<td width="15%">
					<div class="input-group" >
						<span class="input-group-addon" style="width:20px; text-align: left;">会员类型：</span> 
						<select id="type" name="type" style="width: 150px;" class="form-control">
							<option value ="">全部</option>
							<option value ="normal" <c:if test="${member.type=='normal' }">selected="selected"</c:if> >普通会员</option>
							<option value ="card" <c:if test="${member.type=='card' }">selected="selected"</c:if> >持卡会员</option>
						</select>
						<span class="glyphicon glyphicon-star"></span>
					</div>
				</td>
				
				<td width="30%" align="right">
					<div class="input-group" style="padding-right: 10px;">
						<button class="btn btn-primary" type="button" id="reloadDatatable" onclick="seach()" style="margin-right: 10px;">查  询</button>
						<button class="btn btn-primary" type="button" id="reloadDatatable" onclick="resetSeach()" style="margin-right: 10px;">重   置</button>
						<button class="btn btn-primary" type="button" id="create" onclick="handlerAdd(this)">添   加</button>
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
							<th data-width='30'>序号</th>
							<th data-width='50'>姓名</th>
							<th data-width='50'>性别</th>
							<th data-width='80'>手机</th>
							<th data-width='100'>固定电话</th>
							<th data-width='80'>会员类型</th>
							<th data-width='100'>登记门店</th>
							<th data-width='150'>备注</th>
							<th data-width='150' style="text-align:center">操作</th>
						</tr>
					</thead>
					<!-- 同步表格 -->
					<tbody>
 						<!--  PageHelper -->
						<c:forEach items="${page.list}" var="dto" varStatus="status">
							<tr>
								<td>${status.index+1}</td>
								<td>${dto.name }</td>
								<td>
										<c:if test="${dto.sex=='0'}">男
										</c:if>
										<c:if test="${dto.sex=='1'}">女
										</c:if>
								</td>
								<td>${dto.mobilePhone }</td>
								<td>${dto.telephone }</td>
								<td>
										<c:if test="${dto.type=='normal'}">普通会员
										</c:if>
										<c:if test="${dto.type=='card'}">持卡会员
										</c:if>
								</td>
								<td>${dto.storeName }</td>
								<td>${dto.remark }</td>
								<td>
									<a href="javascript:void(0)" onclick="editMember('${dto.id}')">修改 |</a>
									<a href="javascript:void(0)" onclick="distributionCard('${dto.id}')">发卡 |</a>
									<a href="javascript:void(0)" onclick="historyConsumption('${dto.mobilePhone}')">历史消费 |</a>
									<a href="javascript:void(0)" onclick="delMember('${dto.id}')">删除</a>
								</td>
							</tr>
						</c:forEach>
					</tbody>					
					<!-- 同步表格 end  -->
					<tfoot>
						<td align="right" colspan="8">		
						<jsp:include page="../../inc/page_PageHelper.jsp">
								<jsp:param name="path" value="/member" />
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
		sortable : true,
		//checkable : true,
		//checkedClass: "active",
		storage: false,
		fixedHeaderOffset : 0,
	});
</script>
</html>