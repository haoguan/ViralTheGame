package SpellCards;

import game.DataLayer;
import main.GamePlayState;

public class Lethargy extends ActivateSpell {
	
	DataLayer dlayer;
	GamePlayState gps;
	
	public Lethargy(DataLayer dlayer, GamePlayState gps) {
		this.dlayer = dlayer;
		this.gps = gps;
	}
	
	public boolean runEffect() {
		resetDefaultState();
		playergui.setNewTextPane();
		playergui.setTextPane("Current Player lost their turn.\n");
		gps.getCurrentPlayer().getPlayerGui().setNewTextPane();
		gps.getCurrentPlayer().getPlayerGui().setTextPane("You lost your turn!\n");
		gps.getCurrentPlayer().getPlayerGui().resetButton(); //need to reset buttons for current player.
		gps.resetState();
		return isSuccessRun();
	}
	
}
