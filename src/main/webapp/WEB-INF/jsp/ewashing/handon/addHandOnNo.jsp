<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>     
<%@ include file="/WEB-INF/jsp/inc/tag_css_js.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加挂衣区</title>

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
 
<style>
	ul.ztree {margin-top: 10px;border: 1px solid #617775;background: #f0f6e4;width:220px;height:200px;overflow-y:scroll;overflow-x:auto;}
</style>

<script type="text/javascript">
    jQuery(document).ready(function(){
        jQuery("#formId").validationEngine('attach',{promptPosition: "centerRight"}); 

    });
    
    // 添加挂衣号
    function saveHandOnNo(){
    	var handType =$("#handType").val();
    	var handonArea =$("#handonArea").val();
    	var handOnNoBegin =$("#handOnNoBegin").val();
    	var handOnNoEnd =$("#handOnNoEnd").val();
    	if(handType ==null || handType =='' || handType=='0'){
    		art.dialog.alert("请选择类型");
    		return;
    	}
    	
    	if(handonArea ==null || handonArea ==''|| handonArea=='0' ){
    		art.dialog.alert("请选择挂衣区");
    		return;
    	}
    	if(handOnNoBegin ==null || handOnNoBegin =='' || handOnNoBegin=='0'){
    		art.dialog.alert("挂衣号开始值不能为空");
    		return;
    	}
    	if(handOnNoEnd ==null || handOnNoEnd =='' || handOnNoEnd=='0'){
    		art.dialog.alert("挂衣号结束值不能为空");
    		return;
    	}
    	
    	var params = $("#formId").serialize();
   		jQuery.ajax({
   			url : "<%=basePath%>/handon/saveHandOnNo",
   			type : "post",
   			cache : false,
   			dataType : "json",
   			data : params,
   			success : function(data, textStatus) {
   				if (data.resultCode == "0") {
   					art.dialog.alert(data.resultMsg);
   				} else {
   					art.dialog.close();
   				}
   			}
   		});
    }
    
    function chooseType(){
    	
    	$("#handOnNoBegin").val("");
    	$("#handOnNoEnd").val("");
    	
   	    //  如果选择是工厂，则加载工厂对应的挂衣区,如果是门店，直接加载当前门店的挂衣区
    	var handType =$("#handType").val();
   	    if(handType=='0'){
   	    	$("#store").hide();
   	    }
    	if(handType=='1'){
    		$("#store").show();
    		var storeId =$("#storeId_default").val();
    		jQuery.ajax({
    			url : "<%=basePath%>/storeBusiness/queryStoreByHandType",
    			type : "post",
    			cache : false,
    			dataType : "json",
    			data : {'handType':handType,'storeId':storeId},
    			success : function(data, textStatus) {
    				if (data.resultCode == "0") {
    					art.dialog.alert(data.resultMsg);
    				} else {
    					
    					if(data.list!=null){
    						// 动态生成html
    						$("#storeId").html();
    						var str ="";
    						str+='<select id="storeId" name ="storeId" class="form-control validate[required,maxSize[16]] required" style="width: 80px;">';
    						    str+='<option value=0>请选择</option>';
    							for(var i =0;i<data.list.length;i++){
    								str+='<option value='+data.list[i].id+'>'+data.list[i].storeName+'</option>';
    							}
    						str+="</select>";
    						$("#storeId").html(str);
    					}
    				}
    			}
    		});
    		
    		jQuery.ajax({
    			url : "<%=basePath%>/storeBusiness/queryHandOnAreaByHandType",
    			type : "post",
    			cache : false,
    			dataType : "json",
    			data : {'handType':handType,'storeId':storeId},
    			success : function(data, textStatus) {
    				if (data.resultCode == "0") {
    					art.dialog.alert(data.resultMsg);
    				} else {
    					
    					if(data.list!=null){
    						// 动态生成html
    						$("#handonArea").html();
    						var str ="";
    						str+='<select id="handonArea" name ="handonArea" class="form-control validate[required,maxSize[16]] required" style="width: 80px;">';
    							str+='<option value=0>请选择</option>';
    							for(var i =0;i<data.list.length;i++){
    								str+='<option value='+data.list[i].id+'>'+data.list[i].handOnArea+'</option>';
    							}
    						str+="</select>";
    						$("#handonArea").html(str);
    					}
    				}
    			}
    		});
    		
    	}
    	
    	if(handType=='2' || handType =='3' || handType =='4'){
    		$("#store").hide();
    		jQuery.ajax({
    			url : "<%=basePath%>/storeBusiness/queryHandOnAreaByHandType",
    			type : "post",
    			cache : false,
    			dataType : "json",
    			data : {'handType':handType},
    			success : function(data, textStatus) {
    				if (data.resultCode == "0") {
    					art.dialog.alert(data.resultMsg);
    				} else {
    					
    					if(data.list!=null){
    						// 动态生成html
    						$("#handonArea").html();
    						var str ="";
    						str+='<select id="handonArea" name ="handonArea" class="form-control validate[required,maxSize[16]] required" style="width: 80px;">';
    							str+='<option value=0>请选择</option>';
    							for(var i =0;i<data.list.length;i++){
    								str+='<option value='+data.list[i].id+'>'+data.list[i].handOnArea+'</option>';
    							}
    						str+="</select>";
    						$("#handonArea").html(str);
    					}
    				}
    			}
    		});
    	}
    }
    
	function chooseStoreType(){
    	
    	var handType =$("#handType").val();
    	// 获取门店对应的挂衣区
    	var storeId =$("#storeId").val();
		jQuery.ajax({
			url : "<%=basePath%>/storeBusiness/queryStoreHandOnArea",
			type : "post",
			cache : false,
			dataType : "json",
			data : {'storeId':storeId,'handType':handType},
			success : function(data, textStatus) {
				if (data.resultCode == "0") {
					art.dialog.alert(data.resultMsg);
				} else {
					
					if(data.list!=null){
						// 动态生成html
						$("#handonArea").html();
						var str ="";
						str+='<select id="handonArea" name ="handonArea" class="form-control validate[required,maxSize[16]] required" style="width: 80px;">';
						str+='<option value=0>请选择</option>';	
						for(var i =0;i<data.list.length;i++){
								str+='<option value='+data.list[i].id+'>'+data.list[i].handOnArea+'</option>';
							}
						str+="</select>";
						$("#handonArea").html(str);
					}
				}
			}
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
   art.dialog.alert('${resultMsg}',function(){
 		art.dialog.close();
	});
	</script>
</c:if>


  <article>
    <section id='form' class="page-section" >
      	<div class='panel' style="padding-left: 10px">
        	<div class='panel-body' style="padding-left: 10px">
        	<form id="formId" name="formId" class="form-horizontal" role="form" method='post' action="<%=basePath%>/handon/saveHandOnNo">
			<input type="hidden" id="storeId_default" value="${storeId}">
			<div class="input-group" style="margin-bottom: 10px;">
	       		<span class="input-group-addon" style="width:150px;text-align: right;" >类型：</span>
	       		<select id="handType" name="handType" onchange="chooseType()" class="form-control validate[required] required" style="width: 300px;">
                	<c:if test="${userType=='1'}">
                		<option value="0" selected="selected">请选择</option>
                		<option value="1">门店</option>
                	</c:if>
                	<c:if test="${userType=='2'}">
                		<option value="0" selected="selected">请选择</option>
                		<option value="2">工厂</option>
                	</c:if>
                	<c:if test="${userType=='' || userType ==null}">
                		<option value="0" selected="selected">请选择</option>
                		<option value="1">门店</option>
                		<option value="2">工厂</option>
                		<option value="3">萨维亚</option>
                		<option value="4">浣衣坊</option>
                	</c:if>
           		</select>
	    	</div>
			
			
			<!-- 管理员才需要选择门店 -->
	    	<c:if test="${userType==null || userType==''}">
				<div class="input-group" style="margin-bottom: 10px;display: none" id="store">
	       			<span class="input-group-addon" style="width:150px;text-align: right;" >所属门店</span>
    			     <select id="storeId" name="storeId" onchange="chooseStoreType()" class="form-control validate[required] required" style="width: 300px;">
							<option value="0">请选择</option>
			                <c:forEach items="${storeList}" var="dto" varStatus="status">
			                    <option value="${dto.id}">${dto.storeName}</option>
			                </c:forEach>
            		</select>
	    		</div>   	
	    	</c:if>
	    	
		   <div class="input-group" style="margin-bottom: 10px;">
		      <span class="input-group-addon" style="width:150px;text-align: right;" >挂衣区：</span>
		       <select id="handonArea" name="handonArea" class="form-control validate[required] required" style="width: 300px;">
	                <option value="0">请选择挂衣区</option>
	                <c:forEach items="${handonAreaList}" var="dto" varStatus="status">
	                <option value="${dto.id}">${dto.handOnArea}</option>
	                </c:forEach>
	            </select>
		      <span class="glyphicon glyphicon-star"></span>
		    </div>
		    
		    <div class="input-group" style="margin-bottom: 10px;">
		        <span class="input-group-addon" style="width:150px;text-align: right;" >挂衣号：</span>
	        		<input id="handOnNoBegin" placeholder="开始值" name="handOnNoBegin" value="${handon.handOnNo }" type="text" style="width: 100px;" class="form-control validate[required,maxSize[16]] required"/>
     		    	<input id="handOnNoEnd" placeholder="结束值" name="handOnNoEnd" value="${handon.handOnNo }" type="text" style="width: 100px;" class="form-control validate[required,maxSize[16]] required"/>
		    </div>
    
          <div class="form-group">
            <div class="col-md-offset-2 col-md-10">
               <input type='button' id='submit' class='btn btn-primary' onclick="saveHandOnNo()" value='保存' data-loading='稍候...' /> <input type='hidden' name='type' id='type' value='article'  />
            </div>
          </div>
        </form>
        </div>
      </div>
    </section>
  </article>
</body>
</html>