package main.persistence;

import main.commons.Range;
import main.models.Feed;
import main.models.Post;
import main.models.Result;
import main.services.StatusCodes;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@Service
public class MysqlFeedFetcher extends MysqlStorage {

    public Result<Feed> fetchFeed(String id) {
        return null;
    }

    public Boolean createFeed(String id) {
        // create new table to store feed
        return true;
    }

    public Result<Feed> getPosts(Range range, String feedID) {
        Result<Feed> result;
        try (Connection connection = getConnection()) {
            String tableName = "posts_".concat(feedID);
            String query = "select * from " + tableName + " order by creationDate offset " + range.getOffset() + " limit " + range.getCount();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultset = statement.executeQuery();
            List<Post> resultList = new ArrayList<>();
            while (resultset.next()) {
                Post post = new Post(resultset.getString(1),
                                     resultset.getString(2),
                                     resultset.getString(3),
                                     resultset.getString(4));
                resultList.add(post);
            }
            Feed feed = new Feed(resultList);
            result = new Result<>(feed, StatusCodes.OK, "");
        } catch (SQLException e) {
            result = new Result<>(null, StatusCodes.SQL_ERROR, "");
            logger.log(Level.SEVERE, e.getLocalizedMessage());
        }

        return result;
    }
}
