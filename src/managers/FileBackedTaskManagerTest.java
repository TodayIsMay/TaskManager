package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    FileBackedTaskManager fileBackedTaskManager;

    public FileBackedTaskManagerTest() {
        super(new FileBackedTaskManager("tasks.csv"));
    }

    @BeforeEach
    public void create() {
        fileBackedTaskManager = new FileBackedTaskManager("tasks.csv");
    }

    @Test
    public void shouldReturnFileBackedTaskManagerWhenEpicWithoutSubtasks() {
        fileBackedTaskManager.addEpic(new Epic("Feed the cat", "description", 1, Status.NEW));
        fileBackedTaskManager.getEpicById(1);
        assertEquals(fileBackedTaskManager, FileBackedTaskManager.loadFromFile("tasks.csv"));
    }

    @Test
    public void shouldReturnFileBackedTaskManagerWhenEmptyHistory() {
        fileBackedTaskManager.addEpic(new Epic("Feed the cat", "description", 1, Status.NEW));
        fileBackedTaskManager.addSubtask(new Subtask(1, "find the cat", 2, Status.NEW,
                Duration.ofHours(1),
                LocalDateTime.parse("21.03.2022 02:00", formatter)));
        fileBackedTaskManager.addTask(new Task("task", 3, Status.NEW, Duration.ofHours(1),
                LocalDateTime.parse("21.03.2022 04:00", formatter)));
        assertEquals(fileBackedTaskManager, FileBackedTaskManager.loadFromFile("tasks.csv"));
        fileBackedTaskManager.deleteAllTasks();
    }

    @Test
    public void shouldReturnFileBackedTaskManager() {
        fileBackedTaskManager.addEpic(new Epic("Feed the cat", "description", 1, Status.NEW,
                Duration.ofHours(1),
                LocalDateTime.parse("21.03.2022 00:00", formatter),
                LocalDateTime.parse("21.03.2022 01:00", formatter)));
        fileBackedTaskManager.addSubtask(new Subtask(1, "find the cat", 2, Status.NEW,
                Duration.ofHours(1),
                LocalDateTime.parse("21.03.2022 09:00", formatter)));
        fileBackedTaskManager.addTask(new Task("task", 3, Status.NEW, Duration.ofHours(1),
                LocalDateTime.parse("21.03.2022 04:00", formatter)));
        fileBackedTaskManager.getEpicById(1);
        fileBackedTaskManager.getSubtaskById(2);
        fileBackedTaskManager.getTaskById(3);
        assertEquals(fileBackedTaskManager, FileBackedTaskManager.loadFromFile("tasks.csv"));
        fileBackedTaskManager.deleteAllTasks();
    }

    @Test
    public void shouldNotThrowExceptionWhenWringFile() {
        assertDoesNotThrow(() -> {
            FileBackedTaskManager.loadFromFile("some name.csv");
        });
    }
}