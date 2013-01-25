package SpellCards;

import game.DataLayer;
import game.Tile;
import main.GamePlayState;
import main.GamePlayState.STATES;

public class Implosion extends ActivateSpell {

	DataLayer dlayer;
	GamePlayState gps;
	
	public Implosion (DataLayer dlayer, GamePlayState gps) {
		this.dlayer = dlayer;
		this.gps = gps;
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
			Tile lastTargetTile;
			try {
				while(active){
					playergui.setTextPane("Please select a destination tile.\n");
					gps.setSpellTargetTile(null);
					gps.setState(STATES.USE_SPELL_STATE);
					while (gps.getSpellTargetTile() == null) {
						sleep(5);
					}
					targetTile = gps.getSpellTargetTile();
					//conditions for eligibility
					if (targetTile.getNumCells() >= Tile.BOARD_MAX_CELLS) {
						gps.setState(returnState);
						failureToPlay("Target tile is already full.\n");
					}
					else if (targetTile.getElement() == Tile.CHECKPOINT) {
						gps.setState(returnState);
						failureToPlay("Cannot select checkpoint tile.\n");
					}
					else {
						setCheckSuccess(true);
						int numToAdd = (Tile.BOARD_MAX_CELLS - targetTile.getNumCells());
						//change numToAdd to maximum number of cells the player has.
						if (numToAdd > playergui.getPlayer().getNumCells()) {
							numToAdd = playergui.getPlayer().getNumCells();
						}
						lastTargetTile = targetTile;
						while (numToAdd > 0) {
							gps.setNewInput(false);
							playergui.setTextPane("Please select a source tile.\n"); 
							while (!gps.getNewInput()) {
								sleep(5);
							}
							targetTile = gps.getSpellTargetTile();
							
							if (!targetTile.containsCell(playergui.getPlayer())) {
								playergui.setTextPane("Target tile does not contain your cell.\n");
							}
							else {
								targetTile.removeCell(playergui.getPlayer(), true);
								lastTargetTile.addCell(playergui.getPlayer(), gps);
								numToAdd--;
							}
						}
						playergui.setTextPane("Successfully imploded cells.\n");
						gps.setState(returnState);
						setActive(false);
					}
				}
			} catch (InterruptedException e) {}
			return;
		}
	}
}
