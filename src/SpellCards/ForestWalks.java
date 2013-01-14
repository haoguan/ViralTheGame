package SpellCards;

import game.DataLayer;
import game.Player;
import game.Tile;

import java.util.ArrayList;

import main.GamePlayState;
import main.GamePlayState.STATES;

public class ForestWalks extends ActivateSpell {

	DataLayer dlayer;
	GamePlayState gps;
	
	public ForestWalks(DataLayer dlayer, GamePlayState gps) {
		this.dlayer = dlayer;
		this.gps = gps;
	}
	
	@Override
	public boolean runEffect() {
		Player affPlayer = gps.getCurrentPlayer();
		init thread = new init(affPlayer);
		thread.start();
		return isSuccessRun();  //no possible way for exception, so just return true.
	}
	
	class init extends Thread{
		
		Player affectedPlayer;
		
		public init(Player player) {
			affectedPlayer = player;
		}
		
		public void run() {
			resetDefaultState();
			while(active){
				try {
					if(gps.getCurrentPlayer() == affectedPlayer && gps.getCurrentState() == STATES.PRE_MOVEMENT_STATE){
						int sizeOfRing = dlayer.getRingList().get(gps.getCurrentPlayer().getCurrentTile().getRingNum()).size();
						int roll = gps.getCurrentPlayer().getRoll();
						if (sizeOfRing < roll) {
							dlayer.getPossibleMoves().clear();
						}
						else { //only if roll is smaller than size of ring.
							int currTileID = gps.getCurrentPlayer().getCurrentTile().getTileID();
							int ccwNum = currTileID + roll;
							if (ccwNum > sizeOfRing - 1) {
								ccwNum -= sizeOfRing;
							}
							int cwNum = currTileID - roll;
							if (cwNum < 0) {
								cwNum += sizeOfRing;
							}
							
							Tile move1 = dlayer.getRingList().get(gps.getCurrentPlayer().getCurrentTile().getRingNum()).get(ccwNum);
							Tile move2 = dlayer.getRingList().get(gps.getCurrentPlayer().getCurrentTile().getRingNum()).get(cwNum);
							ArrayList<Tile> newMoves = new ArrayList<Tile>();
							newMoves.add(move1);
							newMoves.add(move2);
							
							dlayer.getPossibleMoves().addAll(newMoves);
							dlayer.getPossibleMoves().retainAll(newMoves);
							
							//must find the paths
							ArrayList<Tile> pathsMove1 = new ArrayList<Tile>();
							ArrayList<Tile> pathsMove2 = new ArrayList<Tile>();
							//find path for ccWNum
							for (int i = 0; i <= roll; i++) {
								int idx = currTileID + i;
								if (idx > sizeOfRing - 1) {
									idx -= sizeOfRing;
								}
								Tile pathTile = dlayer.getRingList().get(gps.getCurrentPlayer().getCurrentTile().getRingNum()).get(idx);
								pathsMove1.add(pathTile);
							}
							//find path for cwNum
							for (int i = 0; i <= roll; i++) {
								int idx = currTileID - i;
								if (idx < 0) {
									idx += sizeOfRing;
								}
								Tile pathTile = dlayer.getRingList().get(gps.getCurrentPlayer().getCurrentTile().getRingNum()).get(idx);
								pathsMove2.add(pathTile);
							}
							
							dlayer.getPossibleMovesPaths().clear(); //removes previous paths.
							ArrayList<ArrayList<Tile>> allPathsMove1 = new ArrayList<ArrayList<Tile>>();
							ArrayList<ArrayList<Tile>> allPathsMove2 = new ArrayList<ArrayList<Tile>>();
							allPathsMove1.add(pathsMove1);
							allPathsMove2.add(pathsMove2);
							dlayer.getPossibleMovesPaths().put(move1, allPathsMove1);
							dlayer.getPossibleMovesPaths().put(move2, allPathsMove2);
						}
					}else if (gps.getCurrentPlayer() == affectedPlayer && gps.getCurrentPlayer().getRoll() == 3){
						String expire = "Forest Walk Has Expired.\n";
						writeToAllPlayers(expire);
						setActive(false);
					}
					else {	
						sleep(5);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return;
		}
	}

}
