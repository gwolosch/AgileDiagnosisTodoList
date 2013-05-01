package com.agilediagnosis.todolist.base;

import com.agilediagnosis.todolist.logging.ALog;

import android.support.v4.app.ListFragment;

public class TodoListBaseListFragment extends ListFragment {
	
	private static final String TAG = TodoListBaseListFragment.class.getSimpleName();
	
	private boolean isPaused = true;
	
	public Boolean getIsPaused() {
		return isPaused;
	}

	public void setIsPaused(Boolean isPaused) {
		this.isPaused = isPaused;
	}
	
    @Override
    public void onResume() {
    	super.onResume();
    	synchronized (this) {
    		ALog.v(TAG, "");
    		isPaused = false;
    	}
    }

    @Override
    public void onPause() {
    	super.onPause();
    	synchronized (this) {
    		ALog.v(TAG, "");
    		isPaused = true;
    	}
    }
}
