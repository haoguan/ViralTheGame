package SpellCards;

import java.util.ArrayList;
import java.util.Iterator;

import game.DataLayer;
import game.Player;
import game.Tile;
import main.GamePlayState;
import main.GamePlayState.STATES;

public class TheGreatRingWall extends ActivateSpell{

	DataLayer dlayer;
	GamePlayState gps;
	
	public TheGreatRingWall(DataLayer dlayer, GamePlayState gps) {
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
			boolean first = true;
			boolean turnLogged = false; //variable to allow numTurnsLeft to decrement.
			STATES returnState = gps.getCurrentState();
			Tile targetTile;
			int targetRing = 0;
			try {
				while(active){
					if (first) {
						playergui.setTextPane("Please select a tile located within target ring.\n");
						gps.setSpellTargetTile(null);
						gps.setState(STATES.USE_SPELL_STATE);
						gps.setTrackMouse(true);
						while (gps.getSpellTargetTile() == null) {
							sleep(5);
						}
						targetTile = gps.getSpellTargetTile();
						targetRing = targetTile.getRingNum();
						String message = ("Player " + playergui.getPlayer().getColor() + " has selected ring " + targetTile.getRingNum() + ".\n");
						writeToAllPlayers(message);
						gps.setTrackMouse(false);
						gps.setState(returnState);
						first = false;
					}
	
					if (gps.getCurrentState() == STATES.CHANGE_PLAYER_STATE)
						turnLogged = false;
					
					if (gps.getCurrentState() == STATES.PRE_MOVEMENT_STATE && !turnLogged) {
						//Literally allows them to do forest walk movement.
						int playerRing = gps.getCurrentPlayer().getCurrentTile().getRingNum();
						if (playerRing == targetRing) {
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
								
								dlayer.getPossibleMovesPaths().clear();
								ArrayList<ArrayList<Tile>> allPathsMove1 = new ArrayList<ArrayList<Tile>>();
								ArrayList<ArrayList<Tile>> allPathsMove2 = new ArrayList<ArrayList<Tile>>();
								allPathsMove1.add(pathsMove1);
								allPathsMove2.add(pathsMove2);
								dlayer.getPossibleMovesPaths().put(move1, allPathsMove1);
								dlayer.getPossibleMovesPaths().put(move2, allPathsMove2);
							}
						}
						else if (playerRing < targetRing) {
							for (Iterator<Tile> move = dlayer.getPossibleMoves().iterator(); move.hasNext(); ) {
								if (move.next().getRingNum() >= targetRing) {
									move.remove();
								}
							}
						}
						else {
							for (Iterator<Tile> move = dlayer.getPossibleMoves().iterator(); move.hasNext(); ) {
								if (move.next().getRingNum() <= targetRing) {
									move.remove();
								}
							}
						}
						
						numTurnsLeft--;
						if (numTurnsLeft == 0) {
							String expire = "The Great Ring Wall expires after this turn's movement phase.\n";
							writeToAllPlayers(expire);
							setActive(false);
						}
						turnLogged = true;
					}
					else {
						sleep(5);
					}	
				}
			} catch (InterruptedException e) {}
			return;
		}
	}
}
