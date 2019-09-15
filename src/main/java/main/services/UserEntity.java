package main.services;

import org.springframework.lang.UsesSunMisc;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserEntity {
    private String id;
    private String login;
    private String password;
    private String email;

    final private static Logger logger = Logger.getLogger(UserEntity.class.getName());

    public UserEntity(String id, UserSpec userSpec) {
        this.id = id;
        this.login = userSpec.getLogin();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            byte[] bytes = userSpec.getPassword().getBytes();
            this.password = new String(MessageDigest.getInstance("SHA-512").digest(bytes));
        } catch (NoSuchAlgorithmException e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage());
            throw new IllegalStateException("Unable to hash password");
        }
        this.email = userSpec.getEmail();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
