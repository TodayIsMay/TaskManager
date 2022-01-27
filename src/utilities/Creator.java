package utilities;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.Scanner;

public class Creator {
    private ArrayList<Integer> list = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    public Epic createEpic() {
        System.out.println("Введите название эпика");
        String name = scanner.nextLine();
        System.out.println("Введите описание");
        String description = scanner.nextLine();
        int epicID = generateId();
        Status status = Status.NEW;
        return new Epic(name, description, epicID, status);
    }

    public Subtask createSubtask() {
        System.out.println("К какому эпику относится подзадача(ID)?");
        int belonging = Integer.parseInt(scanner.nextLine());
        System.out.println("Введите название подзадачи");
        String name = scanner.nextLine();
        int ID = generateId();
        Status status = Status.NEW;
        return new Subtask(belonging, name, ID, status);
    }

    public Task createTask() {
        System.out.println("Введите название задачи.");
        String name = scanner.nextLine();
        int ID = generateId();
        Status status = Status.NEW;
        return new Task(name, ID, status);
    }

    public int generateId() {
        int result = 1;
        for (Integer number : list) {
            if (number == result) {
                result++;
            }
        }
        list.add(result);
        return result;
    }
}