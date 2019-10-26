package main.entities;

import main.specs.PostSpec;

import java.util.Date;

public class PostEntity {

    private String id;
    private String userID;
    private String content;
    private String creationDate;

    public PostEntity(String id, String userID, String content, String creationDate) {
        this.id = id;
        this.userID = userID;
        this.content = content;
        this.creationDate = creationDate;
    }

    public PostEntity(String id, PostSpec postSpec) {
        this(id, postSpec.getUserID(), postSpec.getContent(), new Date().toString());
    }

    public String getId() {
        return id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}
