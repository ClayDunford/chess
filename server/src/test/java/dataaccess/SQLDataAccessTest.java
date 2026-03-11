package dataaccess;

import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class SQLDataAccessTest {
    private static SQLUserDAO userDAO;

    @BeforeAll
    public static void startDAO() {userDAO = new SQLUserDAO();}

    @BeforeEach
    public void setUP() throws DataAccessException {userDAO.clearUser();}

    @Test
    @DisplayName("Create User Test")
    @Order(1)
    public void createUserTest() throws DataAccessException {
        UserData fakeUser = new UserData("username", "password", "email");
        userDAO.createUser(fakeUser);
        UserData returnUser = userDAO.getUser(fakeUser);
        assertEquals(fakeUser, returnUser, "Users do not match");
    }

    @Test
    @DisplayName("Null User Test")
    @Order(2)
    public void nullUserTest() {
        DataAccessException error = assertThrows(DataAccessException.class, () -> {
            userDAO.createUser(null);
        });
    }

    @Test
    @DisplayName("Get User Test")
    @Order(3)
    public void getUserTest() throws DataAccessException{
        UserData fakeUser = new UserData("username", "password", "email");
        userDAO.createUser(fakeUser);
        UserData returnUser = userDAO.getUser(fakeUser);
        assertEquals(fakeUser, returnUser, "Unable to Get added user");
    }

    @Test
    @DisplayName("Get NUll User Test")
    @Order(4)

    public void getNullUserTest() {
        DataAccessException error = assertThrows(DataAccessException.class, () -> {
            userDAO.getUser(null);
        });
    }




}
