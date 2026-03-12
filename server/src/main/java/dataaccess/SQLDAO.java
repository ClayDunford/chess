package dataaccess;

import dataaccess.exceptions.DataAccessException;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLDAO {
    SQLDAO() {}

    void configureDatabase(String[] statements) throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : statements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
