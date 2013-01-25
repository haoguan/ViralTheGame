package interm;

import game.Player;

/**
 * TurnManager is a simple class that handles the rotation
 * of players and returns them to GamePlayState.
 * @author Hao Guan
 */
public class TurnManager {
	
	Player[] players;
	int currentPlayer = -1;
	
	/**
	 * The one parameter constructor requires an array of Players.
	 * @param players is the array of Players in the game.
	 */
	public TurnManager(Player[] players) {
		this.players = players;
	}
	
	/**
	 * nextPlayer() updates the currentPlayer to the next
	 * player in line and returns it.
	 * @return the next player in queue.
	 */
	public Player nextPlayer() {
		
		if (currentPlayer == players.length-1) {
			currentPlayer = -1;
		}
//		currentPlayer += 1;
		currentPlayer = 0;
		return players[currentPlayer];
	}

}
