package com.levo.dockerexample.controller;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;

@RestController
@RequestMapping("ws")
public class CftClientController {
	
	@RequestMapping(value = "/sherlock", method = RequestMethod.GET)
	public String callSherlock() throws IOException {
		
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(System.getenv("PROXY_HOST"), Integer.valueOf(System.getenv("PROXY_PORT"))), new
				   UsernamePasswordCredentials(System.getenv("PROXY_USER"), System.getenv("PROXY_PASSWORD")));
		
		//Creating the HttpClientBuilder
		HttpClientBuilder clientbuilder = HttpClients.custom();
		clientbuilder = clientbuilder.setDefaultCredentialsProvider(credsProvider);

		CloseableHttpClient client = clientbuilder.build();
		
		HttpHost proxyHost = new HttpHost(System.getenv("PROXY_HOST"), Integer.valueOf(System.getenv("PROXY_PORT")), "https");
		
		RequestConfig.Builder reqconfigconbuilder= RequestConfig.custom();
		reqconfigconbuilder = reqconfigconbuilder.setProxy(proxyHost);
		RequestConfig requestConfig = reqconfigconbuilder.build();
		
				
		try{
			
			
		    HttpGet httpGet = new HttpGet(System.getenv("SHERLOCK_URL"));
		    
		    httpGet.setConfig(requestConfig);
		    String strResp = null;
		 
		    CloseableHttpResponse response = client.execute(httpGet);
		    
		    try {
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
		        }
		    } finally {
		        response.close();
		    }
		    
		    assertTrue(response.getStatusLine().getStatusCode()==200);
		    
			
			return strResp;
		}catch(Exception e){
			return "Exception:" + e.getMessage();
		}finally{
			try {
				client.close();
			} catch (IOException ioe) {
				return "IOException" + ioe.getMessage();
			}
		}
		
	}
	
}
