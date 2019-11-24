package main.controllers;

import main.utils.Range;
import main.models.Post;
import main.models.Result;
import main.services.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("")
public class FeedController {

    @Autowired
    private FeedService feedService;

    @GetMapping(value = "/feed")
    public ResponseEntity<List<Post>> getPosts(@RequestBody Range range) {
        Result<List<Post>> result = feedService.getPosts(range);
        HttpStatus status = HttpStatus.resolve(result.getStatusCode().getCode());
        return new ResponseEntity(result, status);
    }
}
