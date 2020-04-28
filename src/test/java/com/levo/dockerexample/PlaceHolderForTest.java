package com.levo.dockerexample;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;

public class PlaceHolderForTest {
	@Test
	public void whenSendPostRequestUsingHttpClient_thenCorrect() 
	  throws ClientProtocolException, IOException {
	    CloseableHttpClient client = HttpClients.createDefault();
	    HttpPost httpPost = new HttpPost("http://www.example.com");
	 
	    List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("username", "John"));
	    params.add(new BasicNameValuePair("password", "pass"));
	    httpPost.setEntity(new UrlEncodedFormEntity(params));
	 
	    CloseableHttpResponse response = client.execute(httpPost);
	    assertTrue(response.getStatusLine().getStatusCode()==200);
	    client.close();
	}
	
	@Test
	public void whenSendGetRequestUsingHttpClient_thenCorrect() 
	  throws ClientProtocolException, IOException {
	    CloseableHttpClient client = HttpClients.createDefault();
		
//		HttpClientBuilder clientbuilder = HttpClients.custom();
//	    CloseableHttpClient client = clientbuilder.build();
	    
	    CloseableHttpResponse response = null;
	    String strResp = null;
	    
	    try {
	    	
	    	HttpHost proxyHost = new HttpHost("18.141.10.185", 80, "http");
			
			RequestConfig.Builder reqconfigconbuilder= RequestConfig.custom();
			reqconfigconbuilder = reqconfigconbuilder.setProxy(proxyHost);
			RequestConfig requestConfig = reqconfigconbuilder.build();
		    
		    HttpGet httpGet = new HttpGet("https://sherlock-heroku.herokuapp.com/api/helloworld");
		    httpGet.setConfig(requestConfig);
		   
		 
		    response = client.execute(httpGet);
	    	
	        HttpEntity entity = response.getEntity();
	        if (entity != null) {
	        	InputStream instream = entity.getContent();
	        	
	        	ByteSource byteSource = new ByteSource() {
	                @Override
	                public InputStream openStream() throws IOException {
	                    return instream;
	                }
	            };
	            
	            strResp = byteSource.asCharSource(Charsets.UTF_8).read();
	            instream.close();
	            response.close();
	    		System.out.println("strResp:" + strResp);
	    		assertTrue(response.getStatusLine().getStatusCode()==200);
	        }
	    }catch (Exception e){ 
	    	e.printStackTrace();
	    }
	    finally {
	    	try{
		    	client.close();
		    }catch(IOException ioe) {
		    	ioe.printStackTrace();
		    }
		    
	    }
	    
	}
}
