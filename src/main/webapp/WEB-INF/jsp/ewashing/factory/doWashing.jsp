<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ include file="/WEB-INF/jsp/inc/tag_css_js.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>洗衣工序</title>

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
        $("#dowashing_title").text('当前工序:'+'${washTypeStr}'+'  员工:'+'${user.username}');
        $("#dowashing_clothes_barcode").focus();
    });
    
    function clearData(){
    	$("#l_storename").text("");
        $("#l_clothesmoney").text("");
        $("#l_ordernum").text("");
        $("#l_sendnum").text("");
        $("#l_sendtime").text("");
        $("#r_clothesname").text("");
        $("#r_clothescolor").text("");
        $("#r_clothesbrand").text("");
        $("#r_servicetype").text("");
        $("#r_flaw").text("");
    }
    function queryClothes() {
    	clearData();
        var clothesCode = $("#dowashing_clothes_barcode").val();
        var washType = '${washType}';
        if (clothesCode=="")
            return;
        jQuery.ajax({
            url : "<%=basePath%>/factory/doWashingUpdateType",
            type : "post",
            cache : false,
            dataType : "json",
            data : {
                'barCode' : clothesCode,
                'washType' : washType
            },
            success : function(data, textStatus) {
                if (data.resultCode == "0") {
                    art.dialog.alert(data.resultMsg);
                } else {
                    $("#l_storename").text(trimNull(data.storeclothes.storeName));
                    $("#l_clothesmoney").text(data.storeclothes.price);
                    $("#l_ordernum").text(trimNull(data.storeclothes.orderCode));
                    $("#l_sendnum").text(trimNull(data.storeclothes.sendNumber));
                    $("#l_sendtime").text(trimNull(data.storeclothes.sendDateStr));
                    $("#r_clothesname").text(trimNull(data.storeclothes.clothesName));
                    $("#r_clothescolor").text(trimNull(data.storeclothes.color));
                    $("#r_clothesbrand").text(trimNull(data.storeclothes.brand));
                    /* $("#r_orderprice").text(data.brand); */
                    $("#r_servicetype").text(trimNull(data.storeclothes.serviceType));
                    $("#r_flaw").text(trimNull(data.storeclothes.flaw));
                    
                    // 清空条码，focus
                    $("#dowashing_clothes_barcode").val("");
                    $("#dowashing_clothes_barcode").focus();
                    	
                }
            }
        });
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

    <table>

        <tr>
            <td>
                <div id="dowashing_title" style="margin-top: 10px; margin-left: 20px; font-size: 18px;">当前工序：ccc 员工:XXXX</div>
            </td>
        </tr>
        <tr>
            <td>
                <table
                    style="width: 340px; margin-top: 30px; margin-left: 40px">
                    <tr>
                        <td style="width: 75px;">
                            <div class="dowashing-td-div">送洗门店:</div>
                        </td>
                        <td>
                            <div id="l_storename" class="dowashing-td-div-content"></div>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 75px;">
                            <div class="dowashing-td-div">衣服价格:</div>
                        </td>
                        <td>
                            <div id="l_clothesmoney" class="dowashing-td-div-content"></div>
                        </td>
                    </tr>

                    <tr>
                        <td style="width: 75px;">
                            <div class="dowashing-td-div">订单号:</div>
                        </td>
                        <td>
                            <div id="l_ordernum" class="dowashing-td-div-content"></div>
                        </td>
                    </tr>

                    <tr>
                        <td style="width: 75px;">
                            <div class="dowashing-td-div">送洗单号:</div>
                        </td>
                        <td>
                            <div id="l_sendnum" class="dowashing-td-div-content"></div>
                        </td>
                    </tr>

                    <tr>
                        <td style="width: 75px;">
                            <div class="dowashing-td-div">送洗时间:</div>
                        </td>
                        <td>
                            <div id="l_sendtime" class="dowashing-td-div-content"></div>
                        </td>
                    </tr>

                </table>
            </td>
            <td>
                <table>
                    <tr>
                        <div class="input-group"
                            style="margin-left: 5px; margin-top: 1px; margin-bottom: 30px;">
                            <span class="input-group-addon"
                                style="width: 20px; text-align: left;">衣物条码：</span>
                            <input id="dowashing_clothes_barcode"
                                name="barCode" value="" type="text"
                                class="form-control"
                                style="width: 200px;"
                                onchange="queryClothes()">
                            <!-- <span class="glyphicon glyphicon-star"></span> -->
                        </div>
                    </tr>
                    <tr>
                        <td>
                            <div class="dowashing-td-div">衣服名称:</div>
                        </td>
                        <td>
                            <div id="r_clothesname" class="dowashing-td-div-content"></div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="dowashing-td-div">衣服颜色:</div>
                        </td>
                        <td>
                            <div id="r_clothescolor" class="dowashing-td-div-content"></div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="dowashing-td-div">品牌:</div>
                        </td>
                        <td>
                            <div id="r_clothesbrand" class="dowashing-td-div-content"></div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="dowashing-td-div">单价:</div>
                        </td>
                        <td>
                            <div id="r_orderprice" class="dowashing-td-div-content"></div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="dowashing-td-div">服务:</div>
                        </td>
                        <td>
                            <div id="r_servicetype" class="dowashing-td-div-content"></div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="dowashing-td-div">附件:</div>
                        </td>
                        <td>
                            <div id="r_attachment" class="dowashing-td-div-content"></div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="dowashing-td-div">瑕疵:</div>
                        </td>
                        <td>
                            <div id="r_flaw" class="dowashing-td-div-content"></div>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</body>
</html>