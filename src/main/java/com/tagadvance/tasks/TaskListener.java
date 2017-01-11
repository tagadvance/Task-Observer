package com.tagadvance.tasks;

import java.util.EventListener;

public interface TaskListener extends EventListener {

	/**
	 * Invoked when an existing task is detected.
	 * 
	 * @param e
	 */
	public void taskInitialized(TaskEvent e);

	/**
	 * Invoked when a new task is detected.
	 * 
	 * @param e
	 */
	public void taskCreated(TaskEvent e);

	/**
	 * Invoked when a task is no longer detected.
	 * 
	 * @param e
	 */
	public void taskDestroyed(TaskEvent e);

}
