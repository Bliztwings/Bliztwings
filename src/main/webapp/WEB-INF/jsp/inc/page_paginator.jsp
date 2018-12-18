<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%
	String paramPath = request.getParameter("path");
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
	String realPath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + paramPath;
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title></title> <%-- <link type="text/css" href="<%=basePath%>/zui/docs/css/zui.min.css" rel="stylesheet" />
<script type="text/javascript" src="<%=basePath%>/zui/assets/jquery.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/zui/dist/js/zui.min.js"></script> --%>

</head>

<script type="text/javascript">
	
    /**同步刷新页面*/
	function reload() {
		document.getElementById("formId").submit();
	}
	
    /**异步刷新页面*/
	function async_reload() {
		ajaxReload();
	}
    
/* 	$(document).ready(function(){
		ajaxReload();
	});  */
	
	function ajaxReload(){
		
	    var params = $('#formId').serializeObject();
	    var result = false;
		
		jQuery.ajax({
		    url:'<%=realPath%>',
		    type:"get",
		    cache:false,
		    async:false,
		    dataType: "json",
		    contentType: "application/json; charset=utf-8",
		    data: params,
		    success:function(data) {
		    	showResponse(data);
/*                 if(jQuery.parseJSON(data)){
              	  var obj = jQuery.parseJSON(data); 
              	  if(data == undefined  || data.currentNumber== undefined ){
              		  alert("请求数据发生错误！");
              	  }else{	                	
              		  showResponse(data);
              	  }
              	}else{
              		alert("请求数据发生错误！");
              	} */
		    },
		    error:function(XMLHttpRequest, textStatus, errorThrown) {
		    	
		        alert("请求数据发生错误！XMLHttpRequest = "+ XMLHttpRequest + " textStatus = "+ textStatus + " errorThrown = " + errorThrown);

		    }
		});
		
/* 		var options = {
				type:"get",
	            dataType:"text/xml",  
	            
	            success : function(data,status) {
	                if(jQuery.parseJSON(data)){
	                	  var obj = jQuery.parseJSON(data); 
	                	  if(obj == undefined  || obj.currentNumber== undefined ){
	                		  alert("请求数据发生错误！");
	                	  }else{	                	
	                		  showResponse(obj.resultData);
	                	  }
	                }else{
	                	alert("请求数据发生错误！");
	                }
	            }  
	        };
			 $("#formId").ajaxSubmit(options);  */ 
		     return false;  
    }
	
	function removeOne(id){
		
	    var result = false;
		
		jQuery.ajax({
		    url:'<%=realPath%>/',
		    type:"get",
		    cache:false,
		    async:false,
		    dataType: {"id":id},
		    contentType: "application/json; charset=utf-8",
		    data: params,
		    success:function(data) {
		    	showResponse(data);
/*                 if(jQuery.parseJSON(data)){
              	  var obj = jQuery.parseJSON(data); 
              	  if(data == undefined  || data.currentNumber== undefined ){
              		  alert("请求数据发生错误！");
              	  }else{	                	
              		  showResponse(data);
              	  }
              	}else{
              		alert("请求数据发生错误！");
              	} */
		    },
		    error:function(XMLHttpRequest, textStatus, errorThrown) {
		    	
		        alert("请求数据发生错误！XMLHttpRequest = "+ XMLHttpRequest + " textStatus = "+ textStatus + " errorThrown = " + errorThrown);

		    }
		});
		su
		return false;
	}
	



	
	// 页面加载完成之后初始化分页样式

	
	// 选择每页显示条数
	function changePageSize() {
		var pageNum = $("#pageNum").val();
		var pageSize = $("#pageSize").val();
		if (pageNum == pageSize) {
			return;
		} else {
			changeSelectedList();
			$("#pageSize").val(pageNum);
			ajaxReload();
		}

	}
	// 第一页
	function firstPage() {
		var pageNo = $("#currentNumber").val();
		var pageCount = $("#pageCount").val();
		if (pageNo <= 1) {
			return;
		} else {
			changeSelectedList();
			$("#currentNumber").val(1);
			ajaxReload();
		}
	}
	// 上一页
	function prePage() {
		var pageNo = $("#currentNumber").val();
		var pageCount = $("#pageCount").val();
		if (pageNo <= 1) {
			return;
		} else {
			changeSelectedList();
			$("#currentNumber").val(pageNo - 1);
			ajaxReload();
		}
	}
	// 下一页
	function nextPage() {
		var pageNo = $("#currentNumber").val();
		var pageCount = $("#pageCount").val();
		if (pageNo - pageCount >= 0) {
			return;
		} else {
			changeSelectedList();
			$("#currentNumber").val(Number(pageNo) + 1);
			ajaxReload();
		}
	}
	// 最后一页
	function lastPage() {
		var pageNo = $("#currentNumber").val();
		var pageCount = $("#pageCount").val();
		if (pageNo - pageCount >= 0) {
			return;
		} else {
			changeSelectedList();
			$("#currentNumber").val(pageCount);
			ajaxReload();
		}
	}
	// 输入页面进行跳转
	function selectPage() {
		var num = $("#inputPage").val();
		var isNum = forcheck(num);
		var pageNo = $("#currentNumber").val();
		var pageCount = $("#pageCount").val();
		if (!isNum) {
			alert("页码请输入正整数！");
			return;
		}
		if (num == pageNo) {
			return;
		}
		if (num - pageCount > 0) {
			alert("输入的数字不能超过最大页数！");
			return;
		}
		changeSelectedList();
		$("#currentNumber").val(num);
		ajaxReload();

	}
	// 对一个数字进行判断是否为正整数
	function forcheck(number) {
		var type = "^[0-9]*[1-9][0-9]*$";
		var re = new RegExp(type);
		if (re.test(number)) {
			return true;
		} else {
			return false;
		}
	}

	// 复选框全选
	function checkAll() {
		var pkids = document.getElementsByName("pkidList");
		for ( var j = 0; j < pkids.length; j++) {
			if (pkids.item(j).checked == false) {
				pkids.item(j).checked = true;
			}
		}
		changeTrColor();
	}

	// 复选框全不选
	function uncheckAll() {
		var pkids = document.getElementsByName("pkidList");
		for ( var j = 0; j < pkids.length; j++) {
			if (pkids.item(j).checked == true) {
				pkids.item(j).checked = false;
			}
		}
		changeTrColor();
	}
	// 单选的复选框
	function ckeckboxChange(id) {
		
		var pkids = document.getElementsByName("pkidList");
		var sel=pkids.item(id).checked;
		for ( var j = 0; j < pkids.length; j++) {
			pkids.item(j).checked = false;
		}
		pkids.item(id).checked = sel;
	}

	// 复选框 反选
	function switchAll() {
		var pkids = document.getElementsByName("pkidList");
		for ( var j = 0; j < pkids.length; j++) {
			pkids.item(j).checked = !pkids.item(j).checked;
		}
		changeTrColor();
	}

	// 读取复选框的所有选中的checkbox
	function getPkids() {
		var pks = "";
		var pkids = document.getElementsByName("pkidList");
		for ( var j = 0; j < pkids.length; j++) {
			if (pkids.item(j).checked == true) {
				if (pks == "") {
					pks = pkids.item(j).value;
				} else {
					pks = pks + "," + pkids.item(j).value;
				}
			}
		}
		return pks;
	}

	// 获得当前页面所选的列表。
	function getCurrentSelected() {
		var pks = "";
		var pkids = document.getElementsByName("pkidList");
		for ( var j = 0; j < pkids.length; j++) {
			if (pkids.item(j).checked == true) {
				if (pks == "") {
					pks = pkids.item(j).value;
				} else {
					pks = pks + "," + pkids.item(j).value;
				}
			}
		}
		return pks;
	}

	


	// 清楚所有已选
	function clearSelected() {
		uncheckAll();
	}

	// 将当前要删除的复选框去掉。
	function clearSelectedByPK(pk) {
		var pkids = document.getElementsByName("pkidList");
		for ( var j = 0; j < pkids.length; j++) {
			if (pkids.item(j).value == pk) {
				pkids.item(j).checked = false;
			}
		}
		changeSelectedList();
	}

	function delAjax(urlAddress) {
		jQuery.ajax({
			url : urlAddress,
			type : "GET",
			cache : false,
			async : false,
			dataType : "json",
			success : function(json, textStatus) {
				if (json.code == "0") {
					alert(json.message);
				} else {
					alert(json.message);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert("删除数据时出现异常！");
			}
		});
		reload();

	}
	function delAjaxWithoutReload(urlAddress) {
		jQuery.ajax({
			url : urlAddress,
			type : "GET",
			cache : false,
			async : false,
			dataType : "json",
			success : function(json, textStatus) {
				if (json.code == "0") {
					alert(json.message);
				} else {
					alert(json.message);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert("删除数据时出现异常！");
			}
		});
	}	
	
	
	</script>

<body>


	<div contenteditable="false" spellcheck="false" class="segment"
		align="right"
		style="padding-right: 10px; padding-top: 0px; padding-bottom: 0px; margin-top: 0px;">

		<input id="pageSize" name="pageSize" type="hidden"
			value="${listPaginator.limit}" /> <input id="pageNum" name="pageNum"
			type="hidden" value="${listPaginator.prePage}" />


		<ul class="pager" style="margin: 0 0 0 0;">
			<li class="previous"><a href="#" style="cursor: default;"> 共
					${listPaginator.totalCount}条记录,${listPaginator.page}/${listPaginator.totalPages}</a></li>

			<c:if test="${!listPaginator.firstPage}">
				<li class="previous "><a
					href="<%=realPath%>/?pageNum=1&pageSize=${listPaginator.limit}">«
						首页</a></li>
			</c:if>
			<c:if test="${listPaginator.firstPage}">
				<li class="previous disabled"><a
					href="<%=realPath%>/?pageNum=1&pageSize=${listPaginator.limit}">«
						首页</a></li>
			</c:if>

			<c:if test="${listPaginator.hasPrePage}">
				<li class="previous"><a
					href="<%=realPath%>/?pageNum=${listPaginator.prePage}&pageSize=${listPaginator.limit}">«
						上一页</a></li>
			</c:if>
			<c:if test="${!listPaginator.hasPrePage}">
				<li class="previous disabled"><a
					href="<%=realPath%>/?pageNum=${userListPaginator.prePage}&pageSize=${listPaginator.limit}">«
						上一页</a></li>
			</c:if>

			<c:if test="${listPaginator.hasNextPage}">
				<li class="previous"><a
					href="<%=realPath%>/?pageNum=${listPaginator.nextPage}&pageSize=${listPaginator.limit}">下一页
						»</a></li>
			</c:if>
			<c:if test="${!listPaginator.hasNextPage}">
				<li class="previous disabled"><a
					href="<%=realPath%>/?pageNum=${listPaginator.nextPage}&pageSize=${listPaginator.limit}">下一页
						»</a></li>
			</c:if>

			<c:if test="${!listPaginator.lastPage}">
				<li class="previous"><a
					href="<%=realPath%>/?pageNum=${listPaginator.totalPages}&pageSize=${listPaginator.limit}">末页
						»</a></li>
			</c:if>
			<c:if test="${listPaginator.lastPage}">
				<li class="previous disabled"><a
					href="<%=realPath%>/?pageNum=${listPaginator.totalPages}&pageSize=${listPaginator.limit}">末页
						»</a></li>
			</c:if>
		</ul>
	</div>
</body>
</html>