package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class SQLAuthDataAccessTest {
    private static SQLAuthDAO authDAO;

    @BeforeAll
    public static void startDAO() {authDAO = new SQLAuthDAO();}

    @BeforeEach
    public void setUp() throws DataAccessException {authDAO.clearAuth();}

    @Test
    @DisplayName("Create Auth test")
    @Order(1)
    public void createAuthTest() throws DataAccessException {
        AuthData fakeAuth = new AuthData("username", "authToken");
        authDAO.createAuth(fakeAuth);
        AuthData returnAuth = authDAO.getAuth(fakeAuth.authToken());
        assertEquals(fakeAuth, returnAuth, "AuthData not the same");
    }

    @Test
    @DisplayName("Create Null Auth test")
    @Order(2)
    public void createNullAuthTest() {
        assertThrows(DataAccessException.class, () -> {
            authDAO.createAuth(null);
        });
    }

    @Test
    @DisplayName("Get Auth test")
    @Order(3)
    public void getAuthTest() throws DataAccessException{
        AuthData fakeAuth = new AuthData("username", "authToken");
        authDAO.createAuth(fakeAuth);
        AuthData returnAuth = authDAO.getAuth(fakeAuth.authToken());
        assertEquals(fakeAuth, returnAuth, "AuthData not the same");
    }

    @Test
    @DisplayName("Null Auth Test")
    @Order(4)
    public void getNullAuthTest() {
        assertThrows(DataAccessException.class, () -> {
                    authDAO.getAuth(null);
                });
    }

    @Test
    @DisplayName("Delete Auth Test")
    @Order(5)
    public void deleteAuthTest() throws DataAccessException {
        AuthData fakeAuth = new AuthData("username", "authToken");
        authDAO.createAuth(fakeAuth);
        authDAO.deleteAuth(fakeAuth.authToken());
        assertNull(authDAO.getAuth(fakeAuth.authToken()));
    }

    @Test
    @DisplayName("Null Delete Auth Test")
    @Order(6)
    public void nullDeleteAuthTest() {
        assertThrows(DataAccessException.class, () -> {
            authDAO.deleteAuth(null);
        });
    }






}
