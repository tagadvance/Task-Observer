package com.tagadvance.tasks;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.google.common.testing.NullPointerTester;
import com.tagadvance.poll.PollAction;
import com.tagadvance.poll.PollScheduler;

public class PollingTaskMonitorTest {

	@Test
	public void testConstructorThrowsNPE() {
		NullPointerTester tester = new NullPointerTester();
		tester.testAllPublicConstructors(PollingTaskMonitor.class);
	}

	@Test
	public void testTaskListener() {
		TaskManager taskManager = mock(TaskManager.class);

		List<Task> tasks1 = createInitialTasks();
		List<Task> tasks2 = createTasks_OneCreated_TwoDestroyed();

		Mockito.when(taskManager.tasks()).thenReturn(tasks1.iterator())
				.thenReturn(tasks2.iterator());

		PollScheduler scheduler = mock(PollScheduler.class);
		ArgumentCaptor<PollAction> argument = new ArgumentCaptor<>();
		PollingTaskMonitor monitor = new PollingTaskMonitor(taskManager, scheduler);
		verify(scheduler).schedule(argument.capture());

		TaskListener listener = mock(TaskListener.class);
		monitor.addTaskListener(listener);

		PollAction action = argument.getValue();
		action.run();
		action.run();

		verify(listener, times(3)).taskInitialized(any(TaskEvent.class));
		verify(listener, times(1)).taskCreated(any(TaskEvent.class));
		verify(listener, times(2)).taskDestroyed(any(TaskEvent.class));
	}

	private List<Task> createInitialTasks() {
		List<Task> tasks = new ArrayList<>();

		Map<Integer, String> map = new LinkedHashMap<>();
		map.put(0, "root");
		map.put(1, "foo");
		map.put(2, "bar");
		for (Map.Entry<Integer, String> entry : map.entrySet()) {
			int id = entry.getKey();
			String name = entry.getValue();
			tasks.add(new Task(id, name));
		}

		return tasks;
	}

	private List<Task> createTasks_OneCreated_TwoDestroyed() {
		List<Task> tasks = new ArrayList<>();

		Map<Integer, String> map = new LinkedHashMap<>();
		map.put(0, "root");
		// map.put(1, "foo"); // destroyed
		// map.put(2, "bar"); // destroyed
		map.put(3, "sugar");
		for (Map.Entry<Integer, String> entry : map.entrySet()) {
			int id = entry.getKey();
			String name = entry.getValue();
			tasks.add(new Task(id, name));
		}

		return tasks;
	}

}
