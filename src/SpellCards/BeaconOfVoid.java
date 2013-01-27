package SpellCards;

import game.AdjacentTile;
import game.DataLayer;
import game.RenderObject;
import game.Tile;

import main.GamePlayState;

public class BeaconOfVoid extends ActivateSpell {
	
	DataLayer dlayer;
	GamePlayState gps;
	
	public BeaconOfVoid(DataLayer dlayer, GamePlayState gps) {
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
			AdjacentTile centerAdj = new AdjacentTile(center, false);
			try {
				while(active){
					if (first) {
						writeToAllPlayers("Beacon of Void replaced middle beacon! No player may pass through the center.\n");
						storeBeaconObject(RenderObject.BEACONOFVOID, center, gps.getBeaconOfVoid());
						
						//store new unavailables to the tile.
						for (AdjacentTile avail : center.getAvailableAdjTiles()) {
							avail.getTile().getStoredUnavailTiles().add(centerAdj);
						}
						for (AdjacentTile unavail : center.getUnAvailableAdjTiles()) {
							unavail.getTile().getStoredUnavailTiles().add(centerAdj);
						}
						first = false;
					}
					
					if (System.currentTimeMillis() - startTime >= 1000) {
						//set inactive when new beacon replaces it.
						if (center.getBeaconObj().getType() != RenderObject.BEACONOFVOID) {
							removeRenderObject(RenderObject.BEACONOFVOID, center);
							//remove the unavailable status on other tiles.
							for (AdjacentTile avail : center.getAvailableAdjTiles()) {
								avail.getTile().getStoredUnavailTiles().remove(centerAdj);
							}
							for (AdjacentTile unavail : center.getUnAvailableAdjTiles()) {
								unavail.getTile().getStoredUnavailTiles().remove(centerAdj);
							}
							writeToAllPlayers("Beacon of Void has been replaced.");
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
