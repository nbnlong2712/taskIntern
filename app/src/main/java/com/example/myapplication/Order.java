package com.example.myapplication;

import java.util.Date;
import java.util.UUID;

public class Order {
    private UUID uuid;
    private Date dateOrder;
    private String customer;
    private String phoneNumber;
    private String address;
    private String note;

    public Order()
    {
        this(UUID.randomUUID());
        dateOrder = new Date();
        customer = "";
        phoneNumber = "";
        address = "";
        note = "";
    }

    public Order(UUID uuid)
    {
        this.uuid = uuid;
        dateOrder = new Date();
        customer = "";
        phoneNumber = "";
        address = "";
        note = "";
    }

    public Order(UUID uuid, Date dateOrder, String customer, String phoneNumber, String address, String note) {
        this.uuid = uuid;
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
