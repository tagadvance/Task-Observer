package com.tagadvance.tasks.windows;

import static com.google.common.base.Preconditions.checkNotNull;

import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.ptr.LongByReference;
import com.tagadvance.tasks.Task;
import com.tagadvance.tasks.TaskAffinity;
import com.tagadvance.tasks.TaskException;

public class WindowsTaskAffinity implements TaskAffinity {

	private final AuxiliaryKernel32 kernel32;

	public WindowsTaskAffinity(AuxiliaryKernel32 kernel32) {
		super();
		this.kernel32 = checkNotNull(kernel32, "kernel32 must not be null");
	}

	@Override
	public long getAffinityMask(final Task task) throws TaskException {
		final int processId = task.getId();

		int fdwAccess = ProcessSecurity.PROCESS_QUERY_INFORMATION;
		boolean fInherit = false;
		HANDLE hProcess = null;
		try {
			hProcess = kernel32.OpenProcess(fdwAccess, fInherit, processId);

			LongByReference lpProcessAffinityMask = new LongByReference();
			LongByReference lpSystemAffinityMask = new LongByReference();
			boolean success = kernel32.GetProcessAffinityMask(hProcess,
					lpProcessAffinityMask, lpSystemAffinityMask);
			
			// TODO: SystemTaskAffinity
//			// 00000001 = core 0, 10000000 = core 7
//			long systemAffinity = lpSystemAffinityMask.getValue();
//			int availableProcessors = Long.bitCount(systemAffinity);
//			System.out.println(affinity);
//			System.out.println(Long.toBinaryString(lpProcessAffinityMask.getValue()));
//			System.out.println(availableProcessors);
			
			if (success) {
				return lpProcessAffinityMask.getValue();
			} else {
				throw new TaskException("could not get affinity");
			}
		} finally {
			if (hProcess != null) {
				kernel32.CloseHandle(hProcess);
			}
		}
	}

	@Override
	public void setAffinityMask(final Task process, long affinityMask) throws TaskException {
		final int processId = process.getId();

		int fdwAccess = ProcessSecurity.PROCESS_SET_INFORMATION;
		boolean fInherit = false;
		HANDLE hProcess = null;
		try {
			hProcess = kernel32.OpenProcess(fdwAccess, fInherit, processId);

			boolean success = kernel32.SetProcessAffinityMask(hProcess, (int) affinityMask);
			if (!success) {
				throw new TaskException("could not set affinity");
			}
		} finally {
			if (hProcess != null) {
				kernel32.CloseHandle(hProcess);
			}
		}
	}

}
