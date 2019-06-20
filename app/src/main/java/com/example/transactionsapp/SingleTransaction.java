package com.example.transactionsapp;

import java.sql.Time;
import java.util.Date;
import java.lang.reflect.Constructor;

public class SingleTransaction {

    private String mode, personName, date, time, amount, desc;


    public SingleTransaction(String amt, String modex, String personNamex, String descx) {
        mode = modex;
        personName = personNamex;
        amount = String.valueOf(amt);
        desc = descx;
    }

    public SingleTransaction(String amt, String modex, String personNamex, String descx, String datex, String timex) {
        mode = modex;
        personName = personNamex;
        amount = String.valueOf(amt);
        desc = descx;
        date = datex;
        time = timex;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getAmount() {
            return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
