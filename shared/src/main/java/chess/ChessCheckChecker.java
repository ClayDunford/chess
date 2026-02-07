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
        return bishopqueen() || rookqueen() || knight() || pawn() || king();
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
            if (bq.getPieceType() == ChessPiece.PieceType.QUEEN || bq.getPieceType() == ChessPiece.PieceType.BISHOP) {
                return true;
            }
        }

        // Up Right
        bq = moveDir(1, 1);
        if (bq != null){
            if (bq.getPieceType() == ChessPiece.PieceType.QUEEN || bq.getPieceType() == ChessPiece.PieceType.BISHOP) {
                return true;
            }
        }

        // Down Left
        bq = moveDir(-1, -1);
        if (bq != null){
            if (bq.getPieceType() == ChessPiece.PieceType.QUEEN || bq.getPieceType() == ChessPiece.PieceType.BISHOP) {
                return true;
            }
        }
        // Down Right
        bq = moveDir(-1, 1);
        if (bq != null){
            if (bq.getPieceType() == ChessPiece.PieceType.QUEEN || bq.getPieceType() == ChessPiece.PieceType.BISHOP) {
                return true;
            }
        }
        return false;
    }

    private boolean rookqueen() {
        // Checking if there is a piece there
        // Up
        ChessPiece rq = moveDir(1, 0);
        if (rq != null) {
            if  (rq.getPieceType() == ChessPiece.PieceType.QUEEN || rq.getPieceType() == ChessPiece.PieceType.ROOK) {
                return true;
            }
        }

        // Down
        rq = moveDir(-1, 0);
        if (rq != null) {
            if  (rq.getPieceType() == ChessPiece.PieceType.QUEEN || rq.getPieceType() == ChessPiece.PieceType.ROOK) {
                return true;
            }
        }

        // Left
        rq = moveDir(0, -1);
        if (rq != null) {
            if  (rq.getPieceType() == ChessPiece.PieceType.QUEEN || rq.getPieceType() == ChessPiece.PieceType.ROOK) {
                return true;
            }
        }

        // Right
        rq = moveDir(0, 1);
        if (rq != null) {
            if  (rq.getPieceType() == ChessPiece.PieceType.QUEEN || rq.getPieceType() == ChessPiece.PieceType.ROOK) {
                return true;
            }
        }
        return false;
    }

    private boolean knight() {
        // Checking if there is a piece there
        // Up + Left
        ChessPiece k = pawnknightMoveDir(2, -1);
        if (k != null) {
            if (k.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                return true;
            }
        }
        // Up + Right
        k = pawnknightMoveDir(2, 1);
        if (k != null) {
            if (k.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                return true;
            }
        }
        // Down + Left
        k = pawnknightMoveDir(-2, -1);
        if (k != null) {
            if (k.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                return true;
            }
        }
        // Down + Right
        k = pawnknightMoveDir(-2, 1);
        if (k != null) {
            if (k.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                return true;
            }
        }

        // Right + Down
        k = pawnknightMoveDir(-1, 2);
        if (k != null) {
            if (k.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                return true;
            }
        }

        // Right + Up
        k = pawnknightMoveDir(1, 2);
        if (k != null) {
            if (k.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                return true;
            }
        }

        // Left + Down
        k = pawnknightMoveDir(-1, -2);
        if (k != null) {
            if (k.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                return true;
            }
        }

        // Left + Up
        k = pawnknightMoveDir(1, -2);
        if (k != null) {
            if (k.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                return true;
            }
        }

        return false;

    }

    private boolean pawn() {
        ChessPiece p;
        if (color == ChessGame.TeamColor.WHITE) {
            // Up Left
            p = pawnknightMoveDir(1, -1);
            if (p != null) {
                if (p.getPieceType() == ChessPiece.PieceType.PAWN) {
                    return true;
                }
            }
            // Up Right
            p = pawnknightMoveDir(1, 1);
            if (p != null) {
                if (p.getPieceType() == ChessPiece.PieceType.PAWN) {
                    return true;
                }
            }
        } else {
            // Down Left
            p = pawnknightMoveDir(-1, -1);
            if (p != null) {
                if (p.getPieceType() == ChessPiece.PieceType.PAWN) {
                    return true;
                }
            }
            // Down Right
            p = pawnknightMoveDir(-1, 1);
            if (p != null) {
                if (p.getPieceType() == ChessPiece.PieceType.PAWN) {
                    return true;
                }
            }
        }
        return false;

    }

    private boolean king() {
        ChessPiece K;
        // Up Left
        K = pawnknightMoveDir(1, -1);
        if (K != null) {
            if (K.getPieceType() == ChessPiece.PieceType.KING) {
                return true;
            }
        }
        // Up
        K = pawnknightMoveDir(1, 0);
        if (K != null) {
            if (K.getPieceType() == ChessPiece.PieceType.KING) {
                return true;
            }
        }
        // Up + Right
        K = pawnknightMoveDir(1, 1);
        if (K != null) {
            if (K.getPieceType() == ChessPiece.PieceType.KING) {
                return true;
            }
        }
        // Right
        K = pawnknightMoveDir(0, 1);
        if (K != null) {
            if (K.getPieceType() == ChessPiece.PieceType.KING) {
                return true;
            }
        }
        // Down + Right
        K = pawnknightMoveDir(-1, 1);
        if (K != null) {
            if (K.getPieceType() == ChessPiece.PieceType.KING) {
                return true;
            }
        }
        // Down
        K = pawnknightMoveDir(-1, 0);
        if (K != null) {
            if (K.getPieceType() == ChessPiece.PieceType.KING) {
                return true;
            }
        }
        // Down + Left
        K = pawnknightMoveDir(-1, -1);
        if (K != null) {
            if (K.getPieceType() == ChessPiece.PieceType.KING) {
                return true;
            }
        }
        // Left
        K = pawnknightMoveDir(0, -1);
        if (K != null) {
            if (K.getPieceType() == ChessPiece.PieceType.KING) {
                return true;
            }
        }
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

    private ChessPiece pawnknightMoveDir(int rowDir, int colDir) {
        int startRow = kingPos.getRow();
        int startCol = kingPos.getColumn();

        int row = startRow + rowDir;
        int col = startCol + colDir;
        if (!boardEdge(row, col)) {
            return null;
        }

        ChessPosition newPos = new ChessPosition(row, col);
        if (board.getPiece(newPos) != null &&  board.getPiece(newPos).getTeamColor() != color) {
            return board.getPiece(newPos);
        }

        return null;
    }

    public ChessPosition getKingPos() {
        return kingPos;
    }
}
