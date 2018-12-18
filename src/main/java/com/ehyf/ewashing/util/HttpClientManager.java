package com.ehyf.ewashing.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultHttpResponseParser;
import org.apache.http.impl.conn.DefaultHttpResponseParserFactory;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.LineParser;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.ehyf.ewashing.wechat.proxy.WeChatProxy;




class HttpClientManager {

	private static HttpClientManager instance = new HttpClientManager();
	
	private PoolingHttpClientConnectionManager connManager = null;
	
	private CloseableHttpClient httpclient = null;
	
	private Logger log = Logger.getLogger(WeChatProxy.class);
	
	private HttpClientManager() {
		init();
	}
	
	public static HttpClientManager getInstance() {
		return instance;
	}
	
	private void init() {
		
    	// Use custom message parser / writer to customize the way HTTP
        // messages are parsed from and written out to the data stream.
        HttpMessageParserFactory<HttpResponse> responseParserFactory = new DefaultHttpResponseParserFactory() {

            @Override
            public HttpMessageParser<HttpResponse> create(
                SessionInputBuffer buffer, MessageConstraints constraints) {
                LineParser lineParser = new BasicLineParser() {

                    @Override
                    public Header parseHeader(final CharArrayBuffer buffer) {
                        try {
                            return super.parseHeader(buffer);
                        } catch (ParseException ex) {
                            return new BasicHeader(buffer.toString(), null);
                        }
                    }

                };
                return new DefaultHttpResponseParser(
                    buffer, lineParser, DefaultHttpResponseFactory.INSTANCE, constraints) {

                    @Override
                    protected boolean reject(final CharArrayBuffer line, int count) {
                        // try to ignore all garbage preceding a status line infinitely
                        return false;
                    }

                };
            }

        };
        HttpMessageWriterFactory<HttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();

        // Use a custom connection factory to customize the process of
        // initialization of outgoing HTTP connections. Beside standard connection
        // configuration parameters HTTP connection factory can define message
        // parser / writer routines to be employed by individual connections.
        HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(
                requestWriterFactory, responseParserFactory);

        // Create a registry of custom connection socket factories for supported
        // protocol schemes.
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", PlainConnectionSocketFactory.INSTANCE)
            .build();

        // Use custom DNS resolver to override the system DNS resolution.
        DnsResolver dnsResolver = new SystemDefaultDnsResolver() {

            @Override
            public InetAddress[] resolve(final String host) throws UnknownHostException {
                if (host.equalsIgnoreCase("myhost")) {
                    return new InetAddress[] { InetAddress.getByAddress(new byte[] {127, 0, 0, 1}) };
                } else {
                    return super.resolve(host);
                }
            }

        };

        // Create a connection manager with custom configuration.
        connManager = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry, connFactory, dnsResolver);

        // Create socket configuration
        SocketConfig socketConfig = SocketConfig.custom()
            .setTcpNoDelay(true)
            .build();
        // Configure the connection manager to use socket configuration either
        // by default or for a specific host.
        connManager.setDefaultSocketConfig(socketConfig);
        //connManager.setSocketConfig(new HttpHost("somehost", 80), socketConfig);

        // Create message constraints
        MessageConstraints messageConstraints = MessageConstraints.custom()
            .setMaxHeaderCount(200)
            .setMaxLineLength(2000)
            .build();
        // Create connection configuration
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
            .setMalformedInputAction(CodingErrorAction.IGNORE)
            .setUnmappableInputAction(CodingErrorAction.IGNORE)
            .setCharset(Consts.UTF_8)
            .setMessageConstraints(messageConstraints)
            .build();
        
        // Configure the connection manager to use connection configuration either
        // by default or for a specific host.
        connManager.setDefaultConnectionConfig(connectionConfig);
        //connManager.setConnectionConfig(new HttpHost("somehost", 80), ConnectionConfig.DEFAULT);

        // Configure total max or per route limits for persistent connections
        // that can be kept in the pool or leased by the connection manager.
        connManager.setMaxTotal(600);
        connManager.setDefaultMaxPerRoute(100);
        //connManager.setMaxPerRoute(new HttpRoute(new HttpHost("10.52.10.240", 18080)), 100);
        
        ConnectionKeepAliveStrategy keepAliveStrat = new DefaultConnectionKeepAliveStrategy() {

            @Override
            public long getKeepAliveDuration(
                    HttpResponse response,
                    HttpContext context) {
                long keepAlive = super.getKeepAliveDuration(response, context);
                if (keepAlive == -1) {
                    // Keep connections alive 5 seconds if a keep-alive value
                    // has not be explicitly set by the server
                    keepAlive = 5000;
                }
                return keepAlive;
            }

        };
        // Create an HttpClient with the given custom dependencies and configuration.
        httpclient = HttpClients.custom()
            .setConnectionManager(connManager)
            .setKeepAliveStrategy(keepAliveStrat)
            .build();
	}
	
	/**
	 * POST HTTP JSON请求. <br>
	 * @author chenxiangbai 2012-12-6 下午4:52:43 <br> 
	 * @param url post的URL，例如：http://10.52.2.1:8080/Resteasy/ResteasyService/postTestInfo
	 * @param param post参数的键值对.
	 * @return HttpResponse.
	 * @throws Exception 
	 * @throws NoSuchAlgorithmException 
	 */
	public String httpPostSSL(final String url, final BasicNameValuePair... param) throws Exception {
		
		TrustManager[] tm = { new MyX509TrustManager() };  
        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");  
        sslContext.init(null, tm, new java.security.SecureRandom());
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,new String[] { "TLSv1" },null,SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
   
		HttpPost httpPost = new HttpPost(url);
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		if (param != null) {
			for (BasicNameValuePair nameValuePair : param) {
				nvps.add(nameValuePair);
			}
		}

		CloseableHttpResponse response = null;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
			response = httpclient.execute(httpPost);
			String result = null;
			if (response.getEntity() != null) {
				result = EntityUtils.toString(response.getEntity());
			}
		     
		    return result;
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
		    try {
		    	if (response != null) {
		    		response.close();
		    	}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		}
		
		return null;
		
	}
	
	public String httpGetSSL(final String url) throws Exception{
		 TrustManager[] tm = { new MyX509TrustManager() };  
         SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");  
         sslContext.init(null, tm, new java.security.SecureRandom());
         SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,new String[] { "TLSv1" },null,SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
         httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		        try {
		            HttpGet httpget = new HttpGet(url);

		            //log.info("httpGetSSL " + httpget.getRequestLine());

		            // Create a custom response handler
		            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

		                public String handleResponse(
		                        final HttpResponse response) throws ClientProtocolException, IOException {
		                    int status = response.getStatusLine().getStatusCode();
		                    response.setHeader("Content-type", "text/html; charset=utf-8");
		                    if (status >= 200 && status < 300) {
		                        HttpEntity entity = response.getEntity();
		                        
		                        return entity != null ? EntityUtils.toString(entity,"utf-8") : null;
		                    } else {
		                        throw new ClientProtocolException("Unexpected response status: " + status);
		                    }
		                }

		            };
		            String responseBody = httpclient.execute(httpget, responseHandler);
		            return responseBody;
		        } finally {
		            httpclient.close();
		        }
		    }
	
	public String uploadFile(final String url,final String filePath) throws Exception, IOException{
		String result = null;
	    CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
    
		HttpPost  httpPost = new HttpPost(url);
		log.info("uploadFile :" + httpPost.getRequestLine());
		FileBody bin = new FileBody(new File(filePath));
		HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("bin", bin).build();
		httpPost.setEntity(reqEntity);
		
		//System.out.println("executing request " +bin.getFilename());
		CloseableHttpResponse response = httpclient.execute(httpPost);
        try {
            //System.out.println("----------------------------------------");
            //System.out.println(response.getStatusLine());
            HttpEntity resEntity = response.getEntity();
            
            if (resEntity != null) {
                //System.out.println("Response content length: " + resEntity.getContentLength());
            }
            result = EntityUtils.toString(resEntity);
            EntityUtils.consume(resEntity);
            System.out.println(response.getEntity().toString());
        } finally {
            response.close();
        }
    } finally {
    	httpclient.close();
    }
        return result;
	}
	
	public String httpPostSSL(String url,String json) throws Exception{
		
		 TrustManager[] tm = { new MyX509TrustManager() };  
         SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");  
         sslContext.init(null, tm, new java.security.SecureRandom());
         SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,new String[] { "TLSv1" },null,SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
         httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
         
         HttpPost post = new HttpPost(url);
         StringEntity s = new StringEntity(json,"utf-8");
         s.setContentType("application/json");
         post.setEntity(s);
         log.info("httpPostSSL : " + post.getRequestLine());
         
         CloseableHttpResponse response = null;
 		try {
 			
 			response = httpclient.execute(post);
 			String result = null;
 			if (response.getEntity() != null) {
 				result = EntityUtils.toString(response.getEntity());
 			}
 		     
 		    return result;
 		} catch (UnsupportedEncodingException e1) {
 			e1.printStackTrace();
 		} catch (ClientProtocolException e1) {
 			e1.printStackTrace();
 		} catch (IOException e1) {
 			e1.printStackTrace();
 		} finally {
 		    try {
 		    	if (response != null) {
 		    		response.close();
 		    	}
 				
 			} catch (IOException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
 		    
 		}
 		return null;
	}
	

	 
	 public byte[] httpGetByte(final String url) throws Exception {
		 TrustManager[] tm = { new MyX509TrustManager() };  
         SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");  
         sslContext.init(null, tm, new java.security.SecureRandom());
         SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,new String[] { "TLSv1" },null,SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
         httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		        try {
		            HttpGet httpget = new HttpGet(url);

		            log.info("httpGetByte : " + httpget.getRequestLine());

		            // Create a custom response handler
		            ResponseHandler<byte[]> responseHandler = new ResponseHandler<byte[]>() {

		                public byte[] handleResponse(
		                        final HttpResponse response) throws ClientProtocolException, IOException {
		                    int status = response.getStatusLine().getStatusCode();
		                    
		                    if (status >= 200 && status < 300) {
		                        HttpEntity entity = response.getEntity();
		                        
		                        return entity != null ? EntityUtils.toByteArray(entity) : null;
		                    } else {
		                        throw new ClientProtocolException("Unexpected response status: " + status);
		                    }
		                }

		            };
		            byte[] responseBody = httpclient.execute(httpget, responseHandler);
		            return responseBody;
		        } finally {
		            httpclient.close();
		        }
	 }
	 
	 
	 /**
		 * POST HTTP JSON请求. <br>
		 * @author chenxiangbai 2012-12-6 下午4:52:43 <br> 
		 * @param url post的URL，例如：http://10.52.2.1:8080/Resteasy/ResteasyService/postTestInfo
		 * @param param post参数的键值对.
		 * @return HttpResponse.
		 */
		public String httpPost(final String url, final BasicNameValuePair... param) {
			  CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			log.info("httpPost " + httpPost.getRequestLine());
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			if (param != null) {
				for (BasicNameValuePair nameValuePair : param) {
					nvps.add(nameValuePair);
				}
			}

			CloseableHttpResponse response = null;
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
				response = httpclient.execute(httpPost);
				String result = null;
				if (response.getEntity() != null) {
					result = EntityUtils.toString(response.getEntity());
				}
			     
			    return result;
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			} catch (ClientProtocolException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
			    try {
			    	if (response != null) {
			    		response.close();
			    	}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    
			}
			
			return null;
			
		}
		
		
		 /**
		 * POST HTTP JSON请求. <br>
		 * @author huxiaoyuan 2014-07-06 下午4:52:43 <br> 
		 * @param url post的URL，例如：http://10.52.2.1:8080/Resteasy/ResteasyService/postTestInfo
		 * @param param post参数的键值对.
		 * @return HttpResponse.
		 */
		public String httpPostGrc(final String url, Map<String,String> paramMap,String method) {
			  CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-type","text/xml;charset=utf-8");
			StringBuffer data = new StringBuffer();
			data.append("<?xml version='1.0' encoding='utf-8'?>");
			data.append("<xml>");
			if(paramMap != null){
				int size = paramMap.size();
				httpPost.setHeader("CALLKIND", "0");
				httpPost.setHeader("CALLNAME","com.ygsoft.gris.grc.rc.publicapi.service.service.IWXService."+method);
				httpPost.setHeader("PARAMCOUNT", size+"");
				httpPost.setHeader("encodeName", "utf-8");
				httpPost.setHeader("ClientType", "2");
				httpPost.setHeader("EnableZip", "0");				
				
				int i=0;
				for(String key : paramMap.keySet()){
					data.append("<param"+i+">" + paramMap.get(key) +"</param"+i+">");
					i++;
				}
			}
			data.append("</xml>");
		

			CloseableHttpResponse response = null;
			try {
				httpPost.setEntity(new StringEntity(data.toString(), "utf-8"));
				response = httpclient.execute(httpPost);
				String result = null;
				if (response.getEntity() != null) {
					result = EntityUtils.toString(response.getEntity());
				}
			     
			    return result;
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			} catch (ClientProtocolException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
			    try {
			    	if (response != null) {
			    		response.close();
			    	}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    
			}
			
			return null;
			
		}

}





