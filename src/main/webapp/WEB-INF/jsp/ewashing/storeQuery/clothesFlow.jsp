<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
	String realPath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "store";
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>衣服流程</title>

	<link rel="stylesheet" media="screen" href="<%=basePath%>/css/history.css">
	<script type="text/javascript" src="<%=basePath%>/js/jquery/jquery.js"></script>

	<style type="text/css">
		.nav_tit {
			height: 40px;
			font: 14px/40px 'Microsoft Yahei';
			color: #000;
			padding-left: 25px;
			background-image: url(<%=basePath%>/common/images/public/next.gif);
			background-repeat: no-repeat;
			background-position: 10px 13px;
			background-color: #EDF6FA;
			border-bottom: 1px double #D7E4EA;
		}

		.nav_tit em {
			height: 40px;
			font: 14px/40px 'Microsoft Yahei';
			color: #000;
			float: left;
		}
		
</style>


<script type="text/javascript">

	//$('#datatable').datatable({fixedHeaderOffset: 41, checkable: true});
	jQuery(document).ready(function(){
		
		var clothesId =$("#clothesId").val();
		$("#timeline").html("");
		// 获取衣服日志数据
		jQuery.ajax({
			url : "<%=basePath%>/orderQuery/queryClothesFlows",
			type : "post",
			cache : false,
			dataType : "json",
			data : {'clothesId':clothesId},
			success : function(data, textStatus) {
				if (data.resultCode == "0") {
					art.dialog.alert(data.resultMsg);
				} else {
					// 动态生成时间轴
					var htmlStr ="";
					if(data.list!= null && data.list.length>0){
						
						for(var i=0;i<data.list.length;i++){
							var dataJson =data.list[i];
							htmlStr+="<li class='green'>"
							htmlStr+="<h3><font color='blue'>"+dataJson.optDateStr+"</font><span><font color='blue'>"+dataJson.clothesStatus+"</font></span></h3>"
							htmlStr+="</li>"
						}
					}
					
					$("#timeline").html(htmlStr);
				}
			}
		});
	});
	
	
</script>
</head>

<!-- 异步表格   <body onload="ajaxReload()"> -->
<body id="content">
<input type="hidden" id ="clothesId" name ="clothesId" value="${clothesId}"/>
<div class="nav_tit">
	<em>衣服流程图</em>
</div>

<!-- 代码 开始 -->
<div class="head-warp">
  <div class="head">
        <div class="nav-box">
          <ul>
              <li class="cur" style="text-align:center; font-size:30px; font-family:'微软雅黑', '宋体';">衣服流程</li>
          </ul>
        </div>
  </div>
</div>

<div class="main" style="position: relative;text-align: center;margin-left: 200px;margin-right: 100px;">
  <div class="history">
    <div class="history-date">
      	<ul id="timeline">
       	</ul>
    </div>
    </div>    
</div>

</body>
</html>