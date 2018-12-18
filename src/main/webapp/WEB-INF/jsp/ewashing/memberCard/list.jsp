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
			+ path + "memberCard";
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>会员卡管理</title>

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

function recharge(id){
	art.dialog.open( '<%=basePath%>/memberCard/recharge?id='+id, {
				title : '会员卡充值',
				width : 650,
				height : 500,
				lock : true,
				close:function (){
					seach();
				}
	});
}

function refund(id){
	art.dialog.open( '<%=basePath%>/memberCard/refund?id='+id, {
				title : '会员卡退款',
				width : 650,
				height : 500,
				lock : true,
				close:function (){
					seach();
				}
	});
}

function reportLoss(id){
	art.dialog.confirm('确定要挂失该卡吗？', function () {
		jQuery.ajax({
			url : "<%=basePath%>/memberCard/reportLoss",
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

function unReportLoss(id){
	art.dialog.confirm('确定要恢复该卡吗？', function () {
		jQuery.ajax({
			url : "<%=basePath%>/memberCard/unReportLoss",
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

function reback(id){
	art.dialog.confirm('确定执行退卡操作吗？', function () {
		jQuery.ajax({
			url : "<%=basePath%>/memberCard/reback",
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

function fill(id){
	art.dialog.open( '<%=basePath%>/memberCard/fill?id='+id, {
		title : '会员卡补卡',
		width : 650,
		height : 500,
		lock : true,
		close:function (){
			seach();
		}
	});
}


function rechargeHistory(id,operType){
	art.dialog.open( '<%=basePath%>/memberCard/queryCardFlowing?id='+id+'&operType='+operType, {
		title : '会员卡充值记录',
		width : 1200,
		height : 500,
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
//	params = decodeURIComponent(params,true);
//	alert(params);
//	params=encodeURI(encodeURI(params));
    location.href = "<%=basePath%>/memberCard/list?"+params;
}

// 清空查询参数
function resetSeach(){
	$("#storeName").val();
	$("#storeNo").val();
	location.href = "<%=basePath%>/memberCard/list";
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
		<em>会员卡管理</em>
	</div>
	<!-- 搜索条件 -->
	<form id="formId" name="formId" class="form-horizontal" role="form" method='get' action="<%=basePath%>/memberCard/list">
	<div  style="padding-left: 10px; margin-top: 5px;">
		<table cellpadding="0" cellspacing="0" border="0" width="100%" >
			<tr>
				<td width="15%">
					<div class="input-group" >
						<span class="input-group-addon" style="width:20px; text-align: left;">会员卡号：</span> 
						<input id="cardNumber" name="cardNumber" value="${memberCard.cardNumber }" type="text" class="form-control" style="width: 150px;">
						<span class="glyphicon glyphicon-star"></span>
					</div>
				</td>
				
				<td width="15%">
					<div class="input-group" >
						<span class="input-group-addon" style="width:20px; text-align: left;">手机号码：</span> 
						<input id="mobilePhone" name="mobilePhone" value="${memberCard.mobilePhone }" type="text" class="form-control" style="width: 150px;">
						<span class="glyphicon glyphicon-star"></span>
					</div>
				</td>
				
				<td width="15%">
					<div class="input-group" >
						<span class="input-group-addon" style="width:20px; text-align: left;">创建时间：</span> 
						<input id="createTimeStr" name="createTimeStr" value="${createTimeStr }" type="text" class="form-control form-datetime" style="width: 150px;">
						<span class="glyphicon glyphicon-star"></span>
					</div>
				</td>
				
				<td width="15%">
					<div class="input-group" >
						<span class="input-group-addon" style="width:20px; text-align: left;">卡状态：</span> 
						<select id="cardStatus" name="cardStatus" style="width: 150px;" class="form-control">
							<option value ="">全部</option>
							<option value ="new" <c:if test="${memberCard.cardStatus=='new' }">selected="selected"</c:if> >新卡</option>
							<option value ="normal" <c:if test="${memberCard.cardStatus=='normal' }">selected="selected"</c:if> >正常</option>
							<option value ="back_card" <c:if test="${memberCard.cardStatus=='back_card' }">selected="selected"</c:if> >退卡</option>
							<option value ="report_loss" <c:if test="${memberCard.cardStatus=='report_loss' }">selected="selected"</c:if> >挂失</option>
							<option value ="fill_card" <c:if test="${memberCard.cardStatus=='fill_card' }">selected="selected"</c:if> >补卡</option>
							<option value ="cancel" <c:if test="${memberCard.cardStatus=='cancel' }">selected="selected"</c:if> >作废</option>
						</select>
						<span class="glyphicon glyphicon-star"></span>
					</div>
				</td>
				
				<td width="30%" align="right">
					<div class="input-group" style="padding-right: 10px;">
						<button class="btn btn-primary" type="button" id="reloadDatatable" onclick="seach()" style="margin-right: 10px;">查  询</button>
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
							<th data-width='80'>会员卡号</th>
							<th data-width='100'>手机号码</th>
							<th data-width='100'>姓名</th>
							<th data-width='100'>现金金额</th>
							<th data-width='100'>赠送金额</th>
							<th data-width='100'>登记门店</th>
							<th data-width='50'>卡状态</th>
							<th data-width='200' style="text-align:center">操作</th>
						</tr>
					</thead>
					<!-- 同步表格 -->
					<tbody>
 						<!--  PageHelper -->
						<c:forEach items="${page.list}" var="dto" >
							<tr>
								<td>${dto.cardNumber }</td>
								<td>${dto.mobilePhone }</td>
								<td>${dto.memberName }</td>
								<td>${dto.cashAmount }</td>
								<td>${dto.givedAmount }</td>
								<td>${dto.storeName }</td>
								<td>
									<c:if test="${dto.cardStatus=='new' }">新卡</c:if>
									<c:if test="${dto.cardStatus=='normal' }">正常</c:if>
									<c:if test="${dto.cardStatus=='back_card' }">退卡</c:if>
									<c:if test="${dto.cardStatus=='report_loss' }">挂失</c:if>
									<c:if test="${dto.cardStatus=='fill_card' }">补卡</c:if>
									<c:if test="${dto.cardStatus=='cancel' }">作废</c:if>
								</td>
								<td>
									<c:choose>
									   <c:when test="${dto.cardStatus=='normal' || dto.cardStatus=='fill_card'}">  
									   	<a href="javascript:void(0)" onclick="recharge('${dto.id}')">充值 |</a>    
									   	<a href="javascript:void(0)" onclick="refund('${dto.id}')">退款 |</a>
									   	<a href="javascript:void(0)" onclick="reportLoss('${dto.id}')">挂失 |</a>
									   	<a href="javascript:void(0)" onclick="fill('${dto.id}')">补卡 |</a>
									   	<a href="javascript:void(0)" onclick="reback('${dto.id}')">退卡 |</a>
									   	<a href="javascript:void(0)" onclick="rechargeHistory('${dto.id}','recharge')">充值记录</a>
									   </c:when>
									   <c:when test="${dto.cardStatus=='new' }">
									   </c:when>
									   <c:when test="${dto.cardStatus=='report_loss' }">  
									   	<a href="javascript:void(0)" onclick="unReportLoss('${dto.id}')">取消挂失 |</a> 
									   	<a href="javascript:void(0)" onclick="refund('${dto.id}')">退款 |</a>
									   	<a href="javascript:void(0)" onclick="fill('${dto.id}')">补卡 |</a>
									   	<a href="javascript:void(0)" onclick="reback('${dto.id}')">退卡 |</a>
									   	<a href="javascript:void(0)" onclick="rechargeHistory('${dto.id}','recharge')">充值记录</a>
									   </c:when>
									   <c:otherwise> 
									   	<a href="javascript:void(0)" onclick="rechargeHistory('${dto.id}','recharge')">充值记录</a>
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
								<jsp:param name="path" value="/memberCard" />
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