<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>     
<%@ include file="/WEB-INF/jsp/inc/tag_css_js.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加</title>

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
    
    $.fn.selectReadOnly=function(){
        var tem=$(this).children('option').index($("option:selected"));
        $(this).change(function(){
              $(this).children('option').eq(tem).attr("selected",true); 
        });
    }
    
    $.validationEngineLanguage.allRules.onlyNumber = {  
         "regex": /^\d{0,8}(\.\d{0,2})?$/,  
         "alertText": "金额必须为数字"  
    };  
    
    $("select:[readonly='readonly']").selectReadOnly();
    
    
 // 保存数据
    function updateData(){
    	
    	var dataType =$("#dataTypeSelect").val();
    	if(dataType=='1'){
    		
    		var productType =$("#productType").val();
    		var productChildrenType =$("#productChildrenType").val();
    		var price =$("#price").val();
    	    var commonPrice =$("#commonPrice").val();
    	    var luxuriesPrice =$("#luxuriesPrice").val();
    		/* if(productType==null || productType =='0'){
    			art.dialog.alert("请选择父级");
        		return;
    		} */
    		
    	    if(productType!=null && productType !=''){
    			var imageName =$("#imageName").val();
    			if(imageName==null || imageName ==''){
        			art.dialog.alert("图片不能为空");
            		return;
        		}
    		} 
    		
    		if(price==null || price =='0'){
    			art.dialog.alert("单烫价格不能为空");
        		return;
    		}
    		
    		if(commonPrice==null || commonPrice =='0'){
    			art.dialog.alert("普洗价格不能为空");
        		return;
    		}
    		
    		if(luxuriesPrice==null || luxuriesPrice =='0'){
    			art.dialog.alert("尚品价格不能为空");
        		return;
    		}
    		
    	}
    	
    	var dataName =$("#dataName").val();
    	var dataTypeSelect =$("#dataTypeSelect").val();
    	
    	if(dataName==null || dataName ==''){
			art.dialog.alert("属性名称不能为空");
    		return;
		}
    	
    	if(dataTypeSelect==null || dataTypeSelect ==''){
			art.dialog.alert("属性类型不能为空");
    		return;
		}
    	
    	var params = $("#formId").serialize();
   		jQuery.ajax({
   			url : "<%=basePath%>/data/updateData",
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
    	
    	var produceParentCategoryId =$("#produceParentCategoryId").val();
    	
    	jQuery.ajax({
			url : "<%=basePath%>/data/getChildren",
			type : "post",
			cache : false,
			dataType : "json",
			data : {'produceParentCategoryId':produceParentCategoryId},
			success : function(data, textStatus) {
				if (data.resultCode == "0") {
					art.dialog.alert(data.resultMsg);
				} else {
					
					if(data.list!=null){
						// 动态生成html
						$("#produceCategoryId").html();
						var str ="";
						str+='<select id="produceCategoryId" class="form-control validate[required,maxSize[16]] required" style="width: 80px;">';
							for(var i =0;i<data.list.length;i++){
								str+='<option value='+data.list[i].id+'>'+data.list[i].name+'</option>';
							}
						str+="</select>";
						$("#produceCategoryId").html(str);
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
   	art.dialog.alert('${resultMsg}');
	</script>
</c:if>

  <article>
    <section id='form' class="page-section" >
      	<div class='panel' style="padding-left: 10px">
      	
        	<form id="formId" name="formId" class="form-horizontal" role="form" method='POST' action="<%=basePath%>/data/updateData">
		   	<input type="hidden" id="dataType" name="dataType" value="${dictionary.dataType}">
      		<input type="hidden" id="dictionaryId" name="id" value="${dictionary.id}">
		   	
		   	<c:if test="${dictionary.dataType=='1'}">
		   		<div class="input-group" style="margin-bottom: 10px;">
				      <span class="input-group-addon" style="width:150px;text-align: right;" >产品类型：</span>
				       
				       <select id="produceParentCategoryId" onchange="chooseType()" name="produceParentCategoryId" class="form-control validate[required] required" style="width: 100px;">
			                <c:forEach items="${productTypeList}" var="dto" varStatus="status">
			                <option  value="${dto.id}" <c:if test="${dictionary.produceParentCategoryId==dto.id }">selected</c:if>>${dto.name}</option>
			                </c:forEach>
			            </select>
			          
				      <span class="glyphicon glyphicon-star"></span>
		    	</div>
		    	
		    	<div class="input-group" style="margin-bottom: 10px;">
				      <span class="input-group-addon" style="width:150px;text-align: right;" >产品图片：</span>
				      <input type="file" name="file1">
				      <span class="glyphicon glyphicon-star"></span>
		    	</div>
		    
		    	<div class="input-group" style="margin-bottom: 10px;">
				      <span class="input-group-addon" style="width:150px;text-align: right;" >产品二级分类：</span>
				       
				       <select id="produceCategoryId" name="produceCategoryId" class="form-control validate[required] required" style="width: 100px;">
			                <c:forEach items="${productChildrenList}" var="dto" varStatus="status">
			                <option <c:if test="${dictionary.produceCategoryId==dto.id}">selected</c:if> value="${dto.id}">${dto.name}</option>
			                </c:forEach>
			            </select>
			            
				      <span class="glyphicon glyphicon-star"></span>
		    	</div>
		    	
		    	
		   	</c:if>
		   	
		   	<div class="input-group" style="margin-bottom: 10px;">
		      <span class="input-group-addon" style="width:150px;text-align: right;" >属性名称：</span>
		        <input id="name" name="dataName" value="${dictionary.dataName }" type="text" class="form-control validate[required,maxSize[16]] required" style="width: 300px;"/>
		      <span class="glyphicon glyphicon-star"></span>
		    </div>
		    
		    <c:if test="${dictionary.dataType=='1'}">
		    	<div class="input-group" style="margin-bottom: 10px;">
				      <span class="input-group-addon" style="width:150px;text-align: right;" >单烫价格：</span>
				        <input id="price" name="price" value="${dictionary.price }" type="text" class="form-control validate[required,custom[onlyNumber]]" style="width: 300px;"/>
				      <span class="glyphicon glyphicon-star"></span>
		   		 </div>	
		   		 <div class="input-group" style="margin-bottom: 10px;">
				      <span class="input-group-addon" style="width:150px;text-align: right;" >普洗价格：</span>
				        <input id="commonPrice" name="commonPrice" value="${dictionary.commonPrice }" type="text" class="form-control validate[required,custom[onlyNumber]]" style="width: 300px;"/>
				      <span class="glyphicon glyphicon-star"></span>
		   		 </div>	
		   		 <div class="input-group" style="margin-bottom: 10px;">
				      <span class="input-group-addon" style="width:150px;text-align: right;" >尚品洗涤：</span>
				        <input id="luxuriesPrice" name="luxuriesPrice" value="${dictionary.luxuriesPrice }" type="text" class="form-control validate[required,custom[onlyNumber]]" style="width: 300px;"/>
				      <span class="glyphicon glyphicon-star"></span>
		   		 </div>	
		   		 
		   		 <div class="input-group" style="margin-bottom: 10px;">
				      <span class="input-group-addon" style="width:150px;text-align: right;" >O2O价格：</span>
				        <input id="o2oPrice" name="o2oPrice" value="${dictionary.o2oPrice }" type="text" class="form-control validate[required,custom[onlyNumber]]" style="width: 300px;"/>
				      <span class="glyphicon glyphicon-star"></span>
		   		 </div>	
		   		 
		    </c:if>
		    
		    <div class="input-group" style="margin-bottom: 10px;">
		      <span class="input-group-addon" style="width:150px;text-align: right;" >属性类型：</span>
		       <select id="dataTypeSelect" disabled="disabled" name="dataType" class="form-control validate[required,maxSize[16]] required" style="width: 300px;">
		       		<option value="1" <c:if test="${dictionary.dataType=='1'}">selected</c:if>>衣物名称</option>
		       		<option value="2" <c:if test="${dictionary.dataType=='2'}">selected</c:if>>颜色</option>
		       		<option value="3" <c:if test="${dictionary.dataType=='3'}">selected</c:if>>品牌</option>
		       		<option value="4" <c:if test="${dictionary.dataType=='4'}">selected</c:if>>瑕疵</option>
		       		<option value="5" <c:if test="${dictionary.dataType=='5'}">selected</c:if>>附件</option>
		       </select>
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