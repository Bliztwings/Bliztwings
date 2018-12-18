<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ include file="/WEB-INF/jsp/inc/tag_css_js.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>拍照</title>

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
        $("#takephoto_clothes_barcode").focus();
        $("#takephoto_clothes_takebtn").hide();
    });
    function StartVideo() {
        var index = CmCaptureOcx.Initial();
        if (index == -2) {
            alert("没有设备");
            return;
        } else if (index == -1) {
            alert("设备没有授权");
            index = 0;
            return;
        }
        CmCaptureOcx.StartRun(index);
        CmCaptureOcx.SetJpgQuanlity(100);
        $("#takephoto_clothes_takebtn").show();
    }
    function Capture() {
        clearData();
        var clothesCode = $("#takephoto_clothes_barcode").val();
        if (clothesCode=="") {
            alert("请先输入衣服条码!");
            return;
        }
        CmCaptureOcx.SetFileType(1);
        var fileName = "img_"+new Date().getTime()+".jpg";
        //var fileName = "img_1502008976623.jpg";
        var filePath = "d:\\ewashing-photo\\factory\\" + fileName;
        //1：拍图成功 0：未找到图片 -1：视频流为空 -2：文件名为空 -3：无图像 -4：存图失败
        var captureResult = CmCaptureOcx.CaptureImage(filePath);
        //var captureResult = 1;
        if (captureResult > 0) {
            //var uploadResult = CmCaptureOcx.UpdataFile();//参数一：字符串  URL地址  参数二：文件名（包含路径）参数三 true：上传后删除本地文件;返回：是否成功上传(0失败 1成功)
            CmCaptureOcx.Destory();
            jQuery.ajax({
                url : "<%=basePath%>/factory/saveClothesPhoto",
                type : "post",
                cache : false,
                dataType : "json",
                data : {
                    'clothesId' : clothesCode,
                    'photoName' : fileName,
                    'photoType' : 1,
                    'filePath' :filePath
                },
                success : function(data, textStatus) {
                    if (data.resultCode == "0") {
                        art.dialog.alert(data.resultMsg);
                    } else {
                        art.dialog.alert("图片上传成功!");
                        $("#r_clothesname").text(data.storeClothes.clothesName);
                        $("#r_color").text(data.storeClothes.color);
                        $("#r_brand").text(data.storeClothes.brand);
                        $("#r_price").text(data.storeClothes.price);
                        $("#r_urgency").text(data.storeClothes.urgency);
                        $("#r_servicetype").text(data.storeClothes.serviceType);
                        $("#r_flaw").text(data.storeClothes.flaw);
                    }
                }
            });
        } else {
            alert("拍照失败，错误编码：" + captureResult);
        }
    }
    
    function clearData(){
        $("#r_clothesname").text("");
        $("#r_color").text("");
        $("#r_brand").text("");
        $("#r_price").text("");
        $("#r_urgency").text("");
        $("#r_servicetype").text("");
        $("#r_flaw").text("");
    }
</script>

<SCRIPT type="text/javascript" for="CmCaptureOcx"
    event="GetImageFileName(fileName);">
    CmCaptureOcx.AddPDFImageFile(fileName);
</SCRIPT>

</head>
<body>
    <div style="float: left; width: 450px; height: 500px;">
        <div style="float: left; width: 450px;">

            <button class="btn btn-primary" type="button"
                id="transLogQueryBtn" onclick="StartVideo()"
                style="background-color: #2183c5; margin-right: 10px; margin-top: 10px;">打开摄像头拍照</button>
        </div>
        <div
            style="float: left; width: 450px; height: 500px; margin-top: 20px;">
            <center>
                <p>
                    <OBJECT id="CmCaptureOcx"
                        style="MARGIN-LEFT: 5px; WIDTH: 500px; HEIGHT: 400px"
                        classid="clsid:3CA842C5-9B56-4329-A7CA-35CA77C7128D">
                    </OBJECT>

                </p>
            </center>
            <p>
            <center>
                <input id="takephoto_clothes_takebtn" class="btn btn-primary"
                    style="background-color: #2183c5; margin-top: 20px;"
                    type="button" value=" 拍    照 " onClick="Capture();"
                    name="Capture"> <br> <br>
            </center>
            </p>
        </div>
    </div>

    <div style="float: left; margin-left: 80px;">

        <div class="input-group"
            style="margin-left: 150px; margin-top: 40px;">
            <span class="input-group-addon"
                style="width: 20px; text-align: left;">衣物条码：</span> <input
                id="takephoto_clothes_barcode" name="storeName" value="${storeName}"
                type="text" class="form-control" style="width: 200px;">
            <!-- <span class="glyphicon glyphicon-star"></span> -->
        </div>

        <div style="padding-left: 10px; margin-top: 20px;">
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
                        <div id="r_clothesname" class="infactory-td-div-content"></div>
                    </td>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">颜色:</div>
                    </td>
                    <td>
                        <div id="r_color" class="infactory-td-div-content"></div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">品牌:</div>
                    </td>
                    <td>
                        <div id="r_brand" class="infactory-td-div-content"></div>
                    </td>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">收衣价格:</div>
                    </td>
                    <td>
                        <div id="r_price" class="infactory-td-div-content"></div>
                    </td>
                </tr>

                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">紧急程度:</div>
                    </td>
                    <td>
                        <div id="r_urgency" class="infactory-td-div-content"></div>
                    </td>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">加急费用:</div>
                    </td>
                    <td>
                        <div class="infactory-td-div-content">无</div>
                    </td>
                </tr>

                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">服务:</div>
                    </td>
                    <td colspan="3">
                        <div id="r_servicetype" class="infactory-td-div-content"></div>
                    </td>
                </tr>

                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">附件:</div>
                    </td>
                    <td colspan="3">
                        <div class="infactory-td-div-content"></div>
                    </td>
                </tr>

                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">瑕疵:</div>
                    </td>
                    <td colspan="3">
                        <div id="r_flaw" class="infactory-td-div-content"></div>
                    </td>
                </tr>

                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">附加服务:</div>
                    </td>
                    <td colspan="3">
                        <div class="infactory-td-div-content"></div>
                    </td>
                </tr>

            </table>

        </div>

        <button class="btn btn-primary" type="button"
            id="transLogQueryBtn" onclick="query()"
            style="background-color: #2183c5; margin-right: 10px; margin-top: 10px;">显示衣服历史拍照列表</button>

    </div>
    <!-- 
$("if").val("2222") -->

</body>
</html>