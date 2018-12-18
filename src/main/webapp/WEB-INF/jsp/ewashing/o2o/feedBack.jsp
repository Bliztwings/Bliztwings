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
			+ path + "feedBack";
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>意见反馈</title>

	<link type="text/css" href="<%=basePath%>/zui-1.5.0/css/zui.min.css" rel="stylesheet" />
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/jquery/jquery.js"></script>
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/js/zui.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/datatable/zui.datatable.js"></script>
	<link type="text/css" href="<%=basePath%>/zui-1.5.0/lib/datatable/zui.datatable.css" rel="stylesheet" />
	<script src="<%=basePath%>/js/jquery/jquery.form.js" type="text/javascript"></script>
	<script type="text/javascript" src="<%=basePath%>/js/jquery/jquery.json-2.2.js"></script>
	
	<!--  art.dialog  -->
	<link href="<%=basePath%>/js/artDialog4.1.7/skins/blue.css" rel="stylesheet" type="text/css" />
	<script src="<%=basePath%>/js/artDialog4.1.7/artDialog.js" type="text/javascript"></script>
	<script src="<%=basePath%>/js/artDialog4.1.7/plugins/iframeTools.source.js" type="text/javascript"></script>
		
	<!--  Ztree  -->
    <link rel="stylesheet" href="<%=basePath%>/zui-1.5.0/lib/datetimepicker/datetimepicker.min.css">
    <script src="<%=basePath%>/zui-1.5.0/lib/datetimepicker/datetimepicker.min.js"></script>
    
    <!-- vue1.0 -->
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/js/vue1.0/iview.css">
    <script type="text/javascript" src="<%=basePath%>/js/vue1.0/vue.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/js/vue1.0/vue-resource.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/js/vue1.0/iview.min.js"></script>

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
		
		
	});
	
	
	// 清空查询参数
	function resetQuery(){
		$("#e_name").val();
		$("#e_mobile").val();
		location.href = "<%=basePath%>/feedBack/list";
	}
	
	//  查询
	function query(){
		var params = $("#formId").serialize();
	    location.href = "<%=basePath%>/feedBack/list?"+params;
	}
	
</script>
</head>

<!-- 异步表格   <body onload="ajaxReload()"> -->
<body id="content">

	<div class="nav_tit">
		<em>用户反馈</em>
	</div>
	<!-- 搜索条件 -->
	<form id="formId" name="formId" class="form-horizontal" role="form" method='get' action="<%=basePath%>/feedBack/list">
		<div  style="padding-left: 10px; margin-top: 5px;">
			<table cellpadding="0" cellspacing="0" border="0" width="100%" >
				<tr>
					<td width="10%">
						<div class="input-group" >
							<span class="input-group-addon" style="width:20px; text-align: left;">反馈类型：</span> 
							<select name="feedBackType" id="feedBackType" class="form-control" style="width: 150px">
								<option value="">全部</option>
								<option value="1" <c:if test="${feedBackType=='1'}">selected</c:if>>洗护质量</option>
								<option value="2" <c:if test="${feedBackType=='2'}">selected</c:if>>服务态度</option>
								<option value="3" <c:if test="${feedBackType=='3'}">selected</c:if>>物流速度</option>
								<option value="4" <c:if test="${feedBackType=='4'}">selected</c:if>>产品易用性</option>
								<option value="5" <c:if test="${feedBackType=='5'}">selected</c:if>>付款流程</option>
								<option value="6" <c:if test="${feedBackType=='6'}">selected</c:if>>其他</option>
							</select>
						</div>
					</td>
					<td width="100px" align="left">
						<div class="input-group" style="padding-left: 10px;">
							<button class="btn btn-primary" type="button" id="transLogQueryBtn" onclick="query()" style="margin-right: 10px;">查  询</button>
						</div>
					</td>
				</tr>
				
			</table>
	
		</div>
<br style="border: 1px;border-color: fff"/>
<article>
		<!-- 表格-->
		<div class="segment">
			<div id="dtFromData"></div>
			<div style="padding-left: 10px; padding-right: 10px;">
				<table class="table datatable table-bordered  table-hover" id="datatable">
					<thead>
						<tr>
						    <th style="display:none" class='text-center' data-col-class='text-center' data-width='30%' class='text-primary'>ID</th>
							<th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>返回类型</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>反馈内容</th>
							<th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>反馈时间</th>
						</tr>
					</thead>
					<!-- 同步表格 -->
					<tbody>
 						<!--  PageHelper -->
						<c:forEach items="${page.list}" var="e">
							<tr>
							    <td style="display:none">${e.id}</td>
							    <td>${e.feedBackType}</td>
							    <td>${e.feedBackContent}</td>
							    <td><fmt:formatDate value="${e.feedBackDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							</tr>
						</c:forEach>
					</tbody>
					
					<tfoot>
						<td align="right" colspan="4">		
						<!--  PageHelper -->	
						<jsp:include page="../../inc/page_PageHelper.jsp">
								<jsp:param name="path" value="/feedBack" />
						</jsp:include></td>
					</tfoot>
										
				</table>
			</div>
			
		</div>
		</article>
	</form>
</body>

<script type="text/javascript">
$('table.datatable').datatable({	
	sortable: true,
	checkable: false,
	storage: false,
    //checkedClass: "checked",
	fixedHeaderOffset: 0
	
});

var total = parseInt('${page.total}');
var pageNum = parseInt('${page.pageNum}');
var pageSize = parseInt('${page.pageSize}');


var vm = new Vue({
    el: '#content',
    data: {
        pagination: {
        	total : total,
        	pageNum : pageNum,
        	pageSize : pageSize
        },
    },
	methods: {
		    search: function(nextPage) {
		    	var paras = $("#formId").serialize();
		    	var pageParas = '&pageNum='+nextPage+'&pageSize='+this.pagination.pageSize;
		    	location.href = '<%=basePath%>/store/list?'+paras+pageParas;
		    },
		    doSearch: function(){
		    	var paras = $("#formId").serialize();
		    	location.href = '<%=basePath%>/store/list?'+paras;
		    },
		    resetSearch : function(){
		    	location.href = '<%=basePath%>/o2o/list';
		    }
	}
});   
</script>
</html>