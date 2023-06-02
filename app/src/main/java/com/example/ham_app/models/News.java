package com.example.ham_app.models;

import java.util.Date;

public class News {
    private String id;
    private String title;
    private String body;
    private String imgUrl;

    private Date postDate;
    public News() {

    }

    public News(String title, String body, String imgUrl,Date postDate) {
        this.title = title;
        this.body = body;
        this.imgUrl = imgUrl;
        this.postDate = postDate;
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

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }
}
