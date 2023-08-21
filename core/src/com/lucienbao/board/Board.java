package com.lucienbao.board;

import com.badlogic.gdx.math.Polygon;
import com.lucienbao.hexchess.PlayScreen;

import static java.lang.Math.sqrt;

public class Board {
    private final Hex[][] grid;
    private final Polygon[][] collisionGrid;

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
        collisionGrid = new Polygon[11][11];
        setCollisionGrid();

        // Initialize the board to empty
        for(int file = 0; file < 11; file++)
            for(int rank = 0; rank < getNumRanks(file); rank++)
                grid[file][rank] = new Hex(file, rank, Hex.EMPTY, Hex.EMPTY);

        // White pieces
        setHex(1, 0, Hex.WHITE, Hex.PAWN);
        setHex(2, 0, Hex.WHITE, Hex.ROOK);
        setHex(3, 0, Hex.WHITE, Hex.KNIGHT);
        setHex(4, 0, Hex.WHITE, Hex.QUEEN);
        setHex(5, 0, Hex.WHITE, Hex.BISHOP);
        setHex(6, 0, Hex.WHITE, Hex.KING);
        setHex(7, 0, Hex.WHITE, Hex.KNIGHT);
        setHex(8, 0, Hex.WHITE, Hex.ROOK);
        setHex(9, 0, Hex.WHITE, Hex.PAWN);

        setHex(5, 1, Hex.WHITE, Hex.BISHOP);
        setHex(5, 2, Hex.WHITE, Hex.BISHOP);

        setHex(2, 1, Hex.WHITE, Hex.PAWN);
        setHex(3, 2, Hex.WHITE, Hex.PAWN);
        setHex(4, 3, Hex.WHITE, Hex.PAWN);
        setHex(5, 4, Hex.WHITE, Hex.PAWN);
        setHex(6, 3, Hex.WHITE, Hex.PAWN);
        setHex(7, 2, Hex.WHITE, Hex.PAWN);
        setHex(8, 1, Hex.WHITE, Hex.PAWN);

        // Black pieces
        setHex(1, 6, Hex.BLACK, Hex.PAWN);
        setHex(2, 7, Hex.BLACK, Hex.ROOK);
        setHex(3, 8, Hex.BLACK, Hex.KNIGHT);
        setHex(4, 9, Hex.BLACK, Hex.QUEEN);
        setHex(5, 10, Hex.BLACK, Hex.BISHOP);
        setHex(6, 9, Hex.BLACK, Hex.KING);
        setHex(7, 8, Hex.BLACK, Hex.KNIGHT);
        setHex(8, 7, Hex.BLACK, Hex.ROOK);
        setHex(9, 6, Hex.BLACK, Hex.PAWN);

        setHex(5, 9, Hex.BLACK, Hex.BISHOP);
        setHex(5, 8, Hex.BLACK, Hex.BISHOP);

        setHex(2, 6, Hex.BLACK, Hex.PAWN);
        setHex(3, 6, Hex.BLACK, Hex.PAWN);
        setHex(4, 6, Hex.BLACK, Hex.PAWN);
        setHex(5, 6, Hex.BLACK, Hex.PAWN);
        setHex(6, 6, Hex.BLACK, Hex.PAWN);
        setHex(7, 6, Hex.BLACK, Hex.PAWN);
        setHex(8, 6, Hex.BLACK, Hex.PAWN);
    }

    /**
     * Initialize the 2d array of collision polygons that describe
     * the boundaries of each hex.
     */
    private void setCollisionGrid() {
        for(int file = 0; file < 11; file++) {
            for(int rank = 0; rank < getNumRanks(file); rank++) {
                float x = getX(file, rank);
                float y = getY(file, rank);
                float sideLength = PlayScreen.HEX_WIDTH/2;
                float[] vertices = {
                        x - sideLength,		y,
                        x - sideLength/2,	y + sideLength * (float)sqrt(3)/2,
                        x + sideLength/2,	y + sideLength * (float)sqrt(3)/2,
                        x + sideLength,		y,
                        x + sideLength/2,	y - sideLength * (float)sqrt(3)/2,
                        x - sideLength/2,	y - sideLength * (float)sqrt(3)/2,
                };
                collisionGrid[file][rank] = new Polygon(vertices);
            }
        }
    }

    /**
     * Get the board's collision polygon for the hex at the given
     * rank and file.
     * @param rank Hex rank.
     * @param file Hex file.
     * @return The bounding Polygon of the hex.
     */
    public Polygon getCollisionPolygon(int file, int rank) {
        if(rank < 0 || rank > getNumRanks(file))
            throw new IllegalArgumentException("Rank " + rank
                    + " does not exist at file " + file);
        return collisionGrid[file][rank];
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
     * Returns whether there is a hex at the given rank and file.
     * @param rank A rank of the board.
     * @param file A file of the board.
     * @return Whether such a hex exists on the grid.
     */
    public static boolean hexExists(int file, int rank) {
        return 0 <= file && file < 11
                && 0 <= rank && rank < getNumRanks(file);
    }

    /**
     * Get this board's hex at the given rank and file.
     * @param rank Rank of the cell.
     * @param file File of the cell.
     * @return The cell at this location.
     */
    public Hex getHex(int file, int rank) {
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
    public void setHex(int file, int rank, int color, int id) {
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
    public static float getX(int file, int rank) {
        // I know you technically don't need rank,
        // but I'm keeping it as an argument
        // to be consistent with getY()
        return PlayScreen.BOARD_CENTER_X + (file - 5) * PlayScreen.HEX_NEST_WIDTH;
    }

    /**
     * Calculate the center y-coordinate of the hex
     * at the given rank and file.
     * @param rank Hex rank.
     * @param file Hex file.
     * @return Hex's center y-coordinate.
     */
    public static float getY(int file, int rank) {
        if(file < 6)
            return PlayScreen.BOARD_CENTER_Y + (rank - 5) * PlayScreen.HEX_HEIGHT +
                    (5 - file) * PlayScreen.HEX_HEIGHT / 2;
        return PlayScreen.BOARD_CENTER_Y + (rank - 5) * PlayScreen.HEX_HEIGHT +
                (file - 5) * PlayScreen.HEX_HEIGHT / 2;
    }
}
