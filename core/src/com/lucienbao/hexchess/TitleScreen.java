package com.lucienbao.hexchess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.lucienbao.ui.Button;
import com.lucienbao.utils.AssetLoader;

public class TitleScreen implements Screen {
    private final HexChess game;
    private final SpriteBatch batch;
    private final ShapeRenderer shapes;

    private final Texture splash;

    private final Button playButton;
    private final Button rulesButton;
    private final Button exitButton;

    public static final int BUTTON_WIDTH = 250;
    public static final int BUTTON_HEIGHT = 75;

    public TitleScreen(final HexChess game) {
        this.game = game;
        this.batch = game.batch;
        this.shapes = game.shapes;
        this.splash = AssetLoader.splash;

        this.playButton = new Button("Play!",
                HexChess.SCREEN_WIDTH * 5/16,
                HexChess.SCREEN_HEIGHT / 2 + 100,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                HexChess.BUTTON_PASSIVE,
                HexChess.BUTTON_HOVERED,
                game.mediumFont);
        this.rulesButton = new Button("Rules",
                HexChess.SCREEN_WIDTH * 5/16,
                HexChess.SCREEN_HEIGHT / 2,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                HexChess.BUTTON_PASSIVE,
                HexChess.BUTTON_HOVERED,
                game.mediumFont);
        this.exitButton = new Button("Exit",
                HexChess.SCREEN_WIDTH * 5/16,
                HexChess.SCREEN_HEIGHT / 2 - 100,
                BUTTON_WIDTH, BUTTON_HEIGHT,
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
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw shapes (hexes etc.)
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(HexChess.DARK_HEX_COLOR);
        game.drawHexagon((float) HexChess.SCREEN_WIDTH * 2/3,
                (float) HexChess.SCREEN_HEIGHT * 3/8,
                200, true);

        playButton.displayButtonBackground(shapes);
        rulesButton.displayButtonBackground(shapes);
        exitButton.displayButtonBackground(shapes);
        shapes.end();

        // Draw sprites (pieces and other images)
        batch.begin();
        game.drawCenteredText(1, "HexChess",
                (float) HexChess.SCREEN_WIDTH * 2/3,
                (float) HexChess.SCREEN_HEIGHT * 11/16);

        batch.draw(splash,
                (float) HexChess.SCREEN_WIDTH * 2/3 - (float) splash.getWidth()/2,
                (float) HexChess.SCREEN_HEIGHT * 3/8 - (float) splash.getHeight()/2);

        playButton.displayButtonText(batch);
        rulesButton.displayButtonText(batch);
        exitButton.displayButtonText(batch);
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

    /**
     * Check if any of the buttons belonging to this screen are being
     * hovered over.
     * @param mouseX Mouse x-coordinate.
     * @param mouseY Mouse y-coordinate.
     * @return Whether the mouse is hovering over one of this screen's buttons.
     */
    public boolean checkButtonHover(int mouseX, int mouseY) {
        if(playButton.checkHovered(mouseX, mouseY))
            return true;
        if(rulesButton.checkHovered(mouseX, mouseY))
            return true;
        return exitButton.checkHovered(mouseX, mouseY);
    }

    public Button getPlayButton() {
        return playButton;
    }

    public Button getRulesButton() {
        return rulesButton;
    }

    public Button getExitButton() {
        return exitButton;
    }
}
