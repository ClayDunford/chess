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
        ChessPiece bishopQueen = moveDir(1, -1);
        if (bishopQueen != null){
            if (bishopQueen.getPieceType() == ChessPiece.PieceType.QUEEN || bishopQueen.getPieceType() == ChessPiece.PieceType.BISHOP) {
                return true;
            }
        }

        // Up Right
        bishopQueen = moveDir(1, 1);
        if (bishopQueen != null){
            if (bishopQueen.getPieceType() == ChessPiece.PieceType.QUEEN || bishopQueen.getPieceType() == ChessPiece.PieceType.BISHOP) {
                return true;
            }
        }

        // Down Left
        bishopQueen = moveDir(-1, -1);
        if (bishopQueen != null){
            if (bishopQueen.getPieceType() == ChessPiece.PieceType.QUEEN || bishopQueen.getPieceType() == ChessPiece.PieceType.BISHOP) {
                return true;
            }
        }
        // Down Right
        bishopQueen = moveDir(-1, 1);
        if (bishopQueen != null){
            if (bishopQueen.getPieceType() == ChessPiece.PieceType.QUEEN || bishopQueen.getPieceType() == ChessPiece.PieceType.BISHOP) {
                return true;
            }
        }
        return false;
    }

    private boolean rookqueen() {
        // Checking if there is a piece there
        // Up
        ChessPiece rookQueen = moveDir(1, 0);
        if (rookQueen != null) {
            if  (rookQueen.getPieceType() == ChessPiece.PieceType.QUEEN || rookQueen.getPieceType() == ChessPiece.PieceType.ROOK) {
                return true;
            }
        }

        // Down
        rookQueen = moveDir(-1, 0);
        if (rookQueen != null) {
            if  (rookQueen.getPieceType() == ChessPiece.PieceType.QUEEN || rookQueen.getPieceType() == ChessPiece.PieceType.ROOK) {
                return true;
            }
        }

        // Left
        rookQueen = moveDir(0, -1);
        if (rookQueen != null) {
            if  (rookQueen.getPieceType() == ChessPiece.PieceType.QUEEN || rookQueen.getPieceType() == ChessPiece.PieceType.ROOK) {
                return true;
            }
        }

        // Right
        rookQueen = moveDir(0, 1);
        if (rookQueen != null) {
            if  (rookQueen.getPieceType() == ChessPiece.PieceType.QUEEN || rookQueen.getPieceType() == ChessPiece.PieceType.ROOK) {
                return true;
            }
        }
        return false;
    }

    private boolean knight() {
        // Checking if there is a piece there
        // Up + Left
        ChessPiece knight = pawnknightMoveDir(2, -1);
        if (knight != null) {
            if (knight.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                return true;
            }
        }
        // Up + Right
        knight = pawnknightMoveDir(2, 1);
        if (knight != null) {
            if (knight.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                return true;
            }
        }
        // Down + Left
        knight = pawnknightMoveDir(-2, -1);
        if (knight != null) {
            if (knight.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                return true;
            }
        }
        // Down + Right
        knight = pawnknightMoveDir(-2, 1);
        if (knight != null) {
            if (knight.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                return true;
            }
        }

        // Right + Down
        knight = pawnknightMoveDir(-1, 2);
        if (knight != null) {
            if (knight.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                return true;
            }
        }

        // Right + Up
        knight = pawnknightMoveDir(1, 2);
        if (knight != null) {
            if (knight.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                return true;
            }
        }

        // Left + Down
        knight = pawnknightMoveDir(-1, -2);
        if (knight != null) {
            if (knight.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                return true;
            }
        }

        // Left + Up
        knight = pawnknightMoveDir(1, -2);
        if (knight != null) {
            if (knight.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                return true;
            }
        }

        return false;

    }

    private boolean pawn() {
        ChessPiece pawn;
        if (color == ChessGame.TeamColor.WHITE) {
            // Up Left
            pawn = pawnknightMoveDir(1, -1);
            if (pawn != null) {
                if (pawn.getPieceType() == ChessPiece.PieceType.PAWN) {
                    return true;
                }
            }
            // Up Right
            pawn = pawnknightMoveDir(1, 1);
            if (pawn != null) {
                if (pawn.getPieceType() == ChessPiece.PieceType.PAWN) {
                    return true;
                }
            }
        } else {
            // Down Left
            pawn = pawnknightMoveDir(-1, -1);
            if (pawn != null) {
                if (pawn.getPieceType() == ChessPiece.PieceType.PAWN) {
                    return true;
                }
            }
            // Down Right
            pawn = pawnknightMoveDir(-1, 1);
            if (pawn != null) {
                if (pawn.getPieceType() == ChessPiece.PieceType.PAWN) {
                    return true;
                }
            }
        }
        return false;

    }

    private boolean king() {
        // Up Left
        ChessPiece king = pawnknightMoveDir(1, -1);
        if (king != null) {
            if (king.getPieceType() == ChessPiece.PieceType.KING) {
                return true;
            }
        }
        // Up
        king = pawnknightMoveDir(1, 0);
        if (king != null) {
            if (king.getPieceType() == ChessPiece.PieceType.KING) {
                return true;
            }
        }
        // Up + Right
        king = pawnknightMoveDir(1, 1);
        if (king != null) {
            if (king.getPieceType() == ChessPiece.PieceType.KING) {
                return true;
            }
        }
        // Right
        king = pawnknightMoveDir(0, 1);
        if (king != null) {
            if (king.getPieceType() == ChessPiece.PieceType.KING) {
                return true;
            }
        }
        // Down + Right
        king = pawnknightMoveDir(-1, 1);
        if (king != null) {
            if (king.getPieceType() == ChessPiece.PieceType.KING) {
                return true;
            }
        }
        // Down
        king = pawnknightMoveDir(-1, 0);
        if (king != null) {
            if (king.getPieceType() == ChessPiece.PieceType.KING) {
                return true;
            }
        }
        // Down + Left
        king = pawnknightMoveDir(-1, -1);
        if (king != null) {
            if (king.getPieceType() == ChessPiece.PieceType.KING) {
                return true;
            }
        }
        // Left
        king = pawnknightMoveDir(0, -1);
        if (king != null) {
            if (king.getPieceType() == ChessPiece.PieceType.KING) {
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
