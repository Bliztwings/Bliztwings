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
<link type="text/css" href="<%=basePath%>/zui-1.5.0/css/zui.min.css" rel="stylesheet" />
<link href="<%=basePath%>/js/artDialog4.1.7/skins/blue.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" href="<%=basePath%>/js/JQuery zTree v3.5.15/css/zTreeStyle/zTreeStyle.css">
<script src="<%=basePath%>/js/jquery/jquery-1.11.0.min.js"></script>
<script src="<%=basePath%>/js/jquery/jquery.layout-latest.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/JQuery zTree v3.5.15/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/JQuery zTree v3.5.15/js/jquery.ztree.excheck-3.5.js"></script>
<!--  art.dialog  -->
<link href="<%=basePath%>/js/artDialog4.1.7/skins/blue.css" rel="stylesheet" type="text/css"/>
<script src="<%=basePath%>/js/artDialog4.1.7/artDialog.js" type="text/javascript"></script>
<script src="<%=basePath%>/js/artDialog4.1.7/plugins/iframeTools.source.js" type="text/javascript"></script>	
<style>
	ul.ztree {margin-top: 5px;border: 1px solid #617775;background: #f0f6e4;width:330px;height:330px;overflow-y:scroll;overflow-x:auto;}
</style>
<script>
var zTreeObj;  
var setting = {  
    check : {  
        enable : true,
		chkboxType: { "Y": "ps", "N": "ps" },
    },
    data : {  
        simpleData : {  
            enable : true  
        }  
    },
}; 
  
$(document).ready(function() {  
	var roleId = '${roleId}';
    $.ajax( {  
        url : "<%=basePath%>/role/tree",  
        type : "get",  
        dataType : "json",  
        data : {'roleId':roleId},
        success : initRepoZtree,
    });  
});  
  
function initRepoZtree(json) {  
    var data = json.obj;  
    var zNodes = eval("(" + data + ")");  
    zTreeObj = $.fn.zTree.init($('#repoTree'), setting, zNodes);  
	zTreeObj.expandAll(true);
}      

$(function(){
	$("#saveBtn").click(function(){
		var nodes = new Array();
	    //取得选中的结点
	    nodes = zTreeObj.getCheckedNodes(true);
	    var permissions = "";
	    for (i = 0; i < nodes.length; i++) {
	        if (permissions != "") {
	        	permissions += ",";
	        }
	        permissions += nodes[i].id;
	    }
	    var roleId = '${roleId}';
	    jQuery.ajax({
			url : "<%=basePath%>/role/savePermission",
			type : "post",
			cache : false,
			dataType : "json",
			data : {'permissions':permissions,'roleId':roleId},
			success : function(data, textStatus) {
				if (data.resultCode == "0") {
					art.dialog.alert(data.resultMsg,function(){
						art.dialog.close();
					});
				} else {
					art.dialog.alert(data.resultMsg,function(){
						art.dialog.close();
					});
				}
			},
		});
	});
});
</script>
</head>
<body>
  <article>
    <section id='form' class="page-section" >
      <div class='panel' style="padding-left: 10px">
        <div class='panel-body' style="padding: 10px">
        <form id="formId" name="formId" class="form-horizontal" role="form" method='post' action="">
   		<div class="input-group" style="margin-bottom: 10px;">
				<div>
					<ul id="repoTree" class="ztree"></ul>
				</div>
	    </div>
          <div class="form-group">
            <div class="col-md-offset-2 col-md-10">
               <input type='button' id='saveBtn' class='btn btn-primary' value='保存' data-loading='稍候...' /> 
            </div>
          </div>
        </form>
        </div>
      </div>
    </section>
  </article>
</body>
</html>