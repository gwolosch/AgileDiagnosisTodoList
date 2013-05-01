package com.agilediagnosis.todolist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.acra.ACRA;
import org.acra.ErrorReporter;
import org.acra.ReportField;
import org.acra.annotation.ReportsCrashes;

import com.agilediagnosis.todolist.logging.ALog;
import com.agilediagnosis.todolist.logging.ServerCrashLogReporter;

import android.app.Application;

@ReportsCrashes(formKey = "", // will not be used
				customReportContent = { ReportField.APP_VERSION_CODE, ReportField.STACK_TRACE, ReportField.PHONE_MODEL })              

public class TodoListApplication extends Application {
	
	private static final String TAG = TodoListApplication.class.getSimpleName();
	
	/*
	* Globals
	*/
	public static final boolean DEBUG = true;
	
	public enum TodoWebFormat {
		JSON,
	}
	public static final TodoWebFormat todoWebFormat = TodoWebFormat.JSON;
	public static final String TODO_LIST_WEB_APP_URL = "http://www.gregwoloschyn.com";
	public static final int HTTP_TIMEOUT_CONNECTION_MILLIS = 10000;
	public static final int HTTP_TIMEOUT_SOCKET_MILLIS = 15000;

	public static final String REPORT_CRASH_LOG_URL = "http://www.gregwoloschyn.com/crash";
	private static final int MAX_FILE_SIZE = 500000;
	private static final String gitCommitIdFileName = "git-commit-id.txt";
	public static String gitCommitId = "";
	
	public void onCreate() {
		super.onCreate();
		ALog.v(TAG, "");
		
		ACRA.init(this);
		ErrorReporter.getInstance().setReportSender(new ServerCrashLogReporter());
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(gitCommitIdFileName)), MAX_FILE_SIZE);
			gitCommitId = reader.readLine();
			ALog.v(TAG, "read gitCommitId = " + gitCommitId);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}