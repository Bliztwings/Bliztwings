<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>	
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>	
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	String path1 = request.getContextPath();
	String basePath1 = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path1;
%>
<div id="sidebar" class="menu-min">
	<div id="sidebar-shortcuts">
		<div id="sidebar-shortcuts-large">
			<button class="btn btn-small btn-success" onclick="changeMenu();" title="切换菜单"><i class="icon-pencil"></i></button>
			<button class="btn btn-small btn-info" title="UI实例"><i class="icon-eye-open"></i></button>
			<button class="btn btn-small btn-warning" title="数据字典" id="adminzidian" onclick="zidian();"><i class="icon-book"></i></button>
			<button class="btn btn-small btn-danger" title="菜单管理" id="adminmenu" onclick="menu();"><i class="icon-folder-open"></i></button>
		</div>
		<div id="sidebar-shortcuts-mini">
			<span class="btn btn-success"></span>
			<span class="btn btn-info"></span>
			<span class="btn btn-warning"></span>
			<span class="btn btn-danger"></span>
		</div>
	</div><!-- #sidebar-shortcuts -->

	<ul class="nav nav-list">
		<li class="active" id="fhindex">
			<a href="javascript:void(0)" onclick="showWelcome('<%=basePath1%>/welcome')"> 
				<i class="icon-dashboard"></i> <span class="menu-text">后台首页</span>
			</a>
		</li>
		
		<!-- 判断一级菜单连接为空 -->
		<c:forEach items="${securityResourceList}" var="menu">
			<c:if test="${menu.children!=null && fn:length(menu.children) > 0}">
			<li id="lm${menu.id }">
				  <a style="cursor:pointer;" class="dropdown-toggle" onclick="openMenu('${menu.id }')">
					<c:if test ="${menu.icon  == null}">
						<i class="${menu.icon == null ? 'icon-desktop' : menu.icon}"></i>					
					</c:if>
					
					<c:if test ="${menu.icon != null}">
						<img alt="" src="<%=basePath1%>/static/images/${menu.icon}">				
					</c:if>
					
					<%-- <i class="${menu.icon == null ? 'icon-desktop' : menu.icon}"></i> --%>
					
					<span>${menu.name }</span>
					<b class="arrow icon-angle-down"></b>
				  </a>
				  <ul class="submenu" id="${menu.id }" >
						<c:forEach items="${menu.children}" var="sub">
							<c:if test="${sub.children==null || fn:length(sub.children) == 0}">
							<c:choose>
								<c:when test="${not empty sub.url}">
								<li id="z${sub.id }">
								<a style="cursor:pointer;" target="mainFrame"  onclick="siMenu('z${sub.id }','lm${menu.id }','${sub.name }','<%=basePath1%>${sub.url }')"><i class="icon-double-angle-right"></i>${sub.name }</a></li>
								</c:when>
								<c:otherwise>
								<li><a href="javascript:void(0);"><i class="icon-double-angle-right"></i>${sub.name }</a></li>
								</c:otherwise>
							</c:choose>
							</c:if>
						</c:forEach>
			  		</ul>
			</li>
			</c:if>
		</c:forEach>
	</ul>
		<div id="sidebar-collapse"><i class="icon-double-angle-left"></i></div>
</div>
 
