package main.specs;

public class RatingSpec {
    private String postID;
    private String userID;
    private RatingType type;

    public RatingSpec(String postID, String userID, RatingType type) {
        this.postID = postID;
        this.userID = userID;
        this.type = type;
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
