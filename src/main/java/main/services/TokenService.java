package main.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import main.models.Result;
import main.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Service
public class TokenService {
    @Autowired
    private UserService userService;

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
                    .setPayload("{\"login\":"+ login  + "}") // TODO: use Gson
                    .setIssuedAt(Date.from(Instant.ofEpochMilli(issueDate)))
                    .setExpiration(Date.from(Instant.ofEpochMilli(expirationDate)))
                    .signWith(key)
                    .compact();
            result = new Result<>(jws, StatusCodes.OK);
        } catch (Exception e) {
            result = new Result<>(null, StatusCodes.INVALID_RESULT, e.getMessage());
        }

        return result;
    }
}
