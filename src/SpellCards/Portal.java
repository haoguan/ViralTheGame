package SpellCards;

import game.DataLayer;
import game.Tile;
import main.GamePlayState;
import main.GamePlayState.STATES;

public class Portal extends ActivateSpell {
	DataLayer dlayer;
	GamePlayState gps;
	
	public Portal (DataLayer dlayer, GamePlayState gps) {
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
		return isSuccessRun();  //no possible way for exception, so just return true.
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
					playergui.setTextPane("Select the source tile.\n");
					gps.setSpellTargetTile(null);
					gps.setState(STATES.USE_SPELL_STATE);
					while (gps.getSpellTargetTile() == null) {
						sleep(5);
					}
					targetTile = gps.getSpellTargetTile();
					//conditions for successful play.
					if (!targetTile.hasCells()) {
						gps.setState(returnState);
						failureToPlay("No cells to transfer on targeted tile.\n");
					}
					else if (targetTile.getElement() == Tile.CHECKPOINT) {
						gps.setState(returnState);
						failureToPlay("Cannot select checkpoint tiles!\n");
					}
					else {
						setCheckSuccess(true);
						//store the source tile.
						lastTargetTile = targetTile;
						gps.setNewInput(false);
						
						//ask for new input.
						playergui.setTextPane("Select the destination tile.\n");
						while (!gps.getNewInput()) {
							sleep(5);
						}
						targetTile = gps.getSpellTargetTile();
						
						//find how many cells to move and remove from old tile.
						int numCells = lastTargetTile.findNumCells(playergui.getPlayer());
						if (targetTile.getNumCells() + numCells > Tile.BOARD_MAX_CELLS) {
							gps.setState(returnState);
							failureToPlay("Cannot transfer. Max cells limit will be exceeded.\n");
						}
						else {
							lastTargetTile.removeAllCells(playergui.getPlayer());
							//add them on new tile.
							targetTile.addCell(playergui.getPlayer(), gps, numCells);
							playergui.setTextPane("Cells have been moved.\n");
							gps.setState(returnState);
							setActive(false);
						}
					}
				}	
			} catch (InterruptedException e) {}
			return; 
		}
	}
}
