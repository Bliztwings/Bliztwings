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
	<title>会员订单</title>

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
	    location.href = encodeURI("<%=basePath%>/storeBusiness/memberOrder?"+params);
	}

	function print(id){
		art.dialog.alert("打印待开发...");
	}
	
	// 取衣服操作
	function takeClothes(orderCode){
		
		art.dialog.open( '<%=basePath%>/storeBusiness/takeClothes?orderCode='+orderCode, {
			title : '取衣',
			width : '100%',
			height : 800,
			lock : false,
			close:function (){
				query();						
			}
		});
	}
	
	// 结账取衣
	function doPayment(orderCode,memberId,cardNumber){
		var arrears=1;
		// 判断是否办卡，没有办卡，只能选择现金支付
		art.dialog.open( '<%=basePath%>/storeBusiness/checkout?cardNumber='+cardNumber+'&memberId='+memberId+'&orderCode='+orderCode+'&arrears='+arrears, {
			title : '选择结算方式',
			width : 500,
			height : 200,
			lock : false,
			close:function (){
				query();						
			}
		});
		
	}
	
</script>
</head>

<!-- 异步表格   <body onload="ajaxReload()"> -->
<body id="content">

	<div class="nav_tit">
		<em>会员订单</em>
	</div>
	<!-- 搜索条件 -->
	<form id="formId" name="formId" class="form-horizontal" role="form" method='get' action="<%=basePath%>/storeBusiness/memberOrder">
		
		<input type="hidden" id="cardNumber" value="${member.cardNumber}"/>
		<input type="hidden" id="memberId" value="${member.memberId}"/>
		<input type="hidden" id="orderCode" value="${order.orderCode}"/>
		
		<div  style="padding-left: 10px; margin-top: 5px;">
			<table cellpadding="0" cellspacing="0" border="0" width="100%" >
				<tr>
					<td width="10%">
						<div class="input-group" style="margin-left:10px; ">
							<input id="queryKey" placeholder="手机号或者姓名"  name="queryKey" value="${queryKey}" type="text" class="form-control" style="width: 150px;">
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
				<table class="table table-bordered  table-hover" id="datatable">
					<thead>
						<tr>
						    <th style="display:none" class='text-center' data-col-class='text-center' data-width='30%' class='text-primary'>序号</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>门店名称</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>会员名称</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>会员手机号</th>
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
							    <td class='text-center'>${order.storeName}</td>
							    <td class='text-center'>${order.memberName}</td>
							    <td class='text-center'>${order.mobilePhone}</td>
							    <td class='text-center'>${order.orderCode}</td>
							    <td class='text-center'><fmt:formatDate value="${order.orderDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							    <td class='text-center'>${order.orderStatus}</td>
							    <td class='text-center'>${order.clothesCount}</td>
							    <td class='text-center'>${order.receivableAmount}元</td>
							    <td class='text-center'>
							    	<c:if test="${order.payStatus=='0'}"><font color="red">未付款</font></c:if>
							    	<c:if test="${order.payStatus=='1'}"><font color="green">已付款</font></c:if>
							    	<c:if test="${order.payStatus=='2'}"><font color="red">欠款洗涤</font></c:if>
							    </td>
							    <td class='text-center'>${order.paidAmount}元</td>
							    <td class='text-center'>${order.receivableAmount-order.paidAmount}元</td>
							    <td class='text-center'>
							    	<c:if test="${order.orderStatus !='完成' && order.payStatus=='1'}">
							    		<a href="javascript:void(0)" onclick="takeClothes('${order.orderCode}')">取衣</a>
							    	</c:if>
							    	
							    	<c:if test="${order.payStatus=='0' || order.payStatus=='2'}">
							    		<a href="javascript:void(0)" onclick="doPayment('${order.orderCode}','${order.memberId}','${order.cardNumber}')">支付</a>
							    	</c:if>
							    </td>
							</tr>
						</c:forEach>
					</tbody>
					
					<tfoot>
						<td align="right" colspan="13">		
						<!--  PageHelper -->	
						<jsp:include page="../../inc/page_PageHelper.jsp">
								<jsp:param name="path" value="/storeBusiness" />
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