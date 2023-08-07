package com.lucienbao.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Cursor;
import com.lucienbao.hexchess.HexChess;
import com.lucienbao.hexchess.TitleScreen;
import com.lucienbao.ui.Button;

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
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int mouseButton) {
        // Call mouseMoved() to check for buttons being hovered
        mouseMoved(screenX, screenY);

        for(Button button : game.buttons) {
            // TODO: implement
            if(button.isHovered()) {
                System.out.println("clicked");
            }
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
            boolean hovering = false;
            for(Button button : game.buttons) {
                if(button.checkHovered(screenX, screenY)) {
                    hovering = true;
                }
            }

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
