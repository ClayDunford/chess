package chess.moves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.*;


public class ChessBishopMoves extends ChessMoveGenerator{

    public ChessBishopMoves(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> generateBishopMoves() {
        List<ChessMove> validMoves = new ArrayList<>();
        validMoves.addAll(moveDir(1, 1));
        validMoves.addAll(moveDir(1, -1));
        validMoves.addAll(moveDir(-1, 1));
        validMoves.addAll(moveDir(-1, -1));
        return validMoves;
    }


}

