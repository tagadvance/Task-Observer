package com.tagadvance.tasks;

public interface TaskMonitor {

	public void addTaskListener(TaskListener listener);

	public void removeTaskListener(TaskListener listener);
	
}
