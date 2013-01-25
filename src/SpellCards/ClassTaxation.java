package SpellCards;

import game.DataLayer;
import game.Player;
import game.Tile;
import main.GamePlayState;
import main.GamePlayState.STATES;

public class ClassTaxation extends ActivateSpell {

	DataLayer dlayer;
	GamePlayState gps;
	
	public ClassTaxation (DataLayer dlayer, GamePlayState gps) {
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
			Tile targetTile;
			try {
				while(active){
					Player targetPlayer = playergui.getTargetPlayer();
					if (targetPlayer.getClassCards().size() == 0) {
						gps.setState(returnState);
						failureToPlay("Target player has no classes!\n");
					}
					else {
						setCheckSuccess(true);
						playergui.setTextPane("Please wait for target player to remove checkpoint pieces.\n");
						
						int numToRemove = targetPlayer.getPlayerGui().getClassCount();
						targetPlayer.getPlayerGui().setTextPane("Please remove " + numToRemove + " checkpoint pieces.\n");
						gps.setState(STATES.USE_SPELL_STATE);
						
						while (numToRemove > 0) {
							gps.setNewInput(false);
							while (!gps.getNewInput()){
								sleep(5);
							}
							targetTile = gps.getSpellTargetTile();
							if (targetTile.getElement() != Tile.CHECKPOINT || !targetTile.containsCell(targetPlayer)) {
								targetPlayer.getPlayerGui().setTextPane("Please select an appropriate tile.\n");
							}
							else {
								targetTile.removeCell(targetPlayer, true);
								numToRemove--;
							}
						}
						targetPlayer.getPlayerGui().setTextPane("Successfully removed pieces.\n");
						playergui.setTextPane("Target player successfully removed pieces.\n");
						gps.setState(returnState);
						setActive(false);
					}
				}
			} catch (InterruptedException e) {}
			return;
		}
	}
}