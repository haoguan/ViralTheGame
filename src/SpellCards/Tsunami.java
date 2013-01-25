package SpellCards;

import game.AdjacentTile;
import game.DataLayer;
import game.Player;
import game.Tile;

import java.util.ArrayList;

import main.GamePlayState;
import main.GamePlayState.STATES;

public class Tsunami extends ActivateSpell{

	DataLayer dlayer;
	GamePlayState gps;
	
	public Tsunami(DataLayer dlayer, GamePlayState gps) {
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
			try {
				while(active){
					playergui.setTextPane("Please select a source tile.\n");
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
						setCheckSuccess(true);
						String message = ("Player " + playergui.getPlayer().getColor() + " has selected (" + 
								targetTile.getRingNum() + ", " + targetTile.getTileID() + ").\n");
						writeToAllPlayers(message);
						
						//remove all cells from adjacent tiles.
						for (AdjacentTile tile : targetTile.getAvailableAdjTiles()) {
							//makes sure adj is not checkpoint tile either.
							if (tile.getTile().getRingNum() != 7) {
								for (Player player : gps.getPlayers()) {
									tile.getTile().removeAllCells(player);
								}
							}
						}
						
						for (AdjacentTile tile : targetTile.getUnAvailableAdjTiles()) {
							for (Player player : gps.getPlayers()) {
								tile.getTile().removeAllCells(player);
							}
						}
						gps.setState(returnState);
						
//						//remove cell from targetTile.
//						for (Player player : gps.getPlayers()) {
//							targetTile.removeAllCells(player);
//						}
						setActive(false);
					}
				}
				return;
			} catch (InterruptedException e) {}
		}
	}

}
