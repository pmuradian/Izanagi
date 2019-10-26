package main.controllers;

import main.models.Rating;
import main.models.Result;
import main.services.RatingService;
import main.specs.RatingSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @PostMapping(value = "/ratings")
    public ResponseEntity<Result<Rating>> createRating(@RequestBody RatingSpec ratingSpec) {
        Result<Rating> result = ratingService.createRating(ratingSpec);
        HttpStatus status = HttpStatus.resolve(result.getStatusCode().getCode());
        return new ResponseEntity<>(result, status);
    }

    @DeleteMapping(value = "/rating/{id}")
    public ResponseEntity<Result<Boolean>> deletePost(@RequestParam String id) {
        Result<Boolean> result = ratingService.deleteRating(id);
        HttpStatus status = HttpStatus.resolve(result.getStatusCode().getCode());
        return new ResponseEntity<>(result, status);
    }

    @GetMapping(value = "/rating/{id}")
    public ResponseEntity<Result<Rating>> getPost(@RequestParam String id){
        Result<Rating> result = ratingService.getRating(id);
        HttpStatus status = HttpStatus.resolve(result.getStatusCode().getCode());
        return new ResponseEntity<>(result, status);
    }

    @PatchMapping(value = "/rating/{id}")
    public ResponseEntity<Result<Rating>> updatePost(@RequestParam String id, @RequestBody RatingSpec ratingSpec) {
        Result<Rating> result = ratingService.updateRating(id, ratingSpec);
        HttpStatus status = HttpStatus.resolve(result.getStatusCode().getCode());
        return new ResponseEntity<>(result, status);
    }
}
