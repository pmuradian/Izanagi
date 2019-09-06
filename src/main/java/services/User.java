package services;

public class User {
    private String id;
    private String login;
    private String passowrd;
    private String email;

    public User(String id, String login, String passowrd, String email) {
        this.id = id;
        this.login = login;
        this.passowrd = passowrd;
        this.email = email;
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

    public String getPassowrd() {
        return passowrd;
    }

    public void setPassowrd(String passowrd) {
        this.passowrd = passowrd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
