package SpellCards;

import game.DataLayer;
import game.RenderObject;
import game.Tile;
import main.GamePlayState;

public class BeaconOfHaste extends ActivateSpell {
	
	DataLayer dlayer;
	GamePlayState gps;
	
	public BeaconOfHaste(DataLayer dlayer, GamePlayState gps) {
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
			boolean first = true;
			Tile center = dlayer.getRingList().get(0).get(0);
			long startTime = System.currentTimeMillis();
			try {
				while(active){
					if (first) {
						writeToAllPlayers("Beacon of Haste replaced middle beacon! Passing the middle will add two steps to your roll.\n");
						storeBeaconObject(RenderObject.BEACONOFHASTE, center, gps.getBeaconOfHaste());
						first = false;
					}
					
					center.setAlterRoll(1); //increments the roll by 1, so that 2 extra steps can happen.
					
					if (System.currentTimeMillis() - startTime >= 1000) {
						//set inactive when new beacon replaces it.
						if (center.getBeaconObj().getType() != RenderObject.BEACONOFHASTE) {
							removeRenderObject(RenderObject.BEACONOFHASTE, center);
							writeToAllPlayers("Beacon of Haste has been replaced.");
							center.setAlterRoll(-1); //reset back to original
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
