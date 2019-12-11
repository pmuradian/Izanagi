package main.models;

import java.util.List;
import java.util.UUID;

public class Feed {

    private String id = UUID.randomUUID().toString();

    public Feed(List<Post> posts) {
        this.posts = posts;
    }

    private List<Post> posts;

    public String getId() {
        return id;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
