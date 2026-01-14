package chess;

import java.util.*;


public class ChessBishopMoves {
    private final ChessBoard board;
    private final ChessPosition position;

    public ChessBishopMoves(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> generateBishopMoves() {
        ChessPiece piece = board.getPiece(position);
        ChessGame.TeamColor color = piece.getTeamColor();
        List<ChessMove> validMoves = new ArrayList<ChessMove>();
        validMoves.addAll(upright());
        validMoves.addAll(upleft());
        return validMoves;
    }

    private ArrayList<ChessMove> upright() {
        // Moving up to the right
        int startRow = position.getRow();
        int startCol = position.getColumn();
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        ArrayList<ChessMove> uprightMoves = new ArrayList<ChessMove>();
        int col = startCol + 1;
        int row = startRow + 1;
        while (col < 9 && row < 9) {
            ChessPosition newPos = new ChessPosition(row, col);
            if (board.getPiece(newPos) == null) {
                uprightMoves.add(new ChessMove(position, newPos, null));
                ++col;
                ++row;
            } else {
                if (board.getPiece(newPos).getTeamColor() != color) {
                    uprightMoves.add(new ChessMove(position, newPos, null));
                    break;
                }
                break;
            }
        }
        return uprightMoves;
    }

    private ArrayList<ChessMove> upleft() {
        // Moving up to the right
        int startRow = position.getRow();
        int startCol = position.getColumn();
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        ArrayList<ChessMove> upleftMoves = new ArrayList<ChessMove>();
        int col = startCol - 1;
        int row = startRow + 1;
        while (col > 0 && row < 9) {
            ChessPosition newPos = new ChessPosition(row, col);
            if (board.getPiece(newPos) == null) {
                upleftMoves.add(new ChessMove(position, newPos, null));
                --col;
                ++row;
            } else {
                if (board.getPiece(newPos).getTeamColor() != color) {
                    upleftMoves.add(new ChessMove(position, newPos, null));
                    break;
                }
                break;
            }
        }
        return upleftMoves;
    }
}
