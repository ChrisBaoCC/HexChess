package com.lucienbao.utils;

import com.lucienbao.board.Board;
import com.lucienbao.board.Hex;

import java.util.Arrays;

public class MoveRules {
    public static final int ILLEGAL = 0;
    public static final int MOVE = 1;
    public static final int CAPTURE = 2;
    public static final int MOVE_PROMOTE = 3;
    public static final int CAPTURE_PROMOTE = 4;
    public static final int CAPTURE_EN_PASSANT = 5;

    public static int canMove(Hex from, Hex to, Board board) {
        switch(from.getId()) {
            case Hex.PAWN:
                return pawnCanMove(from, to, board);
            case Hex.KNIGHT:
                return knightCanMove(from, to, board);
            case Hex.BISHOP:
                return bishopCanMove(from, to, board);
            case Hex.ROOK:
                return rookCanMove(from, to, board);
            case Hex.QUEEN:
                return queenCanMove(from, to, board);
            case Hex.KING:
                return kingCanMove(from, to, board);
            default:
                throw new IllegalArgumentException("Unknown piece type");
        }
    }

    public static int[][] generateMoves(Hex piece, Board board) {
        switch(piece.getId()) {
            case Hex.PAWN:
                return generatePawnMoves(piece, board);
            case Hex.KNIGHT:
                return generateKnightMoves(piece, board);
            case Hex.BISHOP:
                return generateBishopMoves(piece, board);
            case Hex.ROOK:
                return generateRookMoves(piece, board);
            case Hex.QUEEN:
                return generateQueenMoves(piece, board);
            case Hex.KING:
                return generateKingMoves(piece, board);
            default:
                throw new IllegalArgumentException("Unknown piece type");
        }
    }

    /**
     * Creates an 2D int array, size 11x11, filled with <code>ILLEGAL</code>.
     * @return An empty array.
     */
    public static int[][] generateEmptyArray() {
        int[][] result = new int[11][11];
        for(int i = 0; i < 11; i++)
            Arrays.fill(result[i], ILLEGAL);
        return result;
    }

    public static boolean isPawnStartingSquare(Hex position) {
        int file = position.getFile();
        int rank = position.getRank();
        if(position.getColor() == Hex.BLACK) {
            return rank == 6;
        }
        // position.getColor() == Hex.WHITE
        return file < 6 && rank == file - 1         // left half + center file
                || file >= 6 && rank == 9 - file;   // right half
    }

    /**
     * Generate the possible moves for a pawn on a certain board.
     *
     * @param position Pawn's hex.
     * @param board    Pawn's board.
     * @return A 2D int array describing the legality of each move.
     */
    public static int[][] generatePawnMoves(Hex position, Board board) {
        // TODO: implement en passant (requires previous-move logic)
        int[][] result = generateEmptyArray();
        int file = position.getFile();
        int rank = position.getRank();
        int color = position.getColor();
        if(color == Hex.WHITE) {
            // Single-step move
            if(Board.hexExists(file, rank + 1)
                    && board.getHex(file, rank + 1).getColor() == Hex.EMPTY) {
                result[file][rank + 1] = MOVE;
                // Double-step move
                if(isPawnStartingSquare(position)
                        && board.getHex(file, rank + 2).getId() == Hex.EMPTY)
                    result[file][rank + 2] = MOVE;
            }

            // Note that the center file is on the left half for leftwards
            // capture but on the right half for rightwards capture.

            // Leftwards capture
            // Left half of board
            if(file <= 5 && Board.hexExists(file - 1, rank)
                    && board.getHex(file - 1, rank).getColor() == Hex.BLACK)
                result[file - 1][rank] = CAPTURE;
            // Right half of board
            if(file > 5 && Board.hexExists(file - 1, rank + 1)
                    && board.getHex(file - 1, rank + 1).getColor() == Hex.BLACK)
                result[file - 1][rank + 1] = CAPTURE;

            // Rightwards capture
            // Left half of board
            if(file < 5 && Board.hexExists(file + 1, rank + 1)
               && board.getHex(file + 1, rank + 1).getColor() == Hex.BLACK)
                result[file + 1][rank + 1] = CAPTURE;
            if(file >= 5 && Board.hexExists(file + 1, rank)
                    && board.getHex(file + 1, rank).getColor() == Hex.BLACK)
                result[file + 1][rank] = CAPTURE;
        }

        else { // color == Hex.BLACK
            // Single-step move
            if(Board.hexExists(file, rank - 1)
                    && board.getHex(file, rank - 1).getColor() == Hex.EMPTY) {
                result[file][rank - 1] = MOVE;
                // Double-step move
                if(isPawnStartingSquare(position)
                        && board.getHex(file, rank - 2).getId() == Hex.EMPTY)
                    result[file][rank - 2] = MOVE;
            }

            // Note that the center file is on the left half for leftwards
            // capture but on the right half for rightwards capture.

            // Leftwards capture
            // Left half of board
            if(file <= 5 && Board.hexExists(file - 1, rank - 1)
                    && board.getHex(file - 1, rank - 1).getColor() == Hex.WHITE)
                result[file - 1][rank - 1] = CAPTURE;
            // Right half of board
            if(file > 5 && Board.hexExists(file - 1, rank)
                    && board.getHex(file - 1, rank).getColor() == Hex.WHITE)
                result[file - 1][rank] = CAPTURE;

            // Rightwards capture
            // Left half of board
            if(file < 5 && Board.hexExists(file + 1, rank)
                    && board.getHex(file + 1, rank).getColor() == Hex.WHITE)
                result[file + 1][rank] = CAPTURE;
            if(file >= 5 && Board.hexExists(file + 1, rank - 1)
                    && board.getHex(file + 1, rank - 1).getColor() == Hex.WHITE)
                result[file + 1][rank - 1] = CAPTURE;
        }
//        System.out.println(Arrays.deepToString(result));
        return result;
    }

    /**
     * Generate the possible moves for a knight on a certain board.
     *
     * @param position Knight's hex.
     * @param board    Knight's board.
     * @return A 2D int array describing the legality of each move.
     */
    public static int[][] generateKnightMoves(Hex position, Board board) {
        return generateEmptyArray();
    }

    /**
     * Generate the possible moves for a bishop on a certain board.
     *
     * @param position Bishop's hex.
     * @param board    Bishop's board.
     * @return A 2D int array describing the legality of each move.
     */
    public static int[][] generateBishopMoves(Hex position, Board board) {
        return generateEmptyArray();
    }

    /**
     * Generate the possible moves for a rook on a certain board.
     *
     * @param position Rook's hex.
     * @param board    Rook's board.
     * @return A 2D int array describing the legality of each move.
     */
    public static int[][] generateRookMoves(Hex position, Board board) {
        return generateEmptyArray();
    }

    /**
     * Generate the possible moves for a queen on a certain board.
     *
     * @param position Queen's hex.
     * @param board    Queen's board.
     * @return A 2D int array describing the legality of each move.
     */
    public static int[][] generateQueenMoves(Hex position, Board board) {
        return generateEmptyArray();
    }

    /**
     * Generate the possible moves for a king on a certain board.
     *
     * @param position King's hex.
     * @param board    King's board.
     * @return A 2D int array describing the legality of each move.
     */
    public static int[][] generateKingMoves(Hex position, Board board) {
        return generateEmptyArray();
    }

    /**
     * Provides the move code corresponding to the given starting
     * and destination squares for a pawn.
     * @param from Starting square.
     * @param to Ending square.
     * @param board Board the pawn is on.
     * @return Corresponding move code.
     */
    public static int pawnCanMove(Hex from, Hex to, Board board) {
        return generatePawnMoves(from, board)[to.getFile()][to.getRank()];
    }

    public static int knightCanMove(Hex from, Hex to, Board board) {
        return MOVE;
    }

    public static int bishopCanMove(Hex from, Hex to, Board board) {
        return MOVE;
    }

    public static int rookCanMove(Hex from, Hex to, Board board) {
        return MOVE;
    }

    public static int queenCanMove(Hex from, Hex to, Board board) {
        return MOVE;
    }

    public static int kingCanMove(Hex from, Hex to, Board board) {
        return MOVE;
    }
}
