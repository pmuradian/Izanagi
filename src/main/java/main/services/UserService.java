package main.services;

import main.entities.UserEntity;
import main.models.Result;
import main.models.User;
import main.persistence.MysqlUserStorage;
import main.specs.UserSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private MysqlUserStorage mysqlUserStorage;

    public Result<User> createUser(UserSpec userSpec) {
        Result<UserEntity> result = mysqlUserStorage.store(userSpec);
        User user = userFrom(result.getValue());
        Result<User> userResult = new Result<>(user, result.getStatusCode(), result.getStatusMessage());
        return userResult;
    }

    public Result<Boolean> deleteUser(String id) {
        return mysqlUserStorage.delete(id);
    }

    public Result<User> getUser(String id) {
        Result<UserEntity> result = mysqlUserStorage.get(id);
        User user = userFrom(result.getValue());
        Result<User> userResult = new Result<>(user, result.getStatusCode(), result.getStatusMessage());
        return userResult;
    }

    public Result<User> updateUser(String id, UserSpec userSpec) {
        Result<UserEntity> result = mysqlUserStorage.update(id, userSpec);
        User user = userFrom(result.getValue());
        Result<User> userResult = new Result<>(user, result.getStatusCode(), result.getStatusMessage());
        return userResult;
    }

    private User userFrom(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        return new User(userEntity.getId(), userEntity.getLogin(), userEntity.getPassword(), userEntity.getEmail());
    }

    public Result<User> getUserByCredentials(String login, String password) {
        Result<UserEntity> result = mysqlUserStorage.get(login, password);
        User user = userFrom(result.getValue());
        Result<User> userResult = new Result<>(user, result.getStatusCode(), result.getStatusMessage());
        return userResult;
    }
}
