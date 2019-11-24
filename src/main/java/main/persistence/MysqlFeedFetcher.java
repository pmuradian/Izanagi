package main.persistence;

import main.utils.Range;
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

    public Result<List<Post>> getPosts(Range range) {
        List<Post> resultList = new ArrayList<>();
        Result<List<Post>> result;
        try (Connection connection = getConnection()) {
            String query = "select * from posts order by creationDate offset " + range.getOffset() + " limit " + range.getCount();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultset = statement.executeQuery();

            while (resultset.next()) {
                Post post = new Post(resultset.getString(1),
                                     resultset.getString(2),
                                     resultset.getString(3),
                                     resultset.getString(4));
                resultList.add(post);
            }
            result = new Result<>(resultList, StatusCodes.OK, "");
        } catch (SQLException e) {
            result = new Result<>(null, StatusCodes.SQL_ERROR, "");
            logger.log(Level.SEVERE, e.getLocalizedMessage());
        }

        return result;
    }
}
