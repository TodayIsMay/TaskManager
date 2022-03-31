package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


abstract class TaskManagerTest<T extends TaskManager> {
    TaskManager taskManager;
    private Epic epic;
    private Subtask subtask;
    private Subtask subtask1;
    private Task task;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public TaskManagerTest(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @BeforeEach
    public void createTasks() {
        epic = new Epic("feed the cat", "description", 1, Status.NEW);
        subtask = new Subtask(1, "find the cat", 2, Status.NEW, Duration.ofHours(1),
                LocalDateTime.parse("21.02.2022 02:00", formatter));
        subtask1 = new Subtask(1, "open pack of food", 3, Status.NEW, Duration.ofHours(1),
                LocalDateTime.parse("21.02.2022 04:00", formatter));
        task = new Task("simple task", 5, Status.NEW, Duration.ofHours(1),
                LocalDateTime.parse("21.03.2022 06:00", formatter));
    }

    @Test
    public void shouldAddEpic() {
        taskManager.addEpic(epic);
        assertEquals(1, taskManager.getEpics().size());
        assertEquals(epic, taskManager.getEpicById(1));
    }

    @Test
    public void shouldAddSubtask() {
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        assertEquals(1, taskManager.showSubtasksFromEpic(1).size());
        assertEquals(subtask, taskManager.getSubtaskById(2));
    }

    @Test
    public void shouldAddTask() {
        taskManager.addTask(task);
        assertEquals(1, taskManager.getTasks().size());
        assertEquals(task, taskManager.getTaskById(5));
    }

    @Test
    public void shouldReturnListOfSubtasks() {
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask1);
        List<Subtask> expected = new ArrayList<>(List.of(subtask, subtask1));
        List<Subtask> actual = taskManager.showAllSubtasksList();
        assertEquals(2, taskManager.showAllSubtasksList().size());
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
    }

    @Test
    public void shouldReturnSubtasksFromEpic() {
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask1);
        List<Subtask> expected = new ArrayList<>(List.of(subtask, subtask1));
        List<Subtask> actual = taskManager.showSubtasksFromEpic(1);
        assertEquals(2, taskManager.showAllSubtasksList().size());
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
    }

    @Test
    public void shouldReturnEpicById() {
        taskManager.addEpic(epic);
        assertEquals(epic, taskManager.getEpicById(1));
    }

    @Test
    public void shouldReturnSubtaskById() {
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask1);
        assertEquals(subtask1, taskManager.getSubtaskById(3));
    }

    @Test
    public void shouldReturnTaskById() {
        taskManager.addTask(task);
        assertEquals(task, taskManager.getTaskById(5));
    }

    @Test
    public void shouldUpdateEpic() {
        taskManager.addEpic(epic);
        Epic expected = new Epic("New name", "new description", 1, Status.NEW, Duration.ofHours(1),
                LocalDateTime.parse("21.03.2022 00:00", formatter),
                LocalDateTime.parse("21.03.2022 01:00", formatter));
        assertEquals("Эпик обновлён!", taskManager.updateEpic(expected, 1));
        assertEquals(expected, taskManager.getEpicById(1));
    }

    @Test
    public void shouldNotUpdateEpicWhenWrongIdInUpdateEpic() {
        taskManager.addEpic(epic);
        Epic newEpic = new Epic("New name", "new description", 1, Status.NEW, Duration.ofHours(1),
                LocalDateTime.parse("21.03.2022 00:00", formatter),
                LocalDateTime.parse("21.03.2022 01:00", formatter));
        assertEquals("Эпик с таким ID не найден!", taskManager.updateEpic(newEpic, 0));
        assertEquals(epic, taskManager.getEpicById(1));
    }

    @Test
    public void shouldUpdateSubtask() {
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        Subtask expected = new Subtask(1, "new subtask", 2, Status.NEW, Duration.ofHours(1),
                LocalDateTime.parse("21.02.2022 02:00", formatter));
        assertEquals("Подзадача обновлена!", taskManager.updateSubtask(expected, 2));
        assertEquals(expected, taskManager.getSubtaskById(2));
    }

    @Test
    public void shouldNotUpdateSubtaskWhenWrongIdInUpdateSubtask() {
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        Subtask newSubtask = new Subtask(1, "new subtask", 2, Status.NEW, Duration.ofHours(1),
                LocalDateTime.parse("21.02.2022 02:00", formatter));
        assertEquals("Подзадача с таким ID не найдена!", taskManager.updateSubtask(newSubtask, 0));
        assertEquals(subtask, taskManager.getSubtaskById(2));
    }

    @Test
    public void shouldUpdateTask() {
        taskManager.addTask(task);
        Task expected = new Task("new task", 5, Status.NEW, Duration.ofHours(1),
                LocalDateTime.parse("21.03.2022 06:00", formatter));
        assertEquals("Задача обновлена!", taskManager.updateTask(expected, 5));
        assertEquals(expected, taskManager.getTaskById(5));
    }

    @Test
    public void shouldNotUpdateTaskWhenWrongIdInUpdateTask() {
        taskManager.addTask(task);
        Task newTask = new Task("new task", 5, Status.NEW, Duration.ofHours(1),
                LocalDateTime.parse("21.03.2022 06:00", formatter));
        assertEquals("Задача с таким ID не найдена!", taskManager.updateTask(newTask, 0));
        assertEquals(task, taskManager.getTaskById(5));
    }

    @Test
    public void shouldDeleteAllTasks() {
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask1);
        taskManager.addTask(task);
        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.showAllSubtasksList().size());
        assertEquals(0, taskManager.getEpics().size());
        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    public void shouldDeleteSubtaskById() {
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask1);
        assertEquals("Задача удалена!", taskManager.deleteSubtask(3));
        assertEquals(1, taskManager.showAllSubtasksList().size());
        assertEquals(subtask, taskManager.showAllSubtasksList().get(0));
    }

    @Test
    public void shouldWriteWarningWhenWrongIdInDeleteSubtask() {
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask1);
        assertEquals("Подзадача с таким ID не найдена!", taskManager.deleteSubtask(0));
        assertEquals(2, taskManager.showAllSubtasksList().size());
    }

    @Test
    public void shouldWriteWarningWhenNoSubtasksInDeleteSubtask() {
        taskManager.addEpic(epic);
        assertEquals("Подзадача с таким ID не найдена!", taskManager.deleteSubtask(0));
        assertEquals(0, taskManager.showAllSubtasksList().size());
    }

    @Test
    public void shouldDeleteEpic() {
        taskManager.addEpic(epic);
        assertEquals("Задача удалена!", taskManager.deleteEpic(1));
        assertEquals(0, taskManager.getEpics().size());
    }

    @Test
    public void shouldDeleteEpicAndSubtasks() {
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask1);
        assertEquals("Задача удалена!", taskManager.deleteEpic(1));
        assertEquals(0, taskManager.getEpics().size());
        assertEquals(0, taskManager.showAllSubtasksList().size());
    }

    @Test
    public void shouldReturnWarningWhenWrongIdInDeleteEpic() {
        taskManager.addEpic(epic);
        assertEquals("Задача с таким ID не найдена!", taskManager.deleteEpic(0));
        assertEquals(1, taskManager.getEpics().size());
        assertEquals(epic, taskManager.getEpics().get(0));
    }

    @Test
    public void shouldReturnWarningWhenNoEpicsInDeleteEpic() {
        assertEquals("Задача с таким ID не найдена!", taskManager.deleteEpic(1));
        assertEquals(0, taskManager.getEpics().size());
    }

    @Test
    public void shouldDeleteTask() {
        taskManager.addTask(task);
        assertEquals("Задача удалена!", taskManager.deleteTask(5));
        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    public void shouldReturnWarningWhenWrongIdInDeleteTask() {
        taskManager.addTask(task);
        assertEquals("Задача с таким ID не найдена!", taskManager.deleteTask(0));
        assertEquals(1, taskManager.getTasks().size());
        assertEquals(task, taskManager.getTasks().get(0));
    }

    @Test
    public void shouldReturnWarningWhenNoTasksInDeleteTask() {
        assertEquals("Задача с таким ID не найдена!", taskManager.deleteTask(5));
        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    public void shouldReturnHistory() {
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask1);
        taskManager.addTask(task);
        taskManager.getSubtaskById(2);
        taskManager.getEpicById(1);
        List<Task> expected = new ArrayList<>(List.of(subtask, epic));
        List<Task> actual = taskManager.showHistory();
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void shouldReturnFalseIfNoIntersection() {
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        Assertions.assertFalse(taskManager.doesIntersect(subtask1));
    }

    @Test
    public void shouldReturnTrueWhenIntersection() {
        taskManager.addTask(task);
        Assertions.assertTrue(taskManager.doesIntersect(new Task("new task", 0, Status.NEW,
                Duration.ofHours(1), LocalDateTime.parse("21.03.2022 06:30", formatter))));
    }

    @Test
    public void shouldReturnFalseWhenBorderBetweenTwoTasks() {
        taskManager.addTask(task);
        Assertions.assertFalse(taskManager.doesIntersect(new Task("new task", 0, Status.NEW,
                Duration.ofHours(1), LocalDateTime.parse("21.03.2022 07:00", formatter))));
    }
}