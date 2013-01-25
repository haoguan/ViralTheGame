package SpellCards;

import game.DataLayer;
import game.Player;
import main.GamePlayState;
import main.GamePlayState.STATES;

public class PlantedBomb extends ActivateSpell{

	DataLayer dlayer;
	GamePlayState gps;
	
	public PlantedBomb(DataLayer dlayer, GamePlayState gps) {
		this.dlayer = dlayer;
		this.gps = gps;
		setTargetRequired(true);
	}
	
	@Override
	public boolean runEffect() {
		resetDefaultState();
		init thread = new init(dlayer, gps);
		thread.start();
		//this while loop allows for thread to not be finished and still return correct result earlier.
		while (!checkSuccess) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {}
		}
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
			while(active){
				if (playergui.getTargetPlayer().getCurrentTile().getNumCells() == 0) {
					failureToPlay("The target player's current tile has no cells.\n");
				}
				else {
					setCheckSuccess(true);
					gps.setState(STATES.USE_SPELL_STATE);
					//remove cell from targetTile.
					for (Player player : gps.getPlayers()) {
						playergui.getTargetPlayer().getCurrentTile().removeAllCells(player);
					}
					writeToAllPlayers("All cells on target player's tile have been annihilated.\n");
					gps.setState(returnState);
					setActive(false);
				}
			}
			return;
		}
	}
}