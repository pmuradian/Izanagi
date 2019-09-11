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
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb","root","");
         } catch (ClassNotFoundException | SQLException ex) {
             logger.log(Level.SEVERE, "Unable to obtain a connection", ex);
         }

         return connection;
     }

//     private void closeConnection(Connection connection) {
//         try {
//             connection.close();
//         } catch (Exception ex) {
//             logger.log(Level.SEVERE, "Unable to close a connection");
//         }
//     }

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
         return null;
//         return storage.get(id);
    }

    public Boolean delete(String id) {
         return null;
//        return storage.remove(id) != null;
    }

    public UserEntity update(String id, UserSpec userSpec) {
        UserEntity userEntity = new UserEntity(id, userSpec);
//        storage.put(id, userEntity);
        return userEntity;
    }
}
