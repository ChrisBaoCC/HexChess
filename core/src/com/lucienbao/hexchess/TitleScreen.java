package com.lucienbao.hexchess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.lucienbao.ui.Button;
import com.lucienbao.utils.AssetLoader;

public class TitleScreen implements Screen {
    final HexChess game;
    final SpriteBatch batch;
    final ShapeRenderer shapes;

    final TextureRegion[][] pieces;
    final Texture splash;

    public TitleScreen(final HexChess game) {
        this.game = game;
        this.batch = game.batch;
        this.shapes = game.shapes;
        this.pieces = AssetLoader.pieces;
        this.splash = AssetLoader.splash;

        game.buttons.add(new Button("Play!",
                HexChess.SCREEN_WIDTH * 5/16,
                HexChess.SCREEN_HEIGHT / 2 + 100,
                250, 75,
                HexChess.BUTTON_PASSIVE,
                HexChess.BUTTON_HOVERED,
                game.mediumFont));
        game.buttons.add(new Button("Rules",
                HexChess.SCREEN_WIDTH * 5/16,
                HexChess.SCREEN_HEIGHT / 2,
                250, 75,
                HexChess.BUTTON_PASSIVE,
                HexChess.BUTTON_HOVERED,
                game.mediumFont));
        game.buttons.add(new Button("Exit",
                HexChess.SCREEN_WIDTH * 5/16,
                HexChess.SCREEN_HEIGHT / 2 - 100,
                250, 75,
                HexChess.BUTTON_PASSIVE,
                HexChess.BUTTON_HOVERED,
                game.mediumFont));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Clear screen with black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw shapes (hexes etc.)
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(HexChess.DARK_HEX_COLOR);
        game.drawHexagon((float) HexChess.SCREEN_WIDTH * 2/3,
                (float) HexChess.SCREEN_HEIGHT * 3/8,
                200, true);

        for(Button button : game.buttons) {
            button.displayButtonBackground(shapes);
        }
        shapes.end();

        // Draw sprites (pieces and other images)
        batch.begin();
        game.drawCenteredText(1, "HexChess",
                (float) HexChess.SCREEN_WIDTH * 2/3,
                (float) HexChess.SCREEN_HEIGHT * 11/16);

        batch.draw(splash,
                (float) HexChess.SCREEN_WIDTH * 2/3 - (float) splash.getWidth()/2,
                (float) HexChess.SCREEN_HEIGHT * 3/8 - (float) splash.getHeight()/2);

        for(Button button : game.buttons) {
            button.displayButtonText(batch);
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
