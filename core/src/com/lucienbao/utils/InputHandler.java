package com.lucienbao.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Cursor;
import com.lucienbao.hexchess.HexChess;
import com.lucienbao.hexchess.PlayScreen;
import com.lucienbao.hexchess.RulesScreen;
import com.lucienbao.hexchess.TitleScreen;

public class InputHandler implements InputProcessor {
    private final HexChess game;

    public InputHandler(HexChess game) {
        this.game = game;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int mouseButton) {
        if(!(game.getScreen() instanceof PlayScreen))
            return true;

        screenY = HexChess.SCREEN_HEIGHT - screenY;

        PlayScreen playScreen = (PlayScreen) game.getScreen();
        playScreen.handleMousePressed(screenX, screenY, mouseButton);

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int mouseButton) {
        // Call mouseMoved() to check for buttons being hovered
        mouseMoved(screenX, screenY);
        screenY = HexChess.SCREEN_HEIGHT - screenY;

        if(game.getScreen() instanceof TitleScreen) {
            TitleScreen titleScreen = (TitleScreen) game.getScreen();
            if(titleScreen.getPlayButton().isHovered()) {
                game.setScreen(new PlayScreen(game));
                AssetLoader.gameStartEnd.play();
                titleScreen.dispose();
            } else if(titleScreen.getRulesButton().isHovered()) {
                game.setScreen(new RulesScreen(game));
                titleScreen.dispose();
            } else if(titleScreen.getExitButton().isHovered())
                Gdx.app.exit();
        }

        else if(game.getScreen() instanceof RulesScreen) {
            RulesScreen rulesScreen = (RulesScreen) game.getScreen();
            if(rulesScreen.getBackButton().isHovered()) {
                game.setScreen(new TitleScreen(game));
                rulesScreen.dispose();
            }

            else if(rulesScreen.getPrevButton().isHovered()) {
                rulesScreen.previousSlide();
            }

            else if(rulesScreen.getNextButton().isHovered()) {
                rulesScreen.nextSlide();
            }
        }

        else if(game.getScreen() instanceof PlayScreen) {
            PlayScreen playScreen = (PlayScreen) game.getScreen();

            if(playScreen.getQuitButton().isHovered()) {
                game.setScreen(new TitleScreen(game));
                AssetLoader.gameStartEnd.play();
                playScreen.dispose();
                return true;
            }

            playScreen.handleMouseReleased(screenX, screenY, mouseButton);
        }

        return true;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // Flip the screenY to get the y-up coordinate
        // system back again
        screenY = HexChess.SCREEN_HEIGHT - screenY;

        if(game.getScreen() instanceof TitleScreen) {
            TitleScreen titleScreen = (TitleScreen) game.getScreen();
            boolean hovering = titleScreen.checkButtonHover(screenX, screenY);

            if(hovering)
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
            else
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        }

        else if(game.getScreen() instanceof RulesScreen) {
            RulesScreen rulesScreen = (RulesScreen) game.getScreen();
            boolean hovering = rulesScreen.checkButtonHover(screenX, screenY);

            if(hovering)
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
            else
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        }

        else if(game.getScreen() instanceof PlayScreen) {
            // TODO: implement
            PlayScreen playScreen = (PlayScreen) game.getScreen();
            boolean hovering = playScreen.checkButtonHover(screenX, screenY);

            if(hovering)
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
            else
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        }

        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
