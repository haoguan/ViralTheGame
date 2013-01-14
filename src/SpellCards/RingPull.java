package SpellCards;

import game.DataLayer;
import game.Player;
import game.Tile;

import java.util.ArrayList;

import main.GamePlayState;
import main.GamePlayState.STATES;

public class RingPull extends ActivateSpell{

	DataLayer dlayer;
	GamePlayState gps;
	
	public RingPull(DataLayer dlayer, GamePlayState gps) {
		this.dlayer = dlayer;
		this.gps = gps;
		setTargetRequired(true);
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
			resetDefaultState();
			STATES returnState = gps.getCurrentState();
			while(active){
				if (playergui.getTargetPlayer().getCurrentTile().getRingNum() == 0) {
					failureToPlay("Target player has no lower ring!\n");
				}
				else {
					gps.setState(STATES.USE_SPELL_STATE);
					Player targetPlayer = playergui.getTargetPlayer();
					ArrayList<Tile> lowerAdjs = targetPlayer.getCurrentTile().findLowerAdjTiles(targetPlayer.getCurrentTile());
					Tile destTile = lowerAdjs.get(0); //get the lower of the two possible lowerAdjs.
					//move the player to new tile.
					targetPlayer.getCurrentTile().leavePlayer(targetPlayer);
					targetPlayer.setCurrentTile(destTile);
					targetPlayer.setCurrentPosition(targetPlayer.getCurrentTile().getPlayerPosition(targetPlayer));
					targetPlayer.setPlayerCoords(gps.getTileFinder().angleToPixels(targetPlayer.getCurrentPosition().getPositionRadius(), targetPlayer.getCurrentPosition().getAngle()));
					targetPlayer.getPlayerGui().setTextPane("You got pulled!\n");
					resetTargetList();
					setActive(false);
					resetRolled();
					gps.setState(returnState);
				}
				setActive(true); //resetting the active boolean.
				return;
			}
		}
	}
}
	
