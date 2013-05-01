package com.agilediagnosis.todolist;

import com.agilediagnosis.todolist.base.TodoListBaseFragmentActivity;
import com.agilediagnosis.todolist.logging.ALog;

import android.os.Bundle;
import android.view.Menu;

public class TodoListFragmentActivity extends TodoListBaseFragmentActivity {
	
	private static final String TAG = TodoListBaseFragmentActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	ALog.v(TAG, "");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	ALog.v(TAG, "");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.todo_list, menu);
        return true;
    }
    
}
