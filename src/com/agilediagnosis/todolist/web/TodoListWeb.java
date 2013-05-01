package com.agilediagnosis.todolist.web;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;

import com.agilediagnosis.todolist.TodoListApplication;
import com.agilediagnosis.todolist.logging.ALog;
import com.agilediagnosis.todolist.model.Todo;

public class TodoListWeb {
	
	private static final String TAG = TodoListWeb.class.getSimpleName();
	
	/**
	 * Get Todos from web app server and fill provided lists with Todos/Todo titles
	 */
	public static boolean getTodos(List<Todo> todos,
								   List<String> fields) {
		boolean ret = false;
		ALog.v(TAG, "");
		switch (TodoListApplication.todoWebFormat) {
			case JSON: {
				ALog.v(TAG, "using JSON...");
				String strJSON = HttpMethods.GET(TodoListApplication.TODO_LIST_WEB_APP_URL, null);
				try {
					ret = JSONParser.parseTodos(strJSON,
												todos,
												fields);
				} catch (JSONException e) {
					e.printStackTrace();
					ret = false;
				}
				break;
			}
			default: {
				ALog.e(TAG, "unknown web format!");
				break;
			}
		}
		return ret;
	}
	
	/**
	 * Set Todos on web app server with provided List of Todos
	 */
	public static boolean setTodos(List<Todo> todos) {
		ALog.v(TAG, "");
		boolean ret = false;
		switch (TodoListApplication.todoWebFormat) {
			case JSON: {
				ALog.v(TAG, "using JSON...");
				try {
					List<NameValuePair> nameValuePairs = JSONParser.todosToNVP(todos);
					if (nameValuePairs != null) {
						String strJSON = HttpMethods.POST(TodoListApplication.TODO_LIST_WEB_APP_URL, nameValuePairs);
						if (strJSON != null) {
							ret = JSONParser.parseStatus(strJSON);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			}
			default: {
				ALog.e(TAG, "unknown web format!");
				break;
			}
		}
		return ret;
	}
}