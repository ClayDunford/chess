package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    ChessBoard board;
    TeamColor curColor;
    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        curColor = TeamColor.WHITE;

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return curColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        curColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece curPiece = board.getPiece(startPosition);
        if (curPiece == null) {
            return null;
        } else {
            TeamColor color = curPiece.getTeamColor();
            Collection<ChessMove> moves = curPiece.pieceMoves(board, startPosition);
            Collection<ChessMove> cleanMoves = new ArrayList<>();
            for (ChessMove move : moves) {
                ChessBoard tempBoard = new ChessBoard();
                tempBoard.squares = board.squareDeepCopy();
                ChessPosition startPos = move.getStartPosition();
                ChessPosition endPos = move.getEndPosition();

                tempBoard.addPiece(startPos, null);
                if (move.getPromotionPiece() != null) {
                    curPiece = new ChessPiece(color, move.getPromotionPiece());
                }
                tempBoard.addPiece(endPos, curPiece);

                ChessCheckChecker moveInCheck = new ChessCheckChecker(tempBoard, color);
                if (!moveInCheck.check()) {
                    cleanMoves.add(move);
                }
            }
            return cleanMoves;
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPos = move.getStartPosition();
        ChessPosition endPos = move.getEndPosition();
        ChessPiece curPiece = board.getPiece(startPos);
        if (curPiece == null){
            throw new InvalidMoveException("No Piece");
        }

        if (curPiece.getTeamColor() != curColor) {
            throw new InvalidMoveException("Wrong Team");
        }

        Collection<ChessMove> valMoves = validMoves(startPos);
        if (!valMoves.contains(move)) {
            throw new InvalidMoveException("Invalid Move");
        }


        // At this point, we should know the move is valid

        // Removing the piece at that location
        board.addPiece(startPos, null);

        // Adding the new piece
        // Checking for promotion
        if (move.getPromotionPiece() != null) {
            curPiece = new ChessPiece(curColor, move.getPromotionPiece());
        }
        board.addPiece(endPos, curPiece);

        // Updating the team color
        if (curColor == TeamColor.BLACK) {
            curColor = TeamColor.WHITE;
        } else {
            curColor = TeamColor.BLACK;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return new ChessCheckChecker(board, teamColor).check();
    }

    private boolean pieceChecker(TeamColor color) {
        /*Finds every piece and checks if they have any valid moves
        True if they do have valid moves False if they do not
         */
        for(int col = 1; col < 9; col++) {
            for (int row = 1; row < 9; row++) {
                ChessPosition curPosition = new ChessPosition(row, col);
                ChessPiece curPiece = board.getPiece(curPosition);
                if (curPiece != null && curPiece.getTeamColor() == color) {
                    if (!validMoves(curPosition).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // Write move checking for every piece on the board that works for checkmate and stalemate
        return isInCheck(teamColor) && pieceChecker(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && pieceChecker(teamColor);
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param newBoard the new board to use
     */
    public void setBoard(ChessBoard newBoard) {
        board = newBoard;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && curColor == chessGame.curColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, curColor);
    }
}
