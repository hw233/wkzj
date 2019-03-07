package com.jtang.core.utility;

import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jtang.core.context.SpringContext;

public class HttpUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

	private static ThreadSafeClientConnManager cm;
	private static HttpClient httpclient;
	
	static {
		try {
			cm = new ThreadSafeClientConnManager();
			Integer maxTotal = (Integer) SpringContext.getBean("httputil.maxtotal");
			cm.setMaxTotal(maxTotal);

			httpclient = new DefaultHttpClient(cm);
			Integer connectionTimeout = (Integer) SpringContext.getBean("httputil.connection.timeout");
			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectionTimeout);
			Integer soTimeout = (Integer) SpringContext.getBean("httputil.so.timeout");
			httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, soTimeout);
			Boolean useProxy = (Boolean) SpringContext.getBean("httputil.useproxy");
			if (useProxy) {
				String proxyIp =  (String) SpringContext.getBean("httputil.proxy.ip");
				Integer proxyPort =  (Integer) SpringContext.getBean("httputil.proxy.port");
				HttpHost proxy = new HttpHost(proxyIp, proxyPort, "http");
				httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			}
			
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager ccm = httpclient.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", 443, ssf));

			
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} 
		
	}
	
	public static String sendGet(String url, Map<String, String> params, boolean encodeParams) {
		return sendGet(url, map2Prefix(params, encodeParams));
	}

	public static String sendGet(String url, Map<String, String> params) {
		return sendGet(url, map2Prefix(params, false));
	}
	
	public static String sendGet(String url, String prefix) {
		if (StringUtils.isNotBlank(prefix)) {
			if (url.indexOf("?") < 1) {
				url += "?";
			}
		}
		return sendGet(url.concat(prefix));
	}

	/**
	 * 向指定URL发送GET方法的请求
	 * @param urlPath	已拼合好的url地址
	 * @return
	 */
	public static String sendGet(String urlPath) {
		try {			
			HttpGet httpget = new HttpGet(urlPath);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(String.format("httputil request url:[%s] ", urlPath));
			}
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = null;
			try {
				entity = response.getEntity();
				return EntityUtils.toString(entity);
			} finally {
				if(entity != null) {
					EntityUtils.consume(entity);	
				}
				
				if (LOGGER.isDebugEnabled()) {
					int statusCode = response.getStatusLine().getStatusCode();
					LOGGER.debug(String.format("url:[%s] status:[%s]", urlPath, statusCode));
				}
			}
		} catch (Exception e) {
			LOGGER.error("{}", e);
		}
		return "";
	}
	
	/**
	 * 向指定 URL 发送POST方法的请求
	 * @param url		发送请求的 URL
	 * @param params	post的字符串
	 * @return
	 */
	public static String sendPost(String url, String params) {
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new ByteArrayEntity(params.getBytes("UTF-8")));
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = null;
			try {
				entity = response.getEntity();
				return EntityUtils.toString(entity);
			} finally {
				if (entity != null) {
					EntityUtils.consume(entity);
				}

				int statusCode = response.getStatusLine().getStatusCode();
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(String.format("url:[%s] prefix:[%s] status:[%s]", url, params, statusCode));
				}
			}
		} catch (Exception e) {
			LOGGER.error("{}", e);
		}
		return "";
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * @param url		发送请求的 URL
	 * @param param		请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, Map<String, String> paramsMaps) {
		try {
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> entry : paramsMaps.entrySet()) {
				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = null;
			try {
				entity = response.getEntity();
				return EntityUtils.toString(entity);
			} finally {
				if (entity != null) {
					EntityUtils.consume(entity);
				}
				
				int statusCode = response.getStatusLine().getStatusCode();
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(String.format("url:[%s] prefix:[%s] status:[%s]", url, map2Prefix(paramsMaps, false), statusCode));
				}
			}
		} catch (Exception e) {
			LOGGER.error("{}", e);
		}
		return "";
	}

	public static String map2Prefix(Map<String, String> data, boolean encodeParams) {
		StringBuilder sb = new StringBuilder();
		try {

			for (Entry<String, String> entry : data.entrySet()) {
				if (encodeParams) {
					sb.append("&" + entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "utf-8"));
				} else {
					sb.append("&" + entry.getKey() + "=" + entry.getValue());
				}
			}

			if (sb.length() > 1) {
				return sb.substring(1).toString();
			}
			return "";
		} catch (Exception ex) {
			LOGGER.warn("{}", ex);
		}
		return "";
	}
	

}
