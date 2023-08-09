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

    private int color, id;

    public Hex(int color, int id) {
        this.color = color;
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setId(int id) {
        this.id = id;
    }
}
