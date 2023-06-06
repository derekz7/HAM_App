package com.example.ham_app.modules;

import java.util.Date;

public class News {
    private String id;
    private String title;
    private String body;
    private String imgUrl;

    private String postDate;
    private String Url;
    public News() {

    }

    public News(String id, String title, String body, String imgUrl, String postDate, String url) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.imgUrl = imgUrl;
        this.postDate = postDate;
        Url = url;
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

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
