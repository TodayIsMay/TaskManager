package tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    Epic epic;

    @BeforeEach
    public void createEpic() {
        epic = new Epic("feed the cat", "description", 1, Status.NEW, Duration.ofHours(1),
                LocalDateTime.parse("21.03.2022 00:00", formatter),
                LocalDateTime.parse("21.03.2022 01:00", formatter));
    }

    @Test
    public void shouldBeNewWhenNoSubtasks() {
        String expected = Status.NEW.toString();
        String actual = epic.getStatus().toString();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldBeNewWhenAllSubtasksAreNew() {
        epic.addSub(new Subtask(1, "find the cat", 1, Status.NEW, Duration.ofHours(1), LocalDateTime.now()));
        epic.addSub(new Subtask(1, "open food pack", 1, Status.NEW, Duration.ofHours(1), LocalDateTime.now()));
        epic.addSub(new Subtask(1, "put food in a bowl", 1, Status.NEW, Duration.ofHours(1), LocalDateTime.now()));
        assertEquals(Status.NEW.toString(), epic.getStatus().toString());
    }

    @Test
    public void shouldBeDoneWhenAllSubtasksAreDone() {
        epic.addSub(new Subtask(1, "find the cat", 1, Status.DONE, Duration.ofHours(1), LocalDateTime.now()));
        epic.addSub(new Subtask(1, "open food pack", 1, Status.DONE, Duration.ofHours(1), LocalDateTime.now()));
        epic.addSub(new Subtask(1, "put food in a bowl", 1, Status.DONE, Duration.ofHours(1), LocalDateTime.now()));
        assertEquals(Status.DONE.toString(), epic.getStatus().toString());
    }

    @Test
    public void shouldBeInProgressWhenSubtasksAreNewAndDone() {
        epic.addSub(new Subtask(1, "find the cat", 1, Status.DONE, Duration.ofHours(1), LocalDateTime.now()));
        epic.addSub(new Subtask(1, "open food pack", 1, Status.NEW, Duration.ofHours(1), LocalDateTime.now()));
        epic.addSub(new Subtask(1, "put food in a bowl", 1, Status.NEW, Duration.ofHours(1), LocalDateTime.now()));
        assertEquals(Status.IN_PROGRESS.toString(), epic.getStatus().toString());
    }

    @Test
    public void shouldBeInProgressWhenAllSubtasksAreInProgress() {
        epic.addSub(new Subtask(1, "find the cat", 1, Status.IN_PROGRESS, Duration.ofHours(1), LocalDateTime.now()));
        epic.addSub(new Subtask(1, "open food pack", 1, Status.IN_PROGRESS, Duration.ofHours(1), LocalDateTime.now()));
        epic.addSub(new Subtask(1, "put food in a bowl", 1, Status.IN_PROGRESS, Duration.ofHours(1), LocalDateTime.now()));
        assertEquals(Status.IN_PROGRESS.toString(), epic.getStatus().toString());
    }

}