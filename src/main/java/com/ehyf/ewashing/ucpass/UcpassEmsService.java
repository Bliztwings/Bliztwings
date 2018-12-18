package com.ehyf.ewashing.ucpass;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.ParseException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ehyf.ewashing.util.EncryptUtil;
import com.ehyf.ewashing.util.HttpClientUtil;
import com.ehyf.ewashing.util.PropertiesUtil;
import com.ehyf.ewashing.wechat.proxy.WeChatProxy;

import net.sf.json.JSONObject;

@Service
public class UcpassEmsService {

	private static PropertiesUtil propUtil =new PropertiesUtil("config.properties");
	private Logger logger =Logger.getLogger(UcpassEmsService.class);

	/**
	 * 发送短信验证码
	 * @param code
	 * @param mobile
	 * @return
	 */
	public JSONObject sendEms(String code,String mobile,String appId) {

		String templateId ="";
		String yunAppId ="";
		if(WeChatProxy.judgeAppIdType(appId)==1){
			templateId =propUtil.getProperty("ems_swy");
			yunAppId =propUtil.getProperty("yunSwyAppId");
			
		}
		if(WeChatProxy.judgeAppIdType(appId)==2){
			templateId =propUtil.getProperty("ems_hyf");
			yunAppId =propUtil.getProperty("yunHyfAppId");
		}
		
        try {
            // MD5加密
            EncryptUtil encryptUtil = new EncryptUtil();
            SimpleDateFormat sb = new SimpleDateFormat("yyyyMMddHHmmss");
            String timestamp = sb.format(new Date());
            String signature = getSignature(propUtil.getProperty("accountSid"),
                    propUtil.getProperty("authToken"), timestamp, encryptUtil);

            String url = getStringBuffer().append("/")
                    .append(propUtil.getProperty("SoftVersion"))
                    .append("/Accounts/")
                    .append(propUtil.getProperty("accountSid"))
                    .append("/Messages/templateSMS").append("?sig=")
                    .append(signature).toString();
            JSONObject jsonParma = new JSONObject();
            JSONObject jsonBody = new JSONObject();
            jsonParma.put("appId", yunAppId);
            jsonParma.put("templateId", templateId);
            jsonParma.put("to", mobile);
            jsonParma.put("param", code);
            jsonBody.put("templateSMS", jsonParma);
            String body = jsonBody.toString();
            return HttpClientUtil.doYunPost(propUtil, timestamp, url,
                    encryptUtil, body);
        }
        catch (ParseException e) {
            logger.error(e.getMessage());
        }
        catch (IOException e) {
            logger.error(e.getMessage());
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }
	
	public static String getSignature(String accountSid, String authToken,
            String timestamp, EncryptUtil encryptUtil) throws Exception {
        String sig = accountSid + authToken + timestamp;
        String signature = encryptUtil.md5Digest(sig);
        return signature;
    }
	
	public static StringBuffer getStringBuffer() {
        StringBuffer sb = new StringBuffer();
        sb.append(propUtil.getProperty("yunUrl"));
        return sb;
    }
	
	public static void main(String[] args) {
		UcpassEmsService u=new UcpassEmsService();
		u.sendNoticeEms("13871201075");
	}

	public JSONObject sendNoticeEms(String mobile) {
		String templateId ="244075";
		String yunAppId =propUtil.getProperty("yunSwyAppId");
        try {
            // MD5加密
            EncryptUtil encryptUtil = new EncryptUtil();
            SimpleDateFormat sb = new SimpleDateFormat("yyyyMMddHHmmss");
            String timestamp = sb.format(new Date());
            String signature = getSignature(propUtil.getProperty("accountSid"),
                    propUtil.getProperty("authToken"), timestamp, encryptUtil);

            String url = getStringBuffer().append("/")
                    .append(propUtil.getProperty("SoftVersion"))
                    .append("/Accounts/")
                    .append(propUtil.getProperty("accountSid"))
                    .append("/Messages/templateSMS").append("?sig=")
                    .append(signature).toString();
            JSONObject jsonParma = new JSONObject();
            JSONObject jsonBody = new JSONObject();
            jsonParma.put("appId", yunAppId);
            jsonParma.put("templateId", templateId);
            jsonParma.put("to", mobile);
            //jsonParma.put("param", message);
            jsonBody.put("templateSMS", jsonParma);
            String body = jsonBody.toString();
            return HttpClientUtil.doYunPost(propUtil, timestamp, url,
                    encryptUtil, body);
        }
        catch (ParseException e) {
            logger.error(e.getMessage());
        }
        catch (IOException e) {
            logger.error(e.getMessage());
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
	}
}
