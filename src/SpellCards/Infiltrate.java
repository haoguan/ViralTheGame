package SpellCards;

import game.Cell;
import game.DataLayer;
import game.Player;
import game.Tile;
import main.GamePlayState;
import main.GamePlayState.STATES;

public class Infiltrate extends ActivateSpell{
	
	DataLayer dlayer;
	GamePlayState gps;
	
	public Infiltrate(DataLayer dlayer, GamePlayState gps) {
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
			try {
				while(active){
					playergui.setTextPane("Select a tile to infiltrate.\n");
					gps.setSpellTargetTile(null);
					gps.setState(STATES.USE_SPELL_STATE);
					while (gps.getSpellTargetTile() == null) {
						sleep(5);
					}
					targetTile = gps.getSpellTargetTile();
					
					if (!targetTile.hasCells()) {
						gps.setState(returnState);
						failureToPlay("No cells to infiltrate on targeted tile.\n");
					}
					else {
						setCheckSuccess(true);
						int cellsOnTile = targetTile.getNumCells();
						for(Player player : gps.getPlayers()) {
							targetTile.removeAllCells(player);
						}
						
						Player currentPlayer = playergui.getPlayer();
						for (int i = 0; i < cellsOnTile; i++) 
							targetTile.addCell(currentPlayer, gps);
						gps.setState(returnState);
						playergui.setTextPane("Successfully infiltrated target tile.\n");
						setActive(false);
					}
				}	
			} catch (InterruptedException e) {}
			return; 
		}
	}
}
