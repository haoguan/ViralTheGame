package main;

import game.PlayerGui;

import javax.swing.SwingUtilities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class MainMenuState extends BasicGameState {

	int stateID = 0;

	Image background = null;
	Image startGameOption = null;
	Image exitOption = null;

	int menuX = 300;
	int menuY = 300;

	// int menuX = 810;
	// int menuY = 550;

	float startGameScale = 1;
	float exitScale = 1;
	float scaleStep = 0.0001f;

	public MainMenuState(int stateID) {
		this.stateID = stateID;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {

		background = new Image("res/background.png");

		// load menu buttons
		Image menuOptions = new Image("res/mainMenuButtons.png");
		startGameOption = menuOptions.getSubImage(0, 0, 377, 71);
		exitOption = menuOptions.getSubImage(0, 71, 377, 71);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		// draw the background
		background.draw(0, 0);

		startGameOption.draw(menuX, menuY, startGameScale);
		exitOption.draw(menuX, menuY + 80, exitScale);

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		Input input = gc.getInput();

		int mouseX = input.getMouseX();
		int mouseY = input.getMouseY();

		boolean insideStartGame = false;
		boolean insideExit = false;

		if ((mouseX >= menuX)
				&& (mouseX <= menuX + startGameOption.getWidth())
				&& ((mouseY >= menuY) && (mouseY <= menuY
						+ startGameOption.getHeight()))) {
			insideStartGame = true;
		} else if ((mouseX >= menuX)
				&& (mouseX <= menuX + exitOption.getWidth())
				&& ((mouseY >= menuY + 80))
				&& (mouseY <= menuY + 80 + exitOption.getHeight())) {
			insideExit = true;
		}

		if (insideStartGame == true) {
			if (startGameScale < 1.05f) {
				startGameScale += scaleStep * delta;
			}
			if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
				sbg.enterState(ViralGame.GAMEPLAYSTATE);
//				SwingUtilities.invokeLater(new Runnable() {
//					public void run() {
//						initializePlayer();
//					}
//				});
			}
			
			
			
		} else {
			if (startGameScale > 1.0f) {
				startGameScale -= scaleStep * delta;
			}
		}

		if (insideExit == true) {
			if (exitScale < 1.05f) {
				exitScale += scaleStep * delta;
			}

			if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
				gc.exit();
			}
		} else {
			if (exitScale > 1.0f) {
				exitScale -= scaleStep * delta;
			}
		}

	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return stateID;
	}



}
