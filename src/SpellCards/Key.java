package SpellCards;

import game.DataLayer;
import game.Tile;

import java.util.Iterator;

import main.GamePlayState;
import main.GamePlayState.STATES;

public class Key extends ActivateSpell{

	DataLayer dlayer;
	GamePlayState gps;
	
	public Key(DataLayer dlayer, GamePlayState gps) {
		this.dlayer = dlayer;
		this.gps = gps;
	}
	
	@Override
	public boolean runEffect() {
		resetDefaultState();
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
			boolean changed = false;
			while(active){
				try {
					if (gps.getCurrentState() == STATES.CHANGE_PLAYER_STATE) {
						for (Iterator<Tile> iter = dlayer.getRingList().get(7).iterator(); iter.hasNext(); )
							iter.next().setLocked(true);
						writeToAllPlayers("Checkpoints are locked.\n");
						setActive(false);
					}
					else if (!changed) {
						for (Iterator<Tile> iter = dlayer.getRingList().get(7).iterator(); iter.hasNext(); )
							iter.next().setLocked(false);
						writeToAllPlayers("Checkpoints are unlocked for one turn.\n");
						changed = true;
					}
					else {
						sleep(5);
					}
				} catch (InterruptedException e) {}
			}
			setActive(true); //resetting the active boolean.
			return;
		}
	}
	

}
