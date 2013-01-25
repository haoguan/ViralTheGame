package SpellCards;

import java.util.ArrayList;

import SpellCards.RingPull.init;
import game.DataLayer;
import game.Player;
import game.Tile;
import main.GamePlayState;

public class RingPush extends ActivateSpell{
	
	DataLayer dlayer;
	GamePlayState gps;
	
	public RingPush(DataLayer dlayer, GamePlayState gps) {
		this.dlayer = dlayer;
		this.gps = gps;
		setTargetRequired(true);
	}


	public boolean runEffect() {
		resetDefaultState();
		init thread = new init(dlayer, gps);
		thread.start();
		//this while loop allows for thread to not be finished and still return correct result earlier.
		while (!checkSuccess) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {}
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
			while(active){
//				try {
//					if (playergui.getTargetPlayer() == null) {
//						sleep(0);
//					}
					if (playergui.getTargetPlayer().getCurrentTile().findUpperAdjTiles(playergui.getTargetPlayer().getCurrentTile()).size() == 0 &&
							playergui.getTargetPlayer().getCurrentTile().findUpperAdjCenter().size() == 0) {
						failureToPlay("Target player has no upper ring!\n");
					}
					else {
						setCheckSuccess(true);
						Player targetPlayer = playergui.getTargetPlayer();
						ArrayList<Tile> upperAdjs;
						if (targetPlayer.getCurrentTile().getRingNum() == 0) {
							upperAdjs = targetPlayer.getCurrentTile().findUpperAdjCenter();
						}
						else {
							upperAdjs = targetPlayer.getCurrentTile().findUpperAdjTiles(targetPlayer.getCurrentTile());
						}
						Tile destTile = upperAdjs.get(0); //get the lower of the two possible upperAdjs.
						//move the player to new tile.
						targetPlayer.getCurrentTile().leavePlayer(targetPlayer);
						targetPlayer.setCurrentTile(destTile);
						targetPlayer.setCurrentPosition(targetPlayer.getCurrentTile().getPlayerPosition(targetPlayer));
						targetPlayer.setPlayerCoords(gps.getTileFinder().angleToPixels(targetPlayer.getCurrentPosition().getPositionRadius(), targetPlayer.getCurrentPosition().getAngle()));
						targetPlayer.getPlayerGui().setTextPane("You got pushed!\n");
						resetTargetList();
						setActive(false);
						resetRolled();
					}
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
			}
			return;
		}
	}
}
