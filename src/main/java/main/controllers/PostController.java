package main.controllers;

import main.persistence.MysqlPostStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import main.services.*;

@RestController("")
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping(value = "/posts")
    public ResponseEntity<Result<Post>> createPost(@RequestBody PostSpec postSpec) {
        Result<Post> result;
        result = postService.createPost(postSpec);
        HttpStatus status = HttpStatus.resolve(result.getStatusCode().getCode());
        return new ResponseEntity<>(result, status);
    }

    @DeleteMapping(value = "/posts/{id}")
    public ResponseEntity<Result<Boolean>> deletePost(@RequestParam String id) {
        Result<Boolean> result = postService.deletePost(id);
        HttpStatus status = HttpStatus.resolve(result.getStatusCode().getCode());
        return new ResponseEntity<>(result, status);
    }

    @GetMapping(value = "/posts/{id}")
    public ResponseEntity<Result<Post>> getPost(@RequestParam String id){
        Result<Post> result = postService.getPost(id);
        HttpStatus status = HttpStatus.resolve(result.getStatusCode().getCode());
        return new ResponseEntity<>(result, status);
    }

    @PatchMapping(value = "/posts/{id}")
    public ResponseEntity<Result<Post>> updatePost(@RequestParam String id, @RequestBody PostSpec postSpec) {
        Result<Post> result = postService.updatePost(id, postSpec);
        HttpStatus status = HttpStatus.resolve(result.getStatusCode().getCode());
        return new ResponseEntity<>(result, status);
    }

}
