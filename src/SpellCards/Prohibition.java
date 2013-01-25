package SpellCards;

import java.util.Iterator;

import game.DataLayer;
import game.Player;
import game.Tile;
import main.GamePlayState;
import main.GamePlayState.STATES;

public class Prohibition extends ActivateSpell{

	DataLayer dlayer;
	GamePlayState gps;
	
	public Prohibition(DataLayer dlayer, GamePlayState gps) {
		this.dlayer = dlayer;
		this.gps = gps;
	}

	@Override
	public boolean runEffect() {
		resetDefaultState();
		init thread = new init(dlayer, gps);
		thread.start();
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
			int numTurnsLeft = 4;
			boolean turnLogged = false; //variable to allow numTurnsLeft to decrement.
			int[] quadBounds = {0, 90, 180, 270, 360};
			while(active){
				if (gps.getCurrentState() == STATES.CHANGE_PLAYER_STATE)
					turnLogged = false;
				
				if (gps.getCurrentState() == STATES.PRE_MOVEMENT_STATE && !turnLogged) {
					int lowBound;
					int highBound;
					Player currPlayer = gps.getCurrentPlayer();
					Tile currTile = currPlayer.getCurrentTile();
					double lowAngle = currTile.getLowAngle();
					
					if (lowAngle < 90) {
						lowBound = quadBounds[0];
						highBound = quadBounds[1];
					}
					else if (lowAngle < 180) {
						lowBound = quadBounds[1];
						highBound = quadBounds[2];
					}
					else if (lowAngle < 270) {
						lowBound = quadBounds[2];
						highBound = quadBounds[3];
					}
					else {
						lowBound = quadBounds[3];
						highBound = quadBounds[4];
					}
					
//					System.out.println("Low Bound: " + lowBound);
//					System.out.println("High Bound: " + highBound);
					for (Iterator<Tile> move = dlayer.getPossibleMoves().iterator(); move.hasNext(); ) {
						if (move.next().getLowAngle() < lowBound || move.next().getLowAngle() > highBound) {
							move.remove();
						}
					}
//					ArrayList<Tile> toRemove = new ArrayList<Tile>();
//					for (int i = 0; i < dlayer.getPossibleMoves().size(); i++) {
//						if (dlayer.getPossibleMoves().get(i).getLowAngle() < lowBound || dlayer.getPossibleMoves().get(i).getLowAngle() > highBound) {
//							toRemove.add(dlayer.getPossibleMoves().get(i));
////							dlayer.getPossibleMovesPaths().remove(dlayer.getPossibleMoves().get(i)); //removes the tile in paths hashmap too.
////							dlayer.getPossibleMoves().remove(i);
//						}
//					}
//					dlayer.getPossibleMoves().removeAll(toRemove);
//					for (Tile remove : toRemove) {
//						dlayer.getPossibleMovesPaths().remove(remove);
//					}
					numTurnsLeft--;
					if (numTurnsLeft == 0) {
						String expire = "Prohibition expires after this turn's movement phase.\n";
						writeToAllPlayers(expire);
						setActive(false);
					}
					turnLogged = true;
				}
				else {
					try {
						sleep(5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}	
			}
			return;
		}
	}
}
