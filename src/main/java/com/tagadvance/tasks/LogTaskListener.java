package com.tagadvance.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTaskListener implements TaskListener {

	private static final Logger logger = LoggerFactory.getLogger(LogTaskListener.class);

	@Override
	public void taskInitialized(TaskEvent e) {
		Task task = e.getSource();
		logger.info("{}\t{} initialized", task.getId(), task.getName());
	}

	@Override
	public void taskDestroyed(TaskEvent e) {
		Task task = e.getSource();
		logger.info("{}\t{} destroyed", task.getId(), task.getName());
	}

	@Override
	public void taskCreated(TaskEvent e) {
		Task task = e.getSource();
		logger.info("{}\t{} created", task.getId(), task.getName());
	}

}
