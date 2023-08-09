package com.lucienbao.hexchess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.lucienbao.board.Board;
import com.lucienbao.board.Hex;
import com.lucienbao.utils.AssetLoader;

import static java.lang.Math.sqrt;

public class PlayScreen implements Screen {
    private final HexChess game;
    private final SpriteBatch batch;
    private final ShapeRenderer shapes;

    private final TextureRegion[][] pieceImages;

    private final Board board;

    public static final float HEX_WIDTH = 96;
    public static final float HEX_NEST_WIDTH = HEX_WIDTH * 3f/4;
    public static final float HEX_HEIGHT = (float) (HEX_WIDTH * sqrt(3) / 2);
    public static final int BOARD_CENTER_X = 700;
    public static final int BOARD_CENTER_Y = 540;

    public PlayScreen(final HexChess game) {
        this.game = game;
        this.batch = game.batch;
        this.shapes = game.shapes;
        this.pieceImages = AssetLoader.pieces;

        board = new Board();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Clear screen with black
        Gdx.gl.glClearColor(HexChess.BACKGROUND_COLOR.r,
                HexChess.BACKGROUND_COLOR.g,
                HexChess.BACKGROUND_COLOR.b,
                HexChess.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw shapes
        drawBoard();

        // Draw pieces
//        game.drawCenteredText(0, "PlayScreen", 1920/2, 1080/2);
        drawPieces();
    }

    /**
     * Set the current <code>ShapeRenderer</code>'s color depending
     * on the rank and file about to be drawn.
     * @param rank Rank of the cell on the board.
     * @param file File of the cell on the board.
     */
    private void setColor(int rank, int file) {
        if(file < 6) {
            int color = (rank + file) % 3;
            if(color == 0)
                shapes.setColor(HexChess.DARK_HEX_COLOR);
            else if(color == 1)
                shapes.setColor(HexChess.MEDIUM_HEX_COLOR);
            else
                shapes.setColor(HexChess.LIGHT_HEX_COLOR);
        } else {
            int color = (rank - file + 9) % 3;
            if(color == 0)
                shapes.setColor(HexChess.MEDIUM_HEX_COLOR);
            else if(color == 1)
                shapes.setColor(HexChess.LIGHT_HEX_COLOR);
            else
                shapes.setColor(HexChess.DARK_HEX_COLOR);
        }
    }

    /**
     * Draw the hexagonal game board.
     */
    private void drawBoard() {
        shapes.begin(ShapeRenderer.ShapeType.Filled);

        for(int file = 0; file < 11; file++) {
            for(int rank = 0; rank < Board.getNumRanks(file); rank++) {
                setColor(rank, file);
                game.drawHexagon(Board.getX(rank, file),
                        Board.getY(rank, file),
                        HEX_WIDTH/2, true);
            }
        }

        shapes.end();
    }

    /**
     * Draw all the pieces on the board.
     */
    private void drawPieces() {
        batch.begin();

        for(int file = 0; file < 11; file++) {
            for(int rank = 0; rank < Board.getNumRanks(file); rank++) {
                Hex hex = board.getHex(rank, file);

                if(hex.getColor() == Hex.EMPTY || hex.getId() == Hex.EMPTY)
                    continue;

                batch.draw(pieceImages[hex.getColor()][hex.getId()],
                        Board.getX(rank, file) - AssetLoader.PIECE_WIDTH/2f,
                        Board.getY(rank, file) - AssetLoader.PIECE_WIDTH/2f);
            }
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
