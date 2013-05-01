package com.agilediagnosis.todolist;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

class LockableScrollView extends ScrollView {
	
    public LockableScrollView(Context context, AttributeSet set) {
		super(context, set);
	}

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
		return super.onTouchEvent(ev);
    }
}
