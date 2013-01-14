package SpellCards;

import java.util.Iterator;

import game.Cell;
import game.DataLayer;
import game.Tile;
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
			Tile targetTile = null;
			boolean first = true;
			boolean addedSpots = false;
			try {
				while(active){
					if (first) {
						playergui.setTextPane("Please select a tile (except checkpoint tiles) to double your own cells.\n");
						gps.setTargetTile(null);
						gps.setState(STATES.USE_SPELL_STATE);
						while (gps.getTargetTile() == null) {
							sleep(5);
						}
						targetTile = gps.getTargetTile();
						
						if (targetTile.getRingNum() == 7) {
							gps.setTargetTile(null);
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
								gps.setTargetTile(null);
								gps.setState(returnState);
								failureToPlay("You currently own no cells on target tile.\n");
							}
							else {
								setCheckSuccess(true);
								targetTile.setCellParts(true);
								addedSpots = true;
								String message = ("Player " + playergui.getPlayer().getColor() + " has selected (" + 
										targetTile.getRingNum() + ", " + targetTile.getTileID() + ").\n");
								writeToAllPlayers(message);
								//add 2 more cells.
								if (numPlayerCells == 2) {
									gps.addCell(playergui.getPlayer(), targetTile, gps, 2);
								}
								else {
									//only add 1.
									gps.addCell(playergui.getPlayer(), targetTile, gps);
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
				setActive(true); //resetting the active boolean.
				return;
			} catch (InterruptedException e) {}
		}
	}
}
