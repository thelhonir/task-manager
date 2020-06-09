package org.thelhonir.taskmanager;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thelhonir.taskmanager.task.controller.TaskController;

@SpringBootTest
class TaskManagerApplicationTests {

	@Autowired
	TaskController taskController;

	@Test
	void contextLoads() {
		assertNotNull(taskController);
	}

}
