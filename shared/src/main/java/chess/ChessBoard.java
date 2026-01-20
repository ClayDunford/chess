package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;

    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */

    private void addPiece(ChessGame.TeamColor color) {
        int fileOne;
        int fileTwo;
        if (color == ChessGame.TeamColor.WHITE) {
            fileOne = 1;
            fileTwo = 2;
        } else {
            fileOne = 8;
            fileTwo = 7;
        }
        for (ChessPiece.PieceType piece : ChessPiece.PieceType.values()) {
            if (piece == ChessPiece.PieceType.KING) {
                ChessPiece king = new ChessPiece(color, piece);
                ChessPosition kingPos =new ChessPosition(fileOne, 5);
                addPiece(kingPos, king);
            } if (piece == ChessPiece.PieceType.QUEEN) {
                ChessPiece queen = new ChessPiece(color, piece);
                ChessPosition queenPos = new ChessPosition(fileOne, 4);
                addPiece(queenPos, queen);
            } if (piece == ChessPiece.PieceType.BISHOP) {
                ChessPiece bishop = new ChessPiece(color, piece);
                ChessPosition bishopPosLeft = new ChessPosition(fileOne 3);
                ChessPosition bishopPosRight = new ChessPosition(fileTwo, 6);
                addPiece(bishopPosLeft, bishop);
                addPiece(bishopPosRight, bishop);
            } if (piece == ChessPiece.PieceType.KNIGHT) {
                ChessPiece knight = new ChessPiece(color, piece);
                ChessPosition knightPosLeft = new ChessPosition(fileOne, 2);
                ChessPosition knightPosRight = new ChessPosition(fileOne, 7);
                addPiece(knightPosLeft, knight);
                addPiece(knightPosRight, knight);
            } if (piece == ChessPiece.PieceType.ROOK) {
                ChessPiece rook = new ChessPiece(color, piece);
                ChessPosition rookPosLeft = new ChessPosition(fileOne, 1);
                ChessPosition rookPosRight = new ChessPosition(fileOne, 8);
                addPiece(rookPosLeft, rook);
                addPiece(rookPosRight, rook);
            } else {
                for (int col = 1; col < 9; col++) {
                    ChessPiece pawn = new ChessPiece(color, piece);
                    ChessPosition pawnPos = new ChessPosition(fileTwo, col);
                    addPiece(pawnPos, pawn);
                }
            }
    }
    public void resetBoard() {
        // White reset
        ChessGame.TeamColor color = ChessGame.TeamColor.WHITE;

        }
    }
}
