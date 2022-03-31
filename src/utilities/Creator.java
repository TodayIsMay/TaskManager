package utilities;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Creator {
    private int maxId;
    private Scanner scanner = new Scanner(System.in);
    DateTimeFormatter startTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public Creator(int maxId) {
        this.maxId = maxId;
    }

    public Epic createEpic() {
        System.out.println("Введите название эпика");
        String name = scanner.nextLine();
        System.out.println("Введите описание");
        String description = scanner.nextLine();
        int epicID = generateID();
        Status status = Status.NEW;
        return new Epic(name, description, epicID, status);
    }

    public Subtask createSubtask() {
        System.out.println("К какому эпику относится подзадача(ID)?");
        int belonging = Integer.parseInt(scanner.nextLine());
        System.out.println("Введите название подзадачи");
        String name = scanner.nextLine();
        int ID = generateID();
        Status status = Status.NEW;
        System.out.println("Введите предполагаемую продолжительность в часах");
        Duration duration = Duration.ofHours(Long.parseLong(scanner.nextLine()));
        LocalDateTime startTime = LocalDateTime.MIN;
        try {
            System.out.println("Введите время начала в формате 01.01.1970 00:00");
            startTime = LocalDateTime.parse(scanner.nextLine(), startTimeFormatter);
        }catch (DateTimeParseException e) {
            System.out.println("Неверный формат времени и/или даты!");
        }
        return new Subtask(belonging, name, ID, status, duration, startTime);
    }

    public Task createTask() {
        System.out.println("Введите название задачи.");
        String name = scanner.nextLine();
        int ID = generateID();
        Status status = Status.NEW;
        System.out.println("Введите предполагаемую продолжительность в часах");
        Duration duration = Duration.ofHours(Long.parseLong(scanner.nextLine()));
        LocalDateTime startTime = LocalDateTime.MIN;
        try {
            System.out.println("Введите время начала в формате 01.01.1970 00:00");
            startTime = LocalDateTime.parse(scanner.nextLine(), startTimeFormatter);
        }catch (DateTimeParseException e) {
            System.out.println("Неверный формат времени и/или даты!");
        }
        return new Task(name, ID, status, duration, startTime);
    }

    public int generateID() {
        this.maxId++;
        return maxId;
    }
}