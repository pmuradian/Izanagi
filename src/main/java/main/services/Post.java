package main.services;

public class Post {

    private String id;
    private String userID;
    private String content;
    private String creationDate;

    public Post(String id, String userID, String content, String creationDate) {
        this.id = id;
        this.userID = userID;
        this.content = content;
        this.creationDate = creationDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
