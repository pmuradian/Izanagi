import main.entities.UserEntity;
import main.models.Result;
import main.persistence.MysqlUserStorage;
import main.services.StatusCodes;
import main.services.TokenService;
import main.services.UserService;
import main.specs.UserSpec;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TokenService.class, MysqlUserStorage.class, UserService.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TokenTest {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private MysqlUserStorage storage;

    private static String userID;
    private static UserSpec userToBeFetched;
    private static String token = "token that exists";

    static {
        userToBeFetched = new UserSpec("rick2", "password2", "rick2@citadel.mul");
    }

    @BeforeAll
    public void setup() {
        Result<UserEntity> result = storage.store(userToBeFetched);
        tokenService.addToken(token);
        UserEntity userEntity = result.getValue();
        if (result.getStatusCode() != StatusCodes.OK || userEntity == null) {
            throw new IllegalStateException(result.getStatusMessage());
        }
        userID = userEntity.getId();
    }

    @AfterAll
    public void teardown() {
        Result<Boolean> result = storage.delete(userID);
        if (result.getStatusCode() != StatusCodes.OK) {
            throw new IllegalStateException(result.getStatusMessage());
        }
    }

    @Test
    public void whenTokenRequestedWithValidCredentials_shouldReturnNewToken() {
        Result<String> result = tokenService.getToken(userToBeFetched.getLogin(), userToBeFetched.getPassword());
        assertEquals(StatusCodes.OK, result.getStatusCode());
        assertNotNull(result.getValue());
        token = result.getValue();
    }

    @Test
    public void whenTokenRequestedWithInvalidCredentials_shouldReturnErrorAndValueNull() {
        Result<String> result = tokenService.getToken(userToBeFetched.getLogin(), userToBeFetched.getPassword() + "wrong_password");
        assertNotEquals(StatusCodes.OK, result.getStatusCode());
        assertNull(result.getValue());
    }

    @Test
    public void whenTokenExists_shouldReturnTrue() {
        Boolean tokenExists = tokenService.exists(token);
        assertEquals(true, tokenExists);
    }

    @Test void whenTokenDoesNotExist_shouldReturnFalse() {
        Boolean tokenExists = tokenService.exists("token that does not exist");
        assertEquals(false, tokenExists);
    }

}
