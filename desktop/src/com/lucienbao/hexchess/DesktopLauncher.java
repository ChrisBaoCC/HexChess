package com.lucienbao.hexchess;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.lucienbao.hexchess.HexChess;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument

public class DesktopLauncher {
	public static void main (String[] args) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("HexChess");
		config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
//		config.setWindowedMode(1920, 1080);
		config.setForegroundFPS(60);
		config.setBackBufferConfig(8,8,8,8,16,0, 4);
		new Lwjgl3Application(new HexChess(), config);
	}
}
