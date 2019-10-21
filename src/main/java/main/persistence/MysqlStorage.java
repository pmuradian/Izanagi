package main.persistence;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MysqlStorage {

    final protected static Logger logger = Logger.getLogger(MysqlUserStorage.class.getName());

    protected Connection getConnection() {
        Connection connection = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "");
        } catch (ClassNotFoundException | SQLException ex) {
            logger.log(Level.SEVERE, "Unable to obtain a connection", ex);
        }

        return connection;
    }
}
