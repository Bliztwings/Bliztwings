package com.ehyf.ewashing.util;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.io.SAXReader;


public class HttpClientUtil {
	
    private static Log log = LogFactory.getLog(HttpClientUtil.class);

    
	public static CloseableHttpResponse getInstanceClientResponse(CloseableHttpClient httpClient,String url,CookieStore cookieStore) throws ClientProtocolException, IOException {
		HttpGet get = new HttpGet(url);

		return HttpClients.custom().setDefaultCookieStore(cookieStore).build().execute(get);
	}
	
	public static CloseableHttpClient newInstance() {
		return HttpClients.createDefault();
	}
	
	public static void closeConnect(CloseableHttpClient httpClient ,CloseableHttpResponse response,InputStream inputStream){
		try {
			if(response!=null){
				response.close();
			}
			if(inputStream!=null){
				inputStream.close();
			}
			httpClient.close();
		} catch (IOException e) {
			
		}
	}
	
	public static CookieStore setCookieStore(HttpServletRequest req){
		CookieStore cookieStore = null;
		HttpSession session =req.getSession();
		Object sessionCookieStore = session.getAttribute("cookiestore");
		if(sessionCookieStore == null){
			cookieStore = new BasicCookieStore();
			session.setAttribute("cookiestore", cookieStore);
		} else {
			cookieStore = (CookieStore) sessionCookieStore;
		}
		return cookieStore;
	}
	
	public static CloseableHttpResponse post(String cType,String accountSid,String authToken,String timestamp,String url,CloseableHttpClient httpclient,EncryptUtil encryptUtil,String body) throws Exception{
		HttpPost httppost = new HttpPost(url);
		httppost.setHeader("Accept", cType);
		httppost.setHeader("Content-Type", cType+";charset=utf-8");
		String src = accountSid + ":" + timestamp;
		String auth = encryptUtil.base64Encoder(src);
		httppost.setHeader("Authorization", auth);
		BasicHttpEntity requestBody = new BasicHttpEntity();
        requestBody.setContent(new ByteArrayInputStream(body.getBytes("UTF-8")));
        requestBody.setContentLength(body.getBytes("UTF-8").length);
        httppost.setEntity(requestBody);
        // 执行客户端请求
        CloseableHttpResponse response = httpclient.execute(httppost);
		return response;
	}
	
	
	public static CloseableHttpResponse get(String cType,String accountSid,String authToken,String timestamp,String url,CloseableHttpClient httpclient,EncryptUtil encryptUtil,String body) throws Exception{
        HttpGet httppost = new HttpGet(url);
        httppost.setHeader("Accept", cType);
        httppost.setHeader("Content-Type", cType+";charset=utf-8");
        String src = accountSid + ":" + timestamp;
        String auth = encryptUtil.base64Encoder(src);
        httppost.setHeader("Authorization", auth);
        // 执行客户端请求
        CloseableHttpResponse response = httpclient.execute(httppost);
        return response;
    }
	
	public static JSONObject doYunPost(PropertiesUtil propUtil ,String timestamp ,String url ,EncryptUtil encryptUtil,String body){
		
		String result ="";
		CloseableHttpClient httpClient = HttpClientUtil.newInstance();
		CloseableHttpResponse response =null;
		try {
            response=HttpClientUtil.post("application/json",propUtil.getProperty("accountSid"), propUtil.getProperty("authToken"), timestamp, url, httpClient, encryptUtil, body);
            log.info("response status="+response.getStatusLine());
            if(response.getStatusLine().getStatusCode()==200){
            	HttpEntity entity = response.getEntity();
            	if (entity != null) {
            		result = EntityUtils.toString(entity, "UTF-8");
            		System.out.println(result);
            	}
            	EntityUtils.consume(entity);
            }
            return JSONObject.fromObject(result);
        }
        catch (IOException e) {
            log.error("IO exception for vist " + url);
        }
        catch (Exception e) {
            log.error("Occure error while vist " + url +". Reason: " + e.getMessage());
        }finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
            }
            
        }
        return null;
	}

    public static JSONObject doYunGet(PropertiesUtil propUtil,
            String timestamp, String url, EncryptUtil encryptUtil, Object object) throws Exception {
        String result ="";
        CloseableHttpClient httpClient = HttpClientUtil.newInstance();
        CloseableHttpResponse response=HttpClientUtil.get("application/json",propUtil.getProperty("accountSid"), propUtil.getProperty("authToken"), timestamp, url, httpClient, encryptUtil, null);
        if(response.getStatusLine().getStatusCode()==200){
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
                System.out.println(result);
            }
            EntityUtils.consume(entity);
        }
        return JSONObject.fromObject(result);
    }

    public static void doPost(String requestUrl, String content, String mobile) throws ClientProtocolException, IOException {
        CloseableHttpClient httpClient = HttpClientUtil.newInstance();
        String result ="";
        HttpPost httppost = new HttpPost(requestUrl);
        StringEntity reqEntity=new StringEntity("phone="+mobile+"&content="+content,"utf-8");  
        reqEntity.setContentType("application/x-www-form-urlencoded");  
        httppost.setEntity(reqEntity);  
        CloseableHttpResponse response =httpClient.execute(httppost);  
        if(response.getStatusLine().getStatusCode()==200){
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
                System.out.println("发送短信成功"+result);
            }
            EntityUtils.consume(entity);
        }
    }

	public static String doWeixinPost(String asXML, String url) throws ParseException, IOException {
		CloseableHttpClient httpClient = HttpClientUtil.newInstance();
		String result = "";
		HttpPost httppost = new HttpPost(url);
		StringEntity reqEntity = new StringEntity(asXML);
		reqEntity.setContentType("application/x-www-form-urlencoded");
		httppost.setEntity(reqEntity);
		httppost.setHeader("Content-Type", "text/xml;charset=ISO-8859-1");
		CloseableHttpResponse response = httpClient.execute(httppost);
		if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
			}
			EntityUtils.consume(entity);
		}
		return result;
	}
	
	public static String doWeixinPostNew(String xmlData, String url) {

		String resp;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			URL httpUrl = new URL(url);
			HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
			httpURLConnection.setRequestProperty("Host", "api.mch.weixin.qq.com");
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setConnectTimeout(10 * 1000);
			httpURLConnection.setReadTimeout(10 * 1000);
			httpURLConnection.connect();
			outputStream = httpURLConnection.getOutputStream();
			outputStream.write(xmlData.getBytes("UTF-8"));

			// 获取内容
			inputStream = httpURLConnection.getInputStream();

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			final StringBuffer stringBuffer = new StringBuffer();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line);
			}
			resp = stringBuffer.toString();

			if (stringBuffer != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return resp;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}
	
}
