package main.persistence;

import main.entities.UserEntity;
import main.models.Result;
import main.services.*;
import main.specs.UserSpec;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

@Service
public class MysqlUserStorage extends MysqlStorage {

    public Result<UserEntity> store(UserSpec userSpec) {
        Result<UserEntity> result = null;

        try (Connection connection = getConnection()) {
            String queryString = "insert into users values(?, ?, ?, ?);";
            PreparedStatement insertStatement = connection.prepareStatement(queryString);
            String id = UUID.randomUUID().toString();

            UserEntity userToInsert = new UserEntity(id, userSpec);
            insertStatement.setString(1, userToInsert.getId());
            insertStatement.setString(2, userToInsert.getLogin());
            insertStatement.setString(3, userToInsert.getPassword());
            insertStatement.setString(4, userToInsert.getEmail());
            int isInserted = insertStatement.executeUpdate();

            if (isInserted > 0) {
                result = new Result<>(userToInsert, StatusCodes.OK, "");
            } else {
                result = new Result<>(null, StatusCodes.SQL_ERROR, "Unable to create user");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            String[] messageArray = e.getMessage().split(" ");
            String lastWord = messageArray[messageArray.length - 1];

            if (lastWord.contains("login")) {
                result = new Result<>(null, StatusCodes.LOGIN_BUSY, "Login is already taken");
            } else if (e.getMessage().contains("email")) {
                result = new Result<>(null, StatusCodes.USER_EXISTS, "User exists");
            }
        } catch (SQLException e) {
            result = new Result<>(null, StatusCodes.SQL_ERROR, "");
        }
        return result;
    }

    public Result<UserEntity> get(String id) {
        Result<UserEntity> result = null;

        try (Connection connection = getConnection()) {
            String queryString = "select * from users where id = ?;";
            PreparedStatement selectStatement = connection.prepareStatement(queryString);
            selectStatement.setString(1, id);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                UserSpec userSpec = new UserSpec(resultSet.getString(2),
                                                 resultSet.getString(3),
                                                 resultSet.getString(4));

                UserEntity userEntity = new UserEntity( id, userSpec);
                result = new Result<>(userEntity, StatusCodes.OK, "");
            } else {
                result = new Result<>(null, StatusCodes.ENTITY_NOT_FOUND, "");
            }
        } catch (SQLException e) {
            result = new Result<>(null, StatusCodes.SQL_ERROR, "");
            logger.log(Level.SEVERE, e.getLocalizedMessage());
        }
        return result;
    }

    public Result<UserEntity> get(String login, String password) {
        Result<UserEntity> result = null;
        String hashedPassword = password;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            byte[] bytes = password.getBytes();
            hashedPassword = new String(MessageDigest.getInstance("SHA-512").digest(bytes));
        } catch (NoSuchAlgorithmException e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage());
            throw new IllegalStateException("Unable to hash password");
        }

        try (Connection connection = getConnection()) {
            String queryString = "select * from users where login = ? and password = ?;";
            PreparedStatement selectStatement = connection.prepareStatement(queryString);
            selectStatement.setString(1, login);
            selectStatement.setString(2, hashedPassword);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                UserSpec userSpec = new UserSpec(resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4));
                String id = resultSet.getString(1);

                UserEntity userEntity = new UserEntity( id, userSpec);
                result = new Result<>(userEntity, StatusCodes.OK, "");
            }
        } catch (SQLException e) {
            result = new Result<>(null, StatusCodes.SQL_ERROR, "");
            logger.log(Level.SEVERE, e.getLocalizedMessage());
        }
        return result;
    }

    public Result<Boolean> delete(String id) {
        Result<Boolean> result;

        try (Connection connection = getConnection()) {
            String queryString = "delete from users where id = ?;";
            PreparedStatement selectStatement = connection.prepareStatement(queryString);
            selectStatement.setString(1, id);
            int updateResult = selectStatement.executeUpdate();

            if (updateResult > 0) {
                result = new Result<>(true, StatusCodes.OK, "");
            } else {
                result = new Result<>(false, StatusCodes.ENTITY_NOT_FOUND, "User does not exist");
            }
        } catch (SQLException e) {
           result = new Result<>(false, StatusCodes.SQL_ERROR, "");
        }
        return result;
    }

    public Result<UserEntity> update(String id, UserSpec userSpec) {
        Result<UserEntity> result;
        try (Connection connection = getConnection()) {
            String queryString = "update users set login = ?, password = ?, email = ? where id = ?;";
            PreparedStatement updateStatement = connection.prepareStatement(queryString);

            UserEntity userToUpdate = new UserEntity(id, userSpec);
            updateStatement.setString(1, userToUpdate.getLogin());
            updateStatement.setString(2, userToUpdate.getPassword());
            updateStatement.setString(3, userToUpdate.getEmail());
            updateStatement.setString(4, userToUpdate.getId());

            int isUpdated = updateStatement.executeUpdate();
            if (isUpdated > 0) {
                result = new Result<>(userToUpdate, StatusCodes.OK, "");
            } else {
                result = new Result<>(null, StatusCodes.ENTITY_NOT_FOUND, "User does not exist");
            }
        } catch (SQLException e) {
            result = new Result<>(null, StatusCodes.SQL_ERROR, "");
        }
        return result;
    }

//    public Result<List<UserEntity>> getFollowings(String userID) {
//        Result<UserEntity> result;
//        try (Connection connection = getConnection()) {
//            String queryString = "select user from users where id = ?;";
//            PreparedStatement updateStatement = connection.prepareStatement(queryString);
//
//            updateStatement.setString(1, userID);
//
//            int isUpdated = updateStatement.executeUpdate();
//            if (isUpdated > 0) {
//                result = new Result<>(userToUpdate, StatusCodes.OK, "");
//            } else {
//                result = new Result<>(null, StatusCodes.ENTITY_NOT_FOUND, "User does not exist");
//            }
//        } catch (SQLException e) {
//            result = new Result<>(null, StatusCodes.SQL_ERROR, "");
//        }
//        return result;
//    }
}
