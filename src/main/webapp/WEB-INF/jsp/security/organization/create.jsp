<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>     
<%@ include file="/WEB-INF/jsp/inc/tag_css_js.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加机构组织</title>

	<link type="text/css" href="<%=basePath%>/zui-1.5.0/css/zui.min.css" rel="stylesheet" />
	<link type="text/css" href="<%=basePath%>/zui-1.5.0/css/zui.css" rel="stylesheet" />
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/jquery/jquery.js"></script>
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/js/zui.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/js/zui.js"></script>
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/js/messager.js"></script>

<!-- form验证 -->
<link rel="stylesheet" href="<%=basePath%>/js/validationEngine/css/validationEngine.jquery.css" type="text/css"/>
<script src="<%=basePath%>/js/jquery/jquery-1.7.2.min.js" type="text/javascript"></script>
<script src="<%=basePath%>/js/validationEngine/js/languages/jquery.validationEngine-zh_CN.js" type="text/javascript" charset="utf-8"></script>
<script src="<%=basePath%>/js/validationEngine/js/jquery.validationEngine-2.6.4.js" type="text/javascript" charset="utf-8"></script>

<!--  art.dialog  -->
<link href="<%=basePath%>/js/artDialog4.1.7/skins/blue.css" rel="stylesheet" type="text/css"/>
<script src="<%=basePath%>/js/artDialog4.1.7/artDialog.js" type="text/javascript"></script>
<script src="<%=basePath%>/js/artDialog4.1.7/plugins/iframeTools.source.js" type="text/javascript"></script>	
	

<script type="text/javascript">
    jQuery(document).ready(function(){
        jQuery("#formId").validationEngine({promptPosition: "centerRight"}); 

    });
  



</script>
	

</head>
<body>      
<c:if test="${resultCode == 1}">
   <script type="text/javascript">

   		art.dialog.alert('${resultMsg}',function(){
   			//	var msg =  $.zui.messager.show('${resultMsg}', {placement: 'top'});
   	   		// 隐藏消息
   	   	//	msg.hide();
   	   		parent.frames['tree'].location.reload();
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
        <form id="formId" name="formId" class="form-horizontal" role="form" method='post' action="<%=basePath%>/organization/create">

<%--  	<div class="input-group" style="margin-bottom: 10px;">
      <span class="input-group-addon" style="width:100px;text-align: right;" >父机构名称：</span>
        <input id="name" name="name" value="${organization.name }" type="text" class="form-control validate[required,maxSize[32]] required"  style="width: 300px;">
      <span class="glyphicon glyphicon-star"></span>
    </div>  --%>
    
    
   <div class="input-group" style="margin-bottom: 10px;">
      <span class="input-group-addon" style="width:100px;text-align: right;">父机构ID：</span>
        <input id="parentId" name="parentId" value="${child.parentId }" type="text" readonly="readonly" class="form-control validate[required,maxSize[32]] required" style="width: 300px;">
      <span class="glyphicon glyphicon-star"></span>
    </div>
 	<div class="input-group" style="margin-bottom: 10px;">
      <span class="input-group-addon" style="width:100px;text-align: right;" >名称：</span>
        <input id="name" name="name" value="${child.name }" type="text" class="form-control validate[required,maxSize[32],ajax[ajaxCheckNameForUpdate]] required"  style="width: 300px;">
      <span class="glyphicon glyphicon-star"></span>
    </div>
    
   <div class="input-group" style="margin-bottom: 10px;">
      <span class="input-group-addon" style="width:100px;text-align: right;">简称：</span>
        <input id="shortName" name="shortName" value="${child.shortName }" type="text" class="form-control validate[maxSize[32]]" style="width: 300px;">
      <span class="glyphicon glyphicon-star"></span>
    </div>
    
   <div class="input-group" style="margin-bottom: 10px;">
      <span class="input-group-addon" style="width:100px;text-align: right;">描述：</span>
        <input id="description" name="description" value="${child.description }" type="text" class="form-control validate[maxSize[64]] " style="width: 300px;">
      <span class="glyphicon glyphicon-star"></span>
    </div>
          

          <div class="form-group">
            <div class="col-md-offset-2 col-md-10">
               <input type='submit' id='submit' class='btn btn-primary' value='保存' data-loading='稍候...' />
            </div>
          </div>
        </form>
        </div>
      </div>


    </section>
  </article>
</body>
</html>