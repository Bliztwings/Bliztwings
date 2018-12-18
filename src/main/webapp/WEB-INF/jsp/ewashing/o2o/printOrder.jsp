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
	<title>O2O订单查询</title>

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
	    location.href = encodeURI("<%=basePath%>/o2oOrder/printOrder?"+params);
	}

	// 分配订单
	function distribute(){
		var count =0;
		var arrays ="";
		var pkids = document.getElementsByName("checkbox");
		for ( var j = 0; j < pkids.length; j++) {
			if (pkids.item(j).checked == true) {
				count+=1;
				arrays+=pkids.item(j).value+","
			}
		}
		if(count==0){
			art.dialog.alert('请选择分配订单!');
			return;
		}
		var sendUserId =$("#sendUserId").val();
		if(sendUserId=='0'){
			art.dialog.alert('请选择分配的小E!');
			return;
		}
		// 进行分配动作
		jQuery.ajax({
			url : "<%=basePath%>/o2oOrder/distribute",
			type : "post",
			cache : false,
			dataType : "json",
			data : {'ids':arrays,'sendUserId':sendUserId,type:'2'},
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
		
	}
	
	var flag=1;
	function checkAllBox(){
		var pkids = document.getElementsByName("checkbox");
		if(flag==1){
			flag=2;
			for ( var j = 0; j < pkids.length; j++) {
				if (pkids.item(j).checked == false) {
					pkids.item(j).checked = true;
				}
			}
		}else{
			if(flag==2){
				flag=1;
				for ( var j = 0; j < pkids.length; j++) {
					if (pkids.item(j).checked == true) {
						pkids.item(j).checked = false;
					}
				}
			}
		}
	}
	
	// 查看订单详情
	function showDetail(orderId){
		art.dialog.open( '<%=basePath%>/orderQuery/orderDetail?orderId='+orderId, {
			title : '订单详情',
			width : '100%',
			height : 700,
			lock : true
		});
	}
	
	// 打印物流单
	function printLogistics(id){
		art.dialog.open( '<%=basePath%>/o2oOrder/printLogistics?orderId='+id, {
			title : '打印详情',
			width : 300,
			height : 450,
			lock : true
		});
	}
	
</script>
</head>

<!-- 异步表格   <body onload="ajaxReload()"> -->
<body id="content">

	<div class="nav_tit">
		<em>O2O物流单打印</em>
	</div>
	<!-- 搜索条件 -->
	<form id="formId" name="formId" class="form-horizontal" role="form" method='get' action="<%=basePath%>/o2oOrder/printOrder">
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
						<div class="input-group" >
							<span class="input-group-addon" style="width:20px; text-align: left;">手&nbsp;&nbsp;机&nbsp;号：</span> 
							<input id="mobilePhone" name="mobilePhone" value="${mobilePhone}" type="text" class="form-control" style="width: 150px;"> <!-- <span class="glyphicon glyphicon-star"></span> -->
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
							<th class='text-center' data-col-class='text-center' data-width='5%'><input type="checkbox" onclick="checkAllBox()"/></th>
							<th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>会员名称</th>
							<th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>会员手机号</th>
							<th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>订单编号</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>订单类型</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>封签号</th>
							<th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>订单日期</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>订单状态</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>衣物数量</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>应收金额</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>付款状态</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>订单地址</th>
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
								<td class='text-center'>
									<input type="checkbox" name="checkbox" value="${order.id}"/>
								</td>
							    <td class='text-center'>${order.memberName}</td>
							    <td class='text-center'>${order.mobilePhone}</td>
							    <td class='text-center'>${order.orderCode}</td>
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
							    
							    <td class='text-center'><fmt:formatDate value="${order.orderDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							    <td class='text-center'>${order.orderStatus}</td>
							    <td class='text-center'>${order.clothesCount}</td>
							    <td class='text-center'>${order.receivableAmount}</td>
							    <td class='text-center'>${order.payStatus}</td>
							    <td class='text-center'>${order.orderAddress}</td>
							    <td class='text-center'>${order.paidAmount}</td>
							    <td class='text-center'>${order.receivableAmount-order.paidAmount}</td>
							    <td class='text-center'>
							    	<c:if test="${order.printStatus=='1' }">
										<a href="javascript:void(0)" onclick="printLogistics('${order.id}')">打印</a>
									</c:if>  
									
									<c:if test="${order.printStatus!='2' }">
										<a href="javascript:void(0)" onclick="printLogistics('${order.id}')">补打</a>
							    		<a href="javascript:void(0)" onclick="showDetail('${order.id}')">订单详情</a>
									</c:if> 
									
							    </td>
							</tr>
						</c:forEach>
					</tbody>
					
					<tfoot>
						<td align="right" colspan="15">		
						<!--  PageHelper -->	
						<jsp:include page="../../inc/page_PageHelper.jsp">
								<jsp:param name="path" value="/o2oOrder" />
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
		    	location.href = '<%=basePath%>/o2oOrder/printOrder?'+paras+pageParas;
		    },
		    doSearch: function(){
		    	var paras = $("#formId").serialize();
		    	location.href = '<%=basePath%>/o2oOrder/printOrder?'+paras;
		    },
		    resetSearch : function(){
		    	location.href = '<%=basePath%>/o2oOrder/printOrder';
		    }
	}
});   
</script>
</html>