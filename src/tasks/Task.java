package tasks;

public class Task {
    private String name;
    private int id;
    private Status status;

    public Task(String name, int id, Status status) {
        this.name = name;
        this.id = id;
        this.status = status;
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
}