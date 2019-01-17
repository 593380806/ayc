package com.ayc.framework.common;
/**
 * APIConnector.java
 */

import com.alibaba.fastjson.JSON;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.*;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Yate
 * @date Oct 17, 2014
 * @description Api杩炴帴鑰�
 * @version 1.0
 */
@Deprecated
public final class ApiConnector {
    private static final Logger logger = LoggerFactory
            .getLogger(ApiConnector.class);

    private static PoolingHttpClientConnectionManager connManager = null;
    private static CloseableHttpClient httpclient = null;

    private static Header DEFAULT_HEADER = new BasicHeader(
            HttpHeaders.USER_AGENT, "pay-engine");

    /**
     * 鏈�ぇ杩炴帴鏁�
     */
    public final static int MAX_TOTAL_CONNECTIONS = 800;
    /**
     * 鑾峰彇杩炴帴鐨勬渶澶х瓑寰呮椂闂�
     */
    public final static int WAIT_TIMEOUT = 60000;
    /**
     * 姣忎釜璺敱鏈�ぇ杩炴帴鏁�
     */
    public final static int MAX_ROUTE_CONNECTIONS = 400;
    /**
     * 杩炴帴瓒呮椂鏃堕棿
     */
    public final static int CONNECT_TIMEOUT = 30000;
   static CookieStore cookieStore = new BasicCookieStore();
    static {
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                .<ConnectionSocketFactory> create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .build();

        connManager = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry);

        // Create socket configuration
        SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true)
                .build();

        connManager.setDefaultSocketConfig(socketConfig);
        // Create message constraints
        MessageConstraints messageConstraints = MessageConstraints.custom()
                .setMaxHeaderCount(200).setMaxLineLength(2000).build();
        // Create connection configuration
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(Consts.UTF_8)
                .setMessageConstraints(messageConstraints).build();
        connManager.setDefaultConnectionConfig(connectionConfig);
        connManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        connManager.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
        
        httpclient = HttpClients
                .custom()
                .setDefaultCookieStore(cookieStore)
                .setConnectionManager(connManager)
                .setRetryHandler(DefaultHttpRequestRetryHandler.INSTANCE)
                .setConnectionReuseStrategy(
                        DefaultConnectionReuseStrategy.INSTANCE)
                .setKeepAliveStrategy(
                        DefaultConnectionKeepAliveStrategy.INSTANCE).build();
    }

    public static String post(String url, List<NameValuePair> pairs,
            String encoding) {
        HttpPost post = new HttpPost(url.trim());
        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(CONNECT_TIMEOUT)
                    .setConnectTimeout(CONNECT_TIMEOUT)
                    .setConnectionRequestTimeout(CONNECT_TIMEOUT)
                    .setExpectContinueEnabled(false).build();
            post.setConfig(requestConfig);
            post.addHeader(DEFAULT_HEADER);

            if (pairs != null && pairs.size() > 0) {
                post.setEntity(new UrlEncodedFormEntity(pairs, encoding));
            }

//            logger.info("[HttpUtils Post] begin invoke url:{} , params:{}",
//                    url, pairs);
//            long s1 = System.currentTimeMillis();
            CloseableHttpResponse response = httpclient.execute(post);
//            long s2 = System.currentTimeMillis() - s1;
            try {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    post.abort();
                    logger.error("[HttpUtils Post] error");
                    return "";
                }

                HttpEntity entity = response.getEntity();
                try {
                    if (entity != null) {
                        String str = EntityUtils.toString(entity, encoding);
//                        logger.info(
//                                "[HttpUtils Post]Debug response, url : {}  , params : {}, response string : {} ,time : {}",
//                                url, pairs, str, s2);
                        return str;
                    }
                } finally {
                    if (entity != null) {
                        entity.getContent().close();
                    }
                }
            } finally {
                if (response != null) {
                    response.close();
                }
            }
        } catch (Exception e) {
            logger.error(
                    "[HttpUtils Post] error");
        } finally {
            post.releaseConnection();
        }
        return "";
    }

    public static String post(String url, List<NameValuePair> pairs) {
        HttpPost post = new HttpPost(url.trim());
        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(CONNECT_TIMEOUT)
                    .setConnectTimeout(CONNECT_TIMEOUT)
                    .setConnectionRequestTimeout(CONNECT_TIMEOUT)
                    .setExpectContinueEnabled(false).build();
            post.setConfig(requestConfig);
            post.addHeader(DEFAULT_HEADER);

            if (pairs != null && pairs.size() > 0) {
                post.setEntity(new UrlEncodedFormEntity(pairs, Consts.UTF_8));
            }

//            logger.info("[HttpUtils Post] begin invoke url:{} , params:{}",
//                    url, pairs);
//            long s1 = System.currentTimeMillis();
            CloseableHttpResponse response = httpclient.execute(post);
//            long s2 = System.currentTimeMillis();
            try {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    post.abort();
                    logger.error(
                            "[HttpUtils Post] error");
                    return "";
                }

                HttpEntity entity = response.getEntity();
                try {
                    if (entity != null) {
                        String str = EntityUtils.toString(entity, Consts.UTF_8);
//                        logger.info(
//                                "[HttpUtils Post]Debug response, url : {}  , params : {}, response string : {} ,time : {}",
//                                url, pairs, str, s2 - s1);
                        return str;
                    }
                } finally {
                    if (entity != null) {
                        entity.getContent().close();
                    }
                }
            } finally {
                if (response != null) {
                    response.close();
                }
            }
        } catch (Exception e) {
            logger.error(
                    "[HttpUtils Post] error");
        } finally {
            post.releaseConnection();
        }
        return "";
    }

    @SuppressWarnings("deprecation")
    public static String get(String url, List<NameValuePair> pairs,
            String encode) {
        String responseString = null;
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(CONNECT_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(CONNECT_TIMEOUT).build();

        StringBuilder sb = new StringBuilder();
        sb.append(url.trim());
        int i = 0;
        if (pairs != null && pairs.size() > 0) {
            for (NameValuePair entry : pairs) {
                if (i == 0 && !url.contains("?")) {
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(entry.getName());
                sb.append("=");
                String value = entry.getValue();
                try {
                    sb.append(URLEncoder.encode(value, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    logger.warn("encode http get params error, value is {}",
                            value, e);
                    sb.append(URLEncoder.encode(value));
                }
                i++;
            }
        }

//        logger.info("[HttpUtils Get] begin invoke:" + sb.toString());
        HttpGet get = new HttpGet(sb.toString());
        get.setConfig(requestConfig);
        get.addHeader(DEFAULT_HEADER);

        try {
//            long s1 = System.currentTimeMillis();
            CloseableHttpResponse response = httpclient.execute(get);
//            long s2 = System.currentTimeMillis();
            try {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    get.abort();
                    logger.error(
                            "[HttpUtils Get] error");
                    return "";
                }

                HttpEntity entity = response.getEntity();
                try {
                    if (entity != null) {
                        responseString = EntityUtils.toString(entity, encode);
                    }
                } finally {
                    if (entity != null) {
                        entity.getContent().close();
                    }
                }
            } catch (Exception e) {
                logger.error("[HttpUtils Get]get response error, url:{}",
                        url.toString(), e);
                return responseString;
            } finally {
                if (response != null) {
                    response.close();
                }
            }
//            logger.info(
//                    "[HttpUtils Get]Debug url:{} , response string :{},time={}",
//                    url.toString(), responseString, s2 - s1);
        } catch (Exception e) {
            logger.error(
                    "[HttpUtils Get] error");
        } finally {
            get.releaseConnection();
        }
        return responseString;
    }

    public static String postJson(String url, JSON data, String encode) {
        String responseString = null;

//        logger.info("[HttpUtils PostJson] begin invoke:{},param:{}",
//                url.toString(), data);
        HttpPost post = new HttpPost(url.trim());
        post.setEntity(new StringEntity(data.toJSONString(),
                ContentType.APPLICATION_JSON));

        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(CONNECT_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(CONNECT_TIMEOUT).build();
        post.setConfig(requestConfig);
        post.addHeader(DEFAULT_HEADER);

        try {
//            long s1 = System.currentTimeMillis();
            CloseableHttpResponse response = httpclient.execute(post);
//            long s2 = System.currentTimeMillis();
            try {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    post.abort();
                    logger.error(
                            "[HttpUtils PostJson] error");
                    return "";
                }

                HttpEntity entity = response.getEntity();
                try {
                    if (entity != null) {
                        responseString = EntityUtils.toString(entity, encode);
                    }
                } finally {
                    if (entity != null) {
                        entity.getContent().close();
                    }
                }
            } catch (Exception e) {
                logger.error("[HttpUtils PostJson]post response error, url:{}",
                        url.toString(), e);
                return responseString;
            } finally {
                if (response != null) {
                    response.close();
                }
            }
//            logger.info(
//                    "[HttpUtils PostJson]Debug response, url : {}  , params : {}, response string : {} ,time : {}",
//                    url, data, responseString, s2 - s1);
        } catch (Exception e) {
            logger.error(
                    "[HttpUtils PostJson] error", e);
        } finally {
            post.releaseConnection();
        }
        return responseString;
    }

    public static String postJson(String url, JSON data) {
        String responseString = null;

//        logger.info("[HttpUtils PostJson] begin invoke:{},param:{}",
//                url.toString(), data);
        HttpPost post = new HttpPost(url.trim());
        post.setEntity(new StringEntity(data.toJSONString(),
                ContentType.APPLICATION_JSON));

        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(CONNECT_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(CONNECT_TIMEOUT).build();
        post.setConfig(requestConfig);
        post.addHeader(DEFAULT_HEADER);

        try {
//            long s1 = System.currentTimeMillis();
            CloseableHttpResponse response = httpclient.execute(post);
//            long s2 = System.currentTimeMillis();
            try {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    post.abort();
                    return "";
                }

                HttpEntity entity = response.getEntity();
                try {
                    if (entity != null) {
                        responseString = EntityUtils.toString(entity,
                                Consts.UTF_8);
                    }
                } finally {
                    if (entity != null) {
                        entity.getContent().close();
                    }
                }
            } catch (Exception e) {
                logger.error("[HttpUtils PostJson]post response error, url:{}",
                        url.toString(), e);
                return responseString;
            } finally {
                if (response != null) {
                    response.close();
                }
            }
//            logger.info(
//                    "[HttpUtils PostJson]Debug url:{} , response string :{},time={}",
//                    url.toString(), responseString, s2 - s1);
        } catch (Exception e) {
            logger.error(
                    "[HttpUtils PostJson] error", e);
        } finally {
            post.releaseConnection();
        }
        return responseString;
    }

    @SuppressWarnings("deprecation")
    public static String get(String url, List<NameValuePair> pairs) {
        String responseString = null;
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(CONNECT_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(CONNECT_TIMEOUT).build();

        StringBuilder sb = new StringBuilder();
        sb.append(url.trim());
        int i = 0;
        if (pairs != null && pairs.size() > 0) {
            for (NameValuePair entry : pairs) {
                if (i == 0 && !url.contains("?")) {
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(entry.getName());
                sb.append("=");
                String value = entry.getValue();
                try {
                    sb.append(URLEncoder.encode(value, Consts.UTF_8.name()));
                } catch (UnsupportedEncodingException e) {
                    logger.warn("encode http get params error, value is {}",
                            value, e);
                    sb.append(URLEncoder.encode(value));
                }
                i++;
            }
        }

//        logger.info("[HttpUtils Get] begin invoke:{}", sb.toString());
        HttpGet get = new HttpGet(sb.toString());
        get.setConfig(requestConfig);
        get.addHeader(DEFAULT_HEADER);

        try {
            CloseableHttpResponse response = httpclient.execute(get);
            
//            String cookie_user = null;
//            String JSESSIONID = "";
            List<Cookie> cookies = cookieStore.getCookies();
            for (int m = 0; m < cookies.size(); m++) {
                if (cookies.get(m).getName().equals("JSESSIONID")) {
//                    JSESSIONID = cookies.get(m).getValue();
                }
                if (cookies.get(m).getName().equals("cookie_user")) {
//                    cookie_user = cookies.get(m).getValue();
                }
            }
//            System.out.println(JSESSIONID +"#"+JSON.toJSONString(cookies));
            
            
            try {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    get.abort();
                    logger.error(
                            "[HttpUtils Get] error");
                    return "";
                }
                HttpEntity entity = response.getEntity();
                try {
                    if (entity != null) {
                        responseString = EntityUtils.toString(entity,
                                Consts.UTF_8);
                    }
                } finally {
                    if (entity != null) {
                        entity.getContent().close();
                    }
                }
            } catch (Exception e) {
                logger.error("[HttpUtils Get]get response error, url:{}",
                        url.toString(), e);
                return responseString;
            } finally {
                if (response != null) {
                    response.close();
                }
            }
//            logger.info(
//                    "[HttpUtils Get]Debug url:{} , response string :{},time={}",
//                    url.toString(), responseString, s2 - s1);
        } catch (Exception e) {
            logger.error(
                    "[HttpUtils PostJson] error", e);
        } finally {
            get.releaseConnection();
        }
        return responseString;
    }

    /**
     * HTTPS璇锋眰锛岄粯璁よ秴鏃朵负5S
     * 
     * @param url
     * @param params
     * @return
     */
    public static String connectPostHttps(String url, Map<String, String> params) {

        String responseContent = null;

        HttpPost httpsPost = new HttpPost(url);
        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(CONNECT_TIMEOUT)
                    .setConnectTimeout(CONNECT_TIMEOUT)
                    .setConnectionRequestTimeout(CONNECT_TIMEOUT).build();

            List<NameValuePair> formParams = new ArrayList<NameValuePair>();
            httpsPost.setEntity(new UrlEncodedFormEntity(formParams,
                    Consts.UTF_8));
            httpsPost.setConfig(requestConfig);
            httpsPost.addHeader(DEFAULT_HEADER);

            // 缁戝畾鍒拌姹�Entry
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formParams.add(new BasicNameValuePair(entry.getKey(), entry
                        .getValue()));
            }
            CloseableHttpResponse response = httpclient.execute(httpsPost);
            try {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    httpsPost.abort();
                    logger.error(
                            "[HttpUtils Security] error");
                    return "";
                }
                // 鎵цPOST璇锋眰
                HttpEntity entity = response.getEntity(); // 鑾峰彇鍝嶅簲瀹炰綋
                try {
                    if (null != entity) {
                        responseContent = EntityUtils.toString(entity,
                                Consts.UTF_8);
                    }
                } finally {
                    if (entity != null) {
                        entity.getContent().close();
                    }
                }
            } finally {
                if (response != null) {
                    response.close();
                }
            }
//            logger.info("requestURI : " + httpsPost.getURI()
//                    + ", responseContent: " + responseContent);
        } catch (ClientProtocolException e) {
            logger.error("ClientProtocolException", e);
        } catch (IOException e) {
            logger.error("IOException", e);
        } finally {
            httpsPost.releaseConnection();
        }
        return responseContent;

    }
    
    /***************http寮傛璇锋眰****************************/
    
    @SuppressWarnings("deprecation")
	public static void asyncGet(String url, List<NameValuePair> pairs) {
		
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(CONNECT_TIMEOUT)
				.setConnectTimeout(CONNECT_TIMEOUT)
				.setConnectionRequestTimeout(CONNECT_TIMEOUT)
				.setExpectContinueEnabled(false).build();
		
		final StringBuilder sb = new StringBuilder();
		sb.append(url.trim());
		int i = 0;
		if (pairs != null && pairs.size() > 0) {
			for (NameValuePair entry : pairs) {
				if (i == 0 && !url.contains("?")) {
					sb.append("?");
				} else {
					sb.append("&");
				}
				sb.append(entry.getName());
				sb.append("=");
				String value = entry.getValue();
				try {
					sb.append(URLEncoder.encode(value, Consts.UTF_8.name()));
				} catch (UnsupportedEncodingException e) {
					logger.warn("encode http get params error, value is {}",
							value, e);
					sb.append(URLEncoder.encode(value));
				}
				i++;
			}
		}

		final CloseableHttpAsyncClient asyncClient = HttpAsyncClients.createDefault();
		final HttpGet httpGet = new HttpGet(sb.toString());
		httpGet.setConfig(requestConfig);
		httpGet.addHeader(DEFAULT_HEADER);

		asyncClient.start();
		asyncClient.execute(httpGet, new FutureCallback<HttpResponse>() {

			@Override
			public void failed(Exception ex) {
				logger.error("[HttpAsyncUtils failed] error:url:{},ex:{}",sb.toString() ,ex);
				try {
					httpGet.releaseConnection();
					asyncClient.close();
				} catch (IOException e) {
					logger.error("IOException:{}",e);
				}

			}

			@Override
			public void completed(HttpResponse result) {
				int statusCode = result.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					httpGet.abort();
					logger.error(
							"[HttpAsyncUtils completed] error, url : {} ,  status :{}",
							sb.toString(), statusCode);
					return;
				}
				 HttpEntity entity = result.getEntity();
	                try {
	                    if (entity != null) {
	                        try {
								logger.info("[HttpAsyncUtils completed] response:{}", EntityUtils.toString(entity,
								        Consts.UTF_8));
							} catch (ParseException e) {
								logger.error("ParseException:{}",e);
							} catch (IOException e) {
								logger.error("IOException:{}",e);
							}
	                    }
	                } finally {
	                        try {
	                        	if (entity != null) {
	                        		entity.getContent().close();
								}
	                        	httpGet.releaseConnection();
								asyncClient.close();
							} catch (UnsupportedOperationException e) {
								logger.error("UnsupportedOperationException:{}",e);
							} catch (IOException e) {
								logger.error("IOException:{}",e);
							}
	                    }
			}

			@Override
			public void cancelled() {
				logger.error("[HttpAsyncUtils cancelled] error:url:{}",sb.toString());
				try {
					httpGet.releaseConnection();
					asyncClient.close();
				} catch (IOException e) {
					logger.error("IOException:{}",e);
				}
			}
		});

	}

    
    
    

}
