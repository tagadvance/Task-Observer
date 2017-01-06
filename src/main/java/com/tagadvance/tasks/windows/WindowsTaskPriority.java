package com.tagadvance.tasks.windows;

import static com.google.common.base.Preconditions.checkNotNull;

import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.tagadvance.tasks.Priority;
import com.tagadvance.tasks.Task;
import com.tagadvance.tasks.TaskException;
import com.tagadvance.tasks.TaskPriority;

public class WindowsTaskPriority implements TaskPriority {

	private static final int IDLE_PRIORITY_CLASS = 0x0040;
	private static final int BELOW_NORMAL_PRIORITY_CLASS = 0x4000;
	private static final int NORMAL_PRIORITY_CLASS = 0x0020;
	private static final int ABOVE_NORMAL_PRIORITY_CLASS = 0x8000;
	private static final int HIGH_PRIORITY_CLASS = 0x0080;
	private static final int REALTIME_PRIORITY_CLASS = 0x0100;

	private final AuxiliaryKernel32 kernel32;

	public WindowsTaskPriority(AuxiliaryKernel32 kernel32) {
		super();
		this.kernel32 = checkNotNull(kernel32, "kernel32 must not be null");
	}

	@Override
	public Priority getPriority(Task task) throws TaskException {
		final int processId = task.getId();

		int fdwAccess = ProcessSecurity.PROCESS_QUERY_INFORMATION ;
		boolean fInherit = false;
		HANDLE hProcess = null;
		try {
			hProcess = kernel32.OpenProcess(fdwAccess, fInherit, processId);
			int priorityClass = kernel32.GetPriorityClass(hProcess);
			if (priorityClass == 0) {
				throw new TaskException("could not get priority");
			}
			return calculatePriority(priorityClass);
		} finally {
			if (hProcess != null) {
				kernel32.CloseHandle(hProcess);
			}
		}
	}

	@Override
	public void setPriority(Task task, Priority priority) throws TaskException {
		final int processId = task.getId();
		int dwPriorityClass = calculatePriorityClass(priority);

		int fdwAccess = ProcessSecurity.PROCESS_SET_INFORMATION;
		boolean fInherit = false;
		HANDLE hProcess = null;
		try {
			hProcess = kernel32.OpenProcess(fdwAccess, fInherit, processId);
			boolean success = kernel32.SetPriorityClass(hProcess, dwPriorityClass);
			if (!success) {
				throw new TaskException("could not set priority");
			}
		} finally {
			if (hProcess != null) {
				kernel32.CloseHandle(hProcess);
			}
		}
	}

	/**
	 * TODO: unit test
	 * 
	 * @param priority
	 * @return
	 */
	public int calculatePriorityClass(Priority priority) {
		switch (priority) {
			case IDLE:
				return IDLE_PRIORITY_CLASS;
			case LOW:
				return BELOW_NORMAL_PRIORITY_CLASS;
			case MEDIUM:
				return ABOVE_NORMAL_PRIORITY_CLASS;
			case HIGH:
				return HIGH_PRIORITY_CLASS;
			case REALTIME:
				return REALTIME_PRIORITY_CLASS;
			default:
				return NORMAL_PRIORITY_CLASS;
		}
	}
	
	/**
	 * TODO: unit test
	 * 
	 * @param priorityClass
	 * @return
	 */
	public Priority calculatePriority(int priorityClass) {
		switch (priorityClass) {
			case IDLE_PRIORITY_CLASS:
				return Priority.IDLE;
			case BELOW_NORMAL_PRIORITY_CLASS:
				return Priority.LOW;
			case ABOVE_NORMAL_PRIORITY_CLASS:
				return Priority.MEDIUM;
			case HIGH_PRIORITY_CLASS:
				return Priority.HIGH;
			case REALTIME_PRIORITY_CLASS:
				return Priority.REALTIME;
			default:
				return Priority.DEFAULT;
		}
	}

}
