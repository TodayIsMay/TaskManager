package utilities;

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

class HistoryManagerTest {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    HistoryManager historyManager;
    Epic epic;
    Subtask subtask;
    Subtask subtask1;
    Task task;

    @BeforeEach
    public void createHistoryManager() {
        historyManager = new InMemoryHistoryManager();
        epic = new Epic("feed the cat", "description", 1, Status.NEW, Duration.ofHours(1),
                LocalDateTime.parse("21.03.2022 00:00", formatter),
                LocalDateTime.parse("21.03.2022 01:00", formatter));
        subtask = new Subtask(1, "find the cat", 2, Status.NEW, Duration.ofHours(1),
                LocalDateTime.parse("21.02.2022 02:00", formatter));
        subtask1 = new Subtask(1, "open pack of food", 3, Status.NEW, Duration.ofHours(1),
                LocalDateTime.parse("21.02.2022 04:00", formatter));
        task = new Task("simple task", 4, Status.NEW, Duration.ofHours(1),
                LocalDateTime.parse("21.03.2022 06:00", formatter));
    }

    @Test
    public void shouldAddTaskToHistory() {
        historyManager.add(task);
        historyManager.add(epic);
        List<Task> expected = new ArrayList<>(List.of(task, epic));
        assertEquals(expected, historyManager.getHistory());
    }

    @Test
    public void shouldRemoveTaskFromTheEndOfHistory() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.remove(1);
        List<Task> expected = new ArrayList<>(List.of(task));
        assertEquals(expected, historyManager.getHistory());
    }

    @Test
    public void shouldRemoveTaskFromTheBeginningOfHistory() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        historyManager.remove(4);
        List<Task> expected = new ArrayList<>(List.of(epic, subtask));
        assertEquals(expected, historyManager.getHistory());
    }

    @Test
    public void shouldRemoveTaskFromTheMiddleOfHistory() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        historyManager.remove(1);
        List<Task> expected = new ArrayList<>(List.of(task, subtask));
        assertEquals(expected, historyManager.getHistory());
    }

    @Test
    public void shouldPutTheSameTaskInTheEnd() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        historyManager.add(epic);
        List<Task> expected = new ArrayList<>(List.of(task, subtask, epic));
        assertEquals(expected, historyManager.getHistory());
    }

    @Test
    public void shouldReturnEmptyList() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.clearHistory();
        List<Task> expected = new ArrayList<>();
        assertEquals(expected, historyManager.getHistory());
    }
}