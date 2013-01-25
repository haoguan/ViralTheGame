package SpellCards;

import game.DataLayer;
import game.Player;
import game.RenderObject;
import game.Tile;

import java.util.ArrayList;
import java.util.Iterator;

import main.GamePlayState;
import main.GamePlayState.STATES;

public class BeaconOfLight extends ActivateSpell {
	
	DataLayer dlayer;
	GamePlayState gps;
	
	public BeaconOfLight(DataLayer dlayer, GamePlayState gps) {
		this.dlayer = dlayer;
		this.gps = gps;
		setImmediateActivate(true);
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
//			STATES returnState = gps.getCurrentState();
			boolean first = true;
			boolean notPrinted = true;
			Tile center = dlayer.getRingList().get(0).get(0);
			Player[] players = gps.getPlayers();
			ArrayList<Player> immunePlayers = new ArrayList<Player>();
			long startTime = System.currentTimeMillis();
			try {
				while(active){
					if (first) {
						writeToAllPlayers("Beacon of Light replaced middle beacon!\n");
						storeBeaconObject(RenderObject.BEACONOFLIGHT, center, gps.getBeaconOfLight());
						first = false;
					}
					
//					if (gps.getCurrentState() == STATES.CHANGE_PLAYER_STATE) {
//						notPrinted = true;
//					}
//					
//					if (gps.getCurrentState() == STATES.PRE_MOVEMENT_STATE && notPrinted) {
//						for (Player player : gps.getPlayers()) {
//							if (player.canTarget()) {
//								System.out.println("Player " + player.getColor() + " can be targetted.");
//							}
//						}
//						notPrinted = false;
//					}
					
					if (System.currentTimeMillis() - startTime >= 1000) {
						//add players in the center to immune list.
						for (int i = 0; i < players.length; i++) {
							if (players[i].getCurrentTile() == center) {
								players[i].setCanTarget(false);
								immunePlayers.add(players[i]);
							}
						}
						
						//remove players from immune list.
						for (Iterator<Player> iter = immunePlayers.iterator(); iter.hasNext(); ) {
							Player currPlayer = iter.next();
							if (currPlayer.getCurrentTile() != center) {
								currPlayer.setCanTarget(true);
								iter.remove();
							}
						}
						
						//set inactive when new beacon replaces it.
						if (center.getBeaconObj().getType() != RenderObject.BEACONOFLIGHT) {
							removeRenderObject(RenderObject.BEACONOFLIGHT, center);
							writeToAllPlayers("Beacon of Light has been replaced.");
							setActive(false);
						}
						
						startTime = System.currentTimeMillis();
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
