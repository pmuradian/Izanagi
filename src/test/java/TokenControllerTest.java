import main.controllers.TokenController;
import main.models.Result;
import main.models.User;
import main.persistence.MysqlUserStorage;
import main.services.StatusCodes;
import main.services.TokenService;
import main.services.UserService;
import main.specs.TokenSpec;
import main.specs.UserSpec;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TokenController.class, MysqlUserStorage.class, UserService.class, TokenService.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TokenControllerTest {
    @Autowired
    TokenController controller;
    @Autowired
    UserService userService;

    static TokenSpec tokenSpec = new TokenSpec("rickc473", "mamikon");
    static UserSpec userSpec = new UserSpec("rickc473", "mamikon", "rickc473@citadel.mul");
    static String userID;
    static String validToken;

    @BeforeAll
    public void setup() {

        Result<User> result = userService.createUser(userSpec);
        if (result.getValue() != null) {
            userID = result.getValue().getId();
        }

        ResponseEntity<Result<String>> responseEntity = controller.getToken(tokenSpec);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            validToken = responseEntity.getBody().getValue();
        }
    }

    @AfterAll
    public void teardown() {
        userService.deleteUser(userID);
    }

    @Test
    public void whenTokenIsRequestedWithValidTokenSpec_shouldReturnNewValidToken() {
        ResponseEntity<Result<String>> responseEntity = controller.getToken(tokenSpec);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        validToken = responseEntity.getBody().getValue();
    }

    @Test
    public void whenTokenIsRequestedWithInvalidTokenSpec_shouldReturnNullInBodyAndErrorInHeader() {
        TokenSpec invalidSpec = new TokenSpec("asdfa-09845287045`12`1", "q");
        ResponseEntity<Result<String>> responseEntity = controller.getToken(invalidSpec);
        assertNotEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody().getValue());
    }

    @Test
    public void whenUserLogsOutWithValidToken_shouldReturnStatusCodeOK() {
        ResponseEntity responseEntity = controller.delete(validToken);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void whenUserLogsOutWithInvalidToken_shouldReturnError() {
        ResponseEntity responseEntity = controller.delete("invalid token");
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}
