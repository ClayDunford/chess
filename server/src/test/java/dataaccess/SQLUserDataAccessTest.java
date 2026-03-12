package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class SQLUserDataAccessTest {
    private static SQLUserDAO userDAO;

    @BeforeAll
    public static void startDAO() {userDAO = new SQLUserDAO();}

    @BeforeEach
    public void setUp() throws DataAccessException {userDAO.clearUser();}


    @Test
    @DisplayName("Create User Test")
    @Order(1)
    public void createUserTest() throws DataAccessException {
        UserData fakeCreateUser = new UserData("username", "password", "email");
        userDAO.createUser(fakeCreateUser);
        UserData returnCreateUser = userDAO.getUser(fakeCreateUser);
        assertEquals(fakeCreateUser.username(), returnCreateUser.username(), "Users do not match");
    }

    @Test
    @DisplayName("Null User Test")
    @Order(2)
    public void nullUserTest() {
        assertThrows(DataAccessException.class, () -> {
            userDAO.createUser(null);
        });
    }

    @Test
    @DisplayName("Get User Test")
    @Order(3)
    public void getUserTest() throws DataAccessException{
        UserData fakeGetUser = new UserData("username", "password", "email");
        userDAO.createUser(fakeGetUser);
        UserData returnGetUser = userDAO.getUser(fakeGetUser);
        assertEquals(fakeGetUser.username(), returnGetUser.username(), "Unable to Get added user");
    }

    @Test
    @DisplayName("Get null User Test")
    @Order(4)

    public void getNullUserTest() {
        assertThrows(DataAccessException.class, () -> {
            userDAO.getUser(null);
        });
    }

    @Test
    @DisplayName("Check Password Test")
    @Order(5)

    public void checkPasswordTest() throws DataAccessException{
        UserData fakeUser = new UserData("username", "password", "email");
        userDAO.createUser(fakeUser);
        assertTrue(userDAO.checkPassword(fakeUser), "passwords don't match");
    }

    @Test
    @DisplayName("Wrong Password Test")
    @Order(6)

    public void wrongPasswordTest() throws DataAccessException{
        UserData fakeUser = new UserData("username", "password", "email");
        userDAO.createUser(fakeUser);
        UserData wrongPasswordUser = new UserData("username", "wrongPassword", "email");
        assertFalse(userDAO.checkPassword(wrongPasswordUser), "passwords match");
    }

    @Test
    @DisplayName("Clear Test")
    @Order(7)

    public void clearTest() throws DataAccessException{
        UserData fakeUser = new UserData("username", "password", "email");
        userDAO.createUser(fakeUser);
        int rowCount = userDAO.clearUser();
        assertEquals(0, rowCount, "Too many rows left in database");
    }






}
