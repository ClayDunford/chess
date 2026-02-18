package chess.moves;

import chess.ChessBoard;

import chess.ChessMove;
import chess.ChessPosition;

import java.util.*;

public class ChessKnightMoves extends ChessMoveGenerator{

    public ChessKnightMoves(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> generateKnightMoves() {
        List<ChessMove> validMoves = new ArrayList<>();
        validMoves.addAll(knightKingMoveDir(2,1));
        validMoves.addAll(knightKingMoveDir(2, -1));
        validMoves.addAll(knightKingMoveDir(-2, 1));
        validMoves.addAll(knightKingMoveDir(-2, -1));
        validMoves.addAll(knightKingMoveDir(1, 2));
        validMoves.addAll(knightKingMoveDir(-1, 2));
        validMoves.addAll(knightKingMoveDir(1, -2));
        validMoves.addAll(knightKingMoveDir(-1, -2));
        return validMoves;
    }


}
