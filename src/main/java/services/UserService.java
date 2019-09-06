package services;

public class UserService {

    public static User createUser(UserSpec userSpec) {
        return new User("0", userSpec.getLogin(), userSpec.getPassowrd(), userSpec.getEmail());
    }
}
