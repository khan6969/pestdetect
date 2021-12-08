package model;

import java.util.Date;

public class userSpecificModel {
 String from,to,message,type;

    public userSpecificModel(String from, String to, String message, String type) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.type = type;
    }

    public userSpecificModel() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}