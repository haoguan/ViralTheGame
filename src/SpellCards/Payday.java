package SpellCards;

import game.Cell;
import game.DataLayer;
import game.Tile;
import java.util.Iterator;
import main.GamePlayState;
import main.GamePlayState.STATES;

public class Payday extends ActivateSpell {

	DataLayer dlayer;
	GamePlayState gps;
	
	public Payday(DataLayer dlayer, GamePlayState gps) {
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
			Tile currTile = playergui.getPlayer().getCurrentTile();
			boolean first = true;
			boolean addedSpots = false;
			try {
				while(active){
					if (first) {
						gps.setState(STATES.USE_SPELL_STATE);
						if (currTile.getElement() == Tile.CHECKPOINT) {
							gps.setState(returnState);
							failureToPlay("Cannot use on checkpoint tiles.\n");
						}
						else {
							int numPlayerCells = 0;
							for (Iterator<Cell> iter = currTile.getCellStorage().keySet().iterator(); iter.hasNext(); ) {
								Cell nextCell = iter.next();
								if (playergui.getPlayer() == currTile.getCellStorage().get(nextCell)) {
									numPlayerCells++;
								}
							}
							if (numPlayerCells == 0) {
								gps.setState(returnState);
								failureToPlay("You currently own no cells on target tile.\n");
							}
							else {
								setCheckSuccess(true);
								//only add spots if total cells will be greater than 2 after double.
								if (currTile.getNumCells() > 1) {
									currTile.setCellParts(true);
									addedSpots = true;
								}
								//add 2 more cells.
								if (numPlayerCells == 2) {
									currTile.addCell(playergui.getPlayer(), gps, 2);
								}
								else {
									//only add 1.
									currTile.addCell(playergui.getPlayer(), gps);
								}
								playergui.setTextPane("Doubled cells on current tile.\n");
							}
							gps.setState(returnState);
							if (!addedSpots) {
								setActive(false);
							}
							first = false;
						}
					}
					
					if (addedSpots && currTile.getNumCells() <= 2) {
						currTile.getCellParts().clear(); //erase positions and restart.
						currTile.setCellParts(false);
						for (Iterator<Cell> iter = currTile.getCellStorage().keySet().iterator(); iter.hasNext(); ) {
							Cell nextCell = iter.next();
							nextCell.setCurrentPosition(currTile.getCellPosition(nextCell)); //reset the position to be aligned.
						}
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
