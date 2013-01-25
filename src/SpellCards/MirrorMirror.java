package SpellCards;

import game.DataLayer;
import game.Player;
import game.Tile;
import main.GamePlayState;
import main.GamePlayState.STATES;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class MirrorMirror extends ActivateSpell{

	DataLayer dlayer;
	GamePlayState gps;
	Sound fx;
	
	public MirrorMirror(DataLayer dlayer, GamePlayState gps) {
		this.dlayer = dlayer;
		this.gps = gps;
		try {
			fx = new Sound("res/GamePlayState/Sounds/mirror.wav");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		setTargetRequired(true);
	}

	@Override
	public boolean runEffect() {
		resetDefaultState();
		init thread = new init(dlayer, gps);
		thread.start();
		return isSuccessRun(); //never fails.
	}
	
	class init extends Thread{
		
		DataLayer dlayer;
		GamePlayState gps;
		
		public init(DataLayer dlayer, GamePlayState gps) {
			this.dlayer = dlayer;
			this.gps = gps;
		}
		
		public void run(){
			while(active){
//				try {
//					if (playergui.getTargetPlayer() == null) {
//						sleep(0);
//					}
//					else {
						fx.play();
						Player targetPlayer = playergui.getTargetPlayer();
						int tilesInRing = dlayer.getRingList().get(targetPlayer.getCurrentTile().getRingNum()).size();
						int halfTiles = tilesInRing/2;
						int destTileNum = targetPlayer.getCurrentTile().getTileID() + halfTiles;
						if (destTileNum >= tilesInRing) {
							destTileNum -= tilesInRing;
						}
						Tile destTile = dlayer.getRingList().get(targetPlayer.getCurrentTile().getRingNum()).get(destTileNum);
						//move the player to new tile.
						targetPlayer.getCurrentTile().leavePlayer(targetPlayer);
						targetPlayer.setCurrentTile(destTile);
						targetPlayer.setCurrentPosition(targetPlayer.getCurrentTile().getPlayerPosition(targetPlayer));
						targetPlayer.setPlayerCoords(gps.getTileFinder().angleToPixels(targetPlayer.getCurrentPosition().getPositionRadius(), targetPlayer.getCurrentPosition().getAngle()));
						targetPlayer.getPlayerGui().setTextPane("You got flipped!\n");
						resetTargetList();
						setActive(false);
						resetRolled();
//					}
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
			}
			return;
		}
	}
}
