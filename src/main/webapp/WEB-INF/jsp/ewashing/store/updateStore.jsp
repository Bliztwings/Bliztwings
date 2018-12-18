<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>     
<%@ include file="/WEB-INF/jsp/inc/tag_css_js.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加门店</title>

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
	
<!--  Ztree  -->
<link rel="stylesheet" href="<%=basePath%>/js/JQuery zTree v3.5.15/css/css.css">
<link rel="stylesheet" href="<%=basePath%>/js/JQuery zTree v3.5.15/css/zTreeStyle/zTreeStyle.css">
<!--  <script src="<%=basePath%>/js/jquery/jquery-1.11.0.min.js"></script>   -->
<script src="<%=basePath%>/js/JQuery zTree v3.5.15/js/jquery.ztree.all-3.5.js"></script>
 
<style>
	ul.ztree {margin-top: 10px;border: 1px solid #617775;background: #f0f6e4;width:220px;height:200px;overflow-y:scroll;overflow-x:auto;}
</style>

<script type="text/javascript">
    jQuery(document).ready(function(){
        jQuery("#formId").validationEngine('attach',{promptPosition: "centerRight"}); 

    });
    
    
</script>
</head>
  <script>
        
  </script>
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
        	<form id="formId" name="formId" class="form-horizontal" role="form" method='post' action="<%=basePath%>/store/update">
			<input type='hidden' name='id' id='id' value='${store.id}' />
			<input type='hidden' name='storeCode' id='storeCode' value='${store.storeCode}' />
		   <div class="input-group" style="margin-bottom: 10px;">
		      <span class="input-group-addon" style="width:150px;text-align: right;" >门店名称：</span>
		        <input id="storeName" name="storeName" value="${store.storeName }" type="text" class="form-control validate[required,maxSize[16]] required" style="width: 300px;"/>
		      <span class="glyphicon glyphicon-star"></span>
		    </div>
    
		   <div class="input-group" style="margin-bottom: 10px;">
		      <span class="input-group-addon" style="width:150px;text-align: right;" >门店地址：</span>
		        <input id="storeAddress" name="storeAddress" value="${store.storeAddress }" type="text" class="form-control validate[required,maxSize[16]] required" style="width: 300px;"/>
		      <span class="glyphicon glyphicon-star"></span>
		    </div> 
		    
		    <div class="input-group" style="margin-bottom: 10px;">
		      <span class="input-group-addon" style="width:150px;text-align: right;" >门店电话：</span>
		        <input id="telephone" name="telephone" value="${store.telephone }" type="text" class="form-control validate[required,maxSize[16]] required" style="width: 300px;"/>
		      <span class="glyphicon glyphicon-star"></span>
		    </div> 
    
          <div class="form-group">
            <div class="col-md-offset-2 col-md-10">
               <input type='submit' id='submit' class='btn btn-primary' value='修改' data-loading='稍候...' /> <input type='hidden' name='type' id='type' value='article'  />
            </div>
          </div>
        </form>
        </div>
      </div>
    </section>
  </article>
</body>
</html>