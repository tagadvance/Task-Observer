package com.tagadvance.tasks;

public interface TaskPriority {

	Priority getPriority(Task task) throws TaskException;

	void setPriority(Task task, Priority priority) throws TaskException;

}
