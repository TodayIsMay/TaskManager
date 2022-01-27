package tasks;

public class Subtask extends Task {
    private int belonging;

    public Subtask(int belonging, String name, int id, Status status) {
        super(name, id, status);
        this.belonging = belonging;
    }

    public void update(Subtask subtask) {
        this.setName(subtask.getName());
        this.setStatus(subtask.getStatus());
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    public int getBelonging() {
        return belonging;
    }
}