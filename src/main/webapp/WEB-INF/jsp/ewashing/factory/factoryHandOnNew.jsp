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
			+ path + "storeBusiness";
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>工厂上挂</title>

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

	// 初始化信息
	jQuery(document).ready(function(){
		$("#queryKey").val("");
		$("#queryKey").focus();
	});
	
	// 扫描上挂
	function query(){
		var params = $("#formId").serialize();
	    location.href = "<%=basePath%>/factory/factoryHandOnNew?"+params;
	}
	
	function waitingHandon(){
		art.dialog.open( '<%=basePath%>/factory/factoryHandOn', {
            title : '待上挂衣服',
            width : '100%',
            height : 690,
            lock : true
 		});
	}
	
</script>
</head>

<!-- 异步表格   <body onload="ajaxReload()"> -->
<body id="content">

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

	<div class="nav_tit">
		<em>工厂上挂</em>
	</div>
	<!-- 搜索条件 -->
	<form id="formId" name="formId" class="form-horizontal" role="form" method="post">
	
	<div  style="padding-left: 10px; margin-top: 5px;">
			<table cellpadding="0" cellspacing="0" border="0" width="100%" >
				<tr>
					<td width="10%">
						<div class="input-group" >
							<input id="queryKey" placeholder="输入条码" onchange="query()"  name="queryKey" value="${queryKey}" type="text" class="form-control" style="width: 150px;"> <!-- <span class="glyphicon glyphicon-star"></span> -->
						</div>
					</td>
					<td width="100px" align="left">
						<div class="input-group" style="padding-left: 10px;">
							<button class="btn btn-primary" type="button" id="transLogQueryBtn" onclick="query()" style="margin-right: 10px;">查  询</button>
							<button class="btn btn-primary" type="button" id="transLogQueryBtn" onclick="waitingHandon()" style="margin-right: 10px;">待上挂衣物</button>
						</div>
					</td>
				</tr>
				
			</table>
		</div>
		
	<div style="margin-top: 10px;"></div>
		<div  style="margin:0 10px;position: relative;">
			<div style="color: blue;font-size: 15px;position: relative;margin-top: 20px;">【订单信息】</div>
			<table class="table datatable table-bordered" cellpadding="0" cellspacing="0" border="1" width="100%">
				<tr>
					<td width="10%" align="right">订单号:</td>
					<td width="100px" align="left" id ="orderCodeStr">${order.orderCode}</td>

					<td width="10%" align="right">订单日期:</td>
					<td width="100px" align="left"><fmt:formatDate value="${order.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>

					<td width="10%" align="right">订单状态:</td>
					<td width="100px" align="left">${order.orderStatus}</td>
				</tr>

				<tr>
					<td width="10%" align="right">衣物件数:</td>
					<td width="100px" align="left">${order.clothesCount}</td>

					<td width="10%" align="right">应收金额:</td>
					<td width="100px" align="left">${order.receivableAmount}元</td>

					<td width="10%" align="right">付款状态:</td>
					<c:if test="${order.payStatus=='0'}"><td width="100px" align="left"><font color="red">未支付</font></td></c:if>
					<c:if test="${order.payStatus=='1'}"><td width="100px" align="left">已支付</td></c:if>
					<c:if test="${order.payStatus=='2'}"><td width="100px" align="left"><font color="red">欠款洗涤</font></td></c:if>
				</tr>

				<tr>
					<td width="10%" align="right">付款时间:</td>
					<td width="100px" align="left"><fmt:formatDate value="${order.payDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>

					<td width="10%" align="right">已付金额:</td>
					<td width="100px" align="left">${order.paidAmount}元</td>

					<td width="10%" align="right">未支付金额:</td>
					<td width="100px" align="left">${order.receivableAmount-order.paidAmount}元</td>
				</tr>
				
				<tr>
					<td width="10%" align="right">已上挂件数:</td>
					<td width="100px" align="left">${handonCount}</td>

					<td width="10%" align="right">已上隔架区:</td>
					<td width="100px" align="left">${order.handOnArea}</td>

					<td width="10%" align="right">已上隔架号</td>
					<td width="100px" align="left">${order.handOnNo}</td>
				</tr>
			</table>
			
		</div>
	<br style="border: 1px;border-color: fff"/>
	<div style="display: inline;">
		<div style="color: blue;font-size: 15px;position: relative;margin-top: 15px;margin-left: 10px;float: left;">【衣物清单】</div>
	</div>
	<article>
		<div class="segment">
			<div style="padding-left: 10px; padding-right: 10px;">
				<table class="table datatable table-bordered  table-hover" id="datatable">
					<thead>
						<tr>
							<th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>衣物条码</th>
							<th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>服务类型</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>衣物名称</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>颜色</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>品牌</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>瑕疵</th>
							
							<th class='text-center' data-col-class='text-center' data-width='10%'>衣物状态</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>送洗时间</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>上挂时间</th>
						</tr>
					</thead>
					<!-- 同步表格 -->
					<tbody>
 						<!--  PageHelper -->
						<c:forEach items="${handOnList}" var="clothes">
							<tr>
							    <td class='text-center'>${clothes.barCode}</td>
							    <td class='text-center'>${clothes.serviceType}</td>
							    <td class='text-center'>${clothes.clothesName}</td>
							    <td class='text-center'>${clothes.color}</td>
							    <td class='text-center'>${clothes.brand}</td>
							    <td class='text-center'>${clothes.flaw}</td>
							    <td class='text-center'>${clothes.status}</td>
							    <td class='text-center'>${clothes.sendDateStr}</td>
							    <td class='text-center'>${clothes.handOnDateStr}</td>
							</tr>
						</c:forEach>
					</tbody>
				   </table>
				</div>
			</div>
		</article>
	</form>
</body>

<script type="text/javascript">
	$('table.datatable').datatable({	
		sortable: true,
		checkable: true,
		storage: false,
	    checkedClass: "checked",
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