package main.services;

import com.sun.org.apache.xpath.internal.operations.Bool;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import main.models.Result;
import main.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

@Service
public class TokenService {
    @Autowired
    private UserService userService;
    private ArrayList<Integer> tokenHashes = new ArrayList<>();

    public Result<String> getToken(String login, String password) {
        Result<String> result = Result.ofType("");
        final Key key = Keys.hmacShaKeyFor("this is my secret key this is my secret key this is my secret key".getBytes());

        final Result<User> userResult = userService.getUserByCredentials(login, password);
        if (userResult.getStatusCode() != StatusCodes.OK || userResult.getValue() == null) {
            return new Result<String>(null, result.getStatusCode(), result.getStatusMessage());
        }

        final long issueDate = System.currentTimeMillis();
        final int oneHour = 60 * 60 * 1000;
        final long expirationDate = issueDate + oneHour;

        try {
            String jws = Jwts.builder()
                    .setSubject(userResult.getValue().getId())
                    .claim("login", login)
                    .setIssuedAt(Date.from(Instant.ofEpochMilli(issueDate)))
                    .setExpiration(Date.from(Instant.ofEpochMilli(expirationDate)))
                    .signWith(key)
                    .compact();
            tokenHashes.add(jws.hashCode());
            result = new Result<>(jws, StatusCodes.OK);
        } catch (Exception e) {
            result = new Result<>(null, StatusCodes.INVALID_RESULT, e.getMessage());
        }
        return result;
    }

    public void addToken(String token) {
        tokenHashes.add(token.hashCode());
    }

    public Boolean exists(String token) {
        return tokenHashes.contains(token.hashCode());
    }
}
