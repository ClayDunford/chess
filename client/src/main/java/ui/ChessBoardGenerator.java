package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessBoardGenerator {
    private ChessBoard chessBoard;
    private ChessGame.TeamColor curTeamColor;
    private PrintStream out;




    public  ChessBoardGenerator(ChessGame chessGame, ChessGame.TeamColor teamColor) {
        chessBoard = chessGame.getBoard();
        curTeamColor = teamColor;
        out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        drawBoard();
    }

    public void drawBoard() {

        out.print(ERASE_SCREEN);
        drawHeaders();
        drawPieces();
        drawHeaders();
    }
    
    private void drawPieces() {
        boolean alternator = true;
        int row;
        int rowIter;

        if (curTeamColor == ChessGame.TeamColor.WHITE) {
            row = 8;
            rowIter = -1;
        } else {
            row = 1;
            rowIter = 1;
        }
        while (row < 9 && row > 0) {
            drawBorder(row);
            for (int col = 1; col <9; col++) {
                if (alternator) {
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                } else {
                    out.print(SET_BG_COLOR_RED);
                }
                drawPiece(row, col);

                alternator = !alternator;
                out.print(RESET_BG_COLOR);
            }
            drawBorder(row);
            out.println();
            alternator = !alternator;
            row = row + rowIter;
        }
    }
    private void drawPiece(int row, int col) {
        out.print(" ");
        ChessPiece curPiece = chessBoard.getPiece(new ChessPosition(row, col));
        if (curPiece != null) {
            ChessGame.TeamColor pieceColor = curPiece.getTeamColor();
            if (pieceColor == ChessGame.TeamColor.WHITE) {
                out.print(SET_TEXT_COLOR_WHITE);
            } else {
                out.print(SET_TEXT_COLOR_BLACK);
            }
            out.print(pieceConverter(curPiece));
        } else {
            out.print(EMPTY);
        }
        out.print(" ");
    }

    private String pieceConverter(ChessPiece piece) {
        ChessPiece.PieceType pieceType = piece.getPieceType();

        return switch (pieceType) {
            case PAWN -> BLACK_PAWN;
            case ROOK -> BLACK_ROOK;
            case KNIGHT -> BLACK_KNIGHT;
            case BISHOP -> BLACK_BISHOP;
            case QUEEN -> BLACK_QUEEN;
            case KING -> BLACK_KING;
        };

    }
    
    private void drawBorder(int row) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(" ");
        printHeaderText(String.valueOf(row));
        out.print(" ");
        out.print(RESET_BG_COLOR);
    }

    private void drawHeaders() {
        out.print(SET_BG_COLOR_DARK_GREY);
        String[] headers = {" ", " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", " "};
        for (int boardCol = 0; boardCol < headers.length; ++boardCol) {
            drawHeader(headers[boardCol]);
        }
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private void drawHeader(String headerText) {
        out.print(" ");
        printHeaderText(headerText);
        out.print(" ");
    }


    private void printHeaderText(String headerText) {
        out.print(SET_TEXT_COLOR_RED);
        out.print(headerText);
        out.print(SET_TEXT_COLOR_BLACK);
    }
    
    


}
