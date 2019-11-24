package main.services;

import main.utils.Range;
import main.models.Post;
import main.models.Result;
import main.persistence.MysqlFeedFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedService {
    @Autowired
    private MysqlFeedFetcher feedFetcher;

    public Result<List<Post>> getPosts(Range range) {
        return feedFetcher.getPosts(range);
    }
}
