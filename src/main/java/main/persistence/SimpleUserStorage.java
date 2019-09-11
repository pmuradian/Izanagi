package main.persistence;

import main.services.User;
import main.services.UserEntity;
import main.services.UserSpec;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
public class SimpleUserStorage {

    private HashMap<String, UserEntity> storage = new HashMap();

    public UserEntity store(UserSpec userSpec) {
        String id = UUID.randomUUID().toString();
        UserEntity userEntity = new UserEntity(id, userSpec);
        storage.put(id, userEntity);
        return userEntity;
    }

    public UserEntity get(String id) {
        return storage.get(id);
    }

    public Boolean delete(String id) {
        return storage.remove(id) != null;
    }

    public UserEntity update(String id, UserSpec userSpec) {
        UserEntity userEntity = new UserEntity(id, userSpec);
        storage.put(id, userEntity);
        return userEntity;
    }

}
