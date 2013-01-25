package SpellCards;

import game.DataLayer;
import game.Player;
import game.Tile;
import main.GamePlayState;
import main.GamePlayState.STATES;

public class PeasantsDream extends ActivateSpell {
	
	DataLayer dlayer;
	GamePlayState gps;
	
	public PeasantsDream(DataLayer dlayer, GamePlayState gps) {
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
		
		public int[] getQuadrantBounds(Tile currTile) {
			int[] quadBounds = {0, 90, 180, 270, 360};
			int lowBound;
			int highBound;
			double lowAngle = currTile.getLowAngle();
			
			if (lowAngle < 90) {
				lowBound = quadBounds[0];
				highBound = quadBounds[1];
			}
			else if (lowAngle < 180) {
				lowBound = quadBounds[1];
				highBound = quadBounds[2];
			}
			else if (lowAngle < 270) {
				lowBound = quadBounds[2];
				highBound = quadBounds[3];
			}
			else {
				lowBound = quadBounds[3];
				highBound = quadBounds[4];
			}
			
			int[] bounds =  {lowBound, highBound};
			return bounds;
		}
		
		public void run(){
			STATES returnState = gps.getCurrentState();
			Tile targetTile = null;
			Tile lastTargetTile = null;
			boolean moved = true;
			boolean first = true;
			boolean failed = false;
			try {
				while(active){
					Player currPlayer = playergui.getPlayer();
					gps.setState(STATES.USE_SPELL_STATE);
					while (moved) {
//					while (targetTile == lastTargetTile || lastTargetTile.getElement() == Tile.BLANK || (lastTargetTile.getElement() == Tile.BLANK && lastTargetTile.containsCell(currPlayer))) {
						moved = false;
						playergui.setTextPane("Select a blank tile with friendly cells.\n");
						gps.setNewInput(false);
						while (!gps.getNewInput()) {
							sleep(5);
						}
						targetTile = gps.getSpellTargetTile();
						if (targetTile.getElement() != Tile.BLANK || (targetTile.getElement() == Tile.BLANK && !targetTile.containsCell(currPlayer))) {
							if (first) {
								gps.setState(returnState);
								failureToPlay("Did not click a blank tile.\n");
								failed = true;
							}
							else {
								break; //gets out of the while loop.
							}
						}
						else {
							setCheckSuccess(true);
							lastTargetTile = targetTile; //lastTargetTile is the blank tile.
							playergui.setTextPane("Select an element tile in the same quadrant.\n");
							int[] bounds = getQuadrantBounds(lastTargetTile);
							gps.setNewInput(false);
							while (!gps.getNewInput()) {
								sleep(5);
							}
							targetTile = gps.getSpellTargetTile();
							if (targetTile.getElement() >= Tile.BLANK || (targetTile.getNumCells() == Tile.BOARD_MAX_CELLS) 
									|| (targetTile.getLowAngle() < bounds[0]) || (targetTile.getHighAngle() > bounds[1])) {
								playergui.setTextPane("Invalid tile selected.\n");
								moved = true;
							}
							else {
								//swap cell locations.
								lastTargetTile.removeCell(currPlayer, true);
								targetTile.addCell(currPlayer, gps);
								moved = true;
							}
						}
						if (first) {
							first = false;
						}
					}
					gps.setState(returnState);
					setActive(false);
					if (!failed) 
						playergui.setTextPane("Successfully moved cells.\n");
				}
				return;
			} catch (InterruptedException e) {}
		}
	}
}
