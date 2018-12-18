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
	<title>门店收衣</title>

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
	
	// 查询订单
	function takeClothesQuery(){
		var params = $("#formId").serialize();
	    location.href = "<%=basePath%>/storeBusiness/takeClothes?"+params;
	}
	
	// 取衣服
	function takeClothes(){
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
			art.dialog.alert('请选择要取走的衣服!');
			return;
		}
		
		// 判断衣服是支付
		var payStatus = $("#payStatus_h").val();
		
		if(payStatus=='0' || payStatus =='2'){
			art.dialog.alert('定单未支付，不能取衣!');
			return;
		}
		
		jQuery.ajax({
			url : "<%=basePath%>/storeBusiness/doTakeClothes",
			type : "post",
			cache : false,
			dataType : "json",
			data : {'ids':arrays,'orderCode':$("#orderCodeStr").text()},
			success : function(data, textStatus) {
				if (data.resultCode == "0") {
					art.dialog.alert(data.resultMsg);
				} else {
					art.dialog.alert(data.resultMsg,function(){
						art.dialog.close();
					});
				}
			}
		});
	}
	var flag=1;
	function checkAll(){
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
	
</script>
</head>

<!-- 异步表格   <body onload="ajaxReload()"> -->
<body id="content">
	<div class="nav_tit">
		<em>取衣</em>
	</div>
	<!-- 搜索条件 -->
	<form id="formId" name="formId" class="form-horizontal" role="form" method="post">
	<input type="hidden" id="payStatus_h" value="${order.payStatus}"/>
	<input type="hidden" id="cardNumber" value="${member.cardNumber}"/>
	<input type="hidden" id="memberId" value="${member.memberId}"/>
	<input type="hidden" id="orderCode" value="${order.orderCode}"/>
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
			</table>
			<div style="color: blue;font-size: 15px;position: relative;margin-top: 20px;">【会员信息】</div>
			<table class="table datatable table-bordered" cellpadding="0" cellspacing="0" border="1" width="100%">
				<tr>
					<td width="10%" align="right">姓名:</td>
					<td width="100px" align="left">${member.memberName}</td>

					<td width="10%" align="right">手机号:</td>
					<td width="100px" align="left">${member.mobilePhone}</td>

					<td width="10%" align="right">会员类别:</td>
					<td width="100px" align="left"></td>
				</tr>

				<tr>
					<td width="10%" align="right">会员卡号:</td>
					<td width="100px" align="left">${member.cardNumber}</td>

					<td width="10%" align="right">卡状态:</td>
					<td width="100px" align="left">${member.cardStatus}</td>

					<td width="10%" align="right">卡余额:</td>
					<td width="100px" align="left">${member.totalAmount}元</td>
				</tr>
			</table>
		</div>
	<br style="border: 1px;border-color: fff"/>
	<div style="display: inline;">
		<div style="color: blue;font-size: 15px;position: relative;margin-top: 15px;margin-left: 10px;float: left;">【衣物清单】</div>
		<div class="input-group" style="padding-left: 10px;float: left;">
			<button class="btn btn-primary" type="button" id="transLogQueryBtn" onclick="takeClothes()" style="margin-right: 10px;">取  衣</button>
		</div>
	</div>
	<article>
		<div class="segment">
			<div style="padding-left: 10px; padding-right: 10px;">
				<table class="table datatable table-bordered  table-hover" id="datatable">
					<thead>
						<tr>
							<th class='text-center' data-col-class='text-center' data-width='10%'><input type="checkbox" onclick="checkAll()"></th>
							<th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>衣物条码</th>
							<th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>服务类型</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>衣物名称</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>颜色</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>品牌</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>瑕疵</th>
							
							<th class='text-center' data-col-class='text-center' data-width='10%'>衣物状态</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>挂衣区</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>挂衣号</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>送洗时间</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>上挂时间</th>
						</tr>
					</thead>
					<!-- 同步表格 -->
					<tbody>
 						<!--  PageHelper -->
						<c:forEach items="${page.list}" var="clothes">
							<tr>
								<td class='text-center'><input name="checkbox" value="${clothes.id}" type="checkbox"></td>
							    <td class='text-center'>${clothes.barCode}</td>
							    <td class='text-center'>${clothes.serviceType}</td>
							    <td class='text-center'>${clothes.clothesName}</td>
							    <td class='text-center'>${clothes.color}</td>
							    <td class='text-center'>${clothes.brand}</td>
							    <td class='text-center'>${clothes.flaw}</td>
							    <td class='text-center'>${clothes.status}</td>
							    <td class='text-center'>${clothes.handOnArea}</td>
							    <td class='text-center'>${clothes.handOnNo}</td>
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