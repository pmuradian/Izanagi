package main.specs;

import main.services.Validatable;
import main.services.ValidationResult;
import org.apache.commons.validator.routines.EmailValidator;

public class UserSpec implements Validatable {
    private String login;
    private String password;
    private String email;
    private ValidationResult validationResult = new ValidationResult();

    public UserSpec(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public ValidationResult validate() {
        isValidEmail(this.email);
        isValidPassword(this.password);
        isValidLogin(this.login);

        return validationResult;
    }

    private void isValidLogin(String login) {
        String field = "login";
        if (login == null) {
            validationResult.put(field, "login cannot be null");
        }
        if (login.length() > 50) {
            validationResult.put(field, "login must be at most 50 characters long");
        }
        if (!login.matches("[A-Za-z0-9\\-_]*")) {
            validationResult.put(field, "login can contain only alphanumerical characters, '-', '_'");
        }
    }

    private void isValidPassword(String password) {
        String field = "password";
        if (password.length() < 8) {
            validationResult.put(field, "password must contain at least 8 characters");
        }
        if (!password.matches("[_\\-?!@#$%^&*+]+")) {
            validationResult.put(field, "password must contain at least one special character (_-?!@#$%^&*+)");
        }
        if (!password.matches("[0-9]+")) {
            validationResult.put(field, "password must contain at least one number");
        }
        if (!password.matches("[A-Z]+")) {
            validationResult.put(field, "password must contain at least one uppercase letter");
        }
        if (!password.matches("[a-z]+")) {
            validationResult.put(field, "password must contain at least one lowercase letter");
        }
    }

    private void isValidEmail(String email) {
        if (!EmailValidator.getInstance().isValid(email)) {
            validationResult.put("email", "email is invalid");
        }
    }
}
