package com.lucienbao.hexchess;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.lucienbao.ui.Button;
import com.lucienbao.utils.AssetLoader;
import com.lucienbao.utils.InputHandler;

import java.util.ArrayList;

import static java.lang.Math.sqrt;

public class HexChess extends Game {
	public SpriteBatch batch;
	public ShapeRenderer shapes;
	public BitmapFont mediumFont;
	public BitmapFont bigFont;
	public OrthographicCamera camera;

	public ArrayList<Button> buttons;

	public static final int SCREEN_WIDTH = 1920;
	public static final int SCREEN_HEIGHT = 1080;

	public static final Color LIGHT_HEX_COLOR = new Color(0.95f, 0.62f, 0.99f, 1);
	public static final Color MEDIUM_HEX_COLOR = new Color(0.91f, 0.16f, 0.99f, 1);
	public static final Color DARK_HEX_COLOR = new Color(0.62f, 0f, 0.69f, 1); // nice

	public static final Color BACKGROUND_COLOR = new Color(0.1f, 0.1f, 0.1f, 1);
	public static final Color BUTTON_PASSIVE = new Color(0.71f, 0.05f, 0.79f, 1);
	public static final Color BUTTON_HOVERED = new Color(0.60f, 0.01f, 0.66f, 1);

	@Override
	public void create () {
		this.batch = new SpriteBatch();
		this.shapes = new ShapeRenderer();
		this.mediumFont = new BitmapFont(Gdx.files.internal("roboto-slab.fnt"));
		this.bigFont = new BitmapFont(Gdx.files.internal("roboto-slab-96.fnt"));
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, 1920, 1080);

		buttons = new ArrayList<>();
		AssetLoader.load();

		Gdx.input.setInputProcessor(new InputHandler(this));
		this.setScreen(new TitleScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		mediumFont.dispose();
		bigFont.dispose();

		this.getScreen().dispose();
		AssetLoader.dispose();
	}

	/**
	 * Draw text at a specified scale and position.
	 * @param scale 0 for medium text, 1 for big text.
	 * @param str Text to draw.
	 * @param x Center x-coordinate.
	 * @param y Center y-coordinate.
	 */
	public void drawCenteredText(int scale, String str, float x, float y) {
		switch(scale) {
			case 0: {
				GlyphLayout gl = new GlyphLayout(this.mediumFont, str);
				this.mediumFont.draw(this.batch, str, x - gl.width / 2, y);
				break;
			}
			case 1: {
				GlyphLayout gl = new GlyphLayout(this.bigFont, str);
				this.bigFont.draw(this.batch, str, x - gl.width / 2, y);
				break;
			}
			default:
				throw new IllegalArgumentException("scale must be 0 or 1");
		}
	}

	/**
	 * Draw a hexagon centered at the given position, with the given
	 * side length. The orientation is with two sides parallel to
	 * the 0 angle, so the width will be <code>2 * sideLength</code>
	 * while the height will be <code>sqrt(3) * sideLength</code>.
	 * <p>
	 * IMPORTANT NOTE! The caller MUST have called <code>ShapeRenderer.begin()</code>
	 * with the correct <code>ShapeType</code> (<code>ShapeType.Filled</code>
	 * or <code>ShapeType.Line</code>), or there WILL be an error!
	 * @param x Center x-coordinate.
	 * @param y Center y-coordinate.
	 * @param sideLength Length of each side.
	 * @param filled Select <code>false</code> for outline,
	 *               <code>true</code> for filled shape.
	 */
	public void drawHexagon(float x, float y, float sideLength,
							boolean filled) {
		if(filled) {
			// We draw 6 triangles, starting from the upper leftmost
			// and proceeding clockwise.
			shapes.triangle(x - sideLength, y,
					x - sideLength/2, y + sideLength * (float)sqrt(3)/2,
					x, y);
			shapes.triangle(x - sideLength/2, y + sideLength * (float)sqrt(3)/2,
					x + sideLength/2,	y + sideLength * (float)sqrt(3)/2,
					x, y);
			shapes.triangle(x + sideLength/2,	y + sideLength * (float)sqrt(3)/2,
					x + sideLength,		y,
					x, y);
			shapes.triangle(x + sideLength,		y,
					x + sideLength/2,	y - sideLength * (float)sqrt(3)/2,
					x, y);
			shapes.triangle(x + sideLength/2,	y - sideLength * (float)sqrt(3)/2,
					x - sideLength/2,	y - sideLength * (float)sqrt(3)/2,
					x, y);
			shapes.triangle(x - sideLength/2,	y - sideLength * (float)sqrt(3)/2,
					x - sideLength,		y,
					x, y);
			return;
		}

		// starts with leftmost and goes clockwise
		float[] vertices = {
			x - sideLength,		y,
			x - sideLength/2,	y + sideLength * (float)sqrt(3)/2,
			x + sideLength/2,	y + sideLength * (float)sqrt(3)/2,
			x + sideLength,		y,
			x + sideLength/2,	y - sideLength * (float)sqrt(3)/2,
			x - sideLength/2,	y - sideLength * (float)sqrt(3)/2,
		};
		shapes.polygon(vertices);
	}
}
