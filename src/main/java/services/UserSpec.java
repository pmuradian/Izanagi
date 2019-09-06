package services;

public class UserSpec {
    private String login;
    private String passowrd;
    private String email;

    public UserSpec(String login, String passowrd, String email) {
        this.login = login;
        this.passowrd = passowrd;
        this.email = email;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassowrd(String passowrd) {
        this.passowrd = passowrd;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public String getPassowrd() {
        return passowrd;
    }

    public String getEmail() {
        return email;
    }
}
