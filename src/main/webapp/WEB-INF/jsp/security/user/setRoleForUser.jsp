<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>     
<%@ include file="/WEB-INF/jsp/inc/tag_css_js.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>分配用户角色</title>

<link type="text/css" href="<%=basePath%>/zui-1.5.0/css/zui.min.css" rel="stylesheet" />
<!-- --><script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/jquery/jquery.js"></script> 
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
        var userId = $("#userId").val();
        queryUserRole(userId);
    });
    
    
    // 选择角色,弹出角色选择页面
    function chooseRole(){
    	var userId = $("#userId").val();
    	art.dialog.open( '<%=basePath%>/user/preChooseRole?userId='+userId, {
			title : '选择角色',
			width : 600,
			height : 300,
			lock : true,
			close: function () {   
				$("#tableStr").html();
				var userId = $("#userId").val();
		        queryUserRole(userId);
			}
		});
    	
    }
    
    // 删除用户的角色
    function deleteUserRole(id){
    	
    	jQuery.ajax({
			url : "<%=basePath%>/user/deleteUserRole",
			type : "post",
			cache : false,
			dataType : "json",
			data : {'id':id},
			success : function(data, textStatus) {
				if (data.resultCode == "0") {
					art.dialog.alert(data.resultMsg);
				} else {
					art.dialog.alert(data.resultMsg,function(){
						var userId = $("#userId").val();
						queryUserRole(userId);
					});					
				}
			},
		});
    }
    function queryUserRole(id){
    	jQuery.ajax({
			url : "<%=basePath%>/user/queryUserRole",
			type : "post",
			cache : false,
			dataType : "json",
			data : {'userId':id},
			success : function(data, textStatus) {
				if (data.resultCode == "0") {
					art.dialog.alert(data.resultMsg);
				} else {
					
					//动态生成表格
					$("#tableStr").html();
					var tableStr ='';
					tableStr+='<table class="table datatable table-bordered  table-hover"  id=\datatable">';
					tableStr+='<thead>';
					tableStr+='<tr>';
					tableStr+='<td  align="center">序号</td>';
					tableStr+='<td  align="center">角色名</td>';
					tableStr+='<td  align="center">操作</td>';
					tableStr+='</tr>';
					
					// 遍历内容
					if(data.userRoleList!=null){
						for(var i=0;i<data.userRoleList.length;i++){
							tableStr+='<tr>';
							tableStr+='<td>'+(i+1)+'</td>';
							tableStr+='<td>'+data.userRoleList[i].roleName+'</td>';
							tableStr+='<td>';
							tableStr+='<a style="cursor:pointer" onclick=deleteUserRole("' + data.userRoleList[i].id +  '");>删除</a>';
							tableStr+='</td>';
							tableStr+='</tr>';
						}
					}
					tableStr+='</thead>';
					tableStr+='</table>';
					$("#tableStr").html(tableStr);
				}
			},
		});
    }
    
    
    // 关闭窗口
    function closeWin(){
    	art.dialog.close();
    }
    
    // 保存用户角色
    function saveUserRole(){
    	
    	var userId = $("#userId").val();
    	var role =$("#role").val();
    	jQuery.ajax({
			url : "<%=basePath%>/user/saveUserRole",
			type : "post",
			cache : false,
			dataType : "json",
			data : {'userId':userId,'role':role},
			success : function(data, textStatus) {
				if (data.resultCode == "0") {
					art.dialog.alert(data.resultMsg);
				} else {
					art.dialog.alert(data.resultMsg,function(){
						art.dialog.top.reload();
			       		art.dialog.close();
					});
				}
			},
		});
    }
</script>
</head>
<body>      
<c:if test="${resultCode == 1}">
   <script type="text/javascript">
        art.dialog.alert('${resultMsg}',function(){
     		art.dialog.top.reload();
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
        	<form id="formId" name="formId" class="form-horizontal" role="form" method='post'>
        		<input type="hidden" value="${securityUser.id}" id="userId">
			   	<div class="input-group" style="margin-bottom: 10px;">
			      <span style="width:150px;text-align: right;" >用户名：${securityUser.username }</span>
			    </div>
			    <div class="input-group" style="margin-bottom: 10px;">
			      <span style="width:150px;text-align: right;">真实姓名：${securityUser.realname }</span>
			    </div>
			    <div class="input-group" style="margin-bottom: 10px;">
			      <span style="width:150px;text-align: right;">电话号码：${securityUser.phone }</span>
			    </div>
			    <div class="input-group" style="margin-bottom: 10px;">
			      <span style="width:150px;text-align: right;">邮箱：${securityUser.email }</span>
			    </div>
			    
			    <div class="input-group" style="margin-bottom: 10px;">
	            	<div style="padding-left: 0px; padding-right: 10px;" id ="tableStr">
			    </div>
			    <div class="form-group">
			      <div class="col-md-offset-2 col-md-10">
			         <input type="button" id='submit' onclick="chooseRole()" class='btn btn-primary' value='选择角色' data-loading='稍候...' />
			         <input type="button" onclick="closeWin()" class='btn btn-primary' value='关闭'/>
			      </div>
			    </div>
      		</form>
        </div>
      </div>
    </section>
  </article>
</body>
</html>