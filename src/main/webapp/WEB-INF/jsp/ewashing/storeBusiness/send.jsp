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
	<title>门店送洗</title>

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
	    location.href = "<%=basePath%>/storeBusiness/send?"+params;
	}
	
	// 送厂洗涤
	function send(){
		var count =0;
		var arrays ="";
		var ids=[];
		var pkids = document.getElementsByName("checkbox");
		for ( var j = 0; j < pkids.length; j++) {
			if (pkids.item(j).checked == true) {
				count+=1;
				arrays+=pkids.item(j).value+","
			}
		}
		if(count==0){
			art.dialog.alert('请选择送洗衣服!');
			return;
		}
		
		jQuery.ajax({
			url : "<%=basePath%>/storeBusiness/sendWashing",
			type : "post",
			cache : false,
			dataType : "json",
			data : {'ids':arrays},
			success : function(data, textStatus) {
				if (data.resultCode == "0") {
					art.dialog.alert(data.resultMsg);
				} else {
					art.dialog.alert(data.resultMsg,function(){
						art.dialog.close();
						query();
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
		<em>待送洗衣服</em>
	</div>
	<!-- 搜索条件 -->
	<form id="formId" name="formId" class="form-horizontal" role="form" method='get' action="<%=basePath%>/storeBusiness/send">
		
		<br style="border: 1px;border-color: fff"/>
		<article>
				<!-- 表格-->
				<div class="segment">
					<div id="dtFromData"></div>
					
					<div style="padding-right: 10px;">
						<div class="input-group" style="padding-left: 10px;padding-bottom: 5px;">
							<button class="btn btn-primary" type="button" id="transLogQueryBtn" onclick="send()" style="margin-right: 10px;">送厂洗涤</button>
							
							<button class="btn btn-primary" type="button" id="transLogQueryBtn" onclick="print()" style="margin-right: 10px;">打印</button>
						</div>
					</div>
			
					<div style="padding-left: 10px; padding-right: 10px;">
						<table class="table datatable table-bordered  table-hover" id="datatable">
							<thead>
								<tr>
									<th class='text-center' data-col-class='text-center' data-width='10%'><input type="checkbox" onclick="checkAll()"></th>
									<th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>会员名称</th>
									<th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>会员手机号</th>
									<th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>订单编号</th>
									<th class='text-center' data-col-class='text-center' data-width='15%' class='text-primary'>订单时间</th>
									<th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>衣物条码</th>
									<th class='text-center' data-col-class='text-center' data-width='10%'>衣物名称</th>
									<th class='text-center' data-col-class='text-center' data-width='10%'>服务类型</th>
									<th class='text-center' data-col-class='text-center' data-width='10%'>颜色</th>
									<th class='text-center' data-col-class='text-center' data-width='10%'>品牌</th>
									<th class='text-center' data-col-class='text-center' data-width='10%'>瑕疵</th>
									<th class='text-center' data-col-class='text-center' data-width='10%'>衣物状态</th>
								</tr>
							</thead>
							<!-- 同步表格 -->
							<tbody>
		 						<!--  PageHelper -->
								<c:forEach items="${page.list}" var="clothes">
									<tr>
										<td class='text-center'><input name="checkbox" value="${clothes.id}" type="checkbox"></td>
									    <td class='text-center'>${clothes.memberName}</td>
									    <td class='text-center'>${clothes.mobilePhone}</td>
									    <td class='text-center'>${clothes.orderCode}</td>
									    <td class='text-center'><fmt:formatDate value="${clothes.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									    <td class='text-center'>${clothes.barCode}</td>
									    <td class='text-center'>${clothes.clothesName}</td>
									    <td class='text-center'>${clothes.serviceType}</td>
									    <td class='text-center'>${clothes.color}</td>
									    <td class='text-center'>${clothes.brand}</td>
									    <td class='text-center'>${clothes.flaw}</td>
									    <td class='text-center'>${clothes.status}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</article>
	</form>
</body>

</html>