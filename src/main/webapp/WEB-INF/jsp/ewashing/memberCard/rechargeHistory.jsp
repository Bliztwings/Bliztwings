<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>     
<%@ include file="/WEB-INF/jsp/inc/tag_css_js.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>会员卡充值记录</title>

<link type="text/css" href="<%=basePath%>/zui-1.5.0/css/zui.min.css" rel="stylesheet" />
<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/jquery/jquery.js"></script> 
<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/js/zui.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/jquery/jquery.json-2.2.js"></script>

<!-- form验证 -->
<link rel="stylesheet" href="<%=basePath%>/js/validationEngine/css/validationEngine.jquery.css" type="text/css"/>
<!-- <script src="<%=basePath%>/js/jquery/jquery-1.7.2.min.js" type="text/javascript"></script> -->
<script src="<%=basePath%>/js/validationEngine/js/languages/jquery.validationEngine-zh_CN.js" type="text/javascript" charset="utf-8"></script>
<script src="<%=basePath%>/js/validationEngine/js/jquery.validationEngine-2.6.4.js" type="text/javascript" charset="utf-8"></script>

<!--  art.dialog  -->
<link href="<%=basePath%>/js/artDialog4.1.7/skins/blue.css" rel="stylesheet" type="text/css"/>
<script src="<%=basePath%>/js/artDialog4.1.7/artDialog.js" type="text/javascript"></script>
<script src="<%=basePath%>/js/artDialog4.1.7/plugins/iframeTools.source.js" type="text/javascript"></script>


<style>
	ul.ztree {margin-top: 10px;border: 1px solid #617775;background: #f0f6e4;width:220px;height:200px;overflow-y:scroll;overflow-x:auto;}
</style>

<script type="text/javascript">
    jQuery(document).ready(function(){
        jQuery("#formId").validationEngine('attach',{promptPosition: "centerRight"}); 
    });
    
    function toPrintTicket(mcfId){
    	art.dialog.open( '<%=basePath%>/memberCard/printRechargeTicket?mcfId='+mcfId, {
    		title : '打印充值小票',
    		width : 300,
    		height : 500,
    		lock : true
    	});
    }
</script>
</head>
<body>      
<c:if test="${resultCode == 1}">
   <script type="text/javascript">
        art.dialog.alert('${resultMsg}',function(){
     		//art.dialog.top.close();
       		art.dialog.close();
		});
  
	</script>
</c:if>
<c:if test="${resultCode == 0}">
   <script type="text/javascript">
   art.dialog.alert('${resultMsg}');
	</script>
</c:if>
<form id="formId" name="formId" class="form-horizontal" role="form" method='get' action="<%=basePath%>/memberCard/queryCardFlowing">
<input type="hidden" name="id" id="id" value="${id}">
<input type="hidden" name="operType" id="operType" value="${operType}">

  <article>
  <!-- 表格-->
	<div class="segment">
		<div id="dtFromData"></div>
		<div style="padding-left: 10px; padding-right: 10px;">
			<table class="table datatable table-bordered table-striped table-hover" id="datatable">
				<thead>
					<tr>
						<th data-width='80'>操作时间</th>
						<th data-width='80'>操作人</th>
						<th data-width='80'>操作前现金账户</th>
						<th data-width='100'>现金账户操作金额</th>
						<th data-width='100'>操作后现金账户</th>
						<th data-width='100'>操作前赠送账户</th>
						<th data-width='100'>赠送账户操作金额</th>
						<th data-width='100'>操作后赠送账户</th>
						<th data-width='50'>操作类型</th>
						<th data-width='50'>付款方式</th>
						<th data-width='50'>操作</th>
					</tr>
				</thead>
				<!-- 同步表格 -->
				<tbody>
					<!--  PageHelper -->
					<c:forEach items="${page.list}" var="dto" >
						<tr>
							<td><fmt:formatDate value="${dto.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td>${dto.createUser }</td>
							<td>${dto.cashAmountBefore }</td>
							<td>${dto.cashAmountOper }</td>
							<td>${dto.cashAmountAfter }</td>
							<td>${dto.givedAmountBefore }</td>
							<td>${dto.givedAmountOper }</td>
							<td>${dto.givedAmountAfter }</td>
							<td>充值</td>
							<td>
								<c:choose>
								   <c:when test="${dto.payedMethod=='cash' }">现金</c:when>
								   <c:when test="${dto.payedMethod=='alipay' }">支付宝</c:when>
								   <c:when test="${dto.payedMethod=='wechat' }">微信</c:when>
								   <c:when test="${dto.payedMethod=='blank' }">银行卡</c:when>
								   <c:otherwise> 
								   </c:otherwise>
								</c:choose>
							</td>
							<td><a href="javascript:void(0)" onclick="toPrintTicket('${dto.id}')">打印小票</a></td>
						</tr>
					</c:forEach>
				</tbody>					
				<!-- 同步表格 end  -->
				<tfoot>
					<td align="right" colspan="11">		
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
</html>