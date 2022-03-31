package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    private String name;
    private int id;
    private Status status;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(String name, int id, Status status, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.id = id;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, int id, Status status) {
        this.name = name;
        this.id = id;
        this.status = status;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    @Override
    public String toString() {
        DateTimeFormatter startTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return this.getId() + "," + Type.TASK + "," +
                this.getName() + "," + this.getStatus() + "," + this.getDuration() + "," +
                this.startTime.format(startTimeFormatter) + "\n";
    }

    public static Task fromString(String value) {
        DateTimeFormatter startTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        Task task = null;
        Status status = null;
        String[] valueArr = value.split(",");
        switch (valueArr[3]) {
            case "NEW":
                status = Status.NEW;
                break;
            case "IN_PROGRESS":
                status = Status.IN_PROGRESS;
                break;
            case "DONE":
                status = Status.DONE;
                break;
        }
        int id = Integer.parseInt(valueArr[0]);
        String name = valueArr[2];
        if (valueArr[1].equals(Type.EPIC.toString())) {
            String description = valueArr[4];
            if(valueArr.length == 8) {
                Duration duration = Duration.ofMillis(Long.parseLong(valueArr[5]) * 3600000);
                LocalDateTime startTime = LocalDateTime.parse(valueArr[6], startTimeFormatter);
                LocalDateTime endTime = LocalDateTime.parse(valueArr[7], startTimeFormatter);
                task = new Epic(name, description, id, status, duration, startTime, endTime);
            } else {
                task = new Epic(name, description, id, status);
            }
        } else if (valueArr[1].equals(Type.SUBTASK.toString())) {
            int belonging = Integer.parseInt(valueArr[4]);
            Duration duration = Duration.ofHours(Long.parseLong(valueArr[5]));
            LocalDateTime startTime = LocalDateTime.parse(valueArr[6], startTimeFormatter);
            task = new Subtask(belonging, name, id, status, duration, startTime);
        } else if (valueArr[1].equals(Type.TASK.toString())) {
            Duration duration = Duration.ofMillis(Long.parseLong(valueArr[4]) * 3600000);
            LocalDateTime startTime = LocalDateTime.parse(valueArr[5], startTimeFormatter);
            task = new Task(name, id, status, duration, startTime);
        }
        return task;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public long getDuration() {
        return  duration.toHours();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void update(Task task) {
        this.setName(task.getName());
        this.setStatus(task.getStatus());
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, status);
    }
}