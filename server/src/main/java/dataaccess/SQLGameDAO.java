package dataaccess;

import com.google.gson.Gson;
import dataaccess.exceptions.DataAccessException;
import model.AuthData;
import model.GameData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Types.NULL;

public class SQLGameDAO extends SQLDAO implements GameDAO{
    public SQLGameDAO() {
        try {
            String[] createGameStatements = {
                    """
            CREATE TABLE IF NOT EXISTS game (
                `gameID` INT NOT NULL,
                `gameData` TEXT NOT NULL,
                PRIMARY KEY (`gameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
            };

            configureDatabase(createGameStatements);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void createGame(GameData gameData) throws DataAccessException{
        if (gameData == null) {
            throw new DataAccessException("gameData is null");
        }
        String statement = "INSERT INTO game (gameID, gameData) VALUES (?,?)";
        executeUpdate(statement, gameData.gameID(), gameData);
    }

    private GameData readGame(ResultSet rs) throws SQLException{
        String rawGameData = rs.getString("gameData");
        return new Gson().fromJson(rawGameData, GameData.class);
    }

    public GameData getGame(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            if (gameID <= 0) {
                throw new DataAccessException("Invalid Game ID");
            }

            var statement = "SELECT gameID, gameData FROM game WHERE gameID=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public int getSize() throws DataAccessException{
        String gameCountStatement = "SELECT COUNT(*) as rowCount FROM game";
        return getRowNum(gameCountStatement);
    }

    public List<GameData> listGames() throws DataAccessException {
        List<GameData> gameList = new ArrayList<>();
        String gameListStatement = "SELECT gameData FROM game";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(gameListStatement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        GameData gameData = readGame(rs);
                        gameList.add(gameData);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return gameList;
    }

    public void deleteGame(int gameID) throws DataAccessException {
        if (gameID <= 0) {
            throw new DataAccessException("Invalid GameID");
        }
        String statement = "DELETE FROM game WHERE gameID = ?";
        executeUpdate(statement, gameID);
    }

    public int clearGame() throws DataAccessException {
        String statement = "TRUNCATE game";
        executeUpdate(statement);
        return getSize();
    }


    private PreparedStatement paramChecker(PreparedStatement ps, Object... params) throws SQLException{
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            if (param instanceof Integer p) {ps.setInt(i + 1, p);}
            else if (param instanceof GameData p) {ps.setString(i+1, p.toString());}
            else if (param == null) {ps.setNull(i + 1, NULL);}
        }
        return ps;
    }
    private String executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                PreparedStatement finalStatement = paramChecker(ps, params);
                finalStatement.executeUpdate();
                ResultSet rs = finalStatement.getGeneratedKeys();
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
