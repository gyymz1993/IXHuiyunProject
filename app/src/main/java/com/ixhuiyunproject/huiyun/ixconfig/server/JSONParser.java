package com.ixhuiyunproject.huiyun.ixconfig.server;

import android.util.Log;

import com.lidroid.xutils.http.client.util.URLEncodedUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;


public class JSONParser {

	static InputStream is=null;
    static JSONObject jObj=null;
    static String json="";
    //constructor
    public JSONParser()
    {}
    
    public JSONObject makeHttpRequest(String url,String method,List<NameValuePair> params)
    {
        try{
        	// check for request method
            if(method.equals("POST")){
            	// request method is POST
            	// defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }else if(method.equals("GET")){
            	// request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?"+ paramString;
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
                Log.d("Log_tag", " GET url is"+url.toString());
            }
            else
            {
            	Log.d("Log_tag", "Params Error");
            }
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch(ClientProtocolException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null) {
                sb.append(line + "\n");
                Log.d("log_tag", "line is :"+line+sb.toString());
            }
            
            is.close();
            json = sb.toString();
            Log.d("Log_tag", "this json is "+json.toString());
        } catch(Exception e) {
            Log.d("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try{
            jObj = new JSONObject(json);
        } catch(JSONException e) {
            Log.d("Log_Debug", "this json"+json.toString());
            Log.d("JSON Parser", "Error parsing data " + e.toString());
        }
        return  jObj;
    }
    
    public static void main(String[] args) {}
    
  
}

class Result {
	private String result;
	private Map<String, Object> data;
	@SuppressWarnings("rawtypes")
	private List device;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	@SuppressWarnings("rawtypes")
	public List getDevice() {
		return device;
	}
	@SuppressWarnings("rawtypes")
	public void setDevice(List device) {
		this.device = device;
	}

}
