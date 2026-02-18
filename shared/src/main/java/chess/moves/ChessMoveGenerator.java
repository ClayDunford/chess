package chess.moves;

import chess.*;

import java.util.ArrayList;

public class ChessMoveGenerator {
    private final ChessBoard board;
    private final ChessPosition position;

    ChessMoveGenerator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }


    public ArrayList<ChessMove> moveDir(int rowDir, int colDir) {
        // colDir determines the direction the columns move, +1 = up, -1 = down
        // rowDir determines the direction the rows move, +1 = right, -1 = left
        int startRow = position.getRow();
        int startCol = position.getColumn();
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        ArrayList<ChessMove> moves = new ArrayList<>();
        int col = startCol + colDir;
        int row = startRow + rowDir;
        while (col < 9 && col > 0 && row < 9 && row > 0) {
            ChessPosition newPos = new ChessPosition(row, col);
            if (board.getPiece(newPos) == null) {
                moves.add(new ChessMove(position, newPos, null));
                col = col + colDir;
                row = row + rowDir;
            } else {
                if (board.getPiece(newPos).getTeamColor() != color) {
                    moves.add(new ChessMove(position, newPos, null));
                    break;
                }
                break;
            }
        }
        return moves;
    }

    public ArrayList<ChessMove> knightKingMoveDir(int rowDir, int colDir) {
        // colDir determines the direction the columns move, +1 = up, -1 = down
        // rowDir determines the direction the rows move, +1 = right, -1 = left
        int startRow = position.getRow();
        int startCol = position.getColumn();
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        ArrayList<ChessMove> moves = new ArrayList<>();
        int col = startCol + colDir;
        int row = startRow + rowDir;

        if (col >= 9 || col <= 0 || row >= 9 || row <= 0) {
            return moves;
        }
        ChessPosition newPos = new ChessPosition(row, col);
        if (board.getPiece(newPos) == null) {
            moves.add(new ChessMove(position, newPos, null));
        } else {
            if (board.getPiece(newPos).getTeamColor() != color) {
                moves.add(new ChessMove(position, newPos, null));
            }
        } return moves;
    }

    public ArrayList<ChessMove> pawnMoveDir() {
        // colDir determines the direction the columns move, +1 = up, -1 = down
        // rowDir determines the direction the rows move, +1 = right, -1 = left
        int startRow = position.getRow();
        int startCol = position.getColumn();
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row;
        int moveDirection;
        boolean white;
        if (color == ChessGame.TeamColor.WHITE) {
            row = startRow + 1;
            moveDirection = 1;
            white = true;
        } else {
            row = startRow - 1;
            moveDirection = -1;
            white = false;
        }
        ChessPosition newPos = new ChessPosition(row, startCol);
        ChessPosition left = new ChessPosition(row, startCol - 1 );
        ChessPosition right = new ChessPosition(row, startCol + 1);
        boolean doubleMove = (startRow == 2 && white) || (startRow == 7 && !white);
        boolean promotion = (startRow == 7 && white) || (startRow == 2 && !white);
        if (board.getPiece(newPos) == null) {
            if (doubleMove) {
                moves.add(new ChessMove(position, newPos, null));
                ChessPosition secondPos = new ChessPosition(row + moveDirection, startCol);
                if (board.getPiece(secondPos) == null) {
                    moves.add(new ChessMove(position, secondPos, null));
                }
            } else if (promotion) {
                // Handling Promotion
                for (ChessPiece.PieceType piece : ChessPiece.PieceType.values()) {
                    if (piece != ChessPiece.PieceType.PAWN && piece != ChessPiece.PieceType.KING) {
                        moves.add(new ChessMove(position, newPos, piece));
                    }
                }
            } else {
                moves.add(new ChessMove(position, newPos, null));
            }
        }
        if (startCol > 1 && board.getPiece(left) != null && board.getPiece(left).getTeamColor() != color) {
            if (promotion) {
                // Handling Promotion
                for (ChessPiece.PieceType piece : ChessPiece.PieceType.values()) {
                    if (piece != ChessPiece.PieceType.PAWN && piece != ChessPiece.PieceType.KING) {
                        moves.add(new ChessMove(position, left, piece));
                    }
                }
            } else {
                moves.add(new ChessMove(position, left, null));
            }
        }
        if (startCol < 8 && board.getPiece(right) != null && board.getPiece(right).getTeamColor() != color) {
            if (promotion) {
                // Handling Promotion
                for (ChessPiece.PieceType piece : ChessPiece.PieceType.values()) {
                    if (piece != ChessPiece.PieceType.PAWN && piece != ChessPiece.PieceType.KING) {
                        moves.add(new ChessMove(position, right, piece));
                    }
                }
            } else {
                moves.add(new ChessMove(position, right, null));
            }
        }


        return moves;
    }
}
