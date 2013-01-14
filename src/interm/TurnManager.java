package interm;

import game.Player;

public class TurnManager {
	
	Player[] players;
	int currentPlayer = -1;
	
	public TurnManager(Player[] players) {
		this.players = players;
	}
	
	public Player nextPlayer() {
		
		if (currentPlayer == players.length-1) {
			currentPlayer = -1;
		}
//		currentPlayer += 1;
		currentPlayer = 0;
		return players[currentPlayer];
	}

}
