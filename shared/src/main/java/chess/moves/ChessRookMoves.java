package chess.moves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChessRookMoves extends ChessMoveGenerator{

    public ChessRookMoves(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> generateRookMoves() {
        List<ChessMove> validMoves = new ArrayList<>();
        validMoves.addAll(moveDir(1, 0));
        validMoves.addAll(moveDir(-1, 0));
        validMoves.addAll(moveDir(0, 1));
        validMoves.addAll(moveDir(0, -1));

        return validMoves;
    }
}
