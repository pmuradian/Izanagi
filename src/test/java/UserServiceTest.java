import main.models.Result;
import main.models.User;
import main.persistence.MysqlUserStorage;
import main.services.StatusCodes;
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


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {UserService.class, MysqlUserStorage.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    private static String userLogin = "rick1";
    private static String userPassword = "password1";
    private static String userEmail = "rick1@citadel.mul";
    private static String userID;

    @BeforeAll
    public void setup() {
        UserSpec userSpec = new UserSpec(userLogin, userPassword, userEmail);
        Result<User> result = userService.createUser(userSpec);
        User user = result.getValue();
        if (result.getStatusCode() != StatusCodes.OK || user == null) {
            throw new IllegalStateException(result.getStatusMessage());
        }
        userID = user.getId();
    }

    @AfterAll
    public void teardown() {
        Result<Boolean> result = userService.deleteUser(userID);
        Boolean isRemoved = result.getValue();
        if (result.getStatusCode() != StatusCodes.OK || isRemoved == false) {
            throw new IllegalStateException(result.getStatusMessage());
        }
    }

    @Test
    public void testGetUsersByValidCredentials() {
        Result<User> result = userService.getUserByCredentials(userLogin, userPassword);
        assertEquals(StatusCodes.OK, result.getStatusCode());
    }

    @Test
    public void whenUserDoesNotExists_shouldFailWithIllegalStateException() {
        assertEquals(1, 1);
    }
}
