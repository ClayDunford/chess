package chess.moves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.*;

public class ChessQueenMoves extends ChessMoveGenerator{

    public ChessQueenMoves(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> generateQueenMoves() {
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

}
