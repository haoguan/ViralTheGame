package SpellCards;

import game.DataLayer;
import game.Tile;

import java.util.ArrayList;
import java.util.Iterator;

import main.GamePlayState;
import main.GamePlayState.STATES;

public class ElementalDiffusion extends ActivateSpell{

	DataLayer dlayer;
	GamePlayState gps;
	
	public ElementalDiffusion(DataLayer dlayer, GamePlayState gps) {
		this.dlayer = dlayer;
		this.gps = gps;
	}

	@Override
	public boolean runEffect() {
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
			resetDefaultState();
			try {
				while(active){
					if (gps.getCurrentState() == STATES.CHANGE_PLAYER_STATE)
						turnLogged = false;
					
					if (gps.getCurrentState() == STATES.PRE_MOVEMENT_STATE && !turnLogged) {
						for (Iterator<Tile> move = dlayer.getPossibleMoves().iterator(); move.hasNext(); ) {
							Tile curTile = move.next();
							for (Iterator<ArrayList<Tile>> pathForMove = dlayer.getPossibleMovesPaths().get(curTile).iterator(); pathForMove.hasNext(); ) {
								ArrayList<Tile> path = pathForMove.next();
								for (Iterator<Tile> tileInPath = path.iterator(); tileInPath.hasNext(); ) {
									if (tileInPath.next().getElement() >= Tile.FIRE && tileInPath.next().getElement() <= Tile.EARTH) {
										pathForMove.remove();
										if (!pathForMove.hasNext()) {
											move.remove();
										}
										break;
									}
								}
							}
						}
						numTurnsLeft--;
						if (numTurnsLeft == 0) {
							String expire = "Elemental Diffusion expires after this turn's movement phase.\n";
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
			setActive(true); //resetting the active boolean.
			return;
		}
	}
}
