package main.services;

import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import main.models.JWTPayload;
import main.models.Result;
import main.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.ArrayList;

@Service
public class TokenService {
    @Autowired
    private UserService userService;
    private ArrayList<Integer> tokenHashes = new ArrayList<>();
    private static final Key key = Keys.hmacShaKeyFor("this is my secret key this is my secret key this is my secret key".getBytes());

    public Result<String> getToken(String login, String password) {
        Result<String> result = Result.ofType("");

        final Result<User> userResult = userService.getUserByCredentials(login, password);
        if (userResult.getStatusCode() != StatusCodes.OK || userResult.getValue() == null) {
            return new Result<String>(null, userResult.getStatusCode(), userResult.getStatusMessage());
        }

        final long issueDate = System.currentTimeMillis();
        final int oneHour = 60 * 60 * 1000;
        final long expirationDate = issueDate + oneHour;

        try {
            JWTPayload payload = JWTPayload.cleanInstance()
                    .setIssuedAt(issueDate / 1000)
                    .setExpirationDate(expirationDate / 1000)
                    .setSubject(userResult.getValue().getId())
                    .setLogin(login);

            String jws = Jwts.builder()
                    .setPayload(new Gson().toJson(payload))
                    .signWith(key)
                    .compact();
            tokenHashes.add(jws.hashCode());
            result = new Result<>(jws, StatusCodes.OK);
        } catch (Exception e) {
            result = new Result<>(null, StatusCodes.INVALID_RESULT, e.getMessage());
        }
        return result;
    }

    public Result<Boolean> deleteToken(String token) {
        Boolean isRemoved = tokenHashes.remove(new Integer(token.hashCode()));
        StatusCodes statusCode = isRemoved ? StatusCodes.OK : StatusCodes.ENTITY_NOT_FOUND;
        return new Result<>(isRemoved, statusCode, null);
    }

    public void addToken(String token) {
        tokenHashes.add(token.hashCode());
    }

    public Boolean exists(String token) {
        return tokenHashes.contains(token.hashCode());
    }

    public Boolean payloadHasValidIssueDate(JWTPayload payload) {
        return payload.hasValidIssueDate();
    }

    public Boolean payloadHasValidLogin(JWTPayload payload) {
        return payload.hasValidLogin();
    }

    public Boolean payloadHasValidExpirationDate(JWTPayload payload) {
        return payload.hasValidExpirationDate();
    }

    public Boolean payloadHasValidSubject(JWTPayload payload) {
        return payload.payloadHasValidSubject();
    }
}
