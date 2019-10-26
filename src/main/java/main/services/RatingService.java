package main.services;

import main.entities.RatingEntity;
import main.models.Rating;
import main.models.Result;
import main.persistence.MysqlRatingStorage;
import main.specs.RatingSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

    @Autowired
    private MysqlRatingStorage storage;

    public Result<Rating> createRating(RatingSpec ratingSpec) {
        Result<RatingEntity> result = storage.store(ratingSpec);
        Rating rating = ratingFrom(result.getValue());
        Result<Rating> ratingResult = new Result<>(rating, result.getStatusCode(), result.getStatusMessage());
        return ratingResult;
    }

    public Result<Boolean> deleteRating(String id) {
        return storage.delete(id);
    }

    public Result<Rating> getRating(String id) {
        Result<RatingEntity> result = storage.get(id);
        Rating rating = ratingFrom(result.getValue());
        Result<Rating> ratingResult = new Result<>(rating, result.getStatusCode(), result.getStatusMessage());
        return ratingResult;
    }

    public Result<Rating> updateRating(String id, RatingSpec ratingSpec) {
        Result<RatingEntity> result = storage.update(id, ratingSpec);
        Rating rating = ratingFrom(result.getValue());
        Result<Rating> ratingResult = new Result<>(rating, result.getStatusCode(), result.getStatusMessage());
        return ratingResult;
    }

    private Rating ratingFrom(RatingEntity ratingEntity) {
        if (ratingEntity == null) {
            return null;
        }
        return new Rating(ratingEntity.getId(), ratingEntity.getPostID(), ratingEntity.getUserID(), ratingEntity.getType());
    }
}
