package SpellCards;

import game.AdjacentTile;
import game.DataLayer;
import game.Player;
import game.Tile;
import interm.SpellActivateManager;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import main.GamePlayState;
import main.GamePlayState.STATES;

public class ChainLightning extends ActivateSpell{

	DataLayer dlayer;
	GamePlayState gps;
	
	public ChainLightning(DataLayer dlayer, GamePlayState gps) {
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
			ArrayList<Tile> targetedTiles = new ArrayList<Tile>();
			Tile targetTile;
			Tile lastTargetTile;
			resetDefaultState();
			try {
				while(active){
					playergui.setTextPane("Please select a tile for the source point of the lightning.\n");
					gps.setTargetTile(null);
					gps.setState(STATES.USE_SPELL_STATE);
					while (gps.getTargetTile() == null) {
						sleep(5);
					}
					targetTile = gps.getTargetTile();
					//conditions for eligibility
					if (!targetTile.hasCells()) {
						gps.setTargetTile(null);
						gps.setState(returnState);
						failureToPlay("No cells located on targeted tile.\n");
					}
					else {
						lastTargetTile = targetTile; //make them equal.
						String message = ("Player " + playergui.getPlayer().getColor() + " has selected (" + 
								targetTile.getRingNum() + ", " + targetTile.getTileID() + ").\n");
						writeToAllPlayers(message);
						
						//check targetTile validity.
						while ((targetTile == lastTargetTile || lastTargetTile.getAvailableAdjTiles().contains(new AdjacentTile(targetTile, true))
								|| lastTargetTile.getUnAvailableAdjTiles().contains(new AdjacentTile(targetTile, false))) && targetTile.hasCells()) {
							targetedTiles.add(targetTile);
							lastTargetTile = targetedTiles.get(targetedTiles.size() - 1);
							gps.setNewInput(false);
							
							playergui.setTextPane("Please click an adjacent tile with cells.\n");
							while (!gps.getNewInput()) {
								sleep(5);
							}
							targetTile = gps.getTargetTile();
						}
						writeToAllPlayers("Lightning has destroyed all cells in path tiles.\n");
						//player has clicked invalid tile to end spell.
						for (Tile tile : targetedTiles) {
							tile.removeAllCells(tile, gps);
						}
						gps.setState(returnState);
						setActive(false);
					}
				}
			} catch (InterruptedException e) {}
			setActive(true); //resetting the active boolean.
			return;
		}
	}
}
