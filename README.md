# Task Observer

Task Observer is a Java library for monitoring system processes. There is an API to set process affinity and priority. Don't use that part.

**Note:** Task and Process are used interchangeably. In most cases I prefer to use _Task_ as it is shorter than _Process_.

Example
===
```java
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.sun.jna.platform.win32.Kernel32;
import com.tagadvance.poll.PollAction;
import com.tagadvance.poll.PollScheduler;
import com.tagadvance.tasks.LogTaskListener;
import com.tagadvance.tasks.PollingTaskMonitor;
import com.tagadvance.tasks.TaskManager;
import com.tagadvance.tasks.TaskMonitor;
import com.tagadvance.tasks.windows.WindowsTaskManager;

public class Foo {

	static {
		System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
		System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace");
	}

	public static void main(String[] args) {
		TaskManager manager = new WindowsTaskManager(Kernel32.INSTANCE);
		ThreadFactory threadFactory = new ThreadFactoryBuilder().setDaemon(false)
				.setNameFormat("task-monitor-%d").build();
		final ScheduledExecutorService service =
				Executors.newSingleThreadScheduledExecutor(threadFactory);
		PollScheduler scheduler = new PollScheduler() {
			@Override
			public void schedule(PollAction poll) {
				int initialDelay = 0, delay = 1;
				service.scheduleWithFixedDelay(poll, initialDelay, delay, TimeUnit.MILLISECONDS);
			}
		};
		TaskMonitor monitor = new PollingTaskMonitor(manager, scheduler);
		monitor.addTaskListener(new LogTaskListener());
	}

}
```