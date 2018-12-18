<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>     
<%@ include file="/WEB-INF/jsp/inc/tag_css_js.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>会员卡审批</title>

<link type="text/css" href="<%=basePath%>/zui-1.5.0/css/zui.min.css" rel="stylesheet" />
<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/jquery/jquery.js"></script> 
<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/js/zui.min.js"></script>

<!-- form验证 -->
<link rel="stylesheet" href="<%=basePath%>/js/validationEngine/css/validationEngine.jquery.css" type="text/css"/>
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
    
    function doPass(){
    	art.dialog.confirm('确定执行审批通过操作吗？', function () {
    		jQuery.ajax({
    			url : "<%=basePath%>/memberCardApply/approvedPass",
    			type : "post",
    			cache : false,
    			dataType : "json",
    			data : {'id':$("#id").val(),'approvedRemark':$("#approvedRemark").val()},
    			success : function(data, textStatus) {
    				if (data.resultCode == "1") {
    					art.dialog.alert(data.resultMsg,function(){
    			       		art.dialog.close();
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
    
    function doUnpass(){
    	art.dialog.confirm('确定执行审批不通过操作吗？', function () {
    		jQuery.ajax({
    			url : "<%=basePath%>/memberCardApply/approvedUnpass",
    			type : "post",
    			cache : false,
    			dataType : "json",
    			data : {'id':$("#id").val(),'approvedRemark':$("#approvedRemark").val()},
    			success : function(data, textStatus) {
    				if (data.resultCode == "1") {
    					art.dialog.alert(data.resultMsg,function(){
    			       		art.dialog.close();
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

  <article>
    <section id='form' class="page-section" >
      	<div class='panel' style="padding-left: 10px">
        <div class='panel-body' style="padding-left: 10px">
        <form id="formId" name="formId" class="form-horizontal" role="form" method='post' action="<%=basePath%>/memberCardApply/memberCardApproved">
		   	<input id="id" name="id" value="${cardApplyRecord.id}" type="hidden"/>
        	<div class="input-group" style="margin-bottom: 10px;">
		      <span class="input-group-addon" style="width:150px;text-align: right;" >申请人：</span>
		        <input id="applyer" name="applyer" value="${cardApplyRecord.applyer }" type="text" class="form-control validate[required,maxSize[16]]" style="width: 300px;" readonly="readonly"/>
		      <span class="glyphicon glyphicon-star"></span>
		    </div>
		    <div class="input-group" style="margin-bottom: 10px;">
		      <span class="input-group-addon" style="width:150px;text-align: right;" >卡供应商：</span>
		        <input id="cardSupplier" name="cardSupplier" value="${cardApplyRecord.cardSupplier }" type="text" class="form-control validate[required,maxSize[16]]" style="width: 300px;" readonly="readonly"/>
		      <span class="glyphicon glyphicon-star"></span>
		    </div>
		    <div class="input-group" style="margin-bottom: 10px;">
		      <span class="input-group-addon" style="width:150px;text-align: right;" >申请门店：</span>
		        <input id="storeName" name="storeName" value="${cardApplyRecord.storeName }" type="text" class="form-control validate[required,maxSize[16]]" style="width: 300px;" readonly="readonly"/>
		      <span class="glyphicon glyphicon-star"></span>
		    </div>
		    <div class="input-group" style="margin-bottom: 10px;">
		      <span class="input-group-addon" style="width:150px;text-align: right;" >开始卡号：</span>
		        <input id="numberBegin" name="numberBegin" value="${cardApplyRecord.numberBegin }" type="text" class="form-control validate[required,maxSize[15]]" style="width: 300px;" readonly="readonly"/>
		      <span class="glyphicon glyphicon-star"></span>
		    </div>
		    <div class="input-group" style="margin-bottom: 10px;">
		      <span class="input-group-addon" style="width:150px;text-align: right;" >结束卡号：</span>
		        <input id="numberEnd" name="numberEnd" value="${cardApplyRecord.numberEnd }" type="text" class="form-control validate[required,maxSize[15]]" style="width: 300px;" readonly="readonly"/>
		      <span class="glyphicon glyphicon-star"></span>
		    </div>
		    <div class="input-group" style="margin-bottom: 10px;">
		      <span class="input-group-addon" style="width:150px;text-align: right;" >备注：</span>
		      <textarea id="remark" name="remark" type="text" class="form-control" placeholder="备注说明" style="width: 300px;" readonly="readonly">${cardApplyRecord.remark}</textarea>
		      <span class="glyphicon glyphicon-star"></span>
		    </div>
		    
		    <div class="input-group" style="margin-bottom: 10px;">
		      <span class="input-group-addon" style="width:150px;text-align: right;" >审批意见：</span>
		      <textarea id="approvedRemark" name="approvedRemark" type="text" class="form-control" placeholder="审批意见" style="width: 300px;">${cardApplyRecord.approvedRemark}</textarea>
		      <span class="glyphicon glyphicon-star"></span>
		    </div>
		    
          <div class="form-group">
            <div class="col-md-offset-2 col-md-10">
               <input type='button' id='pass' class='btn btn-primary' value='审批通过' data-loading='稍候...' onclick="doPass()"/> &nbsp;&nbsp;<input type='button' id='unpass' class='btn btn-primary' value='审批不通过' data-loading='稍候...' onclick="doUnpass()"/> 
            </div>
          </div>
        </form>
        </div>
      </div>
    </section>
  </article>
</body>
</html>