package main.controllers;

import main.commons.Range;
import main.models.Feed;
import main.models.Post;
import main.models.Result;
import main.services.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("")
public class FeedController {

    @Autowired
    private FeedService feedService;

    @GetMapping(value = "users/{id}/feed")
    public ResponseEntity<Feed> getPosts(@RequestBody Range range, @RequestParam String userId) {
        Result<Feed> result = feedService.getPosts(range, userId);
        HttpStatus status = HttpStatus.resolve(result.getStatusCode().getCode());
        return new ResponseEntity(result, status);
    }
}
