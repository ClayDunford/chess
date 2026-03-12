package dataaccess;

import com.google.gson.Gson;
import dataaccess.exceptions.DataAccessException;
import io.javalin.http.GoneResponse;
import model.AuthData;
import model.UserData;

import java.sql.*;

import static java.sql.Types.NULL;

public class SQLAuthDAO  extends SQLDAO implements AuthDAO{
    public SQLAuthDAO() {
        try {
            String[] createAuthStatements = {
                    """
            CREATE TABLE IF NOT EXISTS auth (
                `authToken` varchar(256) NOT NULL,
                `authData` TEXT NOT NULL,
                PRIMARY KEY (`authToken`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
            };
            configureDatabase(createAuthStatements);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void createAuth(AuthData authData) throws DataAccessException{
        if (authData == null) {
            throw new DataAccessException("Invalid authToken Object");
        }
        String statement = "INSERT INTO auth (authToken, authData) VALUES (?,?)";
        executeUpdate(statement, authData.authToken(), authData);

    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        String rawAuthData = rs.getString("authData");
        return new Gson().fromJson(rawAuthData, AuthData.class);

    }

    public AuthData getAuth(String authToken) throws DataAccessException{
        try (Connection conn = DatabaseManager.getConnection()) {
            if (authToken == null) {
                throw new DataAccessException("authToken is null");
            }

            var statement = "SELECT authToken, authData FROM auth WHERE authToken=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void deleteAuth(String authToken) throws DataAccessException{
        if (getAuth(authToken) == null) {
            throw new DataAccessException("authToken does not exist");
        }
        String statement = "DELETE FROM auth WHERE authToken = ?";
        executeUpdate(statement, authToken);
    }

    public int clearAuth() throws DataAccessException{
        String statement = "TRUNCATE auth";
        executeUpdate(statement);
        String authCountStatement = "SELECT COUNT(*) as rowCount FROM auth";
        return getRowNum(authCountStatement);
    }


    private String executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    switch (param) {
                        case String p -> ps.setString(i + 1, p);
                        case AuthData p -> ps.setString(i + 1, p.toString());
                        case null -> ps.setNull(i + 1, NULL);
                        default -> throw new IllegalStateException("Unexpected value: " + param);
                    }
                }
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();

                if (rs.next()) {
                    return rs.getString(1);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
