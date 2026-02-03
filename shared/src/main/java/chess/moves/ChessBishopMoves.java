package chess.moves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.*;


public class ChessBishopMoves {
    private final ChessBoard board;
    private final ChessPosition position;

    public ChessBishopMoves(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> generateBishopMoves() {
        List<ChessMove> validMoves = new ArrayList<>();
        validMoves.addAll(moveDir(1, 1));
        validMoves.addAll(moveDir(1, -1));
        validMoves.addAll(moveDir(-1, 1));
        validMoves.addAll(moveDir(-1, -1));
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
        while (col < 9 && col > 0 && row < 9 && row > 0) {
            ChessPosition newPos = new ChessPosition(row, col);
            if (board.getPiece(newPos) == null) {
                moves.add(new ChessMove(position, newPos, null));
                col = col + colDir;
                row = row + rowDir;
            } else {
                if (board.getPiece(newPos).getTeamColor() != color) {
                    moves.add(new ChessMove(position, newPos, null));
                    break;
                }
                break;
            }
        }
        return moves;
    }
}

