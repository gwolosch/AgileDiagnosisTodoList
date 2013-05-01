package com.agilediagnosis.todolist.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.agilediagnosis.todolist.logging.ALog;
import com.agilediagnosis.todolist.model.Todo;

public class JSONParser {
	private static final String TAG = JSONParser.class.getSimpleName();
	
	private static final String PK_KEY = "pk";
	private static final String FIELDS_KEY = "fields";
	private static final String COMPLETED_KEY = "completed";
	private static final String TITLE_KEY = "title";
	private static final String MODEL_KEY = "model";
	private static final String MODEL_VALUE = "todo_list.todo";
	
	private static final String TODO_LIST_KEY = "todo_list";
	
	private static final String STATUS_KEY = "status";
	
	/**
	 * Take JSON String and fill provided lists with list of Todos/Todo titles
	 * parsed from that String
	 * e.g. String: [{"pk": 1, "model": "todo_list.todo", "fields": {"completed": false, "title": "Finish Android app"}}]
	 */
	public static boolean parseTodos(String strJSON,
									 List<Todo> todos,
									 List<String> fields) throws JSONException {
		ALog.v(TAG, "strJSON = " + strJSON);
		boolean ret = false;
		if (strJSON == null || todos == null || todos == null || fields == null) {
			ALog.e(TAG, "Error invalid input!");
			return ret;
		}
		JSONArray jsa = new JSONArray(strJSON);
		for (int i = 0; i < jsa.length(); i++) {
			JSONObject todoJso = jsa.getJSONObject(i);
			int pk = todoJso.getInt(PK_KEY);
			JSONObject fieldsJso = todoJso.getJSONObject(FIELDS_KEY);
			String title = fieldsJso.getString(TITLE_KEY);
			boolean completed = fieldsJso.getBoolean(COMPLETED_KEY);
			todos.add(new Todo(pk, title, completed));
			fields.add(title);
		}
		ret = true;
		return ret;
	}
	
	public static JSONObject todoToJSON(Todo todo) throws JSONException {
		ALog.v(TAG, "");
		if (todo == null) {
			ALog.e(TAG, "todo is null!");
			return null;
		}
		JSONObject todoJso = new JSONObject();
		todoJso.put(PK_KEY, todo.getPk());
		todoJso.put(MODEL_KEY, MODEL_VALUE);
		
		JSONObject fieldsJso = new JSONObject();
		fieldsJso.put(COMPLETED_KEY, todo.isCompleted());
		fieldsJso.put(TITLE_KEY, todo.getTitle());
		
		todoJso.put(FIELDS_KEY, fieldsJso);
		return todoJso;
	}
	
	/**
	 * Convert List of Todos to NameValuePair which can be used in a POST request
	 */
	public static List<NameValuePair> todosToNVP(List<Todo> todos) throws JSONException {
		ALog.v(TAG, "");
		if (todos == null) {
			ALog.e(TAG, "todos is null!");
			return null;
		}
		JSONArray jsa = new JSONArray();
		for (Todo todo : todos) {
			JSONObject jso = todoToJSON(todo);
			if (jso != null) {
				jsa.put(jso);
			}
		}
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair(TODO_LIST_KEY, jsa.toString()));
		return nameValuePairs;
	}
	
	/**
	 * Parse status of POST request from provide JSON String
	 */
	public static boolean parseStatus(String strJSON) throws JSONException {
		ALog.v(TAG, "strJSON = " + strJSON);
		if (strJSON == null) {
			ALog.v(TAG, "invalid input!");
			return false;
		}
		JSONObject jso = new JSONObject(strJSON);
		return jso.getInt(STATUS_KEY) == 1;
	}	
}