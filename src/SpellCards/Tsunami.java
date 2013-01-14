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
		init thread = new init(dlayer, gps);
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			resetDefaultState();
			try {
				while(active){
					playergui.setTextPane("Please select a tile (except checkpoint tiles) to bomb.\n");
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
				setActive(true); //resetting the active boolean.
				return;
			} catch (InterruptedException e) {}
		}
	}

}
