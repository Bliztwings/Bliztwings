<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>     
<%@ include file="/WEB-INF/jsp/inc/tag_css_js.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>会员卡补卡</title>

<link type="text/css" href="<%=basePath%>/zui-1.5.0/css/zui.min.css" rel="stylesheet" />
<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/jquery/jquery.js"></script> 
<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/js/zui.min.js"></script>

<!-- form验证 -->
<link rel="stylesheet" href="<%=basePath%>/js/validationEngine/css/validationEngine.jquery.css" type="text/css"/>
<!-- <script src="<%=basePath%>/js/jquery/jquery-1.7.2.min.js" type="text/javascript"></script> -->
<script src="<%=basePath%>/js/validationEngine/js/languages/jquery.validationEngine-zh_CN.js" type="text/javascript" charset="utf-8"></script>
<script src="<%=basePath%>/js/validationEngine/js/jquery.validationEngine-2.6.4.js" type="text/javascript" charset="utf-8"></script>

<!--  art.dialog  -->
<link href="<%=basePath%>/js/artDialog4.1.7/skins/blue.css" rel="stylesheet" type="text/css"/>
<script src="<%=basePath%>/js/artDialog4.1.7/artDialog.js" type="text/javascript"></script>
<script src="<%=basePath%>/js/artDialog4.1.7/plugins/iframeTools.source.js" type="text/javascript"></script>

<!--日期控件JS-->
<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/datatable/zui.datatable.js"></script>
<link type="text/css" href="<%=basePath%>/zui-1.5.0/lib/datatable/zui.datatable.css" rel="stylesheet" />
<link rel="stylesheet" href="<%=basePath%>/zui-1.5.0/lib/datetimepicker/datetimepicker.min.css">
<script src="<%=basePath%>/zui-1.5.0/lib/datetimepicker/datetimepicker.min.js"></script> 

<style>
	ul.ztree {margin-top: 10px;border: 1px solid #617775;background: #f0f6e4;width:220px;height:200px;overflow-y:scroll;overflow-x:auto;}
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


  <article>
	<!--头部展示--> 
	<section class="page-section" >
		<div class='panel' style="padding-left: 10px">
			<div class='panel-body' style="padding-left: 10px">
				<div class="input-group" style="margin-bottom: 0px;">
			      <span class="input-group-addon" style="width:100px;text-align: right;" >会员姓名：</span>
			        <input disabled="disabled" value="${member.name }" type="text" class="form-control" style="width: 200px;"/>
			      <span class="glyphicon glyphicon-star"></span>
			      
			      <span class="input-group-addon" style="width:100px;text-align: right;">手机号码：</span>
			        <input disabled="disabled" value="${member.mobilePhone }" type="text" class="form-control" style="width: 200px;"/>
			      <span class="glyphicon glyphicon-star"></span>
			    </div>
			    
			    <div class="input-group" style="margin-bottom: 0px;">
			      <span class="input-group-addon" style="width:100px;text-align: right;" >会员类型：</span>
				    <input disabled="disabled" value="<c:if test="${member.type=='normal' }">普通会员</c:if><c:if test="${member.type=='card'}">持卡会员</c:if>" type="text" class="form-control" style="width: 200px;"/>
			      	<span class="glyphicon glyphicon-star"></span>
			      	
			      	<span class="input-group-addon" style="width:100px;text-align: right;" >登记门店：</span>
				        <input value="${member.storeName }" type="text" class="form-control" style="width: 200px;" disabled="disabled"/>
				    <span class="glyphicon glyphicon-star"></span>
			    </div>
			</div>
		</div>
	</section>
	 
	 <!--操作区展示-->
    <section id='form' class="page-section" >
      	<div class='panel' style="padding-left: 10px">
        <div class='panel-body' style="padding-left: 10px">
        <form id="formId" name="formId" class="form-horizontal" role="form" method='post' action="<%=basePath%>/memberCard/doFill">
        	<input id="memberId" name="memberId" value="${member.id}" type="hidden"/>
        	<input id="id" name="id" value="${memberCard.id}" type="hidden"/>
        	<div class="input-group" style="margin-bottom: 0px;">
		      <span class="input-group-addon" style="width:100px;text-align: right;" >会员卡号：</span>
		      	<input disabled="disabled" id="cardNumber" name="cardNumber" value="${memberCard.cardNumber}" type="text" class="form-control" style="width: 200px;"/>
		      <span class="glyphicon glyphicon-star"></span>
		      <span class="input-group-addon" style="width:100px;text-align: right;">卡片状态：</span>
		      	<input disabled="disabled" id="cardStatus" name="cardStatus" value="<c:if test="${memberCard.cardStatus=='new' }">新卡</c:if><c:if test="${memberCard.cardStatus=='normal' }">正常</c:if><c:if test="${memberCard.cardStatus=='back_card' }">退卡</c:if><c:if test="${memberCard.cardStatus=='report_loss' }">挂失</c:if><c:if test="${memberCard.cardStatus=='fill_card' }">补卡</c:if><c:if test="${memberCard.cardStatus=='cancle' }">作废</c:if>" type="text" class="form-control" style="width: 200px;"/>
		      <span class="glyphicon glyphicon-star"></span>
		    </div>
		    
		    <div class="input-group" style="margin-bottom: 0px;">
		      <span class="input-group-addon" style="width:100px;text-align: right;" >现金余额：</span>
		      	<input disabled="disabled" value="${memberCard.cashAmount}" type="text" class="form-control" style="width: 200px;"/>
		      <span class="glyphicon glyphicon-star"></span>
		      <span class="input-group-addon" style="width:100px;text-align: right;">赠送余额：</span>
		      	<input disabled="disabled" value="${memberCard.givedAmount}" type="text" class="form-control" style="width: 200px;"/>
		      <span class="glyphicon glyphicon-star"></span>
		    </div>
		    <div class="input-group" style="margin-bottom: 0px;">
			    <span class="input-group-addon" style="width:100px;text-align: right;">新卡卡号：</span>
				  <select id="newCardNumber" name="newCardNumber" style="width: 200px;" class="form-control">
				      <c:forEach items="${cardNos}" var="cardNO">
				      	<option value ="${cardNO}">${cardNO}</option>
				      </c:forEach>
				  </select> 
		       <span class="glyphicon glyphicon-star"></span>
		    </div>
           <div class="form-group" >
            <div class="col-md-offset-2 col-md-10;" style="padding-top:10px;padding-left:10px;">
               <input type='submit' onclick="javascript:return confirm('补卡后原卡将作废，您确定要继续吗？');" id='submit' class='btn btn-primary' value='保存' data-loading='稍候...' /> 
            </div>
           </div>
        </form>
        </div>
      </div>
    </section>
  </article>
</body>
</html>