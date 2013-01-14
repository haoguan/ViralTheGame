package SpellCards;

import game.DataLayer;
import game.Player;
import game.Tile;
import main.GamePlayState;

public class GravitationalPull extends ActivateSpell{

	DataLayer dlayer;
	GamePlayState gps;
	
	public GravitationalPull(DataLayer dlayer, GamePlayState gps) {
		this.dlayer = dlayer;
		this.gps = gps;
	}
	
	@Override
	public boolean runEffect() {
		resetDefaultState();
		for (int i = 0; i < gps.getPlayers().length; i++) {
			Player currPlayer = gps.getPlayers()[i];
			Tile center = dlayer.getRingList().get(0).get(0);
			currPlayer.getCurrentTile().leavePlayer(currPlayer);
			currPlayer.setCurrentTile(center);
			currPlayer.setCurrentPosition(currPlayer.getCurrentTile().getPlayerPosition(currPlayer));
			currPlayer.setPlayerCoords(gps.getTileFinder().angleToPixels(currPlayer.getCurrentPosition().getPositionRadius(), currPlayer.getCurrentPosition().getAngle()));
		}
		resetRolled();
		return isSuccessRun();
	}
}
