package main.persistence;

import main.entities.PostEntity;
import main.models.Result;
import main.services.*;
import main.specs.PostSpec;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.UUID;
import java.util.logging.Level;

@Service
public class MysqlPostStorage extends MysqlStorage {

    final static private String tableName = "posts";

    final static private String insertQuery = "insert into " + tableName + " values(?, ?, ?, ?)";
    public Result<PostEntity> store(PostSpec postSpec) {
        Result<PostEntity> result = null;

        try (Connection connection = getConnection()) {
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            String id = UUID.randomUUID().toString();

            PostEntity postToInsert = new PostEntity(id, postSpec);
            insertStatement.setString(1, postToInsert.getId());
            insertStatement.setString(2, postToInsert.getUserID());
            insertStatement.setString(3, postToInsert.getContent());
            insertStatement.setString(4, postToInsert.getCreationDate());
            int isInserted = insertStatement.executeUpdate();

            if (isInserted > 0) {
                result = new Result<>(postToInsert, StatusCodes.OK, "");
            } else {
                result = new Result<>(null, StatusCodes.SQL_ERROR, "Unable to create post");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            result = new Result<>(null, StatusCodes.INVALID_SPEC, "Invalid post data");
        } catch (SQLException e) {
            result = new Result<>(null, StatusCodes.SQL_ERROR, "");
        }
        return result;
    }

    final static private String getQuery = "select * from " + tableName +" where id = ?;";
    public Result<PostEntity> get(String id) {
        Result<PostEntity> result = null;

        try (Connection connection = getConnection()) {
            PreparedStatement selectStatement = connection.prepareStatement(getQuery);
            selectStatement.setString(1, id);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                PostSpec postSpec = new PostSpec(resultSet.getString(1),
                        resultSet.getString(3));

                PostEntity postEntity = new PostEntity( id, postSpec);
                result = new Result<>(postEntity, StatusCodes.OK, "");
            }
        } catch (SQLException e) {
            result = new Result<>(null, StatusCodes.SQL_ERROR, "");
            logger.log(Level.SEVERE, e.getLocalizedMessage());
        }
        return result;
    }

    final static private String deleteQuery = "delete from " + tableName + " where id = ?;";
    public Result<Boolean> delete(String id) {
        Result<Boolean> result;

        try (Connection connection = getConnection()) {
            PreparedStatement selectStatement = connection.prepareStatement(deleteQuery);
            selectStatement.setString(1, id);
            int updateResult = selectStatement.executeUpdate();

            if (updateResult > 0) {
                result = new Result<>(true, StatusCodes.OK, "");
            } else {
                result = new Result<>(false, StatusCodes.ENTITY_NOT_FOUND, "post does not exist");
            }
        } catch (SQLException e) {
            result = new Result<>(false, StatusCodes.SQL_ERROR, "");
        }
        return result;
    }

    final static private String updateQuery = "update " + tableName + " set content = ? where id = ?;";
    public Result<PostEntity> update(String id, PostSpec postSpec) {
        Result<PostEntity> result;
        try (Connection connection = getConnection()) {
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

            PostEntity postToUpdate = new PostEntity(id, postSpec);
            updateStatement.setString(1, id);
            updateStatement.setString(2, postToUpdate.getContent());

            int isUpdated = updateStatement.executeUpdate();
            if (isUpdated > 0) {
                result = new Result<>(postToUpdate, StatusCodes.OK, "");
            } else {
                result = new Result<>(null, StatusCodes.ENTITY_NOT_FOUND, "Post does not exist");
            }
        } catch (SQLException e) {
            result = new Result<>(null, StatusCodes.SQL_ERROR, "");
        }
        return result;
    }
}
