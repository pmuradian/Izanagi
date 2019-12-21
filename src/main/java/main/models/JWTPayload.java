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
}
