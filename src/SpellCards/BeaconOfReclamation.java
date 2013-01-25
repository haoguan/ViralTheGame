package SpellCards;

import game.DataLayer;
import game.Player;
import game.RenderObject;
import game.Tile;
import java.util.ArrayList;
import java.util.Iterator;
import main.GamePlayState;
import main.GamePlayState.STATES;

public class BeaconOfReclamation extends ActivateSpell {
	
	DataLayer dlayer;
	GamePlayState gps;
	
	public BeaconOfReclamation(DataLayer dlayer, GamePlayState gps) {
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
			boolean removed = false;
			Tile targetTile = null;
			Player currPlayer = gps.getCurrentPlayer(); //must use gps because playergui is only attached to one player.
			Tile center = dlayer.getRingList().get(0).get(0);
			long startTime = System.currentTimeMillis();
			try {
				while(active){
					if (first) {
						writeToAllPlayers("Beacon of Reclamation replaced middle beacon!\n");
						storeBeaconObject(RenderObject.BEACONOFRECLAMATION, center, gps.getBeaconOfReclamation());
						first = false;
					}

					if (gps.getCurrentState() == STATES.PRE_MOVEMENT_STATE) {
						removed = false;
					}
					
					if(currPlayer.getCurrentTile() == center && gps.getSpellTargetTile() == center && gps.getCurrentState() != STATES.PRE_MOVEMENT_STATE && !removed) {
						STATES returnState = gps.getCurrentState();
						gps.setState(STATES.USE_SPELL_STATE);
						currPlayer.getPlayerGui().setTextPane("The beacon's magic allows you to remove a cell! Select a tile.\n");
						while (!removed) {
							gps.setNewInput(false);
							while (!gps.getNewInput()) {
								sleep(5);
							}
							targetTile = gps.getSpellTargetTile();
							
							if (!targetTile.containsCell(currPlayer)) {
								currPlayer.getPlayerGui().setTextPane("Please select a valid tile.\n");
							}
							else {
								targetTile.removeCell(currPlayer, true);
								currPlayer.getPlayerGui().setTextPane("Successfully removed cell.\n");
								gps.setState(returnState);
								removed = true;
							}
						}
					}
					else {
						sleep(5);
					}
					
					if (System.currentTimeMillis() - startTime >= 1000) {
						//set inactive when new beacon replaces it.
						if (center.getBeaconObj().getType() != RenderObject.BEACONOFRECLAMATION) {
							removeRenderObject(RenderObject.BEACONOFRECLAMATION, center);
							writeToAllPlayers("Beacon of Reclamation has been replaced.");
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
