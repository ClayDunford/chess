package chess.moves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.*;

public class ChessKingMoves {


    private final ChessBoard board;
    private final ChessPosition position;

    public ChessKingMoves(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> generateKingMoves() {
        List<ChessMove> validMoves = new ArrayList<>();
        // Bishop Moves
        validMoves.addAll(moveDir(1, 1));
        validMoves.addAll(moveDir(1, -1));
        validMoves.addAll(moveDir(-1, 1));
        validMoves.addAll(moveDir(-1, -1));

        // Rook Moves
        validMoves.addAll(moveDir(1, 0));
        validMoves.addAll(moveDir(-1, 0));
        validMoves.addAll(moveDir(0, 1));
        validMoves.addAll(moveDir(0, -1));

        return validMoves;
    }

    private ArrayList<ChessMove> moveDir(int rowDir, int colDir) {
        // colDir determines the direction the columns move, +1 = up, -1 = down
        // rowDir determines the direction the rows move, +1 = right, -1 = left
        int startRow = position.getRow();
        int startCol = position.getColumn();
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        ArrayList<ChessMove> moves = new ArrayList<>();
        int col = startCol + colDir;
        int row = startRow + rowDir;

        if (col >= 9 || col <= 0 || row >= 9 || row <= 0) {
            return moves;
        }
        ChessPosition newPos = new ChessPosition(row, col);
        if (board.getPiece(newPos) == null) {
            moves.add(new ChessMove(position, newPos, null));
        } else {
            if (board.getPiece(newPos).getTeamColor() != color) {
                moves.add(new ChessMove(position, newPos, null));
            }
        } return moves;
    }
}
