package chess;

public class ChessCheckChecker {

    private final ChessBoard board;
    private final ChessGame.TeamColor color;
    private final ChessPosition kingPos;

    public ChessCheckChecker(ChessBoard board, ChessGame.TeamColor color) {
        this.board = board;
        this.color = color;
        this.kingPos = kingFinder();
    }

    public boolean check() {

        return false;
    }

    private ChessPosition kingFinder() {
        for(int col = 1; col < 9; col++) {
            for (int row = 1; row < 9; row++) {
                ChessPosition curPosition = new ChessPosition(row, col);
                ChessPiece curPiece = board.getPiece(curPosition);
                if (curPiece != null) {
                    if (curPiece.getPieceType() == ChessPiece.PieceType.KING && curPiece.getTeamColor() == color){
                        return curPosition;
                    }
                }
            }
        }
        return null;
    }

    private boolean bishopqueen() {
        // Checking if there is a piece there
        // Up Left
        ChessPiece bq = moveDir(1, -1);
        if (bq != null){
            return bq.getPieceType() == ChessPiece.PieceType.QUEEN || bq.getPieceType() == ChessPiece.PieceType.BISHOP;
        }

        // Up Right
        bq = moveDir(1, 1);
        if (bq != null) {
            return bq.getPieceType() == ChessPiece.PieceType.QUEEN || bq.getPieceType() == ChessPiece.PieceType.BISHOP;
        }

        // Down Left
        bq = moveDir(-1, -1);
        if (bq != null) {
            return bq.getPieceType() == ChessPiece.PieceType.QUEEN || bq.getPieceType() == ChessPiece.PieceType.BISHOP;
        }
        // Down Right
        bq = moveDir(-1, 1);
        if (bq != null) {
            return bq.getPieceType() == ChessPiece.PieceType.QUEEN || bq.getPieceType() == ChessPiece.PieceType.BISHOP;
        }
        return false;
    }

    private boolean rookqueen() {
        // Checking if there is a piece there
        // Up
        ChessPiece rq = moveDir(1, 0);
        if (rq != null) {
            return rq.getPieceType() == ChessPiece.PieceType.QUEEN || rq.getPieceType() == ChessPiece.PieceType.ROOK;
        }

        // Down
        rq = moveDir(-1, 0);
        if (rq != null) {
            return rq.getPieceType() == ChessPiece.PieceType.QUEEN || rq.getPieceType() == ChessPiece.PieceType.ROOK;
        }

        // Left
        rq = moveDir(0, -1);
        if (rq != null) {
            return rq.getPieceType() == ChessPiece.PieceType.QUEEN || rq.getPieceType() == ChessPiece.PieceType.ROOK;
        }

        // Right
        rq = moveDir(0, 1);
        if (rq != null) {
            return rq.getPieceType() == ChessPiece.PieceType.QUEEN || rq.getPieceType() == ChessPiece.PieceType.ROOK;
        }
        return false;
    }

    private boolean knight() {
        // Checking if there is a piece there
        // Up + Left
        ChessPiece k = moveDir(2, -1);
        if (k != null) {
            return k.getPieceType() == ChessPiece.PieceType.KNIGHT;
        }
        // Up + Right
        k = moveDir(2, 1);
        if (k != null) {
            return k.getPieceType() == ChessPiece.PieceType.KNIGHT;
        }
        // Down + Left
        k = moveDir(-2, -1);
        if (k != null) {
            return k.getPieceType() == ChessPiece.PieceType.KNIGHT;
        }
        // Down + Right
        k = moveDir(-2, 1);
        if (k != null) {
            return k.getPieceType() == ChessPiece.PieceType.KNIGHT;
        }

        // Right + Down
        k = moveDir(-1, 2);
        if (k != null) {
            return k.getPieceType() == ChessPiece.PieceType.KNIGHT;
        }

        // Right + Up
        k = moveDir(1, 2);
        if (k != null) {
            return k.getPieceType() == ChessPiece.PieceType.KNIGHT;
        }

        // Left + Down
        k = moveDir(-1, -2);
        if (k != null) {
            return k.getPieceType() == ChessPiece.PieceType.KNIGHT;
        }

        // Left + Up
        k = moveDir(1, -2);
        if (k != null) {
            return k.getPieceType() == ChessPiece.PieceType.KNIGHT;
        }

        return false;

    }

    private boolean pawn() {
        return false;
    }

    private boolean boardEdge(int row, int col) {
        return col < 9 && col > 0 && row < 9 && row > 0;
    }

    private ChessPiece moveDir(int rowDir, int colDir) {
        int startRow = kingPos.getRow();
        int startCol = kingPos.getColumn();

        int row = startRow + rowDir;
        int col = startCol + colDir;

        while (boardEdge(row, col)) {
            ChessPosition newPos = new ChessPosition(row, col);
            if (board.getPiece(newPos) == null) {
                row = row + rowDir;
                col = col + colDir;
            } else {
                if (board.getPiece(newPos).getTeamColor() != color) {
                    return board.getPiece(newPos);
                }
                return null;
            }
        }
        return null;
    }

    public ChessPosition getKingPos() {
        return kingPos;
    }
}
