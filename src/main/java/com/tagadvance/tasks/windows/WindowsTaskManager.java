package com.tagadvance.tasks.windows;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Iterator;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.tagadvance.tasks.Task;
import com.tagadvance.tasks.TaskManager;

public class WindowsTaskManager implements TaskManager {

	private final Kernel32 kernel32;

	public WindowsTaskManager(Kernel32 kernel32) {
		super();
		this.kernel32 = checkNotNull(kernel32, "kernel32 must not be null");
	}

	@Override
	public Iterator<Task> tasks() {
		return new ProcessIterator();
	}

	private class ProcessIterator implements Iterator<Task> {

		private WinNT.HANDLE snapshot;
		private Tlhelp32.PROCESSENTRY32.ByReference processEntry;

		private Task next;

		public ProcessIterator() {
			super();
			WinDef.DWORD th32ProcessID = null;
			this.snapshot =
					kernel32.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPPROCESS, th32ProcessID);
			this.processEntry = new Tlhelp32.PROCESSENTRY32.ByReference();
		}

		@Override
		public boolean hasNext() {
			boolean hasNext = kernel32.Process32Next(snapshot, processEntry);
			if (hasNext) {
				int processId = processEntry.th32ProcessID.intValue();
				String fileName = Native.toString(processEntry.szExeFile);
				next = new Task(processId, fileName);
				int parentId = processEntry.th32ParentProcessID.intValue();
				next.setParentId(parentId);
			} else {
				next = null;
				kernel32.CloseHandle(snapshot);
			}
			return hasNext;
		}

		@Override
		public Task next() {
			return next;
		}

	}

}
