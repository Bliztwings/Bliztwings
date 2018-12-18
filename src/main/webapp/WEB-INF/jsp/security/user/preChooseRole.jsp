<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>     
<%@ include file="/WEB-INF/jsp/inc/tag_css_js.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置用户角色</title>

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
<script src="<%=basePath%>/js/jquery/jquery-1.11.0.min.js"></script>
<script src="<%=basePath%>/js/JQuery zTree v3.5.15/js/jquery.ztree.all-3.5.js"></script>
 
<style>
	ul.ztree {margin-top: 10px;border: 1px solid #617775;background: #f0f6e4;width:220px;height:200px;overflow-y:scroll;overflow-x:auto;}
</style>

<script type="text/javascript">
	// 设置用户角色
	function selectUserRole(){
		var count=0;
		var arrays ="";
		var pkids = document.getElementsByName("checkbox");
		for ( var j = 0; j < pkids.length; j++) {
			if (pkids.item(j).checked == true) {
				count+=1;
				arrays+=pkids.item(j).value+","
			}
		}
		
		if(count==0){
			art.dialog.alert('请选择一个角色!');
			return;
		}
		
		/* if(count>1){
			art.dialog.alert('只能选择一个角色');
			return;
		} */
		var userId =$("#userId").val();
		jQuery.ajax({
			url : "<%=basePath%>/user/saveUserRole",
			type : "post",
			cache : false,
			dataType : "json",
			data : {'userId':userId,'ids':arrays},
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
	
	var flag=1;
	function checkAll(){
		var pkids = document.getElementsByName("checkbox");
		if(flag==1){
			flag=2;
			for ( var j = 0; j < pkids.length; j++) {
				if (pkids.item(j).checked == false) {
					pkids.item(j).checked = true;
				}
			}
		}else{
			if(flag==2){
				flag=1;
				for ( var j = 0; j < pkids.length; j++) {
					if (pkids.item(j).checked == true) {
						pkids.item(j).checked = false;
					}
				}
			}
		}
	}
	
	// 取消，关闭窗口
	function closeWin(){
		art.dialog.close();
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
	  <section id='form' class="page-section">

	<div class='panel' style="padding-left: 10px">
			<div class='panel-body' style="padding-left: 10px">
				<form id="formId" name="formId" class="form-horizontal" role="form">
				<input type='hidden' name='userId' id='userId' value='${userId}' />
				<!-- 可用用户列表 -->
					<div class="segment">
						<div id="dtFromData"></div>
						<div style="padding-left: 0px; padding-right: 10px;">
							<table class="table datatable table-bordered  table-hover"  id="datatable">
								<thead>
									<tr>
										<td><input type="checkbox" onclick="checkAll()"></td>
										<td  align="center">序号</th>
										<td  align="center">角色名</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${roleList}" var="role" varStatus="status">
										<tr>
										 	<td><input name="checkbox" value="${role.id}" type="checkbox"></td>
											<td>${status.index+1}</td>
											<td>${role.name }</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
	
					</div>
					
					<div class="form-group">
						<div class="col-md-offset-2 col-md-10">
							<input type="button" onclick="selectUserRole()" id='submit'
								class='btn btn-primary' value='确定' data-loading='稍候...' /> <input
								type='hidden' name='type' id='type' value='article' />
								
							<input type="button" onclick="closeWin()" id='submit'
							class='btn btn-primary' value='取消' data-loading='稍候...' /> <input
							type='hidden' name='type' id='type' value='article' />
						</div>
					</div>
				</form>
			</div>
		</div>
	</section> 
  </article>
</body>
</html>