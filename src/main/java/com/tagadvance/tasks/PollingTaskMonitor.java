package com.tagadvance.tasks;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.event.EventListenerList;

import com.tagadvance.poll.PollAction;
import com.tagadvance.poll.PollScheduler;

/**
 * There doesn't appear to be an easy way to detect when new processes are created. There is the
 * <a href=
 * "https://msdn.microsoft.com/en-us/library/windows/hardware/ff559951(v=vs.85).aspx">PsSetCreateProcessNotifyRoutine
 * routine</a> but it doesn't run in
 * <a href="https://msdn.microsoft.com/en-us/library/windows/hardware/ff554836(v=vs.85).aspx">user
 * mode</a>. Apparently one can use Windows Management Instrumentation (WMI). Yuck. That leaves us
 * with polling. Yay.
 * 
 * @see http://stackoverflow.com/questions/3556048/how-to-detect-win32-process-creation-termination-in-c
 * @see https://forum.sysinternals.com/process-creation-notification_topic14823.html
 */
public class PollingTaskMonitor implements TaskMonitor {

	protected EventListenerList listenerList = new EventListenerList();

	private final TaskManager taskManager;

	public PollingTaskMonitor(TaskManager taskManager, PollScheduler scheduler) {
		super();
		this.taskManager = checkNotNull(taskManager, "taskManager must not be null");
		
		PollAction action = new TaskMonitorPollAction();
		scheduler.schedule(action);
	}

	@Override
	public void addTaskListener(TaskListener listener) {
		listenerList.add(TaskListener.class, listener);
	}

	@Override
	public void removeTaskListener(TaskListener listener) {
		listenerList.remove(TaskListener.class, listener);
	}

	void fireTaskInitializedEvent(TaskEvent e) {
		TaskListener[] listeners = listenerList.getListeners(TaskListener.class);
		for (TaskListener listener : listeners) {
			listener.taskInitialized(e);
		}
	}

	void fireTaskCreatedEvent(TaskEvent e) {
		TaskListener[] listeners = listenerList.getListeners(TaskListener.class);
		for (TaskListener listener : listeners) {
			listener.taskCreated(e);
		}
	}

	void fireTaskDestroyedEvent(TaskEvent e) {
		TaskListener[] listeners = listenerList.getListeners(TaskListener.class);
		for (TaskListener listener : listeners) {
			listener.taskDestroyed(e);
		}
	}

	private class TaskMonitorPollAction implements PollAction {

		private Map<Integer, Task> taskMap = new HashMap<>();

		private boolean isInitializing = true;

		@Override
		public void run() {
			Set<Integer> keySet = taskMap.keySet();
			Set<Integer> deadTaskIds = new HashSet<>(keySet);

			Iterator<Task> tasks = taskManager.tasks();
			while ( tasks.hasNext()) {
				Task task = tasks.next();
				Integer taskId = task.getId();
				if (taskMap.containsKey(taskId)) {
					deadTaskIds.remove(taskId);
				} else {
					TaskEvent e = new TaskEvent(task);
					if (isInitializing) {
						fireTaskInitializedEvent(e);
					} else {
						fireTaskCreatedEvent(e);
					}
					taskMap.put(taskId, task);
				}
			}

			if (isInitializing) {
				isInitializing = false;
			} else {
				for (Integer id : deadTaskIds) {
					Task task = taskMap.remove(id);
					TaskEvent e = new TaskEvent(task);
					fireTaskDestroyedEvent(e);
				}
			}
		}

	}

}
