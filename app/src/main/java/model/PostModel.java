package model;


import android.app.Application;

import com.google.firebase.Timestamp;

public class PostModel{
private String name;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    private String price;
   private String number;
    private String condition;
    private String imageURi;
    private Timestamp timeAdd;
    private static PostModel instance;

    public PostModel() {}

    public PostModel(String name,String price,String number, String condition, String imageURi, Timestamp timeAdd) {
        this.name = name;
        this.condition = condition;
        this.imageURi = imageURi;
        this.timeAdd = timeAdd;
        this.price =price;
        this.number=number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getImageURi() {
        return imageURi;
    }

    public void setImageURi(String imageURi) {
        this.imageURi = imageURi;
    }

    public Timestamp getTimeAdd() {
        return timeAdd;
    }

    public void setTimeAdd(Timestamp timeAdd) {
        this.timeAdd = timeAdd;
    }




//    public void setTimeAdd(com.google.firebase.Timestamp timestamp) {
//    }
}
