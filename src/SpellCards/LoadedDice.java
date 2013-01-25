package SpellCards;

import game.DataLayer;
import game.Player;
import game.Tile;

import java.util.Iterator;

import main.GamePlayState;
import main.GamePlayState.STATES;

public class LoadedDice extends ActivateSpell{
	
	public LoadedDice(DataLayer dlayer, GamePlayState gps) {
		this.dlayer = dlayer;
		this.gps = gps;
	}
	
	@Override
	public boolean runEffect() {
		resetDefaultState();
		init thread = new init(dlayer, gps);
		thread.start();
		return isSuccessRun();
	}
	
	class init extends Thread{
		
		DataLayer dlayer;
		GamePlayState gps;
		
		public init(DataLayer dlayer, GamePlayState gps) {
			this.dlayer = dlayer;
			this.gps = gps;
		}
		
		public void run(){
			STATES returnState = gps.getCurrentState();
			try {
				while(active){
					playergui.setTextPane("Please roll the dice.\n");
					//first get the possible moves for +3.
					int origRoll = gps.getCurrentPlayer().getRoll();
					int highRoll = origRoll + 3;
					int lowRoll = origRoll - 3;
					if (lowRoll <= 0) {
						lowRoll = 1;
					}
					gps.getCurrentPlayer().setRoll(highRoll);
					playergui.setRolled(true);
					playergui.setRollDice(false);
					gps.setState(STATES.ROLL_STATE);
					while (gps.getCurrentState() != STATES.PRE_MOVEMENT_STATE) {
						sleep(5);
					}
					//Then get possible moves for -3.
					
					gps.getCurrentPlayer().setRoll(lowRoll);
					playergui.setRolled(true);
					playergui.setRollDice(false);
					gps.setState(STATES.ROLL_STATE);
					while (gps.getCurrentState() != STATES.PRE_MOVEMENT_STATE) {
						sleep(5);
					}
					playergui.setTextPane("You can now move either " + lowRoll + " or " + highRoll + " spaces.\n");
					gps.setState(returnState);
					setActive(false);
				}
				return;
			} catch (InterruptedException e) {}
		}
	}

}
