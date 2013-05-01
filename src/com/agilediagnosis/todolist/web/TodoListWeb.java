package com.agilediagnosis.todolist.web;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;

import com.agilediagnosis.todolist.TodoListApplication;
import com.agilediagnosis.todolist.logging.ALog;
import com.agilediagnosis.todolist.model.Todo;

public class TodoListWeb {
	
	private static final String TAG = TodoListWeb.class.getSimpleName();
	
	public static boolean getTodos(List<Todo> todos,
								   List<String> fields) {
		boolean ret = false;
		ALog.v(TAG, "");
		switch (TodoListApplication.todoWebFormat) {
			case JSON: {
				ALog.v(TAG, "using JSON...");
				String strJSON = HttpMethods.GET(TodoListApplication.TODO_LIST_WEB_APP_URL, null);
				try {
					//TODO test
					//strJSON = "[{\"pk\": 1, \"model\": \"todo_list.todo\", \"fields\": {\"completed\": false, \"title\": \"FIRST Finish Android app\"}}, {\"pk\": 1, \"model\": \"todo_list.todo\", \"fields\": {\"completed\": false, \"title\": \"Finish Android app maoeu aoeu aoe uaeou oaeu aeou aeou oaeu aoeu aoeu aoeu oaeu aoeu aeou aeo\"}}, {\"pk\": 1, \"model\": \"todo_list.todo\", \"fields\": {\"completed\": false, \"title\": \"Finish Android app\"}}, {\"pk\": 1, \"model\": \"todo_list.todo\", \"fields\": {\"completed\": false, \"title\": \"Finish Android app\"}}, {\"pk\": 1, \"model\": \"todo_list.todo\", \"fields\": {\"completed\": false, \"title\": \"Finish Android app\"}}, {\"pk\": 1, \"model\": \"todo_list.todo\", \"fields\": {\"completed\": false, \"title\": \"Finish Android app\"}},{\"pk\": 1, \"model\": \"todo_list.todo\", \"fields\": {\"completed\": false, \"title\": \"Finish Android app\"}}, {\"pk\": 1, \"model\": \"todo_list.todo\", \"fields\": {\"completed\": false, \"title\": \"Finish Android app\"}}, {\"pk\": 1, \"model\": \"todo_list.todo\", \"fields\": {\"completed\": false, \"title\": \"Finish Android app\"}}, {\"pk\": 1, \"model\": \"todo_list.todo\", \"fields\": {\"completed\": false, \"title\": \"Finish Android app\"}}, {\"pk\": 1, \"model\": \"todo_list.todo\", \"fields\": {\"completed\": false, \"title\": \"Finish Android app\"}}, {\"pk\": 1, \"model\": \"todo_list.todo\", \"fields\": {\"completed\": false, \"title\": \"Finish Android app\"}}, {\"pk\": 1, \"model\": \"todo_list.todo\", \"fields\": {\"completed\": false, \"title\": \"Finish Android app\"}}, {\"pk\": 1, \"model\": \"todo_list.todo\", \"fields\": {\"completed\": false, \"title\": \"Finish Android app\"}}, {\"pk\": 1, \"model\": \"todo_list.todo\", \"fields\": {\"completed\": false, \"title\": \"Finish Android app\"}}, {\"pk\": 1, \"model\": \"todo_list.todo\", \"fields\": {\"completed\": false, \"title\": \"Finish Android app\"}}, {\"pk\": 1, \"model\": \"todo_list.todo\", \"fields\": {\"completed\": false, \"title\": \"LAST Finish Android app\"}}]";
					//end TODO
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
	
	public static boolean setTodos(List<Todo> todos) {
		ALog.v(TAG, "");
		boolean ret = false;
		switch (TodoListApplication.todoWebFormat) {
			case JSON: {
				ALog.v(TAG, "using JSON...");
				try {
					List<NameValuePair> nameValuePairs = JSONParser.todosToNVP(todos);
					String strJSON = HttpMethods.POST(TodoListApplication.TODO_LIST_WEB_APP_URL, nameValuePairs);
					ret = JSONParser.parseStatus(strJSON);
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