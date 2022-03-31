package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private String description;
    private List<Subtask> subtasks;
    Duration duration;
    LocalDateTime startTime;
    LocalDateTime endTime;

    public Epic(String name, String description, int id, Status status, Duration duration, LocalDateTime startTime,
                LocalDateTime endTime) {
        super(name, id, status);
        this.description = description;
        this.subtasks = new ArrayList<>();
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Epic(String name, String description, int id, Status status) {
        super(name, id, status);
        this.description = description;
        this.subtasks = new ArrayList<>();
    }

    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> clone = new ArrayList<>(subtasks);
        return clone;
    }

    public void addSub(Subtask sub) {
        subtasks.add(sub);
    }

    public void deleteSubtask(Subtask subtask) {
        subtasks.remove(subtask);
    }

    @Override
    public void setStatus(Status status) {
        System.out.println("Нельзя изменить статус эпика.");
    }

    public void update(Epic epic) {
        setDescription(epic.getDescription());
        this.setName(epic.getName());
    }

    @Override
    public Status getStatus() {
        Status status;
        boolean isDoneOrInProgress = false;
        ArrayList<Subtask> checkedSubtasks = new ArrayList<>();
        if (subtasks.isEmpty()) {
            status = Status.NEW;
            return status;
        }
        for (Subtask subtask : subtasks) {
            if (subtask.getStatus().equals(Status.DONE) || subtask.getStatus().equals(Status.IN_PROGRESS)) {
                isDoneOrInProgress = true;
                break;
            }
        }
        if (!isDoneOrInProgress) {
            status = Status.NEW;
        } else {
            for (Subtask subtask : subtasks) {
                if (subtask.getStatus().equals(Status.DONE)) {
                    checkedSubtasks.add(subtask);
                }
            }
            if (checkedSubtasks.size() == subtasks.size()) {
                status = Status.DONE;
            } else {
                status = Status.IN_PROGRESS;
            }
        }
        return status;
    }

    @Override
    public LocalDateTime getStartTime() {
        LocalDateTime startTime = LocalDateTime.MAX;
        for(Subtask subtask: subtasks) {
            if(subtask.getStartTime().isBefore(startTime)) {
                startTime = subtask.getStartTime();
            }
        }
        this.startTime = startTime;
        return startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        LocalDateTime endTime = LocalDateTime.MIN;
        for(Subtask subtask: subtasks) {
            if(subtask.getEndTime().isAfter(endTime)) {
                endTime = subtask.getEndTime();
            }
        }
        this.endTime = endTime;
        return endTime;
    }

    @Override
    public long getDuration() {
        this.duration = Duration.between(startTime, endTime);
        return duration.toHours();
    }

    @Override
    public String toString() {
        if(startTime == null | endTime == null | duration == null) {
            return this.getId() + "," + Type.EPIC + "," +
                    this.getName() + "," + this.getStatus() + "," + this.getDescription() + "\n";
        }else {
            return this.getId() + "," + Type.EPIC + "," +
                    this.getName() + "," + this.getStatus() + "," + this.getDescription() + "," +
                    this.getDuration() + "," + this.getStartTime().format(formatter) + "," +
                    this.getEndTime().format(formatter) + "\n";
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}