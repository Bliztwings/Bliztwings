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
	<title>订单查询</title>

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
	
	//  查询
	function query(){
		var params = $("#formId").serialize();
	    location.href = encodeURI("<%=basePath%>/orderQuery/order?"+params);
	}

	function print(id){
		art.dialog.alert("打印待开发...");
	}
	
	function showDetail(orderId){
		art.dialog.open( '<%=basePath%>/orderQuery/orderDetail?orderId='+orderId, {
			title : '订单详情',
			width : '100%',
			height : 700,
			lock : true
		});
	}
</script>
</head>

<!-- 异步表格   <body onload="ajaxReload()"> -->
<body id="content">

	<div class="nav_tit">
		<em>订单查询</em>
	</div>
	<!-- 搜索条件 -->
	<form id="formId" name="formId" class="form-horizontal" role="form" method='get' action="<%=basePath%>/orderQuery/order">
		<div  style="padding-left: 10px; margin-top: 5px;">
			<table cellpadding="0" cellspacing="0" border="0" width="100%" >
				<tr>
					<td width="10%">
						<div class="input-group" >
							<span class="input-group-addon" style="width:20px; text-align: left;">订单编号：</span> 
							<input id="orderCode" name="orderCode" value="${orderCode}" type="text" class="form-control" style="width: 150px;"> <!-- <span class="glyphicon glyphicon-star"></span> -->
						</div>
					</td>
					<td width="10%">
						<div class="input-group" style="padding-left:10px;" >
							<span class="input-group-addon" style="width:20px; text-align: left;">会员卡号：</span> 
							<input id="cardNumber" name="cardNumber" value="${cardNumber}" type="text" class="form-control" style="width: 150px;"> <span class="glyphicon glyphicon-star"></span>
						</div>
					</td>
					
					<td width="10%">
						<div class="input-group" style="padding-left:10px;" >
							<span class="input-group-addon" style="width:20px; text-align: left;">支付状态：</span> 
							<select name="payStatus" id="payStatus" class="form-control" style="width: 150px">
								<option value="">全部</option>
								<option value="0" <c:if test="${payStatus=='0'}">selected</c:if>>未支付</option>
								<option value="1" <c:if test="${payStatus=='1'}">selected</c:if>>已支付</option>
							</select>
						</div>
					</td>
					
					<td width="10%">
						<div class="input-group" style="padding-left:10px;" >
							<span class="input-group-addon" style="width:20px; text-align: left;">订单状态：</span> 
							<select name="orderStatus" id="orderStatus" class="form-control" style="width: 150px">
								<option value="">全部</option>
								<option value="0" <c:if test="${orderStatus=='0'}">selected</c:if>>已收衣待送洗</option>
								<option value="1" <c:if test="${orderStatus=='1'}">selected</c:if>>已送洗待洗护</option>
								<option value="2" <c:if test="${orderStatus=='2'}">selected</c:if>>洗护中</option>
								<option value="3" <c:if test="${orderStatus=='3'}">selected</c:if>>洗护完成</option>
								<option value="4" <c:if test="${orderStatus=='4'}">selected</c:if>>待上挂</option>
								<option value="5" <c:if test="${orderStatus=='5'}">selected</c:if>>已上挂待取衣</option>
								<option value="6" <c:if test="${orderStatus=='6'}">selected</c:if>>完成</option>
							</select>
						</div>
					</td>
					
					<td width="100px" align="left">
						<div class="input-group" style="padding-left: 10px;">
							<button class="btn btn-primary" type="button" id="transLogQueryBtn" onclick="query()" style="margin-right: 10px;">查  询</button>
						</div>
					</td>
				</tr>
				<tr>
					<td width="10%">
						<div class="input-group" >
							<span class="input-group-addon" style="width:20px; text-align: left;">手&nbsp;&nbsp;机&nbsp;号：</span> 
							<input id="mobilePhone" name="mobilePhone" value="${mobilePhone}" type="text" class="form-control" style="width: 150px;"> <!-- <span class="glyphicon glyphicon-star"></span> -->
						</div>
					</td>
					
					<td width="10%">
						<div class="input-group" style="padding-left:10px;">
							<span class="input-group-addon" style="width:20px; text-align: left;">会员名称：</span> 
							<input id="memberName" name="memberName" value="${memberName}" type="text" class="form-control" style="width: 150px;"> <!-- <span class="glyphicon glyphicon-star"></span> -->
						</div>
					</td>
					
					<td width="10%" colspan="3">
						<div class="input-group" style="padding-left:10px;">
							<span class="input-group-addon" style="width:20px; text-align: left;">衣服条码：</span> 
							<input id="barCode" name="barCode" value="${barCode}" type="text" class="form-control" style="width: 150px;"> <!-- <span class="glyphicon glyphicon-star"></span> -->
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
				<table class="table table-bordered  table-hover" id="datatable">
					<thead>
						<tr>
						    <th style="display:none" class='text-center' data-col-class='text-center' data-width='30%' class='text-primary'>序号</th>
							<th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>门店</th>
							<th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>会员名称</th>
							<th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>会员手机号</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>订单类型</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>封签号</th>
							<th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>订单编号</th>
							<th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>订单日期</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>订单状态</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>衣物数量</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>应收金额</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>付款状态</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>已支付金额</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>未支付金额</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>操作</th>
							
						</tr>
					</thead>
					<!-- 同步表格 -->
					<tbody>
 						<!--  PageHelper -->
						<c:forEach items="${page.list}" var="order">
							<tr>
							    <td style="display:none">${order.id}</td>
							    <td>${order.storeName}</td>
							    
							    <td>${order.memberName}</td>
							    <td>${order.mobilePhone}</td>
							    <td>
							    	<c:if test="${order.orderType=='0'}">门店订单</c:if>
							    	<c:if test="${order.orderType=='2'}">
							    		<c:if test="${order.appId=='wx614b63f73b55d7ef'}">
							    			萨维亚订单
							    		</c:if>
							    		<c:if test="${order.appId=='wxc6097b5bf9ba801d'}">
							    			浣衣坊订单
							    		</c:if>
							    	</c:if>
							    </td>
							    <td>${order.sealNumber}</td>
							    <td>${order.orderCode}</td>
							    <td><fmt:formatDate value="${order.orderDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							    <td>${order.orderStatus}</td>
							    <td>${order.clothesCount}</td>
							    <td>${order.receivableAmount}</td>
							    
							    <td>${order.payStatus}</td>
							    <td>${order.paidAmount}</td>
							    <td>${order.receivableAmount-order.paidAmount}</td>
							    <td>
							    	<a href="javascript:void(0)" onclick="print('${order.id}')">打印</a> | 
							    	<a href="javascript:void(0)" onclick="showDetail('${order.id}')">查看详情</a>
							    </td>
							</tr>
						</c:forEach>
					</tbody>
					
					<tfoot>
						<td align="right" colspan="14">		
						<!--  PageHelper -->	
						<jsp:include page="../../inc/page_PageHelper.jsp">
								<jsp:param name="path" value="/orderQuery" />
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
		    	location.href = '<%=basePath%>/orderQuery/list?'+paras+pageParas;
		    },
		    doSearch: function(){
		    	var paras = $("#formId").serialize();
		    	location.href = '<%=basePath%>/orderQuery/list?'+paras;
		    },
		    resetSearch : function(){
		    	location.href = '<%=basePath%>/orderQuery/list';
		    }
	}
});   
</script>
</html>