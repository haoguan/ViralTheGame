package SpellCards;

import game.AdjacentTile;
import game.DataLayer;
import game.Player;
import game.Tile;

import java.util.Iterator;

import main.GamePlayState;
import main.GamePlayState.STATES;

public class DeathDice extends ActivateSpell{

	DataLayer dlayer;
	GamePlayState gps;
	
	public DeathDice(DataLayer dlayer, GamePlayState gps) {
		this.dlayer = dlayer;
		this.gps = gps;
	}
	
	@Override
	public boolean runEffect() {
		init thread = new init(dlayer, gps);
		thread.start();
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
			resetDefaultState();
			try {
				while(active){
					playergui.setTextPane("Please roll the dice.\n");
					playergui.setRolled(false);
					playergui.setRollDice(true);
					gps.setState(STATES.ROLL_STATE);
					while (!playergui.getRolled()) {
						sleep(5);
					}
					int roll = playergui.getPlayer().getRoll();
					
					for (int i = 0; i < dlayer.getRingList().size(); i++) {
						for (Iterator<Tile> tile = dlayer.getRingList().get(i).iterator(); tile.hasNext(); ) {
							Tile next = tile.next();
							if (next.getNumCells() == roll) {
								for (Player player : gps.getPlayers()) {
									next.removeAllCells(player);
								}
							}
						}
					}
					writeToAllPlayers("All tiles with " + roll + "cell(s) will be cleared.\n");
					gps.setState(returnState);
					setActive(false);
				}
				setActive(true); //resetting the active boolean.
				return;
			} catch (InterruptedException e) {}
		}
	}

}