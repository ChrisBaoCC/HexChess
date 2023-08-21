package com.lucienbao.board;

public class Hex {
    public static final int WHITE = 0;
    public static final int BLACK = 1;

    public static final int KING = 0;
    public static final int QUEEN = 1;
    public static final int ROOK = 2;
    public static final int BISHOP = 3;
    public static final int KNIGHT = 4;
    public static final int PAWN = 5;

    public static final int EMPTY = -1;

    private int rank, file;
    private int color, id;

    public Hex(int file, int rank, int color, int id) {
        this.file = file;
        this.rank = rank;
        this.color = color;
        this.id = id;
    }

    /**
     * Return whether the piece is selectable given the
     * current player to move.
     * @param whiteToMove If White is to move.
     * @return Whether the player whose turn it is controls
     * this piece.
     */
    public boolean correctColor(boolean whiteToMove) {
        return (whiteToMove && color == WHITE)
                || (!whiteToMove && color == BLACK);
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getFile() {
        return file;
    }

    public void setFile(int file) {
        this.file = file;
    }

    public int getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    public void setPiece(int color, int id) {
        this.color = color;
        this.id = id;
    }

    public void setEmpty() {
        setColor(EMPTY);
        setId(EMPTY);
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setId(int id) {
        this.id = id;
    }
}
