package com.example.myapplication;

import java.util.Date;
import java.util.UUID;

public class Order {
    private UUID uuid;
    private int sumPrice;
    private Date dateOrder;
    private String customer;
    private String phoneNumber;
    private String address;
    private String note;

    public Order()
    {
        this(UUID.randomUUID());
        sumPrice = 0;
        dateOrder = new Date();
        customer = "";
        phoneNumber = "";
        address = "";
        note = "";
    }

    public Order(UUID uuid)
    {
        this.uuid = uuid;
        sumPrice = 0;
        dateOrder = new Date();
        customer = "";
        phoneNumber = "";
        address = "";
        note = "";
    }

    public Order(UUID uuid, int sumPrice, Date dateOrder, String customer, String phoneNumber, String address, String note) {
        this.uuid = uuid;
        this.sumPrice = sumPrice;
        this.dateOrder = dateOrder;
        this.customer = customer;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.note = note;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(int sumPrice) {
        this.sumPrice = sumPrice;
    }

    public Date getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(Date dateOrder) {
        this.dateOrder = dateOrder;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
