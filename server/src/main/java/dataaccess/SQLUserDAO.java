package dataaccess;

import com.google.gson.Gson;
import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.DataAccessException;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

import static java.sql.Types.NULL;

public class SQLUserDAO  implements UserDAO{

    public SQLUserDAO() {
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public UserData getUser(UserData userData) throws DataAccessException{

        try (Connection conn = DatabaseManager.getConnection()) {
            if (userData == null) {
                throw new DataAccessException("Userdata is null");
            }
            String username = userData.username();
            var statement = "SELECT username, userdata FROM user WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean checkPassword(UserData userData) throws DataAccessException{
        UserData databaseUser = getUser(userData);
        String clearPassword = userData.password();
        String databasePassword = databaseUser.password();
        return BCrypt.checkpw(clearPassword, databasePassword);
    }

    private String hashPassword(UserData userData) {
        String clearPassword = userData.password();
        return BCrypt.hashpw(clearPassword, BCrypt.gensalt());
    }

    public void createUser(UserData userData) throws DataAccessException {
        if (userData == null) {
            throw new DataAccessException("Invalid userdata object");
        }
        String statement = "INSERT INTO user (username, userdata) VALUES (?,?)";
        UserData hashedUser = new UserData(userData.username(), hashPassword(userData), userData.email());
        executeUpdate(statement, hashedUser.username(), hashedUser);
    }

    public int clearUser() throws DataAccessException {
        String statement = "TRUNCATE user";
        executeUpdate(statement);
        String rowCountStatement = "SELECT COUNT(*) as rowCount FROM user";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(rowCountStatement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("rowCount");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        String rawUserData = rs.getString("userdata");
        return new Gson().fromJson(rawUserData, UserData.class);
    }

    private String executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    switch (param) {
                        case String p -> ps.setString(i + 1, p);
                        case UserData p -> ps.setString(i + 1, p.toString());
                        case null -> ps.setNull(i + 1, NULL);
                        default -> {
                        }
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

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS user (
                `username` varchar(256) NOT NULL,
                `userdata` TEXT NOT NULL,
                PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException{
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

