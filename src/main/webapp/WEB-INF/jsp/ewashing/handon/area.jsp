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
	<title>门店管理</title>

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
	
	function deleteHandonArea(id){
		art.dialog.confirm('确实要删除该挂衣区吗？', function () {
			jQuery.ajax({
				url : "<%=basePath%>/handon/deleteHandonArea",
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
	//  查询
	function query(){
		var params = $("#formId").serialize();
	    location.href = "<%=basePath%>/handon/area?"+params;
	}
	
	//  新增
	function add(obj){
		art.dialog.open( '<%=basePath%>/handon/initAdd', {
			title : '添加挂衣区',
			width : 600,
			height : 300,
			lock : true,
			close:function (){
				query();
			}
		});
	}
</script>
</head>

<body id="content">

	<div class="nav_tit">
		<em>挂衣区管理</em>
	</div>
	<!-- 搜索条件 -->
	<form id="formId" name="formId" class="form-horizontal" role="form" method='get' action="<%=basePath%>/handon/area">
		<div  style="padding-left: 10px; margin-top: 5px;">
			<table cellpadding="0" cellspacing="0" border="0" width="100%" >
				<tr>
					<td width="10%">
						<div class="input-group" >
							<span class="input-group-addon" style="width:20px; text-align: left;">挂衣区：</span> 
							<input id="handOnArea" name="handOnArea" type="text" class="form-control" style="width: 150px;">
						</div>
					</td>
					<td width="100px" align="left">
						<div class="input-group" style="padding-left: 10px;">
							<button class="btn btn-primary" type="button" id="transLogQueryBtn" onclick="query()" style="margin-right: 10px;">查  询</button>
							<button class="btn btn-primary" type="button" id="reloadDatatable" onclick="add(this)">新   增</button>
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
				<table class="table  table-bordered  table-hover" id="datatable">
					<thead>
						<tr>
						    <th style="display:none" class='text-center' data-col-class='text-center' data-width='30%' class='text-primary'>ID</th>
						    <th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>所属类型</th>
						    <th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>所属门店</th>
							<th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>挂衣区</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>创建时间</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>操作</th>
						</tr>
					</thead>
					<!-- 同步表格 -->
					<tbody>
 						<!--  PageHelper -->
						<c:forEach items="${page.list}" var="handon">
							<tr>
							    <td class='text-center' style="display:none">${handon.id}</td>
							    <c:if test="${handon.handType=='1' }">
							    	<td  class='text-center'>门店</td>
							    </c:if>
							    <c:if test="${handon.handType=='2' }">
							    	<td class='text-center'>工厂</td>
							    </c:if>
							    <c:if test="${handon.handType=='3' }">
							    	<td class='text-center'>萨维亚</td>
							    </c:if>
							    <c:if test="${handon.handType=='4' }">
							    	<td class='text-center'>浣衣坊</td>
							    </c:if>
							    <td class='text-center'>${handon.storeName}</td>
							    <td class='text-center'>${handon.handOnArea}</td>
							    <td class='text-center'><fmt:formatDate value="${handon.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							    <td class='text-center'>
							    	<a href="javascript:void(0)" onclick="deleteHandonArea('${handon.id}')">删除</a>
							    </td>
							</tr>
						</c:forEach>
					</tbody>
					
					<tfoot>
						<td align="right" colspan="8">		
						<!--  PageHelper -->	
						<jsp:include page="../../inc/page_PageHelper.jsp">
								<jsp:param name="path" value="/handon" />
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
		    	location.href = '<%=basePath%>/store/list';
		    }
	}
});   
</script>
</html>