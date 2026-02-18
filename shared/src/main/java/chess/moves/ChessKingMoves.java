package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.*;

public class ChessKingMoves extends ChessMoveGenerator{


    public ChessKingMoves(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> generateKingMoves() {
        List<ChessMove> validMoves = new ArrayList<>();
        // Bishop Moves
        validMoves.addAll(knightKingMoveDir(1, 1));
        validMoves.addAll(knightKingMoveDir(1, -1));
        validMoves.addAll(knightKingMoveDir(-1, 1));
        validMoves.addAll(knightKingMoveDir(-1, -1));

        // Rook Moves
        validMoves.addAll(knightKingMoveDir(1, 0));
        validMoves.addAll(knightKingMoveDir(-1, 0));
        validMoves.addAll(knightKingMoveDir(0, 1));
        validMoves.addAll(knightKingMoveDir(0, -1));

        return validMoves;
    }

}
