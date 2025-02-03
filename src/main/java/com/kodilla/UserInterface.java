package com.kodilla;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class UserInterface {

    private final Scanner scanner = new Scanner(System.in);
    private Shop shop = new Shop();

    private int menuChoice = 0;

    private final String filePath = "src/main/resources/orders.txt";

    private final String mainMenu = """
                    Wybierz opcję:\s
                    1. Dodaj zamówienie\s
                    2. Edytuj zamówienie\s
                    3. Usuń zamówienie\s
                    4. Wyświetl zamówienia pogrupowane według statusu\s
                    5. Wyświetl statystyki zamówień\s
                    6. Zapis\s
                    7. Odczyt\s
                    8. Wyjście
                    """;

    public void runUI() throws IOException, ClassNotFoundException {

        while (menuChoice != 8) {

            System.out.println(mainMenu);

            System.out.print("Opcja nr: ");
            menuChoice = scanner.nextInt();

            if (menuChoice < 1 || menuChoice > 8) {
                System.out.println("Wybierz opcję pomiędzy nr 1 - 8.");
                continue;
            }

            switch (menuChoice) {
                case 1 -> addNewOrder();
                case 2 -> editOrder();
                case 3 -> deleteOrder();
                case 4 -> viewAllGroupedOrders();
                case 5 -> showStatistics();
                case 6 -> saveToFile();
                case 7 -> readFromFile();
                case 8 -> System.out.println("Dziękujemy za skorzystanie z naszej aplikacji!");
            }
        }
    }

    private void addNewOrder() {

        int id = provideIDNumber();
        if (shop.findOrderExist(id)) {
            System.out.println("Zamówienie z podanym ID istnieje, wybierz inny numer!");
            return;
        }

        System.out.println("Podaj imię i nazwisko klienta: ");
        String customer = scanner.nextLine();

        double amount = -1;
        boolean validAmount = false;
        while (!validAmount) {
            System.out.println("Podaj wartość zamówienia: ");
            String amountInput = scanner.nextLine().trim();
            try {
                amount = Double.parseDouble(amountInput);
                if (amount < 0) {
                    System.out.println("Wartość nie może być ujemna. Spróbuj ponownie.");
                } else {
                    validAmount = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Nieprawidłowe dane. Wprowadź liczbę.");
            }
        }

        LocalDate date = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        boolean validDate = false;
        while (!validDate) {
            System.out.println("Podaj datę zamówienia (YYYY-MM-DD): ");
            String dateInput = scanner.nextLine().trim();
            try {
                date = LocalDate.parse(dateInput, formatter);
                validDate = true;
            } catch (DateTimeParseException e) {
                System.out.println("Nieprawidłowy format daty. Spróbuj ponownie.");
            }
        }

        OrderStatus orderStatus = null;
        boolean validStatus = false;
        while (!validStatus) {
            System.out.println("Podaj status zamówienia (Pending/Shipped/Delivered): ");
            String statusInput = scanner.nextLine().trim();
            switch (statusInput) {
                case "Pending" -> {
                    orderStatus = OrderStatus.PENDING;
                    validStatus = true;
                }
                case "Shipped" -> {
                    orderStatus = OrderStatus.SHIPPED;
                    validStatus = true;
                }
                case "Delivered" -> {
                    orderStatus = OrderStatus.DELIVERED;
                    validStatus = true;
                }
                default -> System.out.println("Niewłaściwy status. Spróbuj ponownie.");
            }
        }

        shop.addNewOrder(new Order(id, customer, amount, date, orderStatus));
    }


    private void editOrder() {

        System.out.println("Jeśli nie chcesz edytować danej pozycji, wciśnij enter.");

        int id = provideIDNumber();
        if (!shop.findOrderExist(id)) {
            System.out.println("Zamówienie z podanym ID nie istnieje!");
            return;
        }

        System.out.printf("Edytuj imię i nazwisko klienta (aktualne) %s: %n",
                shop.getOrderByID(id).getCustomerName());
        String customer = scanner.nextLine();
        if (!customer.isEmpty()) {
            shop.getOrderByID(id).setCustomerName(customer);
        }

        boolean validAmount = false;
        while (!validAmount) {
            System.out.printf("Edytuj wartość zamówienia (aktualna) %.2f: %n",
                    shop.getOrderByID(id).getAmount());
            String amountInput = scanner.nextLine().trim();

            if (amountInput.isEmpty()) break;

            try {
                double amount = Double.parseDouble(amountInput);
                if (amount < 0) {
                    System.out.println("Wartość nie może być ujemna. Spróbuj ponownie.");
                } else {
                    shop.getOrderByID(id).setAmount(amount);
                    validAmount = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Nieprawidłowe dane. Wprowadź liczbę.");
            }
        }

        boolean validDate = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (!validDate) {
            System.out.printf("Edytuj datę zamówienia (aktualna) %s: ",
                    shop.getOrderByID(id).getOrderDate());
            String dateInput = scanner.nextLine().trim();

            if (dateInput.isEmpty()) break;

            try {
                LocalDate date = LocalDate.parse(dateInput, formatter);
                shop.getOrderByID(id).setOrderDate(date);
                validDate = true;
            } catch (DateTimeParseException e) {
                System.out.println("Nieprawidłowy format daty. Spróbuj ponownie.");
            }
        }

        OrderStatus currentStatus = shop.getOrderByID(id).getStatus();
        boolean validStatus = false;
        while (!validStatus) {
            System.out.printf("Edytuj status zamówienia (aktualny) %s: ", currentStatus);
            String statusInput = scanner.nextLine().trim();

            if (statusInput.isEmpty()) break;

            switch (statusInput) {
                case "Pending" -> {
                    shop.getOrderByID(id).setStatus(OrderStatus.PENDING);
                    validStatus = true;
                }
                case "Shipped" -> {
                    shop.getOrderByID(id).setStatus(OrderStatus.SHIPPED);
                    validStatus = true;
                }
                case "Delivered" -> {
                    shop.getOrderByID(id).setStatus(OrderStatus.DELIVERED);
                    validStatus = true;
                }
                default -> System.out.println("Niewłaściwy status. Spróbuj ponownie.");
            }
        }
    }

    private void deleteOrder() {
        System.out.println("Usuń zamówienie ID: ");
        shop.deleteOrder(provideIDNumber());
    }

    private void viewAllGroupedOrders() {
        shop.showOrdersByStatus();
        System.out.println("Wyświetl zamówienia pogrupowane według statusu");
    }

    private void showStatistics() {
        shop.showStatistics();
        System.out.println("Wyświetl statystyki zamówień");
    }

    private void saveToFile() throws IOException {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            objectOutputStream.writeObject(shop);
        }
    }

    private void readFromFile() throws IOException, ClassNotFoundException {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            shop = (Shop) objectInputStream.readObject();
        }
    }

    private int provideIDNumber() {
        int id = -1;
        while (id < 0) {
            System.out.println("Podaj ID zamówienia: ");
            if (scanner.hasNextInt()) {
                id = scanner.nextInt();
                if (id < 0) {
                    System.out.println("ID nie może być ujemne. Spróbuj ponownie.");
                }
            } else {
                System.out.println("Nieprawidłowe dane. Wprowadź liczbę całkowitą.");
                scanner.next();
            }
        }
        scanner.nextLine();
        return id;
    }

}
