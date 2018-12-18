// JavaScript Document
$(document).ready( function (){
	//定位默认窗口
	var wHi = $(window).height();
	var mHi = $(".header").height();
	var mianHi =  wHi - mHi - 26  + "px"
	var mianRHi =  wHi - mHi - 26  + "px"
	var auditHi = wHi - 70 + "px"
	$(".autoheight").css({height: mianHi});
	$(".autoH").css({height: mianRHi});
	$(".autobyH").css({height: auditHi});
	if ($.browser.msie && ($.browser.version == "6.0") ) { 
		$(".autoheight").css({height: mianHi});
		$(".autoH").css({height: mianRHi});
		$(".autobyH").css({height: auditHi});
	};
	
	
});

$(window).resize(function() {
	//随窗口变化而变化
	var wHi = $(window).height();
	var mHi = $(".header").height();
	var mianHi =  wHi - mHi - 26  + "px"
	var mianRHi =  wHi - mHi - 26  + "px"
	var auditHi = wHi - 70 + "px"
	$(".autoheight").css({height: mianHi});
	$(".autoH").css({height: mianRHi});
	$(".autobyH").css({height: auditHi});
	if ($.browser.msie && ($.browser.version == "6.0") ) { 
		$(".autoheight").css({height: mianHi});
		$(".autoH").css({height: mianRHi});
		$(".autobyH").css({height: auditHi});
	};
	
});



$(function(){
	$(".cxjg_tab tr").each(function(k) {  
         var className = ["odd", "even"][k % 2];  
         $(this).addClass(className);  
        $(this).hover( 
		function () { 
			$(this).addClass("highlight");   //鼠标经过时增加样式highlight
		}, 
		function () { 
			$(this).removeClass("highlight"); //鼠标离开时移除样式highlight
		})
    }); 
	
	$(".cxjg_tab th div:last").addClass("no_back");

	$(".bar").toggle(
	function(){
		$(this).addClass("bar_cur");
		//$(".side").hide(0);
		$(".side").css({width: "0px"})
		// $(".right_wrap").css({marginLeft: "0px"});
	},
	function(){
		$(this).removeClass("bar_cur");
		//$(".side").show(0);
		$(".side").css({width:"190px"})
		// $(".right_wrap").css({ marginLeft: "210px"});
	});	
	
	$(".nav_list li:first").addClass("nav_01");
	$(".nav_list li:last").addClass("nav_02");
	var $div_li =$(".cx_tab .nav_list li");
	    $div_li.click(function(){
			$(this).addClass("current").siblings().removeClass("current");  //当前div元素高亮,去掉其它同辈div元素的高亮
            var index =  $div_li.index(this); //获取当前点击的div素 在 全部div元素中的索引。
			//alert(index);
			$(".nav_tab > .nav_con")   	//选取子节点。不选取子节点的话，会引起错误。如果里面还有其它元素
					.eq(index).show()   //显示 <li>元素对应的<p>元素
					.siblings().hide(); //隐藏其它几个同辈的<p>元素
		})
});

$(document).ready(function(){
	
	var $div_li =$(".sys_style span");
	    $div_li.click(function(e){
			$(this).addClass("current").siblings().removeClass("current");  //当前div元素高亮,去掉其它同辈div元素的高亮
		})
		

	
});