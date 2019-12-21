package main.models;

public class JWTPayload {
    private static JWTPayload payload;

    private long iat;
    private long exp;
    private String sub;
    private String login;

    private JWTPayload() {}

    public long getIssuedAt() {
        return iat;
    }

    public JWTPayload setIssuedAt(long iat) {
        this.iat = iat;
        return this;
    }

    public long getExpirationDate() {
        return exp;
    }

    public JWTPayload setExpirationDate(long exp) {
        this.exp = exp;
        return this;
    }

    public String getSubject() {
        return sub;
    }

    public JWTPayload setSubject(String sub) {
        this.sub = sub;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public JWTPayload setLogin(String login) {
        this.login = login;
        return this;
    }

    public static JWTPayload cleanInstance() {
        payload = new JWTPayload();
        return payload;
    }

   public static JWTPayload build() {
        JWTPayload builtPayload = payload;
        payload = null;
        return builtPayload;
   }

   // MARK: Validation
    public Boolean hasValidIssueDate() {
        long issuedAt = this.getIssuedAt() * 1000;
        long currentTimeMillis = System.currentTimeMillis();

        return currentTimeMillis - issuedAt > 0;
    }

    public Boolean hasValidLogin() {
        String login = this.getLogin();
        if (login == null || login.length() > 50 || (!login.matches("[A-Za-z0-9\\-_]*"))) {
            return false;
        }
        return true;
    }

    public Boolean hasValidExpirationDate() {
        long issuedAt = this.getExpirationDate() * 1000;
        long currentTimeMillis = System.currentTimeMillis();
        int oneHour = 60 * 60 * 1000;

        return currentTimeMillis - issuedAt <= oneHour;
    }

    public Boolean payloadHasValidSubject() {
        String subject = this.getSubject();

        if (subject == null) {
            return false;
        }
        return subject.matches("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})");
    }
}