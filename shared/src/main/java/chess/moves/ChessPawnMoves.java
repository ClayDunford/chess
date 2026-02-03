package chess.moves;

import chess.*;

import java.util.*;
public class ChessPawnMoves {

    private final ChessBoard board;
    private final ChessPosition position;

    public ChessPawnMoves(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> generatePawnMoves() {
        List<ChessMove> validMoves = new ArrayList<>();
        if (board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE){
            validMoves.addAll(moveDirWhite());
        } else {
            validMoves.addAll(moveDirBlack());
        }
        return validMoves;
    }

    private ArrayList<ChessMove> moveDirWhite() {
        // colDir determines the direction the columns move, +1 = up, -1 = down
        // rowDir determines the direction the rows move, +1 = right, -1 = left
        int startRow = position.getRow();
        int startCol = position.getColumn();
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = startRow + 1;
        ChessPosition newPos = new ChessPosition(row, startCol);
        ChessPosition downLeft = new ChessPosition(row, startCol - 1 );
        ChessPosition downRight = new ChessPosition(row, startCol + 1);

        if (board.getPiece(newPos) == null) {
            if (startRow == 2) {
                moves.add(new ChessMove(position, newPos, null));
                ChessPosition secondPos = new ChessPosition(row + 1, startCol);
                if (board.getPiece(secondPos) == null) {
                    moves.add(new ChessMove(position, secondPos, null));
                }
            } else if (startRow == 7) {
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
        if (startCol > 1 && board.getPiece(downLeft) != null && board.getPiece(downLeft).getTeamColor() != color) {
            if (startRow == 7) {
                // Handling Promotion
                for (ChessPiece.PieceType piece : ChessPiece.PieceType.values()) {
                    if (piece != ChessPiece.PieceType.PAWN && piece != ChessPiece.PieceType.KING) {
                        moves.add(new ChessMove(position, downLeft, piece));
                    }
                }
            } else {
                moves.add(new ChessMove(position, downLeft, null));
            }
        }
        if (startCol < 8 && board.getPiece(downRight) != null && board.getPiece(downRight).getTeamColor() != color) {
            if (startRow == 7) {
                // Handling Promotion
                for (ChessPiece.PieceType piece : ChessPiece.PieceType.values()) {
                    if (piece != ChessPiece.PieceType.PAWN && piece != ChessPiece.PieceType.KING) {
                        moves.add(new ChessMove(position, downRight, piece));
                    }
                }
            } else {
                moves.add(new ChessMove(position, downRight, null));
            }
        }


        return moves;
    }

    private ArrayList<ChessMove> moveDirBlack() {
        // colDir determines the direction the columns move, +1 = up, -1 = down
        // rowDir determines the direction the rows move, +1 = right, -1 = left
        int startRow = position.getRow();
        int startCol = position.getColumn();
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = startRow - 1;
        ChessPosition newPos = new ChessPosition(row, startCol);
        ChessPosition downLeft = new ChessPosition(row, startCol - 1 );
        ChessPosition downRight = new ChessPosition(row, startCol + 1);

        if (board.getPiece(newPos) == null) {
            if (startRow == 7) {
                moves.add(new ChessMove(position, newPos, null));
                ChessPosition secondPos = new ChessPosition(row - 1, startCol);
                if (board.getPiece(secondPos) == null) {
                    moves.add(new ChessMove(position, secondPos, null));
                }
            } else if (startRow == 2) {
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
        if (startCol > 1 && board.getPiece(downLeft) != null && board.getPiece(downLeft).getTeamColor() != color) {
            if (startRow == 2) {
                // Handling Promotion
                for (ChessPiece.PieceType piece : ChessPiece.PieceType.values()) {
                    if (piece != ChessPiece.PieceType.PAWN && piece != ChessPiece.PieceType.KING) {
                        moves.add(new ChessMove(position, downLeft, piece));
                    }
                }
            } else {
                moves.add(new ChessMove(position, downLeft, null));
            }
        }
        if (startCol < 8 && board.getPiece(downRight) != null && board.getPiece(downRight).getTeamColor() != color) {
            if (startRow == 2) {
                // Handling Promotion
                for (ChessPiece.PieceType piece : ChessPiece.PieceType.values()) {
                    if (piece != ChessPiece.PieceType.PAWN && piece != ChessPiece.PieceType.KING) {
                        moves.add(new ChessMove(position, downRight, piece));
                    }
                }
            } else {
                moves.add(new ChessMove(position, downRight, null));
            }
        }


        return moves;

    }
}
