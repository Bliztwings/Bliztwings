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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>字典对码系统</title>
<link type="text/css" rel="stylesheet" href="common/css/reset.css" />
<link type="text/css" rel="stylesheet" href="common/css/style.css" />
<link type="text/css" rel="stylesheet" href="common/css/public.css" />
<script type="text/javascript" src="common/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="common/js/jquery.autoheight.js"></script>

<!-- 引入factory.css -->
<link rel="stylesheet" href="<%=basePath%>/static/css/factory.css"
    type="text/css" />
    
    <!--  art.dialog  -->
<link href="<%=basePath%>/js/artDialog4.1.7/skins/blue.css"
    rel="stylesheet" type="text/css" />
<script src="<%=basePath%>/js/artDialog4.1.7/artDialog.js"
    type="text/javascript"></script>
<script
    src="<%=basePath%>/js/artDialog4.1.7/plugins/iframeTools.source.js"
    type="text/javascript"></script>
    
<style type="text/css">
body {
	background: #f0f9fd url(common/images/bg/bg_welcome.jpg) center center no-repeat;
}

html {
	overflow: scroll;
	overflow-y: auto;
	overflow-x: auto;
}

{
margin: 0px;
padding: 0px;
}
a {color:black;
}
#keleyislide {
	width: 300px;
	height: 200px;
	border: 1px solid #000;
	position: fixed;
	bottom: 2px;
	right: 2px;
	display: none;
	background-color: White;
	z-index:9999;
}

#keleyislide a {
	position: absolute;
	top: 8px;
	right: 8px;
	font-size: 12px;
	text-decoration: none;
	color: Blue;
}

#keleyislide h2 {
	font-size: 24px;
	text-align: center;
	font-family: "微软雅黑";
}

#reshow {
	position: fixed;
	right: 2px;
	bottom: 2px;
	font-size: 12px;
	display: none;
	background-color: White;
	cursor: pointer;
	border: 1px solid #000;
}
</style>
</head>
<body>
<script type="text/javascript">
	function Jihua_Cnblogs_Com() { 
	   $("#kel"+"eyislide").slideDown("slow"); $("#reshow").hide(); 
	}
	function KeleyiAutoHide() { 
	   $("#ke"+"leyislide").slideUp("slow"); $("#reshow").show(); 
	}
	$(document).ready(function () {
		
	    jQuery.ajax({
			url : "<%=basePath%>/storeBusiness/warning",
			type : "get",
			cache : false,
			dataType : "json",
			success : function(data, textStatus) {
				
				if(data.userType=='1'){
					
					$("#quickKey").show();
						
					$("#sendCountDiv").html("待送洗:<font color='red' size='5'>"+data.sendCount+"件"+"</font>");
					
					
					 setTimeout(function () {
					     Jihua_Cnblogs_Com();
					  }, 1000)
					  setTimeout(function () { 
						KeleyiAutoHide(); },19000)
					    $("#close").click(function () {
					    KeleyiAutoHide();
					  })
					  $("#reshow").mouseover(function () {
					    Jihua_Cnblogs_Com(); //keleyi.com
					  })
				}
				
			}
		});
	})
	
	// 收衣
	function receiceClothes(){
		<%-- art.dialog.open( '<%=basePath%>/storeBusiness/receive', {
            title : '收衣',
            width : '100%',
            height : 690,
            lock : true
        }); --%>
        
        var url ='<%=basePath%>/storeBusiness/receive';
        Open("收衣",url);
	}
	
	// 取衣
	function takeClothes(){
		<%-- art.dialog.open( '<%=basePath%>/storeBusiness/memberOrder', {
            title : '顾客取衣',
            width : '100%',
            height : 690,
            lock : true
        }); --%>
        
        var url ='<%=basePath%>/storeBusiness/memberOrder';
        Open("顾客取衣",url);
	}
	
	// 会员办理
	function addMember(){
		art.dialog.open( '<%=basePath%>/member/beforeAdd', {
			title : '添加会员',
			width : 600,
			height : 500,
			lock : true
		});
	}
	var jq = top.jQuery;
	// 添加菜单到tab
	function Open(text, url) {
		
		//var jq = top.jQuery;
	    if (jq("#tabs").tabs('exists', text)) {
	    	jq('#tabs').tabs('select', text);
	    	/* jq("#tabs").tabs("select", text); 
	        var selTab = $('#tabs').tabs('getSelected'); 
	        jq('#tabs').tabs('<strong>update</strong>', { 
	          tab: selTab, 
	          options: { 
	        	  content :'<iframe name="mainFrame" id="mainFrame" frameborder="0" src="'+url+'" style="margin:0 auto;width:100%;height:100%;z-index: 100000"></iframe>'
	          } 
	        }); */
	        
	    } else {
	    	var content ='<iframe name="mainFrame" id="mainFrame" frameborder="0" src="'+url+'" style="margin:0 auto;width:100%;height:100%;z-index: 100000"></iframe>';
	    	
	    	jq('#tabs').tabs('add', {
	            title : text,
	            closable : true,
	            content : content
	        });
	    }
	}
	
	jq('#tabs').tabs({
		onSelect: function(title){
			
			var tab =jq('#tabs').tabs('getSelected');  
			jq('#tabs').tabs('update', {
				tab : tab,
				options : {
					options: tab.panel('options') 
			     }
	        });
	    }
	});
	
</script>
<div class="autoH">

		<div style="display: inline;display: none" id="quickKey">
			<div class="store-left-td-div" 
	                 style="position: relative;float: left;margin-left: 50px;margin-top: 50px;border-radius: 10px;"
	                 onclick="takeClothes()"></div>
			
			<div class="store-left-td-div1"
	                  style="position: relative;float: left;margin-left: 50px;margin-top: 50px;border-radius: 10px;"
	                  onclick="receiceClothes()"
	                  ></div>
			
			<div class="store-left-td-div2"
	                  style="position: relative;float: left;margin-left: 50px;margin-top: 50px;border-radius: 10px;"
	                  onclick="addMember()"
	                  ></div>
	</div>
	<div id="keleyislide">
		<a href="javaScript:void(0)" id="close" target="_self">关闭</a>
		<span style=" line-height:40px;margin-left: 10px;">预警信息<br />
			<div style="margin-left: 10px;" id="sendCountDiv">待送洗:<font color="red" size="5">${sendCount}件</font></div>
			<!-- <div style="margin-left: 10px;">未付款:<font color="red" size="5">3件</font></div>
			<div style="margin-left: 10px;">待取衣:<font color="red" size="5">3件</font></div> -->
		</span>
	</div>
</div>
</body>
</html>
