package com.example.listapp;

public class Message {
    private String title ="";
    private String subtitle ="";
    public Message(String title, String subtitle) {
        this.title=title;
        this.subtitle =subtitle;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title =title;
    }
    public String getSubtitle() {
        return subtitle;
    }
    public void setSubtitle(String subtitle) {
        this.subtitle= subtitle;
    }
}
