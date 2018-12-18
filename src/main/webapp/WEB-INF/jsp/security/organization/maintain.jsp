<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ page isELIgnored="false"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path;
%>
<html>
<head>
<title></title>
<link type="text/css" href="<%=basePath%>/zui-1.5.0/css/zui.min.css"
	rel="stylesheet" />
<script type="text/javascript"
	src="<%=basePath%>/zui-1.5.0/lib/jquery/jquery.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/zui-1.5.0/js/zui.min.js"></script>

<!-- form验证 -->
<link rel="stylesheet"
	href="<%=basePath%>/js/validationEngine/css/validationEngine.jquery.css"
	type="text/css" />
<script src="<%=basePath%>/js/jquery/jquery-1.7.2.min.js"
	type="text/javascript"></script>
<script
	src="<%=basePath%>/js/validationEngine/js/languages/jquery.validationEngine-zh_CN.js"
	type="text/javascript" charset="utf-8"></script>
<script
	src="<%=basePath%>/js/validationEngine/js/jquery.validationEngine-2.6.4.js"
	type="text/javascript" charset="utf-8"></script>



<!--  art.dialog  -->
<link href="<%=basePath%>/js/artDialog4.1.7/skins/blue.css"
	rel="stylesheet" type="text/css" />
<script src="<%=basePath%>/js/artDialog4.1.7/artDialog.js"
	type="text/javascript"></script>
<script
	src="<%=basePath%>/js/artDialog4.1.7/plugins/iframeTools.source.js"
	type="text/javascript"></script>

<script type="text/javascript">
    jQuery(document).ready(function(){
        jQuery("#formId").validationEngine({promptPosition: "centerRight"}); 

    });


</script>
</head>
<body>

	<%--     <form:form id="form" method="post" commandName="organization">
        <form:hidden path="id"/>
        <form:hidden path="parentId"/>

        <div class="form-group"> 
            <form:label path="name">名称：</form:label>
            <form:input path="name"/>
        </div>
        <shiro:hasPermission name="organization:update">
            <form:button id="updateBtn">修改</form:button>
        </shiro:hasPermission>

        <shiro:hasPermission name="organization:delete">
            <c:if test="${not organization.rootNode}">
            <form:button id="deleteBtn">删除</form:button>
            </c:if>
        </shiro:hasPermission>

        <shiro:hasPermission name="organization:create">
            <form:button id="appendChildBtn">添加子节点</form:button>
        </shiro:hasPermission>

        <shiro:hasPermission name="organization:update">
            <c:if test="${not organization.rootNode}">
            <form:button id="moveBtn">移动节点</form:button>
            </c:if>
        </shiro:hasPermission>
    </form:form> --%>

	<article>
		<section id='form' class="page-section">
			<div class='panel' style="padding-left: 10px; height: 100%">
				<div class='panel-body' style="padding-left: 10px">
					<form id="formId" name="formId" class="form-horizontal"
						method='post'>

						<input id="id" name="id" type="hidden" value="${organization.id }">
						<input id="parentId" name="parentId" type="hidden"  value="${organization.parentId }">
						<input id="oldName" name="oldName" value="${organization.name }" type="hidden">

						<div class="input-group" style="margin-bottom: 10px;">
							<span class="input-group-addon"
								style="width: 100px; text-align: right;">名称：</span> <input
								id="name" name="name" value="${organization.name }" type="text"
								class="form-control validate[required,maxSize[32]] required"
								style="width: 300px;" onchange="checkOrgName();" > <span
								class="glyphicon glyphicon-star"></span>
						</div>

						<div class="input-group" style="margin-bottom: 10px;">
							<span class="input-group-addon"
								style="width: 100px; text-align: right;">简称：</span> <input
								id="shortName" name="shortName"
								value="${organization.shortName }" type="text"
								class="form-control validate[maxSize[32]]"
								style="width: 300px;"> <span
								class="glyphicon glyphicon-star"></span>
						</div>

						<div class="input-group" style="margin-bottom: 10px;">
							<span class="input-group-addon"
								style="width: 100px; text-align: right;">描述：</span> <input
								id="description" name="description"
								value="${organization.description }" type="text"
								class="form-control validate[maxSize[64]] "
								style="width: 300px;"> <span
								class="glyphicon glyphicon-star"></span>
						</div>

						<div class="form-group"
							style="padding-left: 10px; padding-top: 10px;">
							<shiro:hasPermission name="organization:create">		
							<button class="btn btn-primary" type="button" id='add'
								onclick="addOrg()" style="margin-left: 5px;">添 加</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="organization:update">
							<button class="btn btn-primary" type="button" id='update'
								value='修改' onclick="updateOrg()" data-loading='稍候...'
								style="margin-left: 15px;">修 改</button>
							</shiro:hasPermission>
							<c:if test="${organization.parentId!=0}">
							<shiro:hasPermission name="organization:delete">
							<button class="btn btn-primary" type="button" id='delete'
								value='删除' onclick="deleteOrg()" data-loading='稍候...'
								style="margin-left: 15px;">删 除</button>
							</shiro:hasPermission>
							</c:if>
							<shiro:hasPermission name="organization:fuquan">
							<button class="btn btn-primary" type="button" id='auth'
								style="margin-left: 15px;" onclick="setPrivilege();">赋 权</button>
							</shiro:hasPermission>

						</div>
					</form>
				</div>
			</div>


		</section>
	</article>



	<script type="text/javascript">
        /* $(function() {
            $("#updateBtn").click(function() {
                $("#form")
                        .attr("action", "${pageContext.request.contextPath}/organization/${organization.id}/update")
                        .submit();
                return false;
            });
            $("#deleteBtn").click(function() {
                if(confirm("确认删除吗？")) {
                    $("#form")
                            .attr("action", "${pageContext.request.contextPath}/organization/${organization.id}/delete")
                            .submit();
                }
                return false;
            });

            $("#appendChildBtn").click(function() {
                location.href="${pageContext.request.contextPath}/organization/${organization.id}/appendChild";
                return false;
            });

            $("#moveBtn").click(function() {
                location.href="${pageContext.request.contextPath}/organization/${organization.id}/move";
                return false;
            });
        }); */
 
// 添加机构组织       
function addOrg(){
       	parent.art.dialog.open( '<%=basePath%>/organization/${organization.id }/preCreate', {
		title : '添加机构组织',
		width : 600,
		height : 400,
		lock : true
	});
}
        
    	function checkOrgName(){
    		var oldName = $("#oldName").val();
    		var name = $.trim($("#name").val());
    		if(null != name  ){
    			if(oldName !=name ){
    				var classValue =  $("#name").attr("class");   			
    				//$("#name").attr("class","form-control validate[required,maxSize[32],ajax[ajaxCheckNameForUpdate]] required");
    			}else {
    				//$("#name").attr("class","form-control validate[required,maxSize[32]] required");
    			}
    			
    		}else {

    		}
    		
    		
    	}   

function updateOrg(){
	var params = $("#formId").serialize();
//	if( $("#formId").validationEngine("validate") ){	
	jQuery.ajax({
        url:"<%=basePath%>/organization/update",
        type:"POST",
        cache:false,
        async:true,
        dataType: "json",
        data: params,
        success:function(data, textStatus, jqXHR) {
        	 if(data){
            	  if(data.resultCode == 1){
                   	    art.dialog.alert(data.resultMsg,function(){
                   	   		parent.frames['tree'].location.reload();
	                   		art.dialog.close();
	                   	    location.href = "<%=basePath%>/organization/${organization.id }/maintain";
                 	   });
            	  }else {
						art.dialog.alert(data.resultMsg);
      		            return false;
            	  }
        	 }
        },
        error:function(XMLHttpRequest, textStatus, errorThrown) {		        	
        	art.dialog.alert("修改机构组织信息出现异常，请稍后再试！");
    		return false;
        }
    });
//	}
	
}

function deleteOrg (){
	
	jQuery.ajax({
        url:"<%=basePath%>/organization/delete",
        type:"POST",
        cache:false,
        async:true,
        dataType: "json",
        data: {"id": "${organization.id }"},
        success:function(data, textStatus, jqXHR) {
        	 if(data){
            	  if(data.resultCode == 1){
                   	    art.dialog.alert(data.resultMsg,function(){
                   	   		parent.frames['tree'].location.reload();
	                   		art.dialog.close();
	                   		location.href = "<%=basePath%>/organization/${organization.id }/maintain";
                 	   });
            	  }else {
						art.dialog.alert(data.resultMsg);
      		            return false;
            	  }
        	 }
        },
        error:function(XMLHttpRequest, textStatus, errorThrown) {		        	
        	art.dialog.alert("删除机构组织信息出现异常，请稍后再试！");
    		return false;
        }
    });
}

function setPrivilege (){
   	parent.art.dialog.open( '<%=basePath%>/organization/${organization.id }/setPrivilege',
							{
								title : '机构组织赋权限',
								width : 750,
								height : 560,
								lock : true
							});

		}
	</script>
</body>
</html>