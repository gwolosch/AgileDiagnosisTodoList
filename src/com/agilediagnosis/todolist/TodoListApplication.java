package com.agilediagnosis.todolist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

	private static final int MAX_FILE_SIZE = 500000;
	private static final String gitCommitIdFileName = "git-commit-id.txt";
	public static String gitCommitId = "";
	
	public void onCreate() {
		super.onCreate();
		ALog.v(TAG, "");
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(gitCommitIdFileName)), MAX_FILE_SIZE);
			gitCommitId = reader.readLine();
			ALog.v(TAG, "read gitCommitId = " + gitCommitId);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}