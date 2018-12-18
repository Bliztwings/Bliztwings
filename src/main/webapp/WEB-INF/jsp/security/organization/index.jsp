<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
%>
<html>
<head>
   <%--  <link rel="stylesheet" href="<%=basePath%>/common/layout-default-latest.css"> --%>
   
<!--  art.dialog  -->
<link href="<%=basePath%>/js/artDialog4.1.7/skins/blue.css"
		rel="stylesheet" type="text/css" />
<script src="<%=basePath%>/js/artDialog4.1.7/artDialog.js"
		type="text/javascript"></script>
<script src="<%=basePath%>/js/artDialog4.1.7/plugins/iframeTools.source.js"
		type="text/javascript"></script>
		   
</head>
<body>


<iframe name="tree" class="ui-layout-west"
        src="<%=basePath%>/organization/tree" frameborder="0" scrolling="auto"></iframe>
 
<iframe name="content" class="ui-layout-center"
        src="" frameborder="0" scrolling="auto"></iframe>
        
<!-- <div name="content" class="ui-layout-center"  scrolling="auto">

</div>  --> 

<!-- <div name="tree" class="ui-layout-west"
         frameborder="0" scrolling="auto">

</div>   -->      

<script src="<%=basePath%>/js/jquery/jquery-1.11.0.min.js"></script>
<script src="<%=basePath%>/js/jquery/jquery.layout-latest.min.js"></script>
<script>
    $(function () {
        $(document).ready(function () {
            $('body').layout({ applyDemoStyles: true });
        });
    });
</script>
</body>
</html>