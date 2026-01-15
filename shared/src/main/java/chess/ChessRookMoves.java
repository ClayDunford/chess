package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChessRookMoves {

    private final ChessBoard board;
    private final ChessPosition position;

    public ChessRookMoves(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> generateRookMoves() {
        ChessPiece piece = board.getPiece(position);
        ChessGame.TeamColor color = piece.getTeamColor();
        List<ChessMove> validMoves = new ArrayList<ChessMove>();
        validMoves.addAll(up());
        validMoves.addAll(down());

        return validMoves;
    }

    private ArrayList<ChessMove> up() {
        // Moving up to the right
        int startRow = position.getRow();
        int startCol = position.getColumn();
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        ArrayList<ChessMove> upMoves = new ArrayList<ChessMove>();
        int col = startCol;
        int row = startRow + 1;
        while (row < 9) {
            ChessPosition newPos = new ChessPosition(row, col);
            if (board.getPiece(newPos) == null) {
                upMoves.add(new ChessMove(position, newPos, null));
                ++row;
            } else {
                if (board.getPiece(newPos).getTeamColor() != color) {
                    upMoves.add(new ChessMove(position, newPos, null));
                    break;
                }
                break;
            }
        }
        return upMoves;
    }

    private ArrayList<ChessMove> down() {
        // Moving up to the right
        int startRow = position.getRow();
        int startCol = position.getColumn();
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        ArrayList<ChessMove> downMoves = new ArrayList<ChessMove>();
        int col = startCol;
        int row = startRow - 1;
        while (row > 0) {
            ChessPosition newPos = new ChessPosition(row, col);
            if (board.getPiece(newPos) == null) {
                downMoves.add(new ChessMove(position, newPos, null));
                --row;
            } else {
                if (board.getPiece(newPos).getTeamColor() != color) {
                    downMoves.add(new ChessMove(position, newPos, null));
                    break;
                }
                break;
            }
        }
        return downMoves;
    }

}
