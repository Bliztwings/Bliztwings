<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path;
	String realPath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "storeBusiness";
%>

<head>
	<meta http-equiv="X-UA-Compatible" content="IE=11"/>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>拍照</title>
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
    
    <!-- vue1.0 -->
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/js/vue1.0/iview.css">
    <script type="text/javascript" src="<%=basePath%>/js/vue1.0/vue.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/js/vue1.0/vue-resource.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/js/vue1.0/iview.min.js"></script>
<script >


var CamID  = -1;  //此例程默认以第一个摄像头打开
var SupportFormat;    //设备支持的视频格式代号：1->MJPG; 2->YUY2 
var OpenFormat;      //打开格式：0->YUY；1->MJPG
var isOpen = false;
var isWiseCapture =false;

var nFileCount = 0;
var bAdjustMode = 0;
var bCropArea = 0;
var bSeriesCapture = 0;
var bReadBarCode=0;
var bSetDenoise = 0;
var Width = 0;
var Height = 0;
var strFile;
var index = 0;
var strCardFile;
var cardIndex = 0;
var captureCount = 0;


function loadActiveX() {

    if ((navigator.userAgent.indexOf('MSIE') >= 0) && (navigator.userAgent.indexOf('Opera') < 0)) {
         //IE浏览器加载控件
        document.getElementById("ActiveXDivOne").innerHTML = "<OBJECT id=\"axCam_Ocx\"  classid=\"clsid:CE2D72F2-AD28-4013-A24B-C3F76C5A1944\" width=\"100%\" height=\"544px\" ></OBJECT>";
    }
    else {
        //其他浏览器加载控件
        document.getElementById("ActiveXDivOne").innerHTML = "<OBJECT id=\"axCam_Ocx\" type=\"application/x-camera\"  clsid=\"{CE2D72F2-AD28-4013-A24B-C3F76C5A1944}\"  width=\"100%\" height=\"544px\" ></OBJECT>";
     }
}

function show(op){
  alert(op);
}

function ShowInfo(op) {
    var obj = document.getElementById("TextArea1");
    obj.value = op
}

//获取设备;
function GetDevice() {

    var i = 0;
    var devCount = axCam_Ocx.GetDevCount();
    
    if (devCount > 0) {

       CamID = axCam_Ocx.GetMainCameraID();   //获取主摄像头ID
       if (CamID < 0) {
            alert("没有主头设备");
            return;
        }
    }  

    var FormatSum  = 0;  //设备支持的视频格式代号总和
    var scount  = axCam_Ocx.GetFormatCount(CamID);   //获取设备支持的视频格式数目
    for (var k = 0; k < scount; k++)
    {
         FormatSum = FormatSum + axCam_Ocx.GetFormatNumber(k);    //GetFormatNumber（）获取设备支持的视频格式代号
    }
               
    //判断视频格式代号总和的值
    switch(FormatSum)
    {
        case 1:   //（设备只支持MJPG格式）
            OpenFormat = 1 ;     //打开格式为MJPG
            SupportFormat = 1;
            break;
        case 2:   //（设备只支持YUY2格式）
            OpenFormat = 0 ;     //打开格式为YUY
            SupportFormat = 2;
            break;
        case 3:   //（设备支持MJPG与YUY2两种格式）
            OpenFormat = 1 ;     //默认打开格式为MJPG
            SupportFormat = 1;
            break;
    }
    filetypeobj.value = 1;
}

//获取分辨率
function GetDeviceResolution() 
{
    var i = 0;
    var resCount = axCam_Ocx.GetResolutionCount(CamID, SupportFormat); //获取设备支持的分辨率数目
    var obj = document.getElementById("Resolution");
    for (i = 0; i < resCount; i++) {
        var mResolution = axCam_Ocx.GetResolution(i);
        var objOption = document.createElement("option");
        objOption.text = mResolution;
        objOption.value = i;
        obj.options.add(objOption);
    }
    if (resCount > 0) obj.value = 0;
}

//开启摄像头
function StartVideo() 
{
	var OpenFormat =1;
    var i = axCam_Ocx.CAM_Open(CamID, OpenFormat, 640, 480);
    if (i == 0) 
    	alert("开启设备成功");
    else 
    	alert("开启设备失败");

}

//关闭摄像头
function CloseVideo() 
{
    axCam_Ocx.CAM_Close()
    alert("关闭设备");
}

//抓图拍照
function Capture() {

    var tempUploadPath =$("#tempUploadPath").val();
    var fileName ="Img"+new Date().getTime()+".jpg";
    var path = tempUploadPath+"\\"+fileName
    var baseStr=axCam_Ocx.CaptureImage(path);
    var imgobj1 = document.getElementById("img1");
    imgobj1.src = "data:;base64," + baseStr;
    var info="拍照成功\n" + "位置:" + path;
    $("#captureFilePath").val(path);
    $("#captureFileName").val(fileName);
}

//切换摄像头
function ChangeDevice() 
{
    axCam_Ocx.CAM_Close(); //先关闭摄像头
    var devObj = document.getElementById("DeviceName");
    var resObj = document.getElementById("Resolution");
    CamID = devObj.selectedIndex;

    var FormatSum = 0;  //设备支持的视频格式代号总和
    var scount = axCam_Ocx.GetFormatCount(CamID);   //获取设备支持的视频格式数目
    for (var k = 0; k < scount; k++) {
        FormatSum = FormatSum + axCam_Ocx.GetFormatNumber(k);    //GetFormatNumber（）获取设备支持的视频格式代号
    }

    //判断视频格式代号总和的值
    switch (FormatSum) {
        case 1:   //（设备只支持MJPG格式）
            OpenFormat = 1;     //打开格式为MJPG
            SupportFormat = 1;
            break;
        case 2:   //（设备只支持YUY2格式）
            OpenFormat = 0;     //打开格式为YUY
            SupportFormat = 2;
            break;
        case 3:   //（设备支持MJPG与YUY2两种格式）
            OpenFormat = 1;     //默认打开格式为MJPG
            SupportFormat = 1;
            break;
    }

    resObj.options.length = 0;
    GetDeviceResolution();
    var restr = resObj[resObj.selectedIndex].text;
    var pos = restr.lastIndexOf("*");
    var width = parseInt(restr.substring(0, pos));
    var height = parseInt(restr.substring(pos + 1, restr.length));
    var i = axCam_Ocx.CAM_Open(CamID, OpenFormat, width, height);
    if (i == 0) ShowInfo("开启设备成功");
    else ShowInfo("开启设备失败");
}

//切换分辨率
function ChangeResolution() 
{
    axCam_Ocx.CAM_Close();  //先关闭摄像头
    var obj = document.getElementById("Resolution");
    var restr = obj[obj.selectedIndex].text;
    var pos = restr.lastIndexOf("*");
    var width = parseInt(restr.substring(0, pos));
    var height = parseInt(restr.substring(pos + 1, restr.length));

    var i = axCam_Ocx.CAM_Open(CamID, OpenFormat, width, height);
}

//设置图片类型
function SetFileType() 
{
    var obj = document.getElementById("FileType");
    axCam_Ocx.SetFileType(obj.selectedIndex);
}

//设置图片颜色格式
function SetImageColorMode() 
{
    var obj = document.getElementById("ColourMode");
    axCam_Ocx.SetImageColorMode(obj.selectedIndex);
}

//图像设置窗口
function ShowImageSettingWindow()
{
    axCam_Ocx.ShowImageSettingWindow();
}

//设置是否裁剪
function SetCutType() {
    if (document.getElementById("Radio1").checked) {
        axCam_Ocx.SetAutoCut(0);
        axCam_Ocx.CusCrop(0);
    }
    if (document.getElementById("Radio2").checked) {
        axCam_Ocx.SetAutoCut(1);
    }
    if (document.getElementById("Radio3").checked) {
        axCam_Ocx.CusCrop(1);
    }
}


//智能连拍
function WiseCapture() {

    if (document.getElementById("Radio7").checked) {
        var waterType = 0; //水印类型为文字类型
        var waterinfo = document.getElementById("Text1").value;   //文字内容
        var pos = document.getElementById("Select1").selectedIndex;
        var tran = parseInt(document.getElementById("Text2").value); //透明度
        var FontSize = parseInt(document.getElementById("Text3").value); //字体大小
        var mcolor = parseInt(document.getElementById("Text4").value); //颜色
        axCam_Ocx.SetWaterMark(waterinfo, waterType, tran, pos, FontSize, mcolor);
    }
    if (document.getElementById("Radio8").checked) {
        var waterType = 1; //水印类型为图片类型
        var waterinfo = document.getElementById("Text1").value;   //图片路径
        var pos = document.getElementById("Select1").selectedIndex;
        var tran = parseInt(document.getElementById("Text2").value); //透明度
        var FontSize = parseInt(document.getElementById("Text3").value); //字体大小
        var mcolor = parseInt(document.getElementById("Text4").value); //颜色
        axCam_Ocx.SetWaterMark(waterinfo, waterType, tran, pos, FontSize, mcolor);
    }


    var strFolder = "D:";

    if (isWiseCapture == false) {
        axCam_Ocx.WiseCapture(1, strFolder);
        isWiseCapture = true;
    }
    else {
        axCam_Ocx.WiseCapture(0, strFolder);
        isWiseCapture = false;
     }
}

//条码识别
function ReadQrCode()
{
    var CodeStr = axCam_Ocx.GetQRcodeContent("");
    ShowInfo("二维码内容:" + CodeStr);
}


//合并PDF
function ConvertToPDF()
{
    var pat1 = "d:\\add1.jpg";
    var pat2 = "d:\\add2.jpg";
    var pat3 = "d:\\add3.jpg";
    var pdfpath = "d:\\Convert.pdf";
    axCam_Ocx.AddPDFImageFile(pat1,"",0);
    axCam_Ocx.AddPDFImageFile(pat2,"",0);
    axCam_Ocx.AddPDFImageFile(pat3,"",0);
    axCam_Ocx.Convert2PDF(pdfpath);
}

//合并TIFF
function ConvertToTiff() {
    var pat1 = "d:\\add1.jpg";
    var pat2 = "d:\\add2.jpg";
    var pat3 = "d:\\add3.jpg";
    var pdfpath = "d:\\Convert.tiff";
    axCam_Ocx.AddTiffImageFile(pat1);
    axCam_Ocx.AddTiffImageFile(pat2);
    axCam_Ocx.AddTiffImageFile(pat3);
    axCam_Ocx.Convert2Tiff(pdfpath);
}


//图片合并
function CombineTwoImage() 
{
    var dir = 1;  // 1->垂直合并  2->水平合并
    if (document.getElementById("Radio4").checked) { dir = 1;}
    if (document.getElementById("Radio5").checked) { dir = 2;}

    var combinePath = "d:\\CombineImg.jpg";
    var ImgPath1 = "d:\\add1.jpg";
    var ImgPath2 = "d:\\add2.jpg";
    axCam_Ocx.CombineTwoImage(combinePath, ImgPath1, ImgPath2, dir);
 }



//放大
function ZoomOut() {
    axCam_Ocx.CAM_ZoomOut(); 
}

//缩小
function ZoomIn() {
    axCam_Ocx.CAM_ZoomIn();
}

//左旋
function RoateL() {
    axCam_Ocx.CAM_RotateL();
}

//右旋
function RoateR() {
    axCam_Ocx.CAM_RotateR();
}

//适合大小
function BestSize() {
    axCam_Ocx.BestSize();
}

//实际大小
function TrueSize() {
    axCam_Ocx.TrueSize();
}

//上传图片
function UpdataFile() {
	var captureFiilePath =$("#captureFilePath").val();
	var uploadPath =$("#uploadPath").val();
	var captureFileName =$("#captureFileName").val();
    var count =axCam_Ocx.UpdataFile("<%=basePath%>/upload.jsp?captureFileName="+captureFileName, captureFiilePath);
	if(count ==0){
		alert("拍照成功");
	}
	artDialog.data("imagePath", uploadPath+"\\"+captureFileName); //将值存起来，供父页面读取  
}


function getBase64() {
    var textobj = document.getElementById("File1");
    var strpath = textobj.value;
    var basestr = axCam_Ocx.GetBase64FromFile(strpath);  
    ShowInfo(basestr);
}

function SetWaterType()
{
    if (document.getElementById("Radio6").checked) {
        axCam_Ocx.SetAddMark(0);
    }
    if (document.getElementById("Radio7").checked) {
        axCam_Ocx.SetAddMark(1); //使能水印
    }
    if (document.getElementById("Radio8").checked) {
        axCam_Ocx.SetAddMark(1); //使能水印
        var waterType =1; //水印类型为图片类型
    }
}



var hide_show = 0;
function ShowHide() {

    if (hide_show == 0) {
        hide_show = 1;
        document.getElementById("axCam_Ocx").style.visibility = "hidden";
    }
    else {
        hide_show = 0;
        document.getElementById("axCam_Ocx").style.visibility = "visible";
    }

}

/*****************************副摄像头操作部分*******************************************

*******副摄像头默认以1600*1200的分辨率打开，如需切换分辨率等，请参考主摄像头操作部分*****

*****************************************************************************************/


//打开副摄像头
function StartVideo2() {

    var id = axCam_Ocx2.GetSecondCameraID();  //获取副摄像头ID
    var VidroFormat=1;      //打开格式：0->YUY；1->MJPG
    var i = axCam_Ocx2.CAM_Open(id, VidroFormat, 1600, 1200);
    if (i == 0) ShowInfo("开启副摄像头成功");
    else ShowInfo("开启副摄像头失败");

}

//关闭副摄像头
function CloseVideo2() {
    axCam_Ocx2.CAM_Close()
    ShowInfo("关闭副摄像头");
}

//副摄像头拍照

var captureCount2=0;

function Capture2() {

    captureCount2 = captureCount2 + 1;
    var path = "D:\\CamTwoImg_" + captureCount2 + ".jpg";
    var baseStr = axCam_Ocx2.CaptureImage(path);
    var imgobj1 = document.getElementById("img1");
    imgobj1.src = "data:;base64," + baseStr;
    var info = "拍照成功\n" + "位置:" + path;
    ShowInfo(info);
    
}



</script>

<script type="text/javascript" for="axCam_Ocx" event="GetImageFileName(fileName);"> </script>

</head>

	<body onload ="loadActiveX();GetDevice();">
		<input type="hidden" name ="tempUploadPath" id="tempUploadPath" value="${tempUploadPath}"/>
		<input type="hidden" name ="uploadPath" id="uploadPath" value="${uploadPath}"/>
		<input type="hidden" name ="captureFilePath" id="captureFilePath"/>
		<input type="hidden" name ="captureFileName" id="captureFileName"/>
		
		
		<div style="display: inline;">
			<div style="width:600px; height: 590px;float:left">
				<div>
					<input id="Button1" class="btn btn-primary" type="button" value="打开设备" onclick = "StartVideo();"/>
					<input id="Button3" class="btn btn-primary" type="button" value="拍照" onclick = "Capture();"/>
					<input id="Button2" class="btn btn-primary" type="button" value="关闭设备" onclick = "CloseVideo();"/>
					<input id="Button2" class="btn btn-primary" type="button" value="上传图片" onclick = "UpdataFile();"/>
				</div>
				<div id='ActiveXDivOne' width="100%" height="544px"></div>
			</div>
			
			<div style="float: left;margin-left: 50px;margin-top: 20px;">
				<div>
					拍照照片
				</div>
				<div style="border: 1px dashed #000;width: 500px ;height: 400px"> 
					<img id="img1" alt="" src=""/>
				</div>
			</div>
		</div>
		
		
		
        
	</body>

</html>	