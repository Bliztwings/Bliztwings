<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
	String realPath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/config/list";
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>用户列表</title>

	<link type="text/css" href="<%=basePath%>/zui-1.5.0/css/zui.min.css" rel="stylesheet" />
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/jquery/jquery.js"></script>
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/js/zui.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/datatable/zui.datatable.js"></script>
	<link type="text/css" href="<%=basePath%>/zui-1.5.0/lib/datatable/zui.datatable.css" rel="stylesheet" />
	<script src="<%=basePath%>/js/jquery/jquery.form.js" type="text/javascript"></script>
	<script type="text/javascript"
		src="<%=basePath%>/js/jquery/jquery.json-2.2.js"></script>
	
	<!--  art.dialog  -->
	<link href="<%=basePath%>/js/artDialog4.1.7/skins/blue.css"
		rel="stylesheet" type="text/css" />
	<script src="<%=basePath%>/js/artDialog4.1.7/artDialog.js"
		type="text/javascript"></script>
	<script
		src="<%=basePath%>/js/artDialog4.1.7/plugins/iframeTools.source.js"
		type="text/javascript"></script>
		
<!--  Ztree  -->
<link rel="stylesheet" href="<%=basePath%>/js/JQuery zTree v3.5.15/css/css.css">
<link rel="stylesheet" href="<%=basePath%>/js/JQuery zTree v3.5.15/css/zTreeStyle/zTreeStyle.css">
<!--  <script src="<%=basePath%>/js/jquery/jquery-1.11.0.min.js"></script>   -->
<script src="<%=basePath%>/js/JQuery zTree v3.5.15/js/jquery.ztree.all-3.5.js"></script>		
		
<style type="text/css">
.nav_tit{height:40px;font:14px/40px 'Microsoft Yahei';color:#000;padding-left:25px;background-image: url(<%=basePath%>/common/images/public/next.gif);background-repeat: no-repeat;
	background-position: 10px 13px;background-color: #EDF6FA;border-bottom: 1px double #D7E4EA;}
.nav_tit em{height:40px;font:14px/40px 'Microsoft Yahei';color:#000;float:left;}
</style>

<script type="text/javascript">
function add(obj){

	art.dialog.open( '<%=basePath%>/config/toAdd', {
				title : '添加文件资源库',
				width : 600,
				height : 400,
				lock : true
	});

}

// 搜索查询
function seach(){
	var params = $("#formId").serialize();
    location.href = "<%=basePath%>/config/list?"+params;
}

function preEdit(obj){
	art.dialog.open( '<%=basePath%>/config/toEdit?id='+obj, {
				title : '修改参数配置',
				width : 600,
				height : 400,
				lock : true
	});
}

function remove(id) {

	art.dialog.confirm('确实要删除该条记录吗？', function () {
		jQuery.ajax({
			url : "<%=basePath%>/config/remove",
			type : "post",
			cache : false,
			dataType : "json",
			data :　{'id':id},
			success : function(data, textStatus) {
				if (data.resultCode == "0") {
					//art.dialog.alert('删除失败');
					art.dialog.alert(data.resultMsg);
					return false;
				} else {
					//art.dialog.alert('删除成功');
				    art.dialog.alert(data.resultMsg,function(){
				    	var params = $("#formId").serialize();
					    location.href = "<%=realPath%>/?"+params;
					});
				}
			},
		});
	}, function () {
	});
}

// 清空查询参数
function resetSeach(){

	$("#name").val();
	location.href = "<%=basePath%>/config/list";
}


function showResponse(resultData) {
	var trStr ="";
	if(resultData == undefined || resultData =="" ||  resultData == null || resultData.length<=0){
		//数据为空，
		var td_i = $("#datatable thead tr th").size();
		trStr="<tr><td colspan='"+td_i+"'>没有找到任何相关记录</td></tr>";
	}else{
		//得到要填充的字符串
		for(var i=0;i<resultData.length;i++){
			user=resultData[i];
			trStr += '<tr>';
			trStr += '<td style="width: 100px;">'+ user.id +'</td>';
			trStr += '<td>'+ user.username +'</td>';
			trStr += '<td>'+ user.realname +'</td>';
			trStr += '<td>'+ user.email +'</td>';
			trStr += '<td>'+ user.phone +'</td>';
			trStr += '<td>'+ user.orgId +'</td>';
			trStr += '<td>'+ user.createTime +'</td>';

			if( user.status == "enabled"){
				trStr += '<td>可用</td>';
			}else{ 
				trStr += '<td>不可用</td>';
			}
			/* 表格按钮  */
/* 			trStr += '<td><div class="yanshi_newultda">'
					+ '<span class="icon_chakan"> <a href="javascript:showView('+d.pkid+')" title="查看">查看</a></span>' 
					+ '<span class="icon_Modify"> <a href="javascript:preUpdate('+d.pkid+')" title="修改">修改</a></span>'
					+ '<span class="icon_del"> <a href="javascript:del('+d.pkid+')" title="删除">删除</a> </span>'
				    + '</div></td>'; */
		    trStr +='</tr>';
		}
		
	}
	//进行表格填充
	$(".datatable-rows .datatable-rows-span .datatable-wrapper table tbody").html(trStr);
}


</script>
</head>
<!-- 异步表格   <body onload="ajaxReload()"> -->
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

<c:choose>
       <c:when test="${error == 0}">
       	    <button id="mybtn" type="button" class="btn btn-primary" style="display:none" data-toggle="modal" data-target="#myLgModal">大对话框</button>
			<div class="modal fade" id="myLgModal">
			  <div class="modal-dialog alert alert-warning" style="width:500px">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
			        <h4 class="modal-title"><i class="icon icon-warning-sign icon-2x"></i>&nbsp;&nbsp;&nbsp;&nbsp;您还没有启动文件资源库，请先启动一个文件资源库！</h4>
			      </div>
			      <!-- 
			      <div class="modal-body">
			        <p>主题内容...</p>
			      </div> -->
			      <div class="modal-footer" style="text-align:center">
			        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			      </div>
			    </div>
			  </div>
			</div>
			<script type="text/javascript">
			$(function(){
			  $("#mybtn").click();
			});
			</script>
       </c:when>
       <c:otherwise>
       
<div class="nav_tit">
		<em>文件资源库管理</em>
	</div>
	
<iframe name="tree" class="ui-layout-west" src="<%=basePath%>/resource/treeFrame" frameborder="0" scrolling="auto"></iframe>
 
<iframe name="content" class="ui-layout-center" src="" frameborder="0" scrolling="auto"></iframe>
        
</body>

<script src="<%=basePath%>/js/jquery/jquery-1.11.0.min.js"></script>
<script src="<%=basePath%>/js/jquery/jquery.layout-latest.min.js"></script>
<script>
    $(function () {
        $(document).ready(function () {
            $('body').layout({ applyDemoStyles: true });
        });
    });
</script>
</c:otherwise>
</c:choose>

</html>