
import main.entities.UserEntity;
import main.models.Result;
import main.models.User;
import main.persistence.MysqlStorage;
import main.persistence.MysqlUserStorage;
import main.services.UserService;
import main.services.StatusCodes;
import main.specs.UserSpec;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.swing.text.html.parser.Entity;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {UserService.class, MysqlUserStorage.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MysqlUserStorageTest {
    @Autowired
    private MysqlUserStorage storage;

    private static String userLogin = "rick1";
    private static String userPassword = "password1";
    private static String userEmail = "rick1@citadel.mul";
    private static String userID;
    private static String userIDToBeDeleted;
    private static String wrongUserID = "wrong$$$$user***id;";

    private static UserSpec userToBeDeleted;
    private static UserSpec userToBeFetched;

    static {
        userToBeFetched = new UserSpec("rick1", "password1", "rick1@citadel.mul");
        userToBeDeleted = new UserSpec("rick1_delete", "password1_delete", "rick1_delete@citadel.mul");
    }

    @BeforeAll
    public void setup() {
        Result<UserEntity> result = storage.store(userToBeFetched);
        UserEntity userEntity = result.getValue();
        if (result.getStatusCode() != StatusCodes.OK || userEntity == null) {
            throw new IllegalStateException(result.getStatusMessage());
        }
        userID = userEntity.getId();

        result = storage.store(userToBeDeleted);
        userEntity = result.getValue();
        if (result.getStatusCode() != StatusCodes.OK || userEntity == null) {
            throw new IllegalStateException(result.getStatusMessage());
        }
        userIDToBeDeleted = userEntity.getId();
    }

    @AfterAll
    public void teardown() {
        Result<Boolean> result = storage.delete(userID);
        if (result.getStatusCode() != StatusCodes.OK) {
            throw new IllegalStateException(result.getStatusMessage());
        }
    }

    @Test
    public void shouldReturnStatusCodeOKAndUser_whenRequestedUserExists() {
        Result<UserEntity> result = storage.get(userID);
        assertEquals(StatusCodes.OK, result.getStatusCode());
        assertNotNull(result.getValue());
    }

    @Test
    public void shouldReturnStatusCodeEntityNotFound_whenRequestedUserDoesNotExist () {
        Result<UserEntity> result = storage.get(wrongUserID);
        assertEquals(StatusCodes.ENTITY_NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void shouldReturnStatusCodeOKAndTrue_whenUserToBeDeletedExists() {
        Result<Boolean> result = storage.delete(userID);
        assertEquals(StatusCodes.OK, result.getStatusCode());
        assertTrue(result.getValue());
    }

    @Test
    public void shouldReturnStatusCodeEntityNotFoundAndFalse_whenUserToBeDeletedDoesNotExist() {
        Result<Boolean> result = storage.delete(wrongUserID);
        assertEquals(StatusCodes.ENTITY_NOT_FOUND, result.getStatusCode());
        assertFalse(result.getValue());
    }
}
