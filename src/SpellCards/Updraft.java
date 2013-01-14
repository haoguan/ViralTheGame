package SpellCards;

import game.DataLayer;
import game.Tile;
import java.util.ArrayList;
import java.util.Iterator;

import main.GamePlayState;

public class Updraft extends ActivateSpell{

	DataLayer dlayer;
	GamePlayState gps;
	
	public Updraft(DataLayer dlayer, GamePlayState gps) {
		this.dlayer = dlayer;
		this.gps = gps;
	}

	public boolean runEffect() {
		resetDefaultState();
		for (Iterator<Tile> move = dlayer.getPossibleMoves().iterator(); move.hasNext(); ) {
			if (move.next().getRingNum() < gps.getCurrentPlayer().getCurrentTile().getRingNum()) {
				move.remove();
			}
		}
//		ArrayList<Tile> toRemove = new ArrayList<Tile>();
//		for (Tile posMove : dlayer.getPossibleMoves()) {
//			if (posMove.getRingNum() < gps.getCurrentPlayer().getCurrentTile().getRingNum()) {
//				toRemove.add(posMove);
//			}
//		}
//		for (Tile remove : toRemove) {
//			dlayer.getPossibleMoves().remove(remove);
//		}
		resetRolled();
		return isSuccessRun();
	}	
	
	
}
