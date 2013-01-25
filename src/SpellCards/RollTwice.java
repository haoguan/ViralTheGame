package SpellCards;

import game.DataLayer;
import main.GamePlayState;
import main.GamePlayState.STATES;

public class RollTwice extends ActivateSpell {

	public RollTwice(DataLayer dlayer, GamePlayState gps) {
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
					if (playergui.getRolled()) {
						playergui.setTextPane("Roll the dice once more.\n");
						playergui.setRolled(false);
						playergui.setRollDice(true);
						gps.setState(STATES.ROLL_STATE);
						while (gps.getCurrentState() != STATES.PRE_MOVEMENT_STATE) {
							sleep(5);
						}
						gps.setState(returnState);
						setActive(false);
					}
				}
				return;
			} catch (InterruptedException e) {}
		}
	}
}
