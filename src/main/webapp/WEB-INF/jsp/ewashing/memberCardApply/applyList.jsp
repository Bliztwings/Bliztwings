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
			+ path + "memberCardApply";
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>会员卡申请</title>

	<link type="text/css" href="<%=basePath%>/zui-1.5.0/css/zui.min.css" rel="stylesheet" />
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/jquery/jquery.js"></script>
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/js/zui.min.js"></script>
	<script src="<%=basePath%>/js/jquery/jquery.form.js" type="text/javascript"></script>
	<script type="text/javascript" src="<%=basePath%>/js/jquery/jquery.json-2.2.js"></script>
	
	<!-- form验证 -->
	<link rel="stylesheet" href="<%=basePath%>/js/validationEngine/css/validationEngine.jquery.css" type="text/css"/>
	<script src="<%=basePath%>/js/validationEngine/js/languages/jquery.validationEngine-zh_CN.js" type="text/javascript" charset="utf-8"></script>
	<script src="<%=basePath%>/js/validationEngine/js/jquery.validationEngine-2.6.4.js" type="text/javascript" charset="utf-8"></script>
	
	<!--日期控件JS-->
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/datatable/zui.datatable.js"></script>
	<link type="text/css" href="<%=basePath%>/zui-1.5.0/lib/datatable/zui.datatable.css" rel="stylesheet" />
	<link rel="stylesheet" href="<%=basePath%>/zui-1.5.0/lib/datetimepicker/datetimepicker.min.css">
	<script src="<%=basePath%>/zui-1.5.0/lib/datetimepicker/datetimepicker.min.js"></script> 
	
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
jQuery(document).ready(function(){
    jQuery("#formId").validationEngine('attach',{promptPosition: "centerRight"}); 
    $(".form-datetime").datetimepicker({
        weekStart: 1,
        todayBtn:  1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        forceParse: 0,
        showMeridian: 1,
        format: "yyyy-mm-dd hh:ii:ss"
    });
});

// 搜索查询
function seach(){
	var params = $("#formId").serialize();
    location.href = "<%=basePath%>/memberCardApply/list?"+params;
}

// 清空查询参数
function resetSeach(){
	$("#storeName").val();
	$("#storeNo").val();
	location.href = "<%=basePath%>/memberCardApply/list";
}

function apply(obj){
	art.dialog.open( '<%=basePath%>/memberCardApply/beforeApply', {
				title : '会员卡申请',
				width : 600,
				height : 450,
				lock : true,
				close:function (){
					seach();
				}
	});
}

function update(id){
	art.dialog.open( '<%=basePath%>/memberCardApply/beforeUpdate?id='+id, {
				title : '会员卡申请修改',
				width : 600,
				height : 450,
				lock : true,
				close:function (){
					seach();
				}
	});
}

function del(id) {
	art.dialog.confirm('确实要删除该申请记录吗？', function () {
		jQuery.ajax({
			url : "<%=basePath%>/memberCardApply/delMemberCardApply",
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


//收卡操作
function received(id){
	art.dialog.confirm('确定已接收到卡吗？', function () {
		jQuery.ajax({
			url : "<%=basePath%>/memberCardApply/receivedCard",
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
		<em>会员卡申请</em>
	</div>
	<!-- 搜索条件 -->
	<form id="formId" name="formId" class="form-horizontal" role="form" method='get' action="<%=basePath%>/memberCardApply/list">
	<div  style="padding-left: 10px; margin-top: 5px;">
		<table cellpadding="0" cellspacing="0" border="0" width="100%" >
			<tr>
				<td width="15%">
					<div class="input-group" >
						<span class="input-group-addon" style="width:20px; text-align: left;">开始时间：</span> 
						<input id="createTimeBeginStr" name="createTimeBeginStr" value="${createTimeBeginStr }" type="text" class="form-control form-datetime" style="width: 150px;">
						<span class="glyphicon glyphicon-star"></span>
					</div>
				</td>
				
				<td width="15%">
					<div class="input-group" >
						<span class="input-group-addon" style="width:20px; text-align: left;">结束时间：</span> 
						<input id="createTimeEndStr" name="createTimeEndStr" value="${createTimeEndStr }" type="text" class="form-control form-datetime" style="width: 150px;">
						<span class="glyphicon glyphicon-star"></span>
					</div>
				</td>
				
				<td width="15%">
					<div class="input-group" >
						<span class="input-group-addon" style="width:20px; text-align: left;">状态：</span> 
						<select id="status" name="status" style="width: 150px;" class="form-control">
							<option value ="">全部</option>
							<option value ="un_approved" <c:if test="${cardApplyRecord.status=='un_approved' }">selected="selected"</c:if> >待审批</option>
							<option value ="approved" <c:if test="${cardApplyRecord.status=='approved' }">selected="selected"</c:if> >审批通过</option>
							<option value ="un_passed" <c:if test="${cardApplyRecord.status=='un_passed' }">selected="selected"</c:if> >审批不通过</option>
							<!--
							<option value ="warehoused" <c:if test="${cardApplyRecord.status=='warehoused' }">selected="selected"</c:if> >已出库</option>
							-->
							<option value ="received" <c:if test="${cardApplyRecord.status=='received' }">selected="selected"</c:if> >已收卡</option>
						</select>
						<span class="glyphicon glyphicon-star"></span>
					</div>
				</td>
				
				<td width="55%" align="right">
					<div class="input-group" style="padding-right: 10px;">
						<button class="btn btn-primary" type="button" id="reloadDatatable" onclick="seach()" style="margin-right: 10px;">查  询</button>
						<button class="btn btn-primary" type="button" id="reloadDatatable" onclick="apply()" style="margin-right: 10px;">申  请</button>
						<button class="btn btn-primary" type="button" id="reloadDatatable" onclick="resetSeach()" style="margin-right: 10px;">重   置</button>
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
							<th data-width='120'>申请日期</th>
							<th data-width='100'>所属商户</th>
							<th data-width='80'>申请数量</th>
							<th data-width='100'>开始卡号</th>
							<th data-width='100'>结束卡号</th>
							<th data-width='100'>申请门店</th>
							<th data-width='150'>备注</th>
							<th data-width='80'>状态</th>
							<th data-width='80'>审批人</th>
							<th data-width='80'>审批意见</th>
							<th data-width='100'>审批时间</th>
							<th data-width='80' style="text-align:center">操作</th>
						</tr>
					</thead>
					<!-- 同步表格 -->
					<tbody>
 						<!--  PageHelper -->
						<c:forEach items="${page.list}" var="dto" >
							<tr>
								<td><fmt:formatDate value="${dto.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td>${dto.cardSupplier }</td>
								<td>${dto.applyNumber }</td>
								<td>${dto.numberBegin }</td>
								<td>${dto.numberEnd }</td>
								<td>${dto.storeName }</td>
								<td>${dto.remark }</td>
								<td>
									<c:if test="${dto.status=='un_approved' }">待审批</c:if>
									<c:if test="${dto.status=='approved' }">审批通过</c:if>
									<c:if test="${dto.status=='un_passed' }">审批不通过</c:if>
									<c:if test="${dto.status=='warehoused' }">已出库</c:if>
									<c:if test="${dto.status=='received' }">已收卡</c:if>
								</td>
								<td>${dto.approver }</td>
								<td>${dto.approvedRemark }</td>
								<td><fmt:formatDate value="${dto.approvedTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td>
									<c:choose>
									   <c:when test="${dto.status=='un_approved' }">  
									   	<a href="javascript:void(0)" onclick="update('${dto.id}')">修改</a> | <a href="javascript:void(0)" onclick="del('${dto.id}')">删除</a>
									   </c:when>
									   <c:when test="${dto.status=='approved' }">  
									   	<a href="javascript:void(0)" onclick="received('${dto.id}')">收卡</a>       
									   </c:when>
									   <c:otherwise> 
									   </c:otherwise>
									</c:choose>
									
								</td>
							</tr>
						</c:forEach>
					</tbody>					
					<!-- 同步表格 end  -->
					<tfoot>
						<td align="right" colspan="8">		
						<jsp:include page="../../inc/page_PageHelper.jsp">
								<jsp:param name="path" value="/memberCardApply" />
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