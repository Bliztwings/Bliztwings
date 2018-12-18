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
			+ path + "data";
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>基础数据维护</title>

	<link type="text/css" href="<%=basePath%>/zui-1.5.0/css/zui.min.css" rel="stylesheet" />
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/jquery/jquery.js"></script>
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/jquery/tabScript.js"></script>
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
		
		a{
			background-color: #ddeedd;
		}
		
}
		
</style>


<script type="text/javascript">
	//$('#datatable').datatable({fixedHeaderOffset: 41, checkable: true});
	jQuery(document).ready(function(){
		$("#dataType").val("1");
		choose(1);
	});
	
	function reload(){
		var dataType =$("#dataType").val();
		choose(dataType);
	}
	
	// 清空查询参数
	function resetQuery(){
		/* $("#storeCode").val();
		$("#storeName").val(); */
		location.href = "<%=basePath%>/data/list";
	}
	
	//  查询
	function query(){
		<%-- var params = $("#formId").serialize();
	    location.href = "<%=basePath%>/data/list?"+params; --%>
	    var dataType =$("#dataType").val();
	    choose(dataType);
	}
	
	//  新增
	function add(obj){
		var dataType =$("#dataType").val();
		art.dialog.open( '<%=basePath%>/data/initAdd?dataType='+dataType, {
			title : '新增',
			width : 600,
			height : 500,
			lock : true,
			close:function (){
				query();
			}
		});
	}
	
	function preEdit(id){
		art.dialog.open( '<%=basePath%>/data/preUpdate?id='+id, {
			title : '修改',
			width : 600,
			height : 500,
			lock : true,
			close:function (){
				query();
			}
		});
	}
	
	// 监听文本值
	function monitorText(){
		var dataType =$("#dataType").val();
		choose(dataType);
	}
	
	function choose (type){
		$("#dataType").val(type);
		$("choose"+type).addClass("active");
		var dataName =$("#dataName").val();
		// ajax 请求数据
		jQuery.ajax({
			url : "<%=basePath%>/data/queryDataByType",
			type : "post",
			cache : false,
			dataType : "json",
			data : {'dataType':type,'dataName':dataName},
			success : function(data, textStatus) {
				//动态生成表格
				$("#showData").html();
				var tableStr ='';
				
				tableStr+='<table class="table datatable table-bordered  table-hover"  id=datatable">';
				tableStr+='<thead>';
				tableStr+='<tr>';
				tableStr+='<td  align="center">属性名称</td>';
				if($("#dataType").val()=='1'){
					tableStr+='<td  align="center">单烫价格</td>';
					tableStr+='<td  align="center">普洗价格</td>';
					tableStr+='<td  align="center">尚品洗涤</td>';
					tableStr+='<td  align="center">O2O洗涤</td>';
					tableStr+='<td  align="center">图片</td>';
				}
				
				tableStr+='<td  align="center">操作</td>';
				tableStr+='</tr>';
				// 遍历内容
				
				if(data.dataList!=null){
					for(var i=0;i<data.dataList.length;i++){
						tableStr+='<tr>';
						tableStr+='<td align="center">'+data.dataList[i].dataName+'</td>';
						
						// 衣服，有价格设置
						if($("#dataType").val()=='1'){
							tableStr+='<td align="center">'+data.dataList[i].price+'元'+'</td>';
							tableStr+='<td align="center">'+data.dataList[i].commonPrice+'元'+'</td>';
							tableStr+='<td align="center">'+data.dataList[i].luxuriesPrice+'元'+'</td>';
							if(data.dataList[i].o2oPrice==null || data.dataList[i].o2oPrice=='null'){
								tableStr+='<td align="center">0元'+'</td>';
							}
							else {
								tableStr+='<td align="center">'+data.dataList[i].o2oPrice+'元'+'</td>';
							}
							tableStr+='<td align="center"><a target="_blank" href="'+data.dataList[i].imagePath+'"><img style="width: 30px;height: 30px" src="'+data.dataList[i].imagePath+'" alt="" /></a></td>'
						}
						
						tableStr+='<td align="center">';
						tableStr+='<a style="background-color: #ffffff" onclick=deleteData("' + data.dataList[i].id +  '");>删除&nbsp;|&nbsp;</a>';
						tableStr+='<a style="background-color: #ffffff" onclick=preEdit("' + data.dataList[i].id +  '");>修改</a>';
						tableStr+='</td>';
						tableStr+='</tr>';
					}
				}
				tableStr+='</thead>';
				tableStr+='</table>';
				$("#showData").html(tableStr);
			}
		});
	}
	
	
	function deleteData(id){
		
		art.dialog.confirm('确实要删除吗？', function () {
			jQuery.ajax({
				url : "<%=basePath%>/data/deleteData",
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
		<em>属性维护</em>
	</div>
	<!-- 搜索条件 -->
	
	<div style="position: relative;margin-bottom: 10px;">
		<form id="formId" name="formId" class="form-horizontal" role="form" method='get' action="<%=basePath%>/data/queryDataByType">
			<input id="dataType" type="hidden" name="dataType" value=""/>
			<input type="hidden" id="choose" value=""/>
			<div  style="padding-left: 10px; margin-top: 5px;">
				<table cellpadding="0" cellspacing="0" border="0" width="100%" >
					<tr>
						<td width="10%">
							<div class="input-group" >
								<span class="input-group-addon" style="width:20px; text-align: left;">属性名称：</span> 
								<input id="dataName" name="dataName" value="${dataName}" onkeyup="monitorText()"  type="text" class="form-control" style="width: 150px;"> <!-- <span class="glyphicon glyphicon-star"></span> -->
							</div>
						</td>
						<td width="100px" align="left">
							<div class="input-group" style="padding-left: 10px;">
								<button class="btn btn-primary" type="button" id="transLogQueryBtn" onclick="query()" style="margin-right: 10px;">查  询</button>
								<!-- <button class="btn btn-primary" type="button" id="reloadDatatable" onclick="resetQuery()" style="margin-right: 10px;">重   置</button> -->
								<button class="btn btn-primary" type="button" id="reloadDatatable" onclick="add(this)">新   增</button>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</form>
	</div>
	
	<div style="position: relative;margin-left: 10px;width: 58%;">
		<ul class="nav nav-tabs" id="myTab" >
			<li id="choose1" class="active"><a href="#identifier" data-toggle="tab" onclick="choose(1)">衣服名称</a></li>
			<li id="choose2"><a href="#identifier" data-toggle="tab" onclick="choose(2)">颜色</a></li>
			<li id="choose3"><a href="#identifier" data-toggle="tab" onclick="choose(3)">品牌</a></li>
			<li id="choose4"><a href="#identifier" data-toggle="tab" onclick="choose(4)">瑕疵</a></li>
			<li id="choose5"><a href="#identifier" data-toggle="tab" onclick="choose(5)">附件</a></li>
		</ul>
	</div>
	<div>
		<article>
			<!-- 表格-->
			<div class="segment">
				<div style="padding-left: 10px; padding-right: 10px;width: 60%;overflow-x: auto; overflow-y: auto; height: 330px;" id ="showData"></div>
			</div>
		</article>
	</div>
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
			    	location.href = '<%=basePath%>/data/list?'+paras+pageParas;
			    },
			    doSearch: function(){
			    	var paras = $("#formId").serialize();
			    	location.href = '<%=basePath%>/data/list?'+paras;
			    },
			    resetSearch : function(){
			    	location.href = '<%=basePath%>/data/list';
			    }
		}
	});   
</script>
</html>