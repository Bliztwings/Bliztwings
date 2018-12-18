<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path;
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<!-- jsp文件头和头部 -->
	<meta name="description" content="overview & stats" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<title>浣衣坊洗衣管理系统</title>
	<link href="static/css/bootstrap.min.css" rel="stylesheet" />
	<link href="static/css/bootstrap-responsive.min.css" rel="stylesheet" />
	<link rel="stylesheet" href="static/css/font-awesome.min.css" />
	<link rel="stylesheet" href="static/css/ace.min.css" />
	<link rel="stylesheet" href="static/css/ace-responsive.min.css" />
	<link rel="stylesheet" href="static/css/ace-skins.min.css" />
	
	<link rel="stylesheet" type="text/css" href="js/easyui/themes/default/easyui.css">
	
	<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
 
 
	<script type="text/javascript"> 
	  	function openMenu(id){
	  		var display =$('#'+id).css('display');
	  		if(display == 'none'){
	  			$('.submenu').css('display','none');
	  			$('#'+id).parent().parent('.submenu').css('display','block');
	  			$('#'+id).css('display','block');
	  		}else{
	  			
	  			$('#'+id).css('display','none');
	  		}
	  	}
  </script>	
</head>
<body>

	<!-- 页面顶部¨ -->
	<%@ include file="head.jsp"%>
	<div id="websocket_button"></div>
	<div class="container-fluid" id="main-container">
		<a href="#" id="menu-toggler"><span></span></a>
		<!-- 左侧菜单 -->
		<%@ include file="left.jsp"%>
		 <style type="text/css">
			.commitopacity{position:absolute; width:100%; height:100px; background:#7f7f7f; filter:alpha(opacity=50); -moz-opacity:0.8; -khtml-opacity: 0.5; opacity: 0.5; top:0px; z-index:99999;}
		 </style>
	
		<div id="main-content" class="clearfix">
			<div id="mainFrame">
			    <div class="easyui-tabs" fit="true" border="false" id="tabs">
			      <div title="首页">
     					  <iframe name="mainFrame" id="mainFrame" frameborder="0" src="<%=basePath%>/welcome" style="margin:0 auto;width:100%;height:100%;"></iframe>
			      </div>
			    </div>
			</div>
			
			 <!-- 换肤 -->
			<div id="ace-settings-container">
				<div class="btn btn-app btn-mini btn-warning" id="ace-settings-btn">
					<i class="icon-cog"></i>
				</div>
				<div id="ace-settings-box">
					<div>
						<div class="pull-left">
							<select id="skin-colorpicker" class="hidden">
								<option data-class="default" value="#438EB9"
									selected>#438EB9</option>
								<option data-class="skin-1" value="#222A2D"
									>#222A2D</option>
								<option data-class="skin-2" value="#C6487E"
									>#C6487E</option>
								<option data-class="skin-3" value="#D0D0D0"
									>#D0D0D0</option>
							</select>
						</div>
						<span>&nbsp; 选择皮肤</span>
					</div>
					<div>
						<label><input type='checkbox' name='menusf' id="menusf" onclick="menusf();" /><span class="lbl" style="padding-top: 5px;">菜单缩放</span></label>
					</div>
				</div>
			</div>
			 
		</div>
		<!-- #main-content -->
		<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse"> 
		   <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i> 
		</a>
	</div>
	<!-- 引入 -->
	
	<!--  art.dialog  -->
	<link href="<%=basePath%>/js/artDialog4.1.7/skins/blue.css" rel="stylesheet" type="text/css" />
	<script src="<%=basePath%>/js/artDialog4.1.7/artDialog.js" type="text/javascript"></script>
	<script src="<%=basePath%>/js/artDialog4.1.7/plugins/iframeTools.source.js" type="text/javascript"></script>
	
	<script type="text/javascript">window.jQuery || document.write("<script src='<%=basePath%>/static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
	<script src="<%=basePath%>/static/js/bootstrap.min.js"></script>
	<script src="<%=basePath%>/static/js/ace-elements.min.js"></script>
	<script src="<%=basePath%>/static/js/ace.min.js"></script>
	<!-- 引入 -->
	<script type="text/javascript" src="<%=basePath%>/static/js/jquery.cookie.js"></script>
	<script type="text/javascript" src="<%=basePath%>/static/js/myjs/menusf.js"></script>
	<!--引入属于此页面的js -->
	<script type="text/javascript" src="<%=basePath%>/static/js/myjs/index.js"></script>

	<script type="text/javascript" src="<%=basePath%>/js/easyui/jquery.easyui.min.js"></script>
</body>
</html>
