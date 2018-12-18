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
			+ path + "security/operLog";
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>操作日志列表</title>

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
    <link rel="stylesheet" href="<%=basePath%>/zui-1.5.0/lib/datetimepicker/datetimepicker.min.css">
    <script src="<%=basePath%>/zui-1.5.0/lib/datetimepicker/datetimepicker.min.js"></script>
    
    <!-- vue1.0 -->
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/js/vue1.0/iview.css">
    <script type="text/javascript" src="<%=basePath%>/js/vue1.0/vue.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/js/vue1.0/vue-resource.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/js/vue1.0/iview.min.js"></script>
 
<style type="text/css">
.nav_tit{height:40px;font:14px/40px 'Microsoft Yahei';color:#000;padding-left:25px;background-image: url(<%=basePath%>/common/images/public/next.gif);background-repeat: no-repeat;
	background-position: 10px 13px;background-color: #EDF6FA;border-bottom: 1px double #D7E4EA;}
.nav_tit em{height:40px;font:14px/40px 'Microsoft Yahei';color:#000;float:left;}
</style>


<script type="text/javascript">

//$('#datatable').datatable({fixedHeaderOffset: 41, checkable: true});
jQuery(document).ready(function(){
	
	// 选择时间和日期
    $(".form-datetime").datetimepicker({
        weekStart: 1,
        todayBtn:  1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        forceParse: 0,
        showMeridian: 1,
        format: "yyyy-mm-dd hh:ii:ss"
    });
	
});


// 搜索查询
function query(){
	var params = $("#formId").serialize();
	location.href = "<%=basePath%>/operLog/list?"+params;
}

// 清空查询参数
function resetQuery(){
	$("#userName").val();
	$("#operObject").val();
	$("#operStartTime").val();
	$("#operEndTime").val();
	location.href = "<%=basePath%>/operLog/list";
}

/**
 * 删除
 */
 function remove (obj) {
	var ids =  getCheckboxIds(obj , 0)
	if(!ids ){
		return false;
	}

	art.dialog.confirm('确认要删除？', function () {
		jQuery.ajax({
	        url:"<%=basePath%>/operLog/delete",
	        type:"POST",
	        cache:false,
	        async:true,
	        dataType: "json",
	        data: {"ids": ids},
	        success:function(data, textStatus, jqXHR) {
	        	 if(data){
                	  if(data.resultCode == 1){
	                    	art.dialog.alert(data.resultMsg,function(){
	                    		reload();
	                    	});
                	  }else {
                		  	art.dialog.alert(data.resultMsg); 
	      		            return false;
                	  }
	        	 }
	        },
	        error:function(XMLHttpRequest, textStatus, errorThrown) {		        	
	        	art.dialog.alert(XMLHttpRequest.resultMsg); 
	    		return false;
	        }
	    });		
	}, function () {
	});
}

/**
 * 获得所有选中记录行的ID值
 *     中间用 ， 分割
 *
 * elmentIndex 控件下标值
 */
function getCheckboxIds(obj,elmentIndex){
	
	//获取数据表格实例对象
	var myDatatable = $('table.datatable').data('zui.datatable');
	// 获取行选中数据
	var checksObj = myDatatable.checks;
	if(null == checksObj  ){
		art.dialog.alert("您没有选择任何记录！");
		return false;
	}else{
		checksObj = checksObj.checks;
	}
	var checkIds = "";
	if( null != checksObj && checksObj instanceof Array ){
		if(checksObj.length < 1 ){
			art.dialog.alert("您必须至少选中一条记录！");
			return false;
		}else {
			for ( var i = 0; i < checksObj.length; i++) {
				checkIds = checkIds + "" + $(".table tbody tr").eq(checksObj[i]).children().eq(0).html();
				if((i+1) < checksObj.length){
					checkIds = checkIds + ",";
				}
			}			
		}
	}
	return checkIds;
}

</script>
</head>
<!-- 异步表格   <body onload="ajaxReload()"> -->
<body id="content">
<c:if test="${resultCode == 1}">
   <script type="text/javascript">
   
   		art.dialog.alert('${resultMsg}',function(){
   			window.location.reload();
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
		<em>操作日志</em>
	</div>
			<!-- 搜索条件 -->
	<form id="formId" name="formId" class="form-horizontal" role="form" method='get' action="<%=basePath%>/operLog/list">
		<div  style="padding-left: 10px; margin-top: 5px;">

			<table cellpadding="0" cellspacing="0" border="0" width="100%" >
				<tr>
					<td width="15%">
						<div class="input-group" >
							<span class="input-group-addon"
								style="width:20px; text-align: left;">用户名：</span> 
								<input id="userName" name="userName" value="${userName}"
								type="text" class="form-control  "
								 style="width: 150px;"> <span
								class="glyphicon glyphicon-star"></span>
						</div>
					</td>
					<td width="15%">
						<div class="input-group" >
							<span class="input-group-addon"
								style="width:20px; text-align: left;">操作对象：</span> 
								<input id="operObject" name="operObject" value="${operObject}"
								type="text" class="form-control  "
								 style="width: 150px;"> <span
								class="glyphicon glyphicon-star"></span>
						</div>
					</td>
					<td width="20%">
						<div class="input-group" >
							<span class="input-group-addon"
								style="width: 50px; text-align: left;">开始时间：</span> 
								<input id="operStartTime" name="operStartTime" value="${operStartTime}"
								type="text"  class="form-control form-datetime" placeholder="" 
								style="width: 135px;"> <span
								class="glyphicon glyphicon-star"></span>
						</div>
					</td>
					<td width="20%">
						<div class="input-group" >
							<span class="input-group-addon"
								style="width: 50px; text-align: left;">结束时间：</span> 
								<input id="operEndTime" name="operEndTime" value="${operEndTime}"
								type="text" class="form-control form-datetime" placeholder="" 
								style="width: 135px;"> <span class="glyphicon glyphicon-star"></span>
						</div>
					</td>
					
					<td width="30%" align="right">
						<div class="input-group" style="padding-right: 10px;">
							<button class="btn btn-primary" type="button" id="transLogQueryBtn" onclick="query()" style="margin-right: 10px;">查  询</button>
							<button class="btn btn-primary" type="button" id="reloadDatatable" onclick="resetQuery()">重   置</button>
						</div>
					</td>
				</tr>
				
			</table>
	
		</div>
<br style="border: 1px;border-color: fff"/>
<article>
		<!-- 按钮组 -->
		<div class="btn-group"
			style="padding-left: 10px; padding-bottom: 5px; padding-top: 10px;">
			<shiro:hasPermission name="operLog:delete">
			<button type="button" class="btn btn-primary" onclick="remove(this);">
				<i class="icon-remove"></i> 删除
			</button>
			</shiro:hasPermission>
		</div>
		<!-- 表格-->
		<div class="segment">
			<div id="dtFromData"></div>
			<div style="padding-left: 10px; padding-right: 10px;">
				<table class="table datatable table-bordered  table-hover" id="datatable">
					<thead>
						<tr>
						    <th style="display:none" class='text-center' data-col-class='text-center' data-width='30%' class='text-primary'>ID</th>
							<th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>用户名</th>
							<th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>用户实名</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>IP地址</th>
							<th class='text-center' data-col-class='text-center' data-width='15%'>操作时间</th>
							<th class='text-center' data-col-class='text-center' data-width='15%'>操作对象</th>
							<th class='text-center' data-col-class='text-left' data-width='40%'>日志描述</th>
						</tr>
					</thead>
					<!-- 同步表格 -->
					<tbody>
 						<!--  PageHelper -->
						<c:forEach items="${page.list}" var="operLog">
							<tr>
							    <td style="display:none">${operLog.id}</td>
							    <td>${operLog.userName}</td>
							    <td>${operLog.userRealName}</td>
							    <td>${operLog.ip}</td>
								<td><fmt:formatDate value="${operLog.operTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td>${operLog.operObject}</td>
								<td>${operLog.operDesc}</td>
							</tr>
						</c:forEach>
					</tbody>					
				</table>
			</div>
			<Page :total="pagination.total" :current="pagination.pageNum" :page-size="pagination.pageSize" @on-change="search" show-total show-elevator></Page>
		</div>
		</article>
	</form>
</body>

<script type="text/javascript">
$('table.datatable').datatable({	
	sortable: true,
	checkable: true,
	storage: false,
    //checkedClass: "checked",
	fixedHeaderOffset: 0
	
});

var total = parseInt('${page.total}');
var pageNum = parseInt('${page.pageNum}');
var pageSize = parseInt('${page.pageSize}');
//console.log("total: "+total);
//console.log("pageNum: "+pageNum);

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
		    	//console.log("nextPage: "+nextPage);
		    	var paras = $("#formId").serialize();
		    	var pageParas = '&pageNum='+nextPage+'&pageSize='+this.pagination.pageSize;
		    	//$('#formId').submit();
		    	//console.log("paras: "+paras);
		    	//console.log("pageParas: "+pageParas);
		    	location.href = '<%=basePath%>/operLog/list?'+paras+pageParas;
		    },
		    doSearch: function(){
		    	var paras = $("#formId").serialize();
		    	location.href = '<%=basePath%>/operLog/list?'+paras;
		    },
		    resetSearch : function(){
		    	location.href = '<%=basePath%>/operLog/list';
		    }
	}
});   
</script>
</html>