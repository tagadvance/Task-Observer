package com.tagadvance.tasks;

import java.util.EventObject;

@SuppressWarnings("serial")
public class TaskEvent extends EventObject {

	public TaskEvent(Task source) {
		super(source);
	}

	@Override
	public Task getSource() {
		return (Task) super.getSource();
	}

}
