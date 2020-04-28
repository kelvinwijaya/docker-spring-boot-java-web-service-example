package com.levo.dockerexample.controller;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
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
		CloseableHttpClient client = HttpClients.createDefault();
		try{
			
		    HttpGet httpGet = new HttpGet("https://sherlock-ss-i-1.app.gov.sg/api/helloworld");
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
