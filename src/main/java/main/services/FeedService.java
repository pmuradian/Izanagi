package main.services;

import main.commons.Range;
import main.entities.UserEntity;
import main.models.Feed;
import main.models.Post;
import main.models.Result;
import main.models.User;
import main.persistence.MysqlFeedFetcher;
import main.persistence.MysqlUserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FeedService {
    @Autowired
    private MysqlFeedFetcher feedFetcher;
    @Autowired
    private MysqlUserStorage userStorage;

    private Map<String, Feed> feeds = new HashMap<>();

    public Result<Feed> getPosts(Range range,  String userID) {
        Result<UserEntity> user = userStorage.get(userID);

        switch (user.getStatusCode()) {
            case OK:
                UserEntity entity = user.getValue();
//                userStorage.
//                Result result = feedFetcher.getPosts(range, userID);
                return feedFetcher.getPosts(range, userID);
            default:
                return feedFetcher.getPosts(range, userID);
        }
    }
}
