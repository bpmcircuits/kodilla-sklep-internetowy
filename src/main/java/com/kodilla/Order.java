package com.kodilla;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Order implements Serializable {

    private final int id;
    private String customerName;
    private double amount;
    private LocalDate orderDate;
    private OrderStatus status;

    public Order(int id, String customerName, double amount, LocalDate orderDate, OrderStatus status) {
        this.id = id;
        this.customerName = customerName;
        this.amount = amount;
        this.orderDate = orderDate;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && Objects.equals(customerName, order.customerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerName);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                ", Klient: " + customerName +
                ", Kwota: " + amount +
                ", Data: " + orderDate;
    }
}
