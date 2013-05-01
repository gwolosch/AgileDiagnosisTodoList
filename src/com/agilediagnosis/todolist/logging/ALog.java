package com.agilediagnosis.todolist.logging;

import com.agilediagnosis.todolist.TodoListApplication;

import android.util.Log;

public class ALog {
	private static String messageWithMethod(String message) {
		String method = Thread.currentThread().getStackTrace()[4].getMethodName();
		return method + "(): " + message;
	}
	public static void v(String tag, String message) {
		if (TodoListApplication.DEBUG) Log.v(tag, messageWithMethod(message));
	}
	public static void e(String tag, String message) {
		if (TodoListApplication.DEBUG) Log.e(tag, messageWithMethod(message));
	}
	public static void w(String tag, String message) {
		if (TodoListApplication.DEBUG) Log.w(tag, messageWithMethod(message));
	}
	public static void d(String tag, String message) {
		if (TodoListApplication.DEBUG) Log.d(tag, messageWithMethod(message));
	}
	public static void i(String tag, String message) {
		if (TodoListApplication.DEBUG) Log.i(tag, messageWithMethod(message));
	}
}