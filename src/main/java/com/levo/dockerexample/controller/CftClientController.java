package com.levo.dockerexample.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteSource;

@RestController
@RequestMapping("ws")
public class CftClientController {
	
	@RequestMapping(value = "/sherlock", method = RequestMethod.GET)
	public String callSherlock() {
		
		
		CloseableHttpClient client = null;
//		CloseableHttpResponse response =  null;		
		try{
			
			//Creating the HttpClientBuilder
			HttpClientBuilder clientbuilder = HttpClientBuilder.create();
	    	SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustAllStrategy()).build();
			clientbuilder.setSSLContext(sslContext);
//			
//			Collection<Header> defaultHeaders = new ArrayList<Header>();
//			String userAndPass = System.getenv("PROXY_USER") + ":" + System.getenv("PROXY_PASSWORD");
//			Header header = new BasicHeader("Authorization","Basic " + BaseEncoding.base64().encode(userAndPass.getBytes("UTF-8")));
//			defaultHeaders.add(header);
//			
//			BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
			
			
//			credsProvider.setCredentials(new AuthScope(System.getenv("PROXY_HOST"), Integer.valueOf(System.getenv("PROXY_PORT"))), new
//					   UsernamePasswordCredentials(System.getenv("PROXY_USER"), System.getenv("PROXY_PASSWORD")));
			
//			credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(System.getenv("PROXY_USER"), System.getenv("PROXY_PASSWORD")));
//			
//			clientbuilder.setDefaultCredentialsProvider(credsProvider).setDefaultHeaders(defaultHeaders);
			
			
			client = clientbuilder.build();
			
			HttpHost proxyHost = new HttpHost(System.getenv("PROXY_HOST"), Integer.valueOf(System.getenv("PROXY_PORT")), "http");
			
			RequestConfig.Builder reqconfigconbuilder= RequestConfig.custom();
			reqconfigconbuilder = reqconfigconbuilder.setProxy(proxyHost);
			RequestConfig requestConfig = reqconfigconbuilder.build();
			
		    HttpGet httpGet = new HttpGet(System.getenv("SHERLOCK_URL"));
		    
		    httpGet.setConfig(requestConfig);
//		    String strResp = null;
//		    HttpClientContext context = HttpClientContext.create();
//		    context.setCredentialsProvider(credsProvider);
//		    response = client.execute(httpGet, context);
		    
		    
//		    String result = StringUtils.EMPTY;
		    try(CloseableHttpResponse response = client.execute(httpGet)) {
//		        CookieManager.touch(response);
		        String strResp = EntityUtils.toString(response.getEntity());
		        System.out.println("strResp:" + strResp);
		        return "Return(inside):" + strResp;
		    } finally {
		    	httpGet.releaseConnection();
		    }
		    
		    
//	        HttpEntity entity = response.getEntity();
//	        if (entity != null) {
//	        	InputStream instream = entity.getContent();
//	        	
//	        	ByteSource byteSource = new ByteSource() {
//	                @Override
//	                public InputStream openStream() throws IOException {
//	                    return instream;
//	                }
//	            };
//	            
//	            strResp = byteSource.asCharSource(Charsets.UTF_8).read();
//	            instream.close();
//	            
//	        }
		    
//			return "Return:" + strResp;
		}catch(Exception e){
			System.out.println(e);
			return "Exception:" + e.getMessage();
		}
		
//		finally{
//			try {
//				if(null!=response){
//					response.close();
//				}
//				if(null!=client){
//					client.close();
//				}
//			} catch (IOException ioe) {
//				ioe.printStackTrace();
//				return "IOException" + ioe.getMessage();
//			}
//		}
		
	}
	
}
