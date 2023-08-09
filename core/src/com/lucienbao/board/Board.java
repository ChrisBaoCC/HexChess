package com.lucienbao.board;

import com.lucienbao.hexchess.PlayScreen;

public class Board {
    private final Hex[][] grid;

    public Board() {
        /*
        Files go from a to k (0 to 10)
        Real files: left to right
        Array files: top to bottom

        Ranks go from 0 to 10
        Real ranks: bottom to top
        Array ranks: left to right
        */
        grid = new Hex[11][11];

        // Initialize the board to empty
        for(int file = 0; file < 11; file++)
            for(int rank = 0; rank < getNumRanks(file); rank++)
                grid[file][rank] = new Hex(Hex.EMPTY, Hex.EMPTY);

        // Add the pieces
        setHex(0, 1, Hex.WHITE, Hex.PAWN);
        setHex(0, 2, Hex.WHITE, Hex.ROOK);
        setHex(0, 3, Hex.WHITE, Hex.KNIGHT);
        setHex(0, 4, Hex.WHITE, Hex.QUEEN);
        setHex(0, 5, Hex.WHITE, Hex.BISHOP);
        setHex(0, 6, Hex.WHITE, Hex.KING);
        setHex(0, 7, Hex.WHITE, Hex.KNIGHT);
        setHex(0, 8, Hex.WHITE, Hex.ROOK);
        setHex(0, 9, Hex.WHITE, Hex.PAWN);

        setHex(1, 5, Hex.WHITE, Hex.BISHOP);
        setHex(2, 5, Hex.WHITE, Hex.BISHOP);

        setHex(1, 2, Hex.WHITE, Hex.PAWN);
        setHex(2, 3, Hex.WHITE, Hex.PAWN);
        setHex(3, 4, Hex.WHITE, Hex.PAWN);
        setHex(4, 5, Hex.WHITE, Hex.PAWN);
        setHex(3, 6, Hex.WHITE, Hex.PAWN);
        setHex(2, 7, Hex.WHITE, Hex.PAWN);
        setHex(1, 8, Hex.WHITE, Hex.PAWN);
    }

    /**
     * Returns the number of ranks a given file has.
     * @param file A file of the board.
     * @return The number of ranks it has.
     */
    public static int getNumRanks(int file) {
        if(file < 6)
            return file + 6;
        return 16 - file;
    }

    /**
     * Get this board's hex at the given rank and file.
     * @param rank Rank of the cell.
     * @param file File of the cell.
     * @return The cell at this location.
     */
    public Hex getHex(int rank, int file) {
        if(rank < 0 || rank > getNumRanks(file))
            throw new IllegalArgumentException("Rank " + rank
                    + " does not exist at file " + file);
        return grid[file][rank];
    }

    /**
     * Set this board's hex at the given rank and file.
     * @param rank Rank of the cell.
     * @param file File of the cell.
     * @param color Color of piece, or <code>Hex.EMPTY</code> if none.
     * @param id ID number of piece, or <code>Hex.EMPTY</code> if none.
     * @see Hex
     */
    public void setHex(int rank, int file, int color, int id) {
        if(rank < 0 || rank > getNumRanks(file))
            throw new IllegalArgumentException("Rank " + rank
                    + " does not exist at file " + file);
        grid[file][rank].setColor(color);
        grid[file][rank].setId(id);
    }

    /**
     * Calculate the center x-coordinate of the hex
     * at the given rank and file.
     * @param rank Hex rank.
     * @param file Hex file.
     * @return Hex's center x-coordinate.
     */
    public static float getX(int rank, int file) {
        return PlayScreen.BOARD_CENTER_X + (file - 5) * PlayScreen.HEX_NEST_WIDTH;
    }

    /**
     * Calculate the center y-coordinate of the hex
     * at the given rank and file.
     * @param rank Hex rank.
     * @param file Hex file.
     * @return Hex's center y-coordinate.
     */
    public static float getY(int rank, int file) {
        if(file < 6)
            return PlayScreen.BOARD_CENTER_Y + (rank - 5) * PlayScreen.HEX_HEIGHT +
                    (5 - file) * PlayScreen.HEX_HEIGHT / 2;
        return PlayScreen.BOARD_CENTER_Y + (rank - 5) * PlayScreen.HEX_HEIGHT +
                (file - 5) * PlayScreen.HEX_HEIGHT / 2;
    }
}
