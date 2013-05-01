package com.agilediagnosis.todolist;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agilediagnosis.todolist.base.TodoListBaseListFragment;
import com.agilediagnosis.todolist.logging.ALog;
import com.agilediagnosis.todolist.model.Todo;
import com.agilediagnosis.todolist.web.TodoListWeb;

public class TodoListListFragment extends TodoListBaseListFragment {
	
	private static final String TAG = TodoListListFragment.class.getSimpleName();
	
	private Boolean serverTaskRunning = false;
	
    private TodoListAdapter mTodoListAdapter = null;
    private ListView mListView = null;
    private LinearLayout mDataContainer = null;
    private RelativeLayout mNoDataContainer = null;

    private static final int MESSAGE_GET_TODOS_SUCCESS = 0;
    private static final int MESSAGE_GET_TODOS_FAILED = 1;
    private static final int MESSAGE_SET_TODOS_SUCCESS = 2;
    private static final int MESSAGE_SET_TODOS_FAILED = 3;

    public class TodoListAdapter extends BaseAdapter {

        private Context context = null;
		private List<Todo> todos = new ArrayList<Todo>();
        private List<String> fields = new ArrayList<String>();

        public TodoListAdapter(Context context) {
            this.context = context;
        }

        public List<Todo> getTodos() {
			return todos;
		}

		public void setTodos(List<Todo> todos) {
			this.todos = todos;
		}

		public List<String> getFields() {
			return fields;
		}

		public void setFields(List<String> fields) {
			this.fields = fields;
		}

        @Override
        public int getCount() {
            return fields.size();
        }

        @Override
        public Object getItem(int position) {
            return fields.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_list_view_item, null);

            TextView titleTextView = (TextView) convertView.findViewById(R.id.title);
            titleTextView.setText(this.fields.get(position));

            final CheckBox completedCheckBox = (CheckBox) convertView.findViewById(R.id.completed);
            completedCheckBox.setChecked(this.todos.get(position).isCompleted());
            completedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton cb, boolean completed) {
					if (serverTaskRunning) {
						ALog.v(TAG, "server task running. wait for it to finish.");
						Toast.makeText(getActivity(), R.string.server_task_running_wait, Toast.LENGTH_LONG).show();
						completedCheckBox.setChecked(!completed);
					} else {
						todos.get(position).setCompleted(completed);
					}
				}
            	
            });
            return convertView;
        }

    }

	@Override
	public View onCreateView(LayoutInflater inflater,
							 ViewGroup container,
							 Bundle savedInstanceState) {
		
		ALog.v(TAG, "");
		View fragmentRootView = inflater.inflate(R.layout.fragment_todo_list, container, false);
		mListView = (ListView) fragmentRootView.findViewById(android.R.id.list);
		mNoDataContainer = (RelativeLayout) fragmentRootView.findViewById(R.id.no_data_container);
		mDataContainer = (LinearLayout) fragmentRootView.findViewById(R.id.data_container);
		
		
		mTodoListAdapter = new TodoListAdapter(getActivity());
		((Button) fragmentRootView.findViewById(R.id.update_todos_button)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				synchronized(serverTaskRunning) {
					setTodos(mTodoListAdapter.getTodos());
				}
			}
			
		});
		
		mListView.setAdapter((ListAdapter) mTodoListAdapter);
		getTodos();
		
		return fragmentRootView;
	}
	
	public void refreshTodos() {
		ALog.v(TAG, "");
		getTodos();
	}
	
    final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message m) {
        	ALog.v(TAG, "");
        	switch (m.what) {
	        	case MESSAGE_GET_TODOS_SUCCESS: {
	        		ALog.v(TAG, "MESSAGE_GET_TODOS_SUCCESS");
	        		mNoDataContainer.setVisibility(View.GONE);
	                mDataContainer.setVisibility(View.VISIBLE);
	        		ServerTaskResponse resp = (ServerTaskResponse) m.obj;
	        		mTodoListAdapter.setTodos(resp.todos);
	        		mTodoListAdapter.setFields(resp.fields);
	                ((BaseAdapter) mTodoListAdapter).notifyDataSetChanged();
	        		break;
	        	}
	        	case MESSAGE_GET_TODOS_FAILED: {
	        		ALog.v(TAG, "MESSAGE_GET_TODOS_FAILED");
	        		Toast.makeText(getActivity(), R.string.error_getting_todos, Toast.LENGTH_LONG).show();
	        		break;
	        	}
	        	case MESSAGE_SET_TODOS_SUCCESS: {
	        		Toast.makeText(getActivity(), R.string.update_todos_success, Toast.LENGTH_LONG).show();
	        		break;
	        	}
	        	case MESSAGE_SET_TODOS_FAILED: {
	        		Toast.makeText(getActivity(), R.string.update_todos_failed, Toast.LENGTH_LONG).show();
	        		break;
	        	}
	        	default: {
	        		break;
	        	}
        	}
	    	synchronized(serverTaskRunning) {
	    		serverTaskRunning = false;
	    	}
        }
    };

    private class ServerTaskResponse {
 		public List<Todo> todos = null;
 		public List<String> fields = null;
 		public ServerTaskResponse(List<Todo> todos,
 								  List<String> fields) {
 			this.todos = todos;
 			this.fields = fields;
 		}
    }

    /**
     * Provide with a String to show as a dialog, and two Runnables:
     * - one to run in background (doInBackground)
     * - one to run after the task is finished (onPostExecute)
     * Note you must send a message to Handler in onPostExecute
     */
	private class ServerTask extends AsyncTask<Void, Void, Void> {
		
		private boolean status = false;

		private List<Todo> todos = new ArrayList<Todo>();
		private List<String> fields = new ArrayList<String>();
		private ProgressDialog getTodosDialog = null;

		private Runnable doInBackground = null;
		private Runnable onPostExecute = null;
		
		public ServerTask(Context context, String progressDialogStringResId) {
			this.getTodosDialog = ProgressDialog.show(context,
													  "",
													  progressDialogStringResId,
													  true);
			this.getTodosDialog.setCancelable(true);
			this.getTodosDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
	                cancel(true);
	            }
	        });
		}
		
		protected void execute() {
			synchronized(serverTaskRunning) {
				if (serverTaskRunning) {
					ALog.v(TAG, "serverTaskRunning, not starting it.");
					Toast.makeText(getActivity(), R.string.server_task_already_running, Toast.LENGTH_LONG).show();
				} else {
					serverTaskRunning = true;
					super.execute();
				}
			}
		}
		
		@Override
	     protected void onProgressUpdate(Void... arg0) {
	    	 ALog.v(TAG, "");
	     }
	
	     @Override
	     protected void onPostExecute(Void result) {
	    	 ALog.v(TAG, "");
	    	 this.getTodosDialog.dismiss();
	    	 this.onPostExecute.run();
	     }

	     @Override
	     protected Void doInBackground(Void... arg0) {
	    	 ALog.v(TAG, "");
	    	 this.doInBackground.run();
	    	 return null;
	     }
	
	     @Override
	     protected void onCancelled () {
	    	 ALog.v(TAG, "");
	    	 synchronized(serverTaskRunning) {
	    		 serverTaskRunning = false;
	    	 }
	     }

	     public boolean isStatus() {
	    	 return status;
	     }
		
	     public void setStatus(boolean status) {
	    	 this.status = status;
	     }
	
	     public List<Todo> getTodos() {
	    	 return todos;
	     }
		
	     public void setTodos(List<Todo> todos) {
	    	 this.todos = todos;
	     }
		
	     public List<String> getFields() {
	    	 return fields;
	     }
	
		
	     public void setFields(List<String> fields) {
	    	 this.fields = fields;
	     }
		
	     public Runnable getDoInBackground() {
	    	 return doInBackground;
	     }
		
	     public void setDoInBackground(Runnable doInBackground) {
	    	 this.doInBackground = doInBackground;
	     }
		
	     public Runnable getOnPostExecute() {
	    	 return onPostExecute;
	     }
		
	     public void setOnPostExecute(Runnable onPostExecute) {
	    	 this.onPostExecute = onPostExecute;
	     }
	}
	
	/**
	 * Asynchronously get Todo list from server and update UI via Handler
	 */
    private void getTodos() {
    	ALog.v(TAG, "");
    	final ServerTask getTodosTask = new ServerTask(getActivity(), getString(R.string.getting_todos_dialog));
    	getTodosTask.setDoInBackground(new Runnable() {
			@Override
			public void run() {
				getTodosTask.setStatus(TodoListWeb.getTodos(getTodosTask.getTodos(),
															getTodosTask.getFields()));    			
			}
    	});
    	getTodosTask.setOnPostExecute(new Runnable() {
			@Override
			public void run() {
				Message m = new Message();
				if (getTodosTask.isStatus()) {
					m.what = MESSAGE_GET_TODOS_SUCCESS;
					m.obj = new ServerTaskResponse(getTodosTask.getTodos(),
												   getTodosTask.getFields());
				} else {
					m.what = MESSAGE_GET_TODOS_FAILED;
				}
				mHandler.sendMessage(m);
			}
    	});
    	getTodosTask.execute();
    }

    /**
	 * Asynchronously update Todo list on server based on Checkboxes
	 */
    private void setTodos(final List<Todo> todos) {
    	ALog.v(TAG, "");
    	final ServerTask setTodosTask = new ServerTask(getActivity(), getString(R.string.setting_todos_dialog));
    	setTodosTask.setDoInBackground(new Runnable() {
			@Override
			public void run() {
				setTodosTask.setStatus(TodoListWeb.setTodos(todos));    			
			}
    	});
    	setTodosTask.setOnPostExecute(new Runnable() {
			@Override
			public void run() {
				Message m = new Message();
				if (setTodosTask.isStatus()) {
					m.what = MESSAGE_SET_TODOS_SUCCESS;
				} else {
					m.what = MESSAGE_SET_TODOS_FAILED;
				}
				mHandler.sendMessage(m);
			}
    	});
    	setTodosTask.execute();
    }
}
