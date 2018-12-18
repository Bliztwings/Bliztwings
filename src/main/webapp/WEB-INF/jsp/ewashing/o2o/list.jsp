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
	<title>小E管理</title>

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

.navbox {
	position: relative;
	float: left;
	background-color: #f5f5f5;
}

ul.nav {
	list-style: none;
	display: block;
	width: 200px;
	position: relative;
	padding: 0px 0 60px 0;
	-webkit-background-size: 50% 100%;
}
</style>


<script type="text/javascript">
	jQuery(document).ready(function() {
		
	});
	
	/**
	 * 第三方URL
	 */
	function jump(title,url){
		
		if ($('#mtabs').tabs('exists', title)){
			$('#mtabs').tabs('select', title);
		} else {
			var content = '<iframe name="mainFrame" id ="mainFrame" scrolling="auto" frameborder="0"  src="'+url+'" style="height:100%;width:80%;"></iframe>';
			$('#mtabs').tabs('add',{
				title:title,
				content:content,
				closable:true
			});
		}
	}

	// 清空查询参数
	function resetQuery() {
		$("#e_name").val();
		$("#e_mobile").val();
		location.href = "<%=basePath%>/sendReceiver/list";
	}
	
	//  查询
	function query(){
		var params = $("#formId").serialize();
	    location.href = "<%=basePath%>/sendReceiver/list?"+params;
	}
	
	//  新增
	function add(obj){
		art.dialog.open( '<%=basePath%>/sendReceiver/initAdd', {
			title : '添加小E',
			width : 600,
			height : 300,
			lock : true,
			close:function (){
				query();
			}
		});
	}
	
	function preEdit(id){
		art.dialog.open( '<%=basePath%>/sendReceiver/preUpdate?id='+id, {
			title : '修改小E',
			width : 600,
			height : 300,
			lock : true,
			close:function (){
				query();
			}
		});
	}
	
	
	
	function deleteReceicer(id){
		
		art.dialog.confirm('确实要删除小E吗？', function () {
			jQuery.ajax({
				url : "<%=basePath%>/sendReceiver/deleteReceicer",
				type : "post",
				cache : false,
				dataType : "json",
				data : {'id':id},
				success : function(data, textStatus) {
					if (data.resultCode == "0") {
						art.dialog.alert(data.resultMsg);
					} else {
						art.dialog.alert(data.resultMsg,function(){
							query();
						});
					}
				}
			});
		}, function () {
		});
	}
	
	
</script>
</head>

<!-- 异步表格   <body onload="ajaxReload()"> -->
<body id="content">

	<div class="nav_tit">
		<em>小E管理</em>
	</div>
	<!-- 搜索条件 -->
	<form id="formId" name="formId" class="form-horizontal" role="form" method='get' action="<%=basePath%>/sendReceiver/list">
		<div  style="padding-left: 10px; margin-top: 5px;">
			<table cellpadding="0" cellspacing="0" border="0" width="100%" >
				<tr>
					<td width="10%">
						<div class="input-group" >
							<span class="input-group-addon" style="width:20px; text-align: left;">小E名称：</span> 
							<input id="e_name" name="name" value="${name}" type="text" class="form-control" style="width: 150px;"> <!-- <span class="glyphicon glyphicon-star"></span> -->
						</div>
					</td>
					
					<td width="10%">
						<div class="input-group" >
							<span class="input-group-addon" style="width:20px; text-align: left;">平台类型：</span> 
							<select class="form-control"  style="width: 160px;" name="platformType" id="platformType">
								<option value=""></option>
								<option value="1">萨维亚</option>
								<option value="2">浣衣坊</option>
							</select>
						</div>
					</td>
					
					
					<td width="10%">
						<div class="input-group" style="padding-left:10px;" >
							<span class="input-group-addon" style="width:20px; text-align: left;">手机号码：</span> 
							<input id="e_mobile" name="mobile" value="${mobile}" type="text" class="form-control" style="width: 150px;">
						</div>
					</td>
					<td width="100px" align="left">
						<div class="input-group" style="padding-left: 10px;">
							<button class="btn btn-primary" type="button" id="transLogQueryBtn" onclick="query()" style="margin-right: 10px;">查  询</button>
							<button class="btn btn-primary" type="button" id="reloadDatatable" onclick="resetQuery()" style="margin-right: 10px;">重   置</button>
							<button class="btn btn-primary" type="button" id="reloadDatatable" onclick="add(this)">新   增</button>
						</div>
					</td>
				</tr>
				
			</table>
	
		</div>
<article>
		<!-- 表格-->
		<div class="segment">
			<div id="dtFromData"></div>
			<div style="padding-left: 10px; padding-right: 10px;">
				<table class="table  table-bordered  table-hover" id="datatable">
					<thead>
						<tr>
						    <th style="display:none" class='text-center' data-col-class='text-center' data-width='30%' class='text-primary'>ID</th>
							<th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>小E名称</th>
							
							<th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>平台类型</th>
							<th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>收送人员类型</th>
							
							
							<th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>小E电话</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>小E地址</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>创建时间</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>操作</th>
						</tr>
					</thead>
					<!-- 同步表格 -->
					<tbody>
 						<!--  PageHelper -->
						<c:forEach items="${page.list}" var="e">
							<tr>
							    <td style="display:none">${e.id}</td>
							    <td align="center">${e.name}</td>
							    
							    <td align="center"><c:if test="${e.platformType=='1'}">萨维亚</c:if><c:if test="${e.platformType=='2'}">浣衣坊</c:if></td>
							    
							    <td align="center"><c:if test="${e.userType=='1'}">收衣人员</c:if><c:if test="${e.userType=='2'}">送衣人员</c:if></td>
							    
							    <td align="center">${e.mobile}</td>
							    <td align="center">${e.address}</td>
							    <td align="center"><fmt:formatDate value="${e.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							    <td align="center">
							    	<a href="javascript:void(0)" onclick="preEdit('${e.id}')">修改 </a>|
							    	<a href="javascript:void(0)" onclick="deleteReceicer('${e.id}')">删除</a>
							    </td>
							</tr>
						</c:forEach>
					</tbody>
					
					<tfoot>
						<td align="right" colspan="8">		
						<!--  PageHelper -->	
						<jsp:include page="../../inc/page_PageHelper.jsp">
								<jsp:param name="path" value="/sendReceiver" />
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
		    	location.href = '<%=basePath%>/sendReceiver/list?'+paras+pageParas;
		    },
		    doSearch: function(){
		    	var paras = $("#formId").serialize();
		    	location.href = '<%=basePath%>/sendReceiver/list?'+paras;
		    },
		    resetSearch : function(){
		    	location.href = '<%=basePath%>/sendReceiver/list';
		    }
	}
});   
</script>
</html>