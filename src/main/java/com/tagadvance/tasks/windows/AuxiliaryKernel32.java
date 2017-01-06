package com.tagadvance.tasks.windows;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.ptr.LongByReference;


public interface AuxiliaryKernel32 extends Kernel32 {

	AuxiliaryKernel32 INSTANCE = (AuxiliaryKernel32) Native.loadLibrary("Kernel32", AuxiliaryKernel32.class);

	/**
	 * 
	 * @param hProcess
	 * @return
	 * @see https://msdn.microsoft.com/en-us/library/windows/desktop/ms683211(v=vs.85).aspx
	 */
	public int GetPriorityClass(HANDLE hProcess);
	
	/**
	 * 
	 * @param hProcess
	 * @param dwPriorityClass
	 * @return
	 * @see https://msdn.microsoft.com/en-us/library/windows/desktop/ms686219(v=vs.85).aspx
	 */
	public boolean SetPriorityClass(HANDLE hProcess, int dwPriorityClass);
	
	/**
	 * 
	 * @param hProcess
	 * @param dwProcessAffinityMask
	 * @return
	 * @see https://msdn.microsoft.com/en-us/library/windows/desktop/ms686223(v=vs.85).aspx
	 * @see http://stackoverflow.com/q/25211253/625688
	 */
	public boolean SetProcessAffinityMask(HANDLE hProcess, int dwProcessAffinityMask);

	/**
	 * 
	 * @param hProcess
	 * @param lpProcessAffinityMask
	 * @param lpSystemAffinityMask
	 * @return
	 * @see https://msdn.microsoft.com/en-us/library/windows/desktop/ms683213(v=vs.85).aspx
	 * @see https://github.com/OpenHFT/Java-Thread-Affinity/blob/master/affinity/src/main/java/net/openhft/affinity/impl/WindowsJNAAffinity.java
	 */
	public boolean GetProcessAffinityMask(HANDLE hProcess, LongByReference lpProcessAffinityMask,
			LongByReference lpSystemAffinityMask);

}
