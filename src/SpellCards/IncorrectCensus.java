package SpellCards;

import java.util.Iterator;

import game.Cell;
import game.DataLayer;
import game.Tile;
import main.GamePlayState;
import main.GamePlayState.STATES;

public class IncorrectCensus extends ActivateSpell {

	DataLayer dlayer;
	GamePlayState gps;
	
	public IncorrectCensus(DataLayer dlayer, GamePlayState gps) {
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
			Tile targetTile = null;
			boolean first = true;
			boolean addedSpots = false;
			try {
				while(active){
					if (first) {
						playergui.setTextPane("Please select a tile (except checkpoint tiles) to double your own cells.\n");
						gps.setSpellTargetTile(null);
						gps.setState(STATES.USE_SPELL_STATE);
						while (gps.getSpellTargetTile() == null) {
							sleep(5);
						}
						targetTile = gps.getSpellTargetTile();
						
						if (targetTile.getRingNum() == 7) {
							gps.setSpellTargetTile(null);
							gps.setState(returnState);
							failureToPlay("Cannot select checkpoint tiles.\n");
						}
						else {
							int numPlayerCells = 0;
							for (Iterator<Cell> iter = targetTile.getCellStorage().keySet().iterator(); iter.hasNext(); ) {
								Cell nextCell = iter.next();
								if (playergui.getPlayer() == targetTile.getCellStorage().get(nextCell)) {
									numPlayerCells++;
								}
							}
							if (numPlayerCells == 0) {
								gps.setSpellTargetTile(null);
								gps.setState(returnState);
								failureToPlay("You currently own no cells on target tile.\n");
							}
							else {
								setCheckSuccess(true);
								if (targetTile.getNumCells() > 1) {
									targetTile.setCellParts(true);
									addedSpots = true;
								}
								//add 2 more cells.
								if (numPlayerCells == 2) {
									targetTile.addCell(playergui.getPlayer(), gps, 2);
								}
								else {
									//only add 1.
									targetTile.addCell(playergui.getPlayer(), gps);
								}
							}
							gps.setState(returnState);
							if (!addedSpots) {
								setActive(false);
							}
							first = false;
						}
					}
					
					if (addedSpots && targetTile.getNumCells() <= 2) {
						targetTile.getCellParts().clear(); //erase positions and restart.
						targetTile.setCellParts(false);
						for (Iterator<Cell> iter = targetTile.getCellStorage().keySet().iterator(); iter.hasNext(); ) {
							Cell nextCell = iter.next();
							nextCell.setCurrentPosition(targetTile.getCellPosition(nextCell)); //reset the position to be aligned.
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
