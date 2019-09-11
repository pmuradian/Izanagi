package main.services;

import main.persistence.MysqlUserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private MysqlUserStorage mysqlUserStorage;

    public User createUser(UserSpec userSpec) {
        UserEntity userEntity = mysqlUserStorage.store(userSpec);
        return userFrom(userEntity);
    }

    public Boolean deleteUser(String id) {
        return mysqlUserStorage.delete(id);
    }

    public User getUser(String id) {
        UserEntity userEntity = mysqlUserStorage.get(id);
        return userFrom(userEntity);
    }

    public User updateUser(String id, UserSpec userSpec) {
        UserEntity userEntity = mysqlUserStorage.update(id, userSpec);
        return userFrom(userEntity);
    }

    private User userFrom(UserEntity userEntity) {
        return new User(userEntity.getId(), userEntity.getLogin(), userEntity.getPassword(), userEntity.getEmail());
    }
}
