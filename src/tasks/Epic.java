package tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private String epicDescription;
    private List<Subtask> subtasks;

    public Epic(String name, String epicDescription, int id, Status status) {
        super(name, id, status);
        this.epicDescription = epicDescription;
        this.subtasks = new ArrayList<>();
    }

    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> clone = new ArrayList<>(subtasks);
        return clone;
    }

    public void setEpicDescription(String epicDescription) {
        this.epicDescription = epicDescription;
    }

    public String getEpicDescription() {
        return epicDescription;
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
        this.setEpicDescription(epic.getEpicDescription());
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
}