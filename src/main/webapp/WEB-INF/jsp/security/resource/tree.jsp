<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
%>
<html>
<head>
    <link rel="stylesheet" href="<%=basePath%>/js/JQuery zTree v3.5.15/css/zTreeStyle/zTreeStyle.css">
</head>
<body>
<div>
	<div style="width: 30%">
		<ul id="repoTree" class="ztree"></ul>
	</div>
<!-- 	<div style="width: 70%" id="content" name="content">
	
	</div> -->
</div>
<script src="<%=basePath%>/js/jquery/jquery-1.11.0.min.js"></script>
<script src="<%=basePath%>/js/jquery/jquery.layout-latest.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/JQuery zTree v3.5.15/js/jquery.ztree.core-3.5.js"></script>
<script>
var zTreeObj;  
var setting = {  
	/**
    check : {  
        enable : true,
		chkboxType: { "Y": "", "N": "" }
    }, **/ 
    data : {  
        simpleData : {  
            enable : true  
        }  
    },
    callback : {
        onClick : function(event, treeId, treeNode) {
            parent.frames['content'].location.href = "<%=basePath%>/resource/content?parentId="+treeNode.id+"&level="+treeNode.level;
        }
    }
}; 
  
$(document).ready(function() {  
    $.ajax( {  
        url : "<%=basePath%>/resource/tree",  
        type : "get",  
        dataType : "json",  
        success : initRepoZtree,
        error:function(xhr,textStatus,error){
            console.log('错误');
            console.log(xhr);
            console.log(textStatus);
            console.log(error);
        }
    });  
});  
  
function initRepoZtree(json) {  
    var data = json.obj;  
    var zNodes = eval("(" + data + ")");  
    zTreeObj = $.fn.zTree.init($('#repoTree'), setting, zNodes);  
	zTreeObj.expandAll(true);
	
	var nodes = zTreeObj.getNodesByFilter(function(node){return node.level==0;});
	zTreeObj.selectNode(nodes[0]);
	zTreeObj.setting.callback.onClick(null, zTreeObj.setting.treeId, nodes[0]);
}      
</script>
</body>
</html>