package SpellCards;

import game.DataLayer;
import main.GamePlayState;
import main.GamePlayState.STATES;

public class RabbitsFoot extends ActivateSpell {
	
	public RabbitsFoot (DataLayer dlayer, GamePlayState gps) {
		this.dlayer = dlayer;
		this.gps = gps;
		setCounter(true);
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
			boolean first = true;
			try {
				while(active){
					if (first) {
						playergui.setTextPane("You are unaffected by spells this turn.\n");
						gps.setState(STATES.USE_SPELL_STATE);
						playergui.getPlayer().setCanTarget(false);
						gps.setState(returnState);
						first = false;
					}
					
					//set canTarget to true again once a new turn happens.
					if (gps.getCurrentState() == STATES.CHANGE_PLAYER_STATE) {
						playergui.getPlayer().setCanTarget(true);
						setActive(false);
					}
					else {
						sleep(5);
					}
				}
				return;
			} catch (InterruptedException e) {}
		}
	}

}
