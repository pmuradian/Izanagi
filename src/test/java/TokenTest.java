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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    static {
        userToBeFetched = new UserSpec("rick1", "password1", "rick1@citadel.mul");
    }

    @BeforeAll
    public void setup() {
        Result<UserEntity> result = storage.store(userToBeFetched);
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
    }
}
