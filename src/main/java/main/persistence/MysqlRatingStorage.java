package main.persistence;

import main.entities.RatingEntity;
import main.models.Result;
import main.services.StatusCodes;
import main.specs.RatingSpec;
import main.specs.RatingType;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.UUID;
import java.util.logging.Level;

@Service
public class MysqlRatingStorage extends MysqlStorage {
    final static private String tableName = "ratings";

    final static private String insertQuery = "insert into " + tableName + " values(?, ?, ?, ?)";
    public Result<RatingEntity> store(RatingSpec ratingSpec) {
        Result<RatingEntity> result = null;

        try (Connection connection = getConnection()) {
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            String id = UUID.randomUUID().toString();

            RatingEntity ratingToInsert = new RatingEntity(id, ratingSpec);
            insertStatement.setString(1, ratingToInsert.getId());
            insertStatement.setString(3, ratingToInsert.getPostID());
            insertStatement.setString(2, ratingToInsert.getUserID());
            insertStatement.setInt(4, ratingToInsert.getType().rawValue);
            int isInserted = insertStatement.executeUpdate();

            if (isInserted > 0) {
                result = new Result<>(ratingToInsert, StatusCodes.OK, "");
            } else {
                result = new Result<>(null, StatusCodes.SQL_ERROR, "Unable to create rating");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            result = new Result<>(null, StatusCodes.INVALID_SPEC, "Invalid rating data");
        } catch (SQLException e) {
            result = new Result<>(null, StatusCodes.SQL_ERROR, "");
        }
        return result;
    }

    final static private String getQuery = "select * from " + tableName +" where id = ?;";
    public Result<RatingEntity> get(String id) {
        Result<RatingEntity> result = null;

        try (Connection connection = getConnection()) {
            PreparedStatement selectStatement = connection.prepareStatement(getQuery);
            selectStatement.setString(1, id);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                RatingSpec ratingSpec = new RatingSpec(resultSet.getString(2),
                                                        resultSet.getString(3),
                                                        RatingType.fromValue(resultSet.getInt(4)));

                RatingEntity ratingEntity = new RatingEntity(id, ratingSpec);
                result = new Result<>(ratingEntity, StatusCodes.OK, "");
            }
        } catch (SQLException e) {
            result = new Result<>(null, StatusCodes.SQL_ERROR, "");
            logger.log(Level.SEVERE, e.getLocalizedMessage());
        }
        return result;
    }

    // TODO: not needed, remove later
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
                result = new Result<>(false, StatusCodes.ENTITY_NOT_FOUND, "rating does not exist");
            }
        } catch (SQLException e) {
            result = new Result<>(false, StatusCodes.SQL_ERROR, "");
        }
        return result;
    }

    final static private String updateQuery = "update " + tableName + " set type = ? where id = ?;";
    public Result<RatingEntity> update(String id, RatingSpec ratingSpec) {
        Result<RatingEntity> result;
        try (Connection connection = getConnection()) {
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

            RatingEntity ratingEntity = new RatingEntity(id, ratingSpec);
            updateStatement.setString(1, id);
            updateStatement.setInt(2, ratingEntity.getType().rawValue);

            int isUpdated = updateStatement.executeUpdate();
            if (isUpdated > 0) {
                result = new Result<>(ratingEntity, StatusCodes.OK, "");
            } else {
                result = new Result<>(null, StatusCodes.ENTITY_NOT_FOUND, "Rating does not exist");
            }
        } catch (SQLException e) {
            result = new Result<>(null, StatusCodes.SQL_ERROR, "");
        }
        return result;
    }
}
