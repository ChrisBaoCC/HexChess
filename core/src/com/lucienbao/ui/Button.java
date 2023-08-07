package com.lucienbao.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Button {
    private final int x, y;
    private final int width, height;
    private final String text;

    private final Color bg, bgHover;
    private final BitmapFont font;

    private boolean hovered;

    // TODO: rounded corners?
    public Button(String text, int x, int y, int width, int height,
                   Color bg, Color bgHover, BitmapFont font) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.bg = bg;
        this.bgHover = bgHover;

        this.font = font;

        this.hovered = false;
    }

    /**
     * Called by <code>InputHandler.mouseMoved()</code> Recalculates
     * whether the mouse is hovering over this button, and updates
     * the private <code>hovered</code> field.
     * @return Whether the mouse is currently hovering over
     * this button.
     */
    public boolean checkHovered(int mouseX, int mouseY) {
        return this.hovered = this.x - this.width/2 < mouseX
                    && mouseX < this.x + this.width/2
                    && this.y - this.height/2 < mouseY
                    && mouseY < this.y + this.height/2;
    }

    public boolean isHovered() {
        return hovered;
    }

    /**
     * Displays the button background.
     * <p>
     * NOTE! MUST have already called <code>ShapeRenderer.begin()</code>
     * with <code>ShapeType.Filled</code>!
     * @param shapes The <code>ShapeRenderer</code> to use.
     */
    public void displayButtonBackground(ShapeRenderer shapes) {
        if(this.hovered)
            shapes.setColor(bgHover);
        else
            shapes.setColor(bg);

        shapes.rect(x - (float) width/2,
                y - (float) height/2,
                width, height);
    }

    // TODO: GET THE EXIT BUTTON TO WORK!!!!

    /**
     * Displays the button text.
     * <p>
     * NOTE! MUST have already called <code>SpriteBatch.begin()</code>!
     * @param batch The <code>SpriteBatch</code> to use.
     */
    public void displayButtonText(SpriteBatch batch) {
        GlyphLayout gl = new GlyphLayout(this.font, this.text);
        this.font.draw(batch, this.text,
                x - gl.width/2,
                y + gl.height/2);
    }
}
