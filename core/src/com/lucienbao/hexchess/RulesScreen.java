package com.lucienbao.hexchess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.lucienbao.ui.Button;
import com.lucienbao.utils.AssetLoader;

public class RulesScreen implements Screen {
    final HexChess game;
    final SpriteBatch batch;
    final ShapeRenderer shapes;

    private final Button backButton, prevButton, nextButton;

    private final Texture[] diagrams;
    private int slideNumber;
    // 1 for each piece + 1 intro + 1 end
    public static final int NUM_SLIDES = 8;
    
    public static final int BACK_BTN_X = 100;
    public static final int BACK_BTN_Y = 1018;
    public static final int BACK_BTN_WIDTH = 150;
    public static final int BACK_BTN_HEIGHT = 75;

    public static final int PREV_BTN_X = 125;
    public static final int NEXT_BTN_X = HexChess.SCREEN_WIDTH - 125;
    public static final int SLIDE_BTN_Y = 62;
    public static final int SLIDE_BTN_WIDTH = 200;
    public static final int SLIDE_BTN_HEIGHT = 75;

    public static final int DIAGRAM_CENTER_X = 600;
    public static final int DIAGRAM_CENTER_Y = 540;
    public static final int TITLE_X = 1250;
    public static final int TITLE_Y = 775;
    public static final int PARA_X = 1010;
    public static final int PARA_CENTER_X = 360;
    public static final int PARA_Y = 625;
    public static final int PARA_CENTER_Y = 600;
    public static final int PARA_WIDTH = 600;

    public static final String[] TITLES = {
            "How to play HexChess",
            "The Pawn",
            "The Knight",
            "The Bishop",
            "The Rook",
            "The Queen",
            "The King",
            "Additional Rules"
    };

    public static final String[] PARAS = {
            "Welcome to HexChess! It shares the pieces of standard chess, with " +
                    "an additional pawn and bishop for each side. Most of the " +
                    "rules are the same. Press the [Next] button to read about " +
                    "changes to each piece.",
            "The pawn still moves forward one hex (solid circle). However, since " +
                    "diagonals in hexagonal space are a little different, their " +
                    "capturing movement is now to either hex beside the forward hex " +
                    "(X marks). They also retain their double-step move from any " +
                    "starting hex (dashed circle).",
            "Knights jump like in standard chess, so their movement cannot be " +
                    "blocked by any piece. Their movement is still an 'L': two hexes " +
                    "outwards orthogonally (through the sides of hexes), then another " +
                    "orthogonally to the side. This means they always change the " +
                    "color of the hex they stand on when they move.",
            "Bishops move diagonally. But the meaning of 'diagonal' has changed. " +
                    "Instead of literally diagonally across the board, diagonal " +
                    "movement now consists of straight lines through the corners " +
                    "of each hex. Therefore, each bishop is bound to the color it " +
                    "started the game on.",
            "Rooks move orthogonally. This is the simplest movement: directly " +
                    "through the sides of the hex. Rooks can reach any color hex.",
            "Queens combine bishop (white arrows) and rook (black arrows) movement. " +
                    "They are the most powerful piece due to their high mobility.",
            "Kings move like queens, but only one space at a time. The same rules " +
                    "for check apply; you must remove your own king from check if it " +
                    "is your move, and you must not move into check. When your king " +
                    "is in check and cannot escape, you lose!",
            "En passant still applies; a pawn can take an opposite-colored pawn " +
                    "that moved two steps if it could have been captured had it " +
                    "only moved one. Checkmate still wins 1-0, but stalemate is " +
                    "3/4-1/4. A draw is 1/2-1/2 as usual. A pawn may still move " +
                    "two steps if it is on a friendly pawn's starting square. " +
                    "Promotion happens at the opposite edge of the board. There is " +
                    "no castling.",
    };

    public RulesScreen(final HexChess game) {
        this.game = game;
        this.batch = game.batch;
        this.shapes = game.shapes;

        this.backButton = new Button("Back",
                BACK_BTN_X,
                BACK_BTN_Y,
                BACK_BTN_WIDTH, BACK_BTN_HEIGHT,
                HexChess.BUTTON_PASSIVE,
                HexChess.BUTTON_HOVERED,
                game.mediumFont);
        this.prevButton = new Button("Previous",
                PREV_BTN_X,
                SLIDE_BTN_Y,
                SLIDE_BTN_WIDTH, SLIDE_BTN_HEIGHT,
                HexChess.BUTTON_PASSIVE,
                HexChess.BUTTON_HOVERED,
                game.mediumFont);
        prevButton.setHidden(true); // since we start on slide 0
        this.nextButton = new Button("Next",
                NEXT_BTN_X,
                SLIDE_BTN_Y,
                SLIDE_BTN_WIDTH, SLIDE_BTN_HEIGHT,
                HexChess.BUTTON_PASSIVE,
                HexChess.BUTTON_HOVERED,
                game.mediumFont);

        this.slideNumber = 0;
        this.diagrams = new Texture[] {
                AssetLoader.pawnDiagram,
                AssetLoader.knightDiagram,
                AssetLoader.bishopDiagram,
                AssetLoader.rookDiagram,
                AssetLoader.queenDiagram,
                AssetLoader.kingDiagram
        };
    }

    /**
     * Check if any of the buttons belonging to this screen are being
     * hovered over.
     * @param mouseX Mouse x-coordinate.
     * @param mouseY Mouse y-coordinate.
     * @return Whether the mouse is hovering over one of this screen's buttons.
     */
    public boolean checkButtonHover(int mouseX, int mouseY) {
        return backButton.checkHovered(mouseX, mouseY)
                || prevButton.checkHovered(mouseX, mouseY)
                || nextButton.checkHovered(mouseX, mouseY);
    }

    /**
     * Go to the previous slide, if there is any.
     */
    public void previousSlide() {
        if(slideNumber > 0)
            slideNumber--;

        prevButton.setHidden(slideNumber == 0);
        nextButton.setHidden(slideNumber == NUM_SLIDES-1);
    }

    /**
     * Go to the next slide, if there is any.
     */
    public void nextSlide() {
        if(slideNumber < NUM_SLIDES-1)
            slideNumber++;

        prevButton.setHidden(slideNumber == 0);
        nextButton.setHidden(slideNumber == NUM_SLIDES-1);
    }

    public Button getBackButton() {
        return backButton;
    }

    public Button getPrevButton() {
        return prevButton;
    }

    public Button getNextButton() {
        return nextButton;
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
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        backButton.displayButtonBackground(shapes);
        prevButton.displayButtonBackground(shapes);
        nextButton.displayButtonBackground(shapes);
        shapes.end();

        // Draw images/text
        batch.begin();

        drawInstructions();

        backButton.displayButtonText(batch);
        prevButton.displayButtonText(batch);
        nextButton.displayButtonText(batch);

        // TODO: implement slideshow
        if(slideNumber > 0 && slideNumber < NUM_SLIDES-1) {
            Texture diagram = diagrams[slideNumber-1];
            batch.draw(diagram,
                    DIAGRAM_CENTER_X - diagram.getWidth() / 2f,
                    DIAGRAM_CENTER_Y - diagram.getHeight() / 2f);
        }
        batch.end();
    }

    private void drawInstructions() {
        // Center if no diagram
        if(slideNumber == 0 || slideNumber == NUM_SLIDES-1) {
            game.drawCenteredText(1,
                    TITLES[slideNumber],
                    HexChess.SCREEN_WIDTH / 2f, TITLE_Y);
            game.mediumFont.draw(batch,
                    PARAS[slideNumber],
                    PARA_CENTER_X, PARA_CENTER_Y,
                    PARA_WIDTH * 2, Align.center, true);
        }

        // Offset if diagram
        else {
            game.drawCenteredText(1,
                    TITLES[slideNumber],
                    TITLE_X, TITLE_Y);
            game.mediumFont.draw(batch,
                    PARAS[slideNumber],
                    PARA_X, PARA_Y,
                    PARA_WIDTH, Align.left, true);
        }
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
