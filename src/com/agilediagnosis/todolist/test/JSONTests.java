package com.agilediagnosis.todolist.test;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.agilediagnosis.todolist.model.Todo;
import com.agilediagnosis.todolist.web.JSONParser;

import junit.framework.Assert;
import android.test.AndroidTestCase;


public class JSONTests extends AndroidTestCase {

	public void testParseTodos() throws Throwable {
		List<Todo> todos = new ArrayList<Todo>();
		List<String> fields = new ArrayList<String>();
		Assert.assertFalse(JSONParser.parseTodos(null, todos, fields));
		try {
			Assert.assertTrue(JSONParser.parseTodos("", todos, fields));
			Assert.fail();
		} catch(Exception e) {
		}
		String strJSON = "[{\"pk\": 1, \"model\": \"todo_list.todo\", \"fields\": {\"completed\": false, \"title\": \"Finish Android app\"}}]";
		JSONParser.parseTodos(strJSON, todos, fields);
		Assert.assertTrue(todos.size() == 1 && fields.size() == 1);
	}

	public void testTodoToJSON() throws Throwable {
		Todo todo = new Todo(1, "Test", false);
		JSONObject jso = JSONParser.todoToJSON(todo);
		JSONArray jsa = new JSONArray();
		jsa.put(jso);
		String strJSON = jsa.toString();
		List<Todo> todos = new ArrayList<Todo>();
		List<String> fields = new ArrayList<String>();
		JSONParser.parseTodos(strJSON, todos, fields);
		Assert.assertTrue(todos.get(0).getTitle().equals(todo.getTitle()) &&
						  todos.get(0).getPk() == todo.getPk() &&
						  todos.get(0).isCompleted() == todo.isCompleted());
	}
	
	public void testParseStatus() throws Throwable {
		Assert.assertTrue(JSONParser.parseStatus("{\"status\":1}"));
		Assert.assertFalse(JSONParser.parseStatus("{\"status\":0}"));
	}
	
	public void testTodosToNVP() throws Throwable {
		List<Todo> todos = new ArrayList<Todo>();
		List<String> fields = new ArrayList<String>();
		String strJSON = "[{\"pk\": 1, \"model\": \"todo_list.todo\", \"fields\": {\"completed\": false, \"title\": \"Finish Android app\"}}]";
		JSONParser.parseTodos(strJSON, todos, fields);
		List<NameValuePair> nvps = JSONParser.todosToNVP(todos);
		Assert.assertTrue(nvps.size() == 1);
	}
}