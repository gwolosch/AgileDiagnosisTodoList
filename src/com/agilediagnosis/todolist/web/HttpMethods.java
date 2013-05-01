package com.agilediagnosis.todolist.web;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.agilediagnosis.todolist.TodoListApplication;
import com.agilediagnosis.todolist.logging.ALog;

public class HttpMethods
{
	private static final String TAG = HttpMethods.class.getSimpleName();
	
	public static String inputStreamToString(InputStream content) throws IOException {
		BufferedReader rd = new BufferedReader(new InputStreamReader(content));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();
		return sb.toString();
	}
	
	public static String GET(String _URL,
							 String params) {
		ALog.v(TAG, "");
		String result = null;
		if (_URL.startsWith("http://")) {
			String strURL = _URL;
			if (params != null && params.length () > 0) {
				strURL += "?" + params;
			}
			try {
				HttpParams httpParameters = new BasicHttpParams();
				int timeoutConnection = TodoListApplication.HTTP_TIMEOUT_CONNECTION_MILLIS;
				HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
				int timeoutSocket = TodoListApplication.HTTP_TIMEOUT_SOCKET_MILLIS;
				HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpResponse response = httpclient.execute(new HttpGet(strURL));
				InputStream content = response.getEntity().getContent();
				result = inputStreamToString(content);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static String POST(String _URL,
							  List<NameValuePair> nameValuePairs) {
		ALog.v(TAG, "");
		String result = null;
		if (_URL.startsWith("http://")) {
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = TodoListApplication.HTTP_TIMEOUT_CONNECTION_MILLIS;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			int timeoutSocket = TodoListApplication.HTTP_TIMEOUT_SOCKET_MILLIS;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpPost httppost = new HttpPost(_URL);
			try {
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				InputStream content = response.getEntity().getContent();
				result = inputStreamToString(content);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
