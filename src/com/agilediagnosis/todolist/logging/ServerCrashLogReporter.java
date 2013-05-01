package com.agilediagnosis.todolist.logging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.agilediagnosis.todolist.TodoListApplication;
import com.agilediagnosis.todolist.web.HttpMethods;

public class ServerCrashLogReporter implements ReportSender {
	
	private static final String TAG = ServerCrashLogReporter.class.getSimpleName();
	
	private final Map<ReportField, String> mMapping = new HashMap<ReportField, String>() ;
	
	@Override
	public void send(CrashReportData report) throws ReportSenderException {
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("todo_list", "[{\"pk\": 1, \"model\": \"todo_list.todo\", \"fields\": {\"completed\": false, \"title\": \"Finish Android app\"}}]"));
		String response = HttpMethods.POST(TodoListApplication.REPORT_CRASH_LOG_URL,
				 nameValuePairs);
ALog.v(TAG, "response = " + response);
		
		/*
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(report.size());
		for (ReportField key : report.keySet()) {
			ALog.v(TAG, "key " + key + " => " + report.get(key));
			//nameValuePairs.add(new BasicNameValuePair(key.toString(), report.get(key).toString()));
			
		}
		nameValuePairs.add(new BasicNameValuePair("APP_VERSION_CODE", "APP_VERSION_CODE"));
		nameValuePairs.add(new BasicNameValuePair("PHONE_MODEL", "PHONE_MODEL"));
		nameValuePairs.add(new BasicNameValuePair("STACK_TRACE", "STACK_TRACE"));
		String response = HttpMethods.POST(TodoListApplication.REPORT_CRASH_LOG_URL,
						 nameValuePairs);
		ALog.v(TAG, "response = " + response);
		for (NameValuePair nvp : nameValuePairs) {
			ALog.v(TAG, "name = " + nvp.getName() + ", value = " + nvp.getValue());
		}*/
		/*
		report.
	    final Map<String, String> finalReport = remap(report);

	    try {
	        OutputStreamWriter osw = new OutputStreamWriter(crashReport);

	        Set set = finalReport.entrySet();
	        Iterator i = set.iterator();

	        while (i.hasNext()) {
	            Map.Entry<String,String> me = (Map.Entry) i.next();
	            osw.write("[" + me.getKey() + "]=" + me.getValue());
	        }

	        osw.flush();
	        osw.close();
	    } catch (IOException e) {
	        Log.e("TAG", "IO ERROR",e);
	    }
*/
	}
}
