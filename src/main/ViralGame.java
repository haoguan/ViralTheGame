package main;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class ViralGame extends StateBasedGame{
	
	//initialize variables representing the various states.
	public static final String GAMENAME = "Viral";
	public static final int MAINMENUSTATE = 0;
    public static final int GAMEPLAYSTATE = 1;

	public ViralGame(String gamename) {
		super(gamename);
		this.addState(new MainMenuState(MAINMENUSTATE));
		this.addState(new GamePlayState(GAMEPLAYSTATE));
		
	}
	
	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new ViralGame(GAMENAME));
		app.setAlwaysRender(true);
		app.setDisplayMode(700, 700, false);
		app.setVSync(true);
//	    app.setTargetFrameRate(80);
//	    app.setMinimumLogicUpdateInterval(25);
		app.start();
		
	}

	@Override
	public void initStatesList(GameContainer gameContainer) throws SlickException {
		// TODO Auto-generated method stub
		this.getState(MAINMENUSTATE).init(gameContainer, this);
		this.getState(GAMEPLAYSTATE).init(gameContainer, this);
		this.enterState(MAINMENUSTATE);
	}

}
