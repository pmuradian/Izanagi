package main.services;

import main.entities.PostEntity;
import main.models.Post;
import main.models.Result;
import main.persistence.MysqlPostStorage;
import main.specs.PostSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    private MysqlPostStorage mysqlPostStorage;

    public Result<Post> createPost(PostSpec postSpec) {
        Result<PostEntity> result = mysqlPostStorage.store(postSpec);
        Post post = postFrom(result.getValue());
        Result<Post> postResult = new Result<>(post, result.getStatusCode(), result.getStatusMessage());
        return postResult;
    }

    public Result<Boolean> deletePost(String id) {
        return mysqlPostStorage.delete(id);
    }

    public Result<Post> getPost(String id) {
        Result<PostEntity> result = mysqlPostStorage.get(id);
        Post post = postFrom(result.getValue());
        Result<Post> postResult = new Result<>(post, result.getStatusCode(), result.getStatusMessage());
        return postResult;
    }

    public Result<Post> updatePost(String id, PostSpec postSpec) {
        Result<PostEntity> result = mysqlPostStorage.update(id, postSpec);
        Post post = postFrom(result.getValue());
        Result<Post> postResult = new Result<>(post, result.getStatusCode(), result.getStatusMessage());
        return postResult;
    }

    private Post postFrom(PostEntity postEntity) {
        if (postEntity == null) {
            return null;
        }
        return new Post(postEntity.getId(), postEntity.getUserID(), postEntity.getContent(), postEntity.getCreationDate());
    }
}
