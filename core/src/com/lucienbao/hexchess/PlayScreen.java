package com.lucienbao.hexchess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.lucienbao.board.Board;
import com.lucienbao.board.Hex;
import com.lucienbao.ui.Button;
import com.lucienbao.utils.AssetLoader;
import com.lucienbao.utils.MoveRules;

import static com.badlogic.gdx.graphics.GL20.GL_BLEND;
import static com.badlogic.gdx.graphics.GL20.GL_SRC_ALPHA;
import static java.lang.Math.sqrt;

public class PlayScreen implements Screen {
    public static final float HEX_WIDTH = 96;
    public static final float HEX_NEST_WIDTH = HEX_WIDTH * 3f / 4;
    public static final float HEX_HEIGHT = (float) (HEX_WIDTH * sqrt(3) / 2);
    public static final int BOARD_CENTER_X = 750;
    public static final int BOARD_CENTER_Y = 540;

    public static final int QUIT_BTN_X = 100;
    public static final int QUIT_BTN_Y = 1018;
    public static final int QUIT_BTN_WIDTH = 150;
    public static final int QUIT_BTN_HEIGHT = 75;

    private final HexChess game;
    private final SpriteBatch batch;
    private final ShapeRenderer shapes;

    private final TextureRegion[][] pieceImages;

    private final Sound move, capture, error, lowTime, gameStartEnd;

    private final Board board;
    private Hex selectedHex;
    private boolean whiteToMove;

    // TODO: add move stack to allow for viewing of previous moves,
    // like in lichess

    // TODO: add pause button
    private final Button quitButton;

    public PlayScreen(final HexChess game) {
        this.game = game;
        this.batch = game.batch;
        this.shapes = game.shapes;
        this.pieceImages = AssetLoader.pieces;

        this.move = AssetLoader.move;
        this.capture = AssetLoader.capture;
        this.error = AssetLoader.error;
        this.lowTime = AssetLoader.lowTime;
        this.gameStartEnd = AssetLoader.gameStartEnd;

        this.board = new Board();
        this.selectedHex = null;
        this.whiteToMove = true; // White always first to move

        this.quitButton = new Button("Quit",
                QUIT_BTN_X,
                QUIT_BTN_Y,
                QUIT_BTN_WIDTH, QUIT_BTN_HEIGHT,
                HexChess.BUTTON_PASSIVE,
                HexChess.BUTTON_HOVERED,
                game.mediumFont);
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
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

        // Draw shapes
        shapes.begin(ShapeRenderer.ShapeType.Filled);

        drawBoard();

        quitButton.displayButtonBackground(shapes);

        shapes.end();

        // Draw images/text
        batch.begin();

        drawCoordinates();
        drawPieces();

        quitButton.displayButtonText(batch);

        batch.end();

        if(selectedHex != null)
            drawSelected();
    }

    /**
     * Draw things depending on the piece currently selected:
     * <ul>
     *     <li>Possible-move highlights</li>
     *     <li>Selected-piece highlight</li>
     *     <li>Floating piece following mouse-drag</li>
     * </ul>
     */
    private void drawSelected() {
        // Allows for transparent shapes
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapes.begin(ShapeRenderer.ShapeType.Filled);

        // Possible move highlights
        shapes.setColor(HexChess.MOVE_HEX_COLOR);
        int[][] moves = MoveRules.generateMoves(selectedHex, board);
        for(int file = 0; file < 11; file++) {
            for(int rank = 0; rank < Board.getNumRanks(file); rank++) {
                switch(moves[file][rank]) {
                    // Draw movement possible
                    case MoveRules.MOVE:
                    case MoveRules.MOVE_PROMOTE:
                        shapes.circle(Board.getX(file, rank),
                                Board.getY(file, rank),
                                HEX_WIDTH / 8);
                        break;
                    // Draw capture possible
                    case MoveRules.CAPTURE:
                    case MoveRules.CAPTURE_PROMOTE:
                    case MoveRules.CAPTURE_EN_PASSANT:
                        // End the current `Filled` shape type,
                        // switch to `Line`, draw, then switch back
                        // Hacky but it works well enough
                        shapes.end();
                        shapes.begin(ShapeRenderer.ShapeType.Line);
                        // My attempt at drawing a thick circle:
                        // Draw a bunch of thin ones at varying radii
                        for(float i = 0.5f; i < 11; i += 0.4f)
                            shapes.circle(Board.getX(file, rank),
                                    Board.getY(file, rank),
                                    HEX_HEIGHT / 2 - i,
                                    100);
                        shapes.end();
                        shapes.begin(ShapeRenderer.ShapeType.Filled);
                        break;
                }
            }
        }

        // Piece highlight
        shapes.setColor(HexChess.HIGHLIGHT_HEX_COLOR);
        int file = selectedHex.getFile();
        int rank = selectedHex.getRank();
        game.drawHexagon(Board.getX(file, rank),
                Board.getY(file, rank),
                HEX_WIDTH / 2, true);

        shapes.end();
        Gdx.gl.glDisable(GL_BLEND);

        // Floating piece
        batch.begin();
        drawFloatingPiece();
        batch.end();
    }

    /**
     * Set the current <code>ShapeRenderer</code>'s color depending
     * on the rank and file about to be drawn.
     *
     * @param rank Rank of the cell on the board.
     * @param file File of the cell on the board.
     */
    private void setColor(int file, int rank) {
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
        for(int file = 0; file < 11; file++) {
            for(int rank = 0; rank < Board.getNumRanks(file); rank++) {
                setColor(file, rank);
                game.drawHexagon(Board.getX(file, rank),
                        Board.getY(file, rank),
                        HEX_WIDTH / 2, true);
            }
        }
    }

    /**
     * Draw the coordinate hints along the sides of the board.
     */
    private void drawCoordinates() {
        // Draw the files a-k along the bottom
        for(int file = 0; file < 11; file++) {
            game.drawCenteredText(0, (char) ('a' + file) + "",
                    Board.getX(file, -1),
                    Board.getY(file, -1) + HEX_HEIGHT / 5);
        }
        // Draw the ranks 1-6 along the left side
        for(int rank = 0; rank < 6; rank++) {
            game.drawCenteredText(0, rank + 1 + "",
                    Board.getX(-1, rank),
                    Board.getY(-1, rank) + HEX_HEIGHT / 10);
        }
        // Draw the ranks 7-11 along the top left side (diagonal)
        for(int rank = 6; rank < 11; rank++) {
            game.drawCenteredText(0, rank + 1 + "",
                    Board.getX(-6 + rank, rank),
                    Board.getY(-6 + rank, rank) + HEX_HEIGHT / 5);
        }
    }

    /**
     * Draw all the pieces on the board.
     */
    private void drawPieces() {
        for(int file = 0; file < 11; file++) {
            for(int rank = 0; rank < Board.getNumRanks(file); rank++) {
                Hex hex = board.getHex(file, rank);

                if(hex.getColor() == Hex.EMPTY || hex.getId() == Hex.EMPTY)
                    continue;

                batch.draw(pieceImages[hex.getColor()][hex.getId()],
                        Board.getX(file, rank) - AssetLoader.PIECE_WIDTH / 2f,
                        Board.getY(file, rank) - AssetLoader.PIECE_WIDTH / 2f);
            }
        }
    }

    /**
     * Draw floating piece if there is a piece selected and being dragged.
     */
    private void drawFloatingPiece() {
        if(selectedHex == null)
            return;

        if(Gdx.input.isTouched())
            batch.draw(pieceImages[selectedHex.getColor()][selectedHex.getId()],
                    Gdx.input.getX() - AssetLoader.PIECE_WIDTH / 2f,
                    HexChess.SCREEN_HEIGHT - Gdx.input.getY() - AssetLoader.PIECE_WIDTH / 2f);
        else {
            int file = selectedHex.getFile();
            int rank = selectedHex.getRank();
            batch.draw(pieceImages[selectedHex.getColor()][selectedHex.getId()],
                    Board.getX(file, rank) - AssetLoader.PIECE_WIDTH / 2f,
                    Board.getY(file, rank) - AssetLoader.PIECE_WIDTH / 2f);
        }
    }

    /**
     * Check if any of the buttons belonging to this screen are being
     * hovered over.
     *
     * @param mouseX Mouse x-coordinate.
     * @param mouseY Mouse y-coordinate.
     * @return Whether the mouse is hovering over one of this screen's buttons.
     */
    public boolean checkButtonHover(int mouseX, int mouseY) {
        return quitButton.checkHovered(mouseX, mouseY);
    }

    public Button getQuitButton() {
        return quitButton;
    }

    /**
     * Control everything related to pressing a mouse button down.
     *
     * @param mouseX      Mouse x-coordinate.
     * @param mouseY      Mouse y-coordinate.
     * @param mouseButton Mouse button.
     */
    public void handleMousePressed(int mouseX, int mouseY, int mouseButton) {
        // Right mouse button deselects, allowing user to cancel move
        if(mouseButton == Input.Buttons.RIGHT) {
            this.selectedHex = null;
            return;
        }

        // Only care about left mouse button
        if(mouseButton != Input.Buttons.LEFT)
            return;

        Hex hoveredHex = getHoveredHex(mouseX, mouseY);

        // Clicked off grid, cancel selection
        if(hoveredHex == null) {
            selectedHex = null;
            return;
        }

        if(selectedHex == null) {
            if(hoveredHex.correctColor(whiteToMove))
                selectedHex = hoveredHex;
        } else {
            // If the user clicks the same piece, don't do anything
            if(selectedHex == hoveredHex)
                return;
            // If the user clicks another of their pieces, select that instead
            if(hoveredHex.correctColor(whiteToMove))
                selectedHex = hoveredHex;
                // If the user clicks anything else, then try that move
            else
                attemptMove(selectedHex, hoveredHex);
        }
    }

    /**
     * Control everything related to releasing the mouse button.
     *
     * @param mouseX      Mouse x-coordinate.
     * @param mouseY      Mouse y-coordinate.
     * @param mouseButton Mouse button.
     */
    public void handleMouseReleased(int mouseX, int mouseY, int mouseButton) {
        // We only care about the left mouse button
        if(mouseButton != Input.Buttons.LEFT)
            return;

        // Nothing selected, nothing to do
        if(selectedHex == null)
            return;

        Hex hoveredHex = getHoveredHex(mouseX, mouseY);

        // User has dragged piece off the board, cancel selection
        if(hoveredHex == null) {
            selectedHex = null;
            return;
        }

        if(selectedHex != null && selectedHex != hoveredHex)
            attemptMove(selectedHex, hoveredHex);
    }

    /**
     * Check to see if the move is valid, and if so, execute it.
     *
     * @param from Starting hex.
     * @param to   Destination hex.
     */
    private void attemptMove(Hex from, Hex to) {
        // Implementation note: we only change the color and piece type
        // of each hex, rather than modeling moving the pieces around
        // by changing their rank and file. So each Hex will stay at the
        // same rank/file for the entire game.

        // TODO: implement THIS NEXT!!!
        // might have to implement piece rules first tho...
        switch(MoveRules.canMove(from, to, board)) {
            case MoveRules.ILLEGAL:
                error.play();
                break;
            case MoveRules.MOVE:
                to.setPiece(from.getColor(), from.getId());
                from.setPiece(Hex.EMPTY, Hex.EMPTY);
                move.play();
                break;
            case MoveRules.CAPTURE:
                to.setPiece(from.getColor(), from.getId());
                from.setPiece(Hex.EMPTY, Hex.EMPTY);
                capture.play();
                break;
            case MoveRules.MOVE_PROMOTE:
                // TODO: make sound play after promotion popup happens
                break;
            case MoveRules.CAPTURE_PROMOTE:
                // TODO: make sound play after promotion popup happens
                break;
            case MoveRules.CAPTURE_EN_PASSANT:
                // TODO: implement
                capture.play();
                break;
        }

        selectedHex = null;
    }

    /**
     * Get the <code>Hex</code> the mouse is on, or return <code>null</code> if none.
     *
     * @param mouseX Mouse x-coordinate.
     * @param mouseY Mouse y-coordinate.
     * @return The hovered <code>Hex</code> or <code>null</code>.
     */
    private Hex getHoveredHex(int mouseX, int mouseY) {
        for(int file = 0; file < 11; file++)
            for(int rank = 0; rank < Board.getNumRanks(file); rank++)
                if(checkHexHover(file, rank, mouseX, mouseY))
                    return board.getHex(file, rank);
        return null;
    }

    /**
     * Check if the hex at the given rank and file is being hovered over by the mouse.
     *
     * @param rank Hex's rank.
     * @param file Hex's file.
     */
    private boolean checkHexHover(int file, int rank, int mouseX, int mouseY) {
        return board.getCollisionPolygon(file, rank).contains(mouseX, mouseY);
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
