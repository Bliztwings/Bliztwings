<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>     
<%@ include file="/WEB-INF/jsp/inc/tag_css_js.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加小E</title>

<link type="text/css" href="<%=basePath%>/zui-1.5.0/css/zui.min.css" rel="stylesheet" />
<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/jquery/jquery.js"></script> 
<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/js/zui.min.js"></script>

<!-- MD5 -->
<script type="text/javascript" src="<%=basePath%>/js/md5.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/crypto/core.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/crypto/tripledes.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/crypto/mode-ecb.js"></script>


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
    
    
    function saveSendReceivePerson(){
    	 $("#password").val(encrypt($("#password").val(),$("#mobile").val()));
    	 
    	 var params = $("#formId").serialize();
    	 jQuery.ajax({
				url : "<%=basePath%>/sendReceiver/saveSendReceivePerson",
				type : "post",
				cache : false,
				dataType : "json",
				data : params,
				success : function(data, textStatus) {
					if (data.resultCode == "0") {
						art.dialog.alert(data.resultMsg);
					} else {
						art.dialog.alert(data.resultMsg,function(){
							art.dialog.close();
						});
					}
				}
		 });
    }
   
    
    /**
	 * 密码 encrypt 加密
	 */
	function encrypt(content,srcKey){
	      var keyLen = 8;
		  var srcKeyLen = srcKey.length;
		
		 if(srcKeyLen > keyLen){
			 srcKey = srcKey.substr(0,keyLen);
			
		 }else{
			 var paddLen = keyLen -  srcKeyLen;
			 for(var i=0;i<paddLen;i++){
				 srcKey += "0";
			 }
		 }
		 var key = CryptoJS.enc.Utf8.parse(srcKey);		
		 var encryptStr =  CryptoJS.DES.encrypt(content, key, {mode: CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
		return encryptStr.toString();
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
        	<form id="formId" name="formId" class="form-horizontal" role="form" method='post' action="<%=basePath%>/sendReceiver/saveSendReceivePerson">

		   <div class="input-group" style="margin-bottom: 10px;">
		      <span class="input-group-addon" style="width:150px;text-align: right;" >小E名称：</span>
		        <input id="name" name="name" value="${e.name }" type="text" class="form-control validate[required,maxSize[16]] required" style="width: 300px;"/>
		      <span class="glyphicon glyphicon-star"></span>
		    </div>
    
    		<div class="input-group" style="margin-bottom: 10px;">
		      <span class="input-group-addon" style="width:150px;text-align: right;" >小E密码：</span>
		        <input id="password" name="password" value="${e.password }" type="password" class="form-control validate[required,maxSize[16]] required" style="width: 300px;"/>
		      <span class="glyphicon glyphicon-star"></span>
		    </div> 
		    
		    <div class="input-group" style="margin-bottom: 10px;">
		      <span class="input-group-addon" style="width:150px;text-align: right;" >小E类型：</span>
		      <select name ="userType" id="userType" class="form-control validate[required] required">
		      	<option value="1">收衣人员</option>
		      	<option value="2">送衣人员</option>
		      </select>
		    </div> 
		    
		    <div class="input-group" style="margin-bottom: 10px;">
		      <span class="input-group-addon" style="width:150px;text-align: right;" >平台类型：</span>
		      <select name ="platformType" id="platformType" class="form-control validate[required] required">
		      	<option value="1">萨维亚</option>
		      	<option value="2">浣衣坊</option>
		      </select>
		    </div> 
    		
		   <div class="input-group" style="margin-bottom: 10px;">
		      <span class="input-group-addon" style="width:150px;text-align: right;" >小E地址：</span>
		        <input id="address" name="address" value="${e.address }" type="text" class="form-control validate[required,maxSize[16]] required" style="width: 300px;"/>
		      <span class="glyphicon glyphicon-star"></span>
		    </div> 
		    
		    <div class="input-group" style="margin-bottom: 10px;">
		      <span class="input-group-addon" style="width:150px;text-align: right;" >小E电话：</span>
		        <input id="mobile" name="mobile" value="${e.mobile }" type="text" class="form-control validate[required,maxSize[16]] required" style="width: 300px;"/>
		      <span class="glyphicon glyphicon-star"></span>
		    </div> 
    
          <div class="form-group">
            <div class="col-md-offset-2 col-md-10">
               <input type='button' id='submit' onclick="saveSendReceivePerson()" class='btn btn-primary' value='保存' data-loading='稍候...' /> <input type='hidden' name='type' id='type' value='article'  />
            </div>
          </div>
        </form>
        </div>
      </div>
    </section>
  </article>
</body>
</html>