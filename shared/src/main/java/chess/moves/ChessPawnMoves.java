package chess.moves;

import chess.*;

import java.util.*;
public class ChessPawnMoves extends ChessMoveGenerator{

    public ChessPawnMoves(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> generatePawnMoves() {

        return pawnMoveDir();
    }

}
