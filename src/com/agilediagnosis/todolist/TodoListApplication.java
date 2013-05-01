package com.agilediagnosis.todolist;

import com.agilediagnosis.todolist.logging.ALog;

import android.app.Application;

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
	
	public void onCreate() {
		super.onCreate();
		ALog.v(TAG, "");
	}
}