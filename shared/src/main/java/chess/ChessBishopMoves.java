package chess;

import java.util.Collection;

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

        // Moving up to the right


        return null;
    }
}
