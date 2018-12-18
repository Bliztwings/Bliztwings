<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ include file="/WEB-INF/jsp/inc/tag_css_js.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>入厂</title>

<link type="text/css" href="<%=basePath%>/zui-1.5.0/css/zui.min.css"
    rel="stylesheet" />
<script type="text/javascript"
    src="<%=basePath%>/zui-1.5.0/lib/jquery/jquery.js"></script>
<script type="text/javascript"
    src="<%=basePath%>/zui-1.5.0/js/zui.min.js"></script>

<!-- form验证 -->
<link rel="stylesheet"
    href="<%=basePath%>/js/validationEngine/css/validationEngine.jquery.css"
    type="text/css" />
<!-- <script src="<%=basePath%>/js/jquery/jquery-1.7.2.min.js" type="text/javascript"></script> -->
<script
    src="<%=basePath%>/js/validationEngine/js/languages/jquery.validationEngine-zh_CN.js"
    type="text/javascript" charset="utf-8"></script>
<script
    src="<%=basePath%>/js/validationEngine/js/jquery.validationEngine-2.6.4.js"
    type="text/javascript" charset="utf-8"></script>

<!--  art.dialog  -->
<link href="<%=basePath%>/js/artDialog4.1.7/skins/blue.css"
    rel="stylesheet" type="text/css" />
<script src="<%=basePath%>/js/artDialog4.1.7/artDialog.js"
    type="text/javascript"></script>
<script
    src="<%=basePath%>/js/artDialog4.1.7/plugins/iframeTools.source.js"
    type="text/javascript"></script>

<!-- 引入factory.css -->
<link rel="stylesheet" href="<%=basePath%>/static/css/factory.css"
    type="text/css" />

<style>
ul.ztree {
 margin-top: 10px;
 border: 1px solid #617775;
 background: #f0f6e4;
 width: 220px;
 height: 200px;
 overflow-y: scroll;
 overflow-x: auto;
}
</style>

<script type="text/javascript">
    jQuery(document).ready(function() {
		
    	var json =JSON.parse('${clothesresult}');
    	var orderJson =JSON.parse('${orderInfo}');
    	$("#infactory_clothes_barcode").focus();
		$("#l_storename").text(trimNull(json.storeName));
		$("#l_ordernum").text(json.orderCode);
        $("#l_sendnum").text(trimNull(json.sendNumber));
        $("#l_createdate").text(trimNull(json.sendDateStr));
        $("#r_clothesname").text(trimNull(json.clothesName));
        $("#r_clothescolor").text(trimNull(json.color));
        $("#r_brand").text(trimNull(json.brand));
        $("#r_money").text(json.price);
        $("#r_urgency").text(json.urgency);
        $("#r_urgencymoney").text(json.serviceFee);
        $("#r_washtype").text(json.serviceType);
        $("#l_payStatus").text(trimNull(json.payStatus));
        
       
        if(trimNull(orderJson.orderType)=="1"){
        	$("#orderType").text("门店订单");
        }
        if(trimNull(orderJson.orderType)=="2"){
        	
        	if(trimNull(orderJson.appId) =='wx614b63f73b55d7ef'){
        		$("#orderType").text("萨维亚订单");
        	}
        	if(trimNull(orderJson.appId) =='wxc6097b5bf9ba801d'){
        		$("#orderType").text("保利.浣衣坊订单");
        	}
        }
        
        // 附件信息设置
        var attachHtml ="";
        attachHtml+='<table class="table datatable table-bordered  table-hover" id="datatable">';
    	attachHtml+='<tbody>';
        
    	var attachList=json.attachList;
		    	
    	if(attachList==null || attachList=='' || attachList.length==0){
    		$("#r_attachment").text("");
    	}
    	else{
    		for(var i =0;i<attachList.length;i++){
    			attachHtml+='<tr>';
    	        attachHtml+='<th class="text-center" data-col-class="text-center" data-width="10%">'+attachList[i].attachName+'</th>';
    	    	attachHtml+='<th class="text-center" data-col-class="text-center" data-width="10%">'+attachList[i].attachBarCode+'</th>';
    	    	attachHtml+='</tr>';
    		}
    		 $("#r_attachment").html(attachHtml);
    	}
       
        $("#r_flaw").text(json.flaw);
        $("#l_storetel").text(json.mobilePhone);
        $("#l_count").text(json.clothesCount);
        
    });
    
    function photoHistory() {
    	var json =JSON.parse('${clothesresult}');
    	var clothesId = json.id;
    	if (trimNull(clothesId)=='') {
            art.dialog.alert('衣服条码不能为空！');
        } else {
	  	  art.dialog.open( '<%=basePath%>/storeBusiness/showPhoto?clothesId='+clothesId, {
	            title : '拍照历史',
	            width : '100%',
	            height : 700,
	            lock : true
	        });
        }
      }
    
	function trimNull(text){
    	if(text ==null || text =='' || text =='null'){
    		return ""
    	}
    	return text;
    }
    
</script>
</head>
<body>

    <div style="float: left">

        <div style="padding-left: 50px; margin-top: 85px;">
            <table cellpadding="0" cellspacing="1px" border="1px"
                width="520px">
                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">送洗门店:</div>
                    </td>
                    <td>
                        <div id="l_storename" class="infactory-td-div-content"></div>
                    </td>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">门店电话:</div>
                    </td>
                    <td>
                        <div id="l_storetel" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">送洗件数:</div>
                    </td>
                    <td>
                        <div id="l_count" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">已厂件数:</div>
                    </td>
                    <td>
                        <div class="infactory-td-div-content">&nbsp;</div>
                    </td>
                </tr>

				<tr>
                    <td style="width: 80px;">
                        <div class="infactory-td-div">订单类型:</div>
                    </td>
                    <td colspan="3">
                        <div class="infactory-td-div-content" id ="orderType">&nbsp;</div>
                    </td>
                </tr>
                
                <tr>
                    <td style="width: 80px;">
                        <div class="infactory-td-div">未入厂件数:</div>
                    </td>
                    <td colspan="3">
                        <div class="infactory-td-div-content">&nbsp;</div>
                    </td>
                </tr>

                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">订单号:</div>
                    </td>
                    <td colspan="3">
                        <div id="l_ordernum" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                </tr>
                
                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">付款状态:</div>
                    </td>
                    <td colspan="3">
                        <div id="l_payStatus" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                </tr>

                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">送洗单号:</div>
                    </td>
                    <td colspan="3">
                        <div id="l_sendnum" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                </tr>

                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">送洗时间:</div>
                    </td>
                    <td>
                        <div id="l_createdate" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">是否返洗:</div>
                    </td>
                    <td>
                        <div class="infactory-td-div-content">&nbsp;</div>
                    </td>
                </tr>

            </table>

        </div>
    </div>

    <div style="float: left; margin-left: 80px;">

        <div style="padding-left: 10px; margin-top: 85px;">
            <table cellpadding="0" cellspacing="1px" border="1px"
                width="520px">

                <tr>
                    <td colspan="4" style="text-align: center;">
                        <div class="infactory-td-div-title">信息提示区域</div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">衣服名称:</div>
                    </td>
                    <td>
                        <div id="r_clothesname" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">颜色:</div>
                    </td>
                    <td>
                        <div id="r_clothescolor" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">品牌:</div>
                    </td>
                    <td>
                        <div id="r_brand" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">收衣价格:</div>
                    </td>
                    <td>
                        <div id="r_money" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                </tr>

                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">紧急程度:</div>
                    </td>
                    <td>
                        <div id="r_urgency" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">加急费用:</div>
                    </td>
                    <td>
                        <div id="r_urgencymoney" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                </tr>

                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">服务:</div>
                    </td>
                    <td colspan="3">
                        <div id="r_washtype" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                </tr>

                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">附件:</div>
                    </td>
                    <td colspan="3">
                        <div id="r_attachment" class="infactory-td-div-content" style="height: 150px;">&nbsp;</div>
                    </td>
                </tr>

                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">瑕疵:</div>
                    </td>
                    <td colspan="3">
                        <div id="r_flaw" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                </tr>

                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">附加服务:</div>
                    </td>
                    <td colspan="3">
                        <div id="r_additionalservice" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                </tr>

            </table>

        </div>

        <button class="btn btn-primary" type="button"
            id="transLogQueryBtn" onclick="photoHistory()"
            style="background-color: #2183c5; margin-right: 10px; margin-top: 10px;">显示衣服历史拍照列表</button>

    </div>
    <!-- 
$("if").val("2222") -->

</body>
</html>