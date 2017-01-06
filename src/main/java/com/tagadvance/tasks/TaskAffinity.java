package com.tagadvance.tasks;

public interface TaskAffinity {

	long getAffinityMask(Task task) throws TaskException;

	void setAffinityMask(Task task, long affinityMask) throws TaskException;

}
