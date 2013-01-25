package SpellCards;

import game.DataLayer;
import game.Tile;
import java.util.ArrayList;
import java.util.Iterator;

import main.GamePlayState;

public class SlipperySlope extends ActivateSpell{

	DataLayer dlayer;
	GamePlayState gps;
	
	public SlipperySlope(DataLayer dlayer, GamePlayState gps) {
		this.dlayer = dlayer;
		this.gps = gps;
	}

	public boolean runEffect() {
		resetDefaultState();
		for (Iterator<Tile> move = dlayer.getPossibleMoves().iterator(); move.hasNext(); ) {
			if (move.next().getRingNum() > gps.getCurrentPlayer().getCurrentTile().getRingNum()) {
				move.remove();
			}
		}
		resetRolled();
		return isSuccessRun();
	}	
	
	
}
