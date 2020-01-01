import main.entities.UserEntity;
import main.models.JWTPayload;
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
    private static Result<String> token;

    static {
        userToBeFetched = new UserSpec("rick3", "password3", "rick3@citadel.mul");
    }

    @BeforeAll
    public void setup() {
        Result<UserEntity> result = storage.store(userToBeFetched);
        token = tokenService.getToken(userToBeFetched.getLogin(), userToBeFetched.getPassword());

        if (token.getStatusCode() != StatusCodes.OK) {
            throw new IllegalStateException(result.getStatusMessage());

        }
        tokenService.addToken(token.getValue());
        UserEntity userEntity = result.getValue();

        if (result.getStatusCode() != StatusCodes.OK || userEntity == null) {
            throw new IllegalStateException(result.getStatusMessage());
        }
        userID = userEntity.getId();
    }

    private JWTPayload createValidPayload() {
        final long issueDate = System.currentTimeMillis();
        final int oneHour = 60 * 60 * 1000;
        final long expirationDate = issueDate + oneHour;

        JWTPayload payload = JWTPayload.cleanInstance()
                .setIssuedAt(issueDate / 1000)
                .setExpirationDate(expirationDate / 1000)
                .setSubject(userID)
                .setLogin(userToBeFetched.getLogin());

        return payload;
    }

    private JWTPayload createInvalidPayload() {
        long offset = 20000;
        final long issueDate = System.currentTimeMillis() + offset;
        final int towHours = 60 * 60 * 1000 * 2;
        final long expirationDate = issueDate - towHours;

        JWTPayload payload = JWTPayload.cleanInstance()
                .setIssuedAt(issueDate / 1000)
                .setExpirationDate(expirationDate / 1000)
                .setSubject("userID")
                .setLogin("~~~;;;;///,,,,.-$%^!@#$%^&*()");

        return payload;
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

    @Test
    public void whenTokenRequestedWithInvalidCredentials_shouldReturnErrorAndValueNull() {
        Result<String> result = tokenService.getToken(userToBeFetched.getLogin(), userToBeFetched.getPassword() + "wrong_password");
        assertNotEquals(StatusCodes.OK, result.getStatusCode());
        assertNull(result.getValue());
    }

    @Test
    public void whenTokenExists_shouldReturnTrue() {
        Boolean tokenExists = tokenService.exists(token.getValue());
        assertEquals(true, tokenExists);
    }

    @Test
    public void whenTokenDoesNotExist_shouldReturnFalse() {
        Boolean tokenExists = tokenService.exists("token that does not exist");
        assertEquals(false, tokenExists);
    }

    @Test
    public void whenPayloadHasValidExpirationDate_shouldReturnTrue() {
        JWTPayload validPayload = createValidPayload();
        Boolean isPayloadValid = tokenService.payloadHasValidExpirationDate(validPayload);
        assertEquals(true, isPayloadValid);
    }

    @Test
    public void whenPayloadHasInvalidExpirationDate_shouldReturnFalse() {
        JWTPayload invalidPayload = createInvalidPayload();
        Boolean isPayloadValid = tokenService.payloadHasValidExpirationDate(invalidPayload);
        assertEquals(false, isPayloadValid);
    }

    @Test
    public void whenPayloadHasValidIssueDate_shouldReturnTrue() {
        JWTPayload validPayload = createValidPayload();
        Boolean isPayloadValid = tokenService.payloadHasValidIssueDate(validPayload);
        assertEquals(true, isPayloadValid);
    }

    @Test
    public void whenPayloadHasInvalidIssueDate_shouldReturnFalse() {
        JWTPayload invalidPayload = createInvalidPayload();
        Boolean isPayloadValid = tokenService.payloadHasValidIssueDate(invalidPayload);
        assertEquals(false, isPayloadValid);
    }

    @Test
    public void whenPayloadHasValidLogin_shouldReturnTrue() {
        JWTPayload validPayload = createValidPayload();
        Boolean isPayloadValid = tokenService.payloadHasValidLogin(validPayload);
        assertEquals(true, isPayloadValid);
    }

    @Test
    public void whenPayloadHasInvalidLogin_shouldReturnFalse() {
        JWTPayload invalidPayload = createInvalidPayload();
        Boolean isPayloadValid = tokenService.payloadHasValidLogin(invalidPayload);
        assertEquals(false, isPayloadValid);
    }

    @Test
    public void whenPayloadHasValidSubject_shouldReturnTrue() {
        JWTPayload validPayload = createValidPayload();
        Boolean isPayloadValid = tokenService.payloadHasValidSubject(validPayload);
        assertEquals(true, isPayloadValid);
    }

    @Test
    public void whenPayloadHasInvalidSubject_shouldReturnTrue() {
        JWTPayload invalidPayload = createInvalidPayload();
        Boolean isPayloadValid = tokenService.payloadHasValidSubject(invalidPayload);
        assertEquals(false, isPayloadValid);
    }

    @Test
    public void whenPayloadLoginIsNull_shouldReturnFalse() {
        JWTPayload payload = createInvalidPayload();
        payload.setLogin(null);
        Boolean isLoginNotNull = tokenService.payloadHasValidLogin(payload);
        assertEquals(false, isLoginNotNull);
    }

    @Test
    public void whenPayloadSubjectIsNull_shouldReturnFalse() {
        JWTPayload payload = createInvalidPayload();
        payload.setSubject(null);
        Boolean isSubjectNotNull = tokenService.payloadHasValidSubject(payload);
        assertEquals(false, isSubjectNotNull);
    }
}
