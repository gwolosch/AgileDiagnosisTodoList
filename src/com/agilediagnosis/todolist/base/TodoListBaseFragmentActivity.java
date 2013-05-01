package com.agilediagnosis.todolist.base;

import com.agilediagnosis.todolist.logging.ALog;

import android.support.v4.app.FragmentActivity;

public class TodoListBaseFragmentActivity extends FragmentActivity {
	
	private static final String TAG = TodoListBaseFragmentActivity.class.getSimpleName();
	
	private boolean isPaused = true;
	
	public Boolean getIsPaused() {
		return isPaused;
	}

	public void setIsPaused(Boolean isPaused) {
		this.isPaused = isPaused;
	}
    
    @Override
    protected void onResume() {
    	super.onResume();
    	synchronized (this) {
    		ALog.v(TAG, "");
    		isPaused = false;
    	}
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	synchronized (this) {
    		ALog.v(TAG, "");
    		isPaused = true;
    	}
    }
}
