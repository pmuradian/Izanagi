package main.models;

import main.specs.RatingType;

public class Rating {

    private String id;
    private String postID;
    private String userID;
    private RatingType type;

    public Rating(String id, String postID, String userID, RatingType type) {
        this.id = id;
        this.postID = postID;
        this.userID = userID;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public RatingType getType() {
        return type;
    }

    public void setType(RatingType type) {
        this.type = type;
    }
}
