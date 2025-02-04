package com.kodilla;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Shop implements Serializable {

    private final Map<Integer, Order> orderList = new HashMap<>();

    public void addNewOrder(Order order) {
        orderList.put(order.getId(), order);
    }

    public boolean findOrderExist(int id) {
        return orderList.containsKey(id);
    }

    public Order getOrderByID(int id) {
        return orderList.get(id);
    }

    public boolean deleteOrder(int id) {
        if (!orderList.isEmpty() && orderList.get(id) != null) {
            orderList.remove(id);
            System.out.println("Usunięto!!!");
            return true;
        } else return false;
    }

    public void showOrdersByStatus() {

        Map<OrderStatus, Map<Integer, Order>> groupedOrders = orderList.entrySet()
                .stream()
                .collect(Collectors.groupingBy(
                        entry -> entry.getValue().getStatus(),  // Grupowanie po statusie
                        Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue) // Zamiana na Map<Integer, Order>
                ));

        System.out.println("Zamówienia według statusu:");

        for (Map.Entry<OrderStatus, Map<Integer, Order>> entry : groupedOrders.entrySet()) {
            System.out.printf("%s: %n", entry.getKey());
            for (Map.Entry<Integer, Order> o : entry.getValue().entrySet()) {
                System.out.println(o.getValue());
            }
        }
    }

    public void showStatistics() {

        int sumOfOrders = orderList.size();
        double sumOfAmount = orderList.values().stream().mapToDouble(Order::getAmount).sum();

        Map<OrderStatus, Long> orderCountByStatus = orderList.values().stream()
                .collect(Collectors.groupingBy(Order::getStatus, Collectors.counting()));

        System.out.println("Statystyki zamówień:");
        System.out.printf("- Łączna liczba zamówień: %d %n", sumOfOrders);
        System.out.printf("- Suma wartości zamówień: %.2f %n", sumOfAmount);
        System.out.println("- Liczba zamówień w podziale na statusy:");
        orderCountByStatus.forEach(((orderStatus, amount) ->
                System.out.println(orderStatus + ": " + amount)));
    }


}
