package main.persistence;

import main.services.UserEntity;
import main.services.UserSpec;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class MysqlUserStorage {
    final private String tableName = "users";
    final private static Logger logger = Logger.getLogger(MysqlUserStorage.class.getName());

     private Connection getConnection() {
         Connection connection = null;

         try {
             Class.forName("com.mysql.cj.jdbc.Driver");
             connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "");
         } catch (ClassNotFoundException | SQLException ex) {
             logger.log(Level.SEVERE, "Unable to obtain a connection", ex);
         }

         return connection;
     }

    public UserEntity store(UserSpec userSpec) {
        UserEntity userEntity = null;
        try (Connection connection = getConnection()) {
            PreparedStatement insertStatement = connection.prepareStatement("insert into users values(?, ?, ?, ?);");
            String id = UUID.randomUUID().toString();
            UserEntity userToInsert = new UserEntity(id, userSpec);
            insertStatement.setString(1, userToInsert.getId());
            insertStatement.setString(2, userToInsert.getLogin());
            insertStatement.setString(3, userToInsert.getPassword());
            insertStatement.setString(4, userToInsert.getEmail());

            Integer isInserted = insertStatement.executeUpdate();
            if (isInserted > 0) {
                userEntity = userToInsert;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userEntity;
    }

    public UserEntity get(String id) {
        UserEntity userEntity = null;
        try (Connection connection = getConnection()) {
            PreparedStatement selectStatement = connection.prepareStatement("select * from users where id = ?;");
            selectStatement.setString(1, id);
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                UserSpec userSpec = new UserSpec(resultSet.getString(2),
                                                 resultSet.getString(3),
                                                 resultSet.getString(4));
                userEntity = new UserEntity(id, userSpec);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userEntity;
    }

    public Boolean delete(String id) {
        try (Connection connection = getConnection()) {
            PreparedStatement selectStatement = connection.prepareStatement("delete from users where id = ?;");
            selectStatement.setString(1, id);
            Integer result = selectStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public UserEntity update(String id, UserSpec userSpec) {
        UserEntity userEntity = null;
        try (Connection connection = getConnection()) {
            PreparedStatement insertStatement = connection.prepareStatement("update users set login = ?, password = ?, email = ? where id = ?;");
            UserEntity userToInsert = new UserEntity(id, userSpec);
            insertStatement.setString(1, userToInsert.getLogin());
            insertStatement.setString(2, userToInsert.getPassword());
            insertStatement.setString(3, userToInsert.getEmail());
            insertStatement.setString(4, userToInsert.getId());

            Integer isInserted = insertStatement.executeUpdate();
            if (isInserted > 0) {
                userEntity = userToInsert;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userEntity;
    }
}
