package com.example.ham_app.models;

public class News {
    private String id;
    private String title;
    private String body;
    private String imgUrl;

    public News() {

    }

    public News(String title, String body, String imgUrl) {
        this.title = title;
        this.body = body;
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
