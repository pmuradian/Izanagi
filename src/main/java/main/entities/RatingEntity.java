package main.entities;

import main.specs.RatingSpec;
import main.specs.RatingType;

public class RatingEntity {

    private String id;
    private String postID;
    private String userID;
    private RatingType type;

    public RatingEntity(String id, String postID, String userID, RatingType type) {
        this.id = id;
        this.postID = postID;
        this.userID = userID;
        this.type = type;
    }

    public RatingEntity(String id, RatingSpec ratingSpec) {
        this(id, ratingSpec.getPostID(), ratingSpec.getUserID(), ratingSpec.getType());
    }

    public String getId() {
        return id;
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
