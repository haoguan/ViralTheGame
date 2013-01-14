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
		init thread = new init(dlayer, gps);
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			resetDefaultState();
			
			try {
				while(active){
					playergui.setTextPane("Select a tile to infiltrate.\n");
					gps.setTargetTile(null);
					gps.setState(STATES.USE_SPELL_STATE);
					while (gps.getTargetTile() == null) {
						sleep(5);
					}
					targetTile = gps.getTargetTile();
					
					if (!targetTile.hasCells()) {
						gps.setState(returnState);
						failureToPlay("No cells to infiltrate on targeted tile.\n");
					}
					else {
						String message = ("Player " + playergui.getPlayer().getColor() + " has selected (" + 
								targetTile.getRingNum() + ", " + targetTile.getTileID() + ").\n");
						writeToAllPlayers(message);
						
						int cellsOnTile = targetTile.getNumCells();
						for(Player player : gps.getPlayers()) {
							targetTile.removeAllCells(player);
						}
						
						Player currentPlayer = playergui.getPlayer();
						for (int i = 0; i < cellsOnTile; i++) 
							gps.addCell(currentPlayer, targetTile, gps);
						gps.setState(returnState);
						setActive(false);
					}
				}	
			} catch (InterruptedException e) {}
			return; 
		}
	}
}
