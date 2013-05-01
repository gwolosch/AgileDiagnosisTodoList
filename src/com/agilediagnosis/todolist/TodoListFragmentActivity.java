package com.agilediagnosis.todolist;

import com.agilediagnosis.todolist.base.TodoListBaseFragmentActivity;
import com.agilediagnosis.todolist.logging.ALog;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

public class TodoListFragmentActivity extends TodoListBaseFragmentActivity {
	
	private static final String TAG = TodoListBaseFragmentActivity.class.getSimpleName();

	private FragmentManager fragmentManager = null;
	private TodoListListFragment todoListListFragment = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	ALog.v(TAG, "");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        fragmentManager = getSupportFragmentManager();
        todoListListFragment = (TodoListListFragment)fragmentManager.findFragmentById(R.id.listFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	ALog.v(TAG, "");
        getMenuInflater().inflate(R.menu.todo_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	ALog.v(TAG, "");
        switch (item.getItemId()) {
	        case R.id.action_refresh: {
	        	ALog.v(TAG, "refreshing");
	        	todoListListFragment.refreshTodos();
	            return true;
	        }
	        default: {
	            return super.onOptionsItemSelected(item);
	        }
        }
    }
}
