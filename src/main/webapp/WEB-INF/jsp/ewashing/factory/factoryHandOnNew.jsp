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
	<title>工厂上挂</title>

	<link type="text/css" href="<%=basePath%>/zui-1.5.0/css/zui.min.css" rel="stylesheet" />
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/jquery/jquery.js"></script>
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/js/zui.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/datatable/zui.datatable.js"></script>
	<link type="text/css" href="<%=basePath%>/zui-1.5.0/lib/datatable/zui.datatable.css" rel="stylesheet" />
	<script src="<%=basePath%>/js/jquery/jquery.form.js" type="text/javascript"></script>
	<script type="text/javascript" src="<%=basePath%>/js/jquery/jquery.json-2.2.js"></script>

	<!--  打印控件  -->
	<script language="javascript" src="<%=basePath%>/js/LodopFuncs.js"></script>

	<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0>
		<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop32.exe"></embed>
	</object>

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

	// 初始化信息
	jQuery(document).ready(function(){
		//$("#queryKey").val("");
		$("#queryKey").focus();
	});
	
	// 查询
	function query(){
		var params = $("#formId").serialize();
	    location.href = "<%=basePath%>/factory/factoryHandOnNew?"+params;
	}

	//待上挂衣服
	function waitingHandon(){
		art.dialog.open( '<%=basePath%>/factory/factoryHandOn', {
            title : '待上挂衣服',
            width : '100%',
            height : 690,
            lock : true
 		});
	}

	//查询洗衣单
	function PostXiYiDan()
	{
        try
        {
            var LODOP=getLodop();
            if (LODOP.VERSION) {
                if (LODOP.CVERSION)
                    console.log("当前有C-Lodop云打印可用!\n C-Lodop版本:"+LODOP.CVERSION+"(内含Lodop"+LODOP.VERSION+")");
                else
                console.log("本机已成功安装了Lodop控件！\n 版本号:"+LODOP.VERSION);

            };
        }
        catch(err)
        {
            alert("打印机异常，异常代码1013！！");
            return;
        }

		var params = $("#formId").serialize();

		// 执行ajax
		$.ajax({
			type: 'GET',
			url: '<%=basePath%>/factory/queryOrderInfo?'+params,
			dataType : "text",  // 返回的数据格式
			cache: false,
			timeout: 6000,
			success: function(data)
			{
				PrintXiYiDan(data);
				//console.log(data);
			},
			error: function()
			{
				//alert("ajax异常！");
				console.log('ajax异常！');
			}
		});
	}

	// 打印洗衣单
    function PrintXiYiDan(data)
	{
	    var i = 0;
        var len = 0;
		var jsonStr = data;

		len = jsonStr.length;
		if(len==0)
		{
			alert("条码不正确");
			return;
		}

		var orderCode;  //订单号
		var userName;  //用户
		var mobilePhone;  //用户手机号码
		var count;  //件数
		var barCode;  //条码
		var address;  //客户地址
		var clothesName;  //衣服名称
		var color;  //颜色
		var price;  //价格
        var priceTotal;  //总价格
		var brand;  //品牌
		var flaw;  //瑕疵
		var status;  //状态
		var storeAddress;  //本店地址
		var storeName;
		var telephone;
		var leaveMessage;  //用户留言

		var jsonObj =  JSON.parse(jsonStr);
		var jsonStr1 = JSON.stringify(jsonObj);
		var jsonArr = [];

        priceTotal = 0;
		jsonArr[0] = jsonObj[0];
		count = jsonObj.length;
		orderCode = jsonArr[0].orderCode;  //订单号
		barCode = jsonArr[0].barCode;  //条码
		userName = jsonArr[0].name;  //用户
		mobilePhone = jsonArr[0].mobilePhone;  //用户手机号码
		priceTotal = jsonArr[0].handOnNo;
		address = jsonArr[0].orderAddress;  //订单地址
		storeName = jsonArr[0].storeName;  //门店
		telephone = jsonArr[0].telephone;  //服务热线
		leaveMessage = jsonArr[0].leaveDateStr;  //用户留言

        //打印时间
        var curdate = new Date();
        var month = curdate.getMonth()+1;
        var date = curdate.getDate();
        var hours = curdate.getHours();
        var minutes = curdate.getMinutes();

        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (date >= 1 && date <= 9) {
            date = "0" + date;
        }
        if (hours >= 1 && hours <= 9) {
            hours = "0" + hours;
        }
        if (minutes >= 1 && minutes <= 9) {
            minutes = "0" + minutes;
        }

        var printdate = curdate.getFullYear()+"-"+month+"-"+date+" "+hours+":"+minutes;

        var hTop=30;
        var rowHeight=30; //行距
        var lineTop=30;
        var LODOP=getLodop();
        //打印初始化
        LODOP.PRINT_INIT("打印洗衣单");

        //获得打印设备个数
        var iCount=LODOP.GET_PRINTER_COUNT();

		//指定打印设备
        var strPName;
        for(i=0;i<iCount;i++)
        {
            strPName = LODOP.GET_PRINTER_NAME(i);
            //if(strPName=="POS-80C")
			if( strPName.indexOf("POS-80C")!=-1 )
            {
                //指定打印设备
                LODOP.SET_PRINTER_INDEX(i);
                alert("POS-80C----"+i);
            }
        }

        //打印条码
        LODOP.SET_PRINT_STYLE("FontSize",16);
        LODOP.ADD_PRINT_TEXT(hTop,75,200,60,"浣衣坊洗衣单");
        LODOP.SET_PRINT_STYLE("FontSize",10);
        hTop+=rowHeight;
        LODOP.ADD_PRINT_BARCODE(hTop,60,200,47,"128A",orderCode);
        LODOP.SET_PRINT_STYLE("FontSize",16);
        hTop+=rowHeight;
        hTop+=22;
        LODOP.ADD_PRINT_TEXT(hTop,20,280,60,"交易单号："+barCode);
        hTop+=rowHeight;
        LODOP.ADD_PRINT_TEXT(hTop,20,200,60,"件数："+count+"件");
        hTop+=rowHeight;

        LODOP.SET_PRINT_STYLE("FontSize",10);
		LODOP.ADD_PRINT_TEXT(hTop,20,240,60,"用户："+userName+"("+mobilePhone.substr(0,3)+"****"+mobilePhone.substr(7,4)+")");
		hTop+=18;
        LODOP.ADD_PRINT_TEXT(hTop,20,240,60,"订单地址："+address);
        hTop+=rowHeight;
        lineTop = hTop+10;
        LODOP.ADD_PRINT_LINE(lineTop,20,lineTop,240,2,1);
        hTop+=rowHeight-10;
        LODOP.ADD_PRINT_TEXT(hTop,20,200,60,"名称    颜色       价格");
        lineTop = hTop+10+10;
        LODOP.ADD_PRINT_LINE(lineTop,20,lineTop,240,2,1);

        //var jsonArr = [];
        for(i =0 ;i < jsonObj.length;i++)
        {
            jsonArr[i] = jsonObj[i];
            barCode = jsonArr[i].barCode;  //条码
            clothesName=jsonArr[i].clothesName;  //衣服名称
            color=jsonArr[i].color;  //颜色
            price=jsonArr[i].handOnArea;  //价格
            brand=jsonArr[i].brand;  //品牌
            flaw=jsonArr[i].flaw;  //瑕疵
            status=jsonArr[i].status;  //状态
            storeAddress = jsonArr[i].storeAddress;  //本店地址

            //priceTotal+=price;

            if(clothesName.length>4)
            {
                clothesName=clothesName.substr(0,4);
            }

            if(color.length>4)
            {
                color=color.substr(0,4);
            }

            if(brand.length==0)
            {
                brand="无品牌";
            }

            if(flaw.length==0)
            {
                flaw="无瑕疵";
            }

            hTop+=rowHeight;
            LODOP.ADD_PRINT_TEXT(hTop,20,220,60,clothesName+"    "+color+"       "+price);
            rowHeight = 16;
            hTop+=rowHeight;
            LODOP.ADD_PRINT_TEXT(hTop,20,200,60,"编号（"+barCode+"）");
            hTop+=rowHeight;
            LODOP.ADD_PRINT_TEXT(hTop,20,200,60,"品牌（"+brand+"）");
            hTop+=rowHeight;
            LODOP.ADD_PRINT_TEXT(hTop,20,200,60,"瑕疵（"+flaw+"）");
            hTop+=rowHeight;
            LODOP.ADD_PRINT_TEXT(hTop,20,200,60,"状态（"+status+"）");
            hTop+=4;
        }

        hTop+=rowHeight;
        //hTop+=4;
        LODOP.ADD_PRINT_TEXT(hTop,20,240,60,"交易总额："+priceTotal+"（附加费0.00）");

        lineTop = hTop+20;
        LODOP.ADD_PRINT_LINE(lineTop,20,lineTop,240,2,1);
        hTop+=28;
        LODOP.SET_PRINT_STYLE("FontSize",16);
        LODOP.ADD_PRINT_TEXT(hTop,20,200,60,"应收金额："+priceTotal);
        hTop+=26;
        LODOP.SET_PRINT_STYLE("FontSize",10);
        LODOP.ADD_PRINT_TEXT(hTop,20,200,60,"现金支付："+priceTotal);
        lineTop = hTop+20;
        LODOP.ADD_PRINT_LINE(lineTop,20,lineTop,240,2,1);

        rowHeight = 16;
        hTop+=10;
        hTop+=rowHeight;
        LODOP.ADD_PRINT_TEXT(hTop,20,240,60,"本店地址："+storeName);
        hTop+=rowHeight;
        LODOP.ADD_PRINT_TEXT(hTop,20,240,60,"服务热线："+telephone);
        //hTop+=rowHeight;
        //LODOP.ADD_PRINT_TEXT(hTop,20,240,60,"店员：毛海  店号：0003");
        hTop+=rowHeight;
        LODOP.ADD_PRINT_TEXT(hTop,20,240,60,"打印时间："+printdate);

		len = leaveMessage.length;
		console.log('len='+len);
		if(len>0)
		{
			hTop+=24;
			LODOP.ADD_PRINT_TEXT(hTop,20,260,60,"用户留言："+leaveMessage);
		}

        //设定纸张大小
        LODOP.SET_PRINT_PAGESIZE(3,"80mm","4.5mm","CreateCustomPage");

        //预览
        LODOP.PREVIEW();
        //直接打印
        //LODOP.PRINT();
        //选择打印机，再打印
        //LODOP.PRINTA();*/
    }

    //查询粘贴
    function PostZhanTie()
    {
		try
		{
			var LODOP=getLodop();
			if (LODOP.VERSION) {
				if (LODOP.CVERSION)
					console.log("当前有C-Lodop云打印可用!\n C-Lodop版本:"+LODOP.CVERSION+"(内含Lodop"+LODOP.VERSION+")");
				else
					console.log("本机已成功安装了Lodop控件！\n 版本号:"+LODOP.VERSION);

			};
		}
		catch(err)
		{
			alert("打印机异常，异常代码1013！！");
			return;
		}

		var params = $("#formId").serialize();

        // 执行ajax
        $.ajax({
            type: 'GET',
            url: '<%=basePath%>/factory/queryOrderInfo?'+params,
            dataType: 'json',  // 返回的数据格式
            dataType : "text",
            cache: false,
            timeout: 6000,
            success: function(data)
			{
				PrintZhanTie(data);
				//console.log(data);
			},
            error: function()
            {
                //alert("ajax异常！");
                console.log('ajax异常！');
            }
        });
    }

	// 打印粘贴
    function PrintZhanTie(data)
    {
        var i = 0;
        var len = 0;
        var jsonStr = data;

        len = jsonStr.length;
        if(len==0)
		{
			alert("条码不正确");
			return;
		}

        var jsonObj =  JSON.parse(jsonStr);
        var jsonStr1 = JSON.stringify(jsonObj);

        var orderCode;  //订单号
        var barCode;  //条码
        var address;  //客户地址
        var clothesName;  //衣服名称
        var color;  //颜色
        var price;  //价格
        var brand;  //品牌
        var flaw;  //瑕疵
        var status;  //状态
        var storeAddress;  //本店地址

        try
        {
            var hTop=10;
            var rowHeight=16; //行距
            var LODOP=getLodop();

            //打印初始化
            LODOP.PRINT_INIT("打印控件功能演示");

            //获得打印设备个数
            /*var iCount=LODOP.GET_PRINTER_COUNT();

            //指定打印设备
            var strPName;
            for(i=0;i<iCount;i++)
            {
                strPName = LODOP.GET_PRINTER_NAME(i);
                if( strPName.indexOf("Gprinter GP-3120TU")!=-1 )
                {
                    //指定打印设备
                    LODOP.SET_PRINTER_INDEX(i);
                    alert("Gprinter----"+i);
                }
            }*/

            var jsonArr = [];
            i=0;
            //for(i =0 ;i < jsonObj.length;i++)
            {
                jsonArr[i] = jsonObj[i];
                console.log(jsonArr[i].id);

                hTop=10;
                rowHeight=16; //行距

                //打印条码
                LODOP.SET_PRINT_STYLE("FontSize", 8);
                LODOP.ADD_PRINT_BARCODE(hTop, 26, 210, 30, "128A", jsonArr[i].barCode);
                LODOP.SET_PRINT_STYLE("Bold", 1);  //粗体
                LODOP.SET_PRINT_STYLE("FontSize", 10);  //字体大小
                hTop += 26;
                hTop += rowHeight;
                LODOP.ADD_PRINT_TEXT(hTop, 26, 200, 60, "工厂店");
                hTop += rowHeight;
                LODOP.ADD_PRINT_TEXT(hTop, 26, 200, 60, "名称："+jsonArr[i].clothesName);
                hTop += rowHeight;
                LODOP.ADD_PRINT_TEXT(hTop, 26, 200, 60, "品牌："+jsonArr[i].brand);
                hTop += rowHeight;
                LODOP.ADD_PRINT_TEXT(hTop, 26, 200, 60, "客户："+jsonArr[i].name+"("+jsonArr[i].mobilePhone+")");
                hTop += rowHeight;
                LODOP.ADD_PRINT_TEXT(hTop, 26, 200, 60, "取衣日期：2018-12-21");

                //设定纸张大小;
                LODOP.SET_PRINT_PAGESIZE(1, "60mm", "41mm", "CreateCustomPage");

                //设计
                //LODOP.PRINT_DESIGN();
                //预览
                LODOP.PREVIEW();
				//直接打印
                //LODOP.PRINT();
				//选择打印机，再打印
                //LODOP.PRINTA();
            }

        }
        catch(err)
        {
            alert("打印机异常，异常代码1015！！");
        }
    }
	//测试打印控件是否安装成功
	function init()
	{
		//document.getElementById("demo").innerHTML=Date();
		try{
			var LODOP=getLodop();
			if (LODOP.VERSION) {
				if (LODOP.CVERSION)
					alert("当前有C-Lodop云打印可用!\n C-Lodop版本:"+LODOP.CVERSION+"(内含Lodop"+LODOP.VERSION+")");
				else
					alert("本机已成功安装了Lodop控件！\n 版本号:"+LODOP.VERSION);

			};
		}
		catch(err)
		{
			alert("打印机异常，异常代码1013！！");
		}
	}

	function ShouYiDan()
	{
		//document.getElementById("demo").innerHTML=Date();
		try
		{
			var hTop=30;
			var rowHeight=30; //行距
			var lineTop=30;
			var LODOP=getLodop();
			//打印初始化
			LODOP.PRINT_INIT("打印洗衣单");
			//LODOP.PRINT_INITA(0,0,700,400,"打印控件功能演示_Lodop功能_打印条码");
			//指定打印设备
			//LODOP.SET_PRINTER_INDEX(2);

			//设定纸张大小
			//LODOP.SET_PRINT_PAGESI0ZE(3,"80mm","4.5mm","CreateCustomPage");

			//打印条码
			LODOP.SET_PRINT_STYLE("FontSize",16);
			LODOP.ADD_PRINT_TEXT(hTop,75,200,60,"浣衣坊洗衣单");
			LODOP.SET_PRINT_STYLE("FontSize",10);
			hTop+=rowHeight;
			LODOP.ADD_PRINT_BARCODE(hTop,60,200,47,"128A","20171125111425914294");
			LODOP.SET_PRINT_STYLE("FontSize",16);
			hTop+=rowHeight;
			hTop+=22;
			LODOP.ADD_PRINT_TEXT(hTop,20,200,60,"交易单号：C00239");
			hTop+=rowHeight;
			LODOP.ADD_PRINT_TEXT(hTop,20,200,60,"件数：3件");
			hTop+=rowHeight;

			LODOP.SET_PRINT_STYLE("FontSize",10);
			LODOP.ADD_PRINT_TEXT(hTop,20,240,60,"客户地址：洪山区阳光100大湖的3栋4单元67-2-1202");
			hTop+=rowHeight;
			lineTop = hTop+10;
			LODOP.ADD_PRINT_LINE(lineTop,20,lineTop,240,2,1);
			hTop+=rowHeight-10;
			LODOP.ADD_PRINT_TEXT(hTop,20,200,60,"名称    颜色       价格");
			lineTop = hTop+10+10;
			LODOP.ADD_PRINT_LINE(lineTop,20,lineTop,240,2,1);

			hTop+=rowHeight;
			LODOP.ADD_PRINT_TEXT(hTop,20,200,60,"运动鞋    黑色       30.00");
			rowHeight = 16;
			hTop+=rowHeight;
			LODOP.ADD_PRINT_TEXT(hTop,20,200,60,"编号（C00239-1）");
			hTop+=rowHeight;
			LODOP.ADD_PRINT_TEXT(hTop,20,200,60,"品牌（无品牌）");
			hTop+=rowHeight;
			LODOP.ADD_PRINT_TEXT(hTop,20,200,60,"瑕疵（有不明污渍尽洗破洞1）");
			hTop+=rowHeight;
			LODOP.ADD_PRINT_TEXT(hTop,20,200,60,"状态（未取）");

			hTop+=rowHeight;
			hTop+=4;
			LODOP.ADD_PRINT_TEXT(hTop,20,200,60,"运动鞋    黑色       30.00");
			hTop+=rowHeight;
			LODOP.ADD_PRINT_TEXT(hTop,20,200,60,"编号（C00239-2）");
			hTop+=rowHeight;
			LODOP.ADD_PRINT_TEXT(hTop,20,200,60,"品牌（无品牌）瑕疵");
			hTop+=rowHeight;
			LODOP.ADD_PRINT_TEXT(hTop,20,240,60,"瑕疵（有不明污渍尽洗磨损发白）");
			hTop+=rowHeight;
			LODOP.ADD_PRINT_TEXT(hTop,20,200,60,"状态（未取）");

			hTop+=rowHeight;
			hTop+=4;
			LODOP.ADD_PRINT_TEXT(hTop,20,200,60,"运动鞋    黑色       30.00");
			hTop+=rowHeight;
			LODOP.ADD_PRINT_TEXT(hTop,20,200,60,"编号（C00239-3）");
			hTop+=rowHeight;
			LODOP.ADD_PRINT_TEXT(hTop,20,200,60,"品牌（无品牌）瑕疵");
			hTop+=rowHeight;
			LODOP.ADD_PRINT_TEXT(hTop,20,200,60,"瑕疵（有不明污渍尽洗磨损）");
			hTop+=rowHeight;
			LODOP.ADD_PRINT_TEXT(hTop,20,200,60,"状态（未取）");

			hTop+=rowHeight;
			hTop+=4;
			LODOP.ADD_PRINT_TEXT(hTop,20,240,60,"交易总额：90.00（附加费0.00）");

			lineTop = hTop+20;
			LODOP.ADD_PRINT_LINE(lineTop,20,lineTop,240,2,1);
			hTop+=28;
			LODOP.SET_PRINT_STYLE("FontSize",16);
			LODOP.ADD_PRINT_TEXT(hTop,20,200,60,"应收金额：90.00");
			hTop+=26;
			LODOP.SET_PRINT_STYLE("FontSize",10);
			LODOP.ADD_PRINT_TEXT(hTop,20,200,60,"现金支付：90.00");
			lineTop = hTop+20;
			LODOP.ADD_PRINT_LINE(lineTop,20,lineTop,240,2,1);

			rowHeight = 16;
			hTop+=10;
			hTop+=rowHeight;
			LODOP.ADD_PRINT_TEXT(hTop,20,240,60,"本店地址：海马科技");
			hTop+=rowHeight;
			LODOP.ADD_PRINT_TEXT(hTop,20,240,60,"服务热线：18671209825");
			hTop+=rowHeight;
			LODOP.ADD_PRINT_TEXT(hTop,20,240,60,"店员：毛海  店号：0003");
			hTop+=rowHeight;
			LODOP.ADD_PRINT_TEXT(hTop,20,240,60,"打印时间：2018-12-21 09：13");

			//LODOP.SET_PRINT_STYLE("FontSize",16);
			//LODOP.ADD_PRINT_BARCODE(90,34,307,47,"128A","123456789012");

			//设定纸张大小
			//LODOP.SET_PRINT_PAGESIZE(0,"80mm","40mm","CreateCustomPage");
			LODOP.SET_PRINT_PAGESIZE(3,"80mm","4.5mm","CreateCustomPage");

			//设计
			//LODOP.PRINT_DESIGN();
			//预览
			LODOP.PREVIEW();
			//LODOP.PRINT();  //直接打印
			//LODOP.PRINTA();  //选择打印机，再打印

			// LODOP.PRINT_INITA(0,0,800,400,"打印控件功能演示_Lodop功能_内嵌显示演示11_2");
			// LODOP.ADD_PRINT_BARCODE(28,34,109,338,"128A","123456789012");
			// LODOP.SET_PRINT_STYLEA(0,"Angle",90);
			// LODOP.ADD_PRINT_LINE(206,558,207,658,0,1);
			// LODOP.ADD_PRINT_LINE(160,605,242,606,0,1);
			// LODOP.PRINT();
		}
		catch(err){
		}
	}
	
</script>
</head>

<!-- 异步表格   <body onload="ajaxReload()"> -->
<body id="content">

	<c:if test="${resultCode == 1}">
   <script type="text/javascript">
   		art.dialog.alert('${resultMsg}',function(){
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
		<em>工厂上挂</em>
	</div>
	<!-- 搜索条件 -->
	<form id="formId" name="formId" class="form-horizontal" role="form" method="post">
	
	<div  style="padding-left: 10px; margin-top: 5px;">
			<table cellpadding="0" cellspacing="0" border="0" width="100%" >
				<tr>
					<td width="10%">
						<div class="input-group" >
							<input id="queryKey" placeholder="输入条码" name="queryKey" value="${queryKey}" type="text" class="form-control" style="width: 150px;"> <!-- <span class="glyphicon glyphicon-star"></span> -->
						</div>
					</td>
					<td width="100px" align="left">
						<div class="input-group" style="padding-left: 10px;">
							<button class="btn btn-primary" type="button" id="transLogQueryBtn1" onclick="query()" style="margin-right: 10px;">查  询</button>
							<button class="btn btn-primary" type="button" id="transLogQueryBtn2" onclick="waitingHandon()" style="margin-right: 10px;">待上挂衣物</button>
							<button class="btn btn-primary" type="button" id="transLogQueryBtn3" onclick="PostXiYiDan()" style="margin-right: 10px;">打印洗衣单</button>
							<button class="btn btn-primary" type="button" id="transLogQueryBtn4" onclick="PostZhanTie()" style="margin-right: 10px;">打印粘贴</button>
						</div>
					</td>
				</tr>
				
			</table>
		</div>
		
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
				
				<tr>
					<td width="10%" align="right">已上挂件数:</td>
					<td width="100px" align="left">${handonCount}</td>

					<td width="10%" align="right">已上隔架区:</td>
					<td width="100px" align="left">${order.handOnArea}</td>

					<td width="10%" align="right">已上隔架号</td>
					<td width="100px" align="left">${order.handOnNo}</td>
				</tr>
			</table>
			
		</div>
	<br style="border: 1px;border-color: fff"/>
	<div style="display: inline;">
		<div style="color: blue;font-size: 15px;position: relative;margin-top: 15px;margin-left: 10px;float: left;">【衣物清单】</div>
	</div>
	<article>
		<div class="segment">
			<div style="padding-left: 10px; padding-right: 10px;">
				<table class="table datatable table-bordered  table-hover" id="datatable">
					<thead>
						<tr>
							<th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>衣物条码</th>
							<th class='text-center' data-col-class='text-center' data-width='10%' class='text-primary'>服务类型</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>衣物名称</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>颜色</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>品牌</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>瑕疵</th>
							
							<th class='text-center' data-col-class='text-center' data-width='10%'>衣物状态</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>送洗时间</th>
							<th class='text-center' data-col-class='text-center' data-width='10%'>上挂时间</th>
						</tr>
					</thead>
					<!-- 同步表格 -->
					<tbody>
 						<!--  PageHelper -->
						<c:forEach items="${handOnList}" var="clothes">
							<tr>
							    <td class='text-center'>${clothes.barCode}</td>
							    <td class='text-center'>${clothes.serviceType}</td>
							    <td class='text-center'>${clothes.clothesName}</td>
							    <td class='text-center'>${clothes.color}</td>
							    <td class='text-center'>${clothes.brand}</td>
							    <td class='text-center'>${clothes.flaw}</td>
							    <td class='text-center'>${clothes.status}</td>
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